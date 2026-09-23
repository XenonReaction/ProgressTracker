# Full-Stack Developer Master Skill Tree — Abbreviated Guide

## Purpose

This is the navigation-oriented companion to the complete reference. It keeps the overall dependency map and concise descriptions of the major languages, frameworks, databases, delivery tools, cloud/orchestration technologies, observability systems, and AI specializations.

## Overall Skill Tree

```text
FULL-STACK DEVELOPER MASTER PATH
│
├── DEVELOPMENT ENVIRONMENT & VERSION CONTROL
│   ├── Ubuntu / WSL2
│   └── Git
│
├── WEB & FRONTEND FOUNDATIONS
│   ├── HTML & CSS → JavaScript → TypeScript → Angular / React
│   └── HTTP / HTTPS / REST APIs
│
├── JAVA BACKEND FOUNDATIONS
│   └── Java
│       ├── Maven → JUnit → Mockito
│       ├── Spring Boot & Initializr
│       │   ├── AOP / Spring AOP
│       │   ├── JMeter (load/performance testing)
│       │   ├── Spring Data JPA
│       │   ├── RAG Fundamentals → Spring AI
│       │   └── Spring Cloud & Microservices
│       │        └── Eureka Service Discovery → Spring Cloud Gateway
│       └── Java Multithreading & Concurrency
│
├── DATA
│   ├── PostgreSQL → JDBC → ORM / JPA / Hibernate → Spring Data JPA
│   ├── MongoDB
│   └── Neo4j
│
├── AI-ASSISTED DEVELOPMENT & AI APPLICATIONS
│   ├── Programming + Git + Testing
│   │        └── AI-Assisted Software Development
│   │             ├── chat / IDE copilots / coding agents
│   │             ├── context engineering / repo exploration
│   │             ├── plan → implement → test → diff review
│   │             └── retrieval awareness ───────────────┐
│   └── AI / LLM Fundamentals                           │
│            ├── RAG Fundamentals ◄──────────────────────┘
│            │    └── ingest → chunk → embed/index → retrieve
│            │         → rerank → context → grounded answer → evaluate
│            └── Spring AI
│                 └── Spring AI RAG implementation ◄── RAG Fundamentals
│
├── CONTAINERS, DELIVERY & DEPLOYMENT
│   ├── Docker
│   └── Git → CI/CD Fundamentals
│        ├── Jenkins
│        ├── GitHub Actions
│        ├── GitLab CI/CD
│        └── Deployment Fundamentals
│             ├── AWS Fundamentals: EC2 / S3 / EBS
│             └── Kubernetes ◄── Docker
│
└── OBSERVABILITY
    └── ELK / Elastic Stack
```

## Recommended Curriculum Strategy

```text
Foundation → tiny exercises → small feature → tests → debugging → explain from memory → integrate → repeat
```

## Major Skill Trees

