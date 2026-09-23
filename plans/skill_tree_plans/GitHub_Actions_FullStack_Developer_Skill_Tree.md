# GitHub Actions Full-Stack Developer Skill Tree

> **Goal:** Learn how to implement the concepts from the **CI/CD Fundamentals** skill tree using GitHub Actions for normal developer workflows.
>
> **Target level:** Full-stack developer who can read, write, run, troubleshoot, and maintain practical GitHub Actions workflows for Java/Spring Boot and modern frontend applications.
>
> **Primary prerequisites:** **Git / Version Control** and **CI/CD Fundamentals**
>
> **Practical prerequisite:** **Docker** for container image build, registry, and capstone portions.
>
> **Supporting prerequisites:** GitHub repository basics, YAML basics, Maven, automated testing (JUnit/Mockito for Java), npm-based frontend tooling, basic Linux/command-line use, and basic HTTP/webhook concepts.
>
> **Recommended security companion:** A future **Security Fundamentals** tree should provide broader security knowledge. This tree introduces the GitHub Actions security practices developers need immediately: secrets, token permissions, least privilege, third-party action trust, pull-request risks, protected environments, and safe Docker/registry authentication. A future **DevSecOps** tree can build on these concepts.
>
> **Scope boundary:** This tree focuses on **basic professional GitHub Actions workflows**. Advanced reusable-workflow architecture, sophisticated custom action development, large self-hosted runner fleets, enterprise administration, organization-wide governance, and advanced GitHub platform administration are outside scope.
>
> **Deployment boundary:** GitHub Actions can orchestrate deployment, but AWS, Kubernetes, advanced cloud infrastructure, and deployment-platform mechanics belong in **Deployment Fundamentals** and their dedicated skill trees.

---

# Skill Tree Overview

```text
Git / GitHub
     │
     ▼
CI/CD Fundamentals
     │
     ├───────────────┐
     ▼               ▼
GitHub Actions     Docker
     │               │
     └───────┬───────┘
             ▼
          Workflow
             │
       ┌─────┼─────┐
       ▼     ▼     ▼
    Trigger Build  Test
       │     │     │
       └─────┼─────┘
             ▼
          Package
             │
             ▼
        Docker Image
             │
             ▼
          Publish
             │
             ▼
           Deploy
             │
             ▼
           Verify
```

---

# Concept Translation

```text
CI/CD Concept              GitHub Actions Implementation
-------------              -----------------------------
Pipeline                   Workflow
Pipeline as code           YAML file in .github/workflows/
Pipeline trigger           on
Execution unit             Job
Individual action          Step
Execution machine          Runner
Reusable packaged step     Action
Environment value          env
Secret                     GitHub Actions secret
Job output                 output
Artifact                   upload/download artifact actions
Dependency optimization    cache
Conditional execution      if
Parallel work              separate jobs / matrix awareness
Manual trigger             workflow_dispatch
Scheduled trigger          schedule
Deployment environment     environment
Permissions                permissions
```

The goal is not to memorize YAML. The goal is to understand which CI/CD concept each YAML structure implements.

---

# Tier 0 — What GitHub Actions Is

## 1. Definition

GitHub Actions is GitHub's workflow automation platform.

```text
Repository Event
      │
      ▼
GitHub Actions
      │
      ▼
Workflow
      │
      ├── Build
      ├── Test
      ├── Package
      ├── Publish
      └── Deploy
```

## 2. GitHub Actions vs CI/CD

```text
CI/CD
= engineering practices and concepts

GitHub Actions
= one platform for implementing them
```

## 3. Actions Beyond CI/CD

GitHub Actions can automate repository tasks beyond deployment pipelines.

For this tree, prioritize:

```text
software validation
build
test
package
publish
deployment orchestration
```

**Checkpoint:** Explain why GitHub Actions is a CI/CD tool rather than the definition of CI/CD itself.

---

# Tier 1 — Workflow Files

## 4. Workflow Location

GitHub Actions workflow files live in:

```text
.github/
└── workflows/
    ├── ci.yml
    └── deploy.yml
```

## 5. YAML

Workflow definitions use YAML.

Know:

- [ ] indentation
- [ ] mappings/key-value pairs
- [ ] lists
- [ ] strings
- [ ] multiline values
- [ ] basic quoting awareness

Do not turn this into a general YAML mastery tree.

## 6. Version Control

Workflow files live with the repository.

Benefits:

```text
reviewable
version controlled
branchable
traceable
reproducible
```

## 7. Workflow Naming

Prefer meaningful names such as:

```yaml
name: Full Stack CI
```

rather than relying on filenames alone.

---

# Tier 2 — Basic Workflow Structure

## 8. Minimal Mental Model

```yaml
name: CI

on:
  push:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - ...
```

Recognize:

```text
workflow
├── name
├── on
└── jobs
    └── job
        ├── runs-on
        └── steps
```

## 9. Workflow

A workflow is the overall automation definition.

## 10. Job

A job is a group of steps executed on a runner.

## 11. Step

A step is an individual operation.

## 12. Runner

A runner is the machine/environment executing a job.

---

# Tier 3 — Events and `on`

## 13. Event-Driven Execution

GitHub Actions commonly starts from repository events.

```text
push
pull request
manual dispatch
schedule
tag/release-related event awareness
```

## 14. `push`

Use push triggers when work should run after commits are pushed.

## 15. `pull_request`

Use pull-request workflows to validate proposed changes before merge.

```text
Feature Branch
      │
      ▼
Pull Request
      │
      ▼
GitHub Actions
      │
      ├── Build
      └── Test
```

## 16. `workflow_dispatch`

Allows manual workflow execution.

Useful for controlled/manual workflows and learning.

## 17. `schedule`

Supports scheduled workflows.

Use schedules for genuinely time-based work, not as a substitute for event-driven CI.

---

# Tier 4 — Trigger Filters

## 18. Branch Filters

Conceptually:

```text
push to main
      │
      ▼
production-related workflow

push to feature/*
      │
      ▼
CI validation
```

## 19. Path Filters

A workflow can be limited based on changed paths where appropriate.

Example use:

```text
backend/**
frontend/**
```

## 20. Tag Awareness

Tags can be used in release-oriented workflows.

