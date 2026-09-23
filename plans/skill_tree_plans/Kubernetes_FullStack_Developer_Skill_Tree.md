# Kubernetes Full-Stack Developer Skill Tree

> **Goal:** Learn Kubernetes at the **developer level**: understand its core architecture, write and apply Kubernetes manifests, deploy containerized applications, expose them through networking, configure them, attach persistent storage, observe health, scale workloads, perform rolling updates and rollbacks, and troubleshoot common failures.
>
> **Target level:** A full-stack developer who can independently deploy and operate a small-to-medium containerized application on Kubernetes without attempting to become a Kubernetes cluster administrator or platform engineer.
>
> **Primary prerequisites:** **Docker** and **Deployment Fundamentals**
>
> **Supporting prerequisites:** Linux/Ubuntu basics, YAML basics, HTTP/HTTPS fundamentals, basic networking, Git, and familiarity with a containerized Spring Boot + Angular/React application.
>
> **Hands-on environment:** Use a **local Kubernetes cluster** for learning. This tree uses **kind (Kubernetes IN Docker)** as the primary local learning path. Minikube is a valid alternative, but mastering multiple local-cluster tools is not a goal.
>
> **Security companion:** This tree introduces developer-level Kubernetes security concerns such as Secrets, least privilege awareness, container privileges, resource isolation, image trust, and avoiding sensitive data in manifests. Deeper Kubernetes security should become a separate skill tree.
>
> **Scope boundary:** This is **not** a Kubernetes administrator certification or cluster-engineering tree. Advanced cluster installation, control-plane administration, etcd operations, CNI/CSI internals, advanced RBAC, admission controllers, service meshes, multi-cluster architecture, production high availability, and managed-cloud Kubernetes administration are intentionally deferred.
>
> **Capstone:** Deploy a Dockerized Angular/React + Spring Boot + PostgreSQL application to a local Kubernetes cluster using Deployments, Services, ConfigMaps, Secrets, persistent storage, probes, multiple replicas, rolling updates, rollback, scaling, and failure/recovery exercises.
>
> **Important architecture note:** PostgreSQL is intentionally run inside Kubernetes in the capstone to teach Kubernetes storage and workload concepts. This does **not** mean that self-hosting PostgreSQL inside Kubernetes is automatically the preferred production architecture. A managed database such as Amazon RDS may be more appropriate in an AWS production architecture.

---

# Skill Tree Overview

```text
                         Docker
                            │
                            ▼
                 Deployment Fundamentals
                            │
                            ▼
                       Kubernetes
                            │
              ┌─────────────┼─────────────┐
              ▼             ▼             ▼
          Workloads      Networking    Configuration
              │             │             │
              ▼             ▼             ▼
       Pods/Deployments   Services    ConfigMap/Secret
              │             │             │
              └─────────────┼─────────────┘
                            ▼
                         Storage
                            │
                     PV / PVC / Volume
                            │
                            ▼
                     Health & Scaling
                            │
              Probes / Replicas / Resources
                            │
                            ▼
                   Updates & Rollbacks
                            │
                            ▼
                      Troubleshooting
                            │
                            ▼
                    Full-Stack Capstone
```

The central progression is:

```text
Docker asks:
"How do I package and run a container?"

                 │
                 ▼

Kubernetes asks:
"How do I declare, coordinate, expose,
maintain, and scale containerized workloads?"
```

---

# Tier 0 — Why Kubernetes Exists

## 1. From One Container to Many

Docker can run containers on a machine.

As systems grow, new problems appear:

```text
Which machine runs each container?
What happens if a container crashes?
How do applications find each other?
How do I run several copies?
How do I update them safely?
How do I attach persistent storage?
```

Kubernetes helps coordinate these concerns.

## 2. Container Orchestration

Kubernetes is a **container orchestration platform**.

At a high level it manages:

- [ ] Workload placement
- [ ] Desired replica counts
- [ ] Container restarts/replacement
- [ ] Application networking
- [ ] Service discovery
- [ ] Configuration
- [ ] Secrets
- [ ] Persistent storage abstractions
- [ ] Rolling deployments
- [ ] Scaling

## 3. Kubernetes Does Not Replace Docker Knowledge

```text
Docker knowledge
      │
      ▼
container image
container runtime concepts
ports
volumes
registries
      │
      ▼
Kubernetes
```

Kubernetes builds on container concepts.

---

# Tier 1 — Declarative Desired State

## 4. Imperative Mental Model

Without orchestration:

```text
"Start this container."
"Restart that one."
"Create another copy."
```

## 5. Declarative Mental Model

With Kubernetes:

```text
"I want 3 replicas of this application."
```

Kubernetes works to maintain that desired state.

## 6. Reconciliation

```text
Desired State
3 Pods
   │
   ▼
Kubernetes observes
   │
   ▼
Actual State
2 Pods
   │
   ▼
Kubernetes creates replacement
   │
   ▼
Actual State
3 Pods
```

This **reconciliation loop** is one of the most important Kubernetes concepts.

---

# Tier 2 — Cluster Architecture

## 7. Cluster

A Kubernetes cluster is the complete Kubernetes environment.

```text
Kubernetes Cluster
├── Control Plane
└── Worker Nodes
```

## 8. Control Plane

The control plane manages cluster state and coordination.

Developer-level understanding:

```text
"I submit desired state to Kubernetes,
and the control plane coordinates it."
```

## 9. Worker Node

A node is a machine that can run Kubernetes workloads.

```text
Cluster
├── Node A
│   ├── Pod
│   └── Pod
└── Node B
    └── Pod
```

## 10. Administration Boundary

Know what the control plane and nodes are.

Do not attempt to master:

```text
etcd administration
control-plane HA
manual cluster bootstrapping
certificate rotation
scheduler internals
```

in this tree.

---

# Tier 3 — Local Kubernetes with kind

## 11. Why Local First

A local cluster provides a safe learning environment:

```text
Laptop / WSL2
      │
      ▼
Docker
      │
      ▼
kind
      │
      ▼
Kubernetes Cluster
```

## 12. kind

**kind** means **Kubernetes IN Docker**.

It runs Kubernetes nodes as Docker containers.

## 13. Learning Goal

Be able to:

- [ ] Install/verify kind
- [ ] Create a cluster
- [ ] List clusters
- [ ] Delete a cluster
- [ ] Understand that the cluster is local and disposable

## 14. Minikube Awareness

Minikube is another popular local Kubernetes tool.

Do not learn two local-cluster systems simultaneously unless needed.

---

# Tier 4 — `kubectl`

## 15. Kubernetes CLI

`kubectl` is the primary command-line tool developers use to communicate with Kubernetes.

Mental model:

```text
Developer
   │
kubectl
   │
   ▼
Kubernetes API
   │
   ▼
Cluster
```

## 16. Essential Commands

