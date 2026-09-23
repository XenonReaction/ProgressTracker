# PostgreSQL Skill Tree
## Full-Stack Developer Path

**Goal:** Progress from no relational-database experience to professional PostgreSQL application development.

**Scope:** SQL, relational database design, PostgreSQL, application integration, performance, security, testing, and production fundamentals needed by a full-stack developer.

**Not the goal:** Advanced DBA, distributed database engineering, high-availability architecture, deep replication administration, or expert PostgreSQL internals.

---

# 0. Skill Tree Overview

```text
PostgreSQL Full-Stack Developer
│
├── 1. Relational Database Foundations
├── 2. PostgreSQL Environment & psql
├── 3. SQL Query Fundamentals
├── 4. Tables & Schema Definition
├── 5. PostgreSQL Data Types
├── 6. Keys & Constraints
├── 7. Relationships & Joins
├── 8. Aggregation & Data Analysis
├── 9. Database Design & Normalization
├── 10. Intermediate & Advanced SQL
├── 11. Transactions & Concurrency
├── 12. Indexes & Query Performance
├── 13. PostgreSQL-Specific Features
├── 14. Views, Functions & Triggers
├── 15. Security & Permissions
├── 16. Application Integration
├── 17. Schema Migrations & Evolution
├── 18. Testing, Debugging & Data Integrity
├── 19. Production Fundamentals
└── 20. Professional Full-Stack PostgreSQL Mastery
```

---

# 1. Relational Database Foundations

## 1.1 What a Database Is
- [ ] Explain what a database is.
- [ ] Explain why applications use databases instead of ordinary files.
- [ ] Distinguish a database management system from a database.
- [ ] Explain what PostgreSQL is.
- [ ] Distinguish relational databases from document/key-value databases at a basic level.
- [ ] Explain persistent data versus application memory.

**Competency:** Can explain where PostgreSQL fits in a full-stack application and why the application needs persistent structured storage.

## 1.2 Relational Structure
- [ ] Understand database.
- [ ] Understand schema.
- [ ] Understand table.
- [ ] Understand row/record.
- [ ] Understand column/field.
- [ ] Understand relationships between tables.
- [ ] Understand a table's schema versus its stored data.

```text
PostgreSQL Server
└── Database
    └── Schema
        ├── Table
        │   ├── Columns
        │   └── Rows
        ├── View
        ├── Sequence
        └── Other objects
```

## 1.3 Relational Concepts
- [ ] Entity
- [ ] Attribute
- [ ] Relationship
- [ ] Tuple/row
- [ ] Domain
- [ ] Cardinality
- [ ] Referential integrity
- [ ] Candidate key

**Competency:** Can translate a simple application concept such as users, posts, and comments into relational entities.

---

# 2. PostgreSQL Environment & `psql`

## 2.1 PostgreSQL Basics
- [ ] Identify PostgreSQL server and client components.
- [ ] Understand host, port, database, username, and password.
- [ ] Understand PostgreSQL's default port `5432`.
- [ ] Understand connection strings conceptually.
- [ ] Distinguish connecting to PostgreSQL from selecting a database/schema.

## 2.2 `psql`
Learn to connect:

```bash
psql -U username -d database
psql -h localhost -p 5432 -U username -d database
```

Learn essential meta-commands:

```text
\l          list databases
\c          connect to database
\dn         list schemas
\dt         list tables
\d table    describe table
\du         list roles
\df         list functions
\dv         list views
\q          quit
\?          psql help
\h          SQL help
```

- [ ] Run SQL interactively.
- [ ] Run SQL from a `.sql` file.
- [ ] Inspect tables and schemas.
- [ ] Understand the difference between SQL statements and `psql` meta-commands.
- [ ] Read basic PostgreSQL error messages.

**Competency:** Can connect to an unfamiliar local PostgreSQL database and inspect its structure without a GUI.

---

# 3. SQL Query Fundamentals

## 3.1 `SELECT`

```sql
SELECT * FROM users;

SELECT id, username
FROM users;
```

- [ ] Select all columns.
- [ ] Select specific columns.
- [ ] Alias columns with `AS`.
- [ ] Alias tables.
- [ ] Understand result sets.

## 3.2 Filtering with `WHERE`

Operators:

```text
=
<>
!=
<
>
<=
>=
BETWEEN
IN
LIKE
ILIKE
IS NULL
IS NOT NULL
```

Logical operators:

```text
AND
OR
NOT
```

- [ ] Combine multiple conditions.
- [ ] Correctly use parentheses.
- [ ] Understand SQL's handling of `NULL`.

## 3.3 Sorting

```sql
ORDER BY created_at DESC;
ORDER BY last_name ASC, first_name ASC;
```

- [ ] Ascending versus descending.
- [ ] Multi-column ordering.

## 3.4 Limiting Results

```sql
LIMIT 20;
OFFSET 20;
```

- [ ] Limit result counts.
- [ ] Understand basic offset pagination.
- [ ] Understand why deterministic pagination needs a stable `ORDER BY`.

## 3.5 `DISTINCT`

```sql
SELECT DISTINCT country
FROM users;
```

- [ ] Remove duplicate result rows.
- [ ] Understand when `DISTINCT` is hiding a poorly constructed query.

## 3.6 `INSERT`

```sql
INSERT INTO users (username, email)
VALUES ('alice', 'alice@example.com');
```

