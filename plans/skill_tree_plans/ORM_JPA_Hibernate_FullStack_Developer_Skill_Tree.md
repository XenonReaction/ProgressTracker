# ORM / JPA / Hibernate Skill Tree — Full-Stack Java Developer Path

> **Goal:** Understand object-relational mapping, use Jakarta Persistence (JPA) correctly, understand Hibernate as a JPA provider, and build/debug persistence code without losing sight of SQL and PostgreSQL behavior.

> **Target:** Professional full-stack developer competency; advanced specialist topics are awareness-level unless needed for application development.

## Dependency / prerequisite map

```text
Java + SQL/PostgreSQL + JDBC fundamentals
                 ↓
                ORM
                 ↓
      Jakarta Persistence (JPA)
                 ↓
             Hibernate
                 ↓
          Spring Data JPA
        (separate later tree)
```

## Skill Tree Overview

### 1. Terminology & Layering
- [ ] ORM = general object-relational mapping technique
- [ ] JPA/Jakarta Persistence = Java persistence specification/API
- [ ] Hibernate ORM = common JPA provider/implementation
- [ ] Spring Data JPA = separate Spring abstraction built on JPA
- [ ] JDBC remains underneath database communication
- [ ] Do not use ORM/JPA/Hibernate as synonyms

### 2. Why ORM Exists
- [ ] Relational tables vs object graphs
- [ ] Manual JDBC row mapping
- [ ] Identity and relationships
- [ ] Persistence boilerplate reduction
- [ ] Unit-of-work/change tracking concept
- [ ] ORM tradeoffs and abstraction leaks
- [ ] SQL knowledge remains necessary

### 3. JPA Architecture
- [ ] EntityManager
- [ ] EntityManagerFactory awareness
- [ ] Persistence context
- [ ] Entity
- [ ] Transaction relationship
- [ ] Provider
- [ ] Database/JDBC relationship

### 4. Entity Basics
- [ ] @Entity
- [ ] Entity class requirements/practical conventions
- [ ] @Table
- [ ] Entity name vs table name
- [ ] No-arg constructor requirement awareness
- [ ] Entity identity
- [ ] Avoid treating entities as arbitrary DTOs

### 5. Primary Keys & Identity
- [ ] @Id
- [ ] @GeneratedValue
- [ ] Generation strategies awareness
- [ ] PostgreSQL identity/sequence relationship
- [ ] Natural vs surrogate key concepts
- [ ] Composite key awareness without deep mastery
- [ ] Entity equality/hashCode implications awareness

### 6. Basic Field Mapping
- [ ] @Column
- [ ] Column names
- [ ] nullable
- [ ] unique awareness
- [ ] length/precision/scale
- [ ] @Transient
- [ ] Default mapping conventions
- [ ] Schema constraints should still exist in database

### 7. Data Types
- [ ] Strings/numerics/booleans
- [ ] BigDecimal
- [ ] LocalDate
- [ ] LocalTime
- [ ] LocalDateTime
- [ ] Instant/OffsetDateTime awareness
- [ ] UUID
- [ ] Enum mapping with @Enumerated
- [ ] LOB awareness
- [ ] PostgreSQL-specific JSON/array mapping awareness as advanced/provider-specific

### 8. Persistence Context
- [ ] Managed entity
- [ ] Persistence context identity
- [ ] First-level cache
- [ ] find/persist/remove operations
- [ ] Entity state tracking
- [ ] Persistence context lifetime concept
- [ ] Why repeated finds may not hit database

### 9. Entity Lifecycle States
- [ ] Transient/new
- [ ] Managed/persistent
- [ ] Detached
- [ ] Removed
- [ ] Transitions between states
- [ ] Understand detached-entity problems

### 10. EntityManager CRUD
- [ ] persist()
- [ ] find()
- [ ] remove()
- [ ] merge() and why it is often misunderstood
- [ ] flush()
- [ ] clear()
- [ ] detach() awareness
- [ ] refresh() awareness

### 11. Dirty Checking & Flush
- [ ] Managed entity changes
- [ ] Dirty checking
- [ ] Flush vs commit
- [ ] Automatic flush awareness
- [ ] SQL may execute later than the Java line that changed an entity
- [ ] Diagnose constraint errors that appear at flush/commit

### 12. Relationships Overview
- [ ] Relational foreign keys vs object references
- [ ] @OneToOne
- [ ] @OneToMany
- [ ] @ManyToOne
- [ ] @ManyToMany
- [ ] Choose mappings from actual data cardinality
- [ ] Understand join tables

### 13. Owning Side & mappedBy
- [ ] Owning side controls relationship mapping
- [ ] mappedBy identifies inverse side
- [ ] Foreign-key placement
- [ ] Bidirectional vs unidirectional mappings
- [ ] Keep both sides consistent in Java when using bidirectional relationships

### 14. Cascade Operations
- [ ] CascadeType concepts
- [ ] PERSIST
- [ ] MERGE
- [ ] REMOVE
- [ ] ALL awareness
- [ ] Cascade is about entity operations, not database FK cascade equivalence
- [ ] Avoid accidental cascading deletes

### 15. Orphan Removal
- [ ] orphanRemoval concept
- [ ] Difference from cascade remove
- [ ] Use only when child lifecycle is truly owned

### 16. Fetching
- [ ] LAZY vs EAGER
- [ ] Association loading
- [ ] Proxy/lazy-loading concept
- [ ] Session/persistence-context requirement
- [ ] Avoid globally switching everything to EAGER
- [ ] Fetch according to use case

