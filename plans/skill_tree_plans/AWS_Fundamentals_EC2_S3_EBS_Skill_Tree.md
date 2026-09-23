# AWS Fundamentals — EC2, S3, and EBS Skill Tree

> **Goal:** Build an introductory, practical understanding of Amazon Web Services (AWS) by learning three foundational services: **Amazon EC2**, **Amazon S3**, and **Amazon EBS**.
>
> **Target level:** A full-stack developer who is new to AWS and needs enough cloud knowledge to understand, create, use, troubleshoot, and clean up a simple AWS deployment without attempting to master the entire AWS platform.
>
> **Primary prerequisite:** **Deployment Fundamentals**
>
> **Practical supporting prerequisites:** **Docker**, basic Linux/Ubuntu command-line skills, Git, HTTP/HTTPS fundamentals, and basic full-stack application knowledge.
>
> **Security companion:** This tree introduces the security concepts necessary to use these services responsibly, including IAM awareness, least privilege, security groups, SSH keys, private S3 access, credential safety, and the AWS shared responsibility model. Deeper AWS IAM and Cloud Security should become separate later skill trees.
>
> **Scope boundary:** This is intentionally **not an AWS mastery tree**. AWS contains a very large number of services. The learner should leave this tree understanding the AWS platform model and being comfortable with EC2, S3, and EBS—not believing they have learned all of AWS.
>
> **Capstone:** Deploy a simple Dockerized Spring Boot + Angular/React application to an EC2 instance, interact with S3 for object storage, use EBS for persistent block storage, verify the deployment, inspect the resources, and safely clean them up.
>
> **Cost warning:** AWS resources can incur real charges. Cost awareness and cleanup are required skills in this tree. Always inspect current AWS pricing and Free Tier/account eligibility before creating resources.

---

# Skill Tree Overview

```text
                    Deployment Fundamentals
                              │
                              ▼
                       AWS Fundamentals
                              │
          ┌───────────────────┼───────────────────┐
          │                   │                   │
          ▼                   ▼                   ▼
        EC2                  S3                  EBS
      Compute          Object Storage       Block Storage
          │                   │                   │
          └───────────────────┼───────────────────┘
                              ▼
                    Simple AWS Deployment
                              │
                              ▼
                     Observe & Troubleshoot
                              │
                              ▼
                        Clean Up Resources
                              │
                              ▼
                        Understand Cost
```

The central mental model:

```text
AWS
│
├── EC2
│   └── "Where can my application run?"
│
├── S3
│   └── "Where can I store objects/files?"
│
└── EBS
    └── "What persistent disk storage can an EC2 machine use?"
```

---

# Tier 0 — What AWS Is

## 1. Cloud Computing

At an introductory level, understand cloud computing as using computing resources provided through a remote infrastructure platform rather than owning and operating all physical infrastructure yourself.

Examples:

```text
Compute
Storage
Networking
Databases
Monitoring
Security services
```

## 2. AWS

Amazon Web Services provides many cloud services.

This tree deliberately studies only enough AWS-wide concepts to understand:

```text
EC2
S3
EBS
```

## 3. AWS Is Larger Than These Services

Do not develop this mental model:

```text
AWS = EC2 + S3 + EBS
```

Instead:

```text
                         AWS
                          │
          ┌───────────────┼───────────────┐
          ▼               ▼               ▼
        Compute          Storage        Networking
          │               │
         EC2          ┌────┴────┐
                      ▼         ▼
                     S3        EBS

          ...plus databases, containers,
          serverless, security, analytics,
          messaging, AI, and many more.
```

## 4. Managed Infrastructure

AWS owns and operates underlying physical infrastructure.

You configure and consume cloud resources.

The exact responsibility split depends on the service.

---

# Tier 1 — AWS Account and Console

## 5. AWS Account

An AWS account is the administrative and billing boundary in which resources are created.

## 6. AWS Management Console

The browser-based AWS Management Console provides access to AWS services.

Become comfortable with:

- [ ] Service search
- [ ] Region selector
- [ ] Resource pages
- [ ] Creating resources
- [ ] Inspecting resource details
- [ ] Deleting/terminating resources
- [ ] Billing/cost pages at a basic level

## 7. Search Instead of Memorizing Navigation

AWS console layouts evolve.

Learn:

```text
service name
→ search
→ service page
```

rather than memorizing every menu position.

## 8. Root User Awareness

The AWS account root user has extremely powerful account-level access.

Do not use root credentials as ordinary day-to-day application credentials.

Deeper account hardening belongs in a future IAM/Security tree.

---

# Tier 2 — Regions

## 9. Region

AWS infrastructure is divided geographically into Regions.

Concept:

```text
AWS
├── Region A
├── Region B
└── Region C
```

## 10. Resources Are Often Regional

Many AWS resources exist in a selected Region.

Before creating something, ask:

> **Which Region am I currently using?**

## 11. Why Region Matters

Region can affect:

- [ ] Latency
- [ ] Service availability
- [ ] Cost
- [ ] Legal/data requirements
- [ ] Which resources can interact directly
- [ ] Where you see the resource in the console

## 12. Common Beginner Mistake

```text
"I created an EC2 instance,
but now I can't find it."
```

First check whether the console is viewing the same Region.

---

# Tier 3 — Availability Zones

## 13. Availability Zone

A Region contains multiple Availability Zones (AZs).

```text
Region
├── AZ A
├── AZ B
└── AZ C
```

## 14. Why AZs Exist

Availability Zones help AWS customers design systems that can tolerate infrastructure failures.

## 15. Introductory Scope

For this tree, understand:

```text
Region
  │
  └── Availability Zones
```

and that some resources—especially EBS volumes—have AZ-specific relationships.

Do not design advanced multi-AZ architectures yet.

---

# Tier 4 — AWS Resource Identity

## 16. Resource

An AWS resource is something you create/manage in AWS.

Examples:

```text
EC2 instance
EBS volume
S3 bucket
security group
```

## 17. Resource IDs

AWS resources often receive identifiers.

Examples conceptually:

```text
instance ID
volume ID
snapshot ID
```

## 18. Names and Tags

Tags are key/value metadata attached to many AWS resources.

Example:

```text
Name = guestbook-practice
Environment = learning
Project = skill-tree
```

## 19. Why Tag

Tags help answer:

```text
What is this?
Who/what created it?
Which project owns it?
Can I safely delete it?
```

---

# Tier 5 — AWS Billing and Cost Awareness

## 20. Cloud Resources Can Cost Money

Creating infrastructure is not the same as installing software locally.

