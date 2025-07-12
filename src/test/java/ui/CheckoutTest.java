package ui;

import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import utils.CommonUtils;
import utils.TestUtils;

import static utils.CommonUtils.*;
import static utils.TestUtils.*;

public class CheckoutTest extends BaseTest {
    private Double itemPrice;

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Tags({@Tag("UI"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void checkoutPageTest() {
        clickOnRandomWomenCategoryItem(driver, this);
        closePopupIfAvailable(this);
        clickOnRandomItemLink(this);

        itemPrice = convertFromStringToDouble(driver, By.cssSelector("[data-test-product-prices] > *:first-child"));
        getFirstAvailableSize(this, driver);
        clickOnAddToBagButton(this);
        closePopupIfAvailable(this);
        clickOnViewBagButton(this);
        clickOnCheckoutButton();
        TestUtils.calculatePriceWithDiscountIfAvailable(driver, itemPrice);

        String price = getWait5()
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-item-info .cart-item-price span")))
                .getText();

        Assertions.assertTrue(price.contains(String.valueOf(itemPrice)));
    }

    @Step("Click on the Checkout button")
    public void clickOnCheckoutButton() {
        CommonUtils.scrollAndClickWithJS(driver, driver.findElement(By.name("go2checkout")));
        getWait30().until(ExpectedConditions.urlContains("/checkout"));
    }
}
