# Phase 7 Plan — Learning activities (Milestone 3)

**Status:** Fully decided. 7.0 to 7.3 are complete; no code has been written for 7.4 onward.

**Steps, in build order:**

| Step | Topic | State |
|---|---|---|
| — | Architecture: modular monolith | **Fully decided.** |
| 7.0 | Rules and examples | **Complete.** Rules and examples final; main plan updated. |
| 7.1 | Flashcards and review records | **Complete.** |
| 7.2 | Typed node resources | **Complete.** |
| 7.3 | Readiness from several resources | **Complete.** |
| 7.4 | External materials with manual progress | **Fully decided.** |
| 7.5 | Lessons | **Fully decided.** |
| 7.6 | Coding questions | **Fully decided.** |
| 7.7 | Refine based on use | Not scoped yet. Starts with spaced repetition and staleness. |

---

## Where this plan comes from

- **A learning-activities outline from a separate ChatGPT conversation.** Its goal: *extend the tree app so nodes can point to internal learning activities and external materials; a node reports progress based on its linked activities, and a node linking to another tree receives progress from that tree. Start with flashcards to prove the flow, then add lessons and coding questions.* It gave this plan its structure (steps 7.0 to 7.7 cover its eight phases) and most of the scope in each step. Everything it contained is now in this plan.
- **A ChatGPT discussion about monolith vs microservices**: build each piece as its own module inside one backend. See "Architecture".
- **The "Milestone 3 — ideas to track" list in `Phase_5_plan.md`**: every item is placed below, in `Phase_6_plan.md`, in a later phase, or under Future ideas.
- **The main plan's "Future Integrations" section** (`Progession_Tracker_plan.md`).

**Where each Milestone 3 idea went:**

| Milestone 3 idea | Now |
|---|---|
| Several readiness sources per node | 7.2 and 7.3 (the core of Phase 7) |
| Practice-activity tracking | 7.1 (flashcard review records), then 7.4–7.6 |
| "Stale" readiness | 7.7, built on spaced repetition (see 7.1) |
| Flashcard (Anki) and coding-practice integrations | 7.1 (in the app) and 7.6 (written problems) |
| Deployment and CI, logging review | **Phase 6** (`Phase_6_plan.md`): CI and logging now, deployment to revisit |
| Logins and multiple users | Phase 8 |
| Orientation toggle, more connection points, edges around boxes, smarter auto-layout | Phase 9 |
| Revision history | Future ideas |

### Words used here

The outline talked about **progress** and **completion**. The app already has one word for this: **readiness**, a whole number from 0 to 100 on every node. This plan keeps "readiness" throughout.

A resource **counts** when it's marked as counting toward its node's readiness. A reference link that's there only for reading doesn't count.

### What already exists that Phase 7 builds on

- **Node → tree links (5.2).** A node can take its readiness from a linked tree: the average of that tree's nodes, through every level of nesting, with loops refused.
- **A readiness contract (5.2).** `ReadinessCalculator` has one implementation per source type (`manual`, `linked_tree`), and readiness is computed when it's read, never stored. Flashcards, external materials, lessons and coding questions each become one more calculator.
- **External links on nodes.** Plain links (`node_links`: a URL and a label) that don't affect readiness.
- **Per-user ownership.** Every node and tree has an owner, but there's no login: the app acts as one default user (`CurrentUserService`).
- **Tests and migrations.** Flyway (V1–V5); backend unit and integration tests (Testcontainers), frontend unit tests (Vitest) and browser tests (Playwright). After Phase 6, CI runs them all on every push.

The main change Phase 7 brings is **several readiness sources per node**, where 5.2 allows exactly one.

---

## Architecture: a modular monolith

**Decided:**
- **One backend, one deployment, one PostgreSQL database**, as the main plan ("Monolith confirmed") and the original outline both said. There's no service discovery, network calls between services, distributed transactions or separate deployments.
- **Each learning feature is its own module** inside that backend, with clear boundaries, so it can be understood, tested and changed on its own.
- **A module is extracted into a separate service only when there's a reason**, such as running submitted code in isolation or scaling one part separately. Future ideas lists the likely one.

**The modules**, adapted from the ChatGPT map to this codebase:

| Module | What it holds | Today |
|---|---|---|
| **Progression** | Trees, library nodes, placements, prerequisite edges, node tags, and node resources (7.2) | The `node` and `tree` packages |
| **Readiness** | The readiness contract, combining a node's resources (7.3), "last reviewed", and later "review due" and staleness (7.7) | The `readiness` package |
| **Flashcards** | Decks, cards, review records; spaced repetition later (7.7) | New in 7.1 |
| **Materials** | External materials with hand-entered progress | New in 7.4 |
| **Lessons** | Lessons, sections, lesson progress | New in 7.5 |
| **Coding practice** | Problems, solutions, attempts. Test cases and results come only with in-browser coding (Future ideas). | New in 7.6 |
| *Shared* | The current user, error handling | The `user` and `common` packages |

