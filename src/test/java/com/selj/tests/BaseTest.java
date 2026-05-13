package com.selj.tests;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.selj.core.WebDriverFactory;
import com.selj.core.AppModule;
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
    protected WebDriverFactory webDriverFactory;

    @Inject
    @Named("baseUrl")
    protected String baseUrl;

    @Inject
    protected Provider<LoginPage> loginPageProvider;

    @Inject
    protected WebDriver webDriver;

    /**
     * Setup test - navigate to base URL before each test.
     */
    @BeforeMethod
    public void setUp() {
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
    public byte[] takeScreenshot(String testName) {
        try {
            if (webDriver != null) {
                return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
            }
        } catch (Exception e) {
            // Failed to capture screenshot - continue with test
        }
        return new byte[0];
    }

    /**
     * Get fresh LoginPage instance.
     */
    protected LoginPage loginPage() {
        return loginPageProvider.get();
    }
}
