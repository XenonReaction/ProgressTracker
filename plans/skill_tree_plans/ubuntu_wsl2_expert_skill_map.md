# Ubuntu on WSL2 — Expert Skill Map

## Purpose

This skill map targets **Ubuntu running under WSL2 for software development**.

The goal is to become able to:

- operate the Linux command line comfortably from memory,
- understand where you are and what state the system is in,
- navigate and manipulate files safely,
- understand the Linux filesystem hierarchy,
- use permissions, users, `sudo`, packages, processes, environment variables, and networking,
- combine commands with pipes, redirection, expansion, and shell operators,
- write Bash scripts that automate routine development work,
- understand theoretically and practically what WSL2 is,
- understand how Windows and Linux interact under WSL2,
- diagnose common WSL2/Linux development problems without blindly copying commands,
- and explain the environment confidently in a technical interview.

This is **not** intended to replace separate skill maps for Git, Docker, Java, Python, Node.js, databases, or other development tools. Those tools appear here only where their interaction with Linux/WSL2 matters.

---

# 1. Commands and Operators Worth Memorizing

An expert does not memorize every Linux command or every flag. However, common commands and shell operators should become normal vocabulary.

## Tier 1 — Essential Daily Commands

### `pwd`

Print the current working directory.

```bash
pwd
```

Example output:

```text
/home/user/workspace/project
```

You should always be able to answer:

> Where am I in the filesystem?

---

### `ls`

List directory contents.

```bash
ls
```

Common forms:

```bash
ls -l
ls -a
ls -la
ls -lh
```

Know what the long listing displays:

- permissions,
- link count,
- owner,
- group,
- size,
- modification time,
- filename.

---

### `cd`

Change directory.

```bash
cd project
```

Important forms:

```bash
cd ..
cd ~
cd /
cd -
cd
```

Know the difference between absolute and relative paths.

---

### `mkdir`

Create directories.

```bash
mkdir project
```

Create parent directories when necessary:

```bash
mkdir -p projects/java/demo
```

---

### `touch`

Commonly used to create an empty file or update a file's timestamps.

```bash
touch README.md
```

---

### `cp`

Copy files or directories.

```bash
cp source.txt destination.txt
```

Copy a directory recursively:

```bash
cp -r source-dir destination-dir
```

---

### `mv`

Move or rename files and directories.

Move:

```bash
mv file.txt archive/
```

Rename:

```bash
mv old-name.txt new-name.txt
```

---

### `rm`

Remove files.

```bash
rm file.txt
```

Remove recursively:

```bash
rm -r directory
```

Force removal:

```bash
rm -f file.txt
```

Potentially dangerous:

```bash
rm -rf directory
```

Understand exactly what a path refers to before using recursive forced deletion.

---

### `rmdir`

Remove an **empty** directory.

```bash
rmdir directory
```

This differs from:

```bash
rm -r directory
```

which recursively removes directory contents.

---

### `cat`

Print or concatenate file contents.

```bash
cat file.txt
```

Useful for small files.

---

### `less`

View text one screen at a time.

```bash
less large-file.log
```

Learn basic navigation:

```text
Space       next page
b           previous page
/search     search
n           next match
q           quit
```

---

### `nano`

Terminal text editor.

```bash
nano file.txt
```

Know at minimum:

- edit text,
- save,
- exit,
- search,
- cancel an operation.

Common shortcuts:

```text
Ctrl+O   Write Out / save
Ctrl+X   Exit
Ctrl+W   Search
Ctrl+K   Cut line
Ctrl+U   Paste
```

The `^` shown in Nano means `Ctrl`.

---

### `clear`

Clear the terminal display.

```bash
clear
```

Common shortcut:

```text
Ctrl+L
```

---

### `history`

Display shell command history.

```bash
history
```

Useful shell shortcuts:

```text
Up Arrow     previous command
Down Arrow   next command
Ctrl+R       reverse history search
```

---

### `man`

Open a command's manual page.

```bash
man ls
```

Also commonly:

```bash
ls --help
```

An expert knows how to find unfamiliar options rather than memorizing every flag.

---

### `which`

Find the executable selected from `$PATH`.

```bash
which java
```

For richer shell-aware inspection, also know:

```bash
command -v java
type java
```

---

### `echo`

Print text or expanded values.

```bash
echo "Hello"
```

Environment variable:

```bash
echo "$PATH"
```

---

### `sudo`

Execute an authorized command with elevated privileges, commonly as root.

```bash
sudo apt update
```

Understand `sudo`; do not simply prepend it whenever a command fails.

---

### `apt`

Ubuntu's common command-line package-management interface.

Refresh package information:

```bash
sudo apt update
```

Upgrade installed packages:

```bash
sudo apt upgrade
```

Install:

```bash
sudo apt install curl
```

Remove:

```bash
sudo apt remove curl
```

Search:

```bash
apt search curl
```

Important order:

```bash
sudo apt update && sudo apt upgrade
```

`update` refreshes package metadata.

`upgrade` upgrades installed packages using the available package metadata.

---

## Tier 2 — Core Development-Shell Commands

### `find`

Search directory trees.

```bash
find . -name "*.java"
```

Find directories:

```bash
find . -type d -name "target"
```

Find files:

```bash
find . -type f -name ".env*"
```

Understand:

```text
.
-type f
-type d
-name
-iname
```

---

### `grep`

Search text.

```bash
grep "ERROR" app.log
```

Recursive search:

```bash
grep -R "MessageService" .
```

Case-insensitive:

```bash
grep -i "error" app.log
```

Show line numbers:

```bash
grep -n "error" app.log
```

---

### `head`

Display the beginning of input.

```bash
head file.txt
```

Example:

```bash
head -n 20 file.txt
```

---

### `tail`

Display the end of input.

```bash
tail file.txt
```

Follow a changing log:

```bash
tail -f application.log
```

---

### `wc`

Count lines, words, or bytes.

```bash
wc file.txt
```

Common:

```bash
wc -l file.txt
```

---

### `sort`

Sort input.

```bash
sort names.txt
```

---

### `uniq`

Filter adjacent duplicate lines.

Often combined with `sort`:

```bash
sort names.txt | uniq
```

Count occurrences:

```bash
sort names.txt | uniq -c
```

---

### `file`

Identify the apparent type of a file.

```bash
file program
```

---

### `stat`

Display detailed file metadata.

```bash
stat file.txt
```

---

### `ln`

Create links.

Symbolic link:

```bash
ln -s target link-name
```

Understand symbolic links versus hard links.

---

### `chmod`

Change file permissions.

```bash
chmod +x script.sh
```

Also understand numeric notation:

```bash
chmod 644 file.txt
chmod 755 script.sh
```

