# Java Multithreading & Concurrency Skill Tree --- Full-Stack Developer Path

> **Goal:** Progress from no concurrency experience to professionally
> competent use of Java multithreading and concurrency in full-stack
> applications.
>
> **Approach:** Learn general concurrency concepts first, implement them
> with Java, then progress from low-level thread mechanics to the
> higher-level abstractions preferred in modern Java applications.
>
> **Target level:** Professional full-stack Java developer --- not JVM
> concurrency researcher, distributed-systems specialist, or low-level
> scheduler engineer.
>
> **Learning progression:**
> `Java Fundamentals → Concurrency Fundamentals → Java Threading → Synchronization → High-Level Concurrency → Modern Java Concurrency → Application Concurrency → Spring Boot Readiness`

------------------------------------------------------------------------

# Skill Tree Overview

``` text
Java Multithreading & Concurrency
├── 1. Prerequisite Java
├── 2. Processes, Threads & Tasks
├── 3. Concurrency vs Parallelism
├── 4. Thread Creation & Execution
├── 5. Thread Lifecycle
├── 6. Interruption & Cancellation
├── 7. Shared Mutable State
├── 8. Race Conditions
├── 9. Atomicity
├── 10. Memory Visibility
├── 11. Java Memory Model
├── 12. synchronized
├── 13. volatile
├── 14. Atomic Variables
├── 15. Explicit Locks
├── 16. Thread Coordination
├── 17. Deadlock, Livelock & Starvation
├── 18. Immutability & Thread Confinement
├── 19. Executor Framework
├── 20. Thread Pools
├── 21. Callable & Future
├── 22. CompletableFuture
├── 23. Concurrent Collections
├── 24. Blocking Queues & Producer/Consumer
├── 25. Coordination Utilities
├── 26. Fork/Join
├── 27. Parallel Streams
├── 28. Virtual Threads
├── 29. CPU-Bound vs I/O-Bound Work
├── 30. Resource Limits & Backpressure
├── 31. Database Concurrency
├── 32. Web Application Concurrency
├── 33. Thread-Safe Application Design
├── 34. Testing Concurrent Code
├── 35. Debugging Concurrent Code
├── 36. Performance & Profiling
├── 37. Professional Concurrency Practices
└── 38. Spring Boot Readiness
```

------------------------------------------------------------------------

# Dependency Map

``` text
                     Java Fundamentals
                            │
          ┌─────────────────┼─────────────────┐
          ▼                 ▼                 ▼
       Classes          Collections        Lambdas
          │                 │                 │
          └─────────────────┼─────────────────┘
                            ▼
                 Concurrency Fundamentals
                            │
                            ▼
                     Java Threads
                            │
                            ▼
                  Shared Mutable State
                            │
               ┌────────────┼────────────┐
               ▼            ▼            ▼
          Atomicity     Visibility    Ordering
               │            │            │
               └────────────┼────────────┘
                            ▼
                 Synchronization
                            │
              ┌─────────────┼──────────────┐
              ▼             ▼              ▼
          synchronized    Locks         Atomics
              │             │              │
              └─────────────┼──────────────┘
                            ▼
                 High-Level Concurrency
                            │
          ┌─────────────────┼──────────────────┐
          ▼                 ▼                  ▼
      Executors          Futures        Concurrent
                                         Collections
          │                 │
          │                 ▼
          │          CompletableFuture
          │
          └─────────────────┬──────────────────┐
                            ▼                  ▼
                     Virtual Threads      Fork/Join
                            │
                            ▼
                  Application Concurrency
                            │
             ┌──────────────┼──────────────┐
             ▼              ▼              ▼
           JDBC         Web Apps      Spring Boot
```

------------------------------------------------------------------------

# Tier 0 --- Prerequisite Java

## 1. Required Java Skills

-   [ ] Variables and primitive/reference types
-   [ ] Methods
-   [ ] Classes and objects
-   [ ] Constructors
-   [ ] Interfaces
-   [ ] Inheritance and polymorphism
-   [ ] Exceptions
-   [ ] Generics
-   [ ] Collections
-   [ ] Lambdas
-   [ ] Functional interfaces
-   [ ] Method references
-   [ ] `Optional` basics
-   [ ] Try-with-resources
-   [ ] Basic Maven usage

## 2. Helpful Supporting Skills

-   [ ] JUnit fundamentals
-   [ ] Mockito fundamentals
-   [ ] JDBC fundamentals
-   [ ] Basic operating-system process knowledge
-   [ ] Basic CPU/core knowledge

**Checkpoint:** Be able to write a Java program containing multiple
cooperating classes, collections, lambdas, and exception handling before
introducing concurrency.

------------------------------------------------------------------------

# Tier 1 --- Processes, Threads & Tasks

## 3. Processes

-   [ ] Define a process
-   [ ] Understand process memory isolation conceptually
-   [ ] Understand that an operating system can run multiple processes
-   [ ] Distinguish application process from thread
-   [ ] Recognize inter-process communication as a separate concern

## 4. Threads

-   [ ] Define a thread
-   [ ] Understand multiple threads within one process
-   [ ] Understand that threads can share process memory
-   [ ] Understand per-thread execution stacks conceptually
-   [ ] Understand why shared memory creates concurrency hazards
-   [ ] Understand OS/JVM scheduling at a high level

``` text
Java Process
│
├── Thread A
├── Thread B
├── Thread C
└── Shared Heap
```

## 5. Tasks vs Threads

-   [ ] Understand a **task** as work to perform
-   [ ] Understand a **thread** as an execution mechanism
-   [ ] Avoid assuming one logical task must permanently correspond to
    one thread
-   [ ] Prepare for executors and virtual threads

------------------------------------------------------------------------

# Tier 2 --- Concurrency vs Parallelism

## 6. Sequential Execution

``` text
Task A → Task B → Task C
```

-   [ ] Understand sequential execution
-   [ ] Establish sequential behavior as the baseline

## 7. Concurrency

-   [ ] Define concurrency
-   [ ] Understand overlapping progress
-   [ ] Understand task interleaving
-   [ ] Understand that concurrency does not require simultaneous CPU
    execution

## 8. Parallelism

-   [ ] Define parallelism
-   [ ] Understand simultaneous execution on multiple cores
-   [ ] Distinguish parallelism from concurrency
-   [ ] Recognize CPU-bound workloads that may benefit from parallelism

``` text
Concurrency:
CPU: A → B → A → C → B

Parallelism:
Core 1: A ─────────→
Core 2: B ─────────→
Core 3: C ─────────→
```

## 9. Nondeterminism

