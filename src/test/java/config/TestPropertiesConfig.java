package config;

import org.aeonbits.owner.Config;

public interface TestPropertiesConfig extends Config {
    @Key("authorization")
    String getAuthorizationValue();

    @Key("Cookie")
    String getCookieValue();
}