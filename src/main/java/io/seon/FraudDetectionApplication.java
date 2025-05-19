package io.seon;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.seon.config.Config;
import io.seon.config.Config.Environment;
import io.seon.fraud.FraudDetectionService;
import io.seon.model.EvaluationResult;
import io.seon.model.RuleDefinition;
import io.seon.model.Transaction;
import io.seon.rules.Rule;
import io.seon.rules.SimpleRule;


public class FraudDetectionApplication {

    public static void main(String[] args) throws Exception {
        Config.setEnvironment(Environment.PRODUCTION);
        InputData inputData = readInputDataFromFiles();

        FraudDetectionService fraudEvaluator = new FraudDetectionService();

        long start = System.currentTimeMillis();
        List<EvaluationResult> results = fraudEvaluator.evaluate(inputData.transactions(), inputData.rules());
        long end = System.currentTimeMillis();
        System.out.println("\nEvaluation completed in " + (end - start) + " ms");

        for (EvaluationResult result : results) {
            System.out.printf("%s: %s (%d) - Applied rules: %s\n", result.transactionId(), result.action(), result.score(), result.appliedRules());
        }
    }

    public static InputData readInputDataFromFiles() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        InputStream rulesFile = new FileInputStream("src/main/resources/rules.json");
        InputStream transactionsFile = new FileInputStream("src/main/resources/transactions.json");
        List<RuleDefinition> ruleDefinitions = mapper.readValue(rulesFile, new TypeReference<>() {});
        List<Transaction> transactions = mapper.readValue(transactionsFile, new TypeReference<>() {});

        List<Rule> rules = ruleDefinitions.stream()
          .map(SimpleRule::new)
          .map(Rule.class::cast)
          .toList();

        return new InputData(transactions, rules);
    }

    public record InputData(List<Transaction> transactions, List<Rule> rules) {}
}