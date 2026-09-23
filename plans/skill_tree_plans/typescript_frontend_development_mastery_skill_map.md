# TypeScript for Frontend Development — Mastery Skill Map

## Purpose

This skill map assumes:

- completion of a JavaScript fundamentals skill map,
- familiarity with HTML/CSS and browser development,
- zero prior TypeScript knowledge,
- and a goal of preparing for modern frontend development with Angular and React.

The goal is to learn TypeScript as **JavaScript plus a compile-time type system and development tooling**, without confusing TypeScript's type information with runtime behavior.

By the end, you should be able to:

- explain what TypeScript is and why teams use it,
- understand how TypeScript becomes JavaScript,
- configure and use the TypeScript compiler,
- model data with primitive, object, union, intersection, literal, tuple, generic, mapped, and conditional types,
- choose between interfaces and type aliases,
- narrow types safely,
- use `any`, `unknown`, `never`, and `void` correctly,
- type functions, DOM code, events, forms, APIs, classes, and modules,
- understand structural typing,
- understand what types disappear at runtime,
- consume third-party libraries and declaration files,
- read compiler diagnostics,
- debug compile-time and runtime problems separately,
- and enter Angular or React without TypeScript itself being the confusing part.

TypeScript does **not** replace JavaScript.

A useful mental model is:

```text
TypeScript source
      ↓
type checking
      ↓
transpilation / emit
      ↓
JavaScript
      ↓
browser or Node.js
```

---

# 1. What TypeScript Is

TypeScript is a programming language developed as a typed superset of JavaScript.

That means valid JavaScript is generally intended to be valid TypeScript, while TypeScript adds syntax and tooling for static type checking.

Example JavaScript:

```javascript
function add(a, b) {
    return a + b;
}
```

TypeScript:

```typescript
function add(a: number, b: number): number {
    return a + b;
}
```

The type annotations are checked during development/compilation and are not normally present in the final JavaScript runtime code.

---

# 2. TypeScript Does Not Run Directly in the Browser

Browsers execute JavaScript.

TypeScript is transformed into JavaScript.

Conceptually:

```text
app.ts
 ↓
TypeScript compiler/build tool
 ↓
app.js
 ↓
browser
```

This distinction is essential.

---

# 3. TypeScript Is Primarily Compile-Time Safety

TypeScript can catch many mistakes before runtime.

Example:

```typescript
let age: number = 30;

age = "thirty";
```

TypeScript reports an error.

But TypeScript does not magically validate arbitrary runtime input.

If a server returns invalid JSON, TypeScript's static type declarations do not transform that data into a valid object.

---

# 4. TypeScript vs JavaScript

JavaScript:

```javascript
let value = 10;
value = "hello";
```

TypeScript may infer:

```typescript
let value = 10;
```

as:

```text
number
```

and then reject:

```typescript
value = "hello";
```

TypeScript adds static constraints around JavaScript behavior.

At runtime, however, the browser still executes JavaScript.

---

# 5. TypeScript vs Java

Do not assume TypeScript's type system behaves like Java's.

Major distinctions include:

- structural typing,
- unions,
- literal types,
- type inference,
- flexible object shapes,
- JavaScript runtime semantics underneath.

