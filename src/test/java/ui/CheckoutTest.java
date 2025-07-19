package ui;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.Select;
import testData.TestData;
import utils.CommonUtils;
import utils.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.CommonUtils.*;
import static utils.TestUtils.*;

@Epic("UI")
@Feature("Checkout")
public class CheckoutTest extends BaseTest {
    @FindBy(css = "button[aria-label='increase']")
    private WebElement increaseButton;

    @FindBy(name = "placeOrder")
    private WebElement placeOrderButton;

    private Double itemPrice;
    private int counterClickNumber = 0;
    private int popupCounter = 0;
    private By itemPriceLocator = By.cssSelector(".modal-dialog [data-test-sale-price], [data-test-price]");

    @BeforeEach
    public void setUp() {
        super.setUp();
        clickOnRandomWomenCategoryItem(driver, this);
        if (closePopupIfAvailable(this)) {
            popupCounter++;
        }
        clickOnRandomItemLink(this);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void checkoutPageTest() {
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);
        itemPrice = convertFromStringToDouble(driver, itemPriceLocator);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        clickOnViewBagButton(this, driver);
        clickOnCheckoutButton();

        assertEquals("Checkout", driver.findElement(By.cssSelector("h1.qa-page-header")).getText());
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive"), @Tag("Flaky")})
    @Test
    public void checkOneQuantityItemTest() {
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        itemPrice = convertFromStringToDouble(driver, itemPriceLocator);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        int itemQuantity = TestUtils.getItemQuantity(this);

        clickOnViewBagButton(this, driver);
        clickOnCheckoutButton();

        double expectedCartItemPrice = roundTo2Decimals(TestUtils.calculatePriceWithDiscountIfAvailable(driver, itemPrice, itemQuantity));
        double actualCartItemPrice = roundTo2Decimals(convertFromStringToDouble(driver, By.cssSelector(".cart-item-info .cart-item-price span")));

        assertEquals(expectedCartItemPrice, actualCartItemPrice);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void checkSeveralQuantityItemTest() {
        getFirstAvailableSize(this, driver);
        clickOnCounterIfAvailableAndClickAdd();

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        clickOnViewBagButton(this, driver);
        clickOnCheckoutButton();

        assertTrue(
                getWait10()
                        .until(ExpectedConditions.visibilityOfElementLocated(By.className("cart-item-quantity")))
                        .getText()
                        .contains(String.valueOf(1 + counterClickNumber)));
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive"), @Tag("Flaky")})
    @Test
    public void checkSeveralQuantityItemPriceTest() {
        getFirstAvailableSize(this, driver);
        clickOnCounterIfAvailableAndClickAdd();

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        itemPrice = convertFromStringToDouble(driver, itemPriceLocator);

        int itemQuantity = TestUtils.getItemQuantity(this);

        clickOnViewBagButton(this, driver);
        clickOnCheckoutButton();

        double expectedCartItemPrice = roundTo2Decimals(TestUtils.calculatePriceWithDiscountIfAvailable(driver, itemPrice, itemQuantity));
        double actualCartItemPrice = roundTo2Decimals(convertFromStringToDouble(driver, By.cssSelector(".cart-item-price span")));

        assertEquals(expectedCartItemPrice, actualCartItemPrice);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void removeItemTest() {
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        clickOnViewBagButton(this, driver);
        clickOnCheckoutButton();

        TestUtils.removeItemFromCart(this);

        getWait30().until(ExpectedConditions.urlContains(TestData.CART_ENDPOINT));
        String actualEmptyCartMessage = getWait30()
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("qa-empty-cart-msg")))
                .getText();

        assertEquals(TestData.EMPTY_CART_MESSAGE, actualEmptyCartMessage);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void fillCheckoutDataTest() {
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        clickOnViewBagButton(this, driver);
        clickOnCheckoutButton();

        fillEmailField(this);
        fillFirstNameField(driver);
        fillLastNameField(driver);
        fillStreetField();
        fillCityField();
        selectState();
        TestUtils.fillZipCodeField(driver);

        assertTrue(driver.findElement(By.name("billToShippingAddress")).isSelected());
        assertTrue(getWait10().until(ExpectedConditions.elementToBeClickable(placeOrderButton)).isEnabled());
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

    @Step("Update the item quantity via the counter if available and click on the Add button")
    public void clickOnCounterIfAvailableAndClickAdd() {
        WebElement increaseQuantityButton = getWait10().until(ExpectedConditions.elementToBeClickable(increaseButton));

        if ("true".equals(increaseQuantityButton.getDomAttribute("disabled"))) {
            clickOnAddToBagButton(this);
        } else {
            counterClickNumber = TestUtils.clickOnCounterBetween1and9(counterClickNumber, this);
            clickOnAddToBagButton(this);
        }
    }
}
