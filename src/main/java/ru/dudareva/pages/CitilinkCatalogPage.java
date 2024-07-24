package ru.dudareva.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.actions;
import static com.codeborne.selenide.Selenide.page;

/**
 * Класс, представляющий страницу каталога сайта Citilink.
 * Содержит методы для взаимодействия с элементами страницы каталога.
 * @version 1.0
 * @autor Дударева Диана
 */
public class CitilinkCatalogPage {
    /**
     * XPath для раздела.
     */
    private static final String CATEGORY_XPATH = "//a[contains(@class, 'CatalogLayout__link_level-1') and contains(., '%s')]";

    /**
     * XPath для подраздела.
     */
    private static final String SUBCATEGORY_XPATH = "//li[@class='CatalogLayout__children-item']//a[text()='%s']";

    /**
     * Метод для наведения курсора на раздел.
     * @param categoryName Название раздела, на который нужно навести курсор.
     * @return объект типа {@link CitilinkCatalogPage}
     */
    @Step("Наведение мыши на раздел c текстом {categoryName}")
    public CitilinkCatalogPage moveToSection(String categoryName) {
        SelenideElement category = $x(String.format(CATEGORY_XPATH, categoryName)).shouldBe(Condition.visible);
        actions().moveToElement(category).perform();
        return this;
    }

    /**
     * Метод для клика на подраздел.
     * @param categoryName Название подраздела, на который нужно кликнуть.
     * @return объект типа {@link CitilinkSmartphones}
     */
    @Step("Выбор раздела {categoryName}")
    public CitilinkSmartphones clickOnSubsection(String categoryName) {
        $x(String.format(SUBCATEGORY_XPATH, categoryName)).shouldBe(Condition.visible).click();
        return page(CitilinkSmartphones.class);
    }

}
