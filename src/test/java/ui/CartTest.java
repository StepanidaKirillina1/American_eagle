package ui;

import extenstions.AllureExtension;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import testData.TestData;
import utils.CommonUtils;
import utils.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.CommonUtils.*;
import static utils.TestUtils.*;

@Epic("UI")
@Feature("Cart")
@ExtendWith(AllureExtension.class)
public class CartTest extends BaseTest {
    @FindBy(xpath = "//div[@class='modal-dialog'][not(@quickview)]")
    private WebElement modalDialog;

    @FindBy(css = "button[aria-label='increase']")
    private WebElement increaseButton;

    @FindBy(css = ".btn.qa-item-btn-edit")
    private WebElement updateButton;

    private Actions actions;
    private Double itemPrice;
    private int counterClickNumber = 0;
    private final static String ADDED_TO_BAG_MESSAGE = "Added to bag!";
    private By itemPriceLocator = By.cssSelector(".modal-dialog [data-test-sale-price], [data-test-price]");
    private int popupCounter = 0;

    @BeforeEach
    public void setUp() {
        super.setUp();
        actions = new Actions(driver);
        clickOnRandomWomenCategoryItem(driver, this);

        if (closePopupIfAvailable(this)) {
            popupCounter++;
        }
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void addItemToCart() {
        clickOnRandomItemLink(this);
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        assertEquals(ADDED_TO_BAG_MESSAGE,
                getWait10().until(ExpectedConditions.visibilityOf(modalDialog)).findElement(By.tagName("h2")).getText()
        );
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void addItemToCartViaQuickShopButton() {
        addFirstItemToCartViaQuickShopButton(driver, actions, this);
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        assertEquals(ADDED_TO_BAG_MESSAGE,
                getWait10().until(ExpectedConditions.visibilityOf(modalDialog)).findElement(By.tagName("h2")).getText());
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void counterTest() {
        clickOnRandomItemLink(this);
        getFirstAvailableSize(this, driver);

        WebElement increaseQuantityButton = getWait10().until(ExpectedConditions.elementToBeClickable(increaseButton));

        if ("true".equals(increaseQuantityButton.getDomAttribute("disabled"))) {
            clickOnAddToBagButton(this);
        } else {
            counterClickNumber = TestUtils.clickOnCounterBetween1and9(counterClickNumber, this);
            clickOnAddToBagButton(this);
        }

        itemPrice = convertFromStringToDouble(driver, itemPriceLocator);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        String actualText = getWait5()
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-product-quantity]")))
                .getText();

        assertTrue(actualText.contains(String.valueOf(1 + counterClickNumber)));

        clickOnViewBagButton(this, driver);
        CommonUtils.scrollByViewportPercentage(driver, 70);
        TestUtils.calculatePriceWithDiscountIfAvailable(driver, itemPrice);

        assertTrue(
                getWait5()
                        .until(ExpectedConditions.visibilityOfElementLocated(By.className("cart-item-quantity")))
                        .getText()
                        .contains(String.valueOf(1 + counterClickNumber)));

        double finalCartItemPrice = convertFromStringToDouble(driver, By.cssSelector(".cart-item-price span"));

        assertEquals(roundTo2Decimals(itemPrice * (1 + counterClickNumber)), finalCartItemPrice);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void removeItemFromCartTest() {
        clickOnRandomItemLink(this);
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        clickOnViewBagButton(this, driver);
        CommonUtils.scrollByViewportPercentage(driver, 70);
        removeItemFromCart(this);

        String actualEmptyCartMessage = getWait30()
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("qa-empty-cart-msg")))
                .getText();

        assertEquals(TestData.EMPTY_CART_MESSAGE, actualEmptyCartMessage);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void editCartItemQuantity() {
        clickOnRandomItemLink(this);
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        itemPrice = convertFromStringToDouble(driver, itemPriceLocator);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        clickOnViewBagButton(this, driver);

        CommonUtils.scrollByViewportPercentage(driver, 70);

        clickOnEditButton();
        CommonUtils.scrollToItemWithJS(driver, updateButton);

        WebElement increaseQuantityButton = getWait10().until(ExpectedConditions.elementToBeClickable(increaseButton));

        if ("true".equals(increaseQuantityButton.getDomAttribute("disabled"))) {
            clickOnUpdateButton();
        } else {
            counterClickNumber = TestUtils.clickOnCounterBetween1and9(counterClickNumber, this);
            clickOnUpdateButton();
        }

        try {
            getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.className("notification-card-message")));
        } catch (Exception e) {

        }

        CommonUtils.scrollByViewportPercentage(driver, -80);

        String itemQuantity = getWait30()
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("cart-item-quantity")))
                .getText();

        logger.info("check itemQuantity " + itemQuantity);
        logger.info("price " + itemPrice);

        assertTrue(itemQuantity.contains(String.valueOf(1 + counterClickNumber)));

        TestUtils.calculatePriceWithDiscountIfAvailable(driver, itemPrice);
        double finalCartItemPrice = convertFromStringToDouble(driver, By.cssSelector(".cart-item-price span"));

        logger.info("check finalPrice " + finalCartItemPrice);

        assertEquals(roundTo2Decimals(itemPrice * (1 + counterClickNumber)), finalCartItemPrice);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void orderSummaryTest() {
        double shippingPrice = 0.0;
        clickOnRandomItemLink(this);
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        itemPrice = convertFromStringToDouble(driver, itemPriceLocator);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        clickOnViewBagButton(this, driver);
        CommonUtils.scrollByViewportPercentage(driver, 70);
        TestUtils.calculatePriceWithDiscountIfAvailable(driver, itemPrice);

        String shippingPriceText = getWait5()
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='row-shipping-value']")))
                .getText();
        if (shippingPriceText.equals("Free")) {
            shippingPrice = 0;
        } else {
            shippingPrice = convertFromStringToDouble(driver, By.cssSelector("[data-testid='row-shipping-value']"));
        }
        double subTotalPrice = convertFromStringToDouble(driver, By.cssSelector("[data-testid='row-total-value']"));

        assertEquals(roundTo2Decimals(itemPrice + shippingPrice), subTotalPrice);
    }

    @Step("Click on the Edit button")
    public void clickOnEditButton() {
        getWait10().until(ExpectedConditions.elementToBeClickable(By.name("editCommerceItem"))).click();
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#quickview-edit-carousel img")));
    }

    @Step("Click on the Update button")
    public void clickOnUpdateButton() {
        updateButton.click();
    }
}
