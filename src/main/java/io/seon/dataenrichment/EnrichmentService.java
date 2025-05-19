package io.seon.dataenrichment;

import java.util.List;

import io.seon.model.Transaction;


public class EnrichmentService {

    /**
     * Enriches a given transaction by fetching the list of registered sites for the user email and
     * adding it to the transaction metadata.
     *
     * @param transaction the transaction to enrich
     * @return the enriched transaction
     */
    public Transaction enrich(Transaction transaction) {
        EmailChecker checker = new EmailChecker();
        List<String> registeredSites = checker.getRegisteredSites(transaction.email());
        transaction.metadata().put("registeredSites", String.join(",", registeredSites));
        return transaction;
    }
}