---

### `chown`

Change ownership.

```bash
sudo chown user:user file.txt
```

Understand why recursively changing ownership can be dangerous.

---

### `ps`

Display process information.

```bash
ps
```

Common:

```bash
ps aux
```

---

### `top`

Interactive process/system monitor.

```bash
top
```

Optionally recognize `htop` as a commonly installed alternative.

---

### `kill`

Send a signal to a process.

```bash
kill PID
```

Force termination:

```bash
kill -9 PID
```

Understand why `kill -9` should not be the default.

---

### `jobs`

Display jobs managed by the current shell.

```bash
jobs
```

---

### `fg`

Bring a background/stopped job to the foreground.

```bash
fg
```

---

### `bg`

Resume a stopped job in the background.

```bash
bg
```

---

### `curl`

Transfer data to/from URLs and network services.

```bash
curl https://example.com
```

Frequently used for:

- testing HTTP endpoints,
- downloading resources,
- interacting with APIs,
- installation scripts.

Understand that piping downloaded content directly into a shell has security implications.

---

### `wget`

Common command-line downloader.

```bash
wget <url>
```

Know the general distinction between `wget` and `curl`.

---

### `tar`

Create and extract archive files.

Extract:

```bash
tar -xf archive.tar
```

Create:

```bash
tar -cf archive.tar directory/
```

Compressed archives often use options such as:

```bash
tar -xzf archive.tar.gz
```

---

### `zip` / `unzip`

Create and extract ZIP archives.

```bash
zip archive.zip file.txt
unzip archive.zip
```

---

### `df`

Show filesystem disk-space usage.

```bash
df -h
```

---

### `du`

Estimate file/directory disk usage.

```bash
du -sh directory
```

---

### `free`

Display memory usage.

```bash
free -h
```

---

### `uname`

Display kernel/system information.

```bash
uname -a
```

Useful for confirming that you are running a WSL kernel.

---

### `whoami`

Display the current username.

```bash
whoami
```

---

### `id`

Display user and group IDs.

```bash
id
```

---

### `env`

Display environment variables.

```bash
env
```

---

### `export`

Create/export environment variables into the environment inherited by child processes.

```bash
export APP_ENV=development
```

---

### `source`

Execute commands from a file in the current shell.

```bash
source ~/.bashrc
```

Equivalent Bash shorthand:

```bash
. ~/.bashrc
```

---

# 2. Shell Operators Worth Memorizing

Commands are only half of command-line proficiency.

Shell syntax is equally important.

## Pipe

```bash
|
```

Send standard output from one command to standard input of another.

```bash
ps aux | grep java
```

---

## Output Redirection

Overwrite/create:

```bash
>
```

Example:

```bash
echo "hello" > file.txt
```

Append:

```bash
>>
```

Example:

```bash
echo "another line" >> file.txt
```

---

## Input Redirection

```bash
<
```

Example:

```bash
command < input.txt
```

---

## Error Redirection

Standard error:

```bash
2>
```

Example:

```bash
command 2> errors.log
```

Standard output and error:

```bash
command > output.log 2>&1
```

Also recognize Bash shorthand:

```bash
command &> output.log
```

---

## AND Operator

```bash
&&
```

Run the next command only if the previous command succeeds.

```bash
sudo apt update && sudo apt upgrade
```

---

## OR Operator

```bash
||
```

Run the next command if the previous command fails.

```bash
command || echo "Command failed"
```

---

## Sequential Commands

```bash
;
```

Run commands sequentially regardless of the previous exit status.

```bash
echo one; echo two
```

---

## Background Operator

```bash
&
```

Run a command as a background job.

```bash
long-command &
```

---

## Command Substitution

```bash
$(...)
```

Example:

```bash
current_dir=$(pwd)
```

---

## Wildcards / Globbing

All matching names:

```bash
*
```

Single character:

```bash
?
```

Example:

```bash
ls *.java
```

Understand that globbing is normally performed by the shell before the command receives its arguments.

---

## Home Expansion

```bash
~
```

Usually represents your home directory.

```bash
cd ~
```

---

## Environment / Shell Variable Expansion

```bash
$VARIABLE
```

Safer explicit form:

```bash
${VARIABLE}
```

Example:

```bash
echo "$HOME"
```

---

# 3. Understanding Command Syntax

Most command-line programs follow a pattern resembling:

```text
command [options] [arguments]
```

Example:

```bash
ls -la /home/user
```

Breakdown:

```text
ls           command
-la          options
/home/user   argument
```

Understand:

- commands,
- arguments,
- options,
- short options,
- long options,
- positional arguments,
- quoted arguments.

Examples:

```bash
ls -a
find . --help
mkdir "My Project"
```

Do not assume every program follows identical option rules.

---

# 4. Quoting and Escaping

Understand the difference between:

```bash
echo hello world
```

and:

```bash
echo "hello world"
```

## Double Quotes

Variables are expanded:

```bash
echo "$HOME"
```

## Single Quotes

Contents are treated more literally:

```bash
echo '$HOME'
```

prints:

```text
$HOME
```

## Escape Character

Backslash:

```bash
\
```

Example:

```bash
cd My\ Project
```

Quoting correctly is essential for reliable shell scripts.

---

# 5. Linux Filesystem Mental Model

Linux uses one filesystem tree rooted at:

```text
/
```

There are not drive letters like:

```text
C:
D:
```

in normal Linux path syntax.

Typical structure:

```text
/
├── bin
├── boot
├── dev
├── etc
├── home
│   └── user
├── lib
├── media
├── mnt
│   └── c
├── opt
├── proc
├── root
├── run
├── srv
├── sys
├── tmp
├── usr
└── var
```

---

# 6. Important Linux Directories

## `/`

Filesystem root.

Not the same as the root user's home directory.

---

## `/home`

Normal users' home directories.

Example:

```text
/home/user
```

---

## `/root`

Home directory for the root user.

---

## `/etc`

System-wide configuration.

Examples may include:

```text
/etc/hosts
/etc/passwd
/etc/apt/
```

---

## `/usr`

Contains many user-space programs, libraries, and shared resources.

Important paths:

```text
/usr/bin
/usr/local/bin
/usr/lib
```

---

## `/var`

Variable system data.

Common contents include:

- logs,
- caches,
- package data,
- application state.

---

## `/tmp`

Temporary files.

---

## `/dev`

Device representations.

---

## `/proc`

Virtual filesystem exposing process and kernel information.

---

## `/sys`

Kernel/device information and interfaces.

---

## `/mnt`

Common mount point.

Under WSL, Windows drives are commonly exposed here:

```text
/mnt/c
/mnt/d
```

---

# 7. Paths

Understand **absolute paths**:

