# RAG Fundamentals --- Full-Stack Developer Skill Tree

> **Goal:** Learn Retrieval-Augmented Generation (RAG) as a tool- and
> framework-independent application architecture: ingest knowledge,
> transform and index it, retrieve relevant evidence for a query,
> construct grounded model context, generate an answer, evaluate
> retrieval/generation quality, and operate the system securely.
>
> **Target level:** Full-stack/backend developer who can explain,
> design, build, test, debug, and evaluate a practical RAG system and is
> prepared to implement RAG with frameworks such as Spring AI.
>
> **Primary prerequisites:** AI/LLM Fundamentals, practical programming,
> HTTP/API fundamentals, and basic database/data-structure knowledge.
>
> **Strong supporting prerequisites:** SQL, testing, JSON, text
> processing, basic probability/vector intuition, and application
> security fundamentals.
>
> **Downstream connections:** Spring AI RAG, AI search/knowledge
> assistants, repository assistants, enterprise knowledge systems,
> vector databases, agentic AI, and AI-Assisted Software Development.
>
> **Scope boundary:** This tree does not require transformer
> mathematics, training embedding/foundation models, advanced
> information-retrieval research, or mastery of a particular vector
> database. It teaches the transferable RAG architecture and evaluation
> skills first.
>
> **Capstone:** Build a RAG assistant over a controlled document
> collection. Implement ingestion, metadata, chunking, embeddings, a
> searchable index/vector store, retrieval, context construction,
> grounded generation, source attribution, evaluation questions, failure
> analysis, document updates, and access/security considerations.

------------------------------------------------------------------------

# Skill Tree Overview

``` text
AI / LLM Fundamentals
          │
          ▼
     RAG Fundamentals
          │
   ┌──────┴───────┐
   ▼              ▼
Knowledge       Queries
Ingestion
   │              │
   ▼              ▼
Parse/Clean    Query Processing
   │              │
   ▼              │
Chunk             │
   │              │
   ▼              ▼
Metadata      Embedding/Search
   │              │
   ▼              ▼
Embeddings → Index / Vector Store
                  │
                  ▼
               Retrieve
                  │
                  ▼
                Rerank
                  │
                  ▼
          Context Construction
                  │
                  ▼
                 LLM
                  │
                  ▼
          Grounded Response
                  │
                  ▼
        Evaluate / Debug / Improve
```

# End-to-End Architecture

``` text
OFFLINE / INGESTION PATH

Documents
   │
   ▼
Load / Parse
   │
   ▼
Clean / Normalize
   │
   ▼
Chunk
   │
   ▼
Attach Metadata
   │
   ▼
Create Embeddings
   │
   ▼
Index / Vector Store


ONLINE / QUERY PATH

User Question
     │
     ▼
Query Processing
     │
     ▼
Search / Retrieve
     │
     ▼
Relevant Chunks
     │
     ▼
Optional Reranking
     │
     ▼
Build Prompt Context
     │
     ▼
LLM Generation
     │
     ▼
Answer + Sources
```

------------------------------------------------------------------------

# Tier 0 --- What RAG Is

## 1. Definition

**RAG = Retrieval-Augmented Generation.**

It combines:

``` text
Retrieval
   +
Generation
```

The model receives relevant external information at request time.

## 2. Core Mental Model

Without retrieval:

``` text
Question
   │
   ▼
  LLM
   │
   ▼
Answer from available model context/knowledge
```

With RAG:

``` text
Question
   │
   ▼
Search Knowledge Source
   │
   ▼
Relevant Evidence
   │
   └────┐
        ▼
Question + Evidence
        │
        ▼
       LLM
        │
        ▼
Grounded Answer
```

## 3. Why RAG Exists

Models may not know:

-   [ ] Private company data
-   [ ] Your repository
-   [ ] Recently updated documents
-   [ ] Internal policies
-   [ ] Large document collections
-   [ ] Domain-specific facts

RAG provides selected information without retraining the model.

**Checkpoint:** Explain RAG without using the words "vector database."

------------------------------------------------------------------------

# Tier 1 --- RAG vs Related Concepts

## 4. RAG vs Model Training

``` text
Training / fine-tuning
changes model behavior/parameters

RAG
supplies external information at inference time
```

RAG is not "teaching the model permanently."

