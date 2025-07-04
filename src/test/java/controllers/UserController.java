package controllers;

import config.TestPropertiesConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;

import static io.restassured.RestAssured.given;
import static testData.TestData.API_BASE_URL;

public class UserController {
    RequestSpecification requestSpecification;
    private static final String USER_ENDPOINT = "users/v1";
    TestPropertiesConfig config = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());
    TokenController tokenController = new TokenController();

    public UserController() {
        this.requestSpecification = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.URLENC)
                //.contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("login", config.getLogin())
                .formParam("password", config.getPassword())
                .formParam("client", "chrome")
                .formParam("os", "Windows 10")
                .header("Authorization", "Bearer " + tokenController.getGuestToken())
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
