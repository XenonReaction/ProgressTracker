# Spring Data JPA Full-Stack Developer Skill Tree

> **Goal:** Learn Spring Data JPA as the Spring repository abstraction built on top of JPA, while preserving a clear mental model of what Spring Data, JPA, Hibernate, JDBC, and the database each do.
>
> **Required prerequisite trees:**  
> 1. **ORM, JPA & Hibernate** — entities, persistence context, `EntityManager`, mappings, relationships, JPQL, fetching, transactions, and Hibernate's role as a JPA implementation.  
> 2. **Spring Boot & Initializr** — starters, dependency injection, beans, configuration, auto-configuration, application structure, and environment-based configuration.
>
> **Target level:** Professional Spring Boot developer able to design repository layers, query data cleanly, test persistence behavior, recognize performance problems, and choose the appropriate Spring Data JPA feature without treating repository interfaces as magic.
>
> **Scope boundary:** This tree does **not** reteach ORM/JPA/Hibernate fundamentals. It shows how Spring Data JPA builds on them. Deep Criteria API internals, advanced Hibernate tuning, database administration, and migration-tool mastery belong in their respective trees.

---

# Dependency Map

```text
                         Java
                           │
              ┌────────────┴────────────┐
              ▼                         ▼
        JDBC / SQL                 Spring Framework
              │                         │
              ▼                         ▼
        PostgreSQL              Spring Boot & Initializr
              │                         │
              │                         │
              ▼                         │
       ORM / JPA / Hibernate ◄──────────┘
              │
              └────────────┬────────────┘
                           ▼
                    Spring Data JPA
                           │
             ┌─────────────┼─────────────┐
             ▼             ▼             ▼
       Repositories      Queries     Transactions
             │             │             │
             └─────────────┼─────────────┘
                           ▼
                     Service Layer
                           │
                           ▼
                    REST Controller
```

# What Spring Data JPA Adds

```text
Your Service
     │
     ▼
Spring Data Repository
     │
     ├── inherited CRUD
     ├── derived queries
     ├── @Query
     ├── pagination / sorting
     ├── projections
     └── specifications
     │
     ▼
     JPA
     │
     ▼
 Hibernate
     │
     ▼
    JDBC
     │
     ▼
  Database
```

Spring Data JPA does **not** replace JPA or Hibernate. It reduces repetitive repository/data-access code and integrates JPA persistence into the Spring programming model.

---

# Skill Tree Overview

```text
Spring Data JPA
│
├── 1. Prerequisite Review
├── 2. Abstraction Layers
├── 3. Project Setup
├── 4. Repository Concept
├── 5. Repository Hierarchy
├── 6. Repository Generics
├── 7. JpaRepository
├── 8. Inherited CRUD
├── 9. Return Types
├── 10. Derived Query Fundamentals
├── 11. Query Keywords
├── 12. Property Traversal
├── 13. Ordering
├── 14. Limiting Results
├── 15. Null / Optional Handling
├── 16. Sorting
├── 17. Pagination
├── 18. Page vs Slice
├── 19. @Query
├── 20. JPQL Queries
├── 21. Native SQL Queries
├── 22. Parameters
├── 23. Modifying Queries
├── 24. Projections
├── 25. DTO Projections
├── 26. Specifications
├── 27. Dynamic Search
├── 28. Transactions
├── 29. Repository + Service Boundaries
├── 30. Relationship Queries
├── 31. Fetching & N+1
├── 32. Entity Graphs / Fetch Joins
├── 33. Auditing
├── 34. Locking
├── 35. Bulk / Batch Awareness
├── 36. Exception Handling
├── 37. Repository Testing
├── 38. Service Testing with Mockito
├── 39. Database Test Strategy
├── 40. Schema Migration Awareness
├── 41. Performance
├── 42. Debugging
├── 43. Production Practices
└── 44. Architecture & Mastery
```

---

# Tier 0 — Prerequisite Check

## 1. ORM / JPA / Hibernate Readiness

Before beginning, be able to explain at least at a practical level:

- [ ] ORM
- [ ] JPA
- [ ] Hibernate
- [ ] `@Entity`
- [ ] `@Id`
- [ ] `@GeneratedValue`
- [ ] Entity relationships
- [ ] Persistence context
- [ ] Managed vs detached entity awareness
- [ ] `EntityManager`
- [ ] JPQL
- [ ] Lazy vs eager loading
- [ ] Transaction concept

```text
Can I explain why Hibernate exists?
Can I explain what JPA standardizes?
Can I explain what an EntityManager does?
Can I map a Java class to a database table?
                │
                ▼
              YES
```

If not, return to **ORM, JPA & Hibernate**.

## 2. Spring Boot Readiness

Be able to explain:

- [ ] Spring beans
- [ ] Dependency injection
- [ ] Constructor injection
- [ ] `@Service`
- [ ] `@Configuration`
- [ ] Spring Boot starters
- [ ] Auto-configuration at a practical level
- [ ] `application.yml` / `.properties`
- [ ] Datasource configuration
- [ ] Environment variables

```text
Can I create and run a Spring Boot application?
Can I configure a datasource?
Can I inject one bean into another?
                │
                ▼
              YES
```

If not, return to **Spring Boot & Initializr**.

---

# Tier 1 — Understand the Abstraction Stack

## 3. JDBC vs JPA vs Hibernate vs Spring Data JPA

This distinction is mandatory.

```text
Spring Data JPA
       │
       ▼
      JPA
       │
       ▼
   Hibernate
       │
       ▼
      JDBC
       │
       ▼
   PostgreSQL
```

### JDBC

Java API for communicating with relational databases.

You work closer to:

```text
SQL
connections
prepared statements
result sets
```

### JPA

Java persistence specification/API defining concepts such as:

```text
@Entity
EntityManager
persistence context
JPQL
relationships
```

### Hibernate

A common implementation of JPA.

### Spring Data JPA

Spring abstraction that builds repository features on top of JPA.

```text
JpaRepository<Message, Long>
```

does not eliminate the layers underneath it.

