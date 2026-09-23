# React Frontend Development --- Mastery Skill Tree

## Purpose and mastery goal

This map assumes HTML/CSS, JavaScript, and TypeScript are prerequisites
and assumes **zero React knowledge**. It prioritizes modern React:
function components, hooks, TypeScript/TSX, composition, modern
rendering concepts, and practical REST API integration.

The final goal is:

> Given a REST API, I can create a React + TypeScript frontend from
> scratch; structure it into reusable components; use JSX/TSX correctly;
> model props and state; understand React's rendering model; use hooks
> appropriately; manage local and shared state; build forms; perform
> asynchronous API operations; handle loading, errors, cancellation, and
> race conditions; configure client-side routing; test components; debug
> React/browser/network problems; build production assets; and explain
> how React fits into a full-stack architecture.

------------------------------------------------------------------------

# 1. Prerequisite tree

``` text
HTML
├── semantic elements
├── forms
└── accessibility basics

CSS
├── selectors
├── box model
├── layout
├── responsive design
└── component styling

JavaScript
├── variables/functions
├── objects/arrays
├── map/filter/reduce
├── destructuring/spread
├── modules
├── closures
├── events/DOM
├── Promises
└── async/await

TypeScript
├── primitive/object types
├── interfaces/type aliases
├── unions/narrowing
├── generics
├── function typing
└── modules
        ↓
      REACT
```

------------------------------------------------------------------------

# 2. React's place in frontend development

React is a **JavaScript library for building user interfaces**. It is
not an all-inclusive framework in the same sense as Angular.

``` text
React          → UI/components/rendering
React Router   → routing
Vite           → development/build tooling
Redux/Zustand  → optional client-state tools
Next.js        → React application framework
Testing tools  → separate ecosystem
```

### Must know

-   React vs JavaScript/TypeScript.
-   React vs Angular.
-   Library vs framework.
-   Declarative vs imperative UI.
-   Single-page application (SPA).
-   Client-side rendering.
-   Component-based architecture.
-   One-way data flow.
-   React is not a backend, database, router, or HTTP protocol.

### Interview standard

Be able to answer:

-   What is React?
-   What problem does React solve?
-   Why is React called declarative?
-   React vs Angular?
-   React vs Next.js?
-   React vs Vite?

------------------------------------------------------------------------

# 3. Project setup and tooling

## Core commands to memorize

Typical Vite/npm workflow:

``` bash
npm create vite@latest
npm install
npm run dev
npm run build
npm run test
npm install <package>
npm uninstall <package>
npm update
```

Understand that scripts such as `npm run dev` come from `package.json`;
they are not React commands.

## Files to recognize

``` text
project/
├── package.json
├── package-lock.json
├── tsconfig*.json
├── vite.config.*
├── index.html
├── public/
└── src/
    ├── main.tsx
    └── App.tsx
```

Know the purpose of:

-   `package.json`
-   dependencies vs devDependencies
-   npm scripts
-   `node_modules`
-   `src`
-   `public`
-   `index.html`
-   `main.tsx`
-   `App.tsx`
-   TypeScript configuration
-   Vite configuration

### Mental model

``` text
React/TypeScript source
        ↓
development/build tooling
        ↓
HTML/CSS/JavaScript/assets
        ↓
browser
```

Deployment infrastructure and CI/CD should be studied separately.

------------------------------------------------------------------------

# 4. JSX and TSX

JSX describes UI inside JavaScript.

``` tsx
const heading = <h1>Hello</h1>;
```

TSX is JSX used with TypeScript and normally uses `.tsx`.

## Memorize

``` tsx
<h1>{name}</h1>

<button disabled={loading}>
    Save
</button>

<div className="card">
    Content
</div>
```

## Master

-   JSX vs HTML.
-   JavaScript expressions with `{}`.
-   JSX attributes.
-   `className`.
-   inline expressions.
-   fragments.
-   nested components.
-   comments.
-   conditional rendering.
-   list rendering.
-   escaping/rendering text safely.

### Fragments

``` tsx
<>
    <Header />
    <Main />
</>
```

### Conditional rendering

``` tsx
{loggedIn ? <Dashboard /> : <Login />}
```

``` tsx
{error && <ErrorMessage />}
```

### List rendering

``` tsx
{messages.map(message => (
    <MessageCard
        key={message.id}
        message={message}
    />
))}
```

------------------------------------------------------------------------

# 5. Components

Modern React primarily uses function components.

``` tsx
function Greeting() {
    return <h1>Hello</h1>;
}
```

## Master

-   function components.
-   naming conventions.
-   component tree.
-   composition.
-   component responsibility.
-   reusable components.
-   feature vs presentational components.
-   when to split components.
-   when *not* to split components.
-   pure rendering.

### Component tree example

``` text
App
├── Header
├── MessagePage
│   ├── MessageForm
│   └── MessageList
│       └── MessageCard
└── Footer
```

