# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project status

A skill-tree and readiness-tracking app, built in phases. `plans/Progession_Tracker_plan.md` is the only source of truth for scope, decisions and the phase list. Read the relevant section before starting a phase. Anything the plan marks as deferred (revision history, tree linking and readiness aggregation, cross-user features) must not be built early. The data model should only be *shaped* to allow it.

Workflow: after finishing a set of changes, stop and ask the user to review them. Commit only after they approve, then ask before starting the next phase.

## Commands

Everything runs from the repo root unless noted. Docker must be running.

```bash
docker compose up -d          # local Postgres 18 on :5432 (db/user/password: progressiontracker)
docker compose down           # stop it (add -v to delete the data volume)

docker compose --profile app up -d --build   # whole app in containers at http://localhost:8081
docker compose --profile app down            # stop it; plain `down` leaves the app containers running

cd backend
./mvnw spring-boot:run                                   # run the API on :8080 against the compose DB
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev    # same, plus hand-written seed data (seed/DevDataSeeder)
./mvnw test                                              # all tests (Testcontainers starts its own Postgres)
./mvnw test -Dtest=ProgressionTrackerBackendApplicationTests            # a single test class
./mvnw test -Dtest=ProgressionTrackerBackendApplicationTests#contextLoads  # a single test method
./mvnw package                                           # build the jar into backend/target/
```

Frontend, from `frontend/` (the backend must be running for `npm start` to show data):

```bash
npm install                                    # first time only
npm start                                      # dev server on :4200; proxies /api to :8080 (proxy.conf.json)
npm test -- --watch=false                      # all Vitest specs, once
npm test -- --watch=false --include src/app/trees/readiness.spec.ts   # a single spec file
npm run build                                  # production build into frontend/dist/
npx playwright install chromium                # first time only: the browser for the e2e tests
npm run e2e                                    # Playwright browser tests against a throwaway Docker stack
npm run e2e -- e2e/trees.spec.ts               # a single browser-test file
```

No linter is configured. The frontend has a Prettier config (`frontend/.prettierrc`); the backend has no formatter.

## Containers (Phase 4b)

`backend/Dockerfile` and `frontend/Dockerfile` are two-stage builds: Maven then a JRE, and Node then nginx. The `backend` and `frontend` Compose services sit behind the `app` profile so plain `docker compose up` stays database-only for development. The backend container gets its database URL from `SPRING_DATASOURCE_URL` and publishes no port (8080 stays free for `./mvnw spring-boot:run`). `frontend/nginx.conf` forwards `/api` to `backend:8080` and falls back to `index.html` for Angular routes, the same job `proxy.conf.json` does for `npm start`. Image builds skip tests because Testcontainers can't run inside a build.

## Browser tests (Phase 5.0)

Playwright on Chromium only, in `frontend/e2e/`, configured by `frontend/playwright.config.ts`. `e2e/global-setup.ts` runs `docker compose -f e2e/docker-compose.yml` (project `progression-tracker-e2e`: the same Dockerfiles, an in-memory Postgres, the `dev` seed, frontend on port 8090 or `E2E_PORT`), waits for the API, and tears the stack down after the run (`E2E_KEEP_STACK=1` keeps it). It first removes any stack left by an interrupted run, so every run starts from the seed data.

- Tests set up their own data through the `api` fixture (REST calls) and give it titles from the `unique` fixture, so they're independent and run in parallel. Only read the seed data, never change it.
- Find canvas nodes with `nodeBox(page, title)`, which matches the SVG `<title>` (the visible text is shortened). The `page` fixture accepts every `confirm()` dialog.
- On edit pages, wait for the loaded values (`toHaveValue`) before typing, or the load overwrites what was typed.
- Every sub-phase that changes on-screen behavior adds tests here.

## Layout

