package ui;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
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
public class CartTest extends BaseTest {
    @FindBy(xpath = "//div[@class='modal-dialog'][not(@quickview)]//h2")
    private WebElement modalDialogTitle;

    @FindBy(css = "button[aria-label='increase']")
    private WebElement increaseButton;

    @FindBy(css = ".btn.qa-item-btn-edit")
    private WebElement updateButton;

    @FindBy(className = "cart-item-quantity")
    private WebElement cartItemQuantity;

    private Actions actions;
    private Double itemPrice;
    private int counterClickNumber = 0;
    private final static String ADDED_TO_BAG_MESSAGE = "Added to bag!";
    private By itemPriceLocator = By.cssSelector(".modal-dialog [data-test-sale-price], [data-test-price]");
    private int popupCounter = 0;
    private WebElement increaseQuantityButton;

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
    public void addItemToCartTest() {
        clickOnRandomItemLink(this);
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        assertEquals(
                ADDED_TO_BAG_MESSAGE,
                getWait10().until(ExpectedConditions.visibilityOf(modalDialogTitle)).getText());
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void addItemToCartViaQuickShopButtonTest() {
        addFirstItemToCartViaQuickShopButton(driver, actions, this);
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        assertEquals(
                ADDED_TO_BAG_MESSAGE,
                getWait10().until(ExpectedConditions.visibilityOf(modalDialogTitle)).getText()
        );
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void counterTest() {
        clickOnRandomItemLink(this);
        getFirstAvailableSize(this, driver);

        increaseQuantityButton = getWait10().until(ExpectedConditions.elementToBeClickable(increaseButton));

        if ("true".equals(increaseQuantityButton.getDomAttribute("disabled"))) {
            clickOnAddToBagButton(this);
        } else {
            counterClickNumber = TestUtils.clickOnCounterBetween1and9(counterClickNumber, this);
            clickOnAddToBagButton(this);
        }

        logger.info("counterClickNumber " + counterClickNumber);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        int itemQuantity = TestUtils.getItemQuantity(this);

        assertEquals(1 + counterClickNumber, itemQuantity);

        clickOnViewBagButton(this, driver);
        CommonUtils.scrollByViewportPercentage(driver, 70);

        assertTrue(
                getWait5()
                        .until(ExpectedConditions.visibilityOf(cartItemQuantity))
                        .getText()
                        .contains(String.valueOf(1 + counterClickNumber)));
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
    public void editCartItemQuantityTest() {
        clickOnRandomItemLink(this);
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        clickOnViewBagButton(this, driver);

        CommonUtils.scrollByViewportPercentage(driver, 70);

        clickOnEditButton();
        clickOnCounterButtonIfAvailableAndClickUpdate();

        try {
            getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.className("notification-card-message")));
        } catch (Exception e) {

        }

        CommonUtils.scrollByViewportPercentage(driver, -80);

        assertTrue(
                getWait5()
                        .until(ExpectedConditions.visibilityOf(cartItemQuantity))
                        .getText()
                        .contains(String.valueOf(1 + counterClickNumber)));
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive"), @Tag("Flaky")})
    @Test
    public void editedCartItemsPriceTest() {
        clickOnRandomItemLink(this);
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);

        itemPrice = convertFromStringToDouble(driver, itemPriceLocator);

        if (popupCounter == 0) {
            closePopupIfAvailable(this);
        }

        int itemQuantity = TestUtils.getItemQuantity(this);

        clickOnViewBagButton(this, driver);
        CommonUtils.scrollByViewportPercentage(driver, 70);
        clickOnEditButton();

        counterClickNumber = clickOnCounterButtonIfAvailableAndClickUpdate();

        try {
            getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.className("notification-card-message")));
        } catch (Exception e) {

        }

        CommonUtils.scrollByViewportPercentage(driver, -80);

        itemQuantity += counterClickNumber;
        logger.info("check itemQuantity " + itemQuantity);
        logger.info("price " + itemPrice);
        logger.info("counterClickNumber " + counterClickNumber);

        double expectedCartItemPrice = roundTo2Decimals(TestUtils.calculatePriceWithDiscountIfAvailable(driver, itemPrice, itemQuantity));
        double actualCartItemPrice = roundTo2Decimals(convertFromStringToDouble(driver, By.cssSelector(".cart-item-info .cart-item-price span")));

        assertEquals(expectedCartItemPrice, actualCartItemPrice);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive"), @Tag("Flaky")})
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

        int itemQuantity = TestUtils.getItemQuantity(this);

        clickOnViewBagButton(this, driver);
        CommonUtils.scrollByViewportPercentage(driver, 70);

        double expectedCartItemPrice = TestUtils.calculatePriceWithDiscountIfAvailable(driver, itemPrice, itemQuantity);

        String shippingPriceText = getWait5()
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='row-shipping-value']")))
                .getText();

        if (shippingPriceText.equals("Free")) {
            shippingPrice = 0;
        } else {
            shippingPrice = convertFromStringToDouble(driver, By.cssSelector("[data-testid='row-shipping-value']"));
        }
        double subTotalPrice = convertFromStringToDouble(driver, By.cssSelector("[data-testid='row-total-value']"));

        assertEquals(roundTo2Decimals(expectedCartItemPrice + shippingPrice), subTotalPrice);
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

    @Step("Update the item quantity via the counter if available and click on the Update button")
    public int clickOnCounterButtonIfAvailableAndClickUpdate() {
        CommonUtils.scrollToItemWithJS(driver, updateButton);

        increaseQuantityButton = getWait10().until(ExpectedConditions.elementToBeClickable(increaseButton));

        if ("true".equals(increaseQuantityButton.getDomAttribute("disabled"))) {
            counterClickNumber = 0;
            clickOnUpdateButton();
        } else {
            counterClickNumber = TestUtils.clickOnCounterBetween1and9(counterClickNumber, this);
            clickOnUpdateButton();
        }

        return counterClickNumber;
    }
}
