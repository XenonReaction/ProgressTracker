# GitLab CI/CD Full-Stack Developer Skill Tree

> **Goal:** Learn how to implement the concepts from the **CI/CD Fundamentals** skill tree using **GitLab CI/CD** at the developer level.
>
> **Target level:** Full-stack developer who can read, write, run, troubleshoot, and maintain practical GitLab CI/CD pipelines for Java/Spring Boot and modern frontend applications.
>
> **Primary prerequisites:** **Git / Version Control** and **CI/CD Fundamentals**
>
> **Practical prerequisite:** **Docker** for container image build, registry, and capstone portions.
>
> **Supporting prerequisites:** GitLab repository basics, YAML basics, Maven, JUnit/Mockito, npm-based frontend tooling, basic Linux/command-line use, and basic HTTP/webhook concepts.
>
> **Recommended security companion:** A future **Security Fundamentals** tree should provide the broader security foundation. This tree introduces the GitLab CI/CD security practices developers need immediately: protected variables, masked/hidden variables awareness, protected branches/tags, least privilege, runner trust, untrusted merge-request code, deployment environments, and safe registry authentication. A future **DevSecOps** tree can build on these concepts.
>
> **Scope boundary:** This tree focuses specifically on **GitLab CI/CD features**. General GitLab source-control usage belongs in the Git / Version Control tree. Advanced GitLab administration, large runner fleets, enterprise governance, advanced reusable CI component architecture, and platform administration are outside scope.
>
> **Deployment boundary:** GitLab CI/CD can orchestrate deployment, but AWS, Kubernetes, cloud infrastructure, and advanced deployment strategies belong in **Deployment Fundamentals** and platform-specific skill trees.

---

# Skill Tree Overview

```text
Git / Version Control
        │
        ▼
CI/CD Fundamentals
        │
        ├───────────────┐
        ▼               ▼
   GitLab CI/CD       Docker
        │               │
        └───────┬───────┘
                ▼
            Pipeline
                │
       ┌────────┼────────┐
       ▼        ▼        ▼
     Build     Test    Package
       │        │        │
       └────────┼────────┘
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
CI/CD Concept              GitLab CI/CD Implementation
-------------              ---------------------------
Pipeline as code           .gitlab-ci.yml
Pipeline                   Pipeline
Execution unit             Job
Pipeline phase             Stage
Execution machine          Runner
Command                    script
Trigger/rules              rules / workflow / pipeline source
Environment value          CI/CD variable
Secret                     Protected/masked CI/CD variable
Artifact                   artifacts
Cache                      cache
Job dependency             needs
Conditional execution      rules
Manual gate                when: manual
Deployment target          environment
Container registry         GitLab Container Registry / external registry
Pipeline result            job/pipeline status
```

The purpose of this tree is to learn how GitLab expresses CI/CD concepts you already understand—not to memorize YAML without understanding it.

---

# Tier 0 — What GitLab CI/CD Is

## 1. Definition

GitLab CI/CD is GitLab's integrated automation system for building, testing, packaging, publishing, and deploying software.

```text
Git Change
    │
    ▼
GitLab Repository
    │
    ▼
GitLab CI/CD
    │
    ▼
Pipeline
```

## 2. GitLab CI/CD Is Not CI/CD Itself

```text
CI/CD
= engineering practices

GitLab CI/CD
= one implementation platform
```

## 3. Integrated Platform Model

GitLab combines source hosting and CI/CD closely:

```text
Repository
   │
   ├── Code
   ├── Merge Request
   ├── .gitlab-ci.yml
   └── Pipeline
```

**Checkpoint:** Explain what GitLab CI/CD contributes without defining CI/CD as "GitLab pipelines."

---

# Tier 1 — `.gitlab-ci.yml`

## 4. Pipeline File

The primary pipeline definition normally lives at:

```text
.gitlab-ci.yml
```

in the repository.

## 5. Pipeline as Code

Benefits:

- [ ] Version controlled
- [ ] Reviewable
- [ ] Branchable
- [ ] Traceable
- [ ] Reproducible

## 6. YAML Basics

Know enough YAML to understand:

- [ ] mappings
- [ ] lists
- [ ] indentation
- [ ] strings
- [ ] multiline commands
- [ ] comments

## 7. Minimal Mental Model

```yaml
stages:
  - test

test-backend:
  stage: test
  script:
    - ./mvnw test
```

---

# Tier 2 — Pipelines

## 8. Pipeline

A pipeline is the complete CI/CD execution associated with a GitLab event/ref.

```text
Pipeline
├── Build
├── Test
├── Package
├── Publish
└── Deploy
```

## 9. Pipeline Status

Recognize outcomes such as:

```text
passed
failed
running
pending
canceled
skipped
manual
```

## 10. Pipeline Source Awareness

Pipelines may originate from:

- [ ] Push
- [ ] Merge request
- [ ] Manual execution
- [ ] Schedule
- [ ] Tag
- [ ] API/trigger awareness

---

# Tier 3 — Stages

## 11. Stage Concept

Stages group jobs into broad phases.

```yaml
stages:
  - build
  - test
  - package
  - deploy
```

## 12. Default Ordering

Conceptually:

```text
Stage 1
   │
   ▼
Stage 2
   │
   ▼
Stage 3
```

Jobs within the same stage can often run independently/parallel when runners are available.

## 13. Meaningful Stages

Prefer:

```text
build
test
package
publish
deploy
```

over arbitrary names.

## 14. Stage vs Job

```text
Stage
= pipeline phase

Job
= executable unit of work
```

---

# Tier 4 — Jobs

## 15. Job Definition

A job is a top-level pipeline unit.

```yaml
backend-test:
  stage: test
  script:
    - ./mvnw test
```

## 16. Job Names

Use descriptive names:

```text
backend-test
frontend-test
docker-build
deploy-staging
```

