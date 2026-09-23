# Angular Frontend Development — Mastery Skill Map

## Purpose

This skill map assumes completion of:

- HTML and CSS fundamentals,
- JavaScript fundamentals,
- TypeScript fundamentals.

It prioritizes **modern Angular**. Older Angular patterns are included mainly so you can recognize and maintain existing code.

The goal is to become capable of building, explaining, debugging, testing, and maintaining a real Angular frontend that communicates with a backend REST API.

By the end, you should be able to:

- create and run an Angular application,
- understand the Angular project structure and CLI,
- build standalone components,
- use templates, bindings, directives, and modern control flow,
- manage local state with signals,
- understand dependency injection deeply,
- create services and separate application responsibilities,
- use RxJS and Observables effectively,
- communicate with REST APIs through `HttpClient`,
- build typed reactive forms and validation,
- configure routing and navigation,
- understand component lifecycle and rendering/change detection,
- organize a growing Angular application,
- understand frontend security boundaries,
- test components and services,
- recognize Jasmine/Karma-based Angular tests,
- debug Angular, TypeScript, HTTP, browser, and CORS problems,
- and connect Angular cleanly to a Spring Boot/backend architecture.

Angular should be understood as a framework built on top of:

```text
HTML
CSS
JavaScript
TypeScript
Browser APIs
HTTP
        ↓
     Angular
```

---

# 1. What Angular Is

Angular is a frontend application framework.

It provides integrated systems for:

```text
components
templates
dependency injection
routing
forms
HTTP communication
reactivity
testing support
build tooling
```

Angular is not:

```text
a backend
a database
a replacement for HTML/CSS
a replacement for JavaScript/TypeScript
```

---

# 2. Angular vs TypeScript

TypeScript is a programming language/tooling layer.

Angular is a framework that uses TypeScript heavily.

Example:

```typescript
class User {
    constructor(public name: string) {}
}
```

is TypeScript.

Example:

```typescript
@Component({
    selector: "app-user",
    templateUrl: "./user.html"
})
```

uses Angular framework behavior.

Keep language features and framework features mentally separate.

---

# 3. Angular vs React

At a high level:

```text
Angular
    integrated application framework

React
    UI library/ecosystem
```

Angular provides first-party systems for many concerns such as:

```text
DI
routing
HTTP
forms
CLI/build integration
```

React applications commonly assemble more of their architecture from separate libraries/tools.

Neither is inherently "better"; they represent different ecosystem approaches.

---

# 4. Angular's Core Mental Model

A useful simplified model:

```text
Application
   ↓
Routes
   ↓
Components
   ↓
Templates
   ↓
Services / State
   ↓
HttpClient
   ↓
Backend API
```

Dependency injection connects many of these pieces.

---

# 5. Angular Application Flow

Example full-stack request:

```text
User clicks Submit
       ↓
Angular Component
       ↓
Form validation
       ↓
Angular Service
       ↓
HttpClient
       ↓ HTTP POST
Spring Controller
       ↓
Spring Service
       ↓
Repository
       ↓
PostgreSQL
       ↓
HTTP response
       ↓
Angular Service
       ↓
Component state
       ↓
Template updates
```

You should eventually be able to explain this entire flow without notes.

---

# 6. Node.js and npm Relationship

Angular development commonly relies on Node.js tooling.

Conceptually:

```text
Node.js
   ↓
runs development/build tools

npm
   ↓
installs dependencies

Angular CLI
   ↓
creates/builds/tests/serves Angular projects
```

The browser does not run Node.js simply because the frontend was built using Node tooling.

---

# 7. Angular CLI

The Angular CLI command is:

```bash
ng
```

Important commands to recognize and eventually memorize:

```bash
ng new
ng serve
ng generate
ng generate component
ng generate service
ng generate guard
ng build
ng test
ng version
ng add
ng update
```

Common abbreviations may exist, but learn the full command first.

---

# 8. Create an Angular Project

Conceptually:

```bash
ng new my-app
```

The CLI asks/configures project options and creates the project structure.

Understand what it creates rather than treating generated files as magic.

---

# 9. Run the Development Server

```bash
ng serve
```

Often:

```bash
ng serve --open
```

The development server:

- builds the application,
- watches source files,
- rebuilds on changes,
- serves locally,
- provides development feedback.

It is not the same thing as production hosting.

---

# 10. Generate a Component

```bash
ng generate component user-card
```

This can create the component files and update project structure as needed.

Know both:

```text
CLI generation
manual creation
```

Generated code is still ordinary source code you should understand.

---

# 11. Generate a Service

```bash
ng generate service core/message
```

Services are ordinary TypeScript classes that participate in Angular's dependency-injection system when configured appropriately.

---

# 12. Build

```bash
ng build
```

Conceptually:

```text
Angular source
      ↓
compile / bundle / optimize
      ↓
browser-ready application assets
```

Detailed deployment and CI/CD belong in separate skill maps.

For Angular mastery, understand the build boundary.

---

# 13. Important Project Files

Recognize:

```text
package.json
angular.json
tsconfig.json
tsconfig.app.json
tsconfig.spec.json
src/
```

Depending on Angular version/project configuration, exact structure may vary.

---

# 14. `package.json`

Contains information such as:

```text
dependencies
development dependencies
scripts
project metadata
```

Angular packages appear here.

Examples conceptually:

```text
@angular/core
@angular/common
@angular/router
rxjs
typescript
```

---

# 15. `angular.json`

Angular workspace/build configuration may include:

```text
project configuration
build options
serve options
assets
styles
budgets
other tooling configuration
```

Do not memorize the whole schema.

Know why the file exists and how to locate relevant settings.

---

# 16. TypeScript Configuration

Angular projects use TypeScript configuration.

Recognize:

```text
tsconfig.json
tsconfig.app.json
tsconfig.spec.json
```

Understand inheritance/overrides between configuration files.

---

# 17. `src/`

Application source normally lives under:

```text
src/
```

Modern Angular project layouts can evolve, so focus on responsibilities rather than memorizing one generated tree forever.

---

# 18. Bootstrap Process

An Angular application must have a root application/component configuration that starts the framework.

Modern Angular emphasizes standalone APIs.

Conceptually:

```text
browser loads app
      ↓
Angular bootstraps root application
      ↓
root component renders
      ↓
child component tree renders
```

---

# 19. Standalone Components

Modern Angular favors standalone components.

Conceptually:

```typescript
@Component({
    selector: "app-user",
    standalone: true,
    imports: [],
    templateUrl: "./user.html",
    styleUrl: "./user.css"
})
export class UserComponent {
}
```

Depending on current Angular defaults/version, `standalone` may be implied or generated differently.

Understand the standalone architecture rather than memorizing one generated line.

---

# 20. Component Anatomy

A component combines:

```text
TypeScript class
      +
Angular metadata
      +
HTML template
      +
styles
```

Conceptually:

```text
Component class
     ↓ exposes state/behavior
Template
     ↓ displays/interacts with it
Browser DOM
```

---

# 21. `@Component`

`@Component` supplies Angular metadata.

Common metadata includes:

```text
selector
template
templateUrl
styles
styleUrl/styleUrls
imports
changeDetection
```

The decorator tells Angular how the class participates as a component.

---

# 22. Selector

Example:

```typescript
selector: "app-user-card"
```

Then the component may be used in a template as:

```html
<app-user-card></app-user-card>
```

The selector connects Angular component identity to template markup.

---

# 23. Template

Inline:

```typescript
template: `<h1>Hello</h1>`
```

External:

```typescript
templateUrl: "./user.component.html"
```

Larger components commonly use external templates.

---

# 24. Component Styles

Styles can be associated with a component.

Conceptually:

```text
component template
       +
component-specific styles
```

Angular provides style encapsulation mechanisms so component styles can be scoped in useful ways.

---

# 25. Component State

Example:

```typescript
export class CounterComponent {
    count = 0;

    increment(): void {
        this.count++;
    }
}
```

The template can bind to component state and methods.

---

# 26. Interpolation

Syntax:

```html
<p>{{ name }}</p>
```

Conceptually:

```text
component value
      ↓
template expression
      ↓
rendered text
```

Use interpolation for displaying values as text.

---

# 27. Property Binding

Syntax:

```html
<button [disabled]="submitting">
```

Conceptually:

```text
component expression
       ↓
DOM/component property
```

Square brackets indicate property binding.

---

# 28. Event Binding

Syntax:

```html
<button (click)="save()">
```

Conceptually:

```text
browser/component event
        ↓
Angular expression
        ↓
component method
```

Parentheses indicate event binding.

---

# 29. Two-Way Binding

Common syntax:

```html
<input [(ngModel)]="name">
```

The mnemonic:

```text
[()]
banana in a box
```

Two-way binding combines value flow in both directions.

For larger forms, reactive forms are often preferred.

---

# 30. Binding Summary

Memorize:

```text
{{ value }}        interpolation

[property]="value" property binding

(event)="handler()" event binding

[(ngModel)]="value" two-way binding
```

These are fundamental Angular template structures.

---

# 31. Template Expressions

Angular templates support expressions.

Example:

```html
<p>{{ user.name }}</p>
```

Keep templates readable.

Complex business logic generally belongs in TypeScript rather than deeply nested template expressions.

---

# 32. Modern Template Control Flow

Modern Angular provides built-in control-flow syntax.

Important concepts include:

```text
@if
@else
@for
@switch
```

Example conceptually:

```html
@if (user) {
    <p>{{ user.name }}</p>
} @else {
    <p>No user</p>
}
```

Prioritize this modern syntax.

---

# 33. `@for`

Conceptually:

```html
@for (message of messages; track message.id) {
    <p>{{ message.message }}</p>
}
```

Understand:

```text
collection iteration
tracking identity
DOM reuse/update
```

Tracking is important for efficient rendering.

---

# 34. `@switch`

Useful when rendering based on discrete states.

Conceptually:

```html
@switch (status) {
    @case ("loading") {
        <p>Loading...</p>
    }
    @case ("error") {
        <p>Error</p>
    }
}
```

This pairs well with TypeScript literal unions.

---

# 35. Legacy Structural Directives

Existing Angular code may use:

```text
*ngIf
*ngFor
ngSwitch
```

You should recognize and understand them.

For new code, prioritize the current Angular control-flow guidance.

---

# 36. Attribute/Class/Style Binding

Examples:

```html
<div [class.active]="selected">
```

```html
<div [style.width.px]="width">
```

Angular can bind application state into presentation.

Prefer CSS classes over excessive inline style logic when practical.

---

# 37. Component Composition

Angular applications are component trees.

Example:

```text
AppComponent
├── HeaderComponent
├── MessageListComponent
│   └── MessageCardComponent
└── MessageFormComponent
```

Components should have understandable responsibilities.

---

# 38. Parent → Child Data

A parent can provide data to a child through an input.

Modern Angular supports modern input APIs; decorator-based `@Input()` is also widespread and important to recognize.

Conceptually:

```text
Parent state
    ↓ input
Child component
```

Classic decorator form:

```typescript
@Input() userId!: string;
```

## Signal-Based Inputs (Modern)

Since Angular 17.1, inputs can be declared as signals instead of decorated properties:

```typescript
readonly userId = input.required<string>();
readonly pageSize = input<number>(10); // optional, with default
```

Reading a signal input uses call syntax, same as any other signal:

```typescript
this.userId()
```

`@Input()` and `input()` currently coexist in real Angular codebases — both are correct, and you should be comfortable reading either. The signal form integrates directly with `computed()` and `effect()` without extra glue.

---

# 39. Child → Parent Events

A child can communicate an event upward.

Conceptually:

```text
Child action
    ↓ output event
Parent handler
```

Modern output APIs and traditional `@Output()`/`EventEmitter` patterns should both be recognizable.

Classic decorator form:

```typescript
@Output() saved = new EventEmitter<string>();
```

## Signal-Based Outputs (Modern)

Since Angular 17.2, `output()` replaces `@Output()`/`EventEmitter` with a simpler function-based API:

```typescript
readonly saved = output<string>();

// emitting is unchanged in spirit:
this.saved.emit(value);
```

## `model()` — Two-Way Bindable Signals (Modern)

`model()` is for a component that both receives a value and updates it, replacing the manual `@Input()` + `@Output()` pair traditionally used for two-way binding:

```typescript
readonly value = model<string>('');
```

Reading works like any signal (`this.value()`), and the component can also write back:

```typescript
this.value.set('updated');
```

A parent can bind to it with the familiar banana-in-a-box syntax: `[(value)]="parentValue"`.

As with inputs and outputs, the decorator-based two-way pattern and `model()` both exist in the wild — recognize both, prefer `model()` in new code.

---

# 40. Input/Output Design

Use inputs for:

```text
data/configuration entering component
```

Use outputs for:

```text
events/intent leaving component
```

Avoid tightly coupling child components to unrelated parent implementation details.

---

# 41. Smart vs Presentational Responsibilities

A useful architectural idea:

```text
feature/container component
    data + orchestration

presentational component
    display + user interaction
```

Do not treat this as an Angular law.

Use it when it improves separation and reuse.

---

# 42. Content Projection

Angular can project content into a component.

Core concept:

```html
<ng-content></ng-content>
```

This allows reusable wrapper/layout components.

Think of it as:

```text
parent supplies markup
       ↓
child chooses insertion location
```

---

# 43. Template References

Template reference variable:

```html
<input #nameInput>
```

This creates a template-local reference.

Use when direct access is appropriate.

Do not replace normal state/form patterns with excessive template references.

---

# 44. View Queries

Angular can query child elements/components in a component's view.

Recognize concepts such as:

```text
viewChild
viewChildren
```

and legacy/decorator forms such as:

```text
@ViewChild
@ViewChildren
```

Use queries when actual component/element references are needed.

---

# 45. Content Queries

Angular can also query projected content.

Recognize:

```text
contentChild
contentChildren
@ContentChild
@ContentChildren
```

These are more advanced component-composition tools.

---

# 46. Signals

Signals are a major modern Angular reactivity primitive.

Basic concept:

```typescript
const count = signal(0);
```

A signal stores reactive state.

Conceptually:

```text
signal value changes
       ↓
Angular knows consumers depend on it
       ↓
relevant UI can update
```

---

# 47. Reading a Signal

Conceptually:

```typescript
count()
```

Calling the signal reads its current value.

This differs from an ordinary property:

```typescript
count
```

---

# 48. Setting a Signal

Conceptually:

```typescript
count.set(5);
```

This replaces the signal's value.

---

# 49. Updating a Signal

Conceptually:

```typescript
count.update(value => value + 1);
```

Use update logic when the new state depends on previous state.

---

# 50. `computed()`

Derived reactive state:

```typescript
const doubleCount = computed(() => count() * 2);
```

Conceptually:

```text
source signals
     ↓
computed derivation
     ↓
derived value
```

Prefer computed state instead of manually synchronizing duplicated state.

---

# 51. `effect()`

Effects run side-effect logic in response to reactive dependencies.

Conceptually:

```typescript
effect(() => {
    console.log(count());
});
```

Use effects for actual side effects.

Do not use them as a default substitute for computed state.

**Injection context pitfall:** `effect()` must be called within an Angular injection context — a constructor, a field initializer, or anywhere passed an explicit `Injector` via `{ injector: myInjector }`. Calling it elsewhere throws at runtime.

```typescript
// Wrong — no injection context, throws at runtime
ngOnInit() {
    setTimeout(() => {
        effect(() => console.log(this.count()));
    });
}

// Right — field initializer, has an injection context
readonly logCount = effect(() => console.log(this.count()));
```

---

# 52. Signal Mental Model

```text
signal
   ↓
source state

computed
   ↓
derived state

effect
   ↓
side effect
```

This distinction should be memorized.

---

# 53. Signals vs Ordinary Properties

Ordinary property:

```typescript
count = 0;
```

Signal:

```typescript
count = signal(0);
```

Signals explicitly participate in Angular's reactive dependency tracking.

Not every value must become a signal.

Use them where reactive state benefits from the model.

---

# 54. Signals vs RxJS

Very important distinction:

```text
Signals
    synchronous reactive state/value model

RxJS Observables
    streams of values/events over time
```

They overlap in some use cases but are not identical.

Angular applications often use both.

---

# 55. When Signals Fit Well

Examples:

```text
component state
UI state
derived state
selected item
loading flag
computed display values
```

---

# 56. When RxJS Fits Well

Examples:

```text
HTTP streams
router events
complex asynchronous composition
search/autocomplete
event streams
cancellation
multiple async dependencies
```

Do not replace every Observable with a signal automatically.

---

# 57. Dependency Injection

Dependency injection is central to Angular.

Without DI:

```typescript
const service = new MessageService(...);
```

The component manually creates and manages dependencies.

With Angular DI:

```text
component requests MessageService
        ↓
Angular injector resolves provider
        ↓
Angular supplies instance
```

---

# 58. Why Dependency Injection Exists

DI helps with:

```text
separation of concerns
testability
shared services
configuration
lifecycle management
replacement/mocking
looser coupling
```

The consumer says what it needs rather than manually constructing the dependency graph.