```text
/home/user/workspace/project
```

They start from `/`.

Understand **relative paths**:

```text
../project
./script.sh
```

They are interpreted relative to the current working directory.

Special path components:

```text
.     current directory
..    parent directory
~     home directory
/     filesystem root
```

---

# 8. Hidden Files

Linux filenames beginning with `.` are conventionally hidden.

Examples:

```text
.bashrc
.profile
.git
.env
.ssh
```

Display them with:

```bash
ls -a
```

Understand that "hidden" is primarily a naming/UI convention.

---

# 9. File and Directory Manipulation

Be comfortable with:

```bash
touch
mkdir
cp
mv
rm
rmdir
ln
```

Be able to:

- create files,
- create nested directories,
- rename files,
- move files,
- copy files,
- copy directory trees,
- delete files,
- delete directory trees,
- create symbolic links.

Understand the risks of recursive operations.

---

# 10. Viewing Files

Know when to use:

```bash
cat
less
head
tail
```

Typical choices:

```text
cat     small file / combine streams
less    interactively inspect large text
head    beginning
tail    end
tail -f live log monitoring
```

---

# 11. Searching

Two distinct questions:

## Where is the file?

Use tools such as:

```bash
find
```

Example:

```bash
find . -type f -name "*.yml"
```

## Where is this text?

Use:

```bash
grep
```

Example:

```bash
grep -R "SPRING_PROFILES_ACTIVE" .
```

Learn to combine them when appropriate.

---

# 12. Standard Input, Output, and Error

Unix-style programs commonly work with three standard streams:

```text
stdin   standard input
stdout  standard output
stderr  standard error
```

File descriptors:

```text
0   stdin
1   stdout
2   stderr
```

Conceptually:

```text
input
  ↓
program
  ├── stdout
  └── stderr
```

This model explains:

- pipes,
- redirection,
- shell scripting,
- logging,
- command composition.

---

# 13. Pipes and Command Composition

One of the central Unix ideas is combining small tools.

Example:

```bash
ps aux | grep java
```

Conceptually:

```text
ps
 ↓ stdout
 |
 ↓ stdin
grep
```

Other examples:

```bash
history | grep docker
```

```bash
find . -type f | wc -l
```

```bash
cat names.txt | sort | uniq
```

Also learn when a command can read a file directly and therefore does not need `cat`:

```bash
sort names.txt | uniq
```

---

# 14. Users

Linux is a multi-user operating system.

Understand:

- username,
- UID,
- home directory,
- groups,
- login shell,
- root user.

Commands:

```bash
whoami
id
```

---

# 15. Root

`root` is the traditional administrative superuser.

Root can bypass many normal permission restrictions.

Understand why routinely operating as root is dangerous.

---

# 16. `sudo`

`sudo` allows an authorized user to execute commands with another user's privileges, commonly root.

Example:

```bash
sudo apt install package
```

Mental model:

```text
normal user
    ↓
 sudo authorization
    ↓
privileged command
```

Understand:

- why `sudo` asks for your password,
- sudo policy,
- cached authorization,
- root privileges,
- why `sudo` should not be used automatically,
- how root-owned files can accidentally appear in your home/project directories.

---

# 17. File Ownership

Files generally have:

```text
owner
group
```

Inspect with:

```bash
ls -l
```

Example conceptual output:

```text
-rw-r--r-- 1 user developers 1200 Sep 14 file.txt
```

Change ownership with:

```bash
chown
```

---

# 18. Linux Permissions

Understand:

```text
r   read
w   write
x   execute
```

Permission groups:

```text
user
group
other
```

Example:

```text
-rwxr-xr--
```

Break it into:

```text
-   file type
rwx owner
r-x group
r-- others
```

---

# 19. Numeric Permissions

Understand:

```text
r = 4
w = 2
x = 1
```

Examples:

```text
7 = rwx
6 = rw-
5 = r-x
4 = r--
```

Therefore:

```bash
chmod 755 script.sh
```

means:

```text
owner:  rwx
group:  r-x
others: r-x
```

And:

```bash
chmod 644 file.txt
```

means:

```text
owner:  rw-
group:  r--
others: r--
```

---

# 20. Executable Files

A script may contain valid Bash but still lack execute permission.

Example:

```bash
chmod +x script.sh
```

Then:

```bash
./script.sh
```

Understand why:

```bash
script.sh
```

and:

```bash
./script.sh
```

can behave differently.

This requires understanding `$PATH`.

---

# 21. `$PATH`

`PATH` is an environment variable containing directories searched for executable commands.

Inspect:

```bash
echo "$PATH"
```

Conceptually:

```text
/usr/local/bin
/usr/bin
/bin
...
```

When you type:

```bash
java
```

the shell searches directories in `$PATH`.

Useful diagnostics:

```bash
which java
command -v java
type java
```

This concept is extremely important for development tooling.

---

# 22. Environment Variables

Examples:

```text
PATH
HOME
USER
SHELL
PWD
```

Inspect one:

```bash
echo "$HOME"
```

List environment:

```bash
env
```

Create shell variable:

```bash
NAME=value
```

Export it:

```bash
export NAME=value
```

Understand the distinction between:

- shell variables,
- exported environment variables,
- inherited process environments,
- persistent shell configuration.

---

# 23. Shell Startup Configuration

For Bash, understand files such as:

```text
~/.bashrc
~/.profile
```

A common development configuration might modify `$PATH` or initialize a version manager.

After editing `.bashrc`:

```bash
source ~/.bashrc
```

Understand conceptually:

- login shells,
- interactive shells,
- shell initialization,
- why a command may work in one terminal but not another.

---

# 24. Processes

A running program is represented by a process.

Understand:

- PID,
- parent process,
- child process,
- foreground process,
- background process,
- process exit status,
- signals.

Commands:

```bash
ps
ps aux
top
kill
```

---

# 25. Foreground and Background Jobs

Run normally:

```bash
command
```

Run in background:

```bash
command &
```

Job management:

```bash
jobs
fg
bg
```

Common terminal control:

```text
Ctrl+C   usually send SIGINT
Ctrl+Z   usually suspend foreground process
```

Understand that closing a terminal can affect processes attached to that shell.

---

# 26. Signals

Processes can receive signals.

Important examples:

```text
SIGINT
SIGTERM
SIGKILL
```

Typical:

```bash
kill PID
```

usually sends `SIGTERM`.

Force:

```bash
kill -9 PID
```

sends `SIGKILL`.

Prefer graceful termination before force.

---

# 27. Exit Codes

Commands return an exit status.

Conventionally:

```text
0       success
nonzero failure/other status
```

Inspect the previous status:

```bash
echo $?
```

This explains:

```bash
command1 && command2
```

and:

```bash
command1 || command2
```

Exit codes are fundamental to shell automation.

---

# 28. Package Management

Ubuntu commonly uses:

```bash
apt
```

Understand the relationship between:

```text
package
repository
package index
installed version
available version
dependency
```

Core commands:

```bash
sudo apt update
sudo apt upgrade
sudo apt install package
sudo apt remove package
apt search package
apt show package
```

Also recognize lower-level/related tooling:

```text
apt-get
dpkg
```

You do not need to use them for every normal task, but should know they exist.

---

# 29. Why `apt update` Comes First

```bash
sudo apt update
```

refreshes information about packages available from configured repositories.

Then:

```bash
sudo apt upgrade
```

upgrades installed packages where appropriate.

Thus:

```bash
sudo apt update && sudo apt upgrade
```

has a logical order.

The `&&` means the upgrade runs only if the update succeeds.

---

# 30. Software Installation Sources

Linux software may be installed through different mechanisms:

- Ubuntu repositories / APT,
- `.deb` packages,
- language package managers,
- vendor repositories,
- downloaded archives,
- install scripts,
- source compilation,
- version managers,
- Snap.

An expert should ask:

> Where did this program come from?

This matters for:

- updates,
- removal,
- `$PATH`,
- security,
- troubleshooting.

---

# 31. Archives and Compression

Know common formats:

```text
.tar
.tar.gz
.tgz
.zip
.gz
```

Know tools:

```bash
tar
gzip
gunzip
zip
unzip
```

Understand that archiving and compression are distinct concepts.

---

# 32. Shells

A shell is a program that interprets commands.

Common shells:

```text
bash
zsh
sh
fish
```

Ubuntu commonly uses Bash for interactive command-line work by default.

Inspect your configured shell:

```bash
echo "$SHELL"
```

Understand:

> Terminal ≠ shell ≠ Linux ≠ WSL2.

They are different layers.

---

# 33. Terminal vs Shell

A **terminal** provides the interface where text input/output occurs.

A **shell** interprets commands.

Conceptually:

```text
Windows Terminal
      ↓
WSL session
      ↓
Bash
      ↓
Linux commands/programs
```

This distinction is useful when diagnosing terminal configuration problems.

---

# 34. Bash Fundamentals

Know:

- commands,
- arguments,
- variables,
- quoting,
- expansion,
- redirection,
- pipes,
- conditionals,
- loops,
- functions,
- scripts,
- exit codes.

---

# 35. Bash Variables

Create:

```bash
name="project"
```

Read:

```bash
echo "$name"
```

Avoid spaces around `=`:

```bash
name="project"
```

not:

```bash
name = "project"
```

---

# 36. Bash Script Basics

Example:

```bash
#!/usr/bin/env bash

echo "Hello"
```

The first line is a **shebang**.

Make executable:

```bash
chmod +x script.sh
```

Run:

```bash
./script.sh
```

Or explicitly:

```bash
bash script.sh
```

Understand the difference between these execution methods.

---

# 37. Script Arguments

Example invocation:

```bash
./build.sh backend production
```

Inside the script:

```bash
$0
$1
$2
$#
"$@"
```

Meaning:

```text
$0    script name
$1    first argument
$2    second argument
$#    argument count
"$@"  all arguments, preserving boundaries
```

---

# 38. Bash Conditionals

Example:

```bash
if [ -f "pom.xml" ]; then
    echo "Maven project detected"
else
    echo "pom.xml not found"
fi
```

Understand tests for:

- files,
- directories,
- strings,
- numbers,
- command success.

Also learn Bash's `[[ ... ]]` conditional syntax.

---

# 39. Bash Loops

## `for`

```bash
for file in *.txt; do
    echo "$file"
done
```

## `while`

```bash
while condition; do
    command
done
```

Use loops for repetitive development tasks.

---

# 40. Bash Functions

Example:

```bash
build_backend() {
    echo "Building backend..."
    mvn clean package
}
```

Call:

```bash
build_backend
```

Understand:

- function definitions,
- parameters,
- local variables,
- return/exit status.

---

# 41. Development Automation with Bash

You should eventually be able to write scripts that:

- create project directories,
- verify dependencies,
- set environment variables,
- start development services,
- run tests,
- build applications,
- clean generated files,
- search project trees,
- archive output,
- inspect logs,
- perform repeated setup tasks.

Example conceptual script:

```bash
#!/usr/bin/env bash

set -e

echo "Building backend..."
cd backend
mvn clean package

echo "Build complete."
```

The goal is not sophisticated Bash programming.

The goal is to remove repetitive manual development steps safely.

---

# 42. Defensive Shell Scripting

Understand why scripts often use options such as:

```bash
set -e
```

and, as you advance:

```bash
set -euo pipefail
```

Learn the behavior and caveats rather than blindly putting this in every script.

Also understand:

- quoting variables,
- validating arguments,
- checking files before deleting them,
- meaningful exit codes,
- error messages,
- avoiding unsafe wildcard expansion,
- handling spaces in filenames.

---

# 43. Networking Fundamentals

Know basic concepts:

- IP address,
- localhost,
- loopback,
- port,
- TCP,
- UDP,
- DNS,
- hostname,
- listening process,
- client,
- server.

Useful tools may include:

```bash
ip
ss
ping
curl
hostname
```

Examples:

```bash
ip addr
ss -ltn
curl http://localhost:8081
```

---

# 44. SSH

SSH is important for development even though it deserves deeper treatment elsewhere.

Understand:

- SSH client,
- SSH server,
- public/private key pair,
- known hosts,
- authentication.

Common paths:

```text
~/.ssh/
~/.ssh/id_ed25519
~/.ssh/id_ed25519.pub
~/.ssh/known_hosts
```

Typical connection:

```bash
ssh user@host
```

Know why private keys must remain private.

---

# 45. Services and `systemd`

Modern WSL2 distributions can support `systemd`.

Understand conceptually:

- service,
- daemon,
- startup,
- shutdown,
- service status.

Common commands where systemd is enabled:

```bash
systemctl status service
sudo systemctl start service
sudo systemctl stop service
sudo systemctl restart service
```

Do not assume every WSL installation or every program is managed by systemd.

---

# 46. What WSL Is

**WSL** stands for:

**Windows Subsystem for Linux**

It allows Linux environments to run on Windows with integration between the two operating environments.

WSL provides Linux distributions such as Ubuntu while allowing interoperability with Windows.

---

# 47. WSL1 vs WSL2

Understand the architectural distinction.

## WSL1

Primarily translated Linux system calls into Windows behavior.

## WSL2

Runs a real Linux kernel using lightweight virtualization.

Conceptually:

```text
Windows
   ↓
WSL2 virtualization environment
   ↓
Linux kernel
   ↓
Ubuntu user space
   ↓
Bash / Linux programs
```

This architecture improves Linux compatibility substantially.

---

# 48. Distribution vs WSL

Do not confuse:

```text
WSL
```

with:

```text
Ubuntu
```

WSL is Microsoft's Windows/Linux integration and execution environment.

Ubuntu is a Linux distribution running within it.

You can have multiple WSL distributions installed simultaneously.

---

# 49. Installing WSL2

From Windows, know the general administrative command:

```powershell
wsl --install
```

Understand the installation components conceptually:

- WSL platform,
- Linux kernel/environment,
- distribution,
- user account.

Useful Windows-side commands include:

```powershell
wsl --status
wsl --version
wsl --list --verbose
```

Alias:

```powershell
wsl -l -v
```

---

# 50. Starting and Stopping WSL

Start a default distribution:

```powershell
wsl
```

Run a specific distribution:

```powershell
wsl -d Ubuntu
```

Shut down WSL:

```powershell
wsl --shutdown
```

Terminate one distribution:

```powershell
wsl --terminate Ubuntu
```

Understand the distinction between:

- closing a terminal,
- ending a shell,
- terminating a distribution,
- shutting down WSL's virtualized environment.

---

# 51. Windows and Linux Filesystems

This is one of the most important WSL-specific topics.

Your Linux home may look like:

```text
/home/user
```

Windows C: is commonly mounted at:

```text
/mnt/c
```

Therefore:

```text
C:\Users\Benjamin
```

roughly corresponds to a WSL-accessible path like:

```text
/mnt/c/Users/Benjamin
```

Understand that these files can live on different filesystem implementations with different performance and permission behavior.

---

# 52. Where Development Projects Should Live

For Linux-heavy development inside WSL, generally prefer the Linux filesystem:

```text
~/workspace/project
```

rather than:

```text
/mnt/c/Users/.../project
```

when the tools performing heavy filesystem work run inside Linux.

Reasons can include:

- filesystem performance,
- Linux permissions/metadata,
- file watching,
- tool compatibility,
- fewer cross-filesystem surprises.

Use Windows-mounted paths when Windows-side access is the primary requirement.

---

# 53. Accessing WSL Files from Windows

Windows can expose WSL distributions through a network-style path such as:

```text
\\wsl$\
```

Current Windows versions prefer the newer alias:

```text
\\wsl.localhost\
```

Both forms reach the same underlying WSL filesystem integration exposed by Windows — `\\wsl$\` still works as a legacy alias, but `\\wsl.localhost\` is the more current form.

Understand the principle:

> WSL Linux files can be accessed from Windows through supported WSL integration.

Avoid treating the distribution's underlying virtual disk as an ordinary disk image to manipulate manually.

---

# 54. Launching Windows Tools from WSL

WSL supports Windows/Linux interoperability.

Examples:

```bash
explorer.exe .
```

opens the current WSL directory in Windows File Explorer.

If VS Code WSL integration is installed:

```bash
code .
```

can open the current project in VS Code using the WSL environment.

Windows executables can often be invoked from WSL:

```bash
notepad.exe file.txt
```

Understand that the executable is still a **Windows program**, even though it was launched from Bash.

---

# 55. Running Linux Commands from Windows

Windows can invoke Linux commands through `wsl.exe`.

Example conceptually:

```powershell
wsl ls -la
```

Specific distribution:

```powershell
wsl -d Ubuntu -- ls -la
```

Understand which shell/environment is interpreting each portion of a mixed Windows/WSL command.

---

# 56. Windows Paths vs Linux Paths

Windows:

```text
C:\Users\Name\project
```

Linux:

```text
/home/user/project
```

WSL-mounted Windows:

```text
/mnt/c/Users/Name/project
```

Know:

- `/` vs `\`,
- drive letters vs mount points,
- case sensitivity differences,
- quoting paths containing spaces.

WSL provides path-conversion tooling such as:

```bash
wslpath
```

Example:

```bash
wslpath 'C:\Users\Name'
```

`wslpath` can autodetect the direction of the conversion as shown above, but it normally takes an explicit directionality flag: `-u` converts a Windows path to a WSL path, `-w` converts a WSL path to a Windows path, and `-m` produces a mixed/wsl-friendly Windows path (forward slashes). The explicit form is generally recommended, especially in scripts, since it is clearer and more portable:

```bash
wslpath -u 'C:\Users\Name'
```

---

# 57. Case Sensitivity

Linux filenames are normally case-sensitive.

These can be different:

```text
Message.java
message.java
MESSAGE.java
```

Windows commonly behaves case-insensitively by default.

Cross-platform projects can therefore encounter filename-case problems.

This is especially relevant to:

- Git,
- Java packages,
- imports,
- build tools.

---

# 58. Line Endings Across Windows and Linux

Windows commonly uses:

```text
CRLF
```

Unix/Linux conventionally uses:

```text
LF
```

Cross-platform projects may encounter:

- shell scripts that fail,
- noisy Git diffs,
- interpreter errors.

Understand line-ending configuration at the editor/tool/Git level.

---

# 59. File Permissions Across WSL Boundaries

Linux-native files support Linux ownership and permission semantics naturally.

Files accessed under:

```text
/mnt/c
```

involve Windows filesystem semantics plus WSL translation/configuration.

Understand that permission behavior may therefore differ between:

```text
/home/user/project
```

and:

```text
/mnt/c/project
```

This is a major reason to understand where a project physically lives.

---

# 60. WSL Networking

WSL2 has its own Linux networking environment integrated with Windows.

Understand:

- localhost,
- Linux IP,
- Windows host,
- ports,
- NAT/mirrored networking concepts depending on WSL configuration,
- firewall interactions,
- binding to interfaces.

For local development, be able to reason about:

```text
Angular → backend
backend → PostgreSQL
Windows browser → WSL service
Docker container → WSL/host service
```

Do not simply assume every `localhost` refers to the same network namespace in every context.

---

# 61. WSL Configuration Files

Know that WSL has configuration at more than one layer.

Important concepts include:

```text
/etc/wsl.conf
```

inside a distribution.

And Windows-side WSL configuration such as:

```text
%UserProfile%\.wslconfig
```

Understand that these configure different scopes.

Potential configuration areas include:

- memory,
- processors,
- networking,
- mounts,
- systemd,
- interoperability.

Do not change settings without understanding their scope.

---

# 62. WSL Resource Usage

WSL2 uses a lightweight virtualized Linux environment.

Understand that it consumes:

- RAM,
- CPU,
- disk,
- networking resources.

Know that:

```powershell
wsl --shutdown
```

can fully stop WSL's running environment when needed.

Advanced users should understand Windows-side resource controls available through WSL configuration.

---

# 63. WSL Distributions

List:

```powershell
wsl -l -v
```

Understand:

- distribution name,
- running/stopped state,
- WSL version,
- default distribution.

You should understand how multiple distributions can coexist independently.

---

# 64. WSL Backup, Export, and Import

Know conceptually that WSL distributions can be exported and imported.

Commands include Windows-side operations such as:

```powershell
wsl --export
wsl --import
```

Understand their use for:

- backup,
- migration,
- recreating environments,
- moving distributions.

Treat unregister/delete operations as destructive.

---

# 65. VS Code and WSL

Understand the architecture:

```text
Windows VS Code UI
        ↓