Become comfortable with:

```text
kubectl get
kubectl describe
kubectl logs
kubectl exec
kubectl apply
kubectl delete
```

## 17. Discovery

Useful patterns:

```text
kubectl get pods
kubectl get deployments
kubectl get services
kubectl get all
```

## 18. Do Not Memorize Everything

Learn how to use:

```text
kubectl --help
kubectl <command> --help
```

and documentation.

---

# Tier 5 — kubeconfig and Context Awareness

## 19. Cluster Connection

`kubectl` needs configuration describing which Kubernetes cluster/context it should use.

## 20. Context

A context helps identify the current cluster/user/namespace combination.

Mental model:

```text
kubectl
   │
   ▼
Current Context
   │
   ▼
Target Cluster
```

## 21. Safety Habit

Before destructive operations, know:

> **Which cluster and namespace am I operating on?**

## 22. Developer Scope

Understand context switching and inspection.

Advanced kubeconfig/security administration belongs later.

---

# Tier 6 — Kubernetes API Objects

## 23. Resource/Object

Kubernetes manages objects representing desired state.

Examples:

```text
Pod
Deployment
Service
ConfigMap
Secret
PersistentVolumeClaim
```

## 24. API-Based System

Whether you use:

```text
kubectl
YAML
Helm
CI/CD
another client
```

you are ultimately interacting with the Kubernetes API.

## 25. Object Identity

Common metadata includes:

```text
name
namespace
labels
annotations
```

---

# Tier 7 — Kubernetes YAML

## 26. Manifest

A Kubernetes manifest describes an object.

Basic structure:

```yaml
apiVersion: ...
kind: ...
metadata:
  name: ...
spec:
  ...
```

## 27. Four Questions

When reading a manifest, ask:

```text
apiVersion → Which API?
kind       → What object?
metadata   → What is it called/labeled?
spec       → What state do I want?
```

## 28. Apply

```text
YAML
 │
 ▼
kubectl apply
 │
 ▼
Kubernetes API
 │
 ▼
Desired State
```

## 29. Source Control

Application Kubernetes manifests should generally be version controlled.

Do not commit sensitive secret values.

---

# Tier 8 — Pods

## 30. Pod

A Pod is Kubernetes's smallest deployable workload unit.

Common beginner mental model:

```text
Pod
└── Application Container
```

## 31. Pods Can Contain Multiple Containers

A Pod can contain multiple tightly related containers.

However:

```text
one main application container per Pod
```

is a useful starting model.

## 32. Shared Pod Context

Containers in the same Pod share important networking/storage context.

## 33. Pod Lifecycle

Pods are disposable workload units.

Do not design around:

```text
"this exact Pod must live forever"
```

---

# Tier 9 — Running a Pod

## 34. First Workload

Deploy a simple container as a Pod.

Observe:

```text
Pending
   │
   ▼
ContainerCreating
   │
   ▼
Running
```

## 35. Inspect

Practice:

```text
kubectl get pods
kubectl describe pod ...
kubectl logs ...
```

## 36. Delete

Delete the Pod.

Observe that a standalone Pod does not magically reappear unless another controller owns its desired state.

This prepares the learner for Deployments.

---

# Tier 10 — Labels

## 37. Label

Labels are key/value metadata used to organize and select Kubernetes objects.

Example concept:

```text
app=guestbook
tier=backend
```

## 38. Why Labels Matter

Kubernetes resources often find each other through label selectors.

```text
Service
   │
selector
   │
   ▼
Pods with matching labels
```

## 39. Stable Identity

Pod names/IPs can change.

Labels describe logical membership.

---

# Tier 11 — Deployments

## 40. Deployment

A Deployment manages stateless application Pods and their desired rollout state.

```text
Deployment
    │
    ▼
ReplicaSet
    │
    ▼
Pods
```

## 41. Why Use a Deployment

Instead of manually maintaining Pods:

```text
Deployment says:
"I want 3 backend replicas."
```

Kubernetes maintains them.

## 42. Deployment Manifest

Understand fields for:

```text
replicas
selector
Pod template
container image
ports
```

## 43. Normal Application Path

For Spring Boot/Angular/React workloads:

```text
Pod directly
→ learning

Deployment
→ normal managed workload
```

---

# Tier 12 — ReplicaSets

## 44. ReplicaSet

A ReplicaSet maintains a desired number of matching Pods.

## 45. Deployment Relationship

```text
Deployment
    │
    ▼
ReplicaSet
    │
    ▼
Pods
```

## 46. Developer Practice

Understand ReplicaSets and inspect them.

Usually manage the **Deployment**, not ReplicaSets directly.

---

# Tier 13 — Self-Healing

## 47. Failure Exercise

Create a Deployment with multiple replicas.

Then delete one Pod.

```text
Deployment wants 3
       │
       ▼
3 Pods running
       │
delete one
       ▼
2 Pods running
       │
Kubernetes reconciles
       ▼
new Pod created
       │
       ▼
3 Pods running
```

## 48. Important Distinction

Kubernetes did not "repair" the old Pod.

It restored the desired state by creating another workload instance.

---

# Tier 14 — Services

## 49. Problem

Pods are dynamic.

Their individual network identities can change.

Clients need stable access.

## 50. Service

A Kubernetes Service provides stable network access to a selected set of Pods.

```text
Client
  │
  ▼
Service
  │
  ├── Pod
  ├── Pod
  └── Pod
```

## 51. Selection

Services commonly route to Pods using labels/selectors.

## 52. Service Discovery

Applications inside Kubernetes can use stable Service names instead of tracking Pod IPs manually.

---

# Tier 15 — ClusterIP

## 53. ClusterIP

ClusterIP is the common default Service type.

It exposes the Service **inside the cluster**.

```text
Frontend Pod
     │
     ▼
backend-service
     │
     ▼
Backend Pods
```

## 54. Backend Example

Instead of:

```text
http://10.x.x.x:8080
```

the frontend/server-side workload may conceptually use:

```text
http://backend-service:8080
```

depending on architecture.

---

# Tier 16 — NodePort

## 55. NodePort

NodePort exposes a Service on a port of cluster nodes.

Mental model:

```text
Outside
  │
  ▼
Node : NodePort
  │
  ▼
Service
  │
  ▼
Pods
```

## 56. Learning Use

NodePort is useful for understanding external access in simple/local environments.

It is not automatically the preferred production exposure strategy.

---

# Tier 17 — LoadBalancer Service

## 57. LoadBalancer

A Service of type LoadBalancer requests external load-balancing integration from a supporting environment/cloud.

```text
Internet
   │
   ▼
External Load Balancer
   │
   ▼
Kubernetes Service
   │
   ▼
Pods
```

## 58. Local Limitation

A local kind cluster does not automatically behave exactly like AWS or another managed cloud.

Understand the concept without forcing cloud infrastructure into this tree.

