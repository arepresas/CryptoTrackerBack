# agents.md

Operational guide for agents (human and AI) in **CryptoTrackerBack**.

## 1) Objective

Keep changes consistent with this repository, prioritizing:

- code quality,
- security (no secrets),
- small, reviewable changes,
- CI compliance.

## 2) Mandatory minimum context (MCD)

Before implementing changes, load at least:

- `.opencode/context/core/standards/code-quality.md`
- `.opencode/context/core/processes/component-planning.md`

If external libraries are involved, also load:

- `.opencode/context/core/processes/external-libraries.md`

## 3) Project source of truth

Files that define standards and process:

- `CONTRIBUTING.md`
- `README.md`
- `.github/workflows/build.yml`
- `.github/workflows/pull-request.yml`
- `pom.xml`
- `.deepsource.toml`

## 4) Key conventions

- PR target branch: `develop`
- No direct pushes to `develop` or `master`
- Conventional-style commits: `feat|fix|chore|refactor|test|docs|ci`
- Java 25 + Maven Wrapper (`./mvnw`)
- Package-by-feature architecture
- Schema changes: **only** via Liquibase (new `changeSet`, never rewrite an applied one)
- Never commit secrets (`.env`, tokens, keys)

## 5) Recommended workflow for agents

1. Discover context and relevant files.
2. Propose a short approach and request approval.
3. Implement in small steps.
4. Validate each step.
5. Run final validation.
6. Summarize changes and risks.

## 6) Minimum validation

Run locally when applicable:

```bash
./mvnw clean test
./mvnw clean package
```

Optional validation if Sonar credentials are available:

```bash
./mvnw clean verify -Psonar
```

## 7) Available skills for support

Local inventory in `.opencode/skills/`:

- `context7` (up-to-date external documentation)
- `task-management` (subtask planning/status tracking)

> Note: this repository includes skill usage documentation. Execution may depend on the agent environment's global installation.
