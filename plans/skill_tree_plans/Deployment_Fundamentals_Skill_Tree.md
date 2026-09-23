# Deployment Fundamentals Skill Tree

> **Goal:** Build a tool-independent understanding of how a tested software release is safely moved into a runtime environment, verified, operated, upgraded, and recovered.
>
> **Target level:** Full-stack developer who can design and explain a practical deployment process before learning the mechanics of a specific platform such as AWS, Kubernetes, Docker Compose, or a PaaS.
>
> **Primary prerequisite:** **CI/CD Fundamentals**
>
> **Supporting prerequisites:** Git/version control, basic networking and HTTP/HTTPS, application configuration, basic Linux/command-line use, and basic database knowledge.
>
> **Important distinction:** CI/CD answers how software changes are continuously built, verified, packaged, and moved toward release. Deployment focuses on how a particular release is introduced into a runtime environment safely and predictably.
>
> **Scope boundary:** This tree teaches transferable deployment concepts. Docker, Kubernetes, AWS, infrastructure as code, database migrations, observability, security, service discovery, gateways, and GitOps should receive deeper coverage in their own skill trees.

---

# Skill Tree Overview

```text
                    CI/CD Fundamentals
                            │
                            ▼
                  Deployment Fundamentals
                            │
          ┌─────────────────┼─────────────────┐
          ▼                 ▼                 ▼
      Release           Environment        Runtime
      Artifact          Configuration      Infrastructure
          │                 │                 │
          └─────────────────┼─────────────────┘
                            ▼
                       Deployment
                            │
          ┌─────────────────┼──────────────────┐
          ▼                 ▼                  ▼
       Strategy          Verification        Traffic
          │                 │                  │
          ▼                 ▼                  ▼
      Recreate         Health Checks      Load Balancing
      Rolling          Readiness          Routing
      Blue/Green       Smoke Tests        Cutover
      Canary           Metrics
          │                 │
          └─────────────────┼──────────────────┘
                            ▼
                     Production Safety
                            │
             ┌──────────────┼──────────────┐
             ▼              ▼              ▼
          Rollback       Database       Observability
                         Changes
             │              │              │
             └──────────────┼──────────────┘
                            ▼
                     Reliable Operations
                            │
          ┌─────────────────┼─────────────────┐
          ▼                 ▼                 ▼
        Docker             AWS            Kubernetes
```

---

# Dependency Map

```text
Git
 │
 ▼
CI/CD Fundamentals
 │
 ├── Build
 ├── Test
 ├── Package
 └── Publish
       │
       ▼
   Versioned Artifact
       │
       ▼
Deployment Fundamentals
       │
       ├── Environment
       ├── Configuration
       ├── Secrets
       ├── Infrastructure
       ├── Deployment Strategy
       ├── Health Verification
       ├── Traffic Management
       ├── Database Coordination
       ├── Rollback / Roll Forward
       └── Operational Feedback
              │
              ▼
       Platform Implementations
       ├── Docker
       ├── AWS
       └── Kubernetes
```

---

# Tier 0 — What Deployment Is

## 1. Deployment Definition

Deployment is the process of placing a particular software release into an environment where it can run.

```text
Versioned Release
       │
       ▼
Runtime Environment
       │
       ▼
Running Application
```

A deployment may involve:

- [ ] Application binaries
- [ ] Container images
- [ ] Configuration
- [ ] Secrets
- [ ] Infrastructure
- [ ] Database changes
- [ ] Network routing
- [ ] Startup
- [ ] Verification

## 2. Deployment vs Build

```text
Build
source code → artifact

Deployment
artifact → running software
```

## 3. Deployment vs Release

These are related but not identical.

```text
Deployment
"Is the code running?"

Release
"Is the functionality available to users?"
```

Feature flags can separate deployment from release.

## 4. Deployment vs CI/CD

```text
CI/CD
automates the path from change
toward verified/releasable software

Deployment
introduces that software into
a runtime environment
```

**Checkpoint:** Explain build, package, release, and deployment as separate concepts.

---

# Tier 1 — The Deployable Unit

## 5. Artifact

A deployment should begin with an identifiable artifact.

Examples:

```text
JAR
WAR
ZIP
compiled frontend bundle
Docker image
```

## 6. Artifact Identity

Every deployable version should be traceable.

Useful identifiers:

- [ ] Semantic/application version
- [ ] Git commit SHA
- [ ] Build number
- [ ] Container image digest/tag
- [ ] Release identifier

```text
Production
    │
    ▼
app:1.4.2
    │
    ▼
Pipeline Run
    │
    ▼
Git Commit
```

## 7. Immutable Artifact Principle

Prefer promoting the same built artifact:

```text
Build once
   │
   ├── Test
   ├── Staging
   └── Production
```

rather than rebuilding differently for every environment.

## 8. Artifact Integrity

Understand why checksums, signatures, and provenance can help establish:

```text
"This is the artifact we intended to deploy."
```

Deep software supply-chain security belongs in a later tree.

---

# Tier 2 — Environments

## 9. Environment Concept

An environment is a runtime context in which an application operates.

Common examples:

```text
Local
Development
Testing
Staging
Production
```

Names and counts vary by organization.

## 10. Environment Purpose

Different environments support different kinds of verification.

```text
Development
    │
    ▼
Integration/Test
    │
    ▼
Staging
    │
    ▼
Production
```

## 11. Environment Parity

Reduce unnecessary differences between environments.

Potential differences that matter:

- [ ] Operating system/runtime
- [ ] Database type/version
- [ ] Network topology
- [ ] Configuration
- [ ] Dependencies
- [ ] Resource limits
- [ ] Security policies

