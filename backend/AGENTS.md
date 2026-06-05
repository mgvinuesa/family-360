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

Prefer explicit use cases over large generic services.

Good examples:

- `CreateFamilyUseCase`
- `CreateHouseholdUseCase`
- `RegisterExpenseUseCase`
- `CreateContractUseCase`
- `GetFamilyExpenseSummaryUseCase`

Avoid dumping all logic into a single generic service.

## DTOs and API contracts

- API DTOs should come from OpenAPI generation when possible.
- Do not manually modify generated code.
- Map API DTOs to application/domain objects.
- Use MapStruct where it improves clarity.
- Do not expose JPA entities through controllers.

## Persistence

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

Family data access must always be scoped by `FamilyUser`.

A user can access a family only if:

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
