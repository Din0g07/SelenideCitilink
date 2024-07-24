package ru.dudareva.helpers;

import io.qameta.allure.Step;

/**
 * Класс Assertions предоставляет методы для выполнения утверждений с шагами Allure.
 * @version 1.0
 * @author Dudareva Diana
 */
public class Assertions {

    /**
     * Проверка, что условие истинно. Если условие ложно, выбрасывает исключение AssertionError с указанным сообщением {message}.
     *
     * @param condition условие, которое должно быть истинным
     * @param message сообщение, которое будет отображено в случае ошибки
     */
    @Step("Проверка, что условие истинно: {message}")
    public static void assertTrue(boolean condition, String message) {
        org.junit.jupiter.api.Assertions.assertTrue(condition, message);
    }

    /**
     * Проверка на ревенство двух объектов. Если они не равны, выбрасывает исключение AssertionError с указанным сообщением {message}.
     *
     * @param expected ожидаемое значение
     * @param actual фактическое значение
     * @param message сообщение, которое будет отображено в случае ошибки
     */
    @Step("Проверка на ревенство двух объектов: {message}")
    public static void assertEquals(Object expected, Object actual, String message) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual, message);
    }
}