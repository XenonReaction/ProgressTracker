# Neo4j Skill Tree — Full-Stack Developer Path

> **Goal:** Progress from database and graph fundamentals to professional, application-level Neo4j proficiency, including Cypher, graph data modeling, performance, transactions, and Java application integration.
>
> **Learning context:** Neo4j is the graph-database branch of the full-stack path. It should be learned as a database technology in parallel with relational/document database specializations rather than as a replacement for them.
>
> **Target level:** Professional full-stack developer — not Neo4j database-engine engineer, distributed-systems specialist, or graph-algorithm researcher.
>
> **Learning progression:**  
> `Database Fundamentals → Graph Concepts → Neo4j → Cypher → Graph Modeling → Performance → Java Driver → Spring Data Neo4j`
>
> **Scope boundary:** Spring Data Neo4j should have its own later skill tree. Graph Data Science is introduced at awareness level here and can become a separate specialization.

---

# Skill Tree Overview

```text
Neo4j
├── 1. Database & Graph Prerequisites
├── 2. Graph Database Fundamentals
├── 3. Neo4j Concepts & Architecture
├── 4. Installation & Local Development
├── 5. Neo4j Tooling
├── 6. Property Graph Model
├── 7. Cypher Fundamentals
├── 8. MATCH & RETURN
├── 9. Filtering, Ordering & Pagination
├── 10. CREATE, MERGE, SET & DELETE
├── 11. Relationship Traversal
├── 12. Variable-Length Paths
├── 13. Aggregation
├── 14. Cypher Data Types, Lists & Maps
├── 15. OPTIONAL MATCH
├── 16. WITH & Query Pipelines
├── 17. Subqueries
├── 18. Graph Data Modeling
├── 19. Relationship Modeling
├── 20. Constraints & Indexes
├── 21. Transactions & Consistency
├── 22. Query Planning & Performance
├── 23. EXPLAIN & PROFILE
├── 24. Neo4j Java Driver
├── 25. Parameters & Security
├── 26. Repository / Data-Access Architecture
├── 27. Testing
├── 28. Import, Export & Bulk Data
├── 29. Operations & Administration Awareness
├── 30. Graph Data Science Awareness
├── 31. Choosing Neo4j vs Other Databases
├── 32. Debugging & Professional Practices
└── 33. Spring Data Neo4j Readiness
```

# Dependency Map

```text
                 Database Fundamentals
                         │
             ┌───────────┴───────────┐
             ▼                       ▼
       Relational Model         Graph Concepts
             │                       │
             ▼                       ▼
        PostgreSQL                  Neo4j
                                     │
                          ┌──────────┴──────────┐
                          ▼                     ▼
                       Cypher             Graph Modeling
                          │                     │
                          └──────────┬──────────┘
                                     ▼
                          Constraints / Indexes
                                     │
                                     ▼
                        Transactions / Performance
                                     │
                                     ▼
                           Neo4j Java Driver
                                     │
                                     ▼
                          Spring Data Neo4j
                           (separate tree)
```

A useful comparison with the other persistence branches is:

```text
PostgreSQL ──► JDBC ──► JPA / Hibernate ──► Spring Data JPA

MongoDB ─────► Driver / Data Access
              (not covered as a separate skill tree in this curriculum —
               MongoDB is taught via its native driver only)

Neo4j ───────► Neo4j Java Driver ─────────► Spring Data Neo4j
```

---

# Tier 0 — Prerequisites

## 1. Database Prerequisites

- [ ] Understand what a database is
- [ ] Understand persistent data
- [ ] Understand records/entities conceptually
- [ ] Understand identifiers
- [ ] Understand relationships between pieces of data
- [ ] Understand CRUD
- [ ] Understand queries
- [ ] Understand indexes conceptually
- [ ] Understand constraints conceptually
- [ ] Understand transactions conceptually
- [ ] Understand client/server database architecture
- [ ] Have basic experience with at least one database system

### Helpful relational knowledge

- [ ] Tables
- [ ] Rows
- [ ] Columns
- [ ] Primary keys
- [ ] Foreign keys
- [ ] Joins
- [ ] Normalization at a practical level
- [ ] Understand that Neo4j models connected data differently rather than making relational knowledge obsolete

**Checkpoint:** Explain how two related entities would be represented in a relational database before comparing that representation with a graph.

---

# Tier 1 — Graph Database Foundations

## 2. What a Graph Database Is

A graph database emphasizes **entities and the relationships connecting them**.

Basic model:

```text
(Node) ──[RELATIONSHIP]──> (Node)
```

Example:

```text
(Alice:Person)
      │
      │ WORKS_AT
      ▼
(Acme:Company)
```

- [ ] Explain what a graph database is
- [ ] Explain why connected data is important
- [ ] Understand nodes
- [ ] Understand relationships
- [ ] Understand properties
- [ ] Understand labels
- [ ] Understand relationship types
- [ ] Understand relationship direction
- [ ] Understand paths
- [ ] Understand traversal
- [ ] Distinguish a graph database from a graph data structure used only in application memory

## 3. Why Use a Graph Database?

Recognize domains where relationships are central:

- [ ] Social networks
- [ ] Recommendation systems
- [ ] Fraud/network analysis
- [ ] Identity and access relationships
- [ ] Knowledge graphs
- [ ] Dependency graphs
- [ ] Network/infrastructure relationships
- [ ] Supply chains
- [ ] Organizational relationships
- [ ] Connected product/catalog data

Understand that Neo4j is not automatically preferable simply because data contains relationships.

### Mental comparison

