# JDBC Skill Tree --- Full-Stack Java Developer Path

> **Goal:** Progress from basic Java and SQL knowledge to professional,
> application-level JDBC proficiency, with deliberate preparation for
> Spring Boot, JPA, and Spring Data.
>
> **Learning context:** JDBC is the bridge between Java application code
> and relational databases.
>
> **Target database for practice:** PostgreSQL.
>
> **Target level:** Professional full-stack Java developer --- not JDBC
> driver engineer or distributed-database infrastructure specialist.
>
> **Learning progression:**
> `Java + Maven + SQL/PostgreSQL → JDBC → Repository/Data Access → Spring Boot → JPA/Spring Data`

------------------------------------------------------------------------

# Skill Tree Overview

``` text
JDBC
├── 1. Prerequisites
├── 2. What JDBC Is
├── 3. JDBC Architecture
├── 4. PostgreSQL JDBC Setup
├── 5. Connections
├── 6. Statement
├── 7. PreparedStatement
├── 8. Executing SQL
├── 9. ResultSet
├── 10. CRUD Operations
├── 11. SQL ↔ Java Type Mapping
├── 12. NULL Handling
├── 13. Row-to-Object Mapping
├── 14. Resource Management
├── 15. SQLException & Error Handling
├── 16. Transactions
├── 17. Generated Keys
├── 18. Batch Operations
├── 19. DataSource
├── 20. Connection Pooling
├── 21. DAO / Repository Pattern
├── 22. Service + Repository Separation
├── 23. Testing JDBC Code
├── 24. Security
├── 25. Performance
├── 26. Metadata
├── 27. Debugging
├── 28. Professional JDBC Practices
└── 29. Spring Boot / JPA Readiness
```

------------------------------------------------------------------------

# Dependency Map

``` text
              Java
                │
        ┌───────┴───────┐
        ▼               ▼
      Maven        SQL Fundamentals
                        │
                        ▼
                   PostgreSQL
        └───────┬───────┘
                ▼
               JDBC
                │
                ▼
      Repository / DAO
                │
                ▼
          Service Layer
                │
                ▼
           Spring Boot
                │
        ┌───────┴────────┐
        ▼                ▼
      JPA          Spring Data JPA
```

------------------------------------------------------------------------

# Tier 0 --- Prerequisites

## 1. Java Prerequisites

-   [ ] Variables and data types
-   [ ] Methods
-   [ ] Classes and objects
-   [ ] Constructors
-   [ ] Interfaces
-   [ ] Exceptions
-   [ ] Generics basics
-   [ ] Collections
-   [ ] `Optional`
-   [ ] `java.time`
-   [ ] Try-with-resources
-   [ ] Packages and imports
-   [ ] Basic Maven usage

## 2. SQL / PostgreSQL Prerequisites

-   [ ] Tables
-   [ ] Rows and columns
-   [ ] Primary keys
-   [ ] Foreign keys
-   [ ] Constraints
-   [ ] `SELECT`
-   [ ] `INSERT`
-   [ ] `UPDATE`
-   [ ] `DELETE`
-   [ ] `WHERE`
-   [ ] `ORDER BY`
-   [ ] Joins
-   [ ] SQL data types
-   [ ] Transactions conceptually

**Checkpoint:** Manually create a PostgreSQL table and perform CRUD
operations with SQL before accessing it from Java.

------------------------------------------------------------------------

# Tier 1 --- JDBC Fundamentals

## 3. What JDBC Is

JDBC = **Java Database Connectivity**.

``` text
Java Application
       │
       ▼
    JDBC API
       │
       ▼
  JDBC Driver
       │
       ▼
   PostgreSQL
```

-   [ ] Explain JDBC's purpose
-   [ ] Understand that JDBC is a Java API
-   [ ] Understand that JDBC is not a database
-   [ ] Understand that JDBC is not PostgreSQL-specific
-   [ ] Understand the role of a database-specific JDBC driver
-   [ ] Understand that frameworks can build abstractions on top of JDBC
-   [ ] Recognize JDBC as lower-level database access than JPA/Spring
    Data

## 4. Core JDBC Objects

Understand the basic flow:

``` text
Connection
    │
    ▼
PreparedStatement
    │
    ▼
Execute SQL
    │
    ▼
ResultSet
    │
    ▼
Java Objects
```

Core interfaces/classes:

-   [ ] `DriverManager`
-   [ ] `DataSource`
-   [ ] `Connection`
-   [ ] `Statement`
-   [ ] `PreparedStatement`
-   [ ] `ResultSet`
-   [ ] `SQLException`

**Checkpoint:** Explain the responsibility of each major JDBC object
without writing code.

------------------------------------------------------------------------

# Tier 2 --- PostgreSQL JDBC Setup

## 5. PostgreSQL JDBC Driver

