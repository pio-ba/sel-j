package com.selj.pages;

import com.google.inject.Inject;
import com.selj.actions.WebElementInteraction;
import com.selj.core.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BasketPage extends BasePage {

    private static final String REMOVE_FROM_CART = "Remove from cart";
    private static final By CART_BUTTONS = By.xpath(
            "//button[normalize-space()='Add to cart' or normalize-space()='Remove from cart']");
    private static final By BASKET_COUNT = By.cssSelector("span.bg-qa-clr");

    @Inject
    public BasketPage(WebDriverFactory webDriverFactory, WebElementInteraction actions) {
        super(webDriverFactory, actions);
    }

    public BasketPage addToBasket(int productIndex) {
        setBasketState(productIndex, true);
        return this;
    }

    public void removeFromBasket(int productIndex) {
        setBasketState(productIndex, false);
    }

    public boolean isInBasket(int productIndex) {
        return REMOVE_FROM_CART.equals(getCartButton(productIndex).getText().trim());
    }

    public int getBasketCount() {
        List<WebElement> count = getWebDriverFactory().create()
                .findElements(BASKET_COUNT);
        return count.isEmpty() ? 0 : Integer.parseInt(count.getFirst().getText().trim());
    }

    public List<String> getBasketProductNames() {
        List<String> names = new ArrayList<>();
        List<WebElement> cards = getProductCards();
        List<WebElement> buttons = getCartButtons();
        for (int i = 0; i < buttons.size(); i++) {
            if (REMOVE_FROM_CART.equals(buttons.get(i).getText().trim())) {
                names.add(cards.get(i).getText().trim().split("\\R", 2)[0].trim());
            }
        }
        return names;
    }

    public String getProductName(int productIndex) {
        return getProductCards().get(productIndex).getText().trim()
                .split("\\R", 2)[0].trim();
    }

    public BasketPage reload() {
        getWebDriverFactory().create().navigate().refresh();
        new WebDriverWait(getWebDriverFactory().create(), Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(CART_BUTTONS));
        return this;
    }

    private void setBasketState(int productIndex, boolean inBasket) {
        WebElement button = getCartButton(productIndex);
        boolean currentlyInBasket = REMOVE_FROM_CART.equals(button.getText().trim());
        if (currentlyInBasket == inBasket) {
            return;
        }

        int expectedCount = getBasketCount() + (inBasket ? 1 : -1);
        getActions().click(button);
        new WebDriverWait(getWebDriverFactory().create(), Duration.ofSeconds(10)).until(_ ->
                REMOVE_FROM_CART.equals(getCartButton(productIndex).getText().trim()) == inBasket
                        && getBasketCount() == expectedCount);
    }

    private WebElement getCartButton(int productIndex) {
        List<WebElement> buttons = getCartButtons();
        if (productIndex < 0 || productIndex >= buttons.size()) {
            throw new IllegalArgumentException("Product index out of range: " + productIndex);
        }
        return buttons.get(productIndex);
    }

    private List<WebElement> getCartButtons() {
        return getWebDriverFactory().create().findElements(CART_BUTTONS);
    }

    private List<WebElement> getProductCards() {
        List<WebElement> cards = new ArrayList<>();
        for (WebElement cartButton : getCartButtons()) {
            cards.add(cartButton.findElement(By.xpath("../..")));
        }
        return cards;
    }
}
