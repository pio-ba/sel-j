package com.selj.tests;

import com.selj.pages.ProductsPage;
import com.selj.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LoginTest extends BaseTest {

    private static final String PRODUCTS_PAGE_TITLE = "Products";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";

    @Test(dataProvider = "validLogin", dataProviderClass = TestData.class,
            description = "Valid login attempt with correct credentials")
    @Epic("Authentication")
    @Feature("Login")
    @Story("Valid login")
    @Severity(SeverityLevel.NORMAL)
    public void validLoginTest(String username, String password) {
        LoginPage loginPage = loginPage();
        ProductsPage dashboard = loginPage.loginAndGetDashboard(username, password);

        assertTrue(dashboard.getPageTitle().contains(PRODUCTS_PAGE_TITLE),
                "Page title should contain " + PRODUCTS_PAGE_TITLE);
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