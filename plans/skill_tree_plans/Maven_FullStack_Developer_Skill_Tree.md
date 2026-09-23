# Maven Skill Tree --- Full-Stack Java Developer Path

> **Goal:** Progress from basic Java project knowledge to professional,
> application-level Maven proficiency for Java and eventual Spring Boot
> development.
>
> **Learning context:** Maven is the build and dependency-management
> branch of the Java full-stack path.
>
> **Scope boundary:** This tree focuses on Maven. Controller → Service →
> Repository architecture belongs primarily in Java application
> architecture and Spring Boot skill trees, not Maven.
>
> **Target level:** Professional full-stack Java developer --- not Maven
> plugin author or enterprise build-system specialist.

------------------------------------------------------------------------

# Skill Tree Overview

``` text
Maven
├── 1. Prerequisite Java Knowledge
├── 2. What Maven Is
├── 3. Maven Installation & Environment
├── 4. Maven Project Structure
├── 5. POM Fundamentals
├── 6. Coordinates & Artifact Identity
├── 7. Dependencies
├── 8. Dependency Scopes
├── 9. Repositories
├── 10. Build Lifecycles
├── 11. Phases, Plugins & Goals
├── 12. Common Maven Commands
├── 13. Compiler Configuration
├── 14. Testing Integration
├── 15. Resources
├── 16. Packaging & Artifacts
├── 17. Dependency Management & BOMs
├── 18. Transitive Dependencies & Conflict Resolution
├── 19. Parent POMs & Inheritance
├── 20. Properties
├── 21. Profiles
├── 22. Multi-Module Projects
├── 23. Maven Wrapper
├── 24. Plugin Configuration
├── 25. Build Troubleshooting
├── 26. Dependency Troubleshooting
├── 27. Reproducible & Maintainable Builds
├── 28. Security & Dependency Hygiene
├── 29. CI/CD Readiness
├── 30. Spring Boot Readiness
└── 31. Professional Maven Workflow
```

------------------------------------------------------------------------

# Dependency Map

``` text
Java Fundamentals
       │
       ▼
     Maven
       │
       ├───────────────┐
       ▼               ▼
     JUnit         Application
       │           Architecture
       ▼               │
    Mockito             │
       │                │
       └───────┬────────┘
               ▼
          Spring Boot
               │
      ┌────────┼─────────┐
      ▼        ▼         ▼
 Controller  Service  Repository
```

Maven can **build, test, package, and manage dependencies for** a
layered application, but Maven does not define the Controller → Service
→ Repository architecture.

------------------------------------------------------------------------

# Tier 0 --- Prerequisites

## 1. Prerequisite Java Knowledge

-   [ ] Understand `.java` source files
-   [ ] Understand `.class` bytecode files
-   [ ] Compile simple Java code
-   [ ] Run Java programs
-   [ ] Understand packages
-   [ ] Understand imports
-   [ ] Understand basic project directory structure
-   [ ] Understand external libraries conceptually
-   [ ] Understand JAR files conceptually
-   [ ] Know that Java code can depend on code from other libraries

**Checkpoint:** Build and run a small multi-class Java program before
using Maven to automate the process.

------------------------------------------------------------------------

# Tier 1 --- Maven Foundations

## 2. What Maven Is

-   [ ] Explain Maven as a build automation tool
-   [ ] Explain Maven as a dependency-management tool
-   [ ] Explain Maven's convention-over-configuration approach
-   [ ] Understand that Maven is separate from Java
-   [ ] Understand that Maven is separate from Spring Boot
-   [ ] Understand that Maven does not define application architecture
-   [ ] Compare manual `javac`/classpath management with Maven at a high
    level
-   [ ] Recognize Maven projects from `pom.xml`

### Mental model

``` text
Source Code
    │
    ▼
  Maven
    │
    ├── downloads dependencies
    ├── compiles code
    ├── runs tests
    ├── processes resources
    └── packages artifacts
            │
            ▼
          JAR/WAR
```

------------------------------------------------------------------------

## 3. Maven Installation & Environment

-   [ ] Install Maven
-   [ ] Run `mvn -version`
-   [ ] Understand Maven's relationship with the installed JDK
-   [ ] Recognize `JAVA_HOME`
-   [ ] Recognize Maven configuration locations
-   [ ] Understand the user home `.m2` directory
-   [ ] Locate the local Maven repository
-   [ ] Distinguish system Maven from Maven Wrapper usage

**Checkpoint:** Verify Maven and Java versions and explain which JDK
Maven is using.

