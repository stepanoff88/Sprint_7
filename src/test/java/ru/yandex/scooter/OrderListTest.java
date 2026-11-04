package ru.yandex.scooter;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.scooter.client.OrderClient;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Яндекс.Самокат API")
@Feature("Список заказов")
class OrderListTest extends BaseApiTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    @Story("Получение списка")
    @DisplayName("В тело ответа возвращается список заказов")
    void ordersListIsReturned() {
        Response response = orderClient.getOrders();

        assertThat(response.statusCode())
                .as("Код ответа при получении списка заказов")
                .isEqualTo(200);

        List<Map<String, Object>> orders = response.jsonPath().getList("orders");
        assertThat(orders)
                .as("Список заказов в теле ответа")
                .isNotNull()
                .isNotEmpty();
    }
}