```text
create resource
      │
      ▼
resource exists in AWS
      │
      ▼
possible billing
```

## 21. Cost Drivers

Introductory examples:

```text
EC2 runtime
EBS storage
S3 storage
requests
data transfer
snapshots
public IPv4 usage where applicable
```

Exact prices change.

Use current AWS pricing rather than memorizing dollar amounts.

## 22. Free Tier Awareness

Some accounts/services/usages may qualify for Free Tier benefits.

Never assume:

```text
"Free Tier means anything I create is free."
```

## 23. Cleanup Is a Technical Skill

Every practical AWS exercise should end with:

```text
inspect resources
      │
      ▼
identify what is still needed
      │
      ▼
stop/delete/terminate unused resources
      │
      ▼
verify cleanup
```

---

# Tier 6 — Shared Responsibility Model

## 24. Responsibility Is Shared

AWS secures the underlying cloud infrastructure.

You remain responsible for many things you configure or run.

Conceptually:

```text
AWS
└── security OF the cloud

Customer
└── security IN the cloud
```

The exact boundary varies by service.

## 25. EC2 Example

With EC2, the customer is responsible for significant operating-system/application configuration.

Examples:

- [ ] OS updates
- [ ] Application security
- [ ] Security group decisions
- [ ] Credentials
- [ ] Data
- [ ] Software installed on the instance

## 26. S3 Example

AWS operates the storage service, but you are responsible for decisions such as:

```text
who can access the bucket/object?
should it be public?
what data belongs there?
```

---

# Tier 7 — IAM Awareness

## 27. IAM

AWS Identity and Access Management (IAM) controls identities and permissions.

At this stage, understand:

```text
Who?
  │
  ▼
Identity
  │
  ▼
What are they allowed to do?
  │
  ▼
Permissions
```

## 28. User

An IAM user represents a persistent AWS identity.

## 29. Role

An IAM role is an identity that can be assumed by an authorized principal/service.

Roles are particularly important for applications running on AWS.

## 30. Policy

A policy describes permissions.

Conceptually:

```text
allow/deny
     │
     ▼
specific actions
     │
     ▼
specific resources
```

## 31. Least Privilege

Grant only the permissions necessary.

Bad mental model:

```text
"It didn't work,
so give AdministratorAccess."
```

Better:

```text
"What exact permission does this operation need?"
```

## 32. Future Tree

Advanced IAM deserves its own skill tree.

Do not attempt to master:

```text
complex policy evaluation
cross-account access
organizations
permission boundaries
identity federation
advanced IAM architecture
```

here.

---

# Tier 8 — AWS Credentials

## 33. Credential Safety

AWS credentials can grant access to cloud resources.

Never:

- [ ] Commit access keys to Git
- [ ] Put credentials in frontend JavaScript
- [ ] Hardcode credentials in source code
- [ ] Bake credentials into Docker images
- [ ] Paste secrets into public logs

## 34. Environment / Credential Mechanisms

Applications/tools can receive credentials through supported AWS authentication mechanisms.

Do not invent your own credential storage system.

## 35. Prefer Roles on AWS

When software running on EC2 needs AWS access, an IAM role attached appropriately to the instance is generally preferable to copying long-lived access keys onto the server.

Mental model:

```text
EC2 Instance
     │
     ▼
IAM Role
     │
     ▼
Allowed AWS Actions
```

---

# Tier 9 — AWS CLI

## 36. CLI Purpose

The AWS Command Line Interface allows AWS operations from a terminal.

```text
Console
= browser interface

AWS CLI
= terminal interface
```

## 37. Why Learn It

The CLI helps bridge:

```text
manual learning
      │
      ▼
repeatable commands
      │
      ▼
future automation
```

## 38. Introductory Scope

Learn to:

- [ ] Install/verify the CLI
- [ ] Understand profiles/configuration conceptually
- [ ] Identify current identity
- [ ] Identify/configure Region
- [ ] List basic resources
- [ ] Perform simple S3 operations
- [ ] Inspect EC2/EBS resources

Do not turn this tree into AWS CLI command memorization.

## 39. Identity Check

Understand the value of checking:

```text
"Which AWS identity am I currently using?"
```

before making changes.

---

# Tier 10 — Basic AWS Networking Mental Model

## 40. Network Boundary

EC2 instances exist within AWS networking infrastructure.

For introductory purposes:

```text
AWS Region
   │
   ▼
VPC
   │
   ▼
Subnet
   │
   ▼
EC2 Instance
```

## 41. VPC

A Virtual Private Cloud is a logically isolated network in AWS.

For this tree, use/understand the basic/default networking environment rather than designing complex VPC architecture.

## 42. Subnet

A subnet represents a portion of a VPC's address space associated with an Availability Zone.

## 43. Future Networking Tree

Leave these for later:

```text
custom VPC design
route tables in depth
NAT gateways
VPC endpoints
peering
Transit Gateway
advanced subnet architecture
```

---

# Tier 11 — IP Addresses

## 44. Private IP

An EC2 instance typically has a private address used within its network context.

## 45. Public IP

An instance may have a public address used for internet communication depending on configuration.

## 46. Public Address Stability Awareness

Do not assume every automatically assigned public address will remain the same forever across lifecycle changes.

## 47. Elastic IP Awareness

AWS provides mechanisms for persistent public addressing, but detailed public-IP architecture is outside this introductory tree.

---

# Tier 12 — Security Groups

## 48. Security Group

A security group acts as a stateful virtual firewall around supported resources such as EC2 network interfaces.

Mental model:

```text
Internet
   │
   ▼
Security Group
   │
   ├── allowed traffic
   │
   └── blocked traffic
   │
   ▼
EC2
```

## 49. Inbound Rules

Control traffic entering the resource.

Examples:

```text
SSH
HTTP
HTTPS
application port
```

## 50. Outbound Rules

Control traffic leaving the resource.

## 51. Source Matters

Avoid:

```text
allow every source
```

unless the service genuinely must be publicly reachable.

## 52. SSH Safety

For learning, restrict SSH access to your own current IP where practical rather than exposing SSH to the entire internet.

---

# Tier 13 — EC2 Introduction

## 53. EC2

Amazon Elastic Compute Cloud provides virtual compute instances.

Simple mental model:

```text
EC2 instance
≈
a virtual server/computer in AWS
```

## 54. What You Can Run

Examples:

```text
Spring Boot
Docker
web server
background process
development/testing service
```

## 55. EC2 Responsibility

Because EC2 gives substantial control over the machine, you also inherit substantial responsibility for its software configuration.

