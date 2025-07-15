package utils;

import io.qameta.allure.Step;
import models.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import testData.TestData;
import ui.BaseTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static utils.CommonUtils.*;

public class TestUtils {
    private static final String OUT_OF_STOCK_TEXT = "Out of Stock Online";
    private static final By ITEM_LINK_LOCATOR = By.cssSelector("a[role='menuitem']");
    private static final By PROMO_LOCATOR = By.cssSelector("li.qa-promo-item");
    private static WebElement womenCategory;
    public static Logger logger = LogManager.getLogger();

    @Step("Get the first available size of the item")
    public static void getFirstAvailableSize(BaseTest baseTest, WebDriver driver) {
        CommonUtils.scrollToItemWithJS(driver, driver.findElement(By.xpath("//div[text()='Price:']")));

        if (baseTest
                .getWait5()
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Price:']/..//div[@data-test-product-prices]")))
                .getText()
                .toLowerCase()
                .contains("sold")) {
            CommonUtils.scrollAndClickWithJS(driver, driver.findElement(By.className("btn-close")));
            clickOnRandomItemLink(baseTest);
        } else {
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
    }

    private static void clickOnRandomLink(By locator, BaseTest baseTest) {
        List<WebElement> elements = baseTest.getWait30().until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        int randomIndex = new Random().nextInt(elements.size());

        elements.get(randomIndex).click();
    }

    @Step("Click on a random women category item")
    public static void clickOnRandomWomenCategoryItem(WebDriver driver, BaseTest baseTest) {
        try {womenCategory = baseTest
                    .getWait60()
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[data-text='Women']")));
        } catch (Exception e) {
            driver.navigate().refresh();
            womenCategory = baseTest
                    .getWait60()
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[data-text='Women']")));
        }

        new Actions(driver).moveToElement(womenCategory).perform();
        baseTest.logger.info("hovered over the women category");

        baseTest.getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.className("_opened_ali1iz")));
        clickOnRandomLink(By.cssSelector("._opened_ali1iz a[data-test-mm-column-link]"), baseTest);

        baseTest.getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class^='_container'] h1")));
    }

    @Step("Close the popup if it appears")
    public static boolean closePopupIfAvailable(BaseTest baseTest) {
        int popupCounter = 0;
        try {
            baseTest.getWait10().until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".bloomreach-weblayer")))
                    .getShadowRoot()
                    .findElement(By.cssSelector("button.close"))
                    .click();
            popupCounter++;

        } catch (TimeoutException | NoSuchElementException e) {

        }

        return popupCounter != 0;
    }

    @Step("Click on a random item and add it to the cart")
    public static void clickOnRandomItemLink(BaseTest baseTest) {
        clickOnRandomLink(By.xpath("//img[@data-test='product-image']/ancestor::a"), baseTest);

        baseTest.getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1[data-testid='product-name']")));
    }

    @Step("Click on a random item via the Quick Shop button and add it to the cart")
    public static void addFirstItemToCartViaQuickShopButton(WebDriver driver, Actions actions, BaseTest baseTest) {
        actions.moveToElement(driver.findElement(By.cssSelector("[data-testid='media']"))).perform();
        CommonUtils.scrollAndClickWithJS(driver, driver.findElements(By.cssSelector("a.clickable.qa-show-sidetray-quickview")).getFirst());

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
    public static void clickOnViewBagButton(BaseTest baseTest, WebDriver driver) {
        baseTest.getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='modal-dialog'][not(@quickview)]")));

        CommonUtils.scrollAndClickWithJS(driver, driver.findElement(By.cssSelector("button[data-test-view-cart]")));
        logger.info("view button was clicked");

        baseTest.getWait30().until(ExpectedConditions.visibilityOfElementLocated(By.name("loginMessage")));
        logger.info(driver.getCurrentUrl());
    }

    @Step("Remove item from the cart")
    public static void removeItemFromCart(BaseTest baseTest) {
        baseTest.getWait10().until(ExpectedConditions.elementToBeClickable(By.name("removeCommerceItem"))).click();
    }

    @Step("Fill the email field")
    public static void fillEmailField(BaseTest baseTest) {
        baseTest.getWait10()
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[placeholder='Email']")))
                .sendKeys(TestData.generateRandomEmail());
    }

    @Step("Fill the first name field")
    public static void fillFirstNameField(WebDriver driver) {
        driver.findElement(By.name("firstname")).sendKeys(TestData.generateRandomFirstName());
    }

    @Step("Fill the last name field")
    public static void fillLastNameField(WebDriver driver) {
        driver.findElement(By.name("lastname")).sendKeys(TestData.generateRandomLastName());
    }

    @Step("Fill the zip code field")
    public static void fillZipCodeField(WebDriver driver) {
        driver.findElement(By.cssSelector("[placeholder='Zip Code']")).sendKeys("99645");
    }

    @Step("Click on the counter button between 1 and 9")
    public static int clickOnCounterBetween1and9(int counterClickNumber, BaseTest baseTest) {
        counterClickNumber = new Random().nextInt(9) + 1;
        WebElement counter = baseTest.getWait10().until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='increase']")));

        for (int i = 1; i <= counterClickNumber; i++) {
            counter.click();
        }

        return counterClickNumber;
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
