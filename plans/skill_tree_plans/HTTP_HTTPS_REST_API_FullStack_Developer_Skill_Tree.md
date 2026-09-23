# HTTP / HTTPS / REST API Skill Tree — Full-Stack Developer Path

> **Goal:** Understand the web protocol and API concepts that connect browsers/frontends to backend services, independently of Angular, React, or Spring Boot.

> **Target:** Professional full-stack developer competency; advanced specialist topics are awareness-level unless needed for application development.

## Dependency / prerequisite map

```text
Web / networking awareness
        ↓
HTTP fundamentals
        ↓
HTTPS / TLS
        ↓
REST API design
        ↓
Frontend + Backend framework implementations
```

## Skill Tree Overview

### 1. Client–Server & Web Foundations
- [ ] Client vs server
- [ ] Request/response model
- [ ] Host, domain, IP address and port
- [ ] DNS purpose at a practical level
- [ ] TCP/IP awareness without network-engineering depth
- [ ] Stateless request processing concept

### 2. URLs, URIs & Addressing
- [ ] Scheme, host, port, path, query, fragment
- [ ] Absolute vs relative URLs
- [ ] Path parameters vs query parameters
- [ ] URL encoding awareness

### 3. HTTP Requests
- [ ] Request method
- [ ] Request target/path
- [ ] Headers
- [ ] Request body
- [ ] Content-Type
- [ ] Accept
- [ ] Authorization header awareness
- [ ] Cookies awareness

### 4. HTTP Responses
- [ ] Status code
- [ ] Response headers
- [ ] Response body
- [ ] Content negotiation awareness

### 5. HTTP Methods
- [ ] GET semantics
- [ ] POST semantics
- [ ] PUT semantics
- [ ] PATCH semantics
- [ ] DELETE semantics
- [ ] HEAD awareness
- [ ] OPTIONS and preflight relevance
- [ ] Safe methods
- [ ] Idempotent methods

### 6. Status Codes
- [ ] 1xx awareness
- [ ] 2xx success: 200, 201, 204
- [ ] 3xx redirects: 301/302/307/308 awareness
- [ ] 4xx: 400, 401, 403, 404, 405, 409, 415, 422, 429
- [ ] 5xx: 500, 502, 503, 504
- [ ] Choose status codes based on semantics rather than habit

### 7. Headers & Representation
- [ ] Content-Type vs Accept
- [ ] application/json
- [ ] text/html and text/plain awareness
- [ ] multipart/form-data and file upload awareness
- [ ] Location
- [ ] Cache-Control
- [ ] ETag awareness
- [ ] Origin and CORS headers

### 8. JSON for APIs
- [ ] Objects, arrays, strings, numbers, booleans, null
- [ ] Request and response payload shapes
- [ ] Nested data
- [ ] JSON is a representation format, not REST itself
- [ ] Serialization/deserialization only at conceptual level

### 9. REST Fundamentals
- [ ] Resource-oriented design
- [ ] Resources vs actions
- [ ] Representations
- [ ] Statelessness
- [ ] Uniform interface concept
- [ ] Resource identifiers
- [ ] CRUD-to-HTTP mapping as a convention, not an identity
- [ ] REST constraints at practical developer depth

### 10. Endpoint Design
- [ ] Use nouns/resources in paths
- [ ] Collection vs item endpoints
- [ ] Nested resources when justified
- [ ] Filtering
- [ ] Sorting
- [ ] Pagination
- [ ] Search/query endpoints
- [ ] Consistent naming
- [ ] Avoid RPC-style verbs unless the operation is genuinely action-oriented

### 11. API Request & Response Design
- [ ] Request DTO shape concept
- [ ] Response DTO shape concept
- [ ] Consistent response contracts
- [ ] Validation failures
- [ ] Error response structures
- [ ] Empty results vs missing resources
- [ ] Created-resource responses and Location awareness

### 12. CORS & Same-Origin Policy
- [ ] Origin = scheme + host + port
- [ ] Browser same-origin policy
- [ ] Cross-origin requests
- [ ] CORS is browser-enforced HTTP policy, not authentication
- [ ] Simple requests
- [ ] Preflight requests
- [ ] OPTIONS
- [ ] Access-Control-Allow-Origin
- [ ] Allowed methods/headers
- [ ] Credentials awareness

### 13. Cookies, Sessions & Tokens
- [ ] Cookie fundamentals
- [ ] Session concept
- [ ] Cookie attributes awareness: Secure, HttpOnly, SameSite
- [ ] Bearer token concept
- [ ] Authentication vs authorization
- [ ] Leave implementation details to security trees

### 14. HTTPS & TLS
- [ ] HTTP vs HTTPS
- [ ] Encryption in transit
- [ ] Integrity
- [ ] Server authentication
- [ ] TLS purpose
- [ ] Certificates
- [ ] Certificate authorities
- [ ] Certificate validation
- [ ] TLS handshake at conceptual level
- [ ] HTTPS does not make an application automatically secure
- [ ] TLS termination awareness

### 15. Caching
- [ ] Browser/client caching concepts
- [ ] Cache-Control
- [ ] ETag/conditional request awareness
- [ ] Freshness vs validation
- [ ] Know when API responses should not be cached

### 16. API Versioning & Evolution
- [ ] Backward compatibility
- [ ] Breaking vs non-breaking changes
- [ ] URI/header versioning awareness
- [ ] Additive changes
- [ ] Deprecation awareness

### 17. API Documentation
- [ ] Document endpoints, parameters, payloads, responses and errors
- [ ] OpenAPI concept
- [ ] Swagger tooling awareness
- [ ] Treat documentation as an API contract

### 18. Testing & Debugging HTTP
- [ ] Browser DevTools Network tab
- [ ] curl requests
- [ ] API client tools awareness
- [ ] Inspect method, URL, headers, payload and status
- [ ] Reproduce frontend failures independently of frontend code
- [ ] Distinguish DNS/network/TLS/HTTP/application errors

### 19. Professional API Practices
- [ ] Consistent semantics
- [ ] Validation at boundaries
- [ ] Do not expose secrets
- [ ] Rate-limit awareness
- [ ] Timeout awareness
- [ ] Retries and idempotency relationship
- [ ] Observability/correlation IDs awareness
- [ ] Design APIs for clients rather than database tables

## Practical competency checkpoints

- [ ] Explain a complete HTTP request/response exchange
- [ ] Design CRUD-style REST endpoints for a small resource
- [ ] Choose appropriate methods and status codes
- [ ] Diagnose a CORS/preflight failure conceptually
- [ ] Explain what HTTPS adds to HTTP
- [ ] Use curl or DevTools to debug an API
- [ ] Explain authentication vs authorization without conflating them
- [ ] Design pagination/filtering/sorting for a collection endpoint

## Mastery standard

> Can I build, explain, debug, and make appropriate design choices in this domain without relying on a step-by-step tutorial, while recognizing what adjacent frameworks or abstractions are doing on my behalf?
