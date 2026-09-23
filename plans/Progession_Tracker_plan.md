# New App Plan — Skill Tree Builder (v6)

Structured rewrite of `new_app_plan_questions_v5.md`, folding in your round-5 answers, all three of which confirmed or resolved the calls v5 flagged as assumptions. v1–v5 are left untouched as a record of how the plan got here.

## Overview

A new system for creating and editing skill trees, in the same spirit as the existing `SkillTreeOSS` app in this repo, but built from scratch as its own project — with version control, test-driven development, logging, and structured planning as first-class concerns from day one. `SkillTreeOSS` is reference content and a source of tech-stack topics, not code this new project builds on. **v1 is an explicitly single-user-experience proof of concept — the schema is built to support multiple users, but no cross-user features (browsing, sharing, following) are implemented yet.**

## Decided: Users, Trees & Access

- Single-user *experience* for the proof of concept, but the **database schema is built multi-user-ready from day one** (e.g. trees and nodes are owned by a `user_id` even though there's only one user logging in right now).
- Cross-user features — browsing other users' trees, "selecting" one to add to your own set, following vs. copying — are **explicitly deferred**. Not designed in detail yet; not blocking v1.
- **Nodes live independently of trees**, in a per-user library. A tree is a particular arrangement (positions + prerequisite edges) of nodes drawn from that library; the same node can be reused across multiple trees. Position and prerequisite edges belong to the *tree-node association*, not the node itself.

## Decided: Node "Readiness" (formerly "Completion") & Progression

- Readiness is a **manually entered integer, 0–100**, per node.
- A node's readiness value can instead be **sourced from an external reference** rather than typed in by hand. When that happens, the UI should visibly indicate the value is derived, not manually entered.
- **The first supported external source is another tree in this same application** — a node can link to an entire other tree, and that node's readiness becomes an aggregate of that linked tree's nodes. Concretely:

  > `[Node A]` "contains a link to" `[Tree A]` = `{[Node B] → [Node C] → [Node D]}`
  > Node A's readiness = an aggregate of B, C, and D's readiness.

  This is a real structural feature, not just a UI nicety: it means a node can act as a **stand-in for an entire nested tree**, and trees can reference each other.
- **Aggregation formula (decided):** for v1, the aggregate is a **plain average** of the linked tree's node readiness values. The mechanism is built as a pluggable **"readiness function"** concept rather than a hardcoded average, so that:
  - Each *linkable source type* (linked tree today; flashcards/coding-practice later) can register how it computes a readiness value.
  - Swapping the linked-tree aggregate from "plain average" to something smarter later (weighted, threshold-based, etc.) doesn't require redesigning how nodes consume external readiness — only the function registered for that source type changes.
- **Readiness source hook:** `nodes` gets a `readiness_source_type` column (default/only value for Milestone 1: `'manual'`), even though nothing reads it yet — a cheap forward-compatibility hook, not a Milestone-2 feature pulled forward. No service logic, no UI, no readiness-function registry yet; that's still Milestone 2+ work.
- Readiness styling (how a node visually signals whether it's worth starting):
  - **Gradient step count:** **two states only for v1** (locked-looking vs. ready-looking). A more granular multi-band gradient (4–5 steps) is a **future option to explore**, not committed to, and not scheduled into any milestone yet.
  - Two conditions both need to hold before a dependent node flips from locked to ready:
    1. The **sum/aggregate of its prerequisites'** readiness crosses a threshold, **and**
    2. **Each individual prerequisite**, on its own, also crosses its own threshold.
  - **Thresholds (decided):** the two thresholds are **not** the same number, and are **configurable per node** (stored on the tree-node association, so the same library node can have different thresholds in different trees) rather than one global value or one pair per prerequisite edge. **Defaults:** a tree's overall readiness defaults to needing an **80% average across its nodes**, with **no individual node below 70%**.
- Cycle prevention (no cycles in the prerequisite graph) is validated **both client-side and server-side**.

## Decided: Node & Tree Fields

**Node (library-level, shared across trees):**
- Unique identifier
- Title
- Description
- One or more external links
- Readiness value (manual integer 0–100, or derived — see above)
- Readiness source type (schema hook only for v1 — see above)
- **A node's own prerequisites and the nodes that depend on it should both be visible/displayed** wherever a node's detail is shown — not just "what this needs," but also "what this unlocks."

**Tree-node association (per tree):**
- Position (x/y)
- Per-node readiness thresholds: an aggregate threshold and an individual threshold that this node's prerequisites must meet (see above)
- Prerequisite edges (to other tree nodes in the same tree)

**Tree metadata:**
- Category, Tags, Description
- **Versioning = revision history, git-commit-style**, confirmed wanted — but **explicitly deferred past v1** (see Milestone 1 scope below). When it is built:
  - A new revision is created only on an **explicit user-initiated "commit"** action — not automatically on every save.
  - While editing (pre-commit), an in-session **"go back"/"go forward"** (undo/redo) is wanted, separate from the commit history itself.
  - None of this — commits or undo/redo — is required for the first build pass.

## Decided: Persistence

- **Spring Data JPA + Hibernate** for data access, end-to-end, from Milestone 1 — **confirmed this round** (was round-5 Q1). No hand-rolled JDBC layer at any point; the JDBC-first plan from v3/v4 is fully superseded.
- **Repositories are Spring Data JPA interfaces** (e.g. `TreeRepository extends JpaRepository<Tree, UUID>`), one per entity — Spring generates the implementation.
- **Schema management — resolved this round (was round-5 Q2): Flyway is dropped.** Hibernate manages schema directly via `ddl-auto=update` for local/v1 development. No separate migration files to hand-write or keep in sync.
  - **Worth flagging, not blocking:** `ddl-auto=update` is a v1/local-dev convenience — it can silently make lossy or unexpected schema changes and isn't considered safe for a real deployed environment. v4/v5 both noted a longer-term goal of actually deploying this app (via the CI/CD content in `SkillTreeOSS`'s skill trees). When that becomes real, this plan should revisit introducing a migration tool (Flyway or otherwise) rather than shipping `ddl-auto=update` to anything beyond your own machine. Not an open question — just context for future-you.

## Decided: Layout & UI Behavior

- Nodes created purely in the library (outside a tree context) need no position.
- Creating a node inside the tree editor: a **toolbar that changes what a mouse click on the canvas does** (rather than click always meaning "place a node"), specifically so future tools can be bolted onto the same toolbar later without redesigning the interaction model.
- **"Reset to auto-layout" is wanted**, gated behind a confirmation warning since it discards manual positioning.
- **UI polish is explicitly deferred.** This is a proof of concept — use plain Angular with no component library (Bootstrap was a placeholder, now dropped) and don't spend effort on visual design yet.

## Decided: Future Integrations (Design For, Don't Build Yet)

- Data model leaves room for practice-activity tracking per node (e.g. a `practice_events`/`skill_activity`-shaped table), but this is **not required for v1**.
- Target integrations, both still needing research before committing to an approach, and both intended to eventually register as **readiness function** source types (see above):
  - **Flashcards** — a locally-running Anki instance, likely via AnkiConnect, tracking per-deck progress.
  - **Coding practice** — a CodingBat-style tool.
- **Monolith confirmed**: one Spring Boot app, with the flashcard/coding-practice integration logic kept behind a clean internal module boundary so it could be extracted into a real separate service later if it outgrows the monolith. No microservices split now.

## Decided: Tech Stack

**Backend:** Spring Boot, Java, Maven, Spring Data JPA + Hibernate, JUnit, Mockito
**Database:** PostgreSQL
**Schema management:** Hibernate `ddl-auto=update` (no Flyway for v1 — see Persistence above)
**Frontend:** Angular, no component library for now (plain Angular features only)
**Testing (frontend):** Vitest (Angular's default runner since v21, running in jsdom). Chosen in Phase 3 over the originally planned Jasmine/Karma, since Karma is deprecated.
**Testing (integration):** Real local PostgreSQL via Docker/Testcontainers — not an in-memory substitute.

**API style — resolved this round (was round-5 Q3):** REST endpoints are prefixed **`/api/v1/...`** from day one (e.g. `GET /api/v1/trees/{id}/nodes`), rather than adding the prefix later.

**Hosting:** Local-only for now, with a longer-term goal of deploying it using the CI/CD and cloud-deployment content already being built out in the `SkillTreeOSS` skill trees.

**Location:** A new sibling repo/project directory next to `SkillTreeOSS`, inside `~/workspace`.

## Decided: Logging

Start with Spring Boot's default, **SLF4J + Logback** — no extra setup, nothing to configure yet. Concrete "what to log and why" suggestions wait until **after development actually starts**, once there's a running backend to log something meaningful about.

## Decided: Process & Testing Strategy

- Lives as its own repo, not inside `SkillTreeOSS`.
- Testing: unit tests for phase 1, then integration tests (against real local Postgres via Testcontainers) designed in as each subsequent layer/phase is built. End-to-end/UI testing timing is deferred until there's a frontend.

## Decided: Milestone 1 Scope

Milestone 1 stays **pure CRUD**:
- Trees, nodes, tree-node associations (positions, per-node thresholds, prerequisite edges).
- Manually entered readiness values only — no derived/linked-tree readiness yet.
- No revision history yet.
- Backend CRUD + unit tests, plus a minimal frontend pass (node-library CRUD, manual readiness entry, tree CRUD for metadata, read-only tree viewing — canvas editing is its own later phase).

Revision history and tree-linking/readiness-aggregation are real, wanted features, but are pushed to **Milestone 2+** once basic CRUD is proven out. The data model should still be *shaped* to accommodate them later, without actually building them now.

---

## Build Plan (Draft v3)

Rebuilt from v5's draft, reflecting: Flyway dropped in favor of Hibernate `ddl-auto=update`, and REST endpoints versioned under `/api/v1` from the start.

### Phase 0 — Project Setup
- Create the new sibling repo under `~/workspace`, initialize git.
- Spring Boot + Maven project skeleton (backend), no frontend yet.
- Local PostgreSQL running (Docker), plus Testcontainers wired into the test setup so integration tests can hit real Postgres from the start.
- Baseline README describing the project (separate from this plans doc) and a `.gitignore` appropriate for Java/Maven + Angular.

### Phase 1 — Database Design
- Finalize the Milestone-1 schema as JPA entities: `User`, `Node` (library-level, includes `readiness_source_type` defaulted to `'manual'`), `Tree`, `TreeNode` (association: tree_id, node_id, position x/y, per-node prerequisite thresholds), `Prerequisite` (edges between tree_nodes within a tree).
- Configure `spring.jpa.hibernate.ddl-auto=update` — Hibernate creates/updates the schema from the entity mappings directly; no migration files.
- Seed/test data for local development.

### Phase 2 — Backend (Milestone 1: CRUD)
- Entities + **Spring Data JPA repositories** (`UserRepository`, `NodeRepository`, `TreeRepository`, `TreeNodeRepository`, `PrerequisiteRepository`).
- Service layer: business rules on top of repositories — e.g. cycle-prevention validation on prerequisite edges, enforcing per-node readiness thresholds as data (not yet evaluated dynamically, since aggregation is Milestone 2+).
- Controller layer: REST endpoints under **`/api/v1/...`** for CRUD on nodes, trees, tree-node associations, and prerequisite edges.
- Unit tests (JUnit + Mockito) for service-layer logic; integration tests (Testcontainers) for the repository layer against real Postgres.

### Phase 3 — Frontend (Milestone 1, narrowed)
- Angular project skeleton, plain Angular (no component library).
- Node library CRUD: list/create/edit/delete nodes, manual readiness entry.
- **Read-only** tree view: render an existing tree's nodes at their stored positions with prerequisite edges drawn, two-state (locked/ready) styling based on readiness thresholds. No drag, no edge creation yet.
- Vitest tests for components/services as they're built.

### Phase 3b — Frontend Tree CRUD
Added after Phase 3: the backend already supported tree CRUD (Phase 2), but no frontend phase covered it, and Phase 4 assumes a tree already exists to edit.
- Create a tree: title, description, category and tags.
- Edit a tree's metadata (not its contents; placing nodes and edges is Phase 4).
- Delete a tree, behind a confirmation warning that it also removes the tree's node placements and prerequisite edges. Library nodes are kept.
- Reachable from the tree list and from the read-only tree view.
- Vitest tests for the new form and actions.

### Phase 4 — Frontend Tree Editing
- Toolbar-driven canvas interaction model (per the UI decision above) so click-to-place isn't hardcoded.
- Drag-to-position nodes on the canvas; draw/remove prerequisite edges; set per-node thresholds.
- "Reset to auto-layout," gated behind a confirmation warning.
- **Decided while building Phase 4:** every edit is saved to the backend immediately (no Save button; undo/redo stays with Milestone 2's revision history). The "Add node" tool either places an existing library node or creates a new library node (readiness 0) and places it in one step. Auto-layout positions are saved through one bulk backend endpoint, so either every node moves or none does.

### Phase 5 — Milestone 2 candidates (not started yet)
Deferred features that build on the Milestone 1 foundation, to be scoped in detail once Phases 1–4 are done:
- Tree revision history (explicit commit action + in-session undo/redo).
- Node-links-to-tree readiness aggregation, built as the pluggable "readiness function" mechanism (this is what `readiness_source_type` is reserved for).
- Multi-step readiness gradient, if you decide to revisit the two-state default.
- Revisit schema management (introduce a real migration tool) before any deployment past your own machine — see the note under Persistence above.

---

All round-4 and round-5 open questions are resolved as of this version. No new open questions from this pass — this plan is ready to build from unless something above reads wrong to you.