- [ ] Insert one row.
- [ ] Insert multiple rows.
- [ ] Specify columns explicitly.
- [ ] Understand defaults.

## 3.7 `UPDATE`

```sql
UPDATE users
SET username = 'alice2'
WHERE id = 10;
```

- [ ] Update one or many rows.
- [ ] Use conditions safely.
- [ ] Recognize the danger of an omitted `WHERE`.

## 3.8 `DELETE`

```sql
DELETE FROM users
WHERE id = 10;
```

- [ ] Delete rows safely.
- [ ] Predict effects of foreign-key constraints.

## 3.9 PostgreSQL `RETURNING`

```sql
INSERT INTO users (username)
VALUES ('alice')
RETURNING id, username;
```

- [ ] Use `RETURNING` with `INSERT`.
- [ ] Use `RETURNING` with `UPDATE`.
- [ ] Use `RETURNING` with `DELETE`.

**Competency:** Can independently implement CRUD operations for an application table.

---

# 4. Tables & Schema Definition

## 4.1 Creating Tables

```sql
CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

- [ ] Create tables.
- [ ] Define columns.
- [ ] Assign data types.
- [ ] Define constraints.
- [ ] Define defaults.

## 4.2 Altering Tables

```sql
ALTER TABLE users ADD COLUMN display_name TEXT;
ALTER TABLE users DROP COLUMN display_name;
ALTER TABLE users RENAME COLUMN username TO handle;
```

Understand:
- [ ] Add column.
- [ ] Remove column.
- [ ] Rename column.
- [ ] Change data type.
- [ ] Add/remove constraints.
- [ ] Rename tables.

## 4.3 Removing Objects

```sql
DROP TABLE users;
DROP TABLE IF EXISTS users;
```

- [ ] Understand `DROP`.
- [ ] Understand dependency errors.
- [ ] Use `CASCADE` cautiously.

## 4.4 Schemas
- [ ] Understand PostgreSQL schemas.
- [ ] Understand `public`.
- [ ] Create schemas.
- [ ] Qualify objects: `schema.table`.
- [ ] Understand `search_path` conceptually.

**Competency:** Can create and modify the database structure for a small application.

---

# 5. PostgreSQL Data Types

## 5.1 Numeric
- [ ] `SMALLINT`
- [ ] `INTEGER`
- [ ] `BIGINT`
- [ ] `NUMERIC` / `DECIMAL`
- [ ] `REAL`
- [ ] `DOUBLE PRECISION`

Know when exact decimal values are required.

## 5.2 Text
- [ ] `TEXT`
- [ ] `VARCHAR(n)`
- [ ] `CHAR(n)`

Understand why `TEXT` is often appropriate in PostgreSQL.

## 5.3 Boolean

```sql
BOOLEAN
```

Understand `TRUE`, `FALSE`, and `NULL`.

## 5.4 Date and Time
- [ ] `DATE`
- [ ] `TIME`
- [ ] `TIMESTAMP`
- [ ] `TIMESTAMPTZ`
- [ ] `INTERVAL`

**Critical skill:** Understand the practical difference between `TIMESTAMP` and `TIMESTAMPTZ`.

## 5.5 Identifiers
- [ ] Integer identity columns
- [ ] `UUID`

Understand tradeoffs between sequential numeric IDs and UUIDs.

## 5.6 Structured/Specialized Types
- [ ] `JSON`
- [ ] `JSONB`
- [ ] Arrays
- [ ] Enums
- [ ] Binary data (`BYTEA`)
- [ ] Range types
- [ ] Network address types
- [ ] Domain types

These should be learned after relational fundamentals, not used to avoid proper table design.

## 5.7 Type Conversion
- [ ] Implicit conversion.
- [ ] Explicit `CAST`.
- [ ] PostgreSQL `::` casting syntax.

```sql
CAST(value AS INTEGER)
value::INTEGER
```

**Competency:** Can select appropriate PostgreSQL data types based on application requirements instead of defaulting everything to strings.

---

# 6. Keys & Constraints

## 6.1 Primary Keys
- [ ] Purpose of a primary key.
- [ ] Uniqueness.
- [ ] Non-null requirement.
- [ ] Natural versus surrogate keys.
- [ ] Identity-generated keys.
- [ ] UUID keys.

## 6.2 Composite Keys

```sql
PRIMARY KEY (user_id, role_id)
```

- [ ] Recognize when a composite key is appropriate.
- [ ] Use them in junction tables.

## 6.3 Foreign Keys

```sql
FOREIGN KEY (user_id)
REFERENCES users(id)
```

Understand:
- [ ] Referential integrity.
- [ ] Parent/referenced table.
- [ ] Child/referencing table.

## 6.4 Referential Actions
Understand:

```text
ON DELETE CASCADE
ON DELETE RESTRICT
ON DELETE SET NULL
ON DELETE SET DEFAULT
ON UPDATE ...
```

- [ ] Predict the consequences of deleting referenced data.
- [ ] Choose referential actions intentionally.

## 6.5 Other Constraints

```text
NOT NULL
UNIQUE
CHECK
DEFAULT
PRIMARY KEY
FOREIGN KEY
```

Example:

```sql
CHECK (price >= 0)
```

## 6.6 Constraints vs Application Validation
- [ ] Understand why application validation alone is insufficient.
- [ ] Place invariant data rules in the database where appropriate.
- [ ] Avoid duplicating unnecessarily complex business logic in constraints.

**Competency:** Can use database constraints to make invalid application state difficult or impossible to persist.

---

# 7. Relationships & Joins

## 7.1 Relationship Types

### One-to-One

```text
User ─── UserProfile
```

### One-to-Many

```text
User ───< Post
```

### Many-to-Many

```text
User >───< Role
      user_roles