| Skill tree | What it is / why it exists | Main prerequisites | What it unlocks |
|---|---|---|---|
| Ubuntu / WSL2 | Linux shell and development-environment foundation on Windows. | Computer basics | Git, Docker, backend tooling, cloud/CI CLIs |
| Git | Distributed version control, branching, collaboration, recovery, and history. | Filesystem/shell basics | CI/CD and safe iterative development |
| HTML & CSS | Web structure, semantics, layout, responsiveness, and accessibility. | Browser/web basics | JavaScript and frontend frameworks |
| JavaScript | Browser/application programming language for behavior, DOM, async work, and APIs. | HTML/CSS | TypeScript, Angular, React |
| TypeScript | Typed JavaScript for safer large frontend applications. | JavaScript | Angular and typed React |
| Angular | Full TypeScript frontend framework with components, DI, forms, HTTP, routing, and testing. | HTML/CSS + JS + TS | Full-stack frontend specialization |
| React | Component-based frontend library using JSX/TSX, state/hooks, APIs, routing, and testing. | HTML/CSS + JS + TS | Full-stack frontend specialization |
| HTTP / HTTPS / REST APIs | Protocol and API-contract foundation connecting clients and services. | Web/network basics | Frontend-backend integration, Spring, microservices, gateways |
| Java | Primary backend language path: syntax, OOP, collections, modern Java, I/O, concurrency, architecture. | Programming fundamentals | Maven, testing, JDBC, Spring |
| Maven | Java build/dependency/lifecycle/package automation. | Java | JUnit, Spring Boot, CI builds |
| JUnit | Automated Java testing. | Java + Maven | Mockito and backend CI quality gates |
| Mockito | Mocking/isolation for Java unit/service tests. | JUnit + OOP/DI | Spring service testing |
| Java Concurrency | Threads, synchronization, executors, futures, concurrent collections, virtual threads. | Core Java | Safe concurrent backend reasoning |
| JMeter | Load/performance testing of REST APIs and microservices: test plans, thread groups, samplers, assertions, correlation, CLI/CI execution, results analysis. | HTTP/REST APIs + CLI basics | Practical performance testing for Spring Cloud, Eureka, and Gateway capstones; CI/CD pipeline integration |
| PostgreSQL | Relational database/SQL specialization. | Database/SQL basics | JDBC, JPA/Hibernate, Spring Data JPA |
| MongoDB | Document database specialization. | Database basics | Document-oriented application design |
| Neo4j | Graph database specialization using nodes, relationships, Cypher, and graph modeling. | Database basics | Graph-oriented application features |
| JDBC | Low-level Java relational database API. | Java + SQL/PostgreSQL | JPA/Hibernate understanding |
| ORM / JPA / Hibernate | Object-relational mapping, persistence context, entities, relationships, JPQL, Hibernate behavior. | Java + SQL + JDBC | Spring Data JPA |
| Spring Data JPA | Spring repository abstraction over JPA. | JPA/Hibernate + Spring Boot | Professional Spring data-access layers |
| Spring Boot & Initializr | Modern Spring application creation, DI, configuration, REST layers, testing, operations. | Java + Maven + HTTP + testing | Spring Data, AOP, Spring AI, microservices |
| AOP / Spring AOP | Cross-cutting concerns through aspects/proxies/interception. | Java OOP + Spring DI | Spring framework internals and cross-cutting behavior |
| Docker | Container images, Dockerfiles, networking, volumes, Compose, full-stack containerization. | Linux/network basics | CI image builds, deployment, Kubernetes |
| CI/CD Fundamentals | Tool-independent automated build/test/package/release/deploy practices. | Git | Jenkins, GitHub Actions, GitLab CI/CD, deployment |
| Jenkins | Jenkinsfile-based CI/CD implementation. | CI/CD + Git | Automated build/test/package/publish/deploy |
| GitHub Actions | GitHub-native workflow automation. | CI/CD + Git | Automated GitHub workflows |
| GitLab CI/CD | GitLab-native pipeline automation. | CI/CD + Git | Automated GitLab workflows |
| Deployment Fundamentals | Tool-independent release, environment, deployment, verification, rollback, and operations concepts. | CI/CD | AWS and Kubernetes |
| AWS Fundamentals — EC2/S3/EBS | Intro cloud compute, object storage, and block storage. | Deployment fundamentals | Practical AWS deployment and later cloud specialization |
| Kubernetes | Declarative container orchestration: Pods, Deployments, Services, config, storage, health, scaling, rollouts. | Docker + deployment fundamentals | Cloud-native deployment |
| Spring Cloud & Microservices | Service boundaries, independent services, HTTP communication, distributed failures, data/observability concerns. | Spring Boot + HTTP/REST | Eureka and Gateway |
| Eureka Service Discovery | Dynamic service registration and discovery for Spring microservices. | Spring Cloud/microservices | Discovery-based service communication and gateway routing |
| Spring Cloud Gateway | Central API gateway with routes, predicates, filters, discovery routing, path/header/CORS handling. | Microservices + Eureka | Centralized microservice entry point |
| ELK / Elastic Stack | Centralized logging/search/visualization using Elasticsearch, Logstash, and Kibana. | Spring/logging + Docker helpful | Production observability and microservice diagnostics |
| Spring AI | Spring abstractions for model APIs, embeddings, vector stores, tools, RAG, MCP, evaluation, and observability. | Spring Boot + HTTP + testing + AI fundamentals | AI-enabled Spring applications |
| **AI-Assisted Software Development** | Professional use of chat assistants, IDE copilots, CLI/coding agents, context engineering, repository exploration, task decomposition, verification, Git safety, permissions, and secure agentic workflows. | Programming + Git + testing/debugging | Faster development while preserving human ownership; RAG/MCP awareness |
| **RAG Fundamentals** | Framework-independent Retrieval-Augmented Generation: ingest knowledge, chunk and index it, retrieve/rerank evidence, construct context, generate grounded answers, attribute sources, evaluate quality, and secure the pipeline. | AI/LLM fundamentals + programming + HTTP/API + data basics | Spring AI RAG, knowledge/repository assistants, vector-search and agentic AI systems |

## AI Branch Relationship

```text
Programming + Git + Testing
          │
          ▼
AI-Assisted Software Development
          │
          ├── uses retrieval/repo search concepts
          │
          └──────────────► RAG awareness

AI / LLM Fundamentals
          │
          ▼
     RAG Fundamentals
          │
          ├──────────────► Spring AI RAG
          ├──────────────► Knowledge assistants
          └──────────────► Repository/agent context systems

Spring Boot + AI / LLM Fundamentals
          │
          ▼
       Spring AI
          │
          └── implements RAG concepts after RAG Fundamentals
```

### AI-Assisted Software Development — short path

```text
Programming + Git + Testing
→ tool categories
→ prompt/context engineering
→ repository exploration
→ task decomposition
→ bounded code/refactoring/debugging
→ testing & verification ladder
→ Git safety boundary
→ agent permissions/stop conditions
→ retrieval & MCP awareness
→ security/privacy
→ professional supervised-agent workflow
```

### RAG Fundamentals — short path

```text
AI/LLM fundamentals
→ authoritative knowledge sources
→ ingestion / parsing / cleaning
→ chunking + metadata
→ embeddings + vector/index fundamentals
→ query processing
→ keyword / semantic / hybrid retrieval
→ reranking
→ context construction
→ grounded generation + source attribution
→ evaluation / failure analysis
→ updates / deletion / security / prompt injection
→ observability / cost / performance
→ Spring AI RAG implementation
```