---

# 59. Service

A service is usually a TypeScript class responsible for logic that should not live directly in a component.

Examples:

```text
HTTP communication
shared state
business/frontend domain logic
logging
configuration
authentication state
```

---

# 60. `@Injectable`

Traditional/common service:

```typescript
@Injectable({
    providedIn: "root"
})
export class MessageService {
}
```

`@Injectable` participates in Angular DI metadata/configuration.

---

# 61. `providedIn: "root"`

Conceptually:

```text
service provider available from root environment
       ↓
application can inject it
       ↓
typically shared application-level instance
```

Understand provider scope rather than memorizing "root means singleton" without qualification.

---

# 62. Constructor Injection

Common pattern:

```typescript
constructor(
    private messageService: MessageService
) {}
```

TypeScript part:

```text
private property + type
```

Angular part:

```text
DI resolves MessageService
```

This distinction is important.

---

# 63. `inject()`

Modern Angular also supports:

```typescript
private messageService = inject(MessageService);
```

This requests a dependency from the current injection context.

Know both constructor injection and `inject()` because existing codebases use both.

---

# 64. Provider

A provider tells Angular how a dependency should be supplied.

Conceptually:

```text
token
  ↓
provider configuration
  ↓
value/class/factory
```

Services with root provisioning are a common simple case.

---

# 65. Injection Token

Sometimes the dependency is not represented directly by a class.

Example use cases:

```text
configuration
primitive-like values
interfaces/contracts
environment-specific values
```

Angular supports injection tokens for these cases.

---

# 66. Hierarchical DI

Angular DI can operate at multiple scopes.

Conceptually:

```text
root injector
     ↓
route/component/environment providers
     ↓
child consumers
```

Providing a service lower in the tree can create a different scoped instance.

This is important for advanced state and component isolation.

---

# 67. Service Responsibility

Avoid turning one service into:

```text
HTTP client
state store
validation engine
UI controller
logging system
everything else
```

Services should have understandable responsibilities.

---

# 68. Component vs Service

A useful default:

```text
Component
    presentation
    user interaction
    local UI state
    orchestration

Service
    reusable logic
    API access
    shared state
    domain/application operations
```

This is guidance, not an absolute law.

---

# 69. `HttpClient`

Angular provides `HttpClient` for HTTP communication.

Conceptually:

```text
Component
   ↓
Service
   ↓
HttpClient
   ↓
REST API
```

Prefer isolating API access in services rather than scattering requests throughout components.

---

# 70. Providing HTTP Support

Modern standalone Angular applications configure HTTP providers at application setup.

Understand the concept:

```text
application configuration
      ↓
HTTP provider registered
      ↓
HttpClient injectable
```

The exact API should follow the current Angular version's official guidance.

---

# 71. Typed GET

Conceptually:

```typescript
list(): Observable<Message[]> {
    return this.http.get<Message[]>("/api/messages");
}
```

Understand:

```text
Observable<Message[]>
```

as:

```text
RxJS stream expected to emit Message[]
```

The generic does not independently validate malicious/incorrect server JSON at runtime.

---

# 72. POST

Conceptually:

```typescript
create(
    request: CreateMessageRequest
): Observable<Message> {
    return this.http.post<Message>(
        "/api/messages",
        request
    );
}
```

Know:

```text
request body type
response type
Observable
```

---

# 73. REST Operations

Recognize:

```text
GET
POST
PUT
PATCH
DELETE
```

Typical Angular methods:

```text
http.get()
http.post()
http.put()
http.patch()
http.delete()
```

HTTP semantics belong partly in an HTTP/REST skill map, but Angular developers must use them correctly.

---

# 74. Query Parameters

Requests may include query parameters.

Conceptually:

```text
/api/messages?page=2&sort=createdAt
```

Angular provides APIs for request parameters.

Understand URL/query semantics independently of Angular.

---

# 75. Headers

Requests may require headers such as:

```text
Authorization
Content-Type
custom application headers
```

Do not manually add headers that the browser/HttpClient already handles correctly unless needed.

---

# 76. HTTP Errors

Network/API failures should become explicit application states.

Conceptually:

```text
request
  ↓
success → update data
  ↓
failure → error state
```

Do not leave errors only in the console.

---

# 77. Loading State

Common UI model:

```text
idle
loading
success
error
```

This can be represented using:

- signals,
- discriminated unions,
- Observable state,
- a combination.

Choose a model that prevents contradictory states.

---

# 78. Interceptors

HTTP interceptors can apply behavior across requests/responses.

Use cases:

```text
authentication headers
logging
error transformation
request metadata
```

Do not put unrelated business logic in interceptors.

---

# 79. Authentication Interceptor Concept

Conceptually:

```text
outgoing request
       ↓
interceptor
       ↓
attach credentials/token if appropriate
       ↓
backend
```

Security details depend heavily on authentication architecture.

Never treat frontend possession of a token as authorization by itself; the backend must enforce authorization.

---

# 80. Development Proxy

A development proxy can forward frontend API requests to a backend.

Example conceptual flow:

```text
Angular dev server
localhost:4200
     ↓ /api
proxy
     ↓
Spring Boot
localhost:8081
```

This can simplify development and avoid some cross-origin development issues.

---

# 81. Proxy Configuration

Conceptually:

```json
{
    "/api": {
        "target": "http://localhost:8081",
        "secure": false
    }
}
```

Exact setup depends on current Angular tooling.

Understand what the proxy does rather than memorizing one file format forever.

---

# 82. CORS

CORS is a browser security mechanism governing cross-origin requests.

Example:

```text
frontend:
http://localhost:4200

backend:
http://localhost:8081
```

Different ports mean different origins.

CORS configuration is generally enforced through server/browser interaction.

A frontend cannot simply "turn off CORS" for users.

---

# 83. Proxy vs CORS

Development proxy:

```text
browser sees same frontend origin
dev server forwards request
```

CORS:

```text
browser directly requests another origin
server must permit origin/request
```

Understand the distinction.

---

# 84. RxJS

RxJS is a reactive programming library heavily used in Angular.

Central abstraction:

```text
Observable
```

An Observable represents a stream that may emit:

```text
zero values
one value
many values
error
completion
```

over time.

---

# 85. Observable Mental Model

```text
Observable
   ↓ subscribe
Observer receives:
   next values
   error
   complete
```

Do not think of an Observable as simply "a Promise with different syntax."

---

# 86. `subscribe()`

Conceptually:

```typescript
observable.subscribe(value => {
    console.log(value);
});
```

Subscribing activates/listens to the Observable according to its behavior.

Understand ownership and lifecycle of subscriptions.

---

# 87. Observable vs Promise

Simplified comparison:

```text
Promise
    one eventual result/failure

Observable
    stream abstraction
    may emit multiple values
    supports operators/composition
    can support cancellation/unsubscription
```

HTTP Observables commonly emit once and complete, but RxJS is much broader than HTTP.

---

# 88. `pipe()`

Operators are composed through:

```typescript
observable.pipe(
    operator1(),
    operator2()
)
```

Think:

```text
source stream
    ↓
operator
    ↓
operator
    ↓
result stream
```

---

# 89. `map`

Transforms emitted values.

Conceptually:

```text
A
↓ map
B
```

Example:

```typescript
users$.pipe(
    map(users => users.length)
);
```

---

# 90. `filter`

Allows only matching emitted values through.

Conceptually:

```text
stream values
    ↓ predicate
matching values continue
```

---

# 91. `tap`

Used for side effects without intentionally transforming the emitted value.

Examples:

```text
logging
debugging
certain side-effect state updates
```

Do not use `tap` as a hidden replacement for clear data transformation.

---

# 92. `switchMap`

A critical operator for frontend async flows.

Conceptually:

```text
outer value
    ↓
start inner Observable
    ↓
new outer value arrives
    ↓
unsubscribe previous inner
    ↓
switch to newest
```

Excellent for:

```text
search
route-dependent requests
latest-request-wins behavior
```

---

# 93. `catchError`

Handles errors in an Observable pipeline.

Conceptually:

```text
source error
    ↓
catchError
    ↓
recover / transform / rethrow
```

Understand whether you are swallowing, replacing, or propagating the error.

---

# 94. `finalize`

Runs cleanup/final behavior when the Observable terminates.

Useful conceptually for:

```text
loading-state cleanup
resource cleanup
```

---

# 95. `Subject`

A Subject is both:

```text
Observable
+
imperative emission source
```

Conceptually:

```typescript
subject.next(value);
```

Subjects are powerful but can create hidden state/event flows if overused.

---

# 96. `BehaviorSubject`