-   [ ] Understand that thread scheduling is not normally predictable
-   [ ] Understand execution interleavings
-   [ ] Avoid relying on timing assumptions
-   [ ] Recognize why concurrency bugs may appear intermittently

**Checkpoint:** Explain concurrency, parallelism, and multithreading
without using the terms interchangeably.

------------------------------------------------------------------------

# Tier 3 --- Java Thread Fundamentals

## 10. `Thread`

-   [ ] Create a `Thread`
-   [ ] Pass work to a thread
-   [ ] Name threads
-   [ ] Inspect the current thread
-   [ ] Understand daemon vs non-daemon threads conceptually

## 11. `Runnable`

``` java
Runnable task = () -> {
    System.out.println("Running");
};

Thread thread = new Thread(task);
thread.start();
```

-   [ ] Understand `Runnable`
-   [ ] Implement `Runnable`
-   [ ] Use lambdas for simple tasks
-   [ ] Separate task definition from thread creation

## 12. `start()` vs `run()`

**Critical distinction:**

``` text
thread.run()
    │
    └── ordinary method call on current thread

thread.start()
    │
    └── requests a new thread of execution,
        which invokes run()
```

-   [ ] Explain why calling `run()` does not start a new thread
-   [ ] Start threads correctly

## 13. `sleep()`

-   [ ] Use `Thread.sleep()`
-   [ ] Understand that sleep pauses the current thread
-   [ ] Understand interruption during sleep
-   [ ] Avoid using arbitrary sleeps as synchronization

## 14. `join()`

-   [ ] Wait for another thread to terminate
-   [ ] Understand basic coordination through `join()`
-   [ ] Handle interruption

**Checkpoint:** Start multiple threads, observe nondeterministic output,
and wait for them to complete correctly.

------------------------------------------------------------------------

# Tier 4 --- Thread Lifecycle

## 15. Java Thread States

Understand:

-   [ ] `NEW`
-   [ ] `RUNNABLE`
-   [ ] `BLOCKED`
-   [ ] `WAITING`
-   [ ] `TIMED_WAITING`
-   [ ] `TERMINATED`

## 16. State Transitions

``` text
NEW
 │ start()
 ▼
RUNNABLE
 │
 ├── lock unavailable ──→ BLOCKED
 ├── wait/join ─────────→ WAITING
 ├── sleep/timed wait ──→ TIMED_WAITING
 │
 ▼
TERMINATED
```

-   [ ] Inspect thread state
-   [ ] Understand that JVM states do not map perfectly to every OS
    scheduling state
-   [ ] Use thread states when diagnosing blocking problems

------------------------------------------------------------------------

# Tier 5 --- Interruption & Cancellation

## 17. Thread Interruption

-   [ ] Understand cooperative interruption
-   [ ] `interrupt()`
-   [ ] `isInterrupted()`
-   [ ] `Thread.interrupted()`
-   [ ] Understand `InterruptedException`
-   [ ] Preserve interrupt status when appropriate
-   [ ] Avoid swallowing interruption accidentally

## 18. Cooperative Cancellation

-   [ ] Design tasks that can stop
-   [ ] Check interruption where appropriate
-   [ ] Clean up resources during cancellation
-   [ ] Understand why forcibly killing arbitrary threads is unsafe
-   [ ] Understand cancellation in executor/future contexts later

**Checkpoint:** Write a long-running task that shuts down cleanly when
interrupted.

------------------------------------------------------------------------

# Tier 6 --- Shared Mutable State

## 19. Shared State

``` java
class Counter {
    int value = 0;
}
```

If multiple threads access the same `Counter`, its state is shared.

-   [ ] Identify shared data
-   [ ] Identify mutable data
-   [ ] Understand why shared + mutable + concurrent access is dangerous
-   [ ] Distinguish local variables from shared object state
-   [ ] Recognize static mutable state as shared state

## 20. The Core Concurrency Question

For every shared value, ask:

``` text
Who can access this?
        │
        ▼
Can multiple threads access it?
        │
        ▼
Can any thread modify it?
        │
        ▼
What coordinates those accesses?
```

------------------------------------------------------------------------

# Tier 7 --- Race Conditions

## 21. Race Condition

Example:

``` java
count++;
```

Conceptually:

``` text
read count
   ↓
add 1
   ↓
write count
```

Two threads can interleave these steps.

-   [ ] Define race condition
-   [ ] Recognize read-modify-write races
-   [ ] Understand lost updates
-   [ ] Reproduce a simple race experimentally
-   [ ] Understand why a bug may disappear while debugging

## 22. Check-Then-Act

Recognize:

``` text
if condition is true
    perform action
```

as potentially unsafe if another thread can change the condition between
the check and action.

-   [ ] Identify compound operations
-   [ ] Understand why individually safe operations may form an unsafe
    sequence

------------------------------------------------------------------------

# Tier 8 --- Atomicity

## 23. Atomic Operations

-   [ ] Define atomicity
-   [ ] Understand indivisible logical operations
-   [ ] Distinguish atomicity from visibility
-   [ ] Understand why `count++` is not an atomic increment
-   [ ] Recognize compound state transitions

## 24. Critical Sections

-   [ ] Identify code that must execute atomically relative to competing
    operations
-   [ ] Keep critical sections appropriately scoped
-   [ ] Understand that excessive synchronization can reduce concurrency

------------------------------------------------------------------------

# Tier 9 --- Memory Visibility & Ordering

## 25. Visibility

-   [ ] Understand that one thread's write may require synchronization
    guarantees before another thread is guaranteed to observe it
-   [ ] Distinguish visibility from atomicity
-   [ ] Recognize stale-read problems conceptually

## 26. Ordering

-   [ ] Understand that compiler/JVM/CPU optimizations can reorder
    operations within allowed memory-model rules
-   [ ] Avoid reasoning about concurrent code solely from source-code
    line order
-   [ ] Understand that synchronization establishes ordering guarantees

------------------------------------------------------------------------

# Tier 10 --- Java Memory Model

## 27. Java Memory Model Fundamentals

-   [ ] Understand the JMM as the rules governing inter-thread memory
    effects
-   [ ] Understand visibility
-   [ ] Understand ordering
-   [ ] Understand atomicity
-   [ ] Understand synchronization actions conceptually

## 28. Happens-Before

**High-priority conceptual skill.**

-   [ ] Understand happens-before as a visibility/order guarantee
-   [ ] Lock release → later acquisition of same lock
-   [ ] Volatile write → later volatile read
-   [ ] Thread start relationships
-   [ ] Thread completion/join relationships
-   [ ] Understand transitivity conceptually

## 29. Safe Publication

