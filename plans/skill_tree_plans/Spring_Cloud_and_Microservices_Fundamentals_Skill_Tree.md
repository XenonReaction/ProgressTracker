# Spring Cloud & Microservices Fundamentals --- Full-Stack Developer Skill Tree

> **Goal:** Learn microservices architecture and the Spring Cloud
> ecosystem at the developer level: understand why teams split systems
> into services, how independently deployed services communicate, what
> new distributed-system problems appear, and which Spring/Spring Cloud
> tools address those problems.
>
> **Target level:** A full-stack Java/Spring developer who can design,
> build, run, and troubleshoot a small microservices system and is
> prepared to continue into **Eureka Service Discovery** and **Spring
> Cloud Gateway**.
>
> **Primary prerequisites:** **Spring Boot & Initializr** and
> **HTTP/HTTPS & REST API Fundamentals**
>
> **Strong supporting prerequisites:** Java, Maven, Spring dependency
> injection, Spring MVC/REST controllers, DTOs, configuration
> properties/environment variables, PostgreSQL/database fundamentals,
> Git, and basic testing.
>
> **Useful later supporting knowledge:** Docker, JMeter, CI/CD,
> ELK/observability, Kubernetes, and AWS.
>
> **Scope boundary:** This tree teaches microservices and the Spring
> Cloud foundation. It does **not** attempt to master every Spring Cloud
> project. **Eureka Service Discovery** and **Spring Cloud Gateway** are
> intentionally separate downstream trees. Spring Cloud Config,
> Resilience4j/fault tolerance, messaging/event-driven architecture,
> distributed tracing, advanced security, and cloud-native production
> operations are introduced only enough to establish where they fit and
> can become later trees.
>
> **Capstone:** Split a familiar Spring Boot application into a small
> set of independently running REST services, give each service a clear
> responsibility and configuration, implement synchronous
> service-to-service communication, demonstrate partial failure and
> timeout behavior, containerize the services, and document how Eureka
> and Spring Cloud Gateway would improve the architecture in the next
> trees.

------------------------------------------------------------------------

# Skill Tree Overview

``` text
Java
 │
 ▼
Spring Boot & Initializr
 │
 ▼
HTTP / HTTPS / REST API
 │
 ▼
Spring Cloud & Microservices Fundamentals
 │
 ├── Microservice Boundaries
 ├── Independent Applications
 ├── Service-to-Service HTTP
 ├── Configuration
 ├── Distributed Failure
 ├── Data Ownership
 ├── Observability
 ├── Scaling
 └── Deployment Concerns
 │
 ▼
Eureka Service Discovery
 │
 ▼
Spring Cloud Gateway
 │
 ▼
Discovery-Based Routing
```

The key learning progression is:

``` text
Working Monolith
      │
      ▼
Understand WHY services are separated
      │
      ▼
Build multiple independent Spring Boot services
      │
      ▼
Communicate over HTTP
      │
      ▼
Experience distributed-system problems
      │
      ▼
Understand why Spring Cloud tools exist
      │
      ├── Eureka
      ├── Gateway
      ├── Config
      ├── Resilience
      └── Observability
```

------------------------------------------------------------------------

# Tier 0 --- What Is a Microservice?

## 1. Monolith

A monolithic application packages multiple business capabilities into
one deployable application.

``` text
Application
├── Users
├── Messages
├── Orders
└── Payments
```

This is not inherently bad.

## 2. Microservices

A microservices architecture separates selected capabilities into
independently running/deployable services.

``` text
System
├── User Service
├── Message Service
├── Order Service
└── Payment Service
```

## 3. Service

A service should represent a meaningful application/business capability
rather than merely being "a small Spring Boot project."

## 4. Important Principle

``` text
Microservices
≠
"split every class into another server"
```

The goal is useful service boundaries and independent evolution.

------------------------------------------------------------------------

# Tier 1 --- Why Microservices Exist

## 5. Independent Deployment

One service can potentially change and deploy without redeploying the
entire system.

## 6. Independent Scaling

Different capabilities may need different scaling behavior.

``` text
Message Service   → 2 instances
Search Service    → 8 instances
Admin Service     → 1 instance
```

## 7. Team Ownership

Teams can own services and their interfaces.

## 8. Technology/Release Isolation

Services can evolve somewhat independently when their contracts remain
compatible.

## 9. Tradeoff

These benefits introduce substantial complexity.

Never teach:

``` text
microservices = automatically better
```

------------------------------------------------------------------------

# Tier 2 --- Monolith vs Microservices Tradeoffs

## 10. Monolith Advantages

Often simpler:

``` text
development
debugging
deployment
transactions
local execution
testing
```

## 11. Microservice Advantages

Can improve:

``` text
independent deployment
independent scaling
team ownership
fault isolation
system modularity
```

## 12. Microservice Costs

Introduce:

``` text
network communication
distributed failures
deployment coordination
service discovery
observability
data consistency
security boundaries
versioned contracts
```

## 13. Decision Skill

A developer should be able to explain **why** a system benefits from
microservices before splitting it.

------------------------------------------------------------------------

# Tier 3 --- Distributed Systems Mental Model

Once one application becomes multiple processes:

``` text
Service A
   │
 network
   │
   ▼
Service B
```

new questions appear:

``` text
Where is Service B?
Is it running?
How long should A wait?
What if B returns an error?
What if there are 5 copies of B?
How do we trace one request across both?
```

These questions motivate Spring Cloud.

------------------------------------------------------------------------

# Tier 4 --- What Spring Cloud Is

## 14. Spring Boot vs Spring Cloud

Simplified mental model:

``` text
Spring Boot
"Build and run a Spring application."

Spring Cloud
"Help Spring applications solve common
distributed-system/cloud patterns."
```

## 15. Spring Cloud Is an Ecosystem

It contains projects/integrations for concerns such as:

``` text
service discovery
gateway/routing
configuration
resilience integrations
distributed-system support
```

## 16. Do Not Treat It as One Library

Different Spring Cloud components solve different problems.

------------------------------------------------------------------------

# Tier 5 --- Version Compatibility Awareness

## 17. Spring Ecosystem Versions Matter

Spring Boot and Spring Cloud releases have compatibility relationships.

## 18. Developer Habit

