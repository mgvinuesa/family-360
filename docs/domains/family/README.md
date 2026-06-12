# Family

The `family` domain is the main functional root of Family 360. It covers the
family organizational unit and its direct relationships, including platform
users with family access and family members who may not have a user account.

## Current scope

- Family creation and lifecycle.
- Family access through `FamilyUser`.
- Family members represented by `FamilyMember`.
- Family-scoped authorization as a separate capability when requested.

This document maps the domain and its possible capabilities. Items listed here
do not automatically enter the scope of every family task. For example, family
CRUD does not implicitly include authentication, roles, or authorization.

Households, expenses, contracts, and financial accounts relate to a family but
may evolve as independent functional domains and reactor modules.

## Related artifacts

- API contract: `apis/family/openapi.yaml`
- Backend service: `backend/ms-family-360/`
- Backend functional module: `backend/ms-family-360/family/`
- Frontend feature: planned at `frontend/src/app/features/family/`

## Backend composition

`ms-family-360` is the deployable service boundary and Maven reactor. The
`family` module represents this functional domain. The modules
`family-360-application` and `family-360-persistence` provide application
assembly and shared persistence infrastructure; they are not domain modules.
