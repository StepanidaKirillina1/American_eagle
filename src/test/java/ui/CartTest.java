package ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.TestUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class CartTest extends BaseTest {
    Actions actions;
    private static final String OUT_OF_STOCK_TEXT = "Out of Stock Online";

    @BeforeEach
    public void setUp() {
        super.setUp();
        actions = new Actions(driver);
    }

    @Test
    public void homePageTest() {
        Assertions.assertTrue(driver.getTitle().contains("American Eagle"));
    }

    @RepeatedTest(2)
    public void addItemToCart() throws Exception {
        actions.moveToElement(getWait20().until(ExpectedConditions.elementToBeClickable(By.cssSelector("a._top-link_ali1iz")))).perform();
        getWait20().until(ExpectedConditions.visibilityOfElementLocated(By.className("_opened_ali1iz")));
        clickOnRandomLink(By.cssSelector("._opened_ali1iz a[data-test-mm-column-link]"));

        try {
            getWait10().until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".bloomreach-weblayer")))
                    .getShadowRoot()
                    .findElement(By.cssSelector("button.close"))
                    .click();
        } catch (TimeoutException | NoSuchElementException e) {
        }

        int viewportHeight = driver.manage().window().getSize().getHeight();
        actions.moveByOffset(0, viewportHeight / 3);
        clickOnRandomLink(By.xpath("//img[@data-test='product-image']/ancestor::a"));

        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1[data-testid='product-name']")));
        TestUtils.scrollToItemWithJS(driver, driver.findElement(By.cssSelector("[data-test-extras='reviews']")));

        if(driver.findElement(By.className("dropdown-text")).getText().contains("One Size")) {
            driver.findElement(By.xpath("//button[@name='addToBag']")).click();
        } else {
            getWait20().until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@name='addToBag']"))).click();
            getWait10().until(ExpectedConditions.elementToBeClickable(By.cssSelector(".dropdown-selection.open")));
            getWait10().until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[role='menuitem']")));
            getAvailableSize().click();
        }

        WebElement modalDialog = getWait10()
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='modal-dialog'][not(@quickview)]")));

        Assertions.assertEquals("Added to bag!", modalDialog.findElement(By.tagName("h2")).getText());
    }

    @RepeatedTest(2)
    public void addItemToCartViaQuickShopButton() throws InterruptedException {
        actions.moveToElement(getWait20().until(ExpectedConditions.elementToBeClickable(By.cssSelector("a._top-link_ali1iz")))).perform();
        getWait20().until(ExpectedConditions.visibilityOfElementLocated(By.className("_opened_ali1iz")));
        clickOnRandomLink(By.cssSelector("._opened_ali1iz a[data-test-mm-column-link]"));

        try {
            getWait10().until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".bloomreach-weblayer")))
                    .getShadowRoot()
                    .findElement(By.cssSelector("button.close"))
                    .click();
        } catch (TimeoutException | NoSuchElementException e) {
        }

        int viewportHeight = driver.manage().window().getSize().getHeight();
        actions.moveByOffset(0, viewportHeight / 3);
        clickOnQuickShopButton(actions, driver);

        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[data-test-item-image]")));
        TestUtils.scrollToItemWithJS(driver, driver.findElement(By.cssSelector("a.qa-all-details-btn")));

        if(driver.findElement(By.className("dropdown-text")).getText().contains("One Size")) {
            driver.findElement(By.name("add-to-bag")).click();
        } else {
            driver.findElement(By.name("add-to-bag")).click();
            getWait10().until(ExpectedConditions.elementToBeClickable(By.cssSelector(".dropdown-selection.open")));
            getWait10().until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[role='menuitem']")));
            getAvailableSize().click();
        }

        WebElement modalDialog = getWait10()
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='modal-dialog'][not(@quickview)]")));

        Assertions.assertEquals("Added to bag!", modalDialog.findElement(By.tagName("h2")).getText());
   }

   public void clickOnQuickShopButton(Actions actions, WebDriver driver) {
       List<WebElement> products = driver.findElements(By.cssSelector("[data-testid='media']"));
       int randomIndex = new Random().nextInt(products.size());

       actions.moveToElement(products.get(randomIndex)).perform();

       List<WebElement> elements = driver.findElements(By.cssSelector("a.clickable.qa-show-sidetray-quickview"));

       getWait10().until(ExpectedConditions.elementToBeClickable(elements.get(randomIndex))).click();
   }

    public WebElement getAvailableSize() {
        List<WebElement> sizes = driver.findElements(By.cssSelector("a[role='menuitem']"));

        for (WebElement size : sizes) {
            if (!size.getText().contains(OUT_OF_STOCK_TEXT)) {
                return size;
            }
        }

        throw new NoSuchElementException("All sizes are out of stock");
    }

    public void clickOnRandomLink(By locator) {
        List<WebElement> elements = getWait20().until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        int randomIndex = new Random().nextInt(elements.size());

        elements.get(randomIndex).click();
    }
}
