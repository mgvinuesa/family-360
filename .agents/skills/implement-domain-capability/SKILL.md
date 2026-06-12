---
name: implement-domain-capability
description: Coordinate implementation of a functional domain capability across API, application, adapters, persistence, and frontend while keeping layer scope explicit. Use for new business capabilities, end-to-end domain changes, or requests whose technical impact must be discovered and delegated to the appropriate repository skills.
---

# Implement Domain Capability

Coordinate a functional change without assuming that every architectural layer
must be modified.

## Establish Context

1. Read the root `AGENTS.md` and each `AGENTS.md` for potentially affected
   areas.
2. Check `git status --short --branch` and `git worktree list`.
3. Create or switch to a task-specific branch before editing.
4. Identify the domain through `docs/domains/<domain>/README.md`.
5. Inspect the documented API, backend, persistence, and frontend paths.
6. Describe the requested behavior as observable functional outcomes.

## Build The Impact Plan

Assess each layer independently:

- API contract and generated boundaries.
- Domain and application behavior.
- Input and output adapters.
- Persistence schema and backend persistence implementation.
- Frontend generated client and feature integration.

Classify each layer as:

- **Required:** the capability cannot work without changing it.
- **Boundary adaptation:** an adjacent contract changed, but existing
  capabilities remain sufficient.
- **Unaffected:** no change is needed.
- **Deferred:** impact exists but the user has excluded it from this task.

Explain the classification and ask for confirmation before expanding beyond
the user's stated scope. Do not ask again for layers already approved in the
confirmed plan.

## Select Technical Skills

Use the smallest set of available technical skills:

- Use `$evolve-api-contract` for OpenAPI changes, generated backend interfaces
  or models, generated frontend clients, and adaptations at those generated
  boundaries.
- Use `$implement-application-capability` for domain rules, application use
  cases, commands, queries, application ports, and their adapters.
- Persistence requires a dedicated persistence skill. Until that skill exists,
  do not modify Flyway, JPA entities, Spring Data repositories, or persistent
  output adapters. Report this layer as deferred or blocked.

Invoke technical skills in dependency order, but re-evaluate impact after each
one. A later need may only become visible after an earlier contract is
implemented.

## Coordinate Cross-Layer Changes

Apply this protocol throughout the task:

1. Let each technical skill own decisions in its layer.
2. Allow a skill to adapt a boundary that belongs to its layer when the
   adjacent capability is unchanged.
3. Treat a new command, query, rule, port operation, persistence operation, or
   UI behavior as a capability change in that layer.
4. Before activating another skill for a newly discovered capability change,
   explain the impact and request confirmation unless the confirmed plan
   already includes that layer.
5. Never create temporary alternative adapters merely to avoid the correct
   layer change.
6. Keep deferred layers explicit; do not silently approximate their behavior.

## Plan Green Steps

Order work so each published commit is coherent and validated. A typical order
is:

1. Review and validate the API contract first for API-visible changes.
2. Implement required application behavior and contracts.
3. Regenerate and complete input-boundary adaptation against the application
   contract.
4. Implement persistence schema and output adapters when approved and
   supported.
5. Regenerate the frontend client and integrate UI behavior when requested.

Change the order when dependencies make another sequence safer. Temporary
local compilation failures are acceptable while generated signatures change;
published commits must not knowingly leave affected builds broken.

## Validate Completion

1. Run every validation required by affected `AGENTS.md` files and selected
   technical skills.
2. Reassess all layer classifications after implementation.
3. Confirm deferred impacts are documented and the implemented scope remains
   internally coherent.
4. Run `git diff --check`.
5. Commit, push, and open a draft pull request as required by `AGENTS.md`.
6. Record the functional outcome, layer-impact decision, deferred work, and
   validation commands in the pull request description.
