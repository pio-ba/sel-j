package com.selj.tests;

import com.selj.pages.DashboardPage;
import com.selj.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(dataProvider = "validLogin", dataProviderClass = TestData.class,
            description = "Valid login attempt with correct credentials")
    @Epic("Authentication")
    @Feature("Login")
    @Story("Valid login")
    @Severity(SeverityLevel.NORMAL)
    public void validLoginTest(String username, String password) {
        LoginPage loginPage = loginPage();
        DashboardPage dashboard = loginPage.loginAndGetDashboard(username, password);
        String actualTitle = dashboard.getHeaderTitle();
        Assert.assertEquals(actualTitle, "Dashboard", "Dashboard title mismatch");
    }

    @Test(dataProvider = "invalidLogin", dataProviderClass = TestData.class,
            description = "Invalid login attempt with incorrect credentials")
    @Epic("Authentication")
    @Feature("Login")
    @Story("Invalid login")
    @Severity(SeverityLevel.NORMAL)
    public void invalidLoginTest(String username, String password) {
        LoginPage loginPage = loginPage();
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, "Invalid credentials", "Error message mismatch");
    }
}
