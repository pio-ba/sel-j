package com.selj.di;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * ThreadLocal-based WebDriver factory implementation.
 * Each thread gets its own driver instance - thread-safe for parallel tests.
 */
public class ThreadLocalWebDriverFactory implements WebDriverFactory, Provider<WebDriver> {

    private static final String DEFAULT_BROWSER = "chrome";

    private final ThreadLocal<WebDriver> driverHolder = ThreadLocal.withInitial(this::createDriver);
    private final String browser;
    private final String headless;
    private final String windowSize;

    @Inject
    public ThreadLocalWebDriverFactory(
            @Named("browser") String browser,
            @Named("headless") String headless,
            @Named("windowSize") String windowSize) {
        this.browser = (browser == null || browser.isBlank()) ? DEFAULT_BROWSER : browser.toLowerCase().trim();
        this.headless = (headless == null || headless.isBlank()) ? "true" : headless.trim();
        this.windowSize = (windowSize == null || windowSize.isBlank()) ? "1920,1080" : windowSize.trim();
    }

    @Override
    public WebDriver create() {
        return driverHolder.get();
    }

    @Override
    public WebDriver get() {
        return driverHolder.get();
    }

    @Override
    public void cleanup(WebDriver driver) {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (WebDriverException e) {
            System.err.println("Warning: Error quitting driver: " + e.getMessage());
        } finally {
            driverHolder.remove();
        }
    }

    private WebDriver createDriver() {
        return switch (browser) {
            case "chrome" -> createChrome();
            case "firefox" -> createFirefox();
            case "edge" -> createEdge();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    private ChromeDriver createChrome() {
        ChromeOptions options = new ChromeOptions();
        applyHeadlessAndWindow(options);
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        return new ChromeDriver(options);
    }

    private FirefoxDriver createFirefox() {
        FirefoxOptions options = new FirefoxOptions();
        boolean isHeadless = Boolean.parseBoolean(headless);
        if (isHeadless) {
            options.addArguments("--headless");
        }
        String[] dimensions = windowSize.split(",");
        options.addArguments("--width=" + dimensions[0]);
        options.addArguments("--height=" + dimensions[1]);
        return new FirefoxDriver(options);
    }

    private EdgeDriver createEdge() {
        EdgeOptions options = new EdgeOptions();
        applyHeadlessAndWindow(options);
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        return new EdgeDriver(options);
    }

    private void applyHeadlessAndWindow(ChromeOptions options) {
        boolean isHeadless = Boolean.parseBoolean(headless);
        if (isHeadless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=" + windowSize);
    }

    private void applyHeadlessAndWindow(EdgeOptions options) {
        boolean isHeadless = Boolean.parseBoolean(headless);
        if (isHeadless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=" + windowSize);
    }
}