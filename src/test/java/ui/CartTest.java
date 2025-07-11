package ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.CommonUtils;
import utils.TestUtils;

import static testData.TestData.CART_ENDPOINT;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.CommonUtils.*;
import static utils.TestUtils.*;

public class CartTest extends BaseTest {
    @FindBy(className = "qa-btn-add-to-bag")
    private WebElement addToBagButton;

    @FindBy(xpath = "//div[@class='modal-dialog'][not(@quickview)]")
    private WebElement modalDialog;

    @FindBy(css = "button[data-test-view-cart]")
    private WebElement viewBagButton;

    @FindBy(css = "button[aria-label='increase']")
    private WebElement increaseButton;

    @FindBy(css= ".btn.qa-item-btn-edit")
    private WebElement editButton;

    private Actions actions;
    private Double itemPrice;
    private int counterClickNumber = 0;
    private final static String emptyCartMessage = "Your bag is empty. Find something you love!";
    private final static String addedToBagMessage = "Added to bag!";
    private By itemPriceLocator = By.cssSelector("[data-test-product-prices] > *:first-child");
    private static final Logger logger = LogManager.getLogger(CartTest.class);

    @BeforeEach
    public void setUp() {
        super.setUp();
        actions = new Actions(driver);
    }

    @Test
    public void addItemToCart() {
        logger.info("start message");
        clickOnRandomWomenCategoryItem(driver);
        closePopupIfAvailable(driver);
        clickOnRandomItemLink(driver);
        getFirstAvailableSize(driver);
        addToBagButton.click();

        closePopupIfAvailable(driver);

        assertEquals(addedToBagMessage, modalDialog.findElement(By.tagName("h2")).getText());
    }

    @Test
    public void addItemToCartViaQuickShopButton() {
        clickOnRandomWomenCategoryItem(driver);
        closePopupIfAvailable(driver);
        logger.info("the popup was closed");

        addRandomItemToCartViaQuickShopButton(driver, actions);
        getFirstAvailableSize(driver);
        addToBagButton.click();

        closePopupIfAvailable(driver);

        assertEquals(addedToBagMessage, modalDialog.findElement(By.tagName("h2")).getText());
    }

    @Test
    public void counterTest() {
        clickOnRandomWomenCategoryItem(driver);
        closePopupIfAvailable(driver);
        clickOnRandomItemLink(driver);

        itemPrice = convertFromStringToDouble(driver, itemPriceLocator);
        getFirstAvailableSize(driver);

        WebElement increaseQuantityButton = getWait10().until(ExpectedConditions.elementToBeClickable(increaseButton));

        if("true".equals(increaseQuantityButton.getDomAttribute("disabled"))) {
            addToBagButton.click();
            closePopupIfAvailable(driver);
        }
        clickOnCounterBetween1and9();
        addToBagButton.click();
        closePopupIfAvailable(driver);

        String actualText = getWait5()
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-product-quantity]")))
                    .getText();

        Assertions.assertTrue(actualText.contains(String.valueOf(1 + counterClickNumber)));

        CommonUtils.scrollAndClickWithJS(driver, viewBagButton);
        getWait30().until(ExpectedConditions.urlContains(CART_ENDPOINT));

        CommonUtils.scrollByViewportPercentage(driver, 70);

        Assertions.assertTrue(
                getWait5()
                        .until(ExpectedConditions.visibilityOfElementLocated(By.className("cart-item-quantity")))
                        .getText()
                        .contains(String.valueOf(1 + counterClickNumber)));

        double finalCartItemPrice = convertFromStringToDouble(driver, By.cssSelector(".cart-item-price span"));

        Assertions.assertEquals(roundTo2Decimals(itemPrice * (1 + counterClickNumber)), finalCartItemPrice);
    }

    @Test
    public void removeItemFromCartTest() {
        clickOnRandomWomenCategoryItem(driver);
        closePopupIfAvailable(driver);
        clickOnRandomItemLink(driver);

        getFirstAvailableSize(driver);
        addToBagButton.click();
        closePopupIfAvailable(driver);

        getWait10().until(ExpectedConditions.elementToBeClickable(viewBagButton)).click();
        getWait30().until(ExpectedConditions.urlContains(CART_ENDPOINT));

        CommonUtils.scrollByViewportPercentage(driver, 70);
        getWait10().until(ExpectedConditions.elementToBeClickable(By.name("removeCommerceItem"))).click();

        String actualEmptyCartMessage = getWait30()
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("qa-empty-cart-msg")))
                .getText();

        Assertions.assertEquals(emptyCartMessage, actualEmptyCartMessage);
    }

    @Test
    public void editCartItemQuantity() {
        clickOnRandomWomenCategoryItem(driver);
        closePopupIfAvailable(driver);
        clickOnRandomItemLink(driver);

        itemPrice = convertFromStringToDouble(driver, itemPriceLocator);

        getFirstAvailableSize(driver);
        addToBagButton.click();

        closePopupIfAvailable(driver);

        getWait10().until(ExpectedConditions.elementToBeClickable(viewBagButton)).click();
        getWait30().until(ExpectedConditions.urlContains(CART_ENDPOINT));

        CommonUtils.scrollByViewportPercentage(driver, 70);

        getWait10().until(ExpectedConditions.elementToBeClickable(By.name("editCommerceItem"))).click();
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#quickview-edit-carousel img")));
        CommonUtils.scrollToItemWithJS(driver, editButton);

        WebElement increaseQuantityButton = getWait10().until(ExpectedConditions.elementToBeClickable(increaseButton));

        if("true".equals(increaseQuantityButton.getDomAttribute("disabled"))) {
            editButton.click();
        } else {
            clickOnCounterBetween1and9();
            editButton.click();
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

    @Test
    public void orderSummaryTest() {
        clickOnRandomWomenCategoryItem(driver);
        closePopupIfAvailable(driver);
        clickOnRandomItemLink(driver);

        itemPrice = roundTo2Decimals(convertFromStringToDouble(driver, itemPriceLocator));

        getFirstAvailableSize(driver);
        addToBagButton.click();

        closePopupIfAvailable(driver);

        getWait10().until(ExpectedConditions.elementToBeClickable(viewBagButton)).click();
        getWait30().until(ExpectedConditions.urlContains(CART_ENDPOINT));

        CommonUtils.scrollByViewportPercentage(driver, 80);

        double shippingPrice = convertFromStringToDouble(driver, By.cssSelector("[data-testid='row-shipping-value']"));
        double subTotalPrice = convertFromStringToDouble(driver, By.cssSelector("[data-testid='row-total-value']"));

        assertEquals(roundTo2Decimals(itemPrice + shippingPrice), subTotalPrice);
    }

    public void clickOnCounterBetween1and9() {
        counterClickNumber = new Random().nextInt(9) + 1;
        WebElement counter = getWait10().until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='increase']")));

        for (int i = 1; i <= counterClickNumber; i++) {
            counter.click();
        }
    }
}
