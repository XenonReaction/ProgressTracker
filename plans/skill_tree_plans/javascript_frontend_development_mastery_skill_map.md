# JavaScript for Frontend Development — Mastery Skill Map

## Purpose

This skill map assumes **zero prior JavaScript knowledge**. Basic HTML and CSS are prerequisites only where JavaScript interacts with a web page.

The primary goal is frontend development, while teaching JavaScript deeply enough that frameworks such as React, Angular, or Vue do not hide gaps in the underlying language.

By the end, you should be able to:

- write small-to-medium JavaScript programs from memory,
- read unfamiliar JavaScript and reason about its behavior,
- understand JavaScript's type system and unusual language behavior,
- use functions, arrays, objects, maps, sets, classes, and modules,
- understand scope, closures, `this`, prototypes, and references,
- explain synchronous versus asynchronous execution,
- understand callbacks, Promises, the event loop, `async`/`await`, and `fetch`,
- manipulate HTML through the DOM,
- handle browser events and forms,
- communicate with backend APIs,
- debug JavaScript using browser developer tools,
- understand frontend security boundaries,
- understand Node.js and npm sufficiently to work with modern frontend tooling,
- and explain these concepts confidently in interviews.

This is a **JavaScript skill map**, not a React, Angular, TypeScript, or Node backend curriculum.

---

# 1. JavaScript Mental Model

JavaScript is a programming language.

It can run in different environments:

```text
                    JavaScript
                        │
            ┌───────────┴───────────┐
            │                       │
         Browser                  Node.js
            │                       │
       Web APIs / DOM        Files / server / CLI
            │
      Frontend applications
```

JavaScript itself provides language features such as:

```text
variables
functions
objects
arrays
classes
Promises
modules
```

The browser provides APIs such as:

```text
document
window
fetch
localStorage
setTimeout
DOM events
```

Node.js provides a different runtime environment.

A key expert distinction is:

> JavaScript is the language. The browser and Node.js are environments that provide additional APIs around the language.

---

# 2. JavaScript vs HTML vs CSS

A useful beginner model:

```text
HTML        structure and meaning
CSS         presentation and layout
JavaScript  behavior and application logic
```

Example:

```html
<button id="button">Click me</button>
```

```css
.active {
    background: green;
}
```

```javascript
const button = document.querySelector("#button");

button.addEventListener("click", () => {
    button.classList.toggle("active");
});
```

Each technology has a different responsibility.

---

# 3. JavaScript vs Node.js vs React

## JavaScript

Programming language.

## Node.js

A JavaScript runtime outside the browser.

Examples:

```bash
node script.js
npm install
```

Node can be used for:

- servers,
- command-line programs,
- scripts,
- development tooling,
- frontend build systems.

## React

A JavaScript library for building user interfaces.

React uses JavaScript.

Node.js and React are therefore not alternatives:

```text
JavaScript
├── browser JavaScript
│   └── React can run here
└── Node.js
    └── often powers React development/build tooling
```

---

# 4. JavaScript Worth Memorizing

You should eventually recognize and write these structures without documentation.

## Variable

```javascript
const name = "Alice";
let count = 0;
```

## Conditional

```javascript
if (condition) {
    // code
} else {
    // code
}
```

## Loop

```javascript
for (const item of items) {
    console.log(item);
}
```

## Function

```javascript
function add(a, b) {
    return a + b;
}
```

## Arrow Function

```javascript
const add = (a, b) => {
    return a + b;
};
```

Short form:

```javascript
const add = (a, b) => a + b;
```

## Array

```javascript
const numbers = [1, 2, 3];
```

## Object

```javascript
const user = {
    name: "Alice",
    age: 30
};
```

## Class

```javascript
class User {
    constructor(name) {
        this.name = name;
    }

    greet() {
        return `Hello, ${this.name}`;
    }
}
```

## Error Handling

```javascript
try {
    // code
} catch (error) {
    console.error(error);
}
```

## Promise / `await`

```javascript
const response = await fetch("/api/users");
const data = await response.json();
```

## DOM Selection

```javascript
const element = document.querySelector(".item");
```

## Event Listener

```javascript
element.addEventListener("click", () => {
    console.log("clicked");
});
```

## Module

```javascript
export function add(a, b) {
    return a + b;
}
```

```javascript
import { add } from "./math.js";
```

---

# 5. Statements and Expressions

An **expression** produces a value.

```javascript
2 + 3
```

```javascript
name
```

```javascript
add(2, 3)
```

A **statement** performs an action or controls execution.

```javascript
const x = 5;
```

```javascript
if (x > 3) {
    console.log(x);
}
```

Understanding this distinction helps explain JavaScript syntax.

---

# 6. Variables

Variables associate names with values.

Modern JavaScript primarily uses:

```javascript
const
let
```

Older JavaScript frequently uses:

```javascript
var
```

Prefer `const` unless reassignment is required.

Use `let` when the variable must be reassigned.

---

# 7. `const`

```javascript
const name = "Alice";
```

The variable cannot be reassigned:

```javascript
name = "Bob";
```

causes an error.

However:

```javascript
const user = {
    name: "Alice"
};

user.name = "Bob";
```

is allowed.

`const` prevents reassignment of the variable binding. It does **not** automatically make an object immutable.

---

# 8. `let`

```javascript
let count = 0;

count = count + 1;
```

Use `let` when reassignment is part of the program's logic.

---

# 9. `var`

Older JavaScript uses:

```javascript
var count = 0;
```

You must understand `var` because existing code uses it.

Important differences involve:

- function scope,
- hoisting,
- redeclaration.

Modern application code usually favors `const` and `let`.

---

# 10. JavaScript Types

Primitive types:

```text
string
number
boolean
undefined
null
bigint
symbol
```

Objects form the major non-primitive category.

Examples:

```javascript
"hello"
42
true
undefined
null
123n
Symbol("id")
{}
[]
```

Arrays and functions are objects in important JavaScript type-system senses.

---

# 11. `typeof`

Inspect basic type information:

```javascript
typeof "hello"
```

returns:

```text
"string"
```

Examples:

```javascript
typeof 42
typeof true
typeof undefined
typeof {}
typeof []
typeof function () {}
```

Know the historical oddity:

```javascript
typeof null
```

returns:

```text
"object"
```

Do not use this as evidence that `null` conceptually means an ordinary object.

---

# 12. Strings

Examples:

```javascript
const first = "Hello";
const second = 'Hello';
```

Template literal:

```javascript
const name = "Alice";

const message = `Hello, ${name}`;
```

Know common operations:

```javascript
text.length
text.toUpperCase()
text.toLowerCase()
text.includes()
text.slice()
text.trim()
text.replace()
text.split()
```

Strings are immutable.

---

# 13. Numbers

JavaScript commonly uses one `number` type for both integer-like and floating-point values.

```javascript
const age = 30;
const price = 19.99;
```

Operators:

```text
+
-
*
/
%
**
```

Understand floating-point limitations:

```javascript
0.1 + 0.2
```

does not produce an exact decimal `0.3` representation.

---

# 14. `NaN`

`NaN` means:

**Not-a-Number**

Example:

```javascript
Number("hello")
```

produces `NaN`.

Useful check:

```javascript
Number.isNaN(value)
```

`NaN` is still of JavaScript type:

```text
number
```

This is another historical behavior worth recognizing.

---

# 15. Booleans

```javascript
true
false
```

Used heavily in conditions:

```javascript
const loggedIn = true;

if (loggedIn) {
    console.log("Welcome");
}
```

---

# 16. `undefined`

`undefined` generally represents the absence of an assigned/provided value.

Example:

```javascript
let name;

console.log(name);
```

outputs:

```text
undefined
```

Functions without an explicit returned value also return `undefined`.

---

# 17. `null`

`null` is commonly used to intentionally represent:

> no value / no object

Example:

```javascript
const selectedUser = null;
```

A useful distinction:

```text
undefined → value is absent/not provided
null      → absence intentionally represented
```

Actual application conventions vary.

---

# 18. `null` vs `undefined`

Know the difference well enough to explain it.

```javascript
let x;
```

produces:

```text
undefined
```

Whereas:

```javascript
let user = null;
```

explicitly assigns an empty/absent state.

---

# 19. Dynamic Typing

JavaScript is dynamically typed.

```javascript
let value = 10;

value = "hello";
```

This is valid JavaScript.

The variable does not have a permanently declared static type.

The current value has a type.

This differs significantly from statically typed languages, where a variable's type is fixed at compile time. (If you already know Java: a JavaScript variable behaves nothing like a typed Java variable — you'll see Java's static typing in the dedicated Java skill tree.)

---

# 20. Type Coercion

JavaScript sometimes converts values automatically.

Example:

```javascript
"5" + 2
```

produces:

```text
"52"
```

while:

```javascript
"5" - 2
```

produces:

```text
3
```

Understanding coercion prevents many confusing bugs.

Prefer explicit conversions when clarity matters.

---

# 21. Explicit Conversion