## 12. Production Is Special

Production has real:

```text
users
data
traffic
availability requirements
security consequences
business impact
```

A production deployment deserves stronger controls than a local deployment.

---

# Tier 3 — Runtime Requirements

## 13. Know What the Application Needs

Before deployment, identify runtime dependencies.

Example Spring Boot application:

```text
Application
├── Java runtime
├── PostgreSQL
├── environment variables
├── network access
├── filesystem needs
└── external APIs
```

## 14. Dependency Availability

Deployment must answer:

```text
Where is the database?
Can the app reach it?
Where do secrets come from?
What port does the app listen on?
What external services are required?
```

## 15. Runtime Contract

Document assumptions such as:

- [ ] Required ports
- [ ] Required environment variables
- [ ] Runtime version
- [ ] CPU/memory expectations
- [ ] Persistent storage needs
- [ ] Network dependencies
- [ ] Startup command
- [ ] Health endpoint

---

# Tier 4 — Configuration

## 16. External Configuration

Prefer:

```text
Application Artifact
        +
Environment Configuration
```

over hardcoding environment-specific values into the artifact.

## 17. Typical Configuration

Examples:

```text
database URL
API endpoint
logging level
feature setting
allowed origin
service URL
runtime profile
```

## 18. Configuration by Environment

```text
Same application
      │
      ├── Development config
      ├── Staging config
      └── Production config
```

## 19. Configuration Validation

Fail clearly when required configuration is missing or invalid.

A partially configured application should not silently enter service.

---

# Tier 5 — Secrets

## 20. Secret vs Configuration

Configuration:

```text
SERVER_PORT=8080
```

Secret:

```text
DATABASE_PASSWORD=...
```

## 21. Secret Examples

- [ ] Database passwords
- [ ] API tokens
- [ ] Private keys
- [ ] Cloud credentials
- [ ] Signing keys
- [ ] Registry credentials

## 22. Secret Handling Principles

- [ ] Never commit secrets to Git
- [ ] Avoid baking secrets into images/artifacts
- [ ] Do not print them in logs
- [ ] Limit access
- [ ] Rotate them
- [ ] Use platform secret-management mechanisms

## 23. Deployment Access

Only the deployment components that require a secret should receive it.

```text
least privilege
+
shortest useful exposure
```

---

# Tier 6 — Infrastructure

## 24. Deployment Needs Somewhere to Run

Possible targets:

```text
physical server
virtual machine
container host
cloud service
Kubernetes cluster
platform-as-a-service
```

## 25. Infrastructure Responsibilities

At a high level:

- [ ] Compute
- [ ] Network
- [ ] Storage
- [ ] DNS
- [ ] Load balancing
- [ ] Security boundaries
- [ ] Runtime platform

## 26. Application vs Infrastructure

```text
Application Deployment
what software version runs

Infrastructure Provisioning
what resources exist for it to run on
```

They interact, but they are not identical.

Infrastructure as Code deserves its own tree.

---

# Tier 7 — Manual vs Automated Deployment

## 27. Manual Deployment

Example:

```text
SSH to server
stop app
copy JAR
change config
start app
```

Useful for learning, but error-prone at scale.

## 28. Automated Deployment

```text
Known artifact
      │
      ▼
Repeatable process
      │
      ▼
Known target
      │
      ▼
Verification
```

## 29. Automation Goal

Automation should make deployments:

```text
repeatable
auditable
predictable
recoverable
```

not merely faster.

---

# Tier 8 — Deployment Preconditions

## 30. Before Deploying

Verify:

- [ ] Correct artifact
- [ ] Required tests passed
- [ ] Target environment identified
- [ ] Configuration available
- [ ] Secrets available
- [ ] Infrastructure healthy
- [ ] Database migration plan known
- [ ] Rollback/recovery path known

## 31. Deployment Gate

Conceptually:

```text
Artifact Ready?
Tests Passed?
Environment Ready?
Change Authorized?
        │
        ▼
      Deploy
```

---

# Tier 9 — Recreate Deployment

## 32. Recreate Strategy

Simplest model:

```text
Old Version
    │
    ▼
STOP
    │
    ▼
New Version
    │
    ▼
START
```

## 33. Advantages

- [ ] Simple
- [ ] Easy to understand
- [ ] Low infrastructure complexity

## 34. Disadvantages

- [ ] Downtime
- [ ] Risk if new version fails
- [ ] Slow recovery if deployment is slow

Appropriate for some development/internal/small systems.

---

# Tier 10 — Rolling Deployment

## 35. Rolling Strategy

Replace instances gradually.

```text
V1 V1 V1 V1
     │
     ▼
V2 V1 V1 V1
     │
     ▼
V2 V2 V1 V1
     │
     ▼
V2 V2 V2 V2
```

## 36. Benefits

- [ ] Reduced/no planned downtime
- [ ] Gradual replacement
- [ ] Uses existing capacity

## 37. Compatibility Requirement

During rollout:

```text
V1 and V2 may run simultaneously
```

Therefore consider:

- [ ] API compatibility
- [ ] Database compatibility
- [ ] Session/state compatibility
- [ ] Message/event compatibility

## 38. Failure

If V2 is unhealthy, rollout should stop rather than replacing all healthy V1 instances.

---

# Tier 11 — Blue/Green Deployment

## 39. Blue/Green Concept

Maintain two environments:

```text
BLUE
V1
receiving traffic

GREEN
V2
prepared separately
```

After verification:

```text
Traffic
   │
   └────────► GREEN V2

BLUE V1 remains available temporarily
```

## 40. Advantages

