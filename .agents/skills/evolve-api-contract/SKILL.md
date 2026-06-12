---
name: evolve-api-contract
description: Evolve an OpenAPI contract and its generated backend or frontend boundaries, including generated interfaces, models, clients, HTTP adapters, API mappers, validation, and compatibility analysis. Use for endpoints, parameters, payloads, responses, API errors, versioning, or generation changes. Do not implement new domain or application capabilities; detect that impact and request activation of the application skill.
---

# Evolve API Contract

Own the public contract and generated boundaries while preserving application
layer ownership.

## Establish Scope

1. Read root, API, backend, and frontend `AGENTS.md` files for areas that may
   be affected.
2. Inspect `docs/domains/<domain>/README.md` and the documented paths.
3. Check repository and worktree status before editing.
4. Identify which generated targets are configured today. Do not introduce
   frontend generation merely because a frontend path is planned.
5. State whether the task includes backend generation, frontend generation, or
   both.

## Classify Compatibility

Classify the contract change:

- **No contract change:** only a generated-boundary adaptation is needed.
- **Compatible:** add an endpoint, response, optional property, or optional
  parameter to the current major version.
- **Breaking:** remove or rename API elements, change types or semantics,
  remove enum values, or make optional input required.

Do not break an existing major version. Introduce a new URL base and generated
package version for incompatible changes unless the user explicitly approves
another migration strategy.

## Analyze Adjacent-Layer Impact

Before editing, compare the requested contract with existing application
commands, queries, use cases, and ports.

Classify the impact:

- **API-local:** contract, generation, HTTP concerns, or API validation only.
- **Boundary adaptation:** generated names or shapes changed, while the
  application capability and semantics remain unchanged.
- **Application capability change:** the application needs new data, rules,
  commands, queries, use-case behavior, or port operations.
- **Persistence or frontend capability change:** another layer needs behavior
  beyond regenerating or remapping an existing boundary.

Handle API-local changes and boundary adaptations directly. This includes
implementing generated API interfaces and updating API DTO/domain mappings
when the application contract remains semantically unchanged.

Do not modify domain rules, application use cases, application commands,
queries, or ports. For an application capability change:

1. Explain the required application change.
2. Request confirmation to activate `$implement-application-capability`,
   unless a coordinator-approved plan already includes it.
3. Continue in the approved order after the application contract is ready.

Do not invent alternative adapters to avoid an adjacent-layer change.

## Change And Validate OpenAPI

1. Edit `apis/<domain>/openapi.yaml` first.
2. Preserve resource-oriented schema names and repository API conventions.
3. Keep write schemas separate when their shape differs from resources.
4. Model `PATCH` properties as optional.
5. Document stable problem responses.
6. Avoid generator-specific `x-*` extensions without clear necessity.
7. Run from `apis/`:

```bash
npm run api:check
```

Treat lint or bundle failures as contract failures.

## Regenerate Boundaries

Run generation from each configured target:

- Backend: run Maven from the deployable reactor so `generate-sources` uses the
  validated contract.
- Frontend: run the repository's generated-client command only when configured
  and included in scope.

Never edit or commit generated build output such as
`target/generated-sources/openapi`. Inspect generated signatures for:

- Method names and parameter ordering.
- Required, nullable, and optional properties.
- Bean Validation annotations.
- API version and package names.
- Client method and model changes.

## Adapt API Boundaries

Update handwritten API adapters, HTTP error mapping, and API mappers needed to
connect generated types to existing application contracts.

Examples of allowed boundary adaptations:

- Map a corrected query-parameter name to the same query attribute.
- Convert a changed API enum representation to an existing domain enum.
- Implement a newly generated interface method that delegates to an existing
  use case with equivalent semantics.

Adding search criteria to an application query is not a boundary adaptation;
it requires the application skill.

Add focused mapper and HTTP/API tests for changed behavior.

## Validate

1. Run `npm run api:check` from `apis/` when the contract changes.
2. Compile and test every generated backend target affected.
3. Run frontend generation, lint, tests, or build when frontend files change.
4. Run `git diff --check`.
5. Confirm generated build artifacts are not staged.
6. Report compatibility, generated targets, adjacent-layer decisions, and
   validation results.
