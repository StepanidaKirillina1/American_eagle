package controllers;

import filter.SecureAllureFilter;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import models.*;
import org.hamcrest.Matchers;

import java.util.List;

import static io.restassured.RestAssured.given;
import static testData.TestData.API_BASE_URL;

public class CartController {
    RequestSpecification requestSpecification;
    TokenController tokenController = new TokenController();
    private static final String CART_ENDPOINT = "bag/v1";
    private static final String ITEMS_ENDPOINT = "/items";
    private static final String CART_PARAMETERES = "?couponErrorBehavior=cart&inventoryCheck=true";

    public CartController() {
        this.requestSpecification = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("Aesite", "AEO_US")
                .header("Aelang", "en_US")
                .header("Aecountry", "US")
                .header("Authorization", "Bearer " + tokenController.getGuestToken())
                .baseUri(API_BASE_URL)
                .filter(new SecureAllureFilter());;
    }

    public CartResponseData addItemToCart(CartPayloadData items) {
        return given(requestSpecification)
                .body(items)
                .when()
                .post(CART_ENDPOINT + ITEMS_ENDPOINT)
                .then()
                .statusCode(202)
                .body("cartId", Matchers.notNullValue())
                .extract().as(CartResponseData.class);
    }

    public CartResponseData editCartItems(CartEditData items) {
        return given(requestSpecification)
                .body(items)
                .when()
                .patch(CART_ENDPOINT + ITEMS_ENDPOINT)
                .then()
                .statusCode(202)
                .body("cartId", Matchers.notNullValue())
                .extract().as(CartResponseData.class);
    }

    public CartResponseData removeItemFromCart(String itemId) {
        return given(requestSpecification)
                .when()
                .delete(CART_ENDPOINT + ITEMS_ENDPOINT + "?itemIds=" + itemId)
                .then()
                .statusCode(202)
                .extract().as(CartResponseData.class);
    }

    public CartData getCartItemsCount() {
        return given(requestSpecification)
                .when()
                .get(CART_ENDPOINT + "/count")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", CartData.class);
    }

    public List<CartItem> getCartItemData() {
        return given(requestSpecification)
                .when()
                .get(CART_ENDPOINT + CART_PARAMETERES)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("data.items", CartItem.class);
    }

    public OrderSummary getOrderSummary() {
        return given(requestSpecification)
                .when()
                .get(CART_ENDPOINT + CART_PARAMETERES)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject("data.summary", OrderSummary.class);
    }
}
