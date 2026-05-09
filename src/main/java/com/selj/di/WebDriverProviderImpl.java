package com.selj.di;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openqa.selenium.WebDriver;

/**
 * Provider implementation dla WebDriver.
 * Pobiera driver z ThreadLocalWebDriverFactory (ThreadLocal).
 */
public class WebDriverProviderImpl implements Provider<WebDriver> {

    private final WebDriverFactory webDriverFactory;

    @Inject
    public WebDriverProviderImpl(WebDriverFactory webDriverFactory) {
        this.webDriverFactory = webDriverFactory;
    }

    @Override
    public WebDriver get() {
        return webDriverFactory.create();
    }
}
