# Spring Cloud Gateway --- Full-Stack Developer Skill Tree

> **Goal:** Learn Spring Cloud Gateway at the developer level:
> understand why an API gateway exists, create a gateway as a separate
> Spring Boot/Spring Cloud application, define routes, predicates, and
> filters, integrate routing with Eureka service discovery, centralize
> client-facing access to microservices, handle paths/headers/CORS and
> common cross-cutting concerns, troubleshoot routing failures, and
> prepare the system for later security, resilience, observability,
> Docker, Kubernetes, and cloud deployment work.
>
> **Target level:** A full-stack Java/Spring developer who can
> independently place Spring Cloud Gateway in front of a small
> Eureka-discovered microservices system and explain what belongs at the
> gateway versus inside individual services.
>
> **Primary prerequisites:** **Spring Cloud & Microservices
> Fundamentals** and **Eureka Service Discovery**
>
> **Supporting prerequisites:** Spring Boot & Initializr, HTTP/HTTPS &
> REST API Fundamentals, Java, Maven, Spring configuration, environment
> variables, REST service design, CORS fundamentals, Docker basics, and
> basic testing.
>
> **Downstream connections:** Spring Security for microservices,
> OAuth2/OIDC/JWT, Resilience4j, Spring Cloud Config, distributed
> observability/tracing, Docker, Kubernetes, AWS, CI/CD, and JMeter.
>
> **Scope boundary:** This tree focuses on Spring Cloud Gateway's
> developer-facing routing and filtering responsibilities. It does not
> attempt to teach deep reactive programming, advanced Netty internals,
> enterprise API-management platforms, OAuth authorization-server
> implementation, Kubernetes ingress administration, service meshes, or
> production gateway-cluster operations.
>
> **Capstone:** Extend the Eureka microservices capstone by adding a
> Spring Cloud Gateway as the single frontend-facing backend entry
> point. Route `/api/messages/**` and `/api/profiles/**` to logical
> Eureka services, use predicates and filters, rewrite paths where
> necessary, handle CORS centrally where appropriate, add
> request/correlation information, test failure behavior and multiple
> service instances, containerize the gateway with the rest of the
> system, and document which future concerns belong to security,
> resilience, observability, and Kubernetes rather than the gateway
> fundamentals tree.

------------------------------------------------------------------------

# Skill Tree Overview

``` text
Spring Cloud & Microservices Fundamentals
                 │
                 ▼
       Eureka Service Discovery
                 │
                 ▼
        Spring Cloud Gateway
                 │
       ┌─────────┼─────────┐
       ▼         ▼         ▼
     Routes   Predicates  Filters
       │         │         │
       └─────────┼─────────┘
                 ▼
        Discovery-Based Routing
                 │
                 ▼
          Central API Entry
                 │
       ┌─────────┼──────────┐
       ▼         ▼          ▼
     CORS     Security   Observability
              [later]      [later]
                 │
                 ▼
        Docker / Kubernetes / AWS
```

Core architecture:

``` text
BEFORE GATEWAY

Frontend
 ├────────► MESSAGE-SERVICE
 ├────────► PROFILE-SERVICE
 └────────► future services

Client must know multiple backend locations.


AFTER GATEWAY

                    Frontend
                       │
                       ▼
              Spring Cloud Gateway
                       │
                  asks Eureka
                       │
                       ▼
                  Eureka Server
                 ┌─────┴─────┐
                 ▼           ▼
          MESSAGE-SERVICE PROFILE-SERVICE
```

------------------------------------------------------------------------

# Tier 0 --- Why an API Gateway Exists

## 1. Direct Client-to-Service Architecture

Without a gateway:

``` text
Frontend
├── http://message-service/...
├── http://profile-service/...
└── http://order-service/...
```

The client becomes aware of internal service topology.

## 2. Growth Problem

As services increase, clients may need to track:

``` text
addresses
ports
API paths
authentication behavior
CORS behavior
service changes
```

## 3. Gateway Pattern

Introduce one controlled entry point:

``` text
Client
  │
  ▼
Gateway
  │
  ├──► Service A
  ├──► Service B
  └──► Service C
```

## 4. Main Principle

> The client should usually interact with a stable external API surface
> rather than understand every internal service instance.

------------------------------------------------------------------------

# Tier 1 --- Eureka vs Gateway

## 5. Eureka

Answers:

``` text
"Where are the instances of PROFILE-SERVICE?"
```

## 6. Gateway

Answers:

``` text
"Where should this incoming request be routed?"
```

## 7. Combined

``` text
Gateway
   │
   │ find PROFILE-SERVICE
   ▼
Eureka
   │
   ▼
available instances
```

Then the gateway forwards the request.

------------------------------------------------------------------------

# Tier 2 --- What Spring Cloud Gateway Is

## 8. Gateway Application

Spring Cloud Gateway is typically its own Spring application.

Example:

``` text
api-gateway/
```

## 9. Responsibility

It handles gateway concerns such as:

``` text
routing
request matching
request/response filtering
path manipulation
cross-cutting gateway behavior
```

## 10. Keep Business Logic Out

Do not turn the gateway into:

``` text
Controller
Service
Repository
Business Rules
Database Access
```

for every domain.

Business behavior belongs in the owning services.

------------------------------------------------------------------------

# Tier 3 --- Reactive Foundation Awareness

## 11. Reactive Architecture

Spring Cloud Gateway is built around a reactive/non-blocking web stack.

## 12. Developer-Level Requirement

Understand enough to avoid treating the gateway exactly like a
traditional Spring MVC application.

## 13. Scope Boundary

You do **not** need to master reactive programming or Reactor internals
before learning ordinary gateway routing.

## 14. Important Habit

Avoid casually inserting blocking work into gateway request processing.

------------------------------------------------------------------------

# Tier 4 --- Version Compatibility

## 15. Spring Boot and Spring Cloud

