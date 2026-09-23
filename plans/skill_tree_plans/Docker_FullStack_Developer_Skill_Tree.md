# Docker Skill Tree — Full-Stack Developer Path

> **Goal:** Progress from Linux/container fundamentals to independently containerizing, running, debugging, and composing full-stack applications with Docker.
>
> **Learning context:** Docker is the containerization/infrastructure branch of the full-stack path. It connects local development to reproducible environments, CI/CD, cloud deployment, and later container orchestration.
>
> **Target level:** Professional full-stack developer — not container-runtime engineer, Linux-kernel specialist, or Kubernetes platform engineer.
>
> **Learning progression:**  
> `Ubuntu/WSL2 + Linux CLI + Networking Basics → Containers → Docker → Dockerfiles → Networking/Storage → Docker Compose → Full-Stack Containerization → CI/CD/Cloud/Kubernetes Readiness`
>
> **Scope boundary:** Kubernetes, Jenkins/CI/CD, AWS/cloud infrastructure, and advanced orchestration should have their own later skill trees. They are introduced here only where Docker knowledge becomes a prerequisite.

---

# Skill Tree Overview

```text
Docker
├── 1. Prerequisites
├── 2. Containers & Virtualization Fundamentals
├── 3. Docker Architecture
├── 4. Installation & WSL2 Integration
├── 5. Docker CLI Fundamentals
├── 6. Images
├── 7. Containers
├── 8. Image Layers & Build Cache
├── 9. Dockerfile Fundamentals
├── 10. Dockerfile Instructions
├── 11. CMD vs ENTRYPOINT
├── 12. Build Context & .dockerignore
├── 13. Environment Variables & Configuration
├── 14. Ports & Publishing
├── 15. Docker Networking
├── 16. DNS & Service Communication
├── 17. Volumes
├── 18. Bind Mounts
├── 19. Persistence
├── 20. Docker Compose Fundamentals
├── 21. Compose Services
├── 22. Compose Networks & Volumes
├── 23. Compose Configuration
├── 24. Health Checks
├── 25. Dependencies & Startup Ordering
├── 26. Multi-Stage Builds
├── 27. Java / Spring Boot Containers
├── 28. Angular / React Containers
├── 29. Database Containers
├── 30. Full-Stack Compose Architecture
├── 31. Container Security
├── 32. Resource Management
├── 33. Logging & Observability
├── 34. Debugging
├── 35. Image Registries
├── 36. Tags & Versioning
├── 37. Production Image Practices
├── 38. CI/CD Readiness
├── 39. Cloud Deployment Readiness
├── 40. Kubernetes Readiness
└── 41. Professional Docker Workflow
```

# Dependency Map

```text
Ubuntu / WSL2
      │
      ├── Linux CLI / filesystem / processes
      │
      └── Networking fundamentals
                  │
                  ▼
             Containers
                  │
                  ▼
               Docker
        ┌─────────┼─────────┐
        ▼         ▼         ▼
      Images   Containers  Storage
        │         │         │
        └─────────┼─────────┘
                  ▼
              Networking
                  │
                  ▼
            Docker Compose
                  │
                  ▼
         Multi-Container Apps
                  │
        ┌─────────┼──────────┐
        ▼         ▼          ▼
      CI/CD     Cloud    Kubernetes
    (later)    (later)     (later)
```

# Mastery Progression

```text
"I can run this Dockerfile"
             │
             ▼
"I understand this Dockerfile"
             │
             ▼
"I can modify this Dockerfile"
             │
             ▼
"I can write one from requirements"
             │
             ▼
"I can containerize an unfamiliar application"
             │
             ▼
"I can design and debug its multi-container environment"
```

---

# Tier 0 — Prerequisites

## 1. Linux / WSL2 Prerequisites

- [ ] Navigate directories with `cd`
- [ ] Inspect files with `ls`
- [ ] Understand absolute vs relative paths
- [ ] Create/remove/copy/move files and directories
- [ ] Read and edit text configuration files
- [ ] Understand environment variables
- [ ] Understand processes at a practical level
- [ ] Understand permissions at a practical level
- [ ] Understand shell commands and command arguments
- [ ] Understand current working directory
- [ ] Understand ports and localhost
- [ ] Understand basic client/server communication
- [ ] Understand WSL2's role on Windows if using Docker Desktop + WSL2

## 2. Application Prerequisites

Before containerizing an application, be able to run it **without Docker**.

Examples:

```text
Java / Spring Boot
    mvn spring-boot:run
    java -jar app.jar

Angular
    npm install
    ng serve / npm start

React
    npm install
    npm run dev

PostgreSQL
    understand host / port / database / credentials
```

- [ ] Know what command starts the application
- [ ] Know what dependencies it needs
- [ ] Know what port it listens on
- [ ] Know what configuration it requires
- [ ] Know which files must exist at runtime

**Checkpoint:** Explain how one of your applications runs locally before attempting to containerize it.

---

# Tier 1 — Containers & Virtualization

## 3. Why Containers Exist

Traditional application setup often requires:

```text
Machine
├── operating system
├── language/runtime
├── libraries
├── dependencies
├── application
└── configuration
```

Different machines can produce different environments.

Containers package the application and much of its required runtime environment into a reproducible unit.

- [ ] Explain environment inconsistency
- [ ] Explain dependency/version conflicts
- [ ] Explain reproducibility
- [ ] Explain isolation
- [ ] Explain portability at a practical level
- [ ] Understand that containers do not remove all host differences

## 4. Container vs Virtual Machine

Conceptual comparison:

```text
Virtual Machines

Hardware
   │
Host OS
   │
Hypervisor
   ├── Guest OS ── App
   └── Guest OS ── App
```

```text
Containers

Hardware
   │
Host OS / Kernel
   │
Container Runtime
   ├── Container ── App
   └── Container ── App
```

- [ ] Containers share host-kernel capabilities
- [ ] VMs normally contain a complete guest OS
- [ ] Containers are typically lighter weight
- [ ] Containers still provide process/filesystem/network isolation
- [ ] Containers are not simply "tiny virtual machines"

## 5. Image vs Container

Critical distinction:

```text
Image
  │
  │ create/run
  ▼
Container
```

Think:

```text
Image       → packaged template
Container   → running/stopped instance of that image
Dockerfile  → instructions used to build an image
```

**Checkpoint:** Explain Dockerfile vs image vs container without using the terms interchangeably.

---

# Tier 2 — Docker Architecture

## 6. Docker Components

Practical model:

```text
docker command
     │
     ▼
Docker CLI
     │
     ▼
Docker Engine / daemon
     │
     ├── images
     ├── containers
     ├── networks
     └── volumes
```

- [ ] Docker CLI
- [ ] Docker Engine
- [ ] Docker daemon concept
- [ ] Docker API awareness
- [ ] Images
- [ ] Containers
- [ ] Networks
- [ ] Volumes
- [ ] Registries

## 7. Docker Desktop Awareness

For Windows/WSL2 development:

```text
Windows
   │
Docker Desktop
   │
WSL2 integration
   │
Ubuntu / WSL2 terminal
   │
docker CLI
```

- [ ] Understand Docker Desktop's role
- [ ] Understand WSL2 integration
- [ ] Recognize when Docker Desktop is not running
- [ ] Recognize when a WSL distribution lacks integration
- [ ] Distinguish Docker CLI availability from Docker daemon availability

---

# Tier 3 — Installation & Verification

## 8. Verify Docker

Commands to know:

```bash
docker --version
docker version
docker info
```

- [ ] Verify CLI installation
- [ ] Verify engine connection
- [ ] Read basic engine information
- [ ] Distinguish command-not-found from daemon-not-running errors

## 9. First Container

Typical introductory workflow:

```bash
docker run hello-world
```

Understand the conceptual sequence:

```text
docker run
   │
   ├── image exists locally?
   │        │
   │        └── no → pull from registry
   │
   ├── create container
   │
   └── start container
```

When you run `docker pull postgres:16` or `docker run hello-world` without specifying a registry, Docker resolves the image against **Docker Hub** (`hub.docker.com`), Docker's default public registry — this is why image names like `postgres:16` or `nginx:latest` just work without a hostname prefix.

**Checkpoint:** Run a container and explain where its image came from and what happened when the command executed.

---

# Tier 4 — Docker CLI Fundamentals

## 10. Discovery Commands

Memorize practical use of:

```bash
docker --help
docker <command> --help
```

Do not treat guides as the only way to discover commands.

## 11. Core Container Commands

```bash
docker run
docker ps
docker ps -a
docker stop
docker start
docker restart
docker rm
docker logs
docker exec
docker inspect
```

- [ ] List running containers
- [ ] List stopped containers
- [ ] Start/stop/restart
- [ ] Remove containers
- [ ] Read logs
- [ ] Execute commands inside running containers
- [ ] Inspect configuration/state

## 12. Core Image Commands

```bash
docker images
docker pull
docker build
docker image inspect
docker rmi
```

- [ ] List images
- [ ] Pull images
- [ ] Build images
- [ ] Inspect images
- [ ] Remove unused images intentionally

**Checkpoint:** Manage the lifecycle of a container entirely from the CLI.

---

# Tier 5 — Images

## 13. Image Fundamentals

- [ ] Image as immutable packaged filesystem/configuration
- [ ] Image name
- [ ] Repository
- [ ] Tag
- [ ] Image ID
- [ ] Layers
- [ ] Base images
- [ ] Image metadata
- [ ] Local image cache

Example:

```text
postgres:16

postgres → repository/name
16       → tag
```

## 14. Pulling Images

```bash
docker pull postgres:16
```

- [ ] Pull explicit tags
- [ ] Understand local caching
- [ ] Avoid assuming `latest` means newest or production-safe
- [ ] Understand registry source awareness

## 15. Inspecting Images

```bash
docker image inspect <image>
```

Learn to inspect:

- [ ] Entrypoint
- [ ] Command
- [ ] Environment
- [ ] Exposed ports
- [ ] Architecture/platform awareness
- [ ] Labels/metadata

---

# Tier 6 — Containers

## 16. Container Lifecycle

```text
create
  │
  ▼
running
  │
  ├── stop ──► stopped
  │              │
  │              └── start ──► running
  │
  └── exit ──► stopped
                 │
                 ▼
               remove
```

- [ ] Created
- [ ] Running
- [ ] Stopped/exited
- [ ] Removed
- [ ] Understand that stopping does not remove a container
- [ ] Understand that removing a container does not necessarily remove its image or named volumes

## 17. `docker run`

Important options:

```text
--name
-d
-p
-e
--env-file
-v / --mount
--network
--rm
```

Example:

```bash
docker run --name example -d image-name
```

- [ ] Detached mode
- [ ] Container naming
- [ ] Port publishing
- [ ] Environment variables
- [ ] Mounts
- [ ] Networks
- [ ] Automatic removal

## 18. `docker exec`

```bash
docker exec -it <container> sh
```

or, where available:

```bash
docker exec -it <container> bash
```

- [ ] Execute one command
- [ ] Open an interactive shell
- [ ] Inspect files/processes/configuration
- [ ] Understand that minimal images may not contain Bash or common debugging tools

---

# Tier 7 — Layers & Build Cache

## 19. Image Layers

Conceptually:

```text
Base image
   │
   ▼
Layer: install dependencies
   │
   ▼
Layer: copy files
   │
   ▼
Layer: build application
   │
   ▼
Final image
```

- [ ] Dockerfile instructions can create filesystem layers
- [ ] Layers can be reused
- [ ] Layers affect image size
- [ ] Layer ordering affects rebuild efficiency
- [ ] Images share reusable content

## 20. Build Cache

```text
unchanged instruction/input
          │
          ▼
reuse cached result

changed instruction/input
          │
          ▼
rebuild that step and affected later steps
```

- [ ] Understand cache reuse conceptually
- [ ] Order Dockerfile instructions intentionally
- [ ] Copy dependency manifests before frequently changing source when useful
- [ ] Understand cache invalidation
- [ ] Know how to force/review clean builds when debugging

**Checkpoint:** Explain why changing one source file can rebuild some layers but not others.