-   [ ] Understand why Java needs a PostgreSQL JDBC driver
-   [ ] Add the PostgreSQL JDBC dependency with Maven
-   [ ] Understand driver dependency scope
-   [ ] Understand automatic driver discovery in modern JDBC
-   [ ] Recognize older explicit driver-loading code without depending
    on it

## 6. JDBC URLs

Recognize a PostgreSQL JDBC URL:

``` text
jdbc:postgresql://localhost:5432/guestbook
```

Break it down:

``` text
jdbc:postgresql://localhost:5432/guestbook
│        │            │       │      │
│        │            │       │      └─ database
│        │            │       └──────── port
│        │            └──────────────── host
│        └───────────────────────────── driver/protocol
└────────────────────────────────────── JDBC
```

-   [ ] JDBC URL syntax
-   [ ] Host
-   [ ] Port
-   [ ] Database name
-   [ ] Username
-   [ ] Password
-   [ ] Understand local vs remote database connections
-   [ ] Keep credentials out of committed source code

------------------------------------------------------------------------

# Tier 3 --- Connections

## 7. `Connection`

Example concept:

``` java
Connection connection = DriverManager.getConnection(
    url,
    username,
    password
);
```

-   [ ] Open a database connection
-   [ ] Understand what a connection represents
-   [ ] Check connection validity where appropriate
-   [ ] Understand connection state
-   [ ] Close connections
-   [ ] Understand that database connections are limited resources
-   [ ] Understand why repeatedly creating physical connections is
    expensive

## 8. Connection Configuration

-   [ ] Auto-commit
-   [ ] Read-only mode awareness
-   [ ] Transaction isolation awareness
-   [ ] Connection timeouts conceptually
-   [ ] Client/session settings awareness
-   [ ] Avoid unnecessary connection-level customization

**Checkpoint:** Connect a plain Java application to PostgreSQL and close
the connection safely.

------------------------------------------------------------------------

# Tier 4 --- Statements

## 9. `Statement`

-   [ ] Understand what `Statement` does
-   [ ] Execute static SQL
-   [ ] Understand why concatenating untrusted input into SQL is
    dangerous
-   [ ] Recognize SQL injection
-   [ ] Understand why `Statement` is not the normal choice for
    parameterized application queries

Example of what **not** to do with untrusted data:

``` java
String sql =
    "SELECT * FROM users WHERE name = '" + userInput + "'";
```

## 10. `PreparedStatement`

**High-priority JDBC skill.**

``` java
PreparedStatement statement =
    connection.prepareStatement(
        "SELECT id, name FROM users WHERE id = ?"
    );

statement.setLong(1, id);
```

-   [ ] Create a `PreparedStatement`
-   [ ] Understand `?` placeholders
-   [ ] Bind parameters
-   [ ] Understand one-based parameter indexing
-   [ ] `setString()`
-   [ ] `setInt()`
-   [ ] `setLong()`
-   [ ] `setBoolean()`
-   [ ] `setBigDecimal()`
-   [ ] Date/time setters
-   [ ] `setObject()`
-   [ ] Bind SQL `NULL`
-   [ ] Understand why parameter binding helps prevent SQL injection
-   [ ] Prefer `PreparedStatement` for ordinary application SQL

### Mental model

``` text
SQL structure:
SELECT * FROM messages WHERE id = ?

Parameter:
                               42

Database receives:
SQL structure + typed parameter
```

rather than manually constructing SQL text.

------------------------------------------------------------------------

# Tier 5 --- Executing SQL

## 11. `executeQuery()`

-   [ ] Use for queries returning rows
-   [ ] Receive a `ResultSet`
-   [ ] Understand typical `SELECT` usage

``` java
ResultSet result =
    statement.executeQuery();
```

## 12. `executeUpdate()`

-   [ ] Use for `INSERT`
-   [ ] Use for `UPDATE`
-   [ ] Use for `DELETE`
-   [ ] Understand returned affected-row count
-   [ ] Verify expected update counts where useful

## 13. `execute()`

-   [ ] Understand general-purpose execution
-   [ ] Understand its boolean result conceptually
-   [ ] Know why `executeQuery()` or `executeUpdate()` is usually
    clearer when the expected result is known

**Checkpoint:** Correctly choose the execution method for common CRUD
statements.

------------------------------------------------------------------------

# Tier 6 --- ResultSet

## 14. Reading Results

``` java
while (resultSet.next()) {
    long id = resultSet.getLong("id");
    String name = resultSet.getString("name");
}
```

-   [ ] Understand the ResultSet cursor
-   [ ] Call `next()`
-   [ ] Iterate through rows
-   [ ] Read columns by label
-   [ ] Read columns by index
-   [ ] Prefer readable column labels where practical

## 15. Result Getters

-   [ ] `getString()`
-   [ ] `getInt()`
-   [ ] `getLong()`
-   [ ] `getBoolean()`
-   [ ] `getBigDecimal()`
-   [ ] `getObject()`
-   [ ] Date/time retrieval
-   [ ] Understand type conversion behavior

## 16. ResultSet Modes --- Awareness

