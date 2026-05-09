package com.selj.pages;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.selj.utils.ElementActions;
import com.selj.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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
    public LoginPage(WebDriver driver, ElementActions actions, WaitUtils wait,
                     Provider<DashboardPage> dashboardPageProvider) {
        super(driver, actions, wait);
        this.dashboardPageProvider = dashboardPageProvider;
    }

    public LoginPage enterUsername(String username) {
        actions.type(usernameInput, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        actions.type(passwordInput, password);
        return this;
    }

    public LoginPage clickLogin() {
        actions.click(loginButton);
        return this;
    }

    public DashboardPage loginAndGetDashboard(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return submitAndGetDashboard();
    }

    public LoginPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return this;
    }

    private DashboardPage submitAndGetDashboard() {
        actions.click(loginButton);
        return dashboardPageProvider.get();
    }

    public String getErrorMessage() {
        return actions.getText(errorMessage);
    }
}
