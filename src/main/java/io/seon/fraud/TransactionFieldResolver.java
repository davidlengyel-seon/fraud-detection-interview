package io.seon.fraud;

import io.seon.model.Transaction;


/**
 * Static mappings from rule 'target' field to JSON field in transaction
 */
public class TransactionFieldResolver {

    public static Object resolve(Transaction transaction, String key) {
        return switch (key) {
            case "transactionId" -> transaction.id();
            case "userId" -> transaction.userId();
            case "email" -> transaction.email();
            case "amount" -> transaction.amount();
            case "country" -> transaction.country();
            case "timestamp" -> transaction.timestamp();
            default -> transaction.metadata().getOrDefault(key, null);
        };
    }
}