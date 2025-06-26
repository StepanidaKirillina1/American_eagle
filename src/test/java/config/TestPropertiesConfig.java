package config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:${env}.properties",
        "classpath:default.properties"
})

public interface TestPropertiesConfig extends Config {
    @Key("authorization")
    String getAuthorizationValue();

    @Key("Cookie")
    String getCookieValue();
}