- [ ] Fast traffic cutover
- [ ] Fast application rollback
- [ ] New version can be verified before receiving normal traffic

## 41. Costs

- [ ] Additional infrastructure
- [ ] Database compatibility complexity
- [ ] State synchronization concerns
- [ ] More routing complexity

---

# Tier 12 — Canary Deployment

## 42. Canary Concept

Send a small amount of production traffic to the new version.

```text
Users
  │
  ├── 95% → V1
  └──  5% → V2
```

If healthy:

```text
5%
 ↓
20%
 ↓
50%
 ↓
100%
```

## 43. Canary Purpose

Reduce blast radius while gathering real production evidence.

## 44. Canary Signals

Monitor:

- [ ] Error rate
- [ ] Latency
- [ ] Resource usage
- [ ] Business metrics
- [ ] Logs
- [ ] User-impact indicators

## 45. Canary Requirement

A canary without meaningful monitoring is mostly just a slower rollout.

---

# Tier 13 — Choosing a Strategy

## 46. Decision Factors

Consider:

```text
availability requirements
infrastructure cost
rollback speed
application state
database changes
traffic control capability
operational maturity
risk tolerance
```

## 47. Strategy Comparison

```text
Recreate
simple, downtime possible

Rolling
gradual replacement

Blue/Green
parallel environments + traffic cutover

Canary
gradual traffic exposure
```

There is no universally best deployment strategy.

---

# Tier 14 — Stateless vs Stateful Applications

## 48. Stateless Application

Ideally, any application instance can handle the next request.

```text
Request
  │
  ├── Instance A
  ├── Instance B
  └── Instance C
```

This simplifies scaling and rolling deployment.

## 49. Stateful Application

State may be tied to:

```text
local memory
local disk
session
specific instance
```

This complicates replacement and scaling.

## 50. Externalize Appropriate State

Common architecture:

```text
Application Instances
        │
        ▼
Shared Database / Cache / Storage
```

Not all state can or should be removed, but understand the deployment consequences.

---

# Tier 15 — Persistent Data

## 51. Ephemeral Compute vs Persistent Data

Application instances may be replaceable.

Data usually is not.

```text
Replace App Instance
        │
        ▼
Data must survive
```

## 52. Persistent Storage

Examples:

- [ ] Database
- [ ] Object storage
- [ ] Persistent volume
- [ ] Shared filesystem

## 53. Backup Awareness

Deployment safety includes understanding whether important data can be recovered.

Detailed backup/disaster recovery belongs in database/cloud trees.

---

# Tier 16 — Database Migrations

## 54. Deployment + Schema Change

A release may require:

```text
Application V2
      +
Database Schema V2
```

## 55. Migration Tooling

Tools such as:

```text
Flyway
Liquibase
```

can version and apply schema changes.

Detailed usage belongs in a database-migration tree.

## 56. Migration Ordering

Possible sequence:

```text
migration
   │
   ▼
application
```

But real systems may require more careful compatibility planning.

## 57. Backward Compatibility

For zero/low-downtime deployment, design schema changes so old and new application versions can coexist when necessary.

Example pattern:

```text
1. Add new compatible column
2. Deploy code that can use it
3. Migrate/backfill data
4. Stop using old column
5. Remove old column later
```

This is often called an expand/contract style of migration.

---

# Tier 17 — Health Checks

## 58. Liveness Concept

Question:

```text
"Is the process alive?"
```

## 59. Readiness Concept

Question:

```text
"Is this instance ready to receive traffic?"
```

These are not necessarily the same.

## 60. Example

```text
Process started
      │
      ▼
Java running
      │
      ▼
Database connection established
      │
      ▼
Application initialized
      │
      ▼
READY
```

## 61. Platform Integration

Health checks can influence:

```text
traffic routing
restart decisions
deployment progress
```

Specific implementation belongs in Spring Boot/Kubernetes/cloud trees.

---

# Tier 18 — Startup and Shutdown

## 62. Startup

Understand the application startup lifecycle:

```text
process starts
configuration loads
dependencies initialize
application becomes ready
traffic begins
```

Do not route traffic too early.

## 63. Graceful Shutdown

When removing an instance:

```text
stop new traffic
      │
      ▼
finish in-flight work
      │
      ▼
close resources
      │
      ▼
terminate
```

## 64. Why It Matters

Abrupt termination can interrupt:

- [ ] HTTP requests
- [ ] Database transactions
- [ ] Background jobs
- [ ] Message processing

---

# Tier 19 — Smoke Tests

## 65. Smoke Test

A small set of post-deployment checks verifies basic operation.

Examples:

```text
application responds
login works
critical endpoint responds
database access works
```

## 66. Smoke vs Full Test Suite

Smoke tests answer:

```text
"Is the deployed system fundamentally usable?"
```

They do not replace comprehensive automated tests.

---

# Tier 20 — Traffic Management

## 67. Traffic Routing

Deployments often need to decide:

```text
Which application instances receive requests?
```

## 68. Load Balancer

Conceptually:

```text
Clients
   │
   ▼
Load Balancer
   │
   ├── Instance A
   ├── Instance B
   └── Instance C
```

## 69. Deployment Integration

A new instance should typically receive traffic only after it is ready.

```text
Start
 │
 ▼
Ready?
 │
 YES
 │
 ▼
Add to traffic
```

---

# Tier 21 — DNS Awareness

## 70. DNS

DNS maps names to destinations.

```text
api.example.com
       │
       ▼
network destination
```

## 71. Deployment Relevance

DNS may participate in:

- [ ] Environment endpoints
- [ ] Blue/green switching
- [ ] Failover
- [ ] Service access

