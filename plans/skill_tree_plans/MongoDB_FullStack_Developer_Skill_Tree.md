# MongoDB Skill Tree
## Full-Stack Developer Path

**Goal:** Progress from no document-database experience to professional MongoDB application development for full-stack software.

**Scope:** MongoDB fundamentals, BSON, CRUD, query operators, document modeling, embedding and references, validation, aggregation, indexes, transactions, security, application integration, schema evolution, testing, debugging, MongoDB Atlas, and production fundamentals.

**Not the goal:** Advanced MongoDB administration, distributed-database engineering, sophisticated sharding architecture, replica-set internals, or specialist SRE/DBA work.

---

# 0. Skill Tree Overview

```text
MongoDB Full-Stack Developer
│
├── 1. Document Database Foundations
├── 2. MongoDB Environment & mongosh
├── 3. BSON, Documents & Data Types
├── 4. CRUD Fundamentals
├── 5. Query Operators & Projections
├── 6. Updates & Array Operations
├── 7. Document & Schema Design
├── 8. Relationships: Embedding vs Referencing
├── 9. Validation & Data Integrity
├── 10. Aggregation Pipeline
├── 11. Indexes & Query Performance
├── 12. Atomicity, Transactions & Concurrency
├── 13. MongoDB-Specific Capabilities
├── 14. Security & Access Control
├── 15. Application Integration
├── 16. ODM / Framework Integration
├── 17. Schema Evolution & Data Migrations
├── 18. Testing & Debugging
├── 19. MongoDB Atlas & Production Fundamentals
└── 20. Professional Full-Stack MongoDB Mastery
```

---

# 1. Document Database Foundations

## 1.1 What MongoDB Is

- [ ] Explain what a database management system is.
- [ ] Explain what MongoDB is.
- [ ] Explain what a document database is.
- [ ] Explain how documents differ from relational rows.
- [ ] Explain persistent data versus application memory.
- [ ] Understand that MongoDB stores BSON documents.
- [ ] Understand that MongoDB is not simply "JSON files in a database."

**Competency:** Can explain where MongoDB fits in a full-stack application.

## 1.2 Core Structure

Understand:

```text
MongoDB Deployment
└── Database
    └── Collection
        ├── Document
        │   ├── Field
        │   ├── Nested Document
        │   └── Array
        └── Document
```

Learn:
- [ ] Database.
- [ ] Collection.
- [ ] Document.
- [ ] Field.
- [ ] Nested/embedded document.
- [ ] Array.
- [ ] `_id`.

## 1.3 MongoDB vs Relational Databases

Understand the conceptual comparison:

```text
Relational                 MongoDB

Database                   Database
Table                      Collection
Row                        Document
Column                     Field
Primary key                _id
JOIN                       Often embedding,
                           referencing, or $lookup
Schema                     Flexible document structure
```

But also understand that these are only approximate analogies.

## 1.4 Flexible Schema

Understand that documents in one collection can technically have different structures.

Example:

```javascript
{
  name: "Alice",
  email: "alice@example.com"
}
```

and:

```javascript
{
  name: "Bob",
  email: "bob@example.com",
  phone: "555-1234"
}
```

may coexist.

Learn:
- [ ] Flexible schema does not mean "no schema."
- [ ] Applications still require predictable document structures.
- [ ] Validation can enforce structure.
- [ ] Schema design remains important.

**Competency:** Can explain why "MongoDB is schemaless" is an incomplete description.

---

# 2. MongoDB Environment & `mongosh`

## 2.1 Connection Fundamentals

Understand:

```text
Application / mongosh
        ↓
MongoDB Driver
        ↓
MongoDB Server / Atlas
```

Know:
- [ ] Host.
- [ ] Port.
- [ ] Database.
- [ ] Username.
- [ ] Password.
- [ ] Connection URI.
- [ ] Authentication database conceptually.
- [ ] TLS conceptually.

MongoDB's common local port:

```text
27017
```

## 2.2 `mongosh`

Learn basic commands:

```javascript
show dbs
use myDatabase
show collections
db
```

Inspect collections:

```javascript
db.users.find()
db.users.findOne()
```

Inspect indexes:

```javascript
db.users.getIndexes()
```

Learn:
- [ ] Connect to MongoDB.
- [ ] Select a database.
- [ ] List databases.
- [ ] List collections.
- [ ] Inspect documents.
- [ ] Run CRUD operations.
- [ ] Run aggregation pipelines.
- [ ] Inspect indexes.
- [ ] Execute JavaScript-based shell commands.

## 2.3 Database and Collection Creation

Understand MongoDB's commonly lazy creation behavior.

```javascript
use guestbook

db.messages.insertOne({
  name: "Alice",
  message: "Hello"
})
```

- [ ] Understand when databases appear.
- [ ] Understand when collections appear.
- [ ] Explicitly create collections when configuration or validation requires it.

## 2.4 MongoDB Compass Awareness

- [ ] Know what MongoDB Compass is.
- [ ] Browse databases and collections.
- [ ] Inspect documents.
- [ ] Run filters.
- [ ] Inspect indexes.
- [ ] Use GUI tools without becoming dependent on them.

**Competency:** Can connect to an unfamiliar MongoDB database and inspect its collections, documents, and indexes without relying exclusively on a GUI.

---

# 3. BSON, Documents & Data Types

## 3.1 BSON

Understand:

```text
JSON-like application data
        ↓
BSON representation
        ↓
MongoDB storage
```

- [ ] Explain BSON at a developer level.
- [ ] Understand that BSON supports types not represented directly by ordinary JSON.
- [ ] Recognize that driver libraries map language types to BSON types.

## 3.2 Basic Types

Learn:
- [ ] String.
- [ ] Boolean.
- [ ] Null.
- [ ] Integer types.
- [ ] Double.
- [ ] Decimal128.
- [ ] Date.
- [ ] ObjectId.
- [ ] Array.
- [ ] Embedded document.
- [ ] Binary data.

## 3.3 `_id`

Every document requires a unique `_id`.

Example:

```javascript
{
  _id: ObjectId("..."),
  username: "alice"
}
```

Understand:
- [ ] `_id` uniquely identifies a document.
- [ ] MongoDB can generate `_id` automatically.
- [ ] `_id` is indexed automatically.
- [ ] `_id` does not have to be an ObjectId.
- [ ] Changing identifier strategy should be deliberate.

## 3.4 ObjectId

Understand conceptually:
- [ ] ObjectId is a BSON identifier type.
- [ ] Drivers can create ObjectIds.
- [ ] String and ObjectId values are different types.
- [ ] Type mismatches commonly cause failed queries.

## 3.5 Dates

