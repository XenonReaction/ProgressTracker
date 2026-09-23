# Git Version Control Expert Skill Map

## Purpose

This document is a structured skill map for becoming highly proficient with Git and version control.

The goal is not merely to memorize commands. A strong Git user should be able to:

- use the most common commands from memory,
- explain what those commands do,
- reason about repository state,
- collaborate safely with other developers,
- recover from mistakes,
- understand Git's internal model,
- diagnose common Git problems,
- and discuss Git confidently in a technical interview.

An expert Git user does **not** need every obscure command memorized. However, the most common 10–30 commands should generally be available from memory because they form the normal vocabulary of daily Git use.

---

# 1. Core Git Commands to Memorize

These are the commands most worth memorizing for daily development and interviews.

## Highest-Priority Commands

### 1. `git init`

Creates a new Git repository in the current directory.

```bash
git init
```

Use when starting version control for an existing local project.

---

### 2. `git clone`

Creates a local copy of an existing repository.

```bash
git clone <repository-url>
```

Example:

```bash
git clone https://github.com/user/project.git
```

---

### 3. `git status`

Shows the current state of the working directory and staging area.

```bash
git status
```

Use this constantly.

It tells you:

- current branch,
- modified files,
- staged files,
- untracked files,
- deleted files,
- whether your branch is ahead or behind its upstream branch.

---

### 4. `git add`

Moves changes into the staging area.

```bash
git add file.txt
```

Stage everything:

```bash
git add .
```

Interactively select changes:

```bash
git add -p
```

---

### 5. `git commit`

Creates a commit from staged changes.

```bash
git commit -m "Add user validation"
```

Modify the most recent commit:

```bash
git commit --amend
```

---

### 6. `git log`

Displays commit history.

```bash
git log
```

Useful compact version:

```bash
git log --oneline
```

Useful graph:

```bash
git log --oneline --graph --decorate --all
```

---

### 7. `git diff`

Shows changes.

Unstaged changes:

```bash
git diff
```

Staged changes:

```bash
git diff --staged
```

---

### 8. `git branch`

Lists or manages branches.

```bash
git branch
```

Create a branch:

```bash
git branch feature-login
```

Delete:

```bash
git branch -d feature-login
```

---

### 9. `git switch`

Switches branches.

```bash
git switch main
```

Create and switch:

```bash
git switch -c feature-login
```

---

### 10. `git merge`

Combines another branch into the current branch.

```bash
git switch main
git merge feature-login
```

---

### 11. `git fetch`

Downloads remote repository information without integrating it into the current branch.

```bash
git fetch
```

---

### 12. `git pull`

Downloads remote changes and integrates them into the current branch.

```bash
git pull
```

Often conceptually:

```text
git fetch
+
git merge
```

or, when configured:

```text
git fetch
+
git rebase
```

---

### 13. `git push`

Sends local commits to a remote repository.

```bash
git push
```

First push of a branch:

```bash
git push -u origin main
```

---

### 14. `git remote`

Manages connections to other repositories.

```bash
git remote -v
```

Add a remote:

```bash
git remote add origin <url>
```

Change a remote URL:

```bash
git remote set-url origin <url>
```

---

### 15. `git restore`

Restores file contents.

```bash
git restore file.txt
```

Unstage a file:

```bash
git restore --staged file.txt
```

---

### 16. `git reset`

Moves branch references and can modify the staging area and working tree.

```bash
git reset
```

Important modes:

```bash
git reset --soft
git reset --mixed
git reset --hard
```

---

### 17. `git revert`

Creates a new commit that reverses an earlier commit.

```bash
git revert <commit>
```

Useful for undoing changes that have already been shared.

---

### 18. `git rebase`

Moves or recreates commits on a new base.

```bash
git rebase main
```

Interactive:

```bash
git rebase -i HEAD~5
```

---

### 19. `git stash`

Temporarily stores uncommitted changes.

```bash
git stash
```

Restore and remove the stash:

```bash
git stash pop
```

List stashes:

```bash
git stash list
```

---

### 20. `git cherry-pick`

Applies the change from a specific commit onto the current branch.

```bash
git cherry-pick <commit>
```

---

### 21. `git show`

Displays information about a commit or Git object.

```bash
git show <commit>
```

---

### 22. `git reflog`

Shows recent movements of HEAD and other references.

```bash
git reflog
```

Extremely useful for recovering commits after mistakes.