---

# Tier 8 — Dockerfiles

## 21. What a Dockerfile Is

A Dockerfile is a text file containing instructions for building an image.

Typical flow:

```text
Dockerfile
    │
    ▼
docker build
    │
    ▼
Image
    │
    ▼
docker run
    │
    ▼
Container
```

File name:

```text
Dockerfile
```

Normally it has **no file extension**.

## 22. Minimal Example

```dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Be able to explain every line.

---

# Tier 9 — Dockerfile Instructions

## 23. `FROM`

```dockerfile
FROM eclipse-temurin:21-jre
```

- [ ] Select base image
- [ ] Understand image tags
- [ ] Prefer appropriate minimal/supported runtime images
- [ ] Understand base-image trust/update considerations

## 24. `WORKDIR`

```dockerfile
WORKDIR /app
```

- [ ] Set working directory
- [ ] Understand how later relative paths use it
- [ ] Prefer it over repeated shell `cd` operations

## 25. `COPY`

```dockerfile
COPY target/app.jar app.jar
```

- [ ] Source is from build context
- [ ] Destination is inside image
- [ ] Copy files intentionally
- [ ] Understand why build context matters

## 26. `RUN`

```dockerfile
RUN mvn package
```

- [ ] Runs during **image build**
- [ ] Used to install/build/transform image content
- [ ] Not the same as commands executed when container starts

## 27. `ENV`

```dockerfile
ENV APP_MODE=production
```

- [ ] Define image/runtime defaults
- [ ] Runtime values can often override defaults
- [ ] Do not bake secrets into images

## 28. `ARG`

```dockerfile
ARG BUILD_VERSION
```

- [ ] Build-time variables
- [ ] Distinguish from runtime `ENV`
- [ ] Understand that build arguments are not a safe secret store

## 29. `EXPOSE`

```dockerfile
EXPOSE 8080
```

- [ ] Documents/intends container listening port
- [ ] Does **not** automatically publish that port to the host
- [ ] Distinguish from `docker run -p`

## 30. `USER`

- [ ] Run processes as non-root where practical
- [ ] Understand filesystem permission implications
- [ ] Production security relevance

## 31. Other Instructions — Awareness

- [ ] `LABEL`
- [ ] `VOLUME`
- [ ] `HEALTHCHECK`
- [ ] `SHELL`
- [ ] `ADD` awareness and why `COPY` is usually clearer for ordinary file copying

---

# Tier 10 — `CMD` vs `ENTRYPOINT`

## 32. `CMD`

Example:

```dockerfile
CMD ["java", "-jar", "app.jar"]
```

- [ ] Provides default command/arguments
- [ ] Can be overridden by runtime command arguments
- [ ] Understand exec-form preference for many applications

## 33. `ENTRYPOINT`

```dockerfile
ENTRYPOINT ["java", "-jar", "app.jar"]
```

- [ ] Defines primary executable
- [ ] Runtime arguments can be appended depending on configuration
- [ ] Understand interaction with `CMD`

## 34. Shell Form vs Exec Form

Shell form:

```dockerfile
CMD java -jar app.jar
```

Exec form:

```dockerfile
CMD ["java", "-jar", "app.jar"]
```

- [ ] Understand process/signal-handling implications at practical level
- [ ] Prefer exec form for normal application processes unless shell behavior is intentionally needed

**Checkpoint:** Explain `RUN` vs `CMD` vs `ENTRYPOINT`.

---

# Tier 11 — Build Context & `.dockerignore`

## 35. Build Context

```bash
docker build -t my-app .
```

The final `.` is the build context.

- [ ] Understand context directory
- [ ] `COPY` can only use appropriate files from context
- [ ] Large contexts slow builds/transfers
- [ ] Avoid accidentally including secrets or unnecessary artifacts

## 36. `.dockerignore`

Example:

```text
.git
node_modules
target
.env
*.log
```

- [ ] Exclude unnecessary files
- [ ] Reduce context size
- [ ] Avoid copying development artifacts
- [ ] Avoid sending secrets into build context
- [ ] Understand that ignore patterns must match the project's build needs

---

# Tier 12 — Configuration

## 37. Environment Variables

Runtime example:

```bash
docker run -e APP_MODE=production my-app
```

- [ ] Pass environment variables
- [ ] Read environment variables inside application
- [ ] Override defaults
- [ ] Understand configuration separation

## 38. Environment Files

```bash
docker run --env-file .env my-app
```

- [ ] Understand `.env`-style configuration
- [ ] Do not commit real secrets
- [ ] Use example/template env files where useful
- [ ] Understand Docker/Compose interpolation syntax separately from Spring configuration placeholder syntax

## 39. Secrets Awareness

- [ ] Environment variables are configuration, not magical encryption
- [ ] Do not store secrets in Dockerfiles
- [ ] Do not commit secrets
- [ ] Secret-management systems belong in later production/deployment study
- [ ] Minimize secret exposure in logs and process inspection

---

# Tier 13 — Ports

## 40. Container Ports vs Host Ports

Example:

```bash
docker run -p 8081:8080 my-app
```

Mental model:

```text
Host                  Container
localhost:8081 ─────► port 8080
```

Syntax:

```text
HOST_PORT:CONTAINER_PORT
```

- [ ] Understand application listening port
- [ ] Understand host published port
- [ ] Understand port conflicts
- [ ] Understand multiple containers can use the same internal port while publishing different host ports

## 41. `EXPOSE` vs `-p`

```text
EXPOSE 8080
```

documents container intent.

```text
-p 8081:8080
```

publishes the port.

Do not confuse them.

**Checkpoint:** Run two containers using the same internal port but different host ports.

---

# Tier 14 — Docker Networking

## 42. Network Fundamentals

Understand:

```text
container
   │
network interface
   │
Docker network
   │
