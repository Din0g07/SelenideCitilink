package ru.dudareva.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.dudareva.allure.selenide.CustomAllureSelenide;

/**
 * Базовый класс для тестов, содержащий настройки инициализации.
 * Предоставляет методы для настройки логгера Allure и конфигурации браузера перед запуском тестов.
 * @version 1.0
 * @autр рor Дударева Диана
 */
public class BaseTests {

    /**
     * Метод, выполняемый один раз перед всеми тестами.
     * Настраивает логгер Allure для добавления скриншотов и исходной страницы.
     */
    @BeforeAll
    public static void setup(){
        SelenideLogger.addListener("AllureSelenide",new CustomAllureSelenide().screenshots(true).savePageSource(true));
    }

    /**
     * Метод, выполняемый перед каждым тестом.
     * Настраивает параметры конфигурации Selenide и параметры браузера Chrome.
     */
    @BeforeEach
    public void options() {
        Configuration.timeout = 6000;
        Configuration.browser = "chrome";

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--start-maximized");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "none");

        Configuration.browserCapabilities = capabilities;
    }
}
