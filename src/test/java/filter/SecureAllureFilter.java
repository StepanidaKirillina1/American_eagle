package filter;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class SecureAllureFilter extends AllureRestAssured {
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        if (requestSpec.getHeaders().hasHeaderWithName("Authorization")) {
            requestSpec.header("Authorization", "*****");
        }
        if (requestSpec.getHeaders().hasHeaderWithName("Cookie")) {
            requestSpec.header("Cookie", "*****");
        }

        if (requestSpec.getBody() != null) {
            String body = requestSpec.getBody().toString()
                    .replaceAll("\"token\":\"[^\"]*\"", "\"token\":\"*****\"")
                    .replaceAll("\"password\":\"[^\"]*\"", "\"password\":\"*****\"");
            requestSpec.body(body);
        }

        return super.filter(requestSpec, responseSpec, ctx);
    }
}
