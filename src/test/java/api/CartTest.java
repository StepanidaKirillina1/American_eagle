package api;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import models.*;
import org.junit.jupiter.api.*;
import steps.CartSteps;
import testData.TestData;
import utils.TestUtils;

import java.util.List;

@Epic("API")
@Feature("Cart")
public class CartTest {
    CartSteps cartSteps;

    @BeforeEach
    public void setUp() {
        cartSteps = new CartSteps();
        cartSteps.addItemToCart(TestData.items);
    }

    @Tags({@Tag("API"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void getCartItemsCountTest() throws Exception {
        int expectedIndividualItemQuantity = 0;
        int expectedTotalQuantity = 0;
        int actualTotalQuantity = 0;

        String addedItemSku = TestUtils.getSkuIdByIndex(TestData.items, 0);
        int addedItemQuantity = TestUtils.getItemQuantityByIndex(TestData.items, 0);

        CartData cartData = cartSteps.testCartData();
        List<ItemData> cartItemData = cartData.getItems();

        for (int i = 0; i < cartItemData.size(); i++) {
            ItemData itemData = cartItemData.get(i);

            if (itemData.getSku().equals(addedItemSku)) {
                expectedIndividualItemQuantity = itemData.getQuantity();
            }

            expectedTotalQuantity += itemData.getQuantity();
        }

        for (Item item: TestData.items) {
            actualTotalQuantity += item.getQuantity();
        }

        Assertions.assertEquals(addedItemQuantity, expectedIndividualItemQuantity);
        Assertions.assertEquals(actualTotalQuantity, expectedTotalQuantity);
    }

    @Tags({@Tag("API"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void testTotalOrderPriceWithDiscount() {
        double expectedTotalSumWithoutShipping = 0.0;
        List<CartItem> cartItems = cartSteps.getCartItemData();

        for (int i = 0; i < TestData.items.size(); i++) {
            if (cartItems.get(i).getSku().equals(TestData.items.get(i).getSkuId())) {
                expectedTotalSumWithoutShipping = cartSteps.getTotalSumWithoutShipping(cartItems);
            }
        }

        double orderSummaryTotalWithoutShipping = cartSteps.getOrderSummaryTotalWithoutShipping();

        Assertions.assertEquals(expectedTotalSumWithoutShipping, orderSummaryTotalWithoutShipping);
    }

    @Tags({@Tag("API"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void removeAllItemsFromCart() {
        List<CartItem> cartItems = cartSteps.getCartItemData();

        for (CartItem cartItem: cartItems) {
            cartSteps.removeItemFromCart(cartItem.getItemId());
        }

        Assertions.assertEquals(0, cartSteps.getCartItemData().size());
    }

    @Tags({@Tag("API"), @Tag("Critical"), @Tag("Positive")})
    @Test
    public void editCartItems() {
        String itemId = cartSteps.getItemIdBySku("0043385095");
        int itemQuantity = 5;

        List<EditItem> editItems = List.of(
                EditItem.builder()
                        .itemId(itemId)
                        .quantity(itemQuantity)
                        .skuId("0043385095")
                        .build()
        );

        cartSteps.editCartItems(editItems);

        int updatedItemQuantity = cartSteps.getCartItemData().stream()
                .filter(item -> item.getSku().equals("0043385095"))
                .findFirst()
                .map(CartItem::getQuantity)
                .orElse(0);

        Assertions.assertEquals(itemQuantity, updatedItemQuantity);
    }
}