------------------------------------------------------------------------

# Tier 2 --- Maven Project Structure

## 4. Standard Maven Project Structure

``` text
project/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   └── resources/
    └── test/
        ├── java/
        └── resources/
```

-   [ ] `pom.xml`
-   [ ] `src/main/java`
-   [ ] `src/main/resources`
-   [ ] `src/test/java`
-   [ ] `src/test/resources`
-   [ ] `target/`
-   [ ] Understand generated build output
-   [ ] Know why `target/` normally should not be committed
-   [ ] Understand Maven's directory conventions
-   [ ] Recognize nonstandard layouts and know they require
    configuration

### Full-stack relevance

-   [ ] Place production Java classes correctly
-   [ ] Place configuration/resource files correctly
-   [ ] Place JUnit tests correctly
-   [ ] Understand where Maven places compiled output

------------------------------------------------------------------------

# Tier 3 --- POM Fundamentals

## 5. `pom.xml`

-   [ ] Understand POM = Project Object Model
-   [ ] Read basic POM XML
-   [ ] Understand the `<project>` root
-   [ ] Understand `modelVersion`
-   [ ] Understand project metadata
-   [ ] Recognize dependency configuration
-   [ ] Recognize build/plugin configuration

Example structure:

``` xml
<project>
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>guestbook</artifactId>
    <version>1.0.0</version>
</project>
```

## 6. Maven Coordinates

-   [ ] `groupId`
-   [ ] `artifactId`
-   [ ] `version`
-   [ ] `packaging`
-   [ ] Understand GAV coordinates
-   [ ] Understand how coordinates uniquely identify artifacts
-   [ ] Recognize common version conventions
-   [ ] Understand `SNAPSHOT` conceptually

``` text
com.example : guestbook : 1.0.0
     │            │          │
  groupId     artifactId   version
```

**Checkpoint:** Create a minimal POM and explain every required element.

------------------------------------------------------------------------

# Tier 4 --- Dependencies

## 7. Adding Dependencies

``` xml
<dependencies>
    <dependency>
        <groupId>...</groupId>
        <artifactId>...</artifactId>
        <version>...</version>
    </dependency>
</dependencies>
```

-   [ ] Add a dependency
-   [ ] Read dependency coordinates
-   [ ] Understand why Maven downloads dependencies
-   [ ] Understand that dependencies are placed on relevant classpaths
-   [ ] Distinguish application code from third-party libraries
-   [ ] Remove unused dependencies
-   [ ] Update dependency versions intentionally

## 8. Dependency Scopes

-   [ ] `compile`
-   [ ] `runtime`
-   [ ] `test`
-   [ ] `provided`
-   [ ] `system` awareness
-   [ ] `import` scope in dependency management
-   [ ] Understand scope effects on compilation
-   [ ] Understand scope effects on testing
-   [ ] Understand scope effects on runtime/packaging
-   [ ] Choose appropriate scope instead of defaulting blindly

### Testing connection

``` text
JUnit / Mockito
      │
      ▼
test-scoped dependencies
      │
      ▼
Available to tests
but not ordinary production runtime
```

**Checkpoint:** Add JUnit as a test dependency and explain why it should
not normally be a production dependency.

------------------------------------------------------------------------

# Tier 5 --- Repositories

## 9. Maven Repositories

### Local repository

-   [ ] Understand the local `.m2/repository`
-   [ ] Understand dependency caching
-   [ ] Locate a downloaded artifact
-   [ ] Understand that deleting cached artifacts can force
    re-resolution, but should not be the default troubleshooting step

### Central repository

-   [ ] Understand Maven Central
-   [ ] Understand Maven's default remote dependency resolution

### Other repositories

-   [ ] Understand remote repositories conceptually
-   [ ] Recognize organization/private repositories
-   [ ] Recognize repository configuration in POM/settings
-   [ ] Avoid adding arbitrary repositories unnecessarily

### Resolution model

``` text
Project needs dependency
        │
        ▼
Check local repository
        │
   not available
        ▼
Check configured remote repository
        │
        ▼
Download artifact + metadata
        │
        ▼
Cache locally
```

------------------------------------------------------------------------

# Tier 6 --- Build Lifecycle

## 10. Maven Lifecycles

-   [ ] Understand lifecycle
-   [ ] Understand phase
-   [ ] Understand goal
-   [ ] Understand that invoking a later lifecycle phase executes
    preceding phases

### Default lifecycle --- important phases

