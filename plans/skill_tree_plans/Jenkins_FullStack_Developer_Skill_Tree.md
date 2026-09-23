# Jenkins Full-Stack Developer Skill Tree

> **Goal:** Learn how to implement the CI/CD concepts from the **CI/CD Fundamentals** skill tree using Jenkins at the developer level.
>
> **Target level:** Full-stack developer who can read, write, run, troubleshoot, and maintain practical Jenkins pipelines for Java/Spring Boot and modern frontend applications without needing to administer a large Jenkins installation.
>
> **Primary prerequisites:** **CI/CD Fundamentals** and **Git / Version Control**
>
> **Practical prerequisite:** **Docker** for the container-build, registry, and capstone portions of this tree.
>
> **Supporting prerequisites:** Maven, automated testing (JUnit/Mockito for Java projects), npm-based frontend tooling, basic Linux/command-line use, and basic HTTP/webhook knowledge.
>
> **Recommended security companion:** A future **Security Fundamentals** tree should provide the broader security foundation. Jenkins-specific developer security—credentials, secret handling, least privilege, untrusted code, plugin awareness, and pipeline permissions—is introduced here. A future **DevSecOps** tree can build on both CI/CD Fundamentals and Security Fundamentals.
>
> **Scope boundary:** This is a **developer-level Jenkins tree**. It does not attempt to make the learner a Jenkins administrator. Deep controller administration, enterprise plugin governance, complex distributed-agent infrastructure, high availability, disaster recovery, large-scale upgrades, and advanced Jenkins internals are outside scope.
>
> **Deployment boundary:** Jenkins can orchestrate deployment, but AWS, Kubernetes, cloud infrastructure, advanced deployment strategies, and production platform mechanics belong in **Deployment Fundamentals** and their platform-specific skill trees.

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
     Jenkins          Docker
        │               │
        └───────┬───────┘
                ▼
        Jenkins Pipeline
                │
        ┌───────┼────────┐
        ▼       ▼        ▼
    Trigger   Build     Test
        │       │        │
        └───────┼────────┘
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

CI/CD Fundamentals teaches the concept first. Jenkins teaches one implementation.

```text
CI/CD Concept              Jenkins Implementation
-------------              ----------------------
Pipeline                   Jenkins Pipeline
Pipeline as code           Jenkinsfile
Execution machine          Agent
Group of pipeline work     Stage
Individual action          Step
Environment value          environment / env
Secret                     Jenkins Credential
Manual gate                input
Artifact                   archiveArtifacts / external repository
Test result                junit
Trigger                    webhook / SCM polling / schedule / manual
Conditional execution      when
Parallel execution         parallel
Failure handling           post / status / exit result
```

Do not memorize the right column without understanding the left column.

---

# Tier 0 — Jenkins in the CI/CD Ecosystem

## 1. What Jenkins Is

Jenkins is an automation server commonly used to implement CI/CD pipelines.

```text
Git Change
    │
    ▼
 Jenkins
    │
    ▼
Automated Pipeline
    │
    ├── Build
    ├── Test
    ├── Package
    ├── Publish
    └── Deploy
```

## 2. What Jenkins Is Not

```text
Jenkins != CI/CD
```

Instead:

```text
CI/CD
= practices + delivery concepts

Jenkins
= a tool that can automate those practices
```

## 3. Why Learn Jenkins

Jenkins exposes many CI/CD concepts directly:

- [ ] Pipelines
- [ ] Stages
- [ ] Agents
- [ ] Credentials
- [ ] Triggers
- [ ] Build tools
- [ ] Artifacts
- [ ] Test reports
- [ ] Deployment orchestration

**Checkpoint:** Explain what Jenkins contributes to a CI/CD process without defining CI/CD as "using Jenkins."

---

# Tier 1 — Jenkins Architecture Awareness

## 4. Controller

At a developer level, understand that the Jenkins controller coordinates Jenkins.

Conceptually:

```text
                 Jenkins Controller
                        │
          ┌─────────────┼─────────────┐
          ▼             ▼             ▼
       Agent A        Agent B       Agent C
```

The controller can:

- [ ] Store job/pipeline configuration
- [ ] Schedule work
- [ ] Coordinate agents
- [ ] Track pipeline results
- [ ] Provide the Jenkins UI/API

Deep controller administration is outside this tree.

## 5. Agent

An agent is a machine/environment where Jenkins executes work.

```text
Pipeline Job
     │
     ▼
   Agent
     │
     ├── checkout
     ├── mvn test
     ├── npm build
     └── docker build
```

## 6. Executor Awareness

Agents may have one or more executors that determine how many tasks can run concurrently.

You only need enough knowledge to understand:

```text
queued job
vs
running job
```

## 7. Workspace

Jenkins generally gives a job a workspace containing checked-out files and generated build files.

```text
Workspace
├── source code
├── target/
├── node_modules/   (if installed)
└── generated files
```

Do not treat the workspace as permanent artifact storage.

---

# Tier 2 — Jenkins User Interface

## 8. Dashboard

Become comfortable locating:

- [ ] Jobs
- [ ] Build history
- [ ] Pipeline status
- [ ] Console output
- [ ] Test results
- [ ] Artifacts
- [ ] Parameters

## 9. Build Number

A Jenkins job typically has numbered executions.

```text
#41
#42
#43
```

A build number identifies a Jenkins run, not necessarily the application version.

## 10. Build Status

Recognize common states such as:

```text
success
failure
unstable
aborted
```

Understand that Jenkins terminology can distinguish a failed build from an unstable one.

---

# Tier 3 — Jobs

## 11. Job Concept

A Jenkins job defines work Jenkins can execute.

Historically/simple usage may involve UI-configured jobs.

## 12. Freestyle Job Awareness

Know what a Freestyle job is and how it can:

- [ ] Checkout source
- [ ] Execute shell/build commands
- [ ] Run build tools
- [ ] Archive output

But do not make Freestyle jobs the center of modern Jenkins learning.

## 13. Pipeline Job

Prioritize Pipeline jobs.

```text
Pipeline Job
      │
      ▼
 Jenkinsfile
      │
      ▼
Defined CI/CD Process
```

## 14. Why Pipeline as Code

Compared with large amounts of UI-only configuration:

```text
Jenkinsfile
├── version controlled
├── reviewable
├── reproducible
├── branchable
└── stored with project
```

