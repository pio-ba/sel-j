package com.selj.pages;

import com.google.inject.Inject;
import com.selj.ui.WebElementInteraction;
import com.selj.ui.WebElementWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage {

    protected WebDriver driver;
    protected WebElementInteraction actions;

    @Inject
    public BasePage(WebDriver driver, WebElementInteraction actions, WebElementWait wait) {
        this.driver = driver;
        this.actions = actions;
        PageFactory.initElements(driver, this);
    }
}
