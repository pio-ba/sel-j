package tests;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.selj.config.ConfigReader;
import com.selj.config.DriverManager;
import com.selj.config.WebDriverModule;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected Injector injector;
    protected DriverManager driverManager;
    protected ConfigReader configReader;

    @BeforeMethod
    public void setUp() {
        injector = Guice.createInjector(new WebDriverModule());
        driverManager = injector.getInstance(DriverManager.class);
        configReader = injector.getInstance(ConfigReader.class);

        driverManager.initDriver();
        String baseUrl = configReader.getProperty("baseUrl");
        if (baseUrl != null && !baseUrl.isEmpty()) {
            driverManager.getDriver().get(baseUrl);
        }
    }

    @AfterMethod
    public void tearDown() {
        driverManager.quitDriver();
    }
}