``` text
validate
   ↓
compile
   ↓
test
   ↓
package
   ↓
verify
   ↓
install
   ↓
deploy
```

-   [ ] `validate`
-   [ ] `compile`
-   [ ] `test`
-   [ ] `package`
-   [ ] `verify`
-   [ ] `install`
-   [ ] `deploy`

### Clean lifecycle

-   [ ] `clean`

### Site lifecycle awareness

-   [ ] Recognize Maven's site/reporting lifecycle
-   [ ] Deep mastery not required for this path

## 11. Understand Phase Chaining

Running:

``` bash
mvn package
```

conceptually includes earlier required phases:

``` text
validate
  ↓
compile
  ↓
test
  ↓
package
```

-   [ ] Explain why tests run during ordinary packaging
-   [ ] Explain why `mvn install` does more than compile
-   [ ] Understand when `clean` is useful
-   [ ] Avoid assuming every build requires `clean`

**Checkpoint:** Predict what Maven will do for `mvn compile`,
`mvn test`, `mvn package`, and `mvn install`.

------------------------------------------------------------------------

# Tier 7 --- Plugins & Goals

## 12. Plugins

-   [ ] Understand that Maven plugins perform build work
-   [ ] Understand plugin goals
-   [ ] Understand lifecycle phase → plugin goal binding
-   [ ] Distinguish Maven core concepts from plugin-provided behavior
-   [ ] Read plugin configuration in a POM

``` text
Lifecycle Phase
      │
      ▼
Plugin Goal
      │
      ▼
Actual Build Action
```

Examples: - \[ \] Compiler plugin - \[ \] Surefire plugin - \[ \]
Failsafe plugin - \[ \] Resources plugin - \[ \] JAR plugin - \[ \]
Spring Boot Maven plugin later

## 13. Calling Plugin Goals

Recognize syntax such as:

``` bash
mvn dependency:tree
```

``` text
dependency : tree
    │         │
 plugin      goal
```

-   [ ] Distinguish direct goal invocation from lifecycle phase
    invocation
-   [ ] Read plugin documentation when configuration is needed

------------------------------------------------------------------------

# Tier 8 --- Common Commands

## 14. Core Commands

-   [ ] `mvn validate`
-   [ ] `mvn compile`
-   [ ] `mvn test`
-   [ ] `mvn package`
-   [ ] `mvn verify`
-   [ ] `mvn install`
-   [ ] `mvn clean`
-   [ ] `mvn clean package`
-   [ ] `mvn clean verify`
-   [ ] `mvn dependency:tree`
-   [ ] `mvn help:effective-pom`

### Command reasoning

-   [ ] Choose the shortest phase that accomplishes the task
-   [ ] Understand why `mvn clean install` is not automatically the
    correct command for everything
-   [ ] Interpret `BUILD SUCCESS`
-   [ ] Interpret `BUILD FAILURE`

------------------------------------------------------------------------

# Tier 9 --- Compiler Configuration

## 15. Java Version Configuration

-   [ ] Understand Maven compiler configuration
-   [ ] Configure Java release/version appropriately
-   [ ] Understand source/target/release conceptually
-   [ ] Recognize mismatches between Maven JDK and project Java version
-   [ ] Diagnose unsupported release errors

Example concept:

``` xml
<properties>
    <maven.compiler.release>21</maven.compiler.release>
</properties>
```

-   [ ] Understand why build configuration should explicitly communicate
    required Java version

------------------------------------------------------------------------

# Tier 10 --- Testing Integration

## 16. Maven + JUnit

``` text
mvn test
   │
   ▼
Test plugin
   │
   ▼
JUnit Platform
   │
   ▼
JUnit tests
```

-   [ ] Understand how Maven discovers/runs tests
-   [ ] Understand test naming conventions
-   [ ] Run all unit tests
-   [ ] Run selected tests
-   [ ] Read test reports
-   [ ] Understand failed tests causing build failure

## 17. Maven + Mockito

-   [ ] Add Mockito as a test dependency
-   [ ] Understand Mockito remains separate from Maven
-   [ ] Understand Maven provides dependencies/classpath and executes
    the test build
-   [ ] Keep Mockito test-scoped

## 18. Surefire

-   [ ] Understand Surefire's role in unit-test execution
-   [ ] Recognize Surefire reports
-   [ ] Configure only when needed
-   [ ] Diagnose basic test discovery problems

## 19. Integration Testing & Failsafe