Understand:
- [ ] BSON Date.
- [ ] Store actual dates as date values rather than formatted strings when appropriate.
- [ ] Time zones are largely an application/display concern around stored UTC instants.
- [ ] Drivers map date/time types differently.

## 3.6 Numeric Types

Understand:
- [ ] Integers versus doubles.
- [ ] Decimal128 for exact decimal requirements.
- [ ] Why floating-point values can be inappropriate for money.

## 3.7 Nested Documents

Example:

```javascript
{
  username: "alice",
  profile: {
    displayName: "Alice",
    location: "Texas"
  }
}
```

## 3.8 Arrays

Example:

```javascript
{
  username: "alice",
  roles: ["user", "moderator"]
}
```

**Competency:** Can choose appropriate BSON structures and recognize common application/BSON type mismatches.

---

# 4. CRUD Fundamentals

CRUD:

```text
Create
Read
Update
Delete
```

## 4.1 Insert One

```javascript
db.users.insertOne({
  username: "alice",
  active: true
})
```

Learn:
- [ ] Insert a document.
- [ ] Read the returned inserted ID.

## 4.2 Insert Many

```javascript
db.users.insertMany([
  { username: "alice" },
  { username: "bob" }
])
```

- [ ] Insert multiple documents.
- [ ] Understand bulk insertion at a basic level.

## 4.3 `find()`

```javascript
db.users.find()
```

Filter:

```javascript
db.users.find({
  active: true
})
```

## 4.4 `findOne()`

```javascript
db.users.findOne({
  username: "alice"
})
```

Understand the difference between retrieving one document and a result cursor/set.

## 4.5 Projection

```javascript
db.users.find(
  { active: true },
  { username: 1, email: 1 }
)
```

Learn:
- [ ] Include fields.
- [ ] Exclude fields.
- [ ] Understand `_id` projection behavior.
- [ ] Avoid returning unnecessary data.

## 4.6 Sorting

```javascript
db.users.find().sort({
  createdAt: -1
})
```

Understand:

```text
1  ascending
-1 descending
```

## 4.7 Limiting

```javascript
db.users.find().limit(20)
```

## 4.8 Skipping

```javascript
db.users.find()
  .skip(20)
  .limit(20)
```

- [ ] Understand basic offset-style pagination.
- [ ] Recognize scalability limitations of large skips.
- [ ] Learn cursor/range pagination later.

## 4.9 Update One

```javascript
db.users.updateOne(
  { username: "alice" },
  {
    $set: {
      active: false
    }
  }
)
```

## 4.10 Update Many

```javascript
db.users.updateMany(
  { active: false },
  {
    $set: {
      archived: true
    }
  }
)
```

## 4.11 Replace One

```javascript
db.users.replaceOne(
  { username: "alice" },
  {
    username: "alice",
    active: true
  }
)
```

Understand replacement versus field-level update.

## 4.12 Delete One

```javascript
db.users.deleteOne({
  username: "alice"
})
```

## 4.13 Delete Many

```javascript
db.users.deleteMany({
  archived: true
})
```

**Competency:** Can independently implement CRUD operations for a MongoDB-backed application.

---

# 5. Query Operators & Projections

## 5.1 Comparison Operators

Learn:

```text
$eq
$ne
$gt
$gte
$lt
$lte
$in
$nin
```

Example:

```javascript
db.products.find({
  price: {
    $gte: 10,
    $lte: 50
  }
})
```

## 5.2 Logical Operators

Learn:

```text
$and
$or
$nor
$not
```

Example:

```javascript
db.users.find({
  $or: [
    { role: "admin" },
    { role: "moderator" }
  ]
})
```

## 5.3 Element Operators

Learn:
- [ ] `$exists`.
- [ ] `$type`.

Example:

```javascript
db.users.find({
  phone: {
    $exists: true
  }
})
```

## 5.4 Nested Fields

Dot notation:

```javascript
db.users.find({
  "profile.country": "USA"
})
```

- [ ] Query nested values.
- [ ] Project nested values.
- [ ] Update nested values.

## 5.5 Array Queries

Example:

```javascript
db.users.find({
  roles: "admin"
})
```

Learn:
- [ ] Match array elements.
- [ ] `$all`.
- [ ] `$size`.
- [ ] `$elemMatch`.

## 5.6 Evaluation Operators

Developer-level familiarity with:
- [ ] Regular-expression queries.
- [ ] `$expr`.
- [ ] Text query concepts where relevant.

## 5.7 Null and Missing Fields

Understand the important distinction between:
- [ ] Field containing `null`.
- [ ] Field not existing.
- [ ] Queries that may match both depending on syntax.

**Competency:** Can construct filters for realistic application requirements involving nested documents, arrays, ranges, optional fields, and multiple conditions.

---

# 6. Updates & Array Operations

## 6.1 `$set`

```javascript
{
  $set: {
    username: "newName"
  }
}
```

## 6.2 `$unset`

```javascript
{
  $unset: {
    temporaryField: ""
  }
}
```

## 6.3 Numeric Updates

Learn:
- [ ] `$inc`.
- [ ] `$mul`.
- [ ] `$min`.
- [ ] `$max`.

Example:

```javascript
{
  $inc: {
    loginCount: 1
  }
}
```

## 6.4 Array Updates

Learn:
- [ ] `$push`.
- [ ] `$addToSet`.
- [ ] `$pull`.
- [ ] `$pop`.

Understand:

```text
$push       → add value, duplicates possible
$addToSet   → add only if not already present
$pull       → remove matching values
```

## 6.5 Positional Array Updates

Become familiar with:
- [ ] `$`.
- [ ] `$[]`.
- [ ] `$[identifier]`.
- [ ] `arrayFilters`.

## 6.6 Upserts

```javascript
db.users.updateOne(
  { email: "alice@example.com" },
  {
    $set: {
      active: true
    }
  },
  {
    upsert: true
  }
)
```

Understand update-or-insert behavior.

## 6.7 Update Pipelines

Developer-level awareness:
- [ ] Updates can use aggregation-style expressions where appropriate.
- [ ] Useful for computed transformations.

**Competency:** Can modify specific portions of documents safely without unnecessarily reading and replacing entire documents.

---

# 7. Document & Schema Design

This is one of the most important branches of MongoDB mastery.

## 7.1 Model Around Application Access

Instead of asking only:

> What entities exist?

also ask:

> How will the application read and update this data?

Understand that access patterns strongly influence MongoDB schema design.

## 7.2 Document Boundaries

Decide what belongs together.

Example:

```javascript
{
  _id: ObjectId("..."),
  title: "Order",
  shippingAddress: {
    street: "...",
    city: "...",
    state: "..."
  }
}
```

