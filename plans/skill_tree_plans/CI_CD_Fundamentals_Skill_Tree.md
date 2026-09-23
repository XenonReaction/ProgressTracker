# CI/CD Fundamentals Skill Tree

> **Goal:** Build a tool-independent understanding of Continuous Integration, Continuous Delivery, and Continuous Deployment before learning a specific platform such as Jenkins, GitHub Actions, or GitLab CI/CD.
>
> **Target level:** Full-stack developer who can design, explain, troubleshoot, and improve a practical software delivery pipeline without depending on one vendor's terminology or configuration syntax.
>
> **Primary prerequisite:** **Git / Version Control**
>
> **Supporting prerequisites:** Basic command-line use, application build tools (such as Maven or npm), and basic automated testing.
>
> **Important distinction:** CI/CD is a set of software-development practices and automation concepts. Jenkins, GitHub Actions, and GitLab CI/CD are tools that implement those concepts.
>
> **Scope boundary:** This tree teaches the transferable CI/CD concepts. Tool-specific pipeline syntax, Jenkins administration, GitHub Actions workflows, GitLab runners, advanced cloud deployment, Kubernetes deployment, and GitOps belong in later skill trees.

---

# Skill Tree Overview

```text
Git / Version Control
        │
        ▼
CI/CD Fundamentals
        │
        ├── Continuous Integration
        │
        ├── Continuous Delivery
        │
        └── Continuous Deployment
        │
        ▼
Pipeline Design
        │
        ├── Trigger
        ├── Checkout
        ├── Build
        ├── Test
        ├── Quality Checks
        ├── Package
        ├── Publish
        └── Deploy
        │
        ▼
Delivery Reliability
        │
        ├── Environments
        ├── Configuration
        ├── Secrets
        ├── Artifacts
        ├── Quality Gates
        ├── Approvals
        ├── Failure Handling
        ├── Rollback
        └── Observability
        │
        ▼
Production Practices
        │
        ├── Security
        ├── Reproducibility
        ├── Caching
        ├── Parallelism
        ├── Supply Chain
        └── Pipeline Maintenance
        │
        ▼
Choose an Implementation
   ┌────────┼─────────────┐
   ▼        ▼             ▼
Jenkins  GitHub Actions  GitLab CI/CD
```

---

# Dependency Map

```text
                 Git
                  │
                  ▼
          CI/CD Fundamentals
                  │
       ┌──────────┼──────────┐
       ▼          ▼          ▼
      Build      Test      Package
       │          │          │
       └──────────┼──────────┘
                  ▼
               Artifact
                  │
                  ▼
                Release
                  │
                  ▼
               Deploy
                  │
       ┌──────────┼──────────┐
       ▼          ▼          ▼
       Dev       Stage      Prod
                  │
                  ▼
             Operations
```

---

# Tier 0 — Why CI/CD Exists

## 1. Manual Software Delivery

Without automation, a release can look like:

```text
Developer finishes code
        │
        ▼
Remember build commands
        │
        ▼
Run tests manually
        │
        ▼
Copy files manually
        │
        ▼
Configure server manually
        │
        ▼
Hope every step was correct
```

Problems include:

- [ ] Forgotten steps
- [ ] Inconsistent environments
- [ ] Human error
- [ ] Slow feedback
- [ ] Difficult releases
- [ ] Poor reproducibility
- [ ] Unclear responsibility
- [ ] Fear of deploying

## 2. Automation Goal

CI/CD turns repeated delivery steps into a defined process.

```text
Code Change
    │
    ▼
Repeatable Pipeline
    │
    ▼
Verified Software
    │
    ▼
Deployable Release
```

The goal is not merely "deploy faster."

The goal is to make changes:

```text
smaller
repeatable
testable
traceable
safer
easier to release
```

**Checkpoint:** Explain why automating an unreliable process does not automatically make the process reliable.

---

# Tier 1 — Continuous Integration

## 3. Continuous Integration

Continuous Integration means developers integrate changes into shared version control frequently and automated checks validate those changes.

```text
Developer A ──┐
Developer B ──┼──► Shared Repository
Developer C ──┘
                     │
                     ▼
                Automated Build
                     │
                     ▼
                Automated Tests
```

Core ideas:

- [ ] Integrate frequently
- [ ] Keep changes reasonably small
- [ ] Automatically build
- [ ] Automatically test
- [ ] Detect integration problems quickly
- [ ] Keep the main development branch healthy

## 4. Feedback

A major CI goal is:

```text
Change
  │
  ▼
Fast automated feedback
  │
  ├── PASS
  └── FAIL
```

The earlier a defect is detected, the easier it is usually to identify which change caused it.

## 5. CI Is More Than a Server

Installing Jenkins does not create Continuous Integration by itself.

```text
CI Practice
    │
    ├── frequent integration
    ├── automated verification
    ├── healthy shared branch
    └── rapid feedback
```

A CI tool supports those practices.

---

# Tier 2 — Continuous Delivery

## 6. Continuous Delivery

Continuous Delivery extends CI so that software remains in a state that can be released reliably.

