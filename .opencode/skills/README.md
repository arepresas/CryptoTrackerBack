# Local Skills Inventory

This directory documents useful skills for agents in this repository.

## Recommended skills

### 1) context7

- Purpose: retrieve up-to-date external documentation for libraries/frameworks.
- Use it when there are questions about API usage, configuration, or version compatibility.
- Local copy included in `.opencode/skills/context7/`:
  - `SKILL.md`
  - `README.md`
  - `navigation.md`
  - `library-registry.md`

### 2) task-management

- Purpose: break down and track subtasks in `.tmp/tasks/`.
- Useful for complex multi-step or multi-component changes.
- Local copy included in `.opencode/skills/task-management/`:
  - `SKILL.md`
  - `router.sh`
  - `scripts/task-cli.ts`

## Third-party curated skills

Imported curated subset:

- `.opencode/skills/third-party/mattpocock/`
- `.opencode/skills/third-party/jeffallan/`
- `.opencode/skills/third-party/alirezarezvani/`

Included skills:

### mattpocock

- `engineering/diagnose`
- `engineering/tdd`
- `engineering/improve-codebase-architecture`
- `engineering/grill-with-docs`
- `engineering/to-issues`

### jeffallan

- `spring-boot-engineer`
- `api-designer`
- `security-reviewer`
- `sre-engineer`
- `test-master`

### alirezarezvani

- `dependency-auditor`

Attribution and licensing:

- See `.opencode/skills/third-party/mattpocock/UPSTREAM.md`
- See `.opencode/skills/third-party/mattpocock/LICENSE`
- See `.opencode/skills/third-party/jeffallan/UPSTREAM.md`
- See `.opencode/skills/third-party/jeffallan/LICENSE`
- See `.opencode/skills/third-party/alirezarezvani/UPSTREAM.md`
- See `.opencode/skills/third-party/alirezarezvani/LICENSE`

## Note

Skills can be installed globally in the agent environment,
but this local inventory and its versioned copies allow teams to work in this
repository with traceability and PR reviewability.
