# Family database artifact

## Ownership

This artifact owns the PostgreSQL schema named `family`.

It will contain persistence objects for the family bounded area when those
changes are explicitly scoped. It currently defines only the empty desired
schema and has no executable migrations.

## Dependencies

This artifact has no dependencies on other database artifacts.

## Migrations

The first migration must create the `family` schema. Migration history must be
stored in the technical schema `atlas_family_revisions`.
