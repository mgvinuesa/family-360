# Documentation

Documentation is organized by functional domain under `docs/domains/`.

Each domain folder is the entry point for understanding its business behavior,
technical decisions, API contract, backend modules, and frontend features.
Domain folders start with a small `README.md` and should only introduce
additional subfolders when the amount of documentation requires them.

## Navigation

- [Domain index](domains/README.md)
- [Family domain](domains/family/README.md)

## Convention

Use the same singular kebab-case identifier for a domain across the repository:

```text
docs/domains/<domain>/
apis/<domain>/
backend/<service-id>/<domain>/
frontend/src/app/features/<domain>/
```

These paths express a convention, not a requirement to create empty folders.
The domain `README.md` records which artifacts currently exist.
