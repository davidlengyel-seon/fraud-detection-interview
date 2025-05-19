package io.seon.model;

import java.util.List;


/**
 * Represents the result of evaluating all rules for a transaction, including extra information such as the
 * transaction ID, score, action, and applied rules.
 */
public record EvaluationResult(String transactionId, int score, Action action, List<Integer> appliedRules) {
}
