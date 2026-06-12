# frontend/AGENTS.md

## Scope

This folder contains the frontend application.

Do not modify `backend/` from frontend tasks unless explicitly requested.

If frontend work reveals missing API capabilities, report the required API change instead of inventing client-side workarounds.

## Preferred stack

- Angular
- NX
- TypeScript
- OpenAPI-generated API clients
- Component-based architecture

## Recommended structure

Prefer feature-based organization.

```text
frontend/src/app/
├── core/
├── shared/
├── features/
│   ├── families/
│   ├── households/
│   ├── expenses/
│   ├── contracts/
│   └── analytics/
└── api/
```

## API clients

- Generate API clients from OpenAPI.
- Do not hand-write duplicated HTTP clients when generated clients exist.
- Keep API models aligned with the contract.
- Do not manually edit generated API code.
- Keep mapping/adaptation code outside generated files.

Use `evolve-api-contract` when an OpenAPI change requires frontend client
generation or adaptation at the generated-client boundary. Regeneration alone
does not authorize changes to feature behavior or presentation.

If generated changes require new frontend behavior beyond adapting an existing
boundary, return the impact to `implement-domain-capability` for a scope
decision unless frontend behavior is already in its confirmed plan. Do not
introduce handwritten HTTP workarounds.

## UI priorities for MVP

Prioritize:

- Family selector
- Household list
- Member list
- Expense list
- Expense creation/editing
- Contract list
- Monthly expense summary
- Category breakdown

Avoid overengineering dashboards before the core data model works.

## Business logic

Do not place backend business rules in frontend.

Frontend can handle:

- Form validation
- Presentation logic
- Filtering/sorting UI state
- User-friendly formatting

Backend owns these behaviors when they are part of the approved functional
scope:

- Authorization
- Family membership checks
- Expense validation
- Contract consistency
- Cross-entity validation
- Analytics calculations

## Forms

Prefer typed reactive forms.

Validation should mirror API constraints where possible.

Show clear validation messages.

## State management

Start simple.

Do not introduce a global state library unless the application complexity requires it.

Prefer:

- Feature services
- Signals or RxJS depending on project convention
- Route-level loading
- Clear error states

## Testing

Add tests where existing setup supports it.

Prioritize:

- Component behavior
- Form validation
- Service integration with generated clients
- Critical rendering logic

## Suggested commands

```bash
npm install
npm run lint
npm test
npm run build
```

Adjust commands according to the actual frontend setup.

## Frontend checklist

Before finishing a frontend task, check:

- Are generated API clients reused?
- Are generated files untouched?
- Is business logic kept out of components?
- Are loading, empty, and error states handled?
- Are forms typed and validated?
- Does the UI match the current OpenAPI contract?