```text
Commit
  │
  ▼
Build
  │
  ▼
Test
  │
  ▼
Package
  │
  ▼
Release Candidate
  │
  ▼
Ready for Deployment
```

Production deployment may still require a human decision.

## 7. Release Readiness

Continuous Delivery aims to reduce:

```text
"We finished development,
but now we need three weeks
to figure out how to release it."
```

The release process should already be exercised and automated.

---

# Tier 3 — Continuous Deployment

## 8. Continuous Deployment

Continuous Deployment goes one step further.

```text
Code Change
    │
    ▼
Pipeline
    │
    ▼
All Required Checks Pass
    │
    ▼
Automatic Production Deployment
```

No manual production-release decision is required for qualifying changes.

## 9. Delivery vs Deployment

```text
Continuous Delivery
        │
        ▼
Automatically prepared
for production
        │
        ▼
Manual approval MAY exist


Continuous Deployment
        │
        ▼
Qualifying change automatically
reaches production
```

**Checkpoint:** Explain CI, Continuous Delivery, and Continuous Deployment without referring to Jenkins or GitHub Actions.

---

# Tier 4 — Pipeline Fundamentals

## 10. Pipeline

A pipeline is an automated sequence of work that moves a software change toward a verified/releasable/deployed state.

```text
Source
  │
  ▼
Build
  │
  ▼
Test
  │
  ▼
Package
  │
  ▼
Publish
  │
  ▼
Deploy
```

## 11. Stage

A stage groups related work.

Example:

```text
Pipeline
├── Build
├── Test
├── Package
└── Deploy
```

## 12. Step

A step is an individual action.

```text
Test Stage
├── install dependencies
├── run unit tests
└── publish test report
```

## 13. Job

A job is a unit of execution containing steps.

Different platforms use the word differently, but the general concept is:

```text
Job
  │
  ├── step
  ├── step
  └── step
```

Learn the concepts before memorizing platform terminology.

---

# Tier 5 — Pipeline Triggers

## 14. Trigger

A trigger starts pipeline execution.

Common triggers:

- [ ] Push
- [ ] Pull/merge request
- [ ] Tag
- [ ] Manual action
- [ ] Schedule
- [ ] API/webhook
- [ ] Upstream pipeline/event

## 15. Webhooks

Conceptually:

```text
Git Platform
    │
    │ repository event
    ▼
CI/CD System
    │
    ▼
Pipeline Starts
```

A webhook allows one system to notify another that an event occurred.

## 16. Trigger Strategy

Not every event should necessarily run every pipeline stage.

Example:

```text
Pull Request
    │
    └──► Build + Test

Merge to main
    │
    └──► Build + Test + Package

Release Tag
    │
    └──► Build + Test + Publish + Deploy
```

---

# Tier 6 — Source Checkout

## 17. Pipeline Input

The pipeline needs a known source revision.

```text
Repository
    │
    ▼
Commit SHA
    │
    ▼
Pipeline Workspace
```

- [ ] Checkout
- [ ] Branch
- [ ] Tag
- [ ] Commit SHA
- [ ] Repository authentication awareness

## 18. Traceability

You should be able to answer:

```text
Which source commit produced this artifact?
```

That connection is fundamental to reliable delivery.

---

# Tier 7 — Build Automation

## 19. Build

The build transforms source code into usable software or validates that the source can be compiled/bundled.

Examples:

```text
Java
source → Maven/Gradle → JAR

Angular/React
source → npm build → static assets
```

## 20. Build Tool Ownership

CI/CD should call the application's existing build system.

```text
Pipeline
    │
    ▼
mvn test/package

Pipeline
    │
    ▼
npm test/build
```

Do not put all build logic directly into the CI vendor configuration if it belongs in Maven/npm/scripts.

## 21. Build Failure

If compilation fails:

```text
Pipeline should stop
```

A broken build should not proceed toward release.

---

# Tier 8 — Automated Testing

## 22. Unit Tests

Fast tests should normally run early.

```text
Build
  │
  ▼
Unit Tests
  │
  ├── pass → continue
  └── fail → stop
```

## 23. Integration Tests

Integration tests verify interactions between components.

Examples:

```text
Spring Boot ↔ PostgreSQL
service ↔ repository
application ↔ external dependency test double
```

## 24. End-to-End Tests

E2E tests validate larger application workflows.

They are often:

```text
slower
more expensive
more environment-dependent
```

so pipeline placement should be deliberate.

## 25. Test Pyramid Awareness

Conceptually:

```text
        E2E
       /   \
 Integration
 /           \
Unit Unit Unit
```

CI/CD does not define your testing strategy, but it automates the strategy.

---

# Tier 9 — Test Reports

## 26. Machine-Readable Results

A pipeline should not merely print:

```text
tests passed
```

It should preserve useful results.

Examples:

- [ ] Passed/failed counts
- [ ] Failed test names
- [ ] Duration
- [ ] Historical trends awareness
- [ ] Coverage awareness

## 27. Failure Visibility

A failed pipeline should make the reason discoverable without requiring the developer to reproduce everything locally first.