---

# Tier 14 — EC2 Instance Components

## 56. Instance Mental Model

```text
EC2 Instance
├── AMI
├── Instance Type
├── Network
├── Security Group
├── Storage
├── Key Pair / access method
└── IAM Role (optional/appropriate)
```

## 57. Instance ID

Each instance receives a unique AWS identifier.

## 58. Instance State

Recognize:

```text
pending
running
stopping
stopped
shutting-down
terminated
```

---

# Tier 15 — AMIs

## 59. AMI

An Amazon Machine Image provides the template used to launch an EC2 instance.

Concept:

```text
AMI
 │
 ▼
EC2 Instance
```

## 60. AMI Contents

An AMI can define a starting operating-system/software environment.

Examples:

```text
Amazon Linux
Ubuntu
Windows Server
```

## 61. Practice Choice

For a learner already comfortable with Ubuntu/WSL2, an Ubuntu-based EC2 instance can reduce unrelated operating-system learning.

## 62. AMI Scope

Custom AMI pipelines and advanced image management belong later.

---

# Tier 16 — Instance Types

## 63. Instance Type

Instance type determines the compute-resource profile.

Conceptually:

```text
CPU
memory
network characteristics
other capabilities
```

## 64. Families/Sizes Awareness

AWS offers many instance families and sizes.

Do not memorize them.

## 65. Selection Skill

For an introductory application, learn to ask:

```text
How much compute does this workload need?
What does this instance cost?
Is this eligible for any account benefit/free usage?
```

---

# Tier 17 — Launching EC2

## 66. Console Launch Flow

Understand the major choices:

```text
Name
  │
  ▼
AMI
  │
  ▼
Instance Type
  │
  ▼
Key Pair / access
  │
  ▼
Network
  │
  ▼
Security Group
  │
  ▼
Storage
  │
  ▼
Launch
```

## 67. Review Before Launch

Before clicking launch, identify:

- [ ] Region
- [ ] Instance type
- [ ] Security rules
- [ ] Storage
- [ ] Key/access configuration
- [ ] Expected cost implications

---

# Tier 18 — SSH Key Pairs

## 68. Public-Key Authentication

SSH commonly uses a public/private key pair.

Concept:

```text
Private Key
stays with you

Public Key
associated with server access
```

## 69. Private Key Safety

Do not:

```text
commit it
share it
put it in Docker image
paste it publicly
```

## 70. File Permissions

SSH clients may require private key files to have restrictive filesystem permissions.

## 71. Connect

Mental model:

```text
Your Computer
     │
     │ SSH
     ▼
Security Group
     │
     ▼
EC2 Instance
```

---

# Tier 19 — Connecting to EC2

## 72. Connection Requirements

For SSH access you generally need the correct:

```text
instance
public address/DNS
username
private key
security group rule
network reachability
```

## 73. First Commands

Once connected:

```text
whoami
pwd
ls
uname
```

Use familiar Linux fundamentals to orient yourself.

## 74. EC2 Is a Remote Machine

This is a critical mental shift:

```text
WSL2 Ubuntu
= local Linux environment

EC2 Ubuntu
= remote Linux machine running in AWS
```

The Linux skills transfer.

---

# Tier 20 — EC2 Software Setup

## 75. Update Packages

Understand the need to maintain the operating system and installed software.

## 76. Install Runtime Requirements

Depending on the deployment:

```text
Docker
Java
web server
other runtime tools
```

## 77. Docker Path

For this curriculum, prefer connecting EC2 to the existing Docker tree:

```text
EC2
 │
 ▼
Install/Configure Docker
 │
 ▼
Run Existing Image
```

rather than manually reconstructing the application's runtime stack on the server.

---

# Tier 21 — EC2 Lifecycle

## 78. Start

A stopped instance can generally be started.

## 79. Stop

Stopping preserves the instance and its appropriate persistent storage but stops compute execution.

Billing behavior differs by resource.

## 80. Reboot

Reboots the operating system/instance without the same lifecycle implications as termination.

## 81. Terminate

Termination destroys the EC2 instance.

Some attached storage may also be deleted depending on configuration.

## 82. Stop Is Not Delete

```text
Stopped EC2
≠
all costs/resources removed
```

Attached storage and other resources may still incur charges.

---

# Tier 22 — EC2 Monitoring Basics

## 83. Instance Status

Inspect:

```text
instance state
status checks
basic monitoring metrics
```

## 84. CloudWatch Awareness

AWS monitoring commonly integrates with Amazon CloudWatch.

For this introductory tree, recognize CloudWatch and inspect basic EC2 metrics.

A deeper observability/CloudWatch tree can come later.

## 85. Application Health

AWS saying the VM is running does not prove your application works.

Verify:

```text
process/container running
port listening
health endpoint works
frontend/API responds
```

---

# Tier 23 — S3 Introduction

## 86. S3

Amazon Simple Storage Service is an object storage service.

Mental model:

```text
S3
 │
 ▼
Bucket
 │
 ▼
Objects
```

## 87. Object Storage

S3 is not simply a normal mounted disk.

It stores objects accessed through S3 APIs/HTTP-based mechanisms and tools.

## 88. Good Uses

Examples:

```text
images
documents
backups
build outputs
logs/exports
static assets
```

## 89. Bad Mental Model

Do not treat S3 as though it were EBS.

```text
S3
≠
virtual hard drive attached to EC2
```

---

# Tier 24 — S3 Buckets

## 90. Bucket

A bucket is a top-level container for S3 objects.

```text
Bucket
├── object A
├── object B
└── object C
```

## 91. Bucket Naming

Bucket names must follow AWS S3 naming requirements and exist within S3's naming model.

Use meaningful project-oriented names while respecting uniqueness requirements.

## 92. Region

Buckets are created in a Region, although S3's naming/access model differs from EC2/EBS.

---

# Tier 25 — S3 Objects and Keys

## 93. Object

An S3 object consists conceptually of:

```text
data
+
key
+
metadata
```

## 94. Key

The key identifies the object within the bucket.

Example:

```text
images/profile.png
```

## 95. "Folders"

S3 consoles can display folder-like organization based on key prefixes.

Mental model:

```text
S3 is object storage
not a traditional directory filesystem
```

---

# Tier 26 — Basic S3 Operations

## 96. Upload

Practice uploading an object through the console.

## 97. Download

Download the object again.

## 98. Delete

Delete objects you no longer need.

## 99. AWS CLI

Repeat basic operations from the terminal.

Conceptually:

```text
local file
    │
    ▼
AWS CLI
    │
    ▼
S3 bucket
```

## 100. List

Be able to inspect bucket/object contents through console and CLI.

---

# Tier 27 — S3 Permissions

## 101. Private by Default Mental Model

Treat application data as private unless there is a deliberate reason to expose it.

## 102. Public Access

Do not make a bucket public simply because an application cannot access it.

Ask:

```text
Who needs access?
Why?
Through what identity?
```

## 103. IAM Role Integration

An EC2 application can use an attached IAM role with appropriate S3 permissions.

```text
EC2
 │
 ▼
IAM Role
 │
 ▼
S3 Permission
 │
 ▼
Bucket/Object
```

## 104. Least Privilege

If an application only needs one bucket/prefix and a few operations, do not grant unrestricted S3 administration.

---

# Tier 28 — S3 Versioning Awareness

## 105. Versioning

S3 can retain multiple versions of objects when versioning is enabled.

Concept:

```text
file.txt
├── version 1
├── version 2
└── version 3
```

## 106. Benefit

Can help recover from overwrites/deletions in appropriate configurations.

## 107. Cost

Old versions consume storage.

Versioning is not "free backup forever."

---

# Tier 29 — S3 Lifecycle Awareness

## 108. Lifecycle

S3 lifecycle rules can automate storage-management actions over time.

Conceptually:

```text
new object
   │
   ▼
age
   │
   ▼
transition / expire
```

## 109. Scope

Know why lifecycle policies exist.

Do not master every S3 storage class/lifecycle configuration yet.

---

# Tier 30 — S3 Storage Classes Awareness

## 110. Different Access Patterns

AWS provides S3 storage classes optimized for different access/retrieval patterns.

## 111. Introductory Skill

Understand:

```text
frequent access
vs
infrequent/archive-style access
```

can have different pricing/performance tradeoffs.

## 112. Do Not Memorize Pricing Tables

Use current AWS documentation/pricing when making real decisions.

---

# Tier 31 — S3 Static Content Awareness

## 113. Static Files

S3 can store static frontend assets/files.

## 114. Static Website Hosting Awareness

S3 has static website hosting capabilities, but production frontend architecture may involve other AWS services such as CloudFront.

## 115. Scope Boundary

Do not turn this introductory tree into:

```text
S3 + CloudFront + Route 53
production frontend architecture
```

Those can come later.

---

# Tier 32 — EBS Introduction

## 116. EBS

Amazon Elastic Block Store provides block storage volumes commonly used with EC2.

Mental model:

```text
EC2 Instance
     │
     ▼
EBS Volume
     │
     ▼
Block Storage
```

## 117. Disk Analogy

At an introductory level:

```text
EBS volume
≈
virtual disk used by an EC2 instance
```

This analogy is useful but not a complete description of EBS architecture.

## 118. EBS vs S3

```text
EBS
= block storage

S3
= object storage
```

---

# Tier 33 — Root EBS Volume

## 119. Root Volume

Many EC2 instances use an EBS volume for their root filesystem.

Concept:

```text
EC2
 │
 ▼
Root EBS
 │
 ▼
Operating System Files
```

## 120. Launch Configuration

When launching EC2, inspect the configured root storage.

## 121. Delete on Termination

Understand that EBS volume deletion behavior can be configured and may differ between root/additional volumes.

Never assume termination automatically cleans every volume.

---

# Tier 34 — Additional EBS Volume

## 122. Create Volume

Practice creating a separate EBS volume.

Important properties include:

```text
Availability Zone
size
volume type
```

## 123. Availability Zone Relationship

An EBS volume must be available in the appropriate AZ to attach to an EC2 instance.

Mental model:

```text
EC2 in AZ-A
    │
    ▼
EBS in AZ-A
```

## 124. Attach

Attach the volume to the EC2 instance.

AWS attachment does not automatically mean the Linux filesystem is ready for use.

---

# Tier 35 — Linux Disk Discovery

## 125. Inspect Devices

After attachment, use Linux tools to identify block devices.

Understand the difference between:

```text
AWS says volume attached
```

and:

```text
Linux has a usable mounted filesystem
```

## 126. Device Awareness

Device naming can vary depending on instance/storage configuration.

Inspect rather than blindly assuming a device path.

---

# Tier 36 — Formatting EBS

## 127. New Volume

A new block volume may need a filesystem before normal file storage.

Concept:

```text
Raw EBS Volume
      │
      ▼
Create Filesystem
      │
      ▼
Mount
      │
      ▼
Store Files
```

## 128. Destructive Operation Warning

Formatting a volume can destroy existing data.

Always verify the device before creating a filesystem.

This is a critical operational habit.

---

# Tier 37 — Mounting EBS

## 129. Mount Point

Create a directory to serve as the mount point.

Concept:

```text
/dev/... volume
      │
      ▼
/mnt/data
```

## 130. Mount

Mount the filesystem and verify it.

## 131. Verify

Check:

```text
mounted?
expected size?
can write?
can read?
```

## 132. Reboot Persistence Awareness

A manual mount may not automatically persist across reboot.

Learn the purpose of persistent mount configuration such as `/etc/fstab` at a basic Linux level.

Be careful: incorrect mount configuration can create boot problems.

---

# Tier 38 — EBS Persistence

## 133. Why Separate Storage

Application/server lifecycle and data lifecycle are not necessarily identical.

Concept:

```text
Compute
can be replaced

Data
may need to persist
```

## 134. Stop/Start

EBS-backed storage generally persists through ordinary EC2 stop/start lifecycle.

## 135. Termination

Persistence after EC2 termination depends on the EBS volume's deletion configuration.

Inspect it.

---

# Tier 39 — EBS Volume Types Awareness

## 136. Multiple Volume Types

EBS provides different volume types for different workload/performance requirements.

## 137. Introductory Goal

Understand the broad distinction between:

```text
general-purpose storage
higher-performance/specialized storage
throughput-oriented storage
```

Do not memorize every performance limit.

## 138. Cost/Performance Tradeoff

More performance can cost more.

Choose based on workload rather than "largest/faster is better."

---

# Tier 40 — EBS Snapshots

## 139. Snapshot

An EBS snapshot is a point-in-time backup mechanism for an EBS volume.

Mental model:

```text
EBS Volume
    │
    ▼
 Snapshot
    │
    ▼
New/Restored Volume
```

## 140. Practice

At an introductory level:

- [ ] Create a snapshot
- [ ] Identify it
- [ ] Understand restoration concept
- [ ] Delete unneeded practice snapshots

## 141. Snapshot Is Not the Running Volume

