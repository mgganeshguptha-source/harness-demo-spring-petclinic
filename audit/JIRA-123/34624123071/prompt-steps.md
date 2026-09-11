# Plan for: Add a method to check whether an Owner has any pets

**Source:** owner-has-pets-context-260911-164916.md + .github/copilot-instructions.md (absent in this repo; generic Spring Boot defaults apply)
**Stack:** backend
**Total steps:** 6
**Unresolved clarifications:** None

---

## Acceptance criteria coverage

| AC | Criterion (abbreviated) | Covered by |
|---|---|---|
| AC-1 | hasPets() returns true when pets exist | Step 2, Step 3 |
| AC-2 | hasPets() returns false when no pets exist | Step 2, Step 3 |
| AC-3 | hasPets() does not mutate the pets collection | Step 2, Step 3 |
| AC-4 | after the first pet is added, next hasPets() call returns true | Step 2, Step 3 |

---

## Before you execute any step

1. Keep owner-has-pets-context-260911-164916.md in your Copilot Chat context throughout the plan. Re-attach it after any session restart.
2. This repo currently has no .github/copilot-instructions.md. Use the repo's existing Spring Boot and JUnit patterns from the touched files unless that instructions file is added before execution.
3. Execute steps in one Copilot Chat session when possible. If you restart, paste this full plan back into the new chat alongside owner-has-pets-context-260911-164916.md.
4. If a step asks Copilot to modify a file, confirm Copilot is reading the file's current contents rather than guessing. If the response does not reference real methods such as `getPets()` and `addPet(Pet)`, ask Copilot to read the file first.
5. After Step 1, record the confirmed file set in the Impacted Files block. Later steps refer to files by ID only.

---

## Pre-flight

The plan assumes:

1. **Stack assumption:** this is a backend-only Spring Boot domain-model change in the owner package; no UI, controller, endpoint, repository, or persistence-schema work is required.
2. **Behaviour preservation:** the existing pets collection remains the source of truth; current `addPet(Pet)`, `getPets()`, `getPet(...)`, owner-detail rendering, and other owner/pet flows stay unchanged.
3. **Non-functional handling:** there are no new load, accessibility, or security requirements. Performance is handled by keeping `hasPets()` as an in-memory boolean check on the existing collection, with no new field, query, or persistence logic.
4. **Assumed criteria:** no assumed criteria.
5. **Repo-convention assumption:** because .github/copilot-instructions.md is absent, execution should follow the current owner-package conventions visible in the existing domain class and its JUnit 5 tests.

If any of these assumptions are wrong, revise the context file or this plan before implementation starts.

---

## Impacted Files

| ID | Path | Role |
|----|------|------|
| F1 | src/main/java/org/springframework/samples/petclinic/owner/Owner.java | Owner domain entity that exposes the pets collection and will gain the convenience method |
| F2 | src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java | Existing owner-domain test class that should cover the new convenience method |

> Later steps refer to files by ID only. If Step 1 discovers a genuinely required file, add a new ID without renumbering F1 or F2.

---

## Step 1 — Confirm the minimal impacted file set

**Goal:** Verify that the story truly stays local to the owner domain model and its existing unit test coverage, and check whether any non-code file is genuinely required.
**Implements:** — (enabling step, no AC)
**Depends on:** —

**Suggested prompt:**

> Using owner-has-pets-context-260911-164916.md and the repo state as it exists now, inspect these seed candidates only:
>
> - src/main/java/org/springframework/samples/petclinic/owner/Owner.java
> - src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java
>
> Confirm the minimal impacted file set for this story. Add any genuinely required files, including non-code files such as config or build files, and remove any seed candidate that does not need to change. For each confirmed file, give the path and a one-line role. Do not propose edits yet.

**Review checkpoint:** Confirm the file set stays minimal. For this story, anything beyond the owner entity and its owner-focused test should need a clear reason; if Copilot tries to pull in controllers, templates, or repository code, push back unless it shows a real dependency.

---

## Step 2 — Add the `hasPets()` convenience method

**Goal:** Add a small, non-mutating convenience method to F1 that reports whether the existing pets collection currently contains any entries.
**Implements:** AC-1, AC-2, AC-3, AC-4
**Depends on:** Step 1

**Suggested prompt:**

> Using owner-has-pets-context-260911-164916.md, edit F1 only to add the requested convenience method. Keep the existing pets collection as the source of truth, and do not change how pets are stored, added, ordered, or retrieved. Do not modify tests yet. Show the exact diff.
>
> Intended signature and behaviour:
>
> ```java
> public boolean hasPets()
> ```
>
> Expected implementation shape:
>
> ```java
> public boolean hasPets() {
> 	return !getPets().isEmpty();
> }
> ```
>
> If you recommend a different but equivalent implementation, explain why before changing it.

