# Eureka Service Discovery --- Full-Stack Developer Skill Tree

> **Goal:** Learn service discovery at the developer level using Spring
> Cloud Netflix Eureka: understand why dynamic service discovery exists,
> build a Eureka Server, register Spring Boot services as Eureka
> clients, discover logical services without hardcoded instance
> addresses, work with multiple service instances, understand
> health/lease behavior and client-side load-balancing concepts, and
> prepare the architecture for Spring Cloud Gateway.
>
> **Target level:** A full-stack Java/Spring developer who can
> independently add Eureka-based service registration and discovery to a
> small Spring Boot microservices system, troubleshoot common
> registration/discovery problems, and explain when Eureka is useful
> versus when platform-level discovery such as Kubernetes Services/DNS
> may make it unnecessary.
>
> **Primary prerequisite:** **Spring Cloud & Microservices
> Fundamentals**
>
> **Supporting prerequisites:** Spring Boot & Initializr, HTTP/HTTPS &
> REST API Fundamentals, Java, Maven, Spring configuration, environment
> variables, REST service-to-service communication, health checks, and
> basic Docker knowledge.
>
> **Downstream tree:** **Spring Cloud Gateway**
>
> **Scope boundary:** This tree focuses on Eureka as a developer-facing
> service registry/discovery mechanism. It does not attempt to teach
> advanced distributed-systems consensus, production registry-cluster
> administration, deep Spring Cloud Gateway routing, Kubernetes
> networking, or advanced resilience. Those belong in later trees.
>
> **Capstone:** Take the two-service microservices application from the
> Spring Cloud & Microservices Fundamentals capstone, add a Eureka
> Server, register both services, replace a hardcoded downstream URL
> with logical service discovery, run multiple instances of one service,
> observe registration/lease/failure behavior, and leave the system
> ready for a Spring Cloud Gateway to become the single client-facing
> entry point.

------------------------------------------------------------------------

# Skill Tree Overview

``` text
Spring Cloud & Microservices Fundamentals
                 │
                 ▼
      Hardcoded Service Location
                 │
                 ▼
      Service Discovery Problem
                 │
                 ▼
           Eureka Server
                 │
          ┌──────┴──────┐
          ▼             ▼
     Service A       Service B
    Eureka Client   Eureka Client
          │             │
          └──────┬──────┘
                 ▼
        Logical Service Names
                 │
                 ▼
      Multiple Service Instances
                 │
                 ▼
       Discovery + Load Balancing
                 │
                 ▼
        Spring Cloud Gateway
```

Core mental model:

``` text
WITHOUT DISCOVERY

Message Service
      │
      ▼
http://10.0.0.17:8082
      │
      ▼
Profile Service

Problems:
- address changes
- service restarts
- multiple instances
- scaling
- environment differences


WITH EUREKA

Profile Service Instance A ──register──┐
Profile Service Instance B ──register──┤
Message Service ─────────────register──┤
                                      ▼
                                  Eureka Server
                                      │
                                      ▼
Message Service asks:
"Where is PROFILE-SERVICE?"
                                      │
                                      ▼
                           available instance information
```

------------------------------------------------------------------------

# Tier 0 --- Review the Problem Eureka Solves

## 1. Hardcoded Location

Early microservice communication may use:

``` text
PROFILE_SERVICE_URL=http://localhost:8082
```

This works for simple local development.

## 2. The Problem

A fixed address becomes fragile when services:

``` text
restart
move
scale
fail
run in different environments
```

## 3. Logical Service Identity

The caller should ideally ask for:

``` text
PROFILE-SERVICE
```

rather than permanently knowing:

``` text
192.168.1.20:8082
```

## 4. Service Discovery

Service discovery maps a logical service identity to currently available
service instances.

------------------------------------------------------------------------

# Tier 1 --- Service Registry

## 5. Registry Concept

A registry stores information about available service instances.

``` text
              Eureka Server
              Service Registry
              ▲            ▲
              │            │
       Profile A       Profile B
```

## 6. Registration

A service instance announces itself to the registry.

Conceptually:

``` text
"I am PROFILE-SERVICE.
I am available at this host/port."
```

## 7. Discovery

A consumer retrieves information about instances of a logical service.

## 8. Dynamic Infrastructure

The registry helps callers avoid treating physical addresses as
permanent application configuration.

------------------------------------------------------------------------

# Tier 2 --- What Eureka Is

## 9. Eureka

Eureka is a service registration and discovery system associated with
the Spring Cloud Netflix ecosystem.

## 10. Eureka Server

The server maintains the registry.

``` text
Eureka Server
    │
    └── registered applications/instances
```

## 11. Eureka Client

A participating Spring Boot service can register with and query Eureka.

## 12. Developer Mental Model

``` text
Eureka Server = directory

Eureka Client = service that can
register itself and/or discover others
```

------------------------------------------------------------------------

# Tier 3 --- What Eureka Is Not

## 13. Not an API Gateway

Eureka tells services **where other services are**.

It does not itself provide the main external routing layer.

``` text
Discovery → Eureka

Routing / client entry point → Spring Cloud Gateway
```

## 14. Not a Database for Business Data

Do not store application domain records in Eureka.

## 15. Not a Message Broker

It does not replace Kafka/RabbitMQ.

## 16. Not Automatically Required

Some deployment platforms already provide service discovery mechanisms.

------------------------------------------------------------------------

# Tier 4 --- Architecture Before Eureka