### 17. N+1 Query Problem
- [ ] Recognize N+1 pattern
- [ ] Observe generated SQL
- [ ] Understand why object navigation can cause many queries
- [ ] Fetch joins/entity graphs awareness
- [ ] Fix query shape rather than hiding symptoms

### 18. JPQL
- [ ] Entity-oriented query language
- [ ] Query entities/fields rather than table/column names
- [ ] SELECT
- [ ] WHERE
- [ ] JOIN
- [ ] Parameters
- [ ] ORDER BY
- [ ] Aggregates awareness
- [ ] TypedQuery awareness

### 19. Native SQL
- [ ] When native SQL is appropriate
- [ ] Trade portability for database capability/control
- [ ] Parameter binding
- [ ] Result mapping awareness
- [ ] Do not abandon SQL skills because JPA exists

### 20. Transactions
- [ ] Persistence operations and transaction boundaries
- [ ] Commit/rollback
- [ ] Persistence context interaction
- [ ] Atomic business operations
- [ ] Spring @Transactional relationship awareness
- [ ] Leave Spring proxy details to Spring/AOP trees

### 21. Locking & Concurrency
- [ ] Optimistic locking
- [ ] @Version
- [ ] Lost-update prevention concept
- [ ] OptimisticLockException awareness
- [ ] Pessimistic locking awareness
- [ ] Database isolation remains relevant
- [ ] Java thread locks are not database transaction locks

### 22. Hibernate as Provider
- [ ] Hibernate implements Jakarta Persistence plus extensions
- [ ] Session awareness as Hibernate-native analogue
- [ ] Generated SQL
- [ ] Dialect concept
- [ ] Hibernate configuration normally mediated by Spring Boot in Spring apps
- [ ] Prefer standard JPA APIs when provider portability/clarity matters

### 23. Hibernate SQL Generation
- [ ] Observe generated SQL
- [ ] Understand inserts/updates/selects generated from entity operations
- [ ] DDL generation awareness
- [ ] Do not assume generated SQL is optimal
- [ ] Know when to inspect query plans at PostgreSQL layer

### 24. Schema Generation vs Migrations
- [ ] ddl-auto/schema generation modes awareness
- [ ] Useful for learning/tests in some contexts
- [ ] Do not rely on automatic schema mutation as a professional production migration strategy
- [ ] Migration tools such as Flyway/Liquibase awareness for later tree/section

### 25. Caching
- [ ] Persistence-context first-level cache
- [ ] Second-level cache awareness
- [ ] Query cache awareness
- [ ] Caching introduces consistency/complexity tradeoffs
- [ ] Do not enable caches without a measured reason

### 26. Entity Design
- [ ] Keep entities valid/understand invariants
- [ ] Avoid huge object graphs
- [ ] Be careful with Lombok-generated equals/toString on relationships awareness
- [ ] DTOs for API boundaries
- [ ] Avoid serializing lazy entity graphs directly

### 27. Performance
- [ ] Query count
- [ ] Fetch strategy
- [ ] Batching awareness
- [ ] Pagination
- [ ] Indexes remain database responsibility
- [ ] Bulk operations awareness
- [ ] Persistence-context growth in large jobs
- [ ] Measure generated SQL and database behavior

### 28. Error Handling
- [ ] Constraint violations
- [ ] Entity not found semantics
- [ ] Lazy initialization failures
- [ ] Optimistic locking failures
- [ ] Mapping errors
- [ ] Transaction rollback
- [ ] Read provider exception and underlying SQL/database cause

### 29. Testing Persistence
- [ ] JUnit integration
- [ ] Repository/persistence integration tests
- [ ] Use realistic relational behavior
- [ ] Test mappings and constraints
- [ ] Test relationship persistence
- [ ] Test transaction behavior
- [ ] Avoid mocking EntityManager to “prove” SQL works

### 30. Debugging Workflow
- [ ] Inspect entity mapping
- [ ] Inspect generated SQL
- [ ] Inspect bound parameters/logging carefully
- [ ] Run equivalent SQL against PostgreSQL
- [ ] Check transaction/persistence-context state
- [ ] Check fetch behavior
- [ ] Check database constraints/indexes
- [ ] Separate JPA issue from SQL/database issue

### 31. Professional Boundaries
- [ ] Know when JDBC/native SQL is simpler
- [ ] Do not force every query into an object graph
- [ ] Keep persistence details behind repository/data-access boundaries
- [ ] Understand ORM convenience vs performance/control tradeoff
- [ ] Spring Data JPA belongs in its own later skill tree

## Practical competency checkpoints

- [ ] Explain ORM vs JPA vs Hibernate vs Spring Data JPA
- [ ] Map a basic PostgreSQL table to an entity
- [ ] Perform CRUD through EntityManager conceptually/independently
- [ ] Explain transient/managed/detached/removed states
- [ ] Map one-to-many/many-to-one correctly including owning side
- [ ] Explain LAZY/EAGER and diagnose N+1
- [ ] Write a parameterized JPQL query
- [ ] Explain dirty checking and flush vs commit
- [ ] Use @Version for optimistic locking conceptually
- [ ] Inspect generated SQL and trace an ORM problem down to PostgreSQL
- [ ] Explain when native SQL/JDBC is preferable

## Mastery standard

> Can I build, explain, debug, and make appropriate design choices in this domain without relying on a step-by-step tutorial, while recognizing what adjacent frameworks or abstractions are doing on my behalf?
