package com.selj.driver;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.selj.di.WebDriverFactory;
import org.openqa.selenium.WebDriver;

public class WebDriverFacade {

    private final WebDriver webDriver;
    private final WebDriverFactory webDriverFactory;
    private final String baseUrl;

    @Inject
    public WebDriverFacade(WebDriver webDriver, WebDriverFactory webDriverFactory,
                          @Named("baseUrl") String baseUrl) {
        this.webDriver = webDriver;
        this.webDriverFactory = webDriverFactory;
        this.baseUrl = baseUrl;
    }

    public WebDriver getDriver() {
        return webDriver;
    }

    public void navigateTo(String url) {
        webDriver.get(url);
    }

    public void navigateToBaseUrl() {
        if (baseUrl != null && !baseUrl.isBlank()) {
            navigateTo(baseUrl);
        }
    }

    public void quitDriver() {
        webDriverFactory.cleanup(webDriver);
    }
}
