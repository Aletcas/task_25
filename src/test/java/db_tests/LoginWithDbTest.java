package db_tests;

import core.TestBase;
import model.User;
import org.junit.jupiter.api.*;
import pages.LoginPage;
import repository.UserRepository;
import utils.TestUtils; // <-- Добавляем импорт

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест авторизации с использованием данных из БД (рефакторинг)")
public class LoginWithDbTest extends TestBase {

    private UserRepository userRepository;
    private LoginPage loginPage;
    private User testUser;

    @Override
    @BeforeEach
    public void setup(TestInfo testInfo) {  // <-- ИЗМЕНИТЬ НА setup (маленькая u)
        // Вызываем родительский setup
        super.setup(testInfo);  // <-- У вас уже правильно


        // Инициализируем компоненты
        userRepository = new UserRepository(dbConnection);
        loginPage = new LoginPage(page);
        testUser = User.createTestUser();

        // Создаем тестового пользователя в БД
        try {
            userRepository.create(testUser);
        } catch (Exception e) {
            TestUtils.logException("Не удалось создать пользователя в БД", e);
            fail("Не удалось создать пользователя в БД: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Неуспешный логин с пользователем из БД")
    void testLoginFailsWithDbUser() {
        TestUtils.logStep("Перехожу на страницу логина");
        loginPage.navigate(config.uiBaseUrl());
        TestUtils.log("URL: " + loginPage.getCurrentUrl());

        TestUtils.logStep("Логинюсь с пользователем из БД");
        TestUtils.log("Логин: " + testUser.getUsername() + ", Пароль: " + testUser.getPassword());
        loginPage.login(testUser.getUsername(), testUser.getPassword());

        TestUtils.logStep("Проверяю сообщение об ошибке");
        boolean isErrorVisible = loginPage.isErrorMessageVisible();
        TestUtils.logAssert("Сообщение об ошибке отображается", isErrorVisible);

        assertTrue(isErrorVisible, "Должно отображаться сообщение об ошибке");

        String errorText = loginPage.getErrorMessage();
        TestUtils.log("Текст ошибки: " + errorText.trim());

        boolean containsExpectedText = errorText.contains("Your username is invalid!");
        TestUtils.logAssert("Сообщение содержит 'Your username is invalid!'", containsExpectedText);

        assertTrue(containsExpectedText,
                "Сообщение должно содержать 'Your username is invalid!'");

        TestUtils.log("✓ Тест завершен: логин с локальным пользователем БД, как и ожидалось, не сработал");
    }

    @Test
    @DisplayName("Успешный логин с дефолтными credentials сайта")
    void testLoginSuccessWithDefaultCredentials() {
        TestUtils.logStep("Использую дефолтные credentials сайта");
        User defaultUser = new User("tomsmith", "SuperSecretPassword!");

        TestUtils.logStep("Перехожу на страницу логина");
        loginPage.navigate(config.uiBaseUrl());

        TestUtils.logStep("Логинюсь с дефолтными данными");
        loginPage.login(defaultUser.getUsername(), defaultUser.getPassword());

        TestUtils.logStep("Проверяю успешный логин");
        boolean isSuccessVisible = loginPage.isSuccessMessageVisible();
        TestUtils.logAssert("Сообщение об успехе отображается", isSuccessVisible);

        boolean isSecureUrl = loginPage.getCurrentUrl().endsWith("/secure");
        TestUtils.logAssert("URL заканчивается на /secure", isSecureUrl);

        String successText = loginPage.getSuccessMessage();
        boolean containsSuccessText = successText.contains("You logged into a secure area");
        TestUtils.logAssert("Сообщение содержит текст об успешном входе", containsSuccessText);

        assertAll(
                () -> assertTrue(isSuccessVisible, "Должно отображаться сообщение об успехе"),
                () -> assertTrue(isSecureUrl, "URL должен заканчиваться на /secure"),
                () -> assertTrue(containsSuccessText, "Сообщение должно содержать текст об успешном входе")
        );

        TestUtils.log("✓ Успешный логин с дефолтными credentials");
    }

    @Override
    protected void cleanUpTestData() {
        // Специфичная для этого теста очистка: удаляем тестового пользователя
        if (testUser != null) {
            try {
                boolean deleted = userRepository.deleteByUsername(testUser.getUsername());
                if (deleted) {
                    TestUtils.logStep("Тестовый пользователь удален из БД");
                }
            } catch (Exception e) {
                TestUtils.logException("Ошибка при удалении пользователя", e);
            }
        }
    }
}