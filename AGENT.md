# FlatTire Agent Instructions

## Purpose

FlatTire is a collaborative software project.

Agents working in this repository should help with implementation, debugging, testing, architecture, documentation, and project organization while preserving the existing design and team workflow.

These instructions apply to work performed inside the FlatTire repository.

Machine-specific security rules, credentials, paths, IDE settings, and personal development preferences belong in each contributor's local agent configuration and should not be committed here.

## Working Style

Prefer:

- small, focused changes
- readable code
- explicit interfaces
- deterministic behavior
- minimal dependencies
- existing project conventions
- tests for meaningful behavior
- clear explanations of architectural changes

Avoid:

- unrelated refactors
- unnecessary new dependencies
- hidden behavior
- broad rewrites
- changing public contracts without reason
- modifying another contributor's work unnecessarily
- introducing machine-specific paths or assumptions

## Project Scope

Follow the scope defined by the repository documentation and current project decisions.

Do not assume features are in scope simply because they are technically possible.

When a requested feature conflicts with documented scope, identify the conflict before implementing it.

Prefer the smallest implementation that satisfies the current project requirement.

## Architecture

Preserve established architectural boundaries.

When adding functionality:

1. identify the appropriate existing layer or component
2. reuse shared contracts when possible
3. keep ecosystem-specific behavior isolated where appropriate
4. avoid leaking implementation details across abstractions
5. document meaningful architectural decisions

Do not introduce a new architectural pattern when the existing structure already supports the requirement.

## Dependencies

Prefer existing project dependencies and standard-library capabilities.

Before adding a dependency:

- confirm the project does not already provide the needed capability
- explain why the dependency is required
- consider maintenance and supply-chain risk
- avoid large dependencies for small functionality

Do not upgrade dependencies unrelated to the active task.

Do not perform broad dependency modernization unless explicitly requested.

## Source Changes

Before meaningful edits:

- inspect the relevant existing code
- understand nearby conventions
- check for existing tests
- identify interfaces or consumers affected by the change

Keep changes scoped to the task.

Do not silently rename:

- public APIs
- shared interfaces
- major packages
- modules
- configuration keys
- user-facing behavior

Call out compatibility concerns when they exist.

## Testing

Run the relevant existing project tests when practical.

Prefer targeted tests during development and broader verification before completing larger changes.

Do not weaken, delete, or bypass tests merely to make a change pass.

If a test appears incorrect or outdated, explain the issue before changing it.

When reporting completion, identify:

- tests run
- tests not run
- known failures
- anything that could not be verified

## Git and Collaboration

Assume multiple contributors may be working in parallel.

Before substantial changes:

- inspect repository status
- avoid overwriting unrelated modifications
- avoid broad formatting changes
- keep commits conceptually focused when commits are requested

Do not:

- force push
- rewrite shared history
- delete branches
- discard another contributor's work
- run destructive Git cleanup

unless explicitly authorized by the contributor performing the work.

Agents should not commit or push unless their user explicitly requests it.

## Documentation

Update project documentation when a change affects:

- architecture
- public behavior
- developer workflow
- interfaces
- configuration
- system design
- ownership boundaries
- implementation assumptions

Do not duplicate documentation unnecessarily.

Prefer updating the existing source of truth.

## Security

Treat FlatTire as security-sensitive software.

Prefer:

- deterministic behavior
- explainable results
- minimal dependencies
- explicit trust boundaries
- safe handling of external input
- conservative defaults

Do not expose:

- credentials
- tokens
- private keys
- personal data
- machine-specific secrets

Do not commit environment-specific secrets or configuration.

## External Resources

Use external research only when allowed by the local agent environment and user instructions.

Prefer official documentation and primary sources.

Do not execute downloaded scripts or remote material without explicit approval.

Do not silently add software or dependencies based on external research.

## Ambiguity

When requirements are unclear:

1. inspect existing code and documentation
2. preserve existing behavior where possible
3. make the smallest reasonable interpretation
4. explain assumptions that materially affect the implementation

Ask for clarification when guessing could create architectural, security, or compatibility problems.

## Completion

For meaningful changes, report:

- what changed
- files or components affected
- tests performed
- architectural impact
- unresolved questions
- known risks

Keep routine reports concise.

## Core Principle

Work as a careful contributor to a shared codebase.

Improve the requested area without unnecessarily changing the rest of the repository.