## 17. Job Independence

Jobs in the same stage may execute in parallel.

## 18. Job Failure

A required failed job normally causes the pipeline to fail and blocks later dependent work.

---

# Tier 5 — `script`

## 19. Script Commands

The `script` section defines commands executed by the runner.

```yaml
script:
  - ./mvnw test
```

## 20. Multiple Commands

```yaml
script:
  - npm ci
  - npm test
  - npm run build
```

## 21. Keep Build Logic in Build Tools

Prefer:

```text
Maven → pom.xml
npm → package.json
Docker → Dockerfile
GitLab CI → orchestration
```

Do not turn `.gitlab-ci.yml` into the application's build system.

---

# Tier 6 — Runners

## 22. Runner Definition

A GitLab Runner executes CI/CD jobs.

```text
GitLab
   │
   ▼
Runner
   │
   ▼
Job
```

## 23. Runner Environment

The runner must provide the environment/capabilities the job requires.

Examples:

```text
Linux
Java
Node
Docker capability
```

## 24. Hosted vs Self-Managed Awareness

Depending on GitLab environment/plan/configuration, jobs may use GitLab-hosted or self-managed runners.

Developer-level goal:

```text
understand what executes the job
```

not:

```text
administer a large runner fleet
```

## 25. Runner Trust

A runner executing sensitive deployment work is security-sensitive.

Do not assume every runner should receive production credentials.

---

# Tier 7 — Runner Tags

## 26. Tags

Runner tags can help route jobs to appropriate runners.

Concept:

```text
job requires:
docker

runner tagged:
docker
```

## 27. Use Case

Different jobs may need different capabilities.

```text
backend → Java-capable runner
frontend → Node-capable runner
deployment → controlled deployment runner
```

## 28. Scope Boundary

Runner registration and fleet architecture belong to GitLab administration.

---

# Tier 8 — Container-Based Jobs

## 29. `image`

Many GitLab CI jobs can execute inside a specified container image when using an appropriate runner executor.

Concept:

```yaml
image: maven:...
```

## 30. Reproducible Environment

A controlled image can provide:

```text
known Java version
known Maven version
known Node version
```

## 31. Boundary With Docker

Understand why containerized CI environments help reproducibility, but Docker fundamentals remain in the Docker tree.

---

# Tier 9 — Source Checkout

## 32. Repository Availability

GitLab Runner normally prepares the repository for the job according to runner/job configuration.

## 33. Exact Revision

The job should operate on the revision associated with the pipeline.

## 34. Traceability

```text
Pipeline
   │
   ▼
Commit SHA
   │
   ▼
Artifact/Image
```

Be able to identify which commit produced an artifact.

---

# Tier 10 — Predefined CI/CD Variables

## 35. Predefined Variables

GitLab exposes useful runtime metadata as predefined variables.

Conceptual examples:

```text
commit SHA
branch/ref
pipeline ID
project information
registry information
```

## 36. Why They Matter

Use pipeline metadata to create:

```text
traceable image tags
artifact names
conditional behavior
deployment metadata
```

## 37. Do Not Memorize Everything

Learn how to find the appropriate predefined variable in GitLab documentation.

---

# Tier 11 — Custom CI/CD Variables

## 38. Variables in YAML

Non-sensitive configuration can be defined in the pipeline.

Concept:

```yaml
variables:
  APP_NAME: "guestbook"
```

## 39. Scope

Variables can exist at different scopes/configuration levels.

Understand that more specific values may override broader ones depending on GitLab configuration.

## 40. Configuration vs Secret

Do not put sensitive passwords/tokens directly into committed YAML.

---

# Tier 12 — Protected / Masked Variables

## 41. Sensitive CI/CD Variables

GitLab CI/CD settings can store sensitive values outside the repository.

Examples:

```text
registry credentials
deployment tokens
API keys
```

## 42. Masked Variables

Masking helps prevent eligible secret values from appearing plainly in job logs.

Do not treat masking as permission to print secrets.

## 43. Protected Variables

Protected variables can restrict secret availability to protected branches/tags according to GitLab configuration.

Mental model:

```text
feature branch
      │
      └── no production secret

protected main/tag
      │
      └── controlled secret access
```

## 44. Secret Safety

- [ ] Never commit secrets
- [ ] Never intentionally echo secrets
- [ ] Keep secret scope narrow
- [ ] Avoid writing secrets into artifacts
- [ ] Avoid embedding secrets in images

---

# Tier 13 — Maven Backend CI

## 45. Backend Job

```text
Runner
  │
  ▼
Java/Maven Environment
  │
  ▼
./mvnw clean verify
```

## 46. Maven Wrapper

Use the project's Maven Wrapper when available.

## 47. Typical Flow

```text
Compile
   │
   ▼
JUnit / Mockito
   │
   ▼
Package
```

## 48. Build Ownership

Maven owns Java dependency/build behavior.

GitLab orchestrates it.

---

# Tier 14 — npm Frontend CI

## 49. Frontend Job

```text
Node Environment
      │
      ▼
    npm ci
      │
      ▼
     test
      │
      ▼
     build
```

## 50. Angular / React

The pipeline concepts remain the same.

The npm scripts determine framework-specific behavior.

## 51. Deterministic Install

Use lockfile-based CI installation practices such as `npm ci` where appropriate.

---

# Tier 15 — Automated Testing

## 52. Required Tests

Required test failures should block release work.

## 53. Backend Tests

Run:

```text
JUnit
Mockito
Spring tests
```

through Maven.

## 54. Frontend Tests

Run the configured frontend tests.

## 55. Integration Tests

Integration tests may require supporting services such as PostgreSQL.

---

# Tier 16 — Test Reports

## 56. JUnit-Format Reports

GitLab can consume supported test reports, including JUnit-style reports.

Concept:

```text
Test Framework
     │
     ▼
JUnit XML
     │
     ▼
GitLab Pipeline
     │
     ▼
Visible Test Information
```

## 57. Why Publish Reports

Useful for:

- [ ] Failed test visibility
- [ ] Merge request feedback
- [ ] Test counts
- [ ] Pipeline diagnosis

## 58. Framework Independence

JUnit XML is a reporting format usable by more than Java JUnit itself.

---

# Tier 17 — Artifacts

## 59. Artifact Concept

Artifacts are intentional job outputs.

Examples:

```text
JAR
frontend build
test reports
generated package
```

## 60. Artifact Configuration

Jobs can declare files/directories to preserve.

## 61. Artifact Transfer

Later jobs can consume earlier build outputs.

Mental model:

```text
Build
  │
  ▼
Artifact
  │
  ▼
Publish/Deploy
```

## 62. Retention Awareness

Artifacts are not necessarily permanent.

Long-term release artifacts may belong in package/container registries.

---

# Tier 18 — Cache

## 63. Cache Purpose

Cache improves pipeline performance.

Potential targets:

```text
Maven dependency cache
npm cache
```

## 64. Cache vs Artifact

```text
Cache
= speed optimization

Artifact
= intended output
```

## 65. Correctness First

A pipeline should remain correct if the cache disappears.

---

# Tier 19 — `needs`

## 66. Job Dependencies

`needs` can express direct job dependencies and enable DAG-style execution.

Concept:

```text
backend-test ──┐
               ├── docker-build
frontend-test ─┘
```

## 67. Why It Matters

The pipeline does not always need to wait for every job in an entire earlier stage if the actual dependency graph allows earlier execution.

## 68. Start Simple

Understand stages first, then use `needs` when dependency relationships justify it.

---

# Tier 20 — Parallelism

## 69. Same-Stage Parallelism

Independent jobs can run simultaneously.

```text
             ┌── backend-test
test stage ──┤
             └── frontend-test
```

## 70. Benefit

Reduces total feedback time.

## 71. Cost

Can increase:

- [ ] Runner demand
- [ ] Resource use
- [ ] Complexity

---

# Tier 21 — `rules`

## 72. Conditional Job Inclusion

`rules` determines whether/how jobs are included based on pipeline conditions.

## 73. Common Conditions

Conceptually:

```text
merge request?
main branch?
tag?
changed path?
pipeline source?
```

## 74. Deployment Safety

Production deployment should have explicit conditions.

## 75. Avoid Rule Spaghetti

Keep rules understandable.

---

# Tier 22 — `workflow: rules` Awareness

## 76. Pipeline-Level Control

GitLab can control whether an entire pipeline is created using workflow-level rules.

## 77. Use Case

Prevent unnecessary or duplicate pipeline types when repository workflow requires it.

## 78. Basic Scope

Know why it exists.

Do not make highly complex pipeline-source orchestration a beginner requirement.

---

# Tier 23 — Merge Request Pipelines

## 79. Merge Request Validation

Concept:

```text
Feature Branch
      │
      ▼
Merge Request
      │
      ▼
GitLab Pipeline
      │
      ├── Build
      ├── Test
      └── Quality Checks
```

## 80. Merge Safety

Required pipeline success can support merge policies.

General repository approval/governance belongs to Git/GitLab administration topics.

## 81. Security

Merge-request code may be untrusted.

Do not automatically expose powerful protected credentials to untrusted pipeline code.

---

# Tier 24 — Branch and Tag Pipelines

## 82. Branch Behavior

Different branches may have different pipeline responsibilities.

Example:

```text
feature branch → validation
main           → validation + publish
```

## 83. Tags

Tags can represent release events.

Example:

```text
v1.4.0
   │
   ▼
release pipeline
```

## 84. Protected Branch/Tag Awareness

GitLab can protect important refs and connect those protections to sensitive CI/CD behavior.

---

# Tier 25 — Manual Jobs

## 85. `when: manual`

A job can require manual initiation.

Concept:

```text
Staging Verified
      │
      ▼
Deploy Production
   [manual]
```

## 86. Continuous Delivery

This supports a pipeline where production release is prepared but remains a deliberate action.

## 87. Use Approvals Deliberately

Do not require manual clicks for every harmless CI step.

---

# Tier 26 — Scheduled Pipelines

## 88. Schedule

GitLab can run pipelines on schedules.

Potential use cases:

```text
nightly integration suite
periodic maintenance
scheduled security scan
```

## 89. Not a Push Replacement

Normal CI should usually react to source changes rather than wait for a schedule.

---

# Tier 27 — Pipeline Variables / Manual Inputs Awareness

## 90. Runtime Configuration

Manually started pipelines can receive controlled variables/inputs depending on configuration.

Potential examples:

```text
target environment
release identifier
optional operation
```

## 91. Avoid Arbitrary Dangerous Inputs

Do not allow uncontrolled user input to become shell commands.

## 92. Prefer Automatic Derivation

Commit SHA/version/environment should be derived automatically where practical.

---

# Tier 28 — Docker Integration

## 93. Prerequisite Boundary

Assume knowledge of:

```text
Dockerfile
image
container
tag
registry
network
volume
```

## 94. Build Image

GitLab jobs can orchestrate Docker image creation using an appropriate runner/build approach.

Concept:

```text
Application Artifact
      │
      ▼
docker build
      │
      ▼
Image
```

## 95. Runner Capability

The runner must support the chosen container build method.

Deep runner/container executor configuration is outside developer scope.

## 96. Security Awareness

Docker daemon access can be highly privileged.

Do not casually grant powerful Docker access to untrusted jobs.

---

# Tier 29 — GitLab Container Registry

## 97. Integrated Registry Awareness

GitLab projects can integrate with a container registry.

Concept:

```text
GitLab Pipeline
      │
      ▼
Build Image
      │
      ▼
GitLab Container Registry
```

## 98. Registry Variables