Before adding Spring Cloud dependencies:

``` text
check current Spring Boot version
check compatible Spring Cloud release
use supported dependency management
```

## 19. Maven Dependency Management

Understand why a Spring Cloud BOM/dependency-management mechanism may be
used rather than manually guessing every component version.

## 20. Scope

Learn compatibility discipline, not release-history memorization.

------------------------------------------------------------------------

# Tier 6 --- Service Boundaries

## 21. Business Capability

Prefer boundaries around cohesive responsibilities.

Example:

``` text
Guestbook System
│
├── Message Service
└── User/Profile Service
```

## 22. High Cohesion

A service should contain closely related behavior.

## 23. Loose Coupling

Avoid requiring one service to know another service's internal
implementation.

## 24. Contract

Services communicate through explicit interfaces/contracts.

For this tree, primarily:

``` text
HTTP + REST + JSON
```

------------------------------------------------------------------------

# Tier 7 --- Bad Service Boundaries

## 25. Distributed Monolith

A system can be split into services while remaining tightly coupled.

``` text
Service A cannot run without B
B cannot run without C
every deployment changes all three
```

This is often called a **distributed monolith**.

## 26. Excessive Chattiness

Avoid designs where one user request creates dozens of unnecessary
synchronous service calls.

## 27. Shared Internals

Do not expose database tables or internal classes as the service
contract.

------------------------------------------------------------------------

# Tier 8 --- Independent Spring Boot Services

## 28. Separate Applications

Each service can be its own Spring Boot application.

``` text
message-service/
    pom.xml
    src/

profile-service/
    pom.xml
    src/
```

## 29. Independent Runtime

Each service has:

``` text
process
port
configuration
logs
lifecycle
```

## 30. Example

``` text
Profile Service → localhost:8081
Message Service → localhost:8082
```

Hardcoded locations are acceptable temporarily for learning.

They create the problem Eureka will later solve.

------------------------------------------------------------------------

# Tier 9 --- Service APIs

## 31. Service Interface

A service exposes capabilities through endpoints.

Example:

``` text
Profile Service
GET /api/profiles/{id}

Message Service
GET /api/messages
POST /api/messages
```

## 32. DTO Boundary

Use request/response DTOs rather than exposing persistence entities
directly.

## 33. API Contract

Other services depend on the API contract, not implementation details.

------------------------------------------------------------------------

# Tier 10 --- Synchronous Communication

## 34. Request/Response

One service may call another and wait for a response.

``` text
Message Service
      │
 HTTP GET
      │
      ▼
Profile Service
      │
 response
      ▼
Message Service
```

## 35. Synchronous Dependency

During that request:

``` text
Message Service
depends on
Profile Service responding
```

This creates latency and failure coupling.

## 36. Appropriate Use

Synchronous calls are useful when an immediate response is genuinely
required.

------------------------------------------------------------------------

# Tier 11 --- Spring HTTP Client Awareness

## 37. Calling Another API

A Spring application needs an HTTP client to communicate with another
service.

## 38. Modern Client Awareness

Understand the role of current Spring HTTP client approaches rather than
tying architecture to one historical API.

Concept:

``` text
Service Layer
    │
    ▼
HTTP Client
    │
    ▼
Remote REST Service
```

## 39. Client Responsibility

The client must handle:

``` text
URL
method
headers
body
response DTO
errors
timeouts
```

## 40. Scope

Learn one modern Spring approach well enough to implement
service-to-service calls; recognize alternatives without mastering all
of them.

------------------------------------------------------------------------

# Tier 12 --- Remote DTOs and Contracts

## 41. Remote Response DTO

Service A should model the response it expects from Service B.

## 42. Avoid Sharing Entity Classes

Do not make two services depend on the same JPA entity class as their
network contract.

## 43. Contract Evolution

Changing a response can break consumers.

Microservices therefore increase the importance of API compatibility.

------------------------------------------------------------------------

# Tier 13 --- Network Failure

Local Java method call:

``` text
service.method()
```

usually either executes or throws locally.

Remote call:

``` text
Service A
   │
 network
   ▼
Service B
```

can fail because of:

``` text
DNS
connection
timeout
remote crash
deployment
network interruption
overload
```

## 44. Fundamental Rule

> A remote call can fail even when your own service is functioning
> correctly.

------------------------------------------------------------------------

# Tier 14 --- Timeouts

## 45. Waiting Forever Is Dangerous

If Service B never responds:

``` text
Service A
   │
   ▼
wait
wait
wait...
```

resources can accumulate.

## 46. Timeout

Define how long the caller is willing to wait.

## 47. Failure Is a Design Case

Timeout handling should be designed, not treated as an impossible event.

------------------------------------------------------------------------

# Tier 15 --- Retry Awareness

## 48. Retry

Some transient failures may justify trying again.

``` text
request
  │
fails
  │
retry
```

## 49. Retry Danger

Blind retries can make overload worse.

``` text
overloaded service
      │
      ▼
requests fail
      │
      ▼
everyone retries
      │
      ▼
even more load
```

## 50. Scope

Understand retry concepts here.

Detailed resilience policy belongs in a later **Resilience4j / Fault
Tolerance** tree.

------------------------------------------------------------------------

# Tier 16 --- Circuit Breaker Awareness

## 51. Problem

Repeatedly calling an unhealthy dependency wastes resources and
increases cascading failure risk.

## 52. Circuit Breaker Concept

``` text
Calls succeed
    │
    ▼
CLOSED

Failures rise
    │
    ▼
OPEN
stop normal calls temporarily

Later
    │
    ▼
test recovery
```

## 53. Future Tree

Detailed circuit-breaker implementation should be handled with
Resilience4j/fault-tolerance training.

------------------------------------------------------------------------

# Tier 17 --- Cascading Failure

## 54. Dependency Chain

``` text
Client
  │
  ▼
Gateway
  │
  ▼
Service A
  │
  ▼
Service B
  │
  ▼
Database
```

One slow dependency can affect upstream services.

## 55. Distributed Reliability

Microservice reliability must consider dependency chains, not only
whether each individual application can start.

------------------------------------------------------------------------

# Tier 18 --- Service Discovery Problem

## 56. Hardcoded Location

Early learning setup:

``` text
Message Service
      │
      ▼
http://localhost:8081
      │
      ▼
Profile Service
```

## 57. Scaling Breaks Simplicity

What if Profile Service has:

``` text
instance 1
instance 2
instance 3
```

Which address should the caller use?

## 58. Dynamic Environments

Service locations can change during:

``` text
deployment
restart
scaling
failure recovery
```

## 59. Next Tree

This problem leads directly to:

``` text
Eureka Service Discovery
```

------------------------------------------------------------------------

# Tier 19 --- Service Registry Concept

## 60. Registry

A service registry maintains information about available service
instances.

``` text
             Service Registry
             ▲             ▲
             │             │
      Service A        Service B
       registers        registers
```

## 61. Discovery

A consumer can locate a logical service through discovery instead of a
permanently hardcoded host.

## 62. Scope

Understand the pattern here.

Implement it in the **Eureka Service Discovery** tree.

------------------------------------------------------------------------

# Tier 20 --- API Gateway Problem

## 63. Direct Client Calls

Without a gateway:

``` text
Frontend
 ├──► Profile Service
 ├──► Message Service
 └──► Order Service
```

The frontend must know many backend locations/interfaces.

## 64. Gateway Pattern

``` text
Frontend
    │
    ▼
API Gateway
    │
 ┌──┼────┐
 ▼  ▼    ▼
A   B    C
```

## 65. Gateway Responsibilities

Potential concerns include:

``` text
routing
path handling
cross-cutting filters
central entry point
```

## 66. Next Tree

Implement this using:

``` text
Spring Cloud Gateway
```

after the fundamentals and preferably after Eureka basics.

------------------------------------------------------------------------

# Tier 21 --- Configuration in Multiple Services

## 67. Configuration Multiplies

One application might have one configuration set.

Ten services may have ten sets.

``` text
Service A → config
Service B → config
Service C → config
...
```

## 68. Environment Variables

Continue using environment-specific configuration rather than hardcoding
deployment values.

## 69. Examples

``` text
database URL
service URL
port
feature setting
credentials
```

## 70. Future Problem

Managing configuration consistently across many services leads toward
**Spring Cloud Config** or platform-level configuration systems.

------------------------------------------------------------------------

# Tier 22 --- Spring Cloud Config Awareness

## 71. Centralized Configuration Pattern

Concept:

``` text
             Config Source
             ▲     ▲     ▲
             │     │     │
         Service A B     C
```

## 72. Why It Exists

Centralized configuration can reduce duplicated/manual configuration
management.

## 73. Scope

Understand the problem and pattern.

Create a separate **Spring Cloud Config** skill tree later if needed.

------------------------------------------------------------------------

# Tier 23 --- Secrets

## 74. Configuration vs Secret

Examples:

``` text
service URL       → configuration
feature flag      → configuration
database password → secret
API key           → secret
```

## 75. Do Not Commit Secrets

Never solve distributed configuration by placing production credentials
into Git.

## 76. Future Security

Secret management should eventually connect to:

``` text
Docker secrets/config
Kubernetes Secrets + external secret systems
AWS secret services
application security
```

------------------------------------------------------------------------

# Tier 24 --- Data Ownership

## 77. Database-per-Service Principle

A common microservices principle is that a service owns its data.

Concept:

``` text
Service A → Database/Data A
Service B → Database/Data B
```

## 78. Ownership Is More Important Than Physical Server Count

For learning, multiple logical databases may still run on one PostgreSQL
server.

The important concept is:

``` text
Service B should not casually bypass Service A
and manipulate A's private tables.
```

## 79. API Ownership

Other services interact through the owning service's contract.

------------------------------------------------------------------------

# Tier 25 --- Shared Database Anti-Pattern

## 80. Tight Coupling

If every service freely reads/writes every table:

``` text
Service A ─┐
Service B ─┼──► Shared Tables
Service C ─┘
```

services become tightly coupled to shared schema internals.

## 81. Consequence

A database schema change can break multiple supposedly independent
services.

## 82. Nuance

Real systems sometimes share databases for practical reasons.

The learner should understand the coupling tradeoff rather than memorize
an absolute rule.

------------------------------------------------------------------------

# Tier 26 --- Distributed Transactions Problem

## 83. Monolithic Transaction

One application/database can often use a single local transaction.

## 84. Multi-Service Workflow

``` text
Order Service
     │
     ▼
Payment Service
     │
     ▼
Inventory Service
```

A single local database transaction cannot trivially wrap independent
services/databases.

## 85. Scope

Understand why distributed data consistency is harder.

Do not implement advanced distributed transaction/saga systems in this
fundamentals tree.

------------------------------------------------------------------------

# Tier 27 --- Eventual Consistency Awareness

## 86. Immediate Consistency May Not Always Be Possible

Distributed systems may temporarily contain different views of state.

## 87. Eventual Consistency

Some architectures accept temporary inconsistency with processes that
converge later.

## 88. Future Branch

This becomes especially important in:

``` text
messaging
events
Kafka/RabbitMQ
saga patterns
```

which deserve later training.

------------------------------------------------------------------------

# Tier 28 --- Synchronous vs Asynchronous Communication

## 89. Synchronous

``` text
Service A
   │ request
   ▼
Service B
   │ response
   ▼
Service A continues
```

## 90. Asynchronous

Conceptually:

``` text
Service A
   │
   ▼
Message/Event
   │
   ▼
Broker
   │
   ▼
Service B processes later
```

## 91. Scope

This tree focuses primarily on synchronous REST communication.

Event-driven microservices should become a separate branch.

------------------------------------------------------------------------

# Tier 29 --- Idempotency Awareness

## 92. Repeated Requests

Distributed systems can cause retries or duplicate delivery attempts.

## 93. Idempotency

An operation is idempotent when repeating the same operation has the
intended safe/equivalent effect.

## 94. REST Connection

Review HTTP method semantics and think carefully about write operations
under retries.

## 95. Scope

Understand the concern; advanced distributed idempotency patterns come
later.

------------------------------------------------------------------------

# Tier 30 --- Logging Across Services

## 96. Monolith Logging

``` text
one application
one main log stream
```

## 97. Microservice Logging

``` text
Service A logs
Service B logs
Service C logs
```

A single user request may touch several logs.

