package com.selj.pages;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.selj.ui.WebElementInteraction;
import com.selj.ui.WebElementWait;
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
    public LoginPage(WebDriver driver, WebElementInteraction actions, WebElementWait wait,
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

    public DashboardPage loginAndGetDashboard(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        actions.click(loginButton);
        return dashboardPageProvider.get();
    }

    public String getErrorMessage() {
        return actions.getText(errorMessage);
    }
}
