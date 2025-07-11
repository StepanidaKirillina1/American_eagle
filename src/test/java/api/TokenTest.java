package api;

import controllers.TokenController;
import org.junit.jupiter.api.*;

public class TokenTest {

    @Tags({@Tag("API"), @Tag("Positive"), @Tag("Bot_enabled")})
    @Test
    public void testToken() {
        TokenController tokenController = new TokenController();

        Assertions.assertTrue(tokenController.getAuthToken().contains("e"));
    }
}