Useful functions:

```javascript
String(value)
Number(value)
Boolean(value)
```

Examples:

```javascript
Number("42")
String(42)
Boolean(1)
```

Also recognize:

```javascript
parseInt()
parseFloat()
```

and understand that they have different parsing semantics from `Number()`.

---

# 22. Truthy and Falsy

JavaScript conditions convert values to booleans.

Common falsy values include:

```text
false
0
-0
0n
""
null
undefined
NaN
```

Most other values are truthy.

Important:

```javascript
[]
{}
"0"
"false"
```

are truthy.

---

# 23. Equality

JavaScript has:

```javascript
==
!=
===
!==
```

Prefer strict equality in most application code:

```javascript
value === 5
value !== 5
```

`==` performs coercion.

`===` compares without that coercive equality conversion.

Be able to explain why:

```javascript
5 == "5"
```

is true, while:

```javascript
5 === "5"
```

is false.

---

# 24. Comparison Operators

Know:

```text
>
<
>=
<=
===
!==
```

Examples:

```javascript
age >= 18
name === "Alice"
```

---

# 25. Logical Operators

Know:

```text
&&
||
!
```

Examples:

```javascript
loggedIn && isAdmin
```

```javascript
hasName || hasEmail
```

```javascript
!enabled
```

Importantly, `&&` and `||` return operands, not necessarily literal booleans.

---

# 26. Short-Circuit Evaluation

Example:

```javascript
user && user.name
```

The second expression is evaluated only if the first is truthy.

Likewise:

```javascript
cachedValue || createValue()
```

can avoid evaluating the second operand.

Understand this behavior before using it as shorthand.

---

# 27. Nullish Coalescing

Operator:

```javascript
??
```

Example:

```javascript
const name = inputName ?? "Anonymous";
```

The fallback is used only for:

```text
null
undefined
```

This differs from:

```javascript
inputName || "Anonymous"
```

which also replaces other falsy values such as `0` or `""`.

---

# 28. Optional Chaining

Operator:

```javascript
?.
```

Example:

```javascript
user?.address?.city
```

If an intermediate value is `null` or `undefined`, evaluation safely produces `undefined` instead of attempting the next property access.

---

# 29. Arithmetic Operators

Know:

```text
+
-
*
/
%
**
```

Increment/decrement exist:

```javascript
count++;
count--;
```

But explicit forms are often clearer:

```javascript
count += 1;
```

---

# 30. Assignment Operators

Know:

```text
=
+=
-=
*=
/=
??=
&&=
||=
```

You do not need every compound operator memorized immediately, but recognize the pattern.

---

# 31. Operator Precedence

Operators execute according to precedence rules.

Example:

```javascript
2 + 3 * 4
```

evaluates multiplication first.

When intent may be unclear, use parentheses:

```javascript
(2 + 3) * 4
```

Do not try to memorize the entire precedence table.

Know common cases and use parentheses for clarity.

---

# 32. `if` / `else`

```javascript
if (age >= 18) {
    console.log("Adult");
} else {
    console.log("Minor");
}
```

Multiple branches:

```javascript
if (score >= 90) {
    grade = "A";
} else if (score >= 80) {
    grade = "B";
} else {
    grade = "C";
}
```

---

# 33. Ternary Operator

```javascript
condition ? valueIfTrue : valueIfFalse
```

Example:

```javascript
const label = loggedIn ? "Logout" : "Login";
```

Use for simple conditional expressions.

Avoid deeply nested ternaries that reduce readability.

---

# 34. `switch`

```javascript
switch (status) {
    case "loading":
        console.log("Loading...");
        break;

    case "success":
        console.log("Done");
        break;

    default:
        console.log("Unknown");
}
```

Understand fall-through and why `break` is often required.

---

# 35. Loops

Know:

```text
for
while
do...while
for...of
for...in
```

Different loops serve different purposes.

---

# 36. Traditional `for`

```javascript
for (let i = 0; i < 10; i++) {
    console.log(i);
}
```

Understand:

```text
initialization
condition
update
body
```

---

# 37. `while`

```javascript
let count = 0;

while (count < 5) {
    console.log(count);
    count++;
}
```

Useful when iteration count is not naturally tied to traversing a collection.

---

# 38. `for...of`

Iterates values from an iterable:

```javascript
const names = ["Alice", "Bob"];

for (const name of names) {
    console.log(name);
}
```

Excellent default for straightforward array iteration.

---

# 39. `for...in`

Iterates enumerable property keys.

```javascript
const user = {
    name: "Alice",
    age: 30
};

for (const key in user) {
    console.log(key);
}
```

Do not confuse `for...in` with `for...of`.

For arrays, `for...of` is usually the more natural choice.

---

# 40. `break` and `continue`

`break` exits a loop.

```javascript
break;
```

`continue` skips to the next iteration.

```javascript
continue;
```

---

# 41. Functions

Functions package reusable behavior.

```javascript
function greet(name) {
    return `Hello, ${name}`;
}
```

Call:

```javascript
greet("Alice");
```

Know:

```text
function declaration
function name
parameter
argument
return value
```

---

# 42. Parameters vs Arguments

Definition:

```javascript
function greet(name) {
}
```

`name` is a **parameter**.

Invocation:

```javascript
greet("Alice");
```

`"Alice"` is an **argument**.

---

# 43. Return Values

```javascript
function add(a, b) {
    return a + b;
}
```

Usage:

```javascript
const result = add(2, 3);
```

Without explicit `return`, the function returns:

```text
undefined
```

---

# 44. Function Expressions

Functions can be values:

```javascript
const greet = function (name) {
    return `Hello, ${name}`;
};
```

This is important because JavaScript functions are first-class values.

---

# 45. Arrow Functions

```javascript
const greet = (name) => {
    return `Hello, ${name}`;
};
```

Concise:

```javascript
const greet = name => `Hello, ${name}`;
```

Arrow functions differ from normal functions in important ways, especially around `this`.

Do not treat them as merely shorter syntax.

---

# 46. Functions Are First-Class Values

Functions can be:

- assigned to variables,
- passed as arguments,
- returned from functions,
- stored in objects,
- stored in arrays.

Example:

```javascript
function run(operation) {
    operation();
}

run(() => {
    console.log("Running");
});
```

This is fundamental to JavaScript.

---

# 47. Callbacks

A callback is a function passed to another function for later or delegated execution.

```javascript
function processUser(callback) {
    callback("Alice");
}

processUser(name => {
    console.log(name);
});
```

Callbacks appear heavily in:

- events,
- arrays,
- timers,
- asynchronous programming.

A callback is really just one common example of a more general pattern — a function that takes or returns another function — called a **higher-order function**, covered in full later.

---

# 48. Scope

Scope determines where a variable can be accessed.

Important categories:

```text
global scope
module scope
function scope
block scope
```

Example:

```javascript
if (true) {
    const message = "hello";
}

console.log(message);
```

fails because `message` is block-scoped.

---

# 49. Lexical Scope

JavaScript uses lexical scope.

A function's accessible variables are determined by where the function is defined in source structure.

```javascript
const outer = "hello";

function show() {
    console.log(outer);
}
```

`show()` can access `outer`.

This concept leads directly to closures.

---

# 50. Closures

A closure occurs when a function retains access to variables from its lexical environment even after the outer function has finished executing.

```javascript
function createCounter() {
    let count = 0;

    return function () {
        count++;
        return count;
    };
}

const counter = createCounter();

counter(); // 1
counter(); // 2
```

`count` remains accessible to the returned function.

Closures are fundamental to JavaScript and frequently asked about in interviews.

---

# 51. Hoisting

JavaScript processes declarations in ways that can make declarations appear available before their textual position.

Function declaration example:

```javascript
greet();

function greet() {
    console.log("Hello");
}
```

works.

But `let`, `const`, and `var` behave differently.

Learn hoisting as part of JavaScript's execution model, not as a trick to exploit.

Prefer declaring things before they are used when practical.

---

# 52. Temporal Dead Zone

`let` and `const` bindings exist conceptually before their declaration line but cannot be accessed until initialization.

```javascript
console.log(name);

const name = "Alice";
```

throws an error.

The region before initialization is called the **Temporal Dead Zone**.

---

# 53. Arrays

```javascript
const numbers = [10, 20, 30];
```

Access:

```javascript
numbers[0]
```

Length:

```javascript
numbers.length
```

Arrays are zero-indexed.

---

# 54. Common Array Mutation Methods

Know:

```javascript
push()
pop()
shift()
unshift()
splice()
```

Examples:

```javascript
numbers.push(40);
numbers.pop();
```

Understand which operations mutate the original array.

---

# 55. Common Non-Mutating/Transforming Array Methods

Know:

```javascript
map()
filter()
find()
some()
every()
reduce()
slice()
includes()
```

These are central to modern frontend JavaScript.

---

# 56. `forEach()`

```javascript
numbers.forEach(number => {
    console.log(number);
});
```

