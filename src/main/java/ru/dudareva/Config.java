package ru.dudareva;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Единый конфигурационный класс для тестового окружения
 * @author Dudareva Diana
 * @version 1.0
 */
public class Config {

    /**
     * Переменные среды
     */
    private static Properties properties = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Не найден config.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод получает значение переменной среды по её ключу
     * @param key Ключ переменной окружени
     * @return Значение переменной среды
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