```

- [ ] Model each relationship.
- [ ] Know where the foreign key belongs.
- [ ] Build junction/association tables.

## 7.2 `INNER JOIN`

```sql
SELECT users.username, posts.title
FROM users
INNER JOIN posts
    ON users.id = posts.user_id;
```

- [ ] Understand matching rows.
- [ ] Predict the result before running the query.

## 7.3 `LEFT JOIN`

```sql
SELECT users.username, posts.title
FROM users
LEFT JOIN posts
    ON users.id = posts.user_id;
```

- [ ] Preserve unmatched left-side rows.
- [ ] Understand resulting `NULL` values.

## 7.4 Additional Joins
- [ ] `RIGHT JOIN`
- [ ] `FULL OUTER JOIN`
- [ ] `CROSS JOIN`
- [ ] Self joins

Know them, while recognizing `INNER` and `LEFT` joins are especially common in application development.

## 7.5 Multi-Table Joins
- [ ] Join three or more tables.
- [ ] Follow foreign-key relationships.
- [ ] Avoid accidental Cartesian products.
- [ ] Identify duplicated rows caused by relationship cardinality.

**Competency:** Can inspect an application schema and construct queries across its relationships without trial-and-error join selection.

---

# 8. Aggregation & Data Analysis

## 8.1 Aggregate Functions

```text
COUNT()
SUM()
AVG()
MIN()
MAX()
```

## 8.2 `GROUP BY`

```sql
SELECT user_id, COUNT(*)
FROM posts
GROUP BY user_id;
```

- [ ] Understand grouping.
- [ ] Combine grouping with joins.

## 8.3 `HAVING`

```sql
SELECT user_id, COUNT(*)
FROM posts
GROUP BY user_id
HAVING COUNT(*) >= 10;
```

Understand:

```text
WHERE  → filters rows before grouping
HAVING → filters groups after grouping
```

## 8.4 Conditional Aggregation
- [ ] Combine `CASE` and aggregate functions.
- [ ] Count or sum subsets of rows.
- [ ] Understand PostgreSQL aggregate `FILTER`.

**Competency:** Can generate common application statistics and reporting queries.

---

# 9. Database Design & Normalization

## 9.1 Requirements to Entities
Given:

> Users can create posts and posts can have many tags.

Derive:

```text
users
posts
tags
post_tags
```

- [ ] Identify entities.
- [ ] Identify attributes.
- [ ] Identify relationships.
- [ ] Determine cardinality.

## 9.2 Functional Dependencies
Understand conceptually:

```text
key → attribute
```

Example:

```text
user_id → email
```

- [ ] Identify attributes that depend on a key.
- [ ] Recognize duplicated facts.

## 9.3 First Normal Form — 1NF
- [ ] Atomic/appropriately structured column values.
- [ ] No repeating column groups.
- [ ] Rows identifiable by a key.

Recognize:

```text
user
email1
email2
email3
```

as a likely design problem.

## 9.4 Second Normal Form — 2NF
- [ ] Be in 1NF.
- [ ] Remove partial dependencies on part of a composite key.

## 9.5 Third Normal Form — 3NF
- [ ] Be in 2NF.
- [ ] Remove inappropriate transitive dependencies.

## 9.6 BCNF
- [ ] Understand BCNF conceptually.
- [ ] Recognize it as a stronger treatment of certain dependency problems.
- [ ] Do not require formal-theory mastery for ordinary full-stack work.

## 9.7 Denormalization
- [ ] Understand why normalization is the default.
- [ ] Recognize legitimate performance/read-model reasons for duplication.
- [ ] Understand consistency costs introduced by denormalization.

## 9.8 Design Tradeoffs
- [ ] Avoid comma-separated lists masquerading as relationships.
- [ ] Avoid unnecessary tables.
- [ ] Avoid giant catch-all tables.
- [ ] Avoid premature JSONB use for relational data.
- [ ] Understand nullable-column tradeoffs.
- [ ] Choose meaningful constraints.

**Competency:** Can design a normalized relational schema for a small-to-medium full-stack application and explain the relationships and tradeoffs.

---

# 10. Intermediate & Advanced SQL

## 10.1 Subqueries

```sql
SELECT *
FROM users
WHERE id IN (
    SELECT user_id
    FROM posts
);
```

Learn:
- [ ] Scalar subqueries.
- [ ] Subqueries in `WHERE`.
- [ ] Correlated subqueries.
- [ ] `EXISTS`.
- [ ] `NOT EXISTS`.

## 10.2 Common Table Expressions

```sql
WITH active_users AS (
    SELECT *
    FROM users
    WHERE active = TRUE
)
SELECT *
FROM active_users;
```

- [ ] Use CTEs to structure complex queries.
- [ ] Understand their readability benefits.
- [ ] Understand that a CTE is not automatically a performance optimization.

## 10.3 Recursive CTEs
Understand basic recursive querying for:
- [ ] Trees.
- [ ] Hierarchies.
- [ ] Parent/child structures.

## 10.4 Set Operations
- [ ] `UNION`
- [ ] `UNION ALL`
- [ ] `INTERSECT`
- [ ] `EXCEPT`

Understand duplicate-removal behavior.

## 10.5 Conditional Expressions
- [ ] `CASE`
- [ ] `COALESCE`
- [ ] `NULLIF`
- [ ] `GREATEST`
- [ ] `LEAST`

## 10.6 Window Functions

Learn:

```text
OVER
PARTITION BY
ORDER BY
```

Functions:
- [ ] `ROW_NUMBER()`
- [ ] `RANK()`
- [ ] `DENSE_RANK()`
- [ ] `LAG()`
- [ ] `LEAD()`
- [ ] Aggregate window functions.

Understand the distinction:

```text
GROUP BY        collapses rows
window function preserves rows
```

## 10.7 Useful SQL Expressions
- [ ] String operations.
- [ ] Date/time arithmetic.
- [ ] Pattern matching.
- [ ] `NULL` handling.
- [ ] Numeric operations.
- [ ] PostgreSQL `FILTER`.

**Competency:** Can write multi-stage queries for realistic application requirements while keeping them understandable.

---

# 11. Transactions & Concurrency

## 11.1 Transactions

```sql
BEGIN;