Use compatible Spring Boot and Spring Cloud versions.

## 16. Dependency Management

Use the appropriate Spring Cloud dependency-management/BOM mechanism.

## 17. Modern Documentation

Gateway APIs/configuration evolve.

Check documentation appropriate to the project's Spring generation
rather than copying an old tutorial blindly.

------------------------------------------------------------------------

# Tier 5 --- Create the Gateway Project

## 18. Separate Application

Create:

``` text
api-gateway/
├── pom.xml
└── src/
```

## 19. Dependencies

Add the appropriate Spring Cloud Gateway support.

If using Eureka-based routing, also include the needed discovery client
integration.

## 20. Application Name

Use a clear logical name such as:

``` text
API-GATEWAY
```

## 21. Port

Give the gateway its own external-facing development port.

Example:

``` text
Gateway → 8080
```

------------------------------------------------------------------------

# Tier 6 --- Gateway Configuration

## 22. Externalized Configuration

Keep environment-specific values in Spring configuration/environment
mechanisms.

## 23. Configuration Areas

Typical gateway configuration includes:

``` text
application identity
server port
Eureka location
routes
CORS
logging
```

## 24. YAML Awareness

Gateway route configuration is often naturally represented
hierarchically in YAML.

Understand the structure rather than memorizing indentation blindly.

------------------------------------------------------------------------

# Tier 7 --- Route

## 25. Route Definition

A route describes where a matching request should go.

Mental model:

``` text
Route
├── ID
├── destination URI
├── predicates
└── filters
```

## 26. Route ID

Give routes meaningful names.

Example:

``` text
message-service-route
profile-service-route
```

## 27. Destination

The route target may be:

``` text
fixed URI
or
logical discovered service
```

------------------------------------------------------------------------

# Tier 8 --- Predicate

## 28. Predicate

A predicate determines whether a route matches an incoming request.

Concept:

``` text
IF request matches condition
THEN use this route
```

## 29. Path Predicate

A common example:

``` text
/api/messages/**
```

## 30. Other Predicate Awareness

Gateway routing can consider properties such as:

``` text
path
HTTP method
host
header
query parameter
```

Learn the most useful ones first.

------------------------------------------------------------------------

# Tier 9 --- Filter

## 31. Filter

A filter modifies or processes a request/response around routing.

Concept:

``` text
Incoming Request
       │
       ▼
   Gateway Filter
       │
       ▼
 Downstream Service
       │
       ▼
   Gateway Filter
       │
       ▼
Outgoing Response
```

## 32. Uses

Filters can support:

``` text
path changes
headers
logging context
cross-cutting behavior
```

## 33. Separation

Predicates answer:

``` text
"Does this route match?"
```

Filters answer:

``` text
"What should happen to the request/response?"
```

------------------------------------------------------------------------

# Tier 10 --- First Static Route

Start simple before adding discovery.

``` text
Client
   │
   ▼
Gateway
   │
   ▼
http://localhost:8081
   │
   ▼
MESSAGE-SERVICE
```

Create one route using a fixed local destination.

This proves that routing works independently of Eureka.

------------------------------------------------------------------------

# Tier 11 --- Path Routing

Example external API:

``` text
GET /api/messages
```

Gateway matches:

``` text
/api/messages/**
```

and forwards to Message Service.

Test:

``` text
Client
   │
GET /api/messages
   ▼
Gateway
   │
   ▼
Message Service
```

------------------------------------------------------------------------

# Tier 12 --- Multiple Routes

Add Profile Service.

``` text
Gateway
├── /api/messages/** → Message Service
└── /api/profiles/** → Profile Service
```

The frontend now needs only the gateway's address.

------------------------------------------------------------------------

# Tier 13 --- Route Ordering Awareness

## 34. Overlapping Routes

Some predicates can overlap.

## 35. Specificity

Understand which route will match and why.

## 36. Avoid Ambiguity

Design external paths so routing is understandable.

------------------------------------------------------------------------

# Tier 14 --- Path Rewriting

## 37. External vs Internal Path

The gateway's public path does not have to exactly match the downstream
service path.

Example:

``` text
External:
/api/profiles/7

Internal:
/profiles/7
```

## 38. Rewrite

A gateway filter can transform the path before forwarding.

## 39. Purpose

This lets the gateway maintain a stable external API while internal
service paths evolve.

------------------------------------------------------------------------

# Tier 15 --- Strip Prefix Awareness

A common routing need is removing one or more leading path segments.

Concept:

``` text
/public/message/api/messages
            │
            ▼
       /api/messages
```

Understand the behavior and test the actual forwarded path.

------------------------------------------------------------------------

# Tier 16 --- Headers

## 40. Request Headers

The gateway can add, remove, or modify headers.

## 41. Response Headers

It can also affect returned headers.

## 42. Common Uses

Examples:

``` text
correlation/request ID
forwarding metadata
security headers later
diagnostic headers
```

## 43. Caution

Do not blindly trust client-supplied internal/security headers.

------------------------------------------------------------------------

# Tier 17 --- Query Parameters

## 44. Preserve Parameters

Routing should preserve expected query parameters unless intentionally
modified.

Example:

``` text
/api/messages?page=2
```

## 45. Filter Awareness

Gateway filters can manipulate query parameters when needed.

## 46. Contract Discipline

Do not unexpectedly change API semantics at the gateway.

------------------------------------------------------------------------

# Tier 18 --- HTTP Methods

Routes can be constrained by HTTP method when useful.

Example:

``` text
GET /api/messages/**
POST /api/messages/**
```

Do not create unnecessary method-specific complexity if a simpler path
route is sufficient.

------------------------------------------------------------------------

# Tier 19 --- Discovery-Based Routing

Now replace fixed physical destinations.

Before:

``` text
uri: http://localhost:8081
```

Conceptually after:

``` text
uri: load-balanced MESSAGE-SERVICE
```

The gateway uses service discovery/load-balancing integration to locate
instances.

