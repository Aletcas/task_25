package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:test.properties")
public interface TestConfig extends Config {

    @Key("db.url")
    String dbUrl();

    @Key("db.user")
    String dbUser();

    @Key("db.password")
    String dbPassword();

    @Key("db.schema")
    String dbSchema();

    @Key("ui.base.url")
    String uiBaseUrl();

    @Key("ui.login.path")
    String uiLoginPath();

    default String loginUrl() {
        return uiBaseUrl() + uiLoginPath();
    }
}

