# JMeter Skill Tree --- Full-Stack Developer Path

> **Goal:** Progress from understanding HTTP/REST APIs to professional,
> application-level Apache JMeter proficiency for load, performance, and
> correctness-under-load testing of REST endpoints and microservice
> architectures.
>
> **Primary prerequisites:** HTTP/HTTPS/REST API fundamentals (request/
> response model, methods, status codes, headers, JSON payloads) and
> basic command-line usage.
>
> **Downstream connections:** The Spring Cloud & Microservices
> Fundamentals, Eureka Service Discovery, and Spring Cloud Gateway skill
> trees all assume working JMeter competence in their later tiers and
> capstones. CI/CD Fundamentals treats a JMeter run as an optional
> pipeline stage.
>
> **Target level:** Professional full-stack developer who can design,
> run, and correctly interpret a load test against a REST API or a
> gateway-fronted microservice system --- not a dedicated performance
> engineer, capacity-planning specialist, or JMeter plugin author.
>
> **Scope boundary:** This tree focuses on JMeter itself: test plan
> construction, execution modes, and result interpretation. It does not
> teach the REST APIs being tested (see the HTTP/HTTPS/REST API tree),
> the microservice architectures being tested (see the Spring Cloud
> trees), or infrastructure-level capacity planning.

------------------------------------------------------------------------

# Skill Tree Overview

``` text
JMeter
├── 1.  HTTP/REST Prerequisites
├── 2.  Command-Line Prerequisites
├── 3.  What Load/Performance Testing Is
├── 4.  Load, Stress, Soak & Spike Testing
├── 5.  The Test Plan
├── 6.  Thread Groups
├── 7.  Samplers
├── 8.  Listeners
├── 9.  Config Elements
├── 10. Assertions
├── 11. Timers
├── 12. Logic Controllers
├── 13. Scoping & Execution Order
├── 14. Installing JMeter
├── 15. GUI Mode vs CLI (Non-GUI) Mode
├── 16. Thread Group Configuration
├── 17. The HTTP Request Sampler
├── 18. HTTP Request Defaults
├── 19. View Results Tree
├── 20. Summary Report & Aggregate Report
├── 21. The Listener-Overhead Caveat
├── 22. Response Assertion
├── 23. Duration Assertion
├── 24. JSON Assertion
├── 25. CSV Data Set Config
├── 26. Regular Expression Extractor
├── 27. JSON Extractor
├── 28. Correlation: Login → Authenticated Request
├── 29. HTTP Header Manager
├── 30. HTTP Cookie Manager
├── 31. Loop Controller
├── 32. If Controller
├── 33. Transaction Controller
├── 34. Timers for Realistic Think-Time
├── 35. Running JMeter from the CLI
├── 36. The HTML Dashboard Report
├── 37. JMeter in CI/CD Pipelines
├── 38. Throughput
├── 39. Percentiles vs Averages
├── 40. Error Rate
├── 41. Distributed Testing Awareness
├── 42. Single Endpoint vs Full User Journey
├── 43. Testing Microservices Through a Gateway
├── 44. Connecting to the Eureka & Gateway Capstones
└── 45. JMeter vs Gatling vs k6 vs Locust
```

------------------------------------------------------------------------

# Dependency Map

``` text
HTTP / HTTPS / REST API fundamentals
Basic command-line usage (ubuntu/WSL2 skill map)
        │
        ▼
      JMeter
        │
        ├──────────────┬──────────────────┬────────────────┐
        ▼               ▼                  ▼                ▼
Spring Cloud &      Eureka Service    Spring Cloud       CI/CD
Microservices       Discovery         Gateway            Fundamentals
Fundamentals        (capstone         (capstone          (pipeline
(capstone           JMeter            JMeter             stage)
JMeter              exercise)         exercise)
exercise)
```

JMeter does not require Spring, Java application code, or a specific
backend framework --- it only requires something that speaks HTTP. It is
placed after the REST API tree because you cannot design a meaningful
test plan against an API you do not understand, and it is placed before
the microservices trees because those trees' capstones explicitly
instruct the learner to "point JMeter at the gateway" and observe
client-visible performance --- an instruction that presumes everything in
this tree.

------------------------------------------------------------------------

# Tier 0 --- Prerequisites

## 1. HTTP/REST Prerequisites

JMeter test plans are, at their core, structured HTTP requests plus
assertions about the responses. Without solid HTTP/REST fundamentals,
a learner cannot tell a correct test plan from a broken one.

-   [ ] HTTP request/response model
-   [ ] HTTP methods: GET, POST, PUT, PATCH, DELETE
-   [ ] Status codes: 2xx, 4xx, 5xx meaning
-   [ ] Headers: `Content-Type`, `Accept`, `Authorization`
-   [ ] JSON request/response payload shapes
-   [ ] Query parameters vs path parameters vs request body
-   [ ] Cookies and session concepts
-   [ ] Statelessness of HTTP requests
-   [ ] Basic authentication/token concepts (API keys, bearer tokens)

**Checkpoint:** Manually construct and send a request to a REST endpoint
with a tool such as `curl` or Postman, and explain every part of the
request and response, before asking JMeter to do it repeatedly and at
scale.

## 2. Command-Line Prerequisites

JMeter is a Java application distributed as a script-launched tool, and
its most important execution mode is command-line only.

-   [ ] Navigate directories from a shell
-   [ ] Run a script/executable from a shell
-   [ ] Understand environment variables (`PATH`, `JAVA_HOME`)
-   [ ] Redirect output to files
-   [ ] Understand exit codes conceptually
-   [ ] Understand that JMeter requires a installed JDK/JRE

**Checkpoint:** Confirm `java -version` succeeds before attempting to
install or run JMeter.

------------------------------------------------------------------------

# Tier 1 --- Why Performance Testing Exists

## 3. What Load/Performance Testing Is

-   [ ] Explain that functional tests verify correctness for one user;
    performance tests verify behavior under many concurrent users
-   [ ] Understand that a REST endpoint can be functionally correct and
    still fail under realistic traffic
-   [ ] Understand response time, throughput, and error rate as the
    three core performance signals
-   [ ] Understand that performance testing is empirical --- it measures
    a real running system, not a theoretical estimate
-   [ ] Understand that a load test's value depends on the realism of
    its request mix, data, and concurrency pattern
-   [ ] Know that performance testing normally targets a
    non-production/staging environment unless a controlled, approved
    production test has been explicitly planned

### Mental model

