package ru.yandex.scooter.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.scooter.model.Order;

/**
 * Клиент для работы с ручками заказов.
 */
public class OrderClient extends BaseClient {

    @Step("Создать заказ {order}")
    public Response create(Order order) {
        return request()
                .body(order)
                .when()
                .post("/orders");
    }

    @Step("Получить список заказов")
    public Response getOrders() {
        return request()
                .when()
                .get("/orders");
    }

    @Step("Отменить заказ с track = {track}")
    public Response cancel(int track) {
        return request()
                .queryParam("track", track)
                .when()
                .put("/orders/cancel");
    }
}
