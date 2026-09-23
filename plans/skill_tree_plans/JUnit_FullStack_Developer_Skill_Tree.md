# JUnit Skill Tree --- Full-Stack Java Developer Path

> **Goal:** Progress from basic Java knowledge to professional use of
> modern JUnit for automated Java testing, with deliberate preparation
> for Spring Boot testing.
>
> **Primary focus:** JUnit Jupiter / JUnit 5+ style testing.
>
> **Learning dependency:**
> `Java Fundamentals → JUnit → Mockito → Spring Boot Testing`
>
> **Scope boundary:** This tree teaches JUnit and general unit-testing
> practices needed to use it well. Mockito and Spring-specific testing
> are separate skill trees.

------------------------------------------------------------------------

# Dependency Map

``` text
Java Fundamentals
├── Methods
├── Classes & Objects
├── Interfaces
├── Exceptions
├── Basic Collections
├── Basic Lambdas
└── Maven Basics
        │
        ▼
      JUnit
        │
        ▼
     Mockito
        │
        ▼
Spring Boot Testing
```

# Tier 0 --- Prerequisite Java

## 1. Required Java Skills

-   [ ] Variables, expressions, conditions, and loops
-   [ ] Methods, parameters, arguments, and return values
-   [ ] Classes and objects
-   [ ] Constructors
-   [ ] Instance vs static members
-   [ ] Access modifiers
-   [ ] Interfaces
-   [ ] Basic inheritance and polymorphism
-   [ ] Exceptions
-   [ ] `List`, `Set`, and `Map` basics
-   [ ] Basic lambda syntax
-   [ ] Packages and imports
-   [ ] Maven dependency basics

**Checkpoint:** Create a small multi-class Java/Maven application and
explain the behavior you would want to verify automatically.

------------------------------------------------------------------------

# Tier 1 --- Testing Fundamentals

## 2. Why Automated Tests Exist

-   [ ] Explain what an automated test is
-   [ ] Explain why tests improve confidence during change
-   [ ] Distinguish production code from test code
-   [ ] Understand regression testing
-   [ ] Understand that passing tests increase confidence but do not
    prove absence of bugs
-   [ ] Understand repeatability and deterministic testing

## 3. Types of Tests

-   [ ] Unit test
-   [ ] Integration test
-   [ ] End-to-end test
-   [ ] Understand test scope
-   [ ] Understand isolation
-   [ ] Recognize that JUnit can execute more than just unit tests
-   [ ] Know that Spring-specific integration testing belongs later

## 4. Test Anatomy

-   [ ] Arrange
-   [ ] Act
-   [ ] Assert
-   [ ] Identify the system under test
-   [ ] Identify inputs
-   [ ] Identify expected behavior
-   [ ] Distinguish state verification from interaction verification
-   [ ] Give tests behavior-oriented names

``` java
@Test
void additionReturnsExpectedResult() {
    // Arrange
    Calculator calculator = new Calculator();

    // Act
    int result = calculator.add(2, 3);

    // Assert
    assertEquals(5, result);
}
```

**Checkpoint:** Given a Java method, describe its important test cases
before writing JUnit code.

------------------------------------------------------------------------

# Tier 2 --- First JUnit Tests

## 5. JUnit Project Setup

-   [ ] Understand JUnit Platform conceptually
-   [ ] Understand JUnit Jupiter conceptually
-   [ ] Recognize JUnit Vintage as legacy compatibility
-   [ ] Add JUnit to a Maven project
-   [ ] Understand test dependency scope
-   [ ] Understand conventional `src/test/java`
-   [ ] Run tests from an IDE
-   [ ] Run tests with Maven
-   [ ] Interpret passing, failing, and errored tests

## 6. `@Test`

-   [ ] Import `org.junit.jupiter.api.Test`
-   [ ] Mark methods with `@Test`
-   [ ] Understand test discovery
-   [ ] Write independent test methods
-   [ ] Understand why test execution order normally should not matter

