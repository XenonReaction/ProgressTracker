# Spring Boot & Spring Initializr Skill Tree — Full-Stack Java Developer Path

> **Goal:** Progress from Java/Maven/HTTP knowledge to independently creating, configuring, testing, debugging, and explaining a modern Spring Boot REST backend.

> **Target:** Professional full-stack developer competency; advanced specialist topics are awareness-level unless needed for application development.

## Dependency / prerequisite map

```text
Java + Maven + HTTP/REST + JUnit/Mockito
                  ↓
          Spring fundamentals
                  ↓
     Spring Initializr / Boot setup
                  ↓
 Controller → Service → Repository
                  ↓
      Production-ready REST backend
                  ↓
 JPA / Spring Data / Security branches
```

## Skill Tree Overview

### 1. Spring Framework vs Spring Boot
- [ ] Spring Framework purpose
- [ ] Spring Boot purpose
- [ ] Convention over configuration
- [ ] Auto-configuration
- [ ] Starter dependencies
- [ ] Embedded server concept
- [ ] Know what Boot adds rather than treating Spring and Boot as synonyms

### 2. IoC & Dependency Injection
- [ ] Inversion of Control
- [ ] Dependency Injection
- [ ] Dependency vs collaborator
- [ ] Constructor injection as default
- [ ] Why DI improves substitution/testing
- [ ] Avoid field injection as normal modern practice

### 3. Beans & ApplicationContext
- [ ] Spring bean
- [ ] ApplicationContext
- [ ] Bean registration
- [ ] Bean retrieval/resolution concept
- [ ] Bean scopes awareness
- [ ] Singleton default and concurrency implications
- [ ] Bean lifecycle awareness

### 4. Component Scanning & Stereotypes
- [ ] @Component
- [ ] @Service
- [ ] @Repository
- [ ] @Controller
- [ ] @RestController
- [ ] Component scanning
- [ ] Package placement and scan boundaries
- [ ] Stereotypes communicate architectural intent in addition to bean registration

### 5. @SpringBootApplication
- [ ] Entry-point class
- [ ] @SpringBootApplication composition concept
- [ ] Application startup
- [ ] SpringApplication.run()
- [ ] Why package placement matters

### 6. Spring Initializr Website
- [ ] Use start.spring.io conceptually/website UI
- [ ] Choose Maven
- [ ] Choose Java
- [ ] Choose appropriate current Spring Boot line
- [ ] Group, artifact, name, description, package name
- [ ] Packaging
- [ ] Java version
- [ ] Select dependencies intentionally
- [ ] Generate/download/open project
- [ ] Understand generated files rather than blindly accepting them

### 7. Generated Project Structure
- [ ] pom.xml
- [ ] src/main/java
- [ ] src/main/resources
- [ ] src/test/java
- [ ] application.properties/application.yml
- [ ] Main application class
- [ ] Maven Wrapper
- [ ] Static/templates directories awareness

### 8. Spring Boot + Maven
- [ ] Spring Boot parent/BOM awareness
- [ ] Starters
- [ ] Dependency management
- [ ] spring-boot-maven-plugin
- [ ] mvn spring-boot:run
- [ ] mvn test
- [ ] mvn package
- [ ] Executable JAR concept
- [ ] Read dependency/startup failures

### 9. Configuration Fundamentals
- [ ] application.properties
- [ ] application.yml
- [ ] Property keys
- [ ] Externalized configuration
- [ ] Environment variables
- [ ] ${VARIABLE:default} placeholders
- [ ] Command-line/config sources awareness
- [ ] Configuration precedence at practical level
- [ ] Do not commit secrets

### 10. Profiles & Environments
- [ ] @Profile awareness
- [ ] spring.profiles.active
- [ ] application-{profile}.yml/properties
- [ ] local/dev/test/prod configurations
- [ ] Override common configuration cleanly
- [ ] Avoid duplicating entire configuration files unnecessarily

### 11. Typed Configuration
- [ ] @Configuration
- [ ] @Bean
- [ ] @ConfigurationProperties
- [ ] Configuration-properties scanning/enabling
- [ ] Validation awareness
- [ ] @Value for small/simple cases
- [ ] Prefer grouped typed configuration for application settings
- [ ] Understand how environment/config values become bean properties

### 12. REST Controllers
- [ ] @RestController
- [ ] @RequestMapping
- [ ] @GetMapping
- [ ] @PostMapping
- [ ] @PutMapping
- [ ] @PatchMapping
- [ ] @DeleteMapping
- [ ] @PathVariable
- [ ] @RequestParam
- [ ] @RequestBody
- [ ] ResponseEntity
- [ ] Map known HTTP semantics into Spring code

### 13. JSON Serialization — Introduction
- [ ] Spring MVC converts request JSON to Java objects
- [ ] Spring MVC converts Java response objects to JSON
- [ ] Jackson is commonly involved
- [ ] Basic property/record mapping awareness
- [ ] Do not go deeply into custom serialization in this tree

### 14. DTOs & API Boundaries
- [ ] Request DTOs
- [ ] Response DTOs
- [ ] Records/classes for DTOs
- [ ] Do not expose persistence entities automatically
- [ ] Map between API and domain/persistence representations
- [ ] Keep API contracts deliberate

### 15. Validation
- [ ] Jakarta Bean Validation integration
- [ ] @Valid
- [ ] @NotBlank
- [ ] @Size
- [ ] @Min/@Max awareness
- [ ] Validation on request DTOs
- [ ] Understand binding vs validation errors
- [ ] Business validation belongs in appropriate service/domain logic

### 16. Controller → Service → Repository Architecture
- [ ] Controller handles HTTP boundary
- [ ] Service handles business/workflow logic
- [ ] Repository is persistence boundary
- [ ] Dependency direction
- [ ] Keep SQL/JPA details out of controllers
- [ ] Keep HTTP details out of repositories
- [ ] Understand architecture can exist without Spring

### 17. Service Layer
- [ ] @Service
- [ ] Constructor injection
- [ ] Business rules
- [ ] Orchestration
- [ ] Stateless service design
- [ ] Transaction-boundary awareness without deep JPA coverage
- [ ] Custom domain/application exceptions

### 18. Repository Integration — High Level
- [ ] @Repository meaning
- [ ] Inject repository abstractions
- [ ] DataSource awareness
- [ ] JDBC option
- [ ] JPA/Spring Data option
- [ ] Leave detailed persistence mechanics to JDBC and ORM/JPA/Hibernate/Spring Data trees

### 19. Exception Handling
- [ ] @ExceptionHandler
- [ ] @ControllerAdvice / @RestControllerAdvice
- [ ] Map application failures to HTTP status
- [ ] 400/404/409/500 examples
- [ ] Consistent error response DTO
- [ ] Do not expose stack traces/internal details to clients

### 20. CORS & Web Configuration
- [ ] Recall same-origin/CORS prerequisite
- [ ] Configure allowed origins/methods/headers
- [ ] WebMvcConfigurer awareness
- [ ] Controller-level vs global configuration awareness
- [ ] Avoid “allow everything” production defaults

### 21. Testing with JUnit & Mockito
- [ ] JUnit/Mockito as prerequisites
- [ ] Unit-test services without starting Spring when possible
- [ ] Mock repository collaborators
- [ ] Spring context tests awareness
- [ ] @SpringBootTest
- [ ] Web-layer slice testing awareness
- [ ] MockMvc awareness
- [ ] Mock/bean override mechanisms should follow current Spring guidance
- [ ] Test profiles/configuration
- [ ] Keep Spring-specific testing focused here; deeper testing can become its own tree

### 22. Logging
- [ ] SLF4J facade concept
- [ ] Common logging levels
- [ ] Parameterized logging
- [ ] Useful contextual logging
- [ ] Avoid System.out for application logging
- [ ] Do not log passwords/tokens/secrets
- [ ] Configure levels by environment
- [ ] Stack traces when appropriate

### 23. Actuator & Operational Visibility
- [ ] What Spring Boot Actuator is
- [ ] Why health/metrics/management endpoints exist
- [ ] Health endpoint
- [ ] Info endpoint awareness
- [ ] Metrics awareness
- [ ] Endpoint exposure/configuration
- [ ] Do not expose sensitive management endpoints indiscriminately
- [ ] Readiness/liveness awareness

### 24. Developer Workflow
- [ ] Run from Maven/IDE
- [ ] DevTools purpose
- [ ] Automatic restart awareness
- [ ] Read startup logs
- [ ] Recognize port conflicts
- [ ] Use debugger/breakpoints
- [ ] Make small config/code changes and verify behavior
- [ ] Understand application shutdown/restart

### 25. Auto-Configuration Reasoning
- [ ] Understand conditional configuration conceptually
- [ ] Boot configures common infrastructure based on classpath/properties/beans
- [ ] User-defined beans can influence/replace defaults
- [ ] Read condition/startup diagnostics when needed
- [ ] Avoid memorizing every auto-configuration class

### 26. Error Diagnosis
- [ ] Bean not found
- [ ] Multiple bean candidates
- [ ] Circular dependency awareness
- [ ] Component scan/package mismatch
- [ ] Configuration property binding failure
- [ ] Port already in use
- [ ] Missing dependency
- [ ] HTTP 400/404/405/415/500
- [ ] Read root cause rather than final Maven exit-code line

### 27. Production Configuration Awareness
- [ ] Environment-specific configuration
- [ ] Secrets externalization
- [ ] Graceful shutdown awareness
- [ ] Health checks
- [ ] Logging/observability
- [ ] Server/proxy awareness
- [ ] HTTPS commonly terminates outside application awareness
- [ ] Do not make Docker/deployment a major branch here

### 28. Modern Spring Practices
- [ ] Constructor injection
- [ ] Records/immutable DTOs where appropriate
- [ ] Typed configuration
- [ ] Explicit API boundaries
- [ ] Modern Java baseline
- [ ] Prefer current supported Spring APIs
- [ ] Recognize older XML/field-injection patterns without centering curriculum on them

### 29. Boundaries to Separate Trees
- [ ] Spring Security → separate tree
- [ ] ORM/JPA/Hibernate → separate tree
- [ ] Spring Data JPA → separate tree
- [ ] AOP → separate tree
- [ ] Docker → separate tree
- [ ] Deployment/CI-CD → separate trees
- [ ] WebFlux/reactive programming → later specialization
- [ ] Spring Cloud/Batch/Integration/messaging → later specialization

## Practical competency checkpoints

- [ ] Use Spring Initializr website to create a Maven/Java REST project intentionally
- [ ] Explain how @SpringBootApplication leads to bean discovery/startup at a practical level
- [ ] Explain constructor injection from bean registration through dependency resolution
- [ ] Bind an environment variable into typed configuration
- [ ] Build GET/POST/PUT/PATCH/DELETE endpoints
- [ ] Validate request DTOs
- [ ] Implement Controller → Service → Repository separation
- [ ] Create consistent exception responses
- [ ] Configure CORS intentionally
- [ ] Write service unit tests with JUnit/Mockito and a Spring web/context test
- [ ] Use Actuator health information
- [ ] Read startup logs and diagnose a missing-bean/configuration error
- [ ] Package and run an executable Spring Boot JAR

## Mastery standard

> Can I build, explain, debug, and make appropriate design choices in this domain without relying on a step-by-step tutorial, while recognizing what adjacent frameworks or abstractions are doing on my behalf?