-   [ ] Forward-only result sets
-   [ ] Scrollability awareness
-   [ ] Read-only vs updatable awareness
-   [ ] Understand that ordinary application queries usually need only
    simple forward iteration

------------------------------------------------------------------------

# Tier 7 --- CRUD

## 17. CREATE / INSERT

-   [ ] Write parameterized `INSERT`
-   [ ] Bind values
-   [ ] Execute insert
-   [ ] Check affected row count
-   [ ] Retrieve generated keys when needed

## 18. READ / SELECT

-   [ ] Select one row
-   [ ] Select multiple rows
-   [ ] Filter with parameters
-   [ ] Join related tables
-   [ ] Order results
-   [ ] Map rows to Java objects
-   [ ] Represent missing rows appropriately

## 19. UPDATE

-   [ ] Parameterized update
-   [ ] Bind changed values
-   [ ] Restrict rows with `WHERE`
-   [ ] Check affected row count

## 20. DELETE

-   [ ] Parameterized delete
-   [ ] Restrict rows with `WHERE`
-   [ ] Check affected row count
-   [ ] Understand foreign-key effects

**Checkpoint:** Implement full CRUD for one PostgreSQL table using plain
JDBC.

------------------------------------------------------------------------

# Tier 8 --- SQL ↔ Java Type Mapping

## 21. Common Type Relationships

Understand practical mappings such as:

``` text
PostgreSQL / SQL        Java
-----------------------------------
INTEGER                 int / Integer
BIGINT                  long / Long
BOOLEAN                 boolean / Boolean
VARCHAR / TEXT          String
NUMERIC / DECIMAL       BigDecimal
DATE                    LocalDate
TIME                    LocalTime
TIMESTAMP               LocalDateTime
TIMESTAMP WITH TZ       OffsetDateTime
UUID                    UUID
```

-   [ ] Select appropriate Java representations
-   [ ] Understand primitive vs wrapper implications
-   [ ] Understand precision concerns
-   [ ] Prefer `BigDecimal` for exact decimal values such as money when
    appropriate
-   [ ] Understand date/time semantics rather than blindly matching
    names

## 22. PostgreSQL-Specific Awareness

-   [ ] PostgreSQL `UUID`
-   [ ] Arrays awareness
-   [ ] JSON/JSONB awareness
-   [ ] Enum/custom type awareness
-   [ ] Know when driver-specific handling may be necessary
-   [ ] Keep portable JDBC knowledge separate from PostgreSQL-specific
    extensions

------------------------------------------------------------------------

# Tier 9 --- NULL Handling

## 23. SQL NULL vs Java null

-   [ ] Understand SQL `NULL`
-   [ ] Understand Java `null`
-   [ ] Understand that they belong to different type systems
-   [ ] Understand primitive getter complications

## 24. Reading NULL

-   [ ] `ResultSet.wasNull()`
-   [ ] Wrapper/object retrieval where appropriate
-   [ ] Map nullable database values deliberately
-   [ ] Use `Optional` at appropriate API boundaries rather than
    mechanically everywhere

## 25. Writing NULL

-   [ ] `setNull()`
-   [ ] `setObject()` where appropriate
-   [ ] Understand required SQL type information
-   [ ] Respect database `NOT NULL` constraints

------------------------------------------------------------------------

# Tier 10 --- Row-to-Object Mapping

## 26. Manual Mapping

Database:

``` text
messages
--------------------------------
id
name
body
created_at
```

Java:

``` java
Message message = new Message(
    resultSet.getLong("id"),
    resultSet.getString("name"),
    resultSet.getString("body"),
    resultSet.getObject(
        "created_at",
        OffsetDateTime.class
    )
);
```

-   [ ] Map one row to one object
-   [ ] Map multiple rows to a collection
-   [ ] Map joined results
-   [ ] Handle nullable fields
-   [ ] Keep column names understandable
-   [ ] Avoid scattering identical mapping logic throughout the
    application

## 27. Mapping Design

-   [ ] Separate database representation from business logic
-   [ ] Create mapping helper methods where useful
-   [ ] Understand DTO/domain/entity distinctions
-   [ ] Recognize manual row mapping as work later abstractions may
    automate

**Checkpoint:** Map a multi-row query into `List<Message>` correctly.

------------------------------------------------------------------------

# Tier 11 --- Resource Management

## 28. Why Resources Must Be Closed

JDBC resources include:

``` text
Connection
PreparedStatement
ResultSet
```

-   [ ] Understand limited database resources
-   [ ] Understand resource leaks
-   [ ] Understand `AutoCloseable`
-   [ ] Understand close order conceptually

## 29. Try-With-Resources

``` java
try (
    Connection connection = dataSource.getConnection();
    PreparedStatement statement =
        connection.prepareStatement(sql);
    ResultSet resultSet = statement.executeQuery()
) {
    // process results
}
```

