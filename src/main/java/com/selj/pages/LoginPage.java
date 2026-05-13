package com.selj.pages;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.selj.actions.WebElementInteraction;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Login page.
 * Provides methods for user authentication and error handling.
 */
public class LoginPage extends BasePage {

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "loginBtn")
    private WebElement loginButton;

    @FindBy(css = ".error-message")
    private WebElement errorMessage;

    private final Provider<DashboardPage> dashboardPageProvider;

    @Inject
    public LoginPage(WebDriver driver, WebElementInteraction actions,
                     Provider<DashboardPage> dashboardPageProvider) {
        super(driver, actions);
        this.dashboardPageProvider = dashboardPageProvider;
    }

    /**
     * Enter username in the input field.
     */
    public LoginPage enterUsername(String username) {
        actions.type(usernameInput, username);
        return this;
    }

    /**
     * Enter password in the input field.
     */
    public LoginPage enterPassword(String password) {
        actions.type(passwordInput, password);
        return this;
    }

    /**
     * Click login button (stay on LoginPage for error handling).
     */
    public LoginPage clickLogin() {
        actions.click(loginButton);
        return this;
    }

    /**
     * Perform login and navigate to Dashboard.
     */
    public DashboardPage loginAndGetDashboard(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return dashboardPageProvider.get();
    }

    /**
     * Get error message displayed on login page.
     */
    public String getErrorMessage() {
        return actions.getText(errorMessage);
    }
}