## 7. Core Assertions

-   [ ] `assertEquals()`
-   [ ] `assertNotEquals()`
-   [ ] `assertTrue()`
-   [ ] `assertFalse()`
-   [ ] `assertNull()`
-   [ ] `assertNotNull()`
-   [ ] `assertSame()`
-   [ ] `assertNotSame()`
-   [ ] `assertArrayEquals()`
-   [ ] Understand expected vs actual values
-   [ ] Add useful assertion messages
-   [ ] Choose assertions that clearly communicate intent

**Checkpoint:** Test a plain Java class with multiple methods and
meaningful assertions.

------------------------------------------------------------------------

# Tier 3 --- Test Lifecycle & Organization

## 8. Test Fixtures

-   [ ] Understand a test fixture
-   [ ] Create fresh objects for tests
-   [ ] Avoid shared mutable test state
-   [ ] Know when setup reduces duplication
-   [ ] Avoid hiding important test behavior inside excessive setup

## 9. Lifecycle Annotations

-   [ ] `@BeforeEach`
-   [ ] `@AfterEach`
-   [ ] `@BeforeAll`
-   [ ] `@AfterAll`
-   [ ] Understand per-method lifecycle
-   [ ] Understand static requirements where applicable
-   [ ] Understand `@TestInstance`
-   [ ] Know when lifecycle customization is justified

## 10. Organizing Tests

-   [ ] Test class naming conventions
-   [ ] Test method naming conventions
-   [ ] `@DisplayName`
-   [ ] `@Nested`
-   [ ] Group related behavior
-   [ ] Keep test intent visible
-   [ ] Avoid one enormous test class when behaviors naturally separate

------------------------------------------------------------------------

# Tier 4 --- Testing Behavior & Failure

## 11. Exception Testing

-   [ ] `assertThrows()`
-   [ ] Inspect the returned exception
-   [ ] Assert meaningful exception messages/properties when appropriate
-   [ ] `assertDoesNotThrow()`
-   [ ] Distinguish expected domain failures from unexpected test
    failures

## 12. Multiple Assertions

-   [ ] `assertAll()`
-   [ ] Understand grouped assertions
-   [ ] Know when multiple assertions verify one behavior
-   [ ] Avoid combining unrelated behaviors into one test

## 13. Time-Based Assertions

-   [ ] Understand timeout testing
-   [ ] `assertTimeout()`
-   [ ] `assertTimeoutPreemptively()` awareness
-   [ ] Understand why timing-sensitive tests can become flaky
-   [ ] Avoid arbitrary sleeps as a testing strategy

## 14. Assumptions

-   [ ] Understand assumptions vs assertions
-   [ ] `assumeTrue()`
-   [ ] `assumeFalse()`
-   [ ] `assumingThat()`
-   [ ] Use assumptions sparingly for environment-dependent conditions

**Checkpoint:** Test success paths, invalid inputs, boundary conditions,
and expected exceptions.

------------------------------------------------------------------------

# Tier 5 --- Parameterized & Advanced Test Cases

## 15. Parameterized Tests

-   [ ] `@ParameterizedTest`
-   [ ] Understand why parameterization reduces duplicated tests
-   [ ] `@ValueSource`
-   [ ] `@NullSource`
-   [ ] `@EmptySource`
-   [ ] `@NullAndEmptySource`
-   [ ] `@EnumSource`
-   [ ] `@CsvSource`
-   [ ] `@CsvFileSource`
-   [ ] `@MethodSource`
-   [ ] `Arguments`
-   [ ] Create readable parameterized-test names

## 16. Repeated Tests

-   [ ] `@RepeatedTest`
-   [ ] Understand appropriate uses
-   [ ] Avoid using repetition to hide nondeterministic tests

## 17. Dynamic Tests

-   [ ] Understand dynamic tests conceptually
-   [ ] `@TestFactory`
-   [ ] `DynamicTest`
-   [ ] Recognize when parameterized tests are simpler
-   [ ] Treat dynamic tests as lower priority for ordinary application
    development

## 18. Tags & Selective Execution

-   [ ] `@Tag`
-   [ ] Categorize tests
-   [ ] Run selected test groups
-   [ ] Understand how tagging can support build pipelines later

------------------------------------------------------------------------

# Tier 6 --- Testing Real Java Code

## 19. Testing Pure Functions & Utility Logic

-   [ ] Normal inputs
-   [ ] Boundary values
-   [ ] Empty inputs
-   [ ] Null behavior where permitted
-   [ ] Invalid inputs
-   [ ] Deterministic outputs

## 20. Testing Classes

-   [ ] Constructor behavior
-   [ ] State changes
-   [ ] Returned values
-   [ ] Object equality
-   [ ] Domain invariants
-   [ ] Public behavior rather than private implementation

## 21. Testing Collections

-   [ ] Empty collections
-   [ ] Single-element collections
-   [ ] Multiple elements
-   [ ] Ordering
-   [ ] Duplicate behavior
-   [ ] Filtering/transformation results
-   [ ] Map key/value behavior

## 22. Testing Business Logic

-   [ ] Identify business rules
-   [ ] Test valid cases
-   [ ] Test rejected cases
-   [ ] Test boundaries
-   [ ] Test combinations that materially alter behavior
-   [ ] Keep business logic separable from infrastructure

**Checkpoint:** Write a comprehensive JUnit suite for a small
service/domain class without Spring or Mockito.

------------------------------------------------------------------------

# Tier 7 --- Test Design

## 23. Isolation

-   [ ] Tests should not depend on execution order
-   [ ] Tests should not modify shared state unpredictably
-   [ ] Each test should establish its own required conditions
-   [ ] Avoid dependence on external services in unit tests
-   [ ] Recognize when an external dependency changes a test from unit
    to integration scope

## 24. Determinism

-   [ ] Control random inputs
-   [ ] Control current time where behavior depends on time
-   [ ] Avoid arbitrary network dependencies
-   [ ] Avoid arbitrary sleeps
-   [ ] Recognize flaky tests
-   [ ] Diagnose sources of nondeterminism

## 25. Boundary-Value Thinking

-   [ ] Minimum values
-   [ ] Maximum values
-   [ ] Just below/above boundaries
-   [ ] Empty state
-   [ ] Missing values
-   [ ] Invalid formats
-   [ ] Duplicate values
-   [ ] Exceptional states

## 26. Test Quality

-   [ ] One clear behavioral purpose per test
-   [ ] Readable setup
-   [ ] Meaningful assertions
-   [ ] Useful failure output
-   [ ] Avoid testing implementation details
-   [ ] Avoid duplicating production logic inside tests
-   [ ] Avoid tests that can never meaningfully fail
-   [ ] Treat test code as maintainable production-quality code

## 27. FIRST Principles

-   [ ] Fast
-   [ ] Independent
-   [ ] Repeatable
-   [ ] Self-validating
-   [ ] Timely
-   [ ] Understand these as guidelines rather than rigid laws

------------------------------------------------------------------------

# Tier 8 --- JUnit Architecture & Extensions

## 28. JUnit Extension Model

-   [ ] Understand why JUnit supports extensions
-   [ ] `@ExtendWith`
-   [ ] Understand extension registration conceptually
-   [ ] Recognize lifecycle callbacks conceptually
-   [ ] Recognize parameter resolution conceptually
-   [ ] Understand that libraries can integrate with JUnit through
    extensions

## 29. Preparing for Mockito

-   [ ] Understand why a dependency can make a unit difficult to isolate
-   [ ] Understand constructor-based dependencies
-   [ ] Understand dependency substitution conceptually
-   [ ] Understand test doubles conceptually
-   [ ] Distinguish verifying returned state from verifying interaction
-   [ ] Recognize `MockitoExtension` as a JUnit/Mockito integration
    point
-   [ ] Do not assume JUnit itself creates mocks

``` text
JUnit
  │
  ├── discovers tests
  ├── executes tests
  ├── provides lifecycle
  └── provides assertions
          │
          ▼
Mockito Extension
          │
          └── integrates Mockito into that lifecycle
```

------------------------------------------------------------------------

# Tier 9 --- Maven & Professional Workflow

## 30. Maven Test Execution

-   [ ] Run `mvn test`
-   [ ] Run a specific test class
-   [ ] Run selected tests
-   [ ] Understand test reports
-   [ ] Recognize Maven Surefire conceptually
-   [ ] Understand how test failures affect builds
-   [ ] Keep test dependencies separate from production dependencies

## 31. Debugging Tests

-   [ ] Read JUnit failure output
-   [ ] Distinguish assertion failure from unexpected exception
-   [ ] Debug tests with breakpoints
-   [ ] Inspect test fixtures
-   [ ] Trace production code from a failing test
-   [ ] Reduce a complex failure to a minimal reproducible case

## 32. Refactoring with Tests

-   [ ] Use tests as a safety net
-   [ ] Refactor while preserving observable behavior
-   [ ] Update tests when requirements change
-   [ ] Avoid changing tests merely to make incorrect production
    behavior pass

## 33. TDD Awareness

-   [ ] Understand Red → Green → Refactor
-   [ ] Write a failing test for a desired behavior
-   [ ] Implement the smallest reasonable solution
-   [ ] Refactor with tests passing
-   [ ] Understand that JUnit supports TDD but does not require TDD

------------------------------------------------------------------------

# Tier 10 --- Spring Boot Readiness

## 34. What JUnit Does in a Future Spring Application

-   [ ] Understand that JUnit remains the test framework
-   [ ] Understand that Spring adds testing infrastructure on top
-   [ ] Recognize that many Spring classes can still be tested without
    starting Spring
-   [ ] Know why fast plain-Java unit tests remain valuable
-   [ ] Understand that Spring integration tests serve a different
    purpose

## 35. Recognize Future Spring Testing Concepts

These are **not prerequisites for JUnit mastery**, but the learner
should be ready to encounter:

-   [ ] Spring Test
-   [ ] Spring Boot Test
-   [ ] Application context tests
-   [ ] Test slices
-   [ ] Controller tests
-   [ ] Repository tests
-   [ ] `MockMvc`
-   [ ] Spring-managed test dependencies
-   [ ] Mockito integration in Spring tests

``` text
Plain Java class
      │
      ▼
JUnit test
      │
      ├── No Spring required
      │
      ▼
Mockito when isolation is needed
      │
      ▼
Spring testing only when Spring behavior
itself needs to participate in the test
```

------------------------------------------------------------------------

# Professional Mastery Check

Before progressing from JUnit into Mockito, you should independently be
able to:

-   [ ] Add modern JUnit to a Java project
-   [ ] Create and run test classes
-   [ ] Use core assertions
-   [ ] Test exceptions
-   [ ] Use setup/teardown correctly
-   [ ] Write parameterized tests
-   [ ] Test collections and domain objects
-   [ ] Identify normal, boundary, and invalid cases
-   [ ] Keep tests isolated
-   [ ] Recognize flaky tests
-   [ ] Write readable Arrange/Act/Assert tests
-   [ ] Run tests through Maven
-   [ ] Debug failing tests
-   [ ] Explain unit vs integration tests
-   [ ] Explain what JUnit is responsible for
-   [ ] Explain why a dependency sometimes needs to be replaced during a
    unit test
-   [ ] Recognize when Mockito may be useful

# Next Step

``` text
Java
  │
  ▼
JUnit  ← YOU ARE HERE
  │
  ▼
Mockito
  │
  ▼
Spring Boot Testing
```