**Checkpoint:** Explain all four layers without saying they are interchangeable.

---

# Tier 2 — Project Setup

## 4. Dependencies

A typical Spring Boot project needs:

```text
Spring Data JPA starter
+
database JDBC driver
```

Conceptually:

```text
pom.xml
  │
  ├── spring-boot-starter-data-jpa
  └── database driver
```

- [ ] Add the JPA starter
- [ ] Add the correct database driver
- [ ] Understand dependency management
- [ ] Recognize Hibernate as a typical underlying provider

## 5. Datasource Configuration

Understand configuration such as:

```text
URL
username
password
driver/provider configuration
JPA/Hibernate options
```

Use external configuration for credentials.

## 6. Entity Discovery

Understand that entities must be in packages visible to Spring Boot/JPA configuration.

- [ ] Standard package structure
- [ ] Entity scanning awareness
- [ ] Repository scanning awareness
- [ ] Prefer conventional project structure before custom scanning

**Checkpoint:** Start a Spring Boot application connected to a development database with one entity.

---

# Tier 3 — Repository Concept

## 7. Why Repositories Exist

Without a repository abstraction, data access often requires repeated plumbing.

Spring Data JPA allows:

```java
public interface MessageRepository
        extends JpaRepository<Message, Long> {
}
```

Spring creates the implementation at runtime.

```text
Service
   │
   ▼
Repository Interface
   │
   ▼
Spring Data-generated implementation
   │
   ▼
JPA / Hibernate
```

## 8. Repository Responsibility

Repository layer:

- [ ] Access persistent data
- [ ] Express persistence queries
- [ ] Save/load/delete entities
- [ ] Avoid business logic
- [ ] Avoid HTTP concerns

```text
Controller → HTTP boundary
Service    → business logic
Repository → persistence access
```

---

# Tier 4 — Repository Hierarchy

## 9. Repository Interfaces

Understand the conceptual progression:

```text
                          Repository
                    (root marker interface)
                    ┌─────────┼─────────────────┬───────────────────────┐
                    ▼         ▼                 ▼                       ▼
           CrudRepository  PagingAndSortingRepository   QueryByExampleExecutor
                    │         │
                    ▼         ▼
        ListCrudRepository  ListPagingAndSortingRepository
                    │         │
                    └────┬────┴───────────────────┬───────┘
                         ▼                         ▼
                              JpaRepository
                  (composes the interfaces above rather than
                   extending a single linear chain)
```

Exact inheritance relationships can evolve between Spring Data versions, so focus on capability rather than memorizing every interface edge. As of Spring Data 3.0, `PagingAndSortingRepository` no longer extends `CrudRepository` — the interfaces were decoupled and made more independently composable, and `JpaRepository` now assembles `ListCrudRepository`, `ListPagingAndSortingRepository`, and `QueryByExampleExecutor` rather than inheriting through one strict chain.

## 10. `Repository`

Marker/base abstraction.

Learn why Spring Data repository interfaces can be recognized and implemented by the framework.

## 11. `CrudRepository`

CRUD capabilities such as:

```text
save
findById
existsById
findAll
count
delete
```

## 12. `PagingAndSortingRepository`

Adds paging/sorting-oriented access.

## 13. `JpaRepository`

The common practical choice for JPA applications.

Adds JPA-oriented convenience and richer collection operations.

**Checkpoint:** Explain why you commonly extend `JpaRepository` even though lower-level repository abstractions exist.

---

# Tier 5 — Repository Generics

## 14. Entity and ID Types

```java
JpaRepository<Message, Long>
```

means approximately:

```text
Message → managed entity type
Long    → primary-key Java type
```

Examples:

```java
JpaRepository<User, UUID>
JpaRepository<Product, Long>
JpaRepository<Category, Integer>
```

- [ ] Entity generic
- [ ] ID generic
- [ ] Match entity `@Id` type
- [ ] Understand compile-time type safety

---

# Tier 6 — Inherited CRUD

## 15. Save

```java
repository.save(entity);
```

Understand conceptually that JPA entity state determines underlying persistence behavior.

Do not reduce this to:

```text
save() always means SQL INSERT
```

Review entity lifecycle in the JPA prerequisite tree.

## 16. Find by ID

```java
repository.findById(id);
```

Usually returns:

```java
Optional<Entity>
```

- [ ] Handle absence explicitly
- [ ] Do not blindly call `.get()`

## 17. Find All

```java
repository.findAll();
```

Understand when retrieving every row is unsafe.

## 18. Delete

Learn:

- [ ] `delete(entity)`
- [ ] `deleteById(id)`
- [ ] delete-all awareness
- [ ] Entity relationship/cascade consequences belong partly to JPA knowledge

## 19. Exists and Count

```text
existsById(...)
count()
```

Use the operation that expresses the actual question rather than loading entities unnecessarily.

---

# Tier 7 — Repository Return Types

## 20. Common Return Shapes

Understand practical use of:

```text
Entity
Optional<Entity>
List<Entity>
Page<Entity>
Slice<Entity>
Stream awareness
projection
DTO
```

Choose based on semantics.

## 21. `Optional`

Use when a single result may legitimately be absent.

```java
Optional<Message> findById(Long id);
```

## 22. Collections

Use collections when multiple rows are expected.

Do not encode uniqueness assumptions accidentally.

---

# Tier 8 — Derived Query Fundamentals

## 23. Query Derivation

Spring Data can derive queries from method names.

Example:

```java
List<Message> findByName(String name);
```

Conceptually:

```text
findBy + Name
          │
          ▼
entity property
          │
          ▼
generated query
```

## 24. Why It Works

Spring Data parses repository method names according to supported query-method conventions.

This:

```java
findAllByOrderByCreatedAtDesc()
```

encodes:

```text
find all
+
order by createdAt
+
descending
```

## 25. Appropriate Use

Derived queries are excellent for simple, readable conditions.

Avoid enormous names such as:

```text
findByAAndBOrCAndDAndEOrderByF...
```

when `@Query` or Specifications would be clearer.

