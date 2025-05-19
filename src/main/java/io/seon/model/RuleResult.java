package io.seon.model;

/**
 * Represents the result of evaluating a single rule.
 */
public record RuleResult(int id, Action action, int score) {
}