Keep advanced release automation outside the basic scope.

## 21. Avoid Overcomplication

Start with understandable triggers.

Do not build a complex trigger expression merely to save a few seconds of CI time.

---

# Tier 5 — GitHub-Hosted Runners

## 22. `runs-on`

A job chooses a runner environment.

Example:

```yaml
runs-on: ubuntu-latest
```

## 23. Common Hosted Environments

Be aware of:

```text
Ubuntu
Windows
macOS
```

For this full-stack path, prioritize Ubuntu/Linux workflows.

## 24. Fresh Job Environment

GitHub-hosted runners generally provide a fresh environment for jobs.

This helps reduce dirty-workspace problems.

## 25. Installed Software Awareness

Runner images include many tools, but versions change.

Do not rely blindly on "whatever happens to be installed."

Use setup actions where appropriate.

---

# Tier 6 — Self-Hosted Runner Awareness

## 26. Self-Hosted Runner

Organizations can run their own Actions runners.

Concept:

```text
GitHub
   │
   ▼
Self-Hosted Runner
   │
   ▼
Organization Infrastructure
```

## 27. Why They Exist

Possible reasons:

- [ ] Special hardware
- [ ] Private network access
- [ ] Custom software
- [ ] Cost/control requirements

## 28. Scope Boundary

For this tree:

```text
recognize
understand basic security implications
know when one might be required
```

Do not learn runner-fleet administration.

---

# Tier 7 — Steps: `uses` vs `run`

## 29. `run`

Executes shell commands.

```yaml
- name: Run tests
  run: ./mvnw test
```

## 30. `uses`

Invokes an existing GitHub Action.

```yaml
- uses: actions/checkout@...
```

## 31. Mental Model

```text
run
→ execute command/script

uses
→ invoke packaged action
```

## 32. Prefer Existing Project Commands

GitHub Actions should orchestrate:

```text
./mvnw test
npm ci
npm test
docker build
```

rather than duplicating build logic inside YAML.

---

# Tier 8 — Repository Checkout

## 33. Checkout

A workflow commonly needs the repository contents.

```text
Runner
  │
  ▼
Checkout Repository
  │
  ▼
Build/Test
```

## 34. Checkout Action

Recognize the official checkout action as the normal way to retrieve repository contents.

## 35. Git History Awareness

Some tasks require more Git history/tags than a default shallow checkout.

Only change checkout behavior when the pipeline actually needs it.

## 36. Traceability

A workflow run is tied to GitHub repository/ref/commit context.

Be able to identify:

```text
Workflow Run
     │
     ▼
Git Commit
     │
     ▼
Artifact / Image
```

---

# Tier 9 — Setup Actions

## 37. Tool Setup

Use appropriate setup actions to establish known tool versions.

Examples conceptually:

```text
Java
Node.js
```

## 38. Java

For Spring Boot:

```text
checkout
   │
   ▼
setup Java
   │
   ▼
Maven build
```

## 39. Node

For Angular/React:

```text
checkout
   │
   ▼
setup Node
   │
   ▼
npm ci
   │
   ▼
test/build
```

## 40. Reproducibility

Prefer explicit versions appropriate to the project rather than assuming runner defaults.

---

# Tier 10 — Maven Backend Workflow

## 41. Maven Wrapper

Prefer project-controlled Maven Wrapper where available.

```yaml
run: ./mvnw clean verify
```

## 42. Backend Flow

```text
Checkout
   │
   ▼
Setup Java
   │
   ▼
Compile
   │
   ▼
JUnit / Mockito
   │
   ▼
Package JAR
```

## 43. Maven Owns the Build

Keep dependencies/build configuration in `pom.xml`.

GitHub Actions orchestrates Maven.

## 44. Failure Behavior

If Maven exits unsuccessfully, the workflow step/job normally fails.

Required later jobs should not proceed unless intentionally configured.

---

# Tier 11 — npm Frontend Workflow

## 45. Install

For lockfile-based CI:

```text
npm ci
```

is commonly appropriate.

## 46. Test

Run the project's configured test command.

## 47. Build

Run the production build command defined by the project.

Concept:

```text
Checkout
   │
   ▼
Setup Node
   │
   ▼
npm ci
   │
   ▼
npm test
   │
   ▼
npm run build
```

## 48. Angular / React

The CI/CD structure is largely the same.

The project-specific npm commands determine the framework behavior.

---

# Tier 12 — Jobs and Dependencies

## 49. Jobs Can Run Independently

Separate jobs can run in parallel when no dependency exists.

```text
          ┌── backend
Workflow ─┤
          └── frontend
```

## 50. `needs`

Use job dependencies when one job requires another.

Concept:

```text
backend ──┐
          ├── package/publish
frontend ─┘
```

## 51. Failures and Dependencies

If a required upstream job fails, dependent jobs normally do not continue.

## 52. Design by CI/CD Phase

Jobs should represent meaningful independent responsibilities.

---

# Tier 13 — Environment Variables

## 53. `env`

Environment values can be defined at different scopes.

Conceptually:

```text
workflow
job
step
```

## 54. Example

```yaml
env:
  APP_NAME: guestbook
```

## 55. Access

Workflow expressions and shell commands can consume environment values.

## 56. Configuration vs Secret

Ordinary non-sensitive configuration may use `env`.

Sensitive values should use GitHub secrets or appropriate protected identity mechanisms.

---

# Tier 14 — GitHub Contexts

## 57. Context Concept

GitHub Actions exposes structured runtime information.

Examples:

```text
github
env
secrets
runner
job
steps
needs
```

## 58. GitHub Context

Can expose useful metadata such as:

```text
repository
ref
commit SHA
event
actor
```

## 59. `needs` Context

Can access outputs/results from dependent jobs where configured.

## 60. Security Awareness

Do not dump entire contexts into logs casually.

Some contexts may contain sensitive or attacker-controlled information.

---

# Tier 15 — Expressions

## 61. Expression Syntax

Recognize GitHub Actions expression syntax:

```text
${{ ... }}
```

## 62. Common Uses

Expressions support:

- [ ] Variables/context values
- [ ] Conditions
- [ ] Inputs
- [ ] Secrets
- [ ] Job outputs

