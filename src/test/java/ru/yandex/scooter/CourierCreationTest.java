package ru.yandex.scooter;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.scooter.client.CourierClient;
import ru.yandex.scooter.model.Courier;
import ru.yandex.scooter.util.TestData;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Яндекс.Самокат API")
@Feature("Создание курьера")
class CourierCreationTest extends BaseApiTest {

    private final CourierClient courierClient = new CourierClient();

    private Courier courier;

    @BeforeEach
    void setUp() {
        courier = TestData.randomCourier();
    }

    @AfterEach
    void tearDown() {
        deleteCourierIfExists(courier);
    }

    @Test
    @Story("Успешное создание")
    @DisplayName("Курьера можно создать")
    void courierCanBeCreated() {
        Response response = courierClient.create(courier);

        assertThat(response.statusCode())
                .as("Код ответа при создании курьера")
                .isEqualTo(201);
        assertThat(response.jsonPath().getBoolean("ok"))
                .as("Признак успешного создания")
                .isTrue();
    }

    @Test
    @Story("Уникальность логина")
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    void courierCannotBeCreatedTwice() {
        courierClient.create(courier);

        Response duplicateResponse = courierClient.create(courier);

        assertThat(duplicateResponse.statusCode())
                .as("Код ответа при повторном создании курьера")
                .isEqualTo(409);
        assertThat(duplicateResponse.jsonPath().getString("message"))
                .as("Сообщение об ошибке")
                .contains("Этот логин уже используется");
    }

    @Test
    @Story("Уникальность логина")
    @DisplayName("Нельзя создать курьера с логином, который уже есть")
    void courierCannotBeCreatedWithExistingLogin() {
        courierClient.create(courier);

        Courier courierWithSameLogin = new Courier(courier.getLogin(), "anotherPassword", "Другой курьер");
        Response response = courierClient.create(courierWithSameLogin);

        assertThat(response.statusCode())
                .as("Код ответа при создании курьера с занятым логином")
                .isEqualTo(409);
        assertThat(response.jsonPath().getString("message"))
                .as("Сообщение об ошибке")
                .contains("Этот логин уже используется");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("couriersWithoutRequiredFields")
    @Story("Обязательные поля")
    @DisplayName("Нельзя создать курьера без обязательного поля")
    void courierCannotBeCreatedWithoutRequiredField(String description, Courier invalidCourier) {
        Response response = courierClient.create(invalidCourier);

        assertThat(response.statusCode())
                .as("Код ответа: %s", description)
                .isEqualTo(400);
        assertThat(response.jsonPath().getString("message"))
                .as("Сообщение об ошибке")
                .isEqualTo("Недостаточно данных для создания учетной записи");
    }

    static Stream<Arguments> couriersWithoutRequiredFields() {
        return Stream.of(
                Arguments.of("Без логина", new Courier(null, TestData.PASSWORD, "Иван")),
                Arguments.of("Без пароля", new Courier(TestData.randomLogin(), null, "Иван"))
        );
    }
}