## 5. RAG vs Long Prompt

Pasting a document manually into a prompt can augment context, but RAG
automates selection from a larger knowledge source.

## 6. Retrieval vs RAG

Retrieval alone:

``` text
query → search → results
```

RAG:

``` text
query → search → evidence → generation
```

## 7. Semantic Search vs RAG

Semantic search can be a retrieval component.

It is not the entire RAG system.

------------------------------------------------------------------------

# Tier 2 --- RAG System Responsibilities

## 8. Major Components

``` text
RAG
├── Knowledge source
├── Ingestion
├── Parsing
├── Chunking
├── Metadata
├── Embeddings/indexing
├── Retrieval
├── Ranking/reranking
├── Context construction
├── Generation
├── Attribution
├── Evaluation
└── Operations/security
```

## 9. Two Pipelines

Always distinguish:

``` text
Indexing / ingestion time
```

from:

``` text
Query / inference time
```

Many RAG bugs originate in the ingestion pipeline, not the LLM call.

------------------------------------------------------------------------

# Tier 3 --- Knowledge Sources

## 10. Source Types

Examples:

-   [ ] Markdown
-   [ ] HTML
-   [ ] PDFs
-   [ ] Source code
-   [ ] Database rows
-   [ ] Wiki pages
-   [ ] Tickets
-   [ ] Product documentation
-   [ ] Policies
-   [ ] Transcripts

## 11. Source Authority

Ask:

``` text
Is this source authoritative?
Is it current?
Who owns it?
Can it conflict with another source?
```

Retrieving bad information more efficiently still produces a bad system.

## 12. Provenance

Preserve where information came from.

Useful metadata:

``` text
document ID
title
URL/path
section
version
date
owner
access classification
```

------------------------------------------------------------------------

# Tier 4 --- Ingestion

## 13. Ingestion Pipeline

``` text
Source
  │
  ▼
Reader / Loader
  │
  ▼
Parsed Content
  │
  ▼
Transformation
  │
  ▼
Indexable Units
```

## 14. Incremental Ingestion

Real systems need to handle:

-   [ ] New documents
-   [ ] Changed documents
-   [ ] Deleted documents
-   [ ] Re-indexing
-   [ ] Failed ingestion
-   [ ] Duplicate content

## 15. Idempotency Awareness

Repeated ingestion should not accidentally create uncontrolled
duplicates.

------------------------------------------------------------------------

# Tier 5 --- Parsing and Cleaning

## 16. Parsing

Extract useful content and structure.

Examples:

``` text
heading
paragraph
table
code block
page
section
```

## 17. Cleaning

Potential noise:

-   [ ] Navigation
-   [ ] Headers/footers
-   [ ] Repeated legal boilerplate
-   [ ] Broken encoding
-   [ ] HTML chrome
-   [ ] Empty sections

Do not clean away meaningful structure.

## 18. Structure Preservation

Structure can improve retrieval:

``` text
Document
 └── Chapter
      └── Section
           └── Paragraph
```

------------------------------------------------------------------------

# Tier 6 --- Chunking

## 19. Why Chunk

Documents may be too large or too broad to retrieve as one unit.

``` text
Document
├── Chunk A
├── Chunk B
├── Chunk C
└── Chunk D
```

## 20. Chunk Size Tradeoff

Too small:

``` text
precise
but loses context
```

Too large:

``` text
more context
but less precise + more tokens
```

## 21. Overlap

Adjacent chunks may overlap to reduce information lost at boundaries.

## 22. Chunking Strategies

Understand:

-   [ ] Fixed token/character size
-   [ ] Paragraph-based
-   [ ] Heading/section-aware
-   [ ] Sentence-aware
-   [ ] Semantic chunking awareness
-   [ ] Code-aware chunking awareness

## 23. Chunking Is Domain-Dependent

A legal policy, Java class, FAQ, and transcript may need different
boundaries.

**Checkpoint:** Compare two chunking strategies for a Markdown technical
manual.

------------------------------------------------------------------------

# Tier 7 --- Metadata

## 24. Metadata Purpose

Metadata helps preserve context and filter retrieval.

Example:

``` text
text: "..."
source: handbook.md
section: Security
version: 4
department: Engineering
```

## 25. Metadata Filtering

