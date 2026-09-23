# AI-Assisted Software Development --- Full-Stack Developer Skill Tree

> **Goal:** Learn to use AI coding assistants, IDE copilots, CLI agents,
> and agentic development systems as professional software-development
> tools while preserving developer understanding, verification,
> security, and ownership of the code.
>
> **Target level:** Full-stack developer who can choose an appropriate
> AI-assisted workflow, provide useful context, decompose work,
> investigate and debug with AI, generate and review changes, use tests
> and Git as verification boundaries, and safely supervise increasingly
> autonomous coding agents.
>
> **Primary prerequisites:** Programming Fundamentals, Git / Version
> Control, and practical experience writing and debugging code in at
> least one language.
>
> **Strong supporting prerequisites:** Linux/terminal basics, testing
> fundamentals, HTTP/REST awareness, package/build tools, and experience
> reading a multi-file repository.
>
> **Related trees:** **RAG Fundamentals** explains retrieval systems in
> depth. **Spring AI** teaches how to build AI-enabled Spring
> applications. This tree is about using AI to help *develop software*,
> not about implementing foundation models or RAG systems.
>
> **Scope boundary:** This tree does not teach machine-learning
> mathematics, model training, vendor-specific certification, or every
> feature of a particular AI product. Products change quickly; the
> durable skills are context management, task decomposition, tool
> supervision, verification, security, and workflow design.
>
> **Capstone:** Use an AI coding assistant or agent to implement a
> bounded feature in an existing full-stack repository. Require
> repository investigation, a written plan, small commits/diffs,
> automated tests, debugging of at least one failure, documentation
> updates, and a final human review explaining every important change.

------------------------------------------------------------------------

# Skill Tree Overview

``` text
Programming + Git + Testing
           │
           ▼
AI-Assisted Software Development
           │
 ┌─────────┼──────────┐
 ▼         ▼          ▼
Chat      IDE       Coding Agents
Assist   Copilot    CLI / Agentic
 │         │          │
 └─────────┼──────────┘
           ▼
     Context Engineering
           │
 ┌─────────┼──────────┐
 ▼         ▼          ▼
Prompt   Repo       Retrieval
Design   Search      Awareness
           │           │
           │           └────► RAG Fundamentals
           ▼
 Task Decomposition
           │
           ▼
Investigate → Plan → Implement
           │
           ▼
 Test → Review Diff → Verify
           │
           ▼
 Secure Professional Workflow
```

# Dependency Map

``` text
Programming Fundamentals
          │
          ├────► Git
          ├────► Testing
          └────► Debugging
                    │
                    ▼
         AI-Assisted Development
                    │
       ┌────────────┼────────────┐
       ▼            ▼            ▼
   Context       Tool Use     Verification
       │            │            │
       └────────────┼────────────┘
                    ▼
              Agentic Workflow
                    │
                    ├────► RAG awareness
                    ├────► MCP awareness
                    └────► AI application development
```

------------------------------------------------------------------------

# Tier 0 --- Mental Model

## 1. AI Is a Development Tool

Use the mental model:

``` text
Developer
   │
   ├── defines goal
   ├── supplies/permits context
   ├── evaluates tradeoffs
   └── owns result
   │
   ▼
AI Assistant
   │
   ├── explains
   ├── searches
   ├── proposes
   ├── edits
   └── runs permitted tools
   │
   ▼
Compiler / Tests / Runtime / Human Review
```

-   [ ] Distinguish assistance from authority.
-   [ ] Treat generated code as a proposal until verified.
-   [ ] Keep responsibility for architecture, security, and correctness.
-   [ ] Understand that fluent explanations can still be wrong.

**Checkpoint:** Explain why "the AI said it works" is not evidence that
software works.

## 2. Strengths and Weaknesses

Useful strengths:

-   [ ] Boilerplate and repetitive transformations
-   [ ] Explaining unfamiliar code
-   [ ] Searching for likely causes
-   [ ] Generating test cases
-   [ ] Comparing implementation options
-   [ ] Refactoring bounded code
-   [ ] Documentation and examples
-   [ ] Connecting information across several files

Common weaknesses:

-   [ ] Hallucinated APIs
-   [ ] Outdated assumptions
-   [ ] Hidden edge cases
-   [ ] Overconfident explanations
-   [ ] Excessive changes
-   [ ] Weak awareness of unstated project conventions
-   [ ] Security mistakes
-   [ ] Passing a narrow test while violating the broader requirement

------------------------------------------------------------------------

# Tier 1 --- AI Coding Tool Categories

## 3. Chat Assistants

General conversational systems are useful for:

``` text
question → explanation
error → investigation
design → comparison
snippet → review
```

Learn to move between conceptual questions and concrete repository
evidence.

## 4. IDE Assistants

IDE-integrated assistants may provide:

-   [ ] Inline completion
-   [ ] Code explanation
-   [ ] File-aware chat
-   [ ] Refactoring
-   [ ] Test generation
-   [ ] Repository search
-   [ ] Multi-file edits

## 5. CLI / Coding Agents

Agentic tools may be able to:

``` text
inspect files
search repository
edit files
run commands
run tests
read failures
iterate
```

The more authority a tool receives, the more important permissions and
verification become.

## 6. Tool Choice

Choose based on task:

``` text
Small syntax question        → chat/completion
Single-file edit             → IDE assistant
Repository investigation     → repo-aware assistant
Multi-file implementation    → coding agent
Architecture decision        → discussion + human judgment
High-risk production change  → constrained AI + strong review
```

------------------------------------------------------------------------

# Tier 2 --- Prompt Construction

## 7. A Useful Task Prompt

A strong coding request often contains:

``` text
Goal
+ relevant context
+ constraints
+ expected behavior
+ verification method
```

Example structure:

``` text
Goal: add pagination to message listing.
Context: Spring Boot + Spring Data JPA.
Constraints: do not change the public response DTO.
Expected: page and size query parameters.
Verification: update/add tests and run them.
```

## 8. Ask for Investigation Before Editing

For uncertain problems:

``` text
Observe
  │
  ▼
Form hypothesis
  │
  ▼
Gather evidence
  │
  ▼
Propose change
  │
  ▼
Implement
```

Avoid jumping directly from an error message to a large rewrite.

## 9. State Non-Goals

Examples:

-   [ ] Do not change database schema.
-   [ ] Do not add a dependency.
-   [ ] Do not refactor unrelated code.
-   [ ] Preserve API compatibility.
-   [ ] Do not edit generated files.

## 10. Define Done

"Done" may mean:

-   [ ] Code compiles
-   [ ] Required tests pass
-   [ ] New behavior has tests
-   [ ] No unrelated files changed
-   [ ] Documentation updated
-   [ ] Manual endpoint check succeeds

------------------------------------------------------------------------

# Tier 3 --- Context Engineering

## 11. Context Is a Limited Resource

AI quality depends heavily on relevant context.

``` text
Too little context → guessing
Useful context     → grounded work
Too much noise     → distraction / cost / confusion
```

## 12. Relevant Context

Potentially useful context:

-   [ ] Requirements
-   [ ] Relevant source files
-   [ ] Interfaces and DTOs
-   [ ] Build configuration
-   [ ] Tests
-   [ ] Error output
-   [ ] Logs
-   [ ] Architecture notes
-   [ ] Project conventions
-   [ ] Dependency versions

## 13. Progressive Disclosure

Do not dump an entire repository when the task is local.

``` text
Start with task
      │
      ▼
Find likely files
      │
      ▼
Inspect dependencies/callers
      │
      ▼
Expand only as necessary
```

## 14. Context Hygiene

-   [ ] Distinguish facts from assumptions.
-   [ ] Correct stale context.
-   [ ] Start a fresh task/context when prior conversation becomes
    misleading.
-   [ ] Summarize established decisions for long tasks.
-   [ ] Point the assistant to authoritative project files.

------------------------------------------------------------------------

# Tier 4 --- Repository Exploration

## 15. Learn the Repository Before Changing It

A useful exploration sequence:

``` text
README / build files
       │
       ▼
directory structure
       │
       ▼
entry points
       │
       ▼
relevant feature
       │
       ▼
tests / callers / configuration
```

## 16. Search Before Guessing

Use repository evidence:

``` text
symbol search
text search
references
tests
configuration
Git history when useful
```

