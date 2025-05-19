# Interviewer Guide: Senior Java - General Questions

This guide is for conducting a **senior-level technical interview** focused on architectural thinking, concurrency, scalability, and clean design practices in Java.

Each section describes a real-world problem. Use the questions to **drive a conversation**, not just collect answers. Encourage candidates to reason aloud, explore trade-offs, and draw from experience.

---

## 1. API Concurrency Guard

**Scenario:**  
The candidate’s web app connects to a third-party API that can handle a **maximum of 5 concurrent requests**. Their service receives slightly more traffic than that. The API doesn’t rate-limit but degrades under pressure.

**Discussion Goals:**
- How would the candidate handle concurrency control?
- Are they comfortable with Java concurrency primitives (e.g. `Semaphore`, `ExecutorService`)?
- Do they know when to queue, reject, or retry?
- Can they justify their approach under load?

**Key Follow-ups:**
- How would they test this in production?
- Would they add metrics or backpressure signaling?

---

## 2. In-Memory Cache with Safe Refresh

**Scenario:**  
They have a cache with a `get(key)` method and a scheduled background job that refreshes the entire cache.

**Problem:**  
Ensure readers can always access the cache safely, **except while it's being refreshed**.

**What to Listen For:**
- Does the candidate suggest `AtomicReference`, `ReadWriteLock`, or `volatile` references?
- Do they understand the cost of blocking?
- Would they consider returning stale reads vs blocking?

---

## 3. Propagating Request Metadata

**Scenario:**  
Metadata like `customerId`, IP, etc. is in HTTP headers. It needs to be accessible deep in the call stack — without adding it to every DTO.

**What to Listen For:**
- Familiarity with `ThreadLocal` and Spring’s `RequestContextHolder`.
- Trade-offs in testability and async safety.
- Awareness of context propagation (e.g., with async controllers or reactive code).

**Good signs:**  
They think in terms of **observability**, **decoupling**, and **thread isolation**.

---

## 4. DAO Performance Under Load

**Scenario:**  
A DAO loads all users. It's fast under light load but slows down as traffic increases — despite DB queries staying fast.

**Objective:**  
Find out if they can diagnose systemic bottlenecks beyond the database.

**What to Listen For:**
- Understanding of **connection pooling** (HTTP, DB).
- Awareness of **network latency** (e.g., cross-region traffic).
- Tools or techniques they'd use to trace the bottleneck.

**Bonus prompt:**  
“What if the service is hosted in the US and we’re in EU?”

---

## 5. Logging Across a Large Monolith

**Scenario:**  
The candidate must log all HTTP requests in an app with hundreds or thousands of endpoints.

**Evolving Requirements:**  
Later, they need to extract specific headers and act on them.

**Expected Topics:**
- Use of `ServletFilter` vs AOP for cross-cutting concerns.
- Trade-offs in performance and maintainability.
- How they would avoid duplicate logging or redundant code.

**Red flag:**  
They suggest modifying each controller manually.

---

## 6. Safe Vendor API Migration

**Scenario:**  
They want to switch to a new vendor API that offers the same features as the current vendor but cheaper.
The caveat is that the new vendor API hasn't been tested under production load yet. What would their migration plan be?

**Key Concepts:**
- Feature flagging
- Canary rollout / Shadow traffic
- Circuit breakers (e.g., with Resilience4j)

**What to Explore:**
- How do they limit blast radius?
- What’s their rollback plan?
- How do they monitor failures or degradations?

---

## 7. Making Requests Available to Other Teams

**Scenario:**  
Another team wants access to incoming requests for analytics or ML.

**Scale-Up Concern:**  
More teams might join later.

**What to Listen For:**
- Pub/Sub architecture, event streaming (Kafka, SNS, etc.)
- Versioning and schema evolution of requests
- Low-coupling approaches like webhook triggers

**Red flag:**  
Hardcoding outputs or writing files directly.

**Yellow flag:**
Synchronous communication - needs explanation

---

## 8. Syncing Data from an External Table

**Scenario:**  
Their app depends on a table owned by another team. They maintain a **transformed local copy**.

**Goal:**  
Keep their version in sync.

**Expected Approaches:**
- SQL triggers
- Change Data Capture (Debezium, LogMiner)
- Scheduled polling with checksums or timestamps

**Extra Points:**
- Handling deletions and partial failures
- Schema change detection

---

## Final Notes

- Encourage candidates to **think out loud** and justify trade-offs.
- Good candidates will **ask clarifying questions** and not rush to answer.
- Explore adjacent topics if they show deep expertise (e.g., observability, testability, CI/CD implications).
- You don’t need to cover all topics — pick some, depending on time and flow.

