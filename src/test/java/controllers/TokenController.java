package controllers;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;
import static testData.TestData.API_BASE_URL;

public class TokenController {
    RequestSpecification requestSpecification;
    private static final String TOKEN_ENDPOINT = "auth/oauth/v5/token";

    public TokenController() {
        this.requestSpecification = given()
                .contentType(ContentType.URLENC)
                .formParam("grant_type", "client_credentials")
                .header("authorization", "Basic MjBlNDI2OTAtODkzYS00ODAzLTg5ZTctODliZmI0ZWJmMmZlOjVmNDk5NDVhLTdjMTUtNDczNi05NDgxLWU4OGVkYjQwMGNkNg==")
                .header("Cookie", "TLTUID=2E6E736E30B2B065793E15389129CDBA; _abck=71A5FD9585A3ED28F9B418AAD4049538~-1~YAAQJUlnaDxydjqXAQAAPLUrdQ4OkaF7PRtXPcGFbGEFi/f2D1Q7WIBysrSnE0tcpaRVC2T8tHV/lyHZv89kDe3XVEpYC9fQO5cu6ENm1IZnzXjIVX+baMgDmGzCbvSkgXgIdt6CmNtAwtPup9c1Tc3pqnWfkIe8ja01PY3myTix+pCfJatfce7xs2Su05y5uSwZrBLwVoY3D005cvM5mbFTBjiBMCKSFjVbCmmPktdADjNpeenDukfLfOA2++4FT1y7CoQtNqnqSSqTTcPUMO4zjmNVU1UOR/tIJbDizW7VIPhjBD0jJp1gCkndo+Sj2MDfScbclVKInrfiHUxW9wukRiHVzaV/5UyU0Uz13tbEXqu4iP0b0iZbLM6dLr3mgZs4Ouu2H/gezN5YesI0Bi/UdaJNDlh1zwAq3HjUsy7YSleHS/v4tde770nH/G48bnICj1hVtVR/Ao4oY7HNg9HB3VNKv6U7+iMvKddFXAYMyG7Kb0CryXVJQsW6BksHPMa0ECRwv0TuFYyQhmxkodQEmmQ4wwNUku/RQ1gQhBC2LBQ8pmxDxGVoLuuVF+BzMhfl6Na55GpG9mo75/fixutxLVokQLRYu2i53guFp4z+fjtFtNukzQOZeYcJ5eqDdP/uENTmSWwZo9c9S2u7eiIjlzqCNsnaUctD0gjmk3OvJavtstrpK81ds/ir1RI2IT0FCyRx6FWqjtivl2dnh6zJ+9hFzOIXyxWqmTLtdEW6nijIzSXOmwMM3bs6Wj0MeD72zOF7niX3tgNGkQtUEMASTlwTvMGCBUB+Xn5KTivClVfK4Ftbq87jz6F1/rSPKjQaNzf8HyJmiCJXikIsLuk/3taDAyc2PTqzlVlFjil1+usCq5EZOkcqHlxBNAodtg==~-1~-1~-1")
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
