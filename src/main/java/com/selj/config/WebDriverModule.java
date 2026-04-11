package com.selj.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverModule extends AbstractModule {

    @Provides
    @Singleton
    public WebDriver provideWebDriver() {
        return new ChromeDriver();
    }

    @Provides
    @Singleton
    public ConfigReader provideConfigReader() {
        return new ConfigReader();
    }

}

