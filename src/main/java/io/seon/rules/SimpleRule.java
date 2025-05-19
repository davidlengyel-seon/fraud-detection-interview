package io.seon.rules;

import io.seon.config.ProductionDelays;
import io.seon.fraud.ScoringContext;
import io.seon.fraud.TransactionFieldResolver;
import io.seon.model.Operator;
import io.seon.model.RuleDefinition;
import io.seon.model.RuleResult;
import io.seon.model.Transaction;


public class SimpleRule implements Rule {

    private final RuleDefinition ruleDefinition;

    public SimpleRule(RuleDefinition ruleDefinition) {this.ruleDefinition = ruleDefinition;}

    @Override
    public RuleResult apply(Transaction transaction, ScoringContext context) {
        long start = System.currentTimeMillis();
        Object actual = TransactionFieldResolver.resolve(transaction, ruleDefinition.target());
        Object expected = ruleDefinition.expected();

        boolean applied = evaluate(actual, expected, ruleDefinition.operator());

        if (applied) {
            context.recordResult(new RuleResult(ruleDefinition.id(), ruleDefinition.action(), ruleDefinition.score()));
        } else {
            System.out.println("Rule " + ruleDefinition.id() + " did not apply");
        }

        long end = System.currentTimeMillis();
        System.out.printf("Rule %s evaluated in %d ms for transaction: %s\n", ruleDefinition.id(), end - start, transaction.id());
        return null;
    }

    @Override
    public int id() {
        return ruleDefinition.id();
    }

    private boolean evaluate(Object actual, Object expected, Operator operator) {
        ProductionDelays.randomDelay();
        if (actual == null || expected == null) {
            return false;
        }

        if (actual instanceof Number && expected instanceof Number) {
            double a = ((Number) actual).doubleValue();
            double b = ((Number) expected).doubleValue();
            return switch (operator) {
                case GREATER_THAN -> a > b;
                case LESS_THAN -> a < b;
                case GREATER_THAN_OR_EQUAL -> a >= b;
                case LESS_THAN_OR_EQUAL -> a <= b;
                case EQUALS -> a == b;
                case NOT_EQUALS -> a != b;
                default -> false;
            };
        } else {
            return switch (operator) {
                case EQUALS -> actual.equals(expected);
                case NOT_EQUALS -> !actual.equals(expected);
                case IN -> ((String) actual).contains(expected.toString());
                default -> false;
            };
        }
    }
}