## 59. Future Connection

Later:

```text
Kubernetes
    │
    ▼
Amazon EKS
    │
    ▼
AWS Load Balancing Integration
```

---

# Tier 18 — Port Mapping Mental Model

## 60. Container Port

The application listens inside its container.

## 61. Service Port

The Service exposes a stable port.

## 62. Target Port

The Service sends traffic to the target application port.

Mental model:

```text
Client
  │
Service Port
  │
  ▼
Service
  │
Target Port
  │
  ▼
Pod / Container
```

## 63. Troubleshooting Habit

When traffic fails, check every layer instead of randomly changing ports.

---

# Tier 19 — Namespaces

## 64. Namespace

Namespaces provide logical grouping/isolation of Kubernetes resources.

Concept:

```text
Cluster
├── development namespace
├── testing namespace
└── production namespace
```

## 65. Developer Use

Practice:

```text
create namespace
list resources in namespace
apply resources to namespace
delete practice namespace
```

## 66. Namespace Is Not Complete Security

Namespaces help organize/isolate resources but do not automatically create a complete security boundary.

---

# Tier 20 — ConfigMaps

## 67. Configuration

Applications need environment-specific configuration.

Examples:

```text
API URL
feature flag
application mode
non-secret settings
```

## 68. ConfigMap

A ConfigMap stores non-sensitive configuration.

```text
ConfigMap
    │
    ▼
Pod
    │
    ▼
Environment / File
```

## 69. Separation

```text
Container Image
= application package

ConfigMap
= environment configuration
```

This supports reusable images.

---

# Tier 21 — Secrets

## 70. Secret

Kubernetes has a Secret object for sensitive configuration.

Examples:

```text
database password
API token
credential
```

## 71. Critical Warning

```text
Kubernetes Secret
≠
magically secure secret vault
```

Basic Secret values may be represented/encoded in ways that are not equivalent to strong encryption by themselves.

## 72. Git Safety

Do not commit real secret values into a public or ordinary source repository merely because the YAML says:

```text
kind: Secret
```

## 73. Future Security Tree

Later topics:

```text
external secret managers
encryption at rest
RBAC
service accounts
secret rotation
admission policies
cloud identity
```

---

# Tier 22 — Environment Variables from Config

## 74. Inject Configuration

Pods can receive values from ConfigMaps and Secrets.

Mental model:

```text
ConfigMap ─────┐
               ├──► Pod Environment
Secret ────────┘
```

## 75. Spring Boot Connection

This maps naturally to Spring Boot environment-based configuration.

```text
Kubernetes configuration
        │
        ▼
environment variable
        │
        ▼
Spring Boot application.yml placeholder
```

This connects Kubernetes to earlier Spring Boot learning.

---

# Tier 23 — Volumes

## 76. Container Filesystem Problem

Containers and Pods are replaceable.

Important data may need a lifecycle independent of a particular container.

## 77. Kubernetes Volume

A volume provides storage accessible to containers in a Pod.

## 78. Volume Lifetime

Different Kubernetes volume types have different persistence behavior.

Do not assume every volume survives Pod replacement.

---

# Tier 24 — PersistentVolume

## 79. PersistentVolume (PV)

A PersistentVolume represents storage made available to Kubernetes.

Concept:

```text
Actual Storage
      │
      ▼
PersistentVolume
```

## 80. Abstraction

The backing storage can differ by environment.

Examples conceptually:

```text
local storage
cloud block storage
network storage
```

## 81. AWS Connection

Later, an AWS Kubernetes environment can use storage backed by services such as EBS through appropriate Kubernetes storage integration.

This tree does not configure EKS.

---

# Tier 25 — PersistentVolumeClaim

## 82. PVC

A PersistentVolumeClaim represents a workload's request for persistent storage.

```text
Pod
 │
 ▼
PVC
 │
 ▼
PV
 │
 ▼
Actual Storage
```

## 83. Why the Abstraction Matters

The application asks for storage through Kubernetes rather than hardcoding a particular physical disk.

## 84. Practice

Create:

```text
PV
PVC
Pod/Deployment using PVC
```

in the local learning environment.

---

# Tier 26 — Storage Classes Awareness

## 85. Dynamic Provisioning Concept

In real clusters, storage can often be provisioned dynamically.

Mental model:

```text
PVC
 │
 ▼
StorageClass
 │
 ▼
Provisioner
 │
 ▼
Storage
```

## 86. Developer Scope

Know what StorageClasses are for.

Do not master CSI drivers or storage-provider administration here.

---

# Tier 27 — PostgreSQL Persistence Exercise

## 87. Educational Database

Run PostgreSQL in Kubernetes for learning.

```text
PostgreSQL Pod
      │
      ▼
PVC
      │
      ▼
PV
      │
      ▼
Persistent Data
```

## 88. Failure Exercise

Replace/restart the PostgreSQL workload and verify intended persistent data remains.

## 89. Architecture Warning

This teaches Kubernetes persistence.

It does not prove that self-managed PostgreSQL inside Kubernetes is the best production database architecture.

---

# Tier 28 — Liveness Probes

## 90. Liveness

A liveness probe asks roughly:

> **Is this container still healthy enough to keep running?**

## 91. Failure Behavior

If liveness repeatedly fails, Kubernetes can restart the container according to its workload behavior.

```text
Container
   │
liveness fails
   │
   ▼
restart
```

## 92. Do Not Make Probes Arbitrary

A poorly designed liveness probe can create restart loops.

---

# Tier 29 — Readiness Probes

## 93. Readiness

A readiness probe asks roughly:

> **Is this application ready to receive traffic?**

## 94. Service Relationship

```text
Pod starts
   │
   ▼
Not Ready
   │
   ▼
excluded from normal Service traffic
   │
readiness succeeds
   ▼
Ready
   │
   ▼
receives traffic
```

## 95. Liveness vs Readiness

```text
Liveness
"Should this container keep running?"

Readiness
"Should traffic be sent here?"
```

---

# Tier 30 — Startup Probes

## 96. Startup Probe

A startup probe helps applications that need additional startup time.

## 97. Purpose

It can prevent liveness behavior from prematurely killing a slow-starting application.

## 98. Three-Probe Mental Model

```text
Startup
"Has the app finished starting?"

Readiness
"Can it receive traffic?"

Liveness
"Is it still healthy?"
```

---

# Tier 31 — Spring Boot Actuator Connection

## 99. Health Endpoints

Spring Boot Actuator can expose health information useful for Kubernetes probes.

Concept:

```text
Kubernetes Probe
      │
      ▼
HTTP Health Endpoint
      │
      ▼
Spring Boot
```

## 100. Responsibility

The application should expose meaningful health information.

Kubernetes uses that information to make orchestration decisions.

---

# Tier 32 — Replicas and Manual Scaling

## 101. Replica Count

