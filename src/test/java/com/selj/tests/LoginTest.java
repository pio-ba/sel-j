package com.selj.tests;

import com.selj.pages.DashboardPage;
import com.selj.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class LoginTest extends BaseTest {

    private static final String DASHBOARD_TITLE = "Dashboard";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";

    @Test(dataProvider = "validLogin", dataProviderClass = TestData.class,
            description = "Valid login attempt with correct credentials")
    @Epic("Authentication")
    @Feature("Login")
    @Story("Valid login")
    @Severity(SeverityLevel.NORMAL)
    public void validLoginTest(String username, String password) {
        LoginPage loginPage = loginPage();
        DashboardPage dashboard = loginPage.loginAndGetDashboard(username, password);

        assertEquals(dashboard.getHeaderTitle(), DASHBOARD_TITLE, "Dashboard title mismatch");
    }

    @Test(dataProvider = "invalidLogin", dataProviderClass = TestData.class,
            description = "Invalid login attempt with incorrect credentials")
    @Epic("Authentication")
    @Feature("Login")
    @Story("Invalid login")
    @Severity(SeverityLevel.NORMAL)
    public void invalidLoginTest(String username, String password) {
        LoginPage loginPage = submitLogin(username, password);

        assertEquals(loginPage.getErrorMessage(), INVALID_CREDENTIALS_MESSAGE,
                "Error message mismatch");
    }

    @Test(dataProvider = "invalidLogin", dataProviderClass = TestData.class,
            description = "Invalid login keeps the user on the login page")
    @Epic("Authentication")
    @Feature("Login")
    @Story("Invalid login")
    @Severity(SeverityLevel.NORMAL)
    public void invalidLoginDoesNotNavigateAway(String username, String password) {
        submitLogin(username, password);

        assertEquals(baseUrl, getCurrentUrl(),
                "Invalid login should keep the user on the login page");
    }

    private LoginPage submitLogin(String username, String password) {
        return loginPage()
                .enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }
}