-   [ ] Use try-with-resources
-   [ ] Close resources on success
-   [ ] Close resources on exceptions
-   [ ] Avoid manual cleanup when try-with-resources is clearer
-   [ ] Understand nested resource lifetimes

**Checkpoint:** Write JDBC operations that cannot leak normal JDBC
resources during exceptions.

------------------------------------------------------------------------

# Tier 12 --- SQLException & Error Handling

## 30. `SQLException`

-   [ ] Catch `SQLException` at appropriate boundaries
-   [ ] Read exception messages
-   [ ] Understand chained SQL exceptions conceptually
-   [ ] Understand SQLState
-   [ ] Understand vendor error codes
-   [ ] Preserve the original exception as a cause when wrapping it

## 31. Common Database Failures

-   [ ] Connection failure
-   [ ] Authentication failure
-   [ ] Invalid SQL
-   [ ] Missing table/column
-   [ ] Constraint violation
-   [ ] Unique-key violation
-   [ ] Foreign-key violation
-   [ ] Type mismatch
-   [ ] Transaction failure
-   [ ] Timeout/deadlock awareness

## 32. Application Error Boundaries

``` text
SQLException
    │
    ▼
Repository / Data Access Layer
    │
    ▼
Application-level exception
    │
    ▼
Service / higher layer
```

-   [ ] Avoid exposing database implementation details unnecessarily
-   [ ] Avoid silently swallowing SQL errors
-   [ ] Log useful context without logging secrets

------------------------------------------------------------------------

# Tier 13 --- Transactions

## 33. Auto-Commit

-   [ ] Understand default auto-commit behavior
-   [ ] Understand statement-level transactions conceptually
-   [ ] Disable auto-commit when multiple operations must succeed/fail
    together

## 34. Manual Transactions

``` java
connection.setAutoCommit(false);

try {
    // operation 1
    // operation 2

    connection.commit();
} catch (SQLException e) {
    connection.rollback();
    throw e;
}
```

-   [ ] Begin a logical transaction
-   [ ] Commit
-   [ ] Roll back
-   [ ] Restore/release connection state appropriately
-   [ ] Understand transaction boundaries

## 35. ACID Connection

Connect JDBC behavior to database knowledge:

-   [ ] Atomicity
-   [ ] Consistency
-   [ ] Isolation
-   [ ] Durability
-   [ ] Understand that PostgreSQL provides transaction behavior; JDBC
    controls/accesses it from Java

## 36. Isolation Levels --- Application Awareness

-   [ ] Read uncommitted awareness
-   [ ] Read committed
-   [ ] Repeatable read
-   [ ] Serializable
-   [ ] Understand anomalies conceptually
-   [ ] Know PostgreSQL behavior can differ from generic SQL
    descriptions
-   [ ] Avoid changing isolation without a concrete requirement

## 37. Savepoints --- Awareness

-   [ ] Create a savepoint
-   [ ] Roll back to a savepoint
-   [ ] Recognize use cases
-   [ ] Lower priority for ordinary full-stack development

**Checkpoint:** Implement a two-step database operation that rolls back
completely if the second operation fails.

------------------------------------------------------------------------

# Tier 14 --- Generated Keys

## 38. Database-Generated IDs

-   [ ] Understand generated primary keys
-   [ ] Request generated keys
-   [ ] Read returned key values
-   [ ] Assign generated identity to returned application objects where
    appropriate
-   [ ] Understand PostgreSQL `RETURNING` as a useful
    PostgreSQL-specific alternative/pattern

------------------------------------------------------------------------

# Tier 15 --- Batch Operations

## 39. JDBC Batching

-   [ ] Understand why repeated individual round trips can be expensive
-   [ ] `addBatch()`
-   [ ] `executeBatch()`
-   [ ] Interpret update counts
-   [ ] Use parameterized batch operations
-   [ ] Understand transaction interaction
-   [ ] Handle partial failures appropriately

### Mental model

``` text
Without batching:
Java → DB
Java → DB
Java → DB
Java → DB

With batching:
Java ─────→ DB
   multiple operations
```

-   [ ] Use batching when workload justifies it
-   [ ] Avoid premature batching for trivial operations

------------------------------------------------------------------------

# Tier 16 --- DataSource

## 40. `DataSource`

Move beyond:

``` java
DriverManager.getConnection(...)
```

toward:

``` java
dataSource.getConnection()
```

-   [ ] Understand `DataSource`
-   [ ] Understand why applications prefer it
-   [ ] Separate connection configuration from repository logic
-   [ ] Understand that a `DataSource` may provide pooled connections
-   [ ] Pass/inject `DataSource` as a dependency
-   [ ] Prepare for framework-managed data sources

### Architecture

``` text
Repository
    │
    ▼
DataSource
    │
    ▼
Connection
    │
    ▼
PostgreSQL
```

------------------------------------------------------------------------

# Tier 17 --- Connection Pooling

## 41. Why Pool Connections

Physical connection setup can be expensive.