- `backend/`: Spring Boot 4.1 on Java 21, built with Maven (wrapper included). Base package is `com.progressiontracker`. Maven covers only the backend.
- `frontend/`: Angular 22 app built with npm and the Angular CLI, separate from the Maven build. It uses plain Angular with no component library, and UI polish is deliberately deferred. Browser tests are in `frontend/e2e/`.
- `plans/`: the project plan, plus `skill_tree_plans/*.md` as reference content.
- `data/trees/*.json`, `data/schema.json`: skill trees in the format of the older SkillTreeOSS app. They're reference material only. The app doesn't load them, and they don't map directly onto the new model. Phase 1 seed data is written by hand.

## Decided technical conventions

- **Persistence:** Spring Data JPA + Hibernate throughout, with no hand-written JDBC layer.
- **Schema (Phase 5.1):** Flyway migrations in `backend/src/main/resources/db/migration/` create and change the schema. They run on startup everywhere (`spring-boot:run`, Docker, Testcontainers tests). Hibernate runs with `ddl-auto=validate` and refuses to start if an entity and the schema disagree. `V1` is the Milestone 1 schema as Hibernate built it; a database created before Flyway (no history table) is baselined at V1 (`spring.flyway.baseline-on-migrate`), and `MigrationTest` covers both paths. To change the schema, add the next `V<n>__<what_it_does>.sql` and update the entities to match. Never edit a migration that has already run: Flyway checksums them and refuses to start.
- **REST:** every endpoint is under `/api/v1/...`.
- **Tests:** unit tests use JUnit + Mockito (loaded as a Java agent through surefire's `argLine`), with `TestEntities.withId` to set IDs on entities that were never saved. Integration tests run against real Postgres through Testcontainers, never H2 or another in-memory database. Repository tests use `@DataJpaTest`, and `ApiIntegrationTest` drives the whole HTTP stack with `MockMvcTester`, emptying the tables after each test. Test classes that need the database import `TestcontainersConfiguration`, which uses a `@ServiceConnection` `postgres:18` container, the same image as `docker-compose.yml`. `TestProgressionTrackerBackendApplication` runs the app against a throwaway container instead of the compose database.
- **Logging:** Spring Boot's default SLF4J + Logback, left unconfigured for now.
- `spring.jpa.open-in-view=false`: all database access belongs in the service layer.
- **Schema constraints:** every constraint has a readable name: `<table>_<column>_fk` for foreign keys, `<table>_<columns>_unique`, and names such as `nodes_readiness_range` for checks. The migrations create them, and the entities repeat the names (`@CheckConstraint`, `@UniqueConstraint`, `@ForeignKey`) so the mappings document them. Tests assert on those names. Cascades go through `@OnDelete(CASCADE)` on foreign keys (tree → tree_nodes → prerequisites). Deleting a library `Node` that a tree still uses is refused: `NodeService` returns 409, and the foreign key backs that up.
- **Naming:** a field ending in a single capital letter (e.g. `positionX`) doesn't get an underscore from Spring's naming strategy, so name those columns explicitly. Renaming or dropping a column is a migration, not a database reset.
- **Enums:** store them as lowercase strings through an `AttributeConverter` (see `ReadinessSourceTypeConverter`), with a check constraint listing the allowed values. Adding a value means a migration that widens the check.

## Backend structure

Code is organized by feature package (`user`, `node`, `tree`, `readiness`), each with its own entities, repositories, services, controllers and request/response records. `common` holds the exception types and `ApiExceptionHandler`.

- **Readiness (Phase 5.2):** a node's effective readiness is computed when it's read, never stored. `ReadinessService.context()` gives a `ReadinessContext` for one request (it caches node and tree values, and loads linked trees lazily, so use it inside the service transaction). The context picks the `ReadinessCalculator` for the node's `ReadinessSourceType`: `ManualReadinessCalculator` returns the hand-entered value, and `LinkedTreeReadinessCalculator` averages the linked tree's nodes' effective readiness (recursively, rounded to a whole percent, 0 for an empty tree). A new source type adds an enum value, a calculator and a migration widening `nodes_readiness_source_type_check`; `ReadinessService` refuses to start if a type has no calculator. Responses carry the effective `readiness`; `NodeResponse` also has `manualReadiness` and both have `linkedTree`.
- **Tree links:** `TreeLinks.checkNoLoop` refuses (409) a link that would make a tree's readiness depend on itself. It runs when a node's link is set (`NodeService`) and when a linked node is placed in a tree (`TreeNodeService.add`). A tree that nodes link to can't be deleted (`TreeService.delete`, 409 with a `nodes` list).
- **Edit sessions (Phase 5.3a):** `TreeEditSessionService` backs a tree's edit mode. `start` saves a `TreeSnapshot` (details, placements by library node id, thresholds, edges by library node ids) as JSON in `tree_edit_sessions`, one per tree (409 if one exists). Edits are still saved as they're made. `finish` ("Done") deletes the session. `discard` restores the snapshot, matching placements by library node id: it re-places removed nodes, removes added ones, rebuilds every edge, and deletes library nodes created since the session started that end up in no tree. `TreeResponse.editSessionStartedAt` shows a session is open. The backend doesn't refuse edits outside a session; view mode is enforced by the UI.

- **Current user:** `CurrentUserService.getCurrentUser()` is the only way code finds out who the user is. In v1 it returns (and creates on first use) the user named by `progressiontracker.default-username`. When authentication is added, only this class should change. Every lookup is scoped to the owner (`findByIdAndOwner`, `TreeService.findOwned`, `TreeNodeService.findInTree`), and a resource owned by someone else returns 404, not 403.
- **Services return DTOs:** entities never leave the service layer. Because `open-in-view` is off, mapping to response records has to happen inside the service transaction, where lazy associations can still load. Update methods call `flush()` before mapping so `updatedAt` is current.
- **Errors:** throw `NotFoundException` (404), `BadRequestException` (400) or `ConflictException` (409, with optional extra properties such as the `trees` list when a node delete is refused). `ApiExceptionHandler` turns them into `ProblemDetail`, and bean-validation failures come back with an `errors` list of `{field, message}`. Database constraint violations are a 409 backstop, not the main way rules are checked.
- **Edges:** `PrerequisiteService` checks self-edges, same-tree membership, duplicates and cycles (`PrerequisiteGraph`) in that order.

## Frontend structure

Standalone components, zoneless change detection, signals for state, reactive forms, and the 2025 file naming (`node-list.ts`, not `node-list.component.ts`).

- `core/`: `api.models.ts` mirrors the backend's response and request records and must be kept in sync with them by hand. `NodeApi` and `TreeApi` are thin `HttpClient` wrappers using relative `/api/v1` URLs. `problem.ts` turns problem responses into messages. `test-data.ts` has `aNode()`/`aTreeNode()` builders for specs.
- `nodes/`: the library list, `NodeView` (`/nodes/:id`, the node's read-only page with links, tags, the trees using it and its readiness), and the create/edit form. `ReadinessEditor` updates hand-entered readiness in place (`PUT /nodes/{id}/readiness`) and is used on both view pages; it's the only change view mode allows. The form covers title, description, links, tags and the readiness source (hand-entered, or a linked tree; the tree list loads when that option is first chosen). It doesn't edit the hand-entered value but always sends it back, so unlinking restores it. It honours a `returnTo` query param, restricted to in-app paths, and otherwise returns to the node's page.
- `trees/`: the tree list, `TreeForm` for tree metadata CRUD (Phase 3b; comma-separated tags parsed in `tags.ts`), and `TreeView`, the tree editor. `TreeView` draws an inline SVG of boxes centred on the stored positions, marks linked nodes, and loads its data from an `effect` on `id()`, because "Open linked tree" reuses the component for another tree. `readiness.ts` (the ready/locked rule), `tree-layout.ts` (view box and edge geometry) and `auto-layout.ts` (layered layout) are pure functions with their own specs.
- **View and edit modes (Phase 5.3a):** `TreeView` opens in view mode: clicking a node shows its details, links and a `ReadinessEditor`, and nothing can be dragged. "Edit" starts an edit session; "Done" and "Discard changes" end it. The route's `canDeactivate` calls `TreeView.canLeave`, which lets edit mode go to the tree's own details form (opened with `?resumeEdit=true`, so `TreeForm` returns into the session) and otherwise confirms, then discards. `beforeunload` triggers the browser's own warning. A session found on load without `resumeEdit` is unfinished: a banner asks "Keep" (finish) or "Discard" before anything else.
- **Tree editor (Phase 4, edit mode only):** a toolbar picks the `Tool` (`select`, `add`, `connect`, `delete`) that decides what a canvas click does. To add a tool, extend `Tool` and handle it in `onNodeActivate`, `onCanvasClick` and `onEdgeClick`. Every edit is saved immediately. Moves and threshold changes replace the one node from the response, while structural changes (nodes or edges added or removed) call `reloadContents()`. Dragging uses mouse events with a 3-unit threshold before a press counts as a drag, and freezes the view box during the drag. `toCanvasPoint` converts screen coordinates with `getScreenCTM`, falling back to unscaled coordinates in jsdom. `AddNodePanel` places an existing library node or creates and places a new one.
- **Routing:** route and query params bind to component `input()`s (`withComponentInputBinding`), so components read `id()` and never inject `ActivatedRoute`. `TreeView` and `NodeView` load from an `effect` on `id()`, because a link to another tree or node reuses the component.
- **Tests:** Vitest in jsdom through `ng test`, with Jasmine-style `describe`/`it`/`expect` globals and `vi` for spies. HTTP goes through `HttpTestingController`. Because the app is zoneless, call `await fixture.whenStable()` after flushing a request or changing inputs.

## Domain model (from the plan)

- The schema supports multiple users from the start: data is owned by a `user_id`. The v1 experience is still single-user.
- **Node:** lives in a per-user library, independent of any tree. It has a title, description, external links, a hand-entered readiness value (integer 0–100) and `readiness_source_type`: `'manual'`, or `'linked_tree'` with `linked_tree_id` set (Phase 5.2). A linked node's readiness is the average of the linked tree's nodes; its hand-entered value is kept but not used. Trees can nest through links, but never in a loop. Node detail views show both its prerequisites and the nodes that depend on it.
- **Tree:** an arrangement of library nodes, with category, tags and description. The same node can appear in several trees.
- **Ready vs locked** (computed client-side in `trees/readiness.ts`): a tree node is ready when it has no prerequisites, or when its prerequisites' readiness averages at least its `aggregateThreshold` and each one is at least its `individualThreshold`. Its own readiness doesn't count. Prerequisites never feed into readiness itself; only a linked tree does.
- **Node tags** (Phase 5.3a): comma-separated in the form, like tree tags, stored in `node_tags`.
- **Tree edit session:** a tree in edit mode, with the restore point taken when "Edit" was clicked. No tree stays in edit mode: leaving discards, and an unfinished session is resolved on the next visit.
- **TreeNode:** the tree-to-node association. It holds the x/y position and the node's two readiness thresholds for that tree: `aggregateThreshold` (default 80, the minimum average of its prerequisites) and `individualThreshold` (default 70, the minimum for each prerequisite). The thresholds are per node, not per edge; the user decided this in Phase 1. A node shows as "ready" only when both are met.
- **Prerequisite:** a directed edge (`prerequisite` → `dependent`) between two TreeNodes in the same tree. The database blocks self-edges and duplicate edges. The same-tree rule and cycle prevention are enforced in `PrerequisiteService`, and the client checks for cycles too.