**Checkpoint:** Write derived queries for five common application requirements.

---

# Tier 9 — Query Keywords

## 26. Equality and Comparison

Practice concepts represented by method-name keywords such as:

- [ ] `Is`
- [ ] `Equals`
- [ ] `Not`
- [ ] `LessThan`
- [ ] `LessThanEqual`
- [ ] `GreaterThan`
- [ ] `GreaterThanEqual`
- [ ] `Between`

## 27. String Queries

Practice:

- [ ] `Like`
- [ ] `NotLike`
- [ ] `StartingWith`
- [ ] `EndingWith`
- [ ] `Containing`
- [ ] `IgnoreCase`

## 28. Boolean / Null / Collection Conditions

Practice:

- [ ] `True`
- [ ] `False`
- [ ] `IsNull`
- [ ] `IsNotNull`
- [ ] `In`
- [ ] `NotIn`

## 29. Combining Conditions

```text
And
Or
```

Be careful with readability and logical precedence.

---

# Tier 10 — Property Traversal

## 30. Nested Properties

Given:

```text
Order
 └── Customer
      └── email
```

a derived query can navigate properties conceptually:

```text
findByCustomerEmail(...)
```

- [ ] Understand property traversal
- [ ] Recognize ambiguity risk
- [ ] Know relationships are JPA mappings underneath
- [ ] Keep names readable

## 31. Relationship Boundary

This tree does not reteach:

```text
@OneToMany
@ManyToOne
@OneToOne
@ManyToMany
cascade
orphan removal
```

Instead, learn how repositories query across relationships already mapped through JPA.

---

# Tier 11 — Ordering

## 32. Static Ordering in Method Names

Example:

```java
findByStatusOrderByCreatedAtDesc(...)
```

## 33. Dynamic Sorting

Prefer a `Sort` parameter when callers need varying sort choices.

```text
same query
   │
   ├── createdAt ASC
   ├── createdAt DESC
   └── name ASC
```

Avoid creating many nearly identical repository methods solely for sorting.

---

# Tier 12 — Limiting Results

## 34. First / Top

Understand query derivation concepts such as:

```text
findFirst...
findTop...
findTop10...
```

Useful for:

- [ ] Most recent record
- [ ] Highest-ranked records
- [ ] Limited recent history

Always define ordering when "first" or "top" depends on order.

---

# Tier 13 — Null and Optional Handling

## 35. Missing Single Results

Prefer:

```java
Optional<User>
```

where absence is expected.

Service layer decides business meaning:

```text
repository returns empty
        │
        ▼
service decides:
404?
business exception?
default behavior?
```

## 36. Null Query Parameters

Understand that null parameter behavior can differ based on query construction.

Do not assume:

```text
null = "ignore this filter"
```

Dynamic optional filters often belong in Specifications or intentionally constructed queries.

---

# Tier 14 — Sorting

## 37. `Sort`

Conceptually:

```text
Sort.by("createdAt")
Sort descending
multiple properties
```

- [ ] Ascending
- [ ] Descending
- [ ] Multiple fields
- [ ] Avoid exposing arbitrary unvalidated property names directly from users

## 38. API Integration

```text
HTTP query parameters
       │
       ▼
Controller validates
       │
       ▼
Service
       │
       ▼
Repository + Sort
```

---

# Tier 15 — Pagination

## 39. Why Pagination

Bad production pattern:

```text
SELECT / load every row
```

Better:

```text
page 0 → 20 rows
page 1 → 20 rows
...
```

## 40. `Pageable`

Understand:

- [ ] Page number
- [ ] Page size
- [ ] Sort
- [ ] `PageRequest`

## 41. `Page<T>`

Contains:

```text
content
page number
page size
total elements
total pages
```

Total counts can require additional database work.

---

# Tier 16 — `Page` vs `Slice`

## 42. `Page`

Use when the caller needs total-count information.

```text
Page
├── current content
├── total rows
└── total pages
```

## 43. `Slice`

Use when you mainly need:

```text
current content
+
is there another slice?
```

This can avoid some count-query overhead.

## 44. API Judgment

Do not return `Page` automatically just because it is familiar.

Ask whether the UI truly needs exact total counts.

**Checkpoint:** Implement paginated recent messages with sorting.

---

# Tier 17 — `@Query`

## 45. Why `@Query`

Use explicit queries when derived method names become unclear or insufficient.

```java
@Query("...")
List<Entity> someQuery(...);
```

Advantages:

- [ ] Query is explicit
- [ ] Method name stays readable
- [ ] Supports more complex query logic

## 46. Query Ownership

Repository query code belongs in the repository/data-access layer.

Business decisions about **when** to run the query belong in the service layer.

---

# Tier 18 — JPQL Queries

## 47. JPQL

JPQL queries the entity model.

Conceptually:

```text
SQL:
SELECT * FROM messages

JPQL:
SELECT m FROM Message m
```

JPQL speaks in terms of:

```text
entities
entity fields
entity relationships
```

rather than raw table/column names.

## 48. Practical JPQL

Learn:

- [ ] Select entities
- [ ] `WHERE`
- [ ] Parameters
- [ ] Joins
- [ ] Ordering
- [ ] Projection awareness
- [ ] Fetch joins
- [ ] Aggregate queries at practical depth

Deep JPQL belongs primarily to the ORM/JPA/Hibernate tree.

---

# Tier 19 — Native SQL Queries

## 49. Native Queries

Sometimes direct database SQL is appropriate.

Conceptually:

```java
@Query(value = "...SQL...", nativeQuery = true)
```

Use when:

- [ ] Database-specific feature is required
- [ ] Existing SQL is appropriate
- [ ] JPQL cannot express the needed operation cleanly
- [ ] Performance/query requirements justify it

## 50. Tradeoff

```text
JPQL
more entity-oriented / portable

Native SQL
more database-specific / direct
```

Do not default to native SQL merely because SQL is familiar.

---

# Tier 20 — Query Parameters

## 51. Parameters