A shipping address snapshot may naturally belong inside the order document.

## 7.3 Embedding

Advantages:
- [ ] Related data retrieved together.
- [ ] Fewer queries.
- [ ] Atomic updates within one document.
- [ ] Natural ownership relationship.

Risks:
- [ ] Large documents.
- [ ] Unbounded arrays.
- [ ] Duplicated data.
- [ ] Difficult independent updates.

## 7.4 Referencing

Example:

```javascript
{
  userId: ObjectId("...")
}
```

Advantages:
- [ ] Independent lifecycle.
- [ ] Avoid excessive duplication.
- [ ] Better for large/unbounded relationships.

Costs:
- [ ] Additional queries or aggregation.
- [ ] Application-side coordination.
- [ ] More complex consistency management.

## 7.5 Duplication / Denormalization

Understand that controlled duplication can be intentional.

Example order item:

```javascript
{
  productId: ObjectId("..."),
  productName: "Keyboard",
  priceAtPurchase: 49.99
}
```

The current product price and the historical purchase price are different facts.

## 7.6 Document Growth

Understand:
- [ ] Documents have a maximum BSON size.
- [ ] Unbounded arrays are dangerous.
- [ ] Frequently growing documents may become problematic.
- [ ] High-contention documents can create bottlenecks.

## 7.7 Avoid Relational Modeling by Habit

Poor MongoDB design can result from recreating normalized SQL tables as collections without considering access patterns.

Learn to ask:

```text
Should this be embedded?
Should this be referenced?
How often is it read together?
How often does it change?
How large can it grow?
Who owns the data?
Does it need an independent lifecycle?
```

**Competency:** Can design document boundaries based on ownership, cardinality, growth, update patterns, and query patterns.

---

# 8. Relationships: Embedding vs Referencing

## 8.1 One-to-One

Possible embedding:

```javascript
{
  username: "alice",
  profile: {
    displayName: "Alice",
    bio: "..."
  }
}
```

Possible reference:

```javascript
{
  username: "alice",
  profileId: ObjectId("...")
}
```

Know why either might be appropriate.

## 8.2 One-to-Few

Often a strong embedding candidate.

Example:

```javascript
{
  userId: ObjectId("..."),
  addresses: [
    {...},
    {...}
  ]
}
```

## 8.3 One-to-Many

Ask whether the "many" side is bounded.

Example:

```text
User → millions of log entries
```

Do **not** embed millions of growing log records into the user document.

## 8.4 Many-to-Many

Possible strategies:
- [ ] Arrays of references.
- [ ] Relationship collection.
- [ ] Selective duplication.

Choose based on:
- [ ] Cardinality.
- [ ] Query direction.
- [ ] Update frequency.
- [ ] Growth.
- [ ] Consistency requirements.

## 8.5 `$lookup`

Understand aggregation-based joins:

```javascript
{
  $lookup: {
    from: "users",
    localField: "userId",
    foreignField: "_id",
    as: "user"
  }
}
```

- [ ] Know what `$lookup` does.
- [ ] Use it when appropriate.
- [ ] Do not assume `$lookup` should replace thoughtful document modeling.

## 8.6 PostgreSQL Thinking vs MongoDB Thinking

Relational example:

```text
users
orders
order_items
products
```

MongoDB might use:

```javascript
{
  _id: ObjectId("..."),
  customerId: ObjectId("..."),
  items: [
    {
      productId: ObjectId("..."),
      name: "Keyboard",
      priceAtPurchase: 49.99,
      quantity: 1
    }
  ]
}
```

The correct design depends on application behavior.

**Competency:** Can explain and defend an embed/reference decision rather than simply applying a fixed rule.

---

# 9. Validation & Data Integrity

Flexible schemas still need data integrity.

## 9.1 Collection Validation

Understand JSON Schema-style validation.

Conceptual example:

```javascript
{
  $jsonSchema: {
    bsonType: "object",
    required: ["username", "email"],
    properties: {
      username: {
        bsonType: "string"
      },
      email: {
        bsonType: "string"
      }
    }
  }
}
```

Learn:
- [ ] Required fields.
- [ ] BSON type checks.
- [ ] Nested validation.
- [ ] Array validation.
- [ ] Numeric/string restrictions.

## 9.2 Unique Indexes

```javascript
db.users.createIndex(
  { email: 1 },
  { unique: true }
)
```

Use database enforcement for uniqueness.

## 9.3 Application Validation vs Database Validation

Understand:

```text
Frontend validation
        ↓
Backend validation
        ↓
MongoDB validation / indexes
```

Each protects a different boundary.

## 9.4 Missing Foreign Keys

Unlike PostgreSQL foreign keys, ordinary MongoDB references do not automatically guarantee that the referenced document exists.

Understand consequences:
- [ ] Orphaned references.
- [ ] Deletion coordination.
- [ ] Application-level integrity checks.
- [ ] Transactions when appropriate.
- [ ] Schema designs that reduce cross-document invariants.

## 9.5 Validation Evolution

Understand that tightening validation on an existing collection may expose legacy documents that no longer conform.

**Competency:** Can enforce important document invariants while understanding which relational integrity guarantees MongoDB does not provide automatically.

---

# 10. Aggregation Pipeline

Aggregation is a core MongoDB skill.

Think:

```text
Documents
    ↓
Stage 1
    ↓
Stage 2
    ↓
Stage 3
    ↓
Result
```

## 10.1 `$match`

```javascript
{
  $match: {
    active: true
  }
}
```

Filter documents.

## 10.2 `$project`

```javascript
{
  $project: {
    username: 1,
    email: 1
  }
}
```

Shape output.

## 10.3 `$set` / `$addFields`

Add or calculate fields.

## 10.4 `$unset`

Remove fields from pipeline output.

## 10.5 `$sort`

```javascript
{
  $sort: {
    createdAt: -1
  }
}
```

## 10.6 `$limit`

```javascript
{
  $limit: 20
}
```

## 10.7 `$skip`

Understand basic pagination use and limitations.

## 10.8 `$group`

```javascript
{
  $group: {
    _id: "$userId",
    postCount: {
      $sum: 1
    }
  }
}
```

Learn:
- [ ] Group keys.
- [ ] `$sum`.
- [ ] `$avg`.
- [ ] `$min`.
- [ ] `$max`.
- [ ] Other useful accumulators.

## 10.9 `$unwind`

Convert array elements into separate pipeline documents.

```text
Document with 3 tags
        ↓ $unwind
3 pipeline documents
```

## 10.10 `$lookup`

Combine related collections.

## 10.11 `$count`

Count pipeline results.

## 10.12 `$facet`

Run multiple aggregation sub-pipelines over the same input.