------------------------------------------------------------------------

# Tier 20 --- Eureka Integration

Architecture:

``` text
                 Gateway
                    │
                    ▼
                  Eureka
                 /      \
                ▼        ▼
         MESSAGE-SERVICE PROFILE-SERVICE
```

The gateway registers/discovers as configured and routes to logical
services.

------------------------------------------------------------------------

# Tier 21 --- Logical Destination

## 47. Physical Route

``` text
Gateway → localhost:8081
```

## 48. Logical Route

``` text
Gateway → MESSAGE-SERVICE
```

## 49. Benefit

Service instances may change without requiring the client to know their
physical addresses.

------------------------------------------------------------------------

# Tier 22 --- Multiple Instances

Suppose Eureka contains:

``` text
MESSAGE-SERVICE
├── Instance A
├── Instance B
└── Instance C
```

Gateway routing can work with discovery/load-balancing mechanisms to
select available instances.

------------------------------------------------------------------------

# Tier 23 --- Scale Exercise

Run:

``` text
MESSAGE-SERVICE
├── :8081
└── :8082
```

with the same logical service identity.

Call:

``` text
Client → Gateway → MESSAGE-SERVICE
```

repeatedly.

Use logs or temporary instance identifiers to observe which instance
handles each request.

------------------------------------------------------------------------

# Tier 24 --- Discovery Locator Awareness

Spring Cloud Gateway can integrate with discovery information in ways
that can generate routes based on registered services.

Understand:

``` text
explicit routes
vs
discovery-generated routes
```

## 50. Developer Recommendation

Learn explicit route design first.

Automatic discovery routing is convenient, but public API design should
remain intentional.

------------------------------------------------------------------------

# Tier 25 --- Public API vs Internal Service Name

Avoid forcing external clients to understand internal naming
conventions.

Internal:

``` text
MESSAGE-SERVICE
```

External:

``` text
/api/messages
```

These serve different purposes.

------------------------------------------------------------------------

# Tier 26 --- Global Filters vs Route Filters

## 51. Route-Specific Filter

Applies to selected routes.

## 52. Global Filter

Applies broadly across gateway traffic.

## 53. Decision

Ask:

``` text
Does every request need this?
or
only this route?
```

------------------------------------------------------------------------

# Tier 27 --- Correlation IDs

## 54. Problem

One request may travel:

``` text
Client
  │
  ▼
Gateway
  │
  ▼
Message Service
  │
  ▼
Profile Service
```

## 55. Correlation

The gateway is a useful place to establish or propagate request
correlation information.

``` text
request-id: abc123
```

## 56. Propagation

Downstream services should preserve/use the correlation context
appropriately.

## 57. Observability Connection

This prepares for ELK and distributed tracing.

------------------------------------------------------------------------

# Tier 28 --- Logging

## 58. Gateway Logs

Useful gateway logs can show:

``` text
incoming path
matched route
downstream failure
request identifier
status
```

## 59. Avoid Sensitive Logging

Do not log:

``` text
passwords
tokens
secret headers
sensitive request bodies
```

## 60. Distributed Debugging

Combine gateway logs with downstream service logs using correlation
context.

------------------------------------------------------------------------

# Tier 29 --- CORS Review

## 61. Browser Origin

A frontend such as:

``` text
http://localhost:4200
```

may call a gateway such as:

``` text
http://localhost:8080
```

These are different origins.

## 62. Gateway Advantage

With one backend entry point, CORS can often be managed more coherently
than when the frontend calls many services directly.

------------------------------------------------------------------------

# Tier 30 --- CORS at the Gateway

Configure appropriate allowed:

``` text
origins
methods
headers
credentials behavior
```

for the intended frontend.

## 63. Least Necessary Access

Do not default to:

``` text
allow everything
```

without understanding the consequences.

## 64. Scope

Deep web/API security belongs in later security trees.

------------------------------------------------------------------------

# Tier 31 --- Preflight Requests

Understand browser preflight behavior:

``` text
OPTIONS
```

may occur before the actual cross-origin request.

When debugging:

``` text
"frontend request never reaches service"
```

check whether the preflight failed at the gateway.

------------------------------------------------------------------------

# Tier 32 --- Centralized Cross-Cutting Concerns

The gateway can be a useful location for concerns shared across routes.

Examples:

``` text
routing
correlation
selected logging
selected headers
CORS
authentication enforcement later
rate limiting later
```

But centralization must be intentional.

------------------------------------------------------------------------

# Tier 33 --- What Does NOT Belong in Gateway

Avoid:

``` text
order calculations
message validation business rules
database queries
profile persistence
domain workflows
```

The gateway should not become a new monolith containing every service's
logic.

------------------------------------------------------------------------

# Tier 34 --- Authentication Awareness

Future architecture:

``` text
Client
  │
  ▼
Gateway
  │ authenticate/validate
  ▼
Services
```

The gateway can participate in authentication/security enforcement.

But detailed Spring Security/OAuth2/OIDC/JWT belongs in separate
security trees.

------------------------------------------------------------------------

# Tier 35 --- Authorization Awareness

Authentication asks:

``` text
Who are you?
```

Authorization asks:

``` text
Are you allowed to do this?
```

Do not assume that central gateway authentication eliminates all
downstream authorization requirements.

------------------------------------------------------------------------

# Tier 36 --- Token Propagation Awareness

A gateway may need to propagate trusted authentication context/tokens
downstream.

Concept:

``` text
Client
  │ token
  ▼
Gateway
  │ validated/forwarded context
  ▼
Service
```

Deep implementation belongs in security training.

------------------------------------------------------------------------

# Tier 37 --- Rate Limiting Awareness

## 65. Problem

A client can send excessive traffic.

## 66. Gateway Opportunity

A gateway is a natural enforcement point for request-rate policies.

## 67. Scope

Understand the purpose and architecture.

