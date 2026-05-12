package com.selj.ui;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebElementWait {

    private static final long DEFAULT_TIMEOUT = 10L;

    private final WebDriver webDriver;
    private final long timeout;

    @Inject
    public WebElementWait(WebDriver webDriver, @Named("timeout") String timeout) {
        this.webDriver = webDriver;
        this.timeout = parseTimeout(timeout);
    }

    private long parseTimeout(String timeoutStr) {
        if (timeoutStr == null || timeoutStr.isBlank()) {
            return DEFAULT_TIMEOUT;
        }
        try {
            return Long.parseLong(timeoutStr.trim());
        } catch (NumberFormatException e) {
            return DEFAULT_TIMEOUT;
        }
    }

    private WebDriverWait getWait() {
        return new WebDriverWait(webDriver, Duration.ofSeconds(timeout));
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
