---
name: api-change-workflow
description: Implement contract-first changes that may affect OpenAPI contracts, generated backend interfaces or models, backend API adapters, and eventually generated frontend clients. Use for new endpoints, request or response changes, validation changes, API-visible error changes, API versioning, or backend work whose requirements may alter an API contract.
---

# API Change Workflow

Apply API-visible changes in validated baby steps while keeping published
commits compilable.

## Establish Scope

1. Read the root `AGENTS.md` and the `AGENTS.md` files for every affected area.
2. Check `git status --short --branch` and `git worktree list`.
3. Create a task-specific branch or worktree before editing.
4. Identify the domain through `docs/domains/<domain>/README.md`.
5. Inspect the documented API, backend, and frontend paths before deciding
   which areas require modification.

Do not modify frontend merely because the API changes. Include it only when the
request covers frontend integration and a generated-client workflow exists.

## Classify API Impact

Classify the requirement before editing:

- **No API impact:** Implement inside backend without changing OpenAPI.
- **Compatible API change:** Add an endpoint, optional property, response, or
  optional parameter to the current major version.
- **Breaking API change:** Remove or rename API elements, change types or
  semantics, remove enum values, or make optional input required.

Do not break an existing major version. For an incompatible change, introduce a
new URL base such as `/api/v2` and Java packages such as `api.v2`, while keeping
the previous version operational until separately deprecated.

## Plan Green Baby Steps

Prefer these independently meaningful steps:

1. Implement or extend domain rules and application use cases with unit tests.
2. Change the OpenAPI contract, regenerate code, and add the minimum backend
   API adapter and mapping needed to restore compilation.
3. Complete backend behavior, authorization, error mapping, and API tests.
4. Add frontend generated-client integration in a later step when requested.

The working tree may temporarily fail while generated signatures are changing.
Do not publish a commit that knowingly leaves the build broken. Contract and
generated-interface adaptation normally belong in the same commit.

Create a small commit after each verified step when the change is complex.

## Change The Contract

1. Edit `apis/<domain>/openapi.yaml` first.
2. Preserve resource-oriented schema names and existing API conventions.
3. Avoid `x-*` extensions unless the requirement needs generator-specific
   behavior with clear value.
4. Run from `apis/`:

```bash
npm run api:check
```

Treat lint or bundle failures as contract failures. Fix them before adapting
backend code.

## Regenerate And Inspect

Run Maven from the deployable backend reactor:

```bash
mvn -pl <functional-module> -am compile
```

Generation runs during `generate-sources`. Do not edit or commit files under
`target/generated-sources/openapi`.

Inspect the generated differences conceptually:

- API interface methods and checked exceptions.
- Request and response model types.
- Required and nullable properties, especially `PATCH` payloads.
- Bean Validation annotations.
- Versioned base path and package names.

If compilation fails because a generated signature changed, adapt handwritten
code rather than modifying generated sources.

## Implement Backend

Keep generated API types at the HTTP boundary:

1. Implement the generated interface in an API adapter.
2. Delegate to small application use cases.
3. Map generated DTOs to application or domain objects and back.
4. Keep persistence entities out of API responses.
5. Map domain exceptions through central API exception handling.
6. Enforce the authorization and resource-scoping rules defined by the
   repository and affected domain.
7. Apply the privacy and sensitive-data logging rules from the applicable
   `AGENTS.md` files.

Add tests at the lowest useful level:

- Unit tests for domain rules and use cases.
- Mapper tests for non-trivial transformations.
- Controller/API tests for HTTP behavior, validation, and error responses.
- Integration tests only when framework or database behavior matters.

## Integrate Frontend

Use this step only when requested and generation is configured:

1. Regenerate clients from the same validated contract.
2. Keep generated files unmodified.
3. Adapt generated models in feature services or mappers.
4. Keep authorization and business rules in backend.
5. Test forms, client integration, loading, empty, and error states.

If frontend work exposes a missing capability, return to the contract step.
Do not add a handwritten HTTP workaround.

## Validate And Publish

Run the narrowest checks during development, then complete the repository
checks for every modified area:

```bash
cd apis
npm run api:check

cd ../backend/<service-id>
mvn verify
```

Run frontend checks when frontend files change.

Before publishing:

1. Confirm `git diff --check` passes.
2. Confirm generated output and build artifacts are not staged.
3. Review commits for coherent, green baby steps.
4. Push the branch and create a draft pull request as required by `AGENTS.md`.
5. Record the contract compatibility decision and validation commands in the
   pull request description.
