---
name: implement-application-capability
description: Implement an already scoped backend domain and application change through explicit use-case classes, commands, queries, rules, and ports. Use directly only for application-layer tasks whose functional scope is explicit and does not require cross-layer planning; for CRUD, new business behavior, API-visible work, or uncertain multi-layer impact, use implement-domain-capability first.
---

# Implement Application Capability

Own domain and application behavior and assess every adjacent boundary.

## Establish Scope

1. Read the root and backend `AGENTS.md` files.
2. Inspect `docs/domains/<domain>/README.md` and the functional backend module.
3. Check repository and worktree status before editing.
4. Read the coordinator-approved scope ledger when one exists. Otherwise
   classify the request as application-only before editing.
5. List explicit actions, required invariants, inputs, outputs, and failure
   cases. Separate requested behavior from inferred behavior.
6. Treat authentication, authorization, roles, ownership, invitations, and
   audit behavior as product capabilities. Do not add them unless they are in
   the approved scope.
7. Apply cross-cutting secure coding and privacy safeguards without inventing
   those product capabilities.
8. Inspect existing API adapters and persistence adapters to understand current
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

Implement application-local changes and application composition owned by the
functional module. Do not change OpenAPI, generated API code, HTTP adapters, or
concrete persistence adapters directly. For API impact:

1. Explain the required contract or boundary change.
2. Return the impact to `$implement-domain-capability` for a scope decision,
   unless the coordinator-approved plan already includes API work.

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

Follow the backend architecture in `backend/AGENTS.md`:

- Implement one concrete `*UseCase` class per business action, such as
  `CreateFamilyUseCase`, `GetFamilyUseCase`, or `ListFamilyMembersUseCase`.
- Do not group multiple actions behind generic implementations such as
  `FamilyApplicationService`, `FamilyOperations`, or CRUD manager classes.
- A generated or handwritten HTTP adapter may depend on several input ports,
  but each port represents one business action.
- Commands for state-changing inputs.
- Queries for reads and search criteria.
- Domain value objects and enums for meaningful constraints.
- Input ports for use-case invocation.
- Output ports for capabilities required from external systems.
- Domain/application exceptions mapped by boundary adapters.

Keep generated API types and persistence entities outside domain and
application contracts. Implement authorization or resource-scoping behavior
only when included in the approved functional scope.

If an adjacent layer needs a new capability, return control to the coordinator
instead of activating another technical skill directly.

## Test

Add tests at the lowest useful level:

- Domain tests for invariants and state transitions.
- Use-case tests for orchestration, approved authorization behavior, and
  failures.
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