``` text
Without Pool

Request → create connection → query → destroy
Request → create connection → query → destroy


With Pool

        ┌───────────────┐
Request → Connection Pool → existing connection
        └───────────────┘
```

-   [ ] Understand connection pooling
-   [ ] Understand checkout/return behavior
-   [ ] Understand maximum pool size conceptually
-   [ ] Understand connection timeout conceptually
-   [ ] Understand idle connections conceptually
-   [ ] Understand why closing a pooled `Connection` normally returns it
    to the pool
-   [ ] Recognize pool exhaustion
-   [ ] Recognize HikariCP as a common Java/Spring Boot pool
-   [ ] Avoid implementing a custom connection pool

**Target depth:** Configure and troubleshoot ordinary application
pooling; deep pool engineering is unnecessary.

------------------------------------------------------------------------

# Tier 18 --- DAO / Repository Pattern

## 42. Why Separate Data Access?

Avoid:

``` text
MessageService
├── business rules
├── SQL strings
├── Connection handling
├── PreparedStatement
├── ResultSet
└── object mapping
```

Prefer:

``` text
MessageService
      │
      ▼
MessageRepository
      │
      ▼
JdbcMessageRepository
      │
      ▼
PostgreSQL
```

## 43. Repository Interface

``` java
public interface MessageRepository {

    List<Message> findAll();

    Optional<Message> findById(long id);

    Message save(Message message);

    void deleteById(long id);
}
```

-   [ ] Define data-access operations around application needs
-   [ ] Hide JDBC details behind an interface when useful
-   [ ] Return domain/application data rather than JDBC objects
-   [ ] Keep `ResultSet` out of service/business code

## 44. JDBC Implementation

``` java
public class JdbcMessageRepository
        implements MessageRepository {

    private final DataSource dataSource;

    public JdbcMessageRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // JDBC implementations
}
```

-   [ ] Store `DataSource` dependency
-   [ ] Implement CRUD
-   [ ] Use prepared statements
-   [ ] Map rows
-   [ ] Manage resources
-   [ ] Translate database errors appropriately

**Checkpoint:** Build a complete JDBC repository whose callers do not
need to know JDBC is being used.

------------------------------------------------------------------------

# Tier 19 --- Service + Repository Separation

## 45. Layer Responsibilities

``` text
Service
│
├── business rules
├── validation/business decisions
├── workflow
└── calls repository

Repository
│
├── SQL
├── database access
├── JDBC
├── row mapping
└── persistence operations
```

-   [ ] Keep business logic out of JDBC code
-   [ ] Keep SQL out of service code
-   [ ] Understand dependency direction
-   [ ] Use constructor injection manually
-   [ ] Understand how this architecture can exist without Spring

## 46. Preparing for Controller → Service → Repository

``` text
Future Web Application

Controller
    │
    ▼
Service
    │
    ▼
Repository
    │
    ▼
JDBC
    │
    ▼
PostgreSQL
```

At this stage:

-   [ ] Understand Service → Repository thoroughly
-   [ ] Recognize Controller as a later HTTP/API boundary
-   [ ] Do not make JDBC responsible for HTTP behavior
-   [ ] Do not make Maven responsible for application architecture

------------------------------------------------------------------------

# Tier 20 --- Testing JDBC Code

## 47. Unit Testing Repository-Adjacent Logic

-   [ ] Test row-mapping helpers where practical
-   [ ] Test business logic separately from JDBC
-   [ ] Understand why mocking JDBC interfaces directly can become
    cumbersome
-   [ ] Avoid tests dominated by mocks of `Connection`,
    `PreparedStatement`, and `ResultSet` unless there is a specific
    reason

## 48. Repository Integration Tests

-   [ ] Understand why actual SQL behavior requires a real
    database-compatible environment
-   [ ] Test SQL syntax
-   [ ] Test constraints
-   [ ] Test mappings
-   [ ] Test transactions
-   [ ] Test generated IDs
-   [ ] Reset/isolated test data
-   [ ] Keep integration tests deterministic

## 49. JUnit Connection

``` text
JUnit
   │
   ▼
Repository Integration Test
   │
   ▼
JDBC
   │
   ▼
Test Database
```

-   [ ] Use JUnit as the test framework
-   [ ] Understand Mockito is more useful for mocking the repository
    from the service side than for pretending PostgreSQL itself works
-   [ ] Prepare for Spring Boot database integration testing later

------------------------------------------------------------------------

# Tier 21 --- Security

## 50. SQL Injection

**Critical skill.**

-   [ ] Explain SQL injection
-   [ ] Recognize string-concatenated SQL vulnerabilities
-   [ ] Use `PreparedStatement`
-   [ ] Bind user-controlled values
-   [ ] Understand that parameters represent values, not arbitrary SQL
    structure
-   [ ] Safely handle dynamic sorting/table/column choices through
    controlled allowlists/design rather than parameter placeholders

## 51. Credentials

-   [ ] Never hard-code production credentials
-   [ ] Use environment/configuration systems
-   [ ] Keep secrets out of Git
-   [ ] Avoid logging passwords
-   [ ] Understand credential rotation conceptually

