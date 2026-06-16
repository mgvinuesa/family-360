# APIs

OpenAPI contracts and shared schemas.

See `apis/AGENTS.md` before modifying contracts.

## Tooling

API contracts are validated with Redocly CLI.

Redocly CLI provides OpenAPI linting, validation, bundling, and API governance checks. The project uses it as a local development dependency in this folder.

Official references:

- Redocly CLI docs: https://redocly.com/docs/cli
- Installation guide: https://redocly.com/docs/cli/installation
- Lint command: https://redocly.com/docs/cli/commands/lint
- Bundle command: https://redocly.com/docs/cli/commands/bundle

## Requirements

Install Node.js with npm. The API workspace uses npm only to install and run
local tooling such as Redocly CLI.

The project supports Node.js versions accepted by Redocly CLI v2:

- Node.js 22.12.0 or newer
- Node.js 20.19.0 or newer

Use the Node.js version already available in the local environment when
`npm install` and `npm run api:check` pass. If a local version causes tooling
or package-engine errors, prefer the active Node.js LTS line.

Recommended installation:

- Windows/macOS/Linux: https://nodejs.org/
- Windows guide: https://learn.microsoft.com/en-us/windows/dev-environment/javascript/nodejs-on-windows
- Windows package manager alternative: `winget install OpenJS.NodeJS.LTS`
- macOS package manager alternative: `brew install node`

## Installation

From this folder:

```bash
npm install
```

This installs `@redocly/cli` locally for the API workspace.

Verify the installation:

```bash
npx redocly --version
```

## Local Workflow

Run API linting and validation:

```bash
npm run api:lint
```

Bundle the Family API into a single generated file:

```bash
npm run api:bundle
```

Run the full local API check:

```bash
npm run api:check
```

Generated bundle output is written to `apis/dist/` and is intentionally ignored by Git.

## Current APIs

```text
family/openapi.yaml
```

## Commit Rule

Before committing API changes, run:

```bash
npm run api:check
```

If the command cannot be run because the local tooling is unavailable, document the reason in the final response.
