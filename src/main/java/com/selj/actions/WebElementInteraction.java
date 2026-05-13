package com.selj.actions;

import com.google.inject.Inject;
import org.openqa.selenium.WebElement;

/**
 * High-level element interactions with automatic waits.
 * All methods automatically wait for element visibility/clickability before interaction.
 */
public class WebElementInteraction {

    private final WebElementWait webElementWait;

    @Inject
    public WebElementInteraction(WebElementWait webElementWait) {
        this.webElementWait = webElementWait;
    }

    /**
     * Click on element - waits for clickability first.
     */
    public void click(WebElement element) {
        WebElement clickableElement = webElementWait.waitForClickable(element);
        clickableElement.click();
    }

    /**
     * Type text into element - waits for visibility, clears, then types.
     */
    public void type(WebElement element, String text) {
        WebElement visibleElement = webElementWait.waitForVisible(element);
        visibleElement.clear();
        visibleElement.sendKeys(text);
    }

    /**
     * Get text from element - waits for visibility first.
     */
    public String getText(WebElement element) {
        WebElement visibleElement = webElementWait.waitForVisible(element);
        return visibleElement.getText();
    }
}
