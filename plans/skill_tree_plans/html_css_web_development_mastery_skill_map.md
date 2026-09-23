# HTML & CSS Web Development — Mastery Skill Map

## Purpose

This skill map targets the **foundational browser-facing knowledge needed by a future full-stack developer**, with HTML and CSS as the primary focus and a deliberately limited amount of JavaScript where it helps make forms and pages interactive.

The goal is to become able to:

- create a complete web page from memory,
- structure content correctly with semantic HTML,
- style and lay out pages predictably with CSS,
- build responsive interfaces,
- create accessible forms,
- understand how the browser interprets HTML and CSS,
- debug pages with browser developer tools,
- test websites locally without needing a production deployment,
- understand the basic path from a local page to a real hosted website,
- understand HTTP/HTTPS and frontend/backend communication at the level needed to progress into full-stack development,
- use small amounts of JavaScript for DOM interaction, events, forms, validation, and basic HTTP requests,
- and explain HTML/CSS/browser fundamentals confidently in a technical interview.

This document is **not a complete JavaScript curriculum**. JavaScript appears only where it connects HTML/CSS to browser interaction and future backend communication.

## Recommended Work-Through Path

The body of this skill map is long, so here is the actual order it follows, section by section:

```text
HTML structure and syntax
            ↓
semantic HTML, tables, and forms
            ↓
accessibility fundamentals (immediately after forms, not deferred to the end)
            ↓
CSS fundamentals: syntax, selectors, cascade, specificity, box model
            ↓
layout: Flexbox and Grid
            ↓
CSS frameworks: Tailwind (utility-first) and Bootstrap (component-based)
            ↓
responsive design: media queries, responsive images, custom properties
            ↓
browser rendering model and DevTools
            ↓
web/hosting fundamentals: paths, local servers, HTTP/HTTPS, ports, DNS, hosting
            ↓
light JavaScript: DOM selection, events, form handling, fetch(), CORS
            ↓
common problems, debugging workflow, structures to memorize
            ↓
practical exercises (including rebuilding a layout with Tailwind and Bootstrap)
            ↓
mastery levels, interview-focused questions, final mastery standard
```

Accessibility is deliberately placed right after forms, not bundled in with responsive design at the end — accessible forms are a forms concern first. Web/Hosting fundamentals and light JavaScript are full sections in their own right, not an afterthought; both are required before the "ready to progress into full-stack development" milestone near the end of this document.

---

# 1. Structures and Syntax Worth Memorizing

A developer working with HTML and CSS should be able to produce these structures without repeatedly looking them up.

## Minimal HTML Document

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Website</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <h1>Hello, world!</h1>
</body>
</html>
```

Understand every line.

---

## Core Semantic Page Structure

```html
<body>
    <header>
        <nav>
        </nav>
    </header>

    <main>
        <section>
            <h1>Heading</h1>
            <p>Content</p>
        </section>
    </main>

    <footer>
    </footer>
</body>
```

Know the purpose of:

```text
header
nav
main
section
article
aside
footer
```

---

## Basic CSS Rule

```css
selector {
    property: value;
}
```

Example:

```css
p {
    color: black;
    font-size: 1rem;
}
```

---

## Class Selector

HTML:

```html
<p class="message">Hello</p>
```

CSS:

```css
.message {
    font-weight: bold;
}
```

---

## ID Selector

HTML:

```html
<section id="about">
</section>
```

CSS:

```css
#about {
    padding: 2rem;
}
```

Know that IDs should be unique within a document and that classes are usually preferable for reusable styling.

---

## Basic Flexbox

```css
.container {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 1rem;
}
```

---

## Basic Grid

```css
.container {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 1rem;
}
```

---

## Responsive Media Query

```css
@media (max-width: 768px) {
    .container {
        grid-template-columns: 1fr;
    }
}
```

Also understand mobile-first forms such as:

```css
@media (min-width: 768px) {
    /* larger-screen changes */
}
```

---

## Basic Form

```html
<form action="/submit" method="post">
    <label for="name">Name</label>
    <input
        id="name"
        name="name"
        type="text"
        required
    >

    <button type="submit">Submit</button>
</form>
```

Understand every attribute.

---

## Basic JavaScript Event Listener

```html
<script src="script.js" defer></script>
```

```javascript
const button = document.querySelector("#my-button");

button.addEventListener("click", () => {
    console.log("Clicked");
});
```

---

## Basic Form Interception

```javascript
const form = document.querySelector("#contact-form");

form.addEventListener("submit", (event) => {
    event.preventDefault();

    const formData = new FormData(form);

    console.log(formData.get("name"));
});
```

---

## Basic HTTP Request with `fetch()`

```javascript
fetch("/api/messages")
    .then(response => response.json())
    .then(data => {
        console.log(data);
    });
```

You should eventually recognize this structure, but deeper asynchronous JavaScript belongs in a dedicated JavaScript skill map.

---

# 2. What HTML Is

HTML stands for:

**HyperText Markup Language**

HTML describes the **structure and meaning of web content**.

Conceptually:

```text
HTML
 ↓
document structure
 ↓
browser parses document
 ↓
DOM
 ↓
rendered page
```

HTML is not primarily responsible for:

- visual styling,
- application business logic,
- database operations.

Those responsibilities belong to other technologies.

---

# 3. What CSS Is

CSS stands for:

**Cascading Style Sheets**

CSS controls the presentation and layout of documents such as HTML.

Conceptually:

```text
HTML
 ↓
structure/content

CSS
 ↓
presentation/layout
```

CSS controls areas such as:

- colors,
- fonts,
- spacing,
- sizing,
- borders,
- backgrounds,
- layout,
- responsiveness,
- transitions,
- animations.

---

# 4. What JavaScript Contributes

JavaScript adds programmable browser behavior.

Conceptually:

```text
HTML → structure
CSS  → presentation
JS   → behavior
```

Examples:

- react to clicks,
- read form values,
- modify page content,
- show validation messages,
- send HTTP requests,
- update the page without a full reload.

This document uses JavaScript only to connect HTML/CSS concepts to practical browser interaction.

---

# 5. HTML Elements

An element commonly has:

```html
<tag>content</tag>
```

Example:

```html
<p>Hello</p>
```

Understand:

- opening tag,
- content,
- closing tag,
- element.

Some elements are void elements and do not contain closing tags in HTML syntax:

```html
<img>
<input>
<br>
<hr>
<meta>
<link>
```

---

# 6. HTML Attributes

Attributes provide additional information or configuration.

Example:

```html
<a href="https://example.com">Example</a>
```

Here:

```text
a                   element
href                attribute
https://example.com attribute value
Example             content
```

Other common attributes:

```text
class
id
name
type
value
src
alt
href
title
required
disabled
checked
placeholder
autocomplete
```

---

# 7. HTML Nesting

Elements can contain other elements.

```html
<section>
    <h2>About</h2>
    <p>This is the about section.</p>
</section>
```

Understand:

- parent,
- child,
- ancestor,
- descendant,
- sibling.

These relationships become important for CSS selectors and the DOM.

---

# 8. Document Structure

Know:

```html
<!DOCTYPE html>
<html>
<head>
</head>
<body>
</body>
</html>
```

## `<!DOCTYPE html>`

Declares modern HTML document mode.

## `<html>`

Root HTML element.

## `<head>`

Contains document metadata and linked resources.

## `<body>`

Contains content presented as the page.

---

# 9. Important `<head>` Elements

Know:

```html
<title></title>
<meta>
<link>
```

Common structure:

```html
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Title</title>
    <link rel="stylesheet" href="styles.css">
</head>
```

Understand:

- character encoding,
- viewport configuration,
- browser/tab title,
- stylesheet linking.

Later learn additional metadata for:

- search engines,
- social sharing,
- icons,
- application behavior.

---

# 10. Headings

HTML provides:

```html
<h1>
<h2>
<h3>
<h4>
<h5>
<h6>
```

Headings describe content hierarchy.

Do not choose heading levels merely because of their default visual size.

Example:

```html
<h1>Programming Languages</h1>

