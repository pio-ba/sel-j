package com.selj.config;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.selj.di.WebDriverProvider;
import com.selj.pages.BasePage;
import com.selj.pages.DashboardPage;
import com.selj.pages.LoginPage;
import com.selj.utils.ElementActions;
import com.selj.utils.WaitUtils;
import org.openqa.selenium.WebDriver;

public class WebDriverModule extends AbstractModule {

    @Override
    protected void configure() {
        // Zbinduj WebDriverProvider jako Singleton (ThreadLocal inside)
        bind(WebDriverProvider.class).asEagerSingleton();

        // Zbinduj WebDriver do providera - ThreadLocal managed
        bind(WebDriver.class).toProvider(WebDriverProvider.class).in(Scopes.SINGLETON);

        // Zbinduj ConfigReader - wczytywany z config.properties
        bind(ConfigReader.class).toInstance(new ConfigReader());

        // Zbinduj DriverManager - wrapper do Web Drivera
        bind(DriverManager.class).in(Scopes.SINGLETON);

        // Zbinduj utility classes - Singletons
        bind(WaitUtils.class).in(Scopes.SINGLETON);
        bind(ElementActions.class).in(Scopes.SINGLETON);

        // Zbinduj pages - każda instancja może być tworzona na nowo
        bind(LoginPage.class);
        bind(DashboardPage.class);
        bind(BasePage.class);
    }

}

