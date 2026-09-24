# Phase 5 Plan — Milestone 2

**Status:** Round 4 of the discussion. Every question has been answered and is recorded as **Decided**. The plan is ready to approve. No code has been written for any of this.

Milestone 1 (Phases 0–4b) is done: node library and tree CRUD, a tree editor with immediate saving, ready/locked styling from per-node thresholds, and a one-command Docker setup. This document turns Milestone 2 into sub-phases, sets their order, and records the decisions needed before each one is built.

**Where each sub-phase stands:**

| Sub-phase | State |
|---|---|
| 5.0 Browser tests | **Done** (committed). |
| 5.1 Migration tool | **Done** (committed). |
| 5.2 Linked-tree readiness | **Done** (committed). |
| 5.3a View/edit modes, restore point, node pages | **Done** (committed). |
| 5.3b Undo/redo | **Built**, awaiting review. |
| 5.4 Right-angle edges | **Fully decided**, ready to approve. |
| 5.5 Readiness gradient | **Fully decided**, ready to approve. |

**What changed in round 4:**
- **Order confirmed** (O-Q1b).
- **5.0 is decided:** Playwright, Chromium only, a separate throwaway Docker setup, tests in `frontend/e2e/` run with `npm run e2e`.
- **5.3 is split into 5.3a and 5.3b** (V15), each with its own commit. It's decided in full: an unfinished edit session is resolved by asking the next time the tree opens (V9), nodes get tags (V10), hand-entered readiness is edited in view mode only (V11), and every edit-mode change can be undone, up to 50 steps (V12–V14).
- **5.4 uses fixed ports, bottom out and top in** (E1b, option A). Rearranged edges keep their adjustment when a node moves (E6), except that an edge resets to its default route when a move changes its number of segments, and adjusted segments are clamped between the two nodes (E6b). Overlapping edges get no special treatment (E7).
- **Milestone 3 list:** smarter auto-layout is on it for good (no step 5.6), and there's a new item for a **horizontal/vertical orientation toggle**.

---

## How Phase 5 proceeds

Each sub-phase goes through the same steps, one sub-phase at a time:

1. **Scope.** Claude drafts the sub-phase's section in this file, with its open questions. No code changes.
2. **Answer.** You answer the questions (or accept the recommendations). Claude updates the section so it reads as decided.
3. **Approve.** You approve the section. Only then does building start.
4. **Build.** Claude implements it, with:
   - backend and frontend unit tests,
   - **browser tests (from 5.0) for any new or changed on-screen behavior**, added to the suite,
   - a check that the Docker setup still builds and runs (`docker compose --profile app up --build`).
5. **Review and commit.** You review, Claude commits, then asks before starting the next sub-phase.

**Decided:**
- **One commit per sub-phase** (X-Q1). 5.3 counts as two, 5.3a and 5.3b.
- **Work directly on `main`** (X-Q2).
- **Browser tests become part of the project** as step 5.0, and every later step that changes the UI adds to them (X-Q4).

### Why the migration tool comes first (O-Q1, explained)

**What the database structure is:** the database stores data in tables, such as `nodes` and `trees`, and each table has columns, such as `title` and `readiness`. That layout is the **schema**. Several Milestone 2 features need to change it:
- 5.2 adds a "linked tree" column to `nodes`,
- 5.3 adds a table for restore points,
- 5.4 adds a column holding each edge's route.

**How it's changed today:** Hibernate (the database library in the backend) compares the Java classes with the database when the app starts, and **adds** any missing tables or columns. That's all it can do:
- it can't **rename** or **remove** a column, or move existing data into a new shape;
- it keeps **no record** of what changed or when;
- every database (yours, the Docker one, a future server) changes separately, whenever that copy of the app happens to start.

We've already hit this once. When the `positionx` column needed renaming, the only fix was to **wipe the database**. That was fine then, but you now have real data in it.

**What a migration tool does:** a migration tool (Flyway) keeps the schema's history as **numbered SQL scripts in git**, a bit like commits for the database structure:
- `V1` creates today's tables, `V2` renames the constraints, `V3` adds the linked-tree column, and so on;
- when the backend starts, Flyway runs any scripts that database hasn't had yet, in order, and records each one in the database;
- every copy of the database ends up **identical**, renames and data changes become possible, and there's a record of every change.