``` text
Functional testing
    → "Does this endpoint return the correct result?"

Performance testing
    → "Does this endpoint still return the correct result,
       quickly enough, when many users hit it at once?"
```

## 4. Load, Stress, Soak & Spike Testing

These terms describe different questions asked of the same system, and
professional usage keeps them distinct.

### Load testing

-   [ ] Apply an expected, realistic level of concurrent traffic
-   [ ] Answer: "Does the system meet its performance targets under
    normal/anticipated demand?"

### Stress testing

-   [ ] Increase load beyond expected levels, often until failure
-   [ ] Answer: "Where is the breaking point, and how does the system
    fail --- gracefully or catastrophically?"

### Soak testing (endurance testing)

-   [ ] Apply moderate load over an extended duration (hours)
-   [ ] Answer: "Do memory leaks, connection-pool exhaustion, or slow
    resource leaks appear only over time?"

### Spike testing

-   [ ] Apply a sudden, sharp increase in traffic
-   [ ] Answer: "Does the system recover from a sudden burst (e.g. a
    marketing event or retry storm) without cascading failure?"

``` text
Load   ─── steady, expected traffic ─────────────
Stress ─── traffic increased until it breaks ────▲
Soak   ─── moderate traffic, long duration ──────────────────────
Spike  ─── ▲ sudden burst, then back to normal ──▼─────────────
```

-   [ ] Choose the correct test type for the question being asked
-   [ ] Understand that the same JMeter test plan can often be reused
    for multiple test types by changing thread count, ramp-up, and
    duration

