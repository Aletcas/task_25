package pages;

import com.microsoft.playwright.Page;

public class LoginPage {
    private final Page page;

    // Локаторы элементов страницы
    private final String usernameInput = "#username";
    private final String passwordInput = "#password";
    private final String submitButton = "button[type='submit']";
    private final String successMessage = ".flash.success";
    private final String errorMessage = ".flash.error";

    // Конструктор принимает экземпляр Page от Playwright
    public LoginPage(Page page) {
        this.page = page;
    }

    // 1. Навигация на страницу логина
    public void navigate(String baseUrl) {
        page.navigate(baseUrl + "/login");
    }

    // 2. Заполнение формы логина
    public void fillCredentials(String username, String password) {
        page.locator(usernameInput).fill(username);
        page.locator(passwordInput).fill(password);
    }

    // 3. Нажатие кнопки Submit
    public void submit() {
        page.locator(submitButton).click();
    }

    // 4. Комбинированный метод для быстрого логина
    public void login(String username, String password) {
        fillCredentials(username, password);
        submit();
    }

    // 5. Получение текста успешного сообщения
    public String getSuccessMessage() {
        page.waitForSelector(successMessage);
        return page.locator(successMessage).textContent();
    }

    // 6. Получение текста сообщения об ошибке
    public String getErrorMessage() {
        page.waitForSelector(errorMessage);
        return page.locator(errorMessage).textContent();
    }

    // 7. Проверка, отображается ли сообщение об успехе
    public boolean isSuccessMessageVisible() {
        return page.locator(successMessage).isVisible();
    }

    // 8. Проверка, отображается ли сообщение об ошибке
    public boolean isErrorMessageVisible() {
        return page.locator(errorMessage).isVisible();
    }

    // 9. Получение текущего URL (для проверки редиректа)
    public String getCurrentUrl() {
        return page.url();
    }
}
