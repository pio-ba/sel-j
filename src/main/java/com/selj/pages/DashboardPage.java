package com.selj.pages;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.selj.config.DriverManager;
import com.selj.utils.ElementActions;
import com.selj.utils.WaitUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage {

    @FindBy(css = "h1.page-title")
    private WebElement headerTitle;

    @FindBy(id = "logoutBtn")
    private WebElement logoutButton;

    private final Injector injector;

    @Inject
    public DashboardPage(DriverManager driverManager, ElementActions actions, WaitUtils wait, Injector injector) {
        super(driverManager, actions, wait);
        this.injector = injector;
    }

    public String getHeaderTitle() {
        return actions.getText(headerTitle);
    }

    public LoginPage logout() {
        actions.click(logoutButton);
        return injector.getInstance(LoginPage.class);
    }
}
