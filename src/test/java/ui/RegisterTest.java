package ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import testData.TestData;
import utils.CommonUtils;

@Feature("UI")
public class RegisterTest extends BaseTest {

    @FindBy(name = "submit")
    private WebElement submitButton;

    private String password = TestData.generateRandomPassword();

    @Tags({@Tag("UI"), @Tag("Major"), @Tag("Positive")})
    @Test
    public void newUserRegistrationTest() {
        clickOnSideTrayAccountLink();
        clickOnCreateAccountButton();

        fillEmailField();
        fillFirstNameField();
        fillLastNameField();
        fillPasswordField();
        fillConfirmPasswordField();
        fillZipCodeField();

        selectBirthDayMonth();
        selectBirthDay();

        Assertions.assertEquals("true", submitButton.getDomAttribute("disabled"));

        CommonUtils.scrollAndClickWithJS(driver, driver.findElement(By.cssSelector(".qa-checkbox-accept-terms label")));

        Assertions.assertNull(submitButton.getDomAttribute("disabled"));

        clickOnSubmitButton();
    }

    @Step("Click on sidetray account")
    public void clickOnSideTrayAccountLink() {
        getWait30().until(ExpectedConditions.elementToBeClickable(By.className("sidetray-account"))).click();
    }

    @Step("Click on the Create Account button")
    public void clickOnCreateAccountButton() {
        getWait10().until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[data-test='register-button']"))).click();
        getWait30().until(ExpectedConditions.urlContains("/create-account"));
    }

    @Step("Fill the email field")
    public void fillEmailField() {
        getWait10()
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[placeholder='Email']")))
                .sendKeys(TestData.generateRandomEmail());
    }

    @Step("Fill the first name field")
    public void fillFirstNameField() {
        driver.findElement(By.name("firstname")).sendKeys(TestData.generateRandomFirstName());
    }

    @Step("Fill the last name field")
    public void fillLastNameField() {
        driver.findElement(By.name("lastname")).sendKeys(TestData.generateRandomLastName());
    }

    @Step("Fill the password field")
    public void fillPasswordField() {
        driver.findElement(By.cssSelector("[placeholder='Password']")).sendKeys(password);
    }

    @Step("Fill the confirm password field")
    public void fillConfirmPasswordField() {
        driver.findElement(By.cssSelector("[placeholder='Confirm Password']")).sendKeys(password);
    }

    @Step("Fill the zip code field")
    public void fillZipCodeField() {
        driver.findElement(By.cssSelector("[placeholder='Zip Code']")).sendKeys("85249");
    }

    @Step("Select the birthday month")
    public void selectBirthDayMonth() {
        WebElement name = driver.findElement(By.cssSelector("select[name='month']"));
        Select nameDropdown = new Select(name);
        nameDropdown.selectByIndex(CommonUtils.getRandomValueBetween1And12());
    }

    @Step("Select the birth day")
    public void selectBirthDay() {
        WebElement day = driver.findElement(By.cssSelector("select[name='day']"));
        Select dayDropdown = new Select(day);
        dayDropdown.selectByIndex(CommonUtils.getRandomValueBetween1And27());
    }

    @Step("Click on the submit button")
    public void clickOnSubmitButton() {
        submitButton.click();
    }
}