## 63. Do Not Turn YAML Into a Programming Language

Keep workflow logic simple.

Complex application logic belongs in scripts/programs.

---

# Tier 16 — Conditions

## 64. `if`

Steps/jobs can execute conditionally.

Examples conceptually:

```text
only on main
only after success
only for a certain event
```

## 65. Production Safety

Deployment conditions should make it difficult for a feature branch to accidentally deploy production.

## 66. Status Conditions Awareness

Recognize conditions related to:

```text
success
failure
always
cancelled
```

Use them carefully for cleanup/reporting.

---

# Tier 17 — Secrets

## 67. Repository/Environment Secrets

GitHub can provide secrets to workflows.

Examples:

```text
registry token
deployment credential
API key
```

## 68. Secret Reference

Conceptually:

```yaml
${{ secrets.SOME_SECRET }}
```

## 69. Never Hardcode

Bad:

```yaml
env:
  PASSWORD: real-production-password
```

## 70. Secret Masking Is Not a Complete Security Model

Avoid:

- [ ] Printing secrets
- [ ] Writing them to artifacts
- [ ] Embedding them in Docker images
- [ ] Passing them unnecessarily
- [ ] Exposing them to untrusted code

## 71. Scope Secrets

Prefer secrets at the narrowest practical repository/environment scope.

---

# Tier 18 — `GITHUB_TOKEN`

## 72. Automatic Token

GitHub Actions can provide a token for workflow interactions with GitHub.

## 73. Permissions Matter

Do not assume every workflow needs broad repository write permissions.

Concept:

```yaml
permissions:
  contents: read
```

with additional permissions only when needed.

## 74. Least Privilege

Examples:

```text
test workflow
→ mostly read access

publishing workflow
→ only required package permissions

deployment workflow
→ only required deployment permissions
```

## 75. Security Habit

Explicitly think about:

```text
"What can this workflow token do?"
```

---

# Tier 19 — Third-Party Actions

## 76. Actions Execute Code

When a workflow says:

```yaml
uses: owner/action@...
```

it is trusting external code.

## 77. Prefer Trusted Sources

Prioritize:

- [ ] Official GitHub actions
- [ ] Well-established vendor/project actions
- [ ] Organization-approved actions

## 78. Version Pinning Awareness

Understand that action references can target versions/tags/commits.

Security-sensitive environments may use stricter pinning policies.

The exact organization policy belongs in DevSecOps/security governance.

## 79. Minimize Dependencies

Do not add a third-party action for a trivial task that a clear shell command already handles safely.

---

# Tier 20 — Pull Request Security

## 80. Untrusted Changes

A contributor may modify:

```text
workflow YAML
build scripts
Dockerfile
source code
tests
```

## 81. Secrets and Forks

GitHub intentionally restricts secret exposure in various untrusted fork scenarios.

The important mental model:

```text
untrusted code
+
powerful secret
=
security risk
```

## 82. `pull_request_target` Awareness

Know that some event configurations can execute in a more privileged repository context and therefore require special caution.

Do not use advanced privileged PR patterns without understanding their security model.

## 83. Code Review

Workflow changes deserve the same scrutiny as application code—sometimes more, because they can control credentials and deployments.

---

# Tier 21 — Artifacts

## 84. Workflow Artifact

Artifacts allow files to be retained/transferred from workflow execution.

Examples:

```text
JAR
frontend bundle
test report
generated package
```

## 85. Upload

Use an artifact upload action to retain build outputs where appropriate.

## 86. Download

Dependent jobs can retrieve previously uploaded artifacts.

Concept:

```text
Build Job
   │
   ▼
Upload Artifact
   │
   ▼
Deploy Job
   │
   ▼
Download Artifact
```

## 87. Artifact vs Release Repository

GitHub Actions workflow artifacts are not automatically the same thing as a long-term production artifact repository/package registry.

---

# Tier 22 — Caching

## 88. Cache Purpose

Caches reduce repeated dependency/download work.

Potential examples:

```text
Maven dependencies
npm cache
```

## 89. Cache vs Artifact

```text
Cache
= performance optimization

Artifact
= intentional workflow output
```

## 90. Cache Key Awareness

A cache key should change when relevant dependencies change.

## 91. Correctness

A workflow should still produce correct results if the cache is empty.

---

# Tier 23 — Test Results

## 92. Tests Must Gate Progress

Required test failure should prevent release/deployment.

## 93. Java

Run:

```text
JUnit
Mockito
Spring tests
```

through Maven.

## 94. Frontend

Run the project's configured frontend tests.

## 95. Reporting

GitHub Actions can retain/report test results through logs, artifacts, job summaries, or appropriate reporting integrations/actions.

Do not require a specific third-party reporting action for basic mastery.

---

# Tier 24 — Job Summaries Awareness

## 96. Human-Readable Results

Workflows can provide useful summaries for developers.

Potential content:

```text
test totals
artifact version
deployment target
important links
```

## 97. Keep Summaries Useful

Do not dump enormous logs into summaries.

Use them for concise run-level information.

---

# Tier 25 — Matrix Strategy Awareness

## 98. Matrix Concept

A matrix can run the same job across combinations.

Example concept:

```text
Java 21 + Ubuntu
Java 21 + Windows
```

or:

```text
Node 20
Node 22
```

## 99. Basic Scope Only

Know how to recognize and create a simple matrix.

Do not make advanced dynamic matrices a required skill.

## 100. Use Only When Valuable

A full-stack business application may not need to test every OS/runtime combination.

---

# Tier 26 — Timeouts

## 101. Timeout

Jobs/steps should not run indefinitely.

Use appropriate workflow timeout controls for:

```text
hung test
stuck deployment
unresponsive external service
```

## 102. Realistic Values

Base limits on expected runtime.

---

# Tier 27 — Retry Awareness

## 103. GitHub Actions Does Not Mean "Retry Everything"

Retries are useful only for genuinely transient operations.

## 104. Prefer Script/Tool-Level Controlled Retry Where Appropriate

Examples:

```text
temporary HTTP call
registry/network transient failure
```

## 105. Deterministic Failure

Do not repeatedly rerun failed unit tests hoping they turn green.

---

# Tier 28 — Failure Handling