Avoid both extremes:

``` text
one giant component
```

and:

``` text
a component for every <div>
```

Split based on responsibility, reuse, state ownership, and readability.

------------------------------------------------------------------------

# 6. Props

Props are read-only inputs supplied by a parent.

``` tsx
interface UserCardProps {
    name: string;
    age: number;
}

function UserCard({ name, age }: UserCardProps) {
    return <p>{name}: {age}</p>;
}
```

## Data flow

``` text
Parent
   ↓ props
Child
```

## Child-to-parent communication

React commonly passes a callback downward:

``` tsx
interface Props {
    onDelete: () => void;
}
```

``` text
Parent
   ↓ callback prop
Child
   ↓ calls callback
Parent handles action
```

## Master

-   typed props.
-   optional props.
-   destructuring props.
-   callback props.
-   `children`.
-   composition.
-   props are read-only.
-   default values.
-   parent/child responsibility.

------------------------------------------------------------------------

# 7. `children` and composition

``` tsx
<Card>
    <p>Hello</p>
</Card>
```

Nested content is available through `children`.

Understand why React favors composition over large inheritance
hierarchies.

------------------------------------------------------------------------

# 8. Keys and component identity

Lists require stable keys:

``` tsx
<Item key={item.id} item={item} />
```

Keys help React preserve identity across renders.

Avoid index keys when insertion, deletion, or reordering can occur:

``` tsx
key={index}
```

Learn that keys also influence whether React preserves or resets
component state.

------------------------------------------------------------------------

# 9. Events

Common handlers:

``` text
onClick
onChange
onSubmit
onFocus
onBlur
onKeyDown
```

Example:

``` tsx
<button onClick={handleClick}>Save</button>
```

Understand the difference between:

``` tsx
onClick={handleClick}
```

and:

``` tsx
onClick={handleClick()}
```

The first passes a function. The second calls it during rendering.

Type React event objects correctly when needed.

------------------------------------------------------------------------

# 10. State and `useState`

State is changing information that affects UI.

``` tsx
const [count, setCount] = useState(0);
```

Memorize:

``` text
count     → current render's state value
setCount  → requests a state update
0         → initial state
```

## Functional updates

When new state depends on previous state:

``` tsx
setCount(previous => previous + 1);
```

## Critical theory

State behaves like a **snapshot for a render**. Calling a setter does
not mutate the current local variable.

React may batch updates.

------------------------------------------------------------------------

# 11. Immutable state updates

Do not directly mutate React state.

Object:

``` tsx
setUser({
    ...user,
    name: "Bob"
});
```

Array addition:

``` tsx
setItems([
    ...items,
    newItem
]);
```

Array removal:

``` tsx
setItems(items.filter(item => item.id !== id));
```

Array transformation:

``` tsx
setItems(items.map(item =>
    item.id === id
        ? { ...item, completed: true }
        : item
));
```

Memorize practical use of:

``` text
spread
map
filter
slice
```

------------------------------------------------------------------------

# 12. Derived state

Do not store information that can simply be calculated from existing
state/props unless there is a reason.

``` tsx
const completedCount =
    todos.filter(todo => todo.completed).length;
```

Ask:

``` text
Is this true state?
Or can it be derived?
```

Avoid duplicated state that must be manually synchronized.

------------------------------------------------------------------------

# 13. State ownership and lifting state

Ask:

``` text
Who needs this value?
Who changes it?
Who should be authoritative?
How long should it live?
```

Sibling coordination:

``` text
        Parent
      owns state
       ↙     ↘
  Child A   Child B
```

Moving shared state to the nearest useful common ancestor is **lifting
state up**.

------------------------------------------------------------------------

# 14. Controlled and uncontrolled inputs

Controlled:

``` tsx
const [name, setName] = useState("");

<input
    value={name}
    onChange={e => setName(e.target.value)}
/>
```

Flow:

``` text
state
 ↓
input
 ↓ user types
event
 ↓
setter
 ↓
new render
```

Uncontrolled inputs primarily keep their current value in the DOM and
may be accessed using refs.

Know both approaches.

------------------------------------------------------------------------

# 15. Hooks

Core hooks to master:

``` text
useState
useEffect
useRef
useContext
useReducer
useMemo
useCallback
```

Also understand custom hooks.

## Rules of Hooks

-   Call hooks at the top level.
-   Call hooks from React components or custom hooks.
-   Keep hook order consistent.

Do not conditionally call hooks.

------------------------------------------------------------------------

# 16. `useEffect`

`useEffect` is for synchronizing React with systems outside normal
render calculation.

``` tsx
useEffect(() => {
    // setup/synchronization

    return () => {
        // cleanup
    };
}, [dependencies]);
```

Good conceptual uses:

``` text
browser APIs
timers
subscriptions
network synchronization
third-party systems
```

