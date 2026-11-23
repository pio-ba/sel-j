package com.selj.pages;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.selj.actions.WebElementInteraction;
import com.selj.core.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountMenuPage extends BasePage {

    private final Provider<LoginPage> loginPageProvider;
    private final String baseUrl;

    @Inject
    public AccountMenuPage(WebDriverFactory webDriverFactory,
                           WebElementInteraction actions,
                           Provider<LoginPage> loginPageProvider,
                           @Named("baseUrl") String baseUrl) {
        super(webDriverFactory, actions);
        this.loginPageProvider = loginPageProvider;
        this.baseUrl = baseUrl;
    }

    public LoginPage logout() {
        WebDriver driver = getWebDriverFactory().create();
        WebElement accountButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(normalize-space(), '@')]")));
        getActions().click(accountButton);

        WebElement logoutOption = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(
                        "//*[@role='menuitem' or @role='button']["
                                + "contains(translate(normalize-space(), 'LOGOUT', 'logout'), 'logout') "
                                + "or contains(translate(normalize-space(), 'LOGOUT', 'logout'), 'log out') "
                                + "or contains(normalize-space(), 'Sign out')]")));
        getActions().click(logoutOption);

        driver.manage().deleteAllCookies();
        driver.get(baseUrl);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
        return loginPageProvider.get();
    }
}