## 52. Database Permissions

-   [ ] Principle of least privilege
-   [ ] Application database user
-   [ ] Avoid unnecessary administrative permissions
-   [ ] Understand read/write permission requirements
-   [ ] Separate migration/admin privileges where architecture requires
    it

------------------------------------------------------------------------

# Tier 22 --- Performance

## 53. Connection Performance

-   [ ] Reuse pooled connections
-   [ ] Close connections promptly
-   [ ] Recognize pool exhaustion
-   [ ] Avoid holding a connection while performing unrelated slow work

## 54. Query Performance

-   [ ] Avoid unnecessary queries
-   [ ] Avoid selecting unused columns where it matters
-   [ ] Understand database indexes at the SQL/PostgreSQL layer
-   [ ] Recognize N+1-style query patterns conceptually
-   [ ] Batch appropriate operations
-   [ ] Use pagination for large result sets
-   [ ] Understand fetch-size conceptually

## 55. Measurement

-   [ ] Measure before optimizing
-   [ ] Distinguish Java-side delays from database-side delays
-   [ ] Use PostgreSQL query analysis for SQL problems
-   [ ] Use application metrics/logging for connection/query behavior
-   [ ] Avoid attempting to solve poor SQL solely through JDBC tuning

------------------------------------------------------------------------

# Tier 23 --- Metadata

## 56. `DatabaseMetaData`

-   [ ] Understand database metadata
-   [ ] Inspect database/driver information
-   [ ] Inspect supported capabilities conceptually
-   [ ] Recognize schema/table metadata use cases

## 57. `ResultSetMetaData`

-   [ ] Inspect column count
-   [ ] Inspect column names/labels
-   [ ] Inspect column types
-   [ ] Understand dynamic-query/tooling use cases

**Priority:** Awareness/intermediate. Most ordinary repositories know
their schema at compile/design time.

------------------------------------------------------------------------

# Tier 24 --- Debugging

## 58. Connection Problems

-   [ ] Wrong host
-   [ ] Wrong port
-   [ ] Wrong database
-   [ ] Wrong username/password
-   [ ] PostgreSQL not running
-   [ ] Network/firewall problem
-   [ ] JDBC driver missing
-   [ ] Connection pool exhaustion

## 59. SQL Problems

-   [ ] Read PostgreSQL error messages
-   [ ] Inspect SQLState
-   [ ] Check parameter values/types
-   [ ] Run the SQL directly against PostgreSQL when useful
-   [ ] Distinguish SQL syntax errors from Java errors

## 60. Mapping Problems

-   [ ] Wrong column name
-   [ ] Wrong Java getter/type
-   [ ] NULL handling bug
-   [ ] Date/time mismatch
-   [ ] Numeric precision issue
-   [ ] Missing joined data
-   [ ] Duplicate rows from joins

## 61. Transaction Problems

-   [ ] Missing commit
-   [ ] Unexpected auto-commit
-   [ ] Rollback not occurring
-   [ ] Connection state reuse
-   [ ] Locks/deadlocks awareness
-   [ ] Transaction boundary too large or too small

### Troubleshooting process

``` text
Application failure
       │
       ▼
Connection problem?
       │
       ▼
SQL problem?
       │
       ▼
Parameter problem?
       │
       ▼
Database constraint?
       │
       ▼
Result mapping?
       │
       ▼
Transaction/resource problem?
```

------------------------------------------------------------------------

# Tier 25 --- Professional JDBC Practices

## 62. Maintainable SQL

-   [ ] Keep SQL readable
-   [ ] Use explicit column lists
-   [ ] Use meaningful aliases
-   [ ] Keep complex queries understandable
-   [ ] Centralize repeated queries appropriately
-   [ ] Do not build uncontrolled SQL through string concatenation

## 63. Maintainable Repository Code

-   [ ] Small focused methods
-   [ ] Consistent resource management
-   [ ] Consistent error handling
-   [ ] Consistent mapping
-   [ ] Minimal duplicated JDBC boilerplate
-   [ ] Clear transaction ownership
-   [ ] Clear dependency boundaries

## 64. Separation of Concerns

Be able to identify:

``` text
SQL knowledge             → PostgreSQL/SQL
Connection/execution      → JDBC
Persistence boundary      → Repository
Business rules            → Service
HTTP handling             → Controller
Dependency/build setup    → Maven
Test execution            → JUnit
Mocking dependencies      → Mockito
Framework management      → Spring
```

This distinction is a major mastery goal.

------------------------------------------------------------------------

# Tier 26 --- Spring Boot / JPA Readiness

## 65. What Spring Will Eventually Manage

After learning JDBC manually, recognize that Spring can manage or
simplify:

``` text
Manual JDBC
│
├── DataSource configuration
├── Connection acquisition
├── Exception translation
├── Transaction management
├── Repetitive JDBC operations
└── Dependency injection
        │
        ▼
Spring JDBC / JdbcTemplate
```

