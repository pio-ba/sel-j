package com.selj.pages;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.selj.actions.WebElementInteraction;
import com.selj.core.WebDriverFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Login page.
 * Provides methods for user authentication and error handling.
 */
public class LoginPage extends BasePage {

    @FindBy(id = "email")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = ".btn-submit")
    private WebElement loginButton;

    @FindBy(css = "li[data-sonner-toast][data-type='error']")
    private WebElement errorMessage;

    private final Provider<ProductsPage> productsPageProvider;

    @Inject
    public LoginPage(WebDriverFactory webDriverFactory, WebElementInteraction actions,
                     Provider<ProductsPage> productsPageProvider) {
        super(webDriverFactory, actions);
        this.productsPageProvider = productsPageProvider;
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
     * Perform login and navigate to Products page.
     */
    public ProductsPage loginAndGetDashboard(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return productsPageProvider.get();
    }

    /**
     * Get error message displayed on login page.
     */
    public String getErrorMessage() {
        return actions.getText(errorMessage);
    }
}