<h2>Java</h2>
<h3>Collections</h3>

<h2>Python</h2>
<h3>Dictionaries</h3>
```

CSS should control appearance.

HTML should communicate structure.

---

# 11. Paragraphs and Text

Core elements include:

```html
<p>
<strong>
<em>
<br>
<hr>
```

Understand semantic meaning rather than merely visual appearance.

For example:

```html
<strong>Important</strong>
```

has semantic meaning beyond simply making text bold.

---

# 12. Links

Basic:

```html
<a href="about.html">About</a>
```

External:

```html
<a href="https://example.com">Example</a>
```

Page fragment:

```html
<a href="#contact">Contact</a>
```

Target:

```html
<section id="contact">
```

Understand:

- URL,
- relative URL,
- absolute URL,
- fragment identifier,
- navigation.

---

# 13. Images

Basic:

```html
<img src="images/cat.jpg" alt="A sleeping orange cat">
```

Know:

```text
src
alt
width
height
```

`alt` is important for accessibility and fallback meaning.

Decorative images may require different alternative-text treatment than informative images.

---

# 14. Lists

Unordered:

```html
<ul>
    <li>Java</li>
    <li>Python</li>
</ul>
```

Ordered:

```html
<ol>
    <li>Install dependencies</li>
    <li>Build project</li>
    <li>Run project</li>
</ol>
```

Description lists also exist:

```html
<dl>
    <dt>HTML</dt>
    <dd>Structures web content.</dd>
</dl>
```

---

# 15. Semantic HTML

Semantic elements communicate meaning.

Important:

```text
header
nav
main
section
article
aside
footer
figure
figcaption
time
address
```

Prefer meaningful elements over generic containers when an appropriate semantic element exists.

---

# 16. `div` and `span`

`div` is a generic block-level container.

```html
<div class="card">
</div>
```

`span` is a generic inline container.

```html
<p>Status: <span class="status">Active</span></p>
```

They are useful when no more meaningful semantic element fits.

Do not build everything from `div` elements when native semantic HTML describes the structure better.

---

# 17. Tables

Use tables for **tabular data**, not general page layout.

Basic structure:

```html
<table>
    <thead>
        <tr>
            <th>Name</th>
            <th>Score</th>
        </tr>
    </thead>

    <tbody>
        <tr>
            <td>Alice</td>
            <td>95</td>
        </tr>
    </tbody>
</table>
```

Know:

```text
table
caption
thead
tbody
tfoot
tr
th
td
```

Understand accessible headers and table structure.

---

# 18. Forms

Forms collect user input.

Basic:

```html
<form action="/users" method="post">
</form>
```

Important concepts:

```text
form
label
input
textarea
select
option
button
fieldset
legend
```

---

# 19. Labels

Correct association:

```html
<label for="email">Email</label>
<input id="email" name="email" type="email">
```

The `for` value matches the input's `id`.

Labels are important for:

- accessibility,
- click/touch targets,
- understanding the control.

Placeholder text is not a replacement for a label.

---

# 20. Common Input Types

Know:

```text
text
email
password
number
date
checkbox
radio
file
hidden
submit
```

Example:

```html
<input type="email" name="email" required>
```

Different input types can provide browser behavior and validation.

---

# 21. Form `name`

Example:

```html
<input id="email" name="email" type="email">
```

The `id` identifies the element in the document.

The `name` identifies the control's value when form data is submitted.

This distinction is important.

---

# 22. Form `action` and `method`

Example:

```html
<form action="/messages" method="post">
```

`action` tells the browser where to send the form.

`method` describes the HTTP method.

For basic HTML forms, the most important methods are:

```text
GET
POST
```

A form does **not require JavaScript** to submit data to a server.

---

# 23. Native HTML Validation

Useful attributes:

```text
required
min
max
minlength
maxlength
pattern
type
```

Example:

```html
<input
    type="text"
    name="username"
    minlength="3"
    maxlength="30"
    required
>
```

Use native browser capabilities before recreating everything with JavaScript.

Client-side validation improves usability but does **not** replace server-side validation.

---

# 24. Buttons

Know the distinction:

```html
<button type="submit">Submit</button>
<button type="button">Do Something</button>
<button type="reset">Reset</button>
```

Inside a form, be explicit about button type.

---

# 25. Accessibility Fundamentals

Accessibility should be part of normal HTML design, not an optional final step.

Understand:

- semantic elements,
- labels,
- heading hierarchy,
- keyboard access,
- focus,
- alternative text,
- sufficient contrast,
- form errors,
- landmarks,
- meaningful link text.

---

# 26. Native HTML Before ARIA

ARIA stands for:

**Accessible Rich Internet Applications**

Learn basic ARIA concepts, but follow the principle:

> Prefer correct native HTML semantics when they already provide the required meaning and behavior.

Do not add ARIA merely because it looks more advanced.

---

# 27. Keyboard Accessibility

Interactive controls should generally be usable without a mouse.

Test:

```text
Tab
Shift+Tab
Enter
Space
```

Understand:

- focus order,
- visible focus indication,
- semantic buttons vs clickable generic containers.

For example, prefer:

```html
<button>Save</button>
```

over creating a fake button from:

```html
<div>Save</div>
```

---

# 28. CSS Syntax

Basic rule:

```css
selector {
    property: value;
}
```

Example:

```css
h1 {
    font-size: 2rem;
    margin-bottom: 1rem;
}
```

Know:

```text
selector
declaration block
property
value
declaration
```

---

# 29. Connecting CSS to HTML

External stylesheet:

```html
<link rel="stylesheet" href="styles.css">
```

Preferred for most project CSS.

Also recognize:

```html
<style>
</style>
```

and inline style:

```html
<p style="color: red;">Hello</p>
```

Understand why external stylesheets are normally easier to maintain.

---

# 30. Core CSS Selectors

Element:

```css
p {
}
```

Class:

```css
.card {
}
```

ID:

```css
#main {
}
```

Universal:

```css
* {
}
```

Multiple selectors:

```css
h1,
h2,
h3 {
}
```

---

# 31. Relationship Selectors

Descendant:

```css
article p {
}
```

Direct child:

```css
article > p {
}
```

Adjacent sibling:

```css
h2 + p {
}
```

General sibling:

```css
h2 ~ p {
}
```

Understand these through the HTML tree rather than memorizing symbols without context.

---

# 32. Attribute Selectors

Examples:

```css
input[type="email"] {
}
```

```css
a[target="_blank"] {
}
```

Understand how selectors can match attributes and attribute values.

---

# 33. Pseudo-Classes

Pseudo-classes describe state or structural conditions.

Common:

```css
:hover
:focus
:focus-visible
:active
:checked
:disabled
:required
:first-child
:last-child
:nth-child()
```

Example:

```css
button:hover {
    transform: scale(1.02);
}
```

---

# 34. Pseudo-Elements

Common:

```css
::before
::after
::first-letter
::selection
```

Example:

```css
.required::after {
    content: " *";
}
```

Understand that pseudo-elements represent stylable generated/abstract portions of an element rather than ordinary HTML nodes.

---

# 35. The Cascade

"Cascading" is central to CSS.

When multiple rules apply, the browser determines which declaration wins using factors including:

- origin,
- importance,
- cascade layers where used,
- specificity,
- scope/proximity rules where applicable,
- source order.

Do not reduce CSS mastery to "the last rule always wins."

---

# 36. Specificity

Specificity helps determine which competing selector wins within the relevant cascade context.

Conceptually:

```text
ID selectors
    >
class / attribute / pseudo-class selectors
    >
type / pseudo-element selectors
```

Example:

```css
p {
    color: black;
}

.message {
    color: blue;
}

