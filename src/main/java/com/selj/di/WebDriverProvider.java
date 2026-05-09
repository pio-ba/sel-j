package com.selj.di;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

/**
 * Provider WebDrivera z wewnętrznym ThreadLocal.
 * Każdy wątek (test) otrzymuje własną instancję drivera.
 * Inicjalizacja jest leniwa (lazy) — driver tworzy się przy pierwszym get().
 */
public class WebDriverProvider implements Provider<WebDriver> {

    private final ThreadLocal<WebDriver> driverHolder = ThreadLocal.withInitial(this::initDriver);
    private final String browser;

    @Inject
    public WebDriverProvider(@Named("browser") String browser) {
        this.browser = browser;
    }

    @Override
    public WebDriver get() {
        return driverHolder.get();
    }

    private WebDriver initDriver() {
        String browserName = (browser == null || browser.isBlank()) ? "chrome" : browser.toLowerCase().trim();

        return switch (browserName) {
            case "chrome" -> createChromeDriver();
            case "firefox" -> createFirefoxDriver();
            case "edge" -> createEdgeDriver();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        };
    }

    private ChromeDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        return new ChromeDriver(options);
    }

    private FirefoxDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        return new FirefoxDriver(options);
    }

    private EdgeDriver createEdgeDriver() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        return new EdgeDriver(options);
    }

    public void cleanup() {
        WebDriver driver = driverHolder.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {
            }
            driverHolder.remove();
        }
    }

    public boolean hasDriver() {
        return driverHolder.get() != null;
    }
}