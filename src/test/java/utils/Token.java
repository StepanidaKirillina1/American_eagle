package utils;

import controllers.UserController;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;
import static testData.TestData.API_BASE_URL;
import static testData.TestData.PROPERTIES_CONFIG;


public enum Token {

    GUEST {
        @Override
        public String getToken() {
            return extractToken(
                    buildTokenRequest()
                            .formParam("grant_type", "client_credentials")
                            .header("Cookie", PROPERTIES_CONFIG.getCookieValue())
                            .when()
                            .post("auth/oauth/" + "v5/token")
                            .then()
                            .body("token_type", Matchers.equalTo("Bearer"))
                            .body("scope", Matchers.equalTo("guest"))
            );
        }
    },

    AUTH {
        @Override
        public String getToken() {
            UserController userController = new UserController();
            Response response = userController.getCookieData();

            String TLTUID = response.getCookie("TLTUID");
            String abckCookie = response.getCookie("_abck");
            String bm_sz = response.getCookie("bm_sz");
            String akaalb_PROD_ALB_API = response.getCookie("akaalb_PROD_ALB_API");
            String bm_sv = response.getCookie("bm_sv");
            String ak_bmsc = response.getCookie("ak_bmsc");

            return extractToken(
                    buildTokenRequest()
                            .formParam("grant_type", "password")
                            .formParam("username", PROPERTIES_CONFIG.getLogin())
                            .formParam("password", PROPERTIES_CONFIG.getPassword())
                            .header("aelang", "en_US")
                            .header("aesite", "AEO_US")
                            .header("aecountry", "US")
                            .header("Cookie", "TLTUID=" + TLTUID + "; " + "akaalb_PROD_ALB_API=" + akaalb_PROD_ALB_API + "; " +"ak_bmsc=" + ak_bmsc + "; " + "bm_sz=" + bm_sz + "; " + "_abck=" + abckCookie + "; " + "bm_sv=" + bm_sv)
                            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36")
                            .header("x-access-token", Token.GUEST.getToken())
                            .header("Origin", "https://www.ae.com")
                            .when()
                            .post("auth/oauth/" + "v4/token")
                            .then().log().body()
                            .body("token_type", Matchers.equalTo("password"))
            );
        }
    };

    public abstract String getToken();

    protected RequestSpecification buildTokenRequest() {
        return given()
                .contentType(ContentType.URLENC)
                .baseUri(API_BASE_URL)
                .header("authorization", PROPERTIES_CONFIG.getAuthorizationValue());
    }

    protected String extractToken(ValidatableResponse response) {
        return response
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .get("access_token");
    }
}
