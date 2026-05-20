package com.selj.pages;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.selj.actions.WebElementInteraction;
import com.selj.core.WebDriverFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Dashboard page.
 * Provides methods for dashboard interactions.
 */
public class DashboardPage extends BasePage {

    @FindBy(css = "h1.page-title")
    private WebElement headerTitle;

    @FindBy(id = "logoutBtn")
    private WebElement logoutButton;

    private final Provider<LoginPage> loginPageProvider;

    @Inject
    public DashboardPage(WebDriverFactory webDriverFactory, WebElementInteraction actions,
                         Provider<LoginPage> loginPageProvider) {
        super(webDriverFactory, actions);
        this.loginPageProvider = loginPageProvider;
    }

    /**
     * Get dashboard header title text.
     */
    public String getHeaderTitle() {
        return actions.getText(headerTitle);
    }

    /**
     * Click logout button and return to login page.
     */
    public LoginPage logout() {
        actions.click(logoutButton);
        return loginPageProvider.get();
    }
}