DNS caching/TTL means changes may not be instantaneous.

Detailed DNS belongs in networking/cloud study.

---

# Tier 22 — HTTP/HTTPS & TLS

## 72. Deployment Networking

A deployed web application needs:

```text
address
port
protocol
routing
```

## 73. HTTPS

Production applications commonly require TLS.

Understand at a high level:

```text
Client
  │ HTTPS
  ▼
TLS termination point
  │
  ▼
Application
```

TLS may terminate at:

- [ ] Reverse proxy
- [ ] Load balancer
- [ ] Gateway
- [ ] Application

Deep HTTPS belongs in the HTTP/HTTPS/REST tree.

---

# Tier 23 — Reverse Proxies & Gateways

## 74. Reverse Proxy

A reverse proxy receives requests and forwards them to application services.

```text
Internet
   │
   ▼
Reverse Proxy
   │
   ▼
Application
```

## 75. Deployment Uses

- [ ] TLS termination
- [ ] Routing
- [ ] Load balancing
- [ ] Stable public endpoint
- [ ] Hiding internal topology

Spring Cloud Gateway and cloud load balancers belong in later trees.

---

# Tier 24 — Service Discovery Awareness

## 76. Dynamic Services

In distributed systems, service locations can change.

```text
Service A
needs
Service B
```

Service discovery helps locate healthy instances.

## 77. Deployment Relationship

As instances start/stop:

```text
deployment
   │
   ▼
available service instances change
```

Eureka and Kubernetes service discovery belong in dedicated trees.

---

# Tier 25 — Resource Requirements

## 78. CPU and Memory

Applications require finite resources.

Deployment should define reasonable:

```text
CPU
memory
disk
network
```

## 79. Too Few Resources

Symptoms:

- [ ] Slow startup
- [ ] Out-of-memory failure
- [ ] High latency
- [ ] Process termination
- [ ] Failed health checks

## 80. Too Many Resources

Over-allocation wastes capacity/cost.

Measure real behavior.

---

# Tier 26 — Scaling

## 81. Vertical Scaling

```text
same instance
+
more CPU/RAM
```

## 82. Horizontal Scaling

```text
1 instance
      ↓
3 instances
      ↓
10 instances
```

## 83. Deployment Interaction

A deployment process must handle multiple instances consistently.

## 84. Autoscaling Awareness

Some platforms can adjust instance count based on demand/metrics.

Detailed autoscaling belongs in cloud/Kubernetes trees.

---

# Tier 27 — Availability

## 85. Availability Goal

Ask:

```text
What happens to users while we deploy?
```

## 86. Single Instance

```text
one server
   │
   ▼
restart
   │
   ▼
possible downtime
```

## 87. Multiple Instances

```text
Instance A updating
Instance B serving
Instance C serving
```

enables safer rolling approaches.

## 88. Redundancy

High availability generally requires avoiding unnecessary single points of failure.

---

# Tier 28 — Failure Domains & Blast Radius

## 89. Blast Radius

Blast radius describes how much of the system/users a failure affects.

```text
100% immediate rollout
→ potentially large blast radius

5% canary
→ smaller initial blast radius
```

## 90. Isolation

Separate:

```text
environments
regions/zones awareness
services
credentials
```

where appropriate to limit impact.

---

# Tier 29 — Deployment Verification

## 91. Verification Layers

```text
Process Running?
       │
       ▼
Health Endpoint?
       │
       ▼
Smoke Tests?
       │
       ▼
Metrics Healthy?
       │
       ▼
Business Function Healthy?
```

## 92. Do Not Equate "Deployment Command Succeeded" With Success

A deployment is not truly successful merely because the deployment tool returned exit code 0.

The running system must behave acceptably.

---

# Tier 30 — Observability

## 93. Three Common Signals

Understand:

```text
Logs
Metrics
Traces
```

## 94. Deployment Monitoring

Compare before and after:

- [ ] Error rate
- [ ] Latency
- [ ] Throughput
- [ ] CPU/memory
- [ ] Application exceptions
- [ ] Database behavior
- [ ] Business metrics

## 95. Version Correlation

Logs/metrics should ideally identify application version.

```text
error spike
   │
   ▼
which deployment?
   │
   ▼
which version?
```

ELK/OpenTelemetry/observability deserve dedicated trees.

---

# Tier 31 — Deployment Events

## 96. Record Deployments

Know:

```text
what version
who/what deployed it
when
where
result
```

## 97. Operational Correlation

If errors begin at 14:03 and deployment occurred at 14:02, that is useful diagnostic context.

---

# Tier 32 — Rollback

## 98. Rollback

Restore the previously known-good application version.

```text
V2
 │
 │ unhealthy
 ▼
Rollback
 │
 ▼
V1
```

## 99. Requirements

Rollback works best when:

- [ ] Previous artifacts are retained
- [ ] Configuration is known
- [ ] Deployment is repeatable
- [ ] Database remains compatible
- [ ] Traffic can be redirected

## 100. Rollback Is Not Magic

A destructive database migration or irreversible external action can make simple application rollback impossible.

---

# Tier 33 — Roll Forward

## 101. Roll Forward

Sometimes the safest recovery is:

```text
V2 broken
   │
   ▼
fix defect
   │
   ▼
V2.1
```

instead of reverting.

## 102. Decision

Choose rollback vs roll forward based on:

```text
severity
recovery time
database compatibility
artifact availability
risk
```

---

# Tier 34 — Automatic Rollback Awareness

## 103. Automated Failure Response

A deployment platform may automatically stop or reverse rollout when health conditions fail.

Concept:

```text
Deploy V2
   │
   ▼
Health deteriorates
   │
   ▼
Stop rollout
   │
   ▼
Restore healthy capacity
```

## 104. Guardrail

Automatic rollback requires reliable health signals.

Bad metrics can automate the wrong decision.

---

# Tier 35 — Deployment Timeouts

## 105. Timeouts

Deployment operations should not wait forever.

Examples:

```text
startup timeout
health-check timeout
migration timeout
drain timeout
```

## 106. Slow vs Failed

Choose thresholds based on observed application behavior rather than arbitrary guesses.

---

# Tier 36 — Retries

## 107. Transient Failure

Retry may help with:

```text
temporary network issue
temporary registry failure
temporary cloud API failure
```

## 108. Deterministic Failure

Retry does not fix:

```text
bad artifact
invalid configuration
broken migration
application crash
```

## 109. Bounded Retry

Retries should have:

```text
limit
delay/backoff awareness
clear final failure
```

---

# Tier 37 — Deployment Idempotency

## 110. Repeated Execution

Where practical:

```text
deploy version X
deploy version X again
```

should converge on a known state rather than create duplicate/unpredictable resources.

## 111. Why It Matters

Idempotency improves:

- [ ] Retry safety
- [ ] Recovery
- [ ] Automation
- [ ] Predictability

---

# Tier 38 — Concurrency

## 112. Concurrent Deployments

What happens if two deployments target production simultaneously?

```text
Deploy V2
     +
Deploy V3
```

This can create race conditions.

## 113. Coordination

Common policy:

```text
one production deployment at a time
```

or explicit deployment ordering.

Platforms implement locking/concurrency controls differently.

---

# Tier 39 — Deployment Permissions

## 114. Authorization

Not everyone who can commit code necessarily needs unrestricted production deployment access.

## 115. Least Privilege

Separate permissions such as:

```text
read source
run tests
publish artifact
deploy staging
deploy production
modify infrastructure
read production secrets
```

## 116. Auditability

Production changes should be attributable to a user or automated identity.

---

# Tier 40 — Security Boundaries

## 117. Network Exposure

Ask:

```text
Does this service need to be public?
```

Databases commonly should not be exposed like public web endpoints.

## 118. Internal vs External Services

```text
Internet
   │
   ▼
Public Gateway / Load Balancer
   │
   ▼
Application
   │
   ▼
Private Database
```

## 119. Patch and Runtime Awareness

Deployment includes responsibility for:

- [ ] Runtime versions
- [ ] Base images
- [ ] OS/package vulnerabilities
- [ ] Application dependencies

Deep vulnerability management belongs in DevSecOps.

---

# Tier 41 — Deployment and Sessions

## 120. User Sessions

If session state exists only in application memory:

```text
User → Instance A
```

and A disappears during deployment, the user may lose state.

## 121. Strategies

Awareness of:

- [ ] Stateless tokens
- [ ] Shared session storage
- [ ] Sticky sessions
- [ ] Graceful draining

Architecture determines the correct choice.

---

# Tier 42 — Background Work

## 122. Jobs and Workers

Applications may perform:

```text
scheduled jobs
queue processing
batch work
async tasks
```

## 123. Deployment Risk

Stopping an instance may interrupt work.

Design for:

- [ ] Graceful completion
- [ ] Retry
- [ ] Idempotent processing
- [ ] Work ownership/leases awareness

---

# Tier 43 — Distributed-System Compatibility

## 124. Multiple Versions

Rolling/canary deployments can create:

```text
Service A V1
Service A V2
Service B V3
```

simultaneously.

## 125. Compatibility

Consider:

- [ ] REST API compatibility
- [ ] Event/message schema compatibility
- [ ] Database schema compatibility
- [ ] Shared cache format
- [ ] Serialization format

This becomes increasingly important in microservices.

---

# Tier 44 — Feature Flags

## 126. Feature Flag Concept

```text
Code deployed
     │
     ▼
Feature OFF
```

Later:

```text
Feature ON
```

## 127. Deployment vs Release

Feature flags allow:

```text
deployment != user release
```

## 128. Uses

- [ ] Gradual rollout
- [ ] Internal testing
- [ ] Emergency disable
- [ ] Experimentation awareness

Flags require lifecycle management; stale flags become technical debt.

---

# Tier 45 — Deployment Documentation

## 129. Runbook

A deployment/recovery runbook can document:

```text
how to deploy
how to verify
how to rollback
who to contact
known failure modes
```

## 130. Automation Does Not Eliminate Documentation

People still need to understand what automation does and how to respond when it fails.

---

# Tier 46 — Deployment Metrics

## 131. Useful Measurements

Track concepts such as:

- [ ] Deployment duration
- [ ] Deployment frequency
- [ ] Success/failure rate
- [ ] Rollback rate
- [ ] Time to recover
- [ ] Change failure rate
- [ ] Time from artifact ready to production

## 132. Metrics as Feedback

Use measurements to improve systems, not to reward/punish individual developers.

---

# Tier 47 — Troubleshooting

## 133. Debug by Layer

```text
Correct artifact?
      │
      ▼
Correct configuration?
      │
      ▼
Secrets available?
      │
      ▼
Infrastructure available?
      │
      ▼
Network reachable?
      │
      ▼
Application started?
      │
      ▼
Health check passed?
      │
      ▼
Traffic routed?
      │
      ▼
Database compatible?
      │
      ▼
Application behavior healthy?
```

## 134. App Won't Start

Check:

- [ ] Runtime version
- [ ] Startup command
- [ ] Environment variables
- [ ] Secrets
- [ ] Database connection
- [ ] Port conflict
- [ ] Filesystem permissions
- [ ] Memory
- [ ] Logs

