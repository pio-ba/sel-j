package com.selj.pages;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.selj.actions.WebElementInteraction;
import com.selj.core.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Page Object for Products page.
 * Provides methods for products interactions.
 */
public class ProductsPage extends BasePage {

    @FindBy(id = "product-sort")
    private WebElement pageTitle;

    @FindBy(css = "body")
    private WebElement body;

    private static final String SORT_BUTTON_XPATH = "//button[normalize-space()='Select...' or normalize-space()='A to Z (Ascending)' or normalize-space()='Z to A (Descending)' or normalize-space()='Low to High (Price)' or normalize-space()='High to Low (Price)']";
    private static final Pattern PRICE_PATTERN = Pattern.compile("\\$\\s*(\\d+(?:\\.\\d{1,2})?)");
    private static final String LOW_TO_HIGH = "Low to High (Price)";
    private static final String HIGH_TO_LOW = "High to Low (Price)";
    private static final String REMOVE_FROM_CART = "Remove from cart";

    private final Provider<LoginPage> loginPageProvider;
    private final String baseUrl;

    @Inject
    public ProductsPage(WebDriverFactory webDriverFactory, WebElementInteraction actions,
                        Provider<LoginPage> loginPageProvider, @Named("baseUrl") String baseUrl) {
        super(webDriverFactory, actions);
        this.loginPageProvider = loginPageProvider;
        this.baseUrl = baseUrl;
    }

    /**
     * Get page title text.
     */
    public String getPageTitle() {
        return getActions().getText(pageTitle);
    }

    /**
     * Return names of products in their current visible order.
     */
    public List<String> getProductNames() {
        List<String> names = new ArrayList<>();
        for (WebElement card : getProductCards()) {
            String text = card.getText().trim();
            if (!text.isBlank()) {
                names.add(text.split("\\R", 2)[0].trim());
            }
        }
        return names;
    }

    /**
     * Return visible product prices in their current order.
     */
    public List<BigDecimal> getProductPrices() {
        List<BigDecimal> prices = new ArrayList<>();
        for (WebElement cartButton : getProductCartButtons()) {
            WebElement priceRow = cartButton.findElement(By.xpath(".."));
            Matcher matcher = PRICE_PATTERN.matcher(priceRow.getText());
            if (matcher.find()) {
                prices.add(new BigDecimal(matcher.group(1)));
            }
        }
        return prices;
    }

    /**
     * Open sorting menu and select one of the supported options.
     */
    public ProductsPage sortBy(String optionText) {
        WebDriver driver = getWebDriverFactory().create();
        WebElement sortButton = findSortButton(driver);
        getActions().click(sortButton);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String dataValue = null;
        if (LOW_TO_HIGH.equals(optionText)) {
            dataValue = "low";
        } else if (HIGH_TO_LOW.equals(optionText)) {
            dataValue = "high";
        }
        By optionLocator = dataValue != null
                ? By.cssSelector("[role='option'][data-value='" + dataValue + "']")
                : By.xpath("//*[@role='option'][normalize-space()='" + optionText + "']");
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
        getActions().click(option);

        BigDecimal expectedFirstPrice = null;
        if (LOW_TO_HIGH.equals(optionText)) {
            expectedFirstPrice = new BigDecimal("45.00");
        } else if (HIGH_TO_LOW.equals(optionText)) {
            expectedFirstPrice = new BigDecimal("256.45");
        }
        if (expectedFirstPrice != null) {
            BigDecimal expectedPrice = expectedFirstPrice;
            wait.until(_ -> {
                List<BigDecimal> prices = getProductPrices();
                return !prices.isEmpty() && expectedPrice.compareTo(prices.getFirst()) == 0;
            });
        } else {
            wait.until(driverInstance -> !findSortButton(driverInstance).getText().trim().equals("Select..."));
        }
        return this;
    }