GitLab provides useful registry-related predefined variables in supported configurations.

## 99. Authentication

Use appropriate CI-provided credentials/tokens rather than hardcoded passwords.

## 100. External Registries

The same conceptual pipeline can publish to external registries.

---

# Tier 30 — Image Tagging

## 101. Traceable Tags

Prefer tags based on:

```text
commit SHA
release version
Git tag
controlled pipeline identity
```

## 102. Avoid `latest` Alone

You should be able to answer:

> Which source revision produced the running image?

## 103. Promotion

Prefer promoting the same tested image rather than rebuilding unrelated production output later.

---

# Tier 31 — Environments

## 104. Environment Concept

GitLab environments represent deployment targets such as:

```text
development
staging
production
```

## 105. Deployment Tracking

Environment features can help track deployment state/history.

## 106. Environment-Specific Behavior

Use controlled variables and rules to distinguish environments.

## 107. Scope Boundary

Infrastructure configuration still belongs to the deployment platform.

---

# Tier 32 — Staging Deployment

## 108. Staging First

```text
Published Image
      │
      ▼
Deploy Staging
      │
      ▼
Verify
```

## 109. GitLab's Role

GitLab CI/CD invokes the deployment mechanism.

It does not replace:

```text
Docker
AWS
Kubernetes
deployment scripts/tools
```

## 110. Configuration

Staging should use staging-specific configuration/secrets.

---

# Tier 33 — Deployment Verification

## 111. Verify

After deployment:

```text
health endpoint
smoke test
frontend availability
critical API behavior
```

## 112. Failure

If staging verification fails:

```text
do not promote production
```

## 113. Production

Production should also receive appropriate post-deployment verification.

---

# Tier 34 — Production Deployment

## 114. Controlled Production Job

A production job should have:

- [ ] Appropriate branch/tag rule
- [ ] Appropriate runner
- [ ] Appropriate protected credentials
- [ ] Appropriate environment
- [ ] Manual/protected gate where required

## 115. Separation of Authority

```text
CI validation
≠
automatic production authority
```

## 116. Deployment Strategy Boundary

Before designing production deployment jobs, understand:

- [ ] Recreate
- [ ] Rolling
- [ ] Blue/green
- [ ] Canary
- [ ] Health/readiness
- [ ] Rollback
- [ ] Database compatibility

from Deployment Fundamentals.

---

# Tier 35 — Resource Groups / Deployment Concurrency Awareness

## 117. Overlapping Deployment Risk

Two production jobs running simultaneously can conflict.

## 118. Serialization Concept

GitLab provides mechanisms such as resource-group-style control for mutually exclusive deployment work.

Mental model:

```text
Production Deploy A
      │
      ▼
running

Production Deploy B
      │
      ▼
wait
```

## 119. Use for Shared Targets

Protect deployment targets that should receive one deployment operation at a time.

---

# Tier 36 — Job Failure Controls

## 120. Required Failure

Required build/test jobs should fail the pipeline when their command fails.

## 121. `allow_failure` Awareness

GitLab can mark some jobs as non-blocking.

Use for genuinely informational/experimental checks.

## 122. Do Not Hide Broken Tests

Bad:

```text
unit tests fail
+
allow_failure
=
green-looking pipeline
```

unless the check is intentionally non-required.

---

# Tier 37 — Retry and Timeout Awareness

## 123. Retry

Retries may help with transient infrastructure/network failures.

## 124. Deterministic Failures

Do not retry compilation or deterministic unit-test failures as a substitute for fixing them.

## 125. Timeout

Jobs should have appropriate time limits so stuck work does not run indefinitely.

---

# Tier 38 — `before_script` / `after_script`

## 126. Setup Commands

Common setup commands can be executed before main job scripts.

## 127. Cleanup / Diagnostics

Post-job commands can support cleanup or diagnostics where appropriate.

## 128. Keep Jobs Understandable

Do not hide all important behavior in global setup/cleanup sections.

A reader should still understand the job.

---

# Tier 39 — Reuse and `extends`

## 129. Duplication

Large pipeline files may repeat configuration.

## 130. `extends`

GitLab CI/CD supports configuration reuse patterns such as `extends`.

Concept:

```text
base job configuration
        │
        ├── backend-test
        └── frontend-test
```

## 131. YAML Anchors Awareness

YAML itself also has reuse mechanisms.

Recognize them, but prioritize GitLab-native understandable configuration patterns.

## 132. Scope

Basic reuse is useful.

Advanced CI framework/component architecture is outside this tree.

---

# Tier 40 — `include` Awareness

## 133. Split Configuration

GitLab CI/CD can include configuration from other files/sources.

## 134. Why It Exists

Useful when pipelines become larger or organizations standardize common behavior.

## 135. Basic Scope

Required:

```text
know what include does
read an included configuration
understand that pipeline logic may live elsewhere
```

Not required:

```text
design enterprise-wide CI template ecosystems
```

---

# Tier 41 — Services

## 136. Service Containers

Jobs can use service containers where supported by the runner/executor.

Example:

```text
Test Job
├── Application Test Container
└── PostgreSQL Service
```

## 137. PostgreSQL Integration Testing

A Spring Boot integration test can connect to an isolated PostgreSQL service.

## 138. Production Boundary

A CI service container is disposable test infrastructure—not the production database architecture.

---

# Tier 42 — Full-Stack Repository Layout

## 139. Example

```text
project/
├── .gitlab-ci.yml
├── backend/
├── frontend/
├── docker-compose.yml
└── README.md
```

## 140. Working Directories

Commands need to execute in the correct directory.

Example concept:

```text
cd backend
./mvnw test

cd frontend
npm ci
npm test
```

## 141. Avoid Fragile Directory Assumptions

Make repository paths obvious and consistent.

---

# Tier 43 — Pipeline Security

## 142. Pipeline Code Is Executable

