package core;

import com.microsoft.playwright.*;
import config.TestConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import utils.TestUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestBase {
    protected static TestConfig config;
    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext context;
    protected Page page;
    protected Connection dbConnection;

    @BeforeAll
    static void setupAll() {

        config = ConfigFactory.create(TestConfig.class, System.getProperties(), System.getenv());

        TestUtils.log("Конфигурация загружена через Owner:");
        TestUtils.log("  db.url: " + config.dbUrl());
        TestUtils.log("  db.user: " + config.dbUser());
        TestUtils.log("  ui.base.url: " + config.uiBaseUrl());

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );

        TestUtils.log("Инициализация завершена: Playwright + браузер");
    }

    @BeforeEach
    protected void setup(TestInfo testInfo) {
        TestUtils.logTestStart(testInfo.getDisplayName());

        context = browser.newContext();
        page = context.newPage();
        TestUtils.logStep("Создан новый контекст браузера");

        // Устанавливаем подключение к БД
        try {
            TestUtils.log("Подключаюсь к БД:");
            TestUtils.log("  URL: " + config.dbUrl());
            TestUtils.log("  User: " + config.dbUser());

            dbConnection = DriverManager.getConnection(
                    config.dbUrl(),
                    config.dbUser(),
                    config.dbPassword()
            );

            TestUtils.logStep("Подключение к БД установлено успешно!");

            // Проверяем, что можем выполнить запрос
            try (Statement stmt = dbConnection.createStatement()) {
                stmt.executeQuery("SELECT 1");
                TestUtils.logStep("Тестовый запрос к БД выполнен успешно");
            }

        } catch (SQLException e) {
            TestUtils.logException("Не удалось подключиться к БД", e);
            throw new RuntimeException("Не удалось подключиться к БД. Проверьте:\n" +
                    "1. PostgreSQL запущен (pg_ctl status)\n" +
                    "2. Пароль в конфигурации правильный\n" +
                    "3. База testdb существует", e);
        }
    }

    @AfterEach
    void tearDown() {
        // Вызываем метод для специфичной очистки теста
        cleanUpTestData();

        // Общая очистка ресурсов
        TestUtils.logStep("Закрываю подключение к БД");
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException e) {
                TestUtils.logException("Ошибка при закрытии подключения к БД", e);
            }
        }

        TestUtils.logStep("Закрываю контекст браузера");
        if (context != null) {
            context.close();
        }

        TestUtils.logTestEnd("", true);
    }

    @AfterAll
    static void tearDownAll() {
        TestUtils.log("Завершение работы: закрываю браузер и Playwright");

        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }

        TestUtils.separator();
        TestUtils.log("ВСЕ РЕСУРСЫ ОСВОБОЖДЕНЫ");
        TestUtils.separator();
    }

    // Пустой метод, который могут переопределять наследники для своей очистки
    protected void cleanUpTestData() {
    }
}