```text
Relational thinking:
Person table
Company table
Employment table
JOIN tables to reconstruct relationship

Graph thinking:
(:Person)-[:WORKS_AT]->(:Company)
relationship is directly represented in the model
```

**Checkpoint:** Identify three domains that naturally benefit from graph modeling and explain why.

---

# Tier 2 — Neo4j Fundamentals

## 4. What Neo4j Is

- [ ] Explain Neo4j as a graph database management system
- [ ] Understand Neo4j's property graph model
- [ ] Understand that Cypher is its primary graph query language
- [ ] Understand Neo4j server vs client/application
- [ ] Understand database persistence vs in-memory graph objects
- [ ] Understand that Neo4j can be accessed through drivers and application frameworks

### Mental model

```text
Application
    │
    ▼
Neo4j Driver / Protocol
    │
    ▼
Neo4j Database
    │
    ├── nodes
    ├── relationships
    ├── properties
    ├── indexes
    └── constraints
```

## 5. Neo4j Architecture — Practical Level

- [ ] Database/server concept
- [ ] Database instances/databases awareness
- [ ] Driver connections
- [ ] Sessions
- [ ] Transactions
- [ ] Query execution
- [ ] Storage awareness
- [ ] Cluster/distributed deployment awareness
- [ ] Do not require internal storage-engine mastery

---

# Tier 3 — Installation & Development Environment

## 6. Local Neo4j Development

- [ ] Install/run Neo4j locally using an appropriate supported method
- [ ] Recognize Neo4j Desktop
- [ ] Recognize containerized Neo4j development
- [ ] Understand local database ports/configuration
- [ ] Start and stop a local database
- [ ] Connect to a local database
- [ ] Understand authentication credentials
- [ ] Keep secrets outside committed source code
- [ ] Understand persistent storage/volumes when containerized

## 7. Development Tooling

Become comfortable with available Neo4j query/administration interfaces.

- [ ] Execute Cypher interactively
- [ ] Inspect query results
- [ ] View nodes and relationships visually
- [ ] Read tabular query output
- [ ] Inspect schema/index information
- [ ] Distinguish visualization from the underlying stored data
- [ ] Use command-line tooling awareness where appropriate

**Checkpoint:** Start Neo4j locally, connect to it, execute a query, stop it, and explain each step.

---

# Tier 4 — Property Graph Model

## 8. Nodes

A node represents an entity.

```cypher
(:Person)
```

With properties:

```cypher
(:Person {
    name: "Alice",
    age: 30
})
```

- [ ] Create nodes
- [ ] Understand node identity
- [ ] Understand labels
- [ ] Understand multiple labels conceptually
- [ ] Understand properties
- [ ] Understand that labels help describe/classify nodes

## 9. Relationships

```cypher
(:Person)-[:WORKS_AT]->(:Company)
```

Relationships can also have properties:

```cypher
(:Person)-[:WORKS_AT {
    since: 2024
}]->(:Company)
```

- [ ] Relationship type
- [ ] Direction
- [ ] Start node
- [ ] End node
- [ ] Relationship properties
- [ ] Understand relationships as first-class stored structures
- [ ] Understand that query patterns can traverse relationships in useful directions regardless of modeling intent where syntax permits

## 10. Properties

- [ ] Node properties
- [ ] Relationship properties
- [ ] Property values
- [ ] Missing properties
- [ ] `null` behavior awareness
- [ ] Choose properties based on entity/relationship meaning

**Checkpoint:** Model people, companies, and employment relationships as a small property graph.

---

# Tier 5 — Cypher Fundamentals

## 11. Cypher Mental Model

Cypher expresses graph patterns visually.

```cypher
MATCH (p:Person)-[:WORKS_AT]->(c:Company)
RETURN p.name, c.name
```

Conceptually:

```text
MATCH   → find graph pattern
WHERE   → filter
RETURN  → choose output
```

- [ ] Read node patterns
- [ ] Read relationship patterns
- [ ] Assign variables
- [ ] Match labels
- [ ] Match relationship types
- [ ] Return properties
- [ ] Understand query clauses
- [ ] Understand pattern-oriented querying

## 12. Node Pattern Syntax

```cypher
(n)
```

```cypher
(n:Person)
```

```cypher
(n:Person {name: "Alice"})
```

Know:

```text
()            node
n             variable
:Person       label
{name: ...}   property map
```

## 13. Relationship Pattern Syntax

```cypher
(a)-[r]->(b)
```

Typed:

```cypher
(a)-[r:KNOWS]->(b)
```

Without relationship variable:

```cypher
(a)-[:KNOWS]->(b)
```

- [ ] Directed patterns
- [ ] Undirected matching awareness
- [ ] Relationship variables
- [ ] Relationship types
- [ ] Relationship properties

---

# Tier 6 — Reading Data

## 14. `MATCH`

```cypher
MATCH (p:Person)
RETURN p
```

- [ ] Match nodes
- [ ] Match labels
- [ ] Match relationships
- [ ] Match multi-node patterns
- [ ] Bind matched graph elements to variables
- [ ] Understand that patterns describe desired graph structure

## 15. `RETURN`

```cypher
MATCH (p:Person)
RETURN p.name
```

- [ ] Return nodes
- [ ] Return relationships
- [ ] Return properties
- [ ] Return expressions
- [ ] Alias output with `AS`
- [ ] Return distinct results

Example:

```cypher
MATCH (p:Person)
RETURN p.name AS name
```

## 16. `DISTINCT`

```cypher
MATCH (p:Person)-[:WORKS_AT]->(c:Company)
RETURN DISTINCT c.name
```

- [ ] Understand duplicate rows
- [ ] Remove duplicates intentionally
- [ ] Avoid using `DISTINCT` merely to hide incorrect query patterns

