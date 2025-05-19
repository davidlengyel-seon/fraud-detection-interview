package io.seon.config;

import static io.seon.config.Config.Environment.*;


public class ProductionDelays {

    private static final long DELAY = 100;

    public static void delay() {
        if (Config.getEnvironment() == PRODUCTION) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public static void randomDelay() {
        if (Config.getEnvironment() == PRODUCTION) {
            try {
                Thread.sleep((long) (Math.random() * DELAY));
            } catch (InterruptedException ignored) {
            }
        }
    }
}