UPDATE accounts ...;
INSERT INTO transactions ...;

COMMIT;
```

Learn:

```text
BEGIN
COMMIT
ROLLBACK
SAVEPOINT
```

## 11.2 ACID
Understand:
- [ ] Atomicity.
- [ ] Consistency.
- [ ] Isolation.
- [ ] Durability.

Relate each property to application behavior.

## 11.3 Why Transactions Matter
Example:

```text
Create order
├── insert order
├── insert order items
└── decrease inventory
```

Either the complete operation succeeds or it should normally fail as a unit.

## 11.4 Isolation
Understand conceptually:
- [ ] Read Committed.
- [ ] Repeatable Read.
- [ ] Serializable.

Recognize anomalies such as:
- [ ] Non-repeatable behavior.
- [ ] Concurrent updates.
- [ ] Serialization failures.

## 11.5 MVCC
Understand at application-developer depth:
- [ ] PostgreSQL uses Multi-Version Concurrency Control.
- [ ] Readers and writers do not always block one another.
- [ ] Transactions can observe different row versions.
- [ ] Old row versions contribute to the need for vacuuming.

## 11.6 Locks & Deadlocks
- [ ] Understand row locks.
- [ ] Understand table locks conceptually.
- [ ] Use `SELECT ... FOR UPDATE` when appropriate.
- [ ] Recognize deadlocks.
- [ ] Keep transactions appropriately short.
- [ ] Understand consistent lock ordering as a prevention technique.

**Competency:** Can identify multi-query application operations that require transactions and reason about basic concurrency problems.

---

# 12. Indexes & Query Performance

## 12.1 Why Indexes Exist

Understand the tradeoff:

```text
faster reads
      ↕
storage + slower writes + maintenance
```

- [ ] Know that indexes are not free.
- [ ] Know that indexing every column is not a valid strategy.

## 12.2 B-tree Indexes

```sql
CREATE INDEX idx_users_email
ON users(email);
```

Understand common use for:
- [ ] Equality.
- [ ] Ranges.
- [ ] Sorting.

## 12.3 Unique Indexes
Understand relationship between:
- [ ] `UNIQUE` constraints.
- [ ] Unique indexes.

## 12.4 Composite Indexes

```sql
CREATE INDEX idx_posts_user_created
ON posts(user_id, created_at);
```

- [ ] Understand column order.
- [ ] Match indexes to real query patterns.

## 12.5 Specialized Indexes
Developer-level understanding of:
- [ ] GIN.
- [ ] GiST.
- [ ] BRIN.
- [ ] Hash.

Know common use cases without requiring internal implementation mastery.

## 12.6 Advanced Index Forms
- [ ] Partial indexes.
- [ ] Expression indexes.
- [ ] Covering indexes / `INCLUDE`.

## 12.7 `EXPLAIN`

```sql
EXPLAIN
SELECT *
FROM users
WHERE email = 'alice@example.com';
```

Learn to recognize:
- [ ] Sequential scan.
- [ ] Index scan.
- [ ] Bitmap scan.
- [ ] Join strategies conceptually.
- [ ] Estimated rows and costs.

## 12.8 `EXPLAIN ANALYZE`
- [ ] Understand that the query is actually executed.
- [ ] Compare estimates to actual execution.
- [ ] Identify expensive operations.
- [ ] Use carefully with data-changing statements.

## 12.9 Performance Reasoning
Recognize:
- [ ] Missing indexes.
- [ ] Unnecessary columns.
- [ ] Excessive result sets.
- [ ] Poor joins.
- [ ] N+1 query patterns at the application layer.
- [ ] Inefficient pagination.
- [ ] Functions/casts that prevent useful index access.
- [ ] Poor selectivity.

**Competency:** Can diagnose common slow application queries with `EXPLAIN ANALYZE` and make evidence-based indexing/query changes.

---

# 13. PostgreSQL-Specific Features

## 13.1 Identity Columns & Sequences
- [ ] Understand identity columns.
- [ ] Understand sequences conceptually.
- [ ] Recognize older `SERIAL` syntax.
- [ ] Prefer modern identity syntax for new designs when appropriate.

## 13.2 `ON CONFLICT` / Upsert

```sql
INSERT INTO users (email)
VALUES ('alice@example.com')
ON CONFLICT (email)
DO UPDATE SET last_login = CURRENT_TIMESTAMP;
```

Understand:
- [ ] `DO NOTHING`.
- [ ] `DO UPDATE`.
- [ ] Conflict targets.
- [ ] Concurrency-safe application use cases.

## 13.3 JSONB
- [ ] Store JSON documents.
- [ ] Access JSON fields.
- [ ] Query JSONB.
- [ ] Update JSONB.
- [ ] Index JSONB where justified.
- [ ] Know when relational columns/tables are preferable.

## 13.4 Arrays
- [ ] Create array columns.
- [ ] Query arrays.
- [ ] Recognize when a relationship table is preferable.

## 13.5 Full-Text Search
Developer-level introduction:
- [ ] `tsvector`.
- [ ] `tsquery`.
- [ ] Understand PostgreSQL-native search capabilities.
- [ ] Know when dedicated search infrastructure may eventually be warranted.

## 13.6 Extensions
Understand PostgreSQL's extension model.

Examples may include:
- [ ] `pg_trgm`.
- [ ] UUID-related functionality depending on PostgreSQL version/use case.

- [ ] Check installed extensions.
- [ ] Understand that extension availability depends on the server environment.

## 13.7 Useful PostgreSQL Operators
Become comfortable reading PostgreSQL-specific:
- [ ] JSON operators.
- [ ] Array operators.
- [ ] Pattern matching.
- [ ] Cast syntax.
- [ ] `ILIKE`.

**Competency:** Can take advantage of PostgreSQL-specific capabilities without unnecessarily coupling every database operation to proprietary features.

---

# 14. Views, Functions & Triggers

## 14.1 Views

```sql
CREATE VIEW active_users AS
SELECT *
FROM users
WHERE active = TRUE;
```

- [ ] Create views.
- [ ] Query views.
- [ ] Understand views as stored queries/interfaces.
- [ ] Understand limitations and dependency concerns.

## 14.2 Materialized Views
- [ ] Understand stored query results.
- [ ] Refresh materialized views.
- [ ] Understand freshness/performance tradeoffs.

## 14.3 Functions
- [ ] Understand database functions.
- [ ] Understand input parameters and return values.
- [ ] Know when application code is a better location for logic.

## 14.4 Procedures
- [ ] Understand PostgreSQL procedures conceptually.
- [ ] Distinguish procedures from functions at a practical level.

## 14.5 PL/pgSQL
Developer-level familiarity:
- [ ] Variables.
- [ ] Conditions.
- [ ] Basic control flow.
- [ ] Function bodies.

## 14.6 Triggers
Understand:

```text
INSERT
UPDATE
DELETE
       ↓
    Trigger
       ↓
 Trigger Function
