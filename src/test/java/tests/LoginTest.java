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
        LoginPage loginPage = new LoginPage();
        DashboardPage dashboard = loginPage.login("admin", "admin123");

        Assert.assertEquals(dashboard.getHeaderTitle(), "Dashboard");
    }

    @Test
    public void invalidLoginTest() {
        LoginPage loginPage = new LoginPage();
        loginPage.login("wrong", "wrong");

        Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials");
    }
}
