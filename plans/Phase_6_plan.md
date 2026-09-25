# Phase 6 Plan — CI and logging

**Status:** Approved and being built. Every question is answered and recorded as **Decided**. Deployment (the old 6.3) has moved to "Deployment — to revisit" at the end, with its open questions kept for when it's picked up.

**Where each sub-phase stands:**

| Sub-phase | Topic | State |
|---|---|---|
| 6.0 | GitHub Actions CI | **Built**, awaiting review. |
| 6.1 | Configuration from outside and health checks | **Fully decided.** |
| 6.2 | Logging review | **Fully decided.** |
| ~~6.3~~ | ~~Deployment~~ | **Moved to "Deployment — to revisit"** (6.3-Q1). |

**Order (decided, O-Q1):** 6.0 → 6.1 → 6.2. CI comes first, so every later step is tested on each push.

**What changed in round 2:**
- **GitHub with GitHub Actions**, and the repository is already set up: `github.com/XenonReaction/ProgressTracker`, private, with `main` tracking it.
- **Deployment is skipped for now** and moved to the end of this plan, so the title is now "CI and logging". The parts of 6.1 and 6.2 that only matter once deployed are marked as waiting for it. The configuration and logging changes themselves are still built, so the images are ready to deploy later.
- Every other question used the recommendation.

**Where this comes from:**
- The main plan's **Hosting** goal, and its **Logging** section, which deferred "what to log and why" until there was a running backend.
- The Milestone 3 list in `Phase_5_plan.md`: "Deployment and CI" and "Logging review".
- Your decision (`Phase_7_plan.md`, round 3, O-Q1) to do this before the learning activities.

---

## What exists today

- **Code host:** GitHub, private. (Before 6.0 it had no workflows.)
- **Containers (4b):** two-stage Dockerfiles, and a Compose file whose `app` profile runs the database, backend and frontend together. The images are built locally only.
- **Tests:** `./mvnw test` (backend, with Testcontainers), `npm test` (frontend unit tests) and `npm run e2e` (Playwright, against a throwaway Docker copy of the app). All three need Docker, which GitHub's hosted Ubuntu runners have.
- **Configuration:** the database address, username and password are written into `application.properties` and `docker-compose.yml`, marked "local-only". Flyway's `baseline-on-migrate` is on.
- **No login:** every request acts as one default user until Phase 8. This is why deployment needs the "who can reach it" decision before it happens.
- **Logging:** Spring Boot's default console output, with three places that log something (a database conflict, the dev seed, and a readiness loop guard).

---

## 6.0 — GitHub Actions CI

**Decided:**
- **GitHub with GitHub Actions**, in a **private** repository (Q1). Free for this project: private repositories get about 2,000 Actions minutes a month, and one run should take roughly 5–10 minutes.
- **Everything runs on every push** (Q2): backend tests, frontend unit tests and production build, and the Playwright browser tests.
- **Changes are pushed straight to `main`** for now (Q3). CI runs after each push and flags a failure. Branches and pull requests can come later.
- **Both Docker images are published from every green build on `main`** (Q4), to GitHub's container registry (GHCR), tagged with the commit id plus a moving `latest` tag. Build once, and later deploy exactly what was tested.

**Scope:**
- A workflow in `.github/workflows/ci.yml`, triggered on every push and by hand:
  - **backend:** Java 21, `./mvnw test`, with Maven's downloads cached between runs;
  - **frontend:** Node 22, `npm ci`, `npm test -- --watch=false` and `npm run build`, with npm's downloads cached;
  - **browser tests:** install Chromium, then `npm run e2e`. The Playwright report is uploaded when a test fails, so its trace can be opened from the run page;
  - **images:** only on `main`, and only when the jobs above pass: build both images and push them to `ghcr.io/xenonreaction/…`, using the token GitHub provides to each run (no secrets to set up).
- **Image clean-up** *(a choice made while drafting, easy to change)*: keep the latest 10 versions of each image, so the private registry stays within GitHub's free storage.
- A CI status badge in the README, and the workflow documented in the README and CLAUDE.md.

**Done when:** a push runs every test on GitHub, a failure is shown on the commit, and a green build on `main` publishes both images with the commit id as their tag.

---

## 6.1 — Configuration from outside and health checks

**Goal:** the same images run anywhere, with whatever differs (database address, passwords, settings) supplied from outside, and the app reports whether it's healthy.

**Decided:**
- **`baseline-on-migrate` is turned off everywhere** (Q1). An unexpected database is refused instead of silently adopted. Your local database already has Flyway's history table, so nothing changes for it.
- **Production gets its own strong database password** (Q2), and the local `progressiontracker` login stays for development only. *This waits for deployment:* there's no production database yet.

