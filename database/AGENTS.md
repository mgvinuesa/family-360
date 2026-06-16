# database/AGENTS.md

## Scope

This folder owns PostgreSQL schema lifecycle management for Family 360.

Database artifacts have a lifecycle independent from backend microservices.
Do not place migrations under `backend/`, package them inside a deployable
microservice, or make application startup responsible for production schema
changes.

Changes under this folder must not modify backend, API, or frontend behavior
unless the task explicitly includes those areas. When a database change
reveals a required application or contract change, report that impact and
return scope control to `implement-domain-capability`.

When a scoped task includes both database schema evolution and backend
persistence code, use `implement-persistence-capability`. That skill owns the
coordinated Atlas artifact changes, JPA entities, Spring Data repositories,
persistent output adapters, mappings, and database-focused verification inside
the approved scope.

## Preferred stack

- PostgreSQL
- Atlas CLI 1.2 or newer
- Atlas versioned migrations
- HCL for desired schema state when Atlas can model the object clearly
- SQL for generated and manually authored migration files
- Docker-backed Atlas dev databases

Follow the official Atlas guidance for AI agents:

```text
https://atlasgo.io/guides/ai-tools
https://atlasgo.io/guides/ai-tools/codex-instructions
```

Atlas Pro features, Atlas Cloud, migration linting, schema registry, and drift
monitoring are not required by the initial setup. Do not introduce a paid or
hosted Atlas dependency without explicit approval.

## Artifact model

Database work is organized as independently evolvable artifacts:

```text
database/
|-- atlas.hcl
`-- artifacts/
    |-- bootstrap/
    |   |-- README.md
    |   `-- migrations/
    `-- <schema-id>/
        |-- README.md
        |-- schema.pg.hcl
        `-- migrations/
```

Use singular kebab-case for `<schema-id>` and keep it aligned with the
functional domain identifier whenever practical.

Each functional artifact owns:

- One PostgreSQL schema.
- Its desired schema state.
- Its versioned migration directory.
- Its own `atlas.sum`.
- Its own Atlas revision history namespace.
- Its own release and deployment lifecycle.

The `bootstrap` artifact owns only database-wide prerequisites that cannot
belong to one functional schema, such as required PostgreSQL extensions,
technical roles, or shared grants. It must not own business tables.

The functional artifact owns creation of its PostgreSQL schema. A schema must
not require `bootstrap` merely to exist.

## Schema boundaries

An artifact may create, alter, or drop objects only in the PostgreSQL schema it
owns.

Do not:

- Modify another artifact's objects.
- Place business objects in `public`.
- use `public` as a shared application schema.
- Create an unowned shared schema as a shortcut.
- Add cross-schema foreign keys by default.

References to identifiers owned by another schema should normally remain
logical application references without a database foreign key. A cross-schema
constraint creates deployment ordering and lifecycle coupling and requires an
explicit architecture decision.

If an approved cross-schema dependency exists:

- Document it in both artifact `README.md` files.
- Qualify referenced objects explicitly.
- Define the minimum compatible artifact version.
- Validate deployment ordering.
- Do not allow cyclic artifact dependencies.

## Desired state

Use `schema.pg.hcl` as the desired state for functional schemas.

Keep schema and object names explicit:

```hcl
schema "family" {
}

table "family" {
  schema = schema.family
  // ...
}
```

The desired state is not the deployment artifact. Versioned SQL migrations are
the executable and reviewable record used to evolve databases.

Do not use ORM metadata or Hibernate DDL generation as the source of truth for
database evolution.

Objects that Atlas cannot model safely may be authored directly in SQL. Keep
that decision local and document why the object is not represented in HCL.

## Versioned migrations

Each artifact has an independent migration history:

```text
artifacts/<schema-id>/migrations/
|-- 20260615100000_create_schema.sql
|-- 20260615101500_create_example_table.sql
`-- atlas.sum
```

Migration filenames must use:

```text
YYYYMMDDHHMMSS_short_kebab-case-description.sql
```

Rules:

- Never modify or delete a migration that has been published or applied.
- Add a forward migration to correct released behavior.
- Generate migrations with `atlas migrate diff` when possible.
- Review generated SQL before accepting it.
- Keep one coherent database change per migration.
- Prefer forward-only expand/contract changes.
- Avoid destructive changes in the same release that removes application use.
- Qualify schema names in migration SQL.
- Use PostgreSQL-compatible transactional DDL where possible.
- Document migrations that require `--atlas:txmode none`.
- Regenerate `atlas.sum` with Atlas; never edit it manually.

The integrity file is part of the artifact and must be committed whenever its
migration directory changes.

## Atlas configuration

`database/atlas.hcl` is the repository entry point.

Always read `atlas.hcl` before running Atlas commands. Use its environment names
and configuration instead of reconstructing URLs or options manually.

Each artifact must have its own Atlas environment. Environment names should
match artifact folder names.

Commands should run from `database/`:

```bash
atlas schema inspect --env <schema-id> --url "env://src"
atlas schema validate --env <schema-id>
atlas migrate diff --env <schema-id> "<change-name>"
atlas migrate hash --env <schema-id>
atlas migrate validate --env <schema-id>
atlas migrate status --env <schema-id> \
  --revisions-schema atlas_<schema-id>_revisions
