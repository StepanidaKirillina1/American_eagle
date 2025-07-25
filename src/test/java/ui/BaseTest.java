package ui;

import extenstions.AllureExtension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import testData.TestData;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;

@ExtendWith(AllureExtension.class)
public class BaseTest {
    public static WebDriver driver;
    private WebDriverWait wait5;
    private WebDriverWait wait10;
    private WebDriverWait wait30;
    private WebDriverWait wait60;
    public Logger logger = LogManager.getLogger(this);

    @BeforeEach
    public void setUp() {
        initDriver();
        driver.manage().window().maximize();
        driver.get(TestData.UI_BASE_URL);
        PageFactory.initElements(driver,this);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void initDriver() {
        String remoteUrl = System.getenv("SELENIUM_REMOTE_URL");

        if (remoteUrl != null && !remoteUrl.isEmpty()) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.setCapability("goog:loggingPrefs", Map.of("browser", "ALL"));
            try {
                driver = new RemoteWebDriver(new URL(remoteUrl), options);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Malformed URL for Selenium Remote WebDriver", e);
            }
        } else {
            driver = new ChromeDriver();
        }
    }

    public WebDriverWait getWait5() {
        if (wait5 == null) {
            wait5 = new WebDriverWait(driver, Duration.ofSeconds(5));
        }

        return wait5;
    }

    public WebDriverWait getWait10() {
        if (wait10 == null) {
            wait10 = new WebDriverWait(driver, Duration.ofSeconds(20));
        }

        return wait10;
    }

    public WebDriverWait getWait30() {
        if (wait30 == null) {
            wait30 = new WebDriverWait(driver, Duration.ofSeconds(30));
        }

        return wait30;
    }

    public WebDriverWait getWait60() {
        if (wait60 == null) {
            wait60 = new WebDriverWait(driver, Duration.ofSeconds(60));
        }

        return wait60;
    }
}
