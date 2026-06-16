# family-360

Family economy management application.

## Repository areas

- `docs/`: functional and technical documentation organized by domain.
- `apis/`: OpenAPI contracts organized by functional API.
- `backend/`: backend services and their Maven reactor modules.
- `database/`: independent PostgreSQL schema artifacts managed with Atlas.
- `frontend/`: frontend applications and domain features.

Start at [`docs/README.md`](docs/README.md) for the domain documentation map.

## Project software

Install only the tools needed for the repository area you are working on:

- **Common:** Git.
- **Backend:** Java 25 JDK and Maven 3.9+.
- **API contracts and frontend:** Node.js with npm. The current project scripts
  require npm to install and run local tooling. Use the Node.js version already
  available in the Windows environment when the documented npm scripts pass;
  otherwise prefer the active Node.js LTS line.
- **Database schema:** Atlas CLI 1.2+ for schema inspection, validation, and
  migration generation.
- **Database execution environments:** PostgreSQL 17 compatibility. A
  Docker-compatible runtime exposed through the `docker` CLI will be required
  for disposable Atlas dev databases and full migration-apply validation, but
  the local multi-environment strategy is still intentionally deferred.

Area-specific setup and validation commands are documented in each area's
`README.md` and `AGENTS.md`.

## Codex and agent tooling

These tools support local Codex workflows. They are not application runtime or
build requirements for Family 360 itself:

- **GitHub CLI (`gh`):** used by Codex and contributors to authenticate local
  Git operations, push branches, inspect checks, and create pull requests.
  Configure it with `gh auth login` and `gh auth setup-git`.
- **Python with PyYAML:** used for repository skill maintenance, such as
  running Codex skill validation scripts. The application does not use Python.
- **Codex repository skills:** stored under `.agents/skills/` and documented in
  `.agents/skills/README.md`.
