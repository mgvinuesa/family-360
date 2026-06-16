---
name: implement-domain-capability
description: Coordinate a requested functional capability across API, backend application, adapters, persistence, and frontend. Use as the mandatory entry point for new business behavior, CRUD or end-to-end domain work, or any request whose layer impact is not already explicitly bounded. Do not use it as a domain-model implementation skill; it plans scope and delegates to technical skills.
---

# Implement Domain Capability

Coordinate a functional change without implementing layer-owned work itself or
assuming that every architectural layer must be modified.

## Establish Context

1. Read the root `AGENTS.md` and each `AGENTS.md` for potentially affected
   areas.
2. Check `git status --short --branch` and `git worktree list`.
3. Create or switch to a task-specific branch before editing.
4. Identify the domain through `docs/domains/<domain>/README.md`.
5. Inspect the documented API, backend, persistence, and frontend paths.
6. Describe the requested behavior as observable functional outcomes.

## Establish The Scope Ledger

Before selecting skills, separate every candidate behavior into:

- **Explicit requirement:** directly requested by the user.
- **Required technical consequence:** necessary to implement an explicit
  requirement, without adding new product behavior.
- **Inferred product capability:** plausible or documented elsewhere, but not
  requested in this task.
- **Cross-cutting safeguard:** secure handling, privacy, input safety, logging,
  compatibility, or quality measures that do not create a new user-visible
  capability.

Only explicit requirements and required technical consequences enter the
implementation plan automatically. Apply cross-cutting safeguards without
turning them into authentication, authorization, roles, workflows, endpoints,
or other product capabilities.

Do not implement inferred product capabilities. Present them as questions or
follow-up options and request confirmation first. Domain documentation and
general `AGENTS.md` principles provide constraints and context; they do not
expand the requested feature scope by themselves.

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

When identifying adapter impact, use the repository convention:
`<domain>.infrastructure.adapter.in.<technology>` for input adapters,
`<domain>.infrastructure.adapter.out.<technology>` for output adapters, and
`<domain>.infrastructure.configuration` for module composition. Do not confuse
application ports with their adapter implementations.

Explain the classification and ask for confirmation before expanding beyond
the user's stated scope. Do not ask again for layers already approved in the
confirmed plan.

Do not edit production files until the layer plan is confirmed. If a required
layer has no available skill or cannot be implemented, explain why the
functional outcome would remain incomplete and ask whether the user wants a
partial contract, application skeleton, or no implementation yet. Never assume
that a partial implementation is acceptable.

The confirmed plan must list:

- Functional actions included.
- Functional actions explicitly excluded.
- Layers approved for modification.
- Inferred capabilities awaiting a decision.
- Required but unsupported work marked deferred or blocked.
- Whether an incomplete intermediate result has been explicitly accepted.

## Select Technical Skills

Use the smallest set of available technical skills:

- Use `$evolve-api-contract` for OpenAPI changes, generated backend interfaces
  or models, generated frontend clients, and adaptations at those generated
  boundaries.
- Use `$implement-application-capability` for domain rules, application use
  cases, commands, queries, and application ports.
- Use `$implement-persistence-capability` for Atlas desired schema, versioned
  migrations, JPA entities, Spring Data repositories, persistent output
  adapters, mappings, transactions, and database-focused verification.

Invoke technical skills in dependency order, but re-evaluate impact after each
one. A later need may only become visible after an earlier contract is
implemented.

Technical skills must receive the confirmed scope ledger. They may not broaden
it. If a technical skill discovers another capability or layer requirement, it
must return control to this coordinator for a scope decision.

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
7. Do not treat authentication, authorization, roles, invitations, ownership,
   audit workflows, or lifecycle policies as implicit parts of CRUD. Include
   them only when explicitly requested or confirmed after being proposed.

## Plan Green Steps

Order work so each published commit is coherent and validated. A typical order
is:

1. Review and validate the API contract first for API-visible changes.
2. Implement required application behavior and contracts.
3. Regenerate and complete input-boundary adaptation against the application
   contract.
4. Implement persistence schema and output adapters when approved.
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
