# Java Skill Tree --- Full-Stack Developer Path

> **Goal:** Progress from no Java experience to professional,
> application-level Java proficiency suitable for full-stack/backend
> development.
>
> **Scope:** Java language and standard-library skills. Spring/Spring
> Boot, Docker, CI/CD, cloud deployment, and database-specific
> technologies should have their own skill trees.
>
> **Reference basis:** *Java: The Complete Reference, Eleventh Edition*
> by Herbert Schildt (Java SE 11), updated here as a learning path for
> modern Java (Java 21+).
>
> **Target level:** Professional Java application developer --- not
> JVM/compiler engineer or language-runtime specialist.

------------------------------------------------------------------------

## Skill Tree Overview

``` text
Java
├── 1. Development Environment & Java Platform
├── 2. Syntax, Variables & Data Types
├── 3. Operators & Expressions
├── 4. Control Flow
├── 5. Methods
├── 6. Arrays & Strings
├── 7. Classes & Objects
├── 8. Object-Oriented Programming
├── 9. Interfaces, Abstract Types & Composition
├── 10. Packages, Access Control & Project Organization
├── 11. Exceptions & Error Handling
├── 12. Enums, Records & Special-Purpose Types
├── 13. Generics
├── 14. Collections Framework
├── 15. Functional Java, Lambdas & Method References
├── 16. Stream API
├── 17. Null Handling & Optional
├── 18. Modern Java Language Features
├── 19. Input/Output & Files
├── 20. Date & Time
├── 21. Regular Expressions
├── 22. Annotations & Reflection
├── 23. Concurrency & Multithreading
├── 24. Networking & HTTP
├── 25. JVM & Memory Fundamentals
├── 26. Modules, JARs & Classpath
├── 27. Maven, Builds & Dependencies
├── 28. Testing
├── 29. Debugging, Logging & Diagnostics
├── 30. Application Design & Architecture
├── 31. Security Fundamentals
├── 32. Performance & Code Quality
├── 33. Documentation & Professional Workflow
└── 34. Full-Stack Java Readiness
```

------------------------------------------------------------------------

# Tier 0 --- Orientation

## 1. Development Environment & Java Platform

### Java ecosystem

-   [ ] Explain Java source code, bytecode, the JVM, JDK, and standard
    library
-   [ ] Explain Java's platform-independent execution model at a
    practical level
-   [ ] Understand the difference between Java SE and frameworks built
    on Java

### Tooling

-   [ ] Install a modern LTS JDK
-   [ ] Use `java --version` and `javac --version`
-   [ ] Understand `JAVA_HOME` and `PATH`
-   [ ] Compile `.java` files with `javac`
-   [ ] Run compiled classes with `java`
-   [ ] Run simple single-file Java programs
-   [ ] Use JShell for experimentation
-   [ ] Use an IDE effectively without depending on it to understand
    compilation

### Program structure

-   [ ] Understand `.java` and `.class` files
-   [ ] Understand the `main` method
-   [ ] Understand class names and filenames
-   [ ] Understand basic package/directory relationships

**Checkpoint:** Write, compile, and run a small Java program without an
IDE.

------------------------------------------------------------------------

# Tier 1 --- Core Programming

## 2. Syntax, Variables & Data Types

### Syntax

-   [ ] Statements and blocks
-   [ ] Semicolons
-   [ ] Identifiers and keywords
-   [ ] Comments
-   [ ] Java naming conventions

### Primitive types

-   [ ] `byte`, `short`, `int`, `long`
-   [ ] `float`, `double`
-   [ ] `char`
-   [ ] `boolean`
-   [ ] Understand ranges and precision at a practical level

### Variables

-   [ ] Declare, initialize, and reassign variables
-   [ ] Understand scope and lifetime
-   [ ] Distinguish local, instance, and static variables
-   [ ] Use `final`
-   [ ] Understand default field values vs required local initialization

### Literals and conversion

-   [ ] Integer, floating-point, character, string, and boolean literals
-   [ ] Numeric separators
-   [ ] Binary and hexadecimal notation
-   [ ] Widening conversion
-   [ ] Narrowing conversion
-   [ ] Explicit casting
-   [ ] Numeric promotion
-   [ ] Recognize data-loss risks

### Type inference

-   [ ] Use `var`
-   [ ] Understand that `var` does not make Java dynamically typed
-   [ ] Know when an explicit type improves readability

------------------------------------------------------------------------

## 3. Operators & Expressions

-   [ ] Arithmetic: `+ - * / %`
-   [ ] Increment/decrement: `++ --`
-   [ ] Assignment and compound assignment
-   [ ] Comparison: `== != < > <= >=`
-   [ ] Logical: `&& || !`
-   [ ] Understand short-circuit evaluation
-   [ ] Bitwise: `& | ^ ~`
-   [ ] Shifts: `<< >> >>>`
-   [ ] Ternary operator `?:`
-   [ ] Operator precedence
-   [ ] Use parentheses to make intent clear
-   [ ] Distinguish expressions from statements

------------------------------------------------------------------------

## 4. Control Flow

### Selection

-   [ ] `if`
-   [ ] `if/else`
-   [ ] `else if`
-   [ ] Nested conditions

### Switch

-   [ ] Traditional `switch`
-   [ ] Modern switch expressions
-   [ ] Arrow labels (`->`)
-   [ ] Return values from switch expressions
-   [ ] Know when `switch` is clearer than chained conditions

### Iteration

-   [ ] `for`
-   [ ] Enhanced `for`
-   [ ] `while`
-   [ ] `do-while`
-   [ ] Nested loops
-   [ ] `break`
-   [ ] `continue`
-   [ ] Avoid accidental infinite loops

**Checkpoint:** Solve basic searching, counting, filtering, and
accumulation problems using control flow.

