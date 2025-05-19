package io.seon.rules;

import io.seon.fraud.ScoringContext;
import io.seon.model.RuleResult;
import io.seon.model.Transaction;


public interface Rule {

    RuleResult apply(Transaction transaction, ScoringContext context);

    int id();
}
