package utils;

import io.qameta.allure.Step;
import models.Item;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.BaseTest;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static testData.TestData.CART_ENDPOINT;
import static utils.CommonUtils.*;

public class TestUtils {
    private static final String OUT_OF_STOCK_TEXT = "Out of Stock Online";
    private static final By ITEM_LINK_LOCATOR = By.cssSelector("a[role='menuitem']");
    private static final By PROMO_LOCATOR = By.cssSelector("li.qa-promo-item");

    @Step("Get the first available size of the item")
    public static void getFirstAvailableSize(BaseTest baseTest, WebDriver driver) {
        CommonUtils.scrollToItemWithJS(driver, driver.findElement(By.xpath("//div[text()='Color:']")));

        baseTest.getWait5().until(ExpectedConditions.elementToBeClickable(By.className("dropdown"))).click();
        baseTest.getWait10().until(ExpectedConditions.elementToBeClickable(By.cssSelector(".dropdown-selection.open")));
        baseTest.getWait10().until(ExpectedConditions.elementToBeClickable(ITEM_LINK_LOCATOR));

        List<WebElement> sizes = driver.findElements(ITEM_LINK_LOCATOR);

        for (WebElement size : sizes) {
            if (!size.getText().contains(OUT_OF_STOCK_TEXT)) {
                size.click();
                return;
            }
        }

        throw new NoSuchElementException("All sizes are out of stock");
    }

    private static void clickOnRandomLink(By locator, BaseTest baseTest) {
        List<WebElement> elements = baseTest.getWait30().until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        int randomIndex = new Random().nextInt(elements.size());

        elements.get(randomIndex).click();
    }

    @Step("Click on a random women category item")
    public static void clickOnRandomWomenCategoryItem(WebDriver driver, BaseTest baseTest) {
        WebElement womenCategory = new WebDriverWait(driver, Duration.ofSeconds(60))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[data-text='Women']")));
        new Actions(driver).moveToElement(womenCategory).perform();
        baseTest.logger.info("hovered over the women category");

        baseTest.getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.className("_opened_ali1iz")));
        clickOnRandomLink(By.cssSelector("._opened_ali1iz a[data-test-mm-column-link]"), baseTest);

        baseTest.getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class^='_container'] h1")));
    }

    @Step("Close the popup if it appears")
    public static void closePopupIfAvailable(BaseTest baseTest) {
        try {
            baseTest.getWait10().until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".bloomreach-weblayer")))
                    .getShadowRoot()
                    .findElement(By.cssSelector("button.close"))
                    .click();
            baseTest.logger.info("the popup was closed");
        } catch (TimeoutException | NoSuchElementException e) {

        }
    }

    @Step("Click on a random item and add it to the cart")
    public static void clickOnRandomItemLink(BaseTest baseTest) {
        clickOnRandomLink(By.xpath("//img[@data-test='product-image']/ancestor::a"), baseTest);

        baseTest.getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1[data-testid='product-name']")));
    }

    @Step("Click on a random item via the Quick Shop button and add it to the cart")
    public static void addRandomItemToCartViaQuickShopButton(WebDriver driver, Actions actions, BaseTest baseTest) {
        List<WebElement> products = driver.findElements(By.cssSelector("[data-testid='media']"));
        int randomIndex = new Random().nextInt(products.size());

        actions.moveToElement(products.get(randomIndex)).perform();

        List<WebElement> elements = driver.findElements(By.cssSelector("a.clickable.qa-show-sidetray-quickview"));

        CommonUtils.scrollAndClickWithJS(driver, elements.get(randomIndex));
        baseTest.getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[data-test-item-image]")));
    }

    @Step("Calculate the final price with discount if available")
    public static void calculatePriceWithDiscountIfAvailable(WebDriver driver, double itemPrice) {
        try {
            driver.findElement(PROMO_LOCATOR).isDisplayed();
            double discount = convertFromStringToDouble(driver, PROMO_LOCATOR);
            itemPrice = roundTo2Decimals(getDiscountedValue(itemPrice, discount));
        } catch (Exception e) {

        }
    }

    @Step("Click on the Add to bag button")
    public static void clickOnAddToBagButton(BaseTest baseTest) {
        baseTest.getWait10().until(ExpectedConditions.elementToBeClickable(By.className("qa-btn-add-to-bag"))).click();
    }

    @Step("Click on the View Bag button")
    public static void clickOnViewBagButton(BaseTest baseTest) {
        baseTest.getWait10().until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-test-view-cart]"))).click();

        baseTest.getWait30().until(ExpectedConditions.urlContains(CART_ENDPOINT));
    }

    public static String getSkuIdByIndex(List<Item> items, int index) {
        return items
                .stream()
                .map(item -> item.getSkuId())
                .toList()
                .get(index);
    }

    public static int getItemQuantityByIndex(List<Item> items, int index) {
        return items
                .stream()
                .map(item -> item.getQuantity())
                .toList()
                .get(index);
    }
}