**Checkpoint:** Given a business question ("will this survive Black
Friday traffic?" vs "does this leak memory after a week?"), name the
correct test type before opening JMeter.

------------------------------------------------------------------------

# Tier 2 --- JMeter Architecture & Mental Model

JMeter's GUI is a tree. Every element you add is a node in that tree,
and where a node sits in the tree determines what it does and what it
applies to. Learning JMeter is largely learning this tree's vocabulary.

## 5. The Test Plan

-   [ ] Understand the Test Plan as the root container of everything
-   [ ] Understand Test Plan-level variables
-   [ ] Understand the "run thread groups consecutively" option
-   [ ] Understand classpath/library configuration at the Test Plan
    level (e.g. adding a JDBC driver jar for a JDBC Request sampler)
-   [ ] Save a Test Plan as a `.jmx` file
-   [ ] Understand that `.jmx` is XML, and can be read, diffed, and
    version-controlled

## 6. Thread Groups

-   [ ] Understand a Thread Group as a pool of virtual users
-   [ ] Understand that everything under a Thread Group runs once per
    thread, per loop
-   [ ] Understand that a Test Plan can contain multiple Thread Groups,
    each simulating a different user population

## 7. Samplers

-   [ ] Understand a Sampler as the element that actually sends a
    request and records the result
-   [ ] Know the HTTP Request sampler as the primary sampler for REST
    API testing
-   [ ] Recognize that other samplers exist (JDBC Request, TCP Sampler,
    FTP Request, JSR223 Sampler) without needing mastery of all of them

## 8. Listeners

-   [ ] Understand a Listener as the element that collects and displays
    sampler results
-   [ ] Understand that listeners consume results; they do not generate
    load
-   [ ] Know that listener choice affects both what you can see and how
    much overhead the test run incurs

## 9. Config Elements

-   [ ] Understand a Config Element as something that supplies default
    or shared configuration to samplers below it
-   [ ] Recognize HTTP Request Defaults, CSV Data Set Config, HTTP
    Header Manager, HTTP Cookie Manager, and User Defined Variables as
    config elements

## 10. Assertions

-   [ ] Understand an Assertion as a pass/fail check applied to a
    sampler's response
-   [ ] Understand that assertions test correctness, not just capacity
-   [ ] Recognize that a fast, high-throughput response can still be a
    wrong response

## 11. Timers

-   [ ] Understand a Timer as an element that introduces a pause before
    a sampler executes
-   [ ] Understand timers as the mechanism for simulating human
    think-time

## 12. Logic Controllers

-   [ ] Understand a Logic Controller as an element that changes the
    order or condition under which its child samplers run
-   [ ] Recognize Loop Controller, If Controller, and Transaction
    Controller as the most commonly needed controllers

## 13. Scoping & Execution Order

The tree is read top-to-bottom, but scope is hierarchical, not linear.

``` text
Test Plan
└── Thread Group
    ├── HTTP Request Defaults        (applies to all samplers below it)
    ├── HTTP Header Manager          (applies to all samplers below it)
    ├── HTTP Request #1
    │   └── Response Assertion       (applies only to HTTP Request #1)
    ├── HTTP Request #2
    └── View Results Tree            (collects results from siblings above)
```

-   [ ] Understand that config elements, header managers, and cookie
    managers apply to every sampler at or below their position in scope
-   [ ] Understand that assertions/extractors placed directly under one
    sampler apply only to that sampler
-   [ ] Understand that samplers execute top-to-bottom within their
    parent
-   [ ] Predict, given a tree diagram, exactly which elements apply to
    which sampler

**Checkpoint:** Given an unfamiliar `.jmx` tree structure, explain in
plain language what runs, in what order, and which configuration
applies to which request.

------------------------------------------------------------------------

# Tier 3 --- Installing & Running JMeter

## 14. Installing JMeter

-   [ ] Confirm a supported JDK is installed
-   [ ] Download Apache JMeter (5.x) as a binary archive
-   [ ] Extract it and locate `bin/jmeter.sh` (Linux/macOS) or
    `bin/jmeter.bat` (Windows)
-   [ ] Launch JMeter's GUI to confirm the installation works
-   [ ] Locate `bin/jmeter.properties` as the main configuration file
-   [ ] Know that JMeter is not typically installed via a system package
    manager for professional use --- the official binary distribution is
    preferred for version control

## 15. GUI Mode vs CLI (Non-GUI) Mode

This is one of the most important operational facts in all of JMeter.

``` text
GUI mode  (jmeter)
    → for AUTHORING and DEBUGGING test plans only

CLI / non-GUI mode  (jmeter -n -t plan.jmx ...)
    → for ACTUALLY GENERATING LOAD
```

-   [ ] Understand that the GUI itself consumes CPU/memory to render
    the tree, listeners, and live tables
-   [ ] Understand that this rendering overhead competes with the
    threads generating load, corrupting the measured results
-   [ ] State plainly: **never use GUI mode to run a real load test**
-   [ ] Use GUI mode to build, debug, and single-run-verify a test plan
-   [ ] Use CLI/non-GUI mode (`-n`) for every actual load, stress, soak,
    or spike run
-   [ ] Recognize JMeter's own startup warning when a Thread Group with
    many threads is run from the GUI

**Checkpoint:** Explain, to someone who has never used JMeter, why a
test run from the GUI with 500 threads is not trustworthy data.

------------------------------------------------------------------------

# Tier 4 --- Building a Basic Test Plan

## 16. Thread Group Configuration

``` text
Thread Group
├── Number of Threads (users): 50
├── Ramp-Up Period (seconds):  10
└── Loop Count:                5
```

-   [ ] Configure number of threads (concurrent virtual users)
-   [ ] Configure ramp-up period (seconds to start all threads)
-   [ ] Configure loop count (how many times each thread repeats its
    samplers)
-   [ ] Understand that a short ramp-up with many threads creates a
    sudden burst (closer to spike testing)
-   [ ] Understand that a long, gradual ramp-up more closely resembles
    organic traffic growth
-   [ ] Recognize the "Infinite" loop option combined with a Duration
    setting, used for soak testing
-   [ ] Recognize Scheduler options: Duration and Startup Delay

## 17. The HTTP Request Sampler

``` text
HTTP Request
├── Protocol:  https
├── Server Name or IP: api.example.com
├── Port:      443
├── Method:    POST
├── Path:      /api/orders
└── Body Data: { "productId": 42, "quantity": 2 }
```

-   [ ] Configure protocol, server, port, and path
-   [ ] Select the correct HTTP method
-   [ ] Provide a JSON request body for POST/PUT/PATCH requests
-   [ ] Add query parameters via the Parameters tab
-   [ ] Understand the "Body Data" vs "Parameters" tab distinction for
    JSON APIs (JSON bodies belong in Body Data, not form parameters)
-   [ ] Run a single-thread, single-loop test in GUI mode with a View
    Results Tree listener to confirm the request is well-formed before
    scaling up

## 18. HTTP Request Defaults

-   [ ] Add an "HTTP Request Defaults" config element once per Thread
    Group (or Test Plan) to centralize server, port, and protocol
-   [ ] Leave only the path (and method/body) on individual HTTP
    Request samplers
-   [ ] Understand why this avoids repeating the same hostname across
    dozens of samplers, and makes environment switching (dev/staging)
    a single-field change

**Checkpoint:** Build a Thread Group with 5 threads, a 5-second ramp-up,
and 1 loop, sending a single GET request to a real or mock REST
endpoint, and confirm it runs successfully in GUI mode.

------------------------------------------------------------------------

# Tier 5 --- Listeners & Reading Results

## 19. View Results Tree

-   [ ] Inspect individual sampler request/response pairs
-   [ ] Read the raw request headers/body sent
-   [ ] Read the raw response headers/body/status code received
-   [ ] Use it to debug a single misconfigured sampler
-   [ ] Understand that its rendering cost is extremely high per sample

## 20. Summary Report & Aggregate Report

-   [ ] Understand the Summary Report as running totals (samples,
    average, min, max, error %, throughput)
-   [ ] Understand the Aggregate Report as the same idea, plus
    percentile columns (90%, 95%, 99% Line)
-   [ ] Read the columns: # Samples, Average, Median, 90% Line, 95%
    Line, 99% Line, Min, Max, Error %, Throughput, Received KB/sec
-   [ ] Use the Aggregate Report as the primary results table for
    reviewing a completed run

## 21. The Listener-Overhead Caveat

-   [ ] Understand that every active listener adds CPU/memory overhead
    proportional to the number of samples it processes
-   [ ] Understand that View Results Tree, in particular, should be
    disabled (not merely closed) during real load generation
-   [ ] Prefer writing raw results to a `.jtl` file from the command
    line over relying on GUI listeners for actual load runs
-   [ ] Understand that listeners are appropriate during test-plan
    authoring/debugging, and should be stripped down or disabled before
    a load run that anyone will actually trust

``` text
Authoring a test plan
    → many listeners, small thread count, GUI mode

Running a real load test
    → CLI mode, listeners disabled/removed,
      results written to a results file
```

**Checkpoint:** Explain why a load test result showing suspiciously slow
average response times might actually be measuring JMeter's own GUI
overhead rather than the API under test.

------------------------------------------------------------------------

# Tier 6 --- Assertions: Verifying Correctness Under Load

A system can be fast and still be wrong. Assertions catch that.

## 22. Response Assertion

-   [ ] Assert on response code (e.g. `200`)
-   [ ] Assert on response message
-   [ ] Assert on response body text/pattern
-   [ ] Choose "Contains", "Matches", or "Equals" pattern-matching modes
-   [ ] Understand that a failed assertion marks the sample as an error,
    which then shows up in the Error % column

## 23. Duration Assertion

-   [ ] Set a maximum acceptable response time in milliseconds
-   [ ] Understand that this fails individual samples that exceed the
    threshold, independent of whether the response body was correct
-   [ ] Use it to encode an SLA-style expectation directly into the
    test plan

## 24. JSON Assertion

-   [ ] Provide a JSON Path expression to assert a field exists in the
    response
-   [ ] Optionally assert the field's expected value
-   [ ] Understand this validates response *shape and content*, not
    just HTTP status
-   [ ] Prefer a JSON Assertion (or a JSR223 Assertion for complex
    logic) over a brittle Response Assertion regex when validating
    structured JSON

``` text
Response Assertion   → status code / raw text pattern
Duration Assertion    → response time threshold
JSON Assertion        → structured field-level correctness
```

**Checkpoint:** Add a Response Assertion for status `200`, a Duration
Assertion for 1000 ms, and a JSON Assertion confirming a field exists,
all on the same sampler, and observe each fail independently when you
deliberately break the target endpoint.

------------------------------------------------------------------------

# Tier 7 --- Parameterization

Sending the exact same request from every thread on every loop is
unrealistic and can also trigger caching or uniqueness-constraint
artifacts that a real system would not see.

## 25. CSV Data Set Config

``` text
CSV Data Set Config
├── Filename:            users.csv
├── Variable Names:      username,password
├── Delimiter:           ,
├── Recycle on EOF:      True
├── Stop thread on EOF:  False
└── Sharing mode:        All threads
```

-   [ ] Create a CSV file of test data (e.g. usernames/passwords, order
    IDs)
-   [ ] Reference CSV columns via `${username}` style variables in
    samplers
-   [ ] Configure Recycle on EOF and Stop Thread on EOF correctly for
    the test's intent
-   [ ] Understand Sharing Mode: All threads vs Current thread group vs
    Current thread
-   [ ] Understand that different threads should generally receive
    different rows, to avoid every virtual user acting identically

**Checkpoint:** Parameterize an HTTP Request's body with values pulled
from a CSV file, and confirm (via View Results Tree) that different
threads send different data.

------------------------------------------------------------------------

# Tier 8 --- Correlation: Chaining Requests

Real user journeys are not one isolated request --- they are sequences
where later requests depend on values returned by earlier ones (a login
token, a newly created resource's ID, a CSRF token). This dependency is
called correlation.

## 26. Regular Expression Extractor

-   [ ] Apply a Regular Expression Extractor to a sampler's response
-   [ ] Extract a value using a capture group
-   [ ] Store the extracted value into a JMeter variable
-   [ ] Reference the variable in a later sampler with `${variableName}`
-   [ ] Understand the "default value" field as a way to catch silent
    extraction failures

## 27. JSON Extractor

-   [ ] Apply a JSON Extractor (JSON Path-based) to a JSON response
-   [ ] Extract a field (e.g. `$.token`) into a JMeter variable
-   [ ] Prefer the JSON Extractor over the Regular Expression Extractor
    when the response is JSON, since it does not break when whitespace
    or field ordering changes
-   [ ] Handle arrays/missing-field cases with a documented default

## 28. Correlation: Login → Authenticated Request

``` text
HTTP Request: POST /api/login
    body: { "username": "${username}", "password": "${password}" }
    │
    ▼  JSON Extractor: $.token  →  ${authToken}
    │
HTTP Request: GET /api/orders
    header: Authorization: Bearer ${authToken}
```

-   [ ] Build a two-request chain: authenticate, then call a protected
    endpoint using the extracted token
-   [ ] Verify the second request actually receives a fresh token per
    thread/iteration, not a hardcoded one
-   [ ] Understand this is the correct way to load-test authenticated
    APIs, and that a hardcoded token instead is a load-test anti-pattern
    (a token that is stale, shared, or rate-limited)

**Checkpoint:** Build a login → authenticated-request chain with a real
or mock API, and prove correlation works by observing distinct tokens
per thread in View Results Tree.

------------------------------------------------------------------------

# Tier 9 --- Headers, Cookies, and Auth

## 29. HTTP Header Manager

-   [ ] Add an HTTP Header Manager to send common headers (e.g.
    `Content-Type: application/json`)
-   [ ] Set an `Authorization` header using an extracted variable
    (`Bearer ${authToken}`)
-   [ ] Scope a Header Manager at the Thread Group level for headers
    shared by all requests, and locally for headers specific to one
    sampler

## 30. HTTP Cookie Manager

-   [ ] Add an HTTP Cookie Manager to a Thread Group testing a
    cookie/session-based application
-   [ ] Understand that each thread gets its own independent cookie
    storage, correctly simulating separate users
-   [ ] Understand the difference between cookie-based session auth
    (Cookie Manager) and bearer-token auth (Header Manager +
    extractor) and choose the one that matches the API under test

**Checkpoint:** Explain, for a given API, whether it needs a Cookie
Manager, a Header Manager with a correlated token, or both.

------------------------------------------------------------------------

# Tier 10 --- Logic Controllers in Practice

## 31. Loop Controller

-   [ ] Repeat a group of samplers a fixed number of times within a
    single thread iteration
-   [ ] Distinguish this from the Thread Group's own Loop Count, which
    repeats the *entire* thread body

## 32. If Controller

-   [ ] Conditionally execute child samplers based on a JMeter
    expression (e.g. only call a "cancel order" endpoint if a prior
    response indicated the order was created)
-   [ ] Avoid overusing conditional logic to the point that the test
    plan becomes harder to reason about than the system under test

## 33. Transaction Controller

-   [ ] Group several samplers into a single named "transaction"
-   [ ] Understand that the Transaction Controller reports one combined
    response time for the whole group, in addition to the individual
    sampler times
-   [ ] Use it to measure an entire multi-request user action (e.g.
    "checkout" = add-to-cart + apply-coupon + place-order) as one
    meaningful unit in the Aggregate Report

``` text
Transaction Controller: "Checkout"
├── POST /cart/items
├── POST /cart/coupon
└── POST /orders
        │
        ▼
Aggregate Report shows "Checkout" as one row,
in addition to each individual request
```

**Checkpoint:** Wrap a 3-request user journey in a Transaction
Controller and confirm the Aggregate Report shows a combined row for
the named transaction.

------------------------------------------------------------------------

# Tier 11 --- Timers: Realistic Think-Time

## 34. Timers for Realistic Think-Time

-   [ ] Add a Constant Timer for a fixed pause between samplers
-   [ ] Add a Uniform Random Timer for a randomized pause within a range
-   [ ] Understand "think-time" as the pause a real human takes between
    actions (reading a page, deciding what to click)
-   [ ] Understand that omitting think-time causes threads to hammer
    the server as fast as possible, which simulates unrealistic
    machine-gun traffic rather than realistic user load
-   [ ] Understand that unrealistically long or short think-time
    distorts both throughput and the concurrency the test actually
    achieves
-   [ ] Place timers at the scope needed (Thread Group-wide vs
    per-sampler)

**Checkpoint:** Add a Uniform Random Timer between two requests in a
user journey and explain, in terms of throughput, what changed compared
to no timer at all.

------------------------------------------------------------------------

# Tier 12 --- Command-Line Execution & Reporting

## 35. Running JMeter from the CLI

``` bash
jmeter -n -t plan.jmx -l results.jtl -e -o report/
```

-   [ ] `-n` --- run in non-GUI (CLI) mode
-   [ ] `-t plan.jmx` --- the test plan file to run
-   [ ] `-l results.jtl` --- write raw sample results to this file
-   [ ] `-e` --- generate the HTML Dashboard Report after the test
    completes
-   [ ] `-o report/` --- output directory for the generated report
    (JMeter requires this directory to not already exist, or to be
    empty, the first time it is created)
-   [ ] `-j jmeter.log` --- optional explicit log file location
-   [ ] `-J property=value` --- override a JMeter property from the
    command line (e.g. to inject a target host or thread count without
    editing the `.jmx`)
-   [ ] `-g results.jtl -o report/` --- generate a report from an
    existing results file, without re-running the test

## 36. The HTML Dashboard Report

-   [ ] Generate the dashboard with `-e -o <dir>`
-   [ ] Open `index.html` from the generated report directory
-   [ ] Read the APDEX (Application Performance Index) summary
-   [ ] Read statistics tables (matching Aggregate Report data) per
    request and per transaction
-   [ ] Read time-series charts: response times over time, active
    threads over time, throughput over time
-   [ ] Use the report as the shareable artifact handed to a team,
    rather than raw console output

## 37. JMeter in CI/CD Pipelines

``` text
Checkout code
   ↓
Deploy to a test/staging environment
   ↓
jmeter -n -t plan.jmx -l results.jtl -e -o report/
   ↓
Publish report/ as a build artifact
   ↓
(Optional) Fail the pipeline if error rate or
p95 response time exceeds a threshold
```

-   [ ] Understand that a JMeter run can be one stage in an automated
    pipeline, not only a manually triggered activity
-   [ ] Understand that CI runners generating load need adequate CPU/
    memory themselves, or the test again measures JMeter's own
    constraints rather than the target system
-   [ ] Understand that pass/fail pipeline gating is normally
    implemented by parsing the results file or dashboard statistics
    (e.g. with a script, or a JMeter Maven/Gradle plugin), since
    JMeter's own exit code alone does not encode a performance
    threshold
-   [ ] Leave CI-provider-specific configuration (GitHub Actions,
    Jenkins, GitLab CI) to the CI/CD Fundamentals skill tree; JMeter
    only needs to be runnable as a single reproducible shell command

**Checkpoint:** Run a test plan entirely from the command line, produce
an HTML Dashboard Report, and describe how this exact command could be
inserted as a stage in an automated pipeline.

------------------------------------------------------------------------

# Tier 13 --- Interpreting Results Like a Professional

## 38. Throughput

-   [ ] Understand throughput as requests completed per unit time
    (JMeter reports it per second, e.g. in the Aggregate Report)
-   [ ] Understand that throughput is a function of both server
    capacity and the load profile (threads, ramp-up, think-time) you
    configured
-   [ ] Avoid treating a high throughput number alone as evidence of a
    healthy system

## 39. Percentiles vs Averages

-   [ ] Understand that an average response time can hide a long tail
    of very slow requests
-   [ ] Understand p50 (median), p90, and p99 as "N% of requests
    completed in this time or faster"
-   [ ] Understand why service-level objectives are normally written
    against a percentile (e.g. "p99 under 500ms"), not an average
-   [ ] Reason through why: if 1% of users regularly experience a
    multi-second response, an average across all requests can still
    look acceptable while a meaningful fraction of real users have a
    bad experience
-   [ ] Read the 90% Line, 95% Line, and 99% Line columns in JMeter's
    Aggregate Report as exactly this information

``` text
Average: 220 ms   ← looks fine
p50:     180 ms   ← most users
p90:     310 ms   ← still fine
p99:    4200 ms   ← 1 in 100 requests is badly slow

The average hides the p99 problem entirely.
```

## 40. Error Rate

-   [ ] Read the Error % column in the Summary/Aggregate Report
-   [ ] Understand that a sample fails if the HTTP response indicates
    failure (e.g. a 5xx status) or if an assertion attached to it fails
-   [ ] Treat rising error rate as at least as important as rising
    response time --- a server that "fails fast" under load can show
    excellent (low) response times while serving mostly errors
-   [ ] Never evaluate a load test on throughput alone without also
    checking the error rate for the same run

**Checkpoint:** Given an Aggregate Report row showing high throughput,
low average response time, and a 40% error rate, explain in one
sentence why this run should not be reported as a success.

------------------------------------------------------------------------

# Tier 14 --- Distributed Testing Awareness

## 41. Distributed Testing

A single machine has finite CPU, memory, and network capacity, and can
itself become the bottleneck before the system under test does.

``` text
Controller (your machine, running -n -t plan.jmx -R host1,host2)
   │
   ├── jmeter-server on load-generator host1
   └── jmeter-server on load-generator host2
                │
                ▼
        System Under Test
```

-   [ ] Understand the controller/generator model: one controller
    machine coordinates one or more remote `jmeter-server` processes
-   [ ] Recognize the `-R host1,host2` (or `-r` for properties-file
    hosts) flag for specifying remote load-generator hosts
-   [ ] Understand that RMI networking between controller and
    generators must be reachable and correctly configured
    (`server.rmi.localport`, etc.)
-   [ ] Understand this is used when a single machine cannot generate
    enough concurrent load to reach the traffic level under test, or to
    generate load from multiple geographic locations
-   [ ] Recognize this as an awareness-level topic: knowing it exists
    and roughly how it is structured is sufficient; deep multi-host
    cluster administration is not the target depth for this tree

**Checkpoint:** Explain, in one paragraph, when a single JMeter instance
is no longer sufficient and distributed testing becomes necessary.

------------------------------------------------------------------------

# Tier 15 --- Test Design Strategy

## 42. Single Endpoint vs Full User Journey

``` text
Single-endpoint test
    → GET /api/products/42
    → isolates one endpoint's performance

Full user-journey test
    → GET /api/products (browse)
    → POST /api/cart (add item)
    → POST /api/login (authenticate)
    → POST /api/orders (checkout)
    → simulates realistic combined system load
```

-   [ ] Build a single-endpoint test to isolate and characterize one
    piece of functionality
-   [ ] Build a multi-step, correlated user-journey test to measure
    realistic combined load across a workflow
-   [ ] Understand that a system can pass every single-endpoint test in
    isolation and still fail under a realistic combined journey, because
    shared resources (database connections, thread pools, downstream
    services) are exercised differently
-   [ ] Choose Transaction Controllers to keep journey-level reporting
    readable, per Tier 10
-   [ ] Decide, for a given question, whether a single-endpoint test or
    a full-journey test actually answers it

**Checkpoint:** Given a claim like "the checkout flow is too slow,"
explain why a single-endpoint test of just the checkout endpoint might
miss the actual bottleneck.

------------------------------------------------------------------------

# Tier 16 --- Testing Microservices with JMeter

## 43. Testing Microservices Through a Gateway

In a microservice architecture, client traffic normally does not go
directly to each backing service --- it goes through an API gateway.

``` text
JMeter
   │
   ▼
Spring Cloud Gateway
   │
   ├──▶ Order Service
   ├──▶ Inventory Service
   └──▶ Payment Service
```

-   [ ] Point JMeter at the gateway's host/port rather than at
    individual downstream services
-   [ ] Understand that this measures the same path a real client
    traverses: routing, any gateway-level filters, and downstream
    service latency combined
-   [ ] Correlate authentication tokens and IDs across a request chain
    that flows through the gateway, exactly as in Tier 8
-   [ ] After a run, use logs/metrics/tracing on the individual services
    (not JMeter itself) to localize which downstream service
    contributed most to an observed slowdown
-   [ ] Recognize that testing an individual service directly (bypassing
    the gateway) is a valid, narrower complementary test --- but it does
    not represent client-visible performance on its own

## 44. Connecting to the Eureka & Gateway Capstones

This tree exists specifically because the Spring Cloud & Microservices
Fundamentals, Eureka Service Discovery, and Spring Cloud Gateway skill
trees each assume this competence in their own later material:

-   [ ] Recognize that the Spring Cloud Gateway tree's capstone asks the
    learner to point JMeter at the gateway and observe client-visible
    performance across several routed endpoints
-   [ ] Recognize that the Eureka Service Discovery tree's "JMeter
    Connection" tier asks the learner to observe whether scaling the
    number of registered service instances changes client-visible
    performance --- i.e., using JMeter as the measurement tool for a
    service-discovery/load-balancing question, not as a subject to be
    taught there
-   [ ] Recognize that the Spring Cloud & Microservices Fundamentals
    tree's capstone treats a JMeter run against the whole system as an
    optional graded deliverable
-   [ ] Arrive at each of those capstones already able to build a
    correlated, assertion-checked, CLI-executed test plan with an HTML
    Dashboard Report --- this tree is the place that competence is
    actually taught

**Checkpoint:** Build a Thread Group that logs in, extracts a token, and
exercises two or three routes on a gateway (real or mock), reports
p90/p99 and error rate from an Aggregate Report, and explain what a
Transaction Controller around the whole flow would add.

------------------------------------------------------------------------

# Tier 17 --- The Broader Landscape

## 45. JMeter vs Gatling vs k6 vs Locust

JMeter is one of several load-testing tools a professional may
encounter. Awareness of the alternatives helps you recognize why a team
might choose differently, without requiring mastery of each.

  ----------------------------------------------------------------------
  Tool      Model                          Notes
  --------- ------------------------------ -----------------------------
  JMeter    GUI-authored XML test plans,   Mature, huge protocol
            Java-based                     support, GUI aids learning;
                                            heavier resource footprint
                                            per thread

  Gatling   Code-first (Scala DSL),        Efficient async engine, code
            report-focused                 reviewable as source, steeper
                                            initial learning curve

  k6        Code-first (JavaScript),       Lightweight, CLI/CI-native,
            CLI-native                     popular for developer-written
                                            load tests

  Locust    Code-first (Python)            Python-based user behavior
                                            scripts, easy for
                                            Python-fluent teams
  ----------------------------------------------------------------------

-   [ ] Recognize that all four tools answer the same fundamental
    questions (throughput, response time, error rate under load)
-   [ ] Recognize JMeter's GUI-first authoring as both an onboarding
    advantage and a reason test plans can be harder to code-review than
    a plain script
-   [ ] Recognize code-first tools (Gatling/k6/Locust) as often easier
    to keep in version control and review as ordinary source code
-   [ ] Understand this is awareness-level: knowing these tools exist
    and roughly how they differ is the goal, not fluency in all of them

------------------------------------------------------------------------

# Practical Competency Checkpoints

-   [ ] Explain load, stress, soak, and spike testing and pick the right
    one for a given question
-   [ ] Explain the Test Plan tree model: Thread Groups, Samplers,
    Listeners, Config Elements, Assertions, Timers, Logic Controllers
-   [ ] Predict, from a `.jmx` tree diagram, exactly what runs and in
    what scope
-   [ ] Explain why GUI mode must never be used for real load generation
-   [ ] Build a Thread Group + HTTP Request test plan against a real
    REST endpoint
-   [ ] Use HTTP Request Defaults to avoid repeating server/port
-   [ ] Read a Summary Report and an Aggregate Report
-   [ ] Explain why heavy listeners must be disabled during real runs
-   [ ] Add Response, Duration, and JSON assertions to verify
    correctness, not just capacity
-   [ ] Parameterize a test plan with CSV Data Set Config
-   [ ] Correlate a login token (or other extracted value) into a
    subsequent authenticated request using a JSON or Regular Expression
    Extractor
-   [ ] Configure an HTTP Header Manager and an HTTP Cookie Manager and
    choose correctly between them
-   [ ] Use Loop, If, and Transaction Controllers appropriately
-   [ ] Add realistic think-time with timers
-   [ ] Run a test plan from the CLI with `-n -t -l -e -o` and produce
    an HTML Dashboard Report
-   [ ] Explain how a JMeter run fits into a CI/CD pipeline
-   [ ] Explain why percentiles (p90/p99), not averages, drive
    performance conclusions
-   [ ] Never evaluate a run on throughput alone without checking error
    rate
-   [ ] Explain, at an awareness level, when distributed testing becomes
    necessary
-   [ ] Design a full user-journey test, not just single-endpoint tests
-   [ ] Point a test plan at a gateway rather than individual
    microservices, and localize a downstream bottleneck afterward using
    logs/metrics rather than JMeter itself
-   [ ] Compare JMeter to Gatling/k6/Locust at an awareness level

------------------------------------------------------------------------

# Suggested Practice Progression

## Stage 1 --- First Test Plan

``` text
Install JMeter
    ↓
GUI mode
    ↓
Thread Group (1 thread, 1 loop)
    ↓
HTTP Request → a real or mock GET endpoint
    ↓
View Results Tree
```

**Exercise:** Send a single GET request to a public or local REST API
and confirm the response in View Results Tree before touching thread
counts.

## Stage 2 --- Scaling Up Correctly

``` text
Thread Group (50 threads, 10s ramp-up, 5 loops)
    ↓
HTTP Request Defaults
    ↓
CLI mode (-n -t -l)
    ↓
Aggregate Report (read from the .jtl or a rerun with listeners)
```

**Exercise:** Convert the Stage 1 plan into a CLI-run load test, and
read throughput, average, and p90/p99 from the Aggregate Report.

## Stage 3 --- Correctness Under Load

``` text
Response Assertion (status 200)
    ↓
Duration Assertion (e.g. 800ms)
    ↓
JSON Assertion (field exists/matches)
```

**Exercise:** Deliberately introduce a bug or slow down the target
endpoint and confirm each assertion type fails independently and shows
up in Error %.

## Stage 4 --- Data-Driven, Correlated Journeys

``` text
CSV Data Set Config (test users)
    ↓
POST /login  →  JSON Extractor  →  ${authToken}
    ↓
GET /protected-resource  (Authorization: Bearer ${authToken})
    ↓
Transaction Controller around the whole journey
```

**Exercise:** Build a login → authenticated-request chain with distinct
CSV-driven users per thread, and confirm (via View Results Tree during
authoring) that each thread receives its own token.

## Stage 5 --- Reporting & CI

``` text
jmeter -n -t plan.jmx -l results.jtl -e -o report/
    ↓
Open report/index.html
    ↓
Describe this command as a CI pipeline stage
```

**Exercise:** Produce a full HTML Dashboard Report from the command line
and identify the p99 response time and error rate for the run.

## Stage 6 --- Microservices Bridge

``` text
JMeter
    ↓
Spring Cloud Gateway
    ↓
Multiple downstream services (via Eureka discovery)
```

**Exercise:** Point the Stage 4 test plan at a gateway fronting two or
more services instead of a single endpoint, and use downstream logs to
explain any change in p99 response time.

------------------------------------------------------------------------

# Capstone

**Scenario:** You are handed a small e-commerce-style REST API (or a
gateway fronting several microservices) with these endpoints:

``` text
POST /api/login              (returns a bearer token)
GET  /api/products            (browse; no auth required)
POST /api/cart/items          (auth required)
POST /api/orders              (auth required; checkout)
```

**Deliverables:**

1.  A `.jmx` test plan containing:
    -   A CSV Data Set Config supplying at least 5 distinct test users
    -   A login request per thread, with the returned token extracted
        via a JSON Extractor
    -   An HTTP Header Manager injecting `Authorization: Bearer
        ${authToken}` on every authenticated request
    -   A full user journey: browse products, add an item to cart,
        place an order --- wrapped in a Transaction Controller named
        "Checkout Journey"
    -   A Uniform Random Timer between steps simulating realistic
        think-time
    -   A Response Assertion (status `200`/`201` as appropriate) and a
        JSON Assertion confirming the order response contains an order
        ID, on the checkout request
2.  A CLI run: `jmeter -n -t plan.jmx -l results.jtl -e -o report/`
    against a Thread Group of at least 50 users with a 20-second
    ramp-up and a 5-minute duration
3.  A short written summary (a few sentences per point) covering:
    -   Overall throughput and p50/p90/p99 response times for the
        "Checkout Journey" transaction
    -   Overall error rate, and an explanation of what, if anything,
        caused any errors
    -   Whether this run should be classified as a load test, stress
        test, soak test, or spike test, and why
    -   One anti-pattern (see below) you deliberately avoided while
        building this plan, and how

**Stretch goal (microservices variant):** Repeat the exact same test
plan, changing only the target host/port, against a Spring Cloud
Gateway instance fronting the same capabilities split across separate
Order/Cart/Catalog services (as built in the Spring Cloud &
Microservices Fundamentals, Eureka, and Spring Cloud Gateway skill
trees), and compare the resulting p99 and error rate to the
single-service run.

------------------------------------------------------------------------

# Interview Readiness

-   [ ] **What is the difference between load, stress, soak, and spike
    testing?** Load applies expected traffic; stress increases traffic
    until failure to find the breaking point; soak applies moderate
    traffic over a long duration to surface leaks; spike applies a
    sudden burst to test recovery.
-   [ ] **Why should JMeter's GUI never be used to generate real load?**
    The GUI's own rendering (tree, listeners, live tables) consumes
    CPU/memory that competes with the threads generating load, skewing
    the measured results; GUI mode is for authoring and debugging only.
-   [ ] **What is the difference between a Thread Group's Loop Count and
    a Loop Controller?** Loop Count repeats the entire thread's sampler
    sequence; a Loop Controller repeats only the samplers nested beneath
    it, once per outer iteration.
-   [ ] **Why do percentiles matter more than averages for performance
    conclusions?** An average can be pulled down by many fast requests
    and hide a long tail of slow ones; a p99 of several seconds means 1%
    of real users had a bad experience even if the average looks fine.
    SLAs are conventionally written against a percentile for this
    reason.
-   [ ] **What is correlation, and why does it matter?** Extracting a
    dynamic value (a token, session ID, or generated resource ID) from
    one response and feeding it into a later request, because that
    value is unique per session/iteration and cannot be hardcoded
    without producing an unrealistic or broken test.
-   [ ] **How would you test a microservices system with JMeter?** Point
    JMeter at the API gateway rather than individual services, so the
    load test exercises the same path real client traffic takes;
    afterward use logs/metrics/tracing on the individual services to
    localize any bottleneck the load test surfaced.
-   [ ] **What is the difference between the Summary Report and the
    Aggregate Report?** Both show running throughput/response-time
    statistics; the Aggregate Report additionally breaks out response
    time percentiles (90%/95%/99% Line), which the Summary Report does
    not.
-   [ ] **How do you run JMeter as part of a CI/CD pipeline?** Run it in
    non-GUI mode (`-n -t plan.jmx -l results.jtl -e -o report/`) as a
    pipeline stage after deploying to a test environment, publish the
    generated report as a build artifact, and optionally fail the build
    by parsing the results for an error-rate or percentile threshold.
-   [ ] **Why should error rate be checked alongside throughput?** A
    server under overload can fail fast, producing low response times
    and high throughput while serving mostly errors; throughput alone
    can make a failing system look healthy.
-   [ ] **When would you use distributed (multi-machine) JMeter
    testing?** When a single controller machine cannot itself generate
    enough concurrent load to reach the traffic level being tested, or
    when load needs to originate from multiple network locations.
-   [ ] **How does JMeter compare to Gatling, k6, or Locust?** JMeter is
    GUI-authored and Java-based with broad protocol support; Gatling,
    k6, and Locust are code-first (Scala, JavaScript, and Python
    respectively), which some teams prefer for version-control review
    and CI-native workflows, at the cost of JMeter's GUI-assisted
    onboarding.

------------------------------------------------------------------------

# Common Anti-Patterns

-   **Running a real load test from the GUI.** The GUI's rendering
    overhead competes with the load-generating threads and invalidates
    the measured results; use CLI/non-GUI mode for anything you intend
    to report.
-   **Testing against production without warning anyone.** An
    unannounced load test against a live production system can degrade
    or take down real user traffic, trigger on-call pages, and corrupt
    unrelated monitoring/analytics; get explicit sign-off and a planned
    window before testing anything but a dedicated test/staging
    environment.
-   **Watching only throughput while ignoring error rate.** A system
    failing fast under overload can display excellent throughput and
    response times while serving mostly errors; always read Error %
    alongside throughput and response time.
-   **Hardcoding a session token or ID instead of correlating it.** A
    hardcoded token expires, gets rate-limited, or represents only one
    session shared unrealistically across every thread; extract dynamic
    values per iteration with a Regular Expression or JSON Extractor.
-   **Omitting or using unrealistic think-time.** Zero think-time turns
    every thread into a machine-gun of back-to-back requests that no
    real user produces, distorting both achieved concurrency and
    throughput; a think-time so long it defeats the intended concurrency
    is equally unrealistic in the other direction.
-   **Leaving heavy listeners (especially View Results Tree) enabled
    during an actual load run.** This adds CPU/memory overhead that
    scales with sample count and again measures JMeter's own overhead
    instead of the target system.
-   **Reporting only the average response time.** Averages hide tail
    latency; report p90/p95/p99 alongside (or instead of) the average.
-   **Testing only isolated endpoints and never a full user journey.** A
    system can pass every single-endpoint test while failing under a
    realistic combined workflow that shares database connections,
    thread pools, or downstream services differently.
-   **Copying an unfamiliar `.jmx` file and running it without reading
    its tree.** Because scope is hierarchical (Tier 13), an unread test
    plan may apply the wrong headers, skip an assertion, or target the
    wrong host entirely.

------------------------------------------------------------------------

# Relationship to Existing Skill Trees

``` text
HTTP / HTTPS / REST API
        │
        ▼
      JMeter  ← YOU ARE HERE
        │
        ├──────────────┬───────────────────┬────────────────┐
        ▼               ▼                    ▼                ▼
Spring Cloud &      Eureka Service      Spring Cloud       CI/CD
Microservices       Discovery           Gateway            Fundamentals
Fundamentals
```

JMeter sits directly on top of the **HTTP/HTTPS/REST API** skill tree:
you cannot design a meaningful test plan against an API whose methods,
status codes, and payload shapes you do not already understand. Basic
command-line fluency, covered in the ubuntu/WSL2 skill map, is assumed
for running JMeter in CLI mode.

Three existing trees are downstream and already assume this tree's
content: the **Spring Cloud & Microservices Fundamentals** tree's
capstone treats a JMeter run against the whole system as an optional
graded deliverable; the **Eureka Service Discovery** tree's "JMeter
Connection" tier uses JMeter as the measurement tool for observing
whether instance scaling changes client-visible performance; and the
**Spring Cloud Gateway** tree's capstone explicitly instructs the
learner to "point JMeter at the gateway" and grades a dedicated JMeter
exercise phase. None of those trees re-teaches JMeter itself --- they
assume the competence built here. The **CI/CD Fundamentals** tree treats
a non-GUI JMeter run as one possible pipeline stage, using exactly the
`-n -t -l -e -o` invocation taught in Tier 12.

The central JMeter mastery question is:

> **Can I take a REST API or a gateway-fronted microservice system I
> already understand, build a realistic, correlated, assertion-checked
> test plan against it, run that plan correctly (never from the GUI),
> and read the results well enough to say --- with evidence, not a
> guess --- whether it performs and behaves correctly under load?**

------------------------------------------------------------------------

# Mastery Progression

``` text
HTTP/REST fundamentals + command-line basics
        │
        ▼
JMETER  ← YOU ARE HERE
│
├── test plan architecture (Thread Groups, Samplers, Listeners,
│   Config Elements, Assertions, Timers, Logic Controllers)
├── GUI-authoring / CLI-execution discipline
├── correctness-under-load assertions
├── parameterization & correlation
├── realistic think-time and user journeys
├── CLI execution, HTML reporting, CI/CD integration
└── percentile-based, error-rate-aware result interpretation
        │
        ▼
Applied against:
├── Spring Cloud & Microservices Fundamentals
├── Eureka Service Discovery
└── Spring Cloud Gateway
        │
        ▼
CI/CD Fundamentals
(JMeter as an automated pipeline stage)
```

The goal is not to memorize every JMeter sampler or plugin. The goal is
to know enough JMeter to design a test that actually answers a real
performance question, run it in a way that produces trustworthy
numbers, and read those numbers --- percentiles and error rate included
--- with the same rigor a professional would expect of any other test
result.

------------------------------------------------------------------------

# Mastery Standard

A full-stack-oriented developer can consider their JMeter foundation
professionally useful when they can independently:

-   [ ] Explain what a load test measures that a functional test cannot
-   [ ] Distinguish load, stress, soak, and spike testing and choose
    correctly among them
-   [ ] Explain the Test Plan tree model and predict scope from
    structure alone
-   [ ] Build a Thread Group + HTTP Request test plan from scratch
-   [ ] Explain why GUI mode must never be used to generate reportable
    load, and use CLI mode instead
-   [ ] Read and interpret a Summary Report and an Aggregate Report
-   [ ] Add Response, Duration, and JSON assertions to check correctness
    under load, not just capacity
-   [ ] Parameterize a test with CSV Data Set Config
-   [ ] Correlate dynamic values (tokens, IDs) across chained requests
    with Regular Expression or JSON Extractors
-   [ ] Configure HTTP Header Manager and HTTP Cookie Manager
    appropriately for a given auth model
-   [ ] Use Loop, If, and Transaction Controllers to structure a
    realistic multi-step journey
-   [ ] Add realistic think-time with timers
-   [ ] Run a full CLI test with an HTML Dashboard Report and explain
    how it fits into a CI/CD pipeline
-   [ ] Explain and prioritize percentiles (p90/p99) over averages when
    drawing conclusions
-   [ ] Never evaluate a run on throughput without also checking error
    rate
-   [ ] Explain, at an awareness level, when distributed testing becomes
    necessary
-   [ ] Design both single-endpoint and full-user-journey tests, and
    know when each is the right tool
-   [ ] Point a test plan at an API gateway to test a microservices
    system as a real client would experience it, then localize any
    bottleneck using the target system's own logs/metrics
-   [ ] Recognize and avoid every anti-pattern listed in this tree
-   [ ] Compare JMeter to Gatling, k6, and Locust at an awareness level
    and explain why a team might choose differently

For the full-stack path, that capability --- not memorizing every
sampler, listener, or plugin JMeter ships with --- is what makes the
"point JMeter at it and observe performance" instruction in the Spring
Cloud, Eureka, and Spring Cloud Gateway capstones something the learner
can actually execute.