```

- [ ] Before/after triggers.
- [ ] Row-level triggers.
- [ ] Audit/update timestamp use cases.
- [ ] Recognize hidden behavior and debugging costs.

**Competency:** Can read and maintain common database-side logic and make an informed choice between SQL/database logic and application-layer logic.

---

# 15. Security & Permissions

## 15.1 Authentication vs Authorization
Understand:

```text
Authentication → Who are you?
Authorization  → What may you do?
```

## 15.2 Roles
- [ ] Create roles.
- [ ] Login roles.
- [ ] Role membership.
- [ ] Ownership.

## 15.3 Permissions

```text
GRANT
REVOKE
```

Understand privileges for:
- [ ] Database.
- [ ] Schema.
- [ ] Table.
- [ ] Sequence.
- [ ] Function.

## 15.4 Least Privilege
- [ ] Avoid running applications as a PostgreSQL superuser.
- [ ] Give application roles only necessary permissions.
- [ ] Separate administration credentials from application credentials.

## 15.5 SQL Injection

Unsafe concept:

```text
"SELECT ... WHERE username = '" + input + "'"
```

Safe concept:

```text
SELECT ... WHERE username = ?
```

or the driver's equivalent parameter syntax.

- [ ] Understand SQL injection.
- [ ] Use parameterized queries.
- [ ] Understand why escaping manually is not the preferred defense.
- [ ] Understand how ORM/query APIs can still be misused.

## 15.6 Credentials
- [ ] Do not hard-code production passwords.
- [ ] Use environment/configuration secret mechanisms appropriately.
- [ ] Understand database URLs can contain secrets.
- [ ] Keep secrets out of source control.

## 15.7 Row-Level Security
- [ ] Understand RLS conceptually.
- [ ] Know that PostgreSQL can enforce per-row access policies.
- [ ] Recognize when application authorization alone may or may not be sufficient.

## 15.8 Transport Security
- [ ] Understand why TLS matters for remote database connections.
- [ ] Understand that production connection requirements differ from local development.

**Competency:** Can configure and use PostgreSQL from an application without relying on superuser access or vulnerable dynamic SQL.

---

# 16. Application Integration

This branch connects PostgreSQL knowledge to full-stack development without becoming a framework-specific skill tree.

## 16.1 Connection Configuration
Understand:

```text
Application
    ↓
Database driver
    ↓
Connection / pool
    ↓