A Deployment can request multiple application instances.

```text
Deployment
replicas: 3
    │
    ├── Pod
    ├── Pod
    └── Pod
```

## 102. Scale

Practice changing the replica count.

## 103. Statelessness

Horizontal replication is easiest when application instances do not depend on unique local state.

This reinforces earlier application architecture concepts.

---

# Tier 33 — CPU and Memory Requests

## 104. Requests

Resource requests communicate the amount of CPU/memory a workload expects to need for scheduling purposes.

Concept:

```text
Pod says:
"I need approximately this much resource."

Scheduler
   │
   ▼
chooses suitable node
```

## 105. Developer Responsibility

Developers should understand their application's approximate resource needs.

---

# Tier 34 — CPU and Memory Limits

## 106. Limits

Limits constrain resource usage.

## 107. CPU vs Memory Behavior Awareness

CPU and memory limits can affect applications differently.

At a developer level, understand that incorrect limits can cause:

```text
poor performance
throttling
out-of-memory termination
instability
```

## 108. Do Not Guess Production Values Blindly

Measure and observe workloads.

---

# Tier 35 — Scheduling Awareness

## 109. Scheduler

Kubernetes decides where Pods should run based on cluster state and workload requirements.

```text
Pod Desired
    │
    ▼
Scheduler
    │
    ▼
Suitable Node
```

## 110. Developer Scope

Understand the scheduler's purpose.

Advanced:

```text
affinity
anti-affinity
taints
tolerations
topology constraints
```

can be introduced later or only briefly recognized here.

---

# Tier 36 — Rolling Updates

## 111. Image Update

Change a Deployment to a new application image/version.

## 112. Rolling Deployment

Kubernetes can gradually replace old Pods with new Pods.

```text
v1 v1 v1
   │
   ▼
v2 v1 v1
   │
   ▼
v2 v2 v1
   │
   ▼
v2 v2 v2
```

## 113. Goal

Reduce disruption while changing versions.

## 114. Verification

Observe:

```text
kubectl get pods
kubectl rollout status
```

and application behavior.

---

# Tier 37 — Rollback

## 115. Bad Release

Suppose version 2 fails.

```text
v1
 │
 ▼
v2
 │
failure
 ▼
rollback
 │
 ▼
previous working revision
```

## 116. Practice

Perform a deliberately broken rollout and rollback.

## 117. Important Distinction

Application rollback does not automatically solve every database/schema compatibility problem.

Deployment Fundamentals should cover broader release compatibility.

---

# Tier 38 — Image Pulling and Registries

## 118. Image Source

Kubernetes nodes need access to container images.

```text
Container Registry
       │
       ▼
Kubernetes Node
       │
       ▼
Pod
```

## 119. Local kind Images

For local learning, understand how images become available to kind.

## 120. Private Registry Awareness

Private registries require authentication/configuration.

Detailed registry security belongs in Docker/CI-CD/security trees.

---

# Tier 39 — Jobs

## 121. Job

A Kubernetes Job runs work intended to complete.

```text
Job
 │
 ▼
Pod
 │
 ▼
Task completes
```

## 122. Examples

```text
data migration
batch processing
one-time maintenance
```

## 123. Scope

Create one simple Job.

Do not turn batch architecture into a major branch of this tree.

---

# Tier 40 — CronJobs

## 124. CronJob

A CronJob schedules Jobs.

```text
Schedule
   │
   ▼
CronJob
   │
   ▼
Job
   │
   ▼
Pod
```

## 125. Example

```text
nightly cleanup
scheduled report
periodic task
```

## 126. Developer Scope

Understand and create a simple example.

---

# Tier 41 — StatefulSets Awareness

## 127. StatefulSet

A StatefulSet supports workloads needing more stable identity/storage behavior than ordinary interchangeable replicas.

## 128. Why It Exists

Some distributed/stateful applications care about:

```text
stable identity
ordered behavior
persistent storage association
```

## 129. Scope

Understand why StatefulSets exist and inspect a simple example.

Do not master production database clustering here.

---

# Tier 42 — DaemonSets Awareness

## 130. DaemonSet

A DaemonSet ensures a Pod runs on applicable nodes.

Mental model:

```text
Node A → agent Pod
Node B → agent Pod
Node C → agent Pod
```

## 131. Common Uses

Examples include node-level:

```text
logging agents
monitoring agents
network components
```

## 132. Scope

Recognize the workload type.

Do not build cluster infrastructure around it in this developer tree.

---

# Tier 43 — Ingress

## 133. External HTTP Routing Problem

Applications may expose multiple HTTP services.

```text
Internet
   │
   ├── frontend
   └── API
```

## 134. Ingress

Ingress is a Kubernetes API mechanism for defining HTTP/HTTPS routing rules.

Concept:

```text
Client
  │
  ▼
Ingress
  │
  ├── /      → frontend Service
  └── /api   → backend Service
```

## 135. Ingress Controller

Ingress resources require an implementation/controller to actually handle traffic.

```text
Ingress Resource
      │
      ▼
Ingress Controller
      │
      ▼
Services
```

## 136. Scope Boundary

Learn basic routing.

Leave:

```text
advanced ingress controllers
production TLS automation
certificate management
complex routing
WAF integration
```

for later.

---

# Tier 44 — DNS and Service Discovery

## 137. Internal Names

Kubernetes provides DNS-based service discovery in normal cluster configurations.

## 138. Stable Service Name

```text
Backend Pods
change constantly

backend-service
remains stable
```

## 139. Full-Stack Connection

The backend can reach PostgreSQL through a stable Kubernetes Service name rather than a Pod IP.

```text
Backend Pod
    │
    ▼
postgres-service
    │
    ▼
PostgreSQL Pod
```

---

# Tier 45 — Logs

## 140. Container Logs

Use:

```text
kubectl logs
```

to inspect application output.

## 141. Multiple Containers

When a Pod contains multiple containers, identify which container's logs you need.

## 142. Previous Crash Awareness

Learn that troubleshooting may require inspecting logs from a prior container execution where supported.

## 143. Observability Boundary

Centralized logging systems such as ELK belong in their own tree.

---

# Tier 46 — `describe` and Events

## 144. `kubectl describe`

`describe` provides detailed object state and related events.

## 145. Events

Events can explain problems such as:

```text
image pull failure
scheduling failure
mount failure
probe failure
container restart
```

## 146. Troubleshooting Habit

When a Pod fails:

```text
kubectl get
     │
     ▼
kubectl describe
     │
     ▼
kubectl logs
```

is a strong starting sequence.

---

# Tier 47 — `exec`

## 147. Execute Inside Container

`kubectl exec` can run commands inside a running container.

Concept:

```text
Developer
   │
kubectl exec
   │
   ▼
Container
```

## 148. Useful Checks

Examples:

```text
environment
filesystem
DNS resolution
network request
process context
```

## 149. Do Not Debug by Permanently Editing Containers

Containers should remain reproducible from images/configuration.

Fix source/configuration and redeploy.

---

# Tier 48 — Common Pod States and Failures

## 150. Pending

Possible causes:

```text
scheduling
resource shortage
storage
image/configuration dependencies
```

## 151. CrashLoopBackOff

The container repeatedly starts and fails.

Investigate application logs/configuration/probes.

## 152. ImagePullBackOff

Kubernetes cannot successfully retrieve the image.

Check:

```text
image name
tag
registry
authentication
network
```

## 153. OOMKilled

The container exceeded available/allowed memory.

Investigate memory behavior and resource configuration.

---

# Tier 49 — Troubleshooting Networking

## 154. Service Exists but App Fails

Check:

```text
Pod running?
Pod ready?
labels match?
Service selector correct?
ports correct?
application listening?
```

## 155. Selector Failure

A Service with the wrong selector may have no appropriate endpoints.

Mental model:

```text
Service
  │
wrong selector
  │
  X
Pods
```

## 156. DNS Failure

Check:

```text
correct Service name?
correct namespace?
Service exists?
cluster DNS functioning?
```

---

# Tier 50 — Troubleshooting Configuration and Storage

## 157. Config Failure

Check:

```text
ConfigMap exists?
Secret exists?
correct key?
correct environment reference?
Pod restarted/recreated after relevant change?
```

## 158. PVC Pending

Check:

```text
PVC status
PV availability
StorageClass
access mode
requested capacity
events
```

## 159. Mount Failure

Check:

```text
volume definition
claim
mount path
permissions
events
```

---

# Tier 51 — Horizontal Pod Autoscaling Awareness

## 160. Autoscaling Concept

Kubernetes can adjust replica counts based on observed metrics when appropriate components/configuration exist.

```text
Load increases
      │
      ▼
Metric rises
      │
      ▼
Autoscaler
      │
      ▼
More replicas
```

## 161. Developer Scope

Understand why Horizontal Pod Autoscaling exists.

Perform a basic demonstration only if the local environment supports the necessary metrics setup cleanly.

## 162. Autoscaling Is Not Magic

Applications still need:

```text
appropriate architecture
resource requests
metrics
capacity
correct scaling behavior
```

---

# Tier 52 — Security Fundamentals

## 163. Containers Are Not Automatically Safe

Kubernetes orchestration does not make insecure applications or images secure.

## 164. Avoid Privileged Containers

Do not grant unnecessary host/system privileges.

## 165. Least Privilege

Apply the same principle throughout:

```text
minimum permissions
minimum network exposure
minimum secrets
minimum container privilege
```

## 166. Trusted Images

Know where container images come from.

Avoid blindly deploying unknown images.

---

# Tier 53 — Service Accounts and RBAC Awareness

## 167. Service Account

Pods can operate using Kubernetes identities called ServiceAccounts.

## 168. RBAC

Role-Based Access Control can restrict what identities may do through the Kubernetes API.

Mental model:

```text
Identity
   │
   ▼
Role / Permissions
   │
   ▼
Allowed API Actions
```

## 169. Scope Boundary

Understand why these exist.

Advanced RBAC design belongs in a Kubernetes Security tree.

---

# Tier 54 — Network Policy Awareness

## 170. Default Connectivity Concern

Being in the same cluster does not mean every workload should necessarily communicate freely.

## 171. NetworkPolicy

NetworkPolicy can describe allowed network communication in supported cluster networking environments.

## 172. Scope

Recognize the concept.

Do not make advanced zero-trust Kubernetes networking part of this introductory developer tree.

---

# Tier 55 — Helm Awareness

## 173. Problem

Real Kubernetes applications may require many manifests.

```text
Deployment
Service
ConfigMap
Secret references
Ingress
PVC
...
```

## 174. Helm

Helm is a package/configuration management tool commonly used with Kubernetes.

Mental model:

```text
Kubernetes YAML first
        │
        ▼
Understand resources
        │
        ▼
Helm later
```

## 175. Separate Skill Tree

Do **not** hide Kubernetes fundamentals behind Helm yet.

Create a dedicated Helm skill tree later.

---

# Tier 56 — CI/CD Integration Awareness

## 176. Manual First

Learn:

```text
kubectl apply
rollout
verification
rollback
```

before automating everything.

## 177. Later Automation

Existing CI/CD trees can eventually perform:

```text
Build
  │
  ▼
Test
  │
  ▼
Docker Image
  │
  ▼
Registry
  │
  ▼
Kubernetes Deployment
```

## 178. Platform Independence

The same Kubernetes manifests/concepts can participate in:

```text
Jenkins
GitHub Actions
GitLab CI/CD
```

---

# Tier 57 — AWS and EKS Awareness

## 179. Kubernetes First

Learn Kubernetes independently before learning AWS-specific Kubernetes management.

```text
Docker
  │
  ▼
Kubernetes
  │
  ▼
AWS + Kubernetes
  │
  ▼
Amazon EKS
```

## 180. EKS

Amazon Elastic Kubernetes Service is AWS's managed Kubernetes offering.

## 181. Existing AWS Knowledge Connection

Future EKS learning can connect:

```text
AWS networking
IAM
EBS
load balancing
container registries
Kubernetes
```

## 182. Scope Boundary

No EKS cluster is required in this skill tree.

---

# Tier 58 — Full-Stack Kubernetes Architecture

A familiar application can be represented as:

```text
                         User
                          │
                          ▼
                  Ingress / Exposure
                          │
               ┌──────────┴──────────┐
               ▼                     ▼
        Frontend Service       Backend Service
               │                     │
               ▼                     ▼
      Frontend Deployment     Backend Deployment
               │                     │
          ┌────┴────┐          ┌─────┼─────┐
          ▼         ▼          ▼     ▼     ▼
        Pod       Pod        Pod   Pod   Pod
                                    │
                                    ▼
                             PostgreSQL Service
                                    │
                                    ▼
                            PostgreSQL Workload
                                    │
                                    ▼
                                   PVC
                                    │
                                    ▼
                                   PV
```

Configuration:

```text
ConfigMap ───────────────┐
                         ├──► Application Pods
Secret ──────────────────┘
```

Health:

```text
Startup Probe
Readiness Probe
Liveness Probe
```

---

# Practical Competency Checkpoints

A learner completing this tree should be able to:

- [ ] Explain why Kubernetes exists
- [ ] Explain container orchestration
- [ ] Explain desired state and reconciliation
- [ ] Explain cluster, control plane, and node
- [ ] Create/delete a local kind cluster
- [ ] Use `kubectl`
- [ ] Inspect/switch Kubernetes context
- [ ] Read and write basic Kubernetes YAML
- [ ] Explain API objects
- [ ] Create and inspect Pods
- [ ] Explain why Pods are disposable
- [ ] Use labels/selectors
- [ ] Create Deployments
- [ ] Explain ReplicaSets
- [ ] Demonstrate self-healing
- [ ] Create Services
- [ ] Explain ClusterIP
- [ ] Explain NodePort
- [ ] Explain LoadBalancer conceptually
- [ ] Explain Service port vs target port
- [ ] Use namespaces
- [ ] Create/use ConfigMaps
- [ ] Create/use Secrets safely
- [ ] Inject configuration into Pods
- [ ] Explain Kubernetes volumes
- [ ] Explain PV and PVC
- [ ] Use persistent storage locally
- [ ] Explain StorageClasses at a high level
- [ ] Persist PostgreSQL data educationally
- [ ] Configure liveness probes
- [ ] Configure readiness probes
- [ ] Explain startup probes
- [ ] Connect Spring Boot health endpoints to probes
- [ ] Scale Deployment replicas
- [ ] Configure basic CPU/memory requests and limits
- [ ] Explain scheduling at a high level
- [ ] Perform rolling updates
- [ ] Observe rollout status
- [ ] Perform rollback
- [ ] Explain registry/image-pull behavior
- [ ] Create a simple Job
- [ ] Create a simple CronJob
- [ ] Explain StatefulSets
- [ ] Explain DaemonSets
- [ ] Explain Ingress and ingress controllers
- [ ] Use Kubernetes Service discovery
- [ ] Read container logs
- [ ] Use `describe` and events
- [ ] Use `exec` for diagnosis
- [ ] Diagnose CrashLoopBackOff
- [ ] Diagnose ImagePullBackOff
- [ ] Recognize OOMKilled
- [ ] Troubleshoot Service selectors/ports
- [ ] Troubleshoot ConfigMaps/Secrets
- [ ] Troubleshoot PVC/mount problems
- [ ] Explain horizontal autoscaling at a basic level
- [ ] Recognize basic Kubernetes security concerns
- [ ] Explain ServiceAccounts/RBAC at a high level
- [ ] Explain NetworkPolicy at a high level
- [ ] Explain why Helm should follow Kubernetes fundamentals
- [ ] Explain how CI/CD can deploy to Kubernetes
- [ ] Explain where EKS fits without confusing EKS with Kubernetes itself

---

# Suggested Practice Progression

```text
1. Verify Docker
       │
       ▼
2. Install/verify kubectl
       │
       ▼
3. Install/verify kind
       │
       ▼
4. Create local cluster
       │
       ▼
5. Inspect context/nodes
       │
       ▼
6. Run first Pod
       │
       ▼
7. Inspect logs/describe
       │
       ▼
8. Delete standalone Pod
       │
       ▼
9. Write Deployment YAML
       │
       ▼
10. Scale replicas
       │
       ▼
11. Kill Pod and observe replacement
       │
       ▼
12. Add labels/selectors
       │
       ▼
13. Create ClusterIP Service
       │
       ▼
14. Test internal Service discovery
       │
       ▼
15. Expose locally
       │
       ▼
16. Create namespace
       │
       ▼
17. Add ConfigMap
       │
       ▼
18. Add Secret
       │
       ▼
19. Inject configuration
       │
       ▼
20. Add PV/PVC
       │
       ▼
21. Persist sample data
       │
       ▼
22. Deploy PostgreSQL
       │
       ▼
23. Connect backend through Service
       │
       ▼
24. Add readiness probe
       │
       ▼
25. Add liveness probe
       │
       ▼
26. Add startup probe where useful
       │
       ▼
27. Add requests/limits
       │
       ▼
28. Deploy new image version
       │
       ▼
29. Observe rolling update
       │
       ▼
30. Deploy broken version
       │
       ▼
31. Roll back
       │
       ▼
32. Create Job
       │
       ▼
33. Create CronJob
       │
       ▼
34. Introduce Ingress
       │
       ▼
35. Deploy full-stack application
       │
       ▼
36. Run failure drills
       │
       ▼
37. Clean namespace/resources
       │
       ▼
38. Delete local cluster
```

---

# Capstone — Kubernetes Full-Stack Application

## Objective

Deploy the familiar full-stack architecture using Kubernetes primitives rather than manually starting individual Docker containers.

```text
Angular / React
      │
      ▼
Spring Boot
      │
      ▼
PostgreSQL
```

---

## Phase 1 — Container Images

Use existing Docker knowledge to create:

```text
frontend image
backend image
PostgreSQL image/reference
```

The Kubernetes tree assumes the learner understands Docker images before continuing.

---

## Phase 2 — Create Cluster

```text
Docker
  │
  ▼
kind
  │
  ▼
Local Kubernetes Cluster
```

Verify:

```text
cluster exists
kubectl context correct
node ready
```

---

## Phase 3 — Namespace

Create a dedicated namespace:

```text
guestbook
```

Keep capstone resources logically grouped.

---

## Phase 4 — Backend Deployment

```text
Backend Deployment
       │
       ├── Pod
       └── Pod
```

Start with multiple replicas if the application is ready for stateless replication.

---

## Phase 5 — Backend Service

```text
Frontend
   │
   ▼
backend-service
   │
   ├── backend Pod
   └── backend Pod
```

Do not connect using Pod IPs.

---

## Phase 6 — Frontend Deployment

```text
Frontend Deployment
       │
       ├── Pod
       └── Pod
```

Expose it through the appropriate local learning mechanism.

---

## Phase 7 — PostgreSQL

For educational purposes:

```text
PostgreSQL Workload
        │
        ▼
PostgreSQL Service
        │
        ▼
PersistentVolumeClaim
        │
        ▼
PersistentVolume
```

Backend configuration should reference the **PostgreSQL Service**, not the Pod IP.

---

## Phase 8 — Configuration

Move non-sensitive configuration to ConfigMaps.

```text
ConfigMap
├── database host
├── application setting
└── other non-secret config
```

Move sensitive values to Secret handling appropriate for the learning environment.

```text
Secret
├── database password
└── application secret/passcode
```

Explicitly document that Kubernetes Secrets require additional security practices in real production environments.

---

## Phase 9 — Health

Backend:

```text
startup probe
readiness probe
liveness probe
       │
       ▼
Spring Boot health endpoints
```

Frontend:

Use an appropriate readiness/liveness strategy for its serving container.

---

## Phase 10 — Resource Management

Configure reasonable learning values for:

```text
CPU request
memory request
CPU limit
memory limit
```

Observe Pod state.

Do not pretend the learning values are production sizing recommendations.

---

## Phase 11 — Failure Recovery

Delete one backend Pod.

Observe:

```text
3 desired
   │
delete 1
   ▼
2 actual
   │
Kubernetes reconciles
   ▼
3 actual
```

Explain why the replacement Pod may have a different name/IP.

---