-   [ ] Understand what `JdbcTemplate` is intended to simplify
    conceptually
-   [ ] Recognize that JDBC still exists underneath Spring JDBC

## 66. What JPA Will Abstract

``` text
Manual JDBC

SQL
 ↓
PreparedStatement
 ↓
ResultSet
 ↓
Manual mapping
 ↓
Java Object


JPA / ORM

Java Entity
 ↓
ORM mapping
 ↓
Generated/executed SQL
 ↓
Database
```

-   [ ] Understand ORM conceptually
-   [ ] Understand entities conceptually
-   [ ] Understand that JPA does not make SQL/database knowledge
    unnecessary
-   [ ] Recognize why JDBC knowledge helps diagnose ORM problems

## 67. Spring Data Repository Bridge

Manual repository:

``` java
public class JdbcMessageRepository
        implements MessageRepository {
    // SQL + JDBC
}
```

Future Spring Data repository:

``` java
public interface MessageRepository
        extends JpaRepository<Message, Long> {
}
```

Understand what disappeared:

``` text
Spring Data hides much of:

Connection
PreparedStatement
ResultSet
CRUD SQL
row mapping
routine resource handling
```

but those underlying database concepts still matter.

## 68. Spring Architecture Readiness

``` text
HTTP
 │
 ▼
Controller
 │
 ▼
Service
 │
 ▼
Repository
 │
 ├──── JDBC implementation
 │
 └──── JPA/Spring Data implementation
 │
 ▼
PostgreSQL
```

-   [ ] Understand repository as an abstraction, not merely an
    annotation
-   [ ] Understand service/repository separation before Spring
-   [ ] Understand constructor dependency injection manually
-   [ ] Be ready to learn Spring-managed dependency injection
-   [ ] Be ready to learn transaction annotations after understanding
    transactions manually

------------------------------------------------------------------------

# Practical Progression

## Stage 1 --- Connect

``` text
Java
 ↓
PostgreSQL Driver
 ↓
JDBC URL
 ↓
Connection
 ↓
Database
```

**Exercise:** Connect to PostgreSQL and execute a simple `SELECT`.

------------------------------------------------------------------------

## Stage 2 --- Query

``` text
Connection
 ↓
PreparedStatement
 ↓
Parameters
 ↓
executeQuery()
 ↓
ResultSet
```

**Exercise:** Query a message by ID and print its fields.

------------------------------------------------------------------------

## Stage 3 --- CRUD

``` text
INSERT
SELECT
UPDATE
DELETE
```

**Exercise:** Build plain JDBC CRUD for a `messages` table.

------------------------------------------------------------------------

## Stage 4 --- Map Objects

``` text
Database Row
     ↓
ResultSet
     ↓
Mapping
     ↓
Message Object
```

**Exercise:** Return `List<Message>` instead of printing database rows.

------------------------------------------------------------------------

## Stage 5 --- Repository

``` text
MessageService
      │
      ▼
MessageRepository
      │
      ▼
JdbcMessageRepository
      │
      ▼
PostgreSQL
```

**Exercise:** Move all JDBC code behind a repository interface.

------------------------------------------------------------------------

## Stage 6 --- Transactions

``` text
Operation A
     +
Operation B
     │
     ▼
 one transaction
     │
 ┌───┴───┐
 ▼       ▼
commit  rollback
```

**Exercise:** Implement a multi-table operation that cannot leave
partial data after failure.

------------------------------------------------------------------------

## Stage 7 --- Professional Data Access

``` text
DataSource
    ↓
Connection Pool
    ↓
Repository
    ↓
Prepared SQL
    ↓
Mapping
    ↓
PostgreSQL
```

**Exercise:** Replace direct `DriverManager` usage with a `DataSource`
and test the repository against a real PostgreSQL test environment.

------------------------------------------------------------------------

## Stage 8 --- Spring Bridge

Rebuild the same conceptual application with Spring:

``` text
Before Spring

Service
  ↓
Repository Interface
  ↓
JdbcRepository
  ↓
JDBC
  ↓
PostgreSQL


After Spring/JPA

@Service
  ↓
Repository
  ↓
Spring Data / JPA
  ↓
JDBC underneath
  ↓
PostgreSQL
```

**Exercise:** Compare the manual JDBC implementation with the Spring
implementation and identify exactly which responsibilities Spring/JPA
now handles.

------------------------------------------------------------------------

# Knowledge Depth Scale

  -----------------------------------------------------------------------
  Level                               Meaning
  ----------------------------------- -----------------------------------
  **0 --- Unknown**                   Have not learned the concept

  **1 --- Recognize**                 Can identify it and explain its
                                      purpose

  **2 --- Guided**                    Can implement it with
                                      documentation/examples

  **3 --- Independent**               Can use it without step-by-step
                                      instructions

  **4 --- Applied**                   Can choose appropriate JDBC
                                      techniques in a real application

  **5 --- Professional**              Can design, debug, test, and
                                      explain JDBC data-access code and
                                      tradeoffs
  -----------------------------------------------------------------------

