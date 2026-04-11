package com.selj.utils;

import com.google.inject.Inject;
import com.selj.config.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private static final long TIMEOUT = 10;

    private final DriverManager driverManager;

    @Inject
    public WaitUtils(DriverManager driverManager) {
        this.driverManager = driverManager;
    }

    private WebDriverWait getWait() {
        WebDriver driver = driverManager.getDriver();
        return new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }

    public WebElement waitForVisible(WebElement element) {
        return getWait().until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForClickable(WebElement element) {
        return getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForUrlContains(String fragment) {
        getWait().until(ExpectedConditions.urlContains(fragment));
    }

    public void waitForTitle(String title) {
        getWait().until(ExpectedConditions.titleIs(title));
    }
}