#warning {
    color: red;
}
```

Learn to reason about specificity rather than trying to memorize arbitrary huge numeric scores.

---

# 37. `!important`

Example:

```css
color: red !important;
```

This changes cascade priority.

Do not use `!important` as the default fix for specificity problems.

Understand why excessive use makes stylesheets difficult to reason about.

---

# 38. Inheritance

Some CSS properties naturally inherit from ancestors.

For example, text-related properties often inherit:

```css
body {
    font-family: sans-serif;
    color: #222;
}
```

Descendants can inherit these values.

Many layout properties do not inherit.

Understand:

- inherited values,
- initial values,
- explicit values.

---

# 39. CSS Box Model

Every element can be reasoned about as:

```text
+---------------------------+
|          margin           |
|  +---------------------+  |
|  |       border        |  |
|  |  +---------------+  |  |
|  |  |    padding    |  |  |
|  |  | +-----------+ |  |  |
|  |  | |  content  | |  |  |
|  |  | +-----------+ |  |  |
|  |  +---------------+  |  |
|  +---------------------+  |
+---------------------------+
```

Know:

```text
content
padding
border
margin
```

This is essential CSS knowledge.

---

# 40. `box-sizing`

Default box sizing can make width calculations unintuitive.

A common global rule:

```css
*,
*::before,
*::after {
    box-sizing: border-box;
}
```

With:

```css
box-sizing: border-box;
```

declared width includes padding and border.

Understand why this often simplifies layout calculations.

---

# 41. Width and Height

Know:

```css
width
height
min-width
max-width
min-height
max-height
```

Responsive designs frequently prefer constraints such as:

```css
.container {
    width: 100%;
    max-width: 1200px;
}
```

rather than fixed dimensions everywhere.

---

# 42. CSS Units

Know common units:

```text
px
%
em
rem
vw
vh
dvh
ch
fr
```

Understand the difference between:

- fixed-ish CSS pixel units,
- percentages,
- font-relative units,
- viewport-relative units,
- Grid fractional units.

Do not treat one unit as universally correct.

---

# 43. `rem` and `em`

`rem` is relative to the root element's font size.

`em` is relative to font sizing in the current element's context and can compound depending on use.

Understand when relative units improve scalable interfaces.

---

# 44. Colors

Know common formats:

```css
color: red;
color: #ff0000;
color: rgb(255 0 0);
color: hsl(0 100% 50%);
```

Also understand alpha/transparency.

Accessibility requires attention to foreground/background contrast.

---

# 45. Typography

Important properties:

```css
font-family
font-size
font-weight
font-style
line-height
letter-spacing
text-align
text-decoration
```

Understand:

- font stacks,
- fallback fonts,
- readability,
- line length,
- spacing,
- hierarchy.

---

# 46. Backgrounds

Know:

```css
background-color
background-image
background-size
background-position
background-repeat
```

Example:

```css
.hero {
    background-image: url("images/hero.jpg");
    background-size: cover;
    background-position: center;
}
```

---

# 47. Borders and Radius

Know:

```css
border
border-width
border-style
border-color
border-radius
```

Example:

```css
.card {
    border: 1px solid #ccc;
    border-radius: 0.5rem;
}
```

---

# 48. Margin and Padding

Padding creates space **inside** an element's border.

Margin creates space **outside** the border.

Example:

```css
.card {
    padding: 1rem;
    margin-bottom: 2rem;
}
```

Understand margin behavior, including vertical margin collapsing in normal flow.

---

# 49. Normal Document Flow

Before learning positioning, understand normal flow.

Block elements generally stack vertically.

Inline content generally flows within lines.

CSS layout systems modify how elements participate in layout.

Many CSS problems come from fighting normal flow unnecessarily.

---

# 50. `display`

Important values:

```text
block
inline
inline-block
none
flex
grid
```

Understand how `display` changes layout behavior.

---

# 51. `display: none`

```css
.hidden {
    display: none;
}
```

Removes the element from layout.

Understand that hiding content has accessibility implications depending on the technique used.

---

# 52. Positioning

Know:

```text
static
relative
absolute
fixed
sticky
```

Example:

```css
.parent {
    position: relative;
}

.child {
    position: absolute;
    top: 0;
    right: 0;
}
```

Understand containing blocks and why absolutely positioned elements can appear to "escape" expected locations.

---

# 53. `z-index` and Stacking

Know:

```css
z-index
```

But understand that `z-index` operates within stacking contexts.

Do not assume arbitrarily increasing `z-index` will solve every layering problem.

---

# 54. Flexbox

Flexbox is designed primarily for one-dimensional layout.

Basic:

```css
.container {
    display: flex;
}
```

Important properties:

```text
flex-direction
justify-content
align-items
align-content
flex-wrap
gap
flex-grow
flex-shrink
flex-basis
```

---

# 55. Flexbox Axes

Understand:

```text
main axis
cross axis
```

`flex-direction` determines the main axis.

Then:

```text
justify-content → main axis
align-items     → cross axis
```

This mental model is more useful than memorizing individual examples.

---

# 56. Flex Items

Know:

```css
.item {
    flex-grow: 1;
    flex-shrink: 1;
    flex-basis: auto;
}
```

Shorthand:

```css
.item {
    flex: 1;
}
```

Understand conceptually how available and insufficient space are distributed.

---

# 57. CSS Grid

Grid is designed for two-dimensional layout.

Basic:

```css
.container {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
}
```

Common:

```css
.container {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 1rem;
}
```

---

# 58. Grid Concepts

Understand:

```text
grid container
grid item
grid line
grid track
grid cell
grid area
row
column
gap
```

Useful properties include:

```text
grid-template-columns
grid-template-rows
grid-column
grid-row
gap
place-items
```

---

# 59. Responsive Grid

Example:

```css
.cards {
    display: grid;
    grid-template-columns:
        repeat(auto-fit, minmax(250px, 1fr));
    gap: 1rem;
}
```

Understand:

```text
repeat()
minmax()
auto-fit
auto-fill
fr
```

These allow layouts to respond naturally without excessive breakpoints.

---

# 60. Flexbox vs Grid

General guideline:

```text
Flexbox → primarily one-dimensional relationships
Grid    → primarily two-dimensional layout
```

They are complementary.

Real interfaces commonly use Grid for major layout and Flexbox inside individual components, or vice versa.

---

# 61. CSS Frameworks — Why and Tradeoffs

Everything up to this point has been hand-written CSS. In real teams, a large share of interfaces are instead styled with a **CSS framework** — a pre-built library of classes (and sometimes components) that saves you from writing every rule from raw CSS.

Reasons teams reach for one:

- consistency across a team without agreeing on a custom design system from scratch,
- speed — buttons, forms, grids, and spacing scales already exist,
- fewer cross-browser/reset quirks to fight individually,
- a shared vocabulary that new contributors already know.

Tradeoffs to understand clearly:

- extra bundle size (or a required build step to keep it small),
- fighting the framework's opinions when a design departs from its defaults,
- harder to achieve a fully custom, distinctive visual identity,
- another dependency and version to track.

A framework does not remove the need to understand the CSS underneath it. Box model, Flexbox, Grid, specificity, and the cascade are exactly what a framework is generating classes for.

The two dominant styles of framework are:

```text
utility-first   → Tailwind CSS
component-based → Bootstrap
```

---

# 62. Utility-First CSS (Tailwind)

Tailwind ships small, single-purpose utility classes instead of custom named classes. You compose them directly in markup rather than writing a new CSS rule per component.

Hand-written CSS:

```css
.card {
    display: flex;
    align-items: center;
    gap: 1rem;
    border-radius: 0.5rem;
    background-color: #2563eb;
    padding: 0.5rem 1rem;
    color: white;
}
```

The Tailwind equivalent, applied directly in HTML:

```html
<div class="flex items-center gap-4 rounded-lg bg-blue-600 px-4 py-2 text-white">
    Card content