## 135. App Starts but Is Unreachable

Check:

- [ ] Listening address
- [ ] Port
- [ ] Firewall/security rules
- [ ] Load balancer
- [ ] Reverse proxy
- [ ] DNS
- [ ] TLS
- [ ] Routing
- [ ] Readiness

## 136. New Version Is Slow

Check:

- [ ] Resource usage
- [ ] Database queries
- [ ] Connection pools
- [ ] Dependency latency
- [ ] Error retries
- [ ] Traffic distribution
- [ ] Application metrics
- [ ] Version comparison

## 137. Works in Staging, Fails in Production

Compare:

```text
configuration
data volume
traffic
database
network
permissions
secrets
resource limits
external services
runtime versions
```

---

# Tier 48 — Production Deployment Design

## 138. Complete Deployment Flow

```text
Versioned Artifact
       │
       ▼
Pre-Deployment Checks
       │
       ▼
Database Compatibility
       │
       ▼
Deploy New Version
       │
       ▼
Start Instance(s)
       │
       ▼
Readiness Check
       │
       ▼
Introduce Traffic
       │
       ▼
Smoke Test
       │
       ▼
Observe Metrics/Logs
       │
       ├── Healthy → Continue/Complete
       │
       └── Unhealthy
               │
               ▼
        Stop / Rollback /
          Roll Forward
```

## 139. Questions Before Production

Be able to answer:

```text
What exactly are we deploying?
Where is it going?
What configuration will it use?
What secrets does it need?
How will it start?
How do we know it is ready?
How does traffic reach it?
Can old and new versions coexist?
Does the database need to change?
How do we know the deployment worked?
What happens if it fails?
How do we recover?
Who is allowed to deploy?
How do we identify this version later?
```

---

# Tier 49 — Full-Stack Deployment Architecture

## 140. Example

```text
Users
  │
  ▼
DNS
  │
  ▼
HTTPS / Load Balancer
  │
  ▼
Frontend
  │
  ▼
Backend API
  │
  ▼
PostgreSQL
```

Deployment responsibilities may include:

```text
Frontend
├── build artifact/image
├── runtime config
└── routing

Backend
├── JAR/image
├── environment config
├── secrets
├── health checks
└── database connectivity

Database
├── persistent storage
├── migrations
├── backups
└── compatibility
```

---

# Tier 50 — Platform Translation

## 141. Fundamentals First

Once the learner understands deployment conceptually:

```text
Deployment Fundamentals
        │
        ├── Docker
        ├── AWS
        ├── Kubernetes
        └── Other platforms
```

## 142. Concept Translation

```text
Fundamental Concept       Example Platform Implementations
-------------------       --------------------------------
Artifact                  JAR / Docker image
Compute                   VM / container / pod
Configuration             env vars / config files / ConfigMap
Secret                    secret manager / platform secret
Health check              endpoint / probe / target health
Traffic routing           proxy / LB / ingress / gateway
Persistent storage        disk / volume / database
Scaling                   instances / replicas
Rolling deployment        platform rollout mechanism
Rollback                  previous image/release/version
```

The learner should recognize the concept even when the platform vocabulary changes.

---

# Practical Competency Checkpoints

A learner completing this tree should be able to:

- [ ] Explain deployment independently from CI/CD
- [ ] Explain deployment vs release
- [ ] Identify a deployable artifact
- [ ] Trace production software to a Git commit
- [ ] Explain why artifacts should be immutable/versioned
- [ ] Explain environment parity
- [ ] Identify runtime requirements
- [ ] Separate configuration from code
- [ ] Separate secrets from ordinary configuration
- [ ] Explain application vs infrastructure deployment
- [ ] Compare manual and automated deployment
- [ ] Define deployment preconditions
- [ ] Explain recreate deployment
- [ ] Explain rolling deployment
- [ ] Explain blue/green deployment
- [ ] Explain canary deployment
- [ ] Choose a strategy based on tradeoffs
- [ ] Explain stateless vs stateful deployment concerns
- [ ] Protect persistent data during replacement
- [ ] Explain database migration coordination
- [ ] Explain backward-compatible schema changes
- [ ] Distinguish liveness and readiness
- [ ] Explain graceful startup/shutdown
- [ ] Design smoke tests
- [ ] Explain load balancing
- [ ] Explain DNS/TLS/reverse-proxy roles at a high level
- [ ] Explain service discovery's relationship to deployment
- [ ] Define CPU/memory requirements
- [ ] Explain vertical vs horizontal scaling
- [ ] Reason about availability during deployment
- [ ] Explain blast radius
- [ ] Verify a deployment beyond "command succeeded"
- [ ] Use logs/metrics/traces conceptually after deployment
- [ ] Record deployment events
- [ ] Design rollback
- [ ] Explain roll forward
- [ ] Explain automatic rollback guardrails
- [ ] Use timeouts and bounded retries
- [ ] Explain deployment idempotency
- [ ] Prevent conflicting concurrent deployments
- [ ] Apply least privilege to deployment access
- [ ] Reason about network exposure
- [ ] Handle session/background-work concerns
- [ ] Reason about mixed-version compatibility
- [ ] Explain feature flags
- [ ] Create a deployment runbook
- [ ] Identify useful deployment metrics
- [ ] Troubleshoot failures by layer
- [ ] Translate concepts into Docker/AWS/Kubernetes later

---

# Suggested Practice Progression