---

# Tier 10 — Quality Checks

## 28. Quality Gate Concept

A quality gate determines whether software may proceed.

```text
Change
  │
  ▼
Checks
  │
  ├── compilation
  ├── tests
  ├── linting
  ├── static analysis
  └── security checks
  │
  ▼
PASS / FAIL
```

## 29. Linting / Static Analysis

Awareness of:

```text
format checks
linting
code-quality analysis
type checking
```

## 30. Security Scanning Awareness

Possible pipeline checks include:

- [ ] Dependency vulnerabilities
- [ ] Secret detection
- [ ] Static application security testing
- [ ] Container scanning
- [ ] License/policy checks

Detailed DevSecOps deserves a later specialization.

---

# Tier 11 — Packaging

## 31. Package

A package is a distributable output.

Examples:

```text
JAR
WAR
ZIP
frontend bundle
container image
```

## 32. Build Once Principle

Prefer:

```text
Build artifact once
        │
        ├── test
        ├── stage
        └── production
```

over:

```text
rebuild separately for every environment
```

when practical.

This improves confidence that production receives the same tested software.

---

# Tier 12 — Artifacts

## 33. Artifact

An artifact is an output produced by the pipeline that should be preserved or passed onward.

Examples:

```text
JAR
test report
coverage report
ZIP
container image
```

## 34. Artifact Metadata

Useful metadata:

```text
version
commit
build number
timestamp
checksum
```

## 35. Artifact Repository

Artifacts often belong in a dedicated repository/registry.

Concept:

```text
Pipeline
   │
   ▼
Artifact Repository
   │
   ├── version 1
   ├── version 2
   └── version 3
```

Examples of specific tools belong in later trees.

---

# Tier 13 — Container Images & Registries

## 36. Container as Artifact

With Docker:

```text
Source
  │
  ▼
Build
  │
  ▼
Docker Image
  │
  ▼
Registry
  │
  ▼
Deployment Platform
```

Docker itself should be learned in its own skill tree.

## 37. Image Tagging

Understand why:

```text
latest
```

alone is weak traceability.

Prefer immutable/versioned identifiers where appropriate.

```text
app:1.4.2
app:<commit-sha>
```

## 38. Registry

A container registry stores and distributes container images.

CI/CD needs to understand:

- [ ] Authentication
- [ ] Push
- [ ] Pull
- [ ] Version/tag
- [ ] Immutability concepts

---

# Tier 14 — Environments

## 39. Common Environments

```text
Development
     │
     ▼
Testing
     │
     ▼
Staging
     │
     ▼
Production
```

Organizations vary; these names are not universal.

## 40. Purpose

Different environments allow software to be evaluated under progressively more production-like conditions.

## 41. Environment Parity

Aim to reduce unnecessary differences.

```text
"It worked in staging"
```

is less useful if staging and production are fundamentally different.

---

# Tier 15 — Configuration

## 42. Code vs Configuration

The same application artifact may use different runtime configuration.

```text
Same JAR
  │
  ├── dev DB URL
  ├── staging DB URL
  └── production DB URL
```

## 43. Environment Variables

Understand external configuration:

```text
artifact
+
environment-specific configuration
```

Do not hardcode production values into source code.

## 44. Configuration Drift

Manual environment changes can produce:

```text
server A ≠ server B
```

Automated/declarative configuration helps reduce drift.

---

# Tier 16 — Secrets

## 45. Secret Examples

```text
passwords
API keys
private keys
tokens
cloud credentials
registry credentials
```

## 46. Secret Rules

- [ ] Do not commit secrets to Git
- [ ] Do not print secrets in logs
- [ ] Limit secret access
- [ ] Rotate secrets
- [ ] Use platform/secret-management facilities
- [ ] Separate secrets from ordinary configuration

## 47. Least Privilege

A pipeline credential should have only the permissions required.

```text
build job
does not automatically need
production administrator access
```

---

# Tier 17 — Branch & Pull Request Workflows

## 48. Branch Validation

Common pattern:

```text
Feature Branch
      │
      ▼
Pull Request
      │
      ▼
CI Validation
      │
      ├── build
      ├── test
      └── quality checks
```

## 49. Protected Branches

A repository can require checks before merging.

Conceptually:

```text
PR
 │
 ├── review passes
 ├── CI passes
 └── policy passes
       │
       ▼
     merge
```

## 50. Main Branch Health

A useful CI goal is that the main integration branch remains deployable or close to deployable.

---

# Tier 18 — Versioning & Releases

## 51. Version Identity

A release should have a recognizable identity.

Examples:

```text
1.0.0
1.4.2
release tag
commit SHA
build number
```

## 52. Semantic Versioning Awareness

Understand:

```text
MAJOR.MINOR.PATCH
```

at a practical level.

Not every project uses SemVer.

## 53. Release Tag

Git tags can identify release source.

```text
Git Tag
   │
   ▼
Pipeline
   │
   ▼
Versioned Artifact
```

---

# Tier 19 — Deployment Fundamentals

## 54. Deployment

Deployment moves a software release into a runtime environment.