A query may require:

``` text
only current version
only Engineering docs
only documents user can access
```

## 26. Metadata Is Part of Retrieval Design

Do not treat metadata as an afterthought.

------------------------------------------------------------------------

# Tier 8 --- Embeddings

## 27. Embedding

An embedding model converts content into a vector representation.

``` text
Text
 │
 ▼
Embedding Model
 │
 ▼
[0.12, -0.41, 0.88, ...]
```

## 28. Semantic Relationship

Vectors allow a system to compare approximate semantic similarity.

You do not need advanced linear algebra to begin using embeddings.

## 29. Embedding Model Consistency

The index and query must use compatible embedding representations.

Changing embedding models may require re-embedding stored content.

## 30. Embeddings Are Not the Original Text

Store enough original text/metadata to return useful evidence after
retrieval.

------------------------------------------------------------------------

# Tier 9 --- Vector Fundamentals

## 31. Vector

A vector can be thought of as an ordered list of numbers.

## 32. Dimensions

Embedding models produce vectors of a defined dimensionality.

Do not manually interpret individual dimensions as simple human-readable
features.

## 33. Similarity

Common conceptual measures include:

``` text
cosine similarity
dot product
distance measures
```

Exact choice depends on the model/store.

## 34. Nearest Neighbors

Retrieval often asks:

> Which stored vectors are nearest/most similar to the query vector?

------------------------------------------------------------------------

# Tier 10 --- Vector Stores and Indexes

## 35. Vector Store

A vector-capable store/index commonly associates:

``` text
embedding
+
text
+
metadata
+
identifier
```

## 36. Options

Vector search may be provided by:

-   [ ] A dedicated vector database
-   [ ] A relational database extension
-   [ ] A search engine
-   [ ] A cloud-managed search/vector service
-   [ ] An in-memory/local index for learning

Learn the abstraction before specializing.

## 37. Indexing

Index design affects:

``` text
search speed
memory/storage
accuracy
filtering
update behavior
```

------------------------------------------------------------------------

# Tier 11 --- Query Processing

## 38. User Query

The literal user question is not always the ideal retrieval query.

## 39. Query Transformation Awareness

Possible techniques:

-   [ ] Normalize wording
-   [ ] Add domain terms
-   [ ] Rewrite conversational references
-   [ ] Generate multiple searches
-   [ ] Extract filters
-   [ ] Decompose multi-part questions

## 40. Preserve Intent

Query rewriting should not silently change what the user asked.

------------------------------------------------------------------------

# Tier 12 --- Retrieval

## 41. Top-K

A retriever may return the top `K` matching chunks.

Tradeoff:

``` text
too few → miss evidence
too many → noise + token cost
```

## 42. Retrieval Score

Similarity scores help rank candidates but are not universal
truth/confidence values.

## 43. Thresholds

A system may reject weak matches rather than always supplying irrelevant
context.

## 44. Retrieval Failure

A correct generator cannot answer from evidence that was never
retrieved.

This is a central RAG debugging principle.

------------------------------------------------------------------------

# Tier 13 --- Keyword, Semantic, and Hybrid Search

## 45. Keyword Search

Strong for exact terms:

``` text
error code
class name
product ID
acronym
```

## 46. Semantic Search

Strong for meaning expressed with different wording.

## 47. Hybrid Search

Combine lexical/keyword and semantic signals.

``` text
keyword score
      +
semantic score
      │
      ▼
combined candidates
```

Hybrid retrieval is often useful for technical corpora.

------------------------------------------------------------------------

# Tier 14 --- Reranking

## 48. First-Stage Retrieval

Fast retrieval produces candidates.

## 49. Reranker

A reranker can apply a more expensive relevance model to a smaller
candidate set.

``` text
1000s/millions docs
       │
       ▼
fast retrieval
       │
       ▼
20 candidates
       │
       ▼
rerank
       │
       ▼
best 5
```

## 50. Tradeoff

Reranking can improve relevance but adds latency/cost/complexity.

------------------------------------------------------------------------

# Tier 15 --- Context Construction

## 51. Retrieved Context

Do not simply concatenate arbitrary chunks.

Consider:

-   [ ] Relevance order
-   [ ] Source labels
-   [ ] Duplicate removal
-   [ ] Token budget
-   [ ] Section continuity
-   [ ] Conflicting evidence
-   [ ] Access permissions

