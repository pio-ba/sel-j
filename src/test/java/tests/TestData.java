package tests;

import org.testng.annotations.DataProvider;

/**
 * Test data provider for login tests.
 * Centralizes test data for better maintainability.
 */
public class TestData {

    @DataProvider(name = "validLogin")
    public static Object[][] validLoginData() {
        return new Object[][]{
                {"admin", "admin123", "Dashboard"}
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

    @DataProvider(name = "passwordReset")
    public static Object[][] passwordResetData() {
        return new Object[][]{
                {"admin@example.com", true},
                {"user@invalid.com", false},
                {"", false}
        };
    }
}