Use when performing an operation for each item and you do not need a transformed returned array.

---

# 57. `map()`

Transforms every element into a new array.

```javascript
const doubled = numbers.map(number => number * 2);
```

Mental model:

```text
input array
   ↓ transform each item
new array
```

---

# 58. `filter()`

Keeps items matching a condition.

```javascript
const adults = users.filter(user => user.age >= 18);
```

---

# 59. `find()`

Returns the first matching element:

```javascript
const user = users.find(user => user.id === 5);
```

If none is found:

```text
undefined
```

---

# 60. `some()` and `every()`

`some()`:

> Does at least one item satisfy this condition?

```javascript
numbers.some(number => number < 0);
```

`every()`:

> Do all items satisfy this condition?

```javascript
numbers.every(number => number > 0);
```

---

# 61. `reduce()`

Combines array values into an accumulated result.

```javascript
const total = numbers.reduce(
    (sum, number) => sum + number,
    0
);
```

Understand:

```text
accumulator
current value
initial value
```

Do not use `reduce()` merely because it looks advanced. Prefer simpler operations when they communicate intent better.

---

# 62. Objects

Objects store keyed properties.

```javascript
const user = {
    name: "Alice",
    age: 30
};
```

Access:

```javascript
user.name
```

or:

```javascript
user["name"]
```

---

# 63. Dot vs Bracket Notation

Dot:

```javascript
user.name
```

Bracket:

```javascript
user["name"]
```

Bracket notation is necessary when the property key is dynamic:

```javascript
const key = "name";

user[key];
```

---

# 64. Object Methods

Objects can contain functions:

```javascript
const user = {
    name: "Alice",

    greet() {
        return `Hello, ${this.name}`;
    }
};
```

A function stored as object behavior is commonly called a method.

---

# 65. Object Utility Methods

Know:

```javascript
Object.keys()
Object.values()
Object.entries()
```

Example:

```javascript
Object.keys(user);
```

These are useful for iterating object data.

---

# 66. Destructuring

Object:

```javascript
const user = {
    name: "Alice",
    age: 30
};

const { name, age } = user;
```

Array:

```javascript
const [first, second] = numbers;
```

Destructuring is extremely common in modern frontend code.

---

# 67. Spread Syntax

Arrays:

```javascript
const copy = [...numbers];
```

Objects:

```javascript
const updatedUser = {
    ...user,
    name: "Bob"
};
```

Spread is commonly used to create shallow copies and combine data. (A shallow copy is covered in detail in the Shallow Copies section below — briefly, it means only the top-level object or array is duplicated, while any nested objects inside it are still shared with the original.)

---

# 68. Rest Syntax

Collect remaining values:

```javascript
function sum(...numbers) {
}
```

Object:

```javascript
const { id, ...remaining } = user;
```

Spread and rest use the same `...` syntax but perform different roles depending on context.

---

# 69. Primitive vs Reference Behavior

Primitive values behave like independent values when assigned.

```javascript
let a = 5;
let b = a;

b = 10;
```

`a` remains `5`.

Objects behave through references:

```javascript
const a = { value: 5 };
const b = a;

b.value = 10;
```

Now:

```javascript
a.value
```

is also `10`.

This is critical to understanding frontend state.

---

# 70. Shallow Copies

Example:

```javascript
const original = {
    name: "Alice",
    address: {
        city: "Dallas"
    }
};

const copy = { ...original };
```

The top-level object is new.

But:

```javascript
copy.address === original.address
```

is true.

Nested objects remain shared references.

That is a **shallow copy**.

---

# 71. Deep Copy Concept

A deep copy recursively duplicates nested data rather than merely copying top-level references.

Modern JavaScript provides:

```javascript
structuredClone(value)
```

for many structured-data cases.

Understand that deep copying has limitations and should not be used automatically as a substitute for understanding data ownership/state design.

---

# 72. `Map`

A `Map` stores key/value pairs.

```javascript
const users = new Map();

users.set(1, "Alice");
users.set(2, "Bob");

users.get(1);
```

Useful operations:

```text
set()
get()
has()
delete()
size
```

Unlike plain objects, Maps support arbitrary values as keys and have collection-oriented APIs.

---

# 73. `Set`

A `Set` stores unique values.

```javascript
const ids = new Set();

ids.add(1);
ids.add(1);
```

The duplicate is not added again.

Useful:

```text
add()
has()
delete()
size
```

---

# 74. Choosing a Data Structure

General starting point:

```text
ordered collection          → Array
record/entity               → Object
key/value collection        → Map
unique-value collection     → Set
```

Do not choose structures only because you remember their syntax.

Choose based on the operations your program needs.

---

# 75. Mutation vs Immutability

Mutation changes an existing object:

```javascript
user.name = "Bob";
```

Immutable-style update creates a new object:

```javascript
const updated = {
    ...user,
    name: "Bob"
};
```

Frontend frameworks often encourage immutable updates because reference changes make state transitions easier to reason about.

Understand both approaches.

---

# 76. `this`

`this` is one of JavaScript's most misunderstood concepts.

Its value depends on **how a function is invoked**, with important differences for arrow functions.

Example:

```javascript
const user = {
    name: "Alice",

    greet() {
        console.log(this.name);
    }
};

user.greet();
```

Here `this` refers to `user`.

Do not memorize "`this` means the current object." That explanation is incomplete.

---

# 77. Arrow Functions and `this`

Arrow functions do not create their own normal `this` binding.

They capture `this` lexically from the surrounding context.

Therefore:

```javascript
const user = {
    name: "Alice",

    greet: () => {
        console.log(this.name);
    }
};
```

does not behave like the method shorthand version.

This distinction is important.

---

# 78. `call()`, `apply()`, and `bind()`

Recognize:

```javascript
function.call()
function.apply()
function.bind()
```

They can explicitly control or create function invocation context.

You do not need to use them daily, but an advanced JavaScript developer should understand why they exist.

---

# 79. Prototypes

JavaScript uses prototype-based inheritance.

Objects can delegate property lookup to another object through the prototype chain.

Conceptually:

```text
object
  ↓ prototype
another object
  ↓ prototype
another object
  ↓
null
```

When a property is not found directly, JavaScript can search the prototype chain.

---

# 80. Prototype Chain

Example conceptually:

```javascript
array
 ↓
Array.prototype
 ↓
Object.prototype
 ↓
null
```

This helps explain why arrays have methods such as:

```javascript
map()
filter()
push()
```

The methods do not need to be individually stored on every array instance.

---

# 81. Classes

Modern JavaScript provides class syntax:

```javascript
class User {
    constructor(name) {
        this.name = name;
    }

    greet() {
        return `Hello, ${this.name}`;
    }
}
```

Create instance:

```javascript
const user = new User("Alice");
```

---

# 82. JavaScript Classes Are Not What They Look Like

JavaScript `class` syntax is syntax sugar over JavaScript's own prototype model — it is not a separate object system underneath.

Conceptually:

```text
JavaScript
prototype-based object model
    ↑
class syntax provides a familiar, class-like abstraction on top
```

Understand the underlying prototype model even if you normally write `class`.