The ChatGPT map had a single "Progress / Learning history" module. Here that's the **Readiness** module, and it stores no activity records of its own (see below).

**Each learning module owns its own activity records.** Flashcards stores card reviews, Coding practice stores attempts, and so on. The Readiness module stores nothing of its own: it asks each module, through its calculator, for readiness, "last reviewed" and later "review due". Each module keeps its data and rules together, which is what makes it extractable.
- *Considered instead:* one shared activity table (the main plan's original `practice_events` idea) that every module writes to. It's easy to query across all activities, but every module would then depend on one table's shape, and none could be extracted on its own.

**How modules talk to each other:**
- Through small public Java interfaces, such as `ReadinessCalculator`, never through another module's repositories or entities.
- Across modules, tables refer to each other by plain ids with no database foreign keys. For example, a node resource stores a deck's id, and the Flashcards module confirms the deck exists when the resource is added. That's what would let a module move into its own service and database later.
- Within a module, foreign keys and JPA relationships stay as they are today.

**Spring Modulith enforces the module boundaries.** It's a Spring project that treats each top-level package as a module and has a test that fails the build when one module reaches into another's internals or two modules depend on each other in a circle. It can also draw a diagram of the modules. Its version must match Spring Boot 4.1, which is checked when it's added.
- It needs a small refactor first. Today `node`, `tree` and `readiness` depend on each other in a circle (for example, nodes know their linked tree, and trees ask about nodes). They'd move under one `progression` package, or the circle would be broken by an interface.
- Setting it up, with that refactor, is the first step of 7.1, before the Flashcards module is added.
- *Considered instead:* ArchUnit, a general library for writing the same kind of rules by hand (more flexible, more to write); or enforcing boundaries by convention only, through code review and this plan (nothing stops a shortcut).

---

## 7.0 — Rules and examples

*Document the app as it is and agree the first rules, with examples.*

**Known from the codebase:**
- **Stack:** Spring Boot 4.1 on Java 21, PostgreSQL 18, Flyway, Angular 22, Docker Compose. Tests: JUnit, Mockito and Testcontainers; Vitest; Playwright.
- **Storage:** users, trees, tree tags, library nodes, tree placements, prerequisite edges, node links, node tags and tree edit sessions each have a table (Flyway V1–V5). Readiness is hand-entered on the node, or derived from a linked tree when it's read.
- **Login:** none; one default user.
- **Data and rollout:** your own data is in the local database, backed up before each migration. You're the only user, and there are migrations and tests.

**Known from earlier phases:**
- **Prerequisites don't feed readiness** (5.2). Prerequisite edges only decide how close a node is to "ready to start" (5.5's gradient), so they're prerequisites, not subtasks, and a node never needs its prerequisites complete to count as complete itself.
- **Linked trees nest, and loops are refused** (5.2). A linked tree counts as the plain average of its nodes, whatever its size.

**Decided:**
- **Resources are reusable, readiness is per user, and nothing is shared between users** until logins exist in Phase 8. One flashcard deck can sit on several nodes.
- **A node's readiness is the plain average of its counting resources**, for example a deck at 60% and an article at 100% give 80%. *The calculation is to be revisited later* (see Future ideas).
- **Hand-entered readiness is used only when a node has no counting resources.** Once a resource counts, the typed-in value is hidden but kept, as a linked node's is today, and it comes back if the node loses its last counting resource.
- **A node with no counting resources shows its hand-entered readiness**, as today.
- **No deadline.**
- **At least one learning feature is built on its own before everything is wired into nodes.** That's why flashcards (7.1) come first, studied from their own pages, and typed node resources (7.2) and readiness from resources (7.3) follow.

**Examples** (final: they're what 7.3's tests will check):

| Node | Its resources | Readiness |
|---|---|---|
| CSS Selectors | nothing that counts; hand-entered 40% | **40%** |
| CSS Flexbox | a deck with 15 of 20 cards passed (75%); an article at 60%, counting | (75 + 60) / 2 = **68%** (67.5, rounded) |
| CSS Flexbox, after the article is marked as reference only | the deck | **75%** |
| Front-end Basics | a linked "CSS" tree whose nodes average 54%; a lesson at 100% | (54 + 100) / 2 = **77%** |