## Phase 12 — Scaling

Scale backend:

```text
2 replicas
    │
    ▼
4 replicas
    │
    ▼
Service continues selecting Pods
```

Then scale back down.

---

## Phase 13 — Persistent Data

Create guestbook data.

Replace/restart the PostgreSQL workload appropriately.

Verify that intended database data persists through the PVC/PV relationship.

Explain:

```text
Pod lifecycle
≠
persistent data lifecycle
```

---

## Phase 14 — Rolling Update

Build a visible v2 of the backend or frontend.

```text
v1 v1 v1
   │
   ▼
v2 v1 v1
   │
   ▼
v2 v2 v1
   │
   ▼
v2 v2 v2
```

Observe rollout status and Pod replacement.

---

## Phase 15 — Broken Deployment

Deploy an intentionally invalid/broken image or configuration.

Observe:

```text
Pod status
events
logs
readiness
rollout behavior
```

Diagnose before changing anything.

---

## Phase 16 — Rollback

Return to the previous working revision.

Verify the application after rollback.

---

## Phase 17 — Ingress

Introduce basic HTTP routing:

```text
Client
  │
  ▼
Ingress
  │
  ├── /     → frontend-service
  │
  └── /api  → backend-service
```

Keep TLS and production ingress-controller engineering outside the capstone.

---

## Phase 18 — Job/CronJob

Create one small background example.

For example:

```text
Job
└── one-time database/report task

CronJob
└── periodic demonstration task
```

The application does not need to depend on these for its core functionality.

---

## Phase 19 — Failure Drills

### A. Wrong Image

```text
ImagePullBackOff
```

Diagnose with:

```text
get
describe
events
```

### B. Application Crash

```text
CrashLoopBackOff
```

Diagnose with logs.

### C. Wrong Service Selector

```text
Service
   │
   X
Pods
```

Inspect labels/selectors.

### D. Wrong Port

Trace:

```text
Service port
targetPort
container/application port
```

### E. Bad Secret/Config

Inspect:

```text
ConfigMap
Secret reference
environment
application logs
```

### F. Readiness Failure

Observe that the Pod may be running but not ready for Service traffic.

### G. Liveness Failure

Observe restart behavior.

### H. PVC Failure

Inspect:

```text
PVC
PV
events
mount
```

### I. Resource Failure

Use an intentionally inappropriate memory configuration in a safe local exercise and understand OOM/resource symptoms.

---

## Phase 20 — Cleanup

Delete capstone resources.

```text
kubectl delete namespace guestbook
```

where appropriate for the lab.

Verify resources are gone.

Then delete the disposable kind cluster.

```text
Application Resources
        │
        ▼
Namespace Cleanup
        │
        ▼
Cluster Cleanup
```

---

# Interview Readiness

Be able to answer:

- [ ] What is Kubernetes?
- [ ] Why use Kubernetes if Docker already exists?
- [ ] What is container orchestration?
- [ ] What is desired state?
- [ ] What is reconciliation?
- [ ] What is a cluster?
- [ ] What is the control plane?
- [ ] What is a node?
- [ ] What is kind?
- [ ] What is `kubectl`?
- [ ] What is a Kubernetes context?
- [ ] What is a manifest?
- [ ] What do `apiVersion`, `kind`, `metadata`, and `spec` mean?
- [ ] What is a Pod?
- [ ] Why are Pods considered disposable?
- [ ] What is a label?
- [ ] What is a selector?
- [ ] What is a Deployment?
- [ ] What is a ReplicaSet?
- [ ] Why usually manage a Deployment instead of ReplicaSet directly?
- [ ] How does Kubernetes replace a failed Pod?
- [ ] What is a Service?
- [ ] Why shouldn't applications depend on Pod IPs?
- [ ] What is ClusterIP?
- [ ] What is NodePort?
- [ ] What is LoadBalancer?
- [ ] What is `port` vs `targetPort`?
- [ ] What is a namespace?
- [ ] What is a ConfigMap?
- [ ] What is a Secret?
- [ ] Why isn't a Kubernetes Secret automatically a secure vault?
- [ ] What is a volume?
- [ ] What is a PersistentVolume?
- [ ] What is a PersistentVolumeClaim?
- [ ] What is a StorageClass?
- [ ] How could Kubernetes storage eventually map to AWS EBS?
- [ ] What is a liveness probe?
- [ ] What is a readiness probe?
- [ ] What is a startup probe?
- [ ] How can Spring Boot Actuator help Kubernetes?
- [ ] What are replicas?
- [ ] What are resource requests?
- [ ] What are resource limits?
- [ ] What is a rolling update?
- [ ] How do you roll back a Deployment?
- [ ] What is a Job?
- [ ] What is a CronJob?
- [ ] What is a StatefulSet?
- [ ] What is a DaemonSet?
- [ ] What is Ingress?
- [ ] Why does Ingress need a controller?
- [ ] How does Kubernetes service discovery work?
- [ ] How do you view logs?
- [ ] What does `kubectl describe` help diagnose?
- [ ] What does `kubectl exec` do?
- [ ] What is CrashLoopBackOff?
- [ ] What is ImagePullBackOff?
- [ ] What does OOMKilled suggest?
- [ ] How do you troubleshoot a Service that cannot reach its Pods?
- [ ] What is horizontal pod autoscaling?
- [ ] What are ServiceAccounts and RBAC?
- [ ] What is NetworkPolicy?
- [ ] What is Helm?
- [ ] Why learn raw Kubernetes manifests before Helm?
- [ ] How does CI/CD relate to Kubernetes?
- [ ] What is Amazon EKS?
- [ ] Why should Kubernetes be learned before EKS?

---

# Common Anti-Patterns

## Anti-Pattern 1 — "Kubernetes Replaces Docker"

Kubernetes orchestrates containerized workloads. Docker/container knowledge remains foundational.

## Anti-Pattern 2 — Managing Production Workloads as Standalone Pods

Use controllers such as Deployments where appropriate.

## Anti-Pattern 3 — Depending on Pod IPs

Use Services for stable application networking.

## Anti-Pattern 4 — Treating Pod as a Permanent Server

Pods are replaceable.

## Anti-Pattern 5 — Storing Important Data Only in Container Filesystem

Use appropriate persistent storage.

## Anti-Pattern 6 — Committing Real Secrets in YAML

`kind: Secret` does not make committed credentials safe.

## Anti-Pattern 7 — Liveness and Readiness Are the Same

They answer different questions.

## Anti-Pattern 8 — Aggressive Liveness Probe

Bad probes can repeatedly kill healthy-but-slow applications.

## Anti-Pattern 9 — No Resource Requests or Thought About Limits

Resource behavior matters to scheduling and reliability.

## Anti-Pattern 10 — Using `latest` Everywhere

Use traceable image versions.

## Anti-Pattern 11 — Editing Running Containers as the Fix

