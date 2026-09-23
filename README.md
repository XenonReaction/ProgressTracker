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

## Prerequisites

- Java 21
- Docker (for the local database and for integration tests)
- Node.js 22+ and npm (for the frontend)

Maven does not need to be installed; use the included wrapper, `./mvnw`.

## Running locally

```bash
# 1. Start PostgreSQL
docker compose up -d

# 2. Start the backend (http://localhost:8080)
cd backend
./mvnw spring-boot:run

# Or start it with sample data (a demo user, 7 nodes, 2 trees)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Then, in another terminal, start the frontend (http://localhost:4200):

```bash
cd frontend
npm install   # first time only
npm start
```

The Angular dev server forwards `/api` requests to the backend on port 8080.

Hibernate creates and updates the tables from the entity classes on startup
(`ddl-auto=update`). It adds new tables and columns but never renames or drops
them, so after renaming a field, reset the local database with
`docker compose down -v`.

Stop the database with `docker compose down`. The data is kept in a Docker
volume; add `-v` to delete it too.

## API

All endpoints are under `/api/v1` and exchange JSON. There's no login yet: every
request acts as a single default user (`demo`). Errors are returned as
[RFC 9457](https://www.rfc-editor.org/rfc/rfc9457) problem responses
(`application/problem+json`).

| Method & path | Purpose |
|---|---|
| `GET/POST /nodes`, `GET/PUT/DELETE /nodes/{id}` | Node library. Deleting a node that a tree still uses returns 409 and lists those trees. |
| `GET/POST /trees`, `GET/PUT/DELETE /trees/{id}` | Tree metadata. Deleting a tree also removes its placements and edges, but not library nodes. |
| `GET/POST /trees/{treeId}/nodes`, `GET/PUT/DELETE /trees/{treeId}/nodes/{treeNodeId}` | Library nodes placed in a tree, with position and readiness thresholds. |
| `PUT /trees/{treeId}/nodes/positions` | Moves many tree nodes in one transaction (used by auto-layout). An unknown id changes nothing. |
| `GET/POST /trees/{treeId}/prerequisites`, `DELETE /trees/{treeId}/prerequisites/{id}` | Prerequisite edges. Self-edges (400), duplicates (409) and cycles (409) are refused. |

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
