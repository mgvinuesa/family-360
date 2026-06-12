---
name: implement-application-capability
description: Implement backend domain and application behavior, including rules, use cases, commands, queries, ports, and input or output adapters that do not require concrete persistence integration. Use for new business behavior, authorization, validation, orchestration, or application contracts. Detect API and persistence impact, and request the corresponding skill before changing those layers.
---

# Implement Application Capability

Own domain and application behavior and assess every adjacent boundary.

## Establish Scope

1. Read the root and backend `AGENTS.md` files.
2. Inspect `docs/domains/<domain>/README.md` and the functional backend module.
3. Check repository and worktree status before editing.
4. Describe the business outcome, actors, authorization, invariants, inputs,
   outputs, and failure cases.
5. Inspect existing API adapters and persistence adapters to understand current
   contracts without assuming they must change.

## Analyze Adjacent-Layer Impact

Classify each discovered impact:

- **Application-local:** domain rules, application orchestration, validation,
  authorization, commands, queries, or ports.
- **Boundary adaptation:** an existing adapter can map to the changed
  application contract without changing the external capability of its layer.
- **API capability change:** a public endpoint, parameter, payload, response,
  error, or generated boundary must change.
- **Persistence capability change:** a new stored field, schema change, query
  criterion, transaction behavior, repository operation, or persistent mapping
  is required.

Implement application-local changes and adapters owned by the application
module. Do not change OpenAPI or generated API code directly. For API impact:

1. Explain the required contract or boundary change.
2. Request confirmation to activate `$evolve-api-contract`, unless it is
   already included in the coordinator-approved plan.

Persistence is not yet supported by a dedicated repository skill. Do not
modify Flyway, JPA entities, Spring Data repositories, or concrete persistent
adapters. When required:

1. Define the application port contract if that is part of the approved
   application capability.
2. Explain the persistence behavior needed to implement it.
3. Mark concrete persistence work as deferred or blocked.
4. Do not create an in-memory or alternate adapter merely to make the feature
   appear complete.

## Implement The Capability

Prefer explicit concepts:

- Small use cases named after business actions.
- Commands for state-changing inputs.
- Queries for reads and search criteria.
- Domain value objects and enums for meaningful constraints.
- Input ports for use-case invocation.
- Output ports for capabilities required from external systems.
- Domain/application exceptions mapped by boundary adapters.

Keep generated API types and persistence entities outside domain and
application contracts. Enforce family membership, role authorization, and
resource scoping at the appropriate application boundary.

Adapters may be updated when they translate an existing external capability to
the changed application contract. If the adapter's external layer needs a new
capability, activate the corresponding technical skill instead.

## Test

Add tests at the lowest useful level:

- Domain tests for invariants and state transitions.
- Use-case tests for orchestration, authorization, and failures.
- Port interaction tests for required collaborations.
- Adapter tests only for adapters changed within approved scope.

Use mocks or fakes inside isolated tests, but do not add production substitute
adapters for unavailable persistence.

## Reassess Impact

After implementation:

1. Compare new commands, queries, and ports with API and persistence adapters.
2. Identify unimplemented adjacent-layer capabilities.
3. Request confirmation before expanding scope unless already approved.
4. Do not claim the functional capability is complete while required
   persistence or API work remains deferred.

## Validate

1. Run targeted tests during development.
2. Run the affected Maven reactor verification required by `backend/AGENTS.md`.
3. Run `git diff --check`.
4. Report implemented behavior, port changes, adjacent-layer impact, deferred
   integrations, and validation results.
