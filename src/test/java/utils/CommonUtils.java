package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Random;

import static utils.TestUtils.getWait30;

public class CommonUtils {
    private static final Random random = new Random();

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

    public static double roundTo2Decimals(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    public static double roundTo1Decimals(double number) {
        return (long)(number * 10) / 10.0;
    }

    public static double getDiscountedValue(double originalValue, double discountPercent) {
        return originalValue * (1 - discountPercent / 100.0);
    }

    public static double convertFromStringToDouble(WebDriver driver, By locator) {
        return Double.parseDouble(getWait30(driver)
                .until(ExpectedConditions.visibilityOfElementLocated(locator))
                .getText()
                .replaceAll("[^0-9.]", ""));
    }

    public static int getRandomValueBetween1And12() {
        return random.nextInt(12) + 1;
    }

    public static int getRandomValueBetween1And27() {
        return random.nextInt(27) + 1;
    }
}
