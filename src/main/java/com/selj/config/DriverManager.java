package com.selj.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverManager {


    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver() {

driver.set(new ChromeDriver());

    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }



}
