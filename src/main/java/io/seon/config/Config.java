package io.seon.config;

public class Config {

    private static Environment environment;

    public enum Environment {
        DEVELOPMENT, PRODUCTION
    }

    public static Environment getEnvironment() {
        return environment;
    }

    public static void setEnvironment(Environment environment) {
        Config.environment = environment;
    }

}
