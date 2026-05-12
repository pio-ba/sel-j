package tests;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.selj.driver.WebDriverFacade;
import com.selj.di.AppModule;
import com.selj.pages.DashboardPage;
import com.selj.pages.LoginPage;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;

@Guice(modules = AppModule.class)
public class BaseTest {

    @Inject
    protected WebDriverFacade webDriverFacade;

    @Inject
    protected Provider<LoginPage> loginPageProvider;

    @Inject
    protected Provider<DashboardPage> dashboardPageProvider;

    @Inject
    protected WebDriver webDriver;

    @BeforeMethod
    public void setUp() {
        webDriverFacade.navigateToBaseUrl();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(result.getMethod().getMethodName());
        }
        webDriverFacade.quitDriver();
    }

    @Attachment(value = "Screenshot on Failure", type = "image/png")
    public byte[] takeScreenshot(String testName) {
        try {
            if (webDriver != null) {
                return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
            }
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
        return new byte[0];
    }

    protected LoginPage loginPage() {
        return loginPageProvider.get();
    }

    protected DashboardPage dashboardPage() {
        return dashboardPageProvider.get();
    }
}
