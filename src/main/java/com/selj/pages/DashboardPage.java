package com.selj.pages;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.selj.actions.WebElementInteraction;
import org.openqa.selenium.WebDriver;
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
    public DashboardPage(WebDriver driver, WebElementInteraction actions,
                         Provider<LoginPage> loginPageProvider) {
        super(driver, actions);
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
