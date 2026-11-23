package com.selj.pages;

import com.google.inject.Inject;
import com.selj.actions.WebElementInteraction;
import com.selj.core.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FavoritesPage extends BasePage {

    @Inject
    public FavoritesPage(WebDriverFactory webDriverFactory, WebElementInteraction actions) {
        super(webDriverFactory, actions);
    }

    public void toggleFavorite(int productIndex) {
        List<WebElement> favorites = getFavoriteButtons();
        validateProductIndex(productIndex, favorites.size());
        boolean wasFavorite = isFavorite(productIndex);
        getActions().click(favorites.get(productIndex));

        new WebDriverWait(getWebDriverFactory().create(), Duration.ofSeconds(5))
                .until(_ -> isFavorite(productIndex) != wasFavorite);
    }

    public boolean isFavorite(int productIndex) {
        List<WebElement> favorites = getFavoriteButtons();
        if (productIndex < 0 || productIndex >= favorites.size()) {
            return false;
        }
        WebElement favoriteButton = favorites.get(productIndex).findElement(By.xpath(".//button"));
        List<WebElement> paths = favoriteButton.findElements(By.cssSelector("svg path"));
        if (!paths.isEmpty()) {
            String path = paths.getFirst().getAttribute("d");
            if (path != null) {
                return path.startsWith("M256 448")
                        || path.startsWith("M349.6") && !path.startsWith(
                                "M349.6 64c-36.4 0-70.7 16.7-93.6 43.9C233.1 80.7");
            }
        }
        String color = favoriteButton.getCssValue("color").replace(" ", "");
        String style = favoriteButton.getAttribute("style");
        return color.contains("255,0,0")
                || style != null && style.contains("255, 0, 0");
    }

    public String getFavoriteProductName(int productIndex) {
        return getProductCards().get(productIndex).getText().trim()
                .split("\\R", 2)[0].trim();
    }

    public List<String> getFavoriteProductNames() {
        List<String> names = new ArrayList<>();
        List<WebElement> cards = getProductCards();
        for (int i = 0; i < cards.size(); i++) {
            if (isFavorite(i)) {
                names.add(cards.get(i).getText().trim().split("\\R", 2)[0].trim());
            }
        }
        return names;
    }

    public int getFavoriteCount() {
        int count = 0;
        for (int i = 0; i < getProductCards().size(); i++) {
            if (isFavorite(i)) {
                count++;
            }
        }
        return count;
    }

    public FavoritesPage reload() {
        getWebDriverFactory().create().navigate().refresh();
        new WebDriverWait(getWebDriverFactory().create(), java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//button[normalize-space()='Add to cart' "
                                + "or normalize-space()='Remove from cart']")));
        return this;
    }

    private void validateProductIndex(int productIndex, int productCount) {
        if (productIndex < 0) {
            throw new IllegalArgumentException("Product index cannot be negative");
        }
        if (productIndex >= productCount) {
            throw new IllegalArgumentException("Product index out of range: " + productIndex);
        }
    }

    private List<WebElement> getProductCards() {
        List<WebElement> cards = new ArrayList<>();
        for (WebElement addButton : getAddToCartButtons()) {
            cards.add(addButton.findElement(By.xpath("../..")));
        }
        return cards;
    }

    private List<WebElement> getAddToCartButtons() {
        return getWebDriverFactory().create().findElements(By.xpath(
                "//button[normalize-space()='Add to cart' or normalize-space()='Remove from cart']"));
    }

    private List<WebElement> getFavoriteButtons() {
        List<WebElement> favorites = new ArrayList<>();
        for (WebElement card : getProductCards()) {
            favorites.add(card.findElement(By.cssSelector(
                    "span.absolute.top-3.right-3.cursor-pointer")));
        }
        return favorites;
    }
}