-   [ ] Understand why constructing an object is not the entire
    thread-safety story
-   [ ] Publish shared objects safely
-   [ ] Recognize synchronization, volatile references, concurrent
    collections, static initialization, and other safe-publication
    mechanisms conceptually

**Target depth:** Correct application reasoning, not formal JVM
memory-model research.

------------------------------------------------------------------------

# Tier 11 --- `synchronized`

## 30. Intrinsic Locks / Monitors

-   [ ] Understand every Java object can participate in intrinsic
    locking
-   [ ] Understand mutual exclusion
-   [ ] Understand monitor ownership conceptually

## 31. Synchronized Methods

``` java
public synchronized void increment() {
    count++;
}
```

-   [ ] Understand which object is locked
-   [ ] Understand static synchronized locking at a high level

## 32. Synchronized Blocks

``` java
synchronized (lock) {
    count++;
}
```

-   [ ] Choose a lock object
-   [ ] Reduce lock scope
-   [ ] Protect related state consistently
-   [ ] Avoid exposing lock objects unnecessarily

## 33. Synchronization Guarantees

-   [ ] Mutual exclusion
-   [ ] Visibility
-   [ ] Happens-before relationships
-   [ ] Reentrant intrinsic locks

**Checkpoint:** Correct a race condition with `synchronized` and explain
why the correction works.

------------------------------------------------------------------------

# Tier 12 --- `volatile`

## 34. Volatile Variables

``` java
private volatile boolean running = true;
```

-   [ ] Understand visibility guarantees
-   [ ] Understand ordering effects
-   [ ] Understand volatile read/write happens-before relationship
-   [ ] Recognize appropriate status/configuration flag use cases

## 35. What `volatile` Does NOT Do

``` java
volatile int count;
count++;
```

does not make the compound increment atomic.

-   [ ] Explain why volatile does not replace locking for compound
    operations
-   [ ] Distinguish visibility problem from atomicity problem
-   [ ] Avoid using volatile as a universal thread-safety fix

------------------------------------------------------------------------

# Tier 13 --- Atomic Variables

## 36. Atomic Classes

-   [ ] `AtomicInteger`
-   [ ] `AtomicLong`
-   [ ] `AtomicBoolean`
-   [ ] `AtomicReference`
-   [ ] Understand atomic read-modify-write operations

## 37. Common Operations

-   [ ] `get()`
-   [ ] `set()`
-   [ ] `incrementAndGet()`
-   [ ] `getAndIncrement()`
-   [ ] `compareAndSet()`
-   [ ] `updateAndGet()`

## 38. Compare-And-Set

-   [ ] Understand CAS conceptually
-   [ ] Understand optimistic atomic updates
-   [ ] Recognize retry loops conceptually
-   [ ] Avoid implementing complex lock-free algorithms unnecessarily

## 39. Contended Counters Awareness

-   [ ] Recognize `LongAdder`
-   [ ] Understand why highly contended counters may use different
    strategies
-   [ ] Know when simple `AtomicLong` is sufficient

------------------------------------------------------------------------

# Tier 14 --- Explicit Locks

## 40. `Lock`

-   [ ] Understand explicit locking
-   [ ] `lock()`
-   [ ] `unlock()`
-   [ ] Always release locks safely

``` java
lock.lock();
try {
    // protected work
} finally {
    lock.unlock();
}
```

## 41. `ReentrantLock`

-   [ ] Understand reentrancy
-   [ ] `tryLock()`
-   [ ] Interruptible lock acquisition awareness
-   [ ] Fairness option awareness
-   [ ] Know when `synchronized` is simpler

## 42. Read/Write Locks

-   [ ] Understand `ReadWriteLock`
-   [ ] Multiple readers / exclusive writer concept
-   [ ] Recognize read-heavy use cases
-   [ ] Avoid assuming read/write locks automatically improve
    performance

## 43. `StampedLock` Awareness

-   [ ] Recognize optimistic reads
-   [ ] Understand non-reentrant caveats conceptually
-   [ ] Lower priority for full-stack development

------------------------------------------------------------------------

# Tier 15 --- Thread Coordination

## 44. `wait()`

-   [ ] Understand waiting on an object's monitor
-   [ ] Call while holding the monitor
-   [ ] Understand lock release while waiting
-   [ ] Re-check conditions in a loop

## 45. `notify()` / `notifyAll()`

-   [ ] Wake waiting threads
-   [ ] Understand notification does not immediately transfer the lock
-   [ ] Understand why `notifyAll()` is often safer in general condition
    designs
-   [ ] Recognize missed/incorrect signaling risks

## 46. Condition Loops

``` java
synchronized (lock) {
    while (!conditionIsTrue()) {
        lock.wait();
    }

    // proceed
}
```

-   [ ] Understand spurious wakeups
-   [ ] Recheck the condition

## 47. `Condition`

-   [ ] Understand explicit lock conditions
-   [ ] `await()`
-   [ ] `signal()`
-   [ ] `signalAll()`
-   [ ] Prefer higher-level coordination utilities when they directly
    model the problem

**Learning goal:** Understand low-level coordination so higher-level
abstractions make sense, not because most application code should be
built from `wait()`/`notify()`.

------------------------------------------------------------------------

# Tier 16 --- Common Concurrency Failures

## 48. Deadlock

``` text
Thread A owns Lock 1
    │
    └── waits for Lock 2

Thread B owns Lock 2
    │
    └── waits for Lock 1
```

-   [ ] Define deadlock
-   [ ] Recognize circular lock dependencies
-   [ ] Use consistent lock ordering
-   [ ] Minimize nested locking
-   [ ] Diagnose deadlock with thread dumps

## 49. Livelock

-   [ ] Define livelock
-   [ ] Understand threads can remain active without making progress
-   [ ] Recognize excessive mutual retry/yield behavior

## 50. Starvation

-   [ ] Define starvation
-   [ ] Understand unfair access to resources
-   [ ] Recognize long-held locks/resource monopolization

## 51. Contention

-   [ ] Define lock/resource contention
-   [ ] Understand performance effects
-   [ ] Reduce unnecessary shared state
-   [ ] Keep critical sections focused

## 52. Priority Issues --- Awareness

-   [ ] Understand thread priority exists
-   [ ] Avoid relying on priority for correctness
-   [ ] Recognize priority inversion conceptually

------------------------------------------------------------------------

# Tier 17 --- Immutability & Thread Confinement

## 53. Immutability

-   [ ] Understand why immutable objects simplify concurrency
-   [ ] Use final fields appropriately
-   [ ] Avoid setters/mutation when unnecessary
-   [ ] Understand safe construction/publication requirements
-   [ ] Prefer immutable data transfer where practical