```text
Artifact
   │
   ▼
Environment
   │
   ▼
Running Application
```

CI/CD coordinates deployment, but deeper deployment strategies deserve their own future skill tree.

## 55. Deployment Inputs

Know:

- [ ] Artifact/image
- [ ] Configuration
- [ ] Secrets
- [ ] Target environment
- [ ] Deployment version
- [ ] Health verification

---

# Tier 20 — Approvals

## 56. Manual Gate

Automation can intentionally stop.

```text
Staging verified
      │
      ▼
Manual Approval
      │
      ▼
Production
```

A manual approval is not automatically evidence of poor CI/CD.

## 57. Approval Purpose

Approvals may exist for:

- [ ] Business timing
- [ ] Compliance
- [ ] Risk management
- [ ] Production change policy

Avoid approvals that exist only because the pipeline is unreliable.

---

# Tier 21 — Pipeline Conditions

## 58. Conditional Execution

Not every stage runs every time.

Examples:

```text
if branch == main
if tag exists
if frontend changed
if backend changed
if manual release requested
```

## 59. Keep Logic Understandable

Complex conditions can make pipelines difficult to predict.

Treat pipeline logic as production code.

---

# Tier 22 — Parallelism

## 60. Parallel Work

Independent jobs can run simultaneously.

```text
             ┌── Backend Tests
Checkout ────┼── Frontend Tests
             └── Security Scan
```

Then:

```text
all required jobs pass
        │
        ▼
continue
```

## 61. Benefit

Parallelization can reduce feedback time.

## 62. Tradeoff

More parallel workers can mean:

- [ ] More infrastructure
- [ ] More cost
- [ ] More contention
- [ ] More complexity

Optimize after understanding bottlenecks.

---

# Tier 23 — Caching

## 63. Cache Purpose

Repeatedly downloading unchanged dependencies wastes time.

Examples:

```text
Maven dependencies
npm packages
build intermediates
```

## 64. Cache vs Artifact

Do not confuse them.

```text
Cache
speed optimization
may be discarded

Artifact
intentional pipeline output
must often be preserved
```

## 65. Cache Correctness

Bad cache keys can create confusing builds.

A pipeline should remain correct even if its cache is cleared.

---

# Tier 24 — Reproducible Builds

## 66. Reproducibility

Given the same source and declared inputs, builds should behave consistently.

Threats:

```text
undeclared local dependency
floating dependency versions
manual files
environment differences
timestamps/randomness
```

## 67. Clean Environment

CI is valuable partly because it builds outside the developer's customized machine.

```text
"It works on my machine"
        │
        ▼
clean CI environment
        │
        ▼
prove required dependencies are declared
```

---

# Tier 25 — Idempotency

## 68. Idempotent Automation

Where practical, rerunning an operation should not unpredictably damage the environment.

Concept:

```text
run deployment
run deployment again
        │
        ▼
known stable result
```

## 69. Why It Matters

Retries are safer when operations are designed to tolerate repetition.

---

# Tier 26 — Failure Handling

## 70. Fail Fast

If an essential early check fails:

```text
compile fails
      │
      ▼
STOP
```

Do not waste resources on later deployment work.

## 71. Failure Categories

Distinguish:

```text
code failure
test failure
infrastructure failure
network failure
credential failure
deployment failure
```

Different failures may require different responses.

## 72. Useful Failure Output

A pipeline failure should answer:

```text
Which stage failed?
Which command/check failed?
What revision was running?
Where are logs/results?
```

---

# Tier 27 — Retry

## 73. Appropriate Retry

Retry may make sense for transient failures:

```text
temporary network problem
temporary registry outage
```

## 74. Inappropriate Retry

Do not repeatedly retry:

```text
compilation error
failing unit test
invalid configuration
```

and hope it becomes correct.

## 75. Bounded Retry

Retries should be limited.

Infinite retry can hide failures and consume resources.

---

# Tier 28 — Rollback

## 76. Rollback Concept

If a deployment causes unacceptable behavior:

```text
New Version
    │
    ▼
Problem Detected
    │
    ▼
Restore Known-Good Version
```

## 77. Rollback Requirements

Reliable rollback depends on:

- [ ] Versioned artifacts
- [ ] Known deployment history
- [ ] Compatible configuration
- [ ] Database migration strategy
- [ ] Operational procedures

## 78. Database Complication

Application rollback can be difficult if the new version changed the database incompatibly.

Deployment and database migration coordination should receive deeper coverage later.

---

# Tier 29 — Health Verification

## 79. Deployment Is Not Finished at "Process Started"

After deployment, verify behavior.

```text
Deploy
  │
  ▼
Process starts
  │
  ▼
Health check
  │
  ▼
Functional verification
```

## 80. Health Signals

Examples:

- [ ] Application responds
- [ ] Required dependencies available
- [ ] Error rate acceptable
- [ ] Key endpoint works
- [ ] Startup completed

Spring Boot Actuator and Kubernetes probes belong in their respective trees.

---

# Tier 30 — Notifications

## 81. Feedback Channels

Pipeline events may notify:

```text
developer
team
operations
release manager
```

## 82. Useful Notifications

Examples:

- [ ] Pipeline failed
- [ ] Deployment succeeded
- [ ] Production approval required
- [ ] Security gate failed

Avoid flooding people with low-value notifications.

---

# Tier 31 — Pipeline Logs

## 83. Logs

Pipeline logs are essential diagnostic evidence.

They should show:

```text
stage
step
command
result
duration
```

## 84. Secret Redaction

Never rely on "nobody will read the logs."

Credentials must be masked/not emitted.

---

# Tier 32 — Pipeline Security

## 85. Pipelines Are Privileged Systems

A CI/CD system may have access to:

```text
source code
secrets
artifact repositories
container registries
cloud accounts
production systems
```

Compromise can be severe.

## 86. Security Principles

- [ ] Least privilege
- [ ] Strong authentication
- [ ] Restricted production access
- [ ] Secret management
- [ ] Dependency hygiene
- [ ] Controlled pipeline changes
- [ ] Auditability
- [ ] Patch CI infrastructure/tools

## 87. Untrusted Code

Be careful when running pipeline code from:

```text
external pull requests
forks
untrusted contributors
```

especially if secrets are available.

---

# Tier 33 — Software Supply Chain Awareness

## 88. Supply Chain

Your application contains more than code you wrote.

```text
Your Source
   │
   ├── dependencies
   ├── build plugins
   ├── base images
   ├── CI actions/plugins
   └── build infrastructure
```

## 89. Dependency Integrity

Understand:

- [ ] Pinning/version control
- [ ] Trusted sources
- [ ] Vulnerability scanning
- [ ] Checksums/signing awareness
- [ ] Dependency update strategy

## 90. SBOM Awareness

A Software Bill of Materials records components included in software.

Know why organizations may generate one.

Deep supply-chain security belongs in DevSecOps.

---

# Tier 34 — Pipeline as Code

## 91. Pipeline Definition in Version Control

Modern CI/CD commonly stores pipeline configuration with code.

Benefits:

- [ ] Reviewable
- [ ] Versioned
- [ ] Reproducible
- [ ] Branch-aware
- [ ] Auditable

## 92. Pipeline Changes Are Code Changes

A change that modifies:

```text
deployment
credentials
security checks
production behavior
```

deserves careful review.

---

# Tier 35 — Reusable Pipeline Logic

## 93. Duplication Problem

Bad:

```text
20 repositories
×
20 copied pipeline files
×
same bug fixed 20 times
```

## 94. Reuse

Platforms support different reuse mechanisms.

The transferable concept:

```text
common build/deploy behavior
        │
        ▼
reusable pipeline component
```

Do not abstract too early; first understand repeated patterns.

---

# Tier 36 — Monorepo vs Multiple Repositories

## 95. Monorepo Pipeline

One repository may contain:

```text
frontend
backend
shared libraries
infrastructure
```

Pipeline may determine what changed.

## 96. Multiple Repositories

Separate repositories may need:

```text
independent pipelines
version coordination
cross-repository triggers
```

## 97. Change Detection

Avoid rebuilding/deploying unrelated components unnecessarily when the architecture supports selective execution.

---

# Tier 37 — Database Changes in CI/CD

## 98. Database Migrations

Application delivery often includes schema changes.

```text
Application Release
        │
        ├── code
        └── database migration
```

## 99. Migration Automation

CI/CD should eventually coordinate tools such as:

```text
Flyway
Liquibase
```

without relying on ORM auto-update as the production release mechanism.

## 100. Compatibility

Think about:

```text
old app + new schema
new app + old schema
rollback compatibility
```

Detailed migration design belongs in a separate tree.

---

# Tier 38 — Environment Promotion

## 101. Promotion

A release can progress through environments:

```text
Artifact 1.4.2
     │
     ▼
Testing
     │
     ▼
Staging
     │
     ▼
Production
```

Prefer promoting the same tested artifact rather than rebuilding it differently at every stage.

## 102. Evidence

Promotion decisions may use:

- [ ] Automated tests
- [ ] Security results
- [ ] Manual verification
- [ ] Approval
- [ ] Operational metrics

---

# Tier 39 — Deployment Strategy Awareness

## 103. Basic Strategies

Know the names and purposes of:

```text
recreate
rolling
blue/green
canary
```

## 104. Scope Boundary

CI/CD decides **when/how to orchestrate** deployment steps.

Docker, Kubernetes, cloud platforms, and a future Deployment Fundamentals tree should teach the mechanics deeply.

---

# Tier 40 — Feature Flags Awareness

## 105. Deployment vs Release

A useful distinction:

```text
Deployment
code is running

Release
users receive the feature
```

Feature flags can separate the two.

## 106. Why It Matters

A team may deploy code safely while enabling a feature later.

Detailed feature-flag systems are outside this fundamentals tree.

---

# Tier 41 — Pipeline Performance

## 107. Measure Pipeline Duration

Break down:

```text
checkout
dependency install
compile
unit test
integration test
package
image build
publish
deploy
```

