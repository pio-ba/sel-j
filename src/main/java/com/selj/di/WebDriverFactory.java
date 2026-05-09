package com.selj.di;

import org.openqa.selenium.WebDriver;

/**
 * Factory interface for creating WebDriver instances.
 * Enables dependency inversion and easier testing.
 */
public interface WebDriverFactory {
    WebDriver create();
    void cleanup(WebDriver driver);
}