package ru.yandex.scooter;

import io.restassured.response.Response;
import ru.yandex.scooter.client.CourierClient;
import ru.yandex.scooter.client.OrderClient;
import ru.yandex.scooter.model.Courier;
import ru.yandex.scooter.model.CourierCredentials;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Общие действия для тестов: создание и удаление тестовых данных.
 */
public abstract class BaseApiTest {

    protected final CourierClient courierClient = new CourierClient();
    protected final OrderClient orderClient = new OrderClient();

    /**
     * Создаёт курьера и возвращает его id.
     */
    protected int createCourier(Courier courier) {
        Response createResponse = courierClient.create(courier);
        assertThat(createResponse.statusCode())
                .as("Код ответа при подготовке курьера")
                .isEqualTo(201);

        return login(courier);
    }

    /**
     * Авторизует курьера и возвращает его id.
     */
    protected int login(Courier courier) {
        Response loginResponse = courierClient.login(CourierCredentials.from(courier));
        assertThat(loginResponse.statusCode())
                .as("Код ответа при авторизации подготовленного курьера")
                .isEqualTo(200);

        return loginResponse.jsonPath().getInt("id");
    }

    /**
     * Удаляет курьера, если он существует. Используется для очистки после теста.
     */
    protected void deleteCourierIfExists(Courier courier) {
        Response loginResponse = courierClient.login(CourierCredentials.from(courier));

        if (loginResponse.statusCode() == 200) {
            Integer id = loginResponse.jsonPath().getObject("id", Integer.class);
            if (id != null) {
                courierClient.delete(id);
            }
        }
    }
}
