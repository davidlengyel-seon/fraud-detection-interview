# Fraud Detection CLI Exercise

Welcome to the SEON Scoring Team's backend coding challenge! In this exercise, you will work on a simplified fraud detection system.

## Background

Each transaction is run through a set of fraud rules. Each rule evaluates whether a condition applies (e.g., "amount > 1000"), and may contribute a score or change the outcome (`APPROVE`, `REVIEW`, or `DECLINE`).

Rules are read from a JSON file. Transactions are also loaded from a JSON file and run through the system (`FraudDetectionService`).

To each transaction, we add extra data by fetching the list of registered sites for the user email and adding it to the transaction metadata (`EnrichmentService`, `EmailChecker`).
For the purpose of this exercise, it works with stubbed data.

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
