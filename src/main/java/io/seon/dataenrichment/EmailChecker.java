package io.seon.dataenrichment;

import java.util.List;
import java.util.Map;

import io.seon.config.ProductionDelays;


/**
 * Represents a mocked version of an email checker that returns a list of registered sites for each user from stubbed
 * data.
 */
public class EmailChecker {

    private static final Map<String, List<String>> registeredSites = Map.of(
      "alice@example.com", List.of("google", "github", "facebook"),
      "bob@example.com", List.of("facebook", "mysketchysite"),
      "carol@example.com", List.of("tiktok", "facebook", "instagram"),
      "dan@example.com", List.of("mysketchysite")
    );

    public List<String> getRegisteredSites(String email) {
        ProductionDelays.delay();
        return registeredSites.get(email);
    }
}
