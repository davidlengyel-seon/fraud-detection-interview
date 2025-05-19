package io.seon.fraud;

import static io.seon.model.Action.APPROVE;
import static io.seon.model.Action.DECLINE;
import static io.seon.model.Action.REVIEW;
import static io.seon.model.Operator.EQUALS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.seon.FraudDetectionApplication;
import io.seon.FraudDetectionApplication.InputData;
import io.seon.config.Config;
import io.seon.config.Config.Environment;
import io.seon.model.Action;
import io.seon.model.EvaluationResult;
import io.seon.model.RuleDefinition;
import io.seon.model.Transaction;
import io.seon.rules.Rule;
import io.seon.rules.SimpleRule;


@ExtendWith(SoftAssertionsExtension.class)
class FraudDetectionServiceTest {

    static FraudDetectionService fds;
    static InputData inputData;
    @InjectSoftAssertions
    SoftAssertions softly;

    @BeforeAll
    static void init() throws IOException {
        fds = new FraudDetectionService();
        inputData = FraudDetectionApplication.readInputDataFromFiles();
        Config.setEnvironment(Environment.DEVELOPMENT);
    }

    private static Stream<Arguments> e2eArguments() {
        return Stream.of(
          Arguments.of(0, APPROVE, 30, List.of(1, 5, 6, 7)),
          Arguments.of(1, REVIEW, 55, List.of(2, 7, 9)),
          Arguments.of(2, APPROVE, 0, List.of(5, 7)),
          Arguments.of(3, DECLINE, 100, List.of(3, 4, 9, 10))
        );
    }

    @MethodSource("e2eArguments")
    @DisplayName("E2E Test")
    @ParameterizedTest(name = "tx{index} - Action: {1}, Score: {2}, Applied Rules: {3}")
    void overallResult(int transactionIndex, Action expectedAction, int expectedScore, List<Integer> expectedAppliedRules) {
        List<EvaluationResult> results = fds.evaluate(inputData.transactions(), inputData.rules());

        assertEquals(results.size(), 4);
        assertResult(results.get(transactionIndex), expectedAction, expectedScore, expectedAppliedRules);
    }

    private void assertResult(EvaluationResult evaluationResult, Action expectedAction, int expectedScore, List<Integer> expectedAppliedRules) {
        softly.assertThat(evaluationResult.action()).isEqualTo(expectedAction);
        softly.assertThat(evaluationResult.score()).isEqualTo(expectedScore);
        softly.assertThat(evaluationResult.appliedRules()).containsAll(expectedAppliedRules);
    }

    @Nested
    @DisplayName("Score Test")
    class ScoreTest {

        @Test
        @DisplayName("Minimum is 0")
        void scoresShouldNeverBeNegative() {
            Rule rule = new SimpleRule(new RuleDefinition(1, "", "userId", EQUALS, "bob", -10, APPROVE));
            Transaction tx = new Transaction("tx1", "bob", "bob@example.com", 1000, "DE", null, new HashMap<>());
            List<EvaluationResult> results = fds.evaluate(List.of(tx), List.of(rule));
            assertThat(results).hasSize(1);
            assertResult(results.getFirst(), APPROVE, 0, List.of(1));
        }

        @Test
        @DisplayName("Maximum is 100")
        void scoresShouldNeverBeOver100() {
            Rule rule1 = new SimpleRule(new RuleDefinition(1, "", "userId", EQUALS, "bob", 50, REVIEW));
            Rule rule2 = new SimpleRule(new RuleDefinition(2, "", "country", EQUALS, "US", 51, DECLINE));

            Transaction tx = new Transaction("tx1", "bob", "bob@example.com", 1000, "US", null, new HashMap<>());
            List<EvaluationResult> results = fds.evaluate(List.of(tx), List.of(rule1, rule2));
            assertThat(results).hasSize(1);
            assertResult(results.getFirst(), DECLINE, 100, List.of(1));
        }
    }

    @Nested
    @DisplayName("Rule Action Test")
    class RuleActionTest {

        @Test
        @DisplayName("When no rule matches, the action is APPROVE and score is 0")
        void noRuleMatches() {
            Rule rule = new SimpleRule(new RuleDefinition(1, "", "userId", EQUALS, "alice", 100, DECLINE));
            Transaction tx = new Transaction("tx1", "bob", "bob@example.com", 1000, "DE", null, new HashMap<>());
            List<EvaluationResult> results = fds.evaluate(List.of(tx), List.of(rule));
            assertThat(results).hasSize(1);
            assertResult(results.getFirst(), APPROVE, 0, Collections.emptyList());
        }

        @Test
        @DisplayName("When at least one rule results in DECLINE, the overall result should be DECLINE")
        void threeConflictingRules() {
            Rule r1 = new SimpleRule(new RuleDefinition(1, "", "userId", EQUALS, "bob", 0, APPROVE));
            Rule r2 = new SimpleRule(new RuleDefinition(2, "", "email", EQUALS, "bob@example.com", 70, DECLINE));
            Rule r3 = new SimpleRule(new RuleDefinition(3, "", "amount", EQUALS, 1000, 10, REVIEW));
            Transaction tx = new Transaction("tx1", "bob", "bob@example.com", 1000, "DE", null, new HashMap<>());
            List<EvaluationResult> results = fds.evaluate(List.of(tx), List.of(r1, r2, r3));
            assertThat(results).hasSize(1);
            assertResult(results.getFirst(), DECLINE, 80, List.of(1));
        }

        @Test
        @DisplayName("When one rule results in APPROVE and another in REVIEW, the overall result should be REVIEW")
        void conflictingApproveAndReview() {
            Rule r1 = new SimpleRule(new RuleDefinition(1, "", "userId", EQUALS, "bob", 0, APPROVE));
            Rule r2 = new SimpleRule(new RuleDefinition(2, "", "amount", EQUALS, 1000, 10, REVIEW));
            Transaction tx = new Transaction("tx1", "bob", "bob@example.com", 1000, "DE", null, new HashMap<>());
            List<EvaluationResult> results = fds.evaluate(List.of(tx), List.of(r1, r2));
            assertThat(results).hasSize(1);
            assertResult(results.getFirst(), REVIEW, 10, List.of(1));
        }

    }

}