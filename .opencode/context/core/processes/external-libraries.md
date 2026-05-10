# External Libraries Workflow

## When to Use

Apply this workflow when a change depends on external libraries/frameworks or upgrades.

## Workflow

1. Check internal installation/setup scripts first, if they exist.
2. Consult up-to-date library documentation (e.g., `context7` skill / `ExternalScout`).
3. Verify compatibility with:
   - Java 25
   - Spring Boot 4
   - `pom.xml` dependencies
4. Apply the minimum viable change.
5. Validate build, tests, and Sonar (if applicable).

## Required Output in PR

- Motivation for the library change.
- Compatibility risks and mitigations.
- Link(s) to consulted documentation/versions.
