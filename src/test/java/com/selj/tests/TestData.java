package com.selj.tests;

import org.testng.annotations.DataProvider;

/**
 * Test data provider for login tests.
 * Centralizes test data for better maintainability.
 */
public class TestData {

    @DataProvider(name = "validLogin", parallel = false)
    public static Object[][] validLoginData() {
        return new Object[][]{
                {"test@qabrains.com", "Password123"},
                {"practice@qabrains.com", "Password123"}
        };
    }

    @DataProvider(name = "invalidLogin", parallel = false)
    public static Object[][] invalidLoginData() {
        return new Object[][]{
                {"wrong@wrong.com", "wrong1111"},
                {"admin@mail.com", "wrongpass123"},
                {"admin@mail.com", "invalidpass111"},
                {"admin123@mail.com", "invalidpass123!"},
        };
    }

    @DataProvider(name = "productsLogin", parallel = false)
    public static Object[][] productsLoginData() {
        return new Object[][]{
                {"test@qabrains.com", "Password123"}
        };
    }
}