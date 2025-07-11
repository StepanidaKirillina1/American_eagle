package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static utils.CommonUtils.*;

public class TestUtils {
    private static final String OUT_OF_STOCK_TEXT = "Out of Stock Online";
    private static final By ITEM_LINK_LOCATOR = By.cssSelector("a[role='menuitem']");
    private static final By PROMO_LOCATOR = By.cssSelector("li.qa-promo-item");

    public static WebDriverWait getWait5(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public static WebDriverWait getWait10(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public static WebDriverWait getWait30(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public static WebDriverWait getWait60(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    public static void getFirstAvailableSize(WebDriver driver) {
        CommonUtils.scrollToItemWithJS(driver, driver.findElement(By.xpath("//div[text()='Color:']")));

        getWait5(driver).until(ExpectedConditions.elementToBeClickable(By.className("dropdown"))).click();
        getWait10(driver).until(ExpectedConditions.elementToBeClickable(By.cssSelector(".dropdown-selection.open")));
        getWait10(driver).until(ExpectedConditions.elementToBeClickable(ITEM_LINK_LOCATOR));

        List<WebElement> sizes = driver.findElements(ITEM_LINK_LOCATOR);

        for (WebElement size : sizes) {
            if (!size.getText().contains(OUT_OF_STOCK_TEXT)) {
                size.click();
                return;
            }
        }

        throw new NoSuchElementException("All sizes are out of stock");
    }

    public static void clickOnRandomLink(By locator, WebDriver driver) {
        List<WebElement> elements = getWait30(driver).until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        int randomIndex = new Random().nextInt(elements.size());

        elements.get(randomIndex).click();
    }

    public static void clickOnRandomWomenCategoryItem(WebDriver driver) {
        WebElement womenCategory = getWait60(driver).until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[data-text='Women']")));
        new Actions(driver).moveToElement(womenCategory).perform();

        getWait30(driver).until(ExpectedConditions.visibilityOfElementLocated(By.className("_opened_ali1iz")));
        clickOnRandomLink(By.cssSelector("._opened_ali1iz a[data-test-mm-column-link]"), driver);

        getWait10(driver).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class^='_container'] h1")));
    }

    public static void closePopupIfAvailable(WebDriver driver) {
        try {
            getWait10(driver).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".bloomreach-weblayer")))
                    .getShadowRoot()
                    .findElement(By.cssSelector("button.close"))
                    .click();
        } catch (TimeoutException | NoSuchElementException e) {

        }
    }

    public static void clickOnRandomItemLink(WebDriver driver) {
        clickOnRandomLink(By.xpath("//img[@data-test='product-image']/ancestor::a"), driver);

        getWait10(driver).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1[data-testid='product-name']")));
    }

    public static void addRandomItemToCartViaQuickShopButton(WebDriver driver, Actions actions) {
        List<WebElement> products = driver.findElements(By.cssSelector("[data-testid='media']"));
        int randomIndex = new Random().nextInt(products.size());

        actions.moveToElement(products.get(randomIndex)).perform();

        List<WebElement> elements = driver.findElements(By.cssSelector("a.clickable.qa-show-sidetray-quickview"));

        CommonUtils.scrollAndClickWithJS(driver, elements.get(randomIndex));
        getWait10(driver).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[data-test-item-image]")));
    }

    public static void calculatePriceWithDiscountIfAvailable(WebDriver driver, double itemPrice) {
        try {
            driver.findElement(PROMO_LOCATOR).isDisplayed();
            double discount = convertFromStringToDouble(driver, PROMO_LOCATOR);
            itemPrice = roundTo2Decimals(getDiscountedValue(itemPrice, discount));
        } catch (Exception e) {

        }
    }
}