**Done:** the rules are decided, the examples above are final, and the facts above were checked against the code (versions, tables, and `Math.round` rounding 67.5 up to 68). The main plan (`Progession_Tracker_plan.md`) marks what these rules supersede: a single readiness source, the shared `practice_events` table, and Anki/CodingBat-style integrations.

---

## 7.1 — Flashcards and review records

*Also covers "practice-activity tracking" from Milestone 3.*

**Goal:** a Flashcards module with decks built in the app, studied from their own pages and from an Anki-style review page, with every review recorded.

**Decided:**
- **Decks are built in the app**, not through Anki. Importing Anki decks is a future idea.
- **You grade each answer yourself as correct or wrong.** Anki-style ratings (Again, Hard, Good, Easy) can come with spaced repetition.
- **A card has passed when its last 3 answers were all correct.** A new card needs 3 correct answers in a row, and one wrong answer un-passes it until it's right 3 times in a row again.
- **A deck's readiness is the share of its cards that have passed**: 16 of 20 passed gives 80%.
- **A deck counts as complete when 80% of its cards have passed.** The deck page says so. It matches a node's default aggregate threshold of 80%, so a deck at 80% already reads as "ready to move on" in the 5.5 gradient.
- **The review page lists every card not yet passed, across all decks, the least recently reviewed first**, in the style of Anki. Never-seen cards come first, and passed cards drop off. Spaced repetition later adds passed cards back when they're due.
- **Spaced repetition comes later**, and it's where "staleness" gets built; both lead 7.7. Every review is recorded with its result from the start, so that history is there when they arrive.
- **The Flashcards module owns its review records** (see Architecture).

**Scope:**
- **First, the module set-up** (see Architecture): add Spring Modulith and its boundary test, and refactor `node`, `tree` and `readiness` so they no longer depend on each other in a circle (one `progression` package, or an interface that breaks the circle). Check Spring Modulith's compatibility with Spring Boot 4.1. The existing tests must pass unchanged in behaviour.
- The Flashcards module: decks and cards (create, edit, delete); a card has a front and a back.
- A study screen for one deck: show the front, reveal the back, mark correct or wrong.
- The review page.
- A `card_reviews` table owned by the module: user, card, time, correct or wrong. This is the `practice_events` idea the main plan designed for, starting with flashcards.
- Deck readiness and "last reviewed", through a `FlashcardReadinessCalculator`, with the review rules kept inside the Flashcards module.

**Done when:** Spring Modulith's boundary test passes, and you can study a deck, work through the review page, and see the deck's readiness change, reaching "complete" at 80% of cards passed.

**Done:**
- **Module set-up:** Spring Modulith 2.1.1 (built against Spring Boot 4.1.1), test scope only. `node`, `tree` and `readiness` moved under one `progression` package, with the dev seed, so the circle between them is inside one module. `ModularityTest` checks the boundaries; every existing test passed unchanged after the move.
- **Flashcards module:** its public API is `FlashcardReadinessCalculator` with the `CardProgress` and `DeckProgress` records, which hold the rules; everything else is in `flashcards.internal`, out of other modules' reach. `V6__flashcards.sql` adds `decks`, `cards` and `card_reviews`.
- **Pages:** Flashcards (the deck list), each deck's page with its cards, a study page per deck (offering every card once all have passed) and the Review page, with "Flashcards" and "Review" in the navigation. A dev seed deck, "CSS Flexbox", has cards in every state.
- **Built for 7.3, not yet used by it:** the calculator works on the Flashcards module's own entities. 7.3 adds the by-id entry point that the Readiness module will call.

---

## 7.2 — Typed node resources

*Feature boundaries and typed resource references.*

**Goal:** a node can list **typed resources**: a linked tree, a flashcard deck, a lesson, a coding question or an external URL. Each has a label, an order, and whether it counts toward readiness.

**Today:** a node has plain links (`node_links`) that don't count, and at most one linked tree (`nodes.linked_tree_id`).

**Decided:**
- **Today's node links and linked trees move into `node_resources`.** External links become URL resources that don't count, and a linked tree becomes a tree resource that counts. The migration keeps every existing link.
- **A node can link to several trees.** The existing loop checks cover any number of links.
- **Resources are ordered by hand, and a label defaults to the target's title.**

**Scope:**
- A `node_resources` table in the Progression module: resource type, target id or URL, label, position, and "counts toward readiness". Targets in other modules are referred to by plain id (see Architecture).
- Validate targets: internal ones must exist and belong to the user (each module confirms its own); URLs must be http(s).
- The node page and form list resources by type.

