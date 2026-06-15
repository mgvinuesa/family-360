# Database

This area manages the Family 360 PostgreSQL lifecycle with Atlas.

Database artifacts evolve independently from backend microservices. Each
functional artifact owns one PostgreSQL schema, its desired HCL state, and its
versioned SQL migrations.

Read [AGENTS.md](AGENTS.md) before changing this area.

## Structure

```text
database/
|-- AGENTS.md
|-- README.md
|-- atlas.hcl
`-- artifacts/
    |-- bootstrap/
    |   |-- README.md
    |   `-- migrations/
    `-- family/
        |-- README.md
        |-- schema.pg.hcl
        `-- migrations/
```

`bootstrap` is reserved for database-wide prerequisites. `family` is the first
functional schema artifact and currently contains only its empty desired
schema declaration.

No executable migrations have been added yet. The first migration of each
functional artifact must create its owned PostgreSQL schema and generate its
own `atlas.sum`.

The backend currently still declares Flyway dependencies. Removing that legacy
runtime integration is intentionally deferred to a separate backend change.

## Prerequisites

- Atlas CLI 1.2 or newer, available on `PATH`
- Docker-compatible container runtime available through the `docker` CLI
- PostgreSQL 17 compatibility

Verify the local tools:

```bash
atlas version
docker version
```

The Atlas CLI is installed as an official native binary, not as an npm
dependency. The version currently verified by the project maintainer is:

```text
atlas version v1.2.3-fe25ae2-canary
```

This is a canary build used locally, not a repository-wide version pin. Future
CI configuration should pin an explicitly approved Atlas version. After adding
Atlas to `PATH`, restart existing terminals and development tools so they
inherit the updated environment.

On Windows, the current installation convention is:

```text
ATLAS_HOME=<directory containing atlas.exe>
PATH=...;%ATLAS_HOME%
```

Open a new terminal after changing either environment variable.

Set the target database URL before commands that connect to a persistent
database:

```bash
export DATABASE_URL='postgres://user:password@localhost:5432/family360?sslmode=disable'
```

PowerShell:

```powershell
$env:DATABASE_URL = 'postgres://user:password@localhost:5432/family360?sslmode=disable'
```

## Commands

Run commands from `database/`.

```bash
atlas schema inspect --env family --url "env://src"
atlas schema validate --env family
atlas migrate validate --env family
atlas migrate diff --env family "<change-name>"
atlas migrate hash --env family
atlas migrate apply --env family --dry-run \
  --revisions-schema atlas_family_revisions
```

Remove `--dry-run` only after reviewing the migration plan and when applying
changes is intentional.

Atlas migration linting and Atlas Cloud are not required by this initial
setup.

Agent-specific Atlas guidance:

- [Using Atlas with AI agents](https://atlasgo.io/guides/ai-tools)
- [OpenAI Codex with Atlas](https://atlasgo.io/guides/ai-tools/codex-instructions)
