package controllers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.*;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.List;

import static io.restassured.RestAssured.given;
import static testData.TestData.API_BASE_URL;

public class CartController {
    RequestSpecification requestSpecification;
    TokenController tokenController = new TokenController();
    private static final String CART_ENDPOINT = "bag/v1";

    public CartController() {
        this.requestSpecification = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("Aesite", "AEO_US")
                .header("Aelang", "en_US")
                .header("Aecountry", "US")
                //.header("x-access-token", tokenController.getGuestToken())
                .header("Authorization", "Bearer " + tokenController.getGuestToken())
                .baseUri(API_BASE_URL);
    }

    public CartResponseData addItemToCart(CartPayloadData items) {
        return given(requestSpecification)
                .body(items)
                .when()
                .post(CART_ENDPOINT + "/items")
                .then()
                .statusCode(202)
                .body("cartId", Matchers.notNullValue())
                .extract().as(CartResponseData.class);
    }

    public CartResponseData removeItemFromCart(String itemId) {
        return given(requestSpecification)
                .when()
                .delete(CART_ENDPOINT + "/items?itemIds=" + itemId)
                .then()
                .statusCode(202)
                .extract().as(CartResponseData.class);
    }

    public CartData getCartItemsCount() {
        return given(requestSpecification)
                .when()
                .get(CART_ENDPOINT + "/count")
                .then().log().body()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", CartData.class);
    }

    public List<CartItem> getCartItemData() {
        return given(requestSpecification)
                .when()
                .get(CART_ENDPOINT + "?couponErrorBehavior=cart&inventoryCheck=true")
                .then().log().body()
                .statusCode(200)
                .extract().body().jsonPath().getList("data.items", CartItem.class);
    }

    public OrderSummary getOrderSummary() {
        return given(requestSpecification)
                .when()
                .get(CART_ENDPOINT + "?couponErrorBehavior=cart&inventoryCheck=true")
                .then().log().body()
                .statusCode(200)
                .extract().body().jsonPath().getObject("data.summary", OrderSummary.class);
    }
}