---

### A Note on More Advanced Commands

A few more advanced commands — `git bisect`, `git blame`, and `git tag` — are covered later in the Expertise Ladder (Level 4) rather than here, since they're situational rather than everyday commands worth memorizing first.

---

## Useful Additional Commands

These are worth knowing even if they are not used every day.

```bash
git config
git clean
git worktree
git submodule
git grep
git shortlog
git fsck
git gc
git sparse-checkout
```

---

# 2. Version Control Fundamentals

Understand why version control exists.

Topics:

- version control systems,
- source history,
- tracking changes,
- restoring earlier versions,
- comparing versions,
- collaboration,
- parallel development,
- distributed version control,
- centralized version control,
- Git vs SVN/CVS,
- local repositories,
- remote repositories,
- repository history,
- working copies.

Git is a **distributed version control system**.

Each normal Git clone contains its own local repository and history.

---

# 3. Git's Core Mental Model

This is one of the most important parts of Git.

```text
Working Directory
       ↓
Staging Area / Index
       ↓
Local Repository
       ↓
Remote Repository
```

This diagram shows the forward flow (add → commit → push). Data also flows the other direction — `fetch`/`pull` bring remote changes down, and `checkout`/`restore`/`reset` move changes from a repo back into the staging area or working directory — covered in the Undoing Changes section below.

## Working Directory

Files currently checked out and editable.

## Staging Area / Index

The proposed contents of the next commit.

## Local Repository

Git's local database containing commits and other objects.

## Remote Repository

Another Git repository known to your local repository.

Typical remote:

```text
origin
```

---

# 4. Essential Git Terminology

Know and be able to explain:

- repository
- working tree
- working directory
- index
- staging area
- tracked file
- untracked file
- modified file
- staged file
- commit
- commit hash
- SHA
- branch
- tag
- reference
- ref
- HEAD
- detached HEAD
- remote
- origin
- upstream
- local branch
- remote-tracking branch
- clone
- fork
- merge
- merge commit
- fast-forward merge
- rebase
- squash
- cherry-pick
- conflict
- diff
- patch
- stash
- reflog
- ancestry
- parent commit
- merge base

Advanced terminology:

- Git object
- blob
- tree
- commit object
- annotated tag
- symbolic reference
- DAG
- reachability
- packfile

---

# 5. Repository Creation

Know:

```bash
git init
git clone
```

## `git init`

Creates a new repository.

## `git clone`

Creates a local repository based on another repository.

Also understand:

```bash
git init --bare
```

A **bare repository** contains repository data without a normal working directory.

---

# 6. Inspecting Repository State

Important commands:

```bash
git status
git log
git diff
git show
```

Useful log commands:

```bash
git log --oneline
git log --graph
git log --all
git log --decorate
git log --stat
```

Common combination:

```bash
git log --oneline --graph --decorate --all
```

You should be able to answer:

- What branch am I on?
- What files changed?
- What is staged?
- What is unstaged?
- What was the last commit?
- How do these branches relate?
- Is my branch ahead or behind its remote counterpart?

---

# 7. Creating Commits

Core commands:

```bash
git add
git commit
```

Examples:

```bash
git add file.txt
git add .
git add -A
git add -p
```

`git add -p` is especially useful because it allows staging individual chunks of changes.

Commit:

```bash
git commit -m "Add input validation"
```

Amend:

```bash
git commit --amend
```

Important concept:

**Amending a commit creates a new commit.**

Git does not literally edit the existing commit object.

---

# 8. Commit Theory

A commit contains a snapshot plus metadata and parent information.

Conceptually:

```text
Commit C
  ↓
Commit B
  ↓
Commit A
```

A branch can be viewed as a movable reference:

```text
main
 ↓
 C
 ↓
 B
 ↓
 A
```

A branch is not a folder.

It is effectively a reference pointing to a commit.

---

# 9. Branching

Commands:

```bash
git branch
git switch
git checkout
```

Create:

```bash
git branch feature
```

or:

```bash
git switch -c feature
```

Switch:

```bash
git switch feature
```

Delete:

```bash
git branch -d feature
```

Force delete:

```bash
git branch -D feature
```

Rename:

```bash
git branch -m old-name new-name
```

Understand:

- feature branches,
- release branches,
- hotfix branches,
- long-lived branches,
- short-lived branches,
- divergence,
- stale branches,
- branch ancestry.