## Critical mastery topic: when NOT to use an effect

Before adding an effect ask:

``` text
Can this be calculated during render?
Can this happen directly in the event handler?
Is duplicated state causing this problem?
Can state ownership be improved?
```

Effects should not become a generic "when X changes, do Y" mechanism.

------------------------------------------------------------------------

# 17. Effect dependencies and cleanup

Understand:

``` text
effect body
dependency array
cleanup
```

Do not remove dependencies merely to silence warnings.

Cleanup may be needed for:

``` text
timers
event listeners
subscriptions
requests/cancellation
external resources
```

Do not memorize `[]` as simply "runs once." Understand component
lifecycle and development Strict Mode behavior.

------------------------------------------------------------------------

# 18. `useRef`

``` tsx
const inputRef = useRef<HTMLInputElement>(null);
```

Uses:

``` text
DOM access
focus management
persistent mutable values
external library integration
values that should not trigger rendering
```

Example:

``` tsx
inputRef.current?.focus();
```

Do not use refs as a replacement for ordinary state.

------------------------------------------------------------------------

# 19. Context and `useContext`

Context distributes values through a component subtree.

``` text
Provider
   ↓
component tree
   ↓
consumer
```

Common examples:

``` text
theme
authenticated user/session
locale
configuration
feature-level shared state
```

Understand **prop drilling** and when context actually improves
architecture.

Context is a distribution mechanism, not automatically a complete
state-management solution.

------------------------------------------------------------------------

# 20. `useReducer`

Reducer model:

``` text
current state
     +
action
     ↓
reducer
     ↓
new state
```

Conceptual signature:

``` typescript
(state, action) => newState
```

Use reducers for more structured/complex related transitions.

Possible actions:

``` text
ADD_ITEM
REMOVE_ITEM
UPDATE_ITEM
RESET
LOAD_SUCCESS
LOAD_ERROR
```

Understand `useReducer` vs `useState`.

------------------------------------------------------------------------

# 21. Context + reducer

A common shared-state pattern:

``` text
Context
    distributes state/dispatch

Reducer
    defines state transitions
```

Learn this before assuming Redux or another external state library is
required.

------------------------------------------------------------------------

# 22. Custom hooks

Custom hooks package reusable React behavior.

Convention:

``` text
useSomething
```

Examples:

``` text
useMessages
useAuth
useWindowSize
useDebounce
```

A custom hook:

``` text
is a function
uses hooks
packages reusable stateful behavior
```

It does **not** automatically share state between callers.

------------------------------------------------------------------------

# 23. React rendering model

This is one of the most important expert-level areas.

``` text
props/state/context change
        ↓
React renders component
        ↓
component calculates JSX
        ↓
React reconciles old/new tree
        ↓
commit phase
        ↓
necessary DOM changes
```

Master:

-   render phase.
-   commit phase.
-   reconciliation.
-   component identity.
-   state preservation.
-   state reset.
-   keys.
-   pure rendering.
-   Strict Mode.

Rendering a component does **not** mean rebuilding the entire DOM.

------------------------------------------------------------------------

# 24. Pure rendering

Rendering should calculate UI without uncontrolled side effects.

Do not perform actions such as these directly during render:

``` text
network requests
starting timers
DOM mutations
changing global variables
```

Use events/effects or other appropriate boundaries.

------------------------------------------------------------------------

# 25. Strict Mode

Strict Mode helps reveal unsafe assumptions during development.

It can expose problems involving:

``` text
impure rendering
missing cleanup
unsafe effects
```

Do not disable it merely because development behavior seems surprising.

Find the underlying assumption.

------------------------------------------------------------------------

# 26. Async JavaScript and API calls

React itself does not require a special HTTP client.

Start with browser `fetch`.

``` typescript
const response = await fetch("/api/messages");

if (!response.ok) {
    throw new Error("Request failed");
}

const data = await response.json();
```

## Must know

-   Promise.
-   `async`.
-   `await`.
-   `fetch`.
-   `Response`.
-   `response.ok`.
-   status codes.
-   JSON serialization.
-   network errors vs HTTP errors.
-   loading/error/success state.

Important: `fetch()` generally does not reject simply because the server
returns HTTP 404 or 500.

------------------------------------------------------------------------

# 27. API modules

Avoid scattering transport details across components.

Example:

``` text
src/api/messages.ts
```

Functions:

``` text
getMessages()
getMessage(id)
createMessage(request)
updateMessage(id, request)
deleteMessage(id)
```

Conceptually:

``` text
Component
    ↓
Custom hook / API module
    ↓
fetch
    ↓
REST API
```

React has no mandatory equivalent to Angular's service layer.

------------------------------------------------------------------------

# 28. TypeScript and runtime API boundaries

Example:

``` typescript
interface Message {
    id: number;
    name: string;
    message: string;
    createdAt: string;
}
```

Typing:

``` typescript
const data: Message[] = await response.json();
```

does **not** runtime-validate arbitrary JSON.

Understand:

``` text
TypeScript compile-time type
        ≠
runtime validation
```

Runtime validation deserves further study for untrusted/important
boundaries.

------------------------------------------------------------------------

# 29. Async state

Model request state deliberately:

``` text
idle
loading
success
empty
error
```

Avoid treating "nothing rendered" as all of these states.

------------------------------------------------------------------------

# 30. Race conditions

Example:

``` text
search "a"  → request A
search "ab" → request B
B completes
A completes later
```

Without coordination, stale A may overwrite newer B.

Understand:

-   stale responses.
-   request ownership.
-   cancellation.
-   latest-request behavior.

------------------------------------------------------------------------

# 31. `AbortController`

Learn browser request cancellation.

Use cases:

``` text
component no longer needs request
query changes
new request supersedes old request
```

This is important for robust asynchronous frontend work.

------------------------------------------------------------------------

# 32. Forms

React core has no Angular Reactive Forms equivalent.

Learn forms from:

``` text
HTML
+
state
+
events
+
validation
```

Master:

-   controlled fields.
-   uncontrolled fields.
-   `onChange`.
-   `onSubmit`.
-   `preventDefault()`.
-   validation.
-   touched/error concepts.
-   submitting state.
-   server errors.
-   success/reset.

Backend validation remains authoritative.

------------------------------------------------------------------------

# 33. Form libraries

After raw React forms are understood, recognize why form libraries
exist:

``` text
less repetitive state handling
validation integration
performance
large dynamic forms
field registration
```

React Hook Form and schema-validation tools can receive separate maps
later.

------------------------------------------------------------------------

# 34. Routing

React core does not provide a complete router.

Learn routing concepts and a common routing library such as React
Router.

Master:

``` text
route
path
link
nested route
route parameter
query parameter
programmatic navigation
fallback/404
lazy route
```

Concept:

``` text
URL
 ↓
router
 ↓
matching route
 ↓
component tree
```

------------------------------------------------------------------------

# 35. Route security boundary

A React "protected route" (redirecting unauthenticated users away from a
page) only improves navigation and UX — it runs entirely in the browser
and can be bypassed by anyone calling the API directly. Real security is
enforced by the backend on every request, not by whether the frontend
router decided to render a page. Treat route guards as a UX convenience
layered on top of backend authorization, never as a substitute for it.
See the Angular skill tree's "Route Guard Security Limitation" and "CORS
Is Not Authentication" sections (#155-156) for the fuller argument — the
same principle applies unchanged to React.

------------------------------------------------------------------------

# 36. Local vs shared state

Progress through these options in order:

``` text
local component state
        ↓
lifted state
        ↓
context
        ↓
reducer
        ↓
context + reducer
        ↓
external state library
```

Do not make state global simply because multiple components exist.

------------------------------------------------------------------------

# 37. Client state vs server state

Client state:

``` text
modal open
selected tab
draft
theme
```

Server state:

``` text
messages
users
orders
products
```

Server state adds concerns such as:

``` text
fetching
caching
refreshing
deduplication
invalidating
synchronization
```

This distinction is essential when evaluating libraries such as
server-state/query tools.

------------------------------------------------------------------------

# 38. External state libraries

Know why tools such as Redux or Zustand exist, but do not make them
prerequisites for React.

Potential reasons:

``` text
complex cross-feature shared state
structured updates
debugging/devtools
large application coordination
```

They deserve separate skill maps.

------------------------------------------------------------------------

# 39. Memoization and performance

Recognize:

``` text
useMemo
useCallback
React.memo
```

### `useMemo`

Caches/reuses a computed result based on dependencies.

### `useCallback`

Memoizes function identity.

### `React.memo`

Can skip some child rendering when props remain equivalent.

Critical rule:

``` text
measure
 ↓
understand cause
 ↓
improve architecture
 ↓
memoize if justified
```

Do not add memoization everywhere.

------------------------------------------------------------------------

# 40. Lazy loading and code splitting

Understand:

``` text
initial bundle
     ↓
user visits feature
     ↓
feature code loads
```

Study:

-   dynamic imports.
-   lazy component loading.
-   code splitting.
-   loading fallback.
-   Suspense fundamentals.

------------------------------------------------------------------------

# 41. Suspense

Conceptually:

``` text
content unavailable
      ↓
fallback UI
      ↓
content ready
      ↓
render content
```

Learn current supported uses. Do not treat Suspense as a generic
replacement for every loading state.

------------------------------------------------------------------------

# 42. React Server Components, SSR, and hydration

These should be understood conceptually, not dominate this frontend map.

## Client-side rendering

``` text
browser gets assets
      ↓
JavaScript runs
      ↓
React renders UI
```

## Server-side rendering

``` text
server renders initial HTML
      ↓
browser receives HTML
      ↓
React attaches interactivity
```

