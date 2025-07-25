package testData;

import com.github.javafaker.Faker;
import config.TestPropertiesConfig;
import models.Item;
import org.aeonbits.owner.ConfigFactory;

import java.util.Comparator;
import java.util.List;

public class TestData {
    public static final TestPropertiesConfig PROPERTIES_CONFIG = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());
    public static final String API_BASE_URL = "https://www.ae.com/ugp-api/";
    public static final String UI_BASE_URL = "https://www.ae.com/us/en";
    public static final String CART_ENDPOINT = "/cart";
    public final static String EMPTY_CART_MESSAGE = "Your bag is empty. Find something you love!";
    private static final Faker FAKER = new Faker();

    public static String generateRandomEmail() {
        return FAKER.internet().emailAddress();
    }

    public static String generateRandomFirstName() {
        return FAKER.name().firstName();
    }

    public static String generateRandomLastName() {
        return FAKER.name().lastName();
    }

    public static String generateRandomPassword() {
        return FAKER.internet().password(8, 25, true, true, true);
    }

    public static String generateRandomStreet() {
        return FAKER.address().streetAddress();
    }

    public static List<Item> items = List.of(
           Item.builder()
                   .quantity(3)
                   .skuId("0043385095")
                   .build(),
           Item.builder()
                   .quantity(1)
                   .skuId("0043385079")
                   .build())
           .stream()
           .sorted(Comparator.comparing(Item::getSkuId))
           .toList();
}
