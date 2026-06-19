# Family database artifact

## Ownership

This artifact owns the PostgreSQL schema named `family`.

It currently contains persistence objects for the implemented family bounded
area:

- `family.family`
- `family.family_member`

## Dependencies

This artifact has no dependencies on other database artifacts.

## Migrations

Migration history must be stored in the technical schema
`atlas_family_revisions`.