Useful for:
- [ ] Results + count.
- [ ] Multiple statistics.
- [ ] Faceted responses.

## 10.13 Expressions

Become comfortable with:
- [ ] Field references such as `"$price"`.
- [ ] Arithmetic expressions.
- [ ] Conditional expressions.
- [ ] String expressions.
- [ ] Date expressions.
- [ ] Array expressions.

## 10.14 Pipeline Ordering

Understand that stage order affects:
- [ ] Correctness.
- [ ] Amount of data processed.
- [ ] Index opportunities.
- [ ] Performance.

General instinct:

```text
Filter early when possible.
Return only what is needed.
Avoid expanding huge arrays unnecessarily.
```

**Competency:** Can build multi-stage aggregation pipelines for application reporting, transformations, and cross-collection data retrieval.

---

# 11. Indexes & Query Performance

## 11.1 Why Indexes Exist

Tradeoff:

```text
Faster targeted reads
        ↕
Storage + write cost + maintenance
```

## 11.2 `_id` Index

Understand:
- [ ] MongoDB automatically indexes `_id`.
- [ ] `_id` must be unique.

## 11.3 Single-Field Index

```javascript
db.users.createIndex({
  email: 1
})
```

## 11.4 Compound Index

```javascript
db.posts.createIndex({
  userId: 1,
  createdAt: -1
})
```

Understand:
- [ ] Field order matters.
- [ ] Query patterns should drive index design.
- [ ] Compound-index prefixes conceptually.

## 11.5 Multikey Indexes

Understand indexes over arrays.

- [ ] Recognize automatic multikey behavior.
- [ ] Understand array indexing implications at developer level.

## 11.6 Unique Indexes

```javascript
db.users.createIndex(
  { email: 1 },
  { unique: true }
)
```

## 11.7 Partial Indexes

Index only documents satisfying a filter.

Useful when only a subset is frequently queried.

## 11.8 Sparse Index Awareness

- [ ] Understand the concept.
- [ ] Know how missing fields affect indexing.
- [ ] Understand that partial indexes often provide more explicit control.

## 11.9 TTL Indexes

Useful for automatically expiring data such as:
- [ ] Temporary sessions.
- [ ] Verification records.
- [ ] Expiring logs/cache-like records.

## 11.10 Text Index Awareness

- [ ] Understand MongoDB text indexing at a basic level.
- [ ] Know that search requirements can become more sophisticated than simple text indexes.

## 11.11 Covered Queries

Understand conceptually:

```text
Query + returned fields
        ↓
All available from index
        ↓
Document fetch may be avoided
```

## 11.12 `explain()`

Learn to inspect query execution.

Example:

```javascript
db.users.find({
  email: "alice@example.com"
}).explain("executionStats")
```

Recognize concepts such as:
- [ ] Collection scan.
- [ ] Index scan.
- [ ] Documents examined.
- [ ] Keys examined.
- [ ] Documents returned.
- [ ] Execution time.

## 11.13 Performance Problems

Recognize:
- [ ] Missing indexes.
- [ ] Poor compound-index order.
- [ ] Huge result sets.
- [ ] Excessive `$lookup`.
- [ ] Large `$skip`.
- [ ] Unbounded arrays.
- [ ] Fetching unnecessary fields.
- [ ] Too many application queries.
- [ ] Poor schema/access-pattern alignment.
- [ ] Low-selectivity indexes.
- [ ] Excessive indexes slowing writes.

## 11.14 Cursor / Range Pagination

Instead of repeatedly using very large:

```javascript
skip(100000)
```

learn pagination based on an indexed ordering field such as `_id` or creation time.

**Competency:** Can diagnose common slow MongoDB queries with `explain()` and improve queries, indexes, or document design based on evidence.

---

# 12. Atomicity, Transactions & Concurrency

## 12.1 Single-Document Atomicity

Understand that operations modifying a single MongoDB document are atomic at the document level.

This is an important schema-design advantage.

Example:

```javascript
{
  _id: ...,
  inventory: 10
}
```

Atomic update:

```javascript
{
  $inc: {
    inventory: -1
  }
}
```

## 12.2 Atomic Operators

Use operations such as:
- [ ] `$inc`.
- [ ] `$set`.
- [ ] Conditional update filters.

Avoid unnecessary read-modify-write patterns.

## 12.3 Multi-Document Transactions

Understand:
- [ ] Sessions.
- [ ] Start transaction.
- [ ] Commit.
- [ ] Abort/rollback.
- [ ] Transactions can span multiple operations/documents under supported deployment configurations.

## 12.4 When Transactions Are Needed

Example:

```text
Create order
├── create order
├── update inventory
└── create payment record
```

If these must succeed/fail as a unit, a transaction may be appropriate.

## 12.5 When Better Modeling Can Reduce Transactions

If tightly related data can naturally live in one document:

```text
Multiple-document coordination
            ↓
Potential redesign
            ↓
Single-document atomic operation
```

Do not force embedding merely to avoid all transactions, but understand the tradeoff.

## 12.6 Concurrency Problems

Recognize:
- [ ] Lost-update style application bugs.
- [ ] Competing updates.
- [ ] Stale reads/application state.
- [ ] Write conflicts.
- [ ] Retry requirements.

## 12.7 Optimistic Concurrency Patterns

Understand conceptually:
- [ ] Include expected state/version in update filter.
- [ ] Check matched/modified counts.
- [ ] Retry or report conflicts appropriately.

**Competency:** Can reason about atomic document operations and identify when a true multi-document transaction is required.

---

# 13. MongoDB-Specific Capabilities

## 13.1 Bulk Operations

Understand:
- [ ] Bulk inserts.
- [ ] Bulk updates.
- [ ] Bulk deletes.
- [ ] `bulkWrite()` conceptually.

Useful when many operations can be submitted efficiently together.

## 13.2 Change Streams

Understand:

```text
Database change
      ↓
Change stream
      ↓
Application event handling
```

Potential uses:
- [ ] Notifications.
- [ ] Cache invalidation.
- [ ] Event-driven workflows.
- [ ] Real-time updates.

Developer-level knowledge is sufficient.

## 13.3 TTL Data

Use TTL indexes for data that should automatically expire.

## 13.4 Geospatial Data

Become familiar with:
- [ ] GeoJSON.
- [ ] Geospatial indexes.
- [ ] Near/location queries.

No GIS specialization required.

## 13.5 Search

Understand the distinction among:
- [ ] Exact filtering.
- [ ] Regex/pattern matching.
- [ ] MongoDB text search concepts.
- [ ] Atlas Search awareness.

Know that advanced search is a separate concern from ordinary database filtering.