## 98. Structured Context

Logs become more useful when they contain consistent contextual
information.

## 99. ELK Connection

A dedicated ELK / Elastic Stack skill tree, covered later in this
curriculum, becomes more valuable as service count increases --- for
now, be aware that centralized logging is the natural solution to the
problem just described.

------------------------------------------------------------------------

# Tier 31 --- Correlation IDs

## 100. Request Tracking Problem

``` text
Client
  │
  ▼
Service A
  │
  ▼
Service B
```

How do you connect log entries from the same request?

## 101. Correlation Identifier

Concept:

``` text
request-id: abc123
```

propagated across calls.

## 102. Benefit

Search:

``` text
abc123
```

and inspect activity across services.

## 103. Scope

Implement a basic educational correlation mechanism or understand how
frameworks/observability tooling can provide this later.

------------------------------------------------------------------------

# Tier 32 --- Distributed Tracing Awareness

## 104. Trace

A distributed trace represents a request as it moves through multiple
components.

``` text
Client
  │
  ▼
Gateway
  │
  ▼
Service A
  │
  ▼
Service B
```

## 105. Spans

Different operations can be represented as spans within a trace.

## 106. Scope

Understand tracing terminology and purpose.

Dedicated observability/tracing tooling belongs later.

------------------------------------------------------------------------

# Tier 33 --- Metrics

## 107. Logs vs Metrics

Logs describe events/details.

Metrics describe measured values over time.

Examples:

``` text
request count
error rate
response time
CPU
memory
```

## 108. Per-Service Observation

Each service needs to be observable independently.

## 109. System Observation

Also observe the system as a whole.

------------------------------------------------------------------------

# Tier 34 --- Health Checks

## 110. Health

A service should expose meaningful health information where appropriate.

## 111. Spring Boot Actuator

Connect existing Spring Boot Actuator knowledge to distributed
applications.

## 112. Health Is More Than Process Running

A process can exist while being unable to serve useful traffic.

This connects later to Kubernetes readiness/liveness concepts.

------------------------------------------------------------------------

# Tier 35 --- Service Instances and Scaling

## 113. One Logical Service, Multiple Instances

``` text
Message Service
├── Instance A
├── Instance B
└── Instance C
```

## 114. Logical Name vs Physical Instance

Consumers should ideally think:

``` text
"Message Service"
```

rather than permanently binding to:

``` text
10.0.0.17:8082
```

## 115. Leads to Discovery

This reinforces the need for Eureka/service discovery.

------------------------------------------------------------------------

# Tier 36 --- Load Balancing Awareness

## 116. Multiple Instances

When multiple service instances exist, requests need to be distributed.

``` text
Consumer
   │
   ▼
Load-balancing decision
 ┌─┼─┐
 ▼ ▼ ▼
 A B C
```

## 117. Where Load Balancing Can Occur

Conceptually:

``` text
client-side
gateway/proxy
platform/infrastructure
```

## 118. Scope

Understand the problem.

Detailed Spring discovery/load-balancing integration belongs with
Eureka/Gateway.

------------------------------------------------------------------------

# Tier 37 --- API Versioning and Compatibility

## 119. Independent Deployment Requires Compatibility

If Service B changes today but Service A deploys tomorrow, their
contracts must coexist safely.

## 120. Breaking Change

Examples:

``` text
remove expected field
change field meaning
change endpoint
change required input
```

## 121. Prefer Evolution

Design APIs so consumers have migration time.

## 122. Testing

Contract/integration testing becomes increasingly important.

------------------------------------------------------------------------

# Tier 38 --- Integration Testing

## 123. Unit Test Limitation

A unit test can prove local behavior while remote integration is broken.

## 124. Integration Test

Test communication between actual components where appropriate.

## 125. Examples

``` text
Service A → Service B
Service A → database
HTTP serialization/deserialization
configuration
```

## 126. JUnit/Mockito Connection

Use unit tests for isolated logic and integration tests for boundaries.

------------------------------------------------------------------------

# Tier 39 --- Contract Testing Awareness

## 127. Consumer/Provider Risk

Provider changes can break consumers.

## 128. Contract Testing

Contract testing helps verify assumptions between communicating
services.

## 129. Scope

Understand why it exists.

A dedicated Spring Cloud Contract or contract-testing tree can be added
later if useful.

------------------------------------------------------------------------

# Tier 40 --- Local Development Complexity

## 130. Multiple Processes

Instead of:

``` text
start one backend
```

you may need:

``` text
start profile-service
start message-service
start database(s)
start discovery later
start gateway later
```

## 131. Ports

Keep local port assignments understandable and documented.

## 132. Startup Dependencies

Avoid assuming startup order alone guarantees readiness.

------------------------------------------------------------------------

# Tier 41 --- Docker Connection

## 133. Container per Service

Microservices map naturally to containers.

``` text
Profile Service
      │
      ▼
Docker Image

Message Service
      │
      ▼
Docker Image
```

## 134. Compose

Docker Compose can run the educational multi-service system locally.

``` text
compose
├── profile-service
├── message-service
└── postgres
```

## 135. Docker Is Not Microservices

Containers are a deployment mechanism.

A badly designed distributed system remains badly designed inside
Docker.

------------------------------------------------------------------------

# Tier 42 --- Environment-Based Service Locations

## 136. Before Eureka

Service URLs can initially be externalized:

``` text
PROFILE_SERVICE_URL=http://localhost:8081
```

rather than hardcoded in Java.

## 137. Docker Environment

Later:

``` text
http://profile-service:8080
```

may be available through container networking.

## 138. Why Still Learn Discovery?

Different environments solve discovery differently.

Learning the abstract service-discovery problem makes Eureka and
Kubernetes service discovery easier to understand.

------------------------------------------------------------------------

# Tier 43 --- Kubernetes Connection

## 139. Kubernetes Has Platform-Level Discovery

Kubernetes Services/DNS can provide stable service names.

``` text
Service A
   │
   ▼
service-b
   │
   ▼
Pods
```

## 140. Eureka Is Still Educationally Useful

Eureka demonstrates application-level service registry/discovery.

## 141. Compare Later

``` text
Spring Cloud / Eureka
application-oriented discovery

Kubernetes
platform-oriented service discovery
```

