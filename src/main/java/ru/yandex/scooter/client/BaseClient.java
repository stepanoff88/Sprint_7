package ru.yandex.scooter.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.yandex.scooter.config.ApiConfig;

/**
 * Базовый клиент: общие настройки запросов и логирование в Allure.
 */
public abstract class BaseClient {

    protected RequestSpecification request() {
        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URI)
                .basePath(ApiConfig.BASE_PATH)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .filter(new AllureRestAssured());
    }
}