## 13.6 Capped Collections Awareness

Know:
- [ ] What capped collections are conceptually.
- [ ] That they have specialized use cases.
- [ ] They are not the default choice for ordinary application collections.

**Competency:** Recognizes MongoDB-native capabilities and can choose them when they genuinely simplify application requirements.

---

# 14. Security & Access Control

## 14.1 Authentication vs Authorization

```text
Authentication → Who are you?
Authorization  → What may you do?
```

## 14.2 Users and Roles

Understand:
- [ ] Database users.
- [ ] Built-in roles.
- [ ] Custom roles conceptually.
- [ ] Application-specific credentials.

## 14.3 Least Privilege

- [ ] Do not use broad administrative credentials for normal application traffic.
- [ ] Grant only required access.
- [ ] Separate development and production credentials.

## 14.4 Credentials

- [ ] Do not hard-code database passwords.
- [ ] Keep credentials out of Git.
- [ ] Understand that connection URIs may contain secrets.
- [ ] Use environment/secret configuration appropriately.

## 14.5 Query / Operator Injection

Understand that NoSQL does **not** mean injection-safe.

Dangerous application behavior can occur when raw untrusted objects become query filters/operators.

Learn:
- [ ] Validate input types.
- [ ] Construct queries deliberately.
- [ ] Avoid blindly passing request bodies into MongoDB query objects.
- [ ] Use framework/driver safety features correctly.

## 14.6 TLS

Understand:
- [ ] Why encrypted network connections matter.
- [ ] Local and production requirements differ.
- [ ] Managed MongoDB deployments commonly require secure connections.

## 14.7 Network Access

Developer-level understanding:
- [ ] Restrict database exposure.
- [ ] Do not unnecessarily expose MongoDB publicly.
- [ ] Understand managed-service network access controls.

**Competency:** Can configure application access without exposing administrative credentials or trusting arbitrary client-supplied query structures.

---

# 15. Application Integration

## 15.1 MongoDB Driver

Understand:

```text
Backend application
        ↓
MongoDB driver
        ↓
MongoDB
```

The driver:
- [ ] Opens/manages connections.
- [ ] Serializes application values to BSON.
- [ ] Executes operations.
- [ ] Returns results/errors.
- [ ] Supports sessions and transactions.

## 15.2 Connection Strings

Understand the components of MongoDB URIs.

Conceptually:

```text
mongodb://user:password@host:port/database
```

and managed-service SRV forms such as:

```text
mongodb+srv://...
```

- [ ] Protect connection strings.
- [ ] Understand configuration differences across environments.

## 15.3 Connection Pooling

Understand:
- [ ] Drivers normally manage pools.
- [ ] Connections are reusable resources.
- [ ] Applications should not create a new client for every request.
- [ ] Pool exhaustion can cause application failures.
- [ ] Pool sizing should be deliberate rather than arbitrarily huge.

## 15.4 Mapping Application Objects

Understand:

```text
Application object
        ↕
Driver serialization
        ↕
BSON document
```

Be aware of type conversions involving:
- [ ] ObjectId.
- [ ] Date/time.
- [ ] Decimal values.
- [ ] Null/missing fields.
- [ ] Nested objects.

## 15.5 Error Handling

Recognize:
- [ ] Duplicate key errors.
- [ ] Validation errors.
- [ ] Connection errors.
- [ ] Authentication errors.
- [ ] Timeout errors.
- [ ] Transaction/write conflicts.

## 15.6 Full-Stack Request Flow

Trace:

```text
Frontend
   ↓ HTTP
Controller / Route
   ↓
Service / Business Logic
   ↓
Repository / Data Access
   ↓
MongoDB Driver / ODM
   ↓
MongoDB
```

Be able to explain where:
- [ ] Request validation occurs.
- [ ] Business logic occurs.
- [ ] Database query construction occurs.
- [ ] Data integrity is enforced.
- [ ] Database errors return to the application.

## 15.7 Avoid Database Leakage into API Design

Understand that frontend clients should generally not be allowed to send arbitrary MongoDB filters/operators directly to the database.

Backend APIs should define controlled application operations.

**Competency:** Can trace a frontend request all the way through backend code to the MongoDB operation and back.

---

# 16. ODM / Framework Integration

An ODM or framework abstraction should supplement—not replace—MongoDB knowledge.

## 16.1 ODM Concept

```text
Application Model
       ↕
 ODM / Data Framework
       ↕
MongoDB Driver
       ↕
    MongoDB
```

Understand:
- [ ] Model/document mapping.
- [ ] Schema declarations.
- [ ] Validation.
- [ ] Query abstractions.
- [ ] Middleware/hooks where applicable.

## 16.2 Mongoose Awareness

For Node/TypeScript ecosystems, understand concepts such as:
- [ ] Schema.
- [ ] Model.
- [ ] Document.
- [ ] Validation.
- [ ] References/population.
- [ ] Middleware.
- [ ] Query execution.

Do not confuse Mongoose behavior with MongoDB behavior.

## 16.3 Spring Data MongoDB Awareness

For Java/Spring applications, understand:
- [ ] Document/entity mapping.
- [ ] Repository abstractions.
- [ ] `MongoTemplate` conceptually.
- [ ] ObjectId mapping.
- [ ] Query methods.
- [ ] Transactions where supported/configured.

## 16.4 Generated Queries

- [ ] Inspect what the abstraction actually asks MongoDB to do.
- [ ] Recognize inefficient generated access patterns.
- [ ] Understand indexes still need to match real database queries.

## 16.5 ODM Validation vs Database Validation

Understand:

```text
ODM validation
      ≠
MongoDB collection validation
```

Both may have value.

## 16.6 Population vs `$lookup` vs Multiple Queries

Understand that relationship abstractions can result in:
- [ ] Additional database queries.
- [ ] Aggregation joins.
- [ ] Application-side combination.

Measure instead of assuming efficiency.

**Competency:** Can use an ODM/framework productively while still debugging and reasoning at the underlying MongoDB level.

---

# 17. Schema Evolution & Data Migrations

MongoDB's flexible schema does not eliminate migrations.

## 17.1 Schema Versions Over Time

Old document:

```javascript
{
  name: "Alice Smith"
}
```

New document:

```javascript
{
  firstName: "Alice",
  lastName: "Smith"
}
```

The application must handle the transition.

## 17.2 Migration Strategies

Understand:
- [ ] Eager/batch migration.
- [ ] Lazy migration on read/write.
- [ ] Dual-compatible application code.
- [ ] Background backfills.
- [ ] Schema version fields when useful.

## 17.3 Adding Fields

