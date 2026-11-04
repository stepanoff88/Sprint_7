package ru.yandex.scooter;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.scooter.client.CourierClient;
import ru.yandex.scooter.model.Courier;
import ru.yandex.scooter.model.CourierCredentials;
import ru.yandex.scooter.util.TestData;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Яндекс.Самокат API")
@Feature("Авторизация курьера")
class CourierLoginTest extends BaseApiTest {

    private final CourierClient courierClient = new CourierClient();

    private Courier courier;
    private int courierId;

    @BeforeEach
    void setUp() {
        courier = TestData.randomCourier();
        courierId = createCourier(courier);
    }

    @AfterEach
    void tearDown() {
        if (courierId > 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @Story("Успешный вход")
    @DisplayName("Курьер может авторизоваться, ответ содержит id")
    void courierCanLogin() {
        Response response = courierClient.login(CourierCredentials.from(courier));

        assertThat(response.statusCode())
                .as("Код ответа при авторизации")
                .isEqualTo(200);
        assertThat(response.jsonPath().getObject("id", Integer.class))
                .as("id авторизованного курьера")
                .isNotNull()
                .isEqualTo(courierId);
    }

    @Test
    @Story("Неверные учётные данные")
    @DisplayName("Система вернёт ошибку при неверном пароле")
    void loginWithWrongPasswordReturnsError() {
        Response response = courierClient.login(
                new CourierCredentials(courier.getLogin(), "wrong_password")
        );

        assertThat(response.statusCode())
                .as("Код ответа при неверном пароле")
                .isEqualTo(404);
        assertThat(response.jsonPath().getString("message"))
                .as("Сообщение об ошибке")
                .isEqualTo("Учетная запись не найдена");
    }

    @Test
    @Story("Неверные учётные данные")
    @DisplayName("Система вернёт ошибку при неверном логине")
    void loginWithWrongLoginReturnsError() {
        Response response = courierClient.login(
                new CourierCredentials(TestData.randomLogin(), courier.getPassword())
        );

        assertThat(response.statusCode())
                .as("Код ответа при неверном логине")
                .isEqualTo(404);
        assertThat(response.jsonPath().getString("message"))
                .as("Сообщение об ошибке")
                .isEqualTo("Учетная запись не найдена");
    }

    @Test
    @Story("Несуществующий пользователь")
    @DisplayName("Нельзя авторизоваться под несуществующим пользователем")
    void loginWithNonExistentUserReturnsError() {
        Response response = courierClient.login(
                new CourierCredentials(TestData.randomLogin(), TestData.PASSWORD)
        );

        assertThat(response.statusCode())
                .as("Код ответа для несуществующего пользователя")
                .isEqualTo(404);
        assertThat(response.jsonPath().getString("message"))
                .as("Сообщение об ошибке")
                .isEqualTo("Учетная запись не найдена");
    }

    @Test
    @Story("Обязательные поля")
    @DisplayName("Нельзя авторизоваться без логина")
    void loginWithoutLoginReturnsError() {
        Response response = courierClient.login(
                new CourierCredentials(null, courier.getPassword())
        );

        assertThat(response.statusCode())
                .as("Код ответа без логина")
                .isEqualTo(400);
        assertThat(response.jsonPath().getString("message"))
                .as("Сообщение об ошибке")
                .isEqualTo("Недостаточно данных для входа");
    }

    @Test
    @Story("Обязательные поля")
    @DisplayName("Нельзя авторизоваться с пустым паролем")
    void loginWithEmptyPasswordReturnsError() {
        // На текущем стенде запрос вообще без поля password приводит к зависанию
        // и ответу 504 от nginx, поэтому отсутствие пароля проверяем пустым значением.
        Response response = courierClient.login(
                new CourierCredentials(courier.getLogin(), "")
        );

        assertThat(response.statusCode())
                .as("Код ответа с пустым паролем")
                .isEqualTo(400);
        assertThat(response.jsonPath().getString("message"))
                .as("Сообщение об ошибке")
                .isEqualTo("Недостаточно данных для входа");
    }
}