-   [ ] Understand unit vs integration test build stages
-   [ ] Understand Failsafe conceptually
-   [ ] Understand `integration-test` and `verify`
-   [ ] Recognize why integration-test failures should be handled
    differently from unit-test execution
-   [ ] Prepare for Spring Boot integration tests later

**Checkpoint:** Configure and run JUnit/Mockito tests entirely through
Maven.

------------------------------------------------------------------------

# Tier 11 --- Resources

## 20. Application Resources

-   [ ] Understand `src/main/resources`
-   [ ] Understand resource copying
-   [ ] Understand resources appearing on the runtime classpath
-   [ ] Load classpath resources from Java
-   [ ] Recognize YAML/properties files as application resources

## 21. Test Resources

-   [ ] Understand `src/test/resources`
-   [ ] Keep test-only data separate
-   [ ] Understand test classpath behavior

## 22. Filtering Awareness

-   [ ] Understand Maven resource filtering conceptually
-   [ ] Recognize placeholder replacement
-   [ ] Know that application environment configuration and Maven
    build-time filtering are different concerns
-   [ ] Avoid putting secrets into built artifacts accidentally

------------------------------------------------------------------------

# Tier 12 --- Packaging

## 23. JAR Packaging

-   [ ] Understand JAR output
-   [ ] Locate artifacts under `target/`
-   [ ] Inspect JAR contents
-   [ ] Understand manifest conceptually
-   [ ] Understand ordinary vs executable JARs

## 24. WAR Awareness

-   [ ] Understand WAR packaging historically/currently
-   [ ] Recognize servlet-container deployment
-   [ ] Know that modern Spring Boot commonly uses executable JARs
-   [ ] Do not prioritize WAR mastery unless a project requires it

## 25. Artifact Lifecycle

``` text
Source
  ↓
Compile
  ↓
Test
  ↓
Package
  ↓
Artifact
  ↓
Install / Publish
```

------------------------------------------------------------------------

# Tier 13 --- Dependency Management

## 26. `<dependencyManagement>`

-   [ ] Understand dependency declaration vs dependency management
-   [ ] Centralize versions
-   [ ] Understand inherited version management
-   [ ] Know that dependency management does not necessarily add the
    dependency itself

## 27. BOMs

-   [ ] Understand BOM = Bill of Materials
-   [ ] Understand coordinated dependency versions
-   [ ] Understand imported BOMs
-   [ ] Recognize why frameworks use BOMs
-   [ ] Prepare to understand Spring Boot dependency management

### Important distinction

``` text
<dependencies>
    → actually requests dependencies

<dependencyManagement>
    → controls/defaults dependency information,
      especially versions
```

------------------------------------------------------------------------

# Tier 14 --- Transitive Dependencies

## 28. Transitive Dependency Model

``` text
Your Application
      │
      ▼
 Library A
      │
      ▼
 Library B
```

Your project may receive Library B transitively.

-   [ ] Explain transitive dependencies
-   [ ] Understand dependency graphs
-   [ ] Understand nearest-definition mediation conceptually
-   [ ] Recognize version conflicts
-   [ ] Understand dependency exclusions
-   [ ] Avoid adding exclusions without understanding the graph

## 29. Dependency Tree

``` bash
mvn dependency:tree
```

-   [ ] Read direct dependencies
-   [ ] Read transitive dependencies
-   [ ] Identify duplicate/conflicting versions
-   [ ] Identify unexpected libraries
-   [ ] Use filtered dependency-tree output when useful

**Checkpoint:** Diagnose why a library exists in a project's classpath
even though it was not directly declared.

------------------------------------------------------------------------

# Tier 15 --- Parent POMs & Inheritance

## 30. Parent POM

-   [ ] Understand POM inheritance
-   [ ] Understand `<parent>`
-   [ ] Inherit properties
-   [ ] Inherit dependency management
-   [ ] Inherit plugin management/configuration where applicable
-   [ ] Understand effective configuration

## 31. Effective POM

``` bash
mvn help:effective-pom
```

-   [ ] Understand that the effective POM combines multiple
    configuration sources
-   [ ] Use it to diagnose inherited configuration
-   [ ] Recognize settings that do not visibly appear in the project's
    own POM

------------------------------------------------------------------------

# Tier 16 --- Properties

## 32. Maven Properties

-   [ ] Define properties
-   [ ] Reference `${property.name}`
-   [ ] Centralize versions/configuration
-   [ ] Understand built-in project properties
-   [ ] Understand environment/system property access conceptually
-   [ ] Avoid confusing Maven build properties with Spring runtime
    configuration

