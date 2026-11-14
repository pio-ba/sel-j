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
public abstract class BaseTest {

    @Inject
    protected WebDriverFactory webDriverFactory;

    @Inject
    @Named("baseUrl")
    protected String baseUrl;

    @Inject
    protected Provider<LoginPage> loginPageProvider;

    protected WebDriver driver() {
        return webDriverFactory.create();
    }

    /**
     * Setup test - initialize WebDriver explicitly, then navigate to base URL.
     * Prevents multiple browser instances by eagerly creating driver before test.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        webDriverFactory.navigateTo(baseUrl);
    }

    /**
     * Teardown test - capture screenshot on failure and quit driver.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                takeScreenshot(result.getMethod().getMethodName());
            }
        } finally {
            webDriverFactory.quit();
        }
    }

    /**
     * Take screenshot on test failure - attached to Allure report.
     */
    @Attachment(value = "Screenshot on failure", type = "image/png")
    public byte[] takeScreenshot(String testName) {
        try {
            WebDriver driver = driver();
            if (driver != null) {
                return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            }
        } catch (Exception _) {
            // screenshot failure should never break the test suite
        }
        return new byte[0];
    }

    /**
     * Returns the URL of the current browser page.
     *
     * @return the current page URL
     */
    protected String getCurrentUrl() {
        return driver().getCurrentUrl();
    }

    /**
     * Get fresh LoginPage instance.
     */
    protected LoginPage loginPage() {
        return loginPageProvider.get();
    }
}