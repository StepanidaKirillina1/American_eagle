package ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import testData.TestData;
import utils.CommonUtils;

public class RegisterTest extends BaseTest {

    @FindBy(name = "submit")
    private WebElement submitButton;

    @Test
    public void newUserRegistrationTest() throws InterruptedException {
        String password = TestData.generateRandomPassword();

        getWait30().until(ExpectedConditions.elementToBeClickable(By.className("sidetray-account"))).click();
        getWait10().until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[data-test='register-button']"))).click();

        getWait30().until(ExpectedConditions.urlContains("/create-account"));

        getWait10()
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[placeholder='Email']")))
                .sendKeys(TestData.generateRandomEmail());
        driver.findElement(By.name("firstname")).sendKeys(TestData.generateRandomFirstName());
        driver.findElement(By.name("lastname")).sendKeys(TestData.generateRandomLastName());
        driver.findElement(By.cssSelector("[placeholder='Password']")).sendKeys(password);
        driver.findElement(By.cssSelector("[placeholder='Confirm Password']")).sendKeys(password);
        driver.findElement(By.cssSelector("[placeholder='Zip Code']")).sendKeys("85249");

        WebElement name = driver.findElement(By.cssSelector("select[name='month']"));
        Select nameDropdown = new Select(name);
        nameDropdown.selectByIndex(CommonUtils.getRandomValueBetween1And12());

        WebElement day = driver.findElement(By.cssSelector("select[name='day']"));
        Select dayDropdown = new Select(day);
        dayDropdown.selectByIndex(CommonUtils.getRandomValueBetween1And27());

        Assertions.assertEquals("true", submitButton.getDomAttribute("disabled"));

        CommonUtils.scrollAndClickWithJS(driver, driver.findElement(By.cssSelector(".qa-checkbox-accept-terms label")));

        Assertions.assertEquals(null, submitButton.getDomAttribute("disabled"));

        submitButton.click();
    }
}
