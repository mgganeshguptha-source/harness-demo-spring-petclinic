# Plan for: Add a method to check whether an Owner has any pets

**Source:** owner-has-pets-context-260911-160821.md + .github/copilot-instructions.md (not present; use generic backend defaults)
**Stack:** backend
**Total steps:** 6
**Unresolved clarifications:** None

---

## Acceptance criteria coverage

| AC | Criterion (abbreviated) | Covered by |
|---|---|---|
| AC-1 | Returns true when owner has pets | Step 2, Step 3 |
| AC-2 | Returns false when owner has no pets | Step 2, Step 3 |
| AC-3 | Reads collection without modifying pets | Step 2, Step 3 |
| AC-4 | Reflects updated state after addPet | Step 2, Step 3 |
| AC-5 | Does not change existing owner/pet flows | Step 3 |

---

## Before you execute any step

1. Keep owner-has-pets-context-260911-160821.md in your Copilot Chat context throughout the plan. Re-attach it after any session restart.
2. `.github/copilot-instructions.md` is not present in this repo, so use the repository's existing Java 17 / Spring Boot patterns from nearby owner-domain files instead of team-specific instruction overrides.
3. Execute the steps in one Copilot Chat session when possible. If you restart, paste this full plan back into the new chat alongside owner-has-pets-context-260911-160821.md.
4. If a step asks Copilot to modify a file, confirm Copilot is reading the file's current contents rather than guessing. If the response does not reference real methods from the file, ask Copilot to read the file first.
5. After Step 1, confirm the file set in the Impacted Files block below before any edit prompt is executed.

---

## Pre-flight

The plan assumes:

1. **Stack assumption:** This is a Java 17 Spring Boot backend domain-model change inside the owner package. No controller, repository, WebClient, schema, UI, or reactive-flow work is required.
2. **Behaviour preservation:** Existing `getPets()`, `addPet(Pet)`, `getPet(...)`, `addVisit(...)`, and downstream owner-view/list behaviour are preserved exactly as they work today; the story adds only a read-only convenience check.
3. **Non-functional handling:** There is no separate performance or accessibility target for this story. The non-functional constraint is preserving the current collection access pattern and avoiding any change to persistence mapping or pet storage semantics.
4. **Assumed criteria:** No assumed criteria.

If any of these assumptions are wrong, stop and revise the context file or the plan before proceeding.

---

## Impacted Files

| ID | Path | Role |
|----|------|------|
| F1 | src/main/java/org/springframework/samples/petclinic/owner/Owner.java | Owner domain entity where the new convenience method will be added |
| F2 | src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java | Existing owner-domain unit tests to extend for the new helper and regression coverage |

> Later steps refer to files by ID (for example, "edit F1"), never by re-listing paths.

---

## Step 1 — Confirm the exact file set

**Goal:** Verify that the story really touches only the owner entity and its direct unit test, and explicitly rule out any hidden source, config, or non-code files.
**Implements:** — (enabling step, no AC)
**Depends on:** —

**Suggested prompt:**

> Using owner-has-pets-context-260911-160821.md and the repository's existing Java/Spring patterns, inspect this seed file set only:
>
> - `src/main/java/org/springframework/samples/petclinic/owner/Owner.java`
> - `src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java`
>
> Confirm the affected files for this story. Add any genuinely required files, including non-code files such as config or build files, only if the change cannot be completed without them. Remove any file that is not actually impacted. Do not propose edits yet; return only `path | one-line role | why it is needed`.

**Review checkpoint:** Confirm the final list stays limited to the owner entity and its direct unit test. If Copilot adds controller, repository, UI, schema, or build files, require a concrete reason tied to the story before accepting the addition.

---

## Step 2 — Add the read-only convenience method on the owner entity

**Goal:** Add `hasPets()` to the owner domain entity as a boolean read of the existing pets collection, without changing storage or mutation behaviour.
**Implements:** AC-1, AC-2, AC-3, AC-4
**Depends on:** Step 1

**Suggested prompt:**

> Using owner-has-pets-context-260911-160821.md, edit F1 only.
>
> Add a `hasPets()` convenience method to the `Owner` entity that returns `true` when the existing pets collection contains one or more pets and `false` when it contains none. The method must only read the current collection state; it must not add, remove, reorder, or otherwise modify pets. Keep the existing persistence annotations, `getPets()`, `addPet(...)`, `getPet(...)`, and `addVisit(...)` behaviour unchanged. Do not modify F2 yet.

**Intended production change:**

```java
public boolean hasPets() {
	return !getPets().isEmpty();
}
```

