package api;

import controllers.TokenController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TokenTest {
    TokenController tokenController = new TokenController();

    @Test
    public void testToken() {
        Assertions.assertTrue(tokenController.getAuthToken().contains("e"));
    }
}