A `BehaviorSubject` has a current value and emits it to new subscribers.

Historically common for Angular shared state.

Modern Angular signals may be simpler for many synchronous state use cases.

Still learn `BehaviorSubject` because existing Angular code uses it heavily.

---

# 97. Signals vs BehaviorSubject

Simplified:

```text
Signal
    synchronous reactive value
    Angular-native state primitive

BehaviorSubject
    RxJS stream with current value
    stream/operator ecosystem
```

Choose based on whether you primarily need state or stream composition.

---

# 98. Subscription Cleanup

Long-lived subscriptions can cause:

```text
memory leaks
duplicate work
unexpected callbacks
```

Modern Angular provides lifecycle-aware tools/patterns for automatic cleanup.

You should understand the principle:

```text
subscription lifetime
    should not outlive
consumer lifetime
```

---

# 99. Async Pipe

Existing Angular templates often use:

```html
{{ value$ | async }}
```

The async pipe subscribes to an Observable/Promise and manages subscription lifecycle for template usage.

Recognize the `$` suffix as a common convention for Observable variables, not a language requirement.

---

# 100. RxJS Naming Convention

Common:

```typescript
messages$
user$
results$
```

`$` often signals:

```text
this variable is an Observable
```

It is a convention, not Angular syntax.

---

# 101. Forms Overview

Angular supports two major form approaches:

```text
Template-driven forms
Reactive forms
```

Learn both.

Prioritize reactive forms for larger/complex applications.

---

# 102. Template-Driven Forms

Template-driven forms place more form configuration in the template.

Common concepts:

```text
ngModel
form directives
template validation state
```

Useful for smaller/simple forms.

Recognize existing code.

---

# 103. Reactive Forms

Reactive forms define form state/models in TypeScript.

Core concepts:

```text
FormControl
FormGroup
FormArray
validators
value/status changes
```

This approach provides explicit programmatic control.

---

# 104. `FormControl`

Conceptually:

```typescript
const name = new FormControl("");
```

A control tracks:

```text
value
validation
dirty/pristine
touched/untouched
valid/invalid
disabled/enabled
```

---

# 105. `FormGroup`

Conceptually:

```typescript
const form = new FormGroup({
    name: new FormControl(""),
    message: new FormControl("")
});
```

A group represents related controls.

---

# 106. Typed Reactive Forms

Modern Angular reactive forms support strong TypeScript typing.

This helps catch:

```text
wrong control names
wrong value types
incorrect form structure
```

Do not fall back to `any` unnecessarily.

---

# 107. Built-In Validators

Common concepts:

```text
required
minLength
maxLength
pattern
min
max
email
```

Angular validators complement HTML/browser validation.

Backend validation is still required.

---

# 108. Custom Validators

A custom validator implements application-specific validation logic.

Examples:

```text
matching passwords
forbidden value
cross-field rule
domain-specific format
```

Keep validation rules testable and understandable.

---

# 109. Form Submission

Typical flow:

```text
user submits
     ↓
prevent invalid submission
     ↓
read typed form value
     ↓
construct request DTO
     ↓
service/API call
     ↓
success/error state
```

---

# 110. Form State

Know:

```text
valid
invalid
dirty
pristine
touched
untouched
pending
disabled
```

These help decide when to display validation feedback.

---

# 111. Validation UX

Do not display every error immediately on initial page load.

Common approach:

```text
control invalid
AND
user has interacted / attempted submission
```

Then show useful feedback.

---

# 112. Frontend Validation vs Backend Validation

Frontend validation:

```text
fast UX feedback
```

Backend validation:

```text
authoritative trust boundary
```

Never trust frontend validation as security.

Users can bypass frontend code.

---

# 113. Routing

Angular Router maps URLs to application views/components.

Conceptually:

```text
URL
 ↓
router
 ↓
route match
 ↓
component/view
```

---

# 114. Route Configuration

Conceptually:

```typescript
const routes: Routes = [
    {
        path: "",
        component: HomeComponent
    },
    {
        path: "messages",
        component: MessagesComponent
    }
];
```

Modern routing may use lazy component loading.

---

# 115. Router Outlet

A routed component is rendered at a router outlet.

Concept:

```html
<router-outlet></router-outlet>
```

Think of it as the insertion location for the active route.

---

# 116. `routerLink`

Use Angular routing rather than manually forcing full-page navigation for internal SPA routes.

Conceptually:

```html
<a routerLink="/messages">Messages</a>
```

---

# 117. Route Parameters

Example URL:

```text
/users/42
```

Route:

```text
users/:id
```

The component can read the route parameter.

Always remember route parameters are runtime strings/data that require appropriate parsing/validation.

---

# 118. Query Parameters

Example:

```text
/messages?page=2&sort=newest
```

Query parameters are useful for:

```text
filters
pagination
search
sort
shareable UI state
```

---

# 119. Programmatic Navigation

Components/services can navigate using the router when navigation follows application logic.

Use declarative `routerLink` for ordinary links where possible.

---

# 120. Nested Routes

Routes can have children.

Useful for:

```text
settings sections
dashboards
feature layouts
```

Understand nested URL and outlet relationships.

---

# 121. Redirects

Routes can redirect.

Example concept:

```text
old path
   ↓
new path
```

Understand path matching so redirects do not accidentally capture unrelated routes.

---

# 122. Wildcard / 404 Route

A fallback route can handle unknown client-side paths.

Conceptually:

```text
**
```

Place fallback matching after more specific routes.

---

# 123. Route Guards

Guards can control client-side navigation behavior.

Use cases:

```text
authentication UX
unsaved changes
role-based navigation experience
```

Critical rule:

> Route guards are not backend authorization.

A malicious user can bypass frontend code.

The backend must enforce protected operations.

---

# 124. Lazy Loading

Lazy loading defers loading feature code until needed.

Conceptually:

```text
initial application
     ↓
user visits feature
     ↓
feature code loaded
```

Benefits can include smaller initial bundles.

---

# 125. Route-Level Architecture

Routes often align with features.

Example:

```text
/
 /messages
 /messages/:id
 /users
 /settings
```

Use routing structure to reinforce understandable application boundaries.

---

# 126. Lifecycle

Angular components have a lifecycle.

Conceptually:

```text
construction
    ↓
inputs/state initialized
    ↓
view rendered
    ↓
changes occur
    ↓
component destroyed
```

Lifecycle hooks let code run at specific phases.

---

# 127. Constructor

The constructor is fundamentally a TypeScript/JavaScript class constructor.

Use it primarily for:

```text
dependency setup/injection
basic field initialization
```

Do not confuse constructor execution with "the component is fully initialized in the DOM."

---

# 128. Initialization Hook

`ngOnInit` is widely used in Angular code.

Conceptually:

```text
Angular initialized component inputs/basic setup
      ↓
initialization logic
```

Modern signal-based patterns may reduce some traditional hook usage, but you must know it.

---

# 129. Input Changes

`ngOnChanges` can respond to input changes in traditional input patterns.

Understand:

```text
parent input changes
      ↓
child receives new value
      ↓
hook can react
```

Do not duplicate derived state unnecessarily.

---

# 130. View Initialization

Hooks such as:

```text
ngAfterViewInit
```

are used when logic requires initialized view children.

Use lifecycle hooks only when their timing is actually necessary.

---

# 131. Destruction

`ngOnDestroy` is used for cleanup.

Examples:

```text
manual subscriptions
timers
external listeners
resources
```

Modern Angular cleanup APIs can reduce manual lifecycle bookkeeping.

---

# 132. Lifecycle Memorization

Know conceptually:

```text
constructor
ngOnChanges
ngOnInit
ngAfterViewInit
ngOnDestroy
```

Recognize additional lifecycle hooks, but prioritize understanding timing over memorizing every hook immediately.

---

# 133. Rendering and Change Detection

Angular must know when application state changes require DOM updates.

Conceptually:

```text
state changes
    ↓
Angular tracks/checks relevant state
    ↓
template evaluation/update
    ↓
DOM reflects new state
```

Modern signals make dependencies more explicit.

---

# 134. Change Detection

Change detection is the system Angular uses to synchronize component state and rendered views.

You should understand:

- what causes updates,
- why unnecessary checks matter,
- how component boundaries matter,
- how signals interact with rendering.

---

# 135. `OnPush`

`OnPush` is a change-detection strategy used to make component update behavior more explicit/efficient.

Do not memorize it as:

> "Angular only checks when inputs change."

Modern Angular behavior includes additional triggers such as signals/events.

Learn the actual current model when implementing it.

---

# 136. Immutability and Change Detection

Immutable update patterns make state transitions easier to reason about.

Instead of hidden mutation:

```typescript
items.push(newItem);
```

you may sometimes prefer:

```typescript
items = [...items, newItem];
```

With signals:

```typescript
items.update(values => [...values, newItem]);
```

Choose patterns consistent with the state system in use.

---

# 137. Component Architecture

Avoid one giant component.

Possible decomposition:

```text
MessagesPage
├── MessageForm
├── MessageList
│   └── MessageCard
└── StatusBanner
```

Split based on responsibility, reuse, complexity, and ownership of state.

---

# 138. Feature-Oriented Structure

A scalable structure might conceptually resemble:

```text
src/app/
├── core/
├── shared/
├── features/
│   ├── messages/
│   │   ├── components/
│   │   ├── services/
│   │   ├── models/
│   │   └── routes/
│   └── users/
└── app.routes.ts
```

This is a convention, not an Angular requirement.

Do not create folders merely to imitate architecture diagrams.

---

# 139. `core`

Often used for application-wide concerns such as:

```text
global services
authentication infrastructure
HTTP infrastructure
configuration
```

Use only if it clarifies architecture.

---

# 140. `shared`

Often used for reusable:

```text
components
pipes
directives
utilities
```

Avoid turning `shared` into an unorganized dumping ground.

---

# 141. Feature Folder

Feature-oriented organization keeps related code together.

Example:

```text
messages/
    message-list
    message-form
    message.service
    message.model
    message.routes
```

This can make larger applications easier to navigate.

---

# 142. Models / DTOs

TypeScript types/interfaces can model:

```text
API response
request payload
UI state
domain data
```

Example:

```typescript
export interface Message {
    id: number;
    name: string;
    message: string;
    createdAt: string;
}
```

---

# 143. Request vs Response Types

Example:

```typescript
interface CreateMessageRequest {
    name: string;
    message: string;
    passcode: string;
}

interface Message {
    id: number;
    name: string;
    message: string;
    createdAt: string;
}
```

Different shapes should usually have different types.

---

# 144. Pipes

Pipes transform values for template display.

Conceptually:

```html
{{ createdAt | date }}
```

Use pipes for presentation transformations.

Do not hide substantial business logic in templates/pipes.

---

# 145. Built-In Pipes

Recognize common categories:

```text
date
number
currency
percent
async
case transformations
```

Check current Angular documentation for exact built-in pipe APIs.

---

# 146. Custom Pipe

A custom pipe can encapsulate reusable display transformation.

Ask first whether the logic is:

```text
presentation transformation
```

or:

```text
business/domain logic
```

Only the former is a strong pipe candidate.

---

# 147. Directives

Directives attach Angular behavior to elements/components.

Angular historically categorizes directives as:

```text
components
attribute directives
structural directives
```

Modern built-in control flow changes how you encounter some structural behaviors, but custom directives remain important.

---

# 148. Attribute Directive

An attribute directive can modify behavior/appearance of an existing element.

Use for reusable DOM behavior that does not justify a full component.

---

# 149. Component vs Directive

Component:

```text
owns a template/view
```

Directive:

```text
adds behavior to an existing host
```

A component is conceptually a specialized directive with a template.

---

# 150. Security — XSS

Cross-site scripting occurs when attacker-controlled content becomes executable script/unsafe markup.

Angular automatically escapes many template values.

Example:

```html
<p>{{ userInput }}</p>
```

is fundamentally safer than manually constructing unsafe HTML.

Do not bypass Angular's protections casually.

---

# 151. `[innerHTML]`

Rendering HTML from strings introduces additional security concerns.

Angular performs sanitization in supported contexts, but application design still matters.

Never assume arbitrary external HTML is safe.

---

# 152. Sanitization Bypass

Angular exposes APIs capable of marking values trusted.

Treat security-bypass APIs as dangerous.

The name should mentally read:

```text
"I am taking responsibility for this value being safe."
```

Never use them merely to silence a warning.

---

# 153. Frontend Secrets

Anything shipped to the browser can be inspected.

Do not put actual secrets in:

```text
Angular source
frontend environment/config files
bundled JavaScript
```

Examples of secrets that do not belong there:

```text
database password
private API credential
server signing secret
```

---

# 154. Authentication vs Authorization

Authentication:

```text
Who are you?
```

Authorization:

```text
What are you allowed to do?
```

Angular can participate in authentication UX.

The backend must enforce authorization.

---

# 155. Route Guard Security Limitation

A route guard can hide/prevent normal navigation.

It cannot secure a backend endpoint.

Required architecture:

```text
Angular guard
    ↓ improves UX/navigation

Backend authorization
    ↓ actual security boundary
```

---

# 156. CORS Is Not Authentication

CORS controls which browser origins may make/read certain cross-origin requests.

It does not prove user identity.

It does not replace authorization.

---

# 157. CSRF Concept

Cookie-based authentication can require protection against cross-site request forgery depending on architecture.

Angular/backend frameworks provide mechanisms to support secure CSRF patterns.

Detailed web authentication/security deserves its own skill map.

For Angular mastery, understand why frontend/backend coordination matters.

---

# 158. Error Handling Architecture

Errors may originate from:

```text
form validation
application logic
HTTP response
network failure
unexpected runtime exception
```

Do not handle every category identically.

Provide useful user-facing state while preserving diagnostic information for developers.

---

# 159. Global Error Handling

Angular provides mechanisms for centralized error handling.

Use global handling for truly cross-cutting concerns.

Do not use it to avoid handling expected errors where they occur.

---

# 160. Loading / Error / Success UI

A component should often explicitly model:

```text
loading
error
success
empty
```

"Nothing rendered" should not be the accidental representation of every state.

---

# 161. Testing Overview

Angular applications should test behavior at appropriate layers.

Categories:

```text
unit
component/integration
HTTP/service
end-to-end
```

This map focuses on Angular-level testing fundamentals.

Dedicated testing tools can receive deeper maps later.

---

# 162. Jasmine

Jasmine is a JavaScript testing framework historically/common in Angular projects.

Core concepts to recognize:

```text
describe
it
expect
beforeEach
spies
matchers
```

Example:

```typescript
describe("add", () => {
    it("adds two numbers", () => {
        expect(add(2, 3)).toBe(5);
    });
});
```

Learn enough Jasmine here to understand Angular tests.

A dedicated testing skill map can go much deeper.

---

# 163. Karma

Karma is a browser test runner historically used with Angular projects.

Conceptually:

```text
test files
    ↓
Karma
    ↓
browser
    ↓
test execution/results
```

Angular's current default testing tooling can change across versions.

Therefore:

- know Karma because many Angular projects use it,
- do not assume every new Angular project will use Karma forever,
- follow the current Angular project's configured test runner.

---

# 164. Jasmine vs Karma

Memorize:

```text
Jasmine
    test framework
    assertions/spec structure/spies

Karma
    test runner
    launches/runs tests in browsers
```

They solve different problems.

---

# 165. Angular TestBed

Angular provides testing utilities for creating Angular testing environments.

Core concept:

```text
TestBed
    ↓
configure Angular dependencies/components
    ↓
create component/service under test
```

---

# 166. Component Test

A component test may verify:

```text
component creation
rendered text
button interaction
input/output behavior
signal/state update
form behavior
```

Test observable behavior rather than private implementation details whenever practical.

---

# 167. Service Test

Service tests may verify:

```text
business logic
state behavior
dependency interaction
HTTP requests
```

Services are often easier to unit test because they do not require DOM rendering.

---

# 168. Dependency Injection in Tests

Testing can replace real dependencies with:

```text
fake
stub
spy
mock provider
```

This is one major benefit of DI.

Conceptually:

```text
production:
Component → RealService

test:
Component → FakeService
```

---

# 169. Spies

Jasmine spies can observe or replace function behavior.

Use cases:

```text
was method called?
with what arguments?
return controlled value
prevent real side effect
```

Do not over-mock every internal call.

---

# 170. HTTP Testing

Angular provides HTTP testing facilities so service tests can verify requests without calling a real backend.

Conceptually:

```text
service calls HttpClient
       ↓
test intercepts request
       ↓
assert URL/method/body
       ↓
return fake response
```

---

# 171. Form Testing

Test:

```text
initial form state
validation
invalid input
valid input
submission behavior
disabled state
error display
```

---

# 172. Signal Testing

Signals are synchronous for many ordinary state operations.

Test:

```text
initial value
update
computed result
effect/side-effect where appropriate
```

Avoid testing Angular implementation internals.

---

# 173. E2E Testing

End-to-end testing treats the application more like a user would.

Conceptually:

```text
browser
 ↓
frontend
 ↓
backend
 ↓
possibly database
```