---

# Tier 4 — Jenkinsfile Fundamentals

## 15. Jenkinsfile

A `Jenkinsfile` is normally stored in the repository.

Example structure:

```groovy
pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'mvn package'
            }
        }
    }
}
```

## 16. Basic Structure

Recognize:

```text
pipeline
│
├── agent
├── environment
├── options
├── parameters
├── stages
│   ├── stage
│   │   └── steps
│   └── stage
│       └── steps
└── post
```

Not every pipeline needs every section.

## 17. Groovy Awareness

Jenkins Pipeline syntax is based on Groovy.

Developer goal:

```text
understand enough Groovy
to read/write Jenkins pipelines
```

not:

```text
become a Groovy language expert
```

---

# Tier 5 — Declarative Pipeline

## 18. Declarative Pipeline

This tree should prioritize Declarative Pipeline.

```groovy
pipeline {
    agent any

    stages {
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
}
```

## 19. Why Declarative First

It provides a structured pipeline model that maps well to CI/CD fundamentals.

Learn:

- [ ] `pipeline`
- [ ] `agent`
- [ ] `stages`
- [ ] `stage`
- [ ] `steps`
- [ ] `environment`
- [ ] `parameters`
- [ ] `when`
- [ ] `post`

## 20. Scripted Pipeline Awareness

Know that Scripted Pipeline exists and is more programmatic.

```groovy
node {
    stage('Build') {
        sh 'mvn package'
    }
}
```

You should be able to recognize it, but advanced Scripted Pipeline programming is outside this developer-focused tree.

---

# Tier 6 — Agents in Pipelines

## 21. `agent any`

Understand:

```groovy
agent any
```

as allowing Jenkins to select an appropriate available agent.

## 22. `agent none`

Understand why a pipeline might declare no global agent and choose agents per stage.

## 23. Stage-Level Agents

Conceptually:

```text
Frontend Stage → Node-capable agent
Backend Stage  → Java/Maven-capable agent
Docker Stage   → Docker-capable agent
```

## 24. Labels Awareness

Jenkins agents can have labels.

Concept:

```text
label: linux
label: docker
label: java
```

A pipeline can request an agent with needed capabilities.

Do not go deeply into agent provisioning/administration here.

---

# Tier 7 — Stages

## 25. Stage Purpose

Stages make pipeline phases visible.

```text
Checkout
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
```

## 26. Meaningful Stage Names

Prefer:

```text
Backend Unit Tests
Frontend Build
Docker Image
Deploy Staging
```

over:

```text
Stage 1
Stage 2
Stuff
```

## 27. Stage Boundaries

A stage should represent a meaningful CI/CD phase rather than every individual shell command.

---

# Tier 8 — Steps

## 28. Step Concept

Steps perform actions inside a stage.

```groovy
stage('Test') {
    steps {
        sh 'mvn test'
    }
}
```

## 29. Shell Step

On Unix-like agents:

```groovy
sh 'command'
```

## 30. Windows Awareness

Jenkins can execute Windows commands using appropriate steps such as `bat`.

For the user's WSL/Linux-oriented learning path, prioritize `sh`.

## 31. Multiple Commands

Understand when to:

```text
call existing build scripts
```

rather than embedding large application build systems directly inside the Jenkinsfile.

---

# Tier 9 — Source Checkout

## 32. Source Control Integration

Jenkins needs the source revision being built.

## 33. `checkout scm`

In many Pipeline-from-SCM setups:

```groovy
checkout scm
```

checks out the configured source.

## 34. `git` Step Awareness

Recognize simple Git checkout mechanisms while understanding that real repository authentication/configuration may require Jenkins credentials.

## 35. Traceability

A Jenkins build should let you identify:

```text
Build #52
   │
   ▼
Git Commit
   │
   ▼
Application Artifact
```

---

# Tier 10 — Pipeline Triggers

## 36. Manual Trigger

A developer can start a Jenkins job manually.

Useful for:

- [ ] Learning
- [ ] Testing
- [ ] Controlled jobs

## 37. Webhook Trigger

Preferred conceptual flow for repository events:

```text
Git Hosting Platform
        │
        │ webhook
        ▼
      Jenkins
        │
        ▼
     Pipeline
```

## 38. SCM Polling Awareness

Jenkins can periodically check source control for changes.

Understand why webhook/event-driven triggering is often preferable to frequent polling.

## 39. Scheduled Builds

Pipelines can run on schedules for appropriate tasks.

Do not use schedules as a substitute for proper source-change triggers.

---

# Tier 11 — Branches & Pull Requests

## 40. Branch-Aware CI

Conceptually:

```text
Feature Branch
     │
     ▼
Jenkins Validation
     │
     ├── build
     ├── test
     └── quality checks
```

## 41. Multibranch Pipeline

Understand the purpose of a Multibranch Pipeline:

```text
Repository
├── main
├── feature/a
└── feature/b
      │
      ▼
Jenkins discovers branch Jenkinsfiles
```

## 42. Pull Request Awareness

Depending on source-control integration/plugins, Jenkins can validate pull/merge requests.

The important developer concept is:

```text
proposed change
      │
      ▼
automated validation
      │
      ▼
merge decision
```

## 43. Branch Conditions

Use conditions to prevent inappropriate deployment from feature branches.

---

# Tier 12 — `when` Conditions

## 44. Conditional Stage

Declarative Pipeline can conditionally execute stages.

Concept:

```groovy
stage('Deploy') {
    when {
        branch 'main'
    }
    steps {
        // deployment
    }
}
```

## 45. Common Conditions

Understand conditions based on:

- [ ] Branch
- [ ] Environment
- [ ] Expression
- [ ] Change request awareness
- [ ] Tag awareness

Do not build unnecessarily complex logic.

---

# Tier 13 — Environment Variables

## 46. Jenkins Environment

Pipelines can define environment values.

```groovy
environment {
    APP_NAME = 'guestbook'
}
```

## 47. Accessing Values

Understand Jenkins environment access conceptually:

```text
env.APP_NAME
```

and shell environment interpolation.

## 48. Environment Scope

Values may apply:

```text
pipeline-wide
or
stage-specific
```

## 49. Configuration vs Secrets

Do not put passwords directly into ordinary Jenkinsfile environment declarations.

---

# Tier 14 — Credentials

## 50. Jenkins Credentials