## Hydration

React connects client behavior to server-rendered markup.

## React Server Components

Some modern React frameworks can execute certain components in server
environments.

These concepts deserve deeper study with a React framework such as
Next.js.

------------------------------------------------------------------------

# 43. Security

Master practical frontend boundaries.

## XSS

React escapes normal rendered values:

``` tsx
<p>{userInput}</p>
```

## `dangerouslySetInnerHTML`

Recognize why this API is dangerous. Never use it casually with
untrusted content.

## Frontend secrets

Never ship real secrets (API keys, credentials, signing keys) in React
source, `.env`/`import.meta.env` values, the bundled JavaScript, or any
other public configuration — anything sent to the browser is readable by
the user. This is the same rule covered in the Angular skill tree's
"Frontend Secrets" section (#153); only the file names differ
(`.env` in Vite/CRA vs. `environment.ts` in Angular), not the principle.

## Authentication vs authorization

``` text
Authentication → Who are you?
Authorization  → What may you do?
```

The backend must enforce authorization.

## CORS

Understand that CORS is a browser cross-origin policy, not
authentication.

------------------------------------------------------------------------

# 44. Development proxy and CORS

Vite (`localhost:5173`) and Spring Boot (`localhost:8081`) run on
different ports, which the browser treats as different origins — so
requests from the dev server to the API are cross-origin by default. A
dev server proxy (forwarding `/api` requests from Vite to Spring Boot)
sidesteps this in development by making requests look same-origin, while
CORS configuration on the backend is what actually governs which origins
may call the API in production. Keep proxy configuration, CORS
configuration, authentication, and authorization conceptually separate —
fixing one does not fix the others. This is the identical mechanism
covered in more depth in the Angular skill tree's "Development Proxy",
"Proxy Configuration", and "Proxy vs CORS" sections (#80-83).

------------------------------------------------------------------------

# 45. Testing

Learn enough testing inside the React tree to test real React code.
Deeper testing tools should later receive their own maps.

Focus on:

``` text
unit testing
component testing
integration testing
async testing
mocking
E2E concepts
```

## React Testing Library philosophy

Test observable behavior:

``` text
render component
      ↓
find accessible UI
      ↓
interact as user
      ↓
assert result
```

Prefer semantic queries such as:

``` text
role
label
text
```

when appropriate.

------------------------------------------------------------------------

# 46. Testing topics to master

-   render a component.
-   assert text/content.
-   test props.
-   click buttons.
-   type into inputs.
-   submit forms.
-   test loading/error states.
-   test async updates.
-   mock API boundaries.
-   test router/context integration.
-   avoid excessive implementation-detail testing.

Dedicated maps later:

``` text
React Testing Library
Vitest/Jest
E2E testing
mocking/testing theory
```

------------------------------------------------------------------------

# 47. Accessibility

React does not replace semantic HTML.

Prefer:

``` html
<button>
<label>
<input>
<nav>
<main>
<header>
```

over generic clickable elements when semantic elements exist.

Understand:

-   labels.
-   keyboard interaction.
-   focus.
-   accessible names.
-   ARIA when appropriate.
-   dialogs/menus/tabs.
-   dynamic status/error announcements.

------------------------------------------------------------------------

# 48. Debugging

Use browser DevTools:

``` text
Elements
Console
Network
Sources
Application/Storage
Performance
```

Use React DevTools for:

``` text
component tree
props
state
hooks
render behavior
profiling
```

------------------------------------------------------------------------

# 49. Common React problems

## Infinite render

``` text
render
 ↓
setState
 ↓
render
 ↓
setState
 ↓
...
```

## Infinite effect

``` text
effect
 ↓
changes dependency
 ↓
effect
 ↓
changes dependency
```

## Stale closure

A callback captures values from an older render.

Common locations:

``` text
timers
effects
async callbacks
external listeners
```

## Missing/unstable keys

Can cause incorrect identity and state behavior.

## Unexpected state reset

Investigate:

``` text
key changed
component type changed
tree position/identity changed
```

## Direct state mutation

Can create incorrect render behavior and difficult debugging.

## Duplicate API calls

Investigate:

``` text
effects
multiple consumers
Strict Mode revealing unsafe setup
duplicate event execution
architecture
```

------------------------------------------------------------------------

# 50. Network/API debugging

For failed requests inspect:

``` text
request URL
HTTP method
request headers
request body
status code
response headers
response body
timing
CORS messages
```

Recognize common statuses:

``` text
200 OK
201 Created
204 No Content
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
409 Conflict
500 Internal Server Error
```

Determine whether the failure belongs to:

``` text
React
browser
network
proxy
CORS
backend
database
```

------------------------------------------------------------------------

# 51. Legacy React recognition

Modern priority:

``` text
function components
hooks
```

But recognize older:

``` text
class components
this.props
this.state
this.setState
componentDidMount
componentDidUpdate
componentWillUnmount
higher-order components
render props
```

Do not blindly translate every class lifecycle method into one
`useEffect`. Modern hooks often reorganize behavior by synchronization
concern.

------------------------------------------------------------------------

# 52. React vs Angular mental map

``` text
ANGULAR                         REACT
────────────────────────────────────────────
Component                       Component
Template                        JSX/TSX
Input                           Props
Output/EventEmitter             Callback prop
Signal                          State (rough analogy)
Service                         No exact equivalent
DI                              No general equivalent
HttpClient                      fetch / HTTP library
RxJS Observable                 Promise/async common
Reactive Forms                  State/form libraries
Angular Router                  External router
Angular CLI                     Separate tooling
```

## Angular service vs React approach

Angular:

``` text
Component
   ↓ DI
Service
   ↓
HttpClient
```

React commonly:

``` text
Component
   ↓
Custom Hook
   ↓
API Module
   ↓
fetch
```

This is a common architecture, not a React requirement.

------------------------------------------------------------------------

# 53. Full-stack React architecture

``` text
React
────────────────────────

Component
   ↓
Custom Hook / API Module
   ↓
fetch

──────── HTTP boundary ────────

Spring Boot
────────────────────────

Controller
   ↓
Service
   ↓
Repository
   ↓
PostgreSQL
```

Be able to explain why each boundary exists: each layer has one job, and
collapsing layers together (e.g. querying the database from a
component) makes the system harder to test, secure, and change. This
mirrors the same client → API → service → repository → database
layering shown in the Angular skill tree's "Full-Stack Angular/Spring
Architecture" section (#206), adapted for React's hook-based
data-fetching style in place of an injectable service.

------------------------------------------------------------------------

# 54. Full-stack responsibilities

## React component

``` text
rendering
user interaction
local UI state
frontend orchestration
```

## Custom hook

``` text
reusable React behavior
state coordination
effects/API coordination
```

## API module

``` text
URLs
HTTP methods
serialization
response parsing
status handling
```

## Spring Controller

``` text
HTTP backend boundary
```

## Spring Service

``` text
backend business logic
```

## Repository

``` text
database persistence
```

------------------------------------------------------------------------

# 55. Project architecture

Possible medium-sized structure:

``` text
src/
├── app/
├── api/
├── features/
│   ├── messages/
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── api/
│   │   └── types/
│   └── users/
├── shared/
├── routes/
├── App.tsx
└── main.tsx
```

This is a convention, not a React rule.

Organize around coherent responsibilities rather than blindly copying
folder structures.

------------------------------------------------------------------------

# 56. Interview bank --- React fundamentals

Be able to answer without notes:

1.  What is React?
2.  Why is React called a library?
3.  React vs Angular?
4.  What is declarative UI?
5.  What is a component?
6.  What is JSX?
7.  JSX vs HTML?
8.  What is TSX?
9.  What is a Fragment?
10. What are props?
11. Props vs state?
12. What is `children`?
13. How does parent-to-child communication work?
14. How does child-to-parent communication work?
15. What is composition?
16. How do you render a list?
17. Why are keys required?
18. Why can index keys be problematic?

------------------------------------------------------------------------

# 57. Interview bank --- state and hooks

1.  What does `useState` return?
2.  Why can't you assign directly to state?
3.  What is a functional state update?
4.  What does "state is a snapshot" mean?
5.  Why should state updates be immutable?
6.  What is derived state?
7.  What is lifting state up?
8.  What are the Rules of Hooks?
9.  Why does hook order matter?
10. What does `useEffect` do?
11. When should you *not* use an effect?
12. What is effect cleanup?
13. What is an effect dependency?
14. What does `useRef` do?
15. What does `useContext` do?
16. What does `useReducer` do?
17. `useReducer` vs `useState`?
18. What is a custom hook?
19. Does a custom hook automatically share state?

------------------------------------------------------------------------

# 58. Interview bank --- rendering

1.  What causes a component to render?
2.  What is the render phase?
3.  What is the commit phase?
4.  What is reconciliation?
5.  Does React rebuild the whole DOM on every render?
6.  What determines component identity?
7.  How do keys affect identity?
8.  Why can state unexpectedly reset?
9.  What is Strict Mode?
10. Why should rendering be pure?
11. What is a stale closure?

------------------------------------------------------------------------

# 59. Interview bank --- API and async

1.  Does React provide its own HTTP client?
2.  What does `fetch()` return?
3.  Does `fetch` reject for HTTP 404/500?
4.  What is `response.ok`?
5.  Why separate API functions from components?
6.  What states should an async request model?
7.  What is a race condition?
8.  What is `AbortController`?
9.  Why can API calls inside effects become complicated?
10. Client state vs server state?
11. Does a TypeScript interface validate JSON at runtime?

------------------------------------------------------------------------

# 60. Interview bank --- forms, routing, architecture

1.  Controlled vs uncontrolled input?
2.  What does `preventDefault()` do?
3.  Why must backend validation still exist?
4.  Does React provide routing?
5.  What is a route parameter?
6.  What is a query parameter?
7.  Why isn't a protected frontend route security?
8.  What is prop drilling?
9.  When should Context be used?
10. When would an external state library be appropriate?
11. What is a custom hook vs an Angular service?
12. Why doesn't React require dependency injection?

------------------------------------------------------------------------

# 61. Interview bank --- performance/security/testing

1.  What does `useMemo` do?
2.  What does `useCallback` do?
3.  What does `React.memo` do?
4.  Why not memoize everything?
5.  What is lazy loading?
6.  What is code splitting?
7.  What is Suspense?
8.  What is XSS?
9.  What is `dangerouslySetInnerHTML`?
10. Why can't frontend environment variables contain secrets?
11. Authentication vs authorization?
12. What is CORS?
13. What is React Testing Library?
14. Why test user-visible behavior?
15. Unit vs integration vs E2E test?

------------------------------------------------------------------------

# 62. Progressive mastery levels

## Level 1 --- Tooling and JSX

Can:

``` text
create project
install dependencies
run dev server
understand main.tsx
write JSX/TSX
create function components
```

## Level 2 --- Components and props

Can:

``` text
type props
compose components
use children
use callbacks
render conditions/lists
choose stable keys
```

## Level 3 --- State and events

Can:

``` text
useState
handle events
use functional updates
update objects/arrays immutably
build controlled inputs
lift state
```

## Level 4 --- Hooks

Can explain and use:

``` text
useEffect
cleanup
dependencies
useRef
useContext
useReducer
custom hooks
```

## Level 5 --- Rendering theory

Can explain:

``` text
render
commit
reconciliation
identity
keys
state preservation
Strict Mode
pure rendering
```

## Level 6 --- Async/API

Can:

``` text
use fetch
use async/await
handle HTTP failures
model async state
cancel requests
prevent stale responses
separate API logic
```

## Level 7 --- Forms and routing

Can build:

``` text
validated forms
submission flows
routes
route params
query params
404 handling
lazy features
```

## Level 8 --- Architecture/shared state

Can choose between:

``` text
local state
lifted state
context
reducer
custom hook
external state tool
server-state tool
```

## Level 9 --- Testing/security/performance

Can:

``` text
test component behavior
debug React/network failures
explain frontend security boundaries
profile render problems
avoid premature optimization
```

## Level 10 --- Full stack

Can independently build and explain:

``` text
React + TypeScript
       ↓
REST
       ↓
Spring Boot
       ↓
PostgreSQL
```

------------------------------------------------------------------------

# 63. Practical exercises

## Exercise 1 --- Component basics

Build:

``` text
App
Greeting
Footer
```

Practice:

``` text
components
JSX
props
composition
```

## Exercise 2 --- Counter

Build:

``` text
display count
increment
decrement
reset
```

Use `useState` and functional updates.

## Exercise 3 --- User list

Build:

``` text
UserList
   └── UserCard
```

Practice typed props, `map`, keys, callbacks, and conditional rendering.

## Exercise 4 --- Todo list

Implement:

``` text
add
toggle
delete
filter
completed count
```

Practice immutable updates and derived state.

## Exercise 5 --- Lift state

Create two sibling components that need the same authoritative value.
Move ownership to their parent.

## Exercise 6 --- Forms

Build:

``` text
name
email
message
```

Add validation, errors, submission state, and reset behavior.

## Exercise 7 --- Effects

Build examples using:

``` text
document title
timer
window event listener
API request
```

Identify setup, dependency, and cleanup for each.

Then identify cases where an effect is unnecessary.

## Exercise 8 --- Context

Create a theme or session context used several levels deep.

## Exercise 9 --- Reducer

Build a shopping-cart reducer with:

``` text
ADD
REMOVE
CHANGE_QUANTITY
CLEAR
```

## Exercise 10 --- Custom hook

Build:

``` text
useWindowSize
```

or:

``` text
useMessages
```

## Exercise 11 --- API module

Implement:

``` text
GET
POST
PUT/PATCH
DELETE
```

using `fetch`, typed DTOs, and status handling.

## Exercise 12 --- Race-condition lab

Create live search. Intentionally allow stale responses, observe the
bug, then fix it with cancellation/latest-request coordination.

## Exercise 13 --- Router

Create:

``` text
/
 /messages
 /messages/:id
 /about
 /missing
```

Use links, params, query params, nested routing, and lazy loading.

## Exercise 14 --- Testing

Test:

``` text
Counter
TodoList
MessageForm
loading/error states
```

Test behavior rather than private implementation.

## Exercise 15 --- Debugging lab

Intentionally create and repair:

``` text
missing key
unstable key
direct mutation
infinite render
infinite effect
stale closure
unexpected state reset
HTTP 404
HTTP 400
HTTP 500
CORS failure
stale API response
```

------------------------------------------------------------------------

# 64. Capstone --- React Guestbook

Build a React + TypeScript frontend for the same type of REST guestbook
application used in the Angular learning project.

Suggested structure:

``` text
src/
├── api/
│   └── messages.ts
├── features/
│   └── messages/
│       ├── MessagePage.tsx
│       ├── MessageForm.tsx
│       ├── MessageList.tsx
│       ├── MessageCard.tsx
│       ├── useMessages.ts
│       └── types.ts
├── shared/
│   └── StatusMessage.tsx
├── routes/
├── App.tsx
└── main.tsx
```

## Data types

``` typescript
interface Message {
    id: number;
    name: string;
    message: string;
    createdAt: string;
}

interface CreateMessageRequest {
    name: string;
    message: string;
    passcode: string;
}
```

## Required behavior

``` text
GET messages
display messages
validated message form
POST new message
loading state
error state
success state
empty state
reload/update UI
```

Optional:

``` text
GET one
update
delete
routing
pagination
search
```

------------------------------------------------------------------------

# 65. Capstone request flow to memorize

Without notes, explain:

``` text
1. User enters values.
2. React stores/reads form state.
3. User submits.
4. Handler validates/builds request data.
5. Component/custom hook calls API function.
6. API function calls fetch().
7. Browser sends HTTP request.
8. Spring Controller receives request.
9. Spring Service executes business logic.
10. Repository accesses PostgreSQL.
11. Backend sends HTTP response.
12. fetch Promise resolves.
13. React state is updated.
14. Component renders with new state.
15. React reconciles old/new UI.
16. React commits necessary DOM changes.
17. User sees the result.
```

------------------------------------------------------------------------

# 66. Memorization tiers

## Tier 1 --- Immediate recall

``` text
component
JSX / TSX
props
state
useState
events
controlled input
conditional rendering
map
key
lifting state
```

Syntax:

``` tsx
const [value, setValue] = useState(initialValue);
```

``` tsx
<Component prop={value} />
```

``` tsx
{items.map(item => (
    <Item key={item.id} item={item} />
))}
```

## Tier 2 --- Explain without notes

``` text
useEffect
dependencies
cleanup
useRef
useContext
useReducer
custom hook
render
commit
reconciliation
identity
immutability
async request flow
```

## Tier 3 --- Implement comfortably

``` text
forms
API modules
routing
context
reducers
request cancellation
race-condition handling
component tests
shared-state architecture
```

## Tier 4 --- Recognize/explain

``` text
useMemo
useCallback
React.memo
Suspense
lazy loading
SSR
hydration
React Server Components
class components
legacy lifecycle
HOCs
render props
```

------------------------------------------------------------------------

# 67. Recommended learning order

``` text
HTML / CSS
     ↓
JavaScript
     ↓
TypeScript
     ↓
React project/tooling
     ↓
JSX / TSX
     ↓
components
     ↓
props
     ↓
composition
     ↓
events
     ↓
state
     ↓
immutable updates
     ↓
controlled forms
     ↓
lifting state
     ↓
rendering model
     ↓
hooks
     ↓
effects
     ↓
refs
     ↓
context
     ↓
reducers
     ↓
custom hooks
     ↓
async / fetch
     ↓
API architecture
     ↓
routing
     ↓
shared/server state
     ↓
testing
     ↓
security
     ↓
performance
     ↓
full-stack integration
```

------------------------------------------------------------------------

# 68. Follow-on skill trees

React should not absorb every frontend technology into one tree. Good
separate future maps include:

``` text
React Router
React Testing Library
Vitest / Jest
React Hook Form
Redux
Zustand
TanStack Query / server-state management
Next.js
Frontend Architecture
Web Accessibility
Web Security / Authentication
HTTP / REST
Node.js
npm
Vite
Frontend Performance
Deployment
Docker
CI/CD
Cloud Hosting
```

------------------------------------------------------------------------

# 69. Expert reasoning standard

Expertise is not memorizing every React API.

An expert should habitually ask:

``` text
What state exists?
Who owns it?
Who needs it?
Can this value be derived?
What event caused the change?
Should this happen in an event handler?
Is an effect actually necessary?
What external system is the effect synchronizing with?
What component identity is React preserving?
Why did this component render?
Is this client state or server state?
What crosses the HTTP boundary?
What belongs in the browser?
What must be enforced on the server?
```

The target is to understand both:

``` text
HOW to write React code
```

and:

``` text
WHY this component exists
WHY state belongs here
WHY this render occurred
WHY this effect is necessary
WHY this abstraction is appropriate
WHY the backend remains the trust/security boundary
```

That is the level at which React becomes a tool you can reason with
rather than a collection of syntax you copy.
