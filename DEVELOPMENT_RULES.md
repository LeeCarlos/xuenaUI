# OpenSpec + Superpowers Development Rules

## Paradigm Overview
This project follows the **OpenSpec + Superpowers** development paradigm:
- **SDD (Spec-Driven Development)** via OpenSpec: Align requirements before coding
- **TDD (Test-Driven Development)** via Superpowers: Ensure code quality during implementation

## 1. Change Lifecycle

### Phase 1: Open (打开变更)
- Create proposal using `/opsx:propose` or manually create `openspec/changes/<change-name>/`
- Structure must include:
  - `proposal.md`: Why we're making this change
  - `specs/`: What to implement (capability specs)
  - `design.md`: How to implement (technical design)
  - `tasks.md`: Implementation sequence (breakdown)

### Phase 2: Design (深度设计)
- Brainstorm technical details
- Create deep design doc at `docs/superpowers/specs/YYYY-MM-DD-<topic>-design.md`
- Backfill missing acceptance scenarios to delta specs in `openspec/changes/<change-name>/specs/`

### Phase 3: Build (实现)
- Write implementation plan at `docs/superpowers/plans/YYYY-MM-DD-<feature>.md`
- Create feature branch: `git checkout -b <change-name>`
- Implement task by task with TDD approach
- After each task: Check it in tasks.md ([ ] → [x]), commit with intent-driven message

### Phase 4: Verify (验证)
- Run OpenSpec verification
- Run Superpowers finishing checks (tests, code review)
- Fix any issues before proceeding

### Phase 5: Archive (归档)
- Sync delta specs
- Mark status
- Archive to `openspec/changes/archive/`

## 2. Engineering Principles

### TDD (Test-Driven Development)
- Write tests first, then implement code
- All new functionality must have corresponding tests

### YAGNI (You Ain't Gonna Need It)
- Implement only what's required by the spec
- Avoid speculative features or over-engineering

### DRY (Don't Repeat Yourself)
- Eliminate code duplication
- Extract common logic into reusable components

### Code Quality
- Avoid local optima that break global architecture
- Keep code concise and focused

## 3. Spec Completeness Handling

When spec incompleteness is found during implementation:
- **Missing acceptance scenarios/boundaries**: Edit delta spec + design.md, append tasks
- **Interface changes/new components/data flow changes**: Re-brainstorm to update Design Doc
- **New capability requirements**: Create new OpenSpec change
- **New tasks exceed 50% of initial**: Consider splitting into new change

## 4. Directory Structure

```
openspec/
├── changes/
│   ├── <change-name>/
│   │   ├── proposal.md      # 变更意图
│   │   ├── design.md        # 技术设计
│   │   ├── tasks.md         # 任务清单
│   │   └── specs/           # 能力规范
│   │       └── <capability>.md
│   └── archive/             # 已完成变更归档

docs/
└── superpowers/
    ├── specs/               # 深度设计文档
    └── plans/               # 实现计划
```

## 5. Commit Message Guidelines

Commit messages should follow this format:
- `feat(<module>): <intent>` - New feature
- `fix(<module>): <intent>` - Bug fix
- `chore(<module>): <intent>` - Maintenance
- `refactor(<module>): <intent>` - Code refactoring

## 6. Review Process

All changes require:
1. Automated test pass
2. Two-phase code review
3. Spec verification against requirements

## 7. Hard Constraints (Supplier Performance System)

Refer to `project_memory.md` for project-specific constraints including:
- Database naming conventions (`sp_` prefix, `gmt_create`, `gmt_modified`, `is_deleted`)
- Field naming conventions (`is_xxx` for booleans)
- API parameter conventions (`yearMonth` format)
- Package structure (`com.xuena.supplier`)