A snapshot is backup data used to create/restore volumes, not a mounted working disk itself.

---

# Tier 41 — EBS Resize Awareness

## 142. Volume Modification

EBS volumes can be modified in supported ways, including increasing capacity.

## 143. AWS Size vs Filesystem Size

Increasing the AWS volume does not always mean the operating system/filesystem automatically uses all new capacity.

Mental model:

```text
Increase EBS volume
       │
       ▼
OS sees larger device
       │
       ▼
partition/filesystem may need expansion
```

## 144. Scope

Perform one guided expansion exercise if desired, but advanced storage administration is outside this tree.

---

# Tier 42 — Comparing EC2, S3, and EBS

## 145. Core Comparison

```text
EC2
"What computes/runs?"

S3
"Where are objects stored?"

EBS
"What block disk does the server use?"
```

## 146. Example

```text
                    User
                     │
                     ▼
                EC2 Instance
                 Application
                  │      │
                  │      └──────────────┐
                  ▼                     ▼
             EBS Volume               S3
           local/persistent       object storage
           block storage
```

## 147. Choose the Correct Tool

If you need:

```text
run Java process
→ EC2

store uploaded image/object
→ S3

attach filesystem-like block disk to server
→ EBS
```

---

# Tier 43 — Docker on EC2

## 148. Prerequisite

The Docker tree should already teach:

```text
Dockerfile
image
container
ports
volumes
networks
registry
Compose
```

## 149. EC2 as Docker Host

```text
EC2
 │
 ▼
Docker Engine
 │
 ▼
Application Containers
```

## 150. Pull Image

A deployed server can pull an image from an appropriate registry.

## 151. Run

Expose only required application ports and configure environment variables/secrets appropriately.

## 152. AWS vs Docker Responsibilities

```text
AWS/EC2
→ machine/network/cloud resources

Docker
→ application container/runtime packaging
```

---

# Tier 44 — Simple Application Deployment

## 153. Deployment Flow

```text
Build Application
      │
      ▼
Build Docker Image
      │
      ▼
Publish Image
      │
      ▼
Launch EC2
      │
      ▼
Install Docker
      │
      ▼
Pull Image
      │
      ▼
Run Container
```

## 154. Security Group

Open only the application traffic required for the exercise.

## 155. Verify

From your computer:

```text
browser / curl
      │
      ▼
EC2 public endpoint
      │
      ▼
application
```

---

# Tier 45 — EC2 to S3

## 156. Application Storage

A backend application may need object storage.

Concept:

```text
Spring Boot
    │
    ▼
AWS SDK / S3 API
    │
    ▼
S3 Bucket
```

## 157. IAM Role

Prefer:

```text
EC2
 │
 ▼
IAM Role
 │
 ▼
S3 Access
```

rather than placing permanent AWS access keys on the EC2 filesystem.

## 158. Simple Exercise

Have the EC2 environment/application perform a limited S3 operation such as:

```text
list permitted bucket
upload test object
download test object
```

Use least privilege.

---

# Tier 46 — EC2 with EBS

## 159. Persistent Server Data

Attach an additional EBS volume.

```text
EC2
 │
 ▼
EBS
 │
 ▼
mounted directory
```

## 160. Docker Volume Relationship Awareness

A Docker bind mount can point to a directory backed by the mounted EBS filesystem.

Concept:

```text
EBS
 │
 ▼
/mnt/app-data
 │
 ▼
Docker bind mount
 │
 ▼
Container
```

## 161. Educational Use

This demonstrates persistence across container replacement.

It does not mean every application should use EBS for every kind of data.

---

# Tier 47 — PostgreSQL on EC2/EBS: Educational Only

## 162. Learning Demonstration

You may run PostgreSQL in the practice environment to understand:

```text
database process/container
       │
       ▼
filesystem
       │
       ▼
EBS-backed storage
```

## 163. Important Architecture Note

This is useful for learning compute + persistent block storage.

It should **not** automatically become the recommended production AWS database architecture for this curriculum.

## 164. Future RDS Tree

A future **Amazon RDS** skill tree should introduce managed relational databases.

```text
AWS Fundamentals
      │
      ▼
     RDS
      │
      ▼
Managed PostgreSQL
```

---

# Tier 48 — Basic Troubleshooting

## 165. Cannot Find Resource

Check:

```text
AWS account
Region
filters
resource state
```

## 166. Cannot SSH

Check:

```text
instance running?
correct address?
correct username?
correct private key?
security group permits your source IP?
network reachable?
```

## 167. Application Not Reachable

Check:

```text
container/process running?
correct port?
application bound correctly?
security group?
OS firewall?
public address?
health endpoint?
```

## 168. EC2 Cannot Access S3

Check:

```text
IAM role attached?
policy permits operation?
correct bucket/key?
correct Region/config?
application using role credentials?
```

## 169. EBS Not Visible

Check:

```text
volume attached?
same AZ?
correct instance?
Linux device visible?
```

## 170. EBS Attached but No Files

Ask:

```text
filesystem created?
mounted?
correct mount point?
permissions?
```

---

# Tier 49 — Cleanup and Cost Control

## 171. End Every Lab With Inventory

Check:

```text
EC2 instances
EBS volumes
EBS snapshots
S3 buckets/objects
public IP resources if applicable
other resources created during exercise
```

## 172. EC2

Terminate practice instances when finished if they are no longer needed.

## 173. EBS

Verify whether volumes remain after instance termination.

Delete unused practice volumes.

## 174. Snapshots

Delete snapshots that are no longer needed.

## 175. S3

Delete unneeded objects/buckets from practice exercises.

## 176. Verify

Do not assume clicking "terminate" once cleaned up the entire lab.

---

# Tier 50 — What Comes Next

This introductory tree should create branches rather than trying to absorb the rest of AWS.

```text
                    AWS Fundamentals
                   EC2 + S3 + EBS
                          │
       ┌──────────────────┼──────────────────┐
       ▼                  ▼                  ▼
    AWS IAM         AWS Networking        Amazon RDS
                         │
                         ▼
                 VPC / DNS / Load
                    Balancing
                         │
       ┌─────────────────┼─────────────────┐
       ▼                 ▼                 ▼
    ECS / ECR        EKS / AWS        Serverless AWS
                                         Lambda
```

Other later topics may include:

- [ ] CloudWatch
- [ ] Route 53
- [ ] CloudFront
- [ ] Elastic Load Balancing
- [ ] Auto Scaling
- [ ] RDS
- [ ] ECR
- [ ] ECS
- [ ] EKS
- [ ] Lambda
- [ ] API Gateway
- [ ] Infrastructure as Code
- [ ] AWS Security
- [ ] Advanced IAM
- [ ] AWS Networking
- [ ] Backup/disaster recovery
- [ ] Cost optimization

These are **not prerequisites for completing this introductory tree**.

---

# Security Progression

```text
                  Security Fundamentals
                         │
            ┌────────────┼────────────┐
            ▼            ▼            ▼
      Application     Container     Cloud Security
       Security       Security          │
            │            │              ▼
            │            │           AWS Security
            │            │              │
            └────────────┼──────────────┘
                         ▼
                      DevSecOps
```

AWS-specific future branch:

```text
AWS Fundamentals
      │
      ├── Basic IAM awareness
      ├── Security groups
      ├── Credential safety
      └── Shared responsibility
             │
             ▼
        AWS IAM Tree
             │
             ▼
      AWS / Cloud Security
```

---

# Practical Competency Checkpoints

A learner completing this tree should be able to:

- [ ] Explain cloud computing at a basic level
- [ ] Explain what AWS is
- [ ] Explain why EC2/S3/EBS are only a small part of AWS
- [ ] Navigate the AWS Management Console
- [ ] Identify the active Region
- [ ] Explain Region vs Availability Zone
- [ ] Identify AWS resources by ID/name/tag
- [ ] Explain why AWS resources may cost money
- [ ] Clean up unused resources
- [ ] Explain the shared responsibility model
- [ ] Explain IAM user, role, and policy at a high level
- [ ] Explain least privilege
- [ ] Keep AWS credentials out of Git/source code
- [ ] Explain why an EC2 IAM role is preferable to hardcoded access keys
- [ ] Use the AWS CLI for basic inspection/S3 operations
- [ ] Explain VPC and subnet at a basic level
- [ ] Explain public vs private IP
- [ ] Explain security groups
- [ ] Restrict SSH appropriately
- [ ] Explain EC2
- [ ] Choose an AMI
- [ ] Explain instance types
- [ ] Launch an EC2 instance
- [ ] Connect to EC2 using SSH
- [ ] Install/use Docker on EC2
- [ ] Start/stop/reboot/terminate EC2 appropriately
- [ ] Inspect EC2 status/basic monitoring
- [ ] Explain S3 object storage
- [ ] Create an S3 bucket
- [ ] Explain objects and keys
- [ ] Upload/download/delete/list S3 objects
- [ ] Perform basic S3 CLI operations
- [ ] Keep S3 private unless public access is deliberate
- [ ] Grant limited EC2-to-S3 access with an IAM role
- [ ] Explain S3 versioning
- [ ] Explain lifecycle/storage classes at a high level
- [ ] Explain EBS block storage
- [ ] Explain root vs additional EBS volumes
- [ ] Create and attach an EBS volume
- [ ] Verify the Linux block device
- [ ] Format a new volume safely
- [ ] Mount/unmount a filesystem
- [ ] Explain mount persistence
- [ ] Explain EBS persistence
- [ ] Explain EBS volume types at a high level
- [ ] Create/understand EBS snapshots
- [ ] Explain EBS resizing at a high level
- [ ] Clearly distinguish EC2, S3, and EBS
- [ ] Deploy a Dockerized application to EC2
- [ ] Connect EC2/application behavior to S3
- [ ] Use EBS-backed storage with an application/container
- [ ] Explain why PostgreSQL-on-EC2/EBS is educational rather than automatically preferred production architecture
- [ ] Troubleshoot basic EC2/S3/EBS failures
- [ ] Identify which AWS topics belong in later skill trees

---

# Suggested Practice Progression

```text
1. Create / secure AWS learning account
        │
        ▼
2. Explore AWS Console
        │
        ▼
3. Select and identify Region
        │
        ▼
4. Identify Availability Zones
        │
        ▼
5. Review billing / cost tools
        │
        ▼
6. Learn basic IAM concepts
        │
        ▼
7. Install / verify AWS CLI
        │
        ▼
8. Verify AWS identity / Region
        │
        ▼
9. Inspect default VPC/network
        │
        ▼
10. Create security group
        │
        ▼
11. Launch Ubuntu EC2
        │
        ▼
12. SSH into EC2
        │
        ▼
13. Inspect Linux environment
        │
        ▼
14. Install Docker
        │
        ▼
15. Run simple container
        │
        ▼
16. Expose/test application port
        │
        ▼
17. Create S3 bucket
        │
        ▼
18. Upload/download via Console
        │
        ▼
19. Repeat S3 operations via CLI
        │
        ▼
20. Create limited IAM role
        │
        ▼
21. Allow EC2 limited S3 access
        │
        ▼
22. Create additional EBS volume
        │
        ▼
23. Attach to EC2
        │
        ▼
24. Inspect block device
        │
        ▼
25. Format new volume
        │
        ▼
26. Mount volume
        │
        ▼
27. Store test data
        │
        ▼
28. Restart/reboot and inspect persistence
        │
        ▼
29. Create EBS snapshot
        │
        ▼
30. Deploy full-stack Dockerized app
        │
        ▼
31. Connect app/EC2 to S3
        │
        ▼
32. Use EBS-backed application storage
        │
        ▼
33. Verify application externally
        │
        ▼
34. Troubleshoot intentional failures
        │
        ▼
35. Inspect all resources
        │
        ▼
36. Clean up
        │
        ▼
37. Verify no unintended resources remain
```

---

# Capstone — Introductory AWS Full-Stack Deployment

## Objective

Deploy a simple application using the existing full-stack and Docker knowledge while using EC2, S3, and EBS for clearly different purposes.

```text
                   Internet / User
                         │
                         ▼
                    Security Group
                         │
                         ▼
                     EC2 Instance
                         │
                    Docker Engine
                         │
              ┌──────────┴──────────┐
              ▼                     ▼
         Frontend/API          Application Data
                                    │
                       ┌────────────┴────────────┐
                       ▼                         ▼
                      EBS                       S3
                Block Storage             Object Storage
```

---

## Capstone Phase 1 — Plan

Before creating resources, write down:

```text
Region:
EC2 name:
instance type:
AMI:
security-group ports:
S3 bucket purpose:
EBS volume purpose:
expected cleanup:
```

The purpose is to create resources intentionally rather than clicking through the console without a model.

---

## Capstone Phase 2 — EC2

Create an EC2 instance.

Verify:

- [ ] Correct Region
- [ ] Appropriate beginner instance type
- [ ] Ubuntu or chosen AMI
- [ ] Key/access method configured
- [ ] SSH restricted appropriately
- [ ] Application port configured only as needed
- [ ] Resource tagged/named

---

## Capstone Phase 3 — Connect

```text
Local WSL2/Linux Terminal
          │
          ▼
         SSH
          │
          ▼
      EC2 Ubuntu
```

Verify:

```text
whoami
pwd
OS
network
disk
```

---

## Capstone Phase 4 — Docker

Install/configure Docker using the Docker knowledge tree.

Verify with a simple container before deploying the full application.

```text
EC2
 │
 ▼
Docker
 │
 ▼
Test Container
```

---

## Capstone Phase 5 — Application

Deploy the existing simple application.

Concept:

```text
Container Registry
       │
       ▼
      EC2
       │
       ▼
docker pull
       │
       ▼
docker run / Compose
```

Verify application access externally.

---

## Capstone Phase 6 — S3

Create a private S3 bucket for a simple object-storage purpose.

Examples:

```text
uploaded files
sample documents
generated exports
application images
```

Practice:

- [ ] Upload via console
- [ ] Download via console
- [ ] List via CLI
- [ ] Upload via CLI
- [ ] Delete test object

---

## Capstone Phase 7 — IAM Role for S3

Create/use an appropriately limited EC2 role.

```text
EC2
 │
 ▼
IAM Role
 │
 ▼
Permission:
specific S3 operations
on intended bucket
```

Verify EC2 can perform the intended S3 operation without copying a permanent access key onto the instance.

---

## Capstone Phase 8 — EBS

Create an additional EBS volume in the correct Availability Zone.

```text
EBS
 │
 ▼
Attach to EC2
 │
 ▼
Identify Device
 │
 ▼
Create Filesystem
 │
 ▼
Mount
```

Store test application data on it.

---

## Capstone Phase 9 — Docker + EBS

Use an EBS-backed host directory with a container.

```text
EBS Volume
    │
    ▼
Mounted Filesystem
    │
    ▼
/mnt/app-data
    │
    ▼
Docker Bind Mount
    │
    ▼
Container
```

Replace/restart the container and verify the intended data persists.

---

## Capstone Phase 10 — Snapshot

Create an EBS snapshot.

Be able to explain:

```text
running EBS volume
vs
snapshot backup
```

Do not leave unnecessary snapshots after the lab.

---

## Capstone Phase 11 — Verification

Verify all three services independently.

### EC2

```text
Is the server running?
Is Docker running?
Is the application reachable?
```

### S3

```text
Can the authorized identity access the intended objects?
Is unintended public access disabled?
```

### EBS

```text
Is the volume attached?
Is it mounted?
Does expected data persist?
```

---

## Capstone Phase 12 — Intentional Failure Drills

### Failure A — SSH Blocked

Remove/restrict the SSH rule incorrectly.

Diagnose:

```text
EC2 state?
address?
security group?
source IP?
key?
```

### Failure B — Application Port Blocked

Keep the container running but remove external network permission.

Learn:

```text
application can be healthy
while network access is blocked
```

### Failure C — S3 Permission Denied

Remove required role permission.

Diagnose:

```text
identity
role
policy
bucket
operation
```

### Failure D — EBS Not Mounted

Detach/unmount appropriately for the exercise.

Diagnose:

```text
AWS attachment
Linux block device
filesystem
mount
```

---

## Capstone Phase 13 — Cost Inspection

Before cleanup, identify every resource created.

```text
EC2 instance
EBS root volume
EBS additional volume
snapshot
S3 bucket/objects
security group
IAM role/policy created for lab
other associated resources
```

Ask:

> **Which of these can continue costing money if I forget it?**

---

## Capstone Phase 14 — Cleanup

```text
Stop application
      │
      ▼
Remove unneeded S3 objects
      │
      ▼
Delete practice bucket
      │
      ▼
Unmount/detach EBS if needed
      │
      ▼
Terminate EC2
      │
      ▼
Verify EBS deletion behavior
      │
      ▼
Delete leftover EBS volume
      │
      ▼
Delete unneeded snapshot
      │
      ▼
Remove unneeded practice IAM/security resources
      │
      ▼
Inspect console again
```

---

# Interview Readiness

Be able to answer:

- [ ] What is AWS?
- [ ] What is cloud computing?
- [ ] What is an AWS Region?
- [ ] What is an Availability Zone?
- [ ] Why can Region selection matter?
- [ ] What is the AWS shared responsibility model?
- [ ] What is IAM?
- [ ] User vs role vs policy?
- [ ] What is least privilege?
- [ ] Why shouldn't AWS credentials be committed to Git?
- [ ] Why use an IAM role for an EC2 application?
- [ ] What is the AWS CLI?
- [ ] What is a VPC at a high level?
- [ ] What is a subnet?
- [ ] Public vs private IP?
- [ ] What is a security group?
- [ ] What is EC2?
- [ ] What is an AMI?
- [ ] What is an EC2 instance type?
- [ ] What happens when an EC2 instance is stopped?
- [ ] Stop vs terminate?
- [ ] How do you connect to EC2?
- [ ] Why should SSH access be restricted?
- [ ] What is S3?
- [ ] What is object storage?
- [ ] What is an S3 bucket?
- [ ] What is an S3 object?
- [ ] What is an S3 key?
- [ ] Why are S3 "folders" not ordinary filesystem folders?
- [ ] How can EC2 access S3 securely?
- [ ] What is S3 versioning?
- [ ] What are lifecycle rules at a high level?
- [ ] What is EBS?
- [ ] What is block storage?
- [ ] EBS vs S3?
- [ ] What is an EC2 root volume?
- [ ] Why must an EBS volume's AZ matter for attachment?
- [ ] What does attaching an EBS volume do?
- [ ] Why might a new EBS volume need formatting?
- [ ] What is mounting?
- [ ] Why can `/etc/fstab` matter?
- [ ] What is an EBS snapshot?
- [ ] What happens to EBS when EC2 is terminated?
- [ ] How can Docker use EBS-backed storage?
- [ ] Why is PostgreSQL-on-EC2/EBS useful educationally but not automatically the preferred AWS production database?
- [ ] What AWS service would you study next for managed PostgreSQL?
- [ ] How do EC2, S3, and EBS fit together?
- [ ] Why is cleanup part of AWS development?
- [ ] Why shouldn't you memorize AWS pricing?
- [ ] Which AWS subjects are intentionally outside this introductory tree?

---

# Common Anti-Patterns

## Anti-Pattern 1 — "AWS = EC2"