**Checkpoint:** Write queries that retrieve nodes, relationships, and selected properties from a small graph.

---

# Tier 7 — Filtering, Ordering & Pagination

## 17. `WHERE`

```cypher
MATCH (p:Person)
WHERE p.age >= 18
RETURN p
```

- [ ] Equality/comparison
- [ ] Boolean conditions
- [ ] `AND`
- [ ] `OR`
- [ ] `NOT`
- [ ] Property existence/null reasoning
- [ ] String filtering
- [ ] List membership awareness
- [ ] Filter based on graph patterns where appropriate

## 18. Ordering

```cypher
MATCH (p:Person)
RETURN p.name, p.age
ORDER BY p.age DESC
```

- [ ] `ORDER BY`
- [ ] Ascending
- [ ] Descending
- [ ] Multiple sort expressions

## 19. Pagination

- [ ] `SKIP`
- [ ] `LIMIT`
- [ ] Understand pagination requirements
- [ ] Use deterministic ordering where pagination correctness matters
- [ ] Recognize large-offset pagination performance considerations

---

# Tier 8 — Creating & Modifying Data

## 20. `CREATE`

```cypher
CREATE (:Person {
    name: "Alice",
    age: 30
})
```

Relationship:

```cypher
MATCH (p:Person {name: "Alice"}),
      (c:Company {name: "Acme"})
CREATE (p)-[:WORKS_AT]->(c)
```

- [ ] Create nodes
- [ ] Create relationships
- [ ] Set initial properties
- [ ] Understand duplicate-data risks

## 21. `MERGE`

```cypher
MERGE (p:Person {id: $id})
```

- [ ] Understand match-or-create semantics
- [ ] Distinguish `MERGE` from `CREATE`
- [ ] Use stable identifying properties
- [ ] Understand why constraints often matter with `MERGE`
- [ ] `ON CREATE` awareness
- [ ] `ON MATCH` awareness
- [ ] Avoid assuming `MERGE` automatically solves every uniqueness problem

## 22. `SET`

```cypher
MATCH (p:Person {id: $id})
SET p.name = $name
RETURN p
```

- [ ] Update properties
- [ ] Add/change labels awareness
- [ ] Update multiple values
- [ ] Understand property replacement vs individual updates

## 23. Removing Properties / Labels

- [ ] `REMOVE`
- [ ] Remove a property
- [ ] Remove a label
- [ ] Understand effect on matching/model semantics

## 24. `DELETE` and `DETACH DELETE`

```cypher
MATCH (p:Person {id: $id})
DELETE p
```

- [ ] Delete relationships
- [ ] Delete nodes
- [ ] Understand why connected nodes cannot simply be deleted in some situations
- [ ] Understand `DETACH DELETE`
- [ ] Use destructive queries carefully

**Checkpoint:** Implement CRUD operations for a small graph model.

---

# Tier 9 — Traversal & Paths

## 25. Relationship Traversal

```cypher
MATCH (p:Person)-[:KNOWS]->(friend:Person)
RETURN friend
```

- [ ] Traverse one relationship
- [ ] Traverse multiple relationship types where appropriate
- [ ] Traverse multiple hops
- [ ] Understand direction
- [ ] Understand pattern chains

Example:

```cypher
MATCH (p:Person)-[:KNOWS]->(:Person)-[:WORKS_AT]->(c:Company)
RETURN c
```

## 26. Paths

```cypher
MATCH path =
    (a:Person)-[:KNOWS]->(b:Person)
RETURN path
```

- [ ] Bind paths to variables
- [ ] Understand nodes in a path
- [ ] Understand relationships in a path
- [ ] Path length awareness
- [ ] Recognize path querying as a core graph capability

## 27. Variable-Length Patterns

Conceptually:

```cypher
MATCH (a:Person)-[:KNOWS*1..3]->(b:Person)
RETURN b
```

- [ ] Understand bounded variable-length traversal
- [ ] Understand minimum/maximum hops
- [ ] Understand path explosion risk
- [ ] Prefer bounded searches when domain semantics permit
- [ ] Learn current Cypher path-pattern syntax and semantics when implementing production queries

## 28. Shortest-Path Concepts

- [ ] Shortest path awareness
- [ ] Understand path constraints
- [ ] Recognize weighted graph algorithms as a different problem from basic path matching
- [ ] Leave advanced algorithms to Graph Data Science specialization

**Checkpoint:** Query friends, friends-of-friends, and bounded relationship paths while explaining what each pattern means.

---

# Tier 10 — Aggregation

## 29. Aggregation Functions

Know common concepts/functions:

- [ ] `count()`
- [ ] `sum()`
- [ ] `avg()`
- [ ] `min()`
- [ ] `max()`
- [ ] `collect()`

Example:

```cypher
MATCH (p:Person)-[:WORKS_AT]->(c:Company)
RETURN c.name, count(p) AS employees
```

## 30. Grouping

- [ ] Understand grouping from non-aggregated returned expressions
- [ ] Group results by node/property
- [ ] Count relationships/entities
- [ ] Aggregate after filtering
- [ ] Understand how query shape changes aggregation results

**Checkpoint:** Produce counts and grouped summaries from connected data.

---

# Tier 11 — Cypher Values, Lists & Maps

## 31. Common Data Types

Understand practical Cypher values such as:

- [ ] String
- [ ] Integer
- [ ] Floating-point
- [ ] Boolean
- [ ] `null`
- [ ] Lists
- [ ] Maps
- [ ] Temporal values
- [ ] Spatial values awareness
- [ ] Nodes
- [ ] Relationships
- [ ] Paths

