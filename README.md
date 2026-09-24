# Progression Tracker

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

A database created before Flyway was added (Phase 5.1) is recognised as already having
`V1`'s tables, and only the later migrations are run on it.

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
