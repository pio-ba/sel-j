package com.selj.tests;

import com.selj.pages.ProductsPage;
import com.selj.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest extends BaseTest {

    private static final String PRODUCTS_PAGE_TITLE = "Products";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Neither email nor password matched";

    @Test(dataProvider = "validLogin", dataProviderClass = TestData.class,
            description = "Valid login attempt with correct credentials")
    @Epic("Authentication")
    @Feature("Login")
    @Story("Valid login")
    @Severity(SeverityLevel.NORMAL)
    public void validLoginTest(String username, String password) {
        LoginPage loginPage = loginPage();
        ProductsPage dashboard = loginPage.loginAndGetDashboard(username, password);

        assertThat(dashboard.getPageTitle())
                .as("Page title should contain %s", PRODUCTS_PAGE_TITLE)
                .contains(PRODUCTS_PAGE_TITLE);
    }

    @Test(dataProvider = "invalidLogin", dataProviderClass = TestData.class,
            description = "Invalid login attempt with incorrect credentials")
    @Epic("Authentication")
    @Feature("Login")
    @Story("Invalid login")
    @Severity(SeverityLevel.NORMAL)
    public void invalidLoginTest(String username, String password) {
        LoginPage loginPage = submitLogin(username, password);

        assertThat(loginPage.getErrorMessage())
                .as("Error message mismatch")
                .contains(INVALID_CREDENTIALS_MESSAGE);
    }

    @Test(dataProvider = "invalidLogin", dataProviderClass = TestData.class,
            description = "Invalid login keeps the user on the login page")
    @Epic("Authentication")
    @Feature("Login")
    @Story("Invalid login")
    @Severity(SeverityLevel.NORMAL)
    public void invalidLoginDoesNotNavigateAway(String username, String password) {
        submitLogin(username, password);

        assertThat(getCurrentUrl())
                .as("Invalid login should keep the user on the login page")
                .isEqualTo(baseUrl);
    }

    private LoginPage submitLogin(String username, String password) {
        return loginPage()
                .enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }
}