## 108. Improve Bottlenecks

Possible techniques:

- [ ] Caching
- [ ] Parallelization
- [ ] Test selection
- [ ] Faster build tooling
- [ ] Better worker sizing
- [ ] Avoid unnecessary work

Do not sacrifice correctness merely to make a dashboard number smaller.

---

# Tier 42 — Pipeline Reliability

## 109. Flaky Pipelines

A pipeline that fails randomly teaches developers to ignore failures.

Common causes:

```text
flaky tests
shared mutable test environment
timing assumptions
network dependency
resource exhaustion
poor cleanup
```

## 110. Trust

The pipeline must be trustworthy.

```text
RED
should mean
"investigate"

not
"just rerun it until green"
```

---

# Tier 43 — Observability of Delivery

## 111. Pipeline Metrics

Useful metrics can include:

- [ ] Pipeline success rate
- [ ] Duration
- [ ] Queue time
- [ ] Failure stage
- [ ] Deployment frequency
- [ ] Change lead time
- [ ] Recovery time awareness
- [ ] Change failure rate awareness

## 112. DORA Metrics Awareness

Know the common delivery-performance concepts:

```text
Deployment Frequency
Lead Time for Changes
Change Failure Rate
Time to Restore Service
```

Use metrics to understand systems, not punish individual developers.

---

# Tier 44 — CI/CD Troubleshooting

## 113. Debug by Stage

```text
Trigger?
   │
Checkout?
   │
Dependencies?
   │
Build?
   │
Tests?
   │
Package?
   │
Publish?
   │
Deploy?
   │
Verify?
```

Find the first incorrect stage.

## 114. "Works Locally, Fails in CI"

Check:

- [ ] Undeclared dependencies
- [ ] Environment variables
- [ ] File paths
- [ ] OS differences
- [ ] Case sensitivity
- [ ] Tool versions
- [ ] Database/service dependencies
- [ ] Network assumptions
- [ ] Secrets
- [ ] Working directory

## 115. "CI Passes, Deployment Fails"

Check:

- [ ] Artifact identity
- [ ] Environment configuration
- [ ] Credentials
- [ ] Network
- [ ] Database migration
- [ ] Runtime dependencies
- [ ] Health checks
- [ ] Resource limits

---

# Tier 45 — Professional Pipeline Design

## 116. Optimize for Feedback

Place fast/high-value checks early.

```text
cheap + fast
    │
    ▼
expensive + slow
```

Example:

```text
compile
  │
unit tests
  │
lint
  │
integration tests
  │
package
  │
deploy
  │
E2E / verification
```

Exact ordering depends on the project.

## 117. Keep Pipelines Understandable

A developer should be able to answer:

```text
What triggers this?
What does each stage prove?
What artifact is produced?
Where is it published?
What can reach production?
What happens when it fails?
```

## 118. Separate Concerns

Prefer:

```text
Maven → Java build logic
npm → frontend build logic
Dockerfile → image construction
migration tool → schema changes
CI/CD pipeline → orchestration
```

rather than embedding every concern directly into one giant pipeline file.

---

# Tier 46 — Full-Stack CI/CD Architecture

## 119. Example Application

```text
Angular / React
      │
      ▼
Spring Boot
      │
      ▼
PostgreSQL
```

Possible pipeline:

```text
Git Push
   │
   ▼
Trigger
   │
   ▼
Checkout
   │
   ├──────────────────┐
   ▼                  ▼
Backend             Frontend
   │                  │
Maven                npm
   │                  │
Compile              Build
   │                  │
JUnit/Mockito         Tests
   │                  │
   └─────────┬────────┘
             ▼
       Integration Tests
             │
             ▼
          Package
             │
             ▼
        Docker Build
             │
             ▼
        Security Checks
             │
             ▼
      Publish Artifacts
             │
             ▼
       Deploy Staging
             │
             ▼
          Verify
             │
             ▼
     Production Decision
             │
             ▼
      Deploy Production
             │
             ▼
          Observe
```

---

# Practical Competency Checkpoints

A learner completing this tree should be able to:

- [ ] Explain why CI/CD exists
- [ ] Explain Continuous Integration
- [ ] Explain Continuous Delivery
- [ ] Explain Continuous Deployment
- [ ] Distinguish delivery from deployment
- [ ] Explain pipeline, stage, job, and step
- [ ] Identify common pipeline triggers
- [ ] Explain webhooks
- [ ] Trace an artifact back to a source commit
- [ ] Integrate Maven/npm build commands into a conceptual pipeline
- [ ] Place unit, integration, and E2E tests appropriately
- [ ] Explain quality gates
- [ ] Explain artifact packaging
- [ ] Explain artifact repositories
- [ ] Treat a Docker image as a versioned artifact
- [ ] Explain container registries
- [ ] Explain development/test/staging/production environments
- [ ] Separate code, configuration, and secrets
- [ ] Explain pull-request validation
- [ ] Explain release/version identity
- [ ] Explain deployment at a high level
- [ ] Explain manual approval gates
- [ ] Design conditional pipeline stages
- [ ] Identify work that can run in parallel
- [ ] Distinguish cache from artifact
- [ ] Explain reproducible builds
- [ ] Explain idempotency
- [ ] Design fail-fast behavior
- [ ] Distinguish transient from deterministic failures
- [ ] Explain rollback requirements
- [ ] Verify deployments with health checks
- [ ] Design useful pipeline notifications
- [ ] Protect secrets in logs
- [ ] Explain why CI/CD systems are high-value security targets
- [ ] Explain software supply-chain risk
- [ ] Explain pipeline-as-code
- [ ] Recognize reusable pipeline opportunities
- [ ] Reason about monorepo vs multi-repo pipelines
- [ ] Explain database migration coordination
- [ ] Promote the same artifact across environments
- [ ] Recognize rolling/blue-green/canary concepts
- [ ] Explain deployment vs feature release
- [ ] Improve pipeline performance without weakening correctness
- [ ] Recognize and fix flaky pipelines
- [ ] Explain basic delivery metrics
- [ ] Troubleshoot pipeline failures by stage
- [ ] Design a complete tool-independent CI/CD pipeline

---

# Suggested Practice Progression

```text
1. Draw a manual release process
        │
        ▼
2. Identify repeatable steps
        │
        ▼
3. Define CI checks
        │
        ▼
4. Define pipeline stages
        │
        ▼
5. Add build automation
        │
        ▼
6. Add unit tests
        │
        ▼
7. Add integration tests
        │
        ▼
8. Produce a versioned artifact
        │
        ▼
9. Define artifact storage
        │
        ▼
10. Define dev/test/stage/prod
        │
        ▼
11. Separate configuration + secrets
        │
        ▼
12. Define PR and main-branch triggers
        │
        ▼
13. Add quality gates
        │
        ▼
14. Add deployment stage
        │
        ▼
15. Add verification
        │
        ▼
16. Define rollback
        │
        ▼
17. Add security checks
        │
        ▼
18. Add caching/parallelism
        │
        ▼
19. Define pipeline metrics
        │
        ▼
20. Implement the design in Jenkins
    or GitHub Actions
```

---

# Capstone — Tool-Independent Full-Stack Delivery Pipeline

Design CI/CD for:

```text
Frontend: Angular or React
Backend:  Spring Boot
Database: PostgreSQL
Build:    npm + Maven
Tests:    frontend tests + JUnit + Mockito
Runtime:  Docker
```

## Requirement 1 — Pull Request Pipeline

```text
Pull Request
    │
    ▼
Checkout
    │
    ├── Backend Build/Test
    └── Frontend Build/Test
    │
    ▼
Quality Checks
    │
    ▼
PASS / FAIL
```

The PR must not deploy production.

## Requirement 2 — Main Branch Pipeline

```text
Merge to Main
     │
     ▼
Build
     │
     ▼
Test
     │
     ▼
Package
     │
     ▼
Build Versioned Images
     │
     ▼
Publish
     │
     ▼
Deploy Test/Staging
     │
     ▼
Verify
```

## Requirement 3 — Production Release

Choose and justify:

```text
Continuous Delivery
        │
        ▼
manual production approval
```

or:

```text
Continuous Deployment
        │
        ▼
automatic production release
```

## Requirement 4 — Failure Policy

Define behavior for:

```text
compile failure
unit-test failure
integration-test failure
security failure
registry outage
deployment failure
health-check failure
```

## Requirement 5 — Security

Specify:

- [ ] Where secrets live conceptually
- [ ] Which stages can access them
- [ ] Who can deploy production
- [ ] How pipeline changes are reviewed
- [ ] How artifacts are identified
- [ ] How dependency/image security is checked

## Requirement 6 — Traceability

Given a production version, be able to identify:

```text
production version
      │
      ▼
artifact/image
      │
      ▼
pipeline run
      │
      ▼
Git commit
      │
      ▼
pull request / change
```

## Requirement 7 — Recovery

Define:

```text
How do we detect a bad deployment?
How do we stop further promotion?
How do we restore the previous version?
What happens if the database changed?
```

## Final Challenge

Design the complete pipeline **without using Jenkins/GitHub Actions terminology**.

Only after the design is complete should you translate it into:

```text
Jenkins
GitHub Actions
GitLab CI/CD
```

---

# Interview Readiness

Be able to answer:

- [ ] What is CI/CD?
- [ ] What problem does CI/CD solve?
- [ ] What is Continuous Integration?
- [ ] Continuous Delivery vs Continuous Deployment?
- [ ] What is a pipeline?
- [ ] What is a stage?
- [ ] What is a job?
- [ ] What is a pipeline trigger?
- [ ] What is a webhook?
- [ ] What normally happens after a Git push or pull request?
- [ ] Why should builds be automated?
- [ ] Where do unit tests fit?
- [ ] Where do integration tests fit?
- [ ] What is a quality gate?
- [ ] What is an artifact?
- [ ] What is an artifact repository?
- [ ] How can a Docker image be an artifact?
- [ ] What is a container registry?
- [ ] Why version artifacts?
- [ ] Why promote the same artifact across environments?
- [ ] What is environment parity?
- [ ] Configuration vs secret?
- [ ] How should secrets be handled in CI/CD?
- [ ] What is branch protection?
- [ ] Why validate pull requests?
- [ ] What is a release?
- [ ] What is a deployment?
- [ ] Why use manual approvals?
- [ ] What are conditional pipeline stages?
- [ ] Why run jobs in parallel?
- [ ] Cache vs artifact?
- [ ] What is a reproducible build?
- [ ] What does idempotent deployment mean?
- [ ] What does fail fast mean?
- [ ] When should a pipeline retry?
- [ ] What is rollback?
- [ ] Why can database migrations complicate rollback?
- [ ] How do you verify a deployment?
- [ ] Why are CI/CD systems security-sensitive?
- [ ] What is pipeline as code?
- [ ] What is software supply-chain security?
- [ ] What is an SBOM?
- [ ] How do monorepos affect pipelines?
- [ ] What does environment promotion mean?
- [ ] What are rolling, blue/green, and canary deployments?
- [ ] Deployment vs release?
- [ ] What makes a pipeline flaky?
- [ ] What are common CI/CD performance metrics?
- [ ] What are the DORA delivery metrics?
- [ ] How do you debug "works locally but fails in CI"?
- [ ] Jenkins vs CI/CD: what is the difference?

---

# Concept Translation Table

After mastering the fundamentals, map concepts to tools:

```text
Concept             Jenkins         GitHub Actions       GitLab CI/CD
-------             -------         --------------       -------------
Pipeline            Pipeline        Workflow             Pipeline
Definition          Jenkinsfile     workflow YAML        .gitlab-ci.yml
Execution worker    Agent           Runner               Runner
Stage               Stage           Job/steps structure  Stage
Job                 Job/stage       Job                  Job
Step                Step            Step                 script command
Trigger             Webhook/etc.    on:                  rules/workflow
Secret              Credential      Secret               CI/CD variable
Artifact             Artifact       Artifact             Artifact
Manual gate          Input/etc.      Environment/review   Manual job/etc.
```

Exact platform behavior and syntax belong in the platform-specific trees.

---

# Future Skill Tree Branches

```text
                         CI/CD Fundamentals
                                │
             ┌──────────────────┼──────────────────┐
             ▼                  ▼                  ▼
          Jenkins        GitHub Actions       GitLab CI/CD
             │                  │                  │
             └──────────────────┼──────────────────┘
                                │
                    ┌───────────┴───────────┐
                    ▼                       ▼
             Deployment Fundamentals    DevSecOps
                    │
         ┌──────────┼───────────┐
         ▼          ▼           ▼
       Docker      AWS      Kubernetes
                                │
                                ▼
                              GitOps
                         ┌──────┴──────┐
                         ▼             ▼
                       Argo CD        Flux
```

Additional related branches can include:

```text
Artifact / Package Management
Database Migrations
Infrastructure as Code
Cloud Deployment
Release Engineering
Software Supply-Chain Security
Observability
```

---

# Mastery Progression

```text
"I can manually build and deploy"
              │
              ▼
"I understand why automation helps"
              │
              ▼
"I understand Continuous Integration"
              │
              ▼
"I understand Delivery vs Deployment"
              │
              ▼
"I can design pipeline stages"
              │
              ▼
"I can automate build + test"
              │
              ▼
"I understand artifacts and environments"
              │
              ▼
"I can design promotion and deployment"
              │
              ▼
"I can design failure + rollback behavior"
              │
              ▼
"I understand pipeline security"
              │
              ▼
"I can optimize and troubleshoot pipelines"
              │
              ▼
"I can design CI/CD without choosing a tool"
              │
              ▼
"I can translate that design into Jenkins,
 GitHub Actions, or another platform"
```

---

# Mastery Standard

> **Can I take a software project and independently design a tool-agnostic delivery process that integrates source control, triggers, builds, automated tests, quality gates, versioned artifacts, environment configuration, secrets, publishing, deployment, verification, failure handling, rollback, security, and traceability—and then explain how a platform such as Jenkins or GitHub Actions would implement that design without confusing the platform with CI/CD itself?**

The final mental model should be:

```text
                        Software Change
                              │
                              ▼
                             Git
                              │
                              ▼
                            Trigger
                              │
                              ▼
                         CI/CD Pipeline
                              │
          ┌───────────────────┼───────────────────┐
          ▼                   ▼                   ▼
        Build                Test             Quality
          │                   │                   │
          └───────────────────┼───────────────────┘
                              ▼
                           Package
                              │
                              ▼
                           Artifact
                              │
                              ▼
                           Publish
                              │
                              ▼
                          Deployment
                              │
                 ┌────────────┼────────────┐
                 ▼            ▼            ▼
                Dev         Staging       Prod
                              │
                              ▼
                          Verification
                              │
                              ▼
                         Observability
                              │
                              ▼
                           Feedback

Implementation choices:

CI/CD Fundamentals
        │
        ├── Jenkins
        ├── GitHub Actions
        └── GitLab CI/CD
```