Note on erasure: both languages erase generic type parameters at compile time (Java can't do `new T()` either, for the same reason). The real difference is scope. Java erases only the generic parameter — concrete types keep a full runtime presence, so `instanceof`, reflection, and class checks all work. TypeScript erases its entire type system — every type, interface, and annotation is gone after compilation, with zero runtime representation or enforcement of any kind.

TypeScript syntax can look familiar to Java developers while representing a very different type system.

---

# 6. TypeScript Worth Memorizing

You should eventually write these without documentation.

## Variable Annotation

```typescript
const name: string = "Alice";
let count: number = 0;
```

## Typed Function

```typescript
function add(a: number, b: number): number {
    return a + b;
}
```

## Typed Array

```typescript
const names: string[] = ["Alice", "Bob"];
```

## Object Type

```typescript
const user: {
    name: string;
    age: number;
} = {
    name: "Alice",
    age: 30
};
```

## Interface

```typescript
interface User {
    id: number;
    name: string;
}
```

## Type Alias

```typescript
type UserId = number;
```

## Union

```typescript
type Status = "loading" | "success" | "error";
```

## Generic

```typescript
function identity<T>(value: T): T {
    return value;
}
```

## Promise

```typescript
async function loadUsers(): Promise<User[]> {
    // ...
}
```

## DOM Element

```typescript
const form = document.querySelector<HTMLFormElement>("#form");
```

---

# 7. Type Inference

TypeScript often infers types without explicit annotations.

```typescript
const name = "Alice";
```

TypeScript knows this is string-like.

```typescript
let count = 0;
```

TypeScript infers `number`.

Good TypeScript does **not** mean annotating every expression.

Use annotations where they:

- document an API boundary,
- prevent ambiguity,
- constrain behavior,
- improve readability.

---

# 8. Explicit Type Annotations

Example:

```typescript
let age: number = 30;
```

Function:

```typescript
function greet(name: string): string {
    return `Hello, ${name}`;
}
```

Use explicit types intentionally.

Do not add redundant annotations purely to make code look more typed.

---

# 9. Primitive Types

Common primitive type annotations:

```text
string
number
boolean
bigint
symbol
null
undefined
```

Examples:

```typescript
const username: string = "Alice";
const score: number = 100;
const enabled: boolean = true;
```

---

# 10. Arrays

Two common forms:

```typescript
const names: string[] = [];
```

and:

```typescript
const names: Array<string> = [];
```

They express the same general idea.

Use whichever style fits project conventions.

---

# 11. Tuples

A tuple describes an array with known positions and types.

```typescript
const coordinate: [number, number] = [10, 20];
```

Another:

```typescript
const result: [string, number] = ["Alice", 30];
```

Tuples are useful when position has fixed meaning.

Do not use tuples where an object would communicate names more clearly.

---

# 12. Object Types

Inline:

```typescript
const user: {
    id: number;
    name: string;
} = {
    id: 1,
    name: "Alice"
};
```

For reusable shapes, prefer named types or interfaces.

---

# 13. Optional Properties

```typescript
interface User {
    id: number;
    name: string;
    nickname?: string;
}
```

`nickname?` means the property may be absent.

This is not identical in all contexts to:

```typescript
nickname: string | undefined;
```

Understand absence vs explicit `undefined`.

---

# 14. Readonly Properties

```typescript
interface User {
    readonly id: number;
    name: string;
}
```

Then:

```typescript
user.id = 5;
```

is rejected by TypeScript.

`readonly` is a compile-time restriction.

It does not automatically freeze the object at runtime.

---

# 15. `readonly` Arrays

```typescript
const names: readonly string[] = ["Alice", "Bob"];
```

Mutation methods such as `push()` are disallowed by the type system.

Another form:

```typescript
ReadonlyArray<string>
```

---

# 16. Type Aliases

```typescript
type UserId = number;
```

More complex:

```typescript
type User = {
    id: number;
    name: string;
};
```

Type aliases can represent:

- primitives,
- objects,
- unions,
- intersections,
- tuples,
- functions,
- mapped/conditional types.

---

# 17. Interfaces

```typescript
interface User {
    id: number;
    name: string;
}
```

Interfaces are especially common for object-like public shapes.

They can be extended and declaration-merged.

---

# 18. Interface vs Type Alias

Both can describe object shapes.

Interface:

```typescript
interface User {
    id: number;
}
```

Type:

```typescript
type User = {
    id: number;
};
```

General guidance:

- interface is excellent for object contracts and extensibility,
- type aliases are more flexible for unions, intersections, tuples, primitives, and computed types.

Do not treat one as universally superior.

---

# 19. Interface Extension

```typescript
interface Person {
    name: string;
}

interface Employee extends Person {
    employeeId: number;
}
```

This creates an extended object contract.

---

# 20. Intersection Types

```typescript
type Person = {
    name: string;
};

type EmployeeInfo = {
    employeeId: number;
};

type Employee = Person & EmployeeInfo;
```

An intersection combines requirements.

Conceptually:

```text
A & B
```

means:

> satisfy both A and B.

---

# 21. Union Types

```typescript
type Id = string | number;
```

A value can be one of several types.

Example:

```typescript
function printId(id: string | number) {
}
```

Unions are one of TypeScript's most important features.

---

# 22. Literal Types

A literal type represents a specific value.

```typescript
type Direction = "left" | "right";
```

Allowed:

```typescript
const direction: Direction = "left";
```

Rejected:

```typescript
const direction: Direction = "up";
```

Literal unions are extremely common in frontend development.

---

# 23. Literal Unions for UI State

Example:

```typescript
type LoadingState =
    | "idle"
    | "loading"
    | "success"
    | "error";
```

This is often safer than using arbitrary strings.

---

# 24. Discriminated Unions

One of the most useful patterns in TypeScript.

```typescript
type Result =
    | {
        status: "success";
        data: string[];
    }
    | {
        status: "error";
        error: Error;
    };
```

Then:

```typescript
function handle(result: Result) {
    if (result.status === "success") {
        console.log(result.data);
    } else {
        console.error(result.error);
    }
}
```

The `status` property discriminates the union.

---

# 25. Enums

TypeScript supports enums:

```typescript
enum Status {
    Loading,
    Success,
    Error
}
```

String enum:

```typescript
enum Role {
    Admin = "admin",
    User = "user"
}
```

However, many modern codebases prefer literal unions or `as const` objects in some cases.

Understand enums, but do not assume they are always the best design.

---

# 26. `as const`

Example:

```typescript
const roles = {
    admin: "admin",
    user: "user"
} as const;
```

This narrows values to literal types and marks properties readonly.

Useful in configuration and value/type patterns.

---

# 27. `typeof` in Type Positions

JavaScript runtime `typeof`:

```typescript
typeof value
```

can inspect a runtime value.

TypeScript also allows:

```typescript
const config = {
    mode: "dark",
    size: 10
};

type Config = typeof config;
```

Here `typeof` extracts a compile-time type from a value declaration.

---

# 28. `keyof`

Given:

```typescript
interface User {
    id: number;
    name: string;
}
```

Then:

```typescript
type UserKey = keyof User;
```

produces a type equivalent to:

```text
"id" | "name"
```

This is very useful in generic code.

---

# 29. Indexed Access Types

```typescript
interface User {
    id: number;
    name: string;
}
```

You can write:

```typescript
type NameType = User["name"];
```

which gives:

```text
string
```

With `keyof`:

```typescript
type UserValue = User[keyof User];
```

---

# 30. `any`

```typescript
let value: any;
```

`any` effectively disables much of TypeScript's checking for that value.

Example:

```typescript
value.this.does.not.need.to.exist();
```

TypeScript may allow it.

Use `any` sparingly.

It is an escape hatch, not a normal default.

---

# 31. `unknown`

```typescript
let value: unknown;
```

Unlike `any`, you must narrow or validate the value before using it.

Example:

```typescript
if (typeof value === "string") {
    console.log(value.toUpperCase());
}
```

For untrusted/uncertain data, `unknown` is often much safer than `any`.

---

# 32. `never`

`never` represents a value that should never occur.

Example:

```typescript
function fail(message: string): never {
    throw new Error(message);
}
```

It is also useful for exhaustive checking of discriminated unions.

---

# 33. `void`

`void` commonly means a function's return value is not intended to be used.

```typescript
function log(message: string): void {
    console.log(message);
}
```

Do not confuse `void` with `undefined`, even though they are related in function behavior.

---

# 34. `null` and `undefined`

With strict null checking, these are distinct types.

```typescript
let user: User | null = null;
```

You must narrow before accessing properties:

```typescript
if (user !== null) {
    console.log(user.name);
}
```

This prevents a large category of runtime errors.

---

# 35. `strictNullChecks`

A crucial compiler option.

When enabled, TypeScript treats `null` and `undefined` as distinct possibilities requiring handling.

This is part of why strict mode significantly improves safety.

---

# 36. Function Parameter Types

```typescript
function greet(name: string): string {
    return `Hello, ${name}`;
}
```

Know:

```text
parameter type
return type
```

---

# 37. Optional Parameters

```typescript
function greet(name?: string): string {
    return name ? `Hello, ${name}` : "Hello";
}
```

Optional parameters may be `undefined`.

---

# 38. Default Parameters

```typescript
function greet(name = "Guest"): string {
    return `Hello, ${name}`;
}
```

TypeScript can infer the parameter type from the default.

---

# 39. Rest Parameters

```typescript
function sum(...values: number[]): number {
    return values.reduce((total, value) => total + value, 0);
}
```

Rest parameters are arrays.

---

# 40. Function Types

```typescript
type Operation = (a: number, b: number) => number;
```

Then:

```typescript
const add: Operation = (a, b) => a + b;
```

Function types are important for callbacks and event handling.

---

# 41. Callback Types

```typescript
function process(
    value: number,
    callback: (value: number) => void
): void {
    callback(value);
}
```

Typed callbacks improve API clarity.

---

# 42. Overloads

TypeScript supports function overload signatures.

Example:

```typescript
function format(value: string): string;
function format(value: number): string;

function format(value: string | number): string {
    return String(value);
}
```

Use overloads when they express a meaningful relationship between inputs and outputs.

Do not overuse them when a union is simpler.

---

# 43. Type Inference in Functions

TypeScript can infer callback parameter types from context.

```typescript
const numbers = [1, 2, 3];

numbers.map(number => number * 2);
```

`number` is inferred.

This is contextual typing.

---

# 44. Contextual Typing

TypeScript can infer types from the location where a function/value is used.

Example:

```typescript
button.addEventListener("click", event => {
    // event can be inferred from context
});
```

Contextual typing reduces unnecessary annotation noise.

---

# 45. Generics

Generics allow code to preserve type relationships without hard-coding one type.

```typescript
function identity<T>(value: T): T {
    return value;
}
```

Call:

```typescript
const result = identity("hello");
```

TypeScript infers:

```text
string
```

---

# 46. Why Generics Matter

Without generics:

```typescript
function first(values: any[]): any {
    return values[0];
}
```

Type information is lost.

With generics:

```typescript
function first<T>(values: T[]): T | undefined {
    return values[0];
}
```

The relationship is preserved.

---

# 47. Generic Interfaces

```typescript
interface ApiResponse<T> {
    data: T;
    status: number;
}
```

Then:

```typescript
type UserResponse = ApiResponse<User>;
```

This is common in API/data modeling.

---

# 48. Generic Type Aliases

```typescript
type Result<T> = {
    value: T;
    error?: string;
};
```

---

# 49. Generic Classes

```typescript
class Store<T> {
    private items: T[] = [];

    add(item: T): void {
        this.items.push(item);
    }
}
```

Understand the pattern even if you do not write generic classes every day.

---

# 50. Generic Constraints

```typescript
function getId<T extends { id: number }>(value: T): number {
    return value.id;
}
```

`extends` here constrains acceptable generic types.

This differs conceptually from class inheritance.

---

# 51. `keyof` with Generics

```typescript
function getProperty<T, K extends keyof T>(
    object: T,
    key: K
): T[K] {
    return object[key];
}
```

This is a classic example of preserving safe relationships between keys and values.

---

# 52. Utility Types

TypeScript includes utility types that transform existing types.

Important:

```text
Partial<T>
Required<T>
Readonly<T>
Pick<T, K>
Omit<T, K>
Record<K, V>
Exclude<T, U>
Extract<T, U>
NonNullable<T>
ReturnType<T>
Parameters<T>
```

You should know the most common ones from memory.

---

# 53. `Partial<T>`

```typescript
interface User {
    id: number;
    name: string;
}

type UserUpdate = Partial<User>;
```

All properties become optional.

Useful for update payloads, though API semantics should still be modeled intentionally.

---

# 54. `Required<T>`

```typescript
type CompleteUser = Required<User>;
```

Makes optional properties required.

---

# 55. `Readonly<T>`

```typescript
type ImmutableUser = Readonly<User>;
```

Makes properties readonly at compile time.

---

# 56. `Pick<T, K>`

```typescript
type UserSummary = Pick<User, "id" | "name">;
```

Creates a type containing selected properties.

---

# 57. `Omit<T, K>`

```typescript
type NewUser = Omit<User, "id">;
```

Creates a type excluding specified properties.

Very common for create DTOs.

---

# 58. `Record<K, V>`

```typescript
type Scores = Record<string, number>;
```

Represents an object-like mapping from keys to values.

---

# 59. Mapped Types

Mapped types transform properties.

Example:

```typescript
type Optional<T> = {
    [K in keyof T]?: T[K];
};
```

This is conceptually similar to `Partial<T>`.

You should understand how mapped types work even if you rarely write complex ones.

---

# 60. Conditional Types

Example:

```typescript
type IsString<T> =
    T extends string ? true : false;
```

Conditional types allow type-level branching.

They become important in advanced library and framework typing.

---

# 61. `infer`

`infer` can capture a type inside a conditional type.

Example:

```typescript
type Return<T> =
    T extends (...args: any[]) => infer R
        ? R
        : never;
```

This resembles the built-in `ReturnType<T>` concept.

You do not need to write advanced `infer` types daily, but should recognize the pattern.

---

# 62. Type Narrowing

Narrowing means reducing a broad type to a more specific one based on runtime checks.

Example:

```typescript
function print(value: string | number) {
    if (typeof value === "string") {
        console.log(value.toUpperCase());
    } else {
        console.log(value.toFixed(2));
    }
}
```

Narrowing is one of the most important TypeScript skills.

---

# 63. Narrowing with `typeof`

Useful for primitive runtime types:

```typescript
if (typeof value === "string") {
}
```

Common checks:

```text
string
number
boolean
bigint
symbol
undefined
function
object
```

Remember JavaScript's `typeof null === "object"` oddity.

---

# 64. Narrowing with `instanceof`

```typescript
if (error instanceof Error) {
    console.log(error.message);
}
```

Useful for runtime class/prototype checks.

---

# 65. Narrowing with `in`

```typescript
if ("error" in result) {
    console.log(result.error);
}
```

This can distinguish object shapes.

---

# 66. Truthiness Narrowing

```typescript
if (value) {
    // value is narrowed away from certain falsy possibilities
}
```

Be careful:

```text
0
""
false
```

may be legitimate data.

Do not use truthiness checks when they accidentally exclude valid values.

---

# 67. Equality Narrowing

Example:

```typescript
if (value === null) {
}
```

or:

```typescript
if (status === "success") {
}
```

Literal equality checks are especially useful in discriminated unions.

---

# 68. Custom Type Guards

```typescript
function isUser(value: unknown): value is User {
    return (
        typeof value === "object" &&
        value !== null &&
        "id" in value &&
        "name" in value
    );
}
```

The return type:

```typescript
value is User
```

is a type predicate.

Be careful: a type guard must actually validate the assumptions it claims.

---

# 69. Assertion Functions

Advanced pattern:

```typescript
function assertUser(
    value: unknown
): asserts value is User {
    if (!isUser(value)) {
        throw new Error("Invalid user");
    }
}
```

After the function returns successfully, TypeScript narrows `value` to `User`.

---

# 70. Exhaustive Checking

For discriminated unions:

```typescript
type Status =
    | { kind: "loading" }
    | { kind: "success"; data: string[] }
    | { kind: "error"; error: Error };
```

You can use `never` to detect missing cases.

Conceptually:

```typescript
function assertNever(value: never): never {
    throw new Error("Unexpected value");
}
```

This is valuable when adding new union variants later.

---

# 71. Type Assertions

Syntax:

```typescript
const input = document.querySelector("#name") as HTMLInputElement;
```

A type assertion tells TypeScript:

> Trust me; treat this value as this type.

It does **not** perform runtime validation.

Use assertions carefully.

---

# 72. Non-Null Assertion

```typescript
const button = document.querySelector("#button")!;
```

The `!` tells TypeScript the value is not `null` or `undefined`.

It does not add a runtime check.

Overuse can hide real nullability bugs.

---

# 73. `satisfies`

Example:

```typescript
const config = {
    mode: "dark",
    retryCount: 3
} satisfies Config;
```

`satisfies` checks compatibility while often preserving more specific inferred information than a direct annotation.

This is useful for configuration objects and literal inference.

---

# 74. Structural Typing

TypeScript is primarily structurally typed.

Example:

```typescript
interface Named {
    name: string;
}

const person = {
    name: "Alice",
    age: 30
};

function greet(value: Named) {
    console.log(value.name);
}

greet(person);
```

This works because `person` has at least the required shape.

The declared class/type name is not always what determines compatibility.

---

# 75. Structural vs Nominal Typing

Nominal concept:

```text
compatible because declared as same named type
```

Structural concept:

```text
compatible because shape satisfies requirements
```

Java/C# programmers often need to adjust to this difference.

---

# 76. Excess Property Checks

This may error:

```typescript
interface User {
    name: string;
}

const user: User = {
    name: "Alice",
    age: 30
};
```

because object literals receive excess property checking.

But compatibility through existing variables can behave differently.

Understand this as a useful correctness check, not a contradiction of structural typing.

---

# 77. Classes in TypeScript

JavaScript class:

```typescript
class User {
    constructor(name: string) {
        this.name = name;
    }

    name: string;
}
```

TypeScript adds:

- property types,
- access modifiers,
- interfaces,
- abstract classes,
- generics,
- compile-time contracts.

---

# 78. Class Properties

```typescript
class User {
    name: string;
    age: number;

    constructor(name: string, age: number) {
        this.name = name;
        this.age = age;
    }
}
```

With strict property initialization, properties must be initialized safely.

---

# 79. Access Modifiers

TypeScript supports:

```text
public
private
protected
```

Example:

```typescript
class Account {
    private balance: number = 0;
}
```

These modifiers primarily enforce compile-time access rules.

JavaScript also has runtime-enforced `#privateField` syntax, which is a different mechanism.

---

# 80. `public`

```typescript
public name: string;
```

`public` is the default for class members.

It can often be omitted.

---

# 81. `private`

```typescript
private token: string;
```

TypeScript prevents outside access during type checking.

Understand the difference between TypeScript `private` and JavaScript `#private`.

---

# 82. `protected`

```typescript
protected value: number;
```

Accessible inside the class and derived classes, but not ordinary external code.

---

# 83. Constructor Parameter Properties

TypeScript shorthand:

```typescript
class User {
    constructor(
        public name: string,
        private age: number
    ) {}
}
```

This declares and initializes properties from constructor parameters.

Common in Angular code.

---

# 84. `implements`

```typescript
interface Identifiable {
    id: number;
}

class User implements Identifiable {
    constructor(public id: number) {}
}
```

`implements` checks that the class satisfies the interface contract.

It does not change runtime inheritance.

---

# 85. Abstract Classes

```typescript
abstract class Shape {
    abstract area(): number;

    describe(): string {
        return "shape";
    }
}
```

Derived classes must implement abstract members.

Use when you need shared implementation plus required subclass behavior.

---

# 86. Interface vs Abstract Class

Interface:

```text
shape/contract only
no ordinary runtime implementation object
```

Abstract class:

```text
class hierarchy
shared implementation possible
runtime class/prototype exists
```

Choose based on design needs.

---

# 87. TypeScript Private vs JavaScript Private Fields

TypeScript modifier:

```typescript
private value: number;
```

JavaScript private field:

```typescript
#value: number;
```

`#value` has runtime privacy semantics.

`private` is primarily checked by TypeScript tooling.

Know the distinction.

---

# 88. Modules

TypeScript uses standard JavaScript module syntax:

```typescript
export
import
```

Example:

```typescript
export interface User {
    id: number;
}
```

```typescript
import type { User } from "./user";
```

---

# 89. Type-Only Imports

```typescript
import type { User } from "./user";
```

This communicates that the import is used only for type checking.

Since types are erased, the import can be treated differently by tooling.

---

# 90. Type Erasure

Most TypeScript-specific type syntax disappears from emitted JavaScript.

TypeScript:

```typescript
function greet(name: string): string {
    return `Hello ${name}`;
}
```

Emitted JavaScript conceptually:

```javascript
function greet(name) {
    return `Hello ${name}`;
}
```

Therefore runtime code cannot normally ask for an interface that no longer exists.

---

# 91. Runtime vs Compile-Time

This is one of the most important TypeScript concepts.

Compile-time:

```text
types
interfaces
generic constraints
type errors
```

Runtime:

```text
JavaScript values
objects
functions
HTTP responses
DOM
exceptions
```

Never assume compile-time declarations validate runtime input.

---

# 92. API Data Is Untrusted Runtime Data

Suppose:

```typescript
interface User {
    id: number;
    name: string;
}
```

This does **not** make:

```typescript
const user = await response.json();
```

safe automatically.

The server could return:

```json
{
    "id": "wrong",
    "name": 123
}
```

Your TypeScript interface does not change the actual response.

---

# 93. Dangerous API Assertion

```typescript
const user = await response.json() as User;
```

This tells TypeScript to trust you.

It does not validate the JSON.

Use runtime validation where correctness/security requires it.

---

# 94. Runtime Validation Concept

For external data:

```text
HTTP JSON
   ↓
unknown
   ↓
runtime validation
   ↓
trusted typed data
```

This can be implemented manually or with validation libraries.

Library-specific validation belongs in a later tooling/application map.

---

# 95. Typing `fetch()`

Example:

```typescript
async function loadUsers(): Promise<User[]> {
    const response = await fetch("/api/users");

    if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
    }

    const data: unknown = await response.json();

    // validate data before returning as User[]
    return data as User[];
}
```

For learning, understand where static typing ends and runtime trust begins.

---

# 96. Promise Types

```typescript
Promise<User>
Promise<User[]>
Promise<void>
```

Async functions return Promises.

Example:

```typescript
async function saveUser(): Promise<void> {
}
```

---

# 97. Typed API Response Wrapper

```typescript
interface ApiResponse<T> {
    data: T;
    message?: string;
}
```

Then:

```typescript
type UserListResponse = ApiResponse<User[]>;
```

Generics are especially valuable at API boundaries.

---

# 98. DTO-Style Types

Frontend applications often model request/response shapes separately.

```typescript
interface UserResponse {
    id: number;
    name: string;
}

interface CreateUserRequest {
    name: string;
}
```

Do not force one type to represent every stage of data.

---

# 99. `Pick` and `Omit` for DTOs

Example:

```typescript
interface User {
    id: number;
    name: string;
    createdAt: string;
}

type CreateUserRequest =
    Omit<User, "id" | "createdAt">;
```

Convenient, but only use this when the semantic relationship is truly appropriate.

Sometimes explicit DTO types are clearer.

---

# 100. DOM Typing

Browser APIs have TypeScript declaration types.

Example:

```typescript
const button = document.querySelector("#button");
```

Type may be something like:

```text
Element | null
```

You must account for nullability and element specificity.

---

# 101. Generic `querySelector`

```typescript
const form =
    document.querySelector<HTMLFormElement>("#form");
```

Type:

```text
HTMLFormElement | null
```

Still nullable.

You must prove or handle existence.

---

# 102. Safe DOM Null Handling

```typescript
const button =
    document.querySelector<HTMLButtonElement>("#button");

if (button) {
    button.disabled = true;
}
```

This is safer than blindly using a non-null assertion.

---

# 103. Event Types

Examples:

```typescript
MouseEvent
KeyboardEvent
InputEvent
SubmitEvent
Event
```

Different event types expose different properties.

---

# 104. Typed Event Listener

```typescript
const button =
    document.querySelector<HTMLButtonElement>("#button");

button?.addEventListener("click", (event: MouseEvent) => {
    console.log(event.clientX);
});
```

Often contextual typing can infer `event`.

---

# 105. `event.target`

`event.target` is typically typed broadly.

You may need to narrow:

```typescript
if (event.target instanceof HTMLInputElement) {
    console.log(event.target.value);
}
```

This is an excellent real-world use of narrowing.

---

# 106. `currentTarget`

`event.currentTarget` refers to the element whose listener is currently handling the event.

Its typing can still require care depending on API/context.

Understand `target` vs `currentTarget` from JavaScript first.

---

# 107. Form Typing

```typescript
const form =
    document.querySelector<HTMLFormElement>("#user-form");
```

Then:

```typescript
form?.addEventListener("submit", event => {
    event.preventDefault();

    const data = new FormData(form);
});
```

---

# 108. FormData Return Types

```typescript
const value = formData.get("name");
```

The type can include:

```text
FormDataEntryValue | null
```

You may need to narrow before assuming a string.

---

# 109. React Preparation

React with TypeScript commonly uses types for:

- component props,
- state,
- event handlers,
- refs,
- API data,
- context,
- generic reusable components.

This map should make those concepts feel like TypeScript applied to React rather than a new language.

---

# 110. React Props Concept

Conceptual example:

```typescript
type UserCardProps = {
    name: string;
    age: number;
};
```

Then a React component can accept that shape.

Detailed JSX/React typing belongs in the React skill map.

---

# 111. React Event Typing Concept

React uses its own event type wrappers in TypeScript projects.

You should understand:

```text
DOM event concept
      ↓
framework event abstraction
      ↓
typed handler
```

Do not memorize React-specific event types before learning ordinary DOM event types.

---

# 112. Angular Preparation

Angular heavily uses TypeScript for:

- components,
- services,
- dependency injection,
- interfaces/models,
- generics,
- access modifiers,
- decorators,
- RxJS types.

This skill map should make Angular's TypeScript syntax familiar before Angular-specific concepts are introduced.

---

# 113. Angular Class Example

Conceptual:

```typescript
class MessageService {
    private baseUrl: string = "/api/messages";
}
```

Angular adds framework behavior around such classes.

The type annotation itself is ordinary TypeScript.

---

# 114. Decorators

TypeScript/framework projects may use decorator syntax.

Example conceptually:

```typescript
@Component(...)
```

Decorators are a separate advanced/runtime/toolchain feature and heavily framework-dependent.

Learn their detailed behavior in the Angular/framework map, not as a core starting TypeScript concept.

---

# 115. Compiler

The TypeScript compiler command is commonly:

```bash
tsc
```

Its responsibilities can include:

- type checking,
- transforming TypeScript syntax,
- emitting JavaScript,
- generating declaration files,
- source maps.

---

# 116. Installing TypeScript

In a project:

```bash
npm install --save-dev typescript
```

Then commonly:

```bash
npx tsc
```

or use a project script/build tool.

Global installation exists, but project-local versions improve reproducibility.

---

# 117. `tsconfig.json`

TypeScript project configuration commonly lives in:

```text
tsconfig.json
```

This controls:

- compiler options,
- files included/excluded,
- module behavior,
- output behavior,
- strictness.

You should know how to read and modify it.

---

# 118. Creating a Config

Commonly:

```bash
npx tsc --init
```

This creates a starting configuration.

Do not assume the generated defaults are ideal for every project.

---

# 119. Important `tsconfig` Concepts

Know:

```text
target
module
moduleResolution
lib
strict
noEmit
outDir
rootDir
sourceMap
declaration
esModuleInterop
skipLibCheck
include
exclude
```

Exact appropriate values depend on environment/toolchain.

---

# 120. `target`

Controls which JavaScript language level TypeScript emits.

Conceptually:

```text
modern TS/JS syntax
      ↓
target
      ↓
chosen JS output level
```

Your runtime/browser support requirements influence this.

---

# 121. `module`

Controls emitted module format/behavior expectations.

Modern frontend build tools often manage this as part of their ecosystem.

Do not memorize one module setting as universal.

---

# 122. `lib`

Specifies library declaration sets available to TypeScript.

For frontend development, DOM declarations are relevant.

Conceptually:

```text
language library types
browser DOM types
other runtime API types
```

---

# 123. `strict`

A key setting:

```json
{
    "compilerOptions": {
        "strict": true
    }
}
```

`strict` enables a group of stronger type-checking rules.

For new projects, strict typing is generally a strong default.

---

# 124. Important Strictness Checks

Examples include:

```text
strictNullChecks
noImplicitAny
strictFunctionTypes
strictPropertyInitialization
```

You do not need every flag memorized, but understand what categories strict mode protects.

---

# 125. `noImplicitAny`

Helps prevent accidental `any`.

Example:

```typescript
function greet(name) {
}
```

may error if TypeScript cannot infer and implicit `any` is disallowed.

This encourages explicitness at ambiguous boundaries.

---

# 126. `noEmit`

```json
{
    "compilerOptions": {
        "noEmit": true
    }
}
```

Useful when another tool performs JavaScript transformation and TypeScript is used primarily for type checking.

Common in modern frontend toolchains.

---

# 127. Source Maps

Source maps connect generated JavaScript back to TypeScript source during debugging.

Conceptually:

```text
browser executes JavaScript
        ↓
source map
        ↓
DevTools shows TypeScript source
```

This is critical for practical debugging.

---

# 128. Compile vs Build Tool

In many modern projects:

```text
TypeScript checker
     +
Vite/Webpack/esbuild/SWC/etc.
```

may divide responsibilities.

Do not assume `tsc` is always the tool that produces the browser bundle.

Understand the architecture of your toolchain.

---

# 129. Declaration Files

Type declaration files use:

```text
.d.ts
```

They describe types for JavaScript values/modules/APIs.

Example conceptually:

```typescript
declare function legacyFunction(
    value: string
): number;
```

No implementation is provided.

---

# 130. Why `.d.ts` Files Exist

A JavaScript library may exist at runtime but not contain TypeScript source.

A declaration file tells TypeScript:

```text
these runtime values exist
and have these types
```

This is how TypeScript can type-check usage of JavaScript libraries.

---

# 131. `@types/*`

Many libraries historically obtain community-maintained type declarations through packages such as:

```text
@types/library-name
```

These are commonly sourced from the DefinitelyTyped ecosystem.

Some modern libraries ship their own declarations and need no separate `@types` package.

---

# 132. Ambient Declarations

The `declare` keyword can describe values that exist elsewhere at runtime.

Example:

```typescript
declare const APP_VERSION: string;
```

TypeScript trusts that the runtime environment supplies it.

Use carefully.

---

# 133. Module Declaration

You may encounter:

```typescript
declare module "some-library" {
    // type declarations
}
```

This describes an external module's type surface.

Useful when third-party typings are incomplete or unavailable.

---

# 134. Declaration Merging

Interfaces and certain declarations can merge.

Example:

```typescript
interface Window {
    appVersion: string;
}
```

This can augment existing global declarations.

Powerful, but should be used intentionally.

---

# 135. Compiler Diagnostics

TypeScript error messages often describe:

- expected type,
- actual type,
- missing property,
- incompatible property,
- possible null/undefined,
- generic constraint failure.

Train yourself to parse the type relationship rather than only reading the final line.

---

# 136. Common Error — Not Assignable

Example:

```text
Type 'string' is not assignable to type 'number'
```

Ask:

```text
What type was expected?
What type did I provide?
Why is that value flowing here?
```

Trace data flow instead of adding `as any`.

---

# 137. Common Error — Property Does Not Exist

Example:

```text
Property 'foo' does not exist on type 'Bar'
```

Possibilities:

- wrong property name,
- wrong object type,
- incomplete model,
- union not narrowed,
- library declaration mismatch.

Do not immediately force a type assertion.

---

# 138. Common Error — Possibly Null

Example:

```text
Object is possibly 'null'
```

This is TypeScript warning you about a real runtime possibility.

Prefer:

- null check,
- optional chaining,
- earlier invariant validation.

Use `!` only when the invariant is genuinely guaranteed.

---

# 139. Common Error — Implicit Any

Example:

```text
Parameter implicitly has an 'any' type
```

Either:

- annotate it,
- provide enough context for inference,
- improve the API typing.

Do not globally weaken strictness just to silence errors.

---

# 140. Common Error — Generic Constraint

If:

```typescript
T extends { id: number }
```

then a value lacking numeric `id` is rejected.

Read constraints as requirements.

---

# 141. Common Error — Union Property Access

Example:

```typescript
function handle(value: string | number) {
    value.toUpperCase();
}
```

Error because not every union member has that method.

Narrow first.

---

# 142. Common Error — Excess Property

An object literal contains fields not expected by the target type.

Investigate whether:

- type is wrong,
- object shape is wrong,
- target type is too narrow,
- you intended a broader intermediate object.

Do not automatically cast it away.

---

# 143. Avoid `as any` as a Fix

This:

```typescript
value as any
```

removes useful checking.

It can be useful temporarily during migration, but should not become the default way to satisfy the compiler.

A TypeScript error is often evidence of a real modeling issue.

---

# 144. Avoid Over-Annotation

Poor style:

```typescript
const name: string = "Alice";
const age: number = 30;
const active: boolean = true;
```

These annotations may add little value.

Better to focus explicit types at:

- function boundaries,
- public APIs,
- shared models,
- ambiguous values,
- complex generics.

---

# 145. Avoid Overly Clever Types

TypeScript's type system is very powerful.

But a type that requires several minutes to understand may be worse than a simpler design.

Optimize for:

- safety,
- clarity,
- maintainability.

Not type-level cleverness for its own sake.

---

# 146. Type-Driven Design

Good TypeScript can make invalid application states harder to represent.

Weak:

```typescript
interface RequestState {
    loading: boolean;
    error?: string;
    data?: User[];
}
```

Many contradictory states are possible.

Stronger:

```typescript
type RequestState =
    | { status: "loading" }
    | { status: "error"; error: string }
    | { status: "success"; data: User[] };
```

This is a major benefit of discriminated unions.

---

# 147. Prefer Narrow Types

Instead of:

```typescript
status: string;
```

when only three values are valid:

```typescript
status: "idle" | "loading" | "done";
```

Narrow types encode real domain constraints.

---

# 148. Branded/Nominal-Like Types

Advanced TypeScript sometimes simulates nominal distinctions.

Conceptual:

```typescript
type UserId = string & {
    readonly __brand: "UserId";
};
```

This can distinguish otherwise identical primitive structures.

Understand the concept, but do not overuse it.

---

# 149. Readonly and Immutability

TypeScript readonly types protect mutation through the type system.

They do not necessarily freeze runtime objects.

Runtime:

```javascript
Object.freeze()
```

is a separate JavaScript mechanism with its own behavior/limitations.

---

# 150. Function Variance Concept

Advanced typing considers whether one function type is assignable to another based on parameter/return relationships.

You do not need variance theory immediately, but recognize it when strict function type errors appear.

---

# 151. Type Compatibility Is Not Runtime Conversion

If TypeScript allows:

```typescript
const user: User = person;
```

that does not create a new object.

No runtime conversion occurs.

It is only a compile-time compatibility judgment.

---

# 152. `JSON.parse()` and Types

`JSON.parse()` returns broadly typed data.

Example:

```typescript
const value = JSON.parse(text);
```

Do not assume parsing means validating.

Parsing checks JSON syntax, not application shape.

---

# 153. Error Handling with `unknown`

Caught errors may be treated as unknown in strict setups.

Good pattern:

```typescript
try {
    // ...
} catch (error) {
    if (error instanceof Error) {
        console.error(error.message);
    } else {
        console.error("Unknown error");
    }
}
```

This reinforces safe narrowing.

---

# 154. Typed Storage

`localStorage.getItem()` returns:

```text
string | null
```

If storing JSON:

```typescript
const raw = localStorage.getItem("user");

if (raw !== null) {
    const parsed: unknown = JSON.parse(raw);
    // validate before trusting
}
```

Again, static types do not validate persisted runtime data.

---

# 155. TypeScript and Forms

Forms often produce string values.

Example:

```typescript
const ageText = formData.get("age");
```

Even if the HTML input is `type="number"`, form data still needs careful conversion/validation.

Do not assume browser UI control type equals trusted TypeScript domain type.

---

# 156. TypeScript and React State

A React state model might be:

```typescript
type LoadState =
    | { status: "idle" }
    | { status: "loading" }
    | { status: "success"; data: User[] }
    | { status: "error"; error: string };
```

This kind of TypeScript modeling transfers directly into React.

---

# 157. TypeScript and Angular Services

Angular service methods commonly expose typed data:

```typescript
getMessages(): Observable<Message[]> {
    // ...
}
```

`Observable` is an RxJS/Angular ecosystem topic, but the generic:

```text
Observable<Message[]>
```

is ordinary TypeScript generic syntax.

---

# 158. TypeScript and Dependency Injection

Angular constructor example:

```typescript
constructor(
    private messageService: MessageService
) {}
```

The access modifier/type syntax is TypeScript.

The dependency injection behavior is Angular.

Keep those layers separate mentally.

---

# 159. Testing TypeScript

TypeScript helps tests by checking:

- test inputs,
- mock shapes,
- function signatures,
- return types.

But static type checking is not a replacement for runtime tests.

Types prove certain structural relationships, not full application behavior.

---

# 160. Static Types vs Tests

TypeScript can catch:

```text
wrong property name
wrong argument type
possibly null value
invalid union case
```

Tests can catch:

```text
wrong business logic
incorrect calculation
unexpected runtime interaction
API behavior
DOM behavior
```

Use both.

---

# 161. TypeScript Debugging

Debugging has two layers.

## Compile-Time

Use:

- editor diagnostics,
- `tsc`,
- type hover information,
- inferred type inspection.

## Runtime

Use:

- browser DevTools,
- source maps,
- stack traces,
- breakpoints,
- Network panel.

Do not confuse a TypeScript type error with a JavaScript runtime exception.

---

# 162. IDE Type Information

Modern editors can show:

- inferred type,
- function signature,
- generic substitution,
- error explanation,
- declaration location.

Use hover/type inspection as a learning tool.

Do not rely on autocomplete without understanding the type model.

---

# 163. `tsc --noEmit`

Useful:

```bash
npx tsc --noEmit
```

This checks types without writing JavaScript output.

Very common in CI and build pipelines.

---

# 164. Watch Mode

```bash
npx tsc --watch
```

TypeScript can continuously re-check during development.

Framework/build tools may provide their own watch process.

---

# 165. CI Type Checking

A production pipeline may run:

```text
install dependencies
        ↓
type check
        ↓
lint
        ↓
tests
        ↓
build
```

Type checking should be treated as part of code quality, not merely editor convenience.

---

# 166. Dependency Types

When installing a package, determine:

```text
Does it ship TypeScript declarations?
Does it require @types/...?
What version compatibility exists?
```

Incorrect type package versions can create confusing compiler errors.

---

# 167. Library Types vs Runtime Package

A type declaration package does not provide runtime functionality.

Example conceptually:

```text
runtime library package
    +
type declaration package
```

may both be needed.

Do not import a type package expecting actual executable code.

---

# 168. TypeScript Version Awareness

TypeScript evolves quickly.

Features and inference behavior can change across versions.

Before following advanced guidance, check:

```text
TypeScript version
framework version
compiler config
```

---

# 169. React Version Awareness

React typings can evolve alongside React itself.

Framework-specific patterns should be learned from the React version you are actually using.

Keep generic TypeScript fundamentals separate from framework-specific types.

---

# 170. Angular Version Awareness

Angular strongly couples its supported TypeScript range to Angular versions.

When upgrading Angular, TypeScript compatibility matters.

Framework upgrade documentation should be treated as authoritative for supported versions.

---

# 171. Practical Exercise — JavaScript to TypeScript

Start with:

```javascript
function greet(user) {
    return `Hello, ${user.name}`;
}
```

Convert it to TypeScript with:

- user model,
- parameter type,
- return type,
- optional nickname,
- readonly ID.

Explain every added type.

---

# 172. Practical Exercise — Union Narrowing

Create:

```typescript
function format(value: string | number): string
```

Behavior:

- strings become uppercase,
- numbers become fixed to two decimal places.

Use `typeof` narrowing.

---

# 173. Practical Exercise — Discriminated Union

Model:

```text
loading
success with data
error with message
```

Write a rendering function that handles each state exhaustively.

Add a new state later and use exhaustive checking to discover unhandled code.

---

# 174. Practical Exercise — Generics

Write:

```typescript
first<T>()
last<T>()
```

for arrays.

Then write:

```typescript
getProperty<T, K extends keyof T>()
```

Explain how generics preserve relationships.

---

# 175. Practical Exercise — Utility Types

Given:

```typescript
interface User {
    id: number;
    name: string;
    email: string;
    createdAt: string;
}
```

Create:

```text
CreateUserRequest
UpdateUserRequest
UserSummary
ReadonlyUser
```

using:

```text
Omit
Partial
Pick
Readonly
```

Then discuss when explicit interfaces would be clearer.

---

# 176. Practical Exercise — Structural Typing

Create two object types that are not explicitly related but have compatible shape.

Pass one into a function expecting the other.

Explain why it works.

Then add a missing property and observe the compiler error.

---

# 177. Practical Exercise — DOM Typing

Build a form with:

```text
name input
age input
submit button
```

Type:

- form element,
- input elements,
- submit event,
- FormData results.

Safely convert age from string to number.

Handle missing elements without non-null assertions.

---

# 178. Practical Exercise — API Typing

Define:

```typescript
interface Message {
    id: number;
    name: string;
    message: string;
}
```

Write:

```typescript
async function loadMessages(): Promise<Message[]>
```

Then modify it so raw `response.json()` is treated as `unknown` and validated before returning.

---

# 179. Practical Exercise — Error Handling

Write an async function that may:

- return valid data,
- receive a non-2xx response,
- throw a network error,
- receive invalid JSON shape.

Use `unknown`, `instanceof Error`, and explicit validation.

---

# 180. Practical Exercise — Class Typing

Create:

```text
Person
Employee
Manager
```

Use:

- public,
- private,
- protected,
- readonly,
- interface,
- implements,
- abstract class,
- constructor parameter properties.

Explain which rules exist only at compile time.

---

# 181. Practical Exercise — `tsconfig`

Create a small TypeScript project.

Enable:

```text
strict
sourceMap
```

Experiment with:

```text
target
noEmit
outDir
```

Observe how the compiler output changes.

---

# 182. Practical Exercise — Declaration File

Create a tiny plain JavaScript module:

```javascript
export function greet(name) {
    return `Hello ${name}`;
}
```

Then write a `.d.ts` file describing it.

Import it from TypeScript and confirm the editor understands the function signature.

---

# 183. Practical Exercise — React Preparation

Without building a full React app, define types for:

```text
UserCardProps
LoadState
FormState
API response
event callback
```

The goal is to make later React typing feel familiar.

---

# 184. Practical Exercise — Angular Preparation

Define:

```typescript
interface Message
interface CreateMessageRequest
class MessageService
```

Use:

- private property,
- typed method return,
- generic response wrapper.

Do not add Angular decorators yet.

---

# 185. Level 1 — Basic Type Annotations

You understand:

```text
primitive types
arrays
objects
functions
inference
annotations
```

You can convert simple JavaScript into TypeScript.

---

# 186. Level 2 — Reusable Data Models

You understand:

```text
interfaces
type aliases
optional properties
readonly
unions
literal types
tuples
intersections
```

You can model frontend/domain data accurately.

---

# 187. Level 3 — Safe Control Flow

You understand:

```text
narrowing
typeof
instanceof
in
truthiness
custom type guards
discriminated unions
never
```

You can safely work with uncertain/union values.

---

# 188. Level 4 — Generics

You understand:

```text
generic functions
generic interfaces
constraints
keyof
indexed access
utility types
```

You can preserve type relationships in reusable code.

---

# 189. Level 5 — Advanced Type Construction

You understand:

```text
mapped types
conditional types
infer
satisfies
typeof type queries
```

You can read advanced library/framework types without being lost.

---

# 190. Level 6 — Runtime Boundaries

You understand:

```text
type erasure
unknown external data
API validation
JSON parsing
DOM nullability
storage
type assertions
```

You know where TypeScript guarantees stop.

---

# 191. Level 7 — Classes and OOP

You understand:

```text
public
private
protected
readonly
implements
abstract
constructor parameter properties
```

You can read Angular-style TypeScript classes confidently.

---

# 192. Level 8 — Tooling

You understand:

```text
tsc
tsconfig.json
strict
target
module
lib
noEmit
source maps
.d.ts
@types
```

You can configure and debug a TypeScript project.

---

# 193. Level 9 — Framework Readiness

You can:

- type React props/state concepts,
- type DOM events,
- model API data,
- understand Angular class syntax,
- understand generic framework types,
- distinguish TypeScript errors from framework errors.

You are ready for React or Angular without TypeScript being the main obstacle.

---

# 194. Interview Questions — Fundamentals

Be prepared to answer:

1. What is TypeScript?
2. How is TypeScript related to JavaScript?
3. Does the browser run TypeScript directly?
4. What happens to TypeScript types at runtime?
5. What is static typing?
6. What is type inference?
7. When should you annotate a type explicitly?
8. What is structural typing?
9. How does structural typing differ from nominal typing?
10. What is a union type?
11. What is an intersection type?
12. What is a literal type?
13. What is a tuple?
14. What is the difference between interface and type?
15. What does `readonly` do?

---

# 195. Interview Questions — Special Types

1. What is `any`?
2. Why is `any` dangerous?
3. What is `unknown`?
4. Why is `unknown` safer than `any`?
5. What is `never`?
6. What is `void`?
7. What is the difference between `null` and `undefined`?
8. What does strict null checking do?
9. What is a non-null assertion?
10. Why can non-null assertions be dangerous?

---

# 196. Interview Questions — Narrowing

1. What is type narrowing?
2. How does `typeof` narrowing work?
3. How does `instanceof` narrowing work?
4. What does the `in` operator do for narrowing?
5. What is a discriminated union?
6. What is a custom type guard?
7. What is a type predicate?
8. What is exhaustive checking?
9. How is `never` used in exhaustive checks?
10. Why can truthiness narrowing be dangerous for values like `0` or `""`?

---

# 197. Interview Questions — Generics

1. What is a generic?
2. Why use generics instead of `any`?
3. What is a generic constraint?
4. What does `keyof` do?
5. What is indexed access typing?
6. What does `Partial<T>` do?
7. What does `Pick<T, K>` do?
8. What does `Omit<T, K>` do?
9. What does `Record<K, V>` do?
10. What is a mapped type?
11. What is a conditional type?
12. What does `infer` do?

---

# 198. Interview Questions — Runtime Boundary

1. Do interfaces exist at runtime?
2. Does `as User` validate an object?
3. Does `JSON.parse()` validate your application type?
4. Why should API responses often begin as `unknown`?
5. What is type erasure?
6. What does a type assertion do?
7. What is the difference between compile-time and runtime errors?
8. How would you validate external JSON safely?
9. Why doesn't TypeScript replace backend validation?
10. Why doesn't TypeScript replace automated tests?

---

# 199. Interview Questions — Classes/OOP

1. What does `implements` do?
2. What is an abstract class?
3. Interface vs abstract class?
4. What do `public`, `private`, and `protected` mean?
5. What is a constructor parameter property?
6. What is the difference between TypeScript `private` and JavaScript `#private`?
7. Does TypeScript `private` guarantee runtime privacy?
8. What does `readonly` mean for a class property?
9. How do TypeScript classes relate to JavaScript classes/prototypes?
10. Why might Angular code use access modifiers heavily?

---

# 200. Interview Questions — Compiler/Tooling

1. What does `tsc` do?
2. What is `tsconfig.json`?
3. What does `strict` do?
4. What does `noImplicitAny` do?
5. What does `strictNullChecks` do?
6. What is `target`?
7. What is `module`?
8. What is `lib`?
9. What does `noEmit` do?
10. What is a source map?
11. What is a `.d.ts` file?
12. What is `@types`?
13. What is DefinitelyTyped?
14. Why might a library not need an `@types` package?

---

# 201. Interview Questions — Frontend Use

1. How do you type DOM elements?
2. Why does `querySelector()` often return `null`?
3. How do you safely access `event.target`?
4. How do you type form data?
5. How do you type async API functions?
6. What type does an async function return?
7. How would you model loading/success/error state?
8. Why are discriminated unions useful for UI state?
9. How does TypeScript help React?
10. How does TypeScript help Angular?

---

# 202. Memorization Targets — Core Syntax

Know from memory:

```text
: type
interface
type
|
&
?
readonly
extends
implements
abstract
public
private
protected
generic <T>
keyof
typeof (type query)
unknown
never
void
as
satisfies
import type
```

---

# 203. Memorization Targets — Common Utility Types

Know:

```text
Partial
Required
Readonly
Pick
Omit
Record
Exclude
Extract
NonNullable
ReturnType
Parameters
```

You do not need every built-in utility type memorized.

---

# 204. Memorization Targets — Compiler Concepts

Know:

```text
tsc
tsconfig.json
strict
strictNullChecks
noImplicitAny
target
module
lib
noEmit
sourceMap
.d.ts
@types
```

---

# 205. What to Look Up

It is normal to look up:

- advanced conditional types,
- complex `infer` patterns,
- framework-specific generic types,
- unusual compiler flags,
- JSX typing edge cases,
- declaration-merging details,
- third-party library overloads,
- variance details,
- module-resolution edge cases.

Expertise means understanding the model well enough to interpret the documentation.

---

# 206. Documentation Reading Checklist

When reading a TypeScript API/type:

```text
1. What is the runtime value?
2. What is the compile-time type?
3. Is this generic?
4. What does each generic parameter represent?
5. Are there constraints?
6. Is the return type nullable?
7. Is the return type a Promise?
8. Is this a union?
9. How is the union narrowed?
10. Does this type exist at runtime?
11. Is this a declaration only?
12. Is a type assertion being used?
13. Is external data actually validated?
```

---

# 207. Recommended Learning Order

```text
JavaScript prerequisite
        ↓
What TypeScript is
        ↓
inference + annotations
        ↓
primitive/object/array types
        ↓
interfaces + aliases
        ↓
unions + literals
        ↓
narrowing
        ↓
functions
        ↓
generics
        ↓
keyof + indexed access
        ↓
utility types
        ↓
structural typing
        ↓
classes/access modifiers
        ↓
runtime vs compile-time
        ↓
DOM/events/forms
        ↓
API typing
        ↓
tsconfig/compiler
        ↓
.d.ts / @types
        ↓
advanced mapped/conditional types
        ↓
React/Angular preparation
```

---

# 208. Final Mastery Project

Take a small plain JavaScript frontend and convert it to TypeScript.

Example project:

```text
message-board/
├── index.html
├── styles.css
└── src/
    ├── main.ts
    ├── api.ts
    ├── types.ts
    ├── form.ts
    └── ui.ts
```

Required features:

## Type Modeling

Create:

```typescript
interface Message
interface CreateMessageRequest
type LoadState
```

Use:

- readonly where appropriate,
- optional properties where appropriate,
- discriminated unions for request state.

## DOM

Type:

- form,
- inputs,
- buttons,
- event handlers.

Avoid unnecessary `!` assertions.

## API

Write:

```typescript
async function loadMessages(): Promise<Message[]>
```

Treat external JSON as untrusted runtime data.

Validate before returning trusted domain types.

## Generics

Create at least one useful generic, such as:

```typescript
interface ApiResponse<T>
```

## Utility Types

Use at least two:

```text
Pick
Omit
Partial
Readonly
```

## Compiler

Use:

```text
strict
source maps
```

Understand all important settings in the project's `tsconfig.json`.

## Debugging

Demonstrate:

- compiler error diagnosis,
- runtime JavaScript debugging,
- source maps,
- API/network debugging.

## Framework Preparation

Create a small file showing how the same domain types could be reused as:

```text
React props/state
Angular service/component models
```

without actually building either framework application.

---

# 209. Final Standard for TypeScript Mastery

You should be able to take an existing JavaScript frontend and systematically convert it to TypeScript.

You should understand and use:

```text
type inference
explicit annotations
primitive types
arrays
tuples
object types
interfaces
type aliases
unions
intersections
literal types
optional properties
readonly
generics
constraints
keyof
indexed access
utility types
mapped types
conditional types
```

You should be able to narrow uncertain values using:

```text
typeof
instanceof
in
literal discrimination
custom type guards
```

You should understand:

```text
any
unknown
never
void
null
undefined
```

and know when each is appropriate.

You should understand TypeScript's structural type system and be able to explain why values are compatible based on shape rather than just named declarations.

You should understand classes and TypeScript additions such as:

```text
public
private
protected
readonly
abstract
implements
constructor parameter properties
```

You should understand that:

> TypeScript types are compile-time tools and normally disappear before runtime.

Therefore you should never confuse:

```text
type assertion
```

with:

```text
runtime validation
```

You should be able to safely type:

- DOM elements,
- browser events,
- forms,
- API request/response models,
- Promises,
- storage values.

You should understand:

```text
tsc
tsconfig.json
strict mode
source maps
declaration files
@types
```

You should be able to read TypeScript compiler errors and fix the data/type model rather than defaulting to:

```typescript
as any
```

The end goal is:

> **Use TypeScript to make JavaScript code easier to reason about, safer to refactor, and clearer at application boundaries; understand where the type system is powerful and where it stops; and be prepared to use TypeScript naturally inside React or Angular rather than learning the language at the same time as the framework.**

---

# 210. What Comes Next

After this map, the natural frontend paths are:

```text
TypeScript
   ├── React
   └── Angular
```

Useful separate future skill maps:

```text
React
Angular
RxJS
Frontend testing
Vite/build tooling
npm/package management
Runtime schema validation
REST/API design
Web accessibility
Frontend architecture
```

For a full-stack Java/Angular path:

```text
HTML/CSS
   ↓
JavaScript
   ↓
TypeScript
   ↓
Angular
   ↓
HTTP / REST
   ↓
Spring Boot
   ↓
PostgreSQL
```

For a React path:

```text
HTML/CSS
   ↓
JavaScript
   ↓
TypeScript
   ↓
React
   ↓
HTTP / API integration
   ↓
backend
```

TypeScript should become a foundation beneath the framework, not an additional source of confusion.