A change to `.gitlab-ci.yml` can alter commands executed by runners.

Treat CI changes as security-sensitive code.

## 143. Least Privilege

A test job should not receive production credentials merely because it is part of the same repository.

## 144. Runner Isolation

Sensitive runners should not casually execute untrusted code.

## 145. Protected Resources

Use GitLab protections appropriately around:

```text
branches
tags
variables
environments/deployment processes
```

---

# Tier 44 — Supply-Chain Security Awareness

## 146. Pipeline Dependencies

A GitLab pipeline may trust:

```text
Maven packages
npm packages
Docker base images
included CI templates
external scripts
container registries
```

## 147. Security Jobs

GitLab pipelines can orchestrate:

- [ ] Dependency analysis
- [ ] Static analysis
- [ ] Secret detection
- [ ] Container scanning
- [ ] SBOM generation

Specific GitLab security products/features can vary by edition/tier and evolve over time.

## 148. Scope Boundary

Deep vulnerability management, policy enforcement, signing, provenance, and supply-chain governance belong in future DevSecOps/security trees.

---

# Tier 45 — OpenID Connect (OIDC) Awareness

## 149. Problem With Long-Lived Cloud Secrets

Traditional deployment may store:

```text
cloud access key
cloud secret key
```

as CI/CD variables.

Long-lived credentials increase risk.

## 150. `id_tokens` / OIDC Concept

GitLab CI/CD can request a short-lived, GitLab-signed OIDC token and exchange it for temporary cloud credentials, instead of storing long-lived cloud keys as CI/CD variables.

```text
GitLab Pipeline
      │
      ▼
id_tokens Request
      │
      ▼
GitLab-Signed OIDC Token
      │
      ▼
Cloud Provider IAM Trust Policy
      │
      ▼
Short-Lived Credential
```

Conceptual `.gitlab-ci.yml` example:

```yaml
deploy:
  id_tokens:
    AWS_ID_TOKEN:
      aud: https://gitlab.com
  script:
    - aws sts assume-role-with-web-identity --role-arn $ROLE_ARN --web-identity-token $AWS_ID_TOKEN ...
```

The cloud provider's IAM trust policy verifies the token and issues temporary credentials; no long-lived key is stored in GitLab.

## 151. Scope

Understand **why** federated, short-lived credentials are preferred where supported.

Actual AWS/GCP/Azure IAM trust-policy configuration belongs in Cloud Security/DevSecOps trees.

---

# Tier 46 — Logs and Pipeline UI

## 152. Pipeline View

Be comfortable locating:

- [ ] Pipeline
- [ ] Stage
- [ ] Job
- [ ] Job log
- [ ] Artifact
- [ ] Trigger/source
- [ ] Commit
- [ ] Environment/deployment information

## 153. Start With First Failure

```text
Pipeline created?
      │
Runner assigned?
      │
Repository prepared?
      │
Dependencies installed?
      │
Build?
      │
Tests?
      │
Artifact?
      │
Docker?
      │
Registry?
      │
Deploy?
```

## 154. Job Log

Look for:

```text
command
exit code
missing file
permission error
authentication failure
network error
test failure
```

---

# Tier 47 — Troubleshooting

## 155. Pipeline Did Not Start

Check:

- [ ] `.gitlab-ci.yml` location
- [ ] YAML/config validity
- [ ] `workflow`/`rules`
- [ ] Branch/tag
- [ ] Pipeline source
- [ ] Repository/project settings

## 156. Job Stuck / Pending

Check:

- [ ] Runner available?
- [ ] Required tags?
- [ ] Runner online?
- [ ] Runner permitted for project/ref?

Infrastructure issues may require administrator help.

## 157. "Command Not Found"

Check:

```text
job image/environment
PATH
runner capabilities
tool setup
```

## 158. "Works Locally, Fails in GitLab"

Check:

- [ ] Runtime version
- [ ] Container/runner OS
- [ ] Missing environment variable
- [ ] Missing secret
- [ ] Working directory
- [ ] File permissions
- [ ] Case sensitivity
- [ ] Undeclared dependency
- [ ] Network access

## 159. Registry Failure

Check:

- [ ] Registry address
- [ ] Credentials/token
- [ ] CI variable
- [ ] Permission
- [ ] Image name/tag
- [ ] Runner network

## 160. Deployment Failure

Separate:

```text
GitLab pipeline problem
from
deployment-platform problem
```

---

# Tier 48 — Maintainability

## 161. Treat `.gitlab-ci.yml` as Production Code

Use:

- [ ] Clear job names
- [ ] Meaningful stages
- [ ] Comments where helpful
- [ ] Code review
- [ ] Consistent formatting
- [ ] Minimal duplication

## 162. Keep CI/CD Concepts Visible

A reader should quickly find:

```text
build
test
package
publish
deploy
verify
```

## 163. Avoid YAML Programming

Complex application logic belongs in normal scripts/programs.

## 164. Reuse Carefully

Abstraction should reduce duplication without hiding the pipeline's purpose.

---

# Tier 49 — Security Prerequisite Map

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

GitLab CI/CD relationship:

```text
Git ──────────────────────┐
CI/CD Fundamentals ───────┼──► GitLab CI/CD
Docker ───────────────────┤
Security Fundamentals ────┘  recommended companion
```

Future related trees:

- [ ] Security Fundamentals
- [ ] Web / Application Security
- [ ] Spring Security
- [ ] DevSecOps
- [ ] Container Security
- [ ] Cloud Security
- [ ] Software Supply-Chain Security

---

# Tier 50 — Platform Comparison

```text
                    CI/CD Fundamentals
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
     Jenkins         GitHub Actions      GitLab CI/CD
```

Alternative pipeline platform to recognize:

```text
Azure DevOps Pipelines
```

Approximate translation:

```text
Jenkins               GitHub Actions        GitLab CI/CD
-------               --------------        -------------
Jenkinsfile           workflow YAML         .gitlab-ci.yml
Pipeline              Workflow              Pipeline
Agent                 Runner                Runner
Job (freestyle)       Job                   Job
Stage                 Job/logical phase     Stage
Step                  Step                  script command
Credential            Secret                CI/CD variable
Multibranch           branch/event flow     branch/MR pipelines
input                  environment gate      manual job
artifact archive      workflow artifact     artifact
```

These mappings are conceptual, not exact one-to-one equivalents.

---

# Tier 51 — Full-Stack Pipeline Design

## 165. Target Stack

```text
Frontend: Angular or React
Backend: Spring Boot
Database: PostgreSQL
Build: Maven + npm
Tests: JUnit/Mockito + frontend tests
Runtime: Docker
```

## 166. High-Level Pipeline

```text
Push / Merge Request
        │
        ▼
   GitLab CI/CD
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

- [ ] Explain GitLab CI/CD as an implementation of CI/CD
- [ ] Create/read `.gitlab-ci.yml`
- [ ] Explain pipelines, stages, jobs, scripts, and runners
- [ ] Understand runner tags at developer level
- [ ] Use controlled job images/environments
- [ ] Understand GitLab's repository checkout behavior conceptually
- [ ] Use predefined CI/CD variables
- [ ] Define non-secret variables
- [ ] Use protected/masked CI/CD variables safely
- [ ] Run Maven backend builds
- [ ] Run npm frontend builds
- [ ] Run JUnit/Mockito/frontend tests
- [ ] Publish JUnit-style test reports
- [ ] Create/consume artifacts
- [ ] Explain cache vs artifact
- [ ] Use `needs`
- [ ] Run independent jobs in parallel
- [ ] Write basic `rules`
- [ ] Recognize `workflow: rules`
- [ ] Build merge-request pipelines
- [ ] Differentiate branch/tag behavior
- [ ] Create manual jobs
- [ ] Recognize scheduled pipelines
- [ ] Use runtime variables/inputs carefully
- [ ] Build Docker images using prerequisite Docker knowledge
- [ ] Publish images to GitLab/external container registries
- [ ] Create traceable image tags
- [ ] Use GitLab environments
- [ ] Deploy to staging
- [ ] Verify deployment
- [ ] Protect production deployment
- [ ] Prevent conflicting deployments
- [ ] Use `allow_failure` appropriately
- [ ] Apply retry/timeouts appropriately
- [ ] Understand `before_script`/`after_script`
- [ ] Use basic `extends`
- [ ] Recognize `include`
- [ ] Use PostgreSQL services for integration testing
- [ ] Protect CI variables and sensitive runners
- [ ] Recognize supply-chain risks
- [ ] Explain why short-lived OIDC (`id_tokens`) cloud credentials are safer than long-lived CI/CD variables
- [ ] Navigate pipeline/job logs
- [ ] Troubleshoot common failures
- [ ] Maintain readable pipeline YAML
- [ ] Translate the same CI/CD design between GitLab CI/CD, Jenkins, and GitHub Actions

---

# Suggested Practice Progression

```text
1. Create .gitlab-ci.yml
        │
        ▼
2. Add one job with an echo command
        │
        ▼
3. Add stages
        │
        ▼
4. Run Maven tests
        │
        ▼
5. Publish JUnit reports
        │
        ▼
6. Run frontend npm tests/build
        │
        ▼
7. Split backend/frontend jobs
        │
        ▼
8. Observe parallel execution
        │
        ▼
9. Add artifacts
        │
        ▼
10. Add cache
        │
        ▼
11. Add merge-request rules
        │
        ▼
12. Add main-branch behavior
        │
        ▼
13. Add CI/CD variables
        │
        ▼
14. Add protected secret variable
        │
        ▼
15. Add PostgreSQL test service
        │
        ▼
16. Add Docker build
        │
        ▼
17. Tag image with commit identity
        │
        ▼
18. Authenticate to registry
        │
        ▼
19. Push image
        │
        ▼
20. Deploy staging
        │
        ▼
21. Verify staging
        │
        ▼
22. Add manual production job
        │
        ▼
23. Protect production credentials
        │
        ▼
24. Add deployment concurrency control
        │
        ▼
25. Add security checks
        │
        ▼
26. Debug intentional failures
        │
        ▼
27. Compare directly with Jenkins
    and GitHub Actions capstones
```

---

# Capstone — GitLab CI/CD Full-Stack Pipeline

The capstone intentionally implements the same conceptual application and delivery flow used in the **Jenkins** and **GitHub Actions** trees.

The goal is to answer:

> **I already understand the pipeline. How does GitLab CI/CD express it?**

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

---

## Stage 1 — Repository

```text
project/
├── .gitlab-ci.yml
├── backend/
├── frontend/
├── docker-compose.yml
└── README.md
```

No production secrets belong in Git.

---

## Stage 2 — Pipeline Trigger

```text
Push / Merge Request
        │
        ▼
     GitLab
        │
        ▼
      Pipeline
```

Feature/MR validation should not automatically deploy production.

---

## Stage 3 — Pipeline Stages

```text
stages:
  - test
  - package
  - publish
  - deploy
  - verify
```

Exact names can vary, but the CI/CD responsibilities should remain clear.

---

## Stage 4 — Backend Validation

```text
backend-test
     │
     ▼
Java/Maven Environment
     │
     ▼
./mvnw test
     │
     ▼
JUnit / Mockito
     │
     ▼
JUnit Report
```

---

## Stage 5 — Frontend Validation

```text
frontend-test
     │
     ▼
Node Environment
     │
     ▼
npm ci
     │
     ▼
tests
     │
     ▼
