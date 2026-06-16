# Repository Skills

This folder contains repository-scoped Codex skills for Family 360. Skills
complement the mandatory rules in `AGENTS.md`; they provide reusable workflows
and layer-specific decision boundaries.

## Authority Chain

Apply repository instructions in this order:

1. Root `AGENTS.md`: product scope, repository-wide constraints, skill routing,
   and Git workflow.
2. Area `AGENTS.md`: API, backend, or frontend architecture and validation.
3. `implement-domain-capability`: task-specific functional scope and approved
   layer plan.
4. Technical skill: implementation procedure inside one approved
   responsibility.

A lower level may make local implementation decisions but must not broaden the
scope established by a higher level.

## Functional Coordinator

### `implement-domain-capability`

Coordinate a functional domain change, classify its impact by layer, obtain
confirmation for scope expansions, invoke the required technical skills, and
validate the combined result. Despite its name, this is a functional
coordinator, not the owner of domain-model implementation.

## Technical Skills

### `evolve-api-contract`

Own OpenAPI contracts, generated backend and frontend boundaries, HTTP
adapters, and API mappings. It may adapt a boundary when application semantics
remain unchanged, but it must return to the functional coordinator when it
discovers a new application capability.

Handwritten adapters follow the backend package convention:
`<domain>.infrastructure.adapter.in.<technology>` and
`<domain>.infrastructure.adapter.out.<technology>`. Spring composition belongs
under `<domain>.infrastructure.configuration`; application ports remain under
`application.port`.

### `implement-application-capability`

Own domain rules, one class per business use case, commands, queries, ports, and
application behavior already included in the confirmed scope. Authorization is
implemented only when explicitly approved. The skill assesses API and
persistence impact without changing those layers implicitly.

### `implement-persistence-capability`

Own already approved persistence work across Atlas desired schema, versioned
migrations, JPA entities, Spring Data repositories, persistent output adapters,
transactions, mappings, and database-focused verification. It may implement an
existing application output port or adapt a persistence boundary when
application semantics remain unchanged, but it must return to the functional
coordinator when it discovers a new application, API, frontend, or database
artifact capability.

## Invocation

Use the coordinator for functional outcomes and the technical skills for
bounded layer work:

```text
Use $implement-domain-capability to implement family search by member name.
```

```text
Use $evolve-api-contract to correct the pageSize query parameter name without
changing application behavior.
```

If a request names a technical skill but also asks for CRUD or another
functional outcome, activate the coordinator as well and run the named skill
inside the confirmed scope.

Do not chain technical skills directly when a new capability or layer is
discovered. Return to the coordinator for a scope decision.

`AGENTS.md` remains authoritative for repository structure, architecture,
scope, Git workflow, security safeguards, and validation.