Example:

``` xml
<properties>
    <java.version>21</java.version>
</properties>
```

------------------------------------------------------------------------

# Tier 17 --- Profiles

## 33. Maven Profiles

-   [ ] Understand what a Maven profile changes
-   [ ] Define profiles
-   [ ] Activate profiles explicitly
-   [ ] Understand profile-specific dependencies/plugins/properties
-   [ ] Inspect active profiles
-   [ ] Avoid unnecessary environment complexity

### Critical distinction

``` text
Maven Profile
    → changes BUILD configuration

Spring Profile
    → changes APPLICATION/RUNTIME configuration
```

-   [ ] Explain why these are not the same feature

------------------------------------------------------------------------

# Tier 18 --- Multi-Module Projects

## 34. Reactor & Modules

``` text
parent-project/
├── pom.xml
├── domain/
│   └── pom.xml
├── service/
│   └── pom.xml
└── application/
    └── pom.xml
```

-   [ ] Understand aggregator projects
-   [ ] Understand `<modules>`
-   [ ] Understand module build order
-   [ ] Understand inter-module dependencies
-   [ ] Build all modules
-   [ ] Build selected modules when needed
-   [ ] Understand Maven reactor conceptually

## 35. When to Use Multiple Modules

-   [ ] Separate genuinely distinct components
-   [ ] Share controlled build configuration
-   [ ] Avoid splitting small applications prematurely
-   [ ] Recognize multi-module enterprise projects

**Target depth:** Working familiarity. Full-stack developers do not need
to become enterprise Maven architecture specialists.

------------------------------------------------------------------------

# Tier 19 --- Maven Wrapper

## 36. Wrapper Fundamentals

Recognize:

``` text
mvnw
mvnw.cmd
.mvn/
```

-   [ ] Explain Maven Wrapper
-   [ ] Run `./mvnw`
-   [ ] Run `mvnw.cmd` on Windows
-   [ ] Understand reproducible Maven-version usage
-   [ ] Prefer project wrapper when a project provides one
-   [ ] Understand why Spring Initializr projects commonly include it

``` text
System Maven
mvn test

Project Wrapper
./mvnw test
```

------------------------------------------------------------------------

# Tier 20 --- Plugin Configuration

## 37. `<build>` and `<plugins>`

-   [ ] Locate build configuration
-   [ ] Add/configure plugins
-   [ ] Understand plugin versions
-   [ ] Understand executions
-   [ ] Understand phase bindings
-   [ ] Understand plugin configuration inheritance conceptually

## 38. `<pluginManagement>`

-   [ ] Understand plugin management vs active plugins
-   [ ] Centralize plugin versions/configuration
-   [ ] Recognize parent-project usage

## 39. Professional Judgment

-   [ ] Do not add plugins without a concrete build need
-   [ ] Read official plugin documentation
-   [ ] Understand what phase a plugin affects
-   [ ] Avoid copying unexplained POM configuration

------------------------------------------------------------------------

# Tier 21 --- Build Troubleshooting

## 40. Read Maven Output

-   [ ] Find the first meaningful error
-   [ ] Distinguish warning from failure
-   [ ] Identify failing lifecycle phase
-   [ ] Identify failing plugin goal
-   [ ] Read nested Java compiler/test errors
-   [ ] Avoid focusing only on the final generic `BUILD FAILURE`

## 41. Common Failure Categories

### Compilation

-   [ ] Syntax errors
-   [ ] Missing symbols
-   [ ] Wrong Java version
-   [ ] Package mismatch
-   [ ] Missing compile dependency

### Testing

-   [ ] Assertion failure
-   [ ] Test startup error
-   [ ] Missing test dependency
-   [ ] Test discovery problem

### Plugin

-   [ ] Plugin configuration error
-   [ ] Incompatible plugin/version
-   [ ] Goal execution failure

### Packaging

-   [ ] Missing resources
-   [ ] Incorrect artifact configuration

**Checkpoint:** Given a long Maven failure log, identify the actual root
cause rather than reporting only the final Maven exception.

------------------------------------------------------------------------

# Tier 22 --- Dependency Troubleshooting

## 42. Resolution Failures

-   [ ] Artifact not found
-   [ ] Repository unavailable
-   [ ] Incorrect coordinates
-   [ ] Incorrect version
-   [ ] Cached failed resolution awareness
-   [ ] Authentication issues with private repositories conceptually

## 43. Version Conflicts