## 52. Prompt Structure

Conceptually:

``` text
System instructions
      │
Retrieved evidence
      │
User question
      │
Output requirements
```

## 53. Context Window Budget

Retrieved content competes with:

``` text
instructions
conversation history
tool results
output tokens
```

More retrieved text is not automatically better.

------------------------------------------------------------------------

# Tier 16 --- Grounded Generation

## 54. Grounding

Ask the model to base its answer on supplied evidence.

## 55. Abstention

A robust system should be able to say:

``` text
"The provided sources do not contain enough information."
```

rather than inventing an answer.

## 56. Separate Evidence From Instructions

Retrieved documents may contain text that looks like instructions.

Treat retrieved content as data unless explicitly trusted otherwise.

------------------------------------------------------------------------

# Tier 17 --- Citations and Attribution

## 57. Source Attribution

Useful RAG output can identify:

``` text
document
section/page
URL/path
chunk/source ID
```

## 58. Citation Correctness

A citation is useful only if the cited source actually supports the
claim.

## 59. Traceability

Store enough retrieval metadata to inspect:

``` text
question
retrieved chunks
scores
final context
answer
sources
```

This is essential for debugging.

------------------------------------------------------------------------

# Tier 18 --- Hallucinations

## 60. RAG Reduces but Does Not Eliminate Hallucination

Failure modes include:

``` text
wrong retrieval
misread evidence
unsupported synthesis
conflicting sources
missing evidence
```

## 61. RAG Can Introduce New Errors

Example:

``` text
correct model knowledge
+
retrieved outdated policy
=
grounded but wrong answer
```

Source quality matters.

------------------------------------------------------------------------

# Tier 19 --- Retrieval Evaluation

## 62. Retrieval Questions

Evaluate separately:

``` text
Did we retrieve the needed evidence?
Did we rank it highly enough?
Did irrelevant chunks dominate?
```

## 63. Recall and Precision Intuition

At a practical level:

``` text
Recall
"Did we find the relevant material?"

Precision
"How much of what we retrieved was actually relevant?"
```

## 64. Golden Dataset

Create representative test cases:

``` text
question
expected relevant source(s)
expected facts
```

Do not evaluate only with convenient demo questions.

------------------------------------------------------------------------

# Tier 20 --- Generation Evaluation

## 65. Answer Quality

Potential dimensions:

-   [ ] Correctness
-   [ ] Groundedness
-   [ ] Completeness
-   [ ] Relevance
-   [ ] Citation support
-   [ ] Appropriate abstention
-   [ ] Format compliance

## 66. Separate Retrieval and Generation Failures

``` text
Bad answer
   │
   ├── evidence missing? → retrieval problem
   ├── evidence present but ignored? → generation/prompt problem
   └── source itself wrong? → knowledge problem
```

This separation prevents random prompt tweaking.

------------------------------------------------------------------------

# Tier 21 --- RAG Debugging

## 67. Debugging Trace

For a failed question inspect:

``` text
1. original query
2. transformed query
3. filters
4. retrieved candidates
5. scores
6. reranked results
7. final context
8. final prompt
9. answer
```

## 68. Common Failure Causes

-   [ ] Bad parsing
-   [ ] Bad chunk boundaries
-   [ ] Missing metadata
-   [ ] Wrong filter
-   [ ] Poor embedding match
-   [ ] Top-K too low/high
-   [ ] Duplicate chunks
-   [ ] Stale index
-   [ ] Context truncation
-   [ ] Weak prompt
-   [ ] Unsupported user question

------------------------------------------------------------------------

# Tier 22 --- Updating Knowledge

## 69. Freshness

RAG knowledge changes.

Need processes for:

``` text
add
update
delete
re-index
```

## 70. Versioning

Preserve version/effective-date information when older documents remain
stored.

## 71. Deletion

Deleting a source should also remove or invalidate its indexed chunks.

------------------------------------------------------------------------

# Tier 23 --- Access Control

## 72. Retrieval Must Respect Authorization

Bad:

``` text
user cannot open HR document
but RAG retrieves it
and model summarizes it
```

The RAG system has leaked the document.

## 73. Filter Before Disclosure