## 17. Ask the AI to Build a Local Mental Model

Useful questions:

-   [ ] Which files participate in this request?
-   [ ] What is the call path?
-   [ ] Where is this value configured?
-   [ ] Which tests cover this behavior?
-   [ ] What assumptions are you making?

**Checkpoint:** Give an assistant an unfamiliar repository and have it
identify the path from an HTTP request to persistence without changing
code.

------------------------------------------------------------------------

# Tier 5 --- Task Decomposition

## 18. Break Large Work into Verifiable Units

Bad:

``` text
"Build the whole application."
```

Better:

``` text
1. inspect current architecture
2. define API contract
3. implement backend behavior
4. add tests
5. connect frontend
6. verify end-to-end
```

## 19. Separate Planning from Execution

For nontrivial work:

``` text
Plan
  │
  ▼
Review plan
  │
  ▼
Implement one unit
  │
  ▼
Verify
  │
  └────► next unit
```

## 20. Keep Diffs Small

Small diffs are easier to:

-   [ ] Understand
-   [ ] Test
-   [ ] Review
-   [ ] Revert
-   [ ] Attribute to a requirement

------------------------------------------------------------------------

# Tier 6 --- Explanation and Learning

## 21. AI as Tutor

Ask:

``` text
What does this do?
Why is it needed?
What happens if I remove it?
What prerequisite concept am I missing?
Show the runtime flow.
```

## 22. Avoid Passive Copying

After receiving code:

-   [ ] Explain it yourself.
-   [ ] Predict its output.
-   [ ] Modify a small part without AI.
-   [ ] Identify alternatives.
-   [ ] Recreate important patterns later from memory.

## 23. Documentation Companion

Use AI to translate terse documentation into:

``` text
signature
arguments
return value
side effects
example
edge cases
related concepts
```

Then return to the authoritative documentation when exact behavior
matters.

------------------------------------------------------------------------

# Tier 7 --- Code Generation

## 24. Generate Bounded Code

Good targets:

-   [ ] DTO
-   [ ] Validation
-   [ ] Mapper
-   [ ] Repository query
-   [ ] Test fixture
-   [ ] Small component
-   [ ] Configuration example

## 25. Require Fit With Existing Code

Ask the assistant to follow:

``` text
existing naming
existing patterns
existing dependencies
existing test style
existing architecture
```

## 26. Avoid Unnecessary Novelty

Prefer the simplest implementation consistent with the repository.

------------------------------------------------------------------------

# Tier 8 --- Refactoring

## 27. Preserve Behavior

Refactoring should normally mean:

``` text
same external behavior
different internal structure
```

## 28. Establish Safety First

Before a significant refactor:

-   [ ] Identify behavior.
-   [ ] Find/add tests.
-   [ ] Establish baseline.
-   [ ] Refactor incrementally.
-   [ ] Re-run verification.

## 29. Ask for a Diff-Oriented Explanation

Have the assistant explain:

``` text
what changed
why
behavioral impact
risks
tests proving preservation
```

------------------------------------------------------------------------

# Tier 9 --- Debugging

## 30. Supply Reproducible Evidence

Useful debugging context:

-   [ ] Exact error
-   [ ] Stack trace
-   [ ] Command used
-   [ ] Expected result
-   [ ] Actual result
-   [ ] Recent changes
-   [ ] Relevant versions
-   [ ] Minimal reproduction

## 31. Hypothesis-Driven Debugging

``` text
Symptom
  │
  ▼
Possible causes
  │
  ▼
Rank hypotheses
  │
  ▼
Run discriminating check
  │
  ▼
Update hypothesis
```

## 32. Do Not Shotgun-Fix

Avoid changing five unrelated things at once.

One controlled change provides better evidence.

------------------------------------------------------------------------

# Tier 10 --- Testing With AI

## 33. Generate Test Ideas Before Test Code

Ask for cases:

``` text
happy path
boundary
invalid input
empty state
failure
authorization
concurrency where relevant
```

## 34. Tests Are Not Automatically Correct

AI may generate tests that:

-   [ ] Assert the implementation instead of requirement
-   [ ] Mock away the important behavior
-   [ ] Never execute the failing path
-   [ ] Pass for the wrong reason