---

# 10. HEAD

HEAD represents your current checkout position.

Normally:

```text
HEAD → main → commit
```

Understand:

```text
HEAD
HEAD^
HEAD~1
HEAD~2
HEAD~5
```

Know:

- detached HEAD,
- how detached HEAD happens,
- committing while detached,
- recovering detached-HEAD work.

---

# 11. Merging

Command:

```bash
git merge
```

Example:

```bash
git switch main
git merge feature
```

Understand:

- fast-forward merge,
- three-way merge,
- merge commit,
- merge base,
- branch ancestry,
- `--no-ff`,
- `--ff-only`.

The branch currently checked out is normally the branch being changed.

---

# 12. Merge Conflicts

Conflicts happen when Git cannot safely determine how competing changes should be combined.

Example:

```text
Developer A:
color = "red"

Developer B:
color = "blue"
```

Conflict markers:

```text
<<<<<<< HEAD
color = "red"
=======
color = "blue"
>>>>>>> feature
```

Typical resolution process:

```bash
git status
```

Edit the file manually.

Then:

```bash
git add file
git commit
```

Conflict categories include:

- modify/modify,
- add/add,
- delete/modify,
- rename conflicts,
- binary conflicts.

---

# 13. Avoiding Merge Conflicts

Practices:

- make small commits,
- use short-lived branches,
- synchronize frequently,
- communicate about overlapping work,
- avoid giant formatting changes mixed with logic changes,
- keep code modular,
- integrate frequently,
- avoid letting branches diverge for long periods.

Important:

A merge conflict does not necessarily mean someone made a mistake.

Sometimes two legitimate changes overlap.

---

# 14. Rebase

Command:

```bash
git rebase
```

Example:

```bash
git rebase main
```

Conceptually:

Before:

```text
A---B---C main
     \
      D---E feature
```

After rebase:

```text
A---B---C main
         \
          D'---E' feature
```

Important:

```text
D ≠ D'
E ≠ E'
```

Rebase creates new commits with new identities.

Understand the tradeoff:

- merge preserves branch structure,
- rebase can create a cleaner linear history,
- rebase rewrites history.

---

# 15. Interactive Rebase

Command:

```bash
git rebase -i
```

Operations include:

- pick
- reword
- edit
- squash
- fixup
- drop
- reorder

Uses:

- combine commits,
- reorder commits,
- rewrite messages,
- remove commits,
- clean up a branch before integration.

---

# 16. Squashing

Squashing combines multiple commits into fewer commits.

Example history:

```text
Add login page
Fix typo
Fix CSS
Oops
Fix tests
```

Could become:

```text
Implement login page
```

Know:

- interactive rebase squashing,
- squash merging,
- advantages,
- disadvantages,
- when preserving granular history is useful.

---

# 17. Remotes

Commands:

```bash
git remote
git remote -v
git remote add
git remote remove
git remote rename
git remote set-url
```

Example:

```bash
git remote add origin https://github.com/user/repo.git
```

Names like:

```text
origin
upstream
```

are conventions.

Git allows arbitrary remote names.

---

# 18. Fetch

Command:

```bash
git fetch
```

It:

- communicates with remotes,
- downloads Git objects,
- updates remote-tracking references,
- normally does not change your working files.

Understand:

```text
origin/main
```

This is a **local remote-tracking reference**, not the actual branch physically living on GitHub.

---

# 19. Pull

Command:

```bash
git pull
```

Conceptually often:

```text
git fetch
+
git merge
```

or:

```text
git fetch
+
git rebase
```

Know:

```bash
git pull --rebase
```

---

# 20. Push

Command:

```bash
git push
```

Explicit form:

```bash
git push origin main
```

Initial upstream setup:

```bash
git push -u origin main
```

Understand:

- upstream tracking,
- rejected pushes,
- non-fast-forward errors,
- force pushes.

---

# 21. Force Push

Commands:

```bash
git push --force
```

Safer alternative:

```bash
git push --force-with-lease
```

Understand why force pushing can overwrite remote history.

---

# 22. Undoing Changes

Three important commands:

```bash
git restore
git reset
git revert
```

They solve different problems.

## Restore

Usually deals with file contents.

```bash
git restore file.txt
```

## Reset

Moves refs and may affect the index and working tree.

```bash
git reset
git reset --soft
git reset --mixed
git reset --hard
```

## Revert

Creates a new commit reversing another commit.

