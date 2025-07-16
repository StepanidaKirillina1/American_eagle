package ui;

import extenstions.AllureExtension;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@Epic("UI")
@Feature("HomePage")
@ExtendWith(AllureExtension.class)
public class HomePageTest extends BaseTest {

    @Test
    @Tags({@Tag("UI"), @Tag("Smoke"), @Tag("Positive")})
    public void homePageTest() {
        Assertions.assertTrue(driver.getTitle().contains("American Eagle"));
    }

    @Test
    @Tags({@Tag("UI"), @Tag("Smoke"), @Tag("Positive")})
    public void isCategoriesTextVisible() {
        List<String> expectedCategories = List.of(
                "Today's Offers", "Women", "Men", "Jeans", "Shoes & Accessories", "Loungewear & PJs", "Aerie", "Clearance"
        );

        List<String> actualCategories = getWait30()
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("li[data-test='top-link-wrapper']")))
                .stream()
                .map(element -> element.getText())
                .toList();

        for (String category: actualCategories) {
            if(category.contains("Today's Offers")) {
                category = category.substring(0, 14);
            }
        }

        Assertions.assertEquals(expectedCategories, actualCategories);
    }
}