```text
1. Manually run an application
        │
        ▼
2. List its runtime requirements
        │
        ▼
3. Create a versioned artifact
        │
        ▼
4. Deploy it to a local/test environment
        │
        ▼
5. Externalize configuration
        │
        ▼
6. Externalize secrets
        │
        ▼
7. Add health/readiness verification
        │
        ▼
8. Add smoke tests
        │
        ▼
9. Automate deployment
        │
        ▼
10. Deploy multiple instances
        │
        ▼
11. Practice rolling replacement
        │
        ▼
12. Model blue/green
        │
        ▼
13. Model canary traffic
        │
        ▼
14. Introduce database migration
        │
        ▼
15. Design backward-compatible change
        │
        ▼
16. Practice failed deployment
        │
        ▼
17. Roll back
        │
        ▼
18. Practice roll forward
        │
        ▼
19. Add deployment observability
        │
        ▼
20. Translate design to AWS/Kubernetes
```

---

# Capstone — Deploy a Full-Stack Guestbook Application

Use a system such as:

```text
Frontend: Angular or React
Backend: Spring Boot
Database: PostgreSQL
Packaging: Docker
```

The purpose is not to master a cloud platform yet. The purpose is to design the deployment correctly.

## Stage 1 — Runtime Contract

Document:

```text
Frontend
├── port
├── API location
└── runtime/build configuration

Backend
├── Java/runtime
├── port
├── database URL
├── database credentials
├── application secrets
└── health endpoint

PostgreSQL
├── persistent storage
├── database/user
└── network accessibility
```

## Stage 2 — Versioned Release

Produce:

```text
frontend:<version>
backend:<version>
```

and record the Git commit.

## Stage 3 — Configuration

Separate:

```text
code
configuration
secrets
```

No production credentials inside Git or images.

## Stage 4 — Initial Deployment

Deploy:

```text
PostgreSQL
    │
    ▼
Backend
    │
    ▼
Frontend
```

Verify connectivity through the entire stack.

## Stage 5 — Health Verification

Define:

```text
backend liveness
backend readiness
database availability
frontend availability
critical API smoke test
```

## Stage 6 — Rolling Update

Run multiple backend instances conceptually or practically.

Upgrade:

```text
V1 V1
  │
  ▼
V2 V1
  │
  ▼
V2 V2
```

Ensure traffic reaches only ready instances.

## Stage 7 — Blue/Green Design

Design:

```text
BLUE = current production
GREEN = candidate
```

Define how traffic would switch and how rollback would work.

## Stage 8 — Canary Design

Define:

```text
5% → V2
95% → V1
```

Choose the metrics that determine whether V2 proceeds.

## Stage 9 — Database Change

Add a database field.

Plan the change so old and new backend versions can coexist during rollout.

## Stage 10 — Failure Drill

Intentionally model:

```text
bad DB password
bad image/version
failed health check
broken migration
application crash
```

For each, identify:

```text
detection
pipeline/deployment behavior
diagnosis
recovery
```

## Stage 11 — Rollback

Restore the previous application version and verify it.

## Stage 12 — Production Design

Draw:

```text
                    Internet
                       │
                       ▼
                 DNS / HTTPS
                       │
                       ▼
                  Load Balancer
                       │
             ┌─────────┴─────────┐
             ▼                   ▼
        Backend V2          Backend V2
             │                   │
             └─────────┬─────────┘
                       ▼
                  PostgreSQL
                       │
                       ▼
                Persistent Data
```

Then identify where Docker, AWS, Kubernetes, CI/CD, observability, and database migrations would plug into the architecture.

---

# Interview Readiness

Be able to answer:

- [ ] What is deployment?
- [ ] Deployment vs release?
- [ ] Deployment vs CI/CD?
- [ ] What is a deployable artifact?
- [ ] Why build once and promote the same artifact?
- [ ] Why version artifacts?
- [ ] What is environment parity?
- [ ] Configuration vs secrets?
- [ ] Why shouldn't secrets be baked into a Docker image?
- [ ] Application deployment vs infrastructure provisioning?
- [ ] Manual vs automated deployment?
- [ ] What is a recreate deployment?
- [ ] What is a rolling deployment?
- [ ] What is blue/green deployment?
- [ ] What is canary deployment?
- [ ] When would you choose each?
- [ ] What is blast radius?
- [ ] Stateless vs stateful application?
- [ ] Why is persistent storage different from application compute?
- [ ] Why do database migrations complicate deployments?
- [ ] What is an expand/contract migration?
- [ ] Liveness vs readiness?
- [ ] What is graceful shutdown?
- [ ] What is a smoke test?
- [ ] What does a load balancer do?
- [ ] What role does DNS play?
- [ ] Where can TLS terminate?
- [ ] What is a reverse proxy?
- [ ] What is service discovery?
- [ ] Vertical vs horizontal scaling?
- [ ] How can you deploy without downtime?
- [ ] How do you know a deployment succeeded?
- [ ] What should you monitor immediately after deployment?
- [ ] What is rollback?
- [ ] What is roll forward?
- [ ] Why might rollback fail after a database change?
- [ ] When should a deployment retry?
- [ ] What does idempotent deployment mean?
- [ ] How do you prevent simultaneous production deployments?
- [ ] How should production deployment permissions be controlled?
- [ ] How can deployment affect user sessions?
- [ ] How can deployment interrupt background jobs?
- [ ] Why must old/new versions sometimes remain compatible?
- [ ] What is a feature flag?
- [ ] Deployment vs feature release?
- [ ] What belongs in a deployment runbook?
- [ ] How would you debug an app that starts but cannot receive traffic?
- [ ] How would you debug "works in staging, fails in production"?

---

# Deployment Strategy Cheat Sheet

