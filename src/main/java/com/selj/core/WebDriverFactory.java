package com.selj.core;

import org.openqa.selenium.WebDriver;

/**
 * Factory interface for creating WebDriver instances.
 * Enables dependency inversion and easier testing.
 * Manages driver lifecycle and navigation.
 */
public interface WebDriverFactory {

    /**
     * Create or retrieve existing driver instance for current thread.
     */
    WebDriver create();

    /**
     * Clean up driver resources for current thread.
     */
    void cleanup(WebDriver driver);

    /**
     * Quit driver and clean up ThreadLocal.
     */
    default void quit() {
        cleanup(create());
    }

    /**
     * Navigate to given URL.
     */
    default void navigateTo(String url) {
        if (url != null && !url.isBlank()) {
            create().get(url);
        }
    }
}