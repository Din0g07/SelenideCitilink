package ru.dudareva.allure.selenide;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.SelenideLog;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import io.qameta.allure.util.ResultsUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Класс для кастомизации интеграции Selenide с Allure.
 * Добавляет дополнительные возможности, такие как сохранение скриншотов и HTML источников страницы, а также логов браузера.
 * @version 1.0
 * @autor Дударева Диана
 */
public class CustomAllureSelenide extends AllureSelenide {

    /**
    * Логгер для записи сообщений и ошибок.
    */
    private static final Logger LOGGER = LoggerFactory.getLogger(AllureSelenide.class);

    /**
     * Флаг, указывающий, следует ли сохранять скриншоты.
     * Установлен в {@code true} по умолчанию.
     */
    private boolean saveScreenshots;

    /**
     * Флаг, указывающий, следует ли сохранять HTML источник страницы.
     * Установлен в {@code true} по умолчанию.
     */
    private boolean savePageHtml;

    /**
     * Флаг, указывающий, следует ли включать шаги с локаторами Selenide в отчеты.
     * Установлен в {@code true} по умолчанию.
     */
    private boolean includeSelenideLocatorsSteps;

    /**
     * Карта, сопоставляющая типы логов с уровнями логирования.
     * Используется для определения, какие логи браузера сохранять.
     */
    private final Map<LogType, Level> logTypesToSave;

    /**
     * Объект жизненного цикла Allure, управляющий добавлением вложений и обновлением статусов тестов.
     */
    private final AllureLifecycle lifecycle;

    /**
     * Конструктор по умолчанию. Использует стандартный жизненный цикл Allure.
     */
    public CustomAllureSelenide() {
        this(Allure.getLifecycle());
    }

    /**
     * Конструктор, принимающий объект жизненного цикла Allure.
     * @param lifecycle Объект {@link AllureLifecycle}
     */
    public CustomAllureSelenide(AllureLifecycle lifecycle) {
        this.saveScreenshots = true;
        this.savePageHtml = true;
        this.includeSelenideLocatorsSteps = true;
        this.logTypesToSave = new HashMap();
        this.lifecycle = lifecycle;
    }

    /**
     * Метод для получения байтов скриншота.
     * @return Optional с байтами скриншота, если драйвер активен.
     */
    private static Optional<byte[]> getScreenshotBytes() {
        try {
            return WebDriverRunner.hasWebDriverStarted() ? Optional.of(((TakesScreenshot)WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES)) : Optional.empty();
        } catch (WebDriverException var1) {
            LOGGER.warn("Скриншот не получилось получить", var1);
            return Optional.empty();
        }
    }

    /**
     * Метод для получения байтов HTML источника страницы.
     * @return Optional с байтами HTML источника страницы, если драйвер активен.
     */
    private static Optional<byte[]> getPageSourceBytes() {
        try {
            return WebDriverRunner.hasWebDriverStarted() ? Optional.of(WebDriverRunner.getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8)) : Optional.empty();
        } catch (WebDriverException var1) {
            LOGGER.warn("Источник страницы не получилось получить", var1);
            return Optional.empty();
        }
    }

    /**
     * Метод для получения логов браузера.
     * @param logType Тип логов.
     * @param level Уровень логов.
     * @return Логи браузера в виде строки.
     */
    private static String getBrowserLogs(LogType logType, Level level) {
        return String.join("\n\n", Selenide.getWebDriverLogs(logType.toString(), level));
    }

    /**
     * Метод, вызываемый после события логирования.
     * @param event Событие логирования.
     */
    @Override
    public void afterEvent(LogEvent event) {
        this.lifecycle.getCurrentTestCaseOrStep().ifPresent((parentUuid) -> {
            if (this.saveScreenshots) {
                getScreenshotBytes().ifPresent((bytes) -> {
                    this.lifecycle.addAttachment("Скриншот", "image/png", "png", bytes);
                });
            }

            if (this.savePageHtml) {
                getPageSourceBytes().ifPresent((bytes) -> {
                    this.lifecycle.addAttachment("Источник страницы", "text/html", "html", bytes);
                });
            }

            if (!this.logTypesToSave.isEmpty()) {
                this.logTypesToSave.forEach((logType, level) -> {
                    byte[] content = getBrowserLogs(logType, level).getBytes(StandardCharsets.UTF_8);
                    this.lifecycle.addAttachment("Лог : " + logType, "application/json", ".txt", content);
                });
            }

        });


        if (this.stepsShouldBeLogged(event)) {
            this.lifecycle.getCurrentTestCaseOrStep().ifPresent((parentUuid) -> {
                switch(event.getStatus()) {
                    case PASS:
                        this.lifecycle.updateStep((step) -> {
                            step.setStatus(Status.PASSED);
                        });
                        break;
                    case FAIL:
                        this.lifecycle.updateStep((stepResult) -> {
                            stepResult.setStatus((Status) ResultsUtils.getStatus(event.getError()).orElse(Status.BROKEN));
                            stepResult.setStatusDetails((StatusDetails)ResultsUtils.getStatusDetails(event.getError()).orElse(new StatusDetails()));
                        });
                        break;
                    default:
                        LOGGER.warn("Шаг закончился с неверным статусом {}", event.getStatus());
                }

                this.lifecycle.stopStep();
            });
        }

    }

    /**
     * Проверяет, следует ли логировать шаги.
     * @param event Событие логирования.
     * @return true, если шаги следует логировать.
     */
    private boolean stepsShouldBeLogged(LogEvent event) {
        return this.includeSelenideLocatorsSteps || !(event instanceof SelenideLog);
    }
}
