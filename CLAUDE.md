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

cd backend
./mvnw spring-boot:run                                   # run the API on :8080 against the compose DB
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev    # same, plus hand-written seed data (seed/DevDataSeeder)
./mvnw test                                              # all tests (Testcontainers starts its own Postgres)
./mvnw test -Dtest=ProgressionTrackerBackendApplicationTests            # a single test class
./mvnw test -Dtest=ProgressionTrackerBackendApplicationTests#contextLoads  # a single test method
./mvnw package                                           # build the jar into backend/target/
```

There's no linter or formatter configured yet.

## Layout

- `backend/`: Spring Boot 4.1 on Java 21, built with Maven (wrapper included). Base package is `com.progressiontracker`. Maven covers only the backend.
- `frontend/`: the Angular app, which doesn't exist yet. The plan adds it in Phase 3. It will use npm and the Angular CLI with plain Angular (no component library) and Jasmine/Karma tests, and will be kept separate from the Maven build.
- `plans/`: the project plan, plus `skill_tree_plans/*.md` as reference content.
- `data/trees/*.json`, `data/schema.json`: skill trees in the format of the older SkillTreeOSS app. They're reference material only. The app doesn't load them, and they don't map directly onto the new model. Phase 1 seed data is written by hand.

## Decided technical conventions

- **Persistence:** Spring Data JPA + Hibernate throughout, with no hand-written JDBC layer. Hibernate manages the schema through `spring.jpa.hibernate.ddl-auto=update`, so there are no Flyway or migration files. That is a local-only convenience to revisit before any deployment.
- **REST:** every endpoint is under `/api/v1/...`.
- **Tests:** unit tests use JUnit 5 + Mockito. Integration tests run against real Postgres through Testcontainers, never H2 or another in-memory database. Test classes that need the database import `TestcontainersConfiguration`, which uses a `@ServiceConnection` `postgres:18` container, the same image as `docker-compose.yml`. `TestProgressionTrackerBackendApplication` runs the app against a throwaway container instead of the compose database.
- **Logging:** Spring Boot's default SLF4J + Logback, left unconfigured for now.
- `spring.jpa.open-in-view=false`: all database access belongs in the service layer.
- **Schema constraints in the mappings:** ranges (readiness and thresholds, 0–100), unique pairs and the no-self-edge rule are declared as named `@CheckConstraint`/`@UniqueConstraint`s on the entities, and tests assert on those names. Cascades go through `@OnDelete(CASCADE)` on foreign keys (tree → tree_nodes → prerequisites). Deleting a library `Node` that a tree still uses is blocked by the database, and Phase 2 decides how to handle it.
- **Naming:** a field ending in a single capital letter (e.g. `positionX`) doesn't get an underscore from Spring's naming strategy, so name those columns explicitly. `ddl-auto=update` never renames or drops columns, so after a mapping change run `docker compose down -v` to reset the local database.
- **Enums:** store them through an `AttributeConverter` (see `ReadinessSourceTypeConverter`), not `@Enumerated(STRING)`. Hibernate generates a check constraint listing the allowed values for `@Enumerated` columns, and `ddl-auto=update` never widens it.

## Domain model (from the plan)

- The schema supports multiple users from the start: data is owned by a `user_id`. The v1 experience is still single-user.
- **Node:** lives in a per-user library, independent of any tree. It has a title, description, external links, a manual readiness value (integer 0–100) and `readiness_source_type` (only `'manual'` in Milestone 1, a hook with no logic behind it yet). Node detail views show both its prerequisites and the nodes that depend on it.
- **Tree:** an arrangement of library nodes, with category, tags and description. The same node can appear in several trees.
- **TreeNode:** the tree-to-node association. It holds the x/y position and the node's two readiness thresholds for that tree: `aggregateThreshold` (default 80, the minimum average of its prerequisites) and `individualThreshold` (default 70, the minimum for each prerequisite). The thresholds are per node, not per edge; the user decided this in Phase 1. A node shows as "ready" only when both are met.
- **Prerequisite:** a directed edge (`prerequisite` → `dependent`) between two TreeNodes in the same tree. The database blocks self-edges and duplicate edges. The same-tree rule and cycle prevention are Phase 2 service logic, and the client checks for cycles too.
