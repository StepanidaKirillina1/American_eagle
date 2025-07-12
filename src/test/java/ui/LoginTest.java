package ui;

import config.TestPropertiesConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginTest extends BaseTest {

    public static final TestPropertiesConfig PROPERTIES_CONFIG = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());

    @Tags({@Tag("UI"), @Tag("Positive"), @Tag("Bot_enabled")})
    @Test
    public void existingUserLoginTest() {
        String expectedLoginMessage = "test's account";

        getWait30().until(ExpectedConditions.elementToBeClickable(By.className("sidetray-account"))).click();
        getWait10().until(ExpectedConditions.elementToBeClickable(By.name("signin"))).click();

        driver.findElement(By.cssSelector("input[placeholder='Email']")).sendKeys(PROPERTIES_CONFIG.getLogin());
        driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys(PROPERTIES_CONFIG.getPassword());
        driver.findElement(By.name("submit")).click();

        String actualLoginMessage = getWait30().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h2.modal-title"))).getText();

        Assertions.assertEquals(expectedLoginMessage, actualLoginMessage.toLowerCase());
    }
}
