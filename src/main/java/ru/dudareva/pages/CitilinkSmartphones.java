package ru.dudareva.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static ru.dudareva.helpers.Assertions.assertTrue;


/**
 * Класс, представляющий страницу раздела "Смартфоны" на сайте Citilink.
 * Содержит методы для взаимодействия с элементами страницы раздела.
 * @version 1.0
 * @autor Дударева Диана
 */
public class CitilinkSmartphones {

    /**
     * XPath для наименования раздела.
     */
    private static final String SECTION_XPATH = "//div[@data-meta-name='SubcategoryPageTitle']//h1";

    /**
     * XPath для чекбокса с брендом.
     */
    private static final String BRAND_CHECKBOX_XPATH = "//div[@data-meta-name='FilterLabel' and @data-meta-value='%s']//input";

    /**
     * XPath для кнопки принятия файлов cookie.
     */
    private static final String ACCEPT_COOKIES_BUTTON_XPATH = "//button[span[text()='Я согласен']]";

    /**
     * XPath для элементов продуктов.
     */
    private static final String PRODUCT_ELEMENTS_XPATH = "//div[@data-meta-name='ProductVerticalSnippet']";

    /**
     * XPath для кнопки новой страницы.
     */
    private static final String NEXT_PAGE_XPATH = "//a[@data-meta-name='PageLink__page-page-next']";

    /**
     * Метод для проверки перехода в раздел.
     * @param sectionName Название раздела для проверки.
     * @return объект типа {@link CitilinkSmartphones}
     */
    @Step("Проверка, перехода в раздел {sectionName}")
    public CitilinkSmartphones checkForSection(String sectionName) {
        $x(SECTION_XPATH).shouldHave(Condition.text(sectionName));
        return this;
    }

    /**
     * Метод для фильтрации списка товаров по производителю.
     * @param brands Список брендов для фильтрации.
     * @return объект типа {@link CitilinkSmartphones}
     */
    @Step("Фильтрация списка по производителям: {brands}")
    public CitilinkSmartphones selectBrand(List<String> brands) {
        $x(ACCEPT_COOKIES_BUTTON_XPATH).shouldBe(Condition.enabled).click();
        for (String brand : brands) {
            $x(String.format(BRAND_CHECKBOX_XPATH, brand.toUpperCase())).shouldBe(Condition.enabled).click();
        }
        return this;
    }

    /**
     * Метод для ожидания загрузки списка товаров.
     * @return объект типа {@link CitilinkSmartphones}
     */
    @Step("Ожидание загрузки товаров")
    public CitilinkSmartphones waitForProductsToLoad() {
        ElementsCollection products = $$x(PRODUCT_ELEMENTS_XPATH);
        for (int i = 0; i < products.size(); i++) {
            products.get(i).shouldBe(Condition.exist, Duration.ofSeconds(1));
        }
        return this;
    }

    /**
     * Метод для проверки, что товары соответствуют условиям фильтрации.
     * @param filter Условие фильтрации для проверки товаров.
     * @return объект типа {@link CitilinkSmartphones}
     */
    @Step("Проверка, что товары соответствуют условиям фильтрации : {filter}")
    public CitilinkSmartphones resultsMatchFilters(String filter) {
        while (true) {
            $$x(PRODUCT_ELEMENTS_XPATH).forEach(product -> {
                String title = product.text();
                assertTrue(title.toUpperCase().contains(filter.toUpperCase()),
                        "Товар должен совпадать с фильтром " + filter);
            });

            SelenideElement nextPageButton = $x(NEXT_PAGE_XPATH);
            if (nextPageButton.exists()) {
                nextPageButton.click();
                ElementsCollection products = $$x(PRODUCT_ELEMENTS_XPATH);
                for (int i = 0; i < products.size(); i++) {
                    products.get(i).shouldBe(Condition.exist, Duration.ofSeconds(1));
                }
            } else {
                break;
            }
        }
        return this;
    }
}