Detailed production rate limiting/backing stores can be a later advanced
gateway/security/resilience topic.

------------------------------------------------------------------------

# Tier 38 --- Resilience Awareness

Gateway calls can fail because downstream services:

``` text
timeout
crash
overload
return errors
```

Discovery does not remove these failures.

## 68. Future Integration

Resilience4j/fault-tolerance patterns may later cover:

``` text
timeouts
retries
circuit breakers
fallback decisions
```

------------------------------------------------------------------------

# Tier 39 --- Gateway Failure

The gateway itself becomes important infrastructure.

If clients depend on:

``` text
Client → Gateway → Services
```

then gateway unavailability can affect the entire external API.

Understand why production deployments often run multiple gateway
instances.

Detailed high-availability operations are deferred.

------------------------------------------------------------------------

# Tier 40 --- Error Responses

## 69. Routing Failure

Possible causes:

``` text
no route
no service instance
connection failure
timeout
downstream 4xx/5xx
filter failure
```

## 70. Useful Errors

Do not expose unnecessary internal infrastructure details to external
clients.

## 71. Distinguish Source

Learn to determine whether an error originated from:

``` text
client
gateway
discovery
downstream service
```

------------------------------------------------------------------------

# Tier 41 --- 404 Troubleshooting

If gateway returns 404, inspect:

``` text
requested path
route predicate
route order
path rewrite
downstream path
```

A Eureka registration problem is not the only possible cause.

------------------------------------------------------------------------

# Tier 42 --- 503 / No Instance Troubleshooting

If no downstream service is available:

``` text
Gateway
   │
   ▼
Eureka
   │
   ▼
no usable instance
```

check:

``` text
service registration
logical service name
instance health
discovery configuration
network
```

------------------------------------------------------------------------

# Tier 43 --- 500 Troubleshooting

Inspect:

``` text
gateway logs
filter exceptions
configuration
downstream response
```

Do not assume every 500 came from the downstream business service.

------------------------------------------------------------------------

# Tier 44 --- Timeout Troubleshooting

Trace the path:

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

A slow dependency several hops away may cause client-visible gateway
latency.

This reinforces the need for distributed observability.

------------------------------------------------------------------------

# Tier 45 --- Testing Routes

## 72. Basic Route Test

Verify:

``` text
input path
→ expected route
→ expected service
→ expected response
```

## 73. Negative Tests

Verify behavior for:

``` text
unknown path
unavailable service
invalid method
bad request
```

## 74. Integration Testing

Gateway behavior is especially valuable to test at integration
boundaries.

------------------------------------------------------------------------

# Tier 46 --- JMeter Connection

JMeter can target the gateway instead of every service individually.

``` text
JMeter
  │
  ▼
Gateway
  │
  ▼
Microservices
```

This more closely resembles external client traffic.

Measure:

``` text
response time
throughput
errors
percentiles
```

Then use logs/metrics to identify downstream bottlenecks.

------------------------------------------------------------------------

# Tier 47 --- Dockerizing Gateway

Create a gateway image:

``` text
api-gateway
    │
    ▼
Docker Image
```

Run with:

``` text
discovery-server
api-gateway
message-service
profile-service
database
```

------------------------------------------------------------------------

# Tier 48 --- Docker Compose Architecture

``` text
Docker Compose
│
├── discovery-server
├── api-gateway
├── message-service
├── profile-service
└── postgres
```

External clients should generally use the gateway's exposed port for the
capstone.

Internal services need not all be exposed to the host merely for
frontend access.

------------------------------------------------------------------------

# Tier 49 --- Container Networking

Inside containers:

``` text
localhost
```

means the current container.

Gateway/Eureka/service configuration must use appropriate network
hostnames.

The gateway should discover or address services through
container/network-aware configuration.

------------------------------------------------------------------------

# Tier 50 --- Frontend Integration

Before:

``` text
Angular/React
├── Message Service URL
└── Profile Service URL
```

After:

``` text
Angular/React
      │
      ▼
Gateway base URL
```

Frontend service modules can use stable public API paths:

``` text
/api/messages
/api/profiles
```

------------------------------------------------------------------------

# Tier 51 --- Dev Proxy Connection

During frontend development, an Angular/React dev server may proxy API
requests to the gateway.

Concept:

``` text
Browser
  │
  ▼
Frontend Dev Server
  │ /api
  ▼
Spring Cloud Gateway
  │
  ▼
Microservices
```

This builds directly on existing frontend proxy knowledge.

------------------------------------------------------------------------

# Tier 52 --- Gateway vs Reverse Proxy

Understand the broad similarity:

``` text
receive request
choose destination
forward request
```

Spring Cloud Gateway adds Spring-oriented programmable routing/filtering
and integration with the surrounding Spring ecosystem.

Do not assume every system needs Spring Cloud Gateway instead of another
proxy/gateway technology.

------------------------------------------------------------------------

# Tier 53 --- Gateway vs Load Balancer

A load balancer primarily distributes traffic among instances.

An API gateway commonly performs higher-level API routing/cross-cutting
behavior.

Concept:

``` text
Gateway:
"/messages" vs "/profiles"

Load balancing:
"which MESSAGE-SERVICE instance?"
```

The responsibilities can coexist.

------------------------------------------------------------------------

# Tier 54 --- Gateway vs Eureka

``` text
Eureka:
service registry/discovery

Gateway:
request routing/filtering
```

They complement each other but solve different problems.

------------------------------------------------------------------------

# Tier 55 --- Gateway vs Kubernetes Ingress

## 75. Kubernetes Ingress/Gateway Layer

Platform-level mechanisms can route traffic into cluster workloads.

## 76. Spring Cloud Gateway

Application-level gateway logic can provide Spring-aware API
routing/filtering.

## 77. Possible Architecture

``` text
Internet
   │
   ▼
Kubernetes Ingress / Gateway layer
   │
   ▼
Spring Cloud Gateway
   │
   ▼
Services
```