**Done when:** nodes list typed resources, and every existing link and linked tree still works.

**Done:**
- **Types so far:** `url` and `tree`, the two that exist today. Decks join in 7.3, lessons in 7.5 and coding questions in 7.6, each widening `node_resources_resource_type_check`. A URL never counts; a tree counts unless marked for reference only.
- **Storage:** `V7__node_resources.sql` creates `node_resources` and moves every link and linked tree into it (the linked tree first and counting, then the links in order), then drops `node_links`, `nodes.linked_tree_id` and `nodes.readiness_source_type`. A tree is referred to by a foreign key, since it's in the same module; targets in other modules will be plain ids. `MigrationTest` checks the move on data in the old shape.
- **Readiness:** the 7.0 rule applies to the trees that count: their plain average, or the hand-entered value when none count. `ReadinessCalculator` is now one per resource type (`TreeResourceReadinessCalculator`), which is where 7.3 adds decks. Loop checks run for every tree a node counts; a reference-only tree can't make a loop.
- **Pages:** the node form lists resources in order ("+ Add link", "+ Add tree", move up and down, remove, and "Counts toward readiness" on trees). The node page groups them by type, trees first. The tree editor's details panel lists them and offers "Open" for each tree that counts.

---

## 7.3 — Readiness from several resources

*Attach flashcards to nodes, and calculate node and tree readiness from resources.*

**Decided:**
- **Contributions are shown on the node page and in the tree view's node details**, for example "Deck: 75%, Article: 60% → 68%". Node boxes on the canvas keep one number.
- **"Last reviewed" is the most recent review anywhere beneath** a node or tree.
- **Trees show their own readiness** in the tree list and on the tree page.

**Scope:**
- Attach a deck to a node, and open the deck from the node.
- A node's readiness is the average of its counting resources (7.0), or its hand-entered value when none count.
- Tree readiness, linked-tree readiness and loop checks carry on working with several resources and several linked trees.
- A "last reviewed" date on nodes and trees, gathered by the Readiness module from each module's calculator.
- Readiness stays computed when it's read; caching only if 7.7 finds it's needed.

**Done when:** reviewing a card changes the deck's, the node's, the tree's and any linking tree's readiness correctly, and the examples in 7.0 pass as tests.

