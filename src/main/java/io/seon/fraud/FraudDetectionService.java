package io.seon.fraud;

import java.util.ArrayList;
import java.util.List;

import io.seon.model.EvaluationResult;
import io.seon.model.Transaction;
import io.seon.rules.Rule;


public class FraudDetectionService {

    private final ScoringEngine scoringEngine;

    public FraudDetectionService() {
        this.scoringEngine = new ScoringEngine();
    }

    public List<EvaluationResult> evaluate(List<Transaction> transactions, List<Rule> rules) {
        List<EvaluationResult> results = new ArrayList<>();
        for (Transaction transaction : transactions) {
            results.add(scoringEngine.evaluate(transaction, rules));
        }
        return results;
    }

}
