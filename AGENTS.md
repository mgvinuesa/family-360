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
├── backend/
│   └── AGENTS.md
├── apis/
│   └── AGENTS.md
├── frontend/
│   └── AGENTS.md
└── .codex/
    └── skills/
```

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

Because this is a monorepo, product tags should version the whole repository state, not individual artifacts.

Use date-based product tags:

- Normal release: `vYYYY.MM.DD`
- Multiple releases on the same day: `vYYYY.MM.DD.N`

Examples:

- `v2026.06.05`
- `v2026.06.05.2`

This keeps tags readable, artifact-neutral, and aligned with trunk-based releases. Avoid backend-only or frontend-only tags unless the repository is intentionally split in the future.

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

Family data access must always be scoped by family membership.

A user should only access a family if there is a valid `FamilyUser` relation.


## Definition of done

A task is complete when:

- The relevant API contract is updated if needed.
- Backend implementation is complete when required.
- Frontend implementation is complete when required.
- Tests are added or updated when behavior changes.
- Relevant build/test commands pass, or the reason they could not be run is clearly stated.
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
