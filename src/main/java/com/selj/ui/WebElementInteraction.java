package com.selj.ui;

import com.google.inject.Inject;
import org.openqa.selenium.WebElement;

public class WebElementInteraction {

    private final WebElementWait webElementWait;

    @Inject
    public WebElementInteraction(WebElementWait webElementWait) {
        this.webElementWait = webElementWait;
    }

    public void click(WebElement element) {
        WebElement clickableElement = webElementWait.waitForClickable(element);
        clickableElement.click();
    }

    public void type(WebElement element, String text) {
        WebElement visibleElement = webElementWait.waitForVisible(element);
        visibleElement.clear();
        visibleElement.sendKeys(text);
    }

    public String getText(WebElement element) {
        WebElement visibleElement = webElementWait.waitForVisible(element);
        return visibleElement.getText();
    }
}