PostgreSQL
```

Know:
- [ ] Host.
- [ ] Port.
- [ ] Database.
- [ ] Username.
- [ ] Password.
- [ ] SSL options.
- [ ] Connection URL.

## 16.2 Database Drivers
- [ ] Understand the role of a database driver.
- [ ] Distinguish application language APIs from PostgreSQL itself.
- [ ] Recognize common PostgreSQL drivers in major ecosystems.

## 16.3 Parameterized Queries
- [ ] Bind application values safely.
- [ ] Understand placeholders.
- [ ] Avoid string-built SQL.

## 16.4 Connection Pooling
- [ ] Understand why applications pool connections.
- [ ] Understand that database connections are limited resources.
- [ ] Recognize pool exhaustion.
- [ ] Return/release connections correctly.
- [ ] Avoid excessively large pools.

## 16.5 Transactions from Application Code
- [ ] Begin application-managed transactions.
- [ ] Commit successful work.
- [ ] Roll back failures.
- [ ] Understand transaction boundaries.
- [ ] Avoid performing slow external operations inside database transactions when possible.

## 16.6 ORM Fundamentals
Understand:

```text
Application Object
       ↕
      ORM
       ↕
      SQL
       ↕
 PostgreSQL Table
```

- [ ] Understand object-relational mapping.
- [ ] Understand entities/models.
- [ ] Understand repositories/data-access abstractions.
- [ ] Understand generated SQL.
- [ ] Know that ORM knowledge does not replace SQL knowledge.

## 16.7 ORM Performance
Recognize:
- [ ] N+1 queries.
- [ ] Lazy/eager loading consequences.
- [ ] Excessive object loading.
- [ ] Missing indexes.
- [ ] Inefficient generated joins.
- [ ] Transaction boundary mistakes.

## 16.8 Spring Boot / JPA Awareness
At PostgreSQL-tree depth:
- [ ] Understand datasource configuration.
- [ ] Understand PostgreSQL JDBC connectivity.
- [ ] Understand entities mapping to tables.
- [ ] Understand repositories eventually generate database operations.
- [ ] Inspect generated SQL when debugging.
- [ ] Understand why schema migrations are preferable to uncontrolled automatic production schema modification.

## 16.9 Node/TypeScript Awareness
At PostgreSQL-tree depth:
- [ ] Understand PostgreSQL client libraries.
- [ ] Understand asynchronous queries.
- [ ] Understand pooling.
- [ ] Understand parameterized statements.
- [ ] Understand transaction handling.

**Competency:** Can trace a database operation from HTTP request → backend/service/data-access code → SQL → PostgreSQL → returned data.

---

# 17. Schema Migrations & Evolution

## 17.1 Why Migrations Exist

Understand:

```text
Version 1
users(id, name)

       ↓ migration

Version 2
users(id, name, email)
```

- [ ] Treat schema as versioned application state.
- [ ] Reproduce schema changes across environments.

## 17.2 Migration Fundamentals
- [ ] Create tables through migrations.
- [ ] Add/remove columns.
- [ ] Add/remove indexes.
- [ ] Add constraints.
- [ ] Transform existing data.
- [ ] Understand forward and rollback strategies.

## 17.3 Migration Tools
Recognize ecosystem tools such as:
- [ ] Flyway.
- [ ] Liquibase.
- [ ] Framework/ORM migration systems.

Focus on the migration concept rather than mastering every tool.

## 17.4 Safe Schema Evolution
Understand risks of:
- [ ] Dropping populated columns.
- [ ] Adding `NOT NULL` without handling existing rows.
- [ ] Changing data types.
- [ ] Renaming objects while old application versions run.
- [ ] Long-running schema changes.
- [ ] Large table rewrites.

## 17.5 Application/Schema Compatibility
- [ ] Coordinate code and database changes.
- [ ] Understand backward-compatible migrations.
- [ ] Separate schema migration from arbitrary runtime schema generation.

**Competency:** Can evolve a production application's schema deliberately rather than manually editing each environment.

---

# 18. Testing, Debugging & Data Integrity

## 18.1 Query Testing
- [ ] Test SQL independently of application code.
- [ ] Use representative data.
- [ ] Test edge cases.
- [ ] Test `NULL`.
- [ ] Test empty result sets.
- [ ] Test duplicate data.
- [ ] Test boundary values.

## 18.2 Constraint Testing
Verify that invalid data fails:
- [ ] Duplicate unique value.
- [ ] Missing required value.
- [ ] Invalid foreign key.
- [ ] Failed `CHECK`.
- [ ] Invalid deletion.

## 18.3 Transaction Testing
- [ ] Verify rollback behavior.
- [ ] Test partial failures.
- [ ] Test concurrent behavior where important.

## 18.4 Debugging SQL Errors
Learn to interpret:
- [ ] Syntax errors.
- [ ] Missing relations.
- [ ] Missing columns.
- [ ] Type mismatch.
- [ ] Constraint violations.
- [ ] Foreign-key violations.
- [ ] Unique violations.
- [ ] Permission errors.
- [ ] Connection errors.
- [ ] Deadlocks.

## 18.5 Application Debugging

Trace:

```text
Frontend
   ↓
HTTP request
   ↓
Controller / route
   ↓
Service
   ↓
Repository / query layer
   ↓
SQL
   ↓
