package com.selj.pages;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.selj.config.ConfigReader;
import com.selj.utils.ElementActions;
import com.selj.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage {

    @FindBy(css = "h1.page-title")
    private WebElement headerTitle;

    @FindBy(id = "logoutBtn")
    private WebElement logoutButton;

    private final Provider<LoginPage> loginPageProvider;

    @Inject
    public DashboardPage(WebDriver driver, ElementActions actions, WaitUtils wait,
                         Provider<LoginPage> loginPageProvider) {
        super(driver, actions, wait);
        this.loginPageProvider = loginPageProvider;
    }

    public String getHeaderTitle() {
        return actions.getText(headerTitle);
    }

    public LoginPage logout() {
        actions.click(logoutButton);
        return loginPageProvider.get();
    }
}