## 78. Architecture Judgment

Do not add both layers without a reason.

------------------------------------------------------------------------

# Tier 56 --- Gateway and Kubernetes Service Discovery

In Kubernetes, platform-native service discovery may replace Eureka.

Possible future architecture:

``` text
Client
  │
  ▼
Gateway
  │
  ▼
Kubernetes Service/DNS
  │
  ▼
Pods
```

The underlying concepts learned here remain useful even when the
discovery implementation changes.

------------------------------------------------------------------------

# Tier 57 --- AWS Connection

A cloud-hosted gateway architecture may eventually sit in front of
services running on:

``` text
EC2
containers
Kubernetes/EKS
```

AWS also offers native load-balancing/API-management technologies.

The AWS fundamentals tree should introduce alternatives without
requiring this Spring-specific gateway in every deployment.

------------------------------------------------------------------------

# Tier 58 --- Observability Connection

A gateway is a valuable observation point for:

``` text
request count
route
status
latency
errors
correlation IDs
```

But it cannot explain all downstream behavior alone.

Future observability should connect:

``` text
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

with logs, metrics, and traces.

------------------------------------------------------------------------

# Tier 59 --- ELK Connection

A dedicated ELK / Elastic Stack skill tree, covered later in this
curriculum, is where gateway logs can feed into centralized logging
alongside service logs.

``` text
Gateway logs ─┐
Service A logs├──► ELK
Service B logs┘
```

Correlation IDs make cross-service searching much more useful.

------------------------------------------------------------------------

# Tier 60 --- Spring Cloud Config Connection

As the architecture grows:

``` text
gateway routes/config
service config
discovery config
environment config
```

become a configuration-management concern.

Spring Cloud Config can become a later tree.

Do not overload Eureka or Gateway with configuration-management
responsibilities.

------------------------------------------------------------------------

# Tier 61 --- Security Boundaries

The gateway is security-sensitive because it is a major entry point.

Future security work should consider:

``` text
TLS
authentication
authorization
token validation
token propagation
CORS
rate limiting
header trust
secret management
service-to-service security
```

Do not interpret "behind the gateway" as automatically trusted.

------------------------------------------------------------------------

# Tier 62 --- Common Gateway Anti-Patterns

## Anti-Pattern 1 --- Gateway as New Monolith

Do not move all business logic into gateway filters.

## Anti-Pattern 2 --- Client Still Calls Every Service Directly

If the architecture intends a gateway boundary, use it consistently for
the intended external APIs.

## Anti-Pattern 3 --- Hardcoded Physical Instances

Use discovery where the architecture requires dynamic service locations.

## Anti-Pattern 4 --- Expose Internal Service Names as Public API Accidentally

Design stable external paths intentionally.

## Anti-Pattern 5 --- Route Everything Automatically Without API Design

Discovery-generated routes are not a substitute for deliberate public
contracts.

## Anti-Pattern 6 --- Allow-All CORS by Default

Configure only what the frontend requires.

## Anti-Pattern 7 --- Trust Client-Supplied Internal Headers

Treat security/correlation headers carefully.

## Anti-Pattern 8 --- Log Tokens and Secrets

Gateway logging can accidentally expose sensitive data.

## Anti-Pattern 9 --- Assume Eureka Makes Calls Reliable

Discovery is not resilience.

## Anti-Pattern 10 --- Assume Gateway Replaces Service Authorization

Services may still need authorization enforcement.

## Anti-Pattern 11 --- Blocking Heavy Work in Gateway

Keep gateway processing appropriate to its reactive architecture.

## Anti-Pattern 12 --- Add Gateway + Ingress + Proxies Without Purpose

Each routing layer should have a clear responsibility.

------------------------------------------------------------------------

# Practical Competency Checkpoints

A learner completing this tree should be able to:

-   [ ] Explain why an API gateway exists
-   [ ] Explain direct-client-to-service drawbacks
-   [ ] Explain Eureka vs Gateway
-   [ ] Define route
-   [ ] Define predicate
-   [ ] Define filter
-   [ ] Explain Spring Cloud Gateway's reactive foundation at a high
    level
-   [ ] Check Spring Boot/Spring Cloud compatibility
-   [ ] Create a separate gateway application
-   [ ] Configure gateway application identity/port
-   [ ] Create a static route
-   [ ] Route by path
-   [ ] Configure multiple routes
-   [ ] Explain route ordering/overlap
-   [ ] Rewrite paths
-   [ ] Strip prefixes where appropriate
-   [ ] Add/remove/modify headers
-   [ ] Preserve/manipulate query parameters
-   [ ] Match HTTP methods where useful
-   [ ] Register/integrate gateway with Eureka
-   [ ] Route to a logical discovered service
-   [ ] Explain discovery/load-balancing behavior
-   [ ] Route to multiple instances of one service
-   [ ] Explain explicit vs discovery-generated routes
-   [ ] Separate public API paths from internal service names
-   [ ] Explain route-specific vs global filters
-   [ ] Establish/propagate correlation IDs
-   [ ] Use gateway logs safely
-   [ ] Configure CORS appropriately
-   [ ] Explain preflight requests
-   [ ] Identify appropriate gateway cross-cutting concerns
-   [ ] Identify business logic that does not belong in gateway
-   [ ] Explain authentication/authorization gateway roles at a high
    level
-   [ ] Explain token propagation at a high level
-   [ ] Explain rate limiting at a high level
-   [ ] Explain resilience integration at a high level
-   [ ] Explain gateway high-availability concerns
-   [ ] Diagnose 404/500/503/timeout routing failures
-   [ ] Test routes and negative cases
-   [ ] Target the gateway with JMeter
-   [ ] Dockerize the gateway
-   [ ] Run Gateway + Eureka + services through Compose
-   [ ] Configure container networking correctly
-   [ ] Point Angular/React at the gateway
-   [ ] Connect frontend dev proxying to gateway
-   [ ] Explain Gateway vs reverse proxy
-   [ ] Explain Gateway vs load balancer
-   [ ] Explain Gateway vs Eureka
-   [ ] Explain Gateway vs Kubernetes Ingress
-   [ ] Explain Kubernetes-native discovery alternatives
-   [ ] Explain AWS/cloud gateway alternatives at a high level
-   [ ] Explain observability at the gateway
-   [ ] Connect gateway logs to ELK
-   [ ] Explain future Spring Cloud Config integration
-   [ ] Explain gateway security boundaries
-   [ ] Recognize common gateway anti-patterns

------------------------------------------------------------------------

# Suggested Practice Progression

``` text
1. Start Eureka capstone system
        │
        ▼