### Target Levels

**Levels 4--5:** - Connections - Prepared statements - Result sets -
CRUD - Resource management - Row mapping - Error handling -
Transactions - Repository separation - SQL injection prevention -
Debugging

**Levels 3--4:** - DataSource - Connection pooling - Generated keys -
Batch operations - Type mapping - Integration testing - Performance
awareness

**Levels 1--3:** - Metadata - Savepoints - Scrollable/updatable result
sets - Driver-specific advanced behavior - Advanced transaction
configuration

------------------------------------------------------------------------

# Highest-Priority JDBC Skills for Full-Stack Development

1.  Understand JDBC's position between Java and the database
2.  Configure the PostgreSQL JDBC driver
3.  Open and safely close connections
4.  Use `PreparedStatement`
5.  Bind typed parameters
6.  Prevent SQL injection
7.  Use `executeQuery()` and `executeUpdate()`
8.  Traverse `ResultSet`
9.  Map database rows into Java objects
10. Handle SQL/Java type differences
11. Handle SQL `NULL`
12. Use try-with-resources
13. Understand `SQLException`
14. Implement CRUD
15. Understand transactions
16. Commit and roll back correctly
17. Use generated keys
18. Understand `DataSource`
19. Understand connection pooling
20. Separate JDBC into a repository/data-access layer
21. Keep business logic in the service layer
22. Test real SQL with database integration tests
23. Diagnose connection, SQL, mapping, and transaction failures
24. Understand what Spring/JPA later abstracts

------------------------------------------------------------------------

# Lower-Priority / Awareness Topics

These should not block progression into Spring Boot:

-   Writing JDBC drivers
-   Driver protocol internals
-   XA/distributed transactions
-   Advanced distributed transaction managers
-   Complex savepoint strategies
-   Scroll-sensitive result sets
-   Updatable result sets
-   Custom connection-pool implementation
-   Deep JDBC metadata tooling
-   Vendor-specific driver internals

------------------------------------------------------------------------

# Final Mastery Check

A full-stack-oriented Java developer can consider their JDBC foundation
professionally useful when they can independently:

-   [ ] Explain JDBC
-   [ ] Explain JDBC driver responsibilities
-   [ ] Add the PostgreSQL JDBC driver with Maven
-   [ ] Construct/read a JDBC URL
-   [ ] Connect Java to PostgreSQL
-   [ ] Use `PreparedStatement`
-   [ ] Explain why parameterized SQL prevents common injection attacks
-   [ ] Execute SELECT/INSERT/UPDATE/DELETE
-   [ ] Read a `ResultSet`
-   [ ] Map rows to Java objects
-   [ ] Map common SQL types to Java types
-   [ ] Handle nullable columns
-   [ ] Use try-with-resources
-   [ ] Diagnose `SQLException`
-   [ ] Implement transactions
-   [ ] Roll back failed multi-step operations
-   [ ] Retrieve generated IDs
-   [ ] Use batch operations when appropriate
-   [ ] Explain `DataSource`
-   [ ] Explain connection pooling
-   [ ] Recognize pool exhaustion/resource leaks
-   [ ] Create a repository interface
-   [ ] Implement that interface using JDBC
-   [ ] Keep JDBC out of service/business logic
-   [ ] Test JDBC persistence against a database
-   [ ] Diagnose connection/query/mapping/transaction problems
-   [ ] Explain what JDBC responsibilities Spring JDBC simplifies
-   [ ] Explain what JDBC responsibilities JPA/Spring Data further
    abstracts
-   [ ] Read framework-generated database errors with an understanding
    of the lower-level system

------------------------------------------------------------------------

# Relationship to the Full-Stack Skill Trees

``` text
                              Java
                                │
                  ┌─────────────┼─────────────┐
                  ▼             ▼             ▼
                Maven         JUnit       SQL Concepts
                                │             │
                                ▼             ▼
                             Mockito      PostgreSQL
                                │             │
                                │             ▼
                                │            JDBC
                                │             │
                                └──────┬──────┘
                                       ▼
                                  Spring Boot
                                       │
                         ┌─────────────┼─────────────┐
                         ▼             ▼             ▼
                    Controller       Service     Repository
                                                   │
                                      ┌────────────┴───────────┐
                                      ▼                        ▼
                                  Spring JDBC             JPA/Spring Data
                                      │                        │
                                      └────────────┬───────────┘
                                                   ▼
                                              PostgreSQL
```

The central JDBC mastery question is:

> **Can I take SQL that I understand, execute it safely from Java,
> convert database results into useful Java objects, manage connections
> and transactions correctly, isolate persistence behind a clean
> data-access boundary, and understand what later frameworks are
> abstracting away?**

For the full-stack Java path, that is substantially more important than
memorizing every JDBC interface or advanced driver capability.
