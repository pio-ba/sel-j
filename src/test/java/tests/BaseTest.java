package tests;

import com.selj.config.ConfigReader;
import com.selj.config.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod
    public void setUp() {
        DriverManager.initDriver();
        String baseUrl = ConfigReader.getProperty("baseUrl");
        if (baseUrl != null && !baseUrl.isEmpty()) {
            DriverManager.getDriver().get(baseUrl);
        }
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
