# family-360

Family economy management application.

## Repository areas

- `docs/`: functional and technical documentation organized by domain.
- `apis/`: OpenAPI contracts organized by functional API.
- `backend/`: backend services and their Maven reactor modules.
- `database/`: independent PostgreSQL schema artifacts managed with Atlas.
- `frontend/`: frontend applications and domain features.

Start at [`docs/README.md`](docs/README.md) for the domain documentation map.

## Required software

- Git
- Java 25 and Maven 3.9+ for backend work
- Node.js 20.19+ or 22.12+ and npm for API and frontend work
- Atlas CLI 1.2+ and a Docker-compatible runtime exposed through the `docker`
  CLI for database work

Area-specific setup and validation commands are documented in each area's
`README.md` and `AGENTS.md`.