Start with the fundamentals capstone:

``` text
Message Service
      │
      │ configured URL
      ▼
Profile Service
```

Example configuration:

``` text
profile.service.url=http://localhost:8082
```

The goal is to deliberately experience why this becomes awkward.

------------------------------------------------------------------------

# Tier 5 --- Architecture After Eureka

``` text
                 Eureka Server
                 ▲           ▲
                 │           │
          registers       registers
                 │           │
          Message       Profile
          Service       Service
                 │
                 │ discover PROFILE-SERVICE
                 └──────────────►
```

The caller now depends on a **logical service name** rather than a
permanently fixed instance address.

------------------------------------------------------------------------

# Tier 6 --- Spring Cloud Compatibility

## 17. Spring Boot / Spring Cloud Compatibility

Use a Spring Cloud release compatible with the project's Spring Boot
version.

## 18. Dependency Management

Use the appropriate Spring dependency-management/BOM approach instead of
manually guessing transitive component versions.

## 19. Current Documentation Habit

Because Spring Cloud integrations evolve, verify current setup
conventions when starting a new project.

## 20. Avoid Historical Copy/Paste

Do not assume an old Eureka tutorial matches a modern Spring Boot/Spring
Cloud project.

------------------------------------------------------------------------

# Tier 7 --- Create the Eureka Server Project

## 21. Separate Spring Boot Application

Create a dedicated service such as:

``` text
discovery-server/
```

## 22. Responsibility

Its primary responsibility is service registration/discovery.

Keep business controllers, repositories, and domain logic out of it.

## 23. Port

A common educational setup gives the discovery server its own known
port.

Example:

``` text
Eureka Server → 8761
```

The exact port is configuration, not a rule of the architecture.

------------------------------------------------------------------------

# Tier 8 --- Eureka Server Dependency

## 24. Add Server Support

Configure the project with the appropriate Spring Cloud Eureka Server
dependency.

## 25. Enable Server Behavior

Use the current supported Spring configuration/annotation mechanism for
enabling the Eureka server.

## 26. Keep It Focused

The discovery server should remain infrastructure-oriented.

------------------------------------------------------------------------

# Tier 9 --- Eureka Server Configuration

## 27. Application Name

Give the discovery server a clear application name.

## 28. Registry Behavior

A standalone educational Eureka server does not normally need to behave
like a business service registering itself with itself.

Understand configuration that controls whether the server:

``` text
registers with another registry
fetches registry information
```

## 29. Environment Configuration

Do not bury environment-specific addresses inside Java source.

------------------------------------------------------------------------

# Tier 10 --- Start and Verify Eureka Server

## 30. Start Independently

Run the discovery server before adding clients.

## 31. Dashboard

Use the Eureka dashboard as a learning/debugging view of registered
applications and instances.

## 32. Initial State

Before clients register, the registry should contain no business
services.

## 33. Verification Habit

Confirm the server is reachable before troubleshooting clients.

------------------------------------------------------------------------

# Tier 11 --- Convert a Service into a Eureka Client

## 34. Client Dependency

Add the appropriate Eureka client dependency to a Spring Boot
microservice.

## 35. Logical Application Name

Configure:

``` text
spring.application.name
```

This name becomes important for discovery.

Example:

``` text
PROFILE-SERVICE
```

## 36. Registry Location

Configure the client so it knows how to reach the Eureka server.

## 37. Start the Client

Run the service and verify it appears in the registry.

------------------------------------------------------------------------

# Tier 12 --- Application Name as Service Identity

## 38. Physical Identity

``` text
host + port
```

identifies a particular running instance.

## 39. Logical Identity

``` text
PROFILE-SERVICE
```

identifies the service capability.

## 40. Multiple Instances

Several physical instances can share one logical service name.

``` text
PROFILE-SERVICE
├── instance A
├── instance B
└── instance C
```

This distinction is central to discovery.

------------------------------------------------------------------------

# Tier 13 --- Instance Identity

## 41. Service vs Instance

Do not confuse:

``` text
application/service name
```

with:

``` text
individual instance identity
```

## 42. Instance Metadata

A registry entry may include information such as:

``` text
host
port
status
instance identifier
metadata
```

## 43. Troubleshooting

When duplicate/conflicting registrations appear, inspect instance
identity and configuration.

------------------------------------------------------------------------

# Tier 14 --- Register Multiple Services

Add the second microservice.

``` text
              Eureka
             ▲      ▲
             │      │
       MESSAGE    PROFILE
       SERVICE    SERVICE
```

Verify both logical applications appear.

------------------------------------------------------------------------

# Tier 15 --- Registration Lifecycle

## 44. Startup

A client starts and registers.

## 45. Continued Presence

Registration is not conceptually a one-time permanent record.

The system needs evidence that an instance remains alive.

## 46. Shutdown/Failure

When an instance disappears, the registry eventually needs to stop
advertising it as available.

------------------------------------------------------------------------

# Tier 16 --- Heartbeats and Leases

## 47. Heartbeat Concept

A client periodically communicates continued availability.

``` text
Client
 │
 ├── heartbeat
 ├── heartbeat
 └── heartbeat
       │
       ▼
    Eureka
```

## 48. Lease Concept

The registry treats availability as time-sensitive rather than eternal.

## 49. Failure Detection Is Not Instantaneous

Distributed systems generally cannot know immediately and perfectly that
a remote process is dead.

## 50. Important Mental Model

``` text
"not heard from recently"
```

