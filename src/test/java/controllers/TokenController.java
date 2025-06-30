package controllers;

import config.TestPropertiesConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;
import static testData.TestData.API_BASE_URL;

public class TokenController {
    RequestSpecification requestSpecification;
    RequestSpecification freshSpec = given().baseUri(API_BASE_URL);


    private static final String TOKEN_ENDPOINT = "auth/oauth/";
    TestPropertiesConfig config = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());

    public TokenController() {
        this.requestSpecification = given()
                .contentType(ContentType.URLENC)
                .formParam("grant_type", "client_credentials")
                .header("authorization", config.getAuthorizationValue())
                .header("Cookie", config.getCookieValue())
                .baseUri(API_BASE_URL);
    }

    public String getGuestToken() {
        return given(requestSpecification)
                .when()
                .post(TOKEN_ENDPOINT + "v5/token")
                .then().log().body()
                .statusCode(200)
                .body("token_type", Matchers.equalTo("Bearer"))
                .body("scope", Matchers.equalTo("guest"))
                .extract()
                .body()
                .jsonPath()
                .get("access_token");
    }

    public String getAuthToken() {
        UserController userController = new UserController();
        Response response = userController.getCookieData();

        String TLTUID = response.getCookie("TLTUID");
        String abckCookie = response.getCookie("_abck");
        String bm_sz = response.getCookie("bm_sz");
        String akaalb_PROD_ALB_API = response.getCookie("akaalb_PROD_ALB_API");
        String bm_sv = response.getCookie("bm_sv");
        String ak_bmsc = response.getCookie("ak_bmsc");

        return given(freshSpec)
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .formParam("grant_type", "password")
                .formParam("username", config.getLogin())
                .formParam("password", config.getPassword())
                .header("aelang", "en_US")
                .header("aesite", "AEO_US")
                .header("aecountry", "US")
                //.header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Authorization", config.getAuthorizationValue())
                .header("Cookie", "TLTUID=" + TLTUID + "; " + "akaalb_PROD_ALB_API=" + akaalb_PROD_ALB_API + "; " +"ak_bmsc=" + ak_bmsc + "; " + "bm_sz=" + bm_sz + "; " + "_abck=" + abckCookie + "; " + "bm_sv=" + bm_sv)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36")
                .header("x-access-token", getGuestToken())
                //.header("Accept", "application/vnd.oracle.resource+json")
                .header("Origin", "https://www.ae.com")
                .when()
                .post(TOKEN_ENDPOINT + "v4/token")
                .then().log().body()
                .statusCode(200)
                .body("token_type", Matchers.equalTo("password"))
                .extract()
                .body()
                .jsonPath()
                .get("access_token");
    }
}
