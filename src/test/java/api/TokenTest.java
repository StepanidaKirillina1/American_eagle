package api;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import utils.TokenManager;

public class TokenTest {

    @Epic("API")
    @Feature("Token")
    @Tags({@Tag("API"), @Tag("Smoke"),  @Tag("Positive"), @Tag("Bot_enabled")})
    @Test
    public void getAuthTokenTest() {
        Assertions.assertTrue(TokenManager.getTokenByRole().contains("e"));
    }
}
