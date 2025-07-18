package controllers;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.TokenManager;

import static io.restassured.RestAssured.given;
import static testData.TestData.API_BASE_URL;
import static testData.TestData.PROPERTIES_CONFIG;

public class UserController {
    RequestSpecification requestSpecification;
    private static final String USER_ENDPOINT = "users/v1";

    public UserController() {
        this.requestSpecification = given()
                .accept(ContentType.JSON)
                .formParam("login", PROPERTIES_CONFIG.getLogin())
                .formParam("password", PROPERTIES_CONFIG.getPassword())
                .formParam("client", "chrome")
                .formParam("os", "Windows 10")
                .header("Authorization", "Bearer " + TokenManager.getTokenByRole())
                .header("Aesite", "AEO_US")
                .header("Aelang", "en_US")
                .baseUri(API_BASE_URL);
    }

    public Response getCookieData() {
        return given(requestSpecification)
                .when()
                .post(USER_ENDPOINT + "/loginOTP")
                .then().log().headers()
                .statusCode(200)
                .extract().response();
    }
}
