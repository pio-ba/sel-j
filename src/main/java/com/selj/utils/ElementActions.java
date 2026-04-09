package com.selj.utils;

import org.openqa.selenium.WebElement;

public class ElementActions {

    public static void click(WebElement element) {
        WaitUtils.waitForClickable(element).click();
    }

    public static void type(WebElement element, String text) {
        WebElement el = WaitUtils.waitForVisible(element);
        el.clear();
        el.sendKeys(text);
    }

    public static String getText(WebElement element) {
        return WaitUtils.waitForVisible(element).getText();
    }
}