Do not assume every Kubernetes architecture needs Eureka.

------------------------------------------------------------------------

# Tier 44 --- API Gateway vs Kubernetes Ingress

## 142. Related but Different Concerns

Spring Cloud Gateway can implement application/API routing and filters.

Kubernetes Ingress/Gateway mechanisms handle cluster/network entry
routing.

## 143. Possible Architecture

``` text
Internet
   │
   ▼
Kubernetes ingress layer
   │
   ▼
Spring Cloud Gateway
   │
   ▼
Application Services
```

This is possible, but architecture should justify each layer.

## 144. Avoid Redundant Complexity

Do not add multiple routing layers simply because they exist.

------------------------------------------------------------------------

# Tier 45 --- JMeter Connection

## 145. Distributed Performance

JMeter can test the external behavior of the microservices system.

``` text
JMeter
  │
  ▼
Gateway / Service
  │
  ▼
Downstream Services
```

## 146. New Bottlenecks

Performance can degrade because of:

``` text
network calls
downstream latency
database
connection pools
gateway
serialization
resource saturation
```

## 147. Observability Required

JMeter tells you the client experience.

Logs/metrics/traces help identify which service caused the degradation.

------------------------------------------------------------------------

# Tier 46 --- Deployment Independence

## 148. Goal

Services should be deployable independently where practical.

## 149. Hidden Coupling

If every service must always deploy together, investigate whether the
architecture is actually independent.

## 150. Database Migrations

Schema changes need compatibility planning when multiple versions may
temporarily coexist.

------------------------------------------------------------------------

# Tier 47 --- Fault Isolation

## 151. Goal

Failure of one capability should not automatically destroy the entire
system.

## 152. Reality

Synchronous dependency chains can still propagate failures.

## 153. Design Question

Ask:

``` text
What happens to Message Service
if Profile Service is unavailable?
```

Possible answers may include:

``` text
fail request clearly
return partial response
use cached data
degrade feature
retry carefully
```

Choice depends on requirements.

------------------------------------------------------------------------

# Tier 48 --- Security Boundaries

## 154. More Services = More Communication

Each network boundary creates security concerns.

``` text
Client → Gateway
Gateway → Service
Service → Service
Service → Database
```

## 155. Authentication vs Authorization

Know the distinction.

## 156. Internal Does Not Mean Trusted

Do not assume traffic is safe merely because it occurs "inside"
infrastructure.

## 157. Future Security Trees

Detailed topics belong later:

``` text
Spring Security
OAuth2/OIDC
JWT
service identity
mTLS
API security
secret management
```

------------------------------------------------------------------------

# Tier 49 --- Microservice Anti-Patterns

## 158. Nano-Services

Do not create services so tiny that network/distributed overhead
overwhelms useful separation.

## 159. Shared Database Everywhere

Avoid uncontrolled schema coupling.

## 160. Chatty Services

Avoid excessive synchronous request chains.

## 161. No Timeouts

Remote calls must be treated as fallible.

## 162. Hardcoded Locations

Externalize locations and later use discovery where appropriate.

## 163. No Observability

A distributed system without useful logs/metrics/tracing becomes
difficult to diagnose.

------------------------------------------------------------------------

# Tier 50 --- Spring Cloud Architecture Map

At this point, the learner should understand **why** later Spring Cloud
tools exist:

``` text
Problem:
"Where is the service?"
        │
        ▼
Eureka Service Discovery


Problem:
"Where should external requests enter?"
        │
        ▼
Spring Cloud Gateway


Problem:
"How do many services share/manage config?"
        │
        ▼
Spring Cloud Config


Problem:
"What if dependencies fail?"
        │
        ▼
Resilience4j / Fault Tolerance


Problem:
"How do I understand one request
across many services?"
        │
        ▼
Distributed Observability / Tracing
```

------------------------------------------------------------------------

# Practical Competency Checkpoints

A learner completing this tree should be able to:

-   [ ] Define monolith
-   [ ] Define microservice
-   [ ] Explain why microservices exist
-   [ ] Explain why microservices are not automatically better
-   [ ] Compare monolith and microservice tradeoffs
-   [ ] Explain distributed-system complexity
-   [ ] Explain Spring Boot vs Spring Cloud
-   [ ] Explain Spring Cloud as an ecosystem
-   [ ] Recognize Spring Boot/Spring Cloud compatibility concerns
-   [ ] Explain service boundaries
-   [ ] Explain cohesion and coupling
-   [ ] Recognize a distributed monolith
-   [ ] Create multiple independent Spring Boot services
-   [ ] Give services separate ports/configuration
-   [ ] Design REST service contracts
-   [ ] Use DTOs across service boundaries
-   [ ] Implement a synchronous service-to-service HTTP call
-   [ ] Explain remote-call failure modes
-   [ ] Configure/understand timeouts
-   [ ] Explain retry risks
-   [ ] Explain circuit breakers conceptually
-   [ ] Explain cascading failure
-   [ ] Explain why service discovery exists
-   [ ] Explain a service registry
-   [ ] Explain why an API gateway exists
-   [ ] Explain centralized configuration problems
-   [ ] Explain Spring Cloud Config's purpose
-   [ ] Separate ordinary configuration from secrets
-   [ ] Explain service data ownership
-   [ ] Explain shared-database coupling
-   [ ] Explain why distributed transactions are harder
-   [ ] Explain eventual consistency
-   [ ] Compare synchronous and asynchronous communication
-   [ ] Explain idempotency at a useful level
-   [ ] Explain why logging gets harder across services
-   [ ] Explain correlation IDs
-   [ ] Explain distributed tracing
-   [ ] Explain logs vs metrics
-   [ ] Use Spring Boot health/Actuator concepts
-   [ ] Explain multiple instances of one logical service
-   [ ] Explain load-balancing needs
-   [ ] Explain API compatibility concerns
-   [ ] Explain integration testing
-   [ ] Explain contract testing at a high level
-   [ ] Run multiple services locally
-   [ ] Externalize service URLs
-   [ ] Containerize services
-   [ ] Explain Docker Compose's role
-   [ ] Compare Eureka discovery with Kubernetes discovery conceptually
-   [ ] Compare Spring Cloud Gateway with ingress concepts
-   [ ] Explain JMeter's role in microservice performance testing
-   [ ] Explain deployment independence
-   [ ] Explain fault isolation
-   [ ] Recognize microservice security boundaries
-   [ ] Identify common microservice anti-patterns
-   [ ] Explain why Eureka should be learned next
-   [ ] Explain why Spring Cloud Gateway follows naturally