other containers / host / external network
```

- [ ] Container IP awareness
- [ ] Bridge networking
- [ ] User-defined networks
- [ ] Port publishing
- [ ] Network isolation
- [ ] Host networking awareness where supported
- [ ] Do not hard-code ephemeral container IP addresses for normal service discovery

## 43. Network Commands

```bash
docker network ls
docker network inspect <network>
docker network create <network>
docker network rm <network>
```

- [ ] List networks
- [ ] Inspect membership
- [ ] Create networks
- [ ] Attach containers appropriately
- [ ] Diagnose "network not found"

## 44. Default Networks

Understand that tools such as Compose can automatically create project networks.

Example concept:

```text
compose project
      │
      ▼
project_default network
      │
      ├── backend
      └── database
```

Do not assume a Compose-created network exists before the Compose project has created it.

---

# Tier 15 — DNS & Service Communication

## 45. Container-to-Container Communication

Within an appropriate user-defined/Compose network:

```text
backend
   │
   │ jdbc:postgresql://db:5432/guestbook
   ▼
db
```

The service/container DNS name can be used instead of `localhost`.

## 46. The `localhost` Rule

Inside a container:

```text
localhost
```

normally means **that container itself**.

This is one of the most important Docker networking concepts.

```text
Backend container
localhost:5432
      │
      └── asks backend container for port 5432
          NOT the database container
```

Instead:

```text
db:5432
```

when `db` is the reachable service name.

**Checkpoint:** Explain why a Spring Boot datasource URL changes from `localhost` outside Docker to a database service name inside a Compose network.

---

# Tier 16 — Volumes

## 47. Why Persistence Is Needed

Container writable state should not automatically be treated as durable application storage.

```text
Container
   │
   ├── temporary/container filesystem
   │
   └── mounted persistent storage
             │
             ▼
           Volume
```

## 48. Named Volumes

Example:

```bash
docker volume create app_data
```

Compose concept:

```yaml
volumes:
  app_data:
```

- [ ] Persistent Docker-managed storage
- [ ] Survives ordinary container replacement
- [ ] Useful for databases
- [ ] Inspect/list/remove volumes

Commands:

```bash
docker volume ls
docker volume inspect <volume>
docker volume rm <volume>
```

## 49. Volume Lifecycle

- [ ] Container removal vs volume removal
- [ ] Named volumes can outlive containers
- [ ] Understand data-loss risk when explicitly deleting volumes
- [ ] Know how Compose volume removal options affect persistence

---

# Tier 17 — Bind Mounts

## 50. Bind Mount Concept

```text
Host filesystem
      │
      ▼
Container path
```

Useful for:

- [ ] Development source mounting
- [ ] Configuration files
- [ ] Local files that need direct host visibility

## 51. Bind Mount vs Named Volume

```text
Bind mount
Host path is explicit
Good for development/shared files

Named volume
Docker manages storage location
Good for persistent application/database data
```

- [ ] Know when each is appropriate
- [ ] Understand permissions
- [ ] Understand host-platform differences
- [ ] Avoid accidentally hiding files already present in the image by mounting over their directory

---

# Tier 18 — Persistence Strategy

## 52. What Should Be Persistent?

Usually consider persistence for:

```text
database data
uploaded files
stateful service data
```

Usually avoid relying on container filesystem for:

```text
critical durable data
configuration that should be externally managed
source-of-truth application state
```

## 53. Stateless Application Containers

Prefer application containers that can often be:

```text
stop
remove
recreate
```

without losing critical state.

This becomes important for later cloud/orchestration work.

**Checkpoint:** Recreate an application container while preserving database data through a volume.

---

# Tier 19 — Docker Compose

## 54. Why Compose Exists

Manual multi-container startup can become:

```text
create network
create volume
run database
run backend
run frontend
pass environment variables
publish ports
connect everything
```

Compose describes this configuration declaratively.

```text
compose.yaml / docker-compose.yml
             │
             ▼
        docker compose
             │
       ┌─────┼─────┐
       ▼     ▼     ▼
      db   backend frontend
```

## 55. Core Compose Commands

```bash
docker compose up
docker compose up -d
docker compose down
docker compose ps
docker compose logs
docker compose logs -f
docker compose build
docker compose pull
docker compose exec
docker compose config
```

- [ ] Start services
- [ ] Start detached
- [ ] Stop/remove project containers/networks
- [ ] Inspect services
- [ ] Follow logs
- [ ] Build images
- [ ] Execute commands
- [ ] Render/validate resolved configuration

---

# Tier 20 — Compose Services

## 56. `services`

Example:

```yaml
services:
  db:
    image: postgres:16

  backend:
    build: ./backend
```

- [ ] Service names
- [ ] `image`
- [ ] `build`
- [ ] `ports`
- [ ] `environment`
- [ ] `env_file`
- [ ] `volumes`
- [ ] `networks`
- [ ] `depends_on`
- [ ] `healthcheck`
- [ ] restart policy awareness

## 57. Service vs Container

A Compose **service** is configuration describing how containers for that application role should be run.

Do not treat service and container as exact synonyms.

---

# Tier 21 — Compose Networks & Volumes

## 58. Default Compose Network

Typical model:

```text
Compose Project Network
        │
  ┌─────┼────────┐
  ▼     ▼        ▼
frontend backend database
```

Services can communicate using service names.

## 59. Custom Networks

- [ ] Define networks when useful
- [ ] Attach selected services
- [ ] Understand network segmentation
- [ ] Avoid unnecessary complexity for simple local projects

## 60. Compose Volumes

Example:

```yaml
services:
  db:
    volumes:
      - guestbook_pgdata:/var/lib/postgresql/data

volumes:
  guestbook_pgdata:
```

Understand both references:

```text
service mount
      │
      ▼
named volume declaration
```

---

# Tier 22 — Compose Configuration

## 61. Environment Variables

Example:

```yaml
environment:
  SPRING_PROFILES_ACTIVE: docker
```

- [ ] Static values
- [ ] Variable interpolation
- [ ] Defaults
- [ ] Shell environment
- [ ] `.env` awareness
- [ ] Container environment vs Compose interpolation

## 62. Variable Interpolation

Recognize Compose-style forms such as:

```text
${VARIABLE}
${VARIABLE:-default}
```

Understand that syntax belongs to Compose/shell-style configuration and may differ from framework syntax.

For example, Spring configuration commonly has its own placeholder conventions.

Do not mix syntaxes blindly between tools.

## 63. Escaping

Understand that `$` may need escaping in some Compose contexts when the value should be passed through rather than interpolated by Compose.

- [ ] Recognize interpolation errors
- [ ] Use `docker compose config` to inspect resolved configuration
- [ ] Determine which layer is interpreting the variable

---

# Tier 23 — Health Checks

## 64. Why Health Checks Exist

A running process is not necessarily ready to serve requests.

```text
Container started
      │
      ▼
