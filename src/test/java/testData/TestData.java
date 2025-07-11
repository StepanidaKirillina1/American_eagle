package testData;

import com.github.javafaker.Faker;
import config.TestPropertiesConfig;
import models.Item;
import org.aeonbits.owner.ConfigFactory;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    public static final TestPropertiesConfig PROPERTIES_CONFIG = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());
    public static final String API_BASE_URL = "https://www.ae.com/ugp-api/";
    public static final String UI_BASE_URL = "https://www.ae.com/us/en";
    public static final String CART_ENDPOINT = "/cart";
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

    public static List<Item> getListOfProducts(ProductId productId, int quantity) {
        List<Item> listOfJProducts = new ArrayList<>();
        listOfJProducts.add(Item.builder().skuId(productId.getSkuId()).quantity(quantity).build());

        return listOfJProducts;
    }
}