## 106. Default Failure

A failed required command generally fails the step/job.

## 107. `continue-on-error` Awareness

GitHub Actions can allow certain failures without failing the job/workflow.

Use cautiously.

## 108. Required vs Informational Checks

Conceptually separate:

```text
Required:
compile
unit tests
critical validation

Potentially informational:
experimental/non-blocking check
```

## 109. Preserve Evidence

Upload useful logs/reports/artifacts even when appropriate jobs fail.

---

# Tier 29 — Docker Build

## 110. Prerequisite Boundary

Docker concepts are assumed from the Docker skill tree.

## 111. Docker Build

GitHub Actions can invoke Docker:

```text
docker build
```

or approved Docker-oriented actions.

## 112. Image Identity

Use traceable tags such as:

```text
application version
Git commit SHA
release tag
```

## 113. Do Not Depend Only on `latest`

Production should be able to identify exactly which image is running.

---

# Tier 30 — Container Registry

## 114. Registry

A workflow can authenticate to and publish images to a container registry.

## 115. GitHub Container Registry Awareness

GitHub's package/container ecosystem can host images, while external registries are also possible.

## 116. Authentication

Use:

```text
GITHUB_TOKEN with appropriate permissions
or
repository/environment secrets
or
appropriate external identity
```

depending on the registry.

## 117. Publish Flow

```text
Docker Build
     │
     ▼
Versioned Image
     │
     ▼
Registry Login
     │
     ▼
Push
```

---

# Tier 31 — Environments

## 118. GitHub Environments

GitHub Actions environments can represent deployment targets such as:

```text
staging
production
```

## 119. Environment Secrets

Environment-specific secrets can help separate:

```text
staging credentials
from
production credentials
```

## 120. Protection Awareness

Depending on repository/plan/settings, environments can support protection mechanisms such as approvals/rules.

The developer should understand the concept even when exact organization configuration is managed elsewhere.

## 121. Deployment History Awareness

Environment/deployment features can improve traceability of what was deployed where.

---

# Tier 32 — Manual Production Gate

## 122. Controlled Promotion

A professional workflow may use:

```text
CI passes
   │
   ▼
Deploy Staging
   │
   ▼
Verify
   │
   ▼
Protected Production Environment
   │
   ▼
Approval / Rule
   │
   ▼
Deploy Production
```

## 123. Continuous Delivery

This supports a Continuous Delivery model where software is ready for production but production release can remain controlled.

---

# Tier 33 — Deployment Orchestration

## 124. GitHub Actions' Role

```text
GitHub Actions
      │
      ▼
Deployment Command / API / Tool
      │
      ▼
Target Platform
```

## 125. Boundary

The workflow may call:

```text
Docker
AWS CLI
kubectl
deployment script
platform API
```

but the platform mechanics belong in other trees.

## 126. Deployment Fundamentals

Before designing production deployment workflows, understand:

- [ ] Recreate
- [ ] Rolling
- [ ] Blue/green
- [ ] Canary
- [ ] Health/readiness
- [ ] Rollback
- [ ] Database compatibility

from Deployment Fundamentals.

---

# Tier 34 — Deployment Verification

## 127. Verify the Running System

After deployment:

```text
health endpoint
smoke test
frontend availability
critical API behavior
```

## 128. Failure

If staging verification fails:

```text
do not promote production
```

## 129. Production Verification

Production deployment should also be followed by appropriate verification/monitoring.

GitHub Actions can orchestrate checks; observability tools determine system health in depth.

---

# Tier 35 — Workflow Concurrency

## 130. Concurrent Runs

Multiple commits can create overlapping workflow runs.

For CI, this may be acceptable.

For deployment, it can be dangerous.

## 131. Concurrency Controls

Understand the purpose of concurrency groups:

```text
one production deployment at a time
```

## 132. Canceling Superseded Work

For some branch CI workflows, older runs can be canceled when a newer commit supersedes them.

Use based on workflow purpose.

---

# Tier 36 — Outputs

## 133. Step Output

A step can expose a value for later steps.

## 134. Job Output

A job can expose selected values to dependent jobs.

Example conceptual value:

```text
IMAGE_TAG
VERSION
ARTIFACT_NAME
```

## 135. Avoid Passing Secrets as Ordinary Outputs

Use appropriate secret/identity mechanisms instead.

---

# Tier 37 — Working Directories

## 136. Monorepo / Full-Stack Layout

Example:

```text
project/
├── backend/
└── frontend/
```

Commands may need different working directories.

## 137. Step-Level Working Directory

Understand how to execute commands in the correct project directory.

## 138. Defaults Awareness

Workflows can define defaults to reduce repetition.

Keep it readable.

---

# Tier 38 — Services Awareness

## 139. Service Containers

GitHub Actions jobs can use service containers for dependencies such as databases during testing.

Concept:

```text
Test Job
├── Application Tests
└── PostgreSQL Service
```

## 140. Use Case

Integration tests may need:

```text
PostgreSQL
Redis
other supporting service
```

## 141. Boundary

This is for CI test dependencies, not automatically your production deployment architecture.

Docker knowledge remains a prerequisite.

---

# Tier 39 — Database Testing

## 142. Unit vs Integration Tests

Unit tests may not need a database.

Integration tests may.

## 143. PostgreSQL CI Example

```text
Runner
  │
  ├── PostgreSQL service
  │
  └── Spring Boot integration tests
```

## 144. Test Data

Use isolated test data/configuration.

Never point CI tests at production databases.

---

# Tier 40 — Security Scanning Awareness

## 145. GitHub Actions as Orchestrator

A workflow can run:

- [ ] Dependency scanning
- [ ] Secret scanning tools
- [ ] Static analysis
- [ ] Container scanning
- [ ] SBOM generation

## 146. Security Boundary

The scanner provides the security analysis.

GitHub Actions schedules/orchestrates it.

## 147. Future DevSecOps Tree

Deep scanning policy, vulnerability management, supply-chain security, signing, provenance, and enforcement belong in DevSecOps/security trees.

---

# Tier 41 — OpenID Connect Awareness

## 148. Problem With Long-Lived Cloud Secrets

Traditional deployment may store:

```text
cloud access key
cloud secret key
```

