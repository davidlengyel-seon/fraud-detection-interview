package io.seon.fraud;

import java.util.ArrayList;
import java.util.List;

import io.seon.model.Action;
import io.seon.model.RuleResult;


public class ScoringContext {

    private final List<Integer> appliedRules = new ArrayList<>();
    private int score = 0;
    private Action action = Action.APPROVE;

    public void recordResult(RuleResult result) {
        appliedRules.add(result.id());
        score += result.score();
        action = result.action();
    }

    public List<Integer> appliedRules() {
        return appliedRules;
    }

    public int score() {
        return score;
    }

    public Action action() {
        return action;
    }
}
