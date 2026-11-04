package ru.yandex.scooter.util;

import ru.yandex.scooter.model.Courier;
import ru.yandex.scooter.model.Order;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Генерация уникальных тестовых данных.
 */
public final class TestData {

    public static final String PASSWORD = "qwerty123";

    private TestData() {
    }

    public static String randomLogin() {
        return "courier_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    public static Courier randomCourier() {
        return new Courier(randomLogin(), PASSWORD, "Иван");
    }

    public static Order randomOrder() {
        return new Order(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                LocalDate.now().plusDays(1).toString(),
                "Saske, come back to Konoha",
                null
        );
    }
}