Often straightforward, but decide:
- [ ] Is the field optional?
- [ ] Does it have a default meaning?
- [ ] Must old documents be backfilled?
- [ ] Should validation require it?

## 17.4 Renaming Fields

Understand compatibility issues when:
- [ ] Old application instances use the old field.
- [ ] New application instances use the new field.
- [ ] Both versions temporarily coexist.

## 17.5 Changing Types

Example:

```text
"price": "19.99"
        ↓
"price": Decimal128(...)
```

Understand:
- [ ] Existing documents need conversion.
- [ ] Queries/indexes may behave differently across mixed types.
- [ ] Validation can prevent new bad data.

## 17.6 Index Changes

Schema evolution may require:
- [ ] Creating indexes.
- [ ] Removing obsolete indexes.
- [ ] Changing unique constraints.
- [ ] Monitoring build/performance impact.

## 17.7 Backward-Compatible Evolution

Prefer staged changes when necessary:

```text
1. Application supports old + new
2. Backfill/migrate data
3. Application writes new format
4. Verify
5. Remove old compatibility
```

**Competency:** Can evolve MongoDB document structures safely instead of assuming flexible schemas make migrations unnecessary.

---

# 18. Testing & Debugging

## 18.1 Query Testing

Test:
- [ ] Expected matches.
- [ ] No matches.
- [ ] Missing fields.
- [ ] `null`.
- [ ] Wrong BSON types.
- [ ] Empty arrays.
- [ ] Multiple array elements.
- [ ] Boundary values.

## 18.2 Validation Testing

Verify that invalid documents fail:
- [ ] Missing required fields.
- [ ] Incorrect types.
- [ ] Duplicate unique values.
- [ ] Invalid nested structures.

## 18.3 Aggregation Testing

For complex pipelines:
- [ ] Run one stage at a time.
- [ ] Inspect intermediate output.
- [ ] Add stages incrementally.
- [ ] Verify assumptions about arrays and missing values.

## 18.4 Index Testing

Use:

```javascript
.explain("executionStats")
```

Ask:
- [ ] Was an index used?
- [ ] How many keys were examined?
- [ ] How many documents were examined?
- [ ] How many documents were returned?
- [ ] Is the query doing much more work than necessary?

## 18.5 Application Debugging

Trace:

```text
Frontend
   ↓
HTTP Request
   ↓
Controller / Route
   ↓
Service
   ↓
Repository / ODM
   ↓
MongoDB operation
   ↓
MongoDB
```

Ask:
1. Did the request reach the backend?
2. Was the input parsed correctly?
3. Did validation pass?
4. What MongoDB filter was constructed?
5. Were BSON types correct?
6. What update operators were used?
7. Did the database match any documents?
8. Did validation or uniqueness fail?
9. Was a transaction committed?
10. Was the returned document mapped correctly?

## 18.6 Common Bugs

Recognize:
- [ ] String `_id` used where ObjectId is expected.
- [ ] Wrong nested-field path.
- [ ] Unexpected missing field.
- [ ] Incorrect array query.
- [ ] Accidentally replacing a document instead of updating fields.
- [ ] Missing index.
- [ ] Duplicate-key error.
- [ ] Validation failure.
- [ ] Blind request-object query construction.
- [ ] ODM behavior misunderstood as MongoDB behavior.

## 18.7 Integration Testing

- [ ] Test against a real MongoDB-compatible test environment where appropriate.
- [ ] Keep test data isolated.
- [ ] Seed predictable documents.
- [ ] Reset test state.
- [ ] Test indexes/validation when they are part of expected behavior.

**Competency:** Can determine whether a data bug originates in the frontend, backend logic, ODM, MongoDB query, BSON types, document design, validation, indexing, or transaction behavior.

---

# 19. MongoDB Atlas & Production Fundamentals

This branch covers what a full-stack developer should understand without becoming a MongoDB administrator.

## 19.1 MongoDB Atlas

Understand Atlas as MongoDB's managed cloud database platform.

Developer-level skills:
- [ ] Create/use a managed deployment.
- [ ] Create database credentials.
- [ ] Configure network access appropriately.
- [ ] Obtain a connection URI.
- [ ] Connect an application.
- [ ] Store the URI securely.
- [ ] Inspect basic metrics.
- [ ] Understand environment separation.

## 19.2 Local vs Managed Database

Understand:

```text
Local development
      ↓
localhost MongoDB

Production
      ↓
Managed / secured MongoDB deployment
```

Configuration should differ without requiring source-code rewrites.

## 19.3 Backups

Understand:
- [ ] Why backups matter.
- [ ] Managed backup concepts.
- [ ] `mongodump` / `mongorestore` awareness.
- [ ] A backup is useful only if restoration is possible.
- [ ] Production backup strategy is an operational concern that developers should understand.

## 19.4 Monitoring

Developers should recognize metrics involving:
- [ ] Query latency.
- [ ] Connections.
- [ ] CPU.
- [ ] Memory.
- [ ] Disk/storage.
- [ ] Operation counts.
- [ ] Slow queries.
- [ ] Errors.
- [ ] Replication health at an awareness level.

## 19.5 Replica Set Awareness

Understand conceptually:

```text
Primary
├── Secondary
└── Secondary
```

Know:
- [ ] Replication provides redundant copies.
- [ ] Primary handles normal writes.
- [ ] Elections/failover exist.
- [ ] Transactions/change streams may depend on deployment configuration.

No administration mastery required.

## 19.6 Sharding Awareness

Understand conceptually:

```text
Large dataset
     ↓
distributed across
multiple shards
```

Know:
- [ ] Sharding exists for horizontal distribution.
- [ ] Shard-key choice is important.
- [ ] Poor shard design can cause major problems.
- [ ] Ordinary full-stack developers do not need to master cluster architecture for this skill tree.

## 19.7 Production Connection Management

- [ ] Reuse MongoDB clients.
- [ ] Understand pooling.
- [ ] Configure reasonable timeouts.
- [ ] Handle transient errors.
- [ ] Understand retry behavior at a developer level.

## 19.8 Production Security

- [ ] Use authentication.
- [ ] Use least-privilege roles.
- [ ] Protect credentials.
- [ ] Use TLS.
- [ ] Restrict network access.
- [ ] Keep production databases inaccessible directly from frontend/browser clients.

**Competency:** Can deploy/configure a MongoDB-backed application responsibly and communicate effectively with infrastructure/database specialists when deeper operational issues arise.

---

# 20. Professional Full-Stack MongoDB Mastery

A developer completing this tree should be able to build the MongoDB data layer for a realistic application without following a step-by-step tutorial.

## 20.1 Design Challenge

Given:

> Build a project-management application where users create projects, invite members, create tasks, comment on tasks, apply labels, and maintain activity history.

