package com.selj.pages;

import com.selj.utils.ElementActions;
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

    public void enterUsername(String username) {
        ElementActions.type(usernameInput, username);
    }

    public void enterPassword(String password) {
        ElementActions.type(passwordInput, password);
    }

    public DashboardPage clickLogin() {
        ElementActions.click(loginButton);
        return new DashboardPage();
    }

    public DashboardPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }

    public String getErrorMessage() {
        return ElementActions.getText(errorMessage);
    }
}