as repository secrets.

Long-lived credentials increase risk.

## 149. OIDC Concept

Modern cloud integrations can sometimes exchange GitHub workflow identity for short-lived cloud credentials.

```text
GitHub Workflow
      │
      ▼
Identity Assertion
      │
      ▼
Cloud Provider
      │
      ▼
Short-Lived Credential
```

## 150. Scope

Understand **why** OIDC is preferred where supported.

Actual AWS IAM/OIDC configuration belongs in AWS/Cloud Security/DevSecOps trees.

---

# Tier 42 — Workflow Permissions & Security Model

## 151. Least Privilege

Set only permissions required by the workflow.

## 152. Separate CI and Deployment Authority

Conceptually:

```text
Pull Request CI
      │
      └── read/minimal authority

Production Deployment
      │
      └── controlled deployment authority
```

## 153. Protect Workflow Files

Changes under:

```text
.github/workflows/
```

can change what code runs and what credentials are requested.

Treat them as security-sensitive.

## 154. Branch Protection Relationship

CI checks can support repository merge policies.

Git/GitHub repository governance can require successful checks before merge.

Detailed GitHub administration remains outside this tree.

---

# Tier 43 — Logs and Debugging

## 155. Workflow Run Page

Be comfortable finding:

- [ ] Workflow run
- [ ] Failed job
- [ ] Failed step
- [ ] Step logs
- [ ] Artifacts
- [ ] Triggering commit/event

## 156. Start at First Failure

```text
Trigger
  │
  ▼
Runner
  │
  ▼
Checkout
  │
  ▼
Setup
  │
  ▼
Install
  │
  ▼
Build
  │
  ▼
Test
  │
  ▼
Package
  │
  ▼
Publish
  │
  ▼
Deploy
```

Find the first incorrect step.

## 157. "Command Not Found"

Check:

```text
setup action?
PATH?
runner OS?
tool version?
working directory?
```

## 158. "Works Locally, Fails in Actions"

Check:

- [ ] Runtime/tool version
- [ ] OS differences
- [ ] Case sensitivity
- [ ] Missing environment variable
- [ ] Missing secret
- [ ] Clean environment exposes undeclared dependency
- [ ] Working directory
- [ ] Network access
- [ ] File permissions

## 159. Authentication Failure

Check:

- [ ] Secret name
- [ ] Environment/repository scope
- [ ] Token permissions
- [ ] Registry/cloud permission
- [ ] Event security context

## 160. Trigger Did Not Run

Check:

- [ ] Workflow file location
- [ ] YAML validity
- [ ] Event name
- [ ] Branch/path filters
- [ ] Repository settings/permissions
- [ ] Whether the workflow existed on the relevant ref

---

# Tier 44 — Workflow Maintainability

## 161. Treat Workflow YAML as Production Code

Use:

- [ ] Clear names
- [ ] Small meaningful jobs
- [ ] Clear step names
- [ ] Comments where useful
- [ ] Code review
- [ ] Consistent formatting

## 162. Keep Build Logic in Build Tools

```text
Maven logic → pom.xml
npm logic → package.json/scripts
container logic → Dockerfile
deployment logic → deployment tool/scripts
workflow → orchestration
```

## 163. Avoid Excessive Expression Logic

If the workflow becomes a program written in YAML, move complex logic into a tested script/tool.

## 164. Prefer Readability

Another developer should quickly identify:

```text
build
test
package
publish
deploy
verify
```

---

# Tier 45 — Basic Performance

## 165. Measure Slow Work

Typical slow phases:

```text
dependency installation
compilation
tests
Docker build
artifact upload
deployment
```

## 166. Basic Improvements

- [ ] Parallel backend/frontend jobs
- [ ] Appropriate caching
- [ ] Avoid unnecessary repeated builds
- [ ] Cancel superseded branch CI where appropriate

## 167. Reliability First

Optimize after the workflow is correct and trustworthy.

---

# Tier 46 — Workflow Separation

## 168. One vs Multiple Workflow Files

A repository may use:

```text
ci.yml
deploy.yml
```

instead of one enormous workflow.

## 169. Separation Example

```text
Pull Request
    │
    ▼
ci.yml

Main / Release
    │
    ▼
deploy.yml
```

## 170. Keep Basic

For this tree, learn clear separation without building a complex reusable-workflow architecture.

---

# Tier 47 — Reusable Workflows & Custom Actions Awareness

## 171. Reusable Workflows

Know that workflows can be reused across workflows/repositories in supported configurations.

## 172. Custom Actions

Know that organizations can create custom actions.

## 173. Scope Boundary

Required mastery:

```text
recognize why they exist
read basic usage
know they reduce duplication
```

Not required:

```text
advanced reusable workflow architecture
JavaScript action development
Docker action development
large action libraries
```

---

# Tier 48 — Security Prerequisite Map

```text
                    Security Fundamentals
                           │
             ┌─────────────┼─────────────┐
             ▼             ▼             ▼
       App Security    Container      CI/CD Security
                       Security
             │             │             │
             └─────────────┼─────────────┘
                           ▼
                        DevSecOps
```

GitHub Actions relationship:

```text
Git / GitHub ─────────────┐
CI/CD Fundamentals ───────┼──► GitHub Actions
Docker ───────────────────┤
Security Fundamentals ────┘  recommended companion
```

Future security trees:

- [ ] Security Fundamentals
- [ ] Web / Application Security
- [ ] Spring Security
- [ ] DevSecOps
- [ ] Container Security
- [ ] Cloud Security
- [ ] Software Supply-Chain Security

---

# Tier 49 — Platform Comparison Awareness

GitHub Actions is one implementation of the same CI/CD model.

```text
                    CI/CD Fundamentals
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
     Jenkins         GitHub Actions      GitLab CI/CD
```

Alternative worth recognizing:

```text
Azure DevOps Pipelines
```

but it is not part of the current required tree set.

Concept comparison:

```text
Jenkins                  GitHub Actions
-------                  --------------
Jenkinsfile              workflow YAML
Pipeline                 Workflow
Agent                    Runner
Job (freestyle)          Job
Stage                    Job / logical grouping
Step                     Step
Jenkins Credential       GitHub Secret
Multibranch Pipeline     event/branch-aware workflows
input / gate             environment protection/manual flow
post                     status-conditioned cleanup/reporting
```