## 32. Lists

Example:

```cypher
RETURN [1, 2, 3]
```

- [ ] List literals
- [ ] Access elements
- [ ] Membership
- [ ] `collect()`
- [ ] List expressions/comprehensions awareness
- [ ] `UNWIND`

## 33. `UNWIND`

```cypher
UNWIND $people AS person
CREATE (:Person {
    id: person.id,
    name: person.name
})
```

- [ ] Convert a list into rows
- [ ] Use parameterized collections
- [ ] Understand usefulness for batch-style operations

## 34. Maps

```cypher
RETURN {
    name: "Alice",
    age: 30
}
```

- [ ] Map values
- [ ] Parameter maps
- [ ] Property maps
- [ ] Map projection awareness
- [ ] Understand maps as useful application/query result structures

---

# Tier 12 — Optional Data

## 35. `OPTIONAL MATCH`

```cypher
MATCH (p:Person)
OPTIONAL MATCH (p)-[:WORKS_AT]->(c:Company)
RETURN p.name, c.name
```

- [ ] Understand optional pattern matching
- [ ] Understand missing matches
- [ ] Understand resulting `null` values
- [ ] Compare conceptually with outer-join behavior without assuming identical mechanics
- [ ] Place filtering carefully so optional data is not accidentally eliminated

**Checkpoint:** Return all people whether or not they have a related company.

---

# Tier 13 — Query Pipelines

## 36. `WITH`

`WITH` passes selected values from one stage of a query to another.

```cypher
MATCH (p:Person)-[:WORKS_AT]->(c:Company)
WITH c, count(p) AS employeeCount
WHERE employeeCount > 10
RETURN c.name, employeeCount
```

- [ ] Pass variables between query stages
- [ ] Aggregate before later filtering
- [ ] Rename values
- [ ] Control variable scope
- [ ] Order/limit intermediate results
- [ ] Understand `WITH` as a major Cypher composition tool

### Mental model

```text
MATCH
  │
  ▼
rows
  │
  ▼
WITH
  │ transform/filter/aggregate
  ▼
new rows
  │
  ▼
MATCH / WHERE / RETURN
```

## 37. Query Composition

- [ ] Break complicated queries into understandable stages
- [ ] Keep variable scope intentional
- [ ] Avoid excessively clever single-clause expressions
- [ ] Read a Cypher query as a pipeline of row transformations plus graph matching

---

# Tier 14 — Subqueries

## 38. Subquery Concepts

`CALL { ... }` isolates part of a query, optionally importing outer variables into its scope:

```cypher
MATCH (p:Person)
CALL {
  WITH p
  MATCH (p)-[:WROTE]->(post:Post)
  RETURN count(post) AS postCount
}
RETURN p.name, postCount
```

- [ ] Understand why subqueries exist
- [ ] Isolate part of a query
- [ ] Understand variable import/scope
- [ ] Return values from subqueries
- [ ] Use subqueries where they improve correctness or clarity
- [ ] Recognize that current Cypher syntax evolves; consult current documentation for exact advanced forms

## 39. Appropriate Uses

- [ ] Per-row computation
- [ ] Isolated aggregation
- [ ] Complex query composition
- [ ] Conditional/query-structure needs where supported
- [ ] Avoid subqueries when a simple pattern or `WITH` is clearer

---

# Tier 15 — Graph Data Modeling

## 40. Modeling Nodes

Ask:

```text
Is this thing an entity with identity?
Does it participate in relationships?
Will it be traversed/searched independently?
```

Possible node:

```text
Person
Company
Product
Category
Account
Device
Location
```

- [ ] Choose meaningful labels
- [ ] Choose stable identifiers
- [ ] Avoid creating nodes for every primitive value
- [ ] Understand entity identity
- [ ] Model for expected queries as well as conceptual correctness

## 41. Modeling Relationships

Ask:

```text
Is this fact fundamentally a connection between entities?
Does the connection have meaning?
Does the connection have properties?
Will applications traverse it?
```

Example:

```text
(Person)-[:WORKS_AT]->(Company)
```

rather than storing only:

```text
Person.companyId
```

when the relationship itself is central to the graph model.

## 42. Properties vs Nodes vs Relationships

Learn to decide whether information belongs as:

```text
property
node
relationship
relationship property
```

Example:

```text
Alice worked at Acme beginning in 2024
```

Possible model:

```text
(:Person {name: "Alice"})
   │
   └─[:WORKS_AT {since: 2024}]─►
(:Company {name: "Acme"})
```

## 43. Labels

- [ ] Use labels to classify nodes
- [ ] Multiple labels awareness
- [ ] Avoid treating labels as arbitrary tags without considering query/schema use
- [ ] Understand labels' relationship to indexes/constraints

## 44. Directionality

Model relationships with meaningful direction:

```text
(Person)-[:PURCHASED]->(Product)
(Employee)-[:REPORTS_TO]->(Manager)
(Account)-[:TRANSFERRED_TO]->(Account)
```

- [ ] Choose semantic direction
- [ ] Keep conventions consistent
- [ ] Understand that query traversal needs may differ from stored semantic direction

## 45. Dense / Highly Connected Areas

- [ ] Recognize highly connected nodes
- [ ] Understand that model/query shape can affect traversal cost
- [ ] Avoid blindly modeling every possible connection
- [ ] Measure real workloads

**Checkpoint:** Design a graph model from a written domain description and justify every node, relationship, label, and major property.

---

# Tier 16 — Constraints & Indexes

## 46. Constraints

Understand schema constraints as mechanisms for protecting important assumptions.