atlas migrate apply --env <schema-id> --dry-run \
  --revisions-schema atlas_<schema-id>_revisions
```

Run `atlas migrate apply` without `--dry-run` only after reviewing the preview
and only when applying changes is explicitly part of the task.

Use one technical revision schema per artifact:

```text
atlas_bootstrap_revisions
atlas_family_revisions
atlas_expense_revisions
```

Do not let independent artifacts share one `atlas_schema_revisions` table.

Connection URLs must come from environment variables or secret management.
Never commit credentials. `DATABASE_URL` is the default local variable expected
by `atlas.hcl`. Do not ask users to paste database credentials into prompts or
command output.

## Dev database

Atlas operations that compute or validate schema state must use an ephemeral
PostgreSQL dev database matching the production major version.

For multi-schema work, use database scope and omit `search_path` from the dev
URL so generated migrations use qualified identifiers:

```text
docker://postgres/17/dev
```

Do not use an H2, SQLite, or in-memory substitute to validate PostgreSQL schema
behavior.

Update the configured PostgreSQL version intentionally and validate every
artifact before merging the upgrade.

## Validation

Migration linting is intentionally not required in the initial Atlas setup.

After changing a desired-state HCL file, run:

```bash
atlas schema validate --env <schema-id>
```

For every changed migration directory, run:

```bash
atlas migrate hash --env <schema-id>
atlas migrate validate --env <schema-id>
```

`atlas migrate hash` is required after manually editing an unpublished
migration. It must never be used to legitimize edits to a published or applied
migration.

When the artifact contains executable migrations, also apply the full history
to an empty disposable PostgreSQL database before staging or committing:

```bash
atlas migrate apply --env <schema-id> --dry-run \
  --url "$DATABASE_URL" \
  --revisions-schema atlas_<schema-id>_revisions
atlas migrate apply --env <schema-id> \
  --url "$DATABASE_URL" \
  --revisions-schema atlas_<schema-id>_revisions
```

When changing desired state, verify that the migration directory converges to
that state. Do not accept unexplained schema differences.

If Atlas or Docker is unavailable, do not claim validation passed. State the
missing prerequisite clearly.

## Release model

Treat each schema folder as an independently publishable artifact even while
all sources live in this monorepo.

A future publication mechanism may produce an OCI migrator image per artifact.
Do not add packaging, registries, deployment jobs, or release automation until
that delivery scope is explicitly approved.

Artifact versions must be immutable. Record cross-artifact compatibility when
a real dependency exists.

## Security

Do not commit:

- Database passwords or connection URLs containing credentials.
- Production data.
- Dumps containing family or financial information.
- Tokens or cloud credentials.
- Unmasked personal or banking identifiers in seed scripts.

Grant the minimum privileges needed. Do not make application roles schema
owners unless explicitly required.

## Agent workflow

Before editing database files:

1. Read this file, `atlas.hcl`, and the artifact `README.md`.
2. Check `git status --short --branch` and `git worktree list`.
3. Work on a task-specific branch.
4. Identify the owning schema artifact.
5. Classify cross-schema impact before editing.

For schema changes:

1. Inspect the desired source at a high level with
   `atlas schema inspect --env <schema-id> --url "env://src"`.
2. Edit the owning `schema.pg.hcl`.
3. Run `atlas schema validate --env <schema-id>`.
4. Generate SQL with `atlas migrate diff --env <schema-id> "<change-name>"`.
5. Review the generated migration and `atlas.sum`.
6. Validate the migration directory.
7. Use `atlas migrate apply --dry-run` before any approved apply.

When adding a schema artifact:

1. Add `artifacts/<schema-id>/README.md`.
2. Add `artifacts/<schema-id>/schema.pg.hcl`.
3. Add `artifacts/<schema-id>/migrations/`.
4. Add a matching environment to `atlas.hcl`.
5. Make the first migration create the owned PostgreSQL schema.
6. Generate and commit `atlas.sum`.
7. Document dependencies and ownership.

Do not add tables, columns, indexes, constraints, grants, or lifecycle behavior
that were not included in the approved capability.

## Checklist

Before finishing a database task, check:

- Is the change in the owning artifact?
- Does the artifact modify only its own schema?
- Is the desired state aligned with migrations?
- Are identifiers schema-qualified?
- Are published migrations untouched?
- Did `atlas schema validate` pass after desired-state changes?
- Was `atlas.sum` generated by Atlas?
- Did Atlas validation pass?
- Was an apply previewed with `--dry-run`?
- Was the full history applied to disposable PostgreSQL when required?
- Are cross-schema dependencies explicit and acyclic?
- Are credentials and sensitive data absent?