production build
```

---

## Stage 6 — Parallel Validation

```text
                    Pipeline
                       │
          ┌────────────┴────────────┐
          ▼                         ▼
     backend-test              frontend-test
          │                         │
       JUnit                     frontend
       Mockito                     tests
          │                         │
          └────────────┬────────────┘
                       ▼
                  both required
```

---

## Stage 7 — Package

```text
Backend
  │
  ▼
JAR Artifact

Frontend
  │
  ▼
Build Artifact
```

Artifacts should be traceable to the commit/pipeline.

---

## Stage 8 — Docker Build

Using the Docker prerequisite knowledge:

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

---

## Stage 9 — Versioning

Tag images using a traceable identity:

```text
commit SHA
release tag
application version
```

Example mental model:

```text
registry/project/backend:<commit>
registry/project/frontend:<commit>
```

---

## Stage 10 — Registry

```text
GitLab Pipeline
      │
      ▼
Authenticate
      │
      ▼
Container Registry
      │
      ├── backend:<version>
      └── frontend:<version>
```

Use appropriate CI-provided/protected credentials.

---

## Stage 11 — Staging

```text
Published Images
      │
      ▼
Deploy Staging
      │
      ▼
GitLab Environment: staging
```

The deployment target itself may later be:

```text
Docker host
AWS
Kubernetes
other platform
```

---

## Stage 12 — Verify

Run:

```text
backend health check
frontend availability check
critical API smoke test
database-backed behavior check
```

If verification fails:

```text
STOP
do not promote production
```

---

## Stage 13 — Production Gate

```text
Staging Verified
      │
      ▼
Manual Production Job
      │
      ▼
Protected Production Credentials
      │
      ▼
Deploy Production
```

---

## Stage 14 — Deployment Concurrency

Prevent conflicting production releases.

```text
Deploy A
   │
   ▼
Production

Deploy B
   │
   ▼
wait until safe
```

---

## Stage 15 — PostgreSQL Integration Test

Use disposable PostgreSQL infrastructure during CI.

```text
Integration Test Job
        │
        ├── Spring Boot Tests
        │
        └── PostgreSQL Service
```

Never connect automated CI tests to production data.

---

## Stage 16 — Security Review

Verify:

- [ ] `.gitlab-ci.yml` changes are reviewed
- [ ] Secrets are not committed
- [ ] Secrets are not printed
- [ ] Production variables are protected
- [ ] Untrusted MR jobs do not receive production authority
- [ ] Sensitive runners are appropriately controlled
- [ ] Registry credentials are scoped appropriately
- [ ] Images use traceable tags
- [ ] Dependencies/images can be scanned
- [ ] Production deployment requires appropriate conditions/gates

---

## Stage 17 — Failure Drills

Intentionally introduce:

```text
invalid YAML
Java compilation error
JUnit failure
frontend test failure
missing variable
runner-tag mismatch
Docker build failure
registry authentication failure
staging health failure
```

For each failure answer:

```text
Was a pipeline created?
Which stage?
Which job?
What did the log show?
Did later work stop?
Was production protected?
Is this:
application,
GitLab CI config,
runner,
Docker,
registry,
or deployment-platform failure?
```

---

# Same Pipeline Across All Three Platforms

```text
                         Same Application
                               │
                    Spring Boot + Frontend
                          + PostgreSQL
                               │
                               ▼
                             Docker
                               │
          ┌────────────────────┼────────────────────┐
          ▼                    ▼                    ▼
       Jenkins           GitHub Actions        GitLab CI/CD
          │                    │                    │
          ▼                    ▼                    ▼
     Jenkinsfile          workflow YAML         .gitlab-ci.yml
          │                    │                    │
          ▼                    ▼                    ▼
        Build                Build                Build
          │                    │                    │
          ▼                    ▼                    ▼
         Test                 Test                 Test
          │                    │                    │
          ▼                    ▼                    ▼
       Package              Package              Package
          │                    │                    │
          ▼                    ▼                    ▼
    Docker Build         Docker Build         Docker Build
          │                    │                    │
          ▼                    ▼                    ▼
       Publish              Publish              Publish
          │                    │                    │
          ▼                    ▼                    ▼
        Deploy               Deploy               Deploy
          │                    │                    │
          ▼                    ▼                    ▼
        Verify               Verify               Verify
```

The repetition is intentional:

```text
same CI/CD concepts
        │
        ▼