- [ ] Uniqueness
- [ ] Existence/type-related constraint awareness where supported
- [ ] Node/property constraints
- [ ] Relationship constraints awareness
- [ ] Use stable application identifiers
- [ ] Understand that constraints are not replaced by application validation

Example intent:

```text
Every Person.id must uniquely identify a Person.
```

```cypher
CREATE CONSTRAINT person_id_unique IF NOT EXISTS
FOR (p:Person)
REQUIRE p.id IS UNIQUE
```

## 47. Indexes

```cypher
CREATE INDEX person_name_index IF NOT EXISTS
FOR (p:Person)
ON (p.name)
```

- [ ] Understand why indexes exist
- [ ] Understand indexed lookup vs broad scanning conceptually
- [ ] Create appropriate indexes
- [ ] Understand label/property relevance
- [ ] Avoid indexing everything
- [ ] Understand write/storage tradeoffs
- [ ] Recognize specialized index types/features at awareness level

## 48. Constraints + `MERGE`

- [ ] Understand why uniqueness constraints strengthen concurrency/correctness around identity
- [ ] Avoid assuming `MERGE` alone establishes a global uniqueness rule
- [ ] Model identifiers deliberately

**Checkpoint:** Add appropriate schema rules to the graph model from the previous checkpoint.

---

# Tier 17 — Transactions & Consistency

## 49. Transaction Fundamentals

- [ ] Atomicity concept
- [ ] Commit
- [ ] Rollback
- [ ] Transaction boundaries
- [ ] Understand that multiple writes may need to succeed/fail together
- [ ] Understand read/write operations inside transactions

## 50. Neo4j Transactions

- [ ] Auto-commit query awareness
- [ ] Explicit transaction awareness
- [ ] Managed transaction functions in drivers
- [ ] Commit/rollback behavior
- [ ] Transaction retry awareness
- [ ] Keep transactions appropriately scoped

## 51. Concurrency Awareness

- [ ] Concurrent updates
- [ ] Locks/contention awareness
- [ ] Deadlock/retry awareness
- [ ] Uniqueness under concurrency
- [ ] Do not confuse Java thread synchronization with database transaction guarantees

**Checkpoint:** Explain why creating several connected entities may need one transaction.

---

# Tier 18 — Query Planning & Performance

## 52. Performance Mental Model

Performance depends on more than the number of Cypher lines.

Think about:

```text
starting-point selectivity
        ↓
index/constraint lookup
        ↓
matched nodes
        ↓
relationship expansion
        ↓
intermediate rows
        ↓
aggregation/sorting
        ↓
returned result
```

- [ ] Start from selective patterns when appropriate
- [ ] Reduce unnecessary intermediate results
- [ ] Avoid accidental Cartesian products
- [ ] Bound traversals where possible
- [ ] Return only required data
- [ ] Understand index use
- [ ] Understand relationship expansion cost
- [ ] Measure instead of guessing

## 53. Cartesian Products

Recognize suspicious patterns such as unrelated matches that multiply rows.

- [ ] Understand Cartesian-product concept
- [ ] Identify accidental disconnected patterns
- [ ] Connect patterns or restructure queries
- [ ] Read planner warnings where available

## 54. Large Traversals

- [ ] Understand branching factor
- [ ] Understand variable-depth expansion cost
- [ ] Filter intelligently
- [ ] Bound depth
- [ ] Avoid unrestricted exploration of huge connected regions without purpose

---

# Tier 19 — `EXPLAIN` & `PROFILE`

## 55. `EXPLAIN`

Use `EXPLAIN` to inspect the planned query without treating execution as the goal.

- [ ] Read query plan conceptually
- [ ] Identify scans
- [ ] Identify index-backed operations
- [ ] Identify expansions
- [ ] Identify sorts/aggregations
- [ ] Identify suspicious row growth

## 56. `PROFILE`

Use `PROFILE` to inspect actual execution behavior.

- [ ] Compare estimated/planned behavior with actual work
- [ ] Inspect row counts
- [ ] Identify expensive operators
- [ ] Confirm whether an index is useful
- [ ] Optimize based on measured behavior

### Debugging loop

```text
Slow query
   │
   ▼
EXPLAIN
   │
   ▼
Inspect plan
   │
   ▼
PROFILE when appropriate
   │
   ▼
Change query / model / index
   │
   ▼
Measure again
```

**Checkpoint:** Diagnose and improve at least one intentionally inefficient query.

---

# Tier 20 — Neo4j Java Driver

## 57. Driver Purpose

```text
Java Application
       │
       ▼
 Neo4j Java Driver
       │
       ▼
     Neo4j
```

- [ ] Understand what a database driver does
- [ ] Add the Neo4j Java driver using Maven
- [ ] Configure URI/credentials
- [ ] Create a driver
- [ ] Understand driver lifetime
- [ ] Open/use sessions appropriately
- [ ] Execute queries
- [ ] Read results
- [ ] Close resources appropriately

## 58. Driver / Session / Transaction Model

```text
Driver
  │
  ├── Session
  │      │
  │      └── Transaction
  │              │
  │              └── Cypher
  │
  └── Session ...
```

- [ ] Driver as long-lived application resource
- [ ] Sessions as units of database interaction/context
- [ ] Transactions
- [ ] Managed transaction functions
- [ ] Result records
- [ ] Database selection awareness

## 59. Reading Results in Java

- [ ] Records
- [ ] Values
- [ ] Strings/numbers/booleans
- [ ] Lists/maps
- [ ] Node/relationship values awareness
- [ ] Map results into domain/DTO objects
- [ ] Keep database-specific mapping out of unrelated application layers

## 60. Error Handling

