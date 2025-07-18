package controllers;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import models.Product;
import utils.TokenManager;

import static io.restassured.RestAssured.given;
import static testData.TestData.API_BASE_URL;

public class ProductController {

    RequestSpecification requestSpecification;
    private static final String PRODUCT_ENDPOINT = "catalog/v1/product/sizes";

    public ProductController() {
        this.requestSpecification = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("Aesite", "AEO_US")
                .header("Aelang", "en_US")
                .header("Aecountry", "US")
                .header("Authorization", "Bearer " + TokenManager.getTokenByRole())
                .baseUri(API_BASE_URL);
    }

    public Product getProductById(String productId) {
        return given(requestSpecification)
                .when()
                .get(PRODUCT_ENDPOINT + "?productIds=" + productId)
                .then().log().body()
                .extract().body().jsonPath().getObject("data.records[0].sizes", Product.class);
    }
}
