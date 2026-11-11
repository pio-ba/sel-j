package com.selj.tests;

import org.testng.annotations.DataProvider;

/**
 * Test data provider for login tests.
 * Centralizes test data for better maintainability.
 */
public class TestData {

    @DataProvider(name = "validLogin", parallel = true)
    public static Object[][] validLoginData() {
        return new Object[][]{
                {"test@qabrains.com", "Password123"},
                {"practice@qabrains.com", "Password123"}
        };
    }

    @DataProvider(name = "invalidLogin", parallel = true)
    public static Object[][] invalidLoginData() {
        return new Object[][]{
                {"wrong@wrong.com", "wrong1111"},
                {"adminmail.com", "wrongpass123"},
                {"admin@mail.com", "Invalid credentials"},
                {"admin123@mail.com", "Invalid credentials"},
                {"<script>alert('xss')</script>", "test123123!",}
        };
    }
}