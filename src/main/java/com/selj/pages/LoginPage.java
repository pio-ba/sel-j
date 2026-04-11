package com.selj.pages;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.selj.config.DriverManager;
import com.selj.utils.ElementActions;
import com.selj.utils.WaitUtils;
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

    private final Injector injector;

    @Inject
    public LoginPage(DriverManager driverManager, ElementActions actions, WaitUtils wait, Injector injector) {
        super(driverManager, actions, wait);
        this.injector = injector;
    }

    public void enterUsername(String username) {
        actions.type(usernameInput, username);
    }

    public void enterPassword(String password) {
        actions.type(passwordInput, password);
    }

    public DashboardPage clickLogin() {
        actions.click(loginButton);
        return injector.getInstance(DashboardPage.class);
    }

    public DashboardPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }

    public String getErrorMessage() {
        return actions.getText(errorMessage);
    }
}
