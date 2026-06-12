# Repository Skills

This folder contains Codex skills specific to Family 360. Skills complement
the mandatory repository rules in `AGENTS.md`: they describe reusable
procedures for tasks that require a consistent sequence of decisions and
validations.

## Available skills

### `api-change-workflow`

Use this skill for requirements that may affect an OpenAPI contract, generated
backend interfaces or models, backend API implementation, API versioning, or
future generated frontend clients.

It guides the work through:

- API impact and compatibility analysis.
- Contract-first OpenAPI changes.
- Backend regeneration and compilation.
- Backend adapters, mapping, authorization, errors, and tests.
- Green baby-step commits.
- Optional frontend integration.
- Validation, push, and pull request creation.

## How to use skills

Codex may activate a skill automatically when its description matches the
request. To request one explicitly, mention its name with a `$` prefix:

```text
Use $api-change-workflow to add an endpoint for creating a domain resource.
```

The skill does not replace `AGENTS.md`. Codex must follow both, with
repository-wide and area-specific `AGENTS.md` rules taking precedence when
they impose additional constraints.

## Adding a skill

Add a repository skill only when the workflow is recurring, specialized, and
substantial enough that `AGENTS.md` alone would become procedural or verbose.

Each skill must:

- Live at `.codex/skills/<skill-name>/`.
- Contain a valid `SKILL.md` with clear trigger metadata.
- Keep instructions concise and repository-specific.
- Add scripts or references only when they provide reusable value.
- Pass the `skill-creator` validation before publication.

Update this README whenever a repository skill is added, renamed, or removed.
