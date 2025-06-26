package controllers;

import config.TestPropertiesConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;
import static testData.TestData.API_BASE_URL;

public class TokenController {
    RequestSpecification requestSpecification;
    private static final String TOKEN_ENDPOINT = "auth/oauth/v5/token";
    TestPropertiesConfig config = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());

    public TokenController() {
        this.requestSpecification = given()
                .contentType(ContentType.URLENC)
                .formParam("grant_type", "client_credentials")
                .header("authorization", config.getAuthorizationValue())
                .header("Cookie", config.getCookieValue())
                .baseUri(API_BASE_URL);
    }

    public String getToken() {
        return given(requestSpecification)
                .when()
                .post(TOKEN_ENDPOINT)
                .then()
                .statusCode(200)
                .body("token_type", Matchers.equalTo("Bearer"))
                .body("scope", Matchers.equalTo("guest"))
                .extract()
                .body()
                .jsonPath()
                .get("access_token");
    }
}
