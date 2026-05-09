package com.selj.utils;

import com.google.inject.Inject;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

public class ElementActions {

    private final WaitUtils waitUtils;

    @Inject
    public ElementActions(WaitUtils waitUtils) {
        this.waitUtils = waitUtils;
    }

    public void click(WebElement element) {
        try {
            WebElement clickableElement = waitUtils.waitForClickable(element);
            clickableElement.click();
        } catch (StaleElementReferenceException e) {
            throw e;
        } catch (TimeoutException e) {
            throw e;
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    public void type(WebElement element, String text) {
        try {
            WebElement visibleElement = waitUtils.waitForVisible(element);
            visibleElement.clear();
            visibleElement.sendKeys(text);
        } catch (StaleElementReferenceException e) {
            throw e;
        } catch (TimeoutException e) {
            throw e;
        }
    }

    public String getText(WebElement element) {
        try {
            WebElement visibleElement = waitUtils.waitForVisible(element);
            return visibleElement.getText();
        } catch (TimeoutException e) {
            throw e;
        }
    }
}