2. Confirm services register with Eureka
        │
        ▼
3. Confirm frontend/client currently knows services
        │
        ▼
4. Create api-gateway Spring project
        │
        ▼
5. Add Gateway dependencies
        │
        ▼
6. Configure gateway port/name
        │
        ▼
7. Create one fixed static route
        │
        ▼
8. Test path predicate
        │
        ▼
9. Add second route
        │
        ▼
10. Test both services through gateway
        │
        ▼
11. Add Eureka discovery integration
        │
        ▼
12. Replace fixed URI with logical service
        │
        ▼
13. Verify discovered routing
        │
        ▼
14. Start multiple service instances
        │
        ▼
15. Observe routed instance distribution
        │
        ▼
16. Add path rewrite/strip-prefix exercise
        │
        ▼
17. Add request header filter
        │
        ▼
18. Add correlation ID behavior
        │
        ▼
19. Configure frontend CORS
        │
        ▼
20. Test OPTIONS/preflight
        │
        ▼
21. Point frontend to gateway only
        │
        ▼
22. Stop one downstream instance
        │
        ▼
23. Observe continued routing
        │
        ▼
24. Stop all instances of one service
        │
        ▼
25. Observe gateway failure response/logs
        │
        ▼
26. Test unknown route
        │
        ▼
27. Test downstream error
        │
        ▼
28. Containerize gateway
        │
        ▼
29. Add gateway to Docker Compose
        │
        ▼
30. Expose gateway as client-facing backend port
        │
        ▼
31. Run full stack through gateway
        │
        ▼
32. Optionally JMeter-test gateway
        │
        ▼
33. Document future security/resilience work
```

------------------------------------------------------------------------

# Capstone --- Gateway + Eureka Microservices Architecture

## Objective

Transform:

``` text
Frontend
 ├──► MESSAGE-SERVICE
 └──► PROFILE-SERVICE
```

into:

``` text
                    Frontend
                       │
                       ▼
              Spring Cloud Gateway
                       │
                       ▼
                    Eureka
                  ┌────┴────┐
                  ▼         ▼
             MESSAGE     PROFILE
             SERVICE     SERVICE
