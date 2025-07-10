package ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utils.CommonUtils;
import testData.TestData;

import static utils.CommonUtils.*;
import static utils.TestUtils.*;

public class CheckoutTest extends BaseTest {
    @FindBy(className = "qa-btn-add-to-bag")
    private WebElement addToBagButton;

    private By promoLocator = By.cssSelector("li.qa-promo-item");
    private Actions actions;
    private Double itemPrice;

    @BeforeEach
    public void setUp() {
        super.setUp();
        actions = new Actions(driver);
    }

    @Test
    public void checkoutPageTest() {
        clickOnRandomWomenCategoryItem(driver);
        closePopupIfAvailable(driver);
        clickOnRandomItemLink(driver);

        itemPrice = convertFromStringToDouble(driver, By.cssSelector("[data-test-product-prices] > *:first-child"));
        getFirstAvailableSize(driver);
        addToBagButton.click();
        closePopupIfAvailable(driver);

        getWait10().until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-test-view-cart]"))).click();
        getWait30().until(ExpectedConditions.urlContains(TestData.cartEndpoint));

        CommonUtils.scrollAndClickWithJS(driver, driver.findElement(By.name("go2checkout")));
        getWait30().until(ExpectedConditions.urlContains("/checkout"));

        try {
            driver.findElement(promoLocator).isDisplayed();
            double discount = convertFromStringToDouble(driver, promoLocator);
            itemPrice = roundTo2Decimals(getDiscountedValue(itemPrice, discount));
        } catch (Exception e) {

        }

        String price = getWait5()
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-item-info .cart-item-price span")))
                .getText();

        Assertions.assertTrue(price.contains(String.valueOf(itemPrice)));
    }
}