    /**
     * Return options visible in the sorting menu.
     */
    public List<String> getSortOptions() {
        WebDriver driver = getWebDriverFactory().create();
        getActions().click(findSortButton(driver));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("[role='option']")));
        List<String> texts = new ArrayList<>();
        for (WebElement option : options) {
            texts.add(option.getText().trim());
        }
        findSortButton(driver).sendKeys(Keys.ESCAPE);
        return texts;
    }

    /**
     * Add or remove the first product from favorites.
     */
    public void toggleFavorite(int productIndex) {
        if (productIndex < 0) {
            throw new IllegalArgumentException("Product index cannot be negative");
        }
        List<WebElement> favorites = getFavoriteButtons();
        if (productIndex >= favorites.size()) {
            throw new IllegalArgumentException("Product index out of range: " + productIndex);
        }
        boolean wasFavorite = isFavorite(productIndex);
        getActions().click(favorites.get(productIndex));

        WebDriverWait wait = new WebDriverWait(getWebDriverFactory().create(), Duration.ofSeconds(5));
        wait.until(_ -> isFavorite(productIndex) != wasFavorite);
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
                return path.startsWith("M256 448") || path.startsWith("M349.6") && !path.startsWith("M349.6 64c-36.4 0-70.7 16.7-93.6 43.9C233.1 80.7");
            }
        }
        String color = favoriteButton.getCssValue("color").replace(" ", "");
        String style = favoriteButton.getAttribute("style");
        return color.contains("255,0,0")
                || (style != null && style.contains("255, 0, 0"));
    }

    public String getFavoriteProductName(int productIndex) {
        WebElement card = getProductCards().get(productIndex);
        return card.getText().trim().split("\\R", 2)[0].trim();
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

    public ProductsPage reload() {
        getWebDriverFactory().create().navigate().refresh();
        WebDriverWait wait = new WebDriverWait(getWebDriverFactory().create(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[normalize-space()='Add to cart' or normalize-space()='Remove from cart']")));
        return this;
    }

    public String getVisibleText() {
        return getActions().getText(body);
    }

    public ProductsPage addToBasket(int productIndex) {
        setBasketState(productIndex, true);
        return this;
    }

    public void removeFromBasket(int productIndex) {
        setBasketState(productIndex, false);
    }

    public boolean isInBasket(int productIndex) {
        return REMOVE_FROM_CART.equals(getProductCartButton(productIndex).getText().trim());
    }

    public int getBasketCount() {
        List<WebElement> count = getWebDriverFactory().create()
                .findElements(By.cssSelector("span.bg-qa-clr"));
        return count.isEmpty() ? 0 : Integer.parseInt(count.getFirst().getText().trim());
    }

    public List<String> getBasketProductNames() {
        List<String> names = new ArrayList<>();
        List<WebElement> cards = getProductCards();
        List<WebElement> buttons = getProductCartButtons();
        for (int i = 0; i < buttons.size(); i++) {
            if (REMOVE_FROM_CART.equals(buttons.get(i).getText().trim())) {
                names.add(cards.get(i).getText().trim().split("\\R", 2)[0].trim());
            }
        }
        return names;
    }

    private void setBasketState(int productIndex, boolean inBasket) {
        WebElement button = getProductCartButton(productIndex);
        boolean currentlyInBasket = REMOVE_FROM_CART.equals(button.getText().trim());
        if (currentlyInBasket == inBasket) {
            return;
        }

        int expectedCount = getBasketCount() + (inBasket ? 1 : -1);
        getActions().click(button);
        new WebDriverWait(getWebDriverFactory().create(), Duration.ofSeconds(10)).until(_ -> {
            WebElement updatedButton = getProductCartButton(productIndex);
            return (REMOVE_FROM_CART.equals(updatedButton.getText().trim()) == inBasket)
                    && getBasketCount() == expectedCount;
        });
    }

    private WebElement getProductCartButton(int productIndex) {
        List<WebElement> buttons = getProductCartButtons();
        if (productIndex < 0 || productIndex >= buttons.size()) {
            throw new IllegalArgumentException("Product index out of range: " + productIndex);
        }
        return buttons.get(productIndex);
    }

    private List<WebElement> getProductCartButtons() {
        return getWebDriverFactory().create().findElements(By.xpath(
                "//button[normalize-space()='Add to cart' or normalize-space()='" + REMOVE_FROM_CART + "']"));
    }

    private List<WebElement> getProductCards() {
        List<WebElement> cards = new ArrayList<>();
        for (WebElement cartButton : getProductCartButtons()) {
            cards.add(cartButton.findElement(By.xpath("../..")));
        }
        return cards;
    }

    private List<WebElement> getFavoriteButtons() {
        List<WebElement> favorites = new ArrayList<>();
        for (WebElement card : getProductCards()) {
            favorites.add(card.findElement(By.cssSelector("span.absolute.top-3.right-3.cursor-pointer")));
        }
        return favorites;
    }

    private WebElement findSortButton(WebDriver driver) {
        return driver.findElement(By.xpath(SORT_BUTTON_XPATH));
    }

    /**
     * Click logout button and return to login page.
     */
    public LoginPage logout() {
        WebDriver driver = getWebDriverFactory().create();
        WebElement accountButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(normalize-space(), '@')]")));
        getActions().click(accountButton);

        WebElement logoutOption = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@role='menuitem' or @role='button'][contains(translate(normalize-space(), 'LOGOUT', 'logout'), 'logout') or contains(translate(normalize-space(), 'LOGOUT', 'logout'), 'log out') or contains(normalize-space(), 'Sign out')]")));
        getActions().click(logoutOption);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }
        driver.manage().deleteAllCookies();
        driver.get(baseUrl);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
        return loginPageProvider.get();
    }
}