Jenkins provides credential storage/integration for secrets needed by pipelines.

Examples:

- [ ] Username/password
- [ ] Secret text/token
- [ ] SSH credentials
- [ ] Certificates/files awareness

## 51. Credential ID

Pipelines should reference a Jenkins credential by identifier rather than hardcoding the secret.

```text
Jenkinsfile
    │
    ▼
Credential ID
    │
    ▼
Jenkins Credential Store
```

## 52. `credentials()` Awareness

Declarative pipelines can bind supported credentials into the environment.

Understand the purpose without trying to memorize every credential type.

## 53. `withCredentials`

Know the role of credential binding for limited pipeline sections.

Conceptually:

```text
enter credential scope
      │
      ▼
perform authenticated operation
      │
      ▼
leave credential scope
```

## 54. Secret Safety

- [ ] Do not echo secrets
- [ ] Do not write secrets into artifacts
- [ ] Do not commit them
- [ ] Keep credential scope narrow
- [ ] Use least privilege
- [ ] Be careful with shell tracing/log output

---

# Tier 15 — Parameters

## 55. Parameterized Pipeline

A pipeline can accept controlled inputs.

Examples:

```text
target environment
release version
run optional test?
```

## 56. Common Parameter Types Awareness

- [ ] String
- [ ] Boolean
- [ ] Choice

## 57. Appropriate Use

Parameters can support controlled manual workflows.

Do not turn every pipeline behavior into a manual parameter when it should be derived automatically from source/version/environment.

---

# Tier 16 — Maven Integration

## 58. Jenkins + Maven

Jenkins should invoke the Maven build already defined by the project.

```groovy
sh './mvnw clean verify'
```

or appropriate project command.

## 59. Wrapper Preference Awareness

A Maven Wrapper can help standardize Maven version usage.

## 60. Typical Backend Pipeline

```text
Checkout
   │
   ▼
Compile
   │
   ▼
JUnit / Mockito Tests
   │
   ▼
Package JAR
```

## 61. Do Not Duplicate `pom.xml`

Dependency/build logic belongs primarily in Maven.

Jenkins orchestrates it.

---

# Tier 17 — npm / Frontend Integration

## 62. Jenkins + npm

Typical frontend work:

```text
npm install / npm ci
       │
       ▼
      test
       │
       ▼
      build
```

## 63. `npm ci`

Understand why CI environments commonly prefer deterministic lockfile-based installation when appropriate.

## 64. Frontend Pipeline

```text
Checkout
   │
   ▼
Install Dependencies
   │
   ▼
Tests
   │
   ▼
Production Build
```

## 65. Angular / React

Jenkins does not need separate CI concepts for Angular and React.

It executes the build/test commands defined by each project.

---

# Tier 18 — Automated Tests

## 66. Test Failure

If required tests fail:

```text
Pipeline
   │
   ▼
STOP
```

## 67. Java Tests

For the Java path:

```text
JUnit
Mockito
Spring tests
```

are executed through Maven/Gradle.

## 68. Frontend Tests

Run the frontend project's configured automated tests.

## 69. Test Placement

Keep fast, high-value tests early enough to provide useful feedback.

---

# Tier 19 — JUnit Test Reporting

## 70. `junit` Step

Jenkins can consume JUnit-format XML test reports.

Concept:

```text
Test Framework
     │
     ▼
JUnit XML
     │
     ▼
 Jenkins
     │
     ▼
Visible Test Results
```

## 71. Why Publish Results

Jenkins can show:

- [ ] Failed tests
- [ ] Counts
- [ ] Build-to-build history
- [ ] Test duration information

## 72. Framework Independence

Other test tools can often generate JUnit-compatible XML.

The Jenkins `junit` step is about report format, not only Java's JUnit library.

---

# Tier 20 — Artifacts

## 73. Build Artifact

Examples:

```text
backend JAR
frontend build bundle
test report
ZIP
```

## 74. `archiveArtifacts`

Jenkins can archive selected files from a build.

Use it for appropriate build outputs/results.

## 75. External Artifact Repositories

For production workflows, artifacts may be published to:

```text
package repository
container registry
cloud artifact service
```

instead of relying on Jenkins build storage as the long-term release repository.

## 76. Fingerprinting Awareness

Know that Jenkins can track artifact fingerprints for traceability.

Deep artifact governance belongs elsewhere.

---

# Tier 21 — Docker Integration

## 77. Prerequisite Boundary

This tree assumes the learner already understands Docker concepts:

```text
Dockerfile
image
container
registry
tag
volume
network
```

Jenkins only orchestrates them here.

## 78. Build Image

Concept:

```groovy
sh 'docker build -t guestbook-backend:VERSION .'
```

## 79. Tag Image

Use meaningful/versioned tags.

```text
guestbook-backend:1.4.2
guestbook-backend:<commit>
```

## 80. Registry Authentication

Use Jenkins credentials rather than hardcoded registry passwords.

## 81. Push Image

Concept:

```text
Jenkins Agent
     │
     ▼
docker push
     │
     ▼
Container Registry
```

## 82. Docker-Capable Agent

The selected Jenkins agent must have the necessary Docker capability/permissions.

Deep agent security/daemon architecture belongs in Docker/Jenkins administration/security study.

---

# Tier 22 — Pipeline Parallelism

## 83. Independent Work

Frontend and backend validation may run independently.

```text
             ┌── Backend Tests
Checkout ────┤
             └── Frontend Tests
```

## 84. Declarative Parallel Awareness

Jenkins supports parallel execution.

Learn how to recognize and create basic parallel stages/branches.

## 85. Tradeoff

Parallelism can reduce pipeline time but may increase:

- [ ] Agent demand
- [ ] Resource use
- [ ] Debugging complexity

---

# Tier 23 — Timeouts

## 86. Timeout Purpose

Do not allow a stuck pipeline step to run forever.

Examples:

```text
deployment waiting
integration test hanging
external service unavailable
```

## 87. `timeout`

Understand the Declarative/Pipeline timeout concept and where to use it.

## 88. Choose Realistic Limits

Timeouts should reflect expected behavior rather than arbitrary tiny values.

---

# Tier 24 — Retry

## 89. `retry`

Jenkins can retry a block of work.

Appropriate for transient problems such as:

```text
temporary network issue
temporary registry outage
```

## 90. Do Not Retry Deterministic Failures

Bad:

```text
unit test fails
→ retry 10 times
```

Fix the test/code.

---

# Tier 25 — Failure Handling

## 91. Exit Status

Shell commands that return failure normally cause pipeline failure unless explicitly handled.

## 92. Fail Fast

Compilation/test failure should prevent later release/deployment work.

## 93. Preserve Evidence

On failure, retain useful:

- [ ] Console logs
- [ ] Test reports
- [ ] Relevant artifacts
- [ ] Failure stage information

---

# Tier 26 — `post` Actions

## 94. Declarative `post`

`post` allows work based on pipeline/stage outcome.

Conceptually:

```text
always
success
failure
unstable
changed
cleanup
```

## 95. Common Uses

- [ ] Publish test reports
- [ ] Archive logs/results
- [ ] Clean up
- [ ] Send notifications
- [ ] Record deployment result

## 96. Cleanup

Temporary resources should be cleaned up even after failure when appropriate.

---

# Tier 27 — Notifications

## 97. Notification Purpose

Tell the appropriate people when action is needed.

Examples:

```text
pipeline failed
deployment failed
production approval required
```

## 98. Plugin/Integration Awareness

Jenkins commonly uses plugins/integrations for email/chat notifications.

Do not make vendor-specific messaging integrations a core Jenkins skill.

## 99. Avoid Noise

Do not notify everyone for every successful low-risk build.

---

# Tier 28 — Manual Approval

## 100. `input`

Jenkins Pipeline can pause for human input/approval.

Concept:

```text
Deploy Staging
      │
      ▼
Verification
      │
      ▼
input: Approve Production?
      │
      ▼
Deploy Production
```

## 101. Continuous Delivery

This is a natural Jenkins implementation of a Continuous Delivery model with a manual production gate.

## 102. Approval Safety

Approval should not require exposing secrets or manually editing deployment commands.

---

# Tier 29 — Basic Deployment Orchestration

## 103. Jenkins Role

Jenkins may execute or call deployment tooling.

```text
Jenkins
   │
   ▼
Deployment Command / API / Tool
   │
   ▼
Target Environment
```

## 104. Scope Boundary

Jenkins should not replace understanding of:

```text
Docker
AWS
Kubernetes
Deployment Fundamentals
```

## 105. Staging First

Common conceptual pipeline:

```text
Package
   │
   ▼
Publish
   │
   ▼
Deploy Staging
   │
   ▼
Verify
   │
   ▼
Production Decision
```

---

# Tier 30 — Deployment Verification

## 106. Do Not Stop at Deployment Command

After Jenkins deploys:

```text
health check
smoke test
basic functional verification
```

should determine whether the deployment is actually usable.

## 107. Failure

If verification fails:

```text
mark deployment/pipeline failed
stop further promotion
initiate recovery policy
```

## 108. Rollback Boundary

Jenkins can orchestrate rollback, but the rollback strategy itself belongs primarily in Deployment Fundamentals/platform trees.

---

# Tier 31 — Caching Awareness

## 109. Why Cache

Builds repeatedly download dependencies.

Potential cache targets:

```text
Maven repository
npm cache
```

## 110. Cache vs Artifact

```text
Cache
= build-speed optimization

Artifact
= intentional build output
```

## 111. Correctness First

A Jenkins build should remain correct if its cache is cleared.

Implementation varies with agent architecture.

---

# Tier 32 — Workspace Management

## 112. Dirty Workspace Risk

Leftover files can cause builds to behave differently.

Examples:

```text
old compiled files
old generated config
stale test output
```

## 113. Clean Builds

Understand when the pipeline/build tool should clean generated outputs.

## 114. Workspace Cleanup Awareness

Jenkins/plugins can clean workspaces.

Do not depend on stale workspace state.

---

# Tier 33 — Tool Versions

## 115. Reproducibility

Know which versions the agent uses:

```text
Java
Maven
Node
npm
Docker
```

## 116. Avoid "Whatever Is Installed"

Uncontrolled agent versions can create:

```text
works on Agent A
fails on Agent B
```

## 117. Wrappers / Controlled Environments

Use project wrappers or controlled build environments where appropriate.

Containerized agents/build environments can help, but advanced agent architecture is outside scope.

---

# Tier 34 — Plugins

## 118. Jenkins Plugin Model

Jenkins functionality is frequently extended through plugins.

Examples conceptually:

```text
Git integration
credentials
pipeline features
source-host integration
notifications
```

## 119. Developer Responsibility

A developer should know:

```text
"This Jenkinsfile step may depend on a plugin."
```

## 120. Plugin Security Awareness

Plugins add code and attack surface to Jenkins.

Developer-level principles:

- [ ] Prefer established/approved plugins
- [ ] Avoid demanding unnecessary plugins
- [ ] Know that plugin availability differs between Jenkins installations
- [ ] Do not assume every online example works on every Jenkins server

Installation/update governance is an administrator responsibility.

---

# Tier 35 — Pipeline Syntax Help

## 121. Jenkins Documentation

Learn to consult Jenkins Pipeline documentation rather than memorizing every directive.

## 122. Snippet Generator Awareness

Jenkins provides tools that can help generate Pipeline syntax for installed steps/plugins.

Use generated syntax as something to understand, not blindly paste.

## 123. Declarative Directive Generator Awareness

Know that Jenkins can assist with Declarative syntax depending on the installation/version.

---

# Tier 36 — Credentials & Security

## 124. Jenkins Is Security-Sensitive

A Jenkins pipeline may access:

```text
source code
registry credentials
deployment credentials
production systems
cloud accounts
secrets
```

## 125. Least Privilege

A test pipeline should not automatically receive production deployment credentials.

## 126. Credential Scope

Prefer:

```text
only the job
only the stage
only the operation
```

that needs the credential.

## 127. Secret Exposure

Watch for:

- [ ] `echo`
- [ ] shell tracing
- [ ] generated config files
- [ ] archived workspaces
- [ ] build artifacts
- [ ] error messages

## 128. Production Permissions

Separate:

```text
ability to run CI
from
ability to deploy production
```

where appropriate.

---

# Tier 37 — Untrusted Code

## 129. Pipeline Code Is Executable

A `Jenkinsfile` can cause commands to execute on an agent.

Treat pipeline changes as code changes.

## 130. Pull Requests / Forks

Be careful when untrusted contributors can modify:

```text
Jenkinsfile
build scripts
Dockerfile
test scripts
```

and the build has access to credentials.

## 131. Secret Boundary

Untrusted change validation should not automatically expose powerful secrets.

Detailed CI/CD threat modeling belongs in DevSecOps.

---

# Tier 38 — Supply-Chain Awareness

## 132. Pipeline Dependencies

Jenkins builds may depend on:

```text
Maven dependencies
npm packages
Docker base images
Jenkins plugins
external scripts/tools
```

## 133. Security Checks

Jenkins can orchestrate:

- [ ] Dependency scans
- [ ] Secret scans
- [ ] Static analysis
- [ ] Container scans
- [ ] SBOM generation

The specific tools and security policy belong in DevSecOps/security trees.

## 134. Jenkins Is the Orchestrator

Do not confuse:

```text
Jenkins
```

with:

```text
the security scanner Jenkins invokes
```

---

# Tier 39 — Multibranch Pipeline Workflow

## 135. Branch Discovery

A Multibranch Pipeline can create/manage pipeline runs based on repository branches.

## 136. Jenkinsfile Per Branch

A branch can test the Jenkinsfile that belongs to that branch.

This supports pipeline-as-code evolution.

## 137. Branch Lifecycle

Understand at a high level:

```text
branch created
     │
     ▼
discovered
     │
     ▼
pipeline available
     │
     ▼
branch removed
     │
     ▼
job eventually cleaned/disabled
```

Exact behavior depends on configuration/plugins.

---

# Tier 40 — Pipeline Design for Full Stack

## 138. Separate Backend and Frontend Work

Example:

```text
Checkout
   │
   ├──────────────┐
   ▼              ▼
Backend         Frontend
Maven            npm
   │              │
Test             Test
   │              │
Package          Build
   └───────┬──────┘
           ▼
      Docker Images
```

## 139. Shared Failure Policy

If either required branch fails:

```text
do not publish/deploy release
```

## 140. Database

Do not package PostgreSQL as an application artifact.

Deployment should connect the backend to a separately managed database/runtime service.

---

# Tier 41 — Example Declarative Pipeline Progression

## 141. Level 1 — Build

```groovy
pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh './mvnw package'
            }
        }
    }
}
```

## 142. Level 2 — Separate Test and Package

```groovy
pipeline {
    agent any

    stages {
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Package') {
            steps {
                sh './mvnw package -DskipTests'
            }
        }
    }
}
```

## 143. Level 3 — Publish Test Results

Conceptually add:

```groovy
post {
    always {
        junit 'target/surefire-reports/*.xml'
    }
}
```

## 144. Level 4 — Docker

Add stages that:

```text
build image
tag image
authenticate
push image
```

using the Docker knowledge from the Docker prerequisite tree.

## 145. Level 5 — Deployment

Add:

```text
Deploy Staging
Verify
Approval
Deploy Production
```

without trying to teach the deployment platform itself inside Jenkins.

---

# Tier 42 — Troubleshooting Jenkins Pipelines

## 146. Start With the Failed Stage

```text
Trigger?
   │
Checkout?
   │
Build?
   │
Tests?
   │
Package?
   │
Docker?
   │
Publish?
   │
Deploy?
```

Find the first incorrect stage.

## 147. Console Output

The console log is one of the first places to inspect.

Look for:

```text
command
exit status
exception
missing file
permission error
authentication failure
```

## 148. "Command Not Found"

Check agent capabilities:

```text
java?
mvn?
node?
npm?
docker?
```

## 149. "Works Locally, Fails in Jenkins"

Check:

- [ ] Different runtime/tool version
- [ ] Missing environment variable
- [ ] Missing secret
- [ ] Different working directory
- [ ] Linux case sensitivity
- [ ] Undeclared dependency
- [ ] Network access
- [ ] File permissions
- [ ] Clean workspace differences

## 150. Checkout Failure

Check:

- [ ] Repository URL
- [ ] Branch
- [ ] Jenkins credential
- [ ] Network access
- [ ] Source-host permissions

## 151. Docker Permission Failure

Recognize that Docker access is an agent/runtime configuration concern.

Do not "fix" permissions blindly without understanding the security implications.

## 152. Deployment Failure

Separate:

```text
Jenkins pipeline problem
from
target deployment platform problem
```

---

# Tier 43 — Pipeline Maintainability

## 153. Treat Jenkinsfile as Production Code

Use:

- [ ] Clear names
- [ ] Small understandable stages
- [ ] Comments where needed
- [ ] Code review
- [ ] Version control
- [ ] Consistent formatting

## 154. Avoid Giant Shell Scripts Inside Groovy Strings

Prefer application/build logic in:

```text
pom.xml
package.json scripts
Dockerfile
shell scripts
deployment tooling
```

with Jenkins orchestrating them.

## 155. Avoid Excessive Pipeline Cleverness

A pipeline should be understandable by another developer.

## 156. Keep CI/CD Concepts Visible

A reader should be able to identify:

```text
build
test
package
publish
deploy
verify
```

without decoding layers of custom abstraction.

---

# Tier 44 — Reusable Logic Awareness

## 157. Duplication

Multiple repositories may repeat Jenkins logic.

## 158. Shared Libraries Awareness

Jenkins Shared Libraries can centralize reusable Pipeline code.

Concept:

```text
many Jenkinsfiles
       │
       ▼
shared pipeline behavior
```

## 159. Scope Boundary

For this basic developer tree:

- [ ] Know why Shared Libraries exist
- [ ] Recognize their use
- [ ] Understand that organizations may provide them

Do not require advanced Shared Library authoring.

---

# Tier 45 — Basic Pipeline Performance

## 160. Measure Stage Duration

Look for slow stages:

```text
checkout
dependency install
compile
tests
Docker build
publish
```

## 161. Improve Deliberately

Possible improvements:

- [ ] Parallel frontend/backend tests
- [ ] Dependency caching
- [ ] Avoid unnecessary rebuilds
- [ ] Appropriate agents
- [ ] Faster tests

## 162. Reliability Before Speed

A 3-minute pipeline nobody trusts is worse than a 7-minute reliable pipeline.

---

# Tier 46 — Developer-Level Jenkins Operational Awareness

## 163. Queue

Understand why a build can wait:

```text
no matching agent
all executors busy
resource constraints
```

## 164. Abort

Know how/when to stop an unnecessary or stuck build.

