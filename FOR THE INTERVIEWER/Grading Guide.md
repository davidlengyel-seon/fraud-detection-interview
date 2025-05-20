# Grading Guide: Fraud Detection CLI Exercise

## Purpose

This exercise is designed to assess:
- Engineering intuition
- Code quality and refactoring skill
- Understanding of concurrency and race conditions
- Communication during pair programming

---

## Hints

- You can run the Production version of the system by calling `FraudDetectionApplication.main()`.
- Read through `transactions.json` and `rules.json` to better understand the input data.
- Look at the `FraudDetectionServiceTest` class.
- Observe how rules are evaluated in `ScoringEngine`.
- Can results ever vary across runs for the same input?

---

## Success Criteria

### MUST HAVE (core objectives)
- [ ] Fixes score boundary bug
- [ ] Fixes rule state bug
- [ ] Suggests introducing concurrency
- [ ] Introduces concurrency to rule evaluation (via threads or streams)
- [ ] Can explain issues stemming from concurrency and shared mutable state
- [ ] Fixes concurrency bugs by using thread-safe/atomic datastructures or by refactoring `ScoringEngine` to remove the use of mutable state

---

## Bonus Signals

- [ ] Can explain the difference between platform threads and virtual threads
- [ ] Can explain the difference between `Future` and `CompletableFuture`
- [ ] Uses try-with-resources or defensive programming when reading files
- [ ] Notices that `EnrichmentService` is called multiple times for the same transaction
- [ ] Makes clean, modular design decisions
- [ ] Thinks aloud, proposes options, and communicates tradeoffs
- [ ] Suggests new test-cases

---

## Bonus Questions

If time allows, you might also:

- Think about how you would integrate a real `EmailChecker` service.
- Think about how you might implement a new Rule type, called velocity. Velocity rules evaluate in a similar way to simple
  rules but rather than evaluating a condition with a fixed value, they evaluate a condition with values aggregated from
  past transactions. e.g. `amount > average(amount) in last 24 hours`.

## Wrap-Up

Try to time-box the pair programming session to 45 minutes. At the end, ask the candidate:
> If you had another hour, what would you do next?
> If you had to ship this code to production as a web service, what would you do?

Look for thoughtful reflections: test coverage, observability, benchmarking, design cleanup.