The mapping is approximate rather than one-to-one.

---

# Tier 50 — Full-Stack Workflow Design

## 174. Target Application

```text
Frontend: Angular or React
Backend: Spring Boot
Database: PostgreSQL
Build: Maven + npm
Tests: JUnit/Mockito + frontend tests
Runtime: Docker
```

## 175. High-Level Workflow

```text
Git Push / Pull Request
          │
          ▼
     GitHub Actions
          │
     ┌────┴────┐
     ▼         ▼
  Backend    Frontend
     │         │
   Maven      npm
     │         │
   Tests      Tests
     │         │
     └────┬────┘
          ▼
       Package
          │
          ▼
     Docker Build
          │
          ▼
       Registry
          │
          ▼
       Staging
          │
          ▼
        Verify
          │
          ▼
     Production Gate
          │
          ▼
      Production
```

---

# Practical Competency Checkpoints

A learner completing this tree should be able to:

- [ ] Explain GitHub Actions as a CI/CD implementation
- [ ] Locate workflow files under `.github/workflows/`
- [ ] Read basic YAML
- [ ] Explain workflow, job, step, runner, and action
- [ ] Create a basic workflow
- [ ] Use push and pull-request triggers
- [ ] Manually trigger a workflow
- [ ] Recognize scheduled workflows
- [ ] Use basic branch/path filters
- [ ] Use GitHub-hosted runners
- [ ] Explain self-hosted runners at a high level
- [ ] Distinguish `uses` from `run`
- [ ] Checkout repository code
- [ ] Set up Java and Node
- [ ] Run Maven backend builds
- [ ] Run npm frontend builds
- [ ] Run JUnit/Mockito and frontend tests
- [ ] Split backend/frontend work into jobs
- [ ] Use `needs`
- [ ] Define environment variables
- [ ] Use GitHub contexts and expressions
- [ ] Write basic `if` conditions
- [ ] Use secrets safely
- [ ] Explain `GITHUB_TOKEN`
- [ ] Set least-privilege workflow permissions
- [ ] Evaluate third-party action trust
- [ ] Recognize pull-request security risks
- [ ] Upload/download workflow artifacts
- [ ] Explain caching vs artifacts
- [ ] Use basic matrix strategy
- [ ] Apply timeouts
- [ ] Handle required vs non-blocking failures
- [ ] Build/tag Docker images
- [ ] Authenticate and push to a registry
- [ ] Use GitHub environments conceptually
- [ ] Protect production deployment
- [ ] Orchestrate deployment without confusing Actions with the deployment platform
- [ ] Verify deployed software
- [ ] Prevent overlapping production deployments
- [ ] Pass basic outputs between steps/jobs
- [ ] Work with frontend/backend subdirectories
- [ ] Use PostgreSQL service containers for integration testing
- [ ] Explain GitHub Actions' role in security scanning
- [ ] Explain why OIDC can be safer than long-lived cloud keys
- [ ] Troubleshoot common workflow failures
- [ ] Keep workflow YAML maintainable
- [ ] Recognize reusable workflows/custom actions without advanced authoring
- [ ] Translate the same CI/CD pipeline to Jenkins or GitLab CI/CD

---

# Suggested Practice Progression

```text
1. Create .github/workflows/ci.yml
        │
        ▼
2. Run a simple echo step
        │
        ▼
3. Trigger on push
        │
        ▼
4. Checkout repository
        │
        ▼
5. Setup Java
        │
        ▼
6. Run Maven test
        │
        ▼
7. Add pull_request trigger
        │
        ▼
8. Setup Node
        │
        ▼
9. Run frontend npm build/test
        │
        ▼
10. Split backend/frontend into jobs
        │
        ▼
11. Add job dependencies
        │
        ▼
12. Upload artifacts
        │
        ▼
13. Add dependency caching
        │
        ▼
14. Add environment variables
        │
        ▼
15. Add secrets
        │
        ▼
16. Restrict permissions
        │
        ▼
17. Build Docker images
        │
        ▼
18. Authenticate to registry
        │
        ▼
19. Push versioned images
        │
        ▼
20. Deploy staging
        │
        ▼
21. Verify staging
        │
        ▼
22. Add production environment/gate
        │
        ▼
23. Add concurrency protection
        │
        ▼
24. Add security scans
        │
        ▼
25. Debug intentional failures
        │
        ▼
26. Compare workflow directly
    with the Jenkins capstone
```

---

# Capstone — GitHub Actions Full-Stack Pipeline

The capstone should implement essentially the **same conceptual pipeline as the Jenkins tree**.

That repetition is intentional.

The learner should already know what the pipeline needs to do and focus on:

> **How does GitHub Actions express the same CI/CD process?**

## Application

```text
Angular or React
      │
      ▼
Spring Boot API
      │
      ▼
PostgreSQL
```

Docker packages the frontend/backend runtime components.

---

## Stage 1 — Repository Structure

```text
project/
├── .github/
│   └── workflows/
│       ├── ci.yml
│       └── deploy.yml        (optional separation)
├── frontend/
├── backend/
├── docker-compose.yml
└── README.md
```

---

## Stage 2 — Pull Request CI

```text
Pull Request
     │
     ▼
GitHub Actions
     │
     ├── Backend Build/Test
     └── Frontend Build/Test
```

No production deployment credentials should be required for ordinary PR validation.

---

## Stage 3 — Backend Job

```text
Checkout
   │
   ▼
Setup Java 21
   │
   ▼
./mvnw test
   │
   ▼
JUnit / Mockito
   │
   ▼
./mvnw package
```

Retain useful reports/artifacts.

---

## Stage 4 — Frontend Job

```text
Checkout
   │
   ▼
Setup Node
   │
   ▼
npm ci
   │
   ▼
frontend tests
   │
   ▼
production build
```

---

## Stage 5 — Parallel Validation

```text
                   Workflow
                      │
          ┌───────────┴───────────┐
          ▼                       ▼
       Backend                 Frontend
          │                       │
         Test                    Test
          │                       │
        Package                  Build
          │                       │
          └───────────┬───────────┘
                      ▼
                Release Eligible
```