-   [ ] Inspect dependency tree
-   [ ] Identify competing versions
-   [ ] Understand mediation
-   [ ] Use explicit versions/dependency management where appropriate
-   [ ] Use exclusions carefully
-   [ ] Understand framework-managed dependency compatibility

## 44. Classpath Problems

-   [ ] `ClassNotFoundException`
-   [ ] `NoClassDefFoundError`
-   [ ] `NoSuchMethodError`
-   [ ] Recognize that runtime linkage errors may indicate incompatible
    dependency versions
-   [ ] Trace the responsible artifact through the dependency graph

------------------------------------------------------------------------

# Tier 23 --- Reproducible & Maintainable Builds

## 45. Version Discipline

-   [ ] Pin important plugin versions where appropriate
-   [ ] Use dependency management
-   [ ] Understand reproducibility concerns with changing versions
-   [ ] Treat `SNAPSHOT` dependencies intentionally

## 46. Build Simplicity

-   [ ] Prefer Maven conventions
-   [ ] Minimize custom configuration
-   [ ] Remove unused dependencies/plugins
-   [ ] Keep properties understandable
-   [ ] Avoid unnecessary profiles
-   [ ] Keep build behavior discoverable

## 47. Environment Independence

-   [ ] Avoid machine-specific absolute paths
-   [ ] Use Maven Wrapper
-   [ ] Separate secrets from source-controlled build configuration
-   [ ] Understand differences between build-time and runtime
    configuration

------------------------------------------------------------------------

# Tier 24 --- Security & Dependency Hygiene

## 48. Dependency Security

-   [ ] Understand third-party dependencies as supply-chain inputs
-   [ ] Keep dependencies maintained
-   [ ] Remove unused dependencies
-   [ ] Review dependency updates
-   [ ] Recognize vulnerable dependency reports
-   [ ] Understand transitive vulnerabilities conceptually

## 49. Repository Hygiene

-   [ ] Prefer trusted repositories
-   [ ] Avoid arbitrary third-party repositories
-   [ ] Understand private repository credentials conceptually
-   [ ] Never commit repository credentials/secrets

## 50. Build Integrity Awareness

-   [ ] Understand plugin code executes during builds
-   [ ] Treat build plugins as executable dependencies
-   [ ] Review unfamiliar build configuration before running it in
    sensitive environments

------------------------------------------------------------------------

# Tier 25 --- CI/CD Readiness

> CI/CD should eventually have its own skill tree. Maven only needs to
> prepare the learner for it.

## 51. Non-Interactive Builds

-   [ ] Run builds entirely from command line
-   [ ] Run tests through Maven
-   [ ] Produce artifacts through Maven
-   [ ] Ensure build failure returns failure status
-   [ ] Understand why CI servers can invoke the same commands

## 52. Typical Pipeline Concept

``` text
Checkout
   ↓
Set up JDK
   ↓
./mvnw verify
   ↓
Test reports
   ↓
Packaged artifact
   ↓
Deployment stage
```

-   [ ] Understand why `verify` is useful in CI
-   [ ] Understand artifact handoff
-   [ ] Understand dependency caching conceptually
-   [ ] Leave CI-provider-specific configuration to a CI/CD skill tree

------------------------------------------------------------------------

# Tier 26 --- Spring Boot Readiness

## 53. Maven's Role in Spring Boot

``` text
Maven
├── downloads Spring dependencies
├── manages compatible versions
├── compiles Java
├── runs tests
├── processes resources
└── packages application
```

Spring Boot then supplies application/framework behavior.

## 54. Spring Boot Parent & Dependency Management

-   [ ] Recognize Spring Boot parent POM usage
-   [ ] Understand inherited build/dependency configuration conceptually
-   [ ] Recognize Spring Boot BOM/dependency-management approaches
-   [ ] Understand why many Spring dependency versions can be omitted
-   [ ] Avoid manually overriding managed versions without a reason

## 55. Spring Boot Maven Plugin

-   [ ] Recognize the Spring Boot Maven plugin
-   [ ] Understand executable/repackaged JAR conceptually
-   [ ] Recognize `spring-boot:run`
-   [ ] Understand Maven plugin vs Spring runtime behavior

Example future command:

``` bash
./mvnw spring-boot:run
```

## 56. Layered Architecture Boundary

The following is **application/Spring architecture**, not Maven:

``` text
HTTP Request
     │
     ▼
Controller
     │
     ▼
Service
     │
     ▼
Repository
     │
     ▼
Database
```

Maven's relationship to it is:

``` text
Maven
  │
  ├── provides dependencies needed by each layer
  ├── compiles the classes
  ├── runs their tests
  └── packages the application
```

-   [ ] Explain why `@RestController` is Spring, not Maven
-   [ ] Explain why `@Service` is Spring, not Maven
-   [ ] Explain why `@Repository` is Spring, not Maven
-   [ ] Explain why the architectural separation itself can exist
    without Spring
-   [ ] Explain Maven's build role independently of those layers

------------------------------------------------------------------------

# Tier 27 --- Professional Maven Workflow

## 57. Reading an Existing Project

Given an unfamiliar Maven project:

-   [ ] Locate `pom.xml`
-   [ ] Identify coordinates
-   [ ] Identify Java version
-   [ ] Identify parent POM
-   [ ] Identify dependencies
-   [ ] Identify test dependencies
-   [ ] Identify plugins
-   [ ] Identify profiles
-   [ ] Identify modules
-   [ ] Determine packaging type
-   [ ] Determine how to build/test/run the project

## 58. Modifying a Project

-   [ ] Add a dependency intentionally
-   [ ] Remove a dependency safely
-   [ ] Change a version
-   [ ] Add test dependencies
-   [ ] Configure a necessary plugin
-   [ ] Verify the project still builds
-   [ ] Inspect dependency changes
-   [ ] Review the POM diff

## 59. Diagnosing an Existing Project

-   [ ] Run the appropriate lifecycle phase
-   [ ] Read the actual root error
-   [ ] Inspect dependency tree
-   [ ] Inspect effective POM
-   [ ] Check active profiles
-   [ ] Check Java/Maven versions
-   [ ] Distinguish Maven failures from Java failures
-   [ ] Distinguish Maven failures from Spring failures

------------------------------------------------------------------------

# Practical Progression

## Stage 1 --- Maven Beginner

``` text
Java project
    ↓
pom.xml
    ↓
coordinates
    ↓
standard directories
    ↓
compile
    ↓
test
    ↓
package
```

**Exercise:** Convert a small manually compiled Java project into a
Maven project.

------------------------------------------------------------------------

## Stage 2 --- Dependencies & Testing

``` text
dependencies
    ↓
scopes
    ↓
repositories
    ↓
JUnit
    ↓
Mockito
    ↓
mvn test
```

**Exercise:** Add JUnit and Mockito to a project, write tests, and
execute all tests through Maven.

------------------------------------------------------------------------

## Stage 3 --- Build Understanding

``` text
lifecycle
    ↓
phases
    ↓
plugins
    ↓
goals
    ↓
resources
    ↓
packaging
```

**Exercise:** Explain exactly what occurs during `mvn package` and
identify which plugins perform important steps.

------------------------------------------------------------------------

## Stage 4 --- Dependency Management

``` text
transitive dependencies
        ↓
dependency tree
        ↓
version conflicts
        ↓
dependencyManagement
        ↓
BOM
```

**Exercise:** Inspect a nontrivial dependency tree and trace why several
transitive libraries are present.

------------------------------------------------------------------------

## Stage 5 --- Professional Application Build

``` text
parent POM
    ↓
properties
    ↓
profiles
    ↓
plugins
    ↓
wrapper
    ↓
verify
    ↓
packaged application
```

**Exercise:** Take an existing application POM and explain its build
behavior without relying on copy/paste instructions.

------------------------------------------------------------------------

## Stage 6 --- Spring Boot Bridge

``` text
Maven
  │
  ├── Spring dependencies
  ├── dependency management
  ├── JUnit
  ├── Mockito
  ├── resources
  └── Spring Boot plugin
          │
          ▼
     Spring Boot App
          │
    ┌─────┼──────┐
    ▼     ▼      ▼
Controller Service Repository
```

**Exercise:** Read a generated Spring Boot POM and explain which parts
are Maven, which parts are Spring Boot integration, and what Maven does
when the application is built and tested.

------------------------------------------------------------------------

# Knowledge Depth Scale

  -----------------------------------------------------------------------
  Level                               Meaning
  ----------------------------------- -----------------------------------
  **0 --- Unknown**                   Have not learned the concept

  **1 --- Recognize**                 Can identify it and explain its
                                      basic purpose

  **2 --- Guided**                    Can use it with
                                      documentation/examples

  **3 --- Independent**               Can configure/use it without
                                      step-by-step instructions

  **4 --- Applied**                   Can choose appropriate Maven
                                      behavior in a real project

  **5 --- Professional**              Can diagnose build/dependency
                                      problems and explain tradeoffs
  -----------------------------------------------------------------------