is different from:

``` text
"mathematically proven dead instantly"
```

------------------------------------------------------------------------

# Tier 17 --- Registry Freshness

## 51. Discovery Information Can Be Temporarily Stale

A recently failed instance may remain visible for some period.

## 52. Eventual Update

Clients/registry information converge as registration, heartbeat,
expiration, and refresh mechanisms operate.

## 53. Developer Consequence

Discovery reduces hardcoded-location problems but does not make network
failure disappear.

------------------------------------------------------------------------

# Tier 18 --- Eureka Self-Preservation Awareness

## 54. Failure Ambiguity

If many heartbeats disappear simultaneously, the registry must consider
whether:

``` text
many services died
```

or:

``` text
the network/registry communication is failing
```

## 55. Self-Preservation Concept

Eureka includes protective behavior intended to avoid aggressively
removing large portions of the registry during certain communication
failures.

## 56. Scope

Understand why such a mechanism exists.

Do not turn the developer tree into deep Eureka server
operations/tuning.

------------------------------------------------------------------------

# Tier 19 --- Fetching Registry Information

## 57. Consumer Needs Discovery Data

A service that calls another service needs information about available
instances.

## 58. Registry Fetch

Conceptually:

``` text
Consumer
   │
   ▼
Eureka
   │
   ▼
registry information
```

## 59. Caching Awareness

Discovery clients may use locally available/cached registry information
rather than contacting the server for every application request.

## 60. Consequence

Discovery behavior is distributed and can involve temporary staleness.

------------------------------------------------------------------------

# Tier 20 --- Discover by Logical Name

Replace:

``` text
http://localhost:8082/api/profiles/7
```

with a discovery-oriented mental model:

``` text
PROFILE-SERVICE
       │
       ▼
available instance
       │
       ▼
/api/profiles/7
```

The exact Spring client integration belongs in the hands-on exercises.

------------------------------------------------------------------------

# Tier 21 --- Client-Side Load Balancing Concept

## 61. Multiple Instances

Suppose Eureka reports:

``` text
PROFILE-SERVICE
├── A
├── B
└── C
```

The caller needs to choose an instance.

## 62. Load Balancing

``` text
Message Service
      │
      ▼
load-balancing decision
   ┌──┼──┐
   ▼  ▼  ▼
   A  B  C
```

## 63. Spring Cloud LoadBalancer Awareness

Modern Spring Cloud applications can integrate discovery with
client-side load-balancing mechanisms.

## 64. Scope

Learn the practical discovery/load-balancing integration needed for REST
calls.

Advanced load-balancing algorithms belong later.

------------------------------------------------------------------------

# Tier 22 --- Discovery-Aware HTTP Client

## 65. Previous Approach

``` text
HTTP client
   │
   ▼
configured host:port
```

## 66. Discovery-Aware Approach

``` text
HTTP client
   │
   ▼
logical service name
   │
   ▼
discovery/load balancing
   │
   ▼
physical instance
```

## 67. Business Logic

Keep discovery mechanics out of core business logic as much as
practical.

------------------------------------------------------------------------

# Tier 23 --- Replace Hardcoded URL

Before:

``` text
PROFILE_SERVICE_URL=http://localhost:8082
```

After:

``` text
logical target = PROFILE-SERVICE
```

The service should no longer require the exact downstream instance
address for normal discovery-based communication.

------------------------------------------------------------------------

# Tier 24 --- Test One Discovered Call

Build the smallest useful flow:

``` text
GET request
   │
   ▼
Message Service
   │
   │ discover PROFILE-SERVICE
   ▼
Profile Service
   │
   ▼
response
```

Verify both correctness and registry behavior.

------------------------------------------------------------------------

# Tier 25 --- Run Multiple Instances

## 68. Same Logical Name

Start multiple Profile Service processes with:

``` text
same spring.application.name
different ports
```

Conceptually:

``` text
PROFILE-SERVICE
├── localhost:8082
└── localhost:8083
```

## 69. Registry Verification

Confirm both appear as instances of the same logical service.

## 70. Key Insight

Scaling creates more **instances**, not new logical services.

------------------------------------------------------------------------

# Tier 26 --- Observe Request Distribution

## 71. Add Instance Visibility

For learning, make responses/logs identify which instance handled a
request.

Example conceptual response/log:

``` text
handled-by: profile-instance-2
```

## 72. Repeated Calls

Send repeated requests and observe whether discovery/load-balancing
integration selects different instances.

## 73. Do Not Confuse Algorithm with Architecture

The important concept is that callers do not manually hardcode every
instance.

------------------------------------------------------------------------

# Tier 27 --- Failure Drill

## 74. Stop One Instance

``` text
PROFILE-SERVICE
├── A  running
└── B  stopped
```

## 75. Observe Registry

Watch how the failed instance's status/registration changes over time.

## 76. Continue Requests

Determine whether calls can continue through the remaining instance.

## 77. Key Lesson

Service discovery supports changing instance sets, but failure handling
still requires sound client/resilience behavior.

------------------------------------------------------------------------

# Tier 28 --- Eureka Server Failure Awareness

## 78. What If Eureka Is Down?

Ask separately:

``` text
Can already-running clients still communicate?
Can new services register?
Can registry data refresh?
```

## 79. Cached Information

Existing clients may have previously obtained discovery information.

## 80. Scope

Understand the architectural concern.

High-availability Eureka server clusters are production/operations
territory and are not required for developer-level mastery.

