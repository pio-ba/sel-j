package com.selj.pages;

import com.google.inject.Inject;
import com.selj.actions.WebElementInteraction;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Base class for all Page Objects.
 * Provides access to WebDriver and element interaction utilities.
 */
public class BasePage {

    protected WebDriver driver;
    protected WebElementInteraction actions;

    @Inject
    public BasePage(WebDriver driver, WebElementInteraction actions) {
        this.driver = driver;
        this.actions = actions;
        PageFactory.initElements(driver, this);
    }
}
