package com.selj.pages;

import com.google.inject.Inject;
import com.selj.actions.WebElementInteraction;
import com.selj.core.WebDriverFactory;
import lombok.AccessLevel;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Base class for all Page Objects.
 * Provides access to element interaction utilities.
 */
@Getter(AccessLevel.PROTECTED)
public abstract class BasePage {

    protected final WebElementInteraction actions;
    protected final WebDriverFactory webDriverFactory;

    @Inject
    protected BasePage(WebDriverFactory webDriverFactory, WebElementInteraction actions) {
        this.webDriverFactory = webDriverFactory;
        this.actions = actions;
        WebDriver driver = webDriverFactory.create();
        PageFactory.initElements(driver, this);
    }
}
