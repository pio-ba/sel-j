package com.selj.pages;

import com.google.inject.Inject;
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

public class ProductsPage extends BasePage {

    private static final String SORT_BUTTON_XPATH =
            "//button[normalize-space()='Select...' "
                    + "or normalize-space()='A to Z (Ascending)' "
                    + "or normalize-space()='Z to A (Descending)' "
                    + "or normalize-space()='Low to High (Price)' "
                    + "or normalize-space()='High to Low (Price)']";
    private static final Pattern PRICE_PATTERN =
            Pattern.compile("\\$\\s*(\\d+(?:\\.\\d{1,2})?)");
    private static final String LOW_TO_HIGH = "Low to High (Price)";
    private static final String HIGH_TO_LOW = "High to Low (Price)";

    @FindBy(id = "product-sort")
    private WebElement pageTitle;

    @FindBy(css = "body")
    private WebElement body;

    @Inject
    public ProductsPage(WebDriverFactory webDriverFactory,
                        WebElementInteraction actions) {
        super(webDriverFactory, actions);
    }

    public String getPageTitle() {
        return getActions().getText(pageTitle);
    }

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

    public List<BigDecimal> getProductPrices() {
        List<BigDecimal> prices = new ArrayList<>();
        for (WebElement cartButton : getCartButtons()) {
            Matcher matcher = PRICE_PATTERN.matcher(cartButton.findElement(By.xpath("..")).getText());
            if (matcher.find()) {
                prices.add(new BigDecimal(matcher.group(1)));
            }
        }
        return prices;
    }

    public ProductsPage sortBy(String optionText) {
        WebDriver driver = getWebDriverFactory().create();
        getActions().click(findSortButton(driver));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String dataValue = LOW_TO_HIGH.equals(optionText) ? "low"
                : HIGH_TO_LOW.equals(optionText) ? "high" : null;
        By optionLocator = dataValue != null
                ? By.cssSelector("[role='option'][data-value='" + dataValue + "']")
                : By.xpath("//*[@role='option'][normalize-space()='" + optionText + "']");
        getActions().click(wait.until(ExpectedConditions.elementToBeClickable(optionLocator)));

        BigDecimal expectedFirstPrice = LOW_TO_HIGH.equals(optionText)
                ? new BigDecimal("45.00")
                : HIGH_TO_LOW.equals(optionText) ? new BigDecimal("256.45") : null;
        if (expectedFirstPrice != null) {
            BigDecimal expectedPrice = expectedFirstPrice;
            wait.until(_ -> {
                List<BigDecimal> prices = getProductPrices();
                return !prices.isEmpty()
                        && expectedPrice.compareTo(prices.getFirst()) == 0;
            });
        } else {
            wait.until(driverInstance ->
                    !findSortButton(driverInstance).getText().trim().equals("Select..."));
        }
        return this;
    }

    public List<String> getSortOptions() {
        WebDriver driver = getWebDriverFactory().create();
        getActions().click(findSortButton(driver));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> options = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("[role='option']")));
        List<String> texts = new ArrayList<>();
        for (WebElement option : options) {
            texts.add(option.getText().trim());
        }
        findSortButton(driver).sendKeys(Keys.ESCAPE);
        return texts;
    }

    public ProductsPage reload() {
        getWebDriverFactory().create().navigate().refresh();
        new WebDriverWait(getWebDriverFactory().create(), Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//button[normalize-space()='Add to cart' "
                                + "or normalize-space()='Remove from cart']")));
        return this;
    }

    public String getVisibleText() {
        return getActions().getText(body);
    }

    private List<WebElement> getCartButtons() {
        return getWebDriverFactory().create().findElements(By.xpath(
                "//button[normalize-space()='Add to cart' "
                        + "or normalize-space()='Remove from cart']"));
    }

    private List<WebElement> getProductCards() {
        List<WebElement> cards = new ArrayList<>();
        for (WebElement cartButton : getCartButtons()) {
            cards.add(cartButton.findElement(By.xpath("../..")));
        }
        return cards;
    }

    private WebElement findSortButton(WebDriver driver) {
        return driver.findElement(By.xpath(SORT_BUTTON_XPATH));
    }
}
