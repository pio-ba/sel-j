package com.selj.tests;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.selj.core.WebDriverFactory;
import com.selj.pages.LoginPage;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;

@Guice(modules = com.selj.core.AppModule.class)
public class BaseTest {

    @Inject
    protected WebDriverFactory webDriverFactory;

    @Inject
    @Named("baseUrl")
    protected String baseUrl;

    @Inject
    protected Provider<LoginPage> loginPageProvider;

    /**
     * Setup test - initialize WebDriver explicitly, then navigate to base URL.
     * Prevents multiple browser instances by eagerly creating driver before test.
     */
    @BeforeMethod
    public void setUp() {
        webDriverFactory.create();
        webDriverFactory.navigateTo(baseUrl);
    }

    /**
     * Teardown test - capture screenshot on failure and quit driver.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(result.getMethod().getMethodName());
        }
        webDriverFactory.quit();
    }

    /**
     * Take screenshot on test failure - attached to Allure report.
     */
    @Attachment(value = "Screenshot on Failure", type = "image/png")
    public void takeScreenshot(String testName) {
        try {
            WebDriver driver = webDriverFactory.create();
            if (driver != null) {
                ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            }
        } catch (Exception _) {
            // Failed to capture screenshot - continue with test
        }
    }

    /**
     * Get fresh LoginPage instance.
     */
    protected LoginPage loginPage() {
        return loginPageProvider.get();
    }
}