Process running
      │
      ▼
Application initializing
      │
      ▼
Ready / healthy
```

## 65. Docker Health Checks

Example concept:

```yaml
healthcheck:
  test: ["CMD-SHELL", "pg_isready ..."]
  interval: 5s
  timeout: 5s
  retries: 10
```

- [ ] Health command
- [ ] Interval
- [ ] Timeout
- [ ] Retries
- [ ] Start-period awareness
- [ ] Healthy/unhealthy states
- [ ] Use application-appropriate checks

## 66. Liveness vs Readiness Awareness

Understand conceptually:

```text
Liveness  → should this process be considered alive?
Readiness → is it ready to receive work?
```

Deeper orchestration behavior belongs in Kubernetes.

---

# Tier 24 — Dependencies & Startup Ordering

## 67. `depends_on`

- [ ] Express service startup relationships
- [ ] Understand startup order is not automatically application readiness
- [ ] Combine readiness/health behavior appropriately
- [ ] Applications should tolerate dependency startup delays where practical

## 68. Retry / Resilience Awareness

A backend may start before a database is ready.

Professional systems should not depend entirely on:

```text
"database will definitely be ready first"
```

Understand:

- [ ] Connection retries awareness
- [ ] Health checks
- [ ] Graceful startup
- [ ] Dependency availability

---

# Tier 25 — Multi-Stage Builds

## 69. Why Multi-Stage Builds Exist

Build tools may be required to **build** an application but not to **run** it.

Example:

```text
Build stage
Maven + JDK + source
       │
       ▼
     JAR
       │
       ▼
Runtime stage
JRE + JAR
```

## 70. Java Example

```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

- [ ] Named stages
- [ ] `COPY --from`
- [ ] Separate build dependencies from runtime
- [ ] Smaller runtime image
- [ ] Reduced attack surface
- [ ] Better deployment artifact structure

**Checkpoint:** Explain why Maven does not need to exist in the final Spring Boot runtime image.

---

# Tier 26 — Spring Boot Containers

## 71. Containerizing Spring Boot

Understand requirements:

```text
Spring Boot source
      │
      ▼
Maven build
      │
      ▼
Executable JAR
      │
      ▼
Java runtime image
      │
      ▼
Container
```

- [ ] Build JAR
- [ ] Select compatible Java runtime
- [ ] Copy JAR
- [ ] Set runtime command
- [ ] Expose/document application port
- [ ] Externalize configuration
- [ ] Use environment variables
- [ ] Configure database host for container networking
- [ ] Health endpoint awareness

## 72. Spring Profiles

Example:

```text
application.yml
application-docker.yml
```

Possible environment:

```yaml
SPRING_PROFILES_ACTIVE: docker
```

- [ ] Understand why Docker environment may need different hostnames/config
- [ ] Avoid duplicating configuration unnecessarily
- [ ] Keep secrets external

---

# Tier 27 — Angular / React Containers

## 73. Development vs Production Frontend Containers

Development may involve:

```text
Node
dev server
source mounts
hot reload
```

Production commonly involves:

```text
Node build stage
      │
      ▼
static assets
      │
      ▼
web server / hosting layer
```

- [ ] Understand build-time vs runtime frontend requirements
- [ ] `npm install` / deterministic install awareness
- [ ] Build frontend assets
- [ ] Multi-stage builds
- [ ] Serve production assets appropriately
- [ ] Environment/configuration differences
- [ ] Do not assume a development server is a production deployment strategy

## 74. Frontend API Communication

Understand:

```text
Browser
   │
   ▼
published frontend/backend endpoints
```

Important distinction:

Browser requests do not automatically use Docker-internal DNS names.

```text
backend:8080
```

may be valid **container-to-container**, while the browser may need something such as:

```text
localhost:8081
```

during local development or a public hostname in deployment.

**Checkpoint:** Explain the difference between browser-to-container and container-to-container networking.

---

# Tier 28 — Database Containers

## 75. PostgreSQL

Typical concerns:

- [ ] Image version
- [ ] Database name
- [ ] User/password
- [ ] Port
- [ ] Named volume
- [ ] Health check
- [ ] Internal service hostname
- [ ] Initialization awareness

## 76. MongoDB

- [ ] Image/tag
- [ ] Authentication/configuration
- [ ] Persistent volume
- [ ] Port/network
- [ ] Connection string
- [ ] Initialization awareness

## 77. Neo4j

- [ ] Image/tag
- [ ] Authentication
- [ ] Data persistence
- [ ] Relevant ports
- [ ] Network/service name
- [ ] Configuration
- [ ] Development tooling access awareness

## 78. Database Data Safety

- [ ] Understand where data lives
- [ ] Do not casually delete volumes
- [ ] Development resets vs production persistence
- [ ] Backups belong beyond ordinary container persistence

---

# Tier 29 — Full-Stack Compose Architecture

## 79. Basic Architecture

```text
Browser
   │
   ▼
Frontend
   │
   │ HTTP
   ▼
Backend
   │
   │ database protocol
   ▼
Database
   │
   ▼
Named Volume
```

Compose:

```text
┌──────────────────────────────────┐
│ Docker Compose Project           │
│                                  │
│  frontend                        │
│      │                           │
│      ▼                           │
│  backend ─────────► database     │
│                       │          │
│                       ▼          │
│                  named volume    │
│                                  │
│ networks + config + healthchecks │
└──────────────────────────────────┘
```

## 80. Design From Requirements

Given:

```text
Angular frontend
Spring Boot backend
PostgreSQL database
```

derive:

```text
What needs an image?
What needs to be built?
What ports are internal?
What ports must reach the host/browser?
What configuration is required?
What needs persistent storage?
Which services communicate?
What must be healthy?
Which values are secrets?
```