Understand positional vs named concepts, with preference for readable parameter binding.

Example concept:

```text
WHERE m.name = :name
```

- [ ] Bind parameters
- [ ] Avoid string concatenation
- [ ] Prevent injection by using parameter binding
- [ ] Keep Java method parameter names/query bindings clear

## 52. Collection Parameters

Understand use cases such as:

```text
WHERE status IN (...)
```

and empty-collection edge cases.

---

# Tier 21 — Modifying Queries

## 53. `@Modifying`

Queries that update/delete data require modifying-query semantics.

Conceptually:

```text
@Modifying
@Query("UPDATE ...")
```

## 54. Transaction Requirement

Modifying operations usually need an appropriate transaction boundary.

```text
Service @Transactional
        │
        ▼
repository modifying query
```

## 55. Persistence Context Awareness

Bulk update/delete queries can bypass normal entity-state synchronization.

Know that:

```text
database state
```

and:

```text
already-managed entity state
```

can become inconsistent if handled carelessly.

Deep persistence-context behavior belongs to the JPA prerequisite tree.

---

# Tier 22 — Projections

## 56. Why Projections

Sometimes you do not need a full entity.

```text
Entity
├── id
├── name
├── body
├── createdAt
├── large relationships
└── internal fields

UI only needs:
name + createdAt
```

Projection can request a narrower shape.

## 57. Interface-Based Projections

Learn practical interface projections:

```java
interface MessageSummary {
    String getName();
    OffsetDateTime getCreatedAt();
}
```

- [ ] Closed projection concept
- [ ] Nested projection awareness
- [ ] Dynamic projection awareness at high level

## 58. Projection Tradeoffs

Use projections when they improve:

- [ ] Query efficiency
- [ ] API-specific read models
- [ ] Separation from entities

Do not create projections for every trivial query without need.

---

# Tier 23 — DTO Projections

## 59. DTO / Record Projection

Example target:

```java
public record MessageSummary(
    String name,
    OffsetDateTime createdAt
) {}
```

Understand constructor/DTO projection approaches at a practical level.

## 60. Entity vs DTO

Do not automatically return JPA entities from REST APIs.

```text
Repository
   │
   ▼
Entity / Projection
   │
   ▼
Service mapping
   │
   ▼
Response DTO
```

Repository projections and API DTOs can overlap in shape, but they serve different architectural purposes.

---

# Tier 24 — Specifications

## 61. The Dynamic Query Problem

Suppose a search form has optional:

```text
name
status
createdAfter
createdBefore
category
```

Creating a repository method for every combination becomes unmanageable.

## 62. `JpaSpecificationExecutor`

Conceptually:

```text
Specification A
      +
Specification B
      +
Specification C
      │
      ▼
dynamic query
```

Learn practical use of:

- [ ] `Specification<T>`
- [ ] `JpaSpecificationExecutor<T>`
- [ ] Predicate composition
- [ ] `and`
- [ ] `or`
- [ ] Optional filters

## 63. Criteria API Boundary

Specifications are built on JPA Criteria concepts.

For this tree:

- Learn enough Criteria concepts to understand/write practical Specifications.
- Do not turn the tree into deep Criteria API study.

---

# Tier 25 — Dynamic Search

## 64. Search Endpoint Pattern

```text
GET /products?
 category=books
 &minPrice=10
 &maxPrice=50
 &available=true
       │
       ▼
validated search criteria
       │
       ▼
Specifications
       │
       ▼
Repository
```

## 65. Avoid Combinatorial Methods

Bad:

```text
findByCategory
findByCategoryAndPrice
findByCategoryAndPriceAndAvailable
findByPriceAndAvailable
...
```

Better when filters are genuinely dynamic:

```text
compose Specifications
```

## 66. Security

Never convert arbitrary client input into unrestricted query behavior.

Validate:

- [ ] Sort fields
- [ ] Filter fields
- [ ] Page size
- [ ] Search operators
- [ ] Authorization constraints

---

# Tier 26 — Transactions

## 67. Spring Transaction Boundary

For application architecture, transactions commonly belong at the service layer.

```java
@Service
public class OrderService {

    @Transactional
    public void placeOrder(...) {
        ...
    }
}
```

```text
Service transaction
      │
      ├── repository call 1
      ├── repository call 2
      └── repository call 3
```

They participate in one unit of work where configured appropriately.

## 68. Repository Transaction Behavior

Understand that Spring Data repository methods have transaction semantics, but application-level business transactions often need a broader service boundary.

## 69. Read-Only Awareness

Understand:

```text
@Transactional(readOnly = true)
```

as a useful intent/optimization hint in appropriate read operations, not a universal security mechanism.

## 70. Rollback Awareness

Know practical Spring transaction rollback behavior and where exceptions affect it.

Detailed transaction propagation/isolation can branch into deeper Spring/JPA study.

**Checkpoint:** Implement a service operation involving multiple repository actions that must succeed or fail together.

---

# Tier 27 — Repository + Service Boundaries

## 71. Correct Separation

```text
Controller
   │
   ▼
Service
   │
   ├── validation/business rules
   ├── transaction boundary
   ├── orchestration
   └── DTO mapping
   │
   ▼
Repository
   │
   ├── persistence operations
   └── persistence queries
```

## 72. Avoid Repository Business Logic

Repository method:

```text
findActiveSubscriptionsExpiringBefore(...)
```

can describe data selection.

But:

```text
decideWhetherCustomerDeservesRefund(...)
```

is business logic and belongs elsewhere.

## 73. Avoid Controller-to-Repository Shortcuts

For trivial demos this can work, but professional layered applications generally benefit from a service boundary.

---

# Tier 28 — Relationship Queries

## 74. Querying Mapped Relationships

Given:

```text
Order
 └── customer
      └── email
```

learn:

- [ ] Derived property traversal
- [ ] JPQL joins
- [ ] Projection across relationships
- [ ] Specification joins at practical depth

## 75. Do Not Reteach Mapping

Relationship ownership, cascading, orphan removal, and mapping design remain prerequisite JPA material.

