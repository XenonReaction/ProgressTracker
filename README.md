# Progression Tracker

[![CI](https://github.com/XenonReaction/ProgressTracker/actions/workflows/ci.yml/badge.svg)](https://github.com/XenonReaction/ProgressTracker/actions/workflows/ci.yml)

An app for building skill trees and tracking how ready you are on each skill. It's a
single-user proof of concept: there's no login, and every request acts as one default user.

What it does today:

- **Node library.** Skills (nodes) with a description, tags, a readiness value from 0 to
  100, and resources: links to read, and other trees. A tree can count toward the node's
  readiness, which is then the average of the trees that count (each the average of its
  nodes); otherwise readiness is entered by hand. Trees nest to any depth but never in a
  loop.
- **Skill trees.** Arrangements of library nodes on a canvas, joined by prerequisite
  edges. The same node can sit in several trees. Each node shows how close it is to being
  worth starting (not started, early, close or ready), judged by its prerequisites'
  readiness against two thresholds you set per node.
- **Tree editor.** Trees open read-only. "Edit" starts an edit session: add, move, connect
  and remove nodes, auto-layout, drag right-angle edges into place, with undo and redo.
  "Done" keeps the changes, and "Discard changes" puts the tree back as it was.
- **Flashcards (Phase 7.1).** Decks of cards that you study and grade yourself as right or
  wrong. A card passes after 3 correct answers in a row, a deck's readiness is the share
  of its cards passed, and a deck is complete at 80%. A review page gathers every card not
  yet passed, from all decks. Decks don't feed node readiness yet; that's Phase 7.3.

The plan, phase by phase, is in `plans/` (`Progession_Tracker_plan.md`, then one file per
phase; `Phase_7_plan.md` is the current one).

## Repository layout

| Path        | Contents                                                           |
|-------------|--------------------------------------------------------------------|
| `backend/`  | Spring Boot 4.1 REST API (Java 21, Maven, Spring Data JPA, Flyway, PostgreSQL 18) |
| `frontend/` | Angular 22 app (plain Angular, Vitest unit tests, Playwright browser tests in `e2e/`) |
| `plans/`    | Project plan, phase plans and reference skill-tree write-ups       |
| `data/`     | Reference skill-tree JSON files from the older app (not loaded at runtime) |

The backend is a modular monolith: one application and one database, with each top-level
package under `com.progressiontracker` a separate module. `progression` holds nodes, trees
and readiness; `flashcards` holds decks, cards and answers; `user`, `common` and `config`
are shared. A module may only use another module's public top-level package, and
`ModularityTest` ([Spring Modulith](https://spring.io/projects/spring-modulith)) fails the
build if one reaches into another's internals.

## Quick start (Docker only)

With just Docker installed, one command builds and starts the database, backend and
frontend:

```bash
docker compose --profile app up -d --build
```

Then open http://localhost:8081. The first build takes a minute or two; later ones are
cached. Add `--build` again after changing code so the images are rebuilt.

- To load the sample data into an empty database, start it with
  `SPRING_PROFILES_ACTIVE=dev docker compose --profile app up -d --build`.
- To stop everything, run `docker compose --profile app down`. Without `--profile app`,
  only the database stops. Your data is kept in a Docker volume; add `-v` to delete it.

The containers use the same database and data as the development setup below.

## Prerequisites for development

- Java 21
- Docker (for the local database, and for the integration and browser tests)
- Node.js 22+ and npm (for the frontend)

Maven does not need to be installed; use the included wrapper, `./mvnw`.

## Running locally for development

```bash
# 1. Start PostgreSQL (port 5432)
docker compose up -d

# 2. Start the backend (http://localhost:8080)
cd backend
./mvnw spring-boot:run

# Or start it with sample data
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Then, in another terminal, start the frontend (http://localhost:4200):

```bash
cd frontend
npm install   # first time only
npm start
```

The Angular dev server forwards `/api` requests to the backend on port 8080.

Stop the database with `docker compose down`. The data is kept in a Docker volume; add
`-v` to delete it too.

### Sample data

The `dev` profile loads a small hand-written data set for the default user, but only into
an empty database, so restarting doesn't duplicate it:

- **Nodes and trees:** ten library nodes (one in no tree) and three trees. "Java
  Fundamentals" and "Spring Basics" share a node, and "Collections Framework" takes its
  readiness from the third tree, "Collections in Depth".
- **Flashcards:** a "CSS Flexbox" deck of five cards, one in each state: passed, two
  correct answers in, last answered wrong, and two never answered. The deck is at 20%.

Each module seeds itself, so a database that already has trees but no decks still gets the
sample deck.

### Database migrations

The schema is created and changed by [Flyway](https://documentation.red-gate.com/fd)
migrations: numbered SQL files in `backend/src/main/resources/db/migration/` (currently
`V1` to `V6`). When the backend starts (locally, in Docker or in tests), Flyway runs any
the database hasn't had yet, in order, and records them in its `flyway_schema_history`
table. Hibernate then checks that the entity classes match the schema, and the backend
refuses to start if they don't.

To change the schema:

1. Add the next file, for example `V7__add_node_resources.sql`: two underscores after the
   version, then what it does.
2. Update the entity classes to match.
3. Restart the backend. `./mvnw test` also runs every migration on a fresh database.

Never edit a migration that has already run: Flyway notices the change and refuses to
start. Write a new migration instead.

A database that has tables but no Flyway history is refused rather than adopted.

### Configuration

The defaults in `application.properties` are for local development. Anywhere else, supply
the database from the environment:

| Variable | Default (local only) |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/progressiontracker` |
| `SPRING_DATASOURCE_USERNAME` | `progressiontracker` |
| `SPRING_DATASOURCE_PASSWORD` | `progressiontracker` |

With Docker Compose, `POSTGRES_USER` and `POSTGRES_PASSWORD` (for example in a git-ignored
`.env` file) set the login for both the database and the backend.

The `prod` profile (`SPRING_PROFILES_ACTIVE=prod`) is for a future deployment. It refuses to
start unless all three `SPRING_DATASOURCE_*` variables are set, and the sample data never
loads with it.

### Logs

The backend logs one line per API request (method, path, status and time taken), plus
unexpected errors in full and a few important changes, such as a tree or deck being
deleted. It never logs the text of your trees, nodes or cards. Each request gets an id,
shown on every line logged while handling it and returned in the `X-Request-Id` response
header, so a problem seen in the browser can be matched to its log lines.

Logs are plain text locally, and JSON (the ECS format) with the `prod` profile. In Docker,
`docker logs progression-tracker-backend` shows them; each container keeps at most three
10 MB log files.

### Health check

`GET /actuator/health` on the backend (port 8080) reports `UP` or `DOWN`, with the state of
each check, such as `db`, but none of their details. Nothing else under `/actuator` is
exposed. Docker Compose uses it: the frontend container starts once the backend reports
`UP`.

## API

All endpoints are under `/api/v1` and exchange JSON. There's no login yet: every
request acts as a single default user (`demo`). Errors are returned as
[RFC 9457](https://www.rfc-editor.org/rfc/rfc9457) problem responses
(`application/problem+json`); a request that fails validation lists every bad field.

### Nodes and trees

| Method & path | Purpose |
|---|---|
| `GET/POST /nodes`, `GET/PUT/DELETE /nodes/{id}` | Node library, each node with its `resources` in order (see below). Deleting a node that a tree still uses returns 409 and lists those trees. |
| `PUT /nodes/{id}/readiness` | Sets only the hand-entered readiness (the view pages use it). Refused with 409 while any of the node's resources counts. |
| `GET /nodes/{id}/trees` | The trees a node is placed in. |
| `GET/POST /trees`, `GET/PUT/DELETE /trees/{id}` | Tree metadata. Deleting a tree also removes its placements and edges, but not library nodes. A tree that any node lists as a resource can't be deleted (409, listing those nodes). |
| `GET/POST /trees/{treeId}/nodes`, `GET/PUT/DELETE /trees/{treeId}/nodes/{treeNodeId}` | Library nodes placed in a tree, with position and readiness thresholds. |
| `PUT /trees/{treeId}/nodes/positions` | Moves many tree nodes in one transaction (used by auto-layout). An unknown id changes nothing. |
| `POST/DELETE /trees/{treeId}/edit-session`, `POST /trees/{treeId}/edit-session/discard` | Edit mode: `POST` saves a restore point (409 if the tree is already being edited), `DELETE` is "Done" and keeps the changes, and `discard` puts the tree back as it was. A tree's `editSessionStartedAt` is set while a session is open. |
| `GET/POST /trees/{treeId}/prerequisites`, `DELETE /trees/{treeId}/prerequisites/{id}` | Prerequisite edges. Self-edges (400), duplicates (409) and cycles (409) are refused. |
| `PUT /trees/{treeId}/prerequisites/{id}/route`, `DELETE /trees/{treeId}/prerequisites/routes` | An edge's hand-adjusted right-angle route (`{"segments": 3, "offsets": [40]}`, or null for the default), and resetting every route in a tree. |

**Resources and readiness.** A node's `resources` are sent and returned in order. Each is
`{"type": "url", "url": "https://…"}` (for reading, never counts) or `{"type": "tree",
"treeId": 3, "counts": true}` (returned with `tree: {id, title}`), with an optional
`label`; without one, the target's title is shown. A node's `readiness` in every response is
its effective value: the average of the trees that count, each the average of its nodes'
readiness, rounded to a whole percent (0 for an empty tree), with nodes inside those trees
counting their own derived value. When nothing counts, it's the hand-entered value, which
is always sent as `readiness` in requests and returned as `manualReadiness`. A URL that
counts, a tree listed twice or an unknown type is refused with 400. Counting a tree that
would make a tree's readiness depend on itself is refused with 409, whether it comes from
saving a node or placing it in a tree.

Tree node ids and library node ids are different: `/trees/{treeId}/nodes/{treeNodeId}`
and prerequisite edges use tree node ids, and each tree node response includes the
library `nodeId`.

### Flashcards

| Method & path | Purpose |
|---|---|
| `GET/POST /decks`, `GET/PUT/DELETE /decks/{id}` | Decks (title and description), each returned with `cardCount`, `passedCount`, `readiness`, `complete` and `lastReviewedAt`. Deleting a deck deletes its cards and their answers. |
| `GET/POST /decks/{deckId}/cards`, `PUT/DELETE /decks/{deckId}/cards/{cardId}` | A deck's cards (`front` and `back`), each returned with `correctInARow`, `passed` and `lastReviewedAt`. Editing a card keeps its answers. |
| `POST /decks/{deckId}/cards/{cardId}/reviews` | Records one answer, `{"correct": true}` or `false`, and returns the card with its new standing. Every answer is kept. |
| `GET /review-queue`, `GET /review-queue?deckId={id}` | Every card not yet passed, from all decks or one: never-answered cards first, then the least recently answered. |

Readiness is worked out from the recorded answers each time it's read, never stored. A
card has passed when its last 3 answers were all correct, so one wrong answer un-passes
it until it's right 3 times in a row again.

## Tests

Docker must be running for the backend and browser tests.

### Backend

```bash
cd backend
./mvnw test                                     # everything
./mvnw test -Dtest=FlashcardsApiIntegrationTest  # one class
./mvnw test -Dtest=ModularityTest                # just the module-boundary check
```

- **Unit tests** (JUnit and Mockito) cover services and pure rules, such as the flashcard
  pass and complete rules and the prerequisite cycle check.
- **Integration tests** run against a real PostgreSQL 18 started by
  [Testcontainers](https://testcontainers.com/): repository and mapping tests, the Flyway
  migrations on a fresh database, and the whole REST API driven through HTTP. They never
  touch the database started by `docker compose`.
- **`ModularityTest`** fails if one module uses another's internals, or if two modules
  depend on each other in a circle.

### Frontend

```bash
cd frontend
npm test -- --watch=false                                                   # all Vitest specs, run once in jsdom
npm test -- --watch=false --include src/app/flashcards/study-session.spec.ts   # one spec file
npm run build                                                               # production build into dist/
```

### Browser tests

The browser tests drive a real Chromium with [Playwright](https://playwright.dev/): they
click, type and drag like a person and check what appears on screen. They run against a
throwaway copy of the app, never your own data.

```bash
cd frontend
npx playwright install chromium        # first time only (~150 MB)
npm run e2e                            # all browser tests
npm run e2e -- e2e/flashcards.spec.ts  # one file
npm run e2e -- --ui                    # Playwright's interactive runner
```

`npm run e2e` builds the Docker images, starts a separate stack
(`frontend/e2e/docker-compose.yml`, with the sample data in an in-memory database) on
http://localhost:8090, runs the tests, and removes the stack. Your own database and
containers are left alone. Set `E2E_PORT` to use another port, or `E2E_KEEP_STACK=1` to
leave the stack running afterwards for a look around.

Each test creates its own uniquely named data through the API, so tests run in parallel
and only ever read the sample data, never change it.

When a test fails, `npx playwright show-report` opens the results, including a
step-by-step trace of the failing test.

## Continuous integration

GitHub Actions (`.github/workflows/ci.yml`) runs on every push to any branch, and can be
started by hand from the repository's **Actions** tab. Three jobs run side by side:

| Job | What it runs |
|---|---|
| Backend tests | `./mvnw test` (Testcontainers uses the runner's Docker) |
| Frontend tests and build | `npm test -- --watch=false` and `npm run build` |
| Browser tests | `npm run e2e`, on Chromium. When it fails, the Playwright report and traces are attached to the run as `playwright-report`. |

When all three pass on `main`, both Docker images are published to GitHub's container
registry, tagged with the commit id and `latest`:

- `ghcr.io/xenonreaction/progression-tracker-backend`
- `ghcr.io/xenonreaction/progression-tracker-frontend`

The latest 10 versions of each are kept. The repository and its images are private.