**Review checkpoint:** Confirm only F1 changed; the new method is side-effect free; and no existing owner or pet methods, mappings, or collection semantics were altered.

---

## Step 3 — Extend owner tests for the new helper and regression coverage

**Goal:** Cover the new helper's true/false outcomes and verify it reflects `addPet(...)` without changing existing owner behaviour.
**Implements:** AC-1, AC-2, AC-3, AC-4, AC-5
**Depends on:** Step 2

**Suggested prompt:**

> Using owner-has-pets-context-260911-160821.md, edit F2 only.
>
> Follow F2's existing JUnit 5 style and extend the tests to cover:
> 1. `hasPets()` returns `false` for a new owner with no pets.
> 2. `hasPets()` returns `true` when the owner already has one pet.
> 3. `hasPets()` returns `true` after a pet is added through the existing `addPet(...)` path.
> 4. Calling `hasPets()` does not change the collection contents or size.
>
> Preserve the existing add-pet tests unless a small adjustment is needed to keep the suite clear and non-duplicative. Do not add controller, repository, or UI tests. Do not modify production files in this step.

**Suggested test shape:**

```java
void hasPetsReturnsFalseWhenOwnerHasNoPets() { ... }

void hasPetsReturnsTrueWhenOwnerAlreadyHasPets() { ... }

void hasPetsReturnsTrueAfterAddPet() { ... }

void hasPetsDoesNotModifyPetCollection() { ... }
```

**Review checkpoint:** Confirm the tests assert behaviour, not implementation details; all new coverage stays in F2; and the regression checks explicitly protect unchanged add/list/lookup behaviour expectations from the story.

---

## Step 4 — Run the narrow owner-domain test slice

**Goal:** Execute the smallest existing test command that exercises the owner-domain test file and fix only issues directly caused by this story.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 3

**Suggested prompt:**

> Run the narrowest existing test command that executes F2 only, using the repo's current build tool and wrapper. If the new or existing owner-domain tests fail, fix only F1 or F2 as needed for this story. Do not widen scope to unrelated files, and do not rewrite existing owner behaviour just to silence a failing assertion.

**Review checkpoint:** Confirm the run stays scoped to F2, any fix remains limited to F1/F2, and no unrelated failures are folded into this story.

---

## Step 5 — Manual validation against acceptance criteria

**Goal:** Walk every acceptance criterion from the context file against the changed owner-domain code and test outcomes.
**Implements:** — (verification step, no AC)
**Depends on:** Step 4

**Suggested prompt:**

> Using owner-has-pets-context-260911-160821.md, produce a verification checklist with one row per acceptance criterion (AC-1 through AC-5). For each row, give the exact code-level or test-level evidence to inspect, the expected result, and a pass/fail/unclear column. Include unchanged-behaviour checks for existing owner and pet flows. Do not edit code in this step.

**Review checkpoint:** Record a verdict for every AC individually. If any AC is unclear or fails, return to the earliest step that caused the gap instead of patching at the end.

---

## Step 6 — Review for convention drift before handoff

**Goal:** Review the changed files for drift from existing repository conventions and the story's narrow scope before handing the change off.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 5

**Suggested prompt:**

> Review only F1 and F2 against owner-has-pets-context-260911-160821.md and the existing conventions already present in the owner package. Because `.github/copilot-instructions.md` is not present, compare against nearby repository patterns instead of inventing new ones. Flag any drift in naming, method placement, test style, or scope. Do not auto-fix; list the issues for review.

**Review checkpoint:** Confirm the final diff stays limited to F1/F2, the new API is exactly `hasPets()`, and no scope drift introduced extra helpers, refactors, or unrelated test rewrites.

---

## Done criteria

Before opening a PR, confirm:

- `Owner` exposes `hasPets()` as a boolean convenience method that reads the current pets collection only.
- Empty-owner and populated-owner states are both covered, including the `addPet(...)` path updating the observed result.
- No persistence mapping, pet storage, pet ordering, owner view flow, controller behaviour, or repository behaviour changed.
- The code diff is limited to F1 and F2 unless Step 1 found a genuinely required additional file and documented why.
- The validation checklist records pass/fail/unclear for AC-1 through AC-5 individually.

---

## --- EXECUTION RECORD (appended by harness) ---
- timestamp: 2026-09-11T16:10:50
- phase: coding
- approved impacted files: ['src/main/java/org/springframework/samples/petclinic/owner/Owner.java', 'src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java']
- actually touched: ['src/main/java/org/springframework/samples/petclinic/owner/Owner.java']
- scope: matches approved plan (no additions)
- review status: APPROVED by human at 2026-09-11T16:10:50