Determine:
- [ ] Collections.
- [ ] Document boundaries.
- [ ] Embedded data.
- [ ] References.
- [ ] Bounded versus unbounded relationships.
- [ ] Validation rules.
- [ ] Unique requirements.
- [ ] Indexes.
- [ ] Migration strategy.

Possible collections might include:

```text
users
projects
tasks
activity
```

But the correct design depends on access patterns.

Do not assume every conceptual entity requires its own collection.

## 20.2 CRUD Challenge

Write from memory:
- [ ] `insertOne()`.
- [ ] `insertMany()`.
- [ ] `find()`.
- [ ] `findOne()`.
- [ ] Projection.
- [ ] Sorting.
- [ ] Pagination.
- [ ] `updateOne()`.
- [ ] `updateMany()`.
- [ ] Array updates.
- [ ] Upserts.
- [ ] `deleteOne()`.
- [ ] `deleteMany()`.

## 20.3 Query Challenge

Independently construct queries involving:
- [ ] Comparison operators.
- [ ] Logical operators.
- [ ] Nested fields.
- [ ] Arrays.
- [ ] Optional/missing fields.
- [ ] Multiple simultaneous conditions.

## 20.4 Aggregation Challenge

Build pipelines using:
- [ ] `$match`.
- [ ] `$project`.
- [ ] `$group`.
- [ ] `$sort`.
- [ ] `$unwind`.
- [ ] `$lookup`.
- [ ] `$set`.
- [ ] `$facet`.
- [ ] Expressions.

## 20.5 Modeling Challenge

Given a relationship, answer:

```text
Embed or reference?
```

and justify it using:
- [ ] Ownership.
- [ ] Cardinality.
- [ ] Boundedness.
- [ ] Read patterns.
- [ ] Write patterns.
- [ ] Independent lifecycle.
- [ ] Duplication cost.
- [ ] Atomicity requirements.
- [ ] Document growth.

## 20.6 Performance Challenge

Given a slow endpoint:
- [ ] Determine what MongoDB operations execute.
- [ ] Run `explain("executionStats")`.
- [ ] Identify collection scans.
- [ ] Inspect documents/keys examined.
- [ ] Evaluate index coverage/order.
- [ ] Check projection/result size.
- [ ] Check pagination strategy.
- [ ] Check aggregation stage ordering.
- [ ] Determine whether document design is causing the inefficiency.
- [ ] Measure the improvement.

## 20.7 Application Challenge

Build:

```text
Frontend
    ↓ HTTP
Backend API
    ↓
Service / Business Logic
    ↓
Repository / ODM / Driver
    ↓
MongoDB
```

Explain:
- [ ] Where validation occurs.
- [ ] How ObjectIds are converted.
- [ ] How queries are built.
- [ ] How database errors are handled.
- [ ] How connections are managed.
- [ ] Where transaction boundaries exist.

## 20.8 Security Challenge

- [ ] Protect connection credentials.
- [ ] Use least privilege.
- [ ] Prevent query/operator injection.
- [ ] Validate input.
- [ ] Configure secure production connectivity.
- [ ] Prevent browsers/frontends from directly accessing the database.

## 20.9 Schema Evolution Challenge

Safely:
- [ ] Add a new field.
- [ ] Support existing documents.
- [ ] Backfill when required.
- [ ] Change validation.
- [ ] Add an index.
- [ ] Deploy compatible application code.
- [ ] Remove obsolete schema assumptions.

## 20.10 Debugging Challenge

Given a broken feature:
- [ ] Inspect the frontend request.
- [ ] Trace backend logic.
- [ ] Inspect the MongoDB filter/update.
- [ ] Verify BSON types.
- [ ] Run the operation directly.
- [ ] Inspect validation.
- [ ] Inspect indexes if performance is involved.
- [ ] Inspect transaction behavior.
- [ ] Identify the actual failing layer.

---

# Mastery Milestones

## Level 0 — New to Document Databases

Can explain:
- Database.
- Collection.
- Document.
- Field.
- BSON.
- `_id`.

---

## Level 1 — MongoDB Beginner

Can independently use:

```text
insertOne
find
findOne
updateOne
deleteOne
```

Can:
- [ ] Query basic fields.
- [ ] Inspect data with `mongosh`.
- [ ] Work with ObjectIds.

---

## Level 2 — MongoDB Application Data Developer

Can:
- [ ] Query nested documents.
- [ ] Query arrays.
- [ ] Use update operators.
- [ ] Use projections.
- [ ] Model simple relationships.
- [ ] Decide between basic embedding and referencing.
- [ ] Define validation.
- [ ] Create basic indexes.

---

## Level 3 — MongoDB Developer

Can:
- [ ] Design document schemas around access patterns.
- [ ] Handle one-to-many and many-to-many relationships.
- [ ] Build aggregation pipelines.
- [ ] Create compound/multikey/partial indexes.
- [ ] Use `explain()`.
- [ ] Reason about document atomicity.
- [ ] Use transactions where appropriate.
- [ ] Diagnose common query performance problems.

---

## Level 4 — Full-Stack MongoDB Developer

Can:
- [ ] Integrate MongoDB with backend applications.
- [ ] Use drivers or ODMs correctly.
- [ ] Manage connection pooling.
- [ ] Handle ObjectId/BSON mapping.
- [ ] Prevent query/operator injection.
- [ ] Evolve document schemas.
- [ ] Debug across frontend → backend → ODM/driver → MongoDB.
- [ ] Use MongoDB Atlas at application-developer depth.

---

## Level 5 — Professional MongoDB Full-Stack Developer

Can independently:

1. Convert application requirements into an effective MongoDB document model.
2. Choose embedding versus referencing based on actual access patterns.
3. Select appropriate BSON data types.
4. Define validation and uniqueness requirements.
5. Write non-trivial MongoDB queries without depending entirely on an ODM.
6. Build aggregation pipelines.
7. Design useful indexes.
8. Use `explain()` to diagnose performance.
9. Integrate MongoDB with backend application code.
10. Use transactions and atomic operations appropriately.
11. Secure application database access.
12. Evolve schemas and migrate existing documents.
13. Test data behavior and integrity.
14. Debug failures across the full stack.
15. Understand Atlas and MongoDB production fundamentals.
16. Recognize when a problem has moved beyond application development into specialist MongoDB/database engineering.

---

# PostgreSQL vs MongoDB Mental Model

A full-stack developer who knows both should understand that neither database is simply a different syntax for the other.

## Relational Approach

```text
User
 │
 └──< Order
       │
       └──< OrderItem >── Product
```

Often modeled with normalized tables and foreign keys.

## MongoDB Approach