```bash
git revert <commit>
```

General principle:

If history has already been shared, `revert` is often safer than rewriting history.

---

# 23. Reflog

Command:

```bash
git reflog
```

Reflog records movements of references such as HEAD.

Useful after operations such as:

```bash
git reset --hard HEAD~5
```

A commit that disappears from normal `git log` output may still be recoverable through the reflog.

---

# 24. Stashing

Commands:

```bash
git stash
git stash push
git stash list
git stash show
git stash pop
git stash apply
git stash drop
```

Understand:

- stash vs temporary commit,
- staged vs unstaged changes,
- stashing untracked files,
- when stash is useful.

---

# 25. Cherry-Pick

Command:

```bash
git cherry-pick <commit>
```

It applies the change introduced by another commit and creates a new commit on the current branch.

Uses:

- hotfixes,
- moving individual fixes,
- recovering work,
- applying one change without merging an entire branch.

---

# 26. Tags

Command:

```bash
git tag
```

Understand:

- lightweight tags,
- annotated tags,
- release tags,
- semantic versioning.

Examples:

```text
v1.0.0
v1.1.0
v2.0.0
```

Commands:

```bash
git tag v1.0.0
git tag -a v1.0.0 -m "release message"
git push origin v1.0.0
git push --tags
```

Note: running `git tag -a v1.0.0` without `-m "message"` does not fail — it opens your configured editor to write the annotation message.

---

# 27. `.gitignore`

Used to prevent specified untracked files from being added automatically.

Examples:

```text
node_modules/
target/
.env
*.log
.idea/
```

Important:

`.gitignore` does not automatically stop tracking a file that is already tracked.

---

# 28. `.gitattributes`

Understand its role in:

- line-ending normalization,
- binary files,
- diff behavior,
- merge behavior,
- Git LFS integration,
- export behavior.

---

# 29. Git Configuration

Command:

```bash
git config
```

Scopes:

```bash
git config --system
git config --global
git config --local
```

Examples:

```bash
git config --global user.name "Name"
git config --global user.email "email@example.com"
```

Other configuration topics:

- default branch,
- aliases,
- editor,
- merge tools,
- credential helpers,
- line endings,
- pull behavior.

---

# 30. Authentication

Understand the distinction between Git and authentication to a Git host.

Authentication methods:

- HTTPS,
- SSH,
- SSH keys,
- personal access tokens,
- credential managers,
- credential helpers,
- deploy keys,
- service credentials.

Related commands:

```bash
ssh-keygen
ssh-agent
```

---

# 31. Git Hosting / Remote Repository Hosting

A more technical term than "offsite storage" is:

**remote repository hosting**

or:

**Git hosting**

Common services:

- GitHub
- GitLab
- Bitbucket
- Azure Repos
- self-hosted Git servers

These systems add services around Git such as:

- repository hosting,
- authentication,
- permissions,
- code review,
- pull requests,
- merge requests,
- issue tracking,
- CI/CD,
- protected branches,
- releases,
- project management.

---

# 32. Forks

A fork is primarily a hosting-platform feature.

Common structure:

```text
Original repository
       ↓
Your fork
       ↓
Your local clone
```

Typical remotes:

```text
origin   → your fork
upstream → original project
```

---

# 33. Pull Requests and Merge Requests

GitHub generally calls them:

```text
Pull Requests
```

GitLab generally calls them:

```text
Merge Requests
```

Understand:

- base branch,
- compare/head branch,
- code review,
- comments,
- approvals,
- checks,
- requested changes,
- draft PRs,
- merge strategies,
- protected branches.

Important:

Pull requests are **not part of Git itself**.

They are features provided by repository hosting platforms.

---

# 34. Common Git Workflows

## Feature Branching

```text
main
  \
   feature
```

## GitHub Flow

Short-lived branch → pull request → review → merge into main.

## GitLab Flow

Workflow variants combining feature branches, releases, and environments.

## Git Flow

Common branches:

```text
main
develop
feature/*
release/*
hotfix/*
```

## Trunk-Based Development

Developers integrate frequently into a primary trunk such as:

```text
main
```

Understand the tradeoffs of each workflow.

---

# 35. Commit Quality

Good commits should generally:

- represent one logical change,
- build or work when practical,
- avoid unrelated changes,
- avoid generated garbage,
- avoid secrets,
- have meaningful messages.

Examples:

```text
Add validation to registration form
Fix null handling in MessageService
Refactor database configuration
```

Optional convention:

```text
feat:
fix:
docs:
refactor:
test:
chore:
```

---

# 36. History Inspection

Commands:

```bash
git show
git diff
git diff HEAD
git diff branch1..branch2
git diff --staged
git shortlog
```

Understand:

- commit ranges,
- ancestry notation,
- revision syntax.

---

# 37. Blame

Command:

```bash
git blame file.txt
```

Shows which commit most recently modified each line.

It is useful for discovering historical context.

---

# 38. Bisect

Command:

```bash
git bisect
```

Uses binary search across commit history to locate the commit that introduced a regression.

Conceptually:

```text
Known good ← history → Known bad
```

Git repeatedly checks intermediate commits until the responsible commit is isolated.

---

# 39. Git Internals

Understand Git's major object types:

```text
blob
tree
commit
tag
```

Understand the purpose of:

```text
.git/
```

Important contents include:

```text
.git/objects
.git/refs
.git/HEAD
.git/config
.git/index
```

---

# 40. Git Object Model

Git is fundamentally a **content-addressed object database**.

Conceptually:

```text
commit
   ↓
 tree
 ├── blob
 ├── blob
 └── tree
      └── blob
```

Hashes identify objects.

Understanding this explains many Git behaviors.

---

# 41. Commit Graph / DAG

Git history is a:

**Directed Acyclic Graph**

Example:

```text
A---B---C---F
     \     /
      D---E
```

Commits point to parent commits.

This model explains:

- merging,
- rebasing,
- ancestry,
- merge bases,
- fast forwards,
- branches.

---

# 42. References

Understand refs such as:

```text
refs/heads/main
refs/remotes/origin/main
refs/tags/v1.0
```

Also understand symbolic references such as:

```text
HEAD
```

---

# 43. Plumbing vs Porcelain

## Porcelain Commands

Normal user-facing commands:

```text
git add
git commit
git switch
git merge
```

## Plumbing Commands

Lower-level commands:

```text
git hash-object
git cat-file
git ls-tree
git write-tree
git commit-tree
git update-ref
```

An expert does not need to use plumbing commands daily, but should understand what they expose about Git's internals.

---

# 44. History Rewriting

Commands and tools:

```bash
git commit --amend
git rebase -i
git reset
git filter-repo
```

Understand:

- rewriting history,
- new commit hashes,
- dangers of rewriting shared history,
- when rewriting private branch history is appropriate.

A commit's identity depends on its contents, metadata, and ancestry.

Changing those creates a new commit.

---

# 45. Recovery

Be able to recover from:

- accidental reset,
- deleted branch,
- bad rebase,
- bad merge,
- detached HEAD,
- accidental amend,
- force push,
- lost commit,
- deleted file,
- wrong commit,
- wrong branch.

Important tools:

```bash
git reflog
git reset
git restore
git revert
git cherry-pick
git fsck
```

Recovery ability is one of the strongest signs of Git expertise.

---

# 46. Rebase and Merge Recovery

Know:

```bash
git merge --abort
git rebase --abort
git rebase --continue
git rebase --skip
```

Before using them, inspect the repository state with:

```bash
git status
```

---

# 47. Remote Problems

Know how to diagnose errors such as:

```text
remote origin already exists
```

```text
No such remote 'origin'
```

```text
repository not found
```

```text
permission denied (publickey)
```

```text
non-fast-forward
```

```text
refusing to merge unrelated histories
```

```text
failed to push some refs
```

Useful diagnostics:

```bash
git remote -v
git branch -vv
git fetch
git status
```

---

# 48. Branch Problems

Be able to handle:

- wrong branch,
- branch created from wrong base,
- accidental commit to `main`,
- branch behind remote,
- branch ahead of remote,
- diverged branches,
- deleted branches,
- stale branches,
- renamed branches,
- deleted remote branches.

---

# 49. File Problems

Understand:

- accidentally tracked files,
- ignored files that remain tracked,
- filename case sensitivity,
- file permissions,
- line-ending differences,
- huge binaries,
- generated files,
- accidentally committed secrets.

---

# 50. Line Endings

Understand:

```text
LF
CRLF
```

This is especially important when moving between:

- Windows,
- WSL,
- Linux,
- macOS.

Know about:

```text
core.autocrlf
.gitattributes
```

---

# 51. Secrets and Security