### Target depth for full-stack development

**Levels 4--5:** - POM fundamentals - Dependencies - Scopes - Build
lifecycle - Common commands - JUnit integration - Dependency trees -
Troubleshooting - Maven Wrapper - Spring Boot build fundamentals

**Levels 3--4:** - Plugins - Dependency management - BOMs - Parent
POMs - Properties - Profiles

**Levels 2--3:** - Multi-module architecture - Advanced plugin
executions - Repository administration - Complex enterprise build
structures

------------------------------------------------------------------------

# Highest-Priority Maven Skills for Full-Stack Development

1.  Understand what Maven does and does not do
2.  Read and modify `pom.xml`
3.  Understand `groupId`, `artifactId`, and `version`
4.  Add/remove dependencies
5.  Understand dependency scopes
6.  Understand standard Maven project structure
7.  Understand the build lifecycle
8.  Use `compile`, `test`, `package`, `verify`, and `clean`
9.  Understand plugins vs lifecycle phases
10. Use JUnit and Mockito through Maven
11. Understand transitive dependencies
12. Use `mvn dependency:tree`
13. Understand dependency management and BOMs
14. Use Maven Wrapper
15. Read Maven errors effectively
16. Diagnose dependency/version conflicts
17. Understand parent POMs
18. Understand Spring Boot's Maven integration
19. Produce executable application artifacts
20. Run reproducible command-line builds

------------------------------------------------------------------------

# Lower-Priority / Awareness Topics

These should not block progression into Spring Boot:

-   Writing custom Maven plugins
-   Maven internals
-   Complex repository-manager administration
-   Advanced artifact deployment infrastructure
-   Large enterprise parent-POM hierarchies
-   Complex profile activation systems
-   Advanced Maven Site/reporting
-   Deep reactor optimization
-   Exotic packaging types

------------------------------------------------------------------------

# Final Mastery Check

A full-stack-oriented developer can consider their Maven foundation
professionally useful when they can independently:

-   [ ] Create a Maven Java project
-   [ ] Explain Maven's purpose
-   [ ] Explain the standard project structure
-   [ ] Read a POM
-   [ ] Explain project coordinates
-   [ ] Add and remove dependencies
-   [ ] Choose common dependency scopes
-   [ ] Explain local vs remote repositories
-   [ ] Explain transitive dependencies
-   [ ] Inspect a dependency tree
-   [ ] Diagnose a basic version conflict
-   [ ] Explain lifecycle, phase, plugin, and goal
-   [ ] Predict what `mvn test`, `package`, `verify`, and `install` do
-   [ ] Run JUnit/Mockito tests through Maven
-   [ ] Understand Surefire and Failsafe at an application-developer
    level
-   [ ] Package a JAR
-   [ ] Use Maven Wrapper
-   [ ] Understand properties
-   [ ] Understand profiles and distinguish them from Spring profiles
-   [ ] Understand parent POM inheritance
-   [ ] Understand dependency management and BOMs
-   [ ] Read the effective POM when troubleshooting
-   [ ] Read Maven failure output and find the underlying
    Java/test/plugin error
-   [ ] Explain Maven's role in a Spring Boot project
-   [ ] Explain why Controller → Service → Repository is application
    architecture rather than Maven functionality
-   [ ] Build and test a Spring Boot project from the command line
-   [ ] Modify an unfamiliar POM without blindly copying configuration

------------------------------------------------------------------------

# Relationship to Other Skill Trees

``` text
                         Java
                           │
          ┌────────────────┼────────────────┐
          ▼                ▼                ▼
        Maven            JUnit        Application Design
          │                │                │
          │                ▼                │
          │             Mockito             │
          │                │                │
          └────────┬───────┴────────────────┘
                   ▼
              Spring Boot
                   │
          ┌────────┼────────┐
          ▼        ▼        ▼
     Controller  Service  Repository
          │                   │
          │              ┌────┴─────┐
          │              ▼          ▼
          │         PostgreSQL   MongoDB
          │
          ▼
    Angular / React

Additional separate trees:
├── Git
├── Docker
├── CI/CD
└── Deployment / Cloud
```

The central Maven mastery question is:

> **Can I look at a Java project's build configuration, understand where
> its dependencies come from, understand what happens when it
> builds/tests/packages, and diagnose the build when something goes
> wrong?**

For a full-stack Java developer, that capability matters far more than
memorizing every Maven plugin or XML option.
