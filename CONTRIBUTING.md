# Contributing to CryptoTrackerBack

Thanks for contributing.
This guide is based on the current codebase structure and the repository commit/workflow history.

## Development Workflow

- Main integration branch is `develop`.
- Open Pull Requests targeting `develop`.
- Do not push directly to `develop` or `master` unless you are maintaining release automation.
- Create feature/fix branches from `develop`.

Recommended branch naming:

- `<issue>-<short-description>` (example: `14-fix-api`)
- or descriptive names for bootstrap work (example: `init-config`)

## Prerequisites

- Java 25
- Docker + Docker Compose
- Maven (or `./mvnw`)

For local infrastructure and secrets (PostgreSQL + Vault + pgAdmin), follow `README.md`.

## Project Conventions

### Package organization

Use package-by-feature.

- Feature code goes in `src/main/java/stream/arepresas/cryptotracker/features/...`
- External integrations go in `src/main/java/stream/arepresas/cryptotracker/external/...`
- Shared config goes in `src/main/java/stream/arepresas/cryptotracker/config/...`

### API and contracts

- Keep endpoint constants in `CryptoApiEndpoints`.
- Keep DTOs/mappers aligned when updating entity fields.
- Preserve pagination/sorting behavior in criteria classes extending `PaginationCriteria`.

### Database migrations

- Use Liquibase for every schema change.
- Add a new `changeSet` under `src/main/resources/db/` (currently centralized in `init-db.xml`).
- Never rewrite an already-applied `changeSet`; append a new one instead.

## Commit Message Convention

Repository history follows a Conventional Commit style, sometimes prefixed by issue id.

Pattern:

- `<type>(<scope>): <short description>`
- Optional issue prefix: `<issue>-<type>(<scope>): <short description>`

Examples from history:

- `feat(API): Add API to local data`
- `14-fix(API): Fix Lazy fetch and @Data problems in API`
- `chore(Java): Fix dev environment and change java to v18`

Recommended types:

- `feat`, `fix`, `chore`, `refactor`, `test`, `docs`, `ci`

We use a semantic commit type list for commits. Please pick one of the types above for every commit.
If you believe a new type is needed, propose it in the Pull Request description before using it.

## Before Opening a Pull Request

Run locally:

```bash
./mvnw clean test
./mvnw clean package
```

If you have Sonar credentials configured, also validate:

```bash
./mvnw clean verify -Psonar
```

Also verify:

- New behavior is covered by tests where practical.
- API/documentation updates are reflected in `README.md` when relevant.
- Liquibase migration is included for database changes.
- No secrets are committed (`.env`, `.env.local`, tokens, API keys).

## Pull Request Guidelines

- Keep PRs focused and small enough to review.
- Explain why the change is needed, not only what changed.
- Include manual validation steps (curl examples, local scenario, etc.) for API changes.
- Link the related issue/ticket when available.

Suggested PR checklist:

- [ ] Builds successfully
- [ ] Tests pass
- [ ] Migration included (if schema changed)
- [ ] Docs updated (if behavior changed)
- [ ] Commit messages follow convention

## CI/CD Notes

Current GitHub Actions behavior:

- PRs trigger the build workflow.
- Non-`develop`/`master` branch pushes trigger build + branch Docker image flow.
- `develop` pushes trigger build, artifact deploy, and Docker `latest` flow.
- Release process is handled through dedicated workflows (`start-release.yml`, `release.yml`).

If your PR fails CI, fix the root cause and re-push.

## Security and Secrets

- Never commit credentials, tokens, or `.env` files.
- Local secrets are loaded through Vault in dev mode.
- Use environment variables for sensitive values.

## Questions

If anything here is unclear, open a draft PR and describe your assumptions.
Maintainers can confirm direction early and avoid rework.
