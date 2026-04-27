package com.selj.config;

import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;

public class DriverManager {

    private final WebDriver webDriver;

    @Inject
    public DriverManager(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void initDriver() {
        // WebDriver już zarządzany przez Guice z ThreadLocal wewnątrz WebDriverProvider
    }

    public WebDriver getDriver() {
        return webDriver;
    }

    public void quitDriver() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

}
