package ru.dudareva.tests;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.dudareva.pages.CitilinkMainPage;

import java.util.List;

/**
 * Класс для выполнения тестов на сайте Citilink.
 * Содержит методы для проверки различных разделов и фильтров на сайте.
 * @version 1.0
 * @autor Дударева Диана
 */
public class CitilinkTest extends BaseTests {

    /**
     * Тест для проверки раздела смартфонов на сайте Citilink.
     * Открывает главную страницу, переходит в каталог, выбирает раздел, проверяет соответствие бренда и фильтров.
     *
     * @param categoryName Имя категории для навигации.
     * @param brands Список брендов для фильтрации.
     * @param filter Название фильтра для проверки.
     */
    @Feature("Проверка секции сматрфоны")
    @DisplayName("Проверка секции сматрфоны")
    @ParameterizedTest(name = "{displayName} {arguments}")
    @MethodSource("ru.dudareva.helpers.DataProvider#providerCitilinkArgs")
    public void testCitilinkSmartphoneSection(String categoryName, List<String> brands, String filter) {
        CitilinkMainPage page = new CitilinkMainPage();
        page.openPage()
                .clickOnCatalog()
                .moveToSection(categoryName)
                .clickOnSubsection(categoryName)
                .checkForSection(categoryName)
                .selectBrand(brands)
                .waitForProductsToLoad()
                .resultsMatchFilters(filter);
    }
}