WSL integration
        ↓
project in Ubuntu
        ↓
Linux shell/tools
```

This lets the editor run on Windows while development tooling operates inside Linux.

Be able to diagnose:

- terminal opened in Windows instead of WSL,
- extension installed only on one side,
- wrong executable being used,
- incorrect `$PATH`,
- project opened through the wrong filesystem/environment.

---

# 66. Docker and WSL Integration

Docker deserves its own skill map, but understand the WSL boundary.

When using Docker Desktop with WSL integration:

```text
Windows
   ↓
Docker Desktop
   ↓
WSL integration
   ↓
Ubuntu shell
   ↓
docker CLI
```

If:

```bash
docker
```

suddenly disappears or cannot reach the daemon, possible causes include:

- Docker Desktop not running,
- WSL integration disabled,
- wrong distribution,
- `$PATH` problem,
- daemon/backend problem.

The important skill is identifying **which layer failed**.

---

# 67. Development Tool Integration

For tools such as:

```text
Java
Maven
Node
npm
Python
Git
Docker
```

be able to answer:

1. Is this the Windows version or Linux version?
2. Where is the executable?
3. How was it installed?
4. Is its directory in `$PATH`?
5. Which configuration files does it use?
6. Is it running inside WSL or Windows?
7. Where are the project files?

Useful diagnostics:

```bash
which java
command -v java
java --version
echo "$PATH"
pwd
```

---

# 68. Common WSL Problem — Command Not Found

Example:

```text
docker: command not found
```

Possible causes:

- program not installed,
- `$PATH` missing its location,
- shell configuration not loaded,
- Windows version installed but Linux command expected,
- WSL integration disabled,
- wrong distribution.

Investigate:

```bash
command -v docker
which docker
echo "$PATH"
```

Then determine how the tool is supposed to be provided.

---

# 69. Common Linux Problem — Permission Denied

Possible causes:

- file lacks execute permission,
- wrong owner,
- directory permission,
- trying to access protected system files,
- mounted-filesystem behavior.

Inspect:

```bash
ls -l file
id
```

Do not immediately use:

```bash
sudo
```

Determine why access was denied first.

---

# 70. Common Linux Problem — File Not Found

Check:

```bash
pwd
ls -la
```

Then ask:

- Am I in the expected directory?
- Is the filename case correct?
- Is this an absolute or relative path?
- Does the file actually exist?
- Did I mistype the path?
- Am I confusing a Windows path with a Linux path?

---

# 71. Common Linux Problem — Program Works in One Terminal but Not Another

Investigate:

```bash
echo "$PATH"
echo "$SHELL"
command -v program
```

Possible causes:

- `.bashrc` not loaded,
- different shell,
- Windows terminal vs WSL terminal,
- different WSL distribution,
- different user,
- environment variable differences.

---

# 72. Common WSL Problem — Wrong Environment

A development command may accidentally execute in:

```text
PowerShell
Command Prompt
Git Bash
Ubuntu/WSL Bash
Docker container shell
```

These are different environments.

Always identify:

```text
Which terminal?
Which shell?
Which OS environment?
Which filesystem?
Which user?
```

Useful Linux checks:

```bash
pwd
whoami
uname -a
echo "$SHELL"
```

---

# 73. Common WSL Problem — Wrong Filesystem

Symptoms may include:

- slow builds,
- permission oddities,
- file watchers behaving strangely,
- confusing Windows/Linux paths.

Check:

```bash
pwd
```

Compare:

```text
/home/user/workspace/project
```

with:

```text
/mnt/c/Users/user/project
```

Know where the files physically live.

---

# 74. Common WSL Problem — Networking

When a service cannot connect, determine:

```text
Who is the client?
Where is it running?
Who is the server?
Where is it running?
What address is being used?
What port?
Is the server listening?
```

Useful tools:

```bash
ss -ltn
curl
ping
ip addr
```

Do not treat "connection refused" as a generic application error.

---

# 75. Common Package Problems

When package installation fails, investigate:

- package metadata,
- repository configuration,
- package name,
- dependency conflicts,
- network access,
- permissions.

Start with:

```bash
sudo apt update
```

Then inspect the actual error rather than repeatedly rerunning commands.

---

# 76. Disk Space Problems

Inspect filesystem usage:

```bash
df -h
```

Inspect directory size:

```bash
du -sh directory
```

Find large areas carefully.

Understand that WSL2 virtual disk storage has behavior distinct from ordinary Windows folders.

---

# 77. Linux Logs and Troubleshooting

Know how to inspect:

- application logs,
- command output,
- standard error,
- service logs.

Useful tools:

```bash
less
tail
tail -f
grep
journalctl
```

Where systemd is active:

```bash
journalctl
journalctl -u service
```

---

# 78. Safe Command-Line Habits

Before destructive commands:

```bash
pwd
ls
```

Then confirm the target.

Be particularly careful with:

```bash
rm -rf
sudo rm
chmod -R
chown -R
find ... -delete
```

Use tab completion to reduce path typos.

Quote variables in scripts:

```bash
rm "$file"
```

rather than:

```bash
rm $file
```

when appropriate.

---

# 79. Tab Completion

Use:

```text
Tab
```

to complete:

- filenames,
- directory names,
- commands,
- sometimes command options depending on shell configuration.

Benefits:

- speed,
- fewer spelling errors,
- confirmation that a path exists.

This should become habitual.

---

# 80. Keyboard Shortcuts Worth Memorizing

Common Bash/readline terminal shortcuts:

```text
Ctrl+C   interrupt foreground command
Ctrl+L   clear screen
Ctrl+R   search history
Ctrl+A   beginning of line
Ctrl+E   end of line
Ctrl+U   delete before cursor
Ctrl+K   delete after cursor
Ctrl+W   delete previous word
Ctrl+Z   suspend foreground process
```

Exact behavior can depend on terminal/shell configuration.

---

# 81. Command History

Useful:

```bash
history
```

Repeat previous command:

```bash
!!
```

Use `!!` carefully, especially with `sudo`.

Interactive reverse search:

```text
Ctrl+R
```

An experienced command-line user relies heavily on history rather than repeatedly retyping long commands.

---

# 82. Aliases

Example:

```bash
alias ll='ls -lah'
```

Inspect:

```bash
alias
```

Persistent aliases can be added to shell configuration such as:

```text
~/.bashrc
```

Use aliases for convenience, not to hide concepts you do not understand.

---

# 83. Symbolic Links

Create:

```bash
ln -s target link
```

Conceptually:

```text
link → target
```

Understand:

- absolute vs relative link targets,
- broken links,
- links to files,
- links to directories.

---

# 84. Hard Links

A hard link is another directory entry referencing the same underlying inode/file data.

Understand conceptually how this differs from a symbolic link.

You do not need to use hard links frequently, but should know they exist.

---

# 85. Files, Inodes, and Metadata

Advanced Linux knowledge should include the basic concept that a filename is not identical to the underlying file object.

Understand:

- inode,
- filename/directory entry,
- ownership,
- permissions,
- timestamps,
- links.

This helps explain:

- hard links,
- deletion behavior,
- open deleted files,
- filesystem metadata.

---

# 86. Mounting

Linux integrates filesystems into one directory tree through **mount points**.

Conceptually:

```text
filesystem
   ↓ mounted at