</div>
```

This is a different mental model from the rest of this skill map: instead of naming a component and defining its rules once in a stylesheet, you describe its appearance inline with composable primitives. You still need to understand `display`, `flex`, spacing, and color to know which utilities to reach for.

Responsive variants prefix a utility with a breakpoint:

```html
<div class="flex flex-col gap-2 md:flex-row md:gap-6 lg:gap-10">
    ...
</div>
```

This reads as "column layout by default, row layout at the `md` breakpoint and up, wider gap at `lg`" — the same media-query thinking from responsive design, expressed as class names instead of `@media` blocks.

State variants work the same way:

```html
<button class="bg-blue-600 hover:bg-blue-700 focus:ring-2 focus:ring-blue-400">
    Submit
</button>
```

Tailwind is built with a build step that scans your HTML/JS for class names actually used and ships only those to production — commonly called **purging**. This keeps the shipped CSS small even though the full utility library is enormous during development.

---

# 63. Component-Based CSS (Bootstrap)

Bootstrap takes the opposite approach: it ships pre-styled **components** as ready-made classes.

```html
<button class="btn btn-primary">Save</button>
```

Its most-used feature is a 12-column responsive grid:

```html
<div class="container">
    <div class="row">
        <div class="col-md-8">Main content</div>
        <div class="col-md-4">Sidebar</div>
    </div>
</div>
```

Understand how this relates to native CSS Grid, which this skill map already covers:

```text
Bootstrap grid → a fixed 12-column system built from container/row/col-* classes,
                 flexbox-based under the hood, column counts chosen per breakpoint
CSS Grid       → an arbitrary N-row/N-column layout system defined in CSS itself,
                 not limited to 12 columns, no required class-naming convention
```

Bootstrap's grid is a convention layered on top of CSS; native Grid is a browser layout mode. Knowing native Grid and Flexbox (sections 54–60) makes it straightforward to understand what Bootstrap's grid classes are doing internally.

Be aware that several Bootstrap components — modals, dropdowns, carousels, collapsible navbars, tooltips — depend on Bootstrap's own JavaScript (or Popper.js) to function, not just its CSS. Including only the CSS file will render the markup but the interactive behavior will not work.

---

# 64. Choosing Between Hand-Written CSS, Tailwind, and Bootstrap

A short comparison:

```text
Hand-written CSS → full control, no dependency, most typing,
                    best for a distinctive design or learning the fundamentals
Tailwind         → utility-first, fast to compose, small production output,
                    best when you want custom-looking design without naming classes
Bootstrap        → component-first, fastest to a working UI, more generic look
                    unless themed, best for prototypes and admin-style interfaces
```

None of these replace the need to understand box model, selectors, specificity, Flexbox, and Grid — a framework is a layer on top of that knowledge, and reading a framework's generated CSS is far easier once the fundamentals in this skill map are solid.

---

# 65. Responsive Web Design

Responsive design adapts interfaces to different:

- viewport widths,
- devices,
- orientations,
- user preferences,
- content sizes.

Key tools:

- flexible layouts,
- flexible images,
- relative units,
- media queries,
- content-driven breakpoints.

---

# 66. Viewport Metadata

Important:

```html
<meta
    name="viewport"
    content="width=device-width, initial-scale=1.0"
>
```

Understand why mobile browsers need correct viewport behavior for responsive design.

---

# 67. Media Queries

Example:

```css
@media (max-width: 768px) {
    .navigation {
        flex-direction: column;
    }
}
```

Mobile-first example:

```css
.cards {
    display: grid;
    grid-template-columns: 1fr;
}

@media (min-width: 768px) {
    .cards {
        grid-template-columns: repeat(2, 1fr);
    }
}
```

Prefer breakpoints based on when the layout needs adjustment rather than blindly targeting particular device models.

---

# 68. Responsive Images

Basic:

```css
img {
    max-width: 100%;
    height: auto;
}
```

Also understand that HTML provides tools such as:

```text
srcset
sizes
picture
source
```

for responsive image selection.

---

# 69. CSS Custom Properties

Define:

```css
:root {
    --spacing: 1rem;
    --primary-color: #3366cc;
}
```

Use:

```css
.button {
    padding: var(--spacing);
    background: var(--primary-color);
}
```

Useful for:

- design tokens,
- themes,
- repeated values,
- maintainability.

---

# 70. `calc()`, `min()`, `max()`, and `clamp()`

Modern CSS can calculate responsive values.

Examples:

```css
width: calc(100% - 2rem);
```

```css
font-size: clamp(1rem, 2vw, 2rem);
```

Understand these as tools for fluid design rather than memorizing recipes.

---

# 71. Transitions

Example:

```css
button {
    transition: transform 150ms ease;
}

button:hover {
    transform: scale(1.05);
}
```

Know:

```text
transition-property
transition-duration
transition-timing-function
transition-delay
```

Use motion intentionally.

---

# 72. Animations

Example:

```css
@keyframes fade-in {
    from {
        opacity: 0;
    }

    to {
        opacity: 1;
    }
}

.message {
    animation: fade-in 300ms ease;
}
```

Understand:

- keyframes,
- duration,
- timing,
- iteration.

Also consider reduced-motion preferences.

---

# 73. User Preference Media Queries

Recognize accessibility-related queries such as:

```css
@media (prefers-reduced-motion: reduce) {
    /* reduce unnecessary motion */
}
```

Understand that responsive design includes user preferences, not merely screen width.

---

# 74. CSS Organization

As projects grow, CSS needs structure.

Understand approaches such as:

- component-oriented classes,
- utility classes,
- naming conventions,
- design tokens/custom properties,
- separating layout from component concerns,
- avoiding unnecessary selector complexity.

You do not need a CSS framework to write maintainable CSS.

---

# 75. CSS Naming

Prefer names describing purpose:

```css
.profile-card
.navigation
.error-message
.form-field
```

Avoid names tied too tightly to temporary visual details:

```css
.red-box
.left-thing
```

when the class actually represents a reusable semantic/component concept.

---

# 76. Browser Rendering Mental Model

A simplified model:

```text
HTML
 ↓ parse
DOM

CSS
 ↓ parse
CSS rules / style information

DOM + styles
 ↓
layout
 ↓
paint
 ↓
composite
 ↓
screen
```

JavaScript can modify the DOM/styles and cause parts of this process to run again.

You do not need browser-engine internals memorized, but this model helps explain behavior.

---

# 77. The DOM

DOM stands for:

**Document Object Model**

The browser represents HTML as a tree of objects.

Example HTML:

```html
<body>
    <main>
        <h1>Hello</h1>
        <p>Welcome</p>
    </main>
</body>
```

Conceptually:

```text
body
└── main
    ├── h1
    └── p