------------------------------------------------------------------------

# Tier 29 --- Eureka Server High Availability Awareness

## 81. Registry Is Infrastructure

A production registry can itself require redundancy.

## 82. Peer Awareness

Eureka servers can be deployed in more resilient arrangements.

## 83. Scope Boundary

Know **why** multiple registry servers may exist.

Do not make cluster administration a prerequisite for using Eureka as a
developer.

------------------------------------------------------------------------

# Tier 30 --- Health and Status

## 84. Registered Does Not Always Mean Useful

A process may exist but still be unhealthy.

## 85. Health Information

Connect service registration with Spring Boot health/Actuator concepts.

## 86. Readiness Nuance

Different platforms/framework integrations have different ways to
determine whether an instance should receive traffic.

## 87. Developer Goal

Understand the distinction:

``` text
process exists
vs
service can successfully handle requests
```

------------------------------------------------------------------------

# Tier 31 --- Configuration Discipline

## 88. Eureka URL

The location of the registry is itself configuration.

## 89. Environment Differences

Local:

``` text
http://localhost:8761/...
```

Container/cloud:

``` text
different hostname/location
```

## 90. Externalize

Use Spring configuration/environment mechanisms rather than Java
literals.

------------------------------------------------------------------------

# Tier 32 --- Docker Connection

## 91. Containerize the Discovery Server

``` text
Eureka Server
     │
     ▼
Docker Image
```

## 92. Containerize Clients

``` text
message-service
profile-service
discovery-server
```

## 93. Compose

Run them together:

``` text
Docker Compose
├── discovery-server
├── message-service
└── profile-service
```

## 94. Container Networking

Inside Compose, `localhost` refers to the current container, not another
service.

Use the appropriate container/service hostname through configuration.

------------------------------------------------------------------------

# Tier 33 --- Startup Timing

## 95. Process Started vs Ready

Compose startup order does not necessarily mean Eureka is ready to
accept useful client interaction.

## 96. Retry/Registration Behavior

Clients may need to tolerate infrastructure becoming available after
they start.

## 97. Health Checks

Use health/readiness concepts where appropriate instead of assuming
timing delays are reliable.

------------------------------------------------------------------------

# Tier 34 --- Eureka Dashboard as Debugging Tool

Use the dashboard to answer:

``` text
Did the service register?
What application name did it use?
How many instances exist?
What host/port is advertised?
What status is shown?
```

Do not treat the dashboard as your application's user interface.

------------------------------------------------------------------------

# Tier 35 --- Logs as Debugging Tool

When registration/discovery fails, inspect:

``` text
Eureka Server logs
client startup logs
HTTP/client errors
configuration values
```

Avoid debugging only by staring at source code.

------------------------------------------------------------------------

# Tier 36 --- Common Registration Problems

## 98. Wrong Eureka URL

Client cannot reach the registry.

## 99. Wrong Application Name

Consumer looks for a different logical name.

## 100. Port/Host Confusion

The service advertises or uses an unexpected network location.

## 101. Version Mismatch

Spring Boot/Spring Cloud dependency incompatibility can cause confusing
startup/runtime behavior.

## 102. Registry Not Running

Always verify infrastructure first.

------------------------------------------------------------------------

# Tier 37 --- Common Discovery Problems

## 103. Service Registered but Call Fails

Check:

``` text
logical service name
discovery-aware client configuration
load-balancing integration
endpoint path
service health
network reachability
```

## 104. Wrong Path

Discovery finds a service instance; it does not fix an incorrect REST
endpoint.

## 105. Authentication

Discovery does not bypass security requirements.

------------------------------------------------------------------------

# Tier 38 --- Service Names and Naming Discipline

## 106. Stable Logical Names

Choose names that represent service capabilities.

Example:

``` text
MESSAGE-SERVICE
PROFILE-SERVICE
```

## 107. Avoid Environment in Logical Identity When Unnecessary

Prefer environment configuration around deployment rather than inventing
unrelated service identities for every machine.

## 108. Consistency

Consumers, gateway routes, dashboards, and documentation should use
consistent names.

------------------------------------------------------------------------

# Tier 39 --- Discovery Does Not Replace Resilience

Eureka can answer:

``` text
"What instances are known?"
```

It cannot guarantee:

``` text
"this request will succeed"
```

You still need to consider:

``` text
timeouts
connection failures
retries
circuit breakers
downstream overload
```

This connects to the future **Resilience4j & Fault Tolerance** tree.

------------------------------------------------------------------------

# Tier 40 --- Discovery Does Not Replace Observability

When a request fails:

``` text
Message Service
      │
      ▼
PROFILE-SERVICE
```

you still need:

``` text
logs
metrics
correlation IDs
tracing
```

to understand what happened.

This connects to ELK and future observability trees.

------------------------------------------------------------------------

# Tier 41 --- Discovery Does Not Replace Configuration

Eureka discovers service instances.

It does not centralize every configuration property.

``` text
Service discovery → Eureka

Central configuration → Spring Cloud Config / platform configuration
```

------------------------------------------------------------------------

# Tier 42 --- Discovery Does Not Replace Security

## 109. Registry Knowledge Is Not Authorization

Knowing where a service is does not mean a caller should be allowed to
use it.

## 110. Internal Traffic Still Needs Security Design

Later topics:

``` text
Spring Security
JWT
OAuth2/OIDC
service identity
mTLS
network policy
```

## 111. Protect Infrastructure

