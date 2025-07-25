package config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:${env}.properties",
        "classpath:test.properties"
})

public interface TestPropertiesConfig extends Config {
    @Key("authorization")
    String getAuthorizationValue();

    @Key("Cookie")
    String getCookieValue();

    @Key("login")
    String getLogin();

    @Key("password")
    String getPassword();

    @Key("role")
    String getRole();
}