Fix source/image/configuration and redeploy reproducibly.

## Anti-Pattern 12 — Changing Random Ports Until Networking Works

Trace Service → targetPort → application port systematically.

## Anti-Pattern 13 — Using Helm Before Understanding Kubernetes Objects

Learn the resources Helm generates/manages first.

## Anti-Pattern 14 — Assuming Kubernetes Means Automatic Security

Kubernetes introduces its own security responsibilities.

## Anti-Pattern 15 — Assuming Kubernetes Means Automatic High Availability

Application architecture, cluster architecture, storage, networking, and failure domains still matter.

## Anti-Pattern 16 — Running PostgreSQL in Kubernetes Because "Everything Must Be in Kubernetes"

Choose database architecture deliberately.

## Anti-Pattern 17 — Jumping Directly to EKS

Learn Kubernetes first; then learn AWS's implementation/integration.

## Anti-Pattern 18 — Treating `Running` as `Ready`

A running Pod may not be ready to receive traffic.

## Anti-Pattern 19 — Treating Restart as Root-Cause Fix

Use logs/events/probes to understand why the workload failed.

## Anti-Pattern 20 — Learning Commands Without the Object Model

The goal is to understand:

```text
desired state
objects
controllers
selectors
networking
storage
health
```

not merely memorize `kubectl`.

---

# Relationship to Existing Skill Trees

```text
Linux / Ubuntu
      │
      ▼
    Docker
      │
      ▼
Deployment Fundamentals
      │
      ▼
  Kubernetes
```

Full-stack path:

```text
Java ───────────────► Spring Boot
                           │
                           │
TypeScript ──► Angular/React
                           │
                           ▼
                         Docker
                           │
                           ▼
                      Kubernetes
```

Database path:

```text
PostgreSQL
    │
    ▼
Dockerized PostgreSQL
    │
    ▼
Kubernetes Storage Exercise
    │
    ▼
PV / PVC
```

CI/CD path:

```text
CI/CD Fundamentals
        │
  ┌─────┼───────────┐
  ▼     ▼           ▼
Jenkins GitHub    GitLab
        Actions    CI/CD
  │     │           │
  └─────┼───────────┘
        ▼
      Docker
        │
        ▼
    Kubernetes
```

AWS path:

```text
AWS Fundamentals
EC2 + S3 + EBS
        │
        │          Kubernetes
        │              │
        └──────┬───────┘
               ▼
          AWS Kubernetes
               │
               ▼
          Amazon EKS
```

---

# Future Kubernetes Branches

```text
                         Kubernetes
                             │
          ┌──────────────────┼──────────────────┐
          ▼                  ▼                  ▼
        Helm          Kubernetes Security   Observability
          │                  │                  │
          ▼                  ▼                  ▼
   Package/Release       RBAC / Policy      Logs / Metrics
     Management            Secrets             Tracing
                             │
                             ▼
                         DevSecOps
```

Cloud branch:

```text
Kubernetes
    │
    ▼
AWS Fundamentals
    │
    ▼
AWS Networking + IAM
    │
    ▼
Amazon EKS
```

Potential later trees:

- [ ] Helm
- [ ] Kubernetes Security
- [ ] Advanced Kubernetes Networking
- [ ] Kubernetes Storage / CSI
- [ ] Kubernetes Observability
- [ ] Amazon EKS
- [ ] GitOps
- [ ] Argo CD
- [ ] Service Mesh
- [ ] Advanced Kubernetes Operations
- [ ] Kubernetes Administration
- [ ] Kubernetes Autoscaling
- [ ] Kubernetes Production Architecture

---

# Mastery Progression

```text
"I understand Docker containers"
        │
        ▼
"I understand why orchestration exists"
        │
        ▼
"I understand desired state"
        │
        ▼
"I can create a local cluster"
        │
        ▼
"I can use kubectl"
        │
        ▼
"I can write Kubernetes YAML"
        │
        ▼
"I understand Pods"
        │
        ▼
"I can manage apps with Deployments"
        │
        ▼
"I understand self-healing"
        │
        ▼
"I can connect workloads with Services"
        │
        ▼
"I can separate configuration and secrets"
        │
        ▼
"I can attach persistent storage"
        │
        ▼
"I understand probes and readiness"
        │
        ▼
"I can scale replicas"
        │
        ▼
"I understand requests and limits"
        │
        ▼
"I can perform rolling updates"
        │
        ▼
"I can roll back failures"
        │
        ▼
"I can expose HTTP routes"
        │
        ▼
"I can troubleshoot using
get / describe / logs / exec"
        │
        ▼
"I can deploy my full-stack application"
        │
        ▼
"I understand Kubernetes security boundaries"
        │
        ▼
"I know what Helm and EKS add later"
```

---

# Mastery Standard

> **Can I independently create a local Kubernetes cluster, use `kubectl`, write and understand Kubernetes manifests, deploy containerized frontend/backend workloads with Deployments, connect them using Services and label selectors, configure applications using ConfigMaps and Secrets, attach persistent storage through PV/PVC concepts, configure meaningful startup/readiness/liveness probes, set basic resource requests and limits, scale replicas, observe self-healing, perform rolling updates and rollbacks, expose HTTP traffic through basic Ingress concepts, troubleshoot failures using object status/events/logs/exec, and deploy the familiar Spring Boot + Angular/React + PostgreSQL application—while understanding which topics belong to later Helm, security, administration, cloud, and EKS skill trees?**

Final mental model:

```text
                         Developer
                            │
                         kubectl
                            │
                            ▼
                     Kubernetes API
                            │
                            ▼
                      Desired State
                            │
                ┌───────────┼───────────┐
                ▼           ▼           ▼
           Deployments   Services   Configuration
                │           │        ConfigMap
                │           │        Secret
                ▼           │
              Pods ◄────────┘
                │
        ┌───────┼────────┐
        ▼       ▼        ▼
      Health  Resources Storage
      Probes   CPU/RAM   PVC
                         │
                         ▼
                         PV

Kubernetes continuously compares:

          Desired State
                │
                ▼
             Actual
                │
        mismatch?
                │
                ▼
            Reconcile


Application example:

                    User
                     │
                     ▼
                   Ingress
                     │
           ┌─────────┴─────────┐
           ▼                   ▼
     Frontend Service     Backend Service
           │                   │
           ▼                   ▼
    Frontend Pods          Backend Pods
                               │
                               ▼
                       PostgreSQL Service
                               │
                               ▼
                        PostgreSQL Pod
                               │
                               ▼
                              PVC
                               │
                               ▼
                              PV


Progression:

Docker
  │
  ▼
Kubernetes
  │
  ├────────► Helm
  │
  ├────────► Kubernetes Security
  │
  └────────► Cloud Kubernetes
                 │
                 ▼
              Amazon EKS
