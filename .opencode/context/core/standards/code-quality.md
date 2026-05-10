# Code Quality Standards (CryptoTrackerBack)

## Purpose

Minimum standards to keep technical and process consistency in this repository.

## Language & Runtime

- Java 25
- Spring Boot 4
- Maven (`./mvnw` preferred)

## Architecture

- Respect the **package-by-feature** structure.
- Do not mix responsibilities across `features`, `external`, and `config`.
- Keep clear separation between controller / service / repository / mapper / DTO.

## API & Contracts

- Keep endpoint constants centralized (`CryptoApiEndpoints` when applicable).
- If an entity/field changes, update DTOs and mappers in the same change.
- Preserve pagination and sorting semantics in existing criteria classes.

## Database

- Every schema change requires a new Liquibase `changeSet`.
- Never edit an already applied `changeSet` in a destructive way.

## Testing & Validation

- Minimum validation before opening a PR:
  - `./mvnw clean test`
  - `./mvnw clean package`
- If Sonar is available:
  - `./mvnw clean verify -Psonar`

## CI Alignment

- CI uses JDK 25 and Maven build.
- Avoid changes that break workflows in `.github/workflows/`.

## Security

- Never include secrets, tokens, `.env`, or credentials.
- Use environment variables/Vault for sensitive values.

## Commits

- Convention: `feat|fix|chore|refactor|test|docs|ci`.
- Use short, clear commit messages focused on the reason for the change.