## 165. Replay Awareness

Some Jenkins installations allow Pipeline replay/debugging.

Understand the danger of making an unreviewed replayed change the "real fix."

The durable fix belongs in the version-controlled Jenkinsfile.

## 166. Build History

Use build history to compare:

```text
last successful
first failing
recent change
```

## 167. Administrator Boundary

A developer should know when to escalate:

```text
agent offline
plugin missing/broken
controller issue
global credential/config problem
Jenkins upgrade problem
```

rather than trying to administer infrastructure they do not own.

---

# Tier 47 — Security Prerequisite Map

The Jenkins tree introduces immediate developer security, but broader topics should branch into dedicated trees.

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

For Jenkins specifically:

```text
CI/CD Fundamentals ──────┐
                         ├──► Jenkins
Docker ──────────────────┤
                         │
Security Fundamentals ───┘  (recommended companion)
```

Later:

```text
CI/CD Fundamentals ──────┐
Security Fundamentals ───┼──► DevSecOps
Jenkins/GitHub/GitLab ───┘
```

Potential future security trees:

- [ ] Security Fundamentals
- [ ] Web / Application Security
- [ ] Spring Security
- [ ] DevSecOps
- [ ] Container Security
- [ ] Cloud Security
- [ ] Software Supply-Chain Security

---

# Tier 48 — Alternative CI/CD Platforms

Jenkins is one implementation.

```text
                    CI/CD Fundamentals
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
     Jenkins         GitHub Actions      GitLab CI/CD
                           │
                           ▼
                    Other Alternatives
                           │
                           ├── Azure DevOps Pipelines
                           └── other CI/CD platforms
```

Azure DevOps Pipelines should be recognized as an alternative, but it is **not part of the current required skill-tree set**.

The transferable goal is:

```text
learn CI/CD once
      │
      ▼
learn how each tool expresses it
```

---

# Practical Competency Checkpoints

A learner completing this tree should be able to:

- [ ] Explain Jenkins as a CI/CD implementation rather than CI/CD itself
- [ ] Explain controller, agent, executor, and workspace at developer level
- [ ] Navigate jobs, build history, logs, results, and artifacts
- [ ] Explain Freestyle vs Pipeline jobs
- [ ] Create a Pipeline job
- [ ] Store a `Jenkinsfile` in Git
- [ ] Read basic Groovy-based Pipeline syntax
- [ ] Write a Declarative Pipeline
- [ ] Recognize Scripted Pipeline
- [ ] Use global/stage agents at a basic level
- [ ] Organize work into meaningful stages
- [ ] Execute shell/build steps
- [ ] Checkout source
- [ ] Trace Jenkins builds to Git commits
- [ ] Explain manual, webhook, polling, and scheduled triggers
- [ ] Understand Multibranch Pipeline
- [ ] Validate branches/pull requests conceptually
- [ ] Use basic `when` conditions
- [ ] Define environment variables
- [ ] Distinguish configuration from secrets
- [ ] Use Jenkins credentials conceptually and safely
- [ ] Use basic pipeline parameters
- [ ] Run Maven builds
- [ ] Run npm frontend builds
- [ ] Run JUnit/Mockito tests through Maven
- [ ] Publish JUnit-format test results
- [ ] Archive appropriate artifacts
- [ ] Build/tag/push Docker images using prerequisite Docker knowledge
- [ ] Run independent work in parallel
- [ ] Add timeouts
- [ ] Retry only appropriate transient operations
- [ ] Handle pipeline failures
- [ ] Use `post` actions
- [ ] Add useful notifications
- [ ] Add a manual production approval
- [ ] Orchestrate a basic deployment
- [ ] Verify deployment health
- [ ] Understand caching at a basic level
- [ ] Avoid dirty-workspace dependence
- [ ] Control build-tool versions
- [ ] Understand Jenkins plugins at developer level
- [ ] Use Jenkins syntax/documentation helpers
- [ ] Protect credentials and production permissions
- [ ] Recognize untrusted-code risks
- [ ] Explain basic CI/CD supply-chain risks
- [ ] Design a full-stack Jenkins pipeline
- [ ] Troubleshoot common Jenkins failures
- [ ] Keep a Jenkinsfile maintainable
- [ ] Recognize Shared Libraries without needing advanced authoring
- [ ] Improve basic pipeline performance
- [ ] Know when a problem belongs to a Jenkins administrator
- [ ] Transfer the same CI/CD design to GitHub Actions or GitLab CI/CD later

---

# Suggested Practice Progression

```text
1. Open Jenkins and inspect an existing job
        │
        ▼
2. Create a simple Pipeline job
        │
        ▼
3. Run one shell command
        │
        ▼
4. Move pipeline into a Jenkinsfile
        │
        ▼
5. Checkout Git source
        │
        ▼
6. Add Build stage
        │
        ▼
7. Add Test stage
        │
        ▼
8. Publish JUnit results
        │
        ▼
9. Add Package stage
        │
        ▼
10. Archive artifact
        │
        ▼
11. Add repository webhook
        │
        ▼
12. Convert to / explore Multibranch Pipeline
        │
        ▼
13. Add frontend npm build/test
        │
        ▼
14. Parallelize backend/frontend validation
        │
        ▼
15. Add Jenkins credentials
        │
        ▼
16. Build Docker image
        │
        ▼
17. Tag and push image
        │
        ▼
18. Deploy to test/staging target
        │
        ▼
19. Verify deployment
        │
        ▼
20. Add production approval
        │
        ▼
21. Add failure/post handling
        │
        ▼
22. Add security checks
        │
        ▼
23. Debug intentional failures
        │
        ▼
24. Reimplement same conceptual pipeline
    later in GitHub Actions
```

---

# Capstone — Jenkins Full-Stack Pipeline

Use the same capstone architecture that will later be implemented in GitHub Actions and GitLab CI/CD.

```text
Frontend: Angular or React
Backend:  Spring Boot
Database: PostgreSQL
Build:    npm + Maven
Tests:    frontend tests + JUnit + Mockito
Runtime:  Docker
```

## Capstone Dependency Model

```text
Git
 │
 ▼
CI/CD Fundamentals
 │
 ├───────────────┐
 ▼               ▼
Jenkins        Docker
 │               │
 └───────┬───────┘
         ▼
  Full-Stack Pipeline
         │
         ▼
Deployment Fundamentals
```

