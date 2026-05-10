# Component Planning Workflow

## Goal

Split changes into small, verifiable, low-risk units.

## Steps

1. **Scope**
   - Define which components are touched (API, service, DB, external integration, docs).
2. **Impact**
   - Identify impacted contracts (DTOs, endpoints, search criteria, migrations).
3. **Order**
   - Apply this suggested order:
     1) model/DB,
     2) domain/service,
     3) API,
     4) tests,
     5) documentation.
4. **Validation per step**
   - Run quick checks after each significant block.
5. **Final validation**
   - `./mvnw clean test && ./mvnw clean package`

## Definition of Done

- Local build and tests pass.
- CI does not require additional hotfixes.
- Documentation is updated if visible behavior changes.
- No secrets or local artifacts in commits.