different platform syntax
```

---

# Interview Readiness

Be able to answer:

- [ ] What is GitLab CI/CD?
- [ ] How is GitLab CI/CD different from CI/CD itself?
- [ ] What is `.gitlab-ci.yml`?
- [ ] What is a pipeline?
- [ ] What is a stage?
- [ ] What is a job?
- [ ] What does `script` do?
- [ ] What is a GitLab Runner?
- [ ] What are runner tags?
- [ ] What does `image` do in a compatible GitLab job?
- [ ] What are predefined CI/CD variables?
- [ ] How do custom variables work?
- [ ] How should secrets be stored?
- [ ] What does masked mean?
- [ ] What does protected mean for a CI/CD variable?
- [ ] How does GitLab run Maven?
- [ ] How does GitLab run npm?
- [ ] How are test reports published?
- [ ] What is a GitLab artifact?
- [ ] Artifact vs cache?
- [ ] What does `needs` do?
- [ ] How do jobs run in parallel?
- [ ] What do `rules` do?
- [ ] What is `workflow: rules`?
- [ ] What is a merge-request pipeline?
- [ ] How should feature branches differ from main/release behavior?
- [ ] What is a manual job?
- [ ] How can GitLab run scheduled pipelines?
- [ ] How does GitLab CI build Docker images?
- [ ] What does the runner need for container builds?
- [ ] What is GitLab Container Registry?
- [ ] How should images be tagged?
- [ ] What is a GitLab environment?
- [ ] How would you deploy to staging?
- [ ] How would you verify a deployment?
- [ ] How would you protect production?
- [ ] Why prevent concurrent production deployments?
- [ ] What does `allow_failure` do?
- [ ] When is retry appropriate?
- [ ] What do `before_script` and `after_script` do?
- [ ] What does `extends` do?
- [ ] What does `include` do?
- [ ] How can PostgreSQL be used in integration tests?
- [ ] Why is `.gitlab-ci.yml` security-sensitive?
- [ ] Why are protected variables important?
- [ ] Why can untrusted MR code be dangerous?
- [ ] Why can Docker-capable runners be security-sensitive?
- [ ] How can GitLab CI/CD avoid storing long-lived cloud credentials (OIDC / `id_tokens`)?
- [ ] How would you debug a pipeline that never starts?
- [ ] How would you debug a pending job?
- [ ] How would you debug "works locally, fails in GitLab"?
- [ ] How does GitLab CI/CD map to Jenkins and GitHub Actions?
- [ ] How would you build a full-stack pipeline for Spring Boot + Angular/React?

---

# Common Anti-Patterns

## Anti-Pattern 1 — GitLab CI/CD Equals CI/CD

Learn the transferable fundamentals first.

## Anti-Pattern 2 — Hardcoded Secrets

Never commit production passwords/tokens to `.gitlab-ci.yml`.

## Anti-Pattern 3 — Production Variables Available Everywhere

Use protected/scoped secrets appropriately.

## Anti-Pattern 4 — Untrusted Code on Powerful Runners

Runner access is part of the security boundary.

## Anti-Pattern 5 — Giant YAML Build System

Keep Maven/npm/Docker logic in their proper tools.

## Anti-Pattern 6 — `allow_failure` on Required Tests

Do not make broken software appear releasable.

## Anti-Pattern 7 — Cache as Permanent State

A cleared cache should not break correctness.

## Anti-Pattern 8 — `latest` as the Only Image Identity

Use traceable versioning.

## Anti-Pattern 9 — Rebuild Different Production Artifact

Promote the tested artifact/image when possible.

## Anti-Pattern 10 — Deployment Command = Successful Deployment

Verify the running system.

## Anti-Pattern 11 — Feature Branch Automatically Deploys Production

Use explicit rules/protections.

## Anti-Pattern 12 — Concurrent Production Deployments

Serialize shared deployment targets where needed.

## Anti-Pattern 13 — CI Tests Use Production Database

Use isolated disposable test services.

## Anti-Pattern 14 — Excessive `rules` Complexity

Pipeline behavior should remain understandable.

## Anti-Pattern 15 — Copy/Paste `.gitlab-ci.yml` Without Understanding

Map every important structure back to the CI/CD concept it represents.

## Anti-Pattern 16 — Long-Lived Cloud Keys by Default

Prefer short-lived identity such as OIDC (`id_tokens`) where the target cloud provider supports it and the architecture warrants it.

---

# Relationship to Other Skill Trees

```text
Git / Version Control
        │
        ▼
CI/CD Fundamentals
        │
        ├──────────────────┐
        ▼                  ▼
   GitLab CI/CD          Docker
        │                  │
        └────────┬─────────┘
                 ▼
        Practical GitLab CI/CD
                 │
                 ▼
       Deployment Fundamentals
                 │
         ┌───────┼────────┐
         ▼       ▼        ▼
        AWS  Kubernetes  Other Targets
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
   GitLab Backend CI
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
  GitLab Frontend CI
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

Sibling platforms:

```text
                    CI/CD Fundamentals
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
     Jenkins         GitHub Actions      GitLab CI/CD

                         Alternative:
                   Azure DevOps Pipelines
```

---

# Mastery Progression

```text
"I understand CI/CD"
        │
        ▼
"I know what .gitlab-ci.yml does"
        │
        ▼
"I understand pipeline / stage / job / runner"
        │
        ▼
"I can build and test one project"
        │
        ▼
"I can run backend/frontend jobs in parallel"
        │
        ▼
"I can publish reports and artifacts"
        │
        ▼
"I can use variables and rules"
        │
        ▼
"I can validate merge requests"
        │
        ▼
"I can protect sensitive variables"
        │
        ▼
"I can build and publish Docker images"
        │
        ▼
"I can deploy and verify staging"
        │
        ▼
"I can gate production"
        │
        ▼
"I understand runner and pipeline security"
        │
        ▼
"I can troubleshoot pipeline failures"
        │
        ▼
"I can maintain readable GitLab CI YAML"
        │
        ▼
"I can translate the same CI/CD design
between Jenkins, GitHub Actions,
and GitLab CI/CD"
```

---

# Mastery Standard

> **Can I take a CI/CD pipeline that I already understand conceptually and independently implement it in GitLab CI/CD using a maintainable `.gitlab-ci.yml` with appropriate stages, jobs, runners, Maven/npm builds, automated tests and reports, artifacts, variables, rules, Docker image publishing, protected secrets, staging verification, and controlled production deployment—while recognizing merge-request, runner, credential, container, and supply-chain security risks and knowing when a problem belongs to GitLab administration or the deployment platform rather than the pipeline?**

Final mental model:

```text
                         GitLab Repository
                                │
                         push / merge request
                                │
                                ▼
                         GitLab CI/CD
                                │
                                ▼
                         .gitlab-ci.yml
                                │
                  ┌─────────────┴─────────────┐
                  ▼                           ▼
              Backend                     Frontend
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
                       Container Registry
                                │
                                ▼
                         Deploy Staging
                                │
                                ▼
                              Verify
                                │
                                ▼
                          Manual / Protected
                          Production Gate
                                │
                                ▼
                         Deploy Production


GitLab CI/CD is one implementation:

                    CI/CD Fundamentals
                           │
         ┌─────────────────┼─────────────────┐
         ▼                 ▼                 ▼
      Jenkins        GitHub Actions      GitLab CI/CD

                         Alternative:
                   Azure DevOps Pipelines
```
