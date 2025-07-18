package utils;

import config.TestPropertiesConfig;
import org.aeonbits.owner.ConfigFactory;

public class TokenManager {
    public static String getTokenByRole() {
        return Token.valueOf(ConfigFactory.create(TestPropertiesConfig.class, System.getProperties())
                .getRole()
                .toUpperCase())
                .getToken();
    }
}