PostgreSQL
```

Ask:
1. Did the request reach the backend?
2. Did application validation pass?
3. What SQL was executed?
4. What parameters were supplied?
5. Did PostgreSQL accept the query?
6. What rows were affected?
7. Was the transaction committed?
8. Was the result mapped correctly?

## 18.6 Test Databases
- [ ] Keep tests isolated from production data.
- [ ] Reset/recreate test state.
- [ ] Seed deterministic test data.
- [ ] Understand integration tests involving a real PostgreSQL instance.

**Competency:** Can determine whether a data-related bug originates in frontend assumptions, backend logic, generated/manual SQL, schema design, constraints, or PostgreSQL itself.

---

# 19. Production Fundamentals

This section stops at the level a professional full-stack developer should understand. It is **not** intended to produce a PostgreSQL DBA.

## 19.1 Backups
Understand:
- [ ] Why backups are necessary.
- [ ] Logical backups.
- [ ] `pg_dump`.
- [ ] `pg_restore`.
- [ ] Restoration must be tested, not merely assumed.

## 19.2 Maintenance
Developer-level understanding of:
- [ ] `VACUUM`.
- [ ] `ANALYZE`.
- [ ] Autovacuum.
- [ ] Table/index bloat conceptually.
- [ ] Statistics used by the planner.

Know why these exist without requiring expert tuning knowledge.

## 19.3 Monitoring
Know that developers should observe:
- [ ] Slow queries.
- [ ] Connection counts.
- [ ] Locking/blocking.
- [ ] Database size.
- [ ] Query frequency.
- [ ] Errors.
- [ ] Long-running transactions.

Become familiar with the purpose of PostgreSQL `pg_stat_*` views.

## 19.4 Production Configuration Awareness
Understand:
- [ ] Development and production configuration differ.
- [ ] Connection limits matter.
- [ ] Memory/resources are finite.
- [ ] Timeouts are useful.
- [ ] Credentials must be protected.
- [ ] Network access should be restricted.
- [ ] Production applications should not depend on superuser privileges.

## 19.5 Basic Partitioning Awareness
- [ ] Understand what table partitioning is.
- [ ] Recognize when extremely large tables may benefit.
- [ ] Know that most ordinary application tables do not require it.

## 19.6 Replication/WAL Awareness
Know at conceptual level:
- [ ] PostgreSQL uses a Write-Ahead Log (WAL).
- [ ] Replication exists.
- [ ] Read replicas may be used in larger systems.
- [ ] Replication and high availability are separate specialist/DBA topics.

No implementation mastery is required for this tree.

**Competency:** Can participate intelligently in deploying and operating a PostgreSQL-backed application while knowing when a database administrator/platform engineer is needed.

---

# 20. Professional Full-Stack PostgreSQL Mastery

A developer completing this tree should be able to build a database layer without following a step-by-step tutorial.

## 20.1 Design Challenge

Given:

> Build an application where users create projects, invite other users, assign tasks, comment on tasks, and apply tags.

Independently identify something similar to:

```text
users
projects
project_members
tasks
comments
tags
task_tags
```

Then:
- [ ] Select keys.
- [ ] Define relationships.
- [ ] Select data types.
- [ ] Define constraints.
- [ ] Normalize the schema.
- [ ] Identify useful indexes.
- [ ] Create migrations.

## 20.2 Query Challenge

Write from memory:
- [ ] CRUD queries.
- [ ] Filtered queries.
- [ ] Multi-table joins.
- [ ] Aggregate queries.
- [ ] Subqueries.
- [ ] CTEs.
- [ ] Window-function queries when appropriate.
- [ ] Upserts.
- [ ] Transactional operations.

## 20.3 Application Challenge

Build:

```text
Frontend
    ↓ HTTP
Backend API
    ↓
Service / Business Logic
    ↓
Data Access / ORM
    ↓ SQL
PostgreSQL
```

Be able to explain every boundary.

## 20.4 Debugging Challenge

Given a broken feature:
- [ ] Inspect the HTTP request.
- [ ] Trace backend execution.
- [ ] Inspect generated/manual SQL.
- [ ] Run the SQL directly.
- [ ] Inspect the schema.
- [ ] Inspect constraints.
- [ ] Inspect transaction behavior.
- [ ] Inspect query plans when performance is involved.
- [ ] Identify the actual failing layer.

## 20.5 Performance Challenge

Given a slow endpoint:
- [ ] Determine how many SQL queries it executes.
- [ ] Identify N+1 behavior.
- [ ] Run `EXPLAIN ANALYZE`.
- [ ] Interpret the important plan operations.
- [ ] Determine whether an index is appropriate.
- [ ] Rewrite inefficient SQL where necessary.
- [ ] Measure the result rather than assuming improvement.

## 20.6 Security Challenge
- [ ] Use parameterized SQL.
- [ ] Use a non-superuser application role.
- [ ] Protect credentials.
- [ ] Define appropriate permissions.
- [ ] Apply constraints for data integrity.
- [ ] Understand TLS requirements.
- [ ] Explain where database security ends and application authorization begins.

## 20.7 Production Change Challenge
Safely:
- [ ] Add a new feature requiring schema changes.
- [ ] Write the migration.
- [ ] Handle existing rows.
- [ ] Deploy compatible application code.
- [ ] Verify the migration.
- [ ] Understand backup/restore implications.
- [ ] Monitor for database errors or performance regression.

---

# Mastery Milestones

## Level 0 — New to Databases
Can explain:
- Database
- Table
- Row
- Column
- SQL

---

## Level 1 — SQL Beginner
Can independently use:

```text
SELECT
WHERE
ORDER BY
INSERT
UPDATE
DELETE
```

Can create a simple table.

---

## Level 2 — Relational SQL Developer
Can use:

```text
PRIMARY KEY
FOREIGN KEY
UNIQUE
NOT NULL
CHECK
JOIN
GROUP BY
HAVING
```

Can model one-to-many and many-to-many relationships.

---

## Level 3 — PostgreSQL Application Developer
Can:
- Design normalized schemas.
- Use PostgreSQL data types intentionally.
- Write complex joins.
- Use subqueries and CTEs.
- Use transactions.
- Create useful indexes.
- Use `EXPLAIN ANALYZE`.
- Work comfortably with `psql`.
- Use PostgreSQL-specific features appropriately.

---

## Level 4 — Full-Stack PostgreSQL Developer
Can:
- Integrate PostgreSQL with backend applications.
- Understand ORM-generated SQL.
- Manage transaction boundaries.
- Avoid SQL injection.
- Diagnose N+1 queries.
- Use connection pooling appropriately.
- Build and apply schema migrations.
- Debug across frontend → backend → database boundaries.

---

## Level 5 — Professional PostgreSQL Full-Stack Developer

Can independently:

1. Convert application requirements into a relational schema.
2. Normalize that schema appropriately.
3. Select correct PostgreSQL data types.
4. Define keys, constraints, and relationships.
5. Write non-trivial SQL without depending entirely on an ORM.
6. Integrate PostgreSQL with application code.
7. Design safe transaction boundaries.
8. Diagnose query and application performance problems.
9. Design and verify useful indexes.
10. Secure application database access.
11. Evolve schemas through migrations.
12. Test database behavior and data integrity.
13. Debug failures across the full stack.
14. Understand PostgreSQL production fundamentals.
15. Recognize when a problem has moved beyond application development into specialist DBA/database-engineering territory.

---

# Recommended Learning Dependency Graph

```text
Relational Foundations
        │
        ▼