Specific E2E frameworks deserve separate coverage.

---

# 174. Debugging Layers

Angular debugging requires distinguishing:

```text
HTML/CSS problem
JavaScript runtime problem
TypeScript compile problem
Angular framework problem
RxJS problem
HTTP problem
backend problem
CORS/network problem
```

Do not assume every failure is "an Angular error."

---

# 175. Browser DevTools

Use:

```text
Elements
Console
Network
Sources
Application/Storage
```

For frontend debugging.

The Network tab is essential for API integration.

---

# 176. Network Debugging

For a failed request inspect:

```text
request URL
method
status code
request headers
request body
response headers
response body
timing
CORS messages
```

This often tells you whether the bug is frontend or backend.

---

# 177. Angular Error Messages

Read the first meaningful framework error and its stack/context.

Ask:

```text
Which component/service?
Which dependency?
Which template expression?
Which provider?
Which route?
```

Do not focus only on the last generic build-failure line.

---

# 178. Dependency Injection Error

Common conceptual error:

```text
No provider for X
```

Investigate:

```text
Is X injectable?
Is a provider registered?
Is the provider in the correct scope?
Am I injecting the correct token?
```

---

# 179. Template Error

Possible causes:

```text
property not defined
missing import
wrong binding
incorrect control-flow syntax
wrong component selector
type mismatch
```

Modern Angular template type checking can catch many problems before runtime.

---

# 180. Unknown Component/Directive/Pipe

Investigate whether the standalone component imported the dependency it uses.

Conceptually:

```typescript
@Component({
    imports: [
        SomeComponent,
        SomePipe
    ]
})
```

Standalone components explicitly declare template dependencies.

---

# 181. HTTP 404

Usually means:

```text
wrong URL
wrong backend route
proxy mismatch
backend not running
```

It is not usually an Angular rendering problem.

---

# 182. HTTP 400

Often:

```text
request shape invalid
validation failed
missing required field
wrong parameter format
```

Inspect response body and request payload.

---

# 183. HTTP 401 / 403

Conceptually:

```text
401
    authentication missing/invalid

403
    request understood but not authorized
```

Exact backend behavior can vary.

Investigate authentication state and server rules.

---

# 184. HTTP 500

Server-side failure.

Angular should:

```text
handle/display useful error state
```

but the root cause generally belongs in backend logs/debugging.

---

# 185. CORS Error

If browser console reports CORS:

Check:

```text
frontend origin
backend origin
backend CORS policy
request method
preflight
headers
credentials
proxy configuration
```

Do not try random frontend changes without understanding the origin relationship.

---

# 186. Observable Does Nothing

Possible cause:

```text
Observable was created but never consumed/subscribed
```

Also inspect:

```text
template async consumption
signal conversion
operator chain
errors
```

Understand whether the stream is cold/hot and who owns execution.

---

# 187. Duplicate HTTP Requests

Possible causes:

```text
multiple subscriptions
repeated lifecycle execution
template re-subscription patterns
multiple consumers
incorrect state architecture
```

Do not automatically "fix" by caching until you understand why duplicates occur.

---

# 188. Memory Leak

Investigate:

```text
long-lived manual subscriptions
timers
event listeners
subjects
third-party resources
```

Use lifecycle-aware cleanup patterns.

---

# 189. Angular DevTools

Angular-specific browser tooling can help inspect:

```text
component tree
component state
performance/change detection
```

Use it alongside normal browser DevTools rather than instead of them.

---

# 190. Performance Basics

Angular performance usually starts with architecture, not micro-optimization.

Consider:

```text
unnecessary rendering
large lists
expensive template computations
duplicate requests
oversized bundles
poor state boundaries
```

Measure before optimizing.

---

# 191. Template Performance

Avoid repeatedly running expensive computations directly from templates.

Prefer:

```text
computed state
precomputed values
appropriate pipes
clear state transformations
```

when computation is nontrivial.

---

# 192. List Tracking

When rendering lists, track stable identity where possible.

Conceptually:

```text
message.id
user.id
product.id
```

Stable tracking helps Angular reuse DOM rather than unnecessarily recreating it.

---

# 193. Lazy Loading and Performance

Lazy route/feature loading can reduce initial application work.

Do not split every tiny component into a lazy bundle.

Use meaningful feature boundaries.

---

# 194. Accessibility

Angular does not replace semantic HTML.

Continue using:

```text
button
label
input
nav
main
header
footer
proper headings
```

Angular bindings should enhance semantic HTML, not replace it.

---

# 195. Dynamic UI Accessibility

When creating:

```text
dialogs
menus
tabs
alerts
dynamic forms
```

consider:

```text
keyboard behavior
focus management
ARIA where appropriate
screen-reader announcements
```

Accessibility deserves its own deeper skill map later.

---

# 196. Legacy Angular — NgModules

Older/existing Angular applications commonly use:

```typescript
@NgModule({
    declarations: [],
    imports: [],
    providers: [],
    bootstrap: []
})
```

Understand what NgModules organize.

For modern Angular, prioritize standalone architecture.

---

# 197. NgModule Mental Model

Historically:

```text
declarations
    components/directives/pipes owned by module

imports
    dependencies

providers
    injectable dependencies

bootstrap
    root startup component
```

You should be able to read this in legacy code.

---

# 198. Standalone vs NgModule

Simplified:

```text
Modern standalone:
component directly imports template dependencies

NgModule architecture:
module groups declarations/imports/providers
```

Do not assume standalone means "no modules exist anywhere in JavaScript."

It refers specifically to Angular's component/module architecture.

---

# 199. Legacy `*ngIf` / `*ngFor`

Recognize:

```html
<div *ngIf="user">
```

```html
<li *ngFor="let item of items">
```

Understand them when maintaining existing applications.

Prioritize modern built-in control flow for new code.

---

# 200. Architecture — Separation of Concerns

A healthy Angular feature may separate:

```text
Component
    UI state and interaction

Service
    API/application logic

Model/type
    data shape

Route
    navigation boundary

Template
    presentation

Styles
    presentation rules
```

Avoid arbitrary separation merely to create more files.

---

# 201. Avoid Fat Components

Warning signs:

```text
hundreds of unrelated lines
many API calls
complex data transformation
global state
validation rules
navigation rules
business logic
all in one component
```

Move responsibilities to appropriate services/helpers/components.

---

# 202. Avoid God Services

The opposite problem:

```text
one service manages everything
```

Split services by coherent responsibility.

---

# 203. Shared State

Possible approaches include:

```text
service + signals
service + RxJS
route state
component inputs/outputs
specialized state libraries
```

Start with the simplest architecture that satisfies requirements.

Do not introduce a state-management library automatically.

---

# 204. Service + Signal State

Conceptually:

```typescript
@Injectable({ providedIn: "root" })
export class UserState {
    private readonly usersState = signal<User[]>([]);

    readonly users = this.usersState.asReadonly();
}
```

This can provide simple shared synchronous application state.

Exact APIs should follow the Angular version in use.

---

# 205. State Ownership

Ask:

```text
Who owns this state?
Who needs to read it?
Who may modify it?
How long should it live?
```

These questions matter more than picking a fashionable state tool.

---

# 206. Full-Stack Angular/Spring Architecture

Recommended mental model:

```text
Angular
────────────────────────

Template
   ↕
Component
   ↓
Service
   ↓
HttpClient

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

Each layer should have a clear responsibility.

---

# 207. Angular Component in Full Stack

Responsible for:

```text
rendering
form interaction
local UI state
calling frontend services
responding to service results
```

Usually not responsible for:

```text
SQL
backend authorization
database logic
direct repository access
```

---

# 208. Angular Service in Full Stack

Responsible for frontend-side concerns such as:

```text
calling REST endpoints
sharing state
transforming frontend data
coordinating application operations
```

Do not confuse an Angular service with a Spring service.

They share a name but exist on opposite sides of the HTTP boundary.

---

# 209. Angular Service vs Spring Service

```text
Angular service
    browser/frontend process

Spring service
    backend/server process
```

They cannot directly call each other's methods.

Communication crosses HTTP/API boundaries.

---

# 210. DTO Alignment

Frontend:

```typescript
interface CreateMessageRequest {
    name: string;
    message: string;
    passcode: string;
}
```

Backend:

```text
CreateMessageRequest DTO
```

The shapes must agree at the HTTP boundary.

They are separate definitions in separate runtimes unless generated/shared through deliberate tooling.

---

# 211. Date/Time Handling

Backend timestamps arrive over HTTP as serialized values, commonly strings.

TypeScript:

```typescript
createdAt: string;
```

may be more truthful at the transport boundary than pretending JSON directly contains a JavaScript `Date`.

Convert intentionally when needed.

---

# 212. Environment / Configuration Concept

Frontend applications need environment-dependent configuration such as:

```text
API base URL
feature flags
public configuration
```

Remember:

> frontend configuration is visible to users.

Do not store secrets in it.

---

# 213. Development vs Production Configuration

Conceptually:

```text
development
    local API / debugging

