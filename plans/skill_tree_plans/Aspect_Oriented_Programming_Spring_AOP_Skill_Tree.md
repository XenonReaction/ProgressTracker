# Aspect-Oriented Programming & Spring AOP Skill Tree — Full-Stack Java Path

> **Goal:** Understand cross-cutting concerns and AOP generally, then apply Spring proxy-based AOP safely and recognize framework features that use similar interception/proxy mechanisms.

> **Target:** Professional full-stack developer competency; advanced specialist topics are awareness-level unless needed for application development.

## Dependency / prerequisite map

```text
Java OOP + interfaces + annotations
              ↓
       Spring fundamentals
   (beans, IoC, DI, proxies)
              ↓
       General AOP concepts
              ↓
          Spring AOP
```

## Skill Tree Overview

### 1. Why AOP Exists
- [ ] Cross-cutting concerns
- [ ] Scattered/tangled code
- [ ] Separation of core business logic from infrastructure concerns
- [ ] When ordinary composition/decorators/interceptors may be simpler

### 2. Core AOP Vocabulary
- [ ] Aspect
- [ ] Join point
- [ ] Pointcut
- [ ] Advice
- [ ] Target object
- [ ] Proxy
- [ ] Weaving
- [ ] Introduction awareness

### 3. Advice Types
- [ ] Before advice
- [ ] After/finally advice
- [ ] After-returning advice
- [ ] After-throwing advice
- [ ] Around advice
- [ ] Understand control-flow power and risk of around advice

### 4. Pointcuts
- [ ] Method-execution matching
- [ ] Package/type/method matching concepts
- [ ] Annotation-based pointcuts
- [ ] Compose/reuse pointcuts
- [ ] Keep pointcuts understandable and narrow

### 5. Weaving Models
- [ ] Compile-time weaving awareness
- [ ] Load-time weaving awareness
- [ ] Runtime proxy/interception model
- [ ] Understand Spring AOP is primarily proxy-based

### 6. Proxy Fundamentals
- [ ] Proxy wraps/intercepts calls to a target
- [ ] Interface-based vs class-based proxy awareness
- [ ] Proxy identity vs target object
- [ ] Only calls passing through the proxy can be intercepted

### 7. Spring AOP Setup
- [ ] AOP dependency/starter awareness
- [ ] @Aspect
- [ ] Register aspects as Spring beans
- [ ] @Pointcut
- [ ] @Before
- [ ] @After
- [ ] @AfterReturning
- [ ] @AfterThrowing
- [ ] @Around

### 8. JoinPoint & ProceedingJoinPoint
- [ ] Inspect method/signature/arguments appropriately
- [ ] Proceed with an around advice
- [ ] Return the correct value
- [ ] Propagate/handle exceptions correctly
- [ ] Avoid modifying arguments/results without a clear contract

### 9. Pointcut Expressions
- [ ] execution() fundamentals
- [ ] within() awareness
- [ ] @annotation matching
- [ ] args()/target()/this() awareness
- [ ] Prefer maintainable expressions over clever expressions

### 10. Ordering & Multiple Aspects
- [ ] Multiple aspects can match one invocation
- [ ] @Order awareness
- [ ] Understand nested around-advice behavior
- [ ] Avoid correctness that depends on obscure ordering

### 11. Spring Proxy Limitations
- [ ] Self-invocation problem
- [ ] Private/final method limitations awareness depending on proxy strategy
- [ ] Objects created outside Spring are not automatically proxied
- [ ] Bean lifecycle/proxy creation awareness
- [ ] Recognize why annotations may appear to “do nothing”

### 12. Common Uses
- [ ] Logging/tracing
- [ ] Metrics/timing
- [ ] Auditing
- [ ] Authorization awareness
- [ ] Caching awareness
- [ ] Transaction management relationship
- [ ] Do not use AOP to hide core domain behavior

### 13. AOP and @Transactional
- [ ] Recognize transaction interception/proxy concept
- [ ] Understand call enters proxy before transaction behavior is applied
- [ ] Self-invocation implications
- [ ] Leave transaction semantics to persistence/Spring trees

### 14. AOP and @Async / Caching / Security
- [ ] Recognize proxy/interception patterns
- [ ] Understand annotations are not magic by themselves
- [ ] Separate conceptual mechanism from each feature’s dedicated configuration

### 15. Testing Aspects
- [ ] Unit-test advice logic where useful
- [ ] Integration-test that Spring proxying actually occurs
- [ ] Test observable behavior rather than framework internals
- [ ] Use JUnit/Mockito appropriately

### 16. Debugging AOP
- [ ] Determine whether target is a Spring bean
- [ ] Determine whether call passes through proxy
- [ ] Check pointcut match
- [ ] Check proxy type
- [ ] Check aspect registration
- [ ] Inspect ordering/exceptions
- [ ] Recognize self-invocation quickly

### 17. Professional AOP Judgment
- [ ] Use sparingly for genuinely cross-cutting behavior
- [ ] Prefer explicit business logic for domain rules
- [ ] Keep aspects small
- [ ] Avoid surprising side effects
- [ ] Document non-obvious interception
- [ ] Know when filters/interceptors/decorators are a better boundary

### 18. AspectJ Awareness
- [ ] Know AspectJ is broader than Spring AOP
- [ ] Recognize compile/load-time weaving
- [ ] No advanced AspectJ mastery required for full-stack Spring target

## Practical competency checkpoints

- [ ] Explain AOP without Spring terminology
- [ ] Identify a cross-cutting concern
- [ ] Write a simple Spring aspect and pointcut
- [ ] Explain proxy-based interception
- [ ] Diagnose self-invocation/proxy bypass
- [ ] Explain why @Transactional-like features can depend on proxies
- [ ] Decide when AOP is inappropriate

## Mastery standard

> Can I build, explain, debug, and make appropriate design choices in this domain without relying on a step-by-step tutorial, while recognizing what adjacent frameworks or abstractions are doing on my behalf?