This tree focuses on:

```text
"Given a correct mapping, how do I query it through Spring Data JPA?"
```

---

# Tier 29 — Fetching & N+1

## 76. N+1 Problem

Conceptually:

```text
1 query → load orders
+
1 query per order → load customer
=
N + 1 queries
```

- [ ] Recognize N+1
- [ ] Know lazy relationships can trigger additional queries
- [ ] Inspect SQL/query counts
- [ ] Avoid assuming repository convenience means efficient SQL

## 77. Detection

Use:

- [ ] SQL logging during development
- [ ] Hibernate statistics/observability awareness
- [ ] Integration tests where query behavior matters
- [ ] Profiling/APM awareness

## 78. Fix Strategy

Possible approaches include:

- [ ] Fetch joins
- [ ] Entity graphs
- [ ] Projections
- [ ] Query redesign
- [ ] Batch-fetching awareness

Choose based on actual access pattern.

---

# Tier 30 — Entity Graphs & Fetch Joins

## 79. Fetch Join

JPQL can fetch required relationships in one intentional query.

Conceptually:

```text
Order
JOIN FETCH
Customer
```

Use carefully with collections and pagination.

## 80. Entity Graphs

Understand `@EntityGraph` as a way to influence fetch plans for repository queries.

- [ ] Attribute paths
- [ ] Repository method integration
- [ ] Avoid changing global mapping defaults just to satisfy one use case

## 81. Pagination Caveats

Fetching collection relationships while paginating can create surprising SQL/results/performance.

Know this is a warning sign requiring deliberate query design.

---

# Tier 31 — Auditing

## 82. Auditing Purpose

Common fields:

```text
createdAt
updatedAt
createdBy
updatedBy
```

Spring Data JPA can help populate auditing information.

## 83. Common Annotations

Learn practical use of:

- [ ] `@CreatedDate`
- [ ] `@LastModifiedDate`
- [ ] `@CreatedBy`
- [ ] `@LastModifiedBy`
- [ ] `@EnableJpaAuditing`

## 84. Auditor Provider

For user-aware auditing:

```text
current authenticated user
       │
       ▼
AuditorAware
       │
       ▼
createdBy / updatedBy
```

Spring Security integration can be introduced here but belongs more deeply in the Spring Security tree.

---

# Tier 32 — Locking

## 85. Why Locking Exists

Concurrent transactions can interact with the same data.

Understand at a practical level:

- [ ] Optimistic locking belongs primarily to JPA (`@Version`)
- [ ] Repository query locking can be expressed where needed
- [ ] Pessimistic locking awareness
- [ ] Lock choice affects concurrency

## 86. `@Lock`

Learn that repository methods can declare lock modes.

Do not use pessimistic locks as a default fix for concurrency problems.

Deep isolation/concurrency belongs in database/JPA specialization.

---

# Tier 33 — Bulk / Batch Awareness

## 87. Bulk Operations

Be aware of:

```text
saveAll
deleteAll...
bulk JPQL update/delete
```

but do not assume method names guarantee ideal database batching.

## 88. JPA Batching

Actual batching depends on:

- [ ] Hibernate configuration
- [ ] ID generation strategy
- [ ] Flush behavior
- [ ] Database/driver
- [ ] Transaction size

Detailed Hibernate batching belongs in the ORM/Hibernate tree.

## 89. Large Data Sets

Do not load millions of entities into memory and call `saveAll()` blindly.

Consider:

- [ ] Chunking
- [ ] Flush/clear strategy
- [ ] Bulk SQL
- [ ] Dedicated batch-processing tools

---

# Tier 34 — Exception Handling

## 90. Persistence Exceptions

Spring provides data-access exception translation concepts.

Understand that repository/database failures may surface through Spring's data-access exception hierarchy.

## 91. Service Translation

Do not expose low-level database exceptions directly through your API.

```text
Database error
     │
     ▼
Repository exception
     │
     ▼
Service/application handling
     │
     ▼
appropriate API response
```

## 92. Constraint Violations

Examples:

- [ ] Unique constraint
- [ ] Foreign key
- [ ] Not-null
- [ ] Invalid data
- [ ] Optimistic locking

Design both database constraints and application handling intentionally.

---

# Tier 35 — Repository Testing

## 93. What Repository Tests Should Prove

Repository tests should verify actual persistence/query behavior.

Examples:

- [ ] Entity mapping works
- [ ] Derived query returns correct rows
- [ ] `@Query` is valid
- [ ] Sorting/pagination works
- [ ] Projection works
- [ ] Relationship query behaves correctly
- [ ] Constraints behave as expected

## 94. `@DataJpaTest`

Use focused JPA tests where appropriate.

Conceptually:

```text
@DataJpaTest
      │
      ▼
JPA-focused Spring test context
      │
      ▼
real repository implementation
```

Do **not** mock the repository in a test whose purpose is to verify the repository query.

## 95. Test Data

Keep test setup:

- [ ] Small
- [ ] Explicit
- [ ] Independent
- [ ] Representative
- [ ] Easy to understand

---

# Tier 36 — Service Testing with Mockito

## 96. Different Test Goal

Service test:

```text
Service
  │
  ▼
mock Repository
```

This can test:

- [ ] Business rules
- [ ] Repository interaction
- [ ] Error handling
- [ ] DTO mapping
- [ ] Orchestration

## 97. Repository Test vs Service Test

```text
Repository behavior?
→ real repository / database test

Service behavior independent of database?
→ mock repository with Mockito
```

This distinction is important.

**Checkpoint:** Write one `@DataJpaTest` for a derived query and one Mockito unit test for a service that uses that repository.

---

# Tier 37 — Database Test Strategy

## 98. Embedded/Test Database Awareness

A lightweight database can make tests fast, but behavior may differ from production.

Potential differences:

- [ ] SQL dialect
- [ ] Data types
- [ ] Constraints
- [ ] Functions
- [ ] Index behavior
- [ ] Native queries

## 99. Production-Like Database Testing

For important persistence behavior, test against the same database technology used in production where practical.