EC2 is one AWS service.

## Anti-Pattern 2 — "S3 Is a Hard Drive"

S3 is object storage.

EBS is much closer to the attached-disk mental model.

## Anti-Pattern 3 — Making S3 Public to Fix Permission Errors

Fix identity and permissions instead of removing the security boundary.

## Anti-Pattern 4 — Administrator Permissions Everywhere

Use least privilege.

## Anti-Pattern 5 — AWS Keys in Source Code

Never commit cloud credentials.

## Anti-Pattern 6 — AWS Keys Inside EC2 Application Files by Default

Use IAM roles where appropriate.

## Anti-Pattern 7 — SSH Open to the Entire Internet Without Need

Restrict management access appropriately.

## Anti-Pattern 8 — "Running EC2 Means My App Works"

Verify the application itself.

## Anti-Pattern 9 — "Stopped Means Free"

Storage and other resources can remain billable.

## Anti-Pattern 10 — "Terminate EC2 Deletes Everything"

Inspect attached volumes, snapshots, buckets, IP resources, and other resources.

## Anti-Pattern 11 — Formatting an Unknown Disk

Verify the EBS device before filesystem operations.

## Anti-Pattern 12 — Treating EBS Snapshot as the Live Disk

A snapshot is backup data used to create/restore volumes.

## Anti-Pattern 13 — `latest` as the Only Docker Image Identity

Use traceable versions.

## Anti-Pattern 14 — Production PostgreSQL on a Hand-Built EC2 Server Because It Worked in the Lab

Use the lab to understand infrastructure; study RDS before choosing a production AWS database architecture.

## Anti-Pattern 15 — Trying to Learn Every AWS Service at Once

This tree intentionally stops.

---

# Relationship to Existing Skill Trees

```text
Linux / Ubuntu
      │
      ├──────────────────────┐
      ▼                      ▼
    Docker                 Git
      │                      │
      └──────────┬───────────┘
                 ▼
        Deployment Fundamentals
                 │
                 ▼
          AWS Fundamentals
        EC2 + S3 + EBS
```

Application path:

```text
Java / Spring Boot
        │
        ├──────────────┐
        ▼              ▼
     Backend       Angular / React
        │              │
        └──────┬───────┘
               ▼
             Docker
               │
               ▼
       AWS Fundamentals
               │
               ▼
              EC2
```

Storage path:

```text
AWS Fundamentals
       │
   ┌───┴───┐
   ▼       ▼
  S3      EBS
Object    Block
```

CI/CD connection:

```text
CI/CD Fundamentals
        │
        ├── Jenkins
        ├── GitHub Actions
        └── GitLab CI/CD
               │
               ▼
      Deployment Fundamentals
               │
               ▼
         AWS Fundamentals
```

Later, the CI/CD platforms can automate the manual AWS deployment steps learned here.

---

# Future AWS Branches

This tree should deliberately point outward:

```text
                         AWS Fundamentals
                       EC2 + S3 + EBS
                              │
        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                     ▼
     AWS IAM            AWS Networking         Amazon RDS
        │               VPC / DNS / LB             │
        │                     │                     ▼
        │                     │              Managed PostgreSQL
        │                     │
        ▼                     ▼
 AWS Security        Production Networking
                              │
             ┌────────────────┼────────────────┐
             ▼                ▼                ▼
          ECR/ECS            EKS           Serverless
                                             Lambda
```

Possible future dedicated trees:

```text
AWS IAM
AWS Networking / VPC
Amazon RDS
Amazon ECR
Amazon ECS
AWS Load Balancing & Auto Scaling
Route 53
CloudFront
CloudWatch
AWS Security
AWS Lambda & API Gateway
Infrastructure as Code on AWS
EKS / Kubernetes on AWS
AWS Cost Management
```

---

# Mastery Progression

```text
"I know AWS is a cloud platform"
        │
        ▼
"I understand Regions and AZs"
        │
        ▼
"I can navigate the AWS Console"
        │
        ▼
"I understand basic IAM and cost risk"
        │
        ▼
"I understand basic AWS networking"
        │
        ▼
"I can launch EC2"
        │
        ▼
"I can SSH into the instance"
        │
        ▼
"I can run Docker on EC2"
        │
        ▼
"I can create and use S3"
        │
        ▼
"I can give EC2 limited S3 access"
        │
        ▼
"I can create and attach EBS"
        │
        ▼
"I can format and mount EBS"
        │
        ▼
"I understand EBS persistence/snapshots"
        │
        ▼
"I can deploy my simple full-stack app"
        │
        ▼
"I can distinguish EC2, S3, and EBS"
        │
        ▼
"I can troubleshoot basic failures"
        │
        ▼
"I can clean up my resources"
        │
        ▼
"I know what I DON'T know about AWS"
        │
        ▼
"I am ready for specialized AWS trees"
```

---

# Mastery Standard

> **Can I independently enter an AWS account, identify the Region and basic security context, launch and securely access an EC2 instance, deploy a simple Dockerized application, create and safely use a private S3 bucket, give EC2 limited S3 access through an IAM role, create/attach/format/mount an EBS volume, explain the persistence and snapshot model, troubleshoot basic connectivity/storage/permission problems, distinguish compute from object and block storage, inspect likely cost-producing resources, and clean up the environment—while understanding that this is only an introduction to AWS rather than mastery of the platform?**

Final mental model:

```text
                              AWS
                               │
                               ▼
                             Region
                               │
                               ▼
                         Basic Network
                               │
                               ▼
                        Security Group
                               │
                               ▼
                         EC2 Instance
                        "COMPUTE / RUN"
                               │
                          Docker Engine
                               │
                         Application
                          │         │
              ┌───────────┘         └───────────┐
              ▼                                 ▼
             EBS                               S3
        "BLOCK STORAGE"                  "OBJECT STORAGE"
              │                                 │
       mounted filesystem                 bucket / objects
              │                                 │
       server-local style                API-based storage
       persistent data                    files / objects


                       Security Around It
                              │
              ┌───────────────┼───────────────┐
              ▼               ▼               ▼
             IAM       Security Groups    Credentials
              │               │               │
              └───────────────┼───────────────┘
                              ▼
                       Least Privilege


                     Operational Habit
                              │
             Create → Verify → Observe
                              │
                              ▼
                       Troubleshoot
                              │
                              ▼
                        CLEAN UP
                              │
                              ▼
                       Check Costs


And then stop.

You have learned:

EC2 + S3 + EBS + enough AWS context to use them.

You have NOT attempted to learn all of AWS.