- [ ] Driver exceptions
- [ ] Connectivity failures
- [ ] Authentication failures
- [ ] Query syntax errors
- [ ] Constraint failures
- [ ] Transaction failures
- [ ] Retryable failures awareness
- [ ] Read root cause rather than wrapping everything in generic exceptions

**Checkpoint:** Write a plain Java/Maven program that performs parameterized CRUD/query operations against Neo4j.

---

# Tier 21 — Parameters & Security

## 61. Parameterized Cypher

Prefer:

```cypher
MATCH (p:Person {id: $id})
RETURN p
```

with:

```text
id = application-provided parameter
```

rather than concatenating untrusted values into query strings.

- [ ] `$parameter` syntax
- [ ] Scalar parameters
- [ ] Lists
- [ ] Maps
- [ ] Reuse query structure
- [ ] Understand injection risk from unsafe query construction

## 62. Credential Security

- [ ] Do not commit database passwords
- [ ] Environment/configuration variables
- [ ] Secret-management awareness
- [ ] Least-privilege awareness
- [ ] TLS/encrypted connection awareness
- [ ] Authentication vs authorization
- [ ] Do not expose database credentials to frontend/browser code

## 63. Query Security

- [ ] Validate application inputs
- [ ] Parameterize values
- [ ] Be cautious when dynamically constructing labels/types/query structure
- [ ] Limit database privileges
- [ ] Avoid returning sensitive data unnecessarily
- [ ] Logging should not expose secrets

---

# Tier 22 — Repository / Data-Access Architecture

## 64. Separate Database Access

```text
Controller / API
       │
       ▼
    Service
       │
       ▼
 Repository / Data Access
       │
       ▼
 Neo4j Java Driver
       │
       ▼
     Neo4j
```

- [ ] Keep Cypher out of controllers
- [ ] Keep HTTP concerns out of database code
- [ ] Encapsulate query/mapping logic
- [ ] Use service layer for business rules/workflows
- [ ] Use repository/data-access layer for persistence
- [ ] Understand dependency direction

## 65. Repository Responsibilities

A repository may:

- [ ] Execute Cypher
- [ ] Bind parameters
- [ ] Map results
- [ ] Represent missing data appropriately
- [ ] Perform persistence-specific operations

It should generally not:

- [ ] Handle HTTP requests
- [ ] Format UI output
- [ ] Contain unrelated business workflows
- [ ] Store secrets directly in source

## 66. DTO / Domain Boundaries

- [ ] Database result shape vs domain object
- [ ] Domain object vs API DTO
- [ ] Avoid leaking database-driver types throughout the application
- [ ] Keep boundaries explicit

**Checkpoint:** Build a small Java service/repository application using the Neo4j driver without Spring Data.

---

# Tier 23 — Testing

## 67. Unit Testing

- [ ] Unit-test pure business logic independently of Neo4j
- [ ] Mock repository boundaries where isolation is appropriate
- [ ] Use JUnit
- [ ] Use Mockito where appropriate
- [ ] Do not mock Neo4j and conclude that Cypher/database behavior has been tested

## 68. Integration Testing

- [ ] Test actual Cypher
- [ ] Test constraints
- [ ] Test mappings
- [ ] Test transaction behavior
- [ ] Test realistic graph patterns
- [ ] Use isolated test data
- [ ] Clean/reset test state reliably
- [ ] Containerized database testing awareness

## 69. Test Cases for Graph Data

- [ ] No matching path
- [ ] One matching path
- [ ] Multiple matches
- [ ] Optional relationships
- [ ] Duplicate identity attempts
- [ ] Cycles
- [ ] Deep/bounded traversal
- [ ] Relationship-property behavior
- [ ] Deletion of connected data
- [ ] Transaction rollback

**Checkpoint:** Create a repeatable integration test suite for a small Neo4j repository.

---

# Tier 24 — Import, Export & Bulk Data

## 70. Import Fundamentals

- [ ] CSV import concepts
- [ ] `LOAD CSV` awareness/use
- [ ] Headers
- [ ] Data conversion
- [ ] Creating nodes from rows
- [ ] Connecting imported nodes
- [ ] Use constraints/indexes appropriately during import workflows
- [ ] Avoid accidental duplicates

## 71. Application Batch Writes

- [ ] Use lists/maps as parameters
- [ ] `UNWIND`
- [ ] Batch work appropriately
- [ ] Understand transaction-size tradeoffs
- [ ] Avoid one network round trip per trivial row when bulk patterns are appropriate

## 72. Large Initial Imports

- [ ] Recognize dedicated bulk-import tooling
- [ ] Understand offline/initial-load vs ordinary application writes
- [ ] Do not require administration-specialist mastery
- [ ] Validate imported graph structure

## 73. Export Awareness

- [ ] Export query results
- [ ] Backup vs logical export distinction
- [ ] Data migration awareness
- [ ] Preserve identifiers/relationships deliberately

---

# Tier 25 — Operations & Administration Awareness

## 74. Database Operations

Professional developers should recognize:

- [ ] Database startup/shutdown
- [ ] Configuration
- [ ] Authentication
- [ ] Logs
- [ ] Disk/storage usage
- [ ] Memory configuration awareness
- [ ] Connection limits/pooling considerations
- [ ] Database health
- [ ] Monitoring/metrics awareness

## 75. Backup & Restore

- [ ] Understand why backups exist
- [ ] Backup strategy awareness
- [ ] Restore testing awareness
- [ ] Recovery objectives awareness
- [ ] Do not treat copying random database files as a backup strategy

## 76. Deployment Awareness

