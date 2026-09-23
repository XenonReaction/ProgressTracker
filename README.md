# Progression Tracker

An app for building skill trees and tracking how ready you are on each skill.
Version 1 is a single-user proof of concept: create nodes (skills), arrange them
into trees with prerequisite edges, and record a readiness value for each node.

## Repository layout

| Path        | Contents                                                           |
|-------------|--------------------------------------------------------------------|
| `backend/`  | Spring Boot REST API (Java 21, Maven, Spring Data JPA, PostgreSQL) |
| `frontend/` | Angular app (added in a later phase)                               |
| `plans/`    | Project plan and reference skill-tree write-ups                    |
| `data/`     | Reference skill-tree JSON files from the older app (not loaded at runtime) |

## Prerequisites

- Java 21
- Docker (for the local database and for integration tests)

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

Hibernate creates and updates the tables from the entity classes on startup
(`ddl-auto=update`). It adds new tables and columns but never renames or drops
them, so after renaming a field, reset the local database with
`docker compose down -v`.

Stop the database with `docker compose down`. The data is kept in a Docker
volume; add `-v` to delete it too.

## Tests

```bash
cd backend
./mvnw test
```

Integration tests use [Testcontainers](https://testcontainers.com/) to start a
throwaway PostgreSQL container, so Docker must be running. They do not touch the
database started by `docker compose`.