**Done:**
- **Decks as resources:** `V8__deck_resources.sql` adds the `deck` type, stored by plain id (`target_id`, no foreign key). The Flashcards module's public API reads a deck by id for the current user; `NodeService` uses it to confirm a deck exists, and `DeckResourceReadinessCalculator` to read its readiness. The node form has "+ Add deck", and the node page links to the deck.
- **A deck in use can't be deleted** (409, listing the nodes), as for trees. Flashcards can't depend on Progression without a circle, so it declares `DeckDeletionCheck` in its API and Progression implements it.
- **Contributions:** the node page and the tree editor's details show "Deck Flexbox cards: 75%, Tree Article: 60% → 68%", each part linking to its page. Canvas boxes keep one number. Every resource also shows its own readiness, counting or not.
- **Last reviewed:** each calculator returns a resource's readiness and latest review together. A node's is the latest beneath its resources that count (so a deck kept only for reference doesn't move it); a tree's is the latest of its nodes'. Shown on the node page, the tree editor, the tree page and the tree list.
- **Trees show their own readiness** in the tree list and under the tree's title.
- **Tests:** `ReadinessFromResourcesIntegrationTest` holds the 7.0 examples and the full chain from one card review up to a tree above. The article (7.4) and lesson (7.5) don't exist yet, so a tree at 60% and a fully passed deck stand in for them; the arithmetic is the same, and 7.4 and 7.5 can swap in the real types.

---

## 7.4 — External materials with manual progress

**Decided:**
- **You enter progress by hand as 0–100%.**
- **A URL can be purely a reference that doesn't count**; that's what today's node links become in 7.2.
- **Every progress update is kept**: setting an article to 40% on 1 September and 60% on 15 September keeps both. The current value is the latest, and "last reviewed" is the latest update's date. The history is there for charts or staleness later.

**Scope:**
- A small Materials module: URL, title and optional notes.
- Your own progress updates (0–100%, with the date and an optional note on what you covered), each kept as a record owned by the module. Editing the link itself doesn't count as a review.
- Shown as self-reported, and included in the node's readiness only if the resource counts.

**Done when:** updating your progress on a material changes your node and tree readiness.

---

## 7.5 — Lessons

**Decided:**
- **Lessons are written in the app**, in Markdown, with sections.
- **"Last reviewed" is when you last opened the lesson.**
- **Progress is entered by hand as 0–100% for now**, like external materials. *To be revisited: other ways to measure a lesson's progress, or dropping progress for lessons entirely* (see Future ideas).

**Scope:**
- A Lessons module: lessons with sections; create, edit, read.
- Opening a lesson records a review (for "last reviewed"); progress is set on the lesson page.
- A lesson calculator for the readiness contract.

**Done when:** lessons feed node and tree readiness without the tree code knowing lesson rules.

---

## 7.6 — Coding questions

*Also covers "coding-practice integration" from Milestone 3.*

**Decided:**
- **Written problems that you solve on your own machine.** Each question has a problem statement and a solution that stays hidden until you ask to see it. No code runs on the server.
- **In-browser coding later**: writing and running solutions in the app is a future idea.
- **HTML and CSS first, then JavaScript**, to cover the front-end side of the learning plan. JavaScript can come after HTML and CSS are in place.
- **A question counts when you mark it solved**: a question is 0% or 100%, and a set's readiness is the share of its questions solved. Revealing the solution before marking it solved is recorded, so a later version could treat that differently.

**Scope:**
- A Coding practice module. Questions: title, language (HTML, CSS, or later JavaScript), problem statement, worked examples, and solution (hidden until revealed).
- Questions grouped into sets, such as "CSS Flexbox exercises", so a node can point to a whole set.
- Your attempts recorded (marked solved, and whether the solution was revealed first), owned by the module, and a calculator for the readiness contract.

**Done when:** attempts update the question's, node's and tree's readiness.

---

## 7.7 — Refine based on use

*Scoped after 7.3 has been in use for a while.*

**Candidates:**
- **Spaced repetition and staleness** (from 7.1). Cards get due dates from how you've answered them, Anki-style, and passed cards come back to the review page when they're due. Cards that are overdue make their deck, and the nodes and trees above it, show as going stale, and perhaps lose readiness until they're reviewed. The same idea could later cover lessons and external materials, using their last-reviewed dates.
- Focused tests for aggregation, changed resources, cross-tree links, loops and keeping users separate, and migrations run against real data (much of this is added along the way).
- Measure slow tree views, and add caching or batched queries only where they're needed.
- Reconsider whether any module should become a separate service. The expected answer is "not yet": modules stay together unless running them separately has clear value, such as isolating code execution (Future ideas).

---

## Phase order

**Decided:** deployment, CI and logging come before the learning activities.

| Phase | Topic | Plan |
|---|---|---|
| 6 | CI and logging (deployment moved to that plan's "to revisit" section) | `Phase_6_plan.md` |
| 7 | Learning activities (this plan) | `Phase_7_plan.md` |
| 8 | Logins, multiple users and sharing | Not scoped yet (below) |
| 9 | Tree editor improvements | Not scoped yet (below) |

### Phase 8 — Logins, multiple users and sharing

From the Milestone 3 list, and the question of sharing raised in 7.0.
- Logging in, replacing the default user in `CurrentUserService`, the one place designed to change.
- Activity records (7.1, 7.4–7.6) are stored per user from the start. Hand-entered node readiness is stored on the node itself, so sharing a node would need it moved to a per-user table.
- Sharing trees and resources between users, which the main plan kept out of v1.

### Phase 9 — Tree editor improvements

From 5.4's Milestone 3 items.
- **Horizontal/vertical orientation toggle** in view mode, moving the connection points to the sides. 5.4 stores edge routes as offsets so this stays possible.
- **More connection points per node**, spreading edges along a node's sides.
- **Edges that route around node boxes** automatically.
- **Smarter auto-layout** that reduces crossings and overlaps.

---

## Future ideas to revisit

Not scheduled; kept so they aren't lost.

- **Revisit how a node combines its resources** (7.0): for example weights per resource, or counting a linked tree by its size, instead of a plain average.
- **Revisit lesson progress** (7.5): a better measure than a hand-entered percentage, such as sections completed or a short quiz, or no progress for lessons at all.
- **In-browser coding** (7.6): write and check solutions in the app, with test cases and results. HTML, CSS and JavaScript can run in a sandboxed frame in your own browser, so this needs no server-side sandbox, unlike running Java or Python. That makes it far simpler for the languages you've chosen.
- **Extracting a module into its own service** (Architecture): most likely a service for running submitted code in isolation, and only if back-end languages are added.
- **Anki import**, to bring existing decks into the app.
- **Revision history**: git-style saved versions of a tree, with a history list and restoring or comparing versions. 5.3's restore point and undo/redo cover the everyday need for now.
- **Readiness caching**, if 7.7 finds reading it live is too slow.
