# Fraud Detection CLI Exercise

Welcome to the SEON Scoring Team's backend coding challenge! In this exercise, you will work on a simplified fraud detection system.

## Background

You will work on a small scale, simplified example of a Fraud Detection system, very similar to the one that our team works on day-to-day.
It operates by letting our customers define rules (`rules.json`) that should be evaluated for all of their transactions
(`transactions.json`).

**Rules**

Rules are a logical operation that makes assertions about the transaction, and applies a score  and a recommended action.
Rules and Transactions are loaded from JSON files and run through the system (`FraudDetectionService`).

An example rule:
```json
{
    "id": 1,
    "name": "HighAmount",
    "target": "amount",
    "operator": "GREATER_THAN",
    "expected": 1000,
    "score": 50,
    "action": "APPROVE"
}
```
If the condition in this rule `amount > 1000` evaluates to `true` for the given transaction:
- we add the rule's id to `applied_rule_ids` - to report what rules the transaction matched
- we add the defined score to the overall transaction score (+50)
- we recommend `APPROVE` for the transaction outcome

Other rules go through the same process: If their condition evaluates to `true`, they add to the overall score, may
change the transaction outcome, and get added to `applied_rule_ids`.

After all rules are executed, every transaction gets:
- An overall `score` (0-100)
- An action (`APPROVE/REVIEW/DECLINE`)
- A list of `applied_rule_ids`

If multiple rules provide conflicting actions, the order of precedence should be `DECLINE` > `REVIEW` > `APPROVE`.

To each transaction, we add extra data by fetching the list of registered sites for the user email and adding it to the transaction metadata (`EnrichmentService`, `EmailChecker`).

Original transaction:
```json
{
    "id": "tx1001",
    "userId": "alice",
    "email": "alice@example.com",
    "amount": 1500,
    "country": "DE",
    "timestamp": "2025-05-19T11:09:39.468340Z",
    "metadata": {
      "ip": "1.1.1.1"
    }
}
```
Transaction after Data Enrichment:
```json
{
  "id": "tx1001",
  "userId": "alice",
  "email": "alice@example.com",
  "amount": 1500,
  "country": "DE",
  "timestamp": "2025-05-19T11:09:39.468340Z",
  "metadata": {
    "ip": "1.1.1.1",
    "registeredSites": ["google", "github", "facebook"]
  }
}
```

This enables us to write rules based on enriched data, such as:
```json
{
    "id": 6,
    "name": "UserHasGoogleAccount",
    "target": "registeredSites",
    "operator": "IN",
    "expected": "google",
    "score": -5,
    "action": "APPROVE"
}
```

For the purpose of this exercise, data enrichment works with stubbed data.

The `ProductionDelays` class adds some delays to better simulate a production environment, these can be disabled by calling
`Config.setEnvironment(DEVELOPMENT)`, which is the default set in the tests.

Your task is to analyze, improve, and extend this system. There are several bugs and tests that will guide you to solve them.

**Good luck!**

---

## Goals (45 minutes)

You will pair program with your interviewer(s) to:

1. **Fix bugs** in the system (refer to the tests)
2. **Improve performance** 
3. **Refactor** or improve code, add new tests wherever it makes sense

## Project Setup

```console
# Compile
mvn clean compile

# Run
mvn exec:java

# Test
mvn test
```