(If you already know class-based OOP from another language, such as Java: JavaScript's `class` looks similar on the surface, but there is no true class-based object system running underneath it — everything still resolves through prototypes. You'll see Java's actual class model covered in the dedicated Java skill tree; the comparison here is only useful if you already have that background.)

---

# 83. Constructors

```javascript
constructor(name) {
    this.name = name;
}
```

Called during:

```javascript
new User("Alice");
```

The constructor initializes the instance.

---

# 84. Inheritance

```javascript
class Admin extends User {
    constructor(name, permissions) {
        super(name);
        this.permissions = permissions;
    }
}
```

Know:

```text
extends
super
```

Use inheritance deliberately. Composition is often preferable when relationships do not naturally form an "is-a" hierarchy.

---

# 85. Encapsulation

Modern JavaScript supports private class fields:

```javascript
class Counter {
    #count = 0;

    increment() {
        this.#count++;
    }
}
```

Encapsulation can also be achieved through modules and closures.

Understand the principle rather than tying encapsulation only to class syntax.

---

# 86. Composition

Instead of inheritance, behavior can be assembled from functions/objects.

Conceptually:

```text
small behaviors
      ↓
combined object/function
```

JavaScript's flexible functions and objects make composition common.

---

# 87. Functional Programming Concepts

JavaScript supports functional programming styles.

Important concepts:

- functions as values,
- higher-order functions,
- callbacks,
- pure functions,
- avoiding unnecessary mutation,
- transforming collections.

You do not need to become a functional-programming theorist to use these effectively.

---

# 88. Higher-Order Functions

A higher-order function takes a function as an argument or returns one.

Example:

```javascript
const doubled = numbers.map(number => number * 2);
```

`map()` receives a function.

This pattern appears throughout JavaScript.

---

# 89. Pure Functions

A pure function:

- produces output based on its inputs,
- does not modify external state,
- avoids externally visible side effects.

Example:

```javascript
function add(a, b) {
    return a + b;
}
```

Pure functions are easier to test and reason about.

Not every function should or can be pure.

---

# 90. Errors

JavaScript errors interrupt normal execution unless handled.

Example:

```javascript
throw new Error("Invalid input");
```

Know that errors are objects containing information such as:

```text
message
name
stack
```

---

# 91. `try` / `catch`

```javascript
try {
    riskyOperation();
} catch (error) {
    console.error(error);
}
```

Use when you can meaningfully handle or translate an error.

Do not catch errors merely to hide them.

---

# 92. `finally`

```javascript
try {
    // operation
} catch (error) {
    // handle
} finally {
    // cleanup
}
```

`finally` runs whether the operation succeeds or throws, subject to normal control-flow caveats.

Useful for cleanup.

---

# 93. Throwing Errors

```javascript
function divide(a, b) {
    if (b === 0) {
        throw new Error("Cannot divide by zero");
    }

    return a / b;
}
```

Errors let functions communicate exceptional failure conditions.

---

# 94. Synchronous Execution

Normal JavaScript statements execute synchronously in sequence:

```javascript
console.log("A");
console.log("B");
console.log("C");
```

Output:

```text
A
B
C
```

Each operation completes before the next synchronous statement proceeds.

---

# 95. Why Asynchronous Programming Exists

Some operations take time:

- network requests,
- timers,
- user interaction,
- file operations in Node,
- database operations on servers.

Blocking the browser's main execution thread while waiting would freeze interaction.

Asynchronous APIs let work be initiated and completion handled later.

---

# 96. The Call Stack

JavaScript tracks active function calls using a call stack.

Example:

```javascript
function a() {
    b();
}

function b() {
    console.log("hello");
}

a();
```

Conceptually:

```text
a()
 ↓
b()
 ↓
console.log()
```

As calls return, stack frames are removed.

---

# 97. Single-Threaded JavaScript Mental Model

Typical browser JavaScript execution on the main thread processes one JavaScript call stack at a time.

But the **browser environment** can perform work outside that stack.

Conceptually:

```text
JavaScript call stack
        │
        ├── browser timer APIs
        ├── network APIs
        ├── DOM events
        └── other browser systems
```

This is how JavaScript can coordinate asynchronous activity without the main JavaScript stack itself simultaneously executing multiple callbacks.

---

# 98. Browser Web APIs

The browser provides functionality such as:

```text
setTimeout
fetch
DOM events
geolocation
storage
```

These are not all intrinsic JavaScript-language features.

The browser environment supplies them.

---

# 99. Event Loop

The event loop coordinates when queued asynchronous work can execute.

Simplified:

```text
Call Stack
    ↓ finishes current work

Event Loop
    ↓
eligible queued task/callback
    ↓
Call Stack
```

The exact model includes multiple task sources and microtasks, but this simplified model is the starting point.

---

# 100. Tasks and Microtasks

A more accurate model distinguishes:

```text
tasks
microtasks
```

Promise reactions are processed through the microtask queue.

Timers generally schedule tasks.

Simplified ordering:

```text
current synchronous code
        ↓
microtasks
        ↓
next task
```

This explains many interview questions about output order.

---

# 101. `setTimeout()`

```javascript
setTimeout(() => {
    console.log("later");
}, 1000);
```

The delay means:

> Do not run this callback before approximately this delay has elapsed and it becomes eligible.

It does **not** guarantee exact execution at 1000 milliseconds.

The callback must still wait until JavaScript can execute it.

---

# 102. Async Ordering Example

```javascript
console.log("A");

setTimeout(() => {
    console.log("B");
}, 0);

console.log("C");
```

Output:

```text
A
C
B
```

Why?

The timer callback is scheduled for later execution. Current synchronous code finishes first.

---

# 103. Promises

A Promise represents the eventual completion or failure of an asynchronous operation.

Conceptual states:

```text
pending
fulfilled
rejected
```

Example:

```javascript
fetch("/api/users")
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error(error));
```

---

# 104. Creating a Promise

Recognize:

```javascript
const promise = new Promise((resolve, reject) => {
    // asynchronous operation

    if (success) {
        resolve(value);
    } else {
        reject(error);
    }
});
```

You will consume Promises more often than manually construct them in ordinary frontend application code.

For an already-known value or error, `Promise.resolve(value)` and `Promise.reject(error)` are convenience static methods that skip the executor function and immediately return an already-settled Promise:

```javascript
const alreadyDone = Promise.resolve(42);

alreadyDone.then(value => console.log(value)); // 42
```

---

# 105. `.then()`

```javascript
promise.then(value => {
    console.log(value);
});
```

Registers logic to run when the Promise fulfills.

`.then()` itself returns another Promise, enabling chaining.

---

# 106. `.catch()`

```javascript
promise.catch(error => {
    console.error(error);
});
```

Handles rejection/error propagation in a Promise chain.

---

# 107. `.finally()`

```javascript
promise.finally(() => {
    console.log("finished");
});
```

Useful for cleanup behavior that should occur after settlement.

---

# 108. `async`

Declaring:

```javascript
async function loadUsers() {
}
```

means the function always returns a Promise.

Even:

```javascript
async function getNumber() {
    return 5;
}
```

returns a Promise fulfilled with `5`.

---

# 109. `await`

Inside an async function:

```javascript
const response = await fetch("/api/users");
```

`await` pauses the execution of that async function until the awaited Promise settles, without blocking the entire browser environment.

This distinction is essential.

---

# 110. Async/Await Example

```javascript
async function loadUsers() {
    const response = await fetch("/api/users");
    const users = await response.json();

    console.log(users);
}
```

This expresses asynchronous logic in a sequential-looking style.

---

# 111. Async Error Handling

```javascript
async function loadUsers() {
    try {
        const response = await fetch("/api/users");
        const users = await response.json();

        return users;
    } catch (error) {
        console.error(error);
    }
}
```

Understand both Promise-chain and async/await styles.

---

# 112. Sequential vs Concurrent Async Work

Sequential:

```javascript
const users = await fetchUsers();
const messages = await fetchMessages();
```

The second starts after the first completes.

Concurrent start:

```javascript
const usersPromise = fetchUsers();
const messagesPromise = fetchMessages();

const users = await usersPromise;
const messages = await messagesPromise;
```

Or commonly:

```javascript
const [users, messages] = await Promise.all([
    fetchUsers(),
    fetchMessages()
]);
```

Recognizing unnecessary sequential waits is an important frontend performance skill.

---

# 113. `Promise.all()`

```javascript
const results = await Promise.all([
    requestA(),
    requestB(),
    requestC()
]);
```

Useful when independent asynchronous operations can proceed concurrently and all results are required.

Understand failure behavior: one rejection causes the returned `Promise.all()` Promise to reject.

---

# 114. Other Promise Combinators

Recognize:

```text
Promise.all()
Promise.allSettled()
Promise.race()
Promise.any()
```

You do not need all details memorized initially.

Know that different coordination semantics exist.

---

# 115. Async Interview Mental Model

Be able to explain:

```text
JavaScript executes current synchronous work
            ↓
browser/runtime handles asynchronous operations
            ↓
completion schedules follow-up work
            ↓
event loop allows eligible callbacks/microtasks to run
            ↓
JavaScript continues processing
```

Avoid the inaccurate statement:

> "JavaScript just runs everything at the same time."

---

# 116. DOM

DOM stands for:

**Document Object Model**

The browser parses HTML into an object tree JavaScript can interact with.

HTML:

```html
<main>
    <h1>Hello</h1>
</main>
```

Conceptually:

```text
document
└── html
    └── body
        └── main
            └── h1
```

---

# 117. `document`

In browser JavaScript:

```javascript
document
```

represents the current HTML document through the DOM API.

It is supplied by the browser, not by the JavaScript language specification itself.

---

# 118. Selecting Elements

Common:

```javascript
document.querySelector("h1");
document.querySelector(".card");
document.querySelector("#submit");
```

Multiple:

```javascript
document.querySelectorAll(".card");
```

Also recognize:

```javascript
document.getElementById("submit");
```

---

# 119. Changing Text

```javascript
const heading = document.querySelector("h1");

heading.textContent = "New heading";
```

Prefer `textContent` for ordinary text insertion.

---

# 120. `innerHTML`

```javascript
element.innerHTML = "<strong>Hello</strong>";
```

This parses a string as HTML.

It can be useful, but inserting untrusted data with `innerHTML` can create XSS vulnerabilities.

Do not use it when simple text insertion is sufficient.

---

# 121. Creating Elements

```javascript
const paragraph = document.createElement("p");

paragraph.textContent = "Hello";

document.body.append(paragraph);
```

This is a safer and more structured way to build DOM content than concatenating arbitrary HTML strings.

---

# 122. Removing Elements

```javascript
element.remove();
```

You can also manipulate parent/child relationships through DOM APIs.

---

# 123. Attributes

Read:

```javascript
element.getAttribute("href");
```

Set:

```javascript
element.setAttribute("aria-expanded", "true");
```

Many properties can also be accessed through DOM object properties.

Understand the distinction between HTML attributes and DOM properties as you advance.

---

# 124. `classList`

```javascript
element.classList.add("active");
element.classList.remove("active");
element.classList.toggle("active");
element.classList.contains("active");
```

This is a strong HTML/CSS/JS separation pattern:

```text
JavaScript changes state class
        ↓
CSS controls appearance
```

---

# 125. Events

Common browser events:

```text
click
submit
input
change
keydown
keyup
focus
blur
DOMContentLoaded
```

Events communicate that something happened.

---

# 126. Event Listeners

```javascript
button.addEventListener("click", () => {
    console.log("Clicked");
});
```

Know:

```text
event target
event type
listener
handler/callback
event object
```

---

# 127. Event Object

```javascript
button.addEventListener("click", event => {
    console.log(event);
});
```

Useful properties/methods depend on event type.

Common:

```javascript
event.target
event.currentTarget
event.preventDefault()
event.stopPropagation()
```

---

# 128. Event Bubbling

Many events propagate upward through ancestors.

Conceptually:

```text
button
  ↓ event bubbles
div
  ↓
section
  ↓
body
```

This enables event delegation.

---

# 129. Event Delegation

Instead of adding listeners to many children, listen on a common ancestor.

```javascript
list.addEventListener("click", event => {
    if (event.target.matches("button")) {
        console.log("Button clicked");
    }
});
```

Useful for:

- dynamic lists,
- performance,
- simpler event management.

---

# 130. `preventDefault()`

Forms and links have default browser behavior.

Example:

```javascript
form.addEventListener("submit", event => {
    event.preventDefault();
});
```

This prevents the normal submission/navigation behavior.

Use it only when your JavaScript intentionally replaces the default behavior.

---

# 131. Forms

HTML:

```html
<form id="message-form">
    <label for="name">Name</label>
    <input id="name" name="name" required>

    <button type="submit">Send</button>
</form>
```

JavaScript:

```javascript
const form = document.querySelector("#message-form");
```

Forms are a major bridge between browser UI and backend systems.

---

# 132. `FormData`

```javascript
const formData = new FormData(form);

const name = formData.get("name");
```

`name` attributes determine the form-data keys.

---

# 133. Browser Validation

HTML can provide:

```text
required
minlength
maxlength
pattern
type="email"
min
max
```

JavaScript can inspect validity through APIs such as:

```javascript
form.checkValidity();
```

Use native HTML validation where appropriate before rebuilding basic validation manually.

---

# 134. Client-Side Validation Is Not Security

A user can:

- modify JavaScript,
- disable JavaScript,
- modify requests,
- call the backend directly.

Therefore:

```text
frontend validation
      ↓
better user experience

backend validation
      ↓
actual trust boundary
```

The backend must validate incoming data independently.

---

# 135. `fetch()`

`fetch()` performs HTTP requests.

GET:

```javascript
const response = await fetch("/api/messages");
```

Read JSON:

```javascript
const data = await response.json();
```

---

# 136. POST with `fetch()`

```javascript
const response = await fetch("/api/messages", {
    method: "POST",
    headers: {
        "Content-Type": "application/json"
    },
    body: JSON.stringify({
        name: "Alice",
        message: "Hello"
    })
});
```

Understand:

```text
URL
method
headers
body
```

---

# 137. JSON

JSON stands for:

**JavaScript Object Notation**

Example:

```json
{
    "name": "Alice",
    "age": 30
}
```

Despite the name, JSON is a data interchange format used by many languages.

Convert JavaScript → JSON:

```javascript
JSON.stringify(value)
```

JSON → JavaScript:

```javascript
JSON.parse(text)
```

---

# 138. Fetch Does Not Reject for Every HTTP Error

Important:

A `fetch()` Promise generally resolves when an HTTP response is received even if the response status is:

```text
404
500
```

Therefore inspect:

```javascript
response.ok
```

Example:

```javascript
const response = await fetch("/api/messages");

if (!response.ok) {
    throw new Error(`HTTP ${response.status}`);
}
```

This is a common interview/debugging issue.

---

# 139. Full Fetch Pattern

```javascript
async function loadMessages() {
    try {
        const response = await fetch("/api/messages");

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        const messages = await response.json();

        return messages;
    } catch (error) {
        console.error("Failed to load messages:", error);
        throw error;
    }
}
```

Understand every part rather than memorizing it as boilerplate.

---

# 140. HTTP Knowledge for JavaScript

Know:

```text
GET
POST
PUT
PATCH
DELETE
```

Know common statuses:

```text
200
201
204
400
401
403
404
409
500
```

Understand:

```text
request
response
headers
body
JSON
status
```

---

# 141. Same-Origin Policy

Browsers restrict interactions between different origins for security.

An origin is based primarily on:

```text
scheme
host
port
```

Therefore:

```text
http://localhost:4200
```

and:

```text
http://localhost:8081
```

are different origins.

---

# 142. CORS

CORS stands for:

**Cross-Origin Resource Sharing**

A backend can send HTTP headers indicating which cross-origin browser requests are permitted.

JavaScript may issue a request, but the browser enforces CORS rules.

This is why:

> "The server is running and curl works"

does not necessarily mean:

> "The browser will allow my frontend to access the response."

---

# 143. Browser Storage

Important browser storage mechanisms include:

```text
localStorage
sessionStorage
cookies
IndexedDB
```

At this stage, become comfortable with `localStorage` and `sessionStorage` and understand the existence of the others.

---

# 144. `localStorage`

```javascript
localStorage.setItem("theme", "dark");

const theme = localStorage.getItem("theme");

localStorage.removeItem("theme");
```

Values are stored as strings.

Objects require serialization:

```javascript
localStorage.setItem(
    "user",
    JSON.stringify(user)
);
```

---

# 145. `sessionStorage`

API resembles `localStorage`:

```javascript
sessionStorage.setItem("step", "2");
```

Its lifetime is associated with the page/tab session rather than persistent browser storage.

---

# 146. Cookies

Cookies are small pieces of data associated with web origins and HTTP requests.

They are important for:

- sessions,
- authentication,
- preferences.

Security attributes include concepts such as:

```text
HttpOnly
Secure
SameSite
```

Do not implement authentication merely by putting secrets in browser storage.

Authentication deserves its own deeper security/backend study.

---

# 147. Frontend Secrets

Anything sent to browser JavaScript should be considered visible to the user.

Users can inspect:

- source code,
- bundles,
- network requests,
- storage,
- runtime values.

Never rely on frontend JavaScript to hide:

- database passwords,
- private API keys,
- server credentials,
- secret passcodes.

---

# 148. XSS

XSS means:

**Cross-Site Scripting**

It occurs when untrusted content is interpreted as executable browser content/script in an unsafe context.

Dangerous pattern:

```javascript
element.innerHTML = userInput;
```

Prefer safe text APIs when HTML parsing is unnecessary:

```javascript
element.textContent = userInput;
```

XSS prevention becomes increasingly important in real applications.

---

# 149. Modules

Modules split code into separate files with explicit dependencies.

Example:

```javascript
export function add(a, b) {
    return a + b;
}
```

Import:

```javascript
import { add } from "./math.js";
```

Modules improve:

- organization,
- encapsulation,
- reuse,
- dependency clarity.

---

# 150. Named Exports

```javascript
export function add(a, b) {
    return a + b;
}

export function subtract(a, b) {
    return a - b;
}
```

Import:

```javascript
import { add, subtract } from "./math.js";
```

---

# 151. Default Exports

```javascript
export default function greet(name) {
    return `Hello, ${name}`;
}
```

Import:

```javascript
import greet from "./greet.js";
```

Understand named versus default exports.

Different projects establish different conventions.

---

# 152. Browser Module Scripts

HTML:

```html
<script type="module" src="main.js"></script>
```

Modules have their own scope and support `import`/`export`.

Browser module loading also interacts with HTTP origins and paths, which is one reason a local HTTP server is preferable to `file://` for realistic module practice.

---

# 153. Organizing a Small Frontend

Example:

```text
project/
├── index.html
├── css/
│   └── styles.css
└── js/
    ├── main.js
    ├── api.js
    ├── form.js
    └── ui.js
```

Possible responsibilities:

```text
main.js   application startup/wiring
api.js    HTTP communication
form.js   form behavior
ui.js     DOM rendering
```

This introduces separation of concerns before frameworks.

---

# 154. Node.js

Node.js executes JavaScript outside the browser.

Example:

```bash
node script.js
```

Node does not automatically provide browser globals such as:

```text
document
window
DOM
```

because there is no browser document.

Node provides its own runtime APIs.

---

# 155. Why Frontend Developers Need Node.js

Even when application JavaScript ultimately runs in the browser, modern frontend development commonly uses Node-based tooling for:

- package management,
- development servers,
- testing,
- bundling,
- transpilation,
- framework CLIs,
- build scripts.

Therefore a frontend developer should understand Node's role even if they are not writing a Node backend.

---

# 156. npm

npm is commonly used as:

- the Node package manager,
- a package registry ecosystem,
- a script runner through `package.json`.

Common commands:

```bash
npm init
npm install
npm install package-name
npm uninstall package-name
npm run script-name
```

Detailed npm/package-management mastery can be separated into tooling study.

---

# 157. `package.json`

Example:

```json
{
    "name": "my-project",
    "version": "1.0.0",
    "scripts": {
        "start": "node main.js"
    },
    "dependencies": {}
}
```

Understand:

```text
project metadata
scripts
dependencies
devDependencies
```

---

# 158. Dependencies vs Dev Dependencies

Application/runtime dependency:

```bash
npm install package-name
```

Development-only dependency:

```bash
npm install --save-dev package-name
```

Conceptually:

```text
dependencies
needed by application/runtime behavior

devDependencies
needed primarily for development/build/test tooling
```

Exact deployment behavior depends on the project/toolchain.

---

# 159. `node_modules`

Installed npm packages commonly live under:

```text
node_modules/
```

Do not manually edit package source there as your normal workflow.

It is normally generated from package metadata/lock information.

---

# 160. Lock Files

npm commonly generates:

```text
package-lock.json
```

It records resolved dependency information to improve reproducibility.

Understand why:

```text
package.json
```

and:

```text
package-lock.json
```

serve different purposes.

---

# 161. Debugging with `console`

Know:

```javascript
console.log()
console.error()
console.warn()
console.table()
```

Example:

```javascript
console.log("user:", user);
```

Console logging is useful, but do not make it your only debugging technique.

---

# 162. Browser Breakpoints

DevTools can pause JavaScript execution.

Use breakpoints to inspect:

- variables,
- scope,
- call stack,
- objects,
- execution order.

This is especially useful for asynchronous code.

---

# 163. `debugger`

JavaScript supports:

```javascript
debugger;
```

When DevTools is available, execution can pause there.

Useful during development; remove unintended debugger statements from finished code.

---

# 164. Call Stack in DevTools

When execution pauses, inspect the call stack.

Conceptually:

```text
current function
    ↑ called by
previous function
    ↑
previous function
```

This helps answer:

> How did execution get here?

---

# 165. Stack Traces

Errors often include a stack trace showing function calls and source locations.

Read the earliest relevant application frame rather than only the final generic error message.

---

# 166. Network Debugging

For `fetch()` problems inspect:

- request URL,
- HTTP method,
- request headers,
- request body,
- response status,
- response headers,
- response body,
- timing.

The browser Network panel is essential for frontend/full-stack debugging.

---

# 167. Common Problem — `undefined`

If you receive:

```text
undefined
```

ask:

- Was the variable initialized?
- Does this property exist?
- Did the function return anything?
- Did `find()` fail to find an item?
- Is the API response shaped differently than expected?
- Is the DOM selector returning the element you expect?

Trace the data instead of blindly adding null checks.

---

# 168. Common Problem — `null` DOM Selection

```javascript
const button = document.querySelector("#button");
```

may return:

```text
null
```

if no matching element exists.

Possible causes:

- selector typo,
- wrong ID/class,
- script executes before element exists,
- wrong page,
- dynamically generated content.

---

# 169. Common Problem — Script Runs Too Early

A script in `<head>` without appropriate loading behavior may execute before body elements are parsed.

Useful pattern:

```html
<script src="script.js" defer></script>
```

or modules:

```html
<script type="module" src="main.js"></script>
```

Understand document parsing rather than automatically wrapping everything in timers.

---

# 170. Common Problem — Mutation Through Shared Reference

```javascript
const original = {
    settings: {
        theme: "light"
    }
};

const copy = { ...original };

copy.settings.theme = "dark";
```

This also changes the nested object seen through `original`.

Reason:

```text
copy.settings
and
original.settings
```

reference the same nested object.

---

# 171. Common Problem — Async Result Used Too Early

Incorrect mental model:

```javascript
const data = fetch("/api/users");

console.log(data);
```

`data` is not the final response body.

`fetch()` returns a Promise.

You must coordinate with asynchronous completion.

---

# 172. Common Problem — Missing `await`

```javascript
const data = response.json();
```

`response.json()` returns a Promise.

With async/await:

```javascript
const data = await response.json();
```

Understanding return types/concepts prevents this mistake.

---

# 173. Common Problem — HTTP Error Not Caught

This alone:

```javascript
try {
    const response = await fetch("/missing");
} catch (error) {
}
```

does not treat every `404` as a rejected fetch Promise.

Check:

```javascript
response.ok
```

or status explicitly.

---

# 174. Common Problem — Event Listener Does Nothing

Check:

1. Did the selector find an element?
2. Is the script loaded?
3. Did the script throw earlier?
4. Is the event type correct?
5. Is another behavior navigating/reloading the page?
6. Is the element dynamically created?
7. Is the handler actually attached?

Use DevTools rather than guessing.

---

# 175. Common Problem — Form Reloads Page

Normal HTML form submission navigates/reloads according to form behavior.

If JavaScript should handle submission:

```javascript
event.preventDefault();
```

But first decide whether replacing native submission is actually necessary.

---

# 176. Common Problem — CORS

Ask:

```text
Frontend origin?
Backend origin?
Protocol?
Hostname?
Port?
Request method?
Request headers?
Backend CORS configuration?
```

Do not disable browser security as a "solution."

Fix the application/server configuration.

---

# 177. Common Problem — `this`

When `this` is unexpected, ask:

> How was this function called?

Then determine:

- normal function?
- method call?
- constructor?
- explicitly bound?
- arrow function?

Reason from invocation rather than where the function appears visually.

---

# 178. Testing Fundamentals

Testing verifies behavior automatically.

Important conceptual levels:

```text
unit tests
integration tests
end-to-end tests
```

A dedicated testing/tooling map can cover frameworks.

For JavaScript mastery, understand:

- arrange inputs/state,
- execute behavior,
- assert expected result,
- test edge cases,
- keep logic testable.

---

# 179. Pure Functions and Testability

This:

```javascript
function calculateTotal(items) {
    return items.reduce(
        (sum, item) => sum + item.price,
        0
    );
}
```

is easier to test than logic tightly coupled to DOM state.

Separating:

```text
business/data logic
from
DOM rendering
```

improves maintainability and testability.

---

# 180. JavaScript Code Quality

Develop habits such as:

- meaningful variable names,
- small focused functions,
- consistent formatting,
- avoid unnecessary global state,
- prefer `const`,
- avoid deeply nested control flow,
- separate DOM/API/data logic,
- handle errors intentionally,
- remove dead code,
- comment reasons rather than obvious syntax.

---

# 181. Strict Mode

Classic scripts can enable:

```javascript
"use strict";
```

Strict mode prevents or changes certain error-prone legacy behaviors.

ES modules are strict mode automatically.

Recognize strict mode and understand why modern JavaScript favors stricter semantics.

---

# 182. Semicolons

JavaScript has Automatic Semicolon Insertion.

Code can often omit explicit semicolons.

However, ASI has edge cases.

Choose and follow a consistent project style, typically enforced by formatting/linting tools.

Understand the language behavior rather than arguing that one style is universally mandatory.

---

# 183. Formatting and Linting

Modern JavaScript projects often use tools for:

```text
formatting
linting
static analysis
```

Examples in the ecosystem include formatters and linters.

The important concept:

```text
formatter → consistent appearance
linter    → detect suspicious/style/problem patterns
```

Tool-specific mastery belongs in project/tooling study.

---

# 184. Comments

Single line:

```javascript
// comment
```

Block:

```javascript
/*
    comment
*/
```

Prefer code that explains itself.

Use comments to explain:

- why,
- constraints,
- unusual behavior,
- non-obvious decisions.

---

# 185. Documentation

JavaScript APIs are often documented with examples and parameter/return information.

When reading documentation, identify:

```text
name
purpose
receiver/object
parameters
parameter types/meaning
return value
side effects
exceptions/errors
examples
mutation behavior
async behavior
```

For example, when reading:

```javascript
array.map(callback)
```

ask:

1. What object owns `map`?
2. What argument does it receive?
3. What arguments does the callback receive?
4. Does it mutate the array?
5. What does it return?

This turns documentation into an operational specification.

---

# 186. What to Memorize — Core Language

Memorize:

```text
const
let
function
return
if
else
switch
for
while
for...of
break
continue
try
catch
finally
throw
class
extends
new
import
export
async
await
```

Know what each is for.

---

# 187. What to Memorize — Operators

Know:

```text
+
-
*
/
%
**
=
+=
-=
===
!==
>
<
>=
<=
&&
||
!
??
?.
...
? :
```

Understand rather than merely recognize them.

---

# 188. What to Memorize — Types/Data Structures

Know:

```text
string
number
boolean
undefined
null
bigint
symbol
object

Array
Object
Map
Set
```

Know when each common collection structure is appropriate.

---

# 189. What to Memorize — Array Methods

Prioritize:

```text
push
pop
slice
splice
includes
forEach
map
filter
find
some
every
reduce
```

You should know:

- purpose,
- inputs/callback,
- return value,
- whether it mutates.

---

# 190. What to Memorize — Object/Data Syntax

Know:

```javascript
object.property
object[key]
```

```javascript
const { name } = user;
```

```javascript
const copy = { ...user };
```

```javascript
const copy = [...items];
```

```javascript
JSON.stringify()
JSON.parse()
```

---

# 191. What to Memorize — Browser APIs

Know the basic purpose and usage of:

```text
document.querySelector
document.querySelectorAll
document.createElement
textContent
classList
addEventListener
preventDefault
FormData
fetch
localStorage
```

---

# 192. What to Memorize — Async

Know:

```text
callback
Promise
.then()
.catch()
async
await
Promise.all()
event loop
call stack
task
microtask
```

Async concepts should be explainable, not merely syntactically familiar.

---

# 193. What to Look Up

It is normal to look up:

- uncommon String methods,
- obscure Array methods,
- RegExp syntax,
- Date API details,
- Intl APIs,
- advanced Promise combinators,
- unusual DOM APIs,
- browser compatibility,
- exact event properties,
- advanced prototype descriptors,
- npm package APIs.

Expertise is not memorizing the standard library.

Expertise is knowing what category of tool solves the problem and how to verify exact usage.

---

# 194. Practical Exercise — Language Basics

Write a program that:

1. stores a list of users,
2. loops through them,
3. filters adults,
4. calculates an average age,
5. finds a user by ID,
6. prints formatted results.

Use:

```text
variables
objects
arrays
functions
conditionals
map/filter/find/reduce
```

---

# 195. Practical Exercise — Data Transformation

Given:

```javascript
const products = [
    { name: "A", price: 10, active: true },
    { name: "B", price: 20, active: false },
    { name: "C", price: 30, active: true }
];
```

Produce:

- active products,
- product names,
- total active-product price,
- whether any product exceeds a threshold.

Do it first with loops, then with array methods.

Explain the tradeoffs.

---

# 196. Practical Exercise — References

Create:

```javascript
const original = {
    name: "Alice",
    preferences: {
        theme: "light"
    }
};
```

Experiment with:

```text
direct assignment
spread copy
structuredClone
```

Predict which mutations affect the original before executing the code.

---

# 197. Practical Exercise — Closures

Build:

```javascript
createCounter()
```

that returns functions capable of:

```text
increment
decrement
read current value
```

Keep the counter state private through closure.

Explain why the state survives after the outer function returns.

---

# 198. Practical Exercise — Classes and Prototypes

Create:

```text
Person
Employee
```

with:

- constructor data,
- methods,
- inheritance.

Then inspect:

```javascript
Object.getPrototypeOf()
```

and explain where methods are found.

The goal is to connect `class` syntax to prototypes.

---

# 199. Practical Exercise — DOM

Build a plain HTML page with:

- input,
- Add button,
- list.

JavaScript should:

1. read the input,
2. create an `<li>`,
3. insert text safely,
4. append it,
5. clear the input,
6. allow items to be removed.

Do not use a framework.

---

# 200. Practical Exercise — Events

Create:

- button click handler,
- text input handler,
- form submit handler,
- keyboard handler.

Log the event object and inspect:

```text
target
currentTarget
type
```

Then implement event delegation for a dynamic list.

---

# 201. Practical Exercise — Async Ordering

Before running the program, predict the output:

```javascript
console.log("A");

Promise.resolve().then(() => {
    console.log("B");
});

setTimeout(() => {
    console.log("C");
}, 0);

console.log("D");
```

Then execute it and explain the result using:

```text
call stack
microtask queue
task queue
event loop
```

---

# 202. Practical Exercise — API

Use a safe practice/public API or your own local backend.

Implement:

```text
GET collection
render results
loading state
error state
POST data
handle non-2xx response
```

Inspect every request in the Network panel.

---

# 203. Practical Exercise — Modular Frontend

Create:

```text
index.html
styles.css
main.js
api.js
ui.js
form.js
```

Responsibilities:

```text
api.js   HTTP
ui.js    DOM rendering
form.js  form handling
main.js  wiring/startup
```

Use ES modules.

---

# 204. Practical Exercise — Browser Storage

Create a theme preference.

When the user selects:

```text
light
dark
```

save it to:

```javascript
localStorage
```

Restore it when the page reloads.

Keep appearance changes in CSS classes.

---

# 205. Practical Exercise — Debugging

Intentionally create:

- bad selector,
- undefined property,
- rejected Promise,
- 404 fetch,
- JSON parsing failure,
- event listener on `null`,
- shared-reference mutation.

Use:

```text
Console
breakpoints
Network
call stack
scope inspector
```

to diagnose each.

---

# 206. Level 1 — Basic Syntax

You can use:

```text
const
let
primitive values
operators
if/else
loops
functions
arrays
objects
```

You can write small console programs without copying syntax.

---

# 207. Level 2 — Data Manipulation

You understand:

```text
array methods
objects
Map
Set
destructuring
spread/rest
references
shallow copies
mutation
```

You can transform application data predictably.

---

# 208. Level 3 — Functions and Execution

You understand:

```text
scope
lexical scope
closures
callbacks
higher-order functions
hoisting
arrow functions
this
```

You can explain why functions behave as they do.

---

# 209. Level 4 — Object Model

You understand:

```text
objects
methods
prototype chain
classes
constructors
inheritance
encapsulation
composition
```

You understand that JavaScript classes are built on a prototype-based object model.

---

# 210. Level 5 — Browser Programming

You can use:

```text
DOM
selectors
events
forms
FormData
classList
element creation
validation
storage
```

You can build a small interactive frontend without a framework.

---

# 211. Level 6 — Async JavaScript

You can explain:

```text
call stack
Web APIs/runtime
event loop
tasks
microtasks
callbacks
Promises
async/await
concurrency
```

You can implement and debug asynchronous frontend behavior.

---

# 212. Level 7 — API Integration

You understand:

```text
HTTP
fetch
JSON
methods
statuses
headers
request body
response body
CORS
same-origin policy
```

You can connect a frontend to a backend API.

---

# 213. Level 8 — Project Structure and Tooling

You understand:

```text
ES modules
Node.js
npm
package.json
dependencies
devDependencies
node_modules
lock files
```

You are prepared to enter a modern frontend framework/toolchain.

---

# 214. Level 9 — JavaScript Mastery Foundation

You can:

- write medium-sized functions from memory,
- reason about types/coercion,
- choose data structures,
- reason about references,
- explain closures,
- explain `this`,
- explain prototypes,
- organize modules,
- manipulate the DOM,
- handle forms/events,
- coordinate async work,
- call APIs,
- debug browser applications,
- recognize security boundaries.

You are ready to study TypeScript and frameworks without relying on them to conceal JavaScript fundamentals.

---

# 215. Interview Questions — Fundamentals

Be prepared to answer:

1. What is JavaScript?
2. Where can JavaScript run?
3. What is the difference between JavaScript and Node.js?
4. What is the difference between JavaScript and React?
5. What are JavaScript's primitive types?
6. What is dynamic typing?
7. What is `undefined`?
8. What is `null`?
9. What is the difference between `null` and `undefined`?
10. What is `NaN`?
11. What does `typeof` do?
12. What is type coercion?
13. What are truthy and falsy values?
14. What is the difference between `==` and `===`?
15. What is the difference between `let`, `const`, and `var`?
16. Does `const` make an object immutable?
17. What is scope?
18. What is lexical scope?
19. What is hoisting?
20. What is the Temporal Dead Zone?

---

# 216. Interview Questions — Functions

Be prepared to answer:

1. What is a function?
2. What is a parameter?
3. What is an argument?
4. What does `return` do?
5. What does a function return without `return`?
6. What is a function expression?
7. What is an arrow function?
8. How are arrow functions different from normal functions?
9. What does it mean that functions are first-class values?
10. What is a callback?
11. What is a higher-order function?
12. What is a closure?
13. Why are closures useful?
14. What is a pure function?
15. What is a side effect?

---

# 217. Interview Questions — Data

Be prepared to answer:

1. What is an Array?
2. What is an Object?
3. What is a Map?
4. What is a Set?
5. When would you choose each?
6. What does `map()` do?
7. What does `filter()` do?
8. What does `find()` return?
9. What does `reduce()` do?
10. What is destructuring?
11. What is spread syntax?
12. What is rest syntax?
13. What is mutation?
14. What is a reference?
15. What is a shallow copy?
16. Why can `{ ...object }` still share nested state?
17. What is `structuredClone()`?

---

# 218. Interview Questions — Objects/OOP

Be prepared to answer:

1. What is `this`?
2. How is `this` determined?
3. How do arrow functions treat `this`?
4. What is a prototype?
5. What is the prototype chain?
6. How do JavaScript objects inherit behavior?
7. What does `class` do?
8. Are JavaScript classes really class-based under the hood, or something else? (If you know Java: how does that compare to Java's class model, covered in the Java skill tree?)
9. What is a constructor?
10. What does `new` do conceptually?
11. What does `extends` do?
12. What does `super` do?
13. What is encapsulation?
14. What are private class fields?
15. What is composition?
16. Inheritance vs composition?

---

# 219. Interview Questions — Async

Be prepared to answer these especially well:

1. What does synchronous mean?
2. What does asynchronous mean?
3. Why does frontend development need asynchronous behavior?
4. Is JavaScript single-threaded?
5. What is the call stack?
6. What are browser Web APIs?
7. What is the event loop?
8. What is a task?
9. What is a microtask?
10. What is a callback?
11. What is a Promise?
12. What states can a Promise have?
13. What does `.then()` do?
14. What does `.catch()` do?
15. What does `async` do?
16. What does `await` do?
17. Does `await` freeze the browser?
18. Why can `setTimeout(fn, 0)` run after later synchronous code?
19. Why do Promise callbacks commonly run before timer callbacks?
20. What is `Promise.all()`?
21. What is sequential vs concurrent async work?
22. What happens when a Promise rejects?
23. How do you handle errors with async/await?
24. What does `fetch()` return?
25. Does `fetch()` reject automatically for a 404?

---

# 220. Interview Questions — Browser

Be prepared to answer:

1. What is the DOM?
2. Is the DOM part of JavaScript itself?
3. What is `document`?
4. What does `querySelector()` do?
5. What is an event?
6. What does `addEventListener()` do?
7. What is the event object?
8. What is event bubbling?
9. What is event delegation?
10. What does `preventDefault()` do?
11. What is `FormData`?
12. How can JavaScript modify CSS state?
13. What is `localStorage`?
14. What is the difference between `localStorage` and `sessionStorage`?
15. What is XSS?
16. Why can `innerHTML` be dangerous?
17. Why can't frontend JavaScript safely store secrets?

---

# 221. Interview Questions — Full-Stack Boundary

Be prepared to answer:

1. What is `fetch()`?
2. What is JSON?
3. What is an HTTP request?
4. What is an HTTP response?
5. What are GET and POST?
6. What are PUT, PATCH, and DELETE?
7. What is an HTTP status code?
8. What does `response.ok` mean?
9. What is an origin?
10. What is the same-origin policy?
11. What is CORS?
12. Why might curl reach an API while browser JavaScript is blocked?
13. Why must the backend validate frontend input?
14. What does the frontend do with a backend response?
15. How does asynchronous JavaScript relate to API requests?

---

# 222. Recommended Learning Order

Use this progression:

```text
JavaScript vs browser vs Node
            ↓
variables + primitive types
            ↓
operators + coercion
            ↓
conditionals + loops
            ↓
functions
            ↓
arrays + objects
            ↓
array methods
            ↓
references + copying
            ↓
scope + closures
            ↓
this
            ↓
prototypes + classes
            ↓
errors
            ↓
synchronous execution
            ↓
call stack
            ↓
event loop
            ↓
Promises
            ↓
async / await
            ↓
DOM
            ↓
events + forms
            ↓
fetch + JSON + HTTP
            ↓
CORS / browser security
            ↓
modules
            ↓
Node + npm fundamentals
            ↓
testing/debugging
            ↓
TypeScript / framework
```

Do not rush directly to frameworks.

---

# 223. Memorization Strategy

JavaScript should be learned in three layers.

## Layer 1 — Recall

Be able to write common syntax from memory.

Example:

```javascript
function add(a, b) {
    return a + b;
}
```

## Layer 2 — Application

Given a problem, know which structure applies.

Example:

> "I need only users over 18."

Think:

```javascript
filter()
```

## Layer 3 — Explanation

Be able to explain:

- input,
- operation,
- return value,
- mutation behavior,
- runtime behavior.

Example:

> "`filter()` calls a callback for each array element and returns a new array containing the elements for which the callback produced a truthy result. It does not mutate the original array."

That third layer is what turns memorized syntax into interview-ready knowledge.

---

# 224. Documentation Reading Checklist

Whenever you encounter an unfamiliar JavaScript function, answer:

```text
1. What object/module owns it?
2. How is it called?
3. What arguments does it accept?
4. Which arguments are optional?
5. What does each argument mean?
6. Does it accept a callback?
7. What arguments does that callback receive?
8. What does the function return?
9. Is the return value a Promise?
10. Does it mutate existing data?
11. What errors can occur?
12. What is the smallest working example?
```

For example:

```javascript
dictionaryEquivalent.method(...)
```

should become:

```text
receiver
method
arguments
return value
side effects
example
```

This is a reusable strategy for learning APIs from terse documentation.

---

# 225. Final Mastery Project

Build a small frontend application **without React, Angular, Vue, or TypeScript**.

Example: message board, task tracker, notes app, or simple inventory viewer.

Required structure:

```text
project/
├── index.html
├── css/
│   └── styles.css
└── js/
    ├── main.js
    ├── api.js
    ├── ui.js
    └── form.js
```

Requirements:

### HTML

- semantic structure,
- accessible form,
- appropriate labels,
- loading/error/content regions.

### CSS

- responsive layout,
- Flexbox/Grid,
- visible focus states,
- reusable state classes.

### JavaScript

- ES modules,
- variables/functions,
- arrays/objects,
- array transformations,
- DOM creation,
- event handling,
- form processing,
- client validation,
- loading state,
- error state,
- `fetch()`,
- async/await,
- HTTP status handling.

### Debugging

Use:

- Console,
- breakpoints,
- Network panel,
- DOM inspector.

### Backend Boundary

Connect to either:

- a simple practice API,
- or your own backend.

Be able to explain the complete flow:

```text
User
 ↓
HTML control
 ↓ event
JavaScript
 ↓ validation
fetch()
 ↓ HTTP request
Backend
 ↓ response
Promise settles
 ↓
JavaScript
 ↓ data transformation
DOM update
 ↓
User sees result
```

---

# 226. Final Standard for JavaScript Frontend Mastery

You should eventually be able to open an empty `.js` file and write small-to-medium functions without searching for basic syntax.

You should be able to explain:

```text
JavaScript language
vs
browser APIs
vs
Node.js
vs
frontend frameworks
```

You should understand:

- variables and values,
- primitive types,
- dynamic typing,
- coercion,
- equality,
- truthy/falsy behavior,
- control flow,
- functions,
- callbacks,
- arrays,
- objects,
- Map and Set,
- destructuring,
- spread/rest,
- references,
- shallow copying,
- scope,
- closures,
- hoisting,
- `this`,
- prototypes,
- classes,
- functional programming concepts,
- error handling,
- modules.

For frontend development, you should be able to:

- inspect and modify the DOM,
- create/remove elements,
- handle browser events,
- understand bubbling,
- use event delegation,
- process forms,
- use native validation,
- use `FormData`,
- manage CSS state through classes,
- use browser storage appropriately.

For asynchronous development, you should be able to explain—not merely use:

```text
call stack
browser/runtime APIs
event loop
task
microtask
callback
Promise
async
await
concurrent asynchronous operations
```

You should be able to connect to backend systems using:

```text
fetch
HTTP
JSON
GET
POST
PUT
PATCH
DELETE
status codes
headers
request/response bodies
```

You should understand:

- same-origin policy,
- CORS,
- XSS fundamentals,
- why client validation is not security,
- why frontend secrets are not secret.

You should understand enough Node.js/npm to explain:

```text
Node runtime
npm
package.json
dependencies
devDependencies
node_modules
lock files
```

And you should be able to debug using:

```text
Console
breakpoints
call stack
scope inspector
Network panel
DOM inspector
```

The end goal is:

> **Write and reason about JavaScript rather than merely recognize it; understand the language well enough to predict its behavior; understand asynchronous frontend execution well enough to explain it in an interview; build a complete interactive browser application without a framework; communicate with backend APIs; and possess a strong enough JavaScript foundation that TypeScript, Angular, React, or another framework becomes an additional tool rather than a replacement for understanding the language.**

---

# 227. What Comes Next

Once this map is comfortable, the natural progression is:

```text
HTML + CSS
     ↓
JavaScript
     ↓
TypeScript
     ↓
Frontend framework
     ↓
production frontend architecture
```

Possible separate skill maps:

```text
TypeScript
Angular
React
Node.js backend development
npm / frontend tooling
HTTP & REST API design
Frontend testing
Web accessibility
Web security
Browser internals
```

For your existing full-stack direction, a particularly useful progression is:

```text
HTML/CSS
   ↓
JavaScript
   ↓
TypeScript
   ↓
Angular
   ↓
HTTP/API integration
   ↓
Spring Boot
   ↓
PostgreSQL
```

Each layer should make the next one easier to understand rather than hiding the layer underneath it.
