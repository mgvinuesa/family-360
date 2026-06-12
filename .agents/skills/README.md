# Repository Skills

This folder contains repository-scoped Codex skills for Family 360. Skills
complement the mandatory rules in `AGENTS.md`; they provide reusable workflows
and layer-specific decision boundaries.

## Functional Coordinator

### `implement-domain-capability`

Coordinate a functional domain change, classify its impact by layer, obtain
confirmation for scope expansions, invoke the required technical skills, and
validate the combined result.

## Technical Skills

### `evolve-api-contract`

Own OpenAPI contracts, generated backend and frontend boundaries, HTTP
adapters, and API mappings. It may adapt a boundary when application semantics
remain unchanged, but it must activate the application skill for new
application capabilities.

### `implement-application-capability`

Own domain rules, use cases, commands, queries, ports, authorization, and
application behavior. It assesses API and persistence impact without changing
those layers implicitly.

## Planned Skills

A persistence skill will later own Flyway, JPA, Spring Data, persistent
adapters, transactions, and database-focused verification. Until it is
defined, existing skills must report required persistence work as deferred or
blocked and must not create substitute production adapters.

## Invocation

Codex may activate a skill when its description matches the request. Skills can
also be selected explicitly:

```text
Use $implement-domain-capability to implement family search by member name.
```

```text
Use $evolve-api-contract to correct the pageSize query parameter name without
changing application behavior.
```

`AGENTS.md` remains authoritative for repository structure, Git workflow,
security, and validation.