---

## Stage 1 — Repository

Repository contains approximately:

```text
project/
├── frontend/
├── backend/
├── docker-compose.yml or deployment-related files
├── Jenkinsfile
└── README.md
```

Do not store production secrets in the repository.

---

## Stage 2 — Trigger

Configure the conceptual flow:

```text
Git Push / Pull Request
          │
          ▼
        Webhook
          │
          ▼
        Jenkins
```

Feature branches should validate changes without automatically deploying production.

---

## Stage 3 — Checkout

```text
Jenkins Build
     │
     ▼
Checkout exact Git revision
     │
     ▼
Record revision/build identity
```

---

## Stage 4 — Parallel Validation

```text
                    Checkout
                       │
          ┌────────────┴────────────┐
          ▼                         ▼
       Backend                   Frontend
          │                         │
      ./mvnw test                 npm ci
          │                         │
      JUnit/Mockito                tests
          │                         │
      ./mvnw package              build
          │                         │
          └────────────┬────────────┘
                       ▼
                 Required Checks
```

If either required branch fails, the release does not continue.

---

## Stage 5 — Test Reporting

Publish backend JUnit-format reports.

Where the frontend test framework can produce compatible reports, publish useful machine-readable results there as well.

---

## Stage 6 — Package

Produce identifiable application outputs.

```text
Backend
  │
  ▼
JAR

Frontend
  │
  ▼
production build
```

---

## Stage 7 — Docker

Using the Docker prerequisite knowledge:

```text
Backend Artifact
      │
      ▼
Backend Image

Frontend Build
      │
      ▼
Frontend Image
```

Tag images using a traceable version such as a release version/build identifier/commit.

---

## Stage 8 — Registry

Use Jenkins credentials to authenticate.

```text
Jenkins
   │
   ▼
Container Registry
   │
   ├── frontend:<version>
   └── backend:<version>
```

Never hardcode the registry password in the Jenkinsfile.

---

## Stage 9 — Staging Deployment

Jenkins invokes the appropriate deployment mechanism.

```text
Published Images
      │
      ▼
Deploy Staging
      │
      ▼
Wait for Readiness
```

The actual deployment platform is intentionally outside this Jenkins tree.

---

## Stage 10 — Verification

Run:

```text
health check
API smoke test
frontend availability check
```

If verification fails:

```text
STOP
mark failure
preserve evidence
do not promote production
```

---

## Stage 11 — Production Gate

Implement Continuous Delivery:

```text
Staging Verified
      │
      ▼
Jenkins input
"Deploy production?"
      │
      ▼
Approved
      │
      ▼
Production Deployment
```

---

## Stage 12 — Post Actions

Regardless of outcome:

- [ ] Publish test reports
- [ ] Preserve useful results
- [ ] Clean temporary resources
- [ ] Notify appropriately
- [ ] Record deployment/build result

---

## Stage 13 — Security Review

Verify:

- [ ] No credentials in Git
- [ ] No credentials in Docker images
- [ ] No secrets printed in console logs
- [ ] Production credentials available only where required
- [ ] Feature-branch/untrusted builds cannot steal production secrets
- [ ] Jenkinsfile changes receive code review
- [ ] Docker images/dependencies can be scanned
- [ ] Jenkins uses only necessary plugins/integrations
- [ ] Deployment identity is traceable

---

## Stage 14 — Failure Drills

Intentionally introduce:

```text
Java compilation error
JUnit failure
frontend test failure
missing environment variable
invalid credential ID
registry authentication failure
Docker build failure
staging health-check failure
```

For each failure, answer:

```text
Which Jenkins stage fails?
What does the console show?
What evidence is preserved?
Does later work stop?
Who needs to fix it?
Is it a code problem,
Jenkins problem,
Docker problem,
or deployment-platform problem?
```

---

## Stage 15 — Final Jenkinsfile Mental Model

```text
pipeline
│
├── agent
│
├── environment
│
├── stages
│   │
│   ├── Checkout
│   │
│   ├── Validate
│   │   ├── Backend
│   │   └── Frontend
│   │
│   ├── Package
│   │
│   ├── Docker Build
│   │
│   ├── Publish
│   │
│   ├── Deploy Staging
│   │
│   ├── Verify
│   │
│   ├── Approval
│   │
│   └── Deploy Production
│   │
│   └── conditions where appropriate
│
└── post
    ├── reports
    ├── cleanup
    └── notification
```

---

# Interview Readiness

Be able to answer:

- [ ] What is Jenkins?
- [ ] Is Jenkins the same thing as CI/CD?
- [ ] What is the Jenkins controller?
- [ ] What is a Jenkins agent?
- [ ] What is an executor?
- [ ] What is a workspace?
- [ ] What is a Jenkins job?
- [ ] Freestyle job vs Pipeline job?
- [ ] What is a Jenkinsfile?
- [ ] Why store the Jenkinsfile in Git?
- [ ] What is Declarative Pipeline?
- [ ] Declarative vs Scripted Pipeline?
- [ ] What does `agent` mean?
- [ ] What is a stage?
- [ ] What is a step?
- [ ] How does Jenkins checkout source code?
- [ ] How can a Jenkins pipeline be triggered?
- [ ] Webhook vs SCM polling?
- [ ] What is a Multibranch Pipeline?
- [ ] How would Jenkins validate a feature branch?
- [ ] What does `when` do?
- [ ] How do environment variables work in a Jenkins pipeline?
- [ ] How should Jenkins handle passwords/tokens?
- [ ] What is a Jenkins credential ID?
- [ ] Why use `withCredentials`/credential binding?
- [ ] What are parameterized builds?
- [ ] How does Jenkins run a Maven project?
- [ ] How does Jenkins run an npm frontend?
- [ ] How does Jenkins know a test failed?
- [ ] What does the Jenkins `junit` step do?
- [ ] What is `archiveArtifacts`?
- [ ] How would Jenkins build and push a Docker image?
- [ ] What does the Jenkins agent need to build Docker images?
- [ ] Why run stages in parallel?
- [ ] Why use timeouts?
- [ ] When should you retry a Jenkins step?
- [ ] What is the `post` section?
- [ ] How can Jenkins implement a manual production approval?
- [ ] What should Jenkins do after deploying to staging?
- [ ] Cache vs artifact?
- [ ] Why can a dirty workspace cause problems?
- [ ] Why do build-tool versions matter on agents?
- [ ] What are Jenkins plugins?
- [ ] Why can plugins create security/compatibility concerns?
- [ ] Why is a Jenkinsfile security-sensitive?
- [ ] Why are untrusted pull requests dangerous when secrets are available?
- [ ] How should production credentials be scoped?
- [ ] What is a Jenkins Shared Library?
- [ ] How would you debug "works locally, fails in Jenkins"?
- [ ] How would you debug a failed checkout?
- [ ] How would you debug a Docker permission error?
- [ ] When should a developer escalate to a Jenkins administrator?
- [ ] How would you structure a Jenkins pipeline for Spring Boot + Angular/React?
- [ ] How would the same conceptual pipeline translate to GitHub Actions?