Production discovery infrastructure should not be exposed carelessly.

------------------------------------------------------------------------

# Tier 43 --- Eureka and Spring Cloud Gateway

## 112. Eureka Solves Location

``` text
Gateway asks:
"Where is MESSAGE-SERVICE?"
```

## 113. Gateway Solves Routing

``` text
Client
   │
   ▼
Gateway
   │
   ├── /messages → MESSAGE-SERVICE
   └── /profiles → PROFILE-SERVICE
```

## 114. Combined Model

``` text
                 Client
                   │
                   ▼
          Spring Cloud Gateway
                   │
                   │ asks Eureka
                   ▼
              Eureka Server
              /           \
             ▼             ▼
      MESSAGE-SERVICE  PROFILE-SERVICE
```

This is the direct transition to the next skill tree.

------------------------------------------------------------------------

# Tier 44 --- Discovery-Based Gateway Routing Awareness

## 115. Static Route

A gateway could route to a fixed address.

## 116. Discovery Route

With Eureka, it can route toward logical services/instances.

## 117. Benefit

The gateway does not need a permanently fixed physical address for every
instance.

## 118. Scope

Understand the architecture here.

Implement routes, predicates, filters, path rewriting, CORS, and
discovery-based gateway behavior in the **Spring Cloud Gateway** tree.

------------------------------------------------------------------------

# Tier 45 --- Kubernetes Comparison

## 119. Kubernetes Service Discovery

Kubernetes provides service discovery through platform mechanisms such
as Services and DNS.

Concept:

``` text
Service A
   │
   ▼
profile-service
   │
   ▼
Kubernetes Service
   │
   ▼
Pods
```

## 120. Eureka Model

``` text
Spring Application
   │
   ▼
Eureka Registry
   │
   ▼
registered application instances
```

## 121. Important Architectural Lesson

Do not automatically deploy Eureka just because an application uses
Spring Cloud.

If Kubernetes already provides the needed discovery behavior, Eureka may
be redundant.

------------------------------------------------------------------------

# Tier 46 --- Why Learn Eureka Before Kubernetes Replacement Decisions?

Because Eureka makes the underlying problem explicit:

``` text
logical service
      │
      ▼
changing physical instances
```

Once this is understood, Kubernetes discovery is easier to understand as
another solution to the same class of problem.

------------------------------------------------------------------------

# Tier 47 --- AWS Awareness

A non-Kubernetes cloud deployment can also have changing service
locations and scaling.

Service discovery remains an architectural concern.

The exact AWS-native alternatives belong in later AWS/cloud trees.

------------------------------------------------------------------------

# Tier 48 --- JMeter Connection

With multiple service instances:

``` text
JMeter
   │
   ▼
Gateway later / Service
   │
   ▼
discovery
   │
   ▼
multiple instances
```

JMeter can later help observe whether scaling/distribution changes
client-visible performance.

Eureka itself is not a performance-testing tool.

------------------------------------------------------------------------

# Tier 49 --- CI/CD and Deployment Connection

Deployments may:

``` text
start new instances
stop old instances
change instance addresses
scale instance counts
```

Discovery helps decouple callers from those physical changes.

This is one reason discovery becomes valuable in independently deployed
systems.

------------------------------------------------------------------------

# Tier 50 --- Common Eureka Anti-Patterns

## Anti-Pattern 1 --- Hardcoding Every Instance Anyway

If consumers still manually maintain every host/port, discovery provides
little value.

## Anti-Pattern 2 --- Using Eureka as Gateway

Discovery and request routing are different responsibilities.

## Anti-Pattern 3 --- Assuming Registration Means Healthy Forever

Registry state changes over time.

## Anti-Pattern 4 --- Assuming Discovery Prevents Network Failure

It only helps locate instances.

## Anti-Pattern 5 --- Treating One Logical Service Instance as a New Service

Scaling creates instances of the same logical capability.

## Anti-Pattern 6 --- Ignoring Spring Version Compatibility

Old tutorials can lead to broken dependency combinations.

## Anti-Pattern 7 --- Hardcoding the Eureka Server URL in Java

Infrastructure locations are configuration.

## Anti-Pattern 8 --- Exposing Eureka Carelessly

Discovery infrastructure is operationally sensitive.

## Anti-Pattern 9 --- Adding Eureka to Kubernetes Automatically

First determine whether platform-native discovery already solves the
requirement.

## Anti-Pattern 10 --- No Timeout/Resilience Because "Eureka Handles It"

Eureka does not make downstream calls reliable.

## Anti-Pattern 11 --- No Logs/Observability

Discovery failures still need diagnosis.

## Anti-Pattern 12 --- Using `localhost` Incorrectly in Containers

Each container has its own localhost.

------------------------------------------------------------------------

# Practical Competency Checkpoints

A learner completing this tree should be able to:

