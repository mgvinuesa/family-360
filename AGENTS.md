# AGENTS.md

## Product overview

This repository contains a family economy management application.

The goal is to help families organize, understand, and optimize their recurring and non-recurring expenses.

The MVP focuses on:

- Families
- Family members
- Households
- Financial accounts
- Expense categories
- Expenses
- Contracts / recurring financial obligations
- Basic analytics

The application should be built in a contract-first way, keeping backend, APIs, and frontend aligned.

## Repository structure

```text
.
├── AGENTS.md
├── docs/
│   └── domains/
├── backend/
│   └── AGENTS.md
├── database/
│   └── AGENTS.md
├── apis/
│   └── AGENTS.md
├── frontend/
│   └── AGENTS.md
└── .agents/
    └── skills/
```

## Repository skills

Repository-scoped Codex skills live under:

```text
.agents/skills/
```

Instruction precedence is:

1. This root file defines product scope, repository-wide constraints, routing,
   and delivery workflow.
2. Area `AGENTS.md` files define architecture and validation for their area.
3. The functional coordinator defines the confirmed scope for the current
   task.
4. Technical skills define how to execute inside that approved scope.

Lower levels must not broaden requirements established by higher levels.

Use `implement-domain-capability` as the mandatory entry point for functional
requests such as CRUD, new business actions, or end-to-end domain behavior.
The coordinator owns scope and sequencing; it does not implement the domain
model itself. It must classify impact per layer, select the smallest set of
technical skills, and request confirmation before adding inferred behavior or
expanding beyond the user's approved scope.

Technical skills may be invoked directly only for clearly bounded technical
work whose product semantics and affected layer are already explicit, such as
correcting an OpenAPI parameter name or refactoring one existing use case.
When a technical skill discovers a new capability or another layer is needed,
return control to `implement-domain-capability` instead of chaining technical
skills directly.

Explicitly naming a technical skill does not waive coordination when the same
request asks for a functional outcome such as CRUD. In that case, use the
named technical skill under an `implement-domain-capability` scope plan.

Technical skills own responsibilities rather than isolated file sets. Each
technical skill must inspect adjacent-layer contracts and classify discovered
impact as local work, boundary adaptation, or a capability change in another
layer.

- A boundary adaptation may be implemented by the skill responsible for that
  boundary when adjacent semantics remain unchanged.
- A capability change in another layer requires that layer's technical skill.
- Before activating another skill, request confirmation unless the functional
  coordinator's confirmed plan already includes that layer.
- Do not create alternative production adapters merely to avoid a required
  layer change.
- Keep unavailable or excluded layer work explicit as deferred or blocked.
- Repository principles and domain documentation constrain implementation but
  do not automatically add product capabilities to the current task.

The currently available technical skills are:

- `evolve-api-contract`
- `implement-application-capability`

Persistence work will receive a dedicated skill later. Until then, do not use
the existing skills to modify Atlas migrations, JPA entities, Spring Data
repositories, or persistent output adapters as an implicit part of API or
application work.

When persistence or another unsupported layer is required for the requested
functional outcome, the coordinator must state that the capability cannot be
completed and ask whether a partial implementation is wanted. Do not proceed
with a contract-only or application-only skeleton by assumption.

## Domain-oriented repository navigation

Functional and technical documentation lives under:

```text
docs/domains/<domain>/
```

Use the same singular kebab-case domain identifier across repository areas
whenever possible. For example, work on the `family` domain should start by
reviewing:

```text
docs/domains/family/
apis/family/
backend/ms-family-360/family/
frontend/src/app/features/family/
```

Backend paths have two explicit levels:

- `backend/<service-id>/` identifies a deployable service and its Maven reactor.
- `backend/<service-id>/<module-id>/` identifies a functional or technical
  reactor module.

A functional domain or sufficiently autonomous subdomain should normally map to
a functional reactor module. Technical modules such as
`family-360-application` and `family-360-persistence` do not represent domains.

