package com.selj.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.selj.di.WebDriverFactory;
import org.openqa.selenium.WebDriver;

public class DriverManager {

    private final WebDriverFactory webDriverFactory;
    private final String baseUrl;

    @Inject
    public DriverManager(WebDriverFactory webDriverFactory,
                         @Named("baseUrl") String baseUrl) {
        this.webDriverFactory = webDriverFactory;
        this.baseUrl = baseUrl;
    }

    public WebDriver getDriver() {
        return webDriverFactory.create();
    }

    public void initDriver() {
        if (baseUrl != null && !baseUrl.isBlank()) {
            getDriver().get(baseUrl);
        }
    }

    public void quitDriver() {
        webDriverFactory.cleanup(getDriver());
    }
}