- [ ] Local vs production Neo4j
- [ ] Managed/cloud offerings awareness
- [ ] Container deployment awareness
- [ ] Persistent storage
- [ ] Networking/firewalls
- [ ] TLS
- [ ] Secrets
- [ ] High availability/clustering awareness
- [ ] Leave Kubernetes/cloud-specialist operations to dedicated trees

---

# Tier 26 — Graph Data Science Awareness

## 77. Why Graph Algorithms Matter

Some graph questions go beyond ordinary pattern matching.

Examples:

```text
Which nodes are most influential?
Which groups form communities?
What is the shortest/cheapest route?
Which nodes are similar?
What should be recommended?
```

- [ ] Graph algorithms concept
- [ ] Centrality
- [ ] Community detection
- [ ] Pathfinding
- [ ] Similarity
- [ ] Link prediction awareness
- [ ] Graph embeddings awareness

## 78. Neo4j Graph Data Science

- [ ] Recognize Neo4j Graph Data Science tooling
- [ ] Understand analytical graph projections conceptually
- [ ] Understand difference between transactional graph queries and analytical algorithms
- [ ] Do not require algorithm-specialist mastery in this core tree

### Scope boundary

```text
Neo4j Core Skill Tree
        │
        └── Graph Data Science awareness
                    │
                    ▼
       Future GDS / Graph Algorithms Tree
```

---

# Tier 27 — Neo4j vs Other Databases

## 79. Neo4j vs PostgreSQL

PostgreSQL excels at relational/tabular workloads and supports rich SQL, constraints, transactions, joins, and many general application workloads.

Neo4j is especially compelling when:

- [ ] Relationships are central to the domain
- [ ] Queries traverse multiple relationship hops
- [ ] Connected patterns are frequent
- [ ] Graph structure itself carries important meaning

Do not reduce the comparison to:

```text
joins bad
graphs good
```

Both systems can represent related data.

The question is which model and query workload best fit the application.

## 80. Neo4j vs MongoDB

MongoDB emphasizes document-oriented data.

Conceptually:

```text
MongoDB
document-centered aggregates

Neo4j
relationship-centered connected data
```

Choose based on access patterns and domain structure rather than popularity.

## 81. Polyglot Persistence

A system may legitimately use:

```text
PostgreSQL → transactional relational data
MongoDB    → document-oriented data
Neo4j      → relationship-heavy graph data
```

- [ ] Understand benefits
- [ ] Understand operational complexity
- [ ] Avoid adding multiple databases without a real requirement
- [ ] Recognize data synchronization/ownership problems

**Checkpoint:** Given several application requirements, justify PostgreSQL, MongoDB, or Neo4j rather than choosing by habit.

---

# Tier 28 — Debugging Neo4j

## 82. Query Debugging

When a query fails or returns the wrong result:

```text
1. Check syntax
2. Check labels
3. Check relationship types/direction
4. Check property names/types
5. Run a smaller MATCH
6. Inspect intermediate results
7. Add WHERE conditions incrementally
8. Use WITH to inspect stages
9. EXPLAIN / PROFILE performance problems
10. Check indexes/constraints/model assumptions
```

- [ ] Reduce complex patterns
- [ ] Verify data actually exists
- [ ] Verify direction/type
- [ ] Verify parameter values
- [ ] Check `null`/optional behavior
- [ ] Detect duplicates
- [ ] Detect Cartesian products

## 83. Connectivity Debugging

- [ ] Is Neo4j running?
- [ ] Correct host?
- [ ] Correct port?
- [ ] Correct URI scheme/configuration?
- [ ] Authentication correct?
- [ ] Network/firewall?
- [ ] TLS configuration?
- [ ] Driver/server compatibility?
- [ ] Read actual driver error

## 84. Application-Layer Debugging

```text
Frontend/API problem?
       │
       ▼
Service problem?
       │
       ▼
Repository mapping?
       │
       ▼
Cypher?
       │
       ▼
Neo4j data/schema?
```

Trace the failure to the correct layer instead of changing every layer simultaneously.

---

# Tier 29 — Professional Neo4j Practices

## 85. Query Quality

- [ ] Parameterize values
- [ ] Use clear variable names
- [ ] Keep patterns readable
- [ ] Avoid unnecessarily broad traversals
- [ ] Return only required data
- [ ] Use `WITH` deliberately
- [ ] Inspect performance of important queries
- [ ] Comment/document non-obvious query intent where appropriate

## 86. Modeling Quality

- [ ] Model around domain meaning and query patterns
- [ ] Use meaningful relationship types
- [ ] Use consistent direction
- [ ] Use stable identifiers
- [ ] Protect important assumptions with constraints
- [ ] Avoid over-modeling
- [ ] Revisit the model when query complexity exposes structural problems

## 87. Application Quality

- [ ] Repository/data-access boundary
- [ ] Explicit transaction boundaries
- [ ] Externalized configuration
- [ ] Secure credentials
- [ ] Useful logging
- [ ] Integration tests
- [ ] Timeouts/retries awareness
- [ ] Graceful error handling
- [ ] Observability awareness

## 88. Documentation

Document:

- [ ] Node labels
- [ ] Relationship types
- [ ] Direction conventions
- [ ] Important properties
- [ ] Identity rules
- [ ] Constraints
- [ ] Indexes
- [ ] Important query patterns
- [ ] Data ownership
- [ ] Migration/import expectations

---

# Tier 30 — Spring Data Neo4j Readiness

## 89. What You Should Understand Before the Abstraction

Before learning Spring Data Neo4j, be able to explain:

```text
Node
Relationship
Label
Property
Cypher
MATCH
CREATE / MERGE
Traversal
Constraint
Index
Transaction
Driver
Session
Repository boundary
```

