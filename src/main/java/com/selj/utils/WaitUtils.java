package com.selj.utils;

import com.selj.config.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private static final long TIMEOUT = 10;

    private static WebDriverWait getWait() {
        WebDriver driver = DriverManager.getDriver();
        return new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }

    public static WebElement waitForVisible(WebElement element) {
        return getWait().until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForClickable(WebElement element) {
        return getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitForUrlContains(String fragment) {
        getWait().until(ExpectedConditions.urlContains(fragment));
    }

    public static void waitForTitle(String title) {
        getWait().until(ExpectedConditions.titleIs(title));
    }
}