-   [ ] Explain the hardcoded service-location problem
-   [ ] Define service discovery
-   [ ] Define service registry
-   [ ] Explain registration
-   [ ] Explain logical service identity
-   [ ] Distinguish service from service instance
-   [ ] Explain what Eureka is
-   [ ] Explain Eureka Server
-   [ ] Explain Eureka Client
-   [ ] Explain what Eureka does not do
-   [ ] Check Spring Boot/Spring Cloud compatibility
-   [ ] Create a Eureka Server Spring Boot application
-   [ ] Configure a standalone educational Eureka server
-   [ ] Start and verify the Eureka dashboard
-   [ ] Add Eureka client support to a Spring Boot service
-   [ ] Configure `spring.application.name`
-   [ ] Configure the Eureka server location
-   [ ] Verify client registration
-   [ ] Register multiple logical services
-   [ ] Explain registration lifecycle
-   [ ] Explain heartbeat/lease concepts
-   [ ] Explain why failure detection is not instantaneous
-   [ ] Explain registry staleness
-   [ ] Explain Eureka self-preservation at a high level
-   [ ] Explain registry fetching/caching
-   [ ] Discover a service by logical name
-   [ ] Explain client-side load balancing
-   [ ] Use discovery with a Spring HTTP client
-   [ ] Replace a hardcoded downstream URL
-   [ ] Run multiple instances under one service name
-   [ ] Observe request distribution
-   [ ] Stop an instance and observe behavior
-   [ ] Explain what happens if Eureka becomes unavailable
-   [ ] Explain Eureka server high availability at a high level
-   [ ] Connect health information with registration
-   [ ] Externalize Eureka configuration
-   [ ] Run Eureka and clients with Docker Compose
-   [ ] Understand container hostname vs localhost
-   [ ] Troubleshoot startup timing
-   [ ] Use dashboard and logs for troubleshooting
-   [ ] Diagnose common registration problems
-   [ ] Diagnose common discovery problems
-   [ ] Use consistent service naming
-   [ ] Explain why discovery does not replace resilience
-   [ ] Explain why discovery does not replace observability
-   [ ] Explain why discovery does not replace configuration management
-   [ ] Explain why discovery does not replace security
-   [ ] Explain how Eureka integrates conceptually with Spring Cloud
    Gateway
-   [ ] Explain discovery-based gateway routing
-   [ ] Compare Eureka with Kubernetes service discovery
-   [ ] Explain when Eureka may be unnecessary
-   [ ] Explain how discovery supports deployment/scaling
-   [ ] Be ready to continue into Spring Cloud Gateway

------------------------------------------------------------------------

# Suggested Practice Progression

``` text
1. Start previous two-service microservices project
        │
        ▼
2. Confirm Service A calls fixed Service B URL
        │
        ▼
3. Explain why fixed URL becomes a problem
        │
        ▼
4. Create discovery-server Spring Boot app
        │
        ▼
5. Add Eureka Server support
        │
        ▼
6. Configure standalone server
        │
        ▼
7. Start Eureka Server
        │
        ▼
8. Open/inspect dashboard
        │
        ▼
9. Add Eureka client to Profile Service
        │
        ▼
10. Set logical application name
        │
        ▼
11. Configure registry location
        │
        ▼
12. Verify Profile Service registration
        │
        ▼
13. Add Message Service as Eureka client
        │
        ▼
14. Verify both services
        │
        ▼
15. Inspect application vs instance identity
        │
        ▼
16. Replace fixed Profile Service URL
        │
        ▼
17. Call PROFILE-SERVICE by logical identity
        │
        ▼
18. Verify REST request still works
        │
        ▼
19. Start second Profile Service instance
        │
        ▼
20. Verify both instances in Eureka
        │
        ▼
21. Observe load-balanced/distributed calls
        │
        ▼
22. Stop one instance
        │
        ▼
23. Observe lease/registry transition
        │
        ▼
24. Confirm remaining instance can serve calls
        │
        ▼
25. Stop Eureka temporarily
        │
        ▼
26. Observe existing-client behavior
        │
        ▼
27. Restart Eureka and observe recovery
        │
        ▼
28. Containerize discovery server
        │
        ▼
29. Run all services with Docker Compose
        │
        ▼
30. Fix container hostname/config issues
        │
        ▼
31. Document why gateway is now useful
        │
        ▼
32. Continue to Spring Cloud Gateway
```

------------------------------------------------------------------------

# Capstone --- Add Eureka to the Microservices Application

## Objective

Take the previous architecture:

``` text
Message Service
      │
      │ fixed/configured URL
      ▼
Profile Service
```

and transform it into:

``` text
                 Eureka Server
                 ▲           ▲
                 │           │
          Message Service  Profile Service
                 │
                 │ discover PROFILE-SERVICE
                 └────────────────────►
```

Then scale Profile Service to multiple instances.

------------------------------------------------------------------------

## Phase 1 --- Record the Before State

Document:

``` text
Message Service port
Profile Service port
configured Profile Service URL
current REST call
```

Example problem:

``` text
PROFILE_SERVICE_URL=http://localhost:8082
```

Ask:

> What happens if Profile Service moves to port 8083?

------------------------------------------------------------------------

## Phase 2 --- Create Discovery Server

Create:

``` text
discovery-server/
```

Keep it separate from business services.

Add appropriate Eureka Server support and compatible Spring Cloud
dependency management.

------------------------------------------------------------------------

## Phase 3 --- Configure Discovery Server

Configure:

``` text
application name
server port
standalone registry behavior
```

Externalize environment-specific values where useful.

------------------------------------------------------------------------

## Phase 4 --- Verify Empty Registry

Start Eureka.

Confirm:

``` text
Eureka running
business services not yet registered
```

This gives a clean baseline.

------------------------------------------------------------------------

## Phase 5 --- Register Profile Service

Add Eureka client support.

Configure:

``` text
spring.application.name=PROFILE-SERVICE
```

and the registry location.

Start it.

Verify:

``` text
Eureka
└── PROFILE-SERVICE
    └── instance
```

