package com.selj.di;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.selj.config.ConfigReader;
import com.selj.config.DriverManager;
import com.selj.pages.BasePage;
import com.selj.pages.DashboardPage;
import com.selj.pages.LoginPage;
import com.selj.utils.ElementActions;
import com.selj.utils.WaitUtils;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Guice DI Module - centralizuje wszystko bindowanie.
 * ThreadLocal WebDriver zarządzany przez ThreadLocalWebDriverFactory.
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

    private void loadDefaults() {
        properties.setProperty("browser", "chrome");
        properties.setProperty("headless", "true");
        properties.setProperty("windowSize", "1920,1080");
        properties.setProperty("baseUrl", "http://localhost:8080");
        properties.setProperty("timeout", "10");
    }

    @Override
    protected void configure() {
        // 1. Bind properties as @Named
        bindProperties();

        // 2. Bind ConfigReader (backward compatibility)
        bind(ConfigReader.class).toInstance(new ConfigReader());

        // 3. Bind WebDriverFactory as eager singleton - manages ThreadLocal
        bind(WebDriverFactory.class).to(ThreadLocalWebDriverFactory.class).asEagerSingleton();

        // 4. Bind WebDriver to factory provider - uses ThreadLocal inside
        bind(WebDriver.class).toProvider(WebDriverProviderImpl.class).in(Scopes.SINGLETON);

        // 5. Bind DriverManager facade
        bind(DriverManager.class).in(Scopes.SINGLETON);

        // 6. Bind utility classes
        bind(WaitUtils.class).in(Scopes.SINGLETON);
        bind(ElementActions.class).in(Scopes.SINGLETON);

        // 7. Bind page objects (not singletons - fresh state per request)
        bind(LoginPage.class);
        bind(DashboardPage.class);
        bind(BasePage.class);
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