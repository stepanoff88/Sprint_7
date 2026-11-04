package ru.yandex.scooter.config;

/**
 * Настройки тестируемого стенда.
 */
public final class ApiConfig {

    public static final String BASE_URI = System.getProperty(
            "base.url",
            "https://qa-scooter.education-services.ru"
    );

    public static final String BASE_PATH = "/api/v1";

    private ApiConfig() {
    }
}
