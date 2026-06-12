# backend/AGENTS.md

## Scope

This folder contains the Spring Boot backend application.

Do not modify `frontend/` from backend tasks unless explicitly requested.

If a backend change requires a public API change, update `apis/` first or clearly state that the OpenAPI contract needs to be changed.

## Preferred stack

- Java 25
- Spring Boot 4.x
- Maven
- PostgreSQL
- Flyway
- JPA / Hibernate
- MapStruct
- OpenAPI Generator
- JUnit 5
- Mockito
- Testcontainers when database behavior matters

## Architecture

Use a clear layered or hexagonal style.

Recommended functional package structure:

```text
backend/src/main/java/.../
├── family/
├── household/
├── expense/
├── contract/
├── account/
├── category/
└── shared/
```

Each functional area may contain:

```text
application/
domain/
infrastructure/
api/
```

Implement one explicit use-case class per business action. Use the
`<Action><Concept>UseCase` naming convention unless the existing module has a
more specific established pattern.

Good examples:

- `CreateFamilyUseCase`
- `CreateHouseholdUseCase`
- `RegisterExpenseUseCase`
- `CreateContractUseCase`
- `GetFamilyExpenseSummaryUseCase`

Avoid dumping all logic into a single generic service.

Do not implement several CRUD actions in aggregate classes such as
`FamilyApplicationService`, `FamilyOperations`, `FamilyManager`, or generic
CRUD services. HTTP adapters may collaborate with multiple use-case input
ports, but each action must remain independently named, testable, and
injectable.

## DTOs and API contracts

- API DTOs should come from OpenAPI generation when possible.
- Do not manually modify generated code.
- Map API DTOs to application/domain objects.
- Use MapStruct where it improves clarity.
- Do not expose JPA entities through controllers.

For functional backend requests, start with `implement-domain-capability`.
After its scope plan is confirmed, use `evolve-api-contract` for OpenAPI,
generated backend boundaries, HTTP adapters, and API mappings, and use
`implement-application-capability` for domain rules, individual use cases,
commands, queries, ports, and application behavior.

Invoke a technical skill directly only when the task is explicitly confined to
that technical responsibility and does not add product behavior.

A skill may adapt a boundary it owns when adjacent semantics remain unchanged.
If a technical skill discovers a new capability in another layer, return to
the functional coordinator for a scope decision unless that layer was already
approved.

## Persistence

Persistence changes are intentionally excluded from the current API and
application skills. Until a dedicated persistence skill is defined, do not
modify Flyway migrations, JPA entities, Spring Data repositories, or concrete
persistent adapters as an implicit consequence of API or application work.
Define required application ports when appropriate, then report concrete
persistence implementation as deferred or blocked.

- Use Flyway migrations.
- Do not rely on Hibernate DDL auto-generation for real schema evolution.
- Prefer explicit table and column names.
- Use UUID primary keys.
- Use `created_at`, `updated_at`, and `disabled_at` where useful.
- Prefer soft-disable for business entities that should remain historically traceable.
- Expenses should not be hard-deleted unless explicitly requested.
- Avoid N+1 qu

Recommended table names:

- `user_account`
- `family`
- `family_user`
- `family_member`
- `household`
- `household_member`
- `financial_account`
- `expense_category`
- `expense`
- `contract`

## Dates and time

- Use `OffsetDateTime` for technical timestamps.
- Use `LocalDate` for business dates such as `expenseDate`, `startDate`, `endDate`, and `renewalDate`.
- Store timestamps consistently.
- Do not silently convert time zones in domain logic.

## Validation

Validate commands at the application boundary.

Examples:

- Amount must be positive for regular expenses.
- Expense date is required.
- Family ID is required.
- Household must belong to the same family.
- Contract linked to an expense must belong to the same family context.
- Category linked to an expense must belong to the same family or be a system category.

## Error handling

Use domain-specific exceptions.

Avoid leaking low-level exceptions to API responses.

Recommended pattern:

- Domain/application exception
- Central API exception handler
- Stable error response schema

## Security

Always follow secure coding and data-protection practices. Do not expose
sensitive data, trust unvalidated external input, log protected information,
or introduce accidental cross-family queries.

Authentication, authorization, `FamilyUser` membership checks, roles, and
permissions are functional behavior. Do not implement them merely because a
resource belongs to a family. Include them only when explicitly requested or
confirmed by the user.

When family authorization is in the approved scope, a user can access a family
only if:

- The user is authenticated.
- The user has an active relation with the family.
- The requested operation is allowed by the user's family role.

## Testing

Every meaningful behavior change should include tests.

Preferred tests:

- Unit tests for domain rules and use cases
- Repository tests for custom queries
- Controller/API tests for request/response behavior
- Integration tests only when they provide real value

Use Mockito for isolated application service tests.

Use Testcontainers when database-specific behavior matters.

Avoid slow full-context tests unless necessary.

## Suggested commands

```bash
./mvnw clean verify
```

or:

```bash
mvn clean verify
```

For targeted tests:

```bash
mvn -Dtest=ClassNameTest test
```

## Backend implementation checklist

Before finishing a backend task, check:

- Are API contracts aligned?
- Is a Flyway migration required?
- Are generated DTOs/interfaces untouched?
- Are entities not exposed directly through controllers?
- Are validations covered?
- Are tests added or updated?
- Are errors mapped consistently?