Both required jobs must succeed.

---

## Stage 6 — Docker Build

Using the Docker prerequisite tree:

```text
Backend JAR
    │
    ▼
Backend Image

Frontend Build
    │
    ▼
Frontend Image
```

Use a traceable tag based on:

```text
Git SHA
release version
or another immutable release identity
```

---

## Stage 7 — Registry Authentication

Use appropriate:

```text
GitHub token permissions
or
registry secret
```

Do not hardcode credentials.

---

## Stage 8 — Publish

```text
GitHub Actions
      │
      ▼
Container Registry
      │
      ├── backend:<version>
      └── frontend:<version>
```

The published image should be traceable back to the workflow run and commit.

---

## Stage 9 — Staging Environment

```text
Published Images
      │
      ▼
Staging Deployment
      │
      ▼
Readiness / Health
```

Use environment-specific secrets/configuration where appropriate.

---

## Stage 10 — Smoke Test

Verify:

```text
frontend reachable
backend health endpoint
critical API request
database-backed behavior
```

If staging fails:

```text
STOP
```

---

## Stage 11 — Production Gate

Conceptually:

```text
Staging Healthy
      │
      ▼
Protected Production Environment
      │
      ▼
Required Approval / Protection
      │
      ▼
Production Deployment
```

---

## Stage 12 — Concurrency

Protect production:

```text
Production Deploy A
        │
        ▼
running

Production Deploy B
        │
        ▼
wait / controlled behavior
```

Do not allow uncontrolled overlapping production releases.

---

## Stage 13 — Security Review

Verify:

- [ ] Workflow permissions are minimal
- [ ] Secrets are not hardcoded
- [ ] Secrets are not logged
- [ ] PR workflows do not receive unnecessary production authority
- [ ] Third-party actions are trusted and versioned appropriately
- [ ] Workflow changes receive code review
- [ ] Docker registry credentials are scoped appropriately
- [ ] Production uses environment protections where appropriate
- [ ] Long-lived cloud keys are avoided where modern short-lived identity is available
- [ ] Dependencies/images can be security-scanned

---

## Stage 14 — PostgreSQL Integration Test

Add an integration-test job using an isolated PostgreSQL service.

```text
GitHub Runner
      │
      ├── PostgreSQL Service
      │
      └── Spring Boot Integration Tests
```

The test database must be disposable and isolated from real environments.

---

## Stage 15 — Failure Drills

Intentionally create:

```text
Java compilation failure
JUnit failure
frontend test failure
bad YAML
missing secret
incorrect token permission
Docker build failure
registry authentication failure
staging health failure
```

For each, answer:

```text
Did the workflow trigger?
Which job failed?
Which step failed?
What did the logs say?
Were useful artifacts retained?
Did dependent jobs stop?
Was production protected?
Is the problem in:
application code,
workflow YAML,
GitHub permissions,
Docker,
or deployment platform?
```

---

# Same Capstone Across CI/CD Tools

```text
                        Same Application
                              │
                   Spring Boot + Frontend
                         + PostgreSQL
                              │
                              ▼
                            Docker
                              │
          ┌───────────────────┼───────────────────┐
          ▼                   ▼                   ▼
       Jenkins          GitHub Actions       GitLab CI/CD
          │                   │                   │
          ▼                   ▼                   ▼
       Checkout            Checkout            Checkout
          │                   │                   │
          ▼                   ▼                   ▼
        Build               Build               Build
          │                   │                   │
          ▼                   ▼                   ▼
         Test                Test                Test
          │                   │                   │
          ▼                   ▼                   ▼
       Package             Package             Package
          │                   │                   │
          ▼                   ▼                   ▼
    Docker Build         Docker Build         Docker Build
          │                   │                   │
          ▼                   ▼                   ▼
       Publish             Publish             Publish
          │                   │                   │
          ▼                   ▼                   ▼
        Deploy              Deploy              Deploy
          │                   │                   │
          ▼                   ▼                   ▼
       Verify              Verify              Verify
```

This lets the learner distinguish:

```text
CI/CD knowledge
from
tool syntax
```

---

# Interview Readiness

Be able to answer:

- [ ] What is GitHub Actions?
- [ ] How is GitHub Actions different from CI/CD?
- [ ] Where do workflow files live?
- [ ] What is a workflow?
- [ ] What is a job?
- [ ] What is a step?
- [ ] What is a runner?
- [ ] What is an action?
- [ ] `uses` vs `run`?
- [ ] What does `on` do?
- [ ] How do you trigger CI on a pull request?
- [ ] How do you manually trigger a workflow?
- [ ] What does `runs-on` mean?
- [ ] GitHub-hosted vs self-hosted runner?
- [ ] How do you checkout repository code?
- [ ] How do you choose a Java version?
- [ ] How do you choose a Node version?
- [ ] How do you run Maven in Actions?
- [ ] How do you run npm in Actions?
- [ ] How do separate jobs run in parallel?
- [ ] What does `needs` do?
- [ ] How do environment variables work?
- [ ] What are GitHub Actions contexts?
- [ ] What does `${{ ... }}` mean?
- [ ] What does `if` do?
- [ ] How should passwords/tokens be stored?
- [ ] What is `GITHUB_TOKEN`?
- [ ] Why should workflow permissions use least privilege?
- [ ] Why can third-party actions be a security risk?
- [ ] Why are pull-request workflows security-sensitive?
- [ ] What are workflow artifacts?
- [ ] Artifact vs cache?
- [ ] What is a matrix strategy?
- [ ] How do you prevent a stuck job from running forever?
- [ ] What does `continue-on-error` do and why should it be used carefully?
- [ ] How would Actions build and push a Docker image?
- [ ] How would you tag an image so it is traceable?
- [ ] What is a GitHub environment?
- [ ] How can production deployment be protected?
- [ ] How do concurrency controls help deployments?
- [ ] What are step/job outputs?
- [ ] How do you run commands from `backend/` and `frontend/`?
- [ ] What is a service container?
- [ ] How can PostgreSQL be used during integration testing?
- [ ] What role does Actions play in security scanning?
- [ ] What is OIDC at a high level?
- [ ] Why can OIDC be preferable to long-lived cloud credentials?
- [ ] How would you debug a workflow that never triggered?
- [ ] How would you debug "works locally, fails in GitHub Actions"?
- [ ] How would you debug a registry authentication failure?
- [ ] How should workflow YAML be structured for maintainability?
- [ ] What are reusable workflows/custom actions?
- [ ] How does GitHub Actions map to Jenkins concepts?
- [ ] How would you build a CI/CD workflow for Spring Boot + Angular/React?