```text
Requirement / Constraint               Starting Strategy
------------------------               -----------------
Downtime acceptable                    Recreate

Multiple identical instances           Rolling

Need fast traffic cutover              Blue/Green

Need fast app-version rollback         Blue/Green

Want limited real-user exposure        Canary

Need gradual replacement               Rolling

Need progressive evidence              Canary

Very simple internal application       Recreate may suffice

High availability required             Rolling / Blue-Green / Canary
```

Actual choice depends on architecture and platform.

---

# Common Anti-Patterns

## Anti-Pattern 1

```text
"The deployment command returned success,
so the deployment succeeded."
```

Verify the running system.

## Anti-Pattern 2

Rebuilding a different artifact for production than the one tested in staging.

## Anti-Pattern 3

Hardcoding production configuration into application source.

## Anti-Pattern 4

Baking credentials into a Docker image.

## Anti-Pattern 5

Sending traffic before the application is ready.

## Anti-Pattern 6

Changing every instance simultaneously without considering availability.

## Anti-Pattern 7

Calling a rollout "canary" without monitoring the canary.

## Anti-Pattern 8

Assuming application rollback automatically reverses database changes.

## Anti-Pattern 9

Using `latest` as the only way to identify production images.

## Anti-Pattern 10

Restarting failed deployments repeatedly without identifying whether the failure is transient.

## Anti-Pattern 11

Giving the CI/CD system unrestricted administrator credentials when narrower permissions are sufficient.

## Anti-Pattern 12

Keeping critical application state only on replaceable local instances without understanding the consequences.

## Anti-Pattern 13

Treating staging as proof of production behavior despite major environment differences.

## Anti-Pattern 14

Deploying without knowing which Git commit/version is running.

## Anti-Pattern 15

Having no documented recovery path because "the deployment is automated."

---

# Future Skill Tree Branches

```text
                     Deployment Fundamentals
                              │
        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                     ▼
      Docker                 AWS                Kubernetes
        │                     │                     │
        │             ┌───────┼────────┐            │
        │             ▼       ▼        ▼            │
        │            EC2      S3       EBS           │
        │                                           │
        └─────────────────────┬─────────────────────┘
                              ▼
                    Production Deployment
                              │
             ┌────────────────┼────────────────┐
             ▼                ▼                ▼
      Infrastructure      Observability      Security
        as Code
             │
             ▼
          Terraform
                              │
                              ▼
                            GitOps
                      ┌───────┴───────┐
                      ▼               ▼
                    Argo CD          Flux
```

Related future trees:

```text
Database Migrations
Infrastructure as Code
Load Balancing & Reverse Proxies
DNS & Networking
TLS / Certificate Management
Observability / OpenTelemetry
DevSecOps
Disaster Recovery
Release Engineering
Feature Flags
```

---

# Relationship to Existing / Planned Skill Trees

```text
Git
 │
 ▼
CI/CD Fundamentals
 │
 ▼
Deployment Fundamentals
 │
 ├───────────────┬─────────────────┐
 ▼               ▼                 ▼
Docker           AWS           Kubernetes
 │               │                 │
 └───────────────┼─────────────────┘
                 ▼
        Production Architecture
```

CI/CD platform branches remain separate:

```text
CI/CD Fundamentals
 │
 ├── Jenkins
 ├── GitHub Actions
 └── GitLab CI/CD
```

Those tools can automate the deployment process described by this tree.

---

# Mastery Progression

```text
"I can run an application"
        │
        ▼
"I can identify its runtime requirements"
        │
        ▼
"I can create a versioned deployable artifact"
        │
        ▼
"I can configure environments safely"
        │
        ▼
"I can automate deployment"
        │
        ▼
"I understand health and readiness"
        │
        ▼
"I can deploy without unnecessary downtime"
        │
        ▼
"I understand rolling / blue-green / canary"
        │
        ▼
"I can coordinate database changes"
        │
        ▼
"I can verify production behavior"
        │
        ▼
"I can rollback or roll forward"
        │
        ▼
"I can reason about availability,
 traffic, state, security, and scaling"
        │
        ▼
"I can design deployment independently
 of a specific platform"
        │
        ▼
"I can implement that design using
 Docker, AWS, Kubernetes, or another platform"
```

---

# Mastery Standard

> **Can I take a tested, versioned software artifact and independently design how it should be introduced into a runtime environment—including configuration, secrets, infrastructure requirements, health/readiness, traffic routing, deployment strategy, database compatibility, persistent state, verification, observability, rollback/roll-forward, permissions, failure handling, and recovery—without depending on the vocabulary of one deployment platform?**

The final mental model should be:

```text
                       Tested Software
                              │
                              ▼
                      Versioned Artifact
                              │
                              ▼
                     Deployment Decision
                              │
              ┌───────────────┼───────────────┐
              ▼               ▼               ▼
         Configuration      Secrets      Infrastructure
              │               │               │
              └───────────────┼───────────────┘
                              ▼
                         Deploy Version
                              │
                              ▼
                            Start
                              │
                              ▼
                         Readiness
                              │
                              ▼
                       Introduce Traffic
                              │
                              ▼
                           Verify
                              │
                 ┌────────────┴────────────┐
                 ▼                         ▼
              Healthy                   Unhealthy
                 │                         │
                 ▼                         ▼
             Continue              Stop / Rollback /
              Rollout                Roll Forward
                 │
                 ▼
             Production
                 │
                 ▼
             Observe
                 │
                 ▼
             Feedback

Platform implementations:

Deployment Fundamentals
        │
        ├── Docker
        ├── AWS
        └── Kubernetes
```