## 90. What Spring Data Neo4j Will Abstract

At a high level, expect a Spring integration to help with areas such as:

- [ ] Object/graph mapping
- [ ] Repository abstractions
- [ ] Spring configuration/integration
- [ ] Transaction integration
- [ ] Query execution
- [ ] Mapping between Java application objects and graph data

Do **not** allow the abstraction to erase understanding of:

- [ ] Cypher
- [ ] Graph modeling
- [ ] Query performance
- [ ] Constraints
- [ ] Transactions
- [ ] Generated/executed database operations

### Dependency path

```text
Java
 │
 ├── Maven
 │
 └── Spring Boot
       │
       │
Neo4j ─┼──► Neo4j Java Driver
       │
       ▼
Spring Data Neo4j
   (separate tree)
```

---

# Practical Competency Checkpoints

A developer completing this tree should be able to:

- [ ] Explain graph databases and when they are useful
- [ ] Explain Neo4j's property graph model
- [ ] Model a domain using nodes, relationships, labels, and properties
- [ ] Write Cypher CRUD queries from memory
- [ ] Match and traverse relationships
- [ ] Query bounded multi-hop paths
- [ ] Filter, sort, paginate, and aggregate results
- [ ] Use `OPTIONAL MATCH`
- [ ] Use `WITH` to build multi-stage queries
- [ ] Use parameters rather than concatenating untrusted values
- [ ] Create appropriate constraints and indexes
- [ ] Explain `CREATE` vs `MERGE`
- [ ] Explain transactions and rollback
- [ ] Recognize accidental Cartesian products and path explosion
- [ ] Use `EXPLAIN` and `PROFILE` to investigate performance
- [ ] Connect a Java/Maven application using the Neo4j Java driver
- [ ] Map query results into application objects
- [ ] Build a repository/data-access layer
- [ ] Integration-test actual Cypher/database behavior
- [ ] Import structured data into a graph
- [ ] Diagnose query, connectivity, mapping, and schema problems
- [ ] Compare Neo4j appropriately with PostgreSQL and MongoDB
- [ ] Enter Spring Data Neo4j without depending on it to understand the database

---

# Suggested Practice Progression

```text
1. Start Neo4j locally
        │
        ▼
2. Create 5–10 nodes manually
        │
        ▼
3. Connect them with relationships
        │
        ▼
4. MATCH simple patterns
        │
        ▼
5. Perform CRUD
        │
        ▼
6. Traverse 1–3 hops
        │
        ▼
7. Add filtering + aggregation
        │
        ▼
8. Redesign a relational example as a graph
        │
        ▼
9. Add constraints + indexes
        │
        ▼
10. EXPLAIN / PROFILE queries
        │
        ▼
11. Connect with plain Java driver
        │
        ▼
12. Build Repository + Service layers
        │
        ▼
13. Add integration tests
        │
        ▼
14. Build a small REST-backed graph application
        │
        ▼
15. Move into Spring Data Neo4j
```

# Suggested Capstone

Build a small **developer skill graph**:

```text
(Developer)-[:KNOWS]->(Skill)
(Skill)-[:REQUIRES]->(Skill)
(Skill)-[:BELONGS_TO]->(Category)
(Project)-[:USES]->(Skill)
(Developer)-[:COMPLETED]->(Project)
```

Then implement queries such as:

- [ ] Which skills does a developer know?
- [ ] What prerequisites does a skill have?
- [ ] What skills are indirectly required within three levels?
- [ ] Which projects practice a particular skill?
- [ ] Which missing prerequisites block a target skill?
- [ ] Which skills are shared by two projects?
- [ ] Which categories contain the most skills?
- [ ] What is the shortest prerequisite path from one skill to another?

This capstone directly reinforces why graph databases can be useful for a **Skill Tree** application.

---

# Interview Readiness

Be able to answer clearly:

- [ ] What is Neo4j?
- [ ] What is a graph database?
- [ ] What is a property graph?
- [ ] What is a node?
- [ ] What is a relationship?
- [ ] What is a label?
- [ ] What is Cypher?
- [ ] What does `MATCH` do?
- [ ] `CREATE` vs `MERGE`?
- [ ] What is `OPTIONAL MATCH`?
- [ ] What does `WITH` do?
- [ ] How do you prevent Cypher injection?
- [ ] Why use constraints?
- [ ] Why use indexes?
- [ ] What is a graph traversal?
- [ ] What can make a traversal expensive?
- [ ] What are `EXPLAIN` and `PROFILE` for?
- [ ] When would you choose Neo4j instead of PostgreSQL?
- [ ] When would you *not* choose Neo4j?
- [ ] How does a Java application communicate with Neo4j?
- [ ] What should Spring Data Neo4j abstract, and what should you still understand underneath it?

---

# Mastery Standard

> **Can I model connected data, write and optimize Cypher, protect graph integrity, use transactions, integrate Neo4j into a Java application, test and debug the persistence layer, and explain when a graph database is or is not appropriate without relying on a step-by-step tutorial?**

At mastery, Neo4j should fit into the larger full-stack mental model:

```text
                         Application
                              │
                  ┌───────────┴───────────┐
                  ▼                       ▼
              Frontend                 Backend
          Angular / React             Spring Boot
                                          │
                                      Service Layer
                                          │
                                    Repository Layer
                                          │
                  ┌───────────────────────┼───────────────────────┐
                  ▼                       ▼                       ▼
             PostgreSQL                MongoDB                  Neo4j
                  │                                               │
                JDBC                                      Java Driver
                  │                                               │
            JPA / Hibernate                              Spring Data Neo4j
                  │
          Spring Data JPA
```
