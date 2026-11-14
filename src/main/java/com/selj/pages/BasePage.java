package com.selj.pages;

import com.google.inject.Inject;
import com.selj.actions.WebElementInteraction;
import com.selj.core.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Base class for all Page Objects.
 * Provides access to element interaction utilities.
 */
public abstract class BasePage {

    protected final WebElementInteraction actions;

    @Inject
    public BasePage(WebDriverFactory webDriverFactory, WebElementInteraction actions) {
        this.actions = actions;
        WebDriver driver = webDriverFactory.create();
        PageFactory.initElements(driver, this);
    }
}
