# Progression Tracker

[![CI](https://github.com/XenonReaction/ProgressTracker/actions/workflows/ci.yml/badge.svg)](https://github.com/XenonReaction/ProgressTracker/actions/workflows/ci.yml)

An app for building skill trees and tracking how ready you are on each skill.
Version 1 is a single-user proof of concept: create nodes (skills), arrange them
into trees with prerequisite edges, and record a readiness value for each node.

## Repository layout

| Path        | Contents                                                           |
|-------------|--------------------------------------------------------------------|
| `backend/`  | Spring Boot REST API (Java 21, Maven, Spring Data JPA, PostgreSQL) |
| `frontend/` | Angular 22 app (plain Angular, Vitest)                             |
| `plans/`    | Project plan and reference skill-tree write-ups                    |
| `data/`     | Reference skill-tree JSON files from the older app (not loaded at runtime) |

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
- Docker (for the local database and for integration tests)
- Node.js 22+ and npm (for the frontend)

Maven does not need to be installed; use the included wrapper, `./mvnw`.

## Running locally for development

```bash
# 1. Start PostgreSQL
docker compose up -d

# 2. Start the backend (http://localhost:8080)
cd backend
./mvnw spring-boot:run

# Or start it with sample data (a demo user, 10 nodes, 3 trees, one node linked to a tree)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Then, in another terminal, start the frontend (http://localhost:4200):

```bash
cd frontend
npm install   # first time only
npm start
```

The Angular dev server forwards `/api` requests to the backend on port 8080.

### Database migrations

The schema is created and changed by [Flyway](https://documentation.red-gate.com/fd)
migrations: numbered SQL files in `backend/src/main/resources/db/migration/`. When the
backend starts (locally, in Docker or in tests), Flyway runs any the database hasn't had
yet, in order, and records them in its `flyway_schema_history` table. Hibernate then
checks that the entity classes match the schema, and the backend refuses to start if they
don't.

To change the schema:

1. Add the next file, for example `V3__add_linked_tree_to_nodes.sql`: two underscores
   after the version, then what it does.
2. Update the entity classes to match.
3. Restart the backend. `./mvnw test` also runs every migration on a fresh database.

Never edit a migration that has already run: Flyway notices the change and refuses to
start. Write a new migration instead.

A database that has tables but no Flyway history is refused rather than adopted. The one
local database that predated Flyway was brought under it in Phase 5.1.

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
unexpected errors in full and a few important changes, such as a tree being deleted. It
never logs the text of your trees and nodes. Each request gets an id, shown on every line
logged while handling it and returned in the `X-Request-Id` response header, so a problem
seen in the browser can be matched to its log lines.

Logs are plain text locally, and JSON (the ECS format) with the `prod` profile. In Docker,
`docker logs progression-tracker-backend` shows them; each container keeps at most three
10 MB log files.

### Health check

`GET /actuator/health` on the backend (port 8080) reports `UP` or `DOWN`, with the state of
each check, such as `db`, but none of their details. Nothing else under `/actuator` is
exposed. Docker Compose uses it: the frontend container starts once the backend reports
`UP`.

Stop the database with `docker compose down`. The data is kept in a Docker
volume; add `-v` to delete it too.

## API

All endpoints are under `/api/v1` and exchange JSON. There's no login yet: every
request acts as a single default user (`demo`). Errors are returned as
[RFC 9457](https://www.rfc-editor.org/rfc/rfc9457) problem responses
(`application/problem+json`).

| Method & path | Purpose |
|---|---|
| `GET/POST /nodes`, `GET/PUT/DELETE /nodes/{id}` | Node library. Deleting a node that a tree still uses returns 409 and lists those trees. Setting `linkedTreeId` makes the node take its readiness from that tree (see below). |
| `GET/POST /trees`, `GET/PUT/DELETE /trees/{id}` | Tree metadata. Deleting a tree also removes its placements and edges, but not library nodes. A tree that nodes are linked to can't be deleted (409, listing those nodes). |
| `GET/POST /trees/{treeId}/nodes`, `GET/PUT/DELETE /trees/{treeId}/nodes/{treeNodeId}` | Library nodes placed in a tree, with position and readiness thresholds. |
| `PUT /trees/{treeId}/nodes/positions` | Moves many tree nodes in one transaction (used by auto-layout). An unknown id changes nothing. |
| `PUT /nodes/{id}/readiness` | Sets only the hand-entered readiness (the view pages use it). Refused with 409 for a node linked to a tree. |
| `GET /nodes/{id}/trees` | The trees a node is placed in. |
| `POST/DELETE /trees/{treeId}/edit-session`, `POST /trees/{treeId}/edit-session/discard` | Edit mode: `POST` saves a restore point (409 if the tree is already being edited), `DELETE` is "Done" and keeps the changes, and `discard` puts the tree back as it was. A tree's `editSessionStartedAt` is set while a session is open. |
| `GET/POST /trees/{treeId}/prerequisites`, `DELETE /trees/{treeId}/prerequisites/{id}` | Prerequisite edges. Self-edges (400), duplicates (409) and cycles (409) are refused. |
| `PUT /trees/{treeId}/prerequisites/{id}/route`, `DELETE /trees/{treeId}/prerequisites/routes` | An edge's hand-adjusted right-angle route (`{"segments": 3, "offsets": [40]}`, or null for the default), and resetting every route in a tree. |

**Linked trees.** A node's `readiness` in every response is its effective value. With
`linkedTreeId` set, that's the average readiness of the linked tree's nodes, rounded to a
whole percent (0 for an empty tree), and a linked node inside that tree counts with its own
derived value. The hand-entered value is still sent as `readiness` in requests, returned as
`manualReadiness`, and used again when the node is unlinked. A link that would make a
tree's readiness depend on itself is refused with 409, whether it comes from linking a node
or placing a linked node in a tree.

Tree node ids and library node ids are different: `/trees/{treeId}/nodes/{treeNodeId}`
and prerequisite edges use tree node ids, and each tree node response includes the
library `nodeId`.

## Tests

```bash
cd backend
./mvnw test
```

Integration tests use [Testcontainers](https://testcontainers.com/) to start a
throwaway PostgreSQL container, so Docker must be running. They do not touch the
database started by `docker compose`.

```bash
cd frontend
npm test -- --watch=false   # Vitest specs, run once in a simulated browser (jsdom)
```

### Browser tests

The browser tests drive a real Chromium with [Playwright](https://playwright.dev/): they
click, type and drag like a person and check what appears on screen. They run against a
throwaway copy of the app, never your own data.

```bash
cd frontend
npx playwright install chromium   # first time only (~150 MB)
npm run e2e                       # all browser tests
npm run e2e -- e2e/trees.spec.ts  # one file
npm run e2e -- --ui               # Playwright's interactive runner
```

`npm run e2e` builds the Docker images, starts a separate stack
(`frontend/e2e/docker-compose.yml`, with the sample data in an in-memory database) on
http://localhost:8090, runs the tests, and removes the stack. Docker must be running, and
your own database and containers are left alone. Set `E2E_PORT` to use another port, or
`E2E_KEEP_STACK=1` to leave the stack running afterwards for a look around.

When a test fails, `npx playwright show-report` opens the results, including a
step-by-step trace of the failing test.

## Continuous integration

GitHub Actions (`.github/workflows/ci.yml`) runs on every push, and can be started by hand
from the repository's **Actions** tab. Three jobs run side by side:

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