------------------------------------------------------------------------

## 5. Methods

### Fundamentals

-   [ ] Declare and call methods
-   [ ] Parameters vs arguments
-   [ ] Return values
-   [ ] `void`
-   [ ] Method signatures
-   [ ] Local variables and scope

### Argument passing

-   [ ] Understand that Java is pass-by-value
-   [ ] Understand what is copied when an object reference is passed
-   [ ] Distinguish mutating an object from reassigning a parameter

### Method design

-   [ ] Break large operations into smaller methods
-   [ ] Give methods focused responsibilities
-   [ ] Choose meaningful names
-   [ ] Keep parameter lists manageable
-   [ ] Separate calculations from side effects where practical

### Advanced method features

-   [ ] Method overloading
-   [ ] Distinguish overloading from overriding
-   [ ] Varargs (`Type...`)
-   [ ] Recognize overload ambiguity
-   [ ] Recursion
-   [ ] Base and recursive cases
-   [ ] Trace the call stack

**Checkpoint:** Implement 15--20 small functions from memory without
framework code.

------------------------------------------------------------------------

## 6. Arrays & Strings

### Arrays

-   [ ] Declare, instantiate, and initialize arrays
-   [ ] Access elements by index
-   [ ] Use `.length`
-   [ ] Iterate through arrays
-   [ ] Understand default values
-   [ ] Multidimensional arrays
-   [ ] Arrays of object references
-   [ ] Understand fixed-size array behavior

### `String`

-   [ ] Understand immutability
-   [ ] Concatenation
-   [ ] `length()`
-   [ ] `charAt()`
-   [ ] `substring()`
-   [ ] `contains()`
-   [ ] `startsWith()` / `endsWith()`
-   [ ] `replace()`
-   [ ] `split()`
-   [ ] `strip()`
-   [ ] Case conversion
-   [ ] Search strings

### Comparison

-   [ ] Distinguish `==` from `.equals()`
-   [ ] `.equalsIgnoreCase()`
-   [ ] `compareTo()`

### `StringBuilder`

-   [ ] `append()`
-   [ ] `insert()`
-   [ ] `delete()`
-   [ ] `replace()`
-   [ ] `reverse()`
-   [ ] Know when repeated string construction should use
    `StringBuilder`

**Checkpoint:** Parse, search, validate, and transform text without
external libraries.

------------------------------------------------------------------------

# Tier 2 --- Object-Oriented Java

## 7. Classes & Objects

### Classes

-   [ ] Define classes
-   [ ] Instantiate objects with `new`
-   [ ] Fields and methods
-   [ ] Object references
-   [ ] Understand state and behavior

### Constructors

-   [ ] Define constructors
-   [ ] Constructor parameters
-   [ ] Constructor overloading
-   [ ] `this`
-   [ ] Constructor chaining with `this(...)`
-   [ ] Default constructors

### Instance vs static

-   [ ] Instance fields/methods
-   [ ] Static fields/methods
-   [ ] Static initialization
-   [ ] Know when data belongs to an object vs the class

### Object behavior

-   [ ] Reference identity
-   [ ] Override `equals()`
-   [ ] Override `hashCode()`
-   [ ] Understand the equality/hash-code contract
-   [ ] Override `toString()`

### Lifecycle

-   [ ] Object creation
-   [ ] Reachability
-   [ ] Garbage collection conceptually
-   [ ] Do not rely on garbage collection for resource cleanup

**Checkpoint:** Model a small real-world domain with multiple
interacting classes.

------------------------------------------------------------------------

## 8. Object-Oriented Programming

### Encapsulation

-   [ ] Control access to internal state
-   [ ] Protect class invariants
-   [ ] Use access modifiers intentionally
-   [ ] Understand getters/setters without treating them as mandatory

### Abstraction

-   [ ] Separate what an object does from how it does it
-   [ ] Design useful public APIs
-   [ ] Hide unnecessary implementation details

### Inheritance

-   [ ] `extends`
-   [ ] Superclass/subclass relationships
-   [ ] `super`
-   [ ] Superclass constructors
-   [ ] Method overriding
-   [ ] Inherited member visibility

### Polymorphism

-   [ ] Store subtype objects in supertype references
-   [ ] Dynamic method dispatch
-   [ ] Program against abstractions
-   [ ] Runtime method selection

### Abstract and final types

-   [ ] Abstract classes
-   [ ] Abstract methods
-   [ ] Final variables
-   [ ] Final methods
-   [ ] Final classes
-   [ ] Understand that `final` alone does not make an object immutable

### Composition

-   [ ] Model "has-a" relationships
-   [ ] Build objects from other objects
-   [ ] Compare composition with inheritance
-   [ ] Prefer composition when inheritance does not represent a true
    subtype

**Checkpoint:** Explain and demonstrate encapsulation, abstraction,
inheritance, polymorphism, and composition.

------------------------------------------------------------------------

## 9. Interfaces, Abstract Types & Composition

-   [ ] Declare and implement interfaces
-   [ ] Use interface references
-   [ ] Extend interfaces
-   [ ] Implement multiple interfaces
-   [ ] Default interface methods
-   [ ] Static interface methods
-   [ ] Private interface methods
-   [ ] Interface vs abstract class
-   [ ] Interface vs concrete class
-   [ ] Composition vs inheritance
-   [ ] Depend on abstractions when useful
-   [ ] Understand functional interfaces
-   [ ] Use `@FunctionalInterface`

------------------------------------------------------------------------

## 10. Packages, Access Control & Project Organization

### Packages

-   [ ] Declare packages
-   [ ] Import classes
-   [ ] Fully qualified class names
-   [ ] Organize related code into packages
-   [ ] Avoid the default package in real applications

### Access

-   [ ] `public`
-   [ ] `protected`
-   [ ] package-private/default
-   [ ] `private`
-   [ ] Understand visibility across packages and inheritance

### Organization

-   [ ] Separate source and test code
-   [ ] Understand conventional Java project layouts
-   [ ] Organize code by domain/responsibility
-   [ ] Avoid giant utility classes
-   [ ] Keep package dependencies understandable

------------------------------------------------------------------------

# Tier 3 --- Reliable Java Programs

## 11. Exceptions & Error Handling

### Exception hierarchy

-   [ ] `Throwable`
-   [ ] `Error`
-   [ ] `Exception`
-   [ ] Checked exceptions
-   [ ] Unchecked exceptions / `RuntimeException`

### Handling

-   [ ] `try`
-   [ ] `catch`
-   [ ] Multiple catches
-   [ ] Multi-catch
-   [ ] `finally`
-   [ ] `throw`
-   [ ] `throws`

### Custom exceptions

-   [ ] Create custom exception classes
-   [ ] Choose checked vs unchecked intentionally
-   [ ] Preserve causes when wrapping exceptions

### Resource safety

-   [ ] Try-with-resources
-   [ ] `AutoCloseable`
-   [ ] Safely close files, streams, connections, and similar resources

### Design

-   [ ] Catch exceptions at useful boundaries
-   [ ] Avoid empty catch blocks
-   [ ] Avoid overly broad catches without reason
-   [ ] Do not use exceptions as ordinary control flow
-   [ ] Produce useful error messages

**Checkpoint:** Build an application that validates input, throws
meaningful exceptions, handles failures, and closes resources safely.

------------------------------------------------------------------------

## 12. Enums, Records & Special-Purpose Types

### Enums

-   [ ] Declare enums
-   [ ] `values()`
-   [ ] `valueOf()`
-   [ ] Add fields and methods
-   [ ] Replace appropriate magic strings/constants with enums

### Records

-   [ ] Declare records
-   [ ] Understand generated accessors
-   [ ] Understand generated `equals()`, `hashCode()`, and `toString()`
-   [ ] Compact constructors
-   [ ] Use records as data carriers
-   [ ] Know when a normal class is better

### Nested types

-   [ ] Static nested classes
-   [ ] Inner classes
-   [ ] Local classes
-   [ ] Anonymous classes
-   [ ] Know when nesting improves organization

------------------------------------------------------------------------

## 13. Generics

### Fundamentals

-   [ ] Understand why generics exist
-   [ ] Generic classes
-   [ ] Generic methods
-   [ ] Multiple type parameters
-   [ ] Compile-time type safety

### Bounds and wildcards

-   [ ] `<T extends Type>`
-   [ ] Multiple bounds
-   [ ] `<?>`
-   [ ] `<? extends T>`
-   [ ] `<? super T>`
-   [ ] Understand PECS: producer extends, consumer super

### Behavior

-   [ ] Type inference
-   [ ] Raw types
-   [ ] Avoid raw types in modern code
-   [ ] Type erasure at a practical level
-   [ ] Recognize generic restrictions

**Checkpoint:** Write generic utilities and explain why `List<Object>`
cannot simply substitute for `List<String>`.

------------------------------------------------------------------------

# Tier 4 --- Collections & Functional Java

## 14. Collections Framework

### Core hierarchy

-   [ ] `Iterable`
-   [ ] `Collection`
-   [ ] `List`
-   [ ] `Set`
-   [ ] `Queue`
-   [ ] `Deque`
-   [ ] `Map`

### Lists

-   [ ] `ArrayList`
-   [ ] `LinkedList`
-   [ ] Add/get/update/remove/search/iterate
-   [ ] Choose implementations based on operations

### Sets

-   [ ] `HashSet`
-   [ ] `LinkedHashSet`
-   [ ] `TreeSet`
-   [ ] Uniqueness
-   [ ] Relationship between hashing, `equals()`, and `hashCode()`

### Maps

-   [ ] `HashMap`
-   [ ] `LinkedHashMap`
-   [ ] `TreeMap`
-   [ ] `put()`, `get()`, `getOrDefault()`
-   [ ] `containsKey()`, `remove()`
-   [ ] `keySet()`, `values()`, `entrySet()`
-   [ ] `computeIfAbsent()`
-   [ ] `merge()`

### Queues/deques

-   [ ] `Queue`
-   [ ] `Deque`
-   [ ] `ArrayDeque`
-   [ ] `PriorityQueue`
-   [ ] FIFO behavior
-   [ ] Stack/LIFO behavior with `Deque`

### Iteration and ordering

-   [ ] Enhanced `for`
-   [ ] `Iterator`
-   [ ] `ListIterator`
-   [ ] `forEach()`
-   [ ] Safe removal during iteration
-   [ ] `Comparable`
-   [ ] `Comparator`
-   [ ] Natural vs custom ordering
-   [ ] Comparator chaining

### Utilities and factories

-   [ ] `Collections.sort()`
-   [ ] `Collections.reverse()`
-   [ ] `Collections.min()` / `max()`
-   [ ] `List.of()`
-   [ ] `Set.of()`
-   [ ] `Map.of()`
-   [ ] Understand unmodifiable factory collections

### Complexity awareness

-   [ ] Typical `ArrayList` operation costs
-   [ ] Typical `HashMap` operation costs
-   [ ] Typical `HashSet` behavior
-   [ ] Cost of sorted structures
-   [ ] Select data structures based on expected operations

**Checkpoint:** Select and use the appropriate collection for common
application problems without guessing.

------------------------------------------------------------------------

## 15. Functional Java, Lambdas & Method References

### Lambdas

-   [ ] Lambda syntax
-   [ ] Expression and block lambdas
-   [ ] Parameters and return values
-   [ ] Variable capture
-   [ ] Effectively-final variables

### Standard functional interfaces

-   [ ] `Predicate<T>`
-   [ ] `Function<T,R>`
-   [ ] `Consumer<T>`
-   [ ] `Supplier<T>`
-   [ ] `UnaryOperator<T>`
-   [ ] `BinaryOperator<T>`

### Method references

-   [ ] Static method references
-   [ ] Instance method references
-   [ ] Constructor references

### Judgment

-   [ ] Pass behavior as an argument
-   [ ] Replace simple anonymous classes with lambdas
-   [ ] Avoid functional style when ordinary control flow is clearer

------------------------------------------------------------------------

## 16. Stream API

### Fundamentals

-   [ ] Create streams from collections
-   [ ] Understand that streams are not collections
-   [ ] Lazy intermediate operations
-   [ ] Terminal operations
-   [ ] Single-use streams

### Intermediate operations

-   [ ] `filter()`
-   [ ] `map()`
-   [ ] `flatMap()`
-   [ ] `distinct()`
-   [ ] `sorted()`
-   [ ] `limit()`
-   [ ] `skip()`
-   [ ] `peek()` with caution

### Terminal operations

-   [ ] `forEach()`
-   [ ] `toList()`
-   [ ] `collect()`
-   [ ] `reduce()`
-   [ ] `count()`
-   [ ] `min()` / `max()`
-   [ ] `findFirst()` / `findAny()`
-   [ ] `anyMatch()` / `allMatch()` / `noneMatch()`

### Collectors

-   [ ] `toList()`
-   [ ] `toSet()`
-   [ ] `toMap()`
-   [ ] `joining()`
-   [ ] `groupingBy()`
-   [ ] `partitioningBy()`
-   [ ] Counting and summarizing collectors

### Judgment

-   [ ] Decide between streams and loops
-   [ ] Avoid unwanted side effects
-   [ ] Debug stream pipelines
-   [ ] Understand parallel streams conceptually
-   [ ] Avoid parallel streams by default in server applications

**Checkpoint:** Filter, transform, group, aggregate, and map domain data
using both loops and streams.

------------------------------------------------------------------------

## 17. Null Handling & `Optional`

-   [ ] Understand `null`
-   [ ] Recognize and diagnose `NullPointerException`
-   [ ] Explicit null checks
-   [ ] Design APIs to minimize ambiguous null behavior
-   [ ] `Optional.of()`
-   [ ] `Optional.ofNullable()`
-   [ ] `Optional.empty()`
-   [ ] `isPresent()` / `isEmpty()`
-   [ ] `ifPresent()`
-   [ ] `map()` / `flatMap()` / `filter()`
-   [ ] `orElse()`
-   [ ] `orElseGet()`
-   [ ] `orElseThrow()`
-   [ ] Understand eager vs lazy fallback evaluation
-   [ ] Use `Optional` primarily for optional return values rather than
    mechanically everywhere

------------------------------------------------------------------------

# Tier 5 --- Modern Java

## 18. Modern Java Language Features

> The reference book targets Java 11. This branch extends the learning
> path into modern Java used by current applications.

### Switch expressions

-   [ ] Arrow syntax
-   [ ] Expression results
-   [ ] `yield`

### Text blocks

-   [ ] Use text blocks for readable multiline text
-   [ ] Understand indentation behavior

### Pattern matching

-   [ ] Pattern matching with `instanceof`
-   [ ] Pattern variables
-   [ ] Pattern matching in `switch`
-   [ ] Exhaustive type handling where applicable

### Sealed classes

-   [ ] `sealed`
-   [ ] `permits`
-   [ ] `non-sealed`
-   [ ] `final`
-   [ ] Model restricted type hierarchies

### Modern development awareness

-   [ ] Records as modern data carriers
-   [ ] Prefer current APIs over obsolete equivalents
-   [ ] Read documentation for the JDK version in use
-   [ ] Recognize preview features
-   [ ] Understand LTS vs feature releases at a practical level

------------------------------------------------------------------------

# Tier 6 --- Files, Time & Data Processing

## 19. Input/Output & Files

### I/O concepts

-   [ ] Byte streams
-   [ ] Character streams
-   [ ] Buffered I/O
-   [ ] Character encodings

### Core APIs

-   [ ] `InputStream`
-   [ ] `OutputStream`
-   [ ] `Reader`
-   [ ] `Writer`
-   [ ] `BufferedReader`
-   [ ] `BufferedWriter`

### NIO.2

-   [ ] `Path`
-   [ ] `Files`
-   [ ] Create paths
-   [ ] Read/write files
-   [ ] Copy/move/delete files
-   [ ] Create directories
-   [ ] List directories
-   [ ] Inspect file metadata

### Resource management

-   [ ] Try-with-resources
-   [ ] Understand deterministic resource cleanup vs garbage collection

### Serialization awareness

-   [ ] Understand native Java object serialization conceptually
-   [ ] Recognize its risks and limitations
-   [ ] Do not treat it as the default web-data format

**Checkpoint:** Build a command-line application that safely reads,
transforms, and writes files.

------------------------------------------------------------------------

## 20. Date & Time

-   [ ] `LocalDate`
-   [ ] `LocalTime`
-   [ ] `LocalDateTime`
-   [ ] `Instant`
-   [ ] `OffsetDateTime`
-   [ ] `ZonedDateTime`
-   [ ] `ZoneId`
-   [ ] `Duration`
-   [ ] `Period`
-   [ ] `DateTimeFormatter`
-   [ ] Parse date/time strings
-   [ ] Format date/time values
-   [ ] Distinguish local time from an absolute instant
-   [ ] Understand UTC, offsets, and time zones
-   [ ] Avoid ambiguous timestamps when an instant is required

------------------------------------------------------------------------

## 21. Regular Expressions

-   [ ] Regex syntax
-   [ ] `Pattern`
-   [ ] `Matcher`
-   [ ] Match and find text
-   [ ] Capture groups
-   [ ] Replace text
-   [ ] Escape regex correctly inside Java strings
-   [ ] Recognize when ordinary string operations are simpler

------------------------------------------------------------------------

# Tier 7 --- Runtime Capabilities

## 22. Annotations & Reflection

### Annotations

-   [ ] Understand annotation syntax
-   [ ] `@Override`
-   [ ] `@Deprecated`
-   [ ] `@SuppressWarnings`
-   [ ] `@FunctionalInterface`
-   [ ] Create custom annotations
-   [ ] Annotation targets
-   [ ] Retention policies

### Reflection

-   [ ] Understand `Class<?>`
-   [ ] Inspect fields, methods, and constructors
-   [ ] Read runtime annotations
-   [ ] Understand why frameworks use reflection
-   [ ] Understand reflection tradeoffs

### Framework readiness

-   [ ] Understand that annotations alone do not execute behavior
-   [ ] Understand that frameworks/processors inspect annotations
-   [ ] Recognize annotation-driven configuration as behavior layered on
    Java mechanisms

**Checkpoint:** Create a custom annotation and inspect it at runtime.

------------------------------------------------------------------------

## 23. Concurrency & Multithreading

### Fundamentals

-   [ ] Processes vs threads
-   [ ] Java thread model
-   [ ] `Runnable`
-   [ ] `Thread`
-   [ ] Start threads
-   [ ] `join()`
-   [ ] Thread lifecycle at a practical level

### Shared state

-   [ ] Race conditions
-   [ ] Atomicity
-   [ ] Visibility
-   [ ] Thread safety
-   [ ] Immutability as a concurrency tool

### Synchronization

-   [ ] `synchronized`
-   [ ] Synchronized methods and blocks
-   [ ] Intrinsic locks
-   [ ] Recognize deadlock

### Higher-level concurrency

-   [ ] `Executor`
-   [ ] `ExecutorService`
-   [ ] Thread pools
-   [ ] `Callable`
-   [ ] `Future`
-   [ ] `CompletableFuture`
-   [ ] Scheduled execution
-   [ ] Correct executor shutdown

### Utilities

-   [ ] `ConcurrentHashMap`
-   [ ] Atomic variables
-   [ ] Locks
-   [ ] Semaphores
-   [ ] `CountDownLatch`
-   [ ] Concurrent collections

### Virtual threads

-   [ ] Understand virtual threads
-   [ ] Platform vs virtual threads
-   [ ] Recognize suitable workloads
-   [ ] Understand that virtual threads do not eliminate shared-state
    problems

### Backend readiness

-   [ ] Write thread-safe stateless services
-   [ ] Recognize unsafe shared mutable state
-   [ ] Understand that requests may execute concurrently
-   [ ] Avoid unmanaged manual threading where managed concurrency is
    appropriate

**Checkpoint:** Diagnose a simple race condition and replace unnecessary
low-level thread management with an executor-based solution.

------------------------------------------------------------------------

## 24. Networking & HTTP

-   [ ] Understand clients and servers
-   [ ] Sockets conceptually
-   [ ] TCP/IP at an application-developer level
-   [ ] Hostnames, addresses, and ports
-   [ ] `URI`
-   [ ] `URL` awareness
-   [ ] `java.net.http.HttpClient`
-   [ ] Build HTTP requests
-   [ ] Send GET and POST requests
-   [ ] Read responses
-   [ ] Handle HTTP status codes
-   [ ] Handle timeouts and failures
-   [ ] Understand HTTP methods, headers, and request/response bodies
-   [ ] Understand JSON as a common web representation

------------------------------------------------------------------------

# Tier 8 --- JVM & Build Knowledge

## 25. JVM & Memory Fundamentals

> Full-stack developers need practical JVM knowledge, not JVM
> implementation expertise.

-   [ ] Source code → compiler → bytecode → JVM
-   [ ] Class loading conceptually
-   [ ] JIT compilation conceptually
-   [ ] Stack vs heap at a practical level
-   [ ] Primitive values vs object references
-   [ ] Object allocation
-   [ ] Garbage collection
-   [ ] Reachability
-   [ ] Understand retained-reference memory leaks
-   [ ] Recognize `NullPointerException`
-   [ ] Recognize `StackOverflowError`
-   [ ] Recognize `OutOfMemoryError`
-   [ ] Recognize resource leaks and thread exhaustion
-   [ ] Understand JVM arguments
-   [ ] Recognize common JVM tuning options without memorizing them
-   [ ] Know when profiling/diagnostics are needed instead of guessing

------------------------------------------------------------------------

## 26. Modules, JARs & Classpath

### Classpath

-   [ ] Understand what the classpath does
-   [ ] Dependency visibility
-   [ ] Diagnose basic class-not-found problems

### JARs

-   [ ] Understand JAR structure
-   [ ] Package compiled code
-   [ ] Executable JARs conceptually
-   [ ] Inspect JAR contents

### Java Platform Module System

-   [ ] `module-info.java`
-   [ ] `requires`
-   [ ] `exports`
-   [ ] Named vs unnamed modules
-   [ ] Recognize module-based projects
-   [ ] Deep JPMS mastery is optional for this path

------------------------------------------------------------------------

## 27. Maven, Builds & Dependencies

### Maven

-   [ ] Understand `pom.xml`
-   [ ] Group/artifact/version coordinates
-   [ ] Add dependencies
-   [ ] Dependency scopes
-   [ ] Maven repositories
-   [ ] Maven Wrapper
-   [ ] `compile`
-   [ ] `test`
-   [ ] `package`
-   [ ] `clean`
-   [ ] Maven lifecycle
-   [ ] Plugins at a practical level
-   [ ] Inspect dependency trees
-   [ ] Diagnose dependency/version conflicts

### Gradle awareness

-   [ ] Understand what Gradle is
-   [ ] Recognize Gradle project files
-   [ ] Navigate a Gradle-based Java project
-   [ ] Deep Gradle mastery is optional unless a project requires it

### Dependency discipline

-   [ ] Distinguish JDK APIs from third-party dependencies
-   [ ] Understand transitive dependencies
-   [ ] Avoid unnecessary dependencies
-   [ ] Check version compatibility
-   [ ] Keep dependencies maintainable

**Checkpoint:** Create a multi-class Maven project from scratch, add a
dependency, run tests, and package it.

------------------------------------------------------------------------

# Tier 9 --- Testing & Diagnostics

## 28. Testing

### Fundamentals

-   [ ] Unit tests
-   [ ] Integration tests
-   [ ] End-to-end tests conceptually
-   [ ] Arrange / Act / Assert
-   [ ] Test behavior rather than implementation details

### JUnit

-   [ ] Create JUnit tests
-   [ ] `@Test`
-   [ ] Assertions
-   [ ] Setup/teardown lifecycle
-   [ ] Parameterized tests
-   [ ] Exception assertions
-   [ ] Organize test classes

### Testable design

-   [ ] Separate business logic from I/O
-   [ ] Pass dependencies into classes
-   [ ] Avoid unnecessary global/static mutable state
-   [ ] Use abstractions where substitutability is useful

### Test doubles

-   [ ] Understand mocks
-   [ ] Understand stubs
-   [ ] Understand fakes
-   [ ] Know when mocking is useful
-   [ ] Recognize over-mocking
-   [ ] Become comfortable with a common mocking library when required

### Coverage of behavior

-   [ ] Normal cases
-   [ ] Boundary cases
-   [ ] Invalid input
-   [ ] Exceptions
-   [ ] Collection transformations
-   [ ] Business rules

**Checkpoint:** Maintain a small project with meaningful automated tests
run through the build tool.

------------------------------------------------------------------------

## 29. Debugging, Logging & Diagnostics

### Debugging

-   [ ] Read compiler errors
-   [ ] Read stack traces
-   [ ] Find the originating exception
-   [ ] Set breakpoints
-   [ ] Step into/over/out
-   [ ] Inspect variables
-   [ ] Inspect the call stack
-   [ ] Conditional breakpoints

### Logging

-   [ ] Understand TRACE, DEBUG, INFO, WARN, ERROR
-   [ ] Parameterized logging
-   [ ] Avoid logging secrets
-   [ ] Avoid `System.out.println()` as production logging

### Troubleshooting process

-   [ ] Reproduce the problem
-   [ ] Isolate the failing layer
-   [ ] Find the first meaningful cause
-   [ ] Form a testable hypothesis
-   [ ] Change one relevant variable at a time
-   [ ] Verify fixes with tests where practical

------------------------------------------------------------------------

# Tier 10 --- Professional Application Design

## 30. Application Design & Architecture

### Separation of concerns

-   [ ] Separate presentation/API concerns
-   [ ] Separate business logic
-   [ ] Separate data access
-   [ ] Separate external integrations
-   [ ] Keep domain concepts understandable

### Common backend layers

-   [ ] Controller/API boundary concept
-   [ ] Service/business-logic concept
-   [ ] Repository/data-access concept
-   [ ] DTO concept
-   [ ] Domain/entity concept
-   [ ] Understand these as architectural patterns, not Java keywords

### Dependencies

-   [ ] Constructor injection as a design technique
-   [ ] Program against interfaces when useful
-   [ ] Avoid hidden dependencies
-   [ ] Understand inversion of control conceptually
-   [ ] Be ready for framework-managed dependency injection

### Data boundaries

-   [ ] Separate external request/response shapes from internal models
    when useful
-   [ ] Map between representations
-   [ ] Validate data at boundaries
-   [ ] Use records where appropriate for data carriers

### SOLID awareness

-   [ ] Single Responsibility
-   [ ] Open/Closed
-   [ ] Liskov Substitution
-   [ ] Interface Segregation
-   [ ] Dependency Inversion
-   [ ] Apply principles pragmatically

### Common patterns

-   [ ] Strategy
-   [ ] Factory
-   [ ] Builder
-   [ ] Adapter
-   [ ] Observer
-   [ ] Repository
-   [ ] Dependency injection
-   [ ] Recognize patterns without forcing them into every problem

### Immutability

-   [ ] Design immutable objects
-   [ ] Final fields appropriately
-   [ ] Immutable collections where appropriate
-   [ ] Defensive copying

**Checkpoint:** Design a small backend domain whose business logic can
be tested independently of HTTP and database infrastructure.

------------------------------------------------------------------------

## 31. Security Fundamentals

> Framework-specific authentication and authorization belong in a
> Spring/security tree.

-   [ ] Never hard-code secrets in source
-   [ ] Environment-based configuration conceptually
-   [ ] Validate untrusted input
-   [ ] Avoid sensitive data in logs/errors
-   [ ] Password hashing vs encryption conceptually
-   [ ] Cryptographically secure randomness when required
-   [ ] Do not invent custom cryptography
-   [ ] Dependency vulnerability awareness
-   [ ] Keep runtime/dependencies updated
-   [ ] Serialization/deserialization risk
-   [ ] Injection vulnerabilities conceptually
-   [ ] Parameterized database queries and SQL injection prevention
-   [ ] Authentication vs authorization
-   [ ] HTTPS/TLS at an application-developer level

------------------------------------------------------------------------

## 32. Performance & Code Quality

### Complexity

-   [ ] Big-O at a practical level
-   [ ] Recognize linear vs nested-linear work
-   [ ] Choose collections with operation costs in mind
-   [ ] Avoid premature optimization

### Allocation and strings

-   [ ] Recognize excessive temporary allocations
-   [ ] Use `StringBuilder` where appropriate
-   [ ] Understand immutable-object tradeoffs

### Backend performance

-   [ ] Recognize that network/database operations often dominate
    micro-optimizations
-   [ ] Avoid unnecessary repeated external calls
-   [ ] Understand batching conceptually
-   [ ] Understand connection/resource limits conceptually

### Profiling mindset

-   [ ] Measure before optimizing
-   [ ] Use profiling evidence
-   [ ] Distinguish CPU, memory, I/O, and contention bottlenecks

### Code quality

-   [ ] Meaningful names
-   [ ] Small focused methods
-   [ ] Cohesive classes
-   [ ] Limited duplication
-   [ ] Clear control flow
-   [ ] Appropriate comments
-   [ ] Consistent formatting
-   [ ] Prefer simple readable code over clever code

------------------------------------------------------------------------

## 33. Documentation & Professional Workflow

### Reading documentation

-   [ ] Read JDK API documentation
-   [ ] Interpret class documentation
-   [ ] Interpret method signatures
-   [ ] Parameters
-   [ ] Return values
-   [ ] Documented exceptions
-   [ ] Follow interfaces and inheritance

### Javadoc

-   [ ] Documentation comments
-   [ ] `@param`
-   [ ] `@return`
-   [ ] `@throws`
-   [ ] `@see`
-   [ ] `@deprecated`
-   [ ] Document behavior rather than restating code

### Source navigation

-   [ ] Navigate to declarations
-   [ ] Find usages
-   [ ] Inspect inferred types
-   [ ] Read library source when documentation is insufficient
-   [ ] Use IDE refactoring tools safely

### Professional workflow

-   [ ] Keep changes focused
-   [ ] Recognize generated files
-   [ ] Review diffs before committing
-   [ ] Write reviewable code

------------------------------------------------------------------------

# Tier 11 --- Full-Stack Java Readiness

## 34. Full-Stack Java Readiness

A developer reaching this tier should be ready for a dedicated
**Spring/Spring Boot skill tree**.

### Language fluency

-   [ ] Write classes and methods without constant syntax lookup
-   [ ] Use OOP intentionally
-   [ ] Use interfaces and composition
-   [ ] Use generics comfortably
-   [ ] Handle exceptions appropriately
-   [ ] Use common collections from memory
-   [ ] Read and write stream pipelines
-   [ ] Use modern Java syntax

### Data manipulation

-   [ ] Transform lists of domain objects
-   [ ] Group/index data with maps
-   [ ] Remove duplicates with sets
-   [ ] Sort with comparators
-   [ ] Parse/format strings
-   [ ] Parse/format dates and times
-   [ ] Validate incoming values

### Backend readiness

-   [ ] Understand HTTP request/response concepts
-   [ ] Understand DTOs
-   [ ] Understand service layers
-   [ ] Understand repository layers
-   [ ] Understand dependency injection conceptually
-   [ ] Understand annotations conceptually
-   [ ] Understand reflection's role in frameworks
-   [ ] Understand concurrent-request concerns
-   [ ] Understand environment configuration conceptually

### Build/testing readiness

-   [ ] Create Maven projects
-   [ ] Add/update dependencies
-   [ ] Compile/package applications
-   [ ] Run unit tests
-   [ ] Debug failures
-   [ ] Read stack traces
-   [ ] Use logging effectively

### Professional comprehension

-   [ ] Read unfamiliar Java classes
-   [ ] Trace execution across classes
-   [ ] Follow interfaces to implementations
-   [ ] Follow method calls through layers
-   [ ] Understand generic method signatures
-   [ ] Distinguish Java behavior from framework behavior
-   [ ] Use documentation to fill knowledge gaps

------------------------------------------------------------------------

# Practical Progression

## Stage 1 --- Beginner

``` text
variables
→ operators
→ conditions
→ loops
→ methods
→ arrays
→ strings
```

**Projects:** calculator, number guessing game, text statistics program,
command-line menu.

## Stage 2 --- Object-Oriented Beginner

``` text
classes
→ objects
→ constructors
→ encapsulation
→ inheritance
→ interfaces
→ composition
```

**Projects:** bank-account model, library/catalog system,
employee/payroll model, inventory system.

## Stage 3 --- Intermediate Java

``` text
exceptions
→ generics
→ collections
→ enums
→ records
→ lambdas
→ streams
→ Optional
```

**Projects:** in-memory guestbook, contact manager, task manager,
CSV/data processor.

## Stage 4 --- Application Java

``` text
files
→ java.time
→ HTTP client
→ annotations
→ reflection
→ concurrency
→ Maven
→ JUnit
→ logging
```

**Projects:** file-backed task manager, REST API consumer, concurrent
file processor, tested multi-package Maven application.

## Stage 5 --- Professional Full-Stack Java Foundation

``` text
domain models
       ↓
DTOs → validation
       ↓
service/business logic
       ↓
repository abstraction
       ↓
external resources

+ exceptions
+ tests
+ logging
+ configuration
+ concurrency awareness
+ Maven
```

**Capstone:** Build the Java core of a small message-board or guestbook
application without Spring first. Model requests/responses, validation,
business logic, repository interfaces, exception behavior, tests, and
configuration. Then rebuild the architecture with Spring Boot in a
separate Spring skill tree.

------------------------------------------------------------------------

# Knowledge Depth Scale

  -----------------------------------------------------------------------
  Level                               Meaning
  ----------------------------------- -----------------------------------
  **0 --- Unknown**                   Have not learned the concept

  **1 --- Recognize**                 Can identify and broadly explain it

  **2 --- Guided**                    Can use it with
                                      documentation/examples

  **3 --- Independent**               Can implement it without
                                      step-by-step help

  **4 --- Applied**                   Can choose when and why to use it
                                      in a real application

  **5 --- Professional**              Can debug, review, explain
                                      tradeoffs, and help others use it
                                      correctly
  -----------------------------------------------------------------------

For this full-stack path, not every node requires Level 5. Core syntax,
methods, OOP, collections, exceptions, generics, testing, debugging, and
application structure should trend toward **Levels 4--5**. Reflection,
modules, low-level networking, and advanced concurrency can often remain
at **Levels 2--3** until a project demands more depth.

------------------------------------------------------------------------

# Dependency Map

``` text
Java Platform
     │
     ▼
Syntax ──→ Types ──→ Operators ──→ Control Flow
     │                                  │
     └──────────────────────────────────┘
                       │
                       ▼
                    Methods
                       │
              ┌────────┴────────┐
              ▼                 ▼
        Arrays/Strings     Classes/Objects
                                │
                                ▼
                              OOP
                 ┌──────────────┼──────────────┐
                 ▼              ▼              ▼
            Interfaces      Packages       Exceptions
                 │              │              │
                 └──────────────┼──────────────┘
                                ▼
                             Generics
                                │
                                ▼
                           Collections
                                │
                    ┌───────────┴───────────┐
                    ▼                       ▼
                 Lambdas                  Records
                    │
                    ▼
                  Streams
                    │
        ┌───────────┼────────────┐
        ▼           ▼            ▼
       I/O       java.time    Optional
        │
        └───────────┬────────────┘
                    ▼
             Application Logic
                    │
       ┌────────────┼─────────────┐
       ▼            ▼             ▼
   Concurrency   Networking   Annotations
                                  │
                                  ▼
                              Reflection
       └────────────┬─────────────┘
                    ▼
               Maven / Build
                    │
                    ▼
                  Testing
                    │
                    ▼
          Debugging & Logging
                    │
                    ▼
          Application Architecture
                    │
                    ▼
        Professional Java Foundation
                    │
                    ▼
             Spring / Spring Boot
             (separate skill tree)
```

------------------------------------------------------------------------

# Highest-Priority Skills for Full-Stack Development

1.  Methods and problem decomposition
2.  Classes, objects, and constructors
3.  Encapsulation, abstraction, polymorphism, interfaces, and
    composition
4.  `List`, `Set`, `Map`, `Queue`, and common implementations
5.  Generics
6.  Exception handling
7.  Strings and data transformation
8.  Lambdas and streams
9.  Records and DTO-style data objects
10. `java.time`
11. Maven
12. JUnit and testable design
13. Stack traces and debugging
14. Annotations and dependency-injection concepts
15. HTTP and backend architecture
16. Concurrency awareness
17. Clean, maintainable application structure

------------------------------------------------------------------------

# Lower-Priority / Awareness Topics for This Path

These are useful Java topics, but they should not block progression
toward full-stack development:

-   Bit-level programming
-   Low-level socket programming
-   Deep JVM internals
-   Garbage-collector tuning
-   Custom class loaders
-   Advanced reflection
-   Deep Java Platform Module System design
-   Fork/Join internals
-   Native methods/JNI
-   Desktop AWT
-   Swing
-   JavaBeans as a desktop component model
-   Legacy collection classes such as `Vector`, `Stack`, `Hashtable`,
    and `Dictionary`
-   Legacy date/time APIs when `java.time` is available
-   Low-level thread manipulation when higher-level concurrency
    utilities are appropriate

------------------------------------------------------------------------

# Final Mastery Check

A full-stack-oriented learner can call the Java foundation
professionally useful when they can independently:

-   [ ] Create a Java/Maven project
-   [ ] Design a small class hierarchy
-   [ ] Decide between inheritance, interfaces, and composition
-   [ ] Write constructors and methods
-   [ ] Apply access modifiers intentionally
-   [ ] Create immutable-style data objects
-   [ ] Use records appropriately
-   [ ] Use `ArrayList`, `HashMap`, `HashSet`, and `ArrayDeque`
-   [ ] Select collections based on the problem
-   [ ] Write and understand generic types
-   [ ] Sort data with `Comparator`
-   [ ] Filter/map/group data with streams
-   [ ] Handle absent values safely
-   [ ] Design and handle exceptions
-   [ ] Read/write files safely
-   [ ] Work correctly with dates and timestamps
-   [ ] Send HTTP requests with Java's HTTP client
-   [ ] Explain thread safety and identify obvious shared-state problems
-   [ ] Explain annotations and how frameworks can process them
-   [ ] Add/manage Maven dependencies
-   [ ] Write meaningful JUnit tests
-   [ ] Read a stack trace and locate the actual failure
-   [ ] Debug a multi-class application
-   [ ] Separate business logic from I/O and infrastructure
-   [ ] Read unfamiliar professional Java code and trace execution
-   [ ] Use official API documentation to learn an unfamiliar class or
    method independently

------------------------------------------------------------------------

# Next Skill Trees

``` text
Java
  │
  ├── Spring / Spring Boot
  │     ├── Dependency Injection
  │     ├── REST APIs
  │     ├── Validation
  │     ├── Configuration
  │     ├── Spring Data JPA
  │     ├── Security
  │     └── Testing
  │
  ├── PostgreSQL
  ├── Git
  ├── Angular / React
  ├── Docker
  └── CI/CD & Deployment
```

The purpose of this tree is to ensure that when frameworks introduce
abstractions, the underlying **Java language, object model, collections,
generics, exceptions, functional features, concurrency concepts, build
system, and testing practices are already familiar**.
