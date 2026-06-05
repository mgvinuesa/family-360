# apis/AGENTS.md

## Scope

This folder contains OpenAPI contracts and shared API schemas.

API changes should be contract-first.

Do not implement backend or frontend code from this folder unless explicitly requested.

## API design principles

- Use RESTful resources.
- Use UUID identifiers.
- Use explicit request and response DTOs.
- Avoid exposing persistence entities directly.
- Use `OffsetDateTime` for technical timestamps.
- Use ISO-8601 date/time formats.
- Use `date` for business dates such as expense date, contract start date, and renewal date.
- Keep enum values stable and uppercase.
- Prefer pagination for list endpoints.
- Avoid breaking changes unless explicitly requested.

## Naming conventions

Use PascalCase for schemas.

Examples:

- `Family`
- `CreateFamilyRequest`
- `UpdateFamilyRequest`
- `Expense`
- `CreateExpenseRequest`
- `MonthlyExpenseSummary`

Use camelCase for JSON properties.

Examples:

- `familyId`
- `expenseDate`
- `merchantName`
- `estimatedMonthlyCost`

Use plural REST resources.

Examples:

```text
/families
/families/{familyId}/members
/families/{familyId}/households
/families/{familyId}/expenses
/families/{familyId}/contracts
/families/{familyId}/expense-categories
```

## Endpoint examples

Families:

```text
POST   /families
GET    /families
GET    /families/{familyId}
PATCH  /families/{familyId}
```

Members:

```text
POST   /families/{familyId}/members
GET    /families/{familyId}/members
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

## Project structure

Create one yaml file per domain, use $ref in root openapi.yaml.

```
├──openapi.yaml
├── core/
├── family/
├────schema.yaml
├────path.yaml
├────path-id.yaml
├── household/
├── expense/
├── contract/
```

Use $ref in schemas and paths

```
paths:
  /families
    $ref: ./family/path.yaml
  /families/{familyId}:
    $ref: ./paths/path-id.yaml
```

Use folder `core` to keep main schemas, such as error response, common data...

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

Use a stable error response schema based on RFC 9457

Recommended base schema:

```
HTTP/1.1 422 Unprocessable Content
Content-Type: application/problem+json
Content-Language: en

{
 "type": "https://example.net/validation-error",
 "title": "Your request is not valid.",
 "errors": [
             {
               "detail": "must be a positive integer",
               "pointer": "#/age"
             },
             {
               "detail": "must be 'green', 'red' or 'blue'",
               "pointer": "#/profile/color"
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

If Redocly is configured:

```bash
npx @redocly/cli lint family-economy-api.yaml
```

## API checklist

Before finishing an API task, check:

- Are schemas explicit?
- Are DTOs separated between create/update/response?
- Are dates correctly typed?
- Are enum values stable and uppercase?
- Are list endpoints paginated?
- Are errors documented?
- Is the change breaking?