Avoid committing:

```text
.env
API keys
passwords
private keys
tokens
database credentials
```

Important principle:

Deleting a secret in a later commit does **not** remove it from earlier Git history.

Understand:

- secret rotation,
- history rewriting,
- secret scanning,
- protected branches,
- signed commits,
- least-privilege credentials.

---

# 52. Commit and Tag Signing

Understand:

- GPG signing,
- SSH signing,
- signed commits,
- signed tags,
- verification.

---

# 53. Git Hooks

Git hooks live under:

```text
.git/hooks/
```

Common hooks:

```text
pre-commit
commit-msg
pre-push
post-merge
```

Uses:

- formatting,
- linting,
- testing,
- validating commit messages,
- preventing dangerous commits.

---

# 54. CI/CD Relationship

Git often triggers automation.

Example:

```text
push
 ↓
CI pipeline
 ↓
build
 ↓
test
 ↓
deploy
```

Common systems:

- GitHub Actions,
- GitLab CI,
- Jenkins,
- Azure Pipelines.

---

# 55. Git LFS

Git Large File Storage helps handle large binary files.

Understand:

- why standard Git performs poorly with large frequently changing binaries,
- pointer files,
- Git LFS storage,
- when LFS is appropriate.

---

# 56. Submodules

Command:

```bash
git submodule
```

A repository can reference a particular commit in another repository.

Understand common issues:

- synchronization,
- cloning,
- updating,
- nested repositories,
- detached HEAD behavior.

---

# 57. Subtrees

Understand the general difference between:

```text
submodule
```

and:

```text
subtree
```

Both provide approaches for incorporating another repository into a project.

---

# 58. Worktrees

Command:

```bash
git worktree
```

Allows multiple working directories connected to a single Git repository.

Useful for simultaneous work such as:

```text
main
feature-a
hotfix
```

without constantly switching branches.

---

# 59. Repository Maintenance

Understand conceptually:

```bash
git gc
git fsck
git prune
```

Topics:

- garbage collection,
- unreachable objects,
- object packing,
- repository integrity,
- packfiles.

Git normally manages most maintenance automatically.

---

# 60. Large Repository Techniques

Advanced topics:

- shallow clones,
- partial clones,
- sparse checkout,
- Git LFS,
- commit graph optimization,
- packfiles,
- monorepos.

Commands/features include:

```bash
git clone --depth
git sparse-checkout
```

---

# 61. Team Repository Governance

Expert Git usage also includes team practices.

Understand:

- protected branches,
- required reviews,
- required CI checks,
- CODEOWNERS,
- merge policies,
- branch naming conventions,
- release procedures,
- permissions,
- deployment controls.

---

# 62. Common Dangerous Operations

Recognize potentially destructive commands:

```bash
git reset --hard
git clean -fd
git push --force
git branch -D
git rebase
```

These commands are not inherently bad.

The important skill is understanding exactly:

- what state they modify,
- what history they rewrite,
- whether recovery is possible,
- whether the history has already been shared.

---

# 63. Debugging Git

When Git behaves unexpectedly, inspect before acting.

Start with:

```bash
git status
git log --oneline --graph --decorate --all
git branch -vv
git remote -v
git diff
git reflog
```

Then answer:

```text
Where is HEAD?

Where does my local branch point?

Where does the remote-tracking branch point?

Which commits exist?

What is staged?

What is modified?

What is untracked?
```

Most Git problems become easier once repository state is understood.

---

# 64. Git Help System

Useful commands:

```bash
git help
git help commit
git help rebase
```

Equivalent:

```bash
git commit --help
```

An expert does not memorize every option.

They know enough Git vocabulary and concepts to find the correct option quickly.

---

# 65. Practical Expertise Ladder

## Level 1 — Basic User

Know:

- `init`
- `clone`
- `status`
- `add`
- `commit`
- `log`
- `push`
- `pull`

You can version a personal project.

---

## Level 2 — Working Developer

Know:

- branches,
- `switch`,
- `merge`,
- remotes,
- `fetch`,
- `.gitignore`,
- merge conflicts,
- remote tracking.

You can collaborate safely on a normal development team.

---

## Level 3 — Proficient User

Know:

- rebase,
- squash,
- cherry-pick,
- stash,
- reset,
- restore,
- revert,
- tags,
- reflog.

You can manipulate history and recover from normal mistakes.

---

## Level 4 — Advanced User