/mnt/c
```

This explains how Windows drives can appear inside the Linux filesystem.

Know the concepts:

- filesystem,
- device,
- mount point,
- mounted filesystem,
- unmounting.

---

# 87. Linux Philosophy

Several ideas help explain Linux command-line design:

## Small tools

Programs often do one focused job.

## Text streams

Programs frequently communicate through text.

## Composition

Tools are combined:

```bash
command1 | command2 | command3
```

## Files as interfaces

Many system concepts are exposed through file-like interfaces.

These principles explain why shell proficiency is more than memorizing commands.

---

# 88. Expert WSL2 Mental Model

A useful high-level model is:

```text
Windows hardware
      ↓
Windows
      ↓
WSL2 virtualization/integration
      ↓
Linux kernel
      ↓
Ubuntu distribution
      ↓
Bash shell
      ↓
Linux programs
      ↓
Development tools
```

Alongside it:

```text
Windows filesystem
      ↕
WSL filesystem integration
      ↕
Linux filesystem
```

And:

```text
Windows networking
      ↕
WSL networking
      ↕
Linux services
```

When something fails, identify which layer owns the failure.

---

# 89. Troubleshooting Method

Instead of copying random fixes, work from state.

## Step 1 — Identify the environment

```bash
uname -a
whoami
pwd
echo "$SHELL"
```

## Step 2 — Identify the executable

```bash
command -v program
program --version
```

## Step 3 — Inspect files

```bash
ls -la
```

## Step 4 — Inspect environment

```bash
echo "$PATH"
env
```

## Step 5 — Inspect processes/network if relevant

```bash
ps aux
ss -ltn
```

## Step 6 — Read the earliest meaningful error

Do not focus only on a final generic message such as:

```text
Process exited with code 1
```

Look upward for the specific failure.

## Step 7 — Change one thing

Then retest.

This turns troubleshooting into hypothesis testing rather than command guessing.

---

# 90. Practical Expertise Ladder

## Level 1 — Basic Navigation

Know from memory:

```bash
pwd
ls
cd
mkdir
touch
cp
mv
rm
rmdir
cat
nano
clear
```

Understand:

- current directory,
- parent directory,
- home,
- absolute paths,
- relative paths.

You can safely move around the filesystem and manipulate basic files.

---

## Level 2 — Comfortable Linux User

Add:

```bash
less
head
tail
find
grep
which
command -v
chmod
sudo
apt
history
man
```

Understand:

- Linux filesystem hierarchy,
- hidden files,
- users,
- permissions,
- packages,
- PATH,
- environment variables.

You can perform ordinary development-environment work without constantly copying commands.

---

## Level 3 — Proficient Shell User

Understand and use:

```text
|
>
>>
<
2>
&&
||
;
&
*
?
$()
```

Know:

```bash
ps
kill
jobs
fg
bg
export
source
curl
tar
df
du
```

Understand:

- stdin,
- stdout,
- stderr,
- processes,
- exit codes,
- command composition,
- shell initialization.

You can combine tools to solve new command-line problems.

---

## Level 4 — WSL2 Development Proficiency

Understand:

- WSL architecture,
- WSL1 vs WSL2,
- Ubuntu vs WSL,
- Windows/Linux paths,
- `/mnt/c`,
- Linux-native filesystem,
- `wsl.exe`,
- distribution management,
- WSL networking,
- VS Code integration,
- Docker integration,
- Windows executables from WSL,
- WSL filesystem performance,
- line endings,
- cross-platform permissions,
- `.wslconfig`,
- `wsl.conf`.

You can diagnose most WSL development-environment problems.

---

## Level 5 — Automation

Write Bash scripts using:

- variables,
- arguments,
- conditionals,
- loops,
- functions,
- pipes,
- redirection,
- command substitution,
- exit codes,
- defensive checks.

You can automate repetitive development workflows safely.

---

## Level 6 — Advanced / Expert

Understand:

- processes and signals,
- systemd/services,
- filesystem mounts,
- inodes,
- hard and symbolic links,
- shell startup behavior,
- networking boundaries,
- permissions and ownership deeply,
- WSL virtualization,
- virtual disk/storage behavior,
- distribution export/import,
- resource configuration,
- advanced troubleshooting,
- environment boundaries between Windows, WSL, containers, and development tools.

At this level, you can reason about unfamiliar problems from the underlying system model.

---

# 91. Interview-Focused Commands to Memorize

You should be able to explain and use these without needing documentation for their basic operation:

```text
pwd
ls
cd
mkdir
touch
cp
mv
rm
rmdir
cat
less
nano
find
grep
head
tail
chmod
chown
sudo
apt
ps
kill
history
man
which
command
echo
env
export
source
curl
tar
df
du
uname
whoami
id
```

Also memorize these shell concepts/operators:

```text
.
..
~
/
|
>
>>
<
2>
&&
||
;
&
*
?
$VAR
${VAR}
$(command)
"double quotes"
'single quotes'
```

And recognize the Windows-side WSL administration commands:

```text
wsl
wsl --install
wsl --status
wsl --version
wsl -l -v
wsl -d <distribution>
wsl --shutdown
wsl --terminate <distribution>
wsl --export
wsl --import
```

You do not need every option memorized.

You should know what category of operation each command performs.

---

# 92. Interview-Focused Concepts

Be prepared to explain:

- What is Linux?
- What is Ubuntu?
- What is WSL?
- What is WSL2?
- How does WSL2 differ from WSL1?
- Is WSL2 a virtual machine?
- Does WSL2 use a real Linux kernel?
- What is a Linux distribution?
- What is a shell?
- What is Bash?
- What is a terminal?
- What is the difference between a terminal and shell?
- What is the working directory?
- What is an absolute path?
- What is a relative path?
- What do `.`, `..`, `~`, and `/` mean?
- What is the Linux filesystem root?
- What are `/home`, `/etc`, `/usr`, `/var`, `/tmp`, and `/mnt`?
- What is `/mnt/c` under WSL?
- Where should WSL development projects generally live?
- What is `$PATH`?
- What is an environment variable?
- What is `.bashrc`?
- What is root?
- What does `sudo` do?
- What are Linux users and groups?
- How do Linux permissions work?
- What do `r`, `w`, and `x` mean?
- What does `chmod 755` mean?
- What is a process?
- What is a PID?
- What is a signal?
- What does `kill` actually do?
- What is an exit code?
- What are stdin, stdout, and stderr?
- What does a pipe do?
- What does output redirection do?
- What is the difference between `>` and `>>`?
- What does `&&` do?
- What does `||` do?
- What is command substitution?
- What is shell globbing?
- What is a symbolic link?
- What is a mount point?
- What does `apt update` do?
- What does `apt upgrade` do?
- Why should `apt update` normally happen first?
- What is a Bash script?
- What is a shebang?
- How do you make a script executable?
- How are arguments passed into Bash scripts?
- How do Windows and Linux filesystems interact in WSL?
- How can Windows programs be launched from WSL?
- How can Windows invoke Linux commands?
- Why can CRLF/LF differences matter?
- Why might a command exist in PowerShell but not WSL?
- Why might a program work in one WSL terminal but not another?
- How would you diagnose `command not found`?
- How would you diagnose `permission denied`?
- How would you diagnose a localhost/port problem?

---

# 93. Common Practical Exercises

A skill map becomes useful when each concept can be demonstrated.

## Filesystem Exercise

From your home directory, create:

```text
practice/
├── src/
│   ├── main/
│   └── test/
├── logs/
└── README.md
```

using only terminal commands.

Then:

- rename `logs`,
- copy `README.md`,
- move the copy,
- delete the copy,
- remove the temporary directory.

---

## Search Exercise

Create several files and find them using:

```bash
find
```

Then place repeated text inside them and locate the text using:

```bash
grep
```

---

## Pipe Exercise

Take command output and:

1. filter it,
2. sort it,
3. count it,
4. redirect it to a file.

---

## Permission Exercise

Create:

```text
hello.sh
```

Attempt to execute it before adding execute permission.

Then:

```bash
chmod +x hello.sh
```

and execute it again.

Explain why behavior changed.

---

## Environment Exercise

Create:

```bash
export PROJECT_NAME="practice"
```

Read it.

Start a child shell and determine whether it exists there.

Create a non-exported shell variable and compare the behavior.

---

## Bash Automation Exercise

Write a script that:

1. accepts a project directory as an argument,
2. verifies the directory exists,
3. enters the directory,
4. creates a backup directory,
5. copies selected files,
6. prints success,
7. returns a nonzero exit code on failure.

---

## WSL Interoperability Exercise

From Ubuntu:

1. determine your Linux working directory,
2. navigate to `/mnt/c`,
3. identify the corresponding Windows location,
4. return to `~`,
5. run `explorer.exe .`,
6. open a Linux project using `code .`,
7. explain which components are Windows programs and which are Linux programs.

---

## Troubleshooting Exercise

Intentionally create situations involving:

- wrong directory,
- missing executable permission,
- nonexistent command,
- missing `$PATH` entry,
- incorrect filename case,
- occupied port.

Diagnose them from system state rather than following a prepared fix.

---

# 94. Recommended Learning Order

A strong learning progression is:

```text
What WSL2 / Ubuntu / Bash are
            ↓