Example:

```text
PostgreSQL production
       │
       ▼
PostgreSQL integration test
```

Container-based test infrastructure can be introduced here, with deeper coverage in Docker/testing trees.

## 100. Test Layers

```text
Unit tests
   │
   ▼
Repository integration tests
   │
   ▼
Application integration tests
   │
   ▼
End-to-end tests
```

Each proves something different.

---

# Tier 38 — Schema Migration Awareness

## 101. `ddl-auto`

Automatic schema generation/update is useful during early development, but production schema management should be intentional.

Understand common modes at a high level and their risks.

## 102. Migration Tools

Introduce:

```text
Flyway
Liquibase
```

Purpose:

```text
version-controlled database schema changes
```

## 103. Scope Boundary

This tree should teach:

- [ ] Why migrations exist
- [ ] Why `ddl-auto: update` is not a production migration strategy
- [ ] How Spring Data JPA depends on the schema being compatible

Detailed migration authoring should become its own skill tree later.

---

# Tier 39 — Performance

## 104. Repository Convenience Can Hide Cost

This:

```java
repository.findAll();
```

looks cheap in Java.

It may mean:

```text
SELECT huge_table...
+
entity construction
+
relationship loading
+
memory usage
```

Always reason about generated database work.

## 105. Common Performance Problems

Recognize:

- [ ] N+1
- [ ] Fetching too many columns/entities
- [ ] Fetching too many rows
- [ ] Missing pagination
- [ ] Count-query cost
- [ ] Unnecessary relationship traversal
- [ ] Inefficient native/JPQL queries
- [ ] Missing database indexes
- [ ] Excessive flushes
- [ ] Huge transactions
- [ ] Mapping explosion is not a JPA issue—keep technology boundaries clear

## 106. Database Indexes

Spring Data JPA query design and database indexing must align.

Example:

```text
findByEmail(...)
```

may be logically correct but slow if the database must scan a huge table.

Index design belongs primarily to the PostgreSQL/database tree.

## 107. Measure

Use evidence:

```text
SQL logs
query plan
query count
latency
database metrics
APM
```

Do not optimize based only on repository method appearance.

---

# Tier 40 — Debugging

## 108. Debugging Pipeline

When a repository operation fails:

```text
Controller
   │
   ▼
Service
   │
   ▼
Repository
   │
   ▼
Spring Data
   │
   ▼
JPA
   │
   ▼
Hibernate
   │
   ▼
JDBC
   │
   ▼
Database
```

Identify the failing layer.

## 109. Repository Bean Not Found

Check:

- [ ] Package structure
- [ ] Repository interface declaration
- [ ] Spring Boot application package
- [ ] Repository scanning
- [ ] Dependencies
- [ ] Application context errors

## 110. Query Method Fails at Startup

Check:

- [ ] Entity property spelling
- [ ] Nested property path
- [ ] Supported query keyword
- [ ] Parameter types
- [ ] Return type
- [ ] Ambiguous method name

## 111. `@Query` Failure

Check:

- [ ] JPQL uses entity/property names
- [ ] Native SQL uses table/column names
- [ ] Parameter binding
- [ ] Syntax
- [ ] Projection compatibility
- [ ] Database-specific syntax for native queries

## 112. Lazy Loading Failure

If data is accessed outside the required persistence/transaction context, lazy loading may fail.

Do not "fix" every case by globally switching relationships to eager fetching.

Review:

```text
transaction boundary
query fetch plan
DTO mapping location
entity graph / fetch join
```

## 113. Unexpected SQL

Enable/inspect SQL during development and ask:

```text
Why was this query generated?
Why did it run this many times?
Why are these columns/joins present?
```

---

# Tier 41 — Production Practices

## 114. Keep Entities Internal

Avoid using persistence entities as your entire public API contract.

Prefer:

```text
HTTP Request DTO
      │
      ▼
Service
      │
      ▼
Entity
      │
      ▼
Repository

Repository result
      │
      ▼
Service
      │
      ▼
Response DTO
```

## 115. Validate at Appropriate Boundaries

- [ ] Request validation
- [ ] Business validation
- [ ] Database constraints
- [ ] Entity constraints where appropriate

Do not assume one layer replaces all others.

## 116. Bound User-Controlled Queries

Validate:

- [ ] Maximum page size
- [ ] Sort properties
- [ ] Search filters
- [ ] IDs
- [ ] Authorization

Prevent endpoints such as:

```text
?pageSize=2000000
```

from accidentally exhausting resources.

## 117. Logging

Log useful repository/data failures without logging:

- [ ] Passwords
- [ ] Connection secrets
- [ ] Sensitive query parameters unnecessarily
- [ ] Entire sensitive entities

## 118. Schema Discipline

Production:

```text
migration tool
      │
      ▼
known schema version
      │
      ▼
application deployment
```

rather than relying on Hibernate to improvise schema changes.

---

# Tier 42 — Choosing the Right Query Technique

## 119. Decision Tree

```text
Need basic CRUD?
      │
      └──► inherited JpaRepository method

Simple fixed condition?
      │
      └──► derived query

Complex but fixed entity query?
      │
      └──► @Query + JPQL

Database-specific requirement?
      │
      └──► native query

Many optional filters?
      │
      └──► Specification

Need only a few fields?
      │
      └──► projection / DTO query

Need relationship data efficiently?
      │
      └──► fetch plan / projection / fetch join / entity graph
```

## 120. Avoid Feature Worship

Do not use:

```text
Specification
```

when:

```text
findByEmail(...)
```

is clearer.

Do not use a 100-character derived query when a small explicit query is clearer.

Choose the simplest mechanism that remains readable and correct.

---

# Tier 43 — Architecture & Mastery

## 121. Full Request Flow

```text
Angular / React
      │
      ▼
HTTP Request
      │
      ▼
@RestController
      │
      ▼
@Service
      │
      ├── business rules
      ├── transaction
      └── DTO mapping
      │
      ▼
JpaRepository
      │
      ▼
Spring Data JPA
      │
      ▼
JPA
      │
      ▼
Hibernate
      │
      ▼
JDBC
      │
      ▼
PostgreSQL
```