------------------------------------------------------------------------

## Phase 6 --- Register Message Service

Configure:

``` text
spring.application.name=MESSAGE-SERVICE
```

Start it.

Verify:

``` text
Eureka
├── MESSAGE-SERVICE
└── PROFILE-SERVICE
```

------------------------------------------------------------------------

## Phase 7 --- Replace Physical Address

Before:

``` text
Message Service
      │
      ▼
localhost:8082
```

After:

``` text
Message Service
      │
      ▼
PROFILE-SERVICE
      │
      ▼
discovery/load balancing
      │
      ▼
physical Profile instance
```

Use an appropriate modern Spring discovery-aware HTTP client
configuration.

------------------------------------------------------------------------

## Phase 8 --- Verify End-to-End Call

Call Message Service.

It should:

``` text
receive request
    │
    ▼
discover PROFILE-SERVICE
    │
    ▼
call Profile Service
    │
    ▼
map response DTO
    │
    ▼
return useful result
```

------------------------------------------------------------------------

## Phase 9 --- Add Second Profile Instance

Run:

``` text
PROFILE-SERVICE
├── instance on one port
└── instance on another port
```

Both should use the same logical application name.

------------------------------------------------------------------------

## Phase 10 --- Verify Registry

The dashboard should conceptually show:

``` text
PROFILE-SERVICE
├── Instance A
└── Instance B
```

Explain why this is **one service with two instances**, not two
services.

------------------------------------------------------------------------

## Phase 11 --- Observe Distribution

Make repeated calls.

Use instance-specific logging or a temporary diagnostic response to
identify the serving instance.

Observe how discovery/load-balancing integration distributes calls.

------------------------------------------------------------------------

## Phase 12 --- Failure Drill

Stop Instance A.

Observe:

``` text
registry state
client calls
remaining instance
logs
```

Do not expect failure recognition to be mathematically instantaneous.

------------------------------------------------------------------------

## Phase 13 --- Eureka Failure Drill

Temporarily stop the discovery server.

Observe separately:

``` text
existing service behavior
new registration behavior
registry refresh behavior
```

Write down what actually happens rather than assuming.

------------------------------------------------------------------------

## Phase 14 --- Restart and Recover

Restart Eureka.

Observe client/server logs and registry recovery.

------------------------------------------------------------------------

## Phase 15 --- Dockerize

Build images for:

``` text
discovery-server
message-service
profile-service
```

Run with Compose.

------------------------------------------------------------------------

## Phase 16 --- Container Networking

Ensure clients use the correct configured Eureka hostname.

Remember:

``` text
inside message-service container:

localhost
=
message-service container

not
=
discovery-server
```

------------------------------------------------------------------------

## Phase 17 --- Re-run Scaling Exercise

Run multiple Profile Service instances if the local/container setup
supports the intended exercise.

Verify registration and discovery behavior again.

------------------------------------------------------------------------

## Phase 18 --- Document the Improvement

Before:

``` text
Message Service must know
Profile Service physical address
```

After:

``` text
Message Service knows
PROFILE-SERVICE logical identity

Eureka tracks
available instances
```

------------------------------------------------------------------------

## Phase 19 --- Identify Remaining Problem

The frontend still may need to know:

``` text
Message Service address
Profile Service address
future services...
```

This creates the next architectural problem.

------------------------------------------------------------------------

## Phase 20 --- Design the Next Step

Draw:

``` text
                 Frontend
                    │
                    ▼
           Spring Cloud Gateway
                    │
                    │ discovery
                    ▼
                Eureka
              ┌─────┴─────┐
              ▼           ▼
          MESSAGE       PROFILE
          SERVICE       SERVICE
```

This is the handoff to the **Spring Cloud Gateway Skill Tree**.

------------------------------------------------------------------------

# Interview Readiness

Be able to answer:

-   [ ] What problem does service discovery solve?
-   [ ] What is a service registry?
-   [ ] What is Eureka?
-   [ ] What is a Eureka Server?
-   [ ] What is a Eureka Client?
-   [ ] What is service registration?
-   [ ] What is service discovery?
-   [ ] Logical service name vs host/port?
-   [ ] Service vs service instance?
-   [ ] What does `spring.application.name` represent?
-   [ ] Why can multiple instances use the same application name?
-   [ ] Why does Eureka use heartbeats/leases?
-   [ ] Why isn't failure detection instantaneous?
-   [ ] What does stale registry information mean?
-   [ ] What is Eureka self-preservation at a high level?
-   [ ] Do clients necessarily contact Eureka for every business
    request?
-   [ ] What is client-side load balancing?
-   [ ] How does Spring Cloud LoadBalancer relate to discovery?
-   [ ] Why replace hardcoded downstream URLs?
-   [ ] What happens when one of several instances fails?
-   [ ] What happens if the Eureka server fails?
-   [ ] Why might production Eureka infrastructure need redundancy?
-   [ ] Does Eureka guarantee that a discovered request succeeds?
-   [ ] Does Eureka replace timeouts/circuit breakers?
-   [ ] Does Eureka replace an API gateway?
-   [ ] Eureka vs Spring Cloud Gateway?
-   [ ] Eureka vs Spring Cloud Config?
-   [ ] Eureka vs Kubernetes Service/DNS discovery?
-   [ ] Is Eureka always necessary in Kubernetes?
-   [ ] How does Docker networking affect Eureka configuration?
-   [ ] Why is `localhost` often wrong between containers?
-   [ ] How do you troubleshoot a service that does not register?
-   [ ] How do you troubleshoot a service that registers but cannot be
    called?