pwd / ls / cd
            ↓
Linux filesystem and paths
            ↓
mkdir / touch / cp / mv / rm
            ↓
cat / less / nano
            ↓
find / grep
            ↓
command syntax and help
            ↓
users / sudo / permissions
            ↓
packages / apt
            ↓
PATH / environment variables
            ↓
stdin / stdout / stderr
            ↓
pipes / redirection / operators
            ↓
processes / jobs / signals
            ↓
Bash scripting
            ↓
Windows ↔ WSL filesystem
            ↓
Windows ↔ WSL interoperability
            ↓
networking
            ↓
WSL configuration
            ↓
development-tool integration
            ↓
advanced Linux/WSL internals
```

---

# 95. What Not to Memorize

Do not try to memorize:

- every `find` predicate,
- every `grep` option,
- every `tar` flag combination,
- every `apt` subcommand,
- every signal number,
- every `systemctl` option,
- every WSL configuration property,
- every Bash syntax edge case.

Instead, memorize:

1. what the major tools are for,
2. the common syntax you use repeatedly,
3. the system's mental model,
4. how to inspect current state,
5. how to access help.

Then use:

```bash
man command
command --help
```

for uncommon details.

---

# 96. Final Standard for WSL2 / Ubuntu Command-Line Expertise

An expert user should be able to:

- explain what WSL2 is,
- explain what Ubuntu contributes to the environment,
- explain what Bash does,
- distinguish terminal, shell, Linux, Ubuntu, Windows, and WSL,
- navigate the filesystem without confusion,
- reason about absolute and relative paths,
- create, move, copy, rename, inspect, search, and delete files safely,
- understand Linux filesystem structure,
- use Linux permissions and ownership,
- explain `sudo` rather than merely use it,
- manage Ubuntu packages,
- understand `$PATH` and environment variables,
- diagnose why commands cannot be found,
- understand processes and signals,
- use stdin/stdout/stderr,
- compose commands with pipes and redirection,
- understand shell operators,
- write useful Bash automation scripts,
- understand Windows ↔ Linux path translation,
- understand `/mnt/c` versus the Linux filesystem,
- choose sensible locations for development projects,
- recognize Windows programs launched from Linux,
- recognize Linux commands launched from Windows,
- understand WSL networking sufficiently to diagnose development services,
- understand WSL distribution management,
- diagnose integration problems involving VS Code and Docker at the WSL boundary,
- and investigate unfamiliar problems by inspecting system state rather than copying unexplained commands.

The end goal is:

> **Operate Ubuntu on WSL2 comfortably from memory, explain the environment in an interview, automate routine development tasks, and understand the underlying system well enough to predict and troubleshoot its behavior.**