## 122. Responsibility Map

```text
PostgreSQL
stores relational data and executes SQL

JDBC
Java database communication API

JPA
standard Java persistence model/API

Hibernate
JPA implementation / ORM engine

Spring Data JPA
repository abstraction over JPA

Service
business logic + application transaction boundary

Controller
HTTP boundary
```

A developer should be able to locate a problem in this stack instead of calling all of it "Spring."

---

# Practical Competency Checkpoints

A developer completing this tree should be able to:

- [ ] Explain JDBC vs JPA vs Hibernate vs Spring Data JPA
- [ ] Explain why Spring Data JPA depends on JPA/Hibernate knowledge
- [ ] Explain why Spring Boot knowledge is also required
- [ ] Configure Spring Data JPA in a Spring Boot application
- [ ] Create a repository interface
- [ ] Explain repository generic parameters
- [ ] Use inherited CRUD methods
- [ ] Handle `Optional` correctly
- [ ] Write simple derived queries
- [ ] Use comparison/string/null query keywords
- [ ] Traverse mapped properties in derived queries
- [ ] Add static and dynamic ordering
- [ ] Limit query results
- [ ] Use `Sort`
- [ ] Use `Pageable`
- [ ] Explain `Page` vs `Slice`
- [ ] Write `@Query`
- [ ] Write practical JPQL
- [ ] Know when native SQL is appropriate
- [ ] Bind query parameters safely
- [ ] Write modifying queries
- [ ] Explain bulk-query persistence-context concerns
- [ ] Use interface projections
- [ ] Use DTO/record projections
- [ ] Use `JpaSpecificationExecutor`
- [ ] Compose optional search filters
- [ ] Put application transactions at sensible service boundaries
- [ ] Query across mapped relationships
- [ ] Recognize N+1
- [ ] Use fetch joins/entity graphs at practical depth
- [ ] Configure Spring Data auditing
- [ ] Explain locking at a practical level
- [ ] Recognize batch/bulk tradeoffs
- [ ] Handle persistence exceptions appropriately
- [ ] Test repositories with `@DataJpaTest`
- [ ] Test services with mocked repositories using Mockito
- [ ] Explain why repository tests should not mock the repository
- [ ] Choose production-like database tests when database behavior matters
- [ ] Explain why schema migration tools are needed
- [ ] Recognize common JPA performance problems
- [ ] Debug repository/query/configuration failures by layer
- [ ] Keep entities separate from public API contracts
- [ ] Choose the simplest appropriate query mechanism

---

# Suggested Practice Progression

```text
1. Create one @Entity
       │
       ▼
2. Create JpaRepository<Entity, Id>
       │
       ▼
3. Use save/findById/findAll/delete
       │
       ▼
4. Add simple derived queries
       │
       ▼
5. Add ordering and limiting
       │
       ▼
6. Add Sort
       │
       ▼
7. Add Pageable + Page
       │
       ▼
8. Compare Page vs Slice
       │
       ▼
9. Write @Query with JPQL
       │
       ▼
10. Write one native query
       │
       ▼
11. Add projection
       │
       ▼
12. Build dynamic filters with Specification
       │
       ▼
13. Add service @Transactional workflow
       │
       ▼
14. Query an entity relationship
       │
       ▼
15. Create and diagnose an N+1 problem
       │
       ▼
16. Fix fetch behavior intentionally
       │
       ▼
17. Add auditing
       │
       ▼
18. Write @DataJpaTest repository tests
       │
       ▼
19. Mock repository in service unit tests
       │
       ▼
20. Run tests against production-like database
       │
       ▼
21. Replace schema auto-update mindset with migrations
       │
       ▼
22. Performance/debugging pass
```

---

# Capstone — Searchable Guestbook Repository Layer

Extend a Spring Boot guestbook/message-board application.

## Starting Domain

```text
Message
├── id
├── name
├── body
├── createdAt
├── updatedAt
└── status
```

Optional relationship:

```text
Message
   │
   ▼
Category
```

## Repository Progression

### Stage 1 — Basic CRUD

```java
public interface MessageRepository
        extends JpaRepository<Message, Long> {
}
```

Use:

- [ ] `save`
- [ ] `findById`
- [ ] `findAll`
- [ ] `deleteById`
- [ ] `count`

### Stage 2 — Derived Queries

Create requirements such as:

```text
messages by name
messages containing text
messages created after date
messages by status
newest 10 messages
```

### Stage 3 — Pagination

Endpoint concept:

```text
GET /api/messages?page=0&size=20&sort=createdAt,desc
```

Validate page size and sort options.

### Stage 4 — Explicit Query

Write a readable JPQL `@Query` for a requirement that would create an unwieldy derived method name.

### Stage 5 — Projection

Create a lightweight summary:

```text
id
name
createdAt
status
```

without requiring every use case to load/return the full entity shape.

### Stage 6 — Dynamic Search

Support optional filters:

```text
name
text
status
createdAfter
createdBefore
category
```

using Specifications.

### Stage 7 — Transactions

Create a service operation requiring multiple persistence operations in one transaction.

### Stage 8 — Relationship Performance

Add a relationship, intentionally trigger N+1, inspect generated SQL, and correct the query/fetch strategy.

### Stage 9 — Auditing

Automatically maintain:

```text
createdAt
updatedAt
```

and optionally introduce user auditing later when Spring Security exists.

### Stage 10 — Testing

Create:

```text
@DataJpaTest
├── CRUD test
├── derived query test
├── pagination test
├── @Query test
├── projection test
└── Specification test
```

Then:

```text
Mockito service tests
└── mocked MessageRepository
```

### Stage 11 — Production Readiness

- [ ] Production-like PostgreSQL integration test
- [ ] Bounded pagination
- [ ] Appropriate indexes identified
- [ ] No accidental N+1 in primary endpoints
- [ ] API DTOs separate from entities
- [ ] Migration strategy identified
- [ ] Useful SQL/query diagnostics available during development