```

CSS selectors and JavaScript frequently operate on this structure.

---

# 78. Browser Developer Tools

Become comfortable opening DevTools.

Common shortcuts include:

```text
F12
Ctrl+Shift+I
```

depending on browser/OS.

Important panels:

- Elements/Inspector,
- Styles,
- Computed,
- Layout,
- Network,
- Console,
- Accessibility tools where available.

---

# 79. Inspecting HTML

Use the Elements/Inspector panel to:

- inspect DOM structure,
- locate elements,
- edit attributes temporarily,
- add/remove classes,
- inspect accessibility information,
- understand generated DOM changes.

Remember:

DevTools edits normally change only the current browser session, not your source files.

---

# 80. Inspecting CSS

Use DevTools to:

- see matching rules,
- see crossed-out declarations,
- identify inherited styles,
- inspect specificity/cascade behavior,
- toggle declarations,
- test new values,
- inspect pseudo-classes.

This is one of the fastest ways to learn CSS.

---

# 81. Computed Styles

The computed-style view shows the final resolved styles applied to an element.

Use it when asking:

> What value did the browser actually choose?

This is valuable for debugging:

- inheritance,
- specificity,
- dimensions,
- fonts,
- layout.

---

# 82. Box Model Inspector

DevTools usually visualizes:

```text
margin
border
padding
content
```

Use it when debugging unexpected:

- width,
- height,
- spacing,
- overflow.

---

# 83. Responsive Design Tools

Browser DevTools can emulate viewport dimensions.

Use responsive mode to test:

- narrow screens,
- wide screens,
- media queries,
- overflow,
- touch-sized controls.

Do not rely exclusively on preset device names.

Resize through many widths and look for where the design actually breaks.

---

# 84. Network Panel

The Network panel shows resources requested by the browser.

Useful for diagnosing:

- missing CSS,
- missing images,
- failed HTTP requests,
- incorrect paths,
- response status codes,
- caching behavior.

If a stylesheet is not applying, first confirm the browser successfully loaded it.

---

# 85. Console

The Console is useful for:

- JavaScript errors,
- testing DOM expressions,
- inspecting values,
- warnings.

Example:

```javascript
document.querySelector("h1")
```

You do not need advanced JavaScript to use the Console as a debugging tool.

---

# 86. Project Directory Structure

A simple static project might look like:

```text
website/
├── index.html
├── about.html
├── css/
│   └── styles.css
├── js/
│   └── script.js
└── images/
    └── logo.png
```

Another perfectly valid small structure:

```text
website/
├── index.html
├── styles.css
├── script.js
└── images/
```

Understand structure rather than believing there is one mandatory folder layout.

---

# 87. Relative Paths

From:

```text
index.html
```

to:

```text
css/styles.css
```

use:

```html
<link rel="stylesheet" href="css/styles.css">
```

From a file inside:

```text
pages/
```

back one level:

```text
../
```

Path mistakes are one of the most common beginner web-development problems.

---

# 88. Absolute URLs vs Relative URLs

Absolute:

```text
https://example.com/images/logo.png
```

Relative:

```text
images/logo.png
```

Root-relative URL:

```text
/images/logo.png
```

Understand how the browser resolves each form.

---

# 89. Loading a Page Directly

A basic HTML file can often be opened directly in a browser.

The address may begin with:

```text
file://
```

This is useful for simple HTML/CSS testing.

However, it does not behave exactly like a page served through HTTP.

---

# 90. Local HTTP Server

A local server gives development pages an HTTP origin.

For example, if Python is available:

```bash
python3 -m http.server 8000
```

Then visit:

```text
http://localhost:8000
```

This is useful for learning:

- HTTP requests,
- relative resources,
- browser origins,
- networking,
- behavior closer to real hosting.

The command is a development convenience, not a production hosting strategy.

---

# 91. `localhost`

`localhost` refers to the local machine/environment from the perspective of the software making the request.

Common loopback address:

```text
127.0.0.1
```

Example:

```text
http://localhost:8000
```

Understand that containers, virtual machines, WSL, and remote environments can make the meaning of "local" more nuanced.

---

# 92. Ports

A server listens on an address and port.

Examples:

```text
http://localhost:8000
http://localhost:4200
http://localhost:8080
```

Conceptually:

```text
host
  ↓
port
  ↓
specific listening service
```

This becomes essential in full-stack development.

---

# 93. Client and Server

For a website:

```text
Browser
  ↓ request
Web server
  ↓ response
Browser
```

The browser is typically the HTTP client.

A server listens for requests and returns responses.

---

# 94. HTTP

HTTP stands for:

**Hypertext Transfer Protocol**

It defines how clients and servers exchange web requests and responses.

Simplified:

```text
Browser
   |
   | HTTP request
   ↓
Server
   |
   | HTTP response
   ↓
Browser
```

---

# 95. HTTP Request

A request can include:

- method,
- URL/path,
- headers,
- body.

Conceptually:

```text
POST /messages
Content-Type: application/json

{
    "name": "Alice",
    "message": "Hello"
}
```

---

# 96. HTTP Response

A response can include:

- status code,
- headers,
- body.

Conceptually:

```text
HTTP 200 OK
Content-Type: application/json

{
    "id": 1,
    "name": "Alice"
}
```

---

# 97. HTTP Methods

Know at minimum:

```text
GET
POST
PUT
PATCH
DELETE
```

General meanings:

```text
GET     retrieve
POST    create/process
PUT     replace/update resource representation
PATCH   partially update
DELETE  remove
```

HTML forms directly support GET and POST as their normal submission methods.

JavaScript/API clients can use the broader HTTP method set.

---

# 98. HTTP Status Codes

Know major categories:

```text
1xx informational
2xx success
3xx redirection
4xx client-side/request problem
5xx server-side problem
```

Common codes worth recognizing:

```text
200 OK
201 Created
204 No Content
301 Moved Permanently
302 Found
304 Not Modified
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
409 Conflict
500 Internal Server Error
502 Bad Gateway
503 Service Unavailable
```

---

# 99. HTTPS

HTTPS is HTTP protected using TLS.

Conceptually:

```text
HTTP
+
TLS
=
HTTPS
```

HTTPS provides important properties including:

- encryption in transit,
- integrity protection,
- server authentication through certificates.

Understand why production websites should normally use HTTPS.

---

# 100. Domain Names and DNS

A domain might be:

```text
example.com
```

DNS helps translate names into network addressing information.

Simplified:

```text
example.com
    ↓ DNS
server address
    ↓
HTTP/HTTPS connection
```

You do not need DNS administration mastery yet, but understand where DNS fits into web hosting.

---

# 101. Hosting Mental Model

A simplified production path:

```text
Your source files
       ↓
build/deployment process
       ↓
server / hosting platform
       ↓
domain + DNS
       ↓
HTTPS
       ↓
internet
       ↓
user's browser
```

For a static HTML/CSS/JS website, a hosting service may simply serve the files.

For a full-stack application, the architecture usually contains additional backend services.

---

# 102. Static vs Dynamic Websites

A static site can serve existing:

```text
HTML
CSS
JavaScript
images/assets
```

A dynamic/full-stack system may involve:

```text
Browser
   ↓
Frontend
   ↓ HTTP/API
Backend
   ↓
Database
```

Your HTML/CSS knowledge remains relevant in both.

---

# 103. Browser Caching

Browsers cache resources to improve performance.

This can occasionally make development confusing:

> "I changed my CSS, but the browser still looks the same."

Understand:

- reload,
- hard reload,
- cache,
- Network panel.

Do not assume every unchanged page is caused by caching; inspect first.

---

# 104. JavaScript Placement

External:

```html
<script src="js/script.js" defer></script>
```

`defer` allows the HTML parser to continue while the script is fetched and delays execution until parsing is complete.

For this skill level, external scripts with `defer` are a useful default pattern.

---

# 105. Selecting DOM Elements

Common:

```javascript
document.querySelector(".card");
```

Multiple:

```javascript
document.querySelectorAll(".card");
```

By ID:

```javascript
document.getElementById("message");
```

For new code, become especially comfortable with CSS-selector-based:

```javascript
querySelector()
querySelectorAll()
```

---

# 106. Reading and Changing Content

Example:

```javascript
const heading = document.querySelector("h1");

console.log(heading.textContent);

heading.textContent = "Updated Heading";
```

Understand that JavaScript is modifying the DOM representation.

---

# 107. Classes from JavaScript

Useful API:

```javascript
element.classList.add("active");
element.classList.remove("active");
element.classList.toggle("active");
element.classList.contains("active");
```

Prefer changing classes and letting CSS control appearance rather than putting large amounts of styling logic directly into JavaScript.

---

# 108. Events

Browsers generate events such as:

```text
click
submit
input
change
keydown
focus
blur
```

Listen:

```javascript
button.addEventListener("click", () => {
    console.log("Clicked");
});
```

Understand:

```text
event source
event type
event listener
event handler
```

---

# 109. Form Submission with JavaScript

HTML:

```html
<form id="contact-form">
    <label for="name">Name</label>
    <input id="name" name="name" required>

    <button type="submit">Send</button>