**Why first:** if 5.2–5.4 were built first, their schema changes would be made by Hibernate's auto-update, the approach we're replacing, and then have to be converted to migrations afterwards. Doing 5.1 first means every later schema change is written as a migration from the start. 5.0 (browser tests) changes no schema, so it can safely go before it.

**Order (decided, O-Q1b):**

| Step | Topic | Why this position |
|---|---|---|
| 5.0 | Browser tests | Your choice (X-Q4). They then check every later step. |
| 5.1 | Migration tool | Every later step except 5.5 changes the schema (explained above). |
| 5.2 | Node readiness from a linked tree | The plan's central idea. 5.3's view mode then shows linked trees from the start. |
| 5.3a | View and edit modes, restore point, node view pages | Changes how every tree and node page works, so it comes before more canvas features. |
| 5.3b | Undo/redo | Builds on 5.3a's edit mode. A separate commit to keep reviews small (5.3-V15). |
| 5.4 | Right-angle edges with draggable segments | Edge dragging is an edit-mode change, so it relies on 5.3a's Discard and 5.3b's undo. |
| 5.5 | Multi-step readiness gradient | Frontend only. It's easiest last, once derived readiness (5.2) exists. |

---

## 5.0 — Browser tests

*New, from X-Q4.*

**Goal:** a small, committed suite of tests that drive a **real browser**: open pages, click, type and drag the mouse like a person, then check the results. Every later step adds tests for its own on-screen behavior.

**Today:** the frontend tests run in a simulated browser (jsdom), which can't check real mouse dragging or real page layout. During Phase 4 I checked the editor with a Puppeteer script kept outside the project. It drove a real Chrome to test dragging, connecting, deleting, adding nodes and auto-layout.

**Decided:**
- **Tool: Playwright** (B1), with its own test runner, automatic waiting, and a trace (a step-by-step replay) recorded for failures.
- **Tests run against a separate copy of the Docker setup with its own empty database** (B2). It's started fresh for each run with sample data and removed afterwards, and never touches your real database. It uses the same images as `docker compose --profile app up`, so the tests check exactly what you'd run.
- **Tests live in `frontend/e2e/`** and run with **`npm run e2e`** from `frontend/` (B3). That one command starts the throwaway setup, runs the tests, and cleans up.
- **Chromium only** (B4), which keeps runs fast and the download small (~150 MB). Firefox and WebKit can be added later.

**Scope:**
- Add Playwright and a first set of tests covering what Milestone 1 already does:
  - the node library (create, edit, refused delete),
  - tree create and delete,
  - the Phase 4 editor (drag to move, thresholds, connect, cycle refused, delete an edge or node, add a node, auto-layout).
- Document `npm run e2e` in the README and CLAUDE.md.

**Built:** 13 tests in `frontend/e2e/` (node library, trees, and the tree editor), all passing. Each test sets up its own uniquely named data through the API, so the tests don't depend on each other and run in parallel. The throwaway stack is `frontend/e2e/docker-compose.yml`, a separate Compose project on port 8090 with an in-memory database.

**Depends on:** nothing.

---

## 5.1 — Migration tool

**Goal:** the database schema is created and changed by versioned migration files kept in git, not by Hibernate at startup. See "Why the migration tool comes first" above.

**Decided:**
- **Tool: Flyway** (Q1). Migrations are plain SQL files, `V1__...sql`, `V2__...sql`, in `backend/src/main/resources/db/migration/`.
- **`V1` comes from the live schema** (Q2): exported from Postgres and tidied, so it matches exactly what Hibernate built. Existing databases are marked as already at V1.
- **Constraint renames go in `V2`** (Q3), so every database ends up with readable names such as `tree_nodes_tree_id_fk`.
- **Hibernate switches to `ddl-auto=validate`** (Q4): the app refuses to start if the entities and the schema disagree.
- **Migrations run when the backend starts** (Q5), the same with `./mvnw spring-boot:run`, in Docker, and in Testcontainers tests.
- **Seed data stays in the Java seeder** behind the `dev` profile (Q6).
- **Your database is backed up first** with `pg_dump`, to a file outside the repo (Q7). If the switch goes wrong, it's restored from that file.

**Scope:** back up, add Flyway, write V1 and V2, switch to `validate`, make the tests build their schema from the migrations, and document how to add a migration. No other schema changes.

