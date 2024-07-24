package ru.dudareva.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import ru.dudareva.Config;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;

/**
 * Класс, представляющий главную страницу сайта Citilink.
 * Содержит методы для взаимодействия с элементами главной страницы.
 * @version 1.0
 * @autor Дударева Диана
 */
public class CitilinkMainPage {

    /**
     * CSS-селектор для кнопки каталога.
     */
    private static final String CATALOG_BUTTON_CSS = "a[data-meta-name='DesktopHeaderFixed__catalog-menu']";

    /**
     * Элемент кнопки каталога.
     */
    private final SelenideElement catalogButton = $(By.cssSelector(CATALOG_BUTTON_CSS));

    /**
     * Открытие сайта citilink.
     * Метод открывает сайт по прямой ссылке и возвращает главную страницу сайта.
     * @return объект типа {@link CitilinkMainPage}
     */
    @Step("Переход в каталог товаров")
    public CitilinkMainPage openPage() {
        open(Config.getProperty("citilink.url"));
        return this;
    }

    /**
     * Переход в каталог товаров.
     * Метод кликает на кнопку каталога и возвращает страницу каталога.
     * @return объект типа {@link CitilinkCatalogPage}
     */
    @Step("Переход в каталог товаров")
    public CitilinkCatalogPage clickOnCatalog() {
        catalogButton.shouldBe(Condition.visible).click();
        return page(CitilinkCatalogPage.class);
    }

}