```

------------------------------------------------------------------------

## Phase 1 --- Verify Prerequisite System

Before adding Gateway:

``` text
Eureka Server
├── MESSAGE-SERVICE
└── PROFILE-SERVICE
```

Message/Profile services should already be independently runnable and
discoverable.

------------------------------------------------------------------------

## Phase 2 --- Create Gateway

Create:

``` text
api-gateway/
```

Give it:

``` text
Spring Boot application
Spring Cloud Gateway support
Eureka/discovery support
own configuration
own port
```

------------------------------------------------------------------------

## Phase 3 --- First Static Route

Before using discovery, route:

``` text
/api/messages/**
```

to a known Message Service address.

Verify the gateway concept independently.

------------------------------------------------------------------------

## Phase 4 --- Add Profile Route

Create:

``` text
/api/messages/** → Message Service
/api/profiles/** → Profile Service
```

Test both.

------------------------------------------------------------------------

## Phase 5 --- Register/Connect to Eureka

Configure the gateway for Eureka.

Verify it can obtain service information.

------------------------------------------------------------------------

## Phase 6 --- Convert to Discovery-Based Destinations

Replace physical service destinations with logical service identities.

Concept:

``` text
/api/messages/**
       │
       ▼
MESSAGE-SERVICE
       │
       ▼
Eureka-selected instance
```

------------------------------------------------------------------------

## Phase 7 --- Test Multiple Instances

Run multiple Message Service instances.

``` text
MESSAGE-SERVICE
├── A
└── B
```

Send repeated gateway requests and observe which instance responds.

------------------------------------------------------------------------

## Phase 8 --- Public vs Internal Paths

Design stable public routes.

Example:

``` text
Public:
/api/messages

Internal service identity:
MESSAGE-SERVICE
```

Do not expose internal discovery naming merely because it is convenient.

------------------------------------------------------------------------

## Phase 9 --- Path Rewrite Exercise

Create one route where external and internal paths differ.

Example:

``` text
External:
 /api/profile/7

Internal:
 /profiles/7
```

Use a gateway filter to make the transformation.

------------------------------------------------------------------------

## Phase 10 --- Header Filter

Add a harmless diagnostic/correlation header.

Observe:

``` text
Client
  │
  ▼
Gateway adds/propagates request ID
  │
  ▼
Service logs same request ID
```

------------------------------------------------------------------------

## Phase 11 --- CORS

Configure the gateway for the frontend development origin.

Test:

``` text
Angular/React
     │
     ▼
Gateway
```

including preflight behavior where applicable.

------------------------------------------------------------------------

## Phase 12 --- Frontend Migration

Before:

``` text
frontend message service → backend message URL
frontend profile service → backend profile URL
```

After:

``` text
frontend
   │
   ▼
gateway /api/*
```

The frontend should no longer need individual backend service addresses
for these routes.

------------------------------------------------------------------------

## Phase 13 --- Failure Drill: One Instance

Stop one of multiple Message Service instances.

Observe:

``` text
Eureka
Gateway
remaining instance
client response
logs
```

------------------------------------------------------------------------

## Phase 14 --- Failure Drill: Entire Service

Stop all Profile Service instances.

Call:

``` text
/api/profiles/...
```

Observe and classify the resulting behavior.

Ask:

``` text
Was the route missing?
Was discovery empty?
Did connection fail?
What did the client receive?
```

------------------------------------------------------------------------

## Phase 15 --- Unknown Route

Call:

``` text
/api/does-not-exist
```

Confirm understandable gateway behavior.

------------------------------------------------------------------------

## Phase 16 --- Downstream Error

Make a valid route reach a service that returns a controlled error.

Distinguish:

``` text
gateway routing failure
vs
downstream application error
```

------------------------------------------------------------------------

## Phase 17 --- Docker

Containerize:

``` text
discovery-server
api-gateway
message-service
profile-service
```

Run with PostgreSQL as needed.

------------------------------------------------------------------------

## Phase 18 --- Docker Compose

Target architecture:

``` text
                   Host Browser
                        │
                        ▼
                  exposed gateway
                        │
                        ▼
                 Docker Network
                        │
             ┌──────────┼──────────┐
             ▼          ▼          ▼
          Eureka     Message     Profile

                        │
                        ▼
                    PostgreSQL
```

Expose only the ports needed for development/inspection.

------------------------------------------------------------------------

## Phase 19 --- JMeter Exercise

Point JMeter at:

``` text
Gateway
```

rather than directly at each service.

Test:

``` text
GET /api/messages
GET /api/profiles/{id}
POST /api/messages
```

Observe client-visible performance through the complete routing path.

------------------------------------------------------------------------

## Phase 20 --- Architecture Documentation

Explain:

``` text
What does Eureka do?
What does Gateway do?
What is a route?
What is a predicate?
What is a filter?
Why does frontend use Gateway?
How are service instances selected?
What happens if a service disappears?
What belongs in Gateway?
What belongs in services?
```

------------------------------------------------------------------------

## Phase 21 --- Identify Future Security Work

Document where later security fits:

``` text
Client
  │
  ▼
Gateway
  │ authentication/token validation
  ▼
Services
  │ authorization/service security
```

Do not fake production security inside this fundamentals capstone.

------------------------------------------------------------------------

## Phase 22 --- Identify Future Resilience Work

Document:

``` text
timeouts
retry policy
circuit breakers
fallback behavior
rate limiting
```

for the future Resilience4j/fault-tolerance tree.

------------------------------------------------------------------------

## Phase 23 --- Identify Future Observability Work

Document:

``` text
gateway metrics
service metrics
correlation IDs
distributed traces
central logs
```

and connect them to ELK/future observability trees.

------------------------------------------------------------------------

## Phase 24 --- Final Architecture

``` text
                         Frontend
                            │
                            ▼
                   Spring Cloud Gateway
                            │
                routes / filters / CORS
                            │
                            ▼
                         Eureka
                  ┌─────────┴─────────┐
                  ▼                   ▼
           MESSAGE-SERVICE      PROFILE-SERVICE
            ┌──────────┐         ┌──────────┐
            ▼          ▼         ▼          ▼
        Instance A Instance B Instance A Instance B
                  │                    │
                  └────────┬───────────┘
                           ▼
                    owned persistence
                       as required
```

The learner is now ready to add deeper security, resilience,
configuration, observability, and deployment layers.

------------------------------------------------------------------------

# Interview Readiness

Be able to answer:

-   [ ] What is an API gateway?
-   [ ] Why use an API gateway with microservices?
-   [ ] What problem does Spring Cloud Gateway solve?
-   [ ] Eureka vs Gateway?
-   [ ] What is a route?
-   [ ] What is a route ID?
-   [ ] What is a route destination?
-   [ ] What is a predicate?
-   [ ] What is a Path predicate?
-   [ ] What is a filter?
-   [ ] Predicate vs filter?
-   [ ] What is a global filter?
-   [ ] What is a route-specific filter?
-   [ ] Why rewrite paths?
-   [ ] What does stripping a prefix accomplish?
-   [ ] How can Gateway modify headers?
-   [ ] How does Gateway integrate with Eureka?
-   [ ] Fixed URI vs logical discovered URI?
-   [ ] How does Gateway work with multiple service instances?
-   [ ] What is client-side/discovery-based load balancing?
-   [ ] Explicit routes vs discovery-generated routes?
-   [ ] Why keep public API paths separate from service names?
-   [ ] How can Gateway help with CORS?
-   [ ] What is a browser preflight request?
-   [ ] Why are correlation IDs useful?
-   [ ] What should not be logged at a gateway?
-   [ ] What business logic should not live in Gateway?
-   [ ] Can Gateway participate in authentication?
-   [ ] Does Gateway replace downstream authorization?
-   [ ] What is token propagation?
-   [ ] Why is rate limiting useful at a gateway?
-   [ ] Does Gateway replace resilience patterns?
-   [ ] What happens if Gateway itself fails?
-   [ ] How do you troubleshoot a Gateway 404?
-   [ ] How do you troubleshoot no available service instance?
-   [ ] Gateway vs reverse proxy?
-   [ ] Gateway vs load balancer?
-   [ ] Gateway vs Kubernetes Ingress?
-   [ ] Do you always need Eureka in Kubernetes?
-   [ ] How does Docker networking affect Gateway?
-   [ ] Why should a frontend call Gateway rather than each internal
    service?
-   [ ] How can JMeter test a Gateway-based architecture?
-   [ ] How does Gateway connect to ELK/observability?
-   [ ] Why is Gateway security-sensitive?

------------------------------------------------------------------------

# Relationship to Existing Skill Trees

Main progression:

``` text
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
          │
          ▼
Spring Cloud Gateway
```

Frontend connection:

``` text
Angular / React
      │
      ▼
HTTP Client / Dev Proxy
      │
      ▼
Spring Cloud Gateway
      │
      ▼
Microservices
```

Testing connection:

``` text
JUnit / Mockito
      │
      ▼
Service correctness

JMeter
      │
      ▼
Gateway + distributed-system performance
```

Deployment connection:

``` text
Spring Cloud Gateway
      │
      ▼
Docker
      │
      ▼
Deployment Fundamentals
      │
      ▼
Kubernetes
      │
      ▼
AWS
```

Observability connection:

``` text
Gateway
  │
  ▼
Correlation / Logs / Metrics
  │
  ▼
ELK (later) + Distributed Tracing
```

Security connection:

``` text
Gateway
  │
  ▼
Spring Security
  │
  ▼
OAuth2 / OIDC / JWT
  │
  ▼
Microservice Security
```

Resilience connection:

``` text
Gateway
  │
  ▼
Downstream Failure
  │
  ▼
Resilience4j / Fault Tolerance
```

Configuration connection:

``` text
Gateway + Services
      │
      ▼
Many Configurations
      │
      ▼
Spring Cloud Config
```

------------------------------------------------------------------------

# Recommended Next Branches

The three-tree foundation is now:

``` text
Spring Cloud & Microservices Fundamentals
                 │
                 ▼
       Eureka Service Discovery
                 │
                 ▼
        Spring Cloud Gateway
```

From here, the architecture can branch:

``` text
              Gateway + Eureka
                    │
       ┌────────────┼────────────┐
       ▼            ▼            ▼
   Resilience    Security    Configuration
       │            │            │
       ▼            ▼            ▼
 Resilience4j   Spring Sec.   Cloud Config
                    │
                    ▼
              OAuth2/OIDC/JWT

                    │
                    ▼
               Observability
                    │
                    ▼
             Metrics / Tracing
```

Possible future skill trees:

-   [ ] Resilience4j & Fault Tolerance
-   [ ] Spring Cloud Config
-   [ ] Spring Security
-   [ ] OAuth2 / OpenID Connect / JWT
-   [ ] Microservices Security
-   [ ] Distributed Observability & Tracing
-   [ ] Prometheus & Grafana
-   [ ] API Rate Limiting
-   [ ] Spring Cloud Contract
-   [ ] Event-Driven Microservices
-   [ ] Kafka
-   [ ] RabbitMQ
-   [ ] Kubernetes Ingress / Gateway API
-   [ ] Service Mesh Fundamentals

------------------------------------------------------------------------

# Mastery Progression

``` text
"My frontend calls several services directly"
                 │
                 ▼
"I understand why a gateway is useful"
                 │
                 ▼
"I can create Spring Cloud Gateway"
                 │
                 ▼
"I understand routes"
                 │
                 ▼
"I understand predicates"
                 │
                 ▼
"I understand filters"
                 │
                 ▼
"I can route paths to services"
                 │
                 ▼
"I can rewrite paths and headers"
                 │
                 ▼
"I can connect Gateway to Eureka"
                 │
                 ▼
"I can route by logical service name"
                 │
                 ▼
"I can route to multiple instances"
                 │
                 ▼
"I can manage frontend CORS"
                 │
                 ▼
"I can propagate correlation context"
                 │
                 ▼
"I can troubleshoot routing failures"
                 │
                 ▼
"I can run Gateway in Docker Compose"
                 │
                 ▼
"My frontend uses one backend entry point"
                 │
                 ▼
"I know what belongs in Gateway vs services"
                 │
                 ▼
"I understand the future security,
 resilience, and observability layers"
```

------------------------------------------------------------------------

# Mastery Standard

> **Can I independently explain why an API gateway exists, create and
> configure a Spring Cloud Gateway application, define and troubleshoot
> routes/predicates/filters, route stable public paths to internal
> microservices, rewrite paths and manipulate headers where appropriate,
> integrate Gateway with Eureka so destinations use logical service
> identities instead of hardcoded physical instances, demonstrate
> routing across multiple service instances, configure appropriate CORS
> for an Angular/React frontend, establish/propagate request correlation
> information, distinguish gateway failures from downstream failures,
> containerize and run Gateway + Eureka + services through Docker
> Compose, point the frontend and JMeter at the gateway, explain Gateway
> vs Eureka/load balancers/reverse proxies/Kubernetes Ingress, and
> identify which advanced concerns belong to security, resilience,
> configuration, and observability rather than business logic in the
> gateway?**

Final mental model:

``` text
              CLIENT / FRONTEND
                     │
                     │ stable public API
                     ▼
              SPRING CLOUD GATEWAY
                     │
       ┌─────────────┼─────────────┐
       ▼              ▼             ▼
    ROUTES       PREDICATES      FILTERS
       │              │             │
       └─────────────┼─────────────┘
                     │
                     ▼
              EUREKA DISCOVERY
                     │
          ┌──────────┴──────────┐
          ▼                     ▼
   MESSAGE-SERVICE        PROFILE-SERVICE
    ┌──────────┐            ┌──────────┐
    ▼          ▼            ▼          ▼
Instance   Instance     Instance   Instance
   A          B             A          B
```

Eureka answers:

"WHERE are the service instances?"

Gateway answers:

"WHERE should this incoming request go, and what gateway-level
processing should occur?"

Services answer:

"WHAT business behavior should happen?"

Future layers answer:

Security: "WHO is calling and WHAT may they do?"

Resilience: "WHAT happens when dependencies fail?"

Observability: "WHY was this distributed request slow or broken?"

Configuration: "HOW do many applications receive environment config?"

Complete Spring Cloud progression:

Spring Boot │ ▼ HTTP / REST │ ▼ Spring Cloud & Microservices
Fundamentals │ ▼ Eureka Service Discovery │ ▼ Spring Cloud Gateway │
├────────► Resilience4j ├────────► Spring Cloud Config ├────────►
Security └────────► Observability │ ▼ Docker │ ▼ Kubernetes │ ▼ AWS