---

# Common Anti-Patterns

## Anti-Pattern 1 — GitHub Actions Equals CI/CD

Learn CI/CD Fundamentals first.

## Anti-Pattern 2 — Hardcoded Secrets

Never commit deployment/registry/cloud credentials to workflow YAML.

## Anti-Pattern 3 — Broad Token Permissions Everywhere

Give workflows only the authority they need.

## Anti-Pattern 4 — Trusting Every Marketplace Action

Actions execute code. Evaluate the source and version.

## Anti-Pattern 5 — Giving PR Builds Production Secrets

Untrusted code should not receive powerful deployment credentials.

## Anti-Pattern 6 — Giant YAML Program

Move complex logic into normal scripts/build tools.

## Anti-Pattern 7 — Build Logic Duplicated From Maven/npm

Let Maven/npm own project build behavior.

## Anti-Pattern 8 — `latest` as the Only Docker Tag

Keep immutable/traceable image identity.

## Anti-Pattern 9 — Cache as Required State

The workflow should work from an empty cache.

## Anti-Pattern 10 — `continue-on-error` to Hide Required Failures

A red test should remain meaningful.

## Anti-Pattern 11 — Deployment Command Means Deployment Success

Verify the running system.

## Anti-Pattern 12 — Overlapping Production Deployments

Use appropriate concurrency/control.

## Anti-Pattern 13 — Long-Lived Cloud Keys by Default

Prefer short-lived identity such as OIDC where the target platform supports it and the architecture warrants it.

## Anti-Pattern 14 — Testing Against Production Database

CI integration tests use isolated disposable test infrastructure.

## Anti-Pattern 15 — Copy/Pasting Workflow YAML Without Understanding

Translate every important section back to its CI/CD concept.

---

# Relationship to Other Skill Trees

```text
Git
 │
 ▼
CI/CD Fundamentals
 │
 ├──────────────────────┐
 ▼                      ▼
GitHub Actions         Docker
 │                      │
 └───────────┬──────────┘
             ▼
   Practical GitHub CI/CD
             │
             ▼
Deployment Fundamentals
             │
      ┌──────┼──────┐
      ▼      ▼      ▼
     AWS Kubernetes Other Targets
```

Backend:

```text
Java
 │
 ├── Maven
 ├── JUnit
 ├── Mockito
 └── Spring Boot
        │
        ▼
 GitHub Actions Backend CI
```

Frontend:

```text
JavaScript / TypeScript
        │
        ▼
 Angular / React
        │
        ▼
       npm
        │
        ▼
 GitHub Actions Frontend CI
```

Security:

```text
Security Fundamentals
        │
        ├───────────────┐
        ▼               ▼
CI/CD Security      Container Security
        │               │
        └───────┬───────┘
                ▼
             DevSecOps
```

Sibling CI/CD implementations:

```text
                   CI/CD Fundamentals
                          │
         ┌────────────────┼────────────────┐
         ▼                ▼                ▼
      Jenkins       GitHub Actions    GitLab CI/CD
                                             │
                                  Alternative mention:
                                  Azure DevOps Pipelines
```

---

# Mastery Progression

```text
"I understand CI/CD"
        │
        ▼
"I know where GitHub workflow files live"
        │
        ▼
"I understand workflow / job / step / runner"
        │
        ▼
"I can trigger CI from push and pull request"
        │
        ▼
"I can checkout code and configure tools"
        │
        ▼
"I can build and test backend/frontend"
        │
        ▼
"I can coordinate multiple jobs"
        │
        ▼
"I can use artifacts and caches correctly"
        │
        ▼
"I can use secrets and permissions safely"
        │
        ▼
"I can build and publish Docker images"
        │
        ▼
"I can deploy and verify staging"
        │
        ▼
"I can protect production deployment"
        │
        ▼
"I understand PR/action/token security"
        │
        ▼
"I can troubleshoot workflow failures"
        │
        ▼
"I can maintain readable workflow YAML"
        │
        ▼
"I can translate the same pipeline
between Jenkins, GitHub Actions,
and later GitLab CI/CD"
```

---

# Mastery Standard

> **Can I take a CI/CD pipeline that I already understand conceptually and independently implement it using GitHub Actions—creating clear workflow YAML with appropriate triggers, runners, jobs, steps, Maven/npm builds, automated tests, artifacts, Docker image publishing, secrets, least-privilege permissions, staging deployment verification, production protection, and concurrency controls—while recognizing pull-request and third-party-action security risks, troubleshooting common failures, and keeping deployment-platform details in their appropriate skill trees?**

Final mental model:

```text
                         GitHub Repository
                                │
                    push / pull_request
                                │
                                ▼
                         GitHub Actions
                                │
                                ▼
                            Workflow
                                │
                  ┌─────────────┴─────────────┐
                  ▼                           ▼
              Backend                     Frontend
                  │                           │
              Setup Java                  Setup Node
                  │                           │
                Maven                        npm
                  │                           │
                Tests                       Tests
                  │                           │
               Package                      Build
                  │                           │
                  └─────────────┬─────────────┘
                                ▼
                           Docker Build
                                │
                                ▼
                         Versioned Images
                                │
                                ▼
                         Registry Publish
                                │
                                ▼
                         Deploy Staging
                                │
                                ▼
                              Verify
                                │
                                ▼
                    Protected Production
                           Environment
                                │
                                ▼
                         Deploy Production
                                │
                                ▼
                        Observe / Report


GitHub Actions is one implementation:

                    CI/CD Fundamentals
                           │
         ┌─────────────────┼─────────────────┐
         ▼                 ▼                 ▼
      Jenkins        GitHub Actions      GitLab CI/CD

                         Alternative:
                   Azure DevOps Pipelines
```
