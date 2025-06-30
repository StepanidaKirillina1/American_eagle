package controllers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.CartPayloadData;
import models.CartResponseData;
import models.Item;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static testData.TestData.API_BASE_URL;

public class CartController {
    RequestSpecification requestSpecification;
    TokenController tokenController = new TokenController();
    private static final String CART_ENDPOINT = "bag/v1";
    List<Item> items = new ArrayList<>();

    public CartController() {
        this.requestSpecification = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("Aesite", "AEO_US")
                .header("Aelang", "en_US")
                .header("Aecountry", "US")
                //.header("x-access-token", tokenController.getToken())
                .header("Authorization", "Bearer " + tokenController.getToken())
                .baseUri(API_BASE_URL);
    }

    public CartResponseData addItemToCart(CartPayloadData items) {
        return given(requestSpecification)
                .body(items)
                .when()
                .post(CART_ENDPOINT + "/items")
                .then()
                .extract().as(CartResponseData.class);
    }

    public CartResponseData removeItemFromCart(String itemId) {
        return given(requestSpecification)
                .when()
                .delete(CART_ENDPOINT + "/items?itemIds=" + itemId)
                .then()
                .extract().as(CartResponseData.class);
    }

    public Response getCartItemsCount() {
        return given(requestSpecification)
                .when()
                .get(CART_ENDPOINT + "/count")
                .then()
                .extract().response();
    }

    public Response getCartData() {
        return given(requestSpecification)
                .when()
                .get(CART_ENDPOINT + "?couponErrorBehavior=cart&inventoryCheck=true")
                .then()
                .extract().response();
    }
}
