package com.selj.utils;

import com.google.inject.Inject;
import org.openqa.selenium.WebElement;

public class ElementActions {

    private final WaitUtils waitUtils;

    @Inject
    public ElementActions(WaitUtils waitUtils) {
        this.waitUtils = waitUtils;
    }

    public void click(WebElement element) {
        waitUtils.waitForClickable(element).click();
    }

    public void type(WebElement element, String text) {
        WebElement el = waitUtils.waitForVisible(element);
        el.clear();
        el.sendKeys(text);
    }

    public String getText(WebElement element) {
        return waitUtils.waitForVisible(element).getText();
    }
}