Authorization should constrain retrieval/context construction, not rely
on asking the LLM to hide secrets afterward.

## 74. Multi-Tenant Systems

Tenant/user boundaries must be represented in retrieval permissions and
metadata design.

------------------------------------------------------------------------

# Tier 24 --- Prompt Injection and Untrusted Documents

## 75. Retrieved Text Is Potentially Untrusted

A document may contain:

``` text
"Ignore previous instructions..."
```

That text should not automatically control the application.

## 76. Instruction/Data Boundary

``` text
trusted application instructions
            │
            ▼
retrieved content = evidence/data
```

## 77. Tool-Using RAG

Risk increases if retrieved text can influence an agent that has
tools/actions.

Use permission boundaries and validation.

------------------------------------------------------------------------

# Tier 25 --- Privacy and Data Governance

## 78. Sensitive Knowledge

Consider:

-   [ ] PII
-   [ ] Credentials
-   [ ] Legal documents
-   [ ] Customer data
-   [ ] Proprietary code
-   [ ] Retention requirements

## 79. Embeddings Are Data

Do not assume embeddings are automatically harmless or exempt from data
policy.

## 80. Logging

Avoid logging sensitive prompts, retrieved passages, or model outputs
without a legitimate need and appropriate controls.

------------------------------------------------------------------------

# Tier 26 --- Performance

## 81. Latency Components

``` text
query processing
+ embedding
+ search
+ reranking
+ LLM generation
```

## 82. Cost Components

Potential costs:

``` text
ingestion
embedding
storage
search
reranking
LLM input tokens
LLM output tokens
```

## 83. Caching Awareness

Possible caching targets:

-   [ ] Embeddings
-   [ ] Parsed documents
-   [ ] Retrieval results where safe
-   [ ] Responses where semantics/security permit

Correctness and freshness come first.

------------------------------------------------------------------------

# Tier 27 --- RAG Architecture Choices

## 84. Simple RAG First

Start with:

``` text
documents
→ chunks
→ embeddings
→ vector search
→ top-K
→ prompt
→ answer
```

Establish an evaluation baseline before adding complexity.

## 85. Advanced Techniques Awareness

Later improvements may include:

-   [ ] Hybrid search
-   [ ] Reranking
-   [ ] Query expansion
-   [ ] Multi-query retrieval
-   [ ] Parent/child retrieval
-   [ ] Context compression
-   [ ] Graph-assisted retrieval
-   [ ] Agentic retrieval

Add them to solve measured problems.

------------------------------------------------------------------------

# Tier 28 --- RAG vs Fine-Tuning Decision

## 86. Prefer RAG When

The primary need is:

``` text
access to external/private/changing knowledge
```

## 87. Fine-Tuning Awareness

Fine-tuning may be useful for behavior/style/task adaptation, but it is
not a convenient replacement for a frequently changing knowledge base.

## 88. Combined Systems

A system can use both.

They solve different problems.

------------------------------------------------------------------------

# Tier 29 --- RAG for Source Code

## 89. Code Retrieval

Code corpora have structure:

``` text
repository
package/module
class
method
symbol
dependency
test
```

Chunking arbitrary characters may split meaningful code units.

## 90. Exact Search Matters

Code questions often benefit from hybrid retrieval because exact symbols
matter:

``` text
MessageService
findById
APP_SUBMISSION_PASSCODE
```

## 91. Repository RAG

RAG can support coding assistants by retrieving relevant code/docs, but
coding agents may also use direct file/symbol search rather than classic
vector RAG.

------------------------------------------------------------------------

# Tier 30 --- RAG and Databases

## 92. Vector Data Alongside Application Data

A system may store:

``` text
business records
document metadata
embeddings
```

in one or several systems.

## 93. Relational + Vector Awareness

Relational databases can sometimes provide vector search through
extensions/features.

A separate vector database is not mandatory for every RAG system.

## 94. Structured Data Boundary

If a question is best answered by exact SQL/tool querying, do not force
all information through document embeddings.

------------------------------------------------------------------------

# Tier 31 --- RAG and Tools

## 95. Retrieval vs Tool Query

Example:

``` text
"What is our vacation policy?"
→ document retrieval

"What is my remaining vacation balance?"
→ authorized system/tool query
```

