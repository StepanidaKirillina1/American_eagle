package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ui.BaseTest;

import java.util.List;
import java.util.Random;

public class TestUtils {

    public static void scrollToItemWithJS(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'start'});", element);
    }

    public static void scrollAndHoverOverElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor)driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});" +
                      "arguments[0].dispatchEvent(new MouseEvent('mouseover', {bubbles: true}));",
                element
        );
    }

    public static void clickOnRandomLink(WebDriver driver, By locator) {
        List<WebElement> elements = driver.findElements(locator);

        int randomIndex = new Random().nextInt(elements.size());

        System.out.println(elements.get(randomIndex).getText());

        elements.get(randomIndex).click();
    }

//    public void scrollToRandomLink(Actions actions, WebDriver driver, By locator) {
//        List<WebElement> elements = driver.findElements(locator);
//
//        int randomIndex = new Random().nextInt(elements.size());
//
//        System.out.println(elements.get(randomIndex).getText());
//
//        actions.moveToElement(elements.get(randomIndex)).perform();
//    }
}
