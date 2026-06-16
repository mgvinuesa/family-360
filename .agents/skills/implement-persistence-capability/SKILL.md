---
name: implement-persistence-capability
description: Implement an already scoped Family 360 persistence change across Atlas desired schema, versioned migrations, JPA entities, Spring Data repositories, persistent output adapters, mappings, transaction boundaries, and database-focused tests. Use only after implement-domain-capability has approved persistence scope, or for explicitly bounded persistence-only work. Do not create API, frontend, or application behavior.
---

# Implement Persistence Capability

Own concrete persistence work while preserving Atlas as the database source of
truth and application ports as the backend contract.

## Establish Scope

1. Read root `AGENTS.md`, `backend/AGENTS.md`, `database/AGENTS.md`,
   `database/atlas.hcl`, and the owning database artifact `README.md`.
2. Check `git status --short --branch` and `git worktree list` before editing.
3. Read the coordinator-approved scope ledger when one exists. Otherwise prove
   the request is persistence-only before editing.
4. Identify the functional domain, backend module, application output ports,
   owning Atlas environment, PostgreSQL schema, and migration directory.
5. List the exact persistence operations included: stored fields, constraints,
   indexes, queries, mappings, transactional behavior, and adapter methods.
6. Separate required persistence work from inferred product behavior. Do not
   add authentication, authorization, roles, lifecycle workflows, audit
   behavior, soft-disable policy, or cross-schema constraints unless approved.
7. Treat a new application port operation, command, query, rule, endpoint, or
   UI behavior as another layer's capability. Return it to
   `$implement-domain-capability` unless already approved.

## Classify Persistence Impact

Classify every discovered change:

- **Persistence-local:** schema object, JPA mapping, Spring Data query,
  persistence mapper, adapter implementation, transaction boundary, repository
  test, or database validation.
- **Boundary adaptation:** an existing application output port can be
  implemented or remapped without changing application semantics.
- **Application capability change:** a new or changed port operation, domain
  rule, command, query, use case, or error is required.
- **API/frontend capability change:** public contract, generated boundary, or
  UI behavior must change.
- **Cross-artifact database dependency:** another Atlas artifact or schema is
  referenced.

Implement persistence-local changes and boundary adaptations only. Return other
capability changes to the coordinator for a scope decision.

## Work Atlas First

Use `database/atlas.hcl` as the entry point and run Atlas commands from
`database/`.

1. Inspect the desired source:

```bash
atlas schema inspect --env <schema-id> --url "env://src"
```

2. Edit only the owning `artifacts/<schema-id>/schema.pg.hcl` for modeled
   objects.
3. Keep object names explicit and schema-qualified in migrations.
4. Keep each artifact limited to the PostgreSQL schema it owns.
5. Avoid cross-schema foreign keys by default. If explicitly approved,
   document the dependency in both artifact `README.md` files.
6. Run:

```bash
atlas schema validate --env <schema-id>
```

7. Generate SQL with:

```bash
atlas migrate diff --env <schema-id> "<short-kebab-case-change-name>"
```

8. Review the generated SQL and `atlas.sum`. Do not accept unexplained
   destructive changes, unqualified identifiers, public-schema business
   objects, credential leaks, or edits to published migrations.
9. If an unpublished migration must be manually adjusted, run:

```bash
atlas migrate hash --env <schema-id>
```

Never edit `atlas.sum` manually.

When adding a new artifact, add its `README.md`, `schema.pg.hcl`,
`migrations/`, matching `atlas.hcl` environment, first schema-creation
migration, and independent `atlas.sum`.

## Implement Backend Persistence

Place handwritten persistence code under:

```text
<domain>.infrastructure.adapter.out.persistence
```

Use the existing functional backend module structure. Do not place handwritten
persistence code in root-level `repository` or `persistence` packages.

Implement:

- JPA entities that match the Atlas schema explicitly.
- Spring Data repositories for framework persistence access.
- Persistence mappers between JPA entities and domain/application models.
- Output adapters that implement existing `application.port.out` contracts.
- Spring configuration under `<domain>.infrastructure.configuration` only when
  explicit composition is needed.
- Transaction boundaries at the adapter or use-case integration point that
  matches existing project conventions.

Rules:

- Atlas desired state and migrations are the schema source of truth.
- Do not rely on Hibernate DDL auto-generation for schema evolution.
- Do not expose JPA entities through API or application contracts.
- Do not put generated OpenAPI types in persistence contracts.
- Prefer UUID primary keys and explicit table/column names.
- Use `OffsetDateTime` for technical timestamps and `LocalDate` for business
  dates.
- Avoid N+1 query behavior for list/read paths.
- Avoid production in-memory or alternate adapters merely to bypass missing
  persistence.
- Preserve privacy: do not log sensitive financial, personal, token, document,
  or banking values.

## Test

Add the lowest useful tests for changed behavior:

- Mapper tests for non-trivial entity/domain conversions.
- Repository tests for custom queries, constraints, pagination, filtering, or
  PostgreSQL-specific behavior.
- Adapter tests for port implementations and error mapping.
- Testcontainers when database behavior matters.

Do not broaden tests into API, frontend, or application behavior unless that
layer was approved.

## Validate

Run the validations required by changed areas:

1. For changed desired state:

```bash
atlas schema validate --env <schema-id>
```

2. For each changed migration directory:

```bash
atlas migrate hash --env <schema-id>
atlas migrate validate --env <schema-id>
```

3. Preview applying executable migrations:

```bash
atlas migrate apply --env <schema-id> --dry-run \
  --url "$DATABASE_URL" \
  --revisions-schema atlas_<schema-id>_revisions
```

4. Apply the full history to a disposable PostgreSQL database when required by
   `database/AGENTS.md` and explicitly available.
5. Run targeted Maven tests or the affected reactor verification required by
   `backend/AGENTS.md`.
6. Run `git diff --check`.

If Atlas, Docker, Java, Maven, or `DATABASE_URL` is unavailable, report the
missing prerequisite. Do not claim validation passed.

## Report Completion

Report:

- Atlas artifact, schema, migrations, and backend persistence files changed.
- Output ports implemented and persistence operations covered.
- Cross-schema or adjacent-layer impacts.
- Deferred or blocked work.
- Validation commands and results.