## 96. Combined AI Application

``` text
User
 │
 ▼
AI Application
 ├── RAG for knowledge
 └── Tools for live/structured actions/data
```

Choose the correct information mechanism.

------------------------------------------------------------------------

# Tier 32 --- Framework Implementation Boundary

## 97. Frameworks

Frameworks can provide abstractions for:

``` text
document loading
chunking
embedding models
vector stores
retrievers
prompt/advisor pipelines
```

## 98. Do Not Let Framework Vocabulary Replace Architecture

Be able to draw the RAG pipeline without naming a framework.

## 99. Spring AI Connection

After this tree:

``` text
RAG Fundamentals
       │
       ▼
Spring AI
       │
       ▼
Spring AI RAG implementation
```

Spring AI should become an implementation of concepts you already
understand.

------------------------------------------------------------------------

# Tier 33 --- Testing

## 100. Unit Tests

Test deterministic components such as:

-   [ ] Parsing
-   [ ] Metadata extraction
-   [ ] Filters
-   [ ] Context formatting
-   [ ] Authorization
-   [ ] Deduplication

## 101. Integration Tests

Test:

``` text
embedding model ↔ store
retriever ↔ index
application ↔ model provider
```

where practical.

## 102. Evaluation Tests

Maintain a dataset of real questions and expected evidence/behavior.

AI output may require semantic/criteria-based evaluation rather than
exact string matching.

------------------------------------------------------------------------

# Tier 34 --- Observability

## 103. Useful Signals

Track:

-   [ ] Retrieval latency
-   [ ] Generation latency
-   [ ] Retrieved chunk count
-   [ ] Scores/ranks
-   [ ] Token usage
-   [ ] Errors
-   [ ] Empty retrievals
-   [ ] Abstention rate
-   [ ] User feedback
-   [ ] Evaluation quality over time

## 104. Privacy-Aware Tracing

Tracing RAG is valuable, but do not indiscriminately persist sensitive
content.

------------------------------------------------------------------------

# Tier 35 --- Professional RAG Workflow

## 105. Development Loop

``` text
Define use case
     │
     ▼
Choose authoritative corpus
     │
     ▼
Build simple ingestion
     │
     ▼
Build simple retrieval
     │
     ▼
Create evaluation set
     │
     ▼
Measure failures
     │
     ▼
Improve one component
     │
     ▼
Re-evaluate
```

## 106. Avoid Demo-Driven Design

A RAG demo that answers three hand-picked questions is not evidence of
production quality.

## 107. Mastery Check

You are ready to implement RAG professionally when you can:

-   [ ] Explain RAG independently of vector databases/frameworks.
-   [ ] Separate ingestion and query pipelines.
-   [ ] Choose sensible chunking and metadata.
-   [ ] Explain embeddings and similarity practically.
-   [ ] Compare keyword, semantic, and hybrid retrieval.
-   [ ] Inspect retrieved evidence for a failed answer.
-   [ ] Separate retrieval failures from generation failures.
-   [ ] Build an evaluation dataset.
-   [ ] Handle updates/deletions.
-   [ ] Preserve provenance.
-   [ ] Enforce access control before disclosure.
-   [ ] Recognize prompt-injection risks.
-   [ ] Measure latency/cost/quality.
-   [ ] Know when RAG is the wrong solution.

------------------------------------------------------------------------

# Capstone

Build a document-grounded assistant.

Minimum requirements:

1.  Use at least 20 meaningful documents or sections.
2.  Preserve source identity and metadata.
3.  Implement a repeatable ingestion process.
4.  Chunk documents deliberately and document the strategy.
5.  Generate/store embeddings or use an equivalent semantic index.
6.  Retrieve top candidates for a query.
7.  Add metadata filtering.
8.  Construct model context from retrieved evidence.
9.  Return source attribution.
10. Create at least 25 evaluation questions:
    -   direct fact,
    -   paraphrase,
    -   multi-section,
    -   exact keyword,
    -   no-answer,
    -   outdated/conflicting-source case.
11. Diagnose at least five failures by inspecting retrieval before
    changing prompts.
12. Demonstrate document update and deletion.
13. Document security/access assumptions.
14. Compare baseline semantic retrieval with at least one improvement
    such as hybrid search, reranking, or better chunking.
