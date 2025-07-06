package api;

import controllers.TokenController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testng.annotations.Ignore;

@Disabled
public class TokenTest {
    TokenController tokenController = new TokenController();

    @Test
    public void testToken() {
        Assertions.assertTrue(tokenController.getAuthToken().contains("e"));
    }
}