**Built:**
- **Backup:** `~/progressiontracker-backups/progressiontracker-before-flyway-20260924-142945.dump` (restore with `pg_restore`), plus a readable `.sql` copy.
- **V1** was checked against both your live schema and a fresh Hibernate-built one: all three are identical.
- **V2** renames the 9 generated names (`fk...`, `uk...`) to readable ones such as `tree_nodes_tree_id_fk` and `users_username_unique`. The entities declare the same names.
- **Your database** was baselined at V1 and migrated to V2 with all its data (13 nodes, 3 trees, 13 placements, 13 edges).
- **Tests:** `MigrationTest` runs the migrations on an empty database and on one Hibernate created before Flyway. All backend tests and browser tests now build their schema from the migrations.
- **Found along the way:** Hibernate had generated `nodes_readiness_source_type_check`, which allows only `'manual'`, even though the enum converter was meant to prevent that. V1 keeps it as it is. 5.2's migration widens it to allow `'linked_tree'`.

**Depends on:** nothing. **Enables:** 5.2, 5.3 and 5.4.

---

## 5.2 — Node readiness from a linked tree

**Goal:** a node can stand in for a whole tree. A node linked to another tree gets its readiness from that tree's nodes (a plain average, as decided in the main plan) instead of from a number you type in.

**Decided:**
- **One source per node:** a nullable `linked_tree_id` column on `nodes`, with source type `linked_tree` (Q1). Several sources per node, and the "stale" idea, are on the Milestone 3 list.
- **Nesting is allowed, with cycle prevention** (Q2). Tree A's node can link tree B, and one of B's nodes can link tree C. A link is refused if following links would lead back to the tree the node is in.
- **Readiness flows up through linked levels** (Q6). A linked tree's average uses its own linked nodes' computed values, not their hidden hand-entered ones.
  - Example: if Collections links a tree containing List (80%), Map (60%) and a node linked to a tree averaging 30%, Collections shows (80 + 60 + 30) / 3 = 57%.
- **The hand-entered value is kept but hidden while linked** (Q3). Unlinking brings it back.
- **A node linked to an empty tree has 0% readiness** (Q4).
- **A tree that a node links to can't be deleted** (Q5). The error lists the linking nodes.
- **Derived values are computed on the backend when they're read** (Q7), so they're never stale.
- **Simple calculator structure** (Q8): the code keeps a fixed list of readiness calculators, one per source type ("manual" and "linked tree"). Future sources each add one calculator.
- **Prerequisites don't feed into readiness** (Q10, option A). A node's readiness is your progress on that topic. Prerequisites only decide whether it looks ready or locked (or its gradient level, 5.5). Readiness comes from other nodes only when the node is linked to a whole tree.
- **Tree canvas:** a linked node shows a small "linked" marker. Its details show the linked tree's name with an "Open linked tree" link (Q9).
- **Rounding:** derived readiness is rounded to a whole percent, matching hand-entered values.

**Built:**
- **Migration `V3`** adds `nodes.linked_tree_id` (foreign key and index), widens the source-type check to `'manual'` and `'linked_tree'`, and adds `nodes_linked_tree_matches_source`, so a link and its source type always agree.
- **Backend:** a `readiness` package with one calculator per source type and a per-request `ReadinessContext` that computes values on read, caching each tree's average. `TreeLinks` refuses loops both when a node is linked and when a linked node is placed in a tree. Node and tree-node responses carry the effective `readiness` and `linkedTree`, and node responses also carry `manualReadiness`.
- **Frontend:** the node form has a readiness source choice ("Enter it myself" or "From a linked tree", with a tree picker). The node library shows "from <tree>" beside derived values. The tree canvas marks linked nodes, and their details name the tree with an "Open linked tree" link.
- **Sample data:** Collections Framework is linked to a new "Collections in Depth" tree (70% derived, replacing its hand-entered 65%).
- **Not in the plan, but needed:** placing a linked node in a tree can close a loop just as linking can, so both are checked. The node edit form stays the place to set the link for now; 5.3 moves hand-entered readiness to the view page.

**Depends on:** 5.1 (a new column and constraint, added through a migration).

---

## 5.3 — View and edit modes, restore point, and undo/redo (5.3a and 5.3b)

*Replaces "tree revision history", which moves to the Milestone 3 list.*

**Goal:** trees and nodes open in a **view mode** for looking and exploring. An **Edit** button switches to **edit mode**, where changes are made. Entering edit mode on a tree sets a **restore point**. Within edit mode, **undo/redo** steps back and forth through changes, and on the way out you either keep everything ("Done") or throw it all away ("Discard changes").