This is the core skill that replaces copying a prewritten Compose file.

## 81. Build the Compose File Incrementally

Recommended workflow:

```text
1. Run database alone
2. Verify database
3. Build backend image
4. Run backend alone where possible
5. Connect backend to database network
6. Verify API
7. Build frontend
8. Connect browser/API flow
9. Add health checks
10. Add configuration/persistence
11. Recreate from scratch
```

**Checkpoint:** Containerize a complete frontend + Spring Boot + database project without following a line-by-line Docker tutorial.

---

# Tier 30 — Container Security

## 82. Image Security

- [ ] Use trusted base images
- [ ] Pin intentional versions/tags
- [ ] Keep images updated
- [ ] Remove unnecessary build tools from runtime images
- [ ] Minimize image contents
- [ ] Vulnerability scanning awareness
- [ ] Software supply-chain awareness

## 83. Runtime Security

- [ ] Avoid root where practical
- [ ] Least privilege
- [ ] Limit exposed ports
- [ ] Do not mount sensitive host paths unnecessarily
- [ ] Avoid privileged containers unless genuinely required
- [ ] Capability restrictions awareness
- [ ] Read-only filesystem awareness

## 84. Secret Security

- [ ] Never bake credentials into images
- [ ] Never commit credentials
- [ ] Avoid leaking secrets in logs
- [ ] Secret stores/platform secrets awareness
- [ ] Rotate compromised credentials

## 85. Docker Socket Awareness

Understand that access to the Docker daemon/socket is highly privileged.

- [ ] Do not casually expose/mount the Docker socket
- [ ] Understand security implications conceptually

---

# Tier 31 — Resource Management

## 86. CPU & Memory

- [ ] Containers consume host resources
- [ ] Memory limits awareness
- [ ] CPU limits awareness
- [ ] Out-of-memory behavior awareness
- [ ] Avoid assuming container isolation means unlimited resources

## 87. Disk

Consider:

```text
images
stopped containers
volumes
build cache
logs
```

- [ ] Inspect Docker disk usage
- [ ] Clean unused resources intentionally
- [ ] Avoid destructive cleanup commands without understanding what they remove

---

# Tier 32 — Logging & Observability

## 88. Container Logs

```bash
docker logs <container>
docker logs -f <container>
```

Compose:

```bash
docker compose logs
docker compose logs -f backend
```

- [ ] Read stdout/stderr logs
- [ ] Follow logs
- [ ] Filter by service
- [ ] Understand application logging still matters
- [ ] Do not use shell access as the only debugging strategy

## 89. Observability Awareness

- [ ] Health
- [ ] Logs
- [ ] Metrics
- [ ] Traces
- [ ] Container state
- [ ] Resource usage
- [ ] Correlation with application failures

ELK/centralized logging belongs in its later dedicated tree.

---

# Tier 33 — Debugging

## 90. General Debugging Flow

```text
Container won't work
       │
       ▼
Does it exist?
docker ps -a
       │
       ▼
What happened?
docker logs
       │
       ▼
Configuration?
docker inspect
       │
       ▼
Need internal inspection?
docker exec
       │
       ▼
Network?
docker network inspect
       │
       ▼
Storage?
docker volume inspect
```

## 91. Container Exits Immediately

Check:

- [ ] Logs
- [ ] Entrypoint/command
- [ ] Required environment variables
- [ ] Missing files
- [ ] Application exception
- [ ] Permissions
- [ ] Wrong architecture/platform awareness

## 92. Port Problems

Check:

```text
Is application listening?
Which container port?
Which host port?
Is port published?
Is host port already occupied?
```

## 93. Network Problems

Check:

- [ ] Network exists
- [ ] Containers attached
- [ ] Correct service/container DNS name
- [ ] Correct internal port
- [ ] `localhost` misuse
- [ ] Firewall/external networking
- [ ] Application binding address awareness

## 94. Volume Problems

Check:

- [ ] Correct mount
- [ ] Correct container path
- [ ] Existing data
- [ ] Permissions
- [ ] Bind mount hiding image files
- [ ] Wrong volume/project name

## 95. Compose Problems

Use:

```bash
docker compose config
docker compose ps
docker compose logs
```

Check:

- [ ] YAML structure
- [ ] Variable interpolation
- [ ] Build paths
- [ ] Service names
- [ ] Network creation
- [ ] Volume declaration
- [ ] Health state
- [ ] Dependency configuration

**Checkpoint:** Diagnose intentionally broken port, network, environment-variable, and volume configurations.

---

# Tier 34 — Registries

## 96. Registry Concept

```text
Dockerfile
    │
    ▼
Image
    │
    ▼
Registry
    │
    ▼
Other machine / deployment system
```

- [ ] Registry
- [ ] Repository
- [ ] Image name
- [ ] Tag
- [ ] Push
- [ ] Pull
- [ ] Authentication
- [ ] Public vs private registries

The generic pattern `registry/user/app:1.0` maps onto real registries like this:

```bash
docker.io/yourusername/myapp:1.0        # Docker Hub (the default, public)
ghcr.io/yourusername/myapp:1.0          # GHCR — GitHub Container Registry (private/org)
myregistry.example.com/myapp:1.0        # self-hosted or cloud-provider registry
```

In practice, the three registries you'll run into most are **Docker Hub** (the default public registry — no hostname needed), **GHCR**, and **AWS ECR**, with GHCR and ECR being the most common choices for private images.

## 97. Basic Workflow

Conceptually:

```bash
docker build -t registry/user/app:1.0 .
docker push registry/user/app:1.0
docker pull registry/user/app:1.0
```

- [ ] Tag for target registry
- [ ] Authenticate securely

```bash
docker login          # Docker Hub
docker login ghcr.io  # GHCR
```

- [ ] Push image
- [ ] Pull elsewhere
- [ ] Understand that source-code Git repositories and container registries serve different purposes

---

# Tier 35 — Tags & Versioning

## 98. Tags

Examples:

```text
my-app:1.0
my-app:1.1
my-app:dev
my-app:latest
```

