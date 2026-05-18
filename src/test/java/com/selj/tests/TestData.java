package com.selj.tests;

import org.testng.annotations.DataProvider;

/**
 * Test data provider for login tests.
 * Centralizes test data for better maintainability.
 */
public class TestData {

    @DataProvider(name = "validLogin")
    public static Object[][] validLoginData() {
        return new Object[][]{
                {"test@qabrains.com", "practice@qabrains.com", "student@qabrains.com"}
        };
    }

    @DataProvider(name = "invalidLogin")
    public static Object[][] invalidLoginData() {
        return new Object[][]{
                {"wrong", "wrong", "Invalid credentials"},
                {"admin", "wrongpass", "Invalid credentials"},
                {"", "", "Invalid credentials"},
                {"admin", "", "Invalid credentials"},
                {"", "admin123", "Invalid credentials"},
                {"<script>alert('xss')</script>", "test", "Invalid credentials"}
        };
    }
}