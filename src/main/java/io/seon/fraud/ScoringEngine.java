package io.seon.fraud;

import java.util.List;
import java.util.Objects;

import io.seon.dataenrichment.EnrichmentService;
import io.seon.model.EvaluationResult;
import io.seon.model.RuleResult;
import io.seon.model.Transaction;
import io.seon.rules.Rule;


public class ScoringEngine {

    private EnrichmentService enrichmentService = new EnrichmentService();

    public EvaluationResult evaluate(Transaction transaction, List<Rule> rules) {
        ScoringContext context = new ScoringContext();

        System.out.println("Evaluating rules for transaction " + transaction.id());
        for (Rule rule : rules) {
            Transaction enriched = enrichmentService.enrich(transaction);
            RuleResult result = rule.apply(enriched, context);
            if (result != null) {
                context.recordResult(result);
            }

        }

        return new EvaluationResult(transaction.id(), context.score(), context.action(), context.appliedRules());
    }
}