</form>
```

JavaScript:

```javascript
const form = document.querySelector("#contact-form");

form.addEventListener("submit", (event) => {
    event.preventDefault();

    console.log("Form submitted");
});
```

`preventDefault()` prevents the browser's normal form-submission behavior for that event.

Only do this when JavaScript will intentionally handle the submission.

---

# 110. Reading Form Data

Example:

```javascript
const formData = new FormData(form);

const name = formData.get("name");
```

This reinforces why the HTML `name` attribute matters.

---

# 111. Client-Side Validation

HTML validation should often be the first layer:

```html
<input
    type="email"
    name="email"
    required
>
```

JavaScript can add application-specific behavior.

But:

> Never trust client-side validation as your only validation.

Users can bypass browser-side code.

A future backend must independently validate incoming data.

---

# 112. Displaying Form Feedback

HTML:

```html
<p id="form-message" aria-live="polite"></p>
```

JavaScript:

```javascript
const message = document.querySelector("#form-message");

message.textContent = "Form submitted successfully.";
```

Understand accessible feedback patterns rather than merely changing colors.

---

# 113. Basic `fetch()`

Example:

```javascript
fetch("/api/messages")
    .then(response => response.json())
    .then(data => {
        console.log(data);
    });
```

Conceptually:

```text
JavaScript
   ↓
fetch()
   ↓
HTTP request
   ↓
backend
   ↓
HTTP response
   ↓