## 54. Thread Confinement

-   [ ] Keep state local to one thread/task when possible
-   [ ] Prefer local variables
-   [ ] Understand request-local state conceptually
-   [ ] Recognize `ThreadLocal`
-   [ ] Understand cleanup/leak concerns with thread-local state,
    especially with pools

## 55. Reduce Sharing

Core design principle:

``` text
Best synchronization problem:
the shared mutable state that never existed.
```

-   [ ] Prefer ownership
-   [ ] Prefer immutability
-   [ ] Prefer message/task passing where suitable
-   [ ] Synchronize only where sharing is actually required

------------------------------------------------------------------------

# Tier 18 --- Executor Framework

## 56. Why Executors Exist

Instead of:

``` java
new Thread(task).start();
```

application code often separates:

``` text
Task submission
      │
      ▼
Executor
      │
      ▼
Execution strategy
```

-   [ ] Understand `Executor`
-   [ ] Understand task/execution separation
-   [ ] Understand lifecycle management

## 57. `ExecutorService`

-   [ ] `execute()`
-   [ ] `submit()`
-   [ ] `shutdown()`
-   [ ] `shutdownNow()`
-   [ ] `awaitTermination()`
-   [ ] Understand graceful shutdown
-   [ ] Avoid leaking executor threads

## 58. Executor Creation

-   [ ] Understand fixed pools
-   [ ] Understand single-thread executors
-   [ ] Understand cached-pool behavior conceptually
-   [ ] Understand scheduled executors
-   [ ] Prefer deliberate resource bounds over blindly choosing factory
    methods

------------------------------------------------------------------------

# Tier 19 --- Thread Pools

## 59. Why Pools Exist

Traditional platform threads consume resources.

``` text
Tasks
 │ │ │ │ │ │ │
 ▼ ▼ ▼ ▼ ▼ ▼ ▼
┌───────────────┐
│  Thread Pool  │
│ T1  T2  T3 T4 │
└───────────────┘
        │
        ▼
      Work
```

-   [ ] Reuse worker threads
-   [ ] Limit concurrency
-   [ ] Queue work
-   [ ] Understand saturation

## 60. Pool Sizing

-   [ ] CPU-bound reasoning
-   [ ] I/O-bound reasoning
-   [ ] Understand workload characteristics
-   [ ] Understand resource constraints beyond CPU
-   [ ] Avoid treating a single formula as universally correct

## 61. Queues & Rejection

-   [ ] Understand work queues
-   [ ] Bounded vs unbounded queues
-   [ ] Understand rejected execution conceptually
-   [ ] Recognize `RejectedExecutionHandler`
-   [ ] Understand why unbounded queues can hide overload until
    memory/latency becomes problematic

------------------------------------------------------------------------

# Tier 20 --- Callable & Future

## 62. `Callable`

``` java
Callable<Integer> task = () -> calculate();
```

-   [ ] Understand returning a result
-   [ ] Understand checked exception capability
-   [ ] Compare `Callable` with `Runnable`

## 63. `Future`

-   [ ] Receive a future from `submit()`
-   [ ] `get()`
-   [ ] Timed `get()`
-   [ ] `isDone()`
-   [ ] `cancel()`
-   [ ] Understand blocking
-   [ ] Understand `ExecutionException`
-   [ ] Understand cancellation semantics

## 64. Limitations of Basic Futures

-   [ ] Difficult composition
-   [ ] Blocking retrieval
-   [ ] Prepare for `CompletableFuture`

------------------------------------------------------------------------

# Tier 21 --- CompletableFuture

## 65. Creating Async Work

-   [ ] `runAsync()`
-   [ ] `supplyAsync()`
-   [ ] Understand default executor behavior
-   [ ] Supply an explicit executor when appropriate

## 66. Transforming Results

-   [ ] `thenApply()`
-   [ ] `thenAccept()`
-   [ ] `thenRun()`

## 67. Composing Operations

-   [ ] `thenCompose()`
-   [ ] Understand flattening dependent async operations
-   [ ] `thenCombine()`
-   [ ] Combine independent results
-   [ ] `allOf()`
-   [ ] `anyOf()` awareness

## 68. Error Handling

-   [ ] `exceptionally()`
-   [ ] `handle()`
-   [ ] `whenComplete()`
-   [ ] Understand exception propagation through stages

## 69. Async vs Non-Async Variants

-   [ ] Understand execution-thread differences
-   [ ] Recognize `thenApply()` vs `thenApplyAsync()`
-   [ ] Avoid unnecessary async boundaries

## 70. Professional Judgment

-   [ ] Avoid deeply unreadable chains
-   [ ] Avoid blocking inside supposedly asynchronous designs without
    understanding the consequence
-   [ ] Choose simple synchronous code when concurrency provides no
    benefit

**Checkpoint:** Run two independent operations concurrently, combine
their results, and handle failures cleanly.

------------------------------------------------------------------------

# Tier 22 --- Concurrent Collections

## 71. Why Ordinary Collections Can Be Unsafe

-   [ ] Understand concurrent mutation problems
-   [ ] Understand iteration hazards
-   [ ] Do not assume `ArrayList`, `HashMap`, or `HashSet` are
    thread-safe

## 72. `ConcurrentHashMap`

-   [ ] Concurrent access
-   [ ] Atomic compound operations such as `putIfAbsent()`
-   [ ] `computeIfAbsent()`
-   [ ] Understand weakly consistent iteration conceptually
-   [ ] Avoid external check-then-act when collection operations already
    provide atomic alternatives

## 73. `CopyOnWriteArrayList`

-   [ ] Understand copy-on-write behavior
-   [ ] Recognize read-heavy/write-light use cases
-   [ ] Understand expensive writes

## 74. Concurrent Queues

-   [ ] `ConcurrentLinkedQueue`
-   [ ] Understand non-blocking queue use conceptually

## 75. Choosing Collections

``` text
Need concurrent key/value access?
        → ConcurrentHashMap

Need producer/consumer waiting?
        → BlockingQueue

Many reads, very few writes?
        → consider CopyOnWriteArrayList
```

-   [ ] Choose based on access pattern rather than name alone

------------------------------------------------------------------------

# Tier 23 --- Blocking Queues & Producer/Consumer

## 76. `BlockingQueue`

-   [ ] Understand blocking insertion/removal
-   [ ] `put()`
-   [ ] `take()`
-   [ ] Timed operations
-   [ ] Bounded queues
-   [ ] Understand natural backpressure

## 77. Producer / Consumer Pattern

