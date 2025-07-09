package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static utils.TestUtils.getWait10;

public class CommonUtils {

    public static void scrollToItemWithJS(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'start'});", element);
    }

    public static void scrollAndClickWithJS(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public static void scrollByViewportPercentage(WebDriver driver, int scrollValue) {
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollBy(0, window.innerHeight * arguments[0] / 100);",
                scrollValue);
    }

    public static void hoverOverElementWithJS(WebDriver driver, WebElement element) {
        String script =
                "var element = arguments[0]; " +
                        "var mouseOverEvent = new MouseEvent('mouseover', { " +
                        "   bubbles: true, " +
                        "   cancelable: true, " +
                        "   view: window " +
                        "}); " +
                        "element.dispatchEvent(mouseOverEvent); " +

                        "var mouseEnterEvent = new MouseEvent('mouseenter', { " +
                        "   bubbles: true, " +
                        "   cancelable: true " +
                        "}); " +
                        "element.dispatchEvent(mouseEnterEvent);";

        ((JavascriptExecutor) driver).executeScript(script, element);
    }

    public static double roundTo2Decimals(double number) {
        return Math.round(number * 100.0) / 100.0;
    }


    public static double getDiscountedValue(double originalValue, double discountPercent) {
        return originalValue * (1 - discountPercent / 100.0);
    }

    public static double convertFromStringToDouble(WebDriver driver, By locator) {
        return Double.parseDouble(getWait10(driver)
                .until(ExpectedConditions.visibilityOfElementLocated(locator))
                .getText()
                .replaceAll("[^0-9.]", ""));
    }
}
