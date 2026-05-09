package tests;

import com.selj.pages.DashboardPage;
import com.selj.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(dataProvider = "validLogin", dataProviderClass = TestData.class,
            description = "Poprawne logowanie użytkownika")
    @Epic("Authentication")
    @Feature("Login")
    @Story("Valid login")
    @Severity(SeverityLevel.NORMAL)
    public void validLoginTest(String username, String password, String expectedTitle) {
        LoginPage loginPage = loginPage();
        DashboardPage dashboard = loginPage.loginAndGetDashboard(username, password);
        String actualTitle = dashboard.getHeaderTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Dashboard title mismatch");
    }

    @Test(dataProvider = "invalidLogin", dataProviderClass = TestData.class,
            description = "Niepoprawne logowanie")
    @Epic("Authentication")
    @Feature("Login")
    @Story("Invalid login")
    @Severity(SeverityLevel.NORMAL)
    public void invalidLoginTest(String username, String password, String expectedError) {
        LoginPage loginPage = loginPage();
        loginPage.login(username, password);
        loginPage.clickLogin();
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, expectedError, "Error message mismatch");
    }
}