Each domain documentation folder must contain a `README.md` that describes the
domain and lists its current API, backend, and frontend paths. Keep this mapping
small and human-readable; do not add duplicated registries or metadata unless
the repository later needs automated validation.

When asked to work on a domain globally, inspect its documented paths first.
Only modify multiple repository areas when the requested scope requires an
integration change.

## Git workflow

Prefer short iterations and a trunk-based development mindset.

Branches should be short-lived and integrated frequently.

Use these branch naming conventions:

- New developments: `feature/XXXXX`
- Fixes: `hotfix/XXXXX`

`XXXXX` should be a short kebab-case title that describes the change.

Examples:

- `feature/family-api-contract`
- `feature/backend-family-crud`
- `hotfix/family-member-validation`

For every task that requires modifying repository files:

1. Check `git status --short --branch` and `git worktree list`.
2. Create or switch to a task-specific `feature/` or `hotfix/` branch before
   the first file edit. Do this without waiting for an explicit user request.
3. Keep commits scoped and intentional. For complex work, create small commits
   after independently meaningful and verified milestones instead of one large
   final commit.
4. Run the relevant validation commands before publishing.
5. When the task is complete and validated, push the branch and open a pull
   request without waiting for an explicit user request.

The automatic branch, push, and pull request workflow does not apply when the
user explicitly asks not to publish changes or when the task is read-only and
does not modify repository files. Pull requests should be created as drafts
unless the user explicitly requests a ready-for-review pull request.

Because this is a monorepo, product tags should version the whole repository state, not individual artifacts.

Use date-based product tags:

- Normal release: `vYYYY.MM.DD`
- Multiple releases on the same day: `vYYYY.MM.DD.N`

Examples:

- `v2026.06.05`
- `v2026.06.05.2`

This keeps tags readable, artifact-neutral, and aligned with trunk-based releases. Avoid backend-only or frontend-only tags unless the repository is intentionally split in the future.

## Local multi-project workflow

This repository is a monorepo, but local work should be isolated when several Codex conversations or contributors work in parallel.

Use the current folder as the global project workspace:

```text
family-360/
```

The global workspace is useful for repository-wide review, integration, documentation, and final coordination.

For parallel implementation work, create separate local projects with `git worktree`. Prefer one worktree per active development area or task:

```text
family-360/            # global / integration workspace
family-360-backend/    # backend-focused workspace
family-360-frontend/   # frontend-focused workspace
family-360-apis/       # API-focused workspace
```

The default split should follow project areas:

- `apis`
- `backend`
- `frontend`

This can vary when a feature needs a narrower or broader isolation boundary.

Recommended worktree commands:

```bash
git fetch
git worktree add ../family-360-apis -b feature/family-api-validation origin/main
git worktree add ../family-360-backend -b feature/backend-family-crud origin/main
git worktree add ../family-360-frontend -b feature/frontend-shell origin/main
```

Remove completed worktrees after integration:

```bash
git worktree remove ../family-360-apis
```

When starting a new Codex conversation, always identify which workspace is being used and what scope it owns.

Before changing files, check:

```bash
git status --short --branch
git worktree list
```

If unrelated changes are present, assume they belong to another workspace, user, or Codex conversation. Do not include them in commits unless explicitly requested.

Commit by scope, not by whatever happens to be in the working tree.

Examples:

- API task: stage only `apis/` and directly related root docs.
- Backend task: stage only `backend/` and directly related root docs.
- Database task: stage only `database/` and directly related root docs.
- Frontend task: stage only `frontend/` and directly related root docs.
- Integration task: may stage multiple areas, but the task must say so explicitly.

Avoid running multiple Codex conversations that write to the same physical folder. Use worktrees for concurrent work to prevent accidental cross-commits.

## Domain principles

### Family is the main functional root

A `Family` is the central organizational unit for the MVP.

A family can have:

- Users with access
- Members
- Households
- Financial accounts
- Expense categories
- Expenses
- Contracts