- [ ] Tags are references/names
- [ ] Multiple tags may refer to the same image
- [ ] `latest` is a conventional tag, not a guarantee
- [ ] Use intentional versioning

## 99. Immutable Deployment Thinking

Prefer deployments where you can identify the exact artifact.

Awareness:

```text
semantic version tag
Git commit tag
image digest
```

- [ ] Reproducibility
- [ ] Rollback
- [ ] Traceability
- [ ] Avoid silently changing supposedly fixed releases

---

# Tier 36 — Production Image Practices

## 100. Small Runtime Images

- [ ] Multi-stage builds
- [ ] Only runtime dependencies
- [ ] Avoid source/build caches in final image
- [ ] Use `.dockerignore`
- [ ] Balance minimalism with maintainability/debuggability

## 101. Reproducible Builds

- [ ] Version dependencies
- [ ] Use lockfiles/build manifests
- [ ] Intentional base-image versions
- [ ] Build from source control
- [ ] Understand external dependency changes

## 102. One Main Concern per Container

Use containers around clear process/service responsibilities.

Avoid treating a container like a general-purpose VM where many unrelated services are manually started.

## 103. Graceful Shutdown

- [ ] Understand signals conceptually
- [ ] Application should terminate cleanly
- [ ] Exec-form commands help signal handling
- [ ] Close database/network resources
- [ ] Orchestration later depends on predictable shutdown

## 104. Configuration Separation

```text
Image
same application artifact
      │
      ├── dev configuration
      ├── test configuration
      └── production configuration
```

Avoid building a completely different application image merely to change ordinary environment configuration.

---

# Tier 37 — CI/CD Readiness

## 105. Docker in CI/CD

Conceptual pipeline:

```text
Git push
   │
   ▼
CI
   │
   ├── compile
   ├── test
   ├── docker build
   ├── scan
   └── push image
            │
            ▼
         Registry
            │
            ▼
        Deployment
```

Before entering the CI/CD tree, understand:

- [ ] Non-interactive builds
- [ ] Exit codes
- [ ] Reproducible Docker builds
- [ ] Image tags
- [ ] Registries
- [ ] Environment configuration
- [ ] Secrets awareness
- [ ] Test-before-publish workflow

Jenkins/GitHub Actions/other pipeline tooling belongs in dedicated CI/CD study.

---

# Tier 38 — Cloud Deployment Readiness

## 106. Container Deployment Concepts

Understand the transition:

```text
Local Docker
     │
     ▼
Image Registry
     │
     ▼
Remote Compute
```

- [ ] Host/container ports
- [ ] Persistent storage
- [ ] External database considerations
- [ ] DNS
- [ ] TLS
- [ ] Secrets
- [ ] Logging
- [ ] Health checks
- [ ] Restart behavior

## 107. AWS Readiness

Before later AWS work, understand:

```text
Docker image
     │
     ▼
remote host / container service
```

and distinguish application container concepts from:

```text
EC2 compute
EBS block storage
S3 object storage
networking/security groups
container registries/services
```

AWS specifics belong in the AWS tree.

---

# Tier 39 — Kubernetes Readiness

## 108. Why Docker Is Not Kubernetes

```text
Docker
build/run containers

Kubernetes
orchestrate containerized workloads across infrastructure
```

Docker knowledge feeds Kubernetes but does not replace it.

## 109. Concepts That Transfer

Docker knowledge prepares you for:

```text
image
container
registry
port
environment
volume
health check
resource limits
networking
```

Kubernetes later adds concepts such as:

```text
Pod
Deployment
Service
ConfigMap
Secret
PersistentVolume
Ingress
scheduler
cluster
```

- [ ] Understand why immutable/reproducible images matter
- [ ] Understand stateless application design
- [ ] Understand health checks
- [ ] Understand external configuration
- [ ] Understand persistent storage boundaries
- [ ] Understand registries

---

# Tier 40 — Professional Docker Workflow

## 110. Containerize From Requirements

Given an unfamiliar application, ask:

```text
1. How is it built?
2. How is it run?
3. What runtime does it need?
4. What files does it need?
5. What port does it listen on?
6. What configuration does it require?
7. What external services does it need?
8. What data must persist?
9. What should be reachable externally?
10. How do I know it is healthy?
```

Then derive the Docker configuration.

## 111. Build Incrementally

```text
Application works locally
        │
        ▼
Minimal Dockerfile
        │
        ▼
Build image
        │
        ▼
Run container
        │
        ▼
Verify application
        │
        ▼
Add configuration
        │
        ▼
Add networking/storage
        │
        ▼
Optimize image
        │
        ▼
Compose with dependencies
```

## 112. Avoid Cargo-Cult Docker

Do not copy instructions such as:

```dockerfile
RUN ...
ENV ...
EXPOSE ...
ENTRYPOINT ...
```

without being able to answer:

```text
Why is this instruction here?
Does it run during build or startup?
What file/state does it change?
Could it be removed?
What depends on it?
```

## 113. Documentation Workflow

When encountering an unfamiliar image:

1. Identify the official image.
2. Read its supported tags.
3. Find required configuration.
4. Find exposed/listening ports.
5. Find expected volume paths.
6. Find startup behavior.
7. Run the simplest version.
8. Inspect it.
9. Add only the configuration your application needs.

## 114. Rebuild From Memory

A strong competency test:

```text
Delete disposable containers/networks
        │
        ▼
Keep source code
        │
        ▼
Rebuild images
        │
        ▼
Recreate environment
        │
        ▼
Verify persistent data expectations
        │
        ▼
Explain every component
```

---

# Practical Competency Checkpoints

A developer completing this tree should be able to:

- [ ] Explain container vs VM
- [ ] Explain Dockerfile vs image vs container
- [ ] Explain Docker CLI vs Docker Engine
- [ ] Install/verify Docker in a WSL2 development environment
- [ ] Pull, inspect, run, stop, start, remove, and debug containers
- [ ] Build and inspect images
- [ ] Explain image layers and caching
- [ ] Write a Dockerfile without copying one line-for-line
- [ ] Explain `FROM`, `RUN`, `COPY`, `WORKDIR`, `ENV`, `ARG`, `EXPOSE`, `CMD`, and `ENTRYPOINT`
- [ ] Explain build context and use `.dockerignore`
- [ ] Pass runtime configuration safely
- [ ] Explain host port vs container port
- [ ] Explain why `EXPOSE` does not publish a port
- [ ] Create and inspect Docker networks
- [ ] Explain why `localhost` behaves differently inside a container
- [ ] Use service-name DNS for container communication
- [ ] Use named volumes for persistent database data
- [ ] Explain bind mounts vs volumes
- [ ] Write a Compose file from application requirements
- [ ] Configure services, networks, volumes, environment variables, and health checks
- [ ] Explain startup ordering vs readiness
- [ ] Write a multi-stage build
- [ ] Containerize Spring Boot
- [ ] Containerize a production frontend build
- [ ] Run PostgreSQL/MongoDB/Neo4j as development containers
- [ ] Build a complete frontend/backend/database Compose environment
- [ ] Diagnose network, port, volume, build, and configuration failures
- [ ] Tag and push an image to a registry
- [ ] Explain basic container security practices
- [ ] Explain how Docker feeds into CI/CD, cloud deployment, and Kubernetes
- [ ] Containerize an unfamiliar application by reasoning from its requirements

---

# Suggested Practice Progression

```text
1. Run hello-world
        │
        ▼
2. Run an interactive Linux container
        │
        ▼
3. Run a web server and publish a port
        │
        ▼
4. Run PostgreSQL with a named volume
        │
        ▼
5. Create a tiny custom Dockerfile
        │
        ▼
6. Containerize a Java program
        │
        ▼
7. Containerize Spring Boot
        │
        ▼
8. Connect Spring Boot → PostgreSQL
        │
        ▼
9. Rebuild using Docker Compose
        │
        ▼
10. Add health checks
        │
        ▼
11. Add Angular or React frontend
        │
        ▼
12. Create a multi-stage frontend build
        │
        ▼
13. Diagnose intentionally broken configs
        │
        ▼
14. Push an image to a registry
        │
        ▼
15. Recreate the entire stack from memory
```

---

# Full-Stack Capstone

Containerize a complete application:

```text
                    Browser
                       │
                       ▼
               Frontend Container
                       │
                       │ HTTP
                       ▼
               Spring Boot Container
                       │
                       │ DB protocol
                       ▼
                Database Container
                       │
                       ▼
                  Named Volume
```

Compose model:

```text
┌──────────────────────────────────────────┐
│ Full-Stack Docker Compose Project        │
│                                          │
│  frontend                                │
│     │                                    │
│     ▼                                    │
│  backend                                 │
│     │                                    │
│     ▼                                    │
│  database ───────────────► named volume  │
│                                          │
│  configuration                           │
│  service-name DNS                        │
│  internal network                        │
│  published ports                         │
│  health checks                           │
└──────────────────────────────────────────┘
```

Build it in three rounds:

### Round 1 — Guided

Use documentation and notes freely.

### Round 2 — Reduced Guidance

Use documentation only when you cannot remember syntax.

### Round 3 — From Requirements

Start only with:

```text
Frontend: Angular or React
Backend: Spring Boot
Database: PostgreSQL, MongoDB, or Neo4j
```

Determine independently:

- [ ] Dockerfiles
- [ ] Base images
- [ ] Build stages
- [ ] Runtime commands
- [ ] Ports
- [ ] Networks
- [ ] Service names
- [ ] Environment variables
- [ ] Volumes
- [ ] Health checks
- [ ] Compose structure
- [ ] Debugging strategy

The goal is not memorizing YAML. The goal is being able to **derive the YAML and Dockerfiles from the architecture**.

---

# Interview Readiness

Be able to answer:

- [ ] What is Docker?
- [ ] What problem do containers solve?
- [ ] Container vs VM?
- [ ] Image vs container?
- [ ] What is a Dockerfile?
- [ ] What does `FROM` do?
- [ ] `RUN` vs `CMD` vs `ENTRYPOINT`?
- [ ] What is a Docker image layer?
- [ ] How does build caching work?
- [ ] What is build context?
- [ ] What is `.dockerignore`?
- [ ] What does `EXPOSE` do?
- [ ] What does `-p 8081:8080` mean?
- [ ] What does `localhost` mean inside a container?
- [ ] How do two containers communicate?
- [ ] What is a Docker network?
- [ ] What is a Docker volume?
- [ ] Volume vs bind mount?
- [ ] What is Docker Compose?
- [ ] Service vs container?
- [ ] What does `depends_on` do and not do?
- [ ] Why use a health check?
- [ ] Why use multi-stage builds?
- [ ] Why should runtime images be smaller than build images?
- [ ] How do you pass configuration into a container?
- [ ] How should secrets be handled?
- [ ] Why shouldn't database data live only in a container's writable filesystem?
- [ ] How would you debug a container that exits immediately?
- [ ] How would you debug containers that cannot communicate?
- [ ] What is an image registry?
- [ ] Why are tags important?
- [ ] How does Docker fit into CI/CD?
- [ ] Docker vs Kubernetes?
- [ ] How would you containerize an application you have never seen before?

---

# Mastery Standard

> **Can I take an application that already runs normally, determine its build/runtime/network/storage/configuration requirements, write its Dockerfile and Compose configuration, run and debug it, explain every important line, and recreate the environment without relying on a step-by-step tutorial?**

At mastery, Docker should fit naturally into the larger full-stack skill tree:

```text
Development Environment
Ubuntu / WSL2
       │
       ▼
      Git
       │
       ├──────────────────────────────┐
       ▼                              ▼
Frontend                         Java / Spring
Angular / React                       │
       │                              ▼
       │                          Spring Boot
       │                              │
       └──────────────┬───────────────┘
                      ▼
                HTTP / REST
                      │
                      ▼
                 Databases
          PostgreSQL / MongoDB / Neo4j
                      │
                      ▼
                    Docker
                      │
          ┌───────────┼───────────┐
          ▼           ▼           ▼
        CI/CD        Cloud    Kubernetes
       (later)      (later)     (later)
```
