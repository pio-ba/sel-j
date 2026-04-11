package com.selj.config;

import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private final WebDriver webDriver;

    @Inject
    public DriverManager(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void initDriver() {
        driver.set(webDriver);
    }

    public WebDriver getDriver() {
        return driver.get();
    }

    public void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }


}
