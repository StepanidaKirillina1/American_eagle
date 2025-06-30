package controllers;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import models.Product;
import testData.ProductId;

import static io.restassured.RestAssured.given;
import static testData.TestData.API_BASE_URL;

public class ProductController {

    RequestSpecification requestSpecification;
    TokenController tokenController = new TokenController();
    private static final String PRODUCT_ENDPOINT = "catalog/v1/product/sizes";

    public ProductController() {
        this.requestSpecification = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("Aesite", "AEO_US")
                .header("Aelang", "en_US")
                .header("Aecountry", "US")
                //.header("x-access-token", tokenController.getToken())
                .header("Authorization", "Bearer " + tokenController.getGuestToken())
                .baseUri(API_BASE_URL);
    }

    public Product getProductById(ProductId productId) {
        return given(requestSpecification)
                .when()
                .get(PRODUCT_ENDPOINT + "?productIds=" + productId.getProductId())
                .then().log().body()
                .extract().body().jsonPath().getObject("data.records[0].sizes", Product.class);
    }
}