**Review checkpoint:** Confirm only F1 changed, the new method is named `hasPets()`, returns a boolean derived from the current collection state, and has no side effects on the collection or other owner behaviour.

---

## Step 3 — Extend owner-domain tests for the new method

**Goal:** Update F2 so the owner-domain test suite covers the empty, populated, and first-pet-added behaviours, plus the non-mutating contract.
**Implements:** AC-1, AC-2, AC-3, AC-4
**Depends on:** Step 2

**Suggested prompt:**

> Using owner-has-pets-context-260911-164916.md, edit F2 only to add focused JUnit 5 tests for F1's new `hasPets()` method. Match the existing test style in F2. Cover:
>
> 1. new owner with no pets returns false
> 2. owner with one pet returns true
> 3. owner returns true after `addPet(...)` adds the first pet
> 4. calling `hasPets()` does not add, remove, or modify pets
>
> Reuse synthetic in-memory pets only. Do not modify F1 again unless the tests expose a direct issue with the new method. Show the exact diff.

**Review checkpoint:** Confirm only F2 changed, the new tests are deterministic, and they prove both result correctness and the no-mutation requirement instead of only checking that a method exists.

---

## Step 4 — Run the focused owner-domain tests

**Goal:** Execute the smallest existing test command that proves the owner-domain change behaves as intended before broader review.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 3

**Suggested prompt:**

> Using owner-has-pets-context-260911-164916.md, run the existing focused test command for the owner-domain change:
>
> ```bash
> ./mvnw -q -Dtest=OwnerTests test
> ```
>
> Summarize the result briefly. If it fails, show the failure and fix only the files already listed in the Impacted Files block unless the failure proves another file is genuinely required.

**Review checkpoint:** If the focused test fails, loop back to Step 2 or Step 3 based on the failure source instead of widening scope. If it passes, keep the change set constrained to the confirmed impacted files.

---

## Step 5 — Manually validate each acceptance criterion

**Goal:** Walk every acceptance criterion from owner-has-pets-context-260911-164916.md and record a verdict tied to the changed code and focused test results.
**Implements:** — (verification step, no AC)
**Depends on:** Step 4

**Suggested prompt:**

> Using owner-has-pets-context-260911-164916.md, produce a manual verification checklist with one row per acceptance criterion. For each AC, state the exact evidence to inspect in F1, F2, and the `OwnerTests` run output. Do not edit code. Do not run new commands.

**Review checkpoint:** Record pass, fail, or unclear for AC-1 through AC-4 individually. If any AC is unclear, treat that as incomplete and return to the earlier step that should have made the behaviour obvious.

---

## Step 6 — Review for convention drift before handoff

**Goal:** Check the final diff for drift against the repo's existing owner-package conventions and the constraints in owner-has-pets-context-260911-164916.md.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 5

**Suggested prompt:**

> Using owner-has-pets-context-260911-164916.md, review the final diff in F1 and F2 for convention drift. Check naming, method placement, test style, scope control, and the explicit out-of-scope rule that no controller, template, endpoint, or pet-storage behaviour changes are allowed. List any drift you find, but do not auto-fix it.

**Review checkpoint:** Confirm the final change is still a narrow owner-entity enhancement. If drift appears in naming, scope, or side effects, return to the step that introduced it instead of accepting a broader solution.

---

## Done criteria

Before handing the implementation over for PR preparation, confirm:

- F1 contains `public boolean hasPets()` and it reads the existing pets collection only.
- The empty-owner case returns `false`, and populated-owner cases return `true`.
- Adding the first pet through existing behaviour makes the next `hasPets()` call return `true`.
- No controller, endpoint, template, repository, persistence-schema, or pet-storage change was introduced.
- F2 covers the new method's empty, populated, first-add, and non-mutating behaviours.
- The focused `OwnerTests` command result is recorded.
- Step 5 has a verdict for AC-1 through AC-4.
- Step 6 found no unresolved convention drift.

---

## --- EXECUTION RECORD (appended by harness) ---
- timestamp: 2026-09-11T16:51:35
- phase: coding
- approved impacted files: ['src/main/java/org/springframework/samples/petclinic/owner/Owner.java', 'src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java']
- actually touched: ['src/main/java/org/springframework/samples/petclinic/owner/Owner.java']
- scope: matches approved plan (no additions)
- review status: APPROVED by human at 2026-09-11T16:51:35