Basic SQL ──────────────┐
        │               │
        ▼               ▼
Tables & Types     psql Skills
        │
        ▼
Keys & Constraints
        │
        ▼
Relationships
        │
        ▼
Joins
        │
        ├───────────────┐
        ▼               ▼
Normalization      Aggregation
        │               │
        └───────┬───────┘
                ▼
         Advanced SQL
                │
        ┌───────┴────────┐
        ▼                ▼
   Transactions       Indexes
        │                │
        │                ▼
        │          Query Planning
        │                │
        └───────┬────────┘
                ▼
      PostgreSQL Features
                │
        ┌───────┴──────────┐
        ▼                  ▼
    Security       Application Integration
                           │
                           ▼
                       Migrations
                           │
                           ▼
                  Testing & Debugging
                           │
                           ▼
                 Production Fundamentals
                           │
                           ▼
             Professional Full-Stack
               PostgreSQL Developer
```

---

# Full-Stack PostgreSQL Mastery Checklist

A professional full-stack developer should eventually be able to answer **yes** to these questions:

### SQL
- [ ] Can I write CRUD SQL from memory?
- [ ] Can I write joins without guessing?
- [ ] Can I aggregate and group data?
- [ ] Can I use subqueries and CTEs?
- [ ] Can I use window functions when they solve the problem better?

### Schema Design
- [ ] Can I turn application requirements into tables?
- [ ] Can I identify one-to-one, one-to-many, and many-to-many relationships?
- [ ] Can I normalize a schema through 3NF?
- [ ] Can I recognize when denormalization is justified?

### Integrity
- [ ] Can I choose primary and foreign keys?
- [ ] Can I define useful constraints?
- [ ] Can I predict foreign-key deletion behavior?
- [ ] Can I prevent invalid state at the database layer?

### PostgreSQL
- [ ] Can I use `psql` to inspect and debug a database?
- [ ] Can I choose appropriate PostgreSQL data types?
- [ ] Can I use identity columns, `RETURNING`, and `ON CONFLICT`?
- [ ] Can I use JSONB appropriately without treating PostgreSQL as a document database?

### Transactions
- [ ] Can I identify operations that need a transaction?
- [ ] Can I explain ACID?
- [ ] Can I recognize basic concurrency problems?
- [ ] Can I reason about locks and deadlocks?

### Performance
- [ ] Can I explain what an index does?
- [ ] Can I choose useful index columns?
- [ ] Can I use composite and partial indexes appropriately?
- [ ] Can I read the important parts of `EXPLAIN ANALYZE`?
- [ ] Can I diagnose common ORM/query performance problems?

### Application Development
- [ ] Can I connect an application to PostgreSQL?
- [ ] Can I use parameterized queries?
- [ ] Can I explain connection pooling?
- [ ] Can I inspect ORM-generated SQL?
- [ ] Can I trace an HTTP request all the way to its SQL operations?

### Schema Evolution
- [ ] Can I create migrations?
- [ ] Can I safely change populated tables?
- [ ] Can I coordinate application and database changes?

### Security
- [ ] Can I explain SQL injection?
- [ ] Can I prevent SQL injection?
- [ ] Can I configure a least-privilege application role?
- [ ] Can I keep credentials outside source code?

### Debugging
- [ ] Can I test SQL independently?
- [ ] Can I identify constraint failures?
- [ ] Can I diagnose connection and permission errors?
- [ ] Can I determine whether a bug is frontend, backend, ORM, SQL, schema, transaction, or database related?

### Production
- [ ] Do I understand backups and restoration?
- [ ] Do I understand why `VACUUM`, autovacuum, and `ANALYZE` exist?
- [ ] Do I know what database metrics developers should monitor?
- [ ] Do I recognize when a problem requires DBA/database-engineering expertise?

If these capabilities are consistently demonstrable, the PostgreSQL/database portion of a professional full-stack development skill set is in place.
