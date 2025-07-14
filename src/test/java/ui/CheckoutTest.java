package ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.Select;
import testData.TestData;
import utils.CommonUtils;
import utils.TestUtils;

import static utils.CommonUtils.*;
import static utils.TestUtils.*;

@Feature("UI")
public class CheckoutTest extends BaseTest {
    @FindBy(css = "button[aria-label='increase']")
    private WebElement increaseButton;

    @FindBy(name = "placeOrder")
    private WebElement placeOrderButton;

    private Double itemPrice;
    private int counterClickNumber = 0;
    private int popupCounter = 0;

    @BeforeEach
    public void setUp() {
        super.setUp();
        clickOnRandomWomenCategoryItem(driver, this);
        if(closePopupIfAvailable(this)){
            popupCounter++;
        }
        clickOnRandomItemLink(this);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void checkoutPageTest() {
        itemPrice = convertFromStringToDouble(driver, By.cssSelector("[data-test-product-prices] > *:first-child"));
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        clickOnViewBagButton(this);
        clickOnCheckoutButton();

        Assertions.assertEquals("Checkout", driver.findElement(By.cssSelector("h1.qa-page-header")).getText());
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void checkOneQuantityItemTest() {
        itemPrice = convertFromStringToDouble(driver, By.cssSelector("[data-test-product-prices] > *:first-child"));
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        clickOnViewBagButton(this);
        clickOnCheckoutButton();
        TestUtils.calculatePriceWithDiscountIfAvailable(driver, itemPrice);

        String price = getWait5()
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-item-info .cart-item-price span")))
                .getText();

        Assertions.assertTrue(price.contains(String.valueOf(itemPrice)));
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @RepeatedTest(3)
    public void checkSeveralQuantityItemTest() {
        itemPrice = convertFromStringToDouble(driver, By.cssSelector("[data-test-product-prices] > *:first-child"));
        getFirstAvailableSize(this, driver);

        WebElement increaseQuantityButton = getWait10().until(ExpectedConditions.elementToBeClickable(increaseButton));

        if("true".equals(increaseQuantityButton.getDomAttribute("disabled"))) {
            clickOnAddToBagButton(this);
            if (popupCounter == 0) {
                closePopupIfAvailable(this);
            }
        }
        counterClickNumber = TestUtils.clickOnCounterBetween1and9(counterClickNumber, this);
        clickOnAddToBagButton(this);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        clickOnViewBagButton(this);
        clickOnCheckoutButton();
        TestUtils.calculatePriceWithDiscountIfAvailable(driver, itemPrice);

        Assertions.assertTrue(
                getWait10()
                        .until(ExpectedConditions.visibilityOfElementLocated(By.className("cart-item-quantity")))
                        .getText()
                        .contains(String.valueOf(1 + counterClickNumber)));

        double finalCartItemPrice = convertFromStringToDouble(driver, By.cssSelector(".cart-item-price span"));

        Assertions.assertEquals(roundTo2Decimals(itemPrice * (1 + counterClickNumber)), finalCartItemPrice);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void removeItemTest() {
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        clickOnViewBagButton(this);
        clickOnCheckoutButton();

        TestUtils.removeItemFromCart(this);

        getWait30().until(ExpectedConditions.urlContains(TestData.CART_ENDPOINT));
        String actualEmptyCartMessage = getWait30()
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("qa-empty-cart-msg")))
                .getText();

        Assertions.assertEquals(TestData.EMPTY_CART_MESSAGE, actualEmptyCartMessage);
    }

    @Test
    public void fillingCheckoutDataTest() throws InterruptedException {
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);
        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        clickOnViewBagButton(this);
        clickOnCheckoutButton();

        fillEmailField(this);
        fillFirstNameField(driver);
        fillLastNameField(driver);
        fillStreetField();
        fillCityField();
        selectState();
        TestUtils.fillZipCodeField(driver);

        Assertions.assertTrue(driver.findElement(By.name("billToShippingAddress")).isSelected());
        Assertions.assertTrue(getWait10().until(ExpectedConditions.elementToBeClickable(placeOrderButton)).isEnabled());
    }

    @Step("Click on the Checkout button")
    public void clickOnCheckoutButton() {
        CommonUtils.scrollAndClickWithJS(driver, driver.findElement(By.name("go2checkout")));
        getWait30().until(ExpectedConditions.urlContains("/checkout"));
    }

    @Step("Fill the street field")
    public void fillStreetField() {
        driver.findElement(By.cssSelector("input[placeholder='Street Address']")).sendKeys(TestData.generateRandomStreet());
    }

    @Step("Select the state")
    public void selectState() {
        WebElement name = driver.findElement(By.cssSelector("select[name='states']"));
        Select stateDropdown = new Select(name);
        stateDropdown.selectByIndex(5);
    }

    @Step("Fill the city code field")
    public void fillCityField() {
        driver.findElement(By.cssSelector("input[autocomplete='address-level2']")).sendKeys("Palmer");
    }
}