production
    deployed API / optimized build
```

Angular build tooling supports configuration strategies.

Detailed deployment architecture belongs in a deployment/CI/CD skill map.

---

# 214. Build Boundary

For Angular mastery, understand:

```text
source code
    ↓
Angular build tooling
    ↓
HTML/CSS/JavaScript/assets
    ↓
web server
    ↓
browser
```

Stop there for this map.

Server infrastructure, CDN design, Docker deployment, and CI/CD deserve separate maps.

---

# 215. Common CLI Memorization Set

Aim to know these without looking them up:

```bash
ng new
ng serve
ng generate component
ng generate service
ng generate guard
ng build
ng test
ng version
ng add
ng update
```

Also know ordinary npm commands such as:

```bash
npm install
npm run
npm update
```

but npm itself deserves a separate tooling skill map.

---

# 216. Core Angular Vocabulary to Memorize

```text
component
template
selector
binding
interpolation
input
output
signal
computed
effect
service
dependency injection
provider
injector
injection token
Observable
Subject
pipe
operator
HttpClient
reactive form
FormControl
FormGroup
validator
route
guard
router outlet
lifecycle
change detection
standalone component
NgModule
```

---

# 217. Syntax to Recognize from Memory

```text
{{ value }}

[property]="value"

(event)="handler()"

[(ngModel)]="value"

@if (...)

@for (...)

signal(...)

computed(...)

effect(...)

inject(...)

Observable<T>

.pipe(...)

.subscribe(...)

routerLink

<router-outlet>
```

---

# 218. Interview Questions — Angular Fundamentals

Be able to answer:

1. What is Angular?
2. How is Angular different from TypeScript?
3. What is a component?
4. What is a template?
5. What does `@Component` do?
6. What is a selector?
7. What is a standalone component?
8. What is interpolation?
9. What is property binding?
10. What is event binding?
11. What is two-way binding?
12. What is modern Angular control flow?
13. What is component composition?
14. What is content projection?
15. What is a directive?
16. What is a pipe?

---

# 219. Interview Questions — Components

1. How does a parent pass data to a child?
2. How does a child notify a parent?
3. What is an input?
4. What is an output?
5. What is a template reference variable?
6. What is a view query?
7. What is content projection?
8. When should you split a component?
9. What logic belongs in a component?
10. What logic should usually move to a service?

---

# 220. Interview Questions — Signals

1. What is a signal?
2. How do you read a signal?
3. What does `.set()` do?
4. What does `.update()` do?
5. What is `computed()`?
6. What is `effect()`?
7. Computed vs effect?
8. Signal vs ordinary property?
9. Signal vs Observable?
10. Signal vs BehaviorSubject?
11. When would you use a signal for shared state?

---

# 221. Interview Questions — Dependency Injection

1. What is dependency injection?
2. Why does Angular use DI?
3. What is a service?
4. What does `@Injectable` do?
5. What does `providedIn: "root"` mean?
6. What is a provider?
7. What is an injector?
8. What is an injection token?
9. Constructor injection vs `inject()`?
10. What is hierarchical dependency injection?
11. How does DI improve testing?
12. What causes a "No provider" error?

---

# 222. Interview Questions — RxJS

1. What is RxJS?
2. What is an Observable?
3. Observable vs Promise?
4. What does `subscribe()` do?
5. What does `pipe()` do?
6. What does `map()` do?
7. What does `filter()` do?
8. What does `tap()` do?
9. What does `switchMap()` do?
10. What does `catchError()` do?
11. What does `finalize()` do?
12. What is a Subject?
13. What is a BehaviorSubject?
14. Why can subscriptions leak?
15. What does the async pipe do?
16. Why might duplicate subscriptions cause duplicate HTTP requests?

---

# 223. Interview Questions — HTTP

1. What is `HttpClient`?
2. Why put HTTP calls in services?
3. How do you type an HTTP response?
4. Does `get<User>()` validate runtime JSON?
5. GET vs POST?
6. PUT vs PATCH?
7. What are query parameters?
8. What are headers?
9. What is an HTTP interceptor?
10. What is a development proxy?
11. What is CORS?
12. Proxy vs CORS?
13. How do you handle HTTP errors?
14. What would you inspect in the Network tab?

---

# 224. Interview Questions — Forms

1. Template-driven vs reactive forms?
2. What is a `FormControl`?
3. What is a `FormGroup`?
4. What is a `FormArray`?
5. What is a validator?
6. What is a custom validator?
7. What does `dirty` mean?
8. What does `touched` mean?
9. Why use typed reactive forms?
10. Why must the backend validate even if Angular already validates?

---

# 225. Interview Questions — Routing

1. What does Angular Router do?
2. What is a route?
3. What is `router-outlet`?
4. What is `routerLink`?
5. What is a route parameter?
6. What is a query parameter?
7. What is programmatic navigation?
8. What are nested routes?
9. What is a wildcard route?
10. What is a route guard?
11. Why is a route guard not security?
12. What is lazy loading?

---

# 226. Interview Questions — Lifecycle / Rendering

1. What is the Angular component lifecycle?
2. Constructor vs `ngOnInit`?
3. What is `ngOnChanges`?
4. What is `ngAfterViewInit`?
5. What is `ngOnDestroy`?
6. Why does subscription cleanup matter?
7. What is change detection?
8. What is `OnPush`?
9. How do signals relate to rendering?
10. Why can expensive template functions hurt performance?

---

# 227. Interview Questions — Architecture

1. How would you structure a medium Angular app?
2. What is feature-oriented organization?
3. What belongs in `core`?
4. What belongs in `shared`?
5. What is a "fat component"?
6. What is a "god service"?
7. Where should shared state live?
8. When would you use signals vs RxJS for state?
9. How does an Angular service differ from a Spring service?
10. How does Angular communicate with Spring Boot?

---

# 228. Interview Questions — Security

1. What is XSS?
2. How does Angular help prevent XSS?
3. Why is arbitrary `innerHTML` risky?
4. Why are sanitization bypass APIs dangerous?
5. Can frontend environment files contain secrets?
6. Authentication vs authorization?
7. Why don't route guards secure APIs?
8. Is CORS authentication?
9. Why must backend authorization exist?
10. Why must backend validation exist?

---

# 229. Interview Questions — Testing

1. Why test Angular applications?
2. What is Jasmine?
3. What is Karma?
4. Jasmine vs Karma?
5. What is `TestBed`?
6. What is a unit test?
7. What is a component test?
8. How would you test a service?
9. What is a spy?
10. How does DI help testing?
11. How can HTTP calls be tested without a real backend?
12. What should a reactive-form test verify?
13. What is E2E testing?

---

# 230. Interview Questions — Legacy Recognition

1. What is an NgModule?
2. Standalone component vs NgModule?
3. What are `declarations`?
4. What are module `imports`?
5. What are module `providers`?
6. What is `*ngIf`?
7. What is `*ngFor`?
8. Why might a modern Angular developer still need to understand these?

---

# 231. Level 1 — Angular Tooling

You can:

```text
create a project
run dev server
generate components/services
understand project files
build the app
```

---

# 232. Level 2 — Components and Templates

You can use:

```text
standalone components
interpolation
property binding
event binding
modern control flow
component composition
inputs/outputs
```

---

# 233. Level 3 — Signals and State

You can use:

```text
signal
set
update
computed
effect
```

and can decide whether state belongs:

```text
locally
in parent
in service
in RxJS stream
```

---

# 234. Level 4 — Dependency Injection

You understand:

```text
service
provider
injector
root provisioning
inject()
constructor injection
tokens
provider scopes
```

You can diagnose DI errors.

---

# 235. Level 5 — RxJS

You understand:

```text
Observable
subscribe
pipe
map
filter
tap
switchMap
catchError
finalize
Subject
BehaviorSubject
cleanup
async pipe
```

---

# 236. Level 6 — Forms and HTTP

You can:

```text
build typed reactive forms
validate input
create request DTOs
call REST APIs
handle loading/error/success
debug requests
```

---

# 237. Level 7 — Routing

You can implement:

```text
routes
links
parameters
query parameters
nested routes
guards
lazy loading
404 handling
```

---

# 238. Level 8 — Lifecycle and Rendering

You understand:

```text
component lifecycle
cleanup
change detection
OnPush
signal-driven rendering
list tracking
```

---

# 239. Level 9 — Architecture and Security

You can:

```text
organize features
separate responsibilities
manage shared state
avoid frontend secrets
explain frontend/backend trust boundaries
```

---

# 240. Level 10 — Testing and Debugging

You can:

```text
write basic Angular/Jasmine tests
understand Karma's role
test components/services/forms/HTTP
use DevTools
diagnose DI/template/RxJS/HTTP/CORS problems
```

---

# 241. Practical Exercise — First Component

Build:

```text
CounterComponent
```

Requirements:

- display count,
- increment button,
- decrement button,
- reset button,
- use a signal,
- create a computed value such as doubled count.

Explain every binding.

---

# 242. Practical Exercise — Component Communication

Build:

```text
UserListComponent
    ↓
