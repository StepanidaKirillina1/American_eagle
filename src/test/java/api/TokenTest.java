package api;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import utils.TokenManager;

@Epic("API")
@Feature("Token")
public class TokenTest {

    @Tags({@Tag("API"), @Tag("Smoke"),  @Tag("Positive")})
    @Test
    public void getAccessTokenTest() {
        Assertions.assertFalse(TokenManager.getTokenByRole().isEmpty());
    }
}
