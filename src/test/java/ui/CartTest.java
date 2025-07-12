package ui;

import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.CommonUtils;
import utils.TestUtils;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.CommonUtils.*;
import static utils.TestUtils.*;

public class CartTest extends BaseTest {
    @FindBy(xpath = "//div[@class='modal-dialog'][not(@quickview)]")
    private WebElement modalDialog;

    @FindBy(css = "button[aria-label='increase']")
    private WebElement increaseButton;

    @FindBy(css= ".btn.qa-item-btn-edit")
    private WebElement updateButton;

    private Actions actions;
    private Double itemPrice;
    private int counterClickNumber = 0;
    private final static String EMPTY_CART_MESSAGE = "Your bag is empty. Find something you love!";
    private final static String ADDED_TO_BAG_MESSAGE = "Added to bag!";
    private By itemPriceLocator = By.cssSelector("[data-test-product-prices] > *:first-child");

    @BeforeEach
    public void setUp() {
        super.setUp();
        actions = new Actions(driver);
        clickOnRandomWomenCategoryItem(driver, this);
        closePopupIfAvailable(this);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void addItemToCart() {
        clickOnRandomItemLink(this);
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);
        closePopupIfAvailable(this);

        assertEquals(ADDED_TO_BAG_MESSAGE, modalDialog.findElement(By.tagName("h2")).getText());
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void addItemToCartViaQuickShopButton() {
        addRandomItemToCartViaQuickShopButton(driver, actions, this);
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);
        closePopupIfAvailable(this);

        assertEquals(ADDED_TO_BAG_MESSAGE, modalDialog.findElement(By.tagName("h2")).getText());
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void counterTest() {
        clickOnRandomItemLink(this);

        itemPrice = convertFromStringToDouble(driver, itemPriceLocator);
        getFirstAvailableSize(this, driver);

        WebElement increaseQuantityButton = getWait10().until(ExpectedConditions.elementToBeClickable(increaseButton));

        if("true".equals(increaseQuantityButton.getDomAttribute("disabled"))) {
            clickOnAddToBagButton(this);
            closePopupIfAvailable(this);
        }
        clickOnCounterBetween1and9();
        clickOnAddToBagButton(this);
        closePopupIfAvailable(this);

        String actualText = getWait5()
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-product-quantity]")))
                    .getText();

        Assertions.assertTrue(actualText.contains(String.valueOf(1 + counterClickNumber)));

        clickOnViewBagButton(this);
        CommonUtils.scrollByViewportPercentage(driver, 70);

        Assertions.assertTrue(
                getWait5()
                        .until(ExpectedConditions.visibilityOfElementLocated(By.className("cart-item-quantity")))
                        .getText()
                        .contains(String.valueOf(1 + counterClickNumber)));

        double finalCartItemPrice = convertFromStringToDouble(driver, By.cssSelector(".cart-item-price span"));

        Assertions.assertEquals(roundTo2Decimals(itemPrice * (1 + counterClickNumber)), finalCartItemPrice);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void removeItemFromCartTest() {
        clickOnRandomItemLink(this);
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);
        closePopupIfAvailable(this);
        clickOnViewBagButton(this);
        CommonUtils.scrollByViewportPercentage(driver, 70);
        removeItemFromCart();

        String actualEmptyCartMessage = getWait30()
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("qa-empty-cart-msg")))
                .getText();

        Assertions.assertEquals(EMPTY_CART_MESSAGE, actualEmptyCartMessage);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void editCartItemQuantity() {
        clickOnRandomItemLink(this);

        itemPrice = convertFromStringToDouble(driver, itemPriceLocator);

        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);
        closePopupIfAvailable(this);
        clickOnViewBagButton(this);

        CommonUtils.scrollByViewportPercentage(driver, 70);

        clickOnEditButton();
        CommonUtils.scrollToItemWithJS(driver, updateButton);

        WebElement increaseQuantityButton = getWait10().until(ExpectedConditions.elementToBeClickable(increaseButton));

        if("true".equals(increaseQuantityButton.getDomAttribute("disabled"))) {
            clickOnUpdateButton();
        } else {
            clickOnCounterBetween1and9();
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

        logger.info("check itemQuantity " +  itemQuantity);
        logger.info("price " + itemPrice);
        logger.info("assert itemQuantity");

        Assertions.assertTrue(itemQuantity.contains(String.valueOf(1 + counterClickNumber)));

        TestUtils.calculatePriceWithDiscountIfAvailable(driver, itemPrice);
        double finalCartItemPrice = convertFromStringToDouble(driver, By.cssSelector(".cart-item-price span"));

        logger.info("check finalPrice " + finalCartItemPrice);

        Assertions.assertEquals(roundTo2Decimals(itemPrice * (1 + counterClickNumber)), finalCartItemPrice);
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void orderSummaryTest() {
        clickOnRandomItemLink(this);

        itemPrice = roundTo2Decimals(convertFromStringToDouble(driver, itemPriceLocator));

        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);
        closePopupIfAvailable(this);
        clickOnViewBagButton(this);

        CommonUtils.scrollByViewportPercentage(driver, 80);

        double shippingPrice = convertFromStringToDouble(driver, By.cssSelector("[data-testid='row-shipping-value']"));
        double subTotalPrice = convertFromStringToDouble(driver, By.cssSelector("[data-testid='row-total-value']"));

        assertEquals(roundTo2Decimals(itemPrice + shippingPrice), subTotalPrice);
    }

    @Step("Click on the counter button between 1 and 9")
    public void clickOnCounterBetween1and9() {
        counterClickNumber = new Random().nextInt(9) + 1;
        WebElement counter = getWait10().until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='increase']")));

        for (int i = 1; i <= counterClickNumber; i++) {
            counter.click();
        }
    }

    @Step("Remove item from the cart")
    public void removeItemFromCart() {
        getWait10().until(ExpectedConditions.elementToBeClickable(By.name("removeCommerceItem"))).click();
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
