package ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class HomePageTest extends BaseTest {

    @Test
    public void homePageTest() {
        Assertions.assertTrue(driver.getTitle().contains("American Eagle"));
    }

    @Test
    @Tags({@Tag("UI"), @Tag("Smoke")})
    public void isCategoriesListClickable() {
        List<String> expectedCategories = List.of(
                "Women", "Men", "Jeans", "Shoes & Accessories", "Loungewear & PJs", "Aerie", "Clearance"
        );

        List<String> actualCategories = getWait30()
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("_top-link_ali1iz")))
                .stream()
                .map(element -> element.getText())
                .toList();

        Assertions.assertEquals(expectedCategories, actualCategories);
    }
}
