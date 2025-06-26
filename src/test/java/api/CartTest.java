package api;

import controllers.CartController;
import controllers.ProductController;
import models.CartItem;
import models.CartPayloadData;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import testData.ProductId;
import testData.TestData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class CartTest {
    CartController cartController = new CartController();
    CartPayloadData cartPayloadData = new CartPayloadData();
    ProductController productController = new ProductController();

    private static final int ITEM_QUANTITY = 3;

    @Test
    @Order(1)
    public void addToCartAsGuestTest() {
        cartPayloadData.setItems(TestData.getListOfProducts(ProductId.PRODUCT_ID_1, ITEM_QUANTITY));
        cartController.addItemToCart(cartPayloadData)
             .then()
             .statusCode(202)
             .body("cartId", Matchers.notNullValue());
    }

    @Test
    @Order(2)
    public void getCartItemsCountTest() {
        cartController.getCartItemsCount()
                .then()
                .statusCode(200)
                .body("data.itemCount", Matchers.equalTo(ITEM_QUANTITY));
    }

    @Test
    @Order(3)
    public void testOriginalTotalPrice() {
        List<CartItem> cartItems = cartController.getCartData()
                        .then().log().body()
                        .statusCode(200)
                                .extract().body().jsonPath().getList("data.items", CartItem.class);

        double price = productController.getProductById(ProductId.PRODUCT_ID_1).getSkus().get(0).getSalePrice() * ITEM_QUANTITY;
        BigDecimal expectedPrice = new BigDecimal(price);
        expectedPrice = expectedPrice.setScale(2, RoundingMode.HALF_EVEN);

        Assertions.assertEquals(expectedPrice, BigDecimal.valueOf(cartItems.get(0).getOriginalPrice()));
    }
}
