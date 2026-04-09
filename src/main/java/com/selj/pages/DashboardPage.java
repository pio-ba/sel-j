package com.selj.pages;

import com.selj.utils.ElementActions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage {

    @FindBy(css = "h1.page-title")
    private WebElement headerTitle;

    @FindBy(id = "logoutBtn")
    private WebElement logoutButton;

    public String getHeaderTitle() {
        return ElementActions.getText(headerTitle);
    }

    public LoginPage logout() {
        ElementActions.click(logoutButton);
        return new LoginPage();
    }
}
