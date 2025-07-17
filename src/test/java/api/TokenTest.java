package api;

import controllers.TokenController;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;

public class TokenTest {

    @Epic("API")
    @Feature("Token")
    @Tags({@Tag("API"), @Tag("Smoke"),  @Tag("Positive"), @Tag("Bot_enabled")})
    @Test
    public void getAuthTokenTest() {
        TokenController tokenController = new TokenController();

        Assertions.assertTrue(tokenController.getAuthToken().contains("e"));
    }
}