---

# Interview Readiness

Be able to answer:

- [ ] What is Spring Data JPA?
- [ ] Spring Data JPA vs JPA?
- [ ] JPA vs Hibernate?
- [ ] Hibernate vs JDBC?
- [ ] Why use Spring Data JPA?
- [ ] How can Spring create a repository implementation from an interface?
- [ ] What does `JpaRepository<Message, Long>` mean?
- [ ] What methods does `JpaRepository` give you?
- [ ] What is a derived query method?
- [ ] How does `findByName()` work?
- [ ] When does a derived method name become a bad choice?
- [ ] How do you sort repository results?
- [ ] What is `Pageable`?
- [ ] `Page` vs `Slice`?
- [ ] What is `@Query`?
- [ ] JPQL vs native SQL?
- [ ] How are query parameters bound?
- [ ] What is `@Modifying`?
- [ ] Why do modifying queries need transaction awareness?
- [ ] What is a projection?
- [ ] Why use projections?
- [ ] What is `JpaSpecificationExecutor`?
- [ ] When should Specifications be used?
- [ ] Where should `@Transactional` usually go in a layered application?
- [ ] What is the N+1 problem?
- [ ] How can Spring Data JPA help control fetch behavior?
- [ ] What is `@EntityGraph`?
- [ ] What is Spring Data auditing?
- [ ] What does `@DataJpaTest` do?
- [ ] Why shouldn't you mock a repository when testing its query?
- [ ] When should Mockito be used with repositories?
- [ ] Why can an embedded test database hide production bugs?
- [ ] Why shouldn't `ddl-auto: update` be your production migration strategy?
- [ ] What are common Spring Data JPA performance mistakes?
- [ ] Why shouldn't REST controllers expose JPA entities automatically?
- [ ] How would you debug a repository query that fails at startup?
- [ ] How would you debug unexpected extra SQL queries?

---

# Query Technique Cheat Sheet

```text
Requirement                          Preferred starting point
-----------                          ------------------------
CRUD                                 JpaRepository method

Simple fixed lookup                  Derived query

Simple fixed ordering                Derived query / Sort

Caller-controlled ordering           Sort

Paged result                         Pageable

Need total result count              Page

Need "has next" without total        Slice

Complex fixed entity query           @Query + JPQL

Database-specific query              Native SQL

Small read-specific shape            Projection / DTO

Many optional filters                Specification

Relationship fetch optimization      Fetch join / EntityGraph / projection

Multi-step business unit of work     @Transactional service
```

---

# Common Anti-Patterns

## Anti-Pattern 1

```text
"JpaRepository means I don't need to understand JPA."
```

Wrong mental model.

```text
Spring Data JPA
      │
      ▼
simplifies JPA usage
      │
      ▼
does not erase JPA behavior
```

## Anti-Pattern 2

```java
repository.findAll();
```

for every endpoint regardless of table size.

## Anti-Pattern 3

Huge derived method names because "Spring supports it."

## Anti-Pattern 4

Returning persistence entities directly as every REST response.

## Anti-Pattern 5

Putting business decisions inside repository interfaces/queries.

## Anti-Pattern 6

Changing all relationships to `EAGER` to hide lazy-loading problems.

## Anti-Pattern 7

Mocking `JpaRepository` and claiming the repository query was tested.

## Anti-Pattern 8

Using H2-only tests for PostgreSQL-specific native SQL and assuming production compatibility.

## Anti-Pattern 9

Using `ddl-auto: update` as long-term production schema management.

## Anti-Pattern 10

Ignoring generated SQL because the Java repository method looks simple.

---

# Future Branches

```text
Spring Data JPA
      │
      ├──► Database Migrations
      │      ├── Flyway
      │      └── Liquibase
      │
      ├──► Advanced Hibernate
      │      ├── caching
      │      ├── batching
      │      ├── fetch tuning
      │      └── performance
      │
      ├──► Spring Security
      │      └── auditing / authorization-aware data access
      │
      ├──► Spring Data JDBC
      │      └── alternative persistence model
      │
      ├──► Querydsl / advanced dynamic querying
      │
      └──► Database Performance
             ├── indexing
             ├── EXPLAIN
             └── query optimization
```

---

# Mastery Progression

```text
"I can save an entity"
        │
        ▼
"I understand what JpaRepository provides"
        │
        ▼
"I can write derived queries"
        │
        ▼
"I can sort and paginate"
        │
        ▼
"I can write explicit JPQL/native queries"
        │
        ▼
"I can return projections"
        │
        ▼
"I can build dynamic searches"
        │
        ▼
"I understand transaction boundaries"
        │
        ▼
"I can recognize N+1 and fetch problems"
        │
        ▼
"I can test repositories correctly"
        │
        ▼
"I can inspect the SQL/database consequences"
        │
        ▼
"I can design a maintainable repository layer"
```

---

# Mastery Standard

> **Can I design and implement a Spring Data JPA repository layer without treating it as magic: choosing appropriately between inherited CRUD, derived queries, pagination, `@Query`, projections, and Specifications; placing transactions at sensible service boundaries; understanding how JPA/Hibernate behavior affects repository operations; recognizing N+1 and fetch problems; testing repositories against real persistence behavior; and tracing the entire operation down through JPA, Hibernate, JDBC, and the database when something goes wrong?**

At mastery, the persistence branch should be mentally connected like this:

```text
                         Application
                              │
                              ▼
                         Controller
                              │
                              ▼
                           Service
                              │
                    @Transactional
                              │
                              ▼
                      Spring Data JPA
                              │
                 ┌────────────┼─────────────┐
                 ▼            ▼             ▼
             CRUD/Derived   @Query    Specifications
                 │            │             │
                 └────────────┼─────────────┘
                              ▼
                             JPA
                              │
                         Persistence
                           Context
                              │
                              ▼
                          Hibernate
                              │
                              ▼
                            JDBC
                              │
                              ▼
                         PostgreSQL
```