``` text
Producer
   │
   ▼
┌──────────────┐
│ BlockingQueue│
└──────────────┘
   │
   ▼
Consumer
```

-   [ ] Separate producers from consumers
-   [ ] Use queue capacity intentionally
-   [ ] Coordinate shutdown
-   [ ] Handle interruption
-   [ ] Understand multiple producers/consumers

**Checkpoint:** Implement producer/consumer with `BlockingQueue` rather
than low-level `wait()`/`notify()`.

------------------------------------------------------------------------

# Tier 24 --- Coordination Utilities

## 78. `CountDownLatch`

-   [ ] Wait for N events/tasks
-   [ ] `countDown()`
-   [ ] `await()`
-   [ ] Understand one-shot behavior

## 79. `Semaphore`

-   [ ] Limit simultaneous access to a resource
-   [ ] `acquire()`
-   [ ] `release()`
-   [ ] Always release permits correctly
-   [ ] Model limited external resources

## 80. `CyclicBarrier`

-   [ ] Coordinate threads reaching a common phase
-   [ ] Understand reusable barrier behavior
-   [ ] Recognize suitable parallel algorithms

## 81. `Phaser` Awareness

-   [ ] Understand multi-phase coordination conceptually
-   [ ] Recognize dynamic party registration
-   [ ] Lower priority for ordinary full-stack applications

------------------------------------------------------------------------

# Tier 25 --- Fork/Join

## 82. Fork/Join Model

-   [ ] Understand divide-and-conquer parallelism
-   [ ] Understand work stealing conceptually
-   [ ] Recognize `ForkJoinPool`
-   [ ] Understand recursive task decomposition

## 83. Task Types

-   [ ] `RecursiveTask`
-   [ ] `RecursiveAction`
-   [ ] Fork
-   [ ] Join

## 84. Appropriate Workloads

-   [ ] CPU-bound recursive work
-   [ ] Large decomposable calculations
-   [ ] Avoid using Fork/Join for ordinary blocking I/O without
    understanding pool implications

**Target depth:** Working awareness/application competence, not custom
scheduler engineering.

------------------------------------------------------------------------

# Tier 26 --- Parallel Streams

## 85. Parallel Stream Fundamentals

``` java
items.parallelStream()
     .map(...)
     .filter(...)
     .toList();
```

-   [ ] Understand parallel stream execution conceptually
-   [ ] Understand common pool relationship
-   [ ] Understand ordering implications
-   [ ] Understand side-effect dangers
-   [ ] Understand reduction requirements

## 86. When NOT to Use Parallel Streams

-   [ ] Small workloads

-   [ ] Blocking I/O-heavy operations

-   [ ] Shared mutable side effects

-   [ ] Workloads without measured benefit

-   [ ] Code where execution-resource control matters

-   [ ] Benchmark rather than assume parallel is faster

------------------------------------------------------------------------

# Tier 27 --- Virtual Threads

## 87. Virtual Thread Model

Modern Java provides lightweight virtual threads.

``` text
Application Tasks

V1 V2 V3 V4 V5 V6 V7 V8 ...
 │  │  │  │  │  │  │  │
 └──────── JVM Scheduling ───────┐
                                 ▼
                       Platform Threads
                                 │
                                 ▼
                              OS / CPU
```

-   [ ] Understand platform threads
-   [ ] Understand virtual threads
-   [ ] Understand JVM scheduling onto carrier/platform threads
    conceptually
-   [ ] Understand why large numbers of blocking tasks become practical

## 88. Creating Virtual Threads

-   [ ] Start a virtual thread
-   [ ] Use virtual-thread builders
-   [ ] Use virtual-thread-per-task executor
-   [ ] Understand task-per-thread style

## 89. Best-Fit Workloads

-   [ ] Blocking I/O
-   [ ] Request-per-task workloads
-   [ ] Database/network calls
-   [ ] Large numbers of mostly waiting tasks

## 90. What Virtual Threads Do NOT Solve

Virtual threads do **not** eliminate:

-   [ ] Race conditions
-   [ ] Shared mutable state
-   [ ] Deadlocks
-   [ ] Atomicity requirements
-   [ ] Database connection limits
-   [ ] External API rate limits
-   [ ] Memory limits
-   [ ] CPU limits

## 91. Pinning / Runtime Awareness

-   [ ] Understand that blocking behavior and synchronization choices
    can affect virtual-thread scalability
-   [ ] Recognize pinning concerns conceptually
-   [ ] Use current Java guidance rather than memorizing outdated
    limitations
-   [ ] Measure actual application behavior

**Checkpoint:** Explain why 10,000 virtual threads do not imply that
10,000 database queries should execute simultaneously.

------------------------------------------------------------------------

# Tier 28 --- CPU-Bound vs I/O-Bound Work

## 92. CPU-Bound Work

Examples:

``` text
compression
image processing
large calculations
parsing/computation
```

-   [ ] Understand CPU saturation
-   [ ] Understand relationship to processor cores
-   [ ] Recognize when additional concurrency stops helping

## 93. I/O-Bound Work

Examples:

``` text
database query
HTTP request
file access
network wait
```

-   [ ] Understand waiting vs computing
-   [ ] Understand why more concurrent tasks may improve throughput
-   [ ] Understand why virtual threads are especially relevant

## 94. Mixed Workloads

-   [ ] Separate bottlenecks
-   [ ] Avoid one concurrency strategy for every workload
-   [ ] Measure CPU, wait time, queueing, and external limits

------------------------------------------------------------------------

# Tier 29 --- Resource Limits & Backpressure

## 95. Concurrency Is Not Infinite

``` text
10,000 Tasks
      │
      ▼
10,000 Virtual Threads
      │
      ▼
20 DB Connections
```

The database remains limited to approximately the available useful
connection concurrency.

-   [ ] Identify downstream capacity
-   [ ] Database connections
-   [ ] HTTP connection limits
-   [ ] File handles
-   [ ] Memory
-   [ ] CPU
-   [ ] API quotas

## 96. Backpressure

-   [ ] Understand overload
-   [ ] Bound queues where appropriate
-   [ ] Limit concurrency
-   [ ] Reject/defer work intentionally
-   [ ] Use semaphores or resource pools where suitable
-   [ ] Understand throughput vs latency tradeoffs

## 97. Little's Law Awareness

-   [ ] Recognize relationship among concurrency, throughput, and
    latency conceptually
-   [ ] Use measurements rather than arbitrary thread counts
-   [ ] Deep queueing-theory mathematics not required

------------------------------------------------------------------------

# Tier 30 --- Database Concurrency