**Today:** the tree page is always an editor, and every change is saved immediately with no way back. Node pages are a list plus an edit form.

### Decided

**Viewing (trees and nodes):**
- **Trees always open in view mode** (V8).
- **In view mode you can look at the tree and explore its nodes.** Clicking a node shows its details: its links (clickable), what it needs and unlocks, and its linked tree (5.2).
- **The only change allowed in view mode is updating a hand-entered readiness value.** Moving nodes (V5) and rearranging edges (V6) are **edit mode only**. Linked nodes' derived readiness can't be edited anywhere.
- **Viewing a node never changes it:** its fields are shown read-only, and following its links doesn't edit anything (V7).
- **An "Edit" button** switches a tree or node into edit mode.

**Editing a tree:**
- **The restore point is kept on the server** (V1). Clicking "Edit" saves a snapshot of the tree to the database. Changes are still saved as they happen (as in Phase 4), so an error such as a cycle appears straight away.
- **Leaving edit mode:**
  - **"Done"** keeps the changes and deletes the restore point.
  - **"Discard changes"** (with a confirmation) puts the restore point back.
- **Leaving the page without clicking "Done" means the changes aren't kept** (V2). Trying to leave shows a warning that the changes won't be saved, with two choices: **leave without saving**, which puts the restore point back, or **stay and keep editing**.
- **No tree is ever left in edit mode** (V8).
- **"Discard changes" (and leaving without saving) puts back** (V3):
  - the tree's contents: placed and removed nodes, positions, thresholds, edges, and edge routes (5.4);
  - the tree's details (title, description, category, tags), if they were changed during the session;
  - any library nodes created during the session, which are deleted unless they've been placed in another tree since.
- **Undo/redo is included in edit mode** (V4). It's built separately as 5.3b; the details are below.

- **An edit session that didn't finish is resolved on the next visit** (V9). If the tab closed, the browser crashed or the computer restarted during edit mode, the app can't reliably restore the tree at that moment. The next time the tree is opened, it asks: *"This tree has unsaved changes from an edit session that didn't finish. Keep them or discard them?"* "Keep" acts like "Done" and "Discard" acts like "Discard changes". Nothing is lost by accident, and no tree stays in edit mode.

**Editing a node** (V7):
- A node's edit mode covers its **title, description, links, and tags**.
- **Nodes get tags** (V10), entered as comma-separated text like tree tags and shown on the node view page. They're added through a small migration.
- **Hand-entered readiness is edited in view mode only** (V11), not in the edit form, so it's quick to update while studying.
- It's the existing node form, reached with an "Edit" button on a new node view page. The form saves only when you click Save, and Cancel leaves the node unchanged.