Review tests as carefully as production code.

## 35. Red-Green Feedback Loop

``` text
Requirement
   │
   ▼
Test / reproduction
   │
   ▼
Fail
   │
   ▼
Implementation
   │
   ▼
Pass
```

AI can participate in the loop, but the loop provides external evidence.

------------------------------------------------------------------------

# Tier 11 --- Verification

## 36. Verification Ladder

``` text
Read generated change
        │
        ▼
Inspect diff
        │
        ▼
Compile / type-check
        │
        ▼
Run focused tests
        │
        ▼
Run broader tests
        │
        ▼
Lint/static analysis
        │
        ▼
Manual/runtime verification
```

Not every task needs every rung, but high-impact changes need stronger
evidence.

## 37. Ask the Model to Critique Its Own Change

Useful prompts:

-   [ ] What could be wrong with this?
-   [ ] Which edge cases remain?
-   [ ] What assumptions did you make?
-   [ ] What would a reviewer object to?
-   [ ] Which tests would falsify this implementation?

Treat self-critique as additional review, not proof.

------------------------------------------------------------------------

# Tier 12 --- Git as a Safety Boundary

## 38. Start Clean

Before agentic edits:

``` text
git status
```

Know your baseline.

## 39. Inspect the Diff

After changes:

``` text
git diff
git diff --stat
```

Review every file the agent touched.

## 40. Commit in Logical Units

``` text
small task
   │
   ▼
verified diff
   │
   ▼
commit
```

This creates recovery points.

## 41. Revertability

Be comfortable discarding or reverting bad AI changes.

AI should reduce development effort, not make you afraid to undo work.

------------------------------------------------------------------------

# Tier 13 --- Agentic Coding

## 42. Agent Loop

A coding agent may operate like:

``` text
Goal
 │
 ▼
Inspect
 │
 ▼
Plan
 │
 ▼
Edit
 │
 ▼
Run tool/test
 │
 ▼
Observe result
 │
 ├── failure → revise
 │
 └── success → report
```

## 43. Permission Levels

Possible authority:

``` text
read files
edit files
run safe commands
access network
install dependencies
modify Git
deploy
```

Grant only what the task needs.

## 44. Stop Conditions

Define when an agent should stop:

-   [ ] Destructive command required
-   [ ] Requirement ambiguity
-   [ ] Security-sensitive decision
-   [ ] Major architecture change
-   [ ] Repeated failed attempts
-   [ ] Production/deployment action

------------------------------------------------------------------------

# Tier 14 --- Retrieval Awareness

## 45. Why Coding Tools Retrieve Context

Large repositories do not fit usefully into every model request.

Tools may retrieve relevant information using:

``` text
filename search
symbol search
grep/text search
dependency graphs
semantic search
repository indexing
RAG
```

## 46. Retrieval Is Broader Than RAG

An assistant reading files returned by `grep` is retrieving context, but
classic RAG commonly adds indexed semantic retrieval and augmentation.

## 47. RAG Boundary

Learn here:

``` text
what RAG is
why a coding tool may use it
how retrieval quality affects answers
```

Learn implementation in **RAG Fundamentals**.

------------------------------------------------------------------------

# Tier 15 --- Tool Calling and MCP Awareness

## 48. Tool Calling

Models can request structured operations such as:

``` text
read file
search code
run test
query issue tracker
inspect database
```

The surrounding application decides what tools exist and what is
permitted.

## 49. MCP Awareness

Model Context Protocol (MCP) is a way AI applications/tools can
integrate with external tools and context providers.

Developer-level questions:

-   [ ] What capability does the server expose?
-   [ ] What data can it access?
-   [ ] What actions can it take?
-   [ ] What permissions are granted?
-   [ ] Is the source trusted?

Detailed implementation can belong to AI application/tooling
specializations.

------------------------------------------------------------------------

# Tier 16 --- Hallucinations and Uncertainty

## 50. Common Coding Hallucinations

-   [ ] Nonexistent method
-   [ ] Wrong library version
-   [ ] Invented configuration property
-   [ ] Fake command-line flag
-   [ ] Incorrect framework behavior
-   [ ] Imagined repository file

## 51. Demand Evidence

