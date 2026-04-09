package com.selj.pages;

import com.selj.config.DriverManager;
import com.selj.utils.ElementActions;
import com.selj.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage {

        protected WebDriver driver;
        protected ElementActions actions;
        protected WaitUtils wait;

        public BasePage() {
            this.driver = DriverManager.getDriver();
            PageFactory.initElements(driver, this);
        }


}