UserCardComponent
```

Parent passes a user.

Child emits:

```text
selected
delete requested
```

Parent handles the events.

---

# 243. Practical Exercise — Modern Control Flow

Render:

```text
loading
empty
list
error
```

using:

```text
@if
@for
@switch
```

Track list items by stable ID.

---

# 244. Practical Exercise — Service and DI

Create:

```text
NotificationService
```

Inject it into two components.

Observe shared root-level service behavior.

Then provide it at a lower component scope and observe instance differences.

---

# 245. Practical Exercise — Signals Service

Create a simple:

```text
TodoStateService
```

with:

```text
private writable signal
public readonly state
computed completed count
add/remove/toggle methods
```

Use it from multiple components.

---

# 246. Practical Exercise — RxJS

Create a search input.

Flow:

```text
user types
    ↓
input stream
    ↓
debounce concept
    ↓
distinct values
    ↓
switchMap
    ↓
fake/API search
    ↓
results
```

Learn the relevant operators while building the behavior.

---

# 247. Practical Exercise — Reactive Form

Build:

```text
name
email
message
```

Requirements:

- typed controls,
- required validation,
- email validation,
- minimum/maximum length,
- disabled submit while invalid/submitting,
- useful validation messages.

---

# 248. Practical Exercise — Router

Create:

```text
/
 /messages
 /messages/:id
 /about
 /unknown → 404
```

Use:

```text
routerLink
route parameter
query parameter
programmatic navigation
lazy route
```

---

# 249. Practical Exercise — HTTP Service

Create:

```typescript
MessageService
```

with:

```text
list()
get(id)
create()
update()
delete()
```

Use typed request/response models.

Handle API errors explicitly.

---

# 250. Practical Exercise — Interceptor

Create a development/demo interceptor that adds a harmless custom request header or logs request timing.

Explain:

```text
why interceptor
why not component
why not duplicate code in every service
```

Do not place actual secrets in the frontend.

---

# 251. Practical Exercise — Testing

Write tests for:

```text
pure utility
service
component
reactive form
HTTP service
```

Use Jasmine concepts:

```text
describe
it
expect
beforeEach
spy
```

Understand how the configured Angular test runner executes them.

---

# 252. Practical Exercise — Debugging Lab

Intentionally create:

```text
missing provider
wrong component import
template property typo
invalid form control name
HTTP 404
HTTP 400
CORS failure
duplicate subscription
uncleaned subscription
```

Diagnose each with the correct tool.

---

# 253. Capstone — Angular Guestbook Frontend

Build an Angular frontend for a REST guestbook/message API.

Suggested structure:

```text
src/app/
├── core/
│   └── services/
│       └── message.service.ts
├── features/
│   └── messages/
│       ├── message-page/
│       ├── message-form/
│       ├── message-list/
│       ├── message-card/
│       └── models/
├── shared/
│   └── status-banner/
├── app.routes.ts
└── app.config.ts
```

Adapt structure if a simpler organization is clearer.

---

# 254. Capstone — Data Models

Define:

```typescript
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

Add update types if your backend supports them.

---

# 255. Capstone — Service

Implement:

```text
list messages
create message
error handling
typed HTTP
```

Optional expansion:

```text
get one
update
delete
```

Keep API logic outside presentation components.

---

# 256. Capstone — State

Model:

```text
loading
messages
submission state
error
success
```

Use signals where appropriate.

Use RxJS where asynchronous stream composition is appropriate.

Be able to justify which you chose.

---

# 257. Capstone — Form

Use reactive forms.

Requirements:

```text
name validation
message validation
passcode validation
submit state
server error display
reset after success
```

Do not treat the passcode as a real secure authentication architecture; it is only a learning feature unless the backend design provides actual security.

---

# 258. Capstone — Routing

Create:

```text
/messages
/about
```

Optional:

```text
/messages/:id
```

Use a wildcard 404 route.

Lazy-load at least one feature if useful.

---

# 259. Capstone — Full Stack

Connect:

```text
Angular dev server
        ↓
proxy / HTTP
        ↓
Spring Boot
        ↓
PostgreSQL
```

Be able to debug each boundary independently.

---

# 260. Capstone — Tests

Test:

```text
MessageService
MessageForm
MessageList
loading/error states
HTTP request shape
validation
```

Use the project's configured Angular test tooling.

Understand Jasmine/Karma if the project uses them.

---

# 261. Capstone — Explanation Exercise

Without looking at code, explain:

```text
1. User enters a message.
2. Reactive form stores/validates values.
3. Component receives submit event.
4. Component constructs request DTO.
5. Component calls MessageService.
6. MessageService uses HttpClient.
7. Browser sends HTTP request.
8. Spring Controller receives it.
9. Spring Service processes it.
10. Repository saves it.
11. Backend returns response.
12. Angular Observable emits response.
13. Component/service updates signal/state.
14. Angular updates the template.
```

If you can explain this confidently, the frontend/backend architecture is becoming integrated knowledge.

---

# 262. Final Mastery Standard

Given a backend REST API, you should be able to create an Angular application from scratch.

You should be able to:

```text
install/use Angular CLI
understand project configuration
create standalone components
write templates
use bindings
use modern control flow
compose components
use inputs/outputs
use signals
derive state with computed
use effects appropriately
create/inject services
understand providers/injectors
use HttpClient
use RxJS
build reactive forms
validate forms
configure routes
handle route parameters
use guards appropriately
lazy load features
understand lifecycle
understand change detection
organize features
handle errors
apply frontend security principles
test important behavior
debug across application layers
build browser-ready output
```

You should understand enough legacy Angular to read:

```text
NgModules
*ngIf
*ngFor
decorator-based input/output/query patterns
BehaviorSubject-heavy state patterns
Jasmine/Karma test setups
```

without making legacy patterns your default for new code.

You should be able to distinguish:

```text
TypeScript feature
Angular feature
RxJS feature
browser feature
HTTP behavior
backend behavior
```

You should be able to explain dependency injection as an actual resolution process rather than only saying:

> "Angular injects the service."

You should be able to explain why:

```text
Component
Service
HttpClient
Backend Controller
Backend Service
Repository
Database
```

are separate layers and what responsibility belongs in each.

The end goal is:

> **Build Angular applications from understanding rather than copy/paste: know how components render, how state flows, how dependencies are resolved, how asynchronous data moves through RxJS and HTTP, how forms and routes are modeled, how Angular interacts with the browser and backend, and how to diagnose problems when any layer fails.**

---

# 263. Recommended Learning Order

```text
HTML / CSS
     ↓
JavaScript
     ↓
TypeScript
     ↓
Angular CLI/project structure
     ↓
standalone components
     ↓
templates/binding
     ↓
modern control flow
     ↓
component communication
     ↓
signals
     ↓
dependency injection
     ↓
services
     ↓
RxJS fundamentals
     ↓
HttpClient
     ↓
reactive forms
     ↓
routing
     ↓
lifecycle
     ↓
change detection
     ↓
architecture/shared state
     ↓
security boundaries
     ↓
testing
     ↓
full-stack integration
```

---

# 264. Follow-On Skill Maps

Angular mastery naturally connects to separate deeper maps for:

```text
RxJS
Frontend Testing
Jasmine
Karma / browser test runners
End-to-End Testing
Web Accessibility
Frontend Architecture
State Management
Web Security / Authentication
HTTP / REST
npm / Node tooling
Build tooling
Deployment
Docker
CI/CD
Cloud hosting
Performance optimization
```

Recommended distinction:

```text
Angular skill map
    teaches enough Jasmine/Karma to test Angular

Testing skill maps
    teach testing theory/tools deeply
```

Likewise:

```text
Angular skill map
    teaches what `ng build` produces

Deployment / CI/CD maps
    teach how that output reaches production
```

This keeps Angular focused on Angular while preserving the full-stack progression.
