# Sprint_7

Проект с API-тестами для учебного сервиса **Яндекс.Самокат**.

Ссылка на сервис: <https://qa-scooter.education-services.ru>
Документация API: <https://qa-scooter.education-services.ru/docs/>

## Стек

- Java 17
- Maven
- JUnit 5
- REST Assured
- Allure Report
- AssertJ

## Что проверяется

- Создание курьера: успешное создание, уникальность логина, обязательные поля и коды ответов.
- Авторизация курьера: успешный вход, неверный логин/пароль, отсутствующие поля, несуществующий пользователь.
- Создание заказа: параметризованная проверка цветов `BLACK`, `GREY`, обоих цветов и варианта без цвета.
- Получение списка заказов.

Тесты независимы: для каждого теста генерируются уникальные данные, созданные курьеры удаляются,
а созданные заказы отменяются после выполнения теста.

## Запуск тестов

```bash
mvn clean test
```

Результаты Allure сохраняются в `target/allure-results`.

## Allure-отчёт

Сгенерировать HTML-отчёт:

```bash
mvn allure:report
```

Открыть отчёт локально:

```bash
mvn allure:serve
```

Готовый отчёт после генерации находится в `target/site/allure-maven-plugin`.

## Структура проекта

```
src/main/java/ru/yandex/scooter
├── client   — клиенты для работы с API
├── config   — настройки стенда
├── model    — модели запросов и ответов
└── util     — генерация тестовых данных

src/test/java/ru/yandex/scooter
├── CourierCreationTest — создание курьера
├── CourierLoginTest    — авторизация курьера
├── OrderCreationTest   — создание заказа
└── OrderListTest       — список заказов
```
