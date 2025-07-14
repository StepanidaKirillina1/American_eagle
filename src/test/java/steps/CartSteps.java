package steps;

import controllers.CartController;
import io.qameta.allure.Step;
import models.*;

import java.util.Comparator;
import java.util.List;

public class CartSteps {
    CartController cartController = new CartController();
    CartPayloadData cartPayloadData = new CartPayloadData();
    CartEditData cartEditData = new CartEditData();

    @Step("Add items to the cart")
    public CartResponseData addItemToCart(List<Item> items) {
        cartPayloadData.setItems(items);

        return cartController.addItemToCart(cartPayloadData);
    }

    @Step("Edit cart items")
    public CartResponseData editCartItems(List<EditItem> items) {
        cartEditData.setItems(items);

        return cartController.editCartItems(cartEditData);
    }

    @Step("Get the cart data")
    public CartData testCartData() {
        return cartController.getCartItemsCount();
    }

    @Step("Get cart item data")
    public List<CartItem> getCartItemData() {
        return cartController
                .getCartItemData()
                .stream()
                .sorted(Comparator.comparing(CartItem::getSku))
                .toList();
    }

    public String getItemIdBySku(String sku) {
        return cartController.getCartItemData().stream()
                .filter(item -> item.getSku().equals(sku))
                .findFirst()
                .map(CartItem::getItemId)
                .orElseThrow(() -> new RuntimeException("No item found with this sku " + sku));
    }

    @Step("Calculate the total sum with discount and without shipping")
    public double getTotalSumWithoutShipping(List<CartItem> cartItems) {
        double totalSumWithoutShipping = 0.0;

        for (CartItem cartItem : cartItems) {
            totalSumWithoutShipping += cartItem.getUnitSalePrice() * cartItem.getQuantity() - cartItem.getDiscount();
        }

        return totalSumWithoutShipping;
    }

    @Step("Calculate the total order sum without discount and shipping")
    public double getOrderSummaryTotalWithoutShipping() {
        OrderSummary orderSummary = cartController.getOrderSummary();
        return orderSummary.getSubtotal() - orderSummary.getDiscount() - orderSummary.getShipping();
    }

    @Step("Remove item from the cart")
    public void removeItemFromCart(String itemId) {
        cartController.removeItemFromCart(itemId);
    }
}