### A household is not the same as a family

A `Household` represents a physical or economic place where expenses happen.

Examples:

- Main home
- Second residence
- Rented apartment
- Family house
- Temporary residence

Expenses such as electricity, water, internet, community fees, IBI, and home insurance should usually be linked to a household.

### A family member is not necessarily an authenticated user

A `FamilyMember` represents a person in the family domain.

A `UserAccount` represents a real authenticated platform user.

Some members may not have login access.

### Expenses must remain flexible

An expense may be related to:

- A family
- A household
- A family member
- A financial account
- A category
- A contract

Do not force every expense into a single narrow ownership model.

### Contracts represent optimizable recurring obligations

A `Contract` is a recurring or semi-recurring financial obligation that may be optimized.

Examples:

- Electricity
- Water
- Gas
- Internet
- Mobile phone
- Insurance
- Mortgage
- Rent
- Streaming subscription

## MVP entity model

The initial MVP should focus on:

- `UserAccount`
- `Family`
- `FamilyUser`
- `FamilyMember`
- `Household`
- `HouseholdMember`
- `FinancialAccount`
- `ExpenseCategory`
- `Expense`
- `Contract`

Avoid for now:

- PSD2 banking integrations
- Invoice OCR
- AI-based autonomous recommendations
- Real offer marketplace
- Multi-currency accounting engine
- Investment tracking
- Tax reporting
- Native mobile apps

## Contract-first workflow

For API-visible features:

1. Update OpenAPI contract in `apis/`.
2. Regenerate backend and/or frontend code if generation is configured.
3. Implement backend logic.
4. Implement frontend integration if requested.
5. Add or update tests.
6. Run relevant build/test commands.

## Scope control

When working on one area, avoid unrelated changes in other areas.

Before implementing a functional request, distinguish:

- Explicitly requested behavior.
- Technical consequences required to implement that behavior.
- Plausible or documented product capabilities that were not requested.
- Cross-cutting safeguards that do not create new user-visible behavior.

Do not implement the third category without confirmation. In particular,
authentication, authorization, roles, ownership, invitations, audit workflows,
and lifecycle policies are functional capabilities, not implicit technical
details of CRUD.

Examples:

- Backend task: do not modify frontend unless explicitly requested.
- Frontend task: do not modify backend unless the API contract is insufficient.
- API task: do not implement backend/frontend unless explicitly requested.

## Security and privacy

This application handles sensitive financial and family data.

Do not log:

- Full IBANs
- Raw bank movements with sensitive references
- Authentication tokens
- Uploaded documents
- Invoice contents
- Personal identifiers beyond what is necessary

Use masked values where applicable.

Security and privacy safeguards always apply. Avoid introducing data leaks,
unsafe defaults, insecure logging, missing input boundaries, or accidental
cross-family data access in code that is already within scope.

Authentication, authorization, family membership enforcement, roles, and
permission workflows are product capabilities. Implement them only when the
task explicitly requests them or the user confirms them after they are raised
as a scope decision.

When family access control is part of the approved capability, a user should
only access a family if there is a valid `FamilyUser` relation.


## Definition of done

A task is complete when:

- Work was performed on a task-specific branch when repository files changed.
- The relevant API contract is updated if needed.
- Backend implementation is complete when required.
- Frontend implementation is complete when required.
- Tests are added or updated when behavior changes.
- Relevant build/test commands pass, or the reason they could not be run is clearly stated.
- Validated changes are committed, pushed, and exposed through a pull request
  unless the user explicitly requested otherwise.
- The final response summarizes changed files and important decisions.

## Preferred implementation style

Favor boring, maintainable code.

Prefer:

- Explicit domain concepts
- Small use cases
- Clear validation
- Stable API contracts
- Good tests
- Simple UI

Avoid:

- Clever abstractions
- Premature generic frameworks
- Large unreviewable changes
- Hidden side effects
- Business logic spread across frontend and backend
