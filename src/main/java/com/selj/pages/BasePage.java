package com.selj.pages;

import com.google.inject.Inject;
import com.selj.config.DriverManager;
import com.selj.utils.ElementActions;
import com.selj.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage {

    protected WebDriver driver;
    protected ElementActions actions;
    protected WaitUtils wait;

    @Inject
    public BasePage(DriverManager driverManager, ElementActions actions, WaitUtils wait) {
        this.driver = driverManager.getDriver();
        this.actions = actions;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }


}
