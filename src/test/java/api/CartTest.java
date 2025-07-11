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
import utils.CommonUtils;

import java.util.List;

import static utils.CommonUtils.getDiscountedValue;
import static utils.CommonUtils.roundTo2Decimals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class CartTest {
    CartController cartController = new CartController();
    CartPayloadData cartPayloadData = new CartPayloadData();
    ProductController productController = new ProductController();
    List<CartItem> cartItems;

    private static final int ITEM_QUANTITY = 2;

    @Test
    @Order(1)
    public void addToCartAsGuestTest() {
        cartPayloadData.setItems(TestData.getListOfProducts(ProductId.PRODUCT_ID_2, ITEM_QUANTITY));

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
        double expectedPrice = 0.0;
        cartItems = cartController.getCartData()
                .then().log().body()
                .statusCode(200)
                .extract().body().jsonPath().getList("data.items", CartItem.class);

        List<Sku> skus = productController.getProductById(ProductId.PRODUCT_ID_2).getSkus();

        for (Sku sku: skus) {
            if (ProductId.PRODUCT_ID_2.getSkuId().equals(sku.getSkuId())) {
                expectedPrice = CommonUtils.roundTo1Decimals(sku.getListPrice() * ITEM_QUANTITY);
            }
        }

        Assertions.assertEquals(expectedPrice, roundTo2Decimals(cartItems.get(0).getOriginalPrice()));
    }

    @Test
    @Order(4)
    public void testItemsPriceWithDiscount() {
        double expectedSalePrice = 0.0;

        CartItem firstCartItem = cartItems.get(0);

        double discountValue = getDiscountedValue(firstCartItem.getOriginalPrice(), firstCartItem.getDiscountPercent());
        double actualPriceWithDiscount = CommonUtils.roundTo1Decimals(discountValue);

        List<Sku> skus = productController.getProductById(ProductId.PRODUCT_ID_2).getSkus();

        for (Sku sku: skus) {
            if (ProductId.PRODUCT_ID_2.getSkuId().equals(sku.getSkuId())) {
                expectedSalePrice = (sku.getSalePrice() * ITEM_QUANTITY);
            }
        }

        Assertions.assertEquals(expectedSalePrice, actualPriceWithDiscount);
    }

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
