package testData;

import models.Item;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    public static final String API_BASE_URL = "https://www.ae.com/ugp-api/";
    public static final String UI_BASE_URL = "https://www.ae.com/us/en";
    public static String cartEndpoint = "/cart";

    public static List<Item> getListOfProducts(ProductId productId, int quantity) {
        List<Item> listOfJProducts = new ArrayList<>();
        listOfJProducts.add(Item.builder().skuId(productId.getSkuId()).quantity(quantity).build());

        return listOfJProducts;
    }
}
