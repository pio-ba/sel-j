package com.selj.pages;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.selj.actions.WebElementInteraction;
import com.selj.core.WebDriverFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Products page.
 * Provides methods for products interactions.
 */
public class ProductsPage extends BasePage {

    @FindBy(id = "product-sort")
    private WebElement pageTitle;

    @FindBy(id = "logoutBtn")
    private WebElement logoutButton;

    private final Provider<LoginPage> loginPageProvider;

    @Inject
    public ProductsPage(WebDriverFactory webDriverFactory, WebElementInteraction actions,
                        Provider<LoginPage> loginPageProvider) {
        super(webDriverFactory, actions);
        this.loginPageProvider = loginPageProvider;
    }

    /**
     * Get page title text.
     */
    public String getPageTitle() {
        return actions.getText(pageTitle);
    }

    /**
     * Click logout button and return to login page.
     */
    public LoginPage logout() {
        actions.click(logoutButton);
        return loginPageProvider.get();
    }
}