**Undo/redo** (5.3b):
- **Every change made in the tree editor can be undone** (V12):
  - moving a node,
  - changing thresholds,
  - adding a node (existing or newly created),
  - removing a node from the tree (undo brings its edges back too),
  - adding or removing an edge,
  - auto-layout,
  - rearranging an edge's route (5.4).

  Changes made outside the tree editor (on the tree details form or a node's page) aren't in the undo list, but Discard still covers the tree details (V3).
- **Behavior** (V13):
  - up to **50 steps**;
  - Undo and Redo buttons, plus **Ctrl+Z** for undo and **Ctrl+Y** or **Ctrl+Shift+Z** for redo;
  - making a new change after undoing clears the redo steps;
  - the history ends when edit mode ends ("Done", "Discard" or leaving).
- **Undoing "create a new node and place it"** (V14) removes it from the tree and deletes the new library node, unless it's been placed in another tree. This matches Discard (V3), and redo recreates it.
- Each undo or redo is saved to the server straight away, like any other edit.

### Split into two commits (V15)

**5.3a: view and edit modes, restore point, node pages**
- **Tree page:** view mode by default, and "Edit" enters edit mode with the Phase 4 toolbar plus Done and Discard buttons.
- **Server side:** a restore-point table (added through a migration), with endpoints to start an edit session, finish it ("Done") and restore it ("Discard").
- **Leaving the page:** the app's own warning for links inside the app, the browser's "Leave site?" box for closing the tab, and the keep-or-discard question for an unfinished session (V9).
- **Node pages:** a new node view page (read-only fields, clickable links, readiness editable inline if hand-entered, tags, the trees using it, its linked tree). The existing form becomes the node's edit mode, with tags added, and rows in the node library open the view page.
- **Node tags:** a migration adding them, and the API and form changes to go with it.

**5.3a built:**
- **Migration `V4`** adds `node_tags` and `tree_edit_sessions` (one per tree, deleted with the tree). The restore point is a JSON snapshot of the tree's details, placements, thresholds and edges.
- **API:** `POST /trees/{id}/edit-session` (Edit), `DELETE` (Done), `POST .../discard` (Discard changes); `PUT /nodes/{id}/readiness` for view-mode readiness; `GET /nodes/{id}/trees`; tags on nodes; `editSessionStartedAt` on trees.
- **Tree page:** view mode by default (details, links, inline readiness, "Open node page", "Open linked tree"). Edit mode has the Phase 4 toolbar with Done, Discard changes, Edit details and Delete tree. Leaving asks first and discards, closing the tab gets the browser's warning, and an unfinished session shows a Keep/Discard banner. The tree list marks trees with an unfinished edit.
- **Node pages:** a new view page (`/nodes/:id`); library rows open it. The form gains tags and loses the readiness number, which is now set on the view page.
- **Choices made while building:**
  - "Delete tree" and "Edit details" moved into edit mode, since view mode allows no other changes.
  - Opening the tree details form from edit mode keeps the session: the form returns straight into edit mode.
  - "Discard" deletes a node created during the session only if it was placed in this tree and is now in no tree. A node created and removed again within the session stays in the library, as removing a node always has.
  - The backend doesn't refuse edits outside an edit session; view mode is enforced by the UI.

**5.3b: undo/redo**
- Undo and Redo buttons and keyboard shortcuts in edit mode, covering the changes listed above.

**5.3b built:**
- **Frontend only:** no backend or schema changes. Each edit records a command once its save succeeds; undo and redo call the same API endpoints and then reload the tree.
- **Covers** moves, thresholds, placing existing nodes, creating nodes, removing nodes (with their arrows), adding and removing arrows, and auto-layout. Edge routes join in 5.4.
- **Behavior as decided:** 50 steps, Ctrl+Z / Ctrl+Y / Ctrl+Shift+Z (not while typing in a field), a new change clears redo, and the history ends with Done, Discard or leaving. Button tooltips name the step, e.g. "Undo move "OOP"".
- **Choices made while building:**
  - If an undo or redo can't be saved (for example because the tree was changed elsewhere), the error is shown and the history is cleared, so it never drifts from what's saved.
  - A key pressed while an undo or redo is still saving is ignored.

**Depends on:** 5.1 (the restore-point table and node tags, added through migrations), 5.2 (view mode shows linked trees), and 5.0 (browser tests for the new modes, warnings and undo/redo). 5.3b depends on 5.3a.

---

## 5.4 — Right-angle edges with draggable segments

**Goal:** prerequisite arrows are drawn with only 90° turns, like a circuit diagram or org chart, instead of straight diagonal lines. In **edit mode**, the straight pieces (segments) of an edge can be dragged to tidy the route.

**Today:** each edge is one straight line from box edge to box edge, with an arrowhead. Edges have no stored shape.

### Decided

- **Fixed ports: out at the bottom, in at the top** (E1, E1b, option A). Edges leave from a connection point at the bottom-middle of the prerequisite and arrive at a connection point at the top-middle of the dependent. All edges into a node share its top-middle point (E5).
  - Dependent below its prerequisite: **3 segments** (down, across, down).
  - Dependent level with or above it: **5 segments** (down, out to the side, up, across, down into the top). This only happens after moving nodes by hand, never after auto-layout.
  - The other options considered were *nearest side* (edges enter from any side, which makes direction harder to read) and *keeping edges out of reverse* (which restricts where nodes can be placed).
- **Dragging moves existing segments only.** You can't add or remove bends by hand. The number of segments changes automatically as nodes move, for example from 3 to 5 when a dependent moves above its prerequisite (E2).
- **Rearranging is edit mode only**, so it's covered by Discard and undo (5.3-V6, V12).
- **Moving a node keeps your adjustments where they still fit** (E6). If you drag the middle segment of the OOP → Generics edge 40 pixels lower and then move Generics to the right, the middle segment stays 40 pixels lower and only the ends stretch. Two exceptions (E6b):
  - **If a move changes the edge's number of segments** (3 to 5 or back), that edge resets to its default route. Undo restores both the move and the old shape.
  - **If a move would push an adjusted segment past the edge's ends**, the segment is clamped so it stays between the two nodes.
- **Removing a node from a tree removes the edges connected to it** (E3). The app already does this, and 5.3b's undo brings them back together.
- **Reset to auto-layout also resets every edge to its default route** (E3b).
- **Overlapping edges get nothing extra** (E7). When edges run along the same line (for example two edges leaving OOP's bottom point), you can drag segments apart by hand. Smarter auto-layout that reduces overlaps is on the Milestone 3 list.
- **Edges don't automatically avoid other node boxes in Milestone 2** (E4). You can drag a segment out of the way, and automatic avoidance is on the Milestone 3 list.
- **More than one connection point per node** is on the Milestone 3 list (E5).

**Depends on:** 5.1 (the edge-route column, added through a migration), 5.3 (edit mode, Discard and undo), and 5.0 (browser tests for dragging segments).

---

## 5.5 — Multi-step readiness gradient

**Decided: included in Milestone 2** (Q1).

**What it is:** today each node box has two looks: green and solid when **ready**, grey and dashed when **locked**. The gradient replaces them with **4 levels** that show how close a locked node is to becoming ready.

Example, with the default thresholds (average at least 80%, each at least 70%):

| Prerequisites' readiness | Today | With the gradient |
|---|---|---|
| 5% and 10% | locked | **Not started**: grey, dashed |
| 40% and 50% | locked | **Early**: light, dotted |
| 70% and 75% | locked | **Close**: pale green, dashed |
| 90% and 85% | ready | **Ready**: green, solid (unchanged) |

**Decided:**
- **4 levels** (Q2).
- **Progress is measured by the lower of two ratios** (Q3): the prerequisites' average divided by the aggregate threshold, and the weakest prerequisite divided by the individual threshold. Fully ready is unchanged, so the top level means exactly what "ready" means today.
- **The gradient replaces the two-state look** (Q4). There's no toggle.
- **Each level differs in more than color** (border style and a text label), so it stays readable for color-blind users. A legend explains the levels.
- **Linked nodes' derived readiness (5.2) counts like any other readiness.** A prerequisite's readiness is its effective value.

**Depends on:** nothing technically (frontend only). It comes last so it can use 5.2's derived readiness, and 5.0's browser tests check the levels on screen.

---

## Milestone 3 — ideas to track

These aren't scheduled. They're here so they don't get lost. Each will be scoped properly if and when Milestone 3 starts.

- **Revision history.** Git-style saved versions of a tree, with an explicit commit action, a history list, and restoring or comparing revisions. It's replaced for now by 5.3's restore point and undo/redo. *An idea to revisit, not necessarily to build.*
- **Several readiness sources per node.** For example, a linked tree plus flashcards, combined by a rule such as an average or a weighting. 5.2 builds one source per node.
- **"Stale" readiness.** Nodes that were completed and marked ready but haven't been reviewed, tested or studied for a long time should show as going stale, and perhaps lose readiness over time until they're refreshed. This needs a record of when each node was last practiced (see the next item).
- **Practice-activity tracking.** A `practice_events`-style table recording study and review sessions per node, from the main plan's Future Integrations section. It's the foundation for "stale" readiness and for the integrations below.
- **Flashcard (Anki) and coding-practice integrations**, as further readiness sources (main plan, Future Integrations).
- **More connection points per node.** Spread incoming and outgoing edges along a node's edges instead of one shared point at the top and bottom (5.4-E5).
- **Edges that route around node boxes automatically** (5.4-E4).
- **Smarter auto-layout** that reduces edge crossings and overlaps (5.4-E7). The current auto-layout only orders each row by where its prerequisites are.
- **Horizontal/vertical orientation toggle** (5.4-E1b). A button in view mode rotates the tree layout by 90°, switching between top-to-bottom and left-to-right. The connection points move with it: out on the right and in on the left instead of bottom and top. 5.4 only needs to avoid ruling this out.
- **Logins and multiple users.** The schema is already multi-user ready, but v1 acts as one default user.
- **Deployment and CI.** The main plan's longer-term Hosting goal: automated builds and tests (including 5.0's browser tests), and deploying the Docker images from 4b to a server or cloud host. It relies on 5.1's migrations.
- **Logging review.** Decide what's worth logging and why, as the main plan's Logging section said to do once the backend was running.
