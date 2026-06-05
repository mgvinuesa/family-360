# apis/AGENTS.md

## Scope

This folder contains OpenAPI contracts and shared API schemas.

API changes should be contract-first.

Do not implement backend or frontend code from this folder unless explicitly requested.

## API organization

Separate functional APIs into independent folders.

Each functional API should own its OpenAPI entry point:

```text
apis/
  family/
    openapi.yaml
  another-functional-area/
    openapi.yaml
  yet-another-functional-area/
    openapi.yaml
```

Use one functional API per folder when the bounded area can evolve independently.

For the MVP:

- `apis/family/openapi.yaml` contains `Family` and direct family relationships.
- Expenses must not be added to the family API unless explicitly requested.
- Contracts must not be added to the family API unless explicitly requested.

Common shared schemas may be extracted later to `apis/core/` when two or more functional APIs really need them. Do not create shared abstractions prematurely.

## API design principles

- Use RESTful resources.
- Use UUID identifiers.
- Use resource-oriented schema names.
- Use separate read and write schemas when the API shape differs.
- Avoid exposing persistence entities directly.
- Use `OffsetDateTime` for technical timestamps.
- Use ISO-8601 date/time formats.
- Use `date` for business dates.
- Keep enum values stable and uppercase.
- Prefer pagination for list endpoints.
- Avoid breaking changes unless explicitly requested.

## Resource and payload schemas

Prefer resource-oriented names over operation-oriented names.

Recommended:

- `Family`
- `FamilyInput`
- `FamilyPatch`
- `FamilyPage`
- `HouseholdMember`
- `HouseholdMemberInput`
- `HouseholdMemberPatch`
- `HouseholdMemberPage`

Avoid unless there is a strong reason:

- `CreateFamilyRequest`
- `UpdateFamilyRequest`
- `AddHouseholdMemberRequest`
- `UpdateHouseholdMemberRequest`

Use these conventions:

- Resource schemas represent API responses.
- `Input` schemas represent writable payloads for `POST` or full replacement.
- `Patch` schemas represent partial updates for `PATCH`.
- `Page` schemas represent paginated list responses.

POST payloads should not include server-generated fields or identifiers already present in the URL.

PATCH payloads should model partial updates with optional fields.

PUT should represent full replacement only when the endpoint explicitly supports replacement semantics. Prefer PATCH for partial updates.

Use `allOf` to share common schema fragments when it improves consistency, but avoid deep inheritance trees.

## Naming conventions

Use PascalCase for schemas.

Examples:

- `Family`
- `FamilyInput`
- `FamilyPatch`
- `FamilyPage`
- `FamilyMember`
- `Household`

Use camelCase for JSON properties.

Examples:

- `familyId`
- `memberId`
- `householdId`
- `mainHousehold`

Use plural REST resources.

Examples:

```text
/families
/families/{familyId}/members
/families/{familyId}/households
/families/{familyId}/financial-accounts
```

## Endpoint examples

Families:

```text
POST   /families
GET    /families
GET    /families/{familyId}
PATCH  /families/{familyId}
DELETE /families/{familyId}
```

Members:

```text
POST   /families/{familyId}/members
GET    /families/{familyId}/members
GET    /families/{familyId}/members/{memberId}
PATCH  /families/{familyId}/members/{memberId}
DELETE /families/{familyId}/members/{memberId}
```

Households:

```text
POST   /families/{familyId}/households
GET    /families/{familyId}/households
GET    /families/{familyId}/households/{householdId}
PATCH  /families/{familyId}/households/{householdId}
DELETE /families/{familyId}/households/{householdId}
```

## Pagination

For list endpoints, prefer:

Query parameters:

- `limit`
- `page`
- `sort`

Response metadata:

- `page`
- `limit`
- `totalElements`
- `totalPages`

## Error response

Use a stable error response schema based on RFC 9457.

Recommended base schema:

```json
{
  "type": "https://example.net/validation-error",
  "title": "Your request is not valid.",
  "status": 422,
  "errors": [
    {
      "detail": "must be a positive integer",
      "pointer": "#/age"
    }
  ]
}
```

## Breaking changes

Avoid breaking changes unless explicitly requested.

Breaking examples:

- Removing a property
- Renaming a property
- Changing a type
- Removing an enum value
- Making an optional field required
- Removing an endpoint
- Changing response semantics

Non-breaking examples:

- Adding an optional property
- Adding a new endpoint
- Adding a new response schema
- Adding a new optional query parameter

## Validation command

Redocly is the required local API validation tool.

Before committing changes under `apis/`, run these commands from the `apis/` folder:

```bash
npm run api:lint
npm run api:bundle
```

Or run the combined check:

```bash
npm run api:check
```

If the commands cannot be run because Node.js, npm, or dependencies are unavailable, clearly state that in the final response.

The bundled output is generated under `apis/dist/` and should not be committed.

## Agent workflow

Keep this process in `AGENTS.md` because it is repository-specific and should guide every future API change.

A dedicated Codex skill is not necessary yet. Consider creating one only if the API workflow becomes reusable across multiple repositories or grows into a richer process with generation, breaking-change baselines, docs publishing, and CI orchestration.

For larger API changes, an additional robustness step is to ask for an independent API review pass. Codex can use a subagent for that only when the user explicitly asks for subagent-based or delegated review.

## API checklist

Before finishing an API task, check:

- Is the functional API in its own folder?
- Did Redocly lint pass?
- Did Redocly bundle pass?
- Are schemas resource-oriented?
- Are read and write schemas separated only when their shapes differ?
- Do POST payloads avoid server-generated fields and URL identifiers?
- Do PATCH payloads use optional fields?
- Are dates correctly typed?
- Are enum values stable and uppercase?
- Are list endpoints paginated?
- Are errors documented?
- Is the change breaking?
