# Mockito Skill Tree --- Full-Stack Java Developer Path

> **Goal:** Progress from competent JUnit testing to professional
> Mockito usage for isolating Java units, with deliberate preparation
> for Spring Boot service and application testing.
>
> **Learning dependency:**
> `Java Fundamentals → JUnit → Mockito → Spring Boot Testing`
>
> **Important:** Mockito does not technically require JUnit. JUnit is a
> prerequisite here because it creates a clearer professional learning
> progression.
>
> **Scope boundary:** This tree focuses on Mockito in plain Java/JUnit
> tests. Spring-specific testing mechanisms belong in a later Spring
> Boot Testing skill tree.

------------------------------------------------------------------------

# Dependency Map

``` text
Java
├── Classes & Objects
├── Interfaces
├── Constructors
├── Polymorphism
├── Exceptions
├── Lambdas
└── Dependency Relationships
        │
        ▼
      JUnit
├── @Test
├── Assertions
├── Lifecycle
├── Test Isolation
└── Test Design
        │
        ▼
      Mockito
        │
        ▼
Spring Boot Testing
```

# Tier 0 --- Prerequisites

## 1. Required Java Knowledge

-   [ ] Classes and objects
-   [ ] Constructors
-   [ ] Interfaces
-   [ ] Inheritance and polymorphism
-   [ ] Access modifiers
-   [ ] Exceptions
-   [ ] Generics basics
-   [ ] Collections basics
-   [ ] Lambdas basics
-   [ ] Understand object references
-   [ ] Understand dependency relationships between classes

## 2. Required JUnit Knowledge

-   [ ] `@Test`
-   [ ] Core assertions
-   [ ] Arrange / Act / Assert
-   [ ] Test lifecycle basics
-   [ ] Exception testing
-   [ ] Unit-test isolation
-   [ ] Deterministic tests
-   [ ] Maven test execution
-   [ ] Debugging failed tests

**Checkpoint:** Write good JUnit tests for a class that has no external
collaborators before introducing Mockito.

------------------------------------------------------------------------

# Tier 1 --- Why Mockito Exists

## 3. Dependencies & Isolation

Consider:

``` java
class MessageService {
    private final MessageRepository repository;

    MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    Message create(Message message) {
        return repository.save(message);
    }
}
```

To unit-test `MessageService`, you may want to test the service's
behavior without connecting to a real database.

-   [ ] Identify the system under test
-   [ ] Identify its collaborators/dependencies
-   [ ] Understand why real dependencies can make unit tests slow or
    difficult
-   [ ] Understand isolation
-   [ ] Understand dependency substitution
-   [ ] Understand why constructor injection makes substitution
    straightforward
-   [ ] Know that Mockito creates controlled substitutes for
    dependencies
-   [ ] Understand that Mockito does not replace integration testing

## 4. Test Doubles

Learn the vocabulary before learning annotations:

### Dummy

-   [ ] Understand a value/object supplied but not actually used

### Stub

-   [ ] Understand a controlled object that returns predetermined
    results

### Fake

-   [ ] Understand a lightweight working implementation, such as an
    in-memory repository

### Mock

-   [ ] Understand a configurable test double
-   [ ] Understand interaction verification

### Spy

-   [ ] Understand wrapping/partially mocking a real object
-   [ ] Recognize the additional risks of partial mocking

### Judgment

-   [ ] Distinguish mock vs stub behavior conceptually
-   [ ] Understand that terminology can vary between testing literature
    and libraries
-   [ ] Choose the simplest useful test double

**Checkpoint:** Given several dependencies, explain which ones you would
use for real, fake, stub, or mock and why.

------------------------------------------------------------------------

# Tier 2 --- Creating Mocks

## 5. `mock()`

``` java
MessageRepository repository =
    Mockito.mock(MessageRepository.class);
```

-   [ ] Create a mock with `mock()`
-   [ ] Understand that the mock is not the real implementation
-   [ ] Understand default mock return values
-   [ ] Pass mocks through constructors
-   [ ] Use mocks without Spring
-   [ ] Use mocks inside ordinary JUnit tests

## 6. Basic Stubbing

``` java
when(repository.findById(1L))
    .thenReturn(Optional.of(message));
```

-   [ ] `when()`
-   [ ] `thenReturn()`
-   [ ] Stub return values
-   [ ] Stub different methods
-   [ ] Understand that unstubbed methods use Mockito defaults
-   [ ] Understand that stubbing configures behavior; it does not assert
    behavior

## 7. Multiple/Sequential Results

-   [ ] Consecutive `thenReturn()` values
-   [ ] Understand repeated invocations
-   [ ] Use sequential behavior only when it represents meaningful
    behavior

------------------------------------------------------------------------

# Tier 3 --- Verification

## 8. `verify()`

``` java
verify(repository).save(message);
```

-   [ ] Verify a method was called
-   [ ] Verify arguments
-   [ ] Understand default one-time verification
-   [ ] Distinguish verification from JUnit assertions
-   [ ] Know when state assertions are preferable to interaction
    verification

## 9. Verification Modes

-   [ ] `times(n)`
-   [ ] `never()`
-   [ ] `atLeastOnce()`
-   [ ] `atLeast(n)`
-   [ ] `atMost(n)`
-   [ ] Understand why exact call counts can over-couple tests to
    implementation

## 10. No-More-Interaction Checks

-   [ ] `verifyNoInteractions()`
-   [ ] `verifyNoMoreInteractions()`
-   [ ] Know when these checks are useful
-   [ ] Avoid using them mechanically

## 11. Invocation Order

-   [ ] Understand `InOrder`
-   [ ] Verify order only when order is part of required behavior
-   [ ] Avoid coupling tests to irrelevant implementation sequence

**Checkpoint:** Test a service that conditionally calls a repository and
verify both the returned result and meaningful collaborator
interactions.

------------------------------------------------------------------------

# Tier 4 --- Argument Matching

## 12. Matchers

-   [ ] `any()`
-   [ ] `anyString()`
-   [ ] `anyInt()` and related primitive matchers
-   [ ] `eq()`
-   [ ] `isNull()`
-   [ ] `notNull()`
-   [ ] `argThat()`

## 13. Matcher Rules

-   [ ] Understand matcher consistency within an invocation
-   [ ] Know when exact values are clearer than broad matchers
-   [ ] Avoid `any()` everywhere
-   [ ] Use custom predicates only when they improve the test

## 14. ArgumentCaptor

``` java
ArgumentCaptor<Message> captor =
    ArgumentCaptor.forClass(Message.class);

verify(repository).save(captor.capture());

Message saved = captor.getValue();
```

-   [ ] Create an `ArgumentCaptor`
-   [ ] Capture method arguments
-   [ ] Inspect captured values
-   [ ] Assert transformed/generated data
-   [ ] Capture multiple values when needed
-   [ ] Know when a normal equality verification is simpler

**Checkpoint:** Verify that a service constructs the correct object
before passing it to its repository.

------------------------------------------------------------------------

# Tier 5 --- Mockito with JUnit

## 15. `MockitoExtension`

``` java
@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
}
```

-   [ ] Understand `MockitoExtension`
-   [ ] Understand that it integrates Mockito with JUnit Jupiter
-   [ ] Understand `@ExtendWith`
-   [ ] Distinguish JUnit responsibility from Mockito responsibility

## 16. `@Mock`

``` java
@Mock
MessageRepository repository;
```

-   [ ] Declare mocks with `@Mock`
-   [ ] Understand annotation initialization
-   [ ] Compare `@Mock` with `mock()`
-   [ ] Choose the clearer style for the test

## 17. `@InjectMocks`

``` java
@InjectMocks
MessageService service;
```

-   [ ] Understand what `@InjectMocks` attempts to do
-   [ ] Understand constructor-based injection
-   [ ] Recognize limitations
-   [ ] Know that `@InjectMocks` is not Spring dependency injection
-   [ ] Be able to instantiate the system under test manually when
    clearer

### Critical distinction

``` text
@InjectMocks
    │
    └── Mockito assembles an ordinary Java object

Spring dependency injection
    │
    └── Spring manages objects in an application context
```

These are related ideas but different mechanisms.

------------------------------------------------------------------------

# Tier 6 --- Exceptions & Void Methods

## 18. Throwing Exceptions

``` java
when(repository.findById(1L))
    .thenThrow(new RuntimeException("failure"));
```

-   [ ] `thenThrow()`
-   [ ] Simulate collaborator failures
-   [ ] Test error-handling behavior
-   [ ] Combine Mockito stubbing with JUnit `assertThrows()`

## 19. Void Methods

-   [ ] Understand why `when(...).thenReturn(...)` does not apply to
    `void`
-   [ ] `doNothing()`
-   [ ] `doThrow()`
-   [ ] `doAnswer()`
-   [ ] Know when no stubbing is required

## 20. `doReturn()` Family

-   [ ] `doReturn()`
-   [ ] `doThrow()`
-   [ ] `doAnswer()`
-   [ ] Understand why this syntax is particularly relevant to
    spies/void methods
-   [ ] Prefer ordinary `when()` syntax when it is clearer and safe

------------------------------------------------------------------------

# Tier 7 --- Answers & Custom Behavior

## 21. `thenAnswer()`

-   [ ] Understand `Answer`
-   [ ] Inspect invocation arguments
-   [ ] Generate dynamic responses
-   [ ] Return values based on inputs
-   [ ] Avoid recreating complicated production logic in mock
    configuration

Example concept:

``` java
when(repository.save(any(Message.class)))
    .thenAnswer(invocation -> invocation.getArgument(0));
```

## 22. Callback-Like Behavior

-   [ ] Simulate collaborator behavior based on arguments
-   [ ] Simulate failures conditionally
-   [ ] Keep custom answers small and obvious
-   [ ] Consider a fake implementation when mock behavior becomes
    complicated

------------------------------------------------------------------------

# Tier 8 --- Spies & Partial Mocking

## 23. Spies

-   [ ] Understand `spy()`
-   [ ] Understand that a spy wraps real behavior
-   [ ] Recognize that real methods may execute
-   [ ] Stub selected behavior
-   [ ] Verify interactions on spies

## 24. Safe Spy Stubbing

-   [ ] Understand why `doReturn(...).when(spy)...` may be safer
-   [ ] Recognize accidental real-method execution
-   [ ] Avoid spies when straightforward dependency design would be
    cleaner

## 25. Partial-Mock Judgment

-   [ ] Recognize a spy as a possible design smell
-   [ ] Consider refactoring responsibilities
-   [ ] Use partial mocking intentionally rather than by default

------------------------------------------------------------------------

# Tier 9 --- Mocking Design Decisions

## 26. What Should Be Mocked?

Good candidates often include boundaries such as: - \[ \]
Repository/data-access dependency in a service unit test - \[ \]
External API client - \[ \] Email/SMS sender - \[ \] Payment gateway
abstraction - \[ \] Clock/time provider when time is behaviorally
important - \[ \] Expensive or nondeterministic collaborator

## 27. What Usually Should NOT Be Mocked?

-   [ ] Simple value objects
-   [ ] DTOs
-   [ ] Records
-   [ ] Collections
-   [ ] Strings
-   [ ] The class being tested
-   [ ] Trivial domain objects
-   [ ] Everything merely because mocking is available

## 28. Mocking Boundaries

``` text
Unit under test
      │
      ├── internal calculation → REAL
      │
      ├── value object         → REAL
      │
      ├── domain model         → usually REAL
      │
      ├── repository boundary  → MOCK/FAKE
      │
      └── external API         → MOCK/FAKE
```

-   [ ] Identify architectural boundaries
-   [ ] Mock dependencies rather than implementation details
-   [ ] Keep the system under test real
-   [ ] Understand when a fake is easier to maintain than extensive
    stubbing

------------------------------------------------------------------------

# Tier 10 --- Avoiding Over-Mocking

## 29. Recognize Over-Mocking

Warning signs: - \[ \] Every object in the test is mocked - \[ \] Test
setup is larger than the production method - \[ \] Minor refactoring
breaks many tests - \[ \] Tests verify dozens of internal calls - \[ \]
Mock configuration duplicates production logic - \[ \] Tests pass while
real components cannot integrate - \[ \] Private implementation
structure effectively becomes part of the test contract

## 30. Behavior vs Implementation

Prefer:

``` text
Given valid input
When the service creates a message
Then the expected result is returned
And the repository receives the required persisted data
```

over:

``` text
verify method A
verify method B
verify method C
verify temporary internal sequence
verify every getter
```

-   [ ] Test observable behavior
-   [ ] Verify only meaningful interactions
-   [ ] Avoid brittle implementation-detail verification
-   [ ] Allow safe refactoring without rewriting unrelated tests

## 31. Know When to Use a Real Object

-   [ ] Real object is simple and deterministic
-   [ ] Construction is cheap
-   [ ] No undesirable external side effects
-   [ ] Real behavior makes the test clearer

## 32. Know When the Test Should Be Integration-Level Instead

-   [ ] Real database behavior is the subject
-   [ ] Serialization/configuration is the subject
-   [ ] Framework wiring is the subject
-   [ ] Multiple components must be verified together
-   [ ] The mock would hide the behavior you actually need confidence in

**Checkpoint:** Review an over-mocked test and simplify it by replacing
unnecessary mocks with real objects.

------------------------------------------------------------------------

# Tier 11 --- Service-Layer Testing

## 33. Plain-Java Service Test

Production structure:

``` text
MessageService
      │
      ▼
MessageRepository
```

Unit-test structure:

``` text
REAL MessageService
      │
      ▼
MOCK MessageRepository
```

-   [ ] Instantiate the real service
-   [ ] Mock repository dependency
-   [ ] Stub repository responses
-   [ ] Execute business logic
-   [ ] Assert returned state with JUnit
-   [ ] Verify meaningful repository interactions with Mockito
-   [ ] Test repository failure behavior
-   [ ] Test business-rule rejection behavior
-   [ ] Verify a repository is not called when validation/business rules
    reject work

## 34. Arrange / Act / Assert with Mockito

``` java
@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    MessageRepository repository;

    @InjectMocks
    MessageService service;

    @Test
    void createsMessage() {
        // Arrange
        when(repository.save(any(Message.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Message result = service.create(...);

        // Assert (JUnit)
        assertEquals(..., result.getName());

        // Verify interaction (Mockito)
        verify(repository).save(any(Message.class));
    }
}
```

-   [ ] Keep JUnit assertions and Mockito verification conceptually
    distinct
-   [ ] Use stubbing in Arrange
-   [ ] Call real production behavior in Act
-   [ ] Use assertions/verifications in Assert

------------------------------------------------------------------------

# Tier 12 --- Advanced Mockito Awareness

## 35. Strictness

-   [ ] Understand unnecessary stubbing warnings
-   [ ] Understand strict stubbing conceptually
-   [ ] Remove unused stubs
-   [ ] Keep test setup relevant to each test
-   [ ] Use leniency only intentionally

## 36. Mocking Difficult APIs

-   [ ] Understand that static/final mocking capabilities exist
-   [ ] Recognize static mocking as a specialized tool
-   [ ] Understand construction mocking conceptually
-   [ ] Prefer redesign/injection when it produces clearer application
    architecture
-   [ ] Avoid advanced mocking as the first solution

## 37. Generic Types

-   [ ] Mock generic interfaces
-   [ ] Understand type inference with matchers
-   [ ] Recognize unchecked warnings
-   [ ] Avoid unsafe casts where possible

## 38. Async Behavior

-   [ ] Stub `Future`/`CompletableFuture` results where appropriate
-   [ ] Test asynchronous service behavior deterministically
-   [ ] Avoid timing-based sleeps
-   [ ] Understand that concurrency correctness may require more than
    mocking

------------------------------------------------------------------------

# Tier 13 --- Professional Mockito Workflow

## 39. Readability

-   [ ] Keep mock names meaningful
-   [ ] Keep stubbing near the behavior that requires it
-   [ ] Avoid giant shared setup blocks
-   [ ] Make expected collaborator behavior obvious
-   [ ] Prefer minimal stubbing

## 40. Maintainability

-   [ ] Do not duplicate implementation logic
-   [ ] Avoid overspecified verification
-   [ ] Refactor test helpers when repetition obscures intent
-   [ ] Delete obsolete stubs
-   [ ] Treat test code as production-quality code

## 41. Debugging Mockito Tests

-   [ ] Recognize unstubbed default returns
-   [ ] Diagnose argument-matcher failures
-   [ ] Diagnose wanted-but-not-invoked verification failures
-   [ ] Diagnose too-many-invocations failures
-   [ ] Diagnose unnecessary stubbing
-   [ ] Inspect actual captured arguments
-   [ ] Determine whether the test or production behavior is incorrect

------------------------------------------------------------------------

# Tier 14 --- Spring Boot Readiness

## 42. Understand the Boundary

Mockito:

``` text
Creates/configures test doubles
and verifies interactions
```

JUnit:

``` text
Discovers/runs tests
and provides assertions/lifecycle
```

Spring:

``` text
Creates/manages the application context,
dependency injection, configuration,
web/data infrastructure, etc.
```

-   [ ] Explain the responsibility of each tool
-   [ ] Understand that Mockito works without Spring
-   [ ] Understand that JUnit works without Spring
-   [ ] Understand that Spring can integrate both
-   [ ] Avoid starting Spring for a test that only needs plain Java +
    Mockito

## 43. Prepare for Spring Service Tests

``` text
Spring application architecture

Controller
    │
    ▼
Service
    │
    ▼
Repository
```

Possible service unit test:

``` text
JUnit executes test
       │
       ▼
REAL Service
       │
       ▼
Mockito MOCK Repository

No Spring context required
```

-   [ ] Recognize this as a normal unit-testing strategy
-   [ ] Understand why it is fast
-   [ ] Understand what it does NOT verify: Spring wiring, persistence
    mappings, actual SQL, etc.

## 44. Recognize Future Spring Tests

The later Spring Boot Testing tree can introduce: - \[ \] Spring Test -
\[ \] `@SpringBootTest` - \[ \] `@WebMvcTest` - \[ \] `@DataJpaTest` -
\[ \] `MockMvc` - \[ \] Application-context testing - \[ \] Test
slices - \[ \] Spring-managed mock replacement mechanisms - \[ \]
Repository integration testing - \[ \] Controller integration/slice
testing - \[ \] Full application integration testing

Do not confuse those features with Mockito itself.

------------------------------------------------------------------------

# Professional Mastery Check

Before progressing into Spring Boot testing, you should independently be
able to:

-   [ ] Explain why Mockito exists
-   [ ] Explain mock, stub, fake, dummy, and spy
-   [ ] Create mocks manually
-   [ ] Stub return values
-   [ ] Stub exceptions
-   [ ] Verify meaningful interactions
-   [ ] Use argument matchers correctly
-   [ ] Capture and inspect arguments
-   [ ] Use `MockitoExtension`
-   [ ] Use `@Mock`
-   [ ] Use `@InjectMocks`
-   [ ] Explain why `@InjectMocks` is not Spring dependency injection
-   [ ] Test void methods/failures
-   [ ] Use custom answers when justified
-   [ ] Use spies cautiously
-   [ ] Recognize over-mocking
-   [ ] Decide when a real object is better
-   [ ] Decide when a fake is better
-   [ ] Decide when an integration test is actually required
-   [ ] Unit-test a service/repository relationship without Spring
-   [ ] Use JUnit assertions and Mockito verification together without
    confusing their roles
-   [ ] Debug common Mockito failures
-   [ ] Write maintainable tests that survive reasonable refactoring

------------------------------------------------------------------------

# Final Learning Progression

``` text
JAVA
│
├── syntax
├── methods
├── classes
├── interfaces
├── exceptions
├── collections
└── dependency relationships
        │
        ▼
JUNIT
│
├── automated tests
├── assertions
├── lifecycle
├── parameterization
├── isolation
└── test design
        │
        ▼
MOCKITO  ← YOU ARE HERE
│
├── test doubles
├── stubbing
├── verification
├── argument matching
├── dependency isolation
└── mocking judgment
        │
        ▼
SPRING BOOT TESTING
│
├── plain service unit tests
├── Spring context tests
├── controller tests
├── repository tests
├── test slices
└── application integration tests
```

The goal is not to **mock everything**. The goal is to know enough Java,
JUnit, and Mockito to deliberately decide **what should be tested, what
should be real, what should be replaced, and whether Spring needs to
participate in the test at all**.
