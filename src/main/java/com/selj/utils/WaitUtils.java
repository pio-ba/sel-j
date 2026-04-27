package com.selj.utils;

import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private static final long TIMEOUT = 10;
    private final WebDriver webDriver;

    @Inject
    public WaitUtils(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    private WebDriverWait getWait() {
        return new WebDriverWait(webDriver, Duration.ofSeconds(TIMEOUT));
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