------------------------------------------------------------------------

# Suggested Practice Progression

``` text
1. Start with familiar Spring Boot monolith
        │
        ▼
2. Identify two meaningful capabilities
        │
        ▼
3. Define service boundaries
        │
        ▼
4. Create Service A Spring Boot app
        │
        ▼
5. Create Service B Spring Boot app
        │
        ▼
6. Assign independent ports
        │
        ▼
7. Create REST contracts
        │
        ▼
8. Test each service independently
        │
        ▼
9. Call Service B from Service A
        │
        ▼
10. Map remote JSON to DTO
        │
        ▼
11. Externalize Service B URL
        │
        ▼
12. Stop Service B
        │
        ▼
13. Observe Service A failure
        │
        ▼
14. Add sensible timeout handling
        │
        ▼
15. Observe latency propagation
        │
        ▼
16. Add basic request/correlation logging
        │
        ▼
17. Give services separate data ownership
        │
        ▼
18. Test integration boundaries
        │
        ▼
19. Containerize each service
        │
        ▼
20. Run system with Docker Compose
        │
        ▼
21. Scale one service conceptually/practically
        │
        ▼
22. Observe hardcoded-location problem
        │
        ▼
23. Document need for service registry
        │
        ▼
24. Observe multiple public endpoints
        │
        ▼
25. Document need for API gateway
        │
        ▼
26. Continue to Eureka tree
        │
        ▼
27. Continue to Spring Cloud Gateway tree
```

------------------------------------------------------------------------

# Capstone --- Split a Familiar Application into Microservices

## Objective

Take a familiar Spring Boot application and deliberately split it into a
**small** microservices system.

Do not create many services merely to increase the count.

A useful educational architecture:

``` text
                    Frontend
                       │
              ┌────────┴────────┐
              ▼                 ▼
       Message Service    Profile Service
              │                 │
              ▼                 ▼
         Message Data       Profile Data
```

For the fundamentals capstone, the frontend may call services directly
or one service may orchestrate a request. Gateway is intentionally
deferred.

------------------------------------------------------------------------

## Phase 1 --- Start with the Monolith

Document the current responsibilities.

Example:

``` text
Guestbook Backend
├── messages
├── profile/user information
├── validation
└── persistence
```

Ask:

> Which capabilities are cohesive enough to separate without inventing
> artificial boundaries?

------------------------------------------------------------------------

## Phase 2 --- Define Two Services

Example:

``` text
Message Service
- create message
- list messages
- own message data

Profile Service
- retrieve profile/display-name data
- own profile data
```

Write down what each service **owns** and what it **does not own**.

------------------------------------------------------------------------

## Phase 3 --- Define Contracts

Example:

``` text
Profile Service

GET /api/profiles/{id}

Response:
{
  "id": 7,
  "displayName": "Alice"
}
```

Message Service should depend on this contract, not Profile Service's
Java classes/database tables.

------------------------------------------------------------------------

## Phase 4 --- Independent Spring Boot Applications

Create:

``` text
message-service
profile-service
```

Each should have:

``` text
own Spring Boot application
own port
own configuration
own build
own logs
```

------------------------------------------------------------------------

## Phase 5 --- Independent Data Ownership

Educationally:

``` text
Message Service
     │
     ▼
message database/schema

Profile Service
     │
     ▼
profile database/schema
```

They may run on the same local PostgreSQL server while remaining
logically owned separately.

Do not let Message Service query Profile Service's private tables
directly.

------------------------------------------------------------------------

## Phase 6 --- Service-to-Service HTTP

Implement:

``` text
Message Service
      │
      │ GET /api/profiles/{id}
      ▼
Profile Service
```

Use a modern Spring HTTP client approach.

Map the response into a remote DTO.

------------------------------------------------------------------------

## Phase 7 --- Externalize the Location

Do not permanently write:

``` text
"http://localhost:8082"
```

inside business logic.

Use configuration such as:

``` text
PROFILE_SERVICE_URL
```

and Spring configuration binding/property mechanisms.

------------------------------------------------------------------------

## Phase 8 --- Failure Drill

Stop Profile Service.

Call Message Service.

Observe:

``` text
Message Service
      │
      ▼
Profile Service unavailable
      │
      ▼
connection failure / timeout
```

Explain what happened.

------------------------------------------------------------------------

## Phase 9 --- Timeout Behavior

Configure a sensible educational timeout.

Introduce an artificial delay in Profile Service.

Observe:

``` text
Profile slow
    │
    ▼
Message waits
    │
    ▼
user response becomes slow
```

This demonstrates distributed latency.

------------------------------------------------------------------------

## Phase 10 --- Retry/Circuit-Breaker Discussion

Do not implement advanced resilience yet unless needed for a tiny
demonstration.

Document:

``` text
Could retry help?
Could retry hurt?
When would circuit breaking help?
```

Point to the future Resilience4j tree.

------------------------------------------------------------------------

## Phase 11 --- Correlation

Attach or propagate a simple request/correlation identifier.

``` text
Client request: abc123
        │
        ▼
Message Service log: abc123
        │
        ▼
Profile Service log: abc123
```

Use it to follow one request manually.

------------------------------------------------------------------------

## Phase 12 --- Integration Tests

Test:

``` text
service endpoint behavior
remote DTO mapping
failure handling
configuration
database boundary
```

Continue using JUnit/Mockito for isolated logic where appropriate.

------------------------------------------------------------------------

## Phase 13 --- Containerization

Create a Docker image for each service.

``` text
message-service
      │
      ▼
message image

profile-service
      │
      ▼
profile image
```

------------------------------------------------------------------------

## Phase 14 --- Docker Compose

Run:

``` text
Docker Compose
├── message-service
├── profile-service
└── PostgreSQL
```

Use container-network service names/configuration instead of localhost
where appropriate.

------------------------------------------------------------------------

## Phase 15 --- Scaling Thought Experiment

Imagine:

``` text
Profile Service
├── instance A
├── instance B
└── instance C
```

Ask:

> How does Message Service know which instance exists and where it is?

Answer:

``` text
We now need service discovery/load-balancing mechanisms.
```

This is the transition to **Eureka**.

------------------------------------------------------------------------

## Phase 16 --- Gateway Thought Experiment

Now imagine the frontend needs:

``` text
Message Service URL
Profile Service URL
future Order Service URL
future Auth Service URL
```

Ask:

> Should every client know every internal service location?

Introduce:

``` text
Frontend
   │
   ▼
API Gateway
   │
   ├── Message Service
   └── Profile Service
```

This is the transition to **Spring Cloud Gateway**.

------------------------------------------------------------------------

## Phase 17 --- Performance Observation

Optionally reuse JMeter against the system.

Compare:

``` text
single-service call
vs
service chain
```

Observe that network/downstream latency becomes part of end-user
response time.

Do not attempt serious optimization yet.

------------------------------------------------------------------------

## Phase 18 --- Architecture Write-Up

Document:

``` text
Why were these boundaries chosen?
What does each service own?
How do services communicate?
What happens when one fails?
Where is configuration stored?
What is currently hardcoded/configured?
Why is service discovery needed?
Why is a gateway needed?
What would resilience tooling add?
What would observability tooling add?
```

The capstone is complete when the learner can explain the architecture,
not merely start both applications.

------------------------------------------------------------------------

# Interview Readiness

Be able to answer:

-   [ ] What is a monolith?
-   [ ] What is a microservice?
-   [ ] Why use microservices?
-   [ ] When might a monolith be better?
-   [ ] What complexity do microservices introduce?
-   [ ] What is a distributed system?
-   [ ] Spring Boot vs Spring Cloud?
-   [ ] What is Spring Cloud?
-   [ ] Why does Spring Boot/Spring Cloud version compatibility matter?
-   [ ] What is a service boundary?
-   [ ] What are cohesion and coupling?
-   [ ] What is a distributed monolith?
-   [ ] Why can overly small services be harmful?
-   [ ] How do microservices communicate?
-   [ ] What is synchronous communication?
-   [ ] What is asynchronous communication?
-   [ ] What can go wrong with a remote call?
-   [ ] Why are timeouts necessary?
-   [ ] Why can retries be dangerous?
-   [ ] What is a circuit breaker?
-   [ ] What is cascading failure?
-   [ ] What is service discovery?
-   [ ] What is a service registry?
-   [ ] Why use Eureka?
-   [ ] What is an API gateway?
-   [ ] Why use Spring Cloud Gateway?
-   [ ] Why externalize configuration?
-   [ ] What problem does Spring Cloud Config address?
-   [ ] What is service data ownership?
-   [ ] Why can a shared database tightly couple services?
-   [ ] Why are distributed transactions difficult?
-   [ ] What is eventual consistency?
-   [ ] What is idempotency?
-   [ ] Why is observability harder in microservices?
-   [ ] What is a correlation ID?
-   [ ] What is distributed tracing?
-   [ ] Logs vs metrics?
-   [ ] Why expose service health?
-   [ ] What does scaling a service mean?
-   [ ] Why is load balancing needed?
-   [ ] Why does API compatibility matter more with independent
    deployment?
-   [ ] What is integration testing?
-   [ ] What is contract testing?
-   [ ] How does Docker help microservices?
-   [ ] Does Docker automatically make an architecture microservices?
-   [ ] Eureka vs Kubernetes service discovery?
-   [ ] Spring Cloud Gateway vs Kubernetes Ingress?
-   [ ] How can JMeter help test microservices?
-   [ ] What is fault isolation?
-   [ ] Why are internal service calls still security boundaries?

------------------------------------------------------------------------

# Common Anti-Patterns

## Anti-Pattern 1 --- Microservices Because They Are Popular

Architecture should solve actual problems.

## Anti-Pattern 2 --- One Service per Class/Table

Service boundaries should represent cohesive capabilities.

## Anti-Pattern 3 --- Distributed Monolith

Services are separated physically but cannot evolve/deploy
independently.

## Anti-Pattern 4 --- Shared Database as the Integration API

Prefer explicit service contracts where independence matters.

## Anti-Pattern 5 --- Directly Sharing JPA Entities Across Services

Network contracts should not depend on persistence internals.

## Anti-Pattern 6 --- Hardcoded Service URLs

Externalize locations and later use discovery/platform mechanisms.

## Anti-Pattern 7 --- No Timeouts

Remote calls are fallible.

## Anti-Pattern 8 --- Retry Everything

Retries can amplify overload and duplicate side effects.

## Anti-Pattern 9 --- Long Synchronous Call Chains

Each dependency adds latency/failure risk.

## Anti-Pattern 10 --- No Correlation Across Logs

Distributed debugging becomes unnecessarily difficult.

## Anti-Pattern 11 --- No API Compatibility Strategy

Independent deployment requires contract discipline.

## Anti-Pattern 12 --- Treating "Internal" as "Secure"

Internal network calls still require security design.

## Anti-Pattern 13 --- One Giant Shared Configuration File with Secrets

Separate configuration responsibilities and protect secrets.

## Anti-Pattern 14 --- Assuming Containers Solve Architecture

Docker packages applications; it does not define good service
boundaries.

## Anti-Pattern 15 --- Assuming Kubernetes Replaces All Spring Cloud Concepts

Some responsibilities overlap, some differ, and architecture determines
what is needed.

## Anti-Pattern 16 --- Adding Eureka Inside Kubernetes Automatically

Kubernetes already provides service discovery mechanisms; justify
additional discovery layers.

## Anti-Pattern 17 --- Adding Gateway + Ingress + Proxies Without Purpose

Every routing layer adds operational complexity.

## Anti-Pattern 18 --- Splitting Before Understanding the Monolith

Understand responsibilities before defining boundaries.

------------------------------------------------------------------------

# Relationship to Existing Skill Trees

Core prerequisite path:

``` text
Java
 │
 ▼
Spring Boot & Initializr
 │
 ▼
HTTP / HTTPS / REST API
 │
 ▼
Spring Cloud & Microservices Fundamentals
```

Persistence connection:

``` text
ORM / JPA / Hibernate
       │
       ▼
Spring Data JPA
       │
       ▼
Service-Owned Persistence
       │
       ▼
Microservices Data Boundaries
```

Testing connection:

``` text
JUnit / Mockito
       │
       ▼
Service Tests
       │
       ▼
Integration / Contract Awareness

JMeter
       │
       ▼
Distributed-System Performance
```

Deployment connection:

``` text
Microservices
     │
     ▼
Docker
     │
     ▼
Deployment Fundamentals
     │
     ▼
Kubernetes
```

Observability connection:

``` text
Microservices
     │
     ▼
Multiple Logs / Metrics / Traces
     │
     ▼
ELK + Future Observability Trees
```

Immediate Spring Cloud progression:

``` text
Spring Cloud & Microservices Fundamentals
             │
      ┌──────┴──────┐
      ▼             ▼
   Eureka      Spring Cloud
  Discovery       Gateway
      │             │
      └──────┬──────┘
             ▼
    Discovery-Based Routing
```

Recommended learning order:

``` text
Spring Cloud & Microservices Fundamentals
             │
             ▼
      Eureka Service Discovery
             │
             ▼
      Spring Cloud Gateway
             │
             ▼
     Combined Microservices
           Capstone
```

Gateway does not conceptually require Eureka in every architecture, but
learning Eureka first makes discovery-based routing easier to
understand.

------------------------------------------------------------------------

# Future Microservices Branches

``` text
              Spring Cloud & Microservices
                         │
       ┌─────────────────┼──────────────────┐
       ▼                 ▼                  ▼
    Eureka            Gateway          Configuration
       │                 │                  │
       │                 │                  ▼
       │                 │          Spring Cloud Config
       │                 │
       └────────┬────────┘
                │
                ▼
          Service Platform
                │
     ┌──────────┼───────────┐
     ▼          ▼           ▼
Resilience   Security   Observability
     │                      │
     ▼                      ▼
Resilience4j          Metrics/Tracing
```

Another major branch:

``` text
Microservices
     │
     ▼
Synchronous REST
     │
     └──────────────┐
                    ▼
          Event-Driven Architecture
                    │
              ┌─────┴─────┐
              ▼           ▼
            Kafka      RabbitMQ
```

Potential future skill trees:

-   [ ] Eureka Service Discovery
-   [ ] Spring Cloud Gateway
-   [ ] Spring Cloud Config
-   [ ] Resilience4j & Fault Tolerance
-   [ ] Spring Security for Microservices
-   [ ] OAuth2 / OpenID Connect / JWT
-   [ ] Distributed Observability & Tracing
-   [ ] Spring Cloud Contract / Contract Testing
-   [ ] Event-Driven Microservices Fundamentals
-   [ ] Apache Kafka
-   [ ] RabbitMQ
-   [ ] Saga / Distributed Data Consistency
-   [ ] API Design & Versioning
-   [ ] Production Microservices Architecture

------------------------------------------------------------------------

# Mastery Progression

``` text
"I can build one Spring Boot application"
            │
            ▼
"I understand monoliths"
            │
            ▼
"I understand why microservices exist"
            │
            ▼
"I understand their costs"
            │
            ▼
"I can identify useful service boundaries"
            │
            ▼
"I can create independent Spring Boot services"
            │
            ▼
"I can define REST contracts between them"
            │
            ▼
"I can call one service from another"
            │
            ▼
"I understand remote calls can fail"
            │
            ▼
"I understand timeouts/retries/circuit breakers"
            │
            ▼
"I understand service data ownership"
            │
            ▼
"I understand distributed consistency problems"
            │
            ▼
"I can observe requests across services"
            │
            ▼
"I can containerize and run multiple services"
            │
            ▼
"I see why hardcoded service locations fail"
            │
            ▼
"I understand why Eureka exists"
            │
            ▼
"I see why clients should not know every service"
            │
            ▼
"I understand why Gateway exists"
            │
            ▼
"I am ready for Eureka and Gateway"
```

------------------------------------------------------------------------

# Mastery Standard

> **Can I independently explain the tradeoffs between a monolith and
> microservices, identify reasonable service boundaries, create multiple
> independent Spring Boot applications, define REST/DTO contracts
> between them, implement synchronous service-to-service HTTP
> communication, externalize service locations and configuration,
> explain and demonstrate timeout/failure behavior, understand
> retry/circuit-breaker/cascading-failure concepts, maintain service
> data ownership, explain distributed transaction and
> eventual-consistency problems, correlate activity across service logs,
> test integration boundaries, containerize and run the services
> together, and clearly explain why the next architecture needs service
> discovery through Eureka and centralized routing through Spring Cloud
> Gateway?**

Final mental model:

``` text
MICROSERVICES SYSTEM

                         Client
                           │
                           ▼
                    [Gateway Later]
                           │
               ┌───────────┼───────────┐
               ▼           ▼           ▼
           Service A   Service B   Service C
               │           │           │
               ▼           ▼           ▼
             Data A      Data B      Data C

Services communicate through contracts:

Service A
  │
  │  HTTP / REST / JSON
  ▼
Service B

But the network introduces:

  - latency
  - timeouts / failure
  - changing locations
  - multiple instances
  - security boundaries
  - observability problems
  - data consistency problems

Therefore:

Changing Service Locations
  │
  ▼
Service Discovery Problem
  │
  ▼
Eureka

Many Client-Facing Services
  │
  ▼
Routing / Entry-Point Problem
  │
  ▼
Spring Cloud Gateway

Many Configuration Sets
  │
  ▼
Configuration Problem
  │
  ▼
Spring Cloud Config [later]

Dependency Failure
  │
  ▼
Resilience Problem
  │
  ▼
Resilience4j [later]

Many Logs / Calls
  │
  ▼
Observability Problem
  │
  ▼
ELK / Metrics / Tracing [later]

Overall progression:

Spring Boot
  │
  ▼
REST APIs
  │
  ▼
Spring Cloud & Microservices Fundamentals
  │
  ▼
Eureka Service Discovery
  │
  ▼
Spring Cloud Gateway
  │
  ▼
Resilience / Config / Observability
  │
  ▼
Docker
  │
  ▼
Kubernetes / Cloud Deployment
```
