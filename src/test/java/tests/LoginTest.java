package tests;

import com.selj.pages.DashboardPage;
import com.selj.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Poprawne logowanie użytkownika")
    @Epic("Authentication")
    @Feature("Login")
    @Story("Valid login")
    @Severity(SeverityLevel.CRITICAL)
    public void validLoginTest() {
        loginPage.login("admin", "admin123");
        DashboardPage dashboard = loginPage.submitLoginAndGetDashboard();

        Assert.assertEquals(dashboard.getHeaderTitle(), "Dashboard");
    }

    @Test
    public void invalidLoginTest() {
        loginPage.login("wrong", "wrong");
        loginPage.clickLogin();

        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials");
    }
}
