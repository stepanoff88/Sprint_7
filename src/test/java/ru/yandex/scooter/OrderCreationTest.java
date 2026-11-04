package ru.yandex.scooter;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.scooter.client.OrderClient;
import ru.yandex.scooter.model.Order;
import ru.yandex.scooter.util.TestData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Яндекс.Самокат API")
@Feature("Создание заказа")
class OrderCreationTest extends BaseApiTest {

    private final OrderClient orderClient = new OrderClient();
    private final List<Integer> createdTracks = new ArrayList<>();

    @AfterEach
    void tearDown() {
        createdTracks.forEach(orderClient::cancel);
        createdTracks.clear();
    }

    @ParameterizedTest(name = "Цвет: {0}")
    @MethodSource("colorOptions")
    @Story("Цвет заказа")
    @DisplayName("Заказ можно создать с разными вариантами цвета")
    void orderCanBeCreatedWithDifferentColors(String description, List<String> colors) {
        Order order = TestData.randomOrder();
        order.setColor(colors);

        Response response = orderClient.create(order);

        assertThat(response.statusCode())
                .as("Код ответа при создании заказа (%s)", description)
                .isEqualTo(201);
        Integer track = response.jsonPath().getObject("track", Integer.class);
        assertThat(track)
                .as("Тело ответа содержит track")
                .isNotNull()
                .isPositive();

        createdTracks.add(track);
    }

    static Stream<Arguments> colorOptions() {
        return Stream.of(
                Arguments.of("не указан", (Object) null),
                Arguments.of("BLACK", List.of("BLACK")),
                Arguments.of("GREY", List.of("GREY")),
                Arguments.of("BLACK и GREY", List.of("BLACK", "GREY"))
        );
    }
}