---

# Common Anti-Patterns

## Anti-Pattern 1 — Jenkins Equals CI/CD

```text
"I know CI/CD because I know Jenkins syntax."
```

Learn the CI/CD Fundamentals tree first.

## Anti-Pattern 2 — Everything Configured in the UI

Keep important pipeline logic in version-controlled Jenkinsfiles where practical.

## Anti-Pattern 3 — Giant Jenkinsfile

Do not move all Maven/npm/Docker/application logic into Jenkins.

## Anti-Pattern 4 — Hardcoded Secrets

```groovy
PASSWORD = 'production-password'
```

Never do this.

## Anti-Pattern 5 — Echoing Credentials

Masking is a safety mechanism, not permission to print secrets.

## Anti-Pattern 6 — Production Secrets in Feature Builds

Untrusted pipeline code should not receive powerful production credentials.

## Anti-Pattern 7 — `latest` Is the Only Image Tag

Use traceable image identity.

## Anti-Pattern 8 — Rebuild Separately for Production

Prefer promoting the tested artifact/image.

## Anti-Pattern 9 — Retry Until Green

Do not hide deterministic failures with repeated retries.

## Anti-Pattern 10 — Deployment Command = Successful Deployment

Verify health and functionality.

## Anti-Pattern 11 — Random Plugin Installation

Plugins have maintenance, compatibility, and security implications.

## Anti-Pattern 12 — Fixing Only Through Replay/UI

Make durable fixes in the version-controlled Jenkinsfile/build scripts.

## Anti-Pattern 13 — Jenkins Admin Knowledge as a Developer Requirement

Know the architecture and your pipeline. Escalate controller/agent/plugin infrastructure problems appropriately.

## Anti-Pattern 14 — Copying Jenkinsfile Examples Without Understanding

Translate each line back to the CI/CD concept it implements.

---

# Relationship to Other Skill Trees

```text
Git
 │
 ▼
CI/CD Fundamentals
 │
 ├─────────────────────┐
 ▼                     ▼
Jenkins               Docker
 │                     │
 └──────────┬──────────┘
            ▼
    Practical Jenkins CI
            │
            ▼
Deployment Fundamentals
            │
    ┌───────┼────────┐
    ▼       ▼        ▼
   AWS   Kubernetes  Other Targets
```

Testing dependencies:

```text
Java
 │
 ├── Maven
 ├── JUnit
 └── Mockito
      │
      ▼
 Jenkins Backend CI
```

Frontend dependencies:

```text
JavaScript / TypeScript
        │
        ▼
 Angular / React
        │
        ▼
      npm tooling
        │
        ▼
 Jenkins Frontend CI
```

Security progression:

```text
Security Fundamentals
        │
        ├──────────────┐
        ▼              ▼
Jenkins Security    Docker Security
        │              │
        └──────┬───────┘
               ▼
            DevSecOps
```

CI/CD platform siblings:

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
"I know what Jenkins is"
        │
        ▼
"I understand controller / agent / workspace"
        │
        ▼
"I can create a Pipeline job"
        │
        ▼
"I can write a basic Jenkinsfile"
        │
        ▼
"I can build and test a project"
        │
        ▼
"I can publish test results and artifacts"
        │
        ▼
"I can trigger pipelines from Git"
        │
        ▼
"I can handle branches and conditions"
        │
        ▼
"I can use Jenkins credentials safely"
        │
        ▼
"I can build and publish Docker images"
        │
        ▼
"I can orchestrate staging deployment"
        │
        ▼
"I can verify and gate production"
        │
        ▼
"I can troubleshoot common failures"
        │
        ▼
"I can maintain a developer-level Jenkinsfile"
        │
        ▼
"I understand where Jenkins ends
and Docker/deployment/security begin"
        │
        ▼
"I can translate the same CI/CD design
into another pipeline platform"
```

---

# Mastery Standard

> **Can I take a CI/CD pipeline that I already understand conceptually and independently implement it as a maintainable Jenkins Declarative Pipeline—using Git integration, appropriate agents, meaningful stages and steps, Maven/npm builds, automated tests and reports, versioned artifacts, Jenkins credentials, Docker image build/publish, branch conditions, failure handling, staging verification, and a controlled production gate—while keeping secrets safe, recognizing untrusted-code risks, troubleshooting common failures, and knowing when a problem belongs to Jenkins administration or the deployment platform rather than the Jenkinsfile?**

Final mental model:

```text
                         Git Repository
                              │
                              │ webhook
                              ▼
                            Jenkins
                              │
                              ▼
                          Jenkinsfile
                              │
             ┌────────────────┼────────────────┐
             ▼                ▼                ▼
          Checkout         Backend          Frontend
                              │                │
                            Maven             npm
                              │                │
                            Tests             Tests
                              │                │
             └────────────────┼────────────────┘
                              ▼
                           Package
                              │
                              ▼
                         Docker Build
                              │
                              ▼
                       Versioned Images
                              │
                              ▼
                    Authenticated Publish
                              │
                              ▼
                       Deploy Staging
                              │
                              ▼
                           Verify
                              │
                              ▼
                      Production Gate
                              │
                              ▼
                      Deploy Production
                              │
                              ▼
                        Post Actions
                              │
                   ┌──────────┼──────────┐
                   ▼          ▼          ▼
                Reports     Cleanup   Notification


Jenkins is one implementation:

                    CI/CD Fundamentals
                           │
         ┌─────────────────┼─────────────────┐
         ▼                 ▼                 ▼
      Jenkins        GitHub Actions      GitLab CI/CD

                         Alternative:
                   Azure DevOps Pipelines
```
