package api;

import controllers.CartController;
import controllers.ProductController;
import models.CartItem;
import models.CartPayloadData;
import models.Sku;
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
    List<CartItem> cartItems;

    private static final int ITEM_QUANTITY = 1;

    @Test
    @Order(1)
    public void addToCartAsGuestTest() {
        cartPayloadData.setItems(TestData.getListOfProducts(ProductId.PRODUCT_ID_1, ITEM_QUANTITY));

        Assertions.assertFalse(cartController.addItemToCart(cartPayloadData).getCartId().isEmpty());
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
    public void testOriginalTotalPriceInCart() {
        BigDecimal expectedPrice = null;
        cartItems = cartController.getCartData()
                .then().log().body()
                .statusCode(200)
                .extract().body().jsonPath().getList("data.items", CartItem.class);

        List<Sku> skus = productController.getProductById(ProductId.PRODUCT_ID_1).getSkus();

        for (Sku sku: skus) {
            if (ProductId.PRODUCT_ID_1.getSkuId().equals(sku.getSkuId())) {
                expectedPrice = new BigDecimal((sku.getListPrice()) * ITEM_QUANTITY)
                        .setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        Assertions.assertEquals(expectedPrice, BigDecimal.valueOf(cartItems.get(0).getOriginalPrice()));
    }

    @Disabled
    @Test
    @Order(4)
    public void testItemsPriceWithDiscount() {
        double expectedSalePrice = 0.0;

        CartItem firstCartItem = cartItems.get(0);
        double originalPricePerItem = firstCartItem.getOriginalPrice() / ITEM_QUANTITY * (1 - firstCartItem.getDiscountPercent() / 100.0);
        double actualPriceWithDiscount = BigDecimal.valueOf(originalPricePerItem)
                .setScale(2, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(ITEM_QUANTITY))
                .doubleValue();

        List<Sku> skus = productController.getProductById(ProductId.PRODUCT_ID_1).getSkus();

        for (Sku sku: skus) {
            if (ProductId.PRODUCT_ID_1.getSkuId().equals(sku.getSkuId())) {
                expectedSalePrice = (sku.getSalePrice()) * ITEM_QUANTITY;
            }
        }

        Assertions.assertEquals(expectedSalePrice, actualPriceWithDiscount);
    }

    @Disabled
    @Test
    @Order(5)
    public void removeItemFromCart() {
        String firstCartItemId = cartItems.get(0).getItemId();

        Assertions.assertFalse(cartController.removeItemFromCart(firstCartItemId).getCartId().isEmpty());

        cartController.getCartData()
                .then().log().body()
                .statusCode(200)
                .body("data.items", Matchers.hasSize(0));
    }
}