Understand:

- interactive rebase,
- bisect,
- hooks,
- worktrees,
- submodules,
- authentication,
- repository security,
- workflow design,
- CI/CD integration,
- advanced recovery.

You can diagnose complicated Git problems and design effective team workflows.

---

## Level 5 — Expert

Understand:

- Git object model,
- blobs,
- trees,
- commit objects,
- refs,
- HEAD,
- DAGs,
- merge bases,
- reachability,
- packfiles,
- plumbing,
- history rewriting,
- repository administration,
- complex recovery,
- large repository techniques,
- Git governance.

At this level you should be able to reason about Git's behavior rather than merely remember recipes.

For example, given:

```text
A---B---C---F
     \     /
      D---E
```

you should be able to explain how operations such as:

```text
merge
rebase
reset
revert
cherry-pick
branch
HEAD
```

will affect the commit graph.

---

# 66. Interview-Focused Knowledge

For a typical software engineering interview, prioritize being able to explain the following clearly.

## Essential Commands

Memorize:

```text
git init
git clone
git status
git add
git commit
git log
git diff
git branch
git switch
git merge
git fetch
git pull
git push
git remote
git restore
git reset
git revert
git rebase
git stash
git cherry-pick
git reflog
```

You should recognize:

```text
git show
git tag
git blame
git bisect
```

---

## Essential Concepts

Be able to explain:

- repository
- working directory
- staging area
- commit
- branch
- HEAD
- local vs remote repository
- origin
- upstream
- tracked vs untracked files
- merge
- rebase
- squash
- merge conflict
- fetch vs pull
- reset vs revert
- restore
- stash
- cherry-pick
- reflog
- pull request
- fork
- `.gitignore`

---

## Common Interview Questions

You should be prepared for questions such as:

1. What is Git?
2. What is version control?
3. What is distributed version control?
4. What is the difference between Git and GitHub?
5. What is a repository?
6. What is a commit?
7. What is a branch?
8. What is HEAD?
9. What is the staging area?
10. What does `git add` actually do?
11. What is the difference between `git fetch` and `git pull`?
12. What is the difference between merge and rebase?
13. What is a merge conflict?
14. How do you resolve a merge conflict?
15. What is a fast-forward merge?
16. What is a remote?
17. What is `origin`?
18. What is an upstream branch?
19. What is a fork?
20. What is a pull request?
21. What is the difference between `reset` and `revert`?
22. What is `git stash` used for?
23. What does `git cherry-pick` do?
24. What is interactive rebase?
25. What does squashing commits mean?
26. What is detached HEAD?
27. What does `git reflog` do?
28. How would you recover a lost commit?
29. Why should you avoid force pushing shared branches?
30. What is `.gitignore`?
31. Why might a file still be tracked after adding it to `.gitignore`?
32. How do developers reduce merge conflicts?
33. What happens internally when you amend or rebase a commit?
34. How would you undo a commit that has already been pushed?
35. How would you investigate a bug introduced somewhere in Git history?

---

# 67. Recommended Learning Priority

A useful progression is:

```text
Version control theory
        ↓
Working tree / staging / repository
        ↓
status / add / commit / log / diff
        ↓
branches / HEAD / switch
        ↓
remotes / fetch / pull / push
        ↓
merge / conflicts
        ↓
restore / reset / revert
        ↓
rebase / squash / cherry-pick
        ↓
stash / reflog / recovery
        ↓
team workflows
        ↓
Git internals
```

Do not begin by trying to memorize every unusual Git command.

First become extremely comfortable reasoning about:

```text
Working Tree
Staging Area
Commit History
Branches
HEAD
Remote-Tracking Branches
```

Those concepts explain most everyday Git behavior.

---

# 68. Final Standard for Git Expertise

A Git expert should be able to:

- use common Git commands without constantly looking them up,
- understand repository state,
- explain the staging model,
- create clean commits,
- manage branches,
- work with remotes,
- resolve merge conflicts,
- choose between merge and rebase,
- safely undo changes,
- recover apparently lost commits,
- understand shared-history risks,
- diagnose remote and branch problems,
- design team workflows,
- reason about the Git commit graph,
- explain Git's internal object model,
- and know when to consult documentation for unusual operations.

The goal is not memorization for its own sake.

The goal is to understand Git well enough that the most common commands become natural vocabulary while the underlying model lets you reason through unfamiliar situations.