Possible order document:

```javascript
{
  _id: ObjectId("..."),
  customerId: ObjectId("..."),
  createdAt: ISODate("..."),
  items: [
    {
      productId: ObjectId("..."),
      productName: "Keyboard",
      unitPrice: 49.99,
      quantity: 2
    }
  ]
}
```

MongoDB asks the developer to think strongly about:

```text
How is the data read?
How is it updated?
What belongs together?
What grows without bound?
What needs atomic modification?
What needs an independent lifecycle?
```

The goal is not:

```text
Normalize everything
```

or:

```text
Embed everything
```

The goal is:

```text
Design documents around application behavior
while preserving correctness,
maintainability, and performance.
```

---

# Recommended Learning Dependency Graph

```text
Document Database Foundations
            │
            ▼
      BSON & Documents
            │
            ▼
        Basic CRUD ────────── mongosh
            │
            ▼
      Query Operators
            │
            ▼
       Update Operators
            │
            ▼
      Document Modeling
            │
            ▼
   Embedding vs Referencing
            │
       ┌────┴─────┐
       ▼          ▼
  Validation   Aggregation
       │          │
       └────┬─────┘
            ▼
          Indexes
            │
            ▼
      Query Performance
            │
            ▼
 Atomicity & Transactions
            │
       ┌────┴─────────┐
       ▼              ▼
   Security     MongoDB Features
       │              │
       └──────┬───────┘
              ▼
     Application Integration
              │
              ▼
        ODM / Frameworks
              │
              ▼
       Schema Evolution
              │
              ▼
      Testing & Debugging
              │
              ▼
     Atlas / Production
              │
              ▼
       Professional
    Full-Stack MongoDB
         Developer
```

---

# Full-Stack MongoDB Mastery Checklist

A professional full-stack developer should eventually be able to answer **yes** to these questions.

## Foundations

- [ ] Can I explain a document database?
- [ ] Can I explain BSON?
- [ ] Can I explain database → collection → document → field?
- [ ] Can I explain `_id` and ObjectId?
- [ ] Can I explain why flexible schema does not mean no schema?

## CRUD

- [ ] Can I insert documents?
- [ ] Can I query documents?
- [ ] Can I project fields?
- [ ] Can I sort and paginate?
- [ ] Can I update specific fields?
- [ ] Can I update arrays?
- [ ] Can I delete documents safely?
- [ ] Can I use upserts appropriately?

## Querying

- [ ] Can I use comparison operators?
- [ ] Can I combine logical conditions?
- [ ] Can I query nested fields?
- [ ] Can I query arrays?
- [ ] Can I distinguish missing fields from null values?

## Document Design

- [ ] Can I identify natural document boundaries?
- [ ] Can I decide when to embed?
- [ ] Can I decide when to reference?
- [ ] Can I recognize unbounded-array problems?
- [ ] Can I use controlled duplication intentionally?
- [ ] Can I design around application access patterns?

## Relationships

- [ ] Can I model one-to-one?
- [ ] Can I model one-to-few?
- [ ] Can I model one-to-many?
- [ ] Can I model many-to-many?
- [ ] Can I explain when `$lookup` is appropriate?
- [ ] Can I explain why MongoDB references do not behave like PostgreSQL foreign keys?

## Validation

- [ ] Can I define required fields?
- [ ] Can I validate BSON types?
- [ ] Can I enforce uniqueness with an index?
- [ ] Can I explain application validation versus database validation?

## Aggregation

- [ ] Can I use `$match`?
- [ ] Can I use `$project`?
- [ ] Can I use `$group`?
- [ ] Can I use `$sort`?
- [ ] Can I use `$unwind`?
- [ ] Can I use `$lookup`?
- [ ] Can I build multi-stage pipelines?
- [ ] Can I debug a pipeline stage-by-stage?

## Indexes & Performance

- [ ] Can I explain what an index does?
- [ ] Can I create single-field indexes?
- [ ] Can I design compound indexes?
- [ ] Can I explain multikey indexes?
- [ ] Can I use unique and partial indexes?
- [ ] Can I explain TTL indexes?
- [ ] Can I use `explain("executionStats")`?
- [ ] Can I recognize collection scans?
- [ ] Can I identify inefficient pagination?
- [ ] Can I recognize when schema design is causing poor performance?

## Transactions & Concurrency

- [ ] Can I explain single-document atomicity?
- [ ] Can I use atomic update operators?
- [ ] Can I identify when a multi-document transaction is necessary?
- [ ] Can I recognize basic concurrency problems?
- [ ] Can I explain how document design can sometimes reduce transaction requirements?

## Security

- [ ] Can I protect database credentials?
- [ ] Can I use least-privilege database access?
- [ ] Can I explain query/operator injection?
- [ ] Can I prevent raw client objects from becoming database queries?
- [ ] Can I explain why TLS and network restrictions matter?

## Application Integration

- [ ] Can I connect a backend application to MongoDB?
- [ ] Can I explain the role of the MongoDB driver?
- [ ] Can I explain connection pooling?
- [ ] Can I correctly map ObjectIds and dates?
- [ ] Can I handle database errors?
- [ ] Can I trace an HTTP request to its MongoDB operation?

## ODM / Frameworks

- [ ] Can I explain what an ODM does?
- [ ] Can I distinguish ODM behavior from MongoDB behavior?
- [ ] Can I inspect the queries generated by an abstraction?
- [ ] Can I recognize inefficient relationship loading?
- [ ] Can I work with Mongoose or Spring Data MongoDB without losing underlying MongoDB knowledge?

## Schema Evolution

- [ ] Can I add fields safely?
- [ ] Can I migrate old documents?
- [ ] Can I change field types safely?
- [ ] Can I support old and new document versions during deployment?
- [ ] Can I evolve validation and indexes?

## Debugging

- [ ] Can I test MongoDB operations independently of application code?
- [ ] Can I identify BSON type mismatches?
- [ ] Can I diagnose duplicate-key errors?
- [ ] Can I diagnose validation errors?
- [ ] Can I debug aggregation pipelines?
- [ ] Can I determine whether a bug belongs to the frontend, backend, ODM, driver, query, schema, validation, index, or transaction layer?

## Production

- [ ] Can I connect an application securely to MongoDB Atlas?
- [ ] Do I understand backups and restoration?
- [ ] Do I understand the metrics a developer should monitor?
- [ ] Do I understand replica sets conceptually?
- [ ] Do I understand sharding conceptually?
- [ ] Can I recognize when a problem requires MongoDB infrastructure/database-engineering expertise?

If these capabilities are consistently demonstrable, the MongoDB/document-database portion of a professional full-stack development skill set is in place.
