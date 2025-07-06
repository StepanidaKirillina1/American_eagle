package ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestUtils;

import java.time.Duration;
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

    @Test
    public void addSmokeTest() {
        getWait10().until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.sidetray-account"))).click();

        String actualTitle = getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h2.modal-title"))).getText();

        Assertions.assertEquals("Account", actualTitle);
    }

    @Test
    public void addItemToCartViaQuickShopButton() {
        //scrollToRandomLink(actions, driver, By.className("_top-link_ali1iz"));
        //TestUtils.scrollAndHoverOverElement(driver, driver.findElement(By.className("_top-link_ali1iz")));
        actions.moveToElement(getWait20().until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[data-test-mm-top-link]")))).perform();
        //TestUtils.hoverElementWithJS(driver, driver.findElement(By.cssSelector("a[data-test-mm-top-link]")));
        //getWait60().until(ExpectedConditions.elementToBeClickable(By.className("_top-link_ali1iz"))).click();
        getWait10().until(ExpectedConditions.visibilityOfElementLocated(By.className("_opened_ali1iz")));
        TestUtils.clickOnRandomLink(driver, By.cssSelector("._opened_ali1iz a[data-test-mm-column-link]"));

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
        driver.findElement(By.name("add-to-bag")).click();
        getWait5().until(ExpectedConditions.elementToBeClickable(By.cssSelector(".dropdown-selection.open")));
        getAvailableSize().click();

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
                actions.moveToElement(sizes.get(sizes.size()-1));
                return size;
            }
        }

        throw new NoSuchElementException("All sizes are out of stock");
    }

//    public void scrollToRandomLink(Actions actions, WebDriver driver, By locator) {
//        //List<WebElement> elements = driver.findElements(By.className("_top-link_ali1iz"));
//        List<WebElement> elements = new WebDriverWait(driver, Duration.ofSeconds(30))
//                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
//
//        int randomIndex = new Random().nextInt(elements.size());
//
//        System.out.println(elements.get(randomIndex).getText());
//
//        actions.moveToElement(elements.get(randomIndex)).perform();
//    }
}
