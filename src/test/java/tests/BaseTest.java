package tests;

import com.google.inject.Inject;
import com.selj.config.ConfigReader;
import com.selj.config.DriverManager;
import com.selj.config.WebDriverModule;
import com.selj.di.WebDriverProvider;
import com.selj.pages.DashboardPage;
import com.selj.pages.LoginPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;

@Guice(modules = WebDriverModule.class)
public class BaseTest {

    @Inject
    protected DriverManager driverManager;

    @Inject
    protected ConfigReader configReader;

    @Inject
    protected LoginPage loginPage;

    @Inject
    protected DashboardPage dashboardPage;

    @Inject
    protected WebDriverProvider webDriverProvider;

    @BeforeMethod
    public void setUp() {
        driverManager.initDriver();
        String baseUrl = configReader.getProperty("baseUrl");
        if (baseUrl != null && !baseUrl.isEmpty()) {
            driverManager.getDriver().get(baseUrl);
        }
    }

    @AfterMethod
    public void tearDown() {
        webDriverProvider.cleanup();
    }
}
