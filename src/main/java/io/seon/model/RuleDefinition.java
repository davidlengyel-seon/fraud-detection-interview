package io.seon.model;

/**
 * Canonical rule definition, used for JSON serialization
 */
public record RuleDefinition(
  int id,
  String name,
  String target,
  Operator operator,
  Object expected,
  int score,
  Action action
) {
}
