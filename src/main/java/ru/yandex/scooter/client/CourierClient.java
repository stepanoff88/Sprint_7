package ru.yandex.scooter.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.scooter.model.Courier;
import ru.yandex.scooter.model.CourierCredentials;

/**
 * Клиент для работы с ручками курьера.
 */
public class CourierClient extends BaseClient {

    @Step("Создать курьера {courier}")
    public Response create(Courier courier) {
        return request()
                .body(courier)
                .when()
                .post("/courier");
    }

    @Step("Авторизовать курьера {credentials}")
    public Response login(CourierCredentials credentials) {
        return request()
                .body(credentials)
                .when()
                .post("/courier/login");
    }

    @Step("Удалить курьера с id = {id}")
    public Response delete(int id) {
        return request()
                .when()
                .delete("/courier/{id}", id);
    }
}
