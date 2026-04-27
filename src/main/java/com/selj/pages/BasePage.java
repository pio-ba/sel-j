package com.selj.pages;

import com.google.inject.Inject;
import com.selj.config.ConfigReader;
import com.selj.utils.ElementActions;
import com.selj.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage {

    protected WebDriver driver;
    protected ElementActions actions;
    protected WaitUtils wait;
    @Inject
    protected ConfigReader configReader;

    @Inject
    public BasePage(WebDriver driver, ElementActions actions, WaitUtils wait) {
        this.driver = driver;
        this.actions = actions;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }


}