**Scope:**
- The database address, username and password can all come from environment variables (`SPRING_DATASOURCE_*`). The local defaults stay, so `./mvnw spring-boot:run`, `docker compose up` and the browser tests work as they do now.
- A `prod` Spring profile for production-only settings: JSON logs (6.2), and the dev seed can never run with it. It's ready for deployment and checked by a test, but nothing runs with it yet.
- **Health checks:** Spring Boot Actuator's health endpoint (`/actuator/health`), reporting the app and its database. Only health is exposed, nothing else.
  - The `backend` service in `docker-compose.yml` and the browser tests' stack use it as a Docker health check, so the frontend waits until the backend is really up. The browser tests' start-up can then wait on it instead of polling an API route.
- Documented in the README ("Configuration") and CLAUDE.md.

**Done when:** nothing environment-specific is only settable in code, `/actuator/health` reports the app and database as up, and Docker waits for it.

---

## 6.2 — Logging review

**Goal:** decide what's worth logging and why, as the main plan asked, and set it up so it's useful when you can't watch the console.

**Decided:**
- **What's logged** (Q1):
  - **Every API request, one line each:** method, path, status and how long it took, at INFO.
  - **Unexpected errors (500s):** the full stack trace, at ERROR.
  - **Important changes, one line each, at INFO:** trees and nodes deleted, edit sessions started, finished and discarded, and links refused because of a loop.
  - **Expected refusals** (validation errors, 404s, 409s): DEBUG only, since they're normal.
  - **Never logged:** the text of nodes or trees, or any passwords or secrets.
  - A **request id** on every line, so all the lines from one request can be found together. It's also returned in a response header, so a problem seen in the browser can be matched to its log lines.
- **Plain text locally, JSON with the `prod` profile** (Q2), using Spring Boot's built-in ECS format, which suits the ELK stack if it's added later.
- **Logs go to the container's standard output**, kept by Docker with a size limit (Q3), so `docker logs` shows them.

**Scope:**
- A request-logging filter that sets the request id and writes the one-line summary.
- The chosen INFO events in the services that make those changes.
- An ERROR handler for anything unexpected, returning a plain 500 problem response without internal details.
- Size-limited Docker logging for the `app` profile in `docker-compose.yml`.
- Tests: a request produces its log line with a request id, and an unexpected error is logged at ERROR.
- Documented in CLAUDE.md, replacing the "Logging: default, unconfigured" convention.

**Done when:** the chosen events are logged in the chosen format, and the tests check the request line and error logging.

---

## Deployment — to revisit

*The old 6.3, moved here by your answer to 6.3-Q1 ("skip deployment and move that to future plans to revisit").*

When it's picked up, it starts from what 6.0–6.2 leave ready: tested images in GHCR tagged by commit, configuration from environment variables, a `prod` profile, a health check and JSON logs.

**Questions to answer then** (kept from round 1, with their recommendations):
- **Where it runs.** Recommended: one small cloud server, such as an AWS EC2 instance (the `aws-fundamentals-ec2-s3-ebs` tree), running the existing Docker Compose setup. Alternatives: a managed container service, Kubernetes, or a machine at home.
- **Who can reach it: important while there's no login.** Until Phase 8, anyone who reaches the app can read and change everything. Recommended: only you, over a private network such as Tailscale. Alternatives: public behind a password, or bringing Phase 8's logins forward.
- **How a deployment happens.** Recommended: a manual button in GitHub Actions that deploys a chosen tested version and checks `/actuator/health`.
- **The database and backups.** Recommended: PostgreSQL in a container on the server, with a nightly `pg_dump` kept off the server.
- **Domain name and HTTPS.** Not needed on a private network; otherwise a domain and a free Let's Encrypt certificate.

---

## Future ideas to revisit

Not scheduled; kept so they aren't lost.

- **Deployment** (above).
- **A central log stack** (ELK: Elasticsearch, Logstash, Kibana), from the `elk-elastic-stack` tree, if container logs aren't enough.
- **Metrics and dashboards:** Actuator metrics with Prometheus and Grafana, to watch request times and errors over time.
- **Branches and pull requests**, with CI required to pass before merging (6.0-Q3), if working straight on `main` stops suiting.
- **A staging environment**, and **automatic deployment** on every green push, once deployment exists.
- **Kubernetes**, from the `kubernetes-fullstack` tree, if the app grows or as a learning exercise.
- **Automatic dependency updates** (for example Dependabot), which CI then tests.