Ask:

``` text
Which file supports that?
Which test proves it?
Which documentation/API are you relying on?
Can you verify the symbol exists?
```

## 52. Calibrate Confidence

Prefer:

``` text
"I found X in file Y, which suggests..."
```

over unsupported certainty.

------------------------------------------------------------------------

# Tier 17 --- Security

## 53. Secrets

Never casually expose:

``` text
passwords
API keys
private keys
production tokens
customer data
```

to tools that do not need them.

## 54. Untrusted Repository Content

Repositories can contain:

``` text
comments
README instructions
generated text
issues
dependencies
scripts
```

that should not automatically be treated as trusted instructions for an
AI agent.

## 55. Destructive Actions

Require explicit scrutiny for:

``` text
rm
database deletion
force push
credential changes
production deployment
cloud resource deletion
permission changes
```

## 56. Dependency Risk

Do not install a package merely because an AI suggested its name.

Verify:

-   [ ] Package exists
-   [ ] Correct ecosystem
-   [ ] Maintained/trusted
-   [ ] Version compatible
-   [ ] Actually necessary

------------------------------------------------------------------------

# Tier 18 --- Privacy and Organizational Policy

## 57. Code/Data Sensitivity

Know whether a tool is approved for:

``` text
proprietary code
customer information
production logs
credentials
regulated data
```

## 58. Organizational Controls

Professional environments may define:

-   [ ] Approved models/tools
-   [ ] Data retention rules
-   [ ] Repository permissions
-   [ ] Audit requirements
-   [ ] Human review requirements

Tool convenience does not override policy.

------------------------------------------------------------------------

# Tier 19 --- Efficiency

## 59. Use AI Where It Has Leverage

High-value pattern:

``` text
repetitive + reviewable + testable
```

Lower-value pattern:

``` text
tiny task where prompting/review costs more than coding
```

## 60. Token / Context Efficiency

-   [ ] Supply relevant files rather than everything.
-   [ ] Avoid repeating huge logs.
-   [ ] Summarize stable decisions.
-   [ ] Use repository search.
-   [ ] Break long projects into bounded tasks.

## 61. Avoid Endless Agent Loops

If an agent repeatedly fails:

``` text
stop
inspect evidence
reduce scope
correct assumptions
resume deliberately
```

------------------------------------------------------------------------

# Tier 20 --- Professional Workflow

## 62. Recommended Loop

``` text
Define requirement
      │
      ▼
Establish clean Git state
      │
      ▼
Ask AI to investigate
      │
      ▼
Review plan
      │
      ▼
Implement small change
      │
      ▼
Run verification
      │
      ▼
Inspect diff
      │
      ▼
Explain change
      │
      ▼
Commit
```

## 63. Developer Ownership

You should be able to explain:

-   [ ] Why the change exists
-   [ ] How it works
-   [ ] Why this design was chosen
-   [ ] How it was tested
-   [ ] What risks remain

## 64. Mastery Check

You are ready to use AI coding tools professionally when you can:

-   [ ] Choose chat vs IDE vs agent workflows intentionally.
-   [ ] Provide focused context.
-   [ ] Make the AI investigate before guessing.
-   [ ] Decompose work into verifiable units.
-   [ ] Review generated code and tests.
-   [ ] Use Git to bound/recover changes.
-   [ ] Detect likely hallucinations.
-   [ ] Protect secrets and sensitive data.
-   [ ] Supervise tool permissions.
-   [ ] Explain all important accepted code yourself.
-   [ ] Reject AI output when evidence does not support it.

------------------------------------------------------------------------

# Capstone

Use an existing full-stack project.

Requirements:

1.  Select a feature requiring at least frontend/backend or
    backend/database changes.
2.  Begin from a clean Git state.
3.  Ask the AI to map the relevant architecture before editing.
4.  Produce and review a plan.
5.  Implement in small units.
6.  Add/update tests.
7.  Intentionally debug at least one failed build/test.
8.  Inspect every changed file and the final diff.
9.  Run the appropriate full verification.
10. Write a short postmortem:

-   what AI did well,
-   what it got wrong,
-   what evidence caught the error,
-   what context improved performance,
-   what you still needed to understand personally.
