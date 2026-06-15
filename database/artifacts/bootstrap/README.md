# Bootstrap database artifact

## Ownership

This artifact owns only PostgreSQL prerequisites that apply to the complete
database and cannot reasonably belong to one functional schema.

Examples include:

- Required PostgreSQL extensions.
- Technical roles.
- Shared grants.

It must not contain business tables or create functional schemas on their
behalf.

## Migrations

Migrations live in `migrations/` and use an independent Atlas history stored in
`atlas_bootstrap_revisions`.

There are no bootstrap migrations yet.