JavaScript
```

This is the bridge from frontend fundamentals to full-stack development.

---

# 114. Basic POST Request

Example:

```javascript
fetch("/api/messages", {
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

At this stage, understand the structure.

Detailed Promises, async/await, error handling, JavaScript objects, modules, and application architecture belong in the JavaScript skill map.

---

# 115. Same-Origin Concept

Browsers attach security meaning to a page's **origin**.

An origin is based primarily on:

```text
scheme/protocol
host
port
```

For example:

```text
http://localhost:4200
```

and:

```text
http://localhost:8081
```

have different origins because the ports differ.

This becomes important when frontends call backends.

---

# 116. CORS Introduction

CORS stands for:

**Cross-Origin Resource Sharing**

Browsers enforce same-origin security rules and use CORS response headers to determine whether certain cross-origin requests are allowed.

Example development architecture:

```text
Frontend
http://localhost:4200

Backend
http://localhost:8081
```

These are different origins.

CORS deserves deeper treatment with backend/API development, but frontend developers should understand why the browser may block a request even though the backend server itself is running.

---

# 117. Security Fundamentals for HTML/Forms

Understand at a conceptual level:

- never trust client input,
- client-side validation is not security,
- passwords/secrets should not be embedded in frontend source,
- HTTPS protects data in transit,
- browsers enforce important origin rules,
- user-provided content can create injection risks,
- HTML injection and XSS are important future topics,
- forms and APIs require server-side validation.

Do not implement your own cryptography.

---

# 118. HTML Comments

```html
<!-- This is a comment -->
```

Comments are visible in page source delivered to the browser.

Never put secrets in HTML comments.

---

# 119. CSS Comments

```css
/* This is a CSS comment */
```

Again, frontend source delivered to users should not contain secrets.

---

# 120. Browser Source Is Public to the User

Anything delivered to the browser should generally be considered inspectable by the user:

```text
HTML
CSS
JavaScript
client-visible network requests
```

Therefore:

> Frontend source code is not an appropriate place to hide passwords, private API secrets, or server credentials.

This principle is essential for future full-stack development.

---

# 121. Common HTML Problem — Broken Path

Symptoms:

- image missing,
- CSS not loading,
- link gives 404.

Investigate:

1. project directory structure,
2. current document location,
3. path spelling,
4. capitalization,
5. browser Network panel.

Do not randomly add `../` until the path works.

Reason from the filesystem/document location.

---

# 122. Common CSS Problem — Rule Does Not Apply

Check:

1. Did the stylesheet load?
2. Does the selector match?
3. Is the property valid?
4. Is another declaration winning?
5. Is the property inherited?
6. Is the element in the layout mode you expect?

Use DevTools.

When using a framework, also check whether a utility class was purged because it was never written literally in the markup Tailwind scans (dynamically built class strings are a common cause), or whether a Bootstrap component class is being overridden by a more specific custom rule loaded after it.

---

# 123. Common CSS Problem — Unexpected Size

Check:

- width/height,
- padding,
- border,
- margin,
- `box-sizing`,
- parent constraints,
- min/max dimensions,
- overflow.

Use the DevTools box-model view.

---

# 124. Common CSS Problem — Flexbox Confusion

Ask:

1. Which element is the flex container?
2. Which elements are flex items?
3. What is the main axis?
4. What is the cross axis?
5. Is wrapping enabled?
6. Is available space being distributed?

Reason from the flex model.

---

# 125. Common CSS Problem — Grid Confusion

Ask:

1. Which element is the grid container?
2. What columns exist?
3. What rows exist?
4. How are tracks sized?
5. Where is the item placed?
6. Is content forcing a track larger?

Use DevTools Grid overlays when available.

---

# 126. Common CSS Problem — Overflow

Symptoms:

- horizontal scrollbar,
- content escapes container,
- text/images exceed layout.

Check:

- fixed widths,
- long unbreakable content,
- image sizing,
- Grid/Flex minimum sizing,
- padding/border,
- viewport units.

Avoid immediately hiding the problem with:

```css
overflow: hidden;
```

Find the cause first.

---

# 127. Common Form Problem — Data Missing

Check:

- does the control have `name`?
- is it disabled?
- is the form being submitted?
- does JavaScript call `preventDefault()`?
- is the backend expecting the same field names?
- is the correct HTTP method used?

Use the Network panel for real submissions.

---

# 128. Common HTTP Problem — 404

`404 Not Found` usually means the requested resource/path was not found by the server.

Check:

- URL,
- path,
- filename,
- route,
- capitalization,
- server root.

---

# 129. Common HTTP Problem — CORS Error

Determine:

```text
Where is the frontend running?
Where is the backend running?
What protocol?
What hostname?
What port?
```

Then determine whether the request is cross-origin.

Do not treat CORS as a generic networking failure.

---

# 130. Debugging Workflow

When a page behaves incorrectly:

## HTML

Inspect the DOM.

## CSS

Inspect matching/computed styles.

## Layout

Inspect box model, Flexbox, or Grid overlays.

## Assets

Inspect Network requests.

## JavaScript

Inspect Console errors.

## HTTP

Inspect request URL, method, status, headers, and response.

Change one thing at a time and retest.

---

# 131. Separation of Concerns

A useful beginner mental model:

```text
HTML
structure + meaning

CSS
presentation + layout

JavaScript
behavior + interaction
```

Real applications sometimes blur these boundaries, but understanding them clearly first makes frameworks much easier to learn later.

---

# 132. Progressive Enhancement

A useful web-development principle is:

1. start with meaningful HTML,
2. add CSS presentation,
3. add JavaScript behavior where it provides value.

A basic form can work through HTML/server submission before JavaScript enhances its interaction.

This encourages robust, accessible designs.

---

# 133. Maintainability

As pages grow:

- use semantic HTML,
- avoid unnecessary nesting,
- use reusable CSS classes,
- avoid excessive specificity,
- keep naming consistent,
- centralize repeated values,
- separate files logically,
- avoid inline styles for normal application styling,
- avoid JavaScript manipulating presentation when a class can express state.

Code should remain understandable after the page grows beyond the first prototype.

---

# 134. HTML Structures to Memorize

Be able to write these without reference:

```text
DOCTYPE
html
head
meta charset
meta viewport
title
link stylesheet
body
```

And common content:

```text
header
nav
main
section
article
aside
footer
h1-h6
p
a
img
ul
ol
li
div
span
table
form
label
input
textarea
select
button
```

You do not need every HTML element memorized.

Know the common vocabulary and how to find specialized elements when needed.

---

# 135. CSS Structures to Memorize

Know from memory:

```css
.class {
    property: value;
}
```

Selectors:

```text
element
.class
#id
parent child
parent > child
:hover
:focus
::before
[attribute]
```

Core properties/concepts:

```text
display
width
height
min/max dimensions
margin
padding
border
box-sizing
color
background
font
position
top/right/bottom/left
z-index
overflow
```

Layout:

```text
flex
grid
gap
justify-content
align-items
grid-template-columns
```

Responsive:

```text
%
rem
fr
vw/vh
media queries
min()
max()
clamp()
```

---

# 136. Light JavaScript to Memorize

At this stage, know the shape of:

```javascript
const element = document.querySelector(".class");
```

```javascript
element.addEventListener("click", () => {
});
```

```javascript
element.textContent = "Text";
```

```javascript
element.classList.add("active");
```

```javascript
form.addEventListener("submit", (event) => {
    event.preventDefault();
});
```

```javascript
const data = new FormData(form);
```

and recognize:

```javascript
fetch("/api/resource")
```

Do **not** attempt to squeeze complete JavaScript mastery into this HTML/CSS skill map.

---

# 137. Practical Exercise — First Page

Build from an empty directory:

```text
website/
├── index.html
└── styles.css
```

Create:

- page title,
- header,
- navigation,
- main content,
- several sections,
- images,
- links,
- footer.

Do not copy a template.

Write the basic HTML skeleton from memory.

---

# 138. Practical Exercise — Semantic Site

Build a small informational site containing:

```text
Home
About
Projects
Contact
```

Use appropriate:

```text
header
nav
main
section
article
footer
```

Explain why each semantic element was chosen.

---

# 139. Practical Exercise — Box Model

Create several boxes.

Experiment with:

```css
width
padding
border
margin
box-sizing
```

Predict the resulting dimensions before inspecting them.

Then verify with DevTools.

---

# 140. Practical Exercise — Flexbox

Build:

- horizontal navigation,
- centered content,
- row of cards,
- wrapping card layout.

Use:

```text
display: flex
flex-direction
justify-content
align-items
flex-wrap
gap
```

Explain the main and cross axes for each example.

---

# 141. Practical Exercise — Grid

Create:

```text
header
sidebar
main
footer
```

using Grid.

Then create a responsive card gallery.

Practice:

```text
grid-template-columns
repeat()
minmax()
fr
gap
```

---

# 142. Practical Exercise — Responsive Site

Create a page that works at:

```text
narrow mobile width
tablet-like width
desktop width
very wide desktop width
```

Do not optimize for only one named device.

Resize continuously and identify natural breakpoints.

---

# 143. Practical Exercise — Accessible Form

Build:

```text
Name
Email
Message
Submit
```

Use:

- labels,
- correct input types,
- `name`,
- `required`,
- length constraints,
- accessible status/error messaging.

Test the form using only the keyboard.

---

# 144. Practical Exercise — JavaScript Form Enhancement

Enhance the form so JavaScript:

1. listens for submit,
2. prevents normal submission,
3. reads values with `FormData`,
4. performs a small application-specific validation,
5. displays feedback in the page,
6. changes a CSS class to represent state.

Keep HTML responsible for structure and CSS responsible for presentation.

---

# 145. Practical Exercise — Local HTTP

Start a local server.

For example:

```bash
python3 -m http.server 8000
```

Load:

```text
http://localhost:8000
```

Use DevTools Network to inspect:

- HTML request,
- CSS request,
- image requests,
- status codes.

Compare this mentally with opening the page directly using `file://`.

---

# 146. Practical Exercise — Simulated Frontend/Backend Boundary

Even before building a full backend, understand this intended architecture:

```text
HTML form
   ↓
JavaScript submit event
   ↓
fetch()
   ↓
HTTP request
   ↓
future backend endpoint
```

Write the frontend portion and be able to explain what the missing backend will eventually need to do.

---

# 147. Practical Exercise — Reproduce a Layout

Choose a simple existing page layout and reproduce its structure without copying its source code.

Identify:

- semantic regions,
- major layout system,
- spacing,
- typography,
- responsive behavior.

Then implement it using only HTML and CSS.

This is excellent practice for converting visual requirements into code.

---

# 148. Practical Exercise — Rebuild the Card Layout with Tailwind

Take the card/navigation layout you built in the Flexbox exercise (section 140) and rebuild it using Tailwind utility classes instead of a custom stylesheet.

Reproduce:

```text
horizontal navigation
row of cards
wrapping card layout
hover state on interactive elements
```

using only utility classes such as:

```text
flex
items-center
justify-between
gap-4
rounded-lg
hover:bg-*
md:flex-row
```

Do not write any custom CSS rules for this version.

Afterward, write down which felt faster, which produced more readable markup, and which would be easier for a teammate to modify six months later.

---

# 149. Practical Exercise — Rebuild the Grid Layout with Bootstrap

Take the header/sidebar/main/footer Grid layout from the Grid exercise (section 141) and rebuild it using Bootstrap's `container`/`row`/`col-*` classes instead of `display: grid`.

Reproduce the same visual regions:

```text
header
sidebar
main
footer
```

using Bootstrap's 12-column grid, for example:

```html
<div class="container">
    <div class="row">
        <div class="col-12">Header</div>
        <div class="col-md-3">Sidebar</div>
        <div class="col-md-9">Main</div>
        <div class="col-12">Footer</div>
    </div>
</div>
```

Compare the result to the native CSS Grid version: which required more markup, which was easier to make responsive, and what Bootstrap's grid classes are doing internally in terms of Flexbox.

---

# 150. Level 1 — HTML Fundamentals

Know:

- HTML document skeleton,
- elements,
- attributes,
- nesting,
- headings,
- paragraphs,
- links,
- images,
- lists,
- paths.

You can create a navigable static page from scratch.

---

# 151. Level 2 — Semantic HTML and Forms

Know:

- semantic layout elements,
- tables,
- forms,
- labels,
- inputs,
- validation attributes,
- accessibility basics.

You can structure a meaningful, accessible website.

---

# 152. Level 3 — CSS Fundamentals

Know:

- selectors,
- cascade,
- specificity,
- inheritance,
- box model,
- typography,
- colors,
- spacing,
- sizing,
- display.

You can style pages predictably instead of relying on trial and error.

---

# 153. Level 4 — Layout Mastery

Know:

- normal flow,
- positioning,
- stacking,
- Flexbox,
- Grid,
- overflow.

You can translate common interface layouts into CSS.

---

# 154. Level 5 — Responsive and Accessible Design

Know:

- flexible sizing,
- media queries,
- responsive images,
- content-driven breakpoints,
- keyboard interaction,
- focus,
- user preferences,
- accessible forms.

You can create pages usable across screen sizes and input methods.

---

# 155. Level 6 — Browser and Debugging Proficiency

Know:

- DOM,
- browser rendering model,
- Elements panel,
- computed styles,
- box model inspector,
- Flex/Grid inspectors,
- Network panel,
- Console,
- caching basics.

You can determine **why** a page behaves incorrectly.

---

# 156. Level 7 — Light Browser Programming

Know enough JavaScript to:

- select DOM elements,
- respond to events,
- change text/classes,
- read forms,
- prevent default submission,
- provide feedback,
- understand basic `fetch()`.

You can create useful interactive prototypes without yet needing a framework.

---

# 157. Level 8 — Web/Hosting Fundamentals

Understand:

- client/server model,
- local HTTP servers,
- localhost,
- ports,
- requests,
- responses,
- HTTP methods,
- status codes,
- HTTPS,
- TLS conceptually,
- domains,
- DNS,
- origins,
- CORS,
- static vs dynamic hosting.

You understand how your local HTML/CSS work fits into a real deployed web application.

---

# 158. Level 9 — Ready to Progress into Full Stack

At this stage, HTML/CSS should no longer feel like mysterious browser syntax.

You should be ready to add dedicated skill maps for:

```text
JavaScript
    ↓
TypeScript
    ↓
frontend framework
```

and separately:

```text
HTTP/API design
    ↓
backend framework
    ↓
database
```

Eventually:

```text
Browser
   ↓
HTML/CSS/JS frontend
   ↓
HTTP API
   ↓
backend
   ↓
database
```

---

# 159. Interview-Focused HTML Questions

Be prepared to answer:

1. What is HTML?
2. Is HTML a programming language?
3. What is an HTML element?
4. What is an attribute?
5. What is semantic HTML?
6. Why use semantic HTML?
7. What is the difference between `div` and `section`?
8. What is the difference between `id` and `class`?
9. Why is heading hierarchy important?
10. What does `<!DOCTYPE html>` do?
11. What belongs in `<head>`?
12. What does the viewport meta tag do?
13. What is the purpose of `alt` text?
14. What is the difference between absolute and relative URLs?
15. What is the DOM?
16. What is a form?
17. What does `action` do?
18. What does `method` do?
19. What does an input's `name` do?
20. What is the difference between `name` and `id`?
21. Why should inputs have labels?
22. What does `required` do?
23. Can an HTML form work without JavaScript?
24. Why is client-side validation insufficient for security?
25. What is accessibility?
26. Why prefer native semantic controls before recreating them with ARIA?

---

# 160. Interview-Focused CSS Questions

Be prepared to answer:

1. What is CSS?
2. Why is it called cascading?
3. What is a selector?
4. What is specificity?
5. What is inheritance?
6. What is the box model?
7. What does `box-sizing: border-box` do?
8. What is the difference between margin and padding?
9. What is normal document flow?
10. What is the difference between block and inline elements?
11. What does `display` control?
12. What is Flexbox?
13. What is the main axis?
14. What is the cross axis?
15. What does `justify-content` do?
16. What does `align-items` do?
17. What is CSS Grid?
18. When would you choose Grid instead of Flexbox?
19. What does `1fr` mean?
20. What is a media query?
21. What is responsive design?
22. What is the difference between `px`, `em`, and `rem`?
23. What is `position: relative`?
24. How does absolute positioning work?
25. What is a stacking context?
26. What is a pseudo-class?
27. What is a pseudo-element?
28. What are CSS custom properties?
29. Why can excessive `!important` usage cause problems?
30. How would you debug a CSS rule that is not applying?

---

# 161. Interview-Focused Browser/Web Questions

Be prepared to explain:

1. What happens when a browser loads an HTML page?
2. What is the DOM?
3. How does CSS relate to the DOM?
4. What are browser developer tools?
5. How would you debug missing CSS?
6. How would you debug a broken image?
7. What is localhost?
8. What is a port?
9. What is HTTP?
10. What is HTTPS?
11. What is an HTTP request?
12. What is an HTTP response?
13. What are GET and POST?
14. What do 2xx, 4xx, and 5xx status codes mean?
15. What is a 404?
16. What is a domain?
17. What does DNS do?
18. What is an origin?
19. What is CORS?
20. What is the difference between a static site and a full-stack application?
21. Why can `file://` behavior differ from HTTP?
22. Why should secrets never be placed in frontend source?

---

# 162. Interview-Focused Light JavaScript Questions

At this stage, be able to explain:

1. What role does JavaScript play alongside HTML and CSS?
2. What is an event?
3. What does `addEventListener()` do?
4. What does `querySelector()` do?
5. What is `textContent`?
6. What is `classList`?
7. What is a submit event?
8. What does `preventDefault()` do?
9. What is `FormData`?
10. What is `fetch()` used for?
11. Why might JavaScript send an HTTP request instead of allowing a normal form navigation?
12. Why must the backend still validate data?

Detailed JavaScript language questions belong in the dedicated JavaScript skill map.

---

# 163. Recommended Learning Order

A strong progression is:

```text
What HTML / CSS / browser are
            ↓
HTML document skeleton
            ↓
elements + attributes
            ↓
paths + links + images
            ↓
semantic HTML
            ↓
forms + accessibility
            ↓
CSS syntax + selectors
            ↓
cascade + specificity + inheritance
            ↓
box model
            ↓
normal flow + display
            ↓
Flexbox
            ↓
Grid
            ↓
responsive design
            ↓
DevTools
            ↓
DOM fundamentals
            ↓
light JavaScript events/forms
            ↓
local HTTP server
            ↓
HTTP requests/responses
            ↓
fetch()
            ↓
origins + CORS
            ↓
hosting/deployment concepts
            ↓
dedicated JavaScript study
            ↓
full-stack integration
```

---

# 164. What to Memorize vs What to Look Up

## Memorize

You should memorize:

- HTML document skeleton,
- common semantic elements,
- common form structure,
- core attributes,
- stylesheet linking,
- basic CSS rule syntax,
- common selectors,
- box model,
- core Flexbox properties,
- core Grid structure,
- media-query structure,
- common units,
- DOM selection shape,
- event-listener shape,
- basic form event handling,
- basic HTTP concepts.

## Look Up When Needed

It is normal to look up:

- uncommon HTML elements,
- unusual input attributes,
- complex ARIA patterns,
- obscure CSS properties,
- advanced selectors,
- complex Grid syntax,
- animation details,
- browser compatibility,
- unusual media features,
- exact HTTP specifications.

Expertise means knowing **what tools exist, how they behave, and where to find precise details**, not memorizing the entire platform specification.

---

# 165. Final Mastery Standard

A strong HTML/CSS developer preparing for full-stack work should be able to start with an empty directory and create:

```text
website/
├── index.html
├── css/
│   └── styles.css
├── js/
│   └── script.js
└── images/
```

without needing a framework.

They should be able to:

- write the HTML document skeleton from memory,
- structure a page semantically,
- create accessible navigation,
- create links and images,
- build accessible forms,
- use native form validation,
- connect external stylesheets,
- understand CSS selectors,
- predict the cascade,
- reason about specificity and inheritance,
- reason about the box model,
- build layouts using normal flow, Flexbox, and Grid,
- create responsive interfaces,
- use appropriate CSS units,
- style interactive states,
- use custom properties,
- inspect and debug HTML/CSS with DevTools,
- understand the DOM,
- add small amounts of JavaScript behavior,
- respond to browser events,
- read form values,
- understand `preventDefault()`,
- understand basic `fetch()` usage,
- explain client/server communication,
- understand HTTP methods and status codes,
- explain HTTP vs HTTPS,
- understand localhost and ports,
- understand domains and DNS conceptually,
- understand origins and basic CORS,
- distinguish static hosting from full-stack architecture,
- and explain why frontend validation and frontend secrecy cannot replace backend security.

The end goal is:

> **Be able to build a complete, responsive, accessible browser interface from memory; understand why the HTML and CSS behave as they do; diagnose problems using the browser's tools; add enough JavaScript to make forms and basic interactions practical; and understand how that interface will eventually connect to a real backend and production hosting environment.**

---

# 166. Boundary of This Skill Map

After mastering this material, the next major subject should be a dedicated **JavaScript skill map**.

That map should expand into topics intentionally omitted here, including:

- JavaScript primitive/reference types,
- variables and scope,
- functions,
- arrays and objects,
- loops and iteration,
- destructuring,
- spread/rest,
- modules,
- closures,
- prototypes,
- classes,
- error handling,
- promises,
- `async` / `await`,
- asynchronous execution,
- deeper DOM programming,
- HTTP/API error handling,
- JSON,
- browser storage,
- npm,
- package management,
- testing,
- build tooling.

After JavaScript fundamentals are strong, TypeScript and a frontend framework can be added without using the framework to hide gaps in the underlying web platform knowledge.
