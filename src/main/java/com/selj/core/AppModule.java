package com.selj.core;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.selj.pages.DashboardPage;
import com.selj.pages.LoginPage;
import com.selj.actions.WebElementInteraction;
import com.selj.actions.WebElementWait;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Guice DI Module - centralizuje wszystko bindowanie.
 * ThreadLocal WebDriver zarzadzany przez ThreadLocalWebDriverFactory.
 */
public class AppModule extends AbstractModule {

    private final Properties properties = new Properties();

    public AppModule() {
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = AppModule.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                loadDefaults();
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    /**
     * Load default properties - used when config.properties is not found.
     */
    private void loadDefaults() {
        properties.setProperty("browser", "chrome");
        properties.setProperty("headless", "true");
        properties.setProperty("windowSize", "1920,1080");
        properties.setProperty("baseUrl", "http://localhost:8080");
        properties.setProperty("timeout", "10");
    }

    /**
     * Configure all dependency bindings.
     * Binds properties, WebDriver factory, utilities, and page objects.
     */
    @Override
    protected void configure() {
        // 1. Bind properties as @Named
        bindProperties();

        // 2. Bind WebDriverFactory as eager singleton - manages ThreadLocal
        bind(WebDriverFactory.class)
                .to(ThreadLocalWebDriverFactory.class)
                .asEagerSingleton();

        // 3. Bind WebDriver directly - ThreadLocalWebDriverFactory supplies it
        bind(org.openqa.selenium.WebDriver.class)
                .toProvider(ThreadLocalWebDriverFactory.class)
                .in(Scopes.SINGLETON);

        // 4. Bind utility classes
        bind(WebElementWait.class).in(Scopes.SINGLETON);
        bind(WebElementInteraction.class).in(Scopes.SINGLETON);

        // 5. Bind page objects (not singletons - fresh state per request)
        bind(LoginPage.class);
        bind(DashboardPage.class);
    }

    private void bindProperties() {
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            bind(String.class)
                    .annotatedWith(Names.named(key))
                    .toInstance(value);
        }
    }
}