> **Preview note:** This tier previews how concurrency concerns show up
> in database access — you'll get the full JDBC and PostgreSQL
> treatment (connection pooling, transaction isolation levels, etc.)
> in their own dedicated skill trees later in this curriculum. For
> now, focus on the concurrency principles below, not the JDBC/SQL
> specifics.

## 98. JDBC + Threads

``` text
Thread / Task A ──┐
Thread / Task B ──┤
Thread / Task C ──┤
                  ▼
            Connection Pool
             │   │   │
             ▼   ▼   ▼
             PostgreSQL
```

-   [ ] Understand that JDBC connections generally represent independent
    database sessions
-   [ ] Avoid casually sharing one `Connection` across unrelated
    concurrent work
-   [ ] Acquire/release connections according to application/pool design
-   [ ] Understand pool size as a concurrency limit
-   [ ] Understand transaction boundaries per operation/request

## 99. Database Transactions

-   [ ] Understand concurrent transactions
-   [ ] Recognize isolation levels
-   [ ] Recognize locking
-   [ ] Recognize deadlocks
-   [ ] Recognize lost-update/business concurrency problems
-   [ ] Understand optimistic vs pessimistic concurrency conceptually
-   [ ] Keep Java synchronization distinct from database transaction
    isolation

### Critical distinction

``` text
Java lock
    → coordinates threads in one JVM/process

Database transaction/lock
    → coordinates access to database state,
      potentially across many application processes
```

------------------------------------------------------------------------

# Tier 31 --- Web Application Concurrency

## 100. Concurrent Requests

A server may handle many requests at once:

``` text
Request A ──→ Application
Request B ──→ Application
Request C ──→ Application
Request D ──→ Application
```

-   [ ] Understand that application components can be invoked
    concurrently
-   [ ] Avoid request-specific mutable data in shared singleton objects
-   [ ] Keep request data local
-   [ ] Understand stateless service design

## 101. Shared Application State

Potentially dangerous:

``` java
class MessageService {
    private String currentUser;
}
```

if one service object is shared across concurrent requests.

Prefer:

``` java
Message create(String user, String body) {
    // request-specific state stays local
}
```

-   [ ] Identify shared service state
-   [ ] Prefer stateless services
-   [ ] Protect truly shared mutable state intentionally

------------------------------------------------------------------------

# Tier 32 --- Thread-Safe Application Design

## 102. Thread Safety

-   [ ] Define thread-safe behavior
-   [ ] Identify class invariants
-   [ ] Determine which state is shared
-   [ ] Document concurrency assumptions where necessary

## 103. Preferred Design Order

When solving a concurrency problem, consider:

``` text
1. Can sharing be removed?
        ↓ no
2. Can the state be immutable?
        ↓ no
3. Can ownership/confinement solve it?
        ↓ no
4. Is a concurrent collection/atomic
   or higher-level utility sufficient?
        ↓ no
5. Use explicit synchronization/locking
   with a clearly defined invariant.
```

## 104. Composition

-   [ ] Understand that individually thread-safe components do not
    automatically make a compound operation thread-safe
-   [ ] Protect multi-step invariants
-   [ ] Avoid leaking mutable internal state

------------------------------------------------------------------------

# Tier 33 --- Testing Concurrent Code

## 105. Testing Fundamentals

-   [ ] Understand why concurrency tests can be nondeterministic
-   [ ] Avoid arbitrary `sleep()` as the primary synchronization
    mechanism
-   [ ] Use latches/barriers/futures to coordinate tests
-   [ ] Test eventual completion with bounded timeouts
-   [ ] Repeat stress-oriented tests when appropriate without treating
    repetition as proof

## 106. JUnit Integration

-   [ ] Test concurrent operations with JUnit
-   [ ] Use assertions after coordinated completion
-   [ ] Use timeouts carefully
-   [ ] Ensure executors shut down after tests
-   [ ] Preserve useful failure information from worker tasks

## 107. Mockito Caution

-   [ ] Understand thread-safety assumptions of collaborators/mocks
-   [ ] Avoid overspecifying invocation order when order is
    intentionally concurrent
-   [ ] Prefer testing observable outcomes
-   [ ] Use integration/stress tests when mocking would hide the actual
    concurrency behavior

## 108. Race Testing

-   [ ] Create controlled interleavings when possible
-   [ ] Use coordination primitives rather than timing guesses
-   [ ] Understand that absence of failure does not prove thread safety
-   [ ] Use specialized stress-testing tools conceptually where higher
    assurance is required

------------------------------------------------------------------------

# Tier 34 --- Debugging Concurrent Code

## 109. Thread Dumps

-   [ ] Understand thread dumps
-   [ ] Inspect thread names
-   [ ] Inspect thread states
-   [ ] Identify blocked/waiting threads
-   [ ] Identify lock ownership
-   [ ] Recognize deadlock reports

## 110. Logging

-   [ ] Include useful thread/task context
-   [ ] Understand that log order across threads can be misleading
-   [ ] Avoid changing timing excessively while debugging
-   [ ] Correlate request/task IDs where useful

## 111. Debugger Limitations

-   [ ] Understand breakpoints alter timing
-   [ ] Recognize heisenbug-like behavior
-   [ ] Avoid concluding a bug is fixed because it disappears under a
    debugger

## 112. Failure Classification

``` text
Wrong result?
    → race / atomicity / visibility?

Never completes?
    → deadlock / missed signal / blocked I/O?

Very slow?
    → contention / queueing / pool exhaustion?

Rejected work?
    → executor saturation?

Database timeout?
    → connection pool / DB contention / slow query?
```

------------------------------------------------------------------------

# Tier 35 --- Performance & Profiling

## 113. Measure Before Optimizing

-   [ ] Establish sequential baseline
-   [ ] Measure throughput
-   [ ] Measure latency
-   [ ] Measure CPU utilization
-   [ ] Measure queue depth
-   [ ] Measure lock contention
-   [ ] Measure pool utilization
-   [ ] Measure external-resource wait time

## 114. Thread/Concurrency Profiling

-   [ ] Recognize Java Flight Recorder (JFR)
-   [ ] Recognize Java Mission Control (JMC)
-   [ ] Inspect thread activity
-   [ ] Inspect blocking
-   [ ] Inspect lock contention
-   [ ] Recognize CPU hot spots
-   [ ] Understand profiler overhead conceptually

## 115. Common Performance Mistakes

-   [ ] Too many platform threads
-   [ ] Too much locking
-   [ ] Lock scope too broad
-   [ ] Unbounded queues
-   [ ] Blocking the wrong executor
-   [ ] Excessive context switching
-   [ ] Parallelizing tiny workloads
-   [ ] Increasing application concurrency beyond downstream capacity

------------------------------------------------------------------------

# Tier 36 --- Professional Concurrency Practices

## 116. Prefer High-Level Abstractions

Understand low-level:

``` text
Thread
synchronized
wait/notify
locks
```

but normally prefer the highest-level tool that correctly models the
problem:

``` text
ExecutorService
ConcurrentHashMap
BlockingQueue
Atomics
CompletableFuture
Semaphore
Virtual Threads
```

## 117. Keep Concurrency Localized

-   [ ] Avoid making every class concurrency-aware
-   [ ] Define clear concurrency boundaries
-   [ ] Encapsulate shared state
-   [ ] Keep synchronization policy understandable
-   [ ] Document ownership when non-obvious

## 118. Lifecycle Management

-   [ ] Shut down executors
-   [ ] Handle application shutdown
-   [ ] Cancel tasks cooperatively
-   [ ] Release permits/resources
-   [ ] Close database/network resources
-   [ ] Avoid orphaned background work

## 119. Failure Handling

-   [ ] Do not silently lose worker-thread exceptions
-   [ ] Propagate/report async failures
-   [ ] Define cancellation behavior
-   [ ] Define partial-failure behavior
-   [ ] Understand timeout strategy
-   [ ] Avoid retry storms

## 120. Simplicity

-   [ ] Prefer sequential code when it meets requirements
-   [ ] Add concurrency for a reason
-   [ ] Choose readability over clever lock-free designs
-   [ ] Treat concurrency as a correctness concern before a performance
    optimization

------------------------------------------------------------------------

# Tier 37 --- Spring Boot Readiness

## 121. Spring Bean Concurrency

A common Spring service is effectively shared:

``` text
             ┌── Request A
             │
Singleton ───┼── Request B
Service      │
             └── Request C
```

-   [ ] Understand singleton bean sharing conceptually
-   [ ] Prefer stateless service beans
-   [ ] Avoid mutable request-specific instance fields
-   [ ] Understand that dependency injection does not automatically make
    code thread-safe

## 122. Controller → Service → Repository

> **Preview note:** This architecture (Controller → Service →
> Repository) is the standard layering you'll build explicitly once
> you reach the Spring Boot skill tree — it's introduced here only to
> show WHERE concurrency concerns typically live in a real
> application, not as something you need to build yet.

``` text
Concurrent HTTP Requests
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
   Connection Pool
         │
         ▼
     PostgreSQL
```

-   [ ] Understand concurrent invocation through each layer
-   [ ] Keep controller/service request state local
-   [ ] Understand repository/database resource constraints
-   [ ] Understand transaction boundaries
-   [ ] Understand that the database participates in concurrency control

## 123. Async Application Work

Prepare to encounter:

-   [ ] Framework-managed executors
-   [ ] Scheduled work
-   [ ] Async methods
-   [ ] HTTP client concurrency
-   [ ] Messaging/event consumers
-   [ ] Background jobs
-   [ ] Virtual-thread integration

The Spring skill tree should teach Spring-specific configuration. This
tree teaches the concurrency concepts needed to understand those
features.

------------------------------------------------------------------------

# Practical Progression

## Stage 1 --- Observe Threads

``` text
Main Thread
    │
    ├── start Thread A
    ├── start Thread B
    └── join both
```

**Exercise:** Run two independent tasks concurrently and observe
changing output order.

------------------------------------------------------------------------

## Stage 2 --- Create a Race

``` text
Thread A ──┐
           ▼
         Counter
           ▲
Thread B ──┘
```

**Exercise:** Have multiple threads increment the same counter and
observe lost updates.

------------------------------------------------------------------------

## Stage 3 --- Fix the Race Three Ways

Implement the counter using:

1.  `synchronized`
2.  `AtomicInteger`
3.  confinement/no shared mutable counter where the problem permits

Explain the tradeoffs.

------------------------------------------------------------------------

## Stage 4 --- Coordinate Work

**Exercise:** Implement producer/consumer first conceptually, then with
`BlockingQueue`.

Compare it with the complexity of a low-level `wait()`/`notify()`
design.

------------------------------------------------------------------------

## Stage 5 --- Executors

``` text
100 Tasks
   │
   ▼
ExecutorService
   │
   ▼
4 Worker Threads
```

**Exercise:** Submit tasks, collect futures, handle failures, and shut
down the executor cleanly.

------------------------------------------------------------------------

## Stage 6 --- CompletableFuture

``` text
API A ──────┐
            ├── combine → result
API B ──────┘
```

**Exercise:** Run two independent simulated I/O operations concurrently
and combine their results.

------------------------------------------------------------------------

## Stage 7 --- Virtual Threads

``` text
Many Blocking Tasks
        │
        ▼
Virtual Thread per Task
        │
        ▼
Limited External Resource
```

**Exercise:** Compare the conceptual design of a bounded platform-thread
pool with virtual-thread-per-task execution for blocking operations.

------------------------------------------------------------------------

## Stage 8 --- JDBC Concurrency

``` text
100 Requests
     │
     ▼
Application Tasks
     │
     ▼
10 DB Connections
     │
     ▼
PostgreSQL
```

**Exercise:** Explain what happens when 100 concurrent requests all
require database access but the connection pool contains only 10
connections.

------------------------------------------------------------------------

## Stage 9 --- Thread-Safe Service Design

**Exercise:** Review a service class containing mutable instance fields
and determine whether concurrent HTTP requests could corrupt its state.
Refactor it toward stateless/request-local data.

------------------------------------------------------------------------

## Stage 10 --- Diagnose Failure

Given a thread dump or application symptom:

-   identify deadlock,
-   identify pool exhaustion,
-   identify a race candidate,
-   identify blocking I/O,
-   identify excessive contention,
-   distinguish application-thread problems from database concurrency
    problems.

------------------------------------------------------------------------

# Knowledge Depth Scale

  -----------------------------------------------------------------------
  Level                               Meaning
  ----------------------------------- -----------------------------------
  **0 --- Unknown**                   Have not learned the concept

  **1 --- Recognize**                 Can identify it and explain its
                                      basic purpose

  **2 --- Guided**                    Can implement/use it with
                                      documentation or examples

  **3 --- Independent**               Can use it without step-by-step
                                      instructions

  **4 --- Applied**                   Can select appropriate concurrency
                                      techniques in a real application

  **5 --- Professional**              Can design, debug, test, and
                                      explain application concurrency and
                                      its tradeoffs
  -----------------------------------------------------------------------

## Target Depth for Full-Stack Development

### Levels 4--5

-   Concurrency vs parallelism
-   Shared mutable state
-   Race conditions
-   Atomicity
-   Visibility
-   `synchronized`
-   `volatile`
-   Atomics
-   Executors
-   Thread pools
-   Futures
-   Concurrent collections
-   Resource limits
-   Web-service thread safety
-   Database connection-pool concurrency
-   Debugging common concurrency failures

### Levels 3--4

-   Java Memory Model / happens-before
-   Explicit locks
-   CompletableFuture
-   BlockingQueue
-   Semaphores/latches
-   Virtual threads
-   Testing concurrent code
-   Performance measurement

### Levels 1--3

-   Low-level `wait()`/`notify()` implementation
-   Fork/Join internals
-   `StampedLock`
-   Phaser
-   Advanced lock-free algorithms
-   Deep JVM scheduler/memory-model internals

------------------------------------------------------------------------

# Highest-Priority Skills for Full-Stack Java Development

1.  Explain process vs thread
2.  Explain concurrency vs parallelism
3.  Understand nondeterministic scheduling
4.  Create and start basic Java threads
5.  Understand `Runnable`
6.  Understand interruption
7.  Identify shared mutable state
8.  Recognize race conditions
9.  Understand atomicity
10. Understand memory visibility
11. Understand happens-before at an application level
12. Use `synchronized`
13. Use `volatile` correctly
14. Use atomic variables
15. Recognize deadlock, livelock, starvation, and contention
16. Prefer immutability and reduced sharing
17. Use `ExecutorService`
18. Understand thread pools
19. Use `Callable` and `Future`
20. Use `CompletableFuture` appropriately
21. Use concurrent collections
22. Use `BlockingQueue`
23. Use latches/semaphores where appropriate
24. Understand CPU-bound vs I/O-bound work
25. Understand virtual threads
26. Understand that virtual threads do not remove resource limits
27. Understand connection-pool limits
28. Design stateless/thread-safe service objects
29. Test concurrent behavior without relying on arbitrary sleeps
30. Read basic thread dumps
31. Diagnose common concurrency failures
32. Measure before optimizing concurrency

------------------------------------------------------------------------

# Lower-Priority / Awareness Topics

These should not block progression into professional Spring Boot
development:

-   Custom thread scheduler implementation
-   Advanced lock-free algorithms
-   JVM memory-barrier internals
-   Formal memory-model proofs
-   Complex Fork/Join tuning
-   `StampedLock` mastery
-   Phaser mastery
-   Thread priority tuning
-   OS scheduler internals
-   NUMA/cache-coherence optimization
-   Distributed consensus
-   Distributed locking algorithms

Those topics may matter for specialized concurrency, JVM, performance,
or distributed-systems roles, but are beyond the target of this tree.

------------------------------------------------------------------------

# Professional Mastery Check

A full-stack Java developer can consider this concurrency foundation
professionally useful when they can independently:

-   [ ] Explain process, thread, task, concurrency, and parallelism
-   [ ] Create basic threads and understand `start()` vs `run()`
-   [ ] Coordinate thread completion
-   [ ] Handle interruption correctly
-   [ ] Identify shared mutable state
-   [ ] Explain and reproduce a race condition
-   [ ] Explain atomicity and visibility separately
-   [ ] Explain happens-before at an application level
-   [ ] Protect critical sections with `synchronized`
-   [ ] Use `volatile` only for appropriate visibility/order cases
-   [ ] Use atomic variables for suitable atomic operations
-   [ ] Use explicit locks when their additional features are justified
-   [ ] Recognize and diagnose deadlock
-   [ ] Prefer immutable/thread-confined designs where possible
-   [ ] Submit work to an `ExecutorService`
-   [ ] Shut executors down correctly
-   [ ] Reason about thread-pool size and queueing
-   [ ] Use `Future`
-   [ ] Compose asynchronous work with `CompletableFuture`
-   [ ] Select appropriate concurrent collections
-   [ ] Implement producer/consumer with `BlockingQueue`
-   [ ] Use a semaphore/latch for suitable coordination problems
-   [ ] Explain Fork/Join and parallel streams without automatically
    using them
-   [ ] Explain virtual threads and their best-fit workloads
-   [ ] Explain why virtual threads do not eliminate synchronization or
    downstream resource limits
-   [ ] Distinguish CPU-bound and I/O-bound concurrency
-   [ ] Reason about application concurrency vs database connection
    capacity
-   [ ] Explain Java locking vs database transaction locking
-   [ ] Design stateless service objects for concurrent web requests
-   [ ] Write deterministic-enough concurrent tests using coordination
    primitives
-   [ ] Read a basic thread dump
-   [ ] Diagnose race/deadlock/contention/pool-exhaustion symptoms
-   [ ] Measure throughput, latency, blocking, and resource utilization
    before optimizing
-   [ ] Decide when **not** to introduce concurrency

------------------------------------------------------------------------

# Relationship to Other Skill Trees

> **Preview note:** The JDBC, PostgreSQL, Spring Boot, and
> Controller → Service → Repository layering shown below are covered
> in depth in their own dedicated skill trees later in this
> curriculum. They're included here only to show where the
> concurrency concepts from this file will later apply, not as
> prerequisite knowledge for this file.

``` text
                             Java
                              │
          ┌───────────────────┼────────────────────┐
          ▼                   ▼                    ▼
        Maven               JUnit             Concurrency
                              │                    │
                              ▼                    │
                           Mockito                 │
                              │                    │
                 ┌────────────┴────────────┐       │
                 ▼                         ▼       │
               JDBC                   PostgreSQL  │
                 │                         │       │
                 └────────────┬────────────┘       │
                              │                    │
                              └──────────┬─────────┘
                                         ▼
                                    Spring Boot
                                         │
                          ┌──────────────┼──────────────┐
                          ▼              ▼              ▼
                     Controller        Service       Repository
                          │              │              │
                          │              └──────┬───────┘
                          │                     ▼
                          │              Concurrent Requests
                          │                     │
                          │                     ▼
                          │              Connection Pool
                          │                     │
                          │                     ▼
                          │                 PostgreSQL
                          │
                          ▼
                    Angular / React
```

------------------------------------------------------------------------

# Core Mental Model

The central concurrency mastery question is:

> **When multiple tasks can make progress at the same time, can I
> identify what state and resources they share, determine what
> correctness guarantees are required, choose the simplest safe
> concurrency mechanism, and diagnose the system when timing or resource
> contention causes unexpected behavior?**

For a full-stack Java developer, the goal is not to make everything
multithreaded. The goal is to understand concurrency well enough that a
multi-request application remains **correct, scalable, testable, and
understandable**.