-   [ ] Why is Spring Boot/Spring Cloud compatibility important?
-   [ ] Why does Eureka lead naturally into Spring Cloud Gateway?

------------------------------------------------------------------------

# Relationship to Existing Skill Trees

Primary prerequisite chain:

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
 ▼
Eureka Service Discovery
```

Immediate next step:

``` text
Eureka Service Discovery
          │
          ▼
Spring Cloud Gateway
```

Combined architecture:

``` text
                Client
                  │
                  ▼
        Spring Cloud Gateway
                  │
                  ▼
             Eureka
          ┌───────┴───────┐
          ▼               ▼
   MESSAGE-SERVICE   PROFILE-SERVICE
```

Docker path:

``` text
Eureka
  │
  ▼
Docker
  │
  ▼
Docker Compose
  │
  ▼
Multi-service local environment
```

Resilience path:

``` text
Eureka
  │
  ▼
"Where is the service?"
  │
  ▼
Resilience4j
  │
  ▼
"What if the discovered service fails?"
```

Observability path:

``` text
Eureka
  │
  ▼
Dynamic service instances
  │
  ▼
Logs / Metrics / Tracing
  │
  ▼
ELK + future observability
```

Kubernetes comparison:

``` text
Traditional Spring Cloud path

Microservices
    │
    ▼
Eureka
    │
    ▼
Service Discovery


Kubernetes path

Microservices
    │
    ▼
Kubernetes Services / DNS
    │
    ▼
Service Discovery
```

------------------------------------------------------------------------

# Future Branches

``` text
                  Eureka
                    │
                    ▼
          Spring Cloud Gateway
                    │
          ┌─────────┼─────────┐
          ▼         ▼         ▼
       Config    Resilience  Observability
          │         │         │
          ▼         ▼         ▼
 Spring Cloud   Resilience4j  Tracing/
    Config                    Metrics
```

Possible later topics:

-   [ ] Spring Cloud Gateway
-   [ ] Spring Cloud Config
-   [ ] Resilience4j & Fault Tolerance
-   [ ] Distributed Observability & Tracing
-   [ ] Spring Security for Microservices
-   [ ] Kubernetes Service Discovery
-   [ ] Cloud-native Service Discovery
-   [ ] Advanced Eureka Operations / High Availability

------------------------------------------------------------------------

# Mastery Progression

``` text
"I can call another service by URL"
              │
              ▼
"I understand why fixed URLs become fragile"
              │
              ▼
"I understand service discovery"
              │
              ▼
"I understand a service registry"
              │
              ▼
"I can run a Eureka Server"
              │
              ▼
"I can register a Spring Boot client"
              │
              ▼
"I understand logical service names"
              │
              ▼
"I can register multiple services"
              │
              ▼
"I can discover a service by name"
              │
              ▼
"I can replace a hardcoded downstream address"
              │
              ▼
"I can run multiple instances of one service"
              │
              ▼
"I understand discovery + load balancing"
              │
              ▼
"I understand heartbeats, leases, and stale state"
              │
              ▼
"I can observe instance failure/recovery"
              │
              ▼
"I can run Eureka with Docker Compose"
              │
              ▼
"I know Eureka does not replace resilience/security"
              │
              ▼
"I can compare Eureka with Kubernetes discovery"
              │
              ▼
"I am ready to add Spring Cloud Gateway"
```

------------------------------------------------------------------------

# Mastery Standard

> **Can I independently explain the service-discovery problem, create
> and configure a Eureka Server, register multiple Spring Boot services
> as Eureka clients, distinguish logical services from physical
> instances, replace a hardcoded service address with discovery by
> logical service name, use discovery with client-side load balancing,
> run multiple instances of one service, explain
> heartbeats/leases/registry staleness/self-preservation at an
> appropriate developer level, observe instance failure and recovery,
> troubleshoot registration/discovery problems, run the system through
> Docker Compose, explain why Eureka does not replace resilience,
> security, configuration, or observability, compare Eureka with
> Kubernetes-native service discovery, and clearly explain how Eureka
> will support the next Spring Cloud Gateway architecture?**

Final mental model:

``` text
DYNAMIC MICROSERVICES

                   Eureka Server
                  Service Registry
                ▲        ▲        ▲
                │        │        │
             register  register  register
                │        │        │
            Service A  Service B  Service B
                        Instance   Instance
                           1          2

Consumer asks:

"Where is SERVICE-B?"
  │
  ▼
Discovery information
  │
  ▼
Load-balancing decision
 ┌─┴─┐
 ▼   ▼
 B1  B2

Physical instances may:

  - start
  - stop
  - restart
  - move
  - scale

but callers depend primarily on:

SERVICE-B logical identity

Eureka solves:

"WHERE is the service?"

It does NOT solve:

"How should external requests be routed?"
  │
  ▼
Spring Cloud Gateway

"What if the service fails?"
  │
  ▼
Resilience4j / Fault Tolerance

"How do services share configuration?"
  │
  ▼
Spring Cloud Config

"Why did this distributed request fail?"
  │
  ▼
Logs / Metrics / Tracing / ELK

Next progression:

Spring Cloud & Microservices Fundamentals
  │
  ▼
Eureka Service Discovery
  │
  ▼
Spring Cloud Gateway
  │
  ▼
Combined Microservices Architecture
```
