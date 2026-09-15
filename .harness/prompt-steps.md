# Plan for: Add a method to check whether an Owner has any pets

**Source:** owner-has-pets-context-260915-105037.md + .github/copilot-instructions.md (not present in repo; generic defaults apply)
**Stack:** backend
**Total steps:** 7
**Unresolved clarifications:** None

---

## Acceptance criteria coverage

| AC | Criterion (abbreviated) | Covered by |
|---|---|---|
| AC-1 | hasPets returns true when pets exist | Step 3, Step 4 |
| AC-2 | hasPets returns false when no pets exist | Step 3, Step 4 |
| AC-3 | reads existing pets collection only | Step 3, Step 4 |
| AC-4 | does not add, remove, or modify pets | Step 3, Step 4 |
| AC-5 | existing owner and pet behaviour remains unchanged | No step required — additive change limited to F1 and F2 only; verify unchanged behaviour in Step 6 |

---

## Before you execute any step

1. Keep owner-has-pets-context-260915-105037.md in your Copilot Chat context throughout the plan. Re-attach it after any session restart.
2. .github/copilot-instructions.md is not present in this repo, so Copilot will follow generic Spring Boot / JUnit defaults plus the local patterns already present in the owner package.
3. Execute steps in one Copilot Chat session when possible. If you restart, paste the full plan back into the new chat alongside owner-has-pets-context-260915-105037.md.
4. If a step asks Copilot to modify a file, confirm Copilot is reading the file's current contents rather than guessing. If the response does not reference real methods already present in the file, ask Copilot to read the file first.
5. After Step 1, record the confirmed file set in the Impacted Files block so later steps can refer to F1 and F2 instead of repeating paths.

---

## Pre-flight

The plan assumes:

1. **Stack assumption:** this is a backend-only Spring Boot domain-model change in the owner package; no controller, repository, template, endpoint, or persistence-mapping change is intended.
2. **Behaviour preservation:** existing `addPet(...)`, direct `getPets()` access, owner lookups, list-pets flows, and owner-view behaviour are preserved exactly as they are today; the new method is additive only.
3. **Non-functional handling:** there is no separate performance or accessibility target in the story. The non-functional expectation is that `hasPets()` stays a constant-time read of the already-loaded pets collection and introduces no extra mutation or persistence behaviour.
4. **Assumed criteria:** No assumed criteria.

If any of these assumptions are wrong, stop and revise the context file or the plan before proceeding.

---

## Impacted Files

| ID | Path | Role |
|----|------|------|
| F1 | src/main/java/org/springframework/samples/petclinic/owner/Owner.java | Owner entity that owns the pets collection and will expose the new boolean helper |
| F2 | src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java | Focused unit test file for owner-domain pet behaviour |

> Later steps refer to files by ID (for example, "edit F1 and F2"), never by re-listing paths. If Step 1 discovers another required file, add a new ID here and do not renumber the existing ones.

---

## Step 1 — Confirm the impacted file set

**Goal:** Verify that only the owner entity and its focused owner-domain tests need to change, and explicitly confirm that no controller, repository, view, config, schema, or seed file is required.
**Implements:** — (enabling step, no AC)
**Depends on:** —

**Suggested prompt:**

> Using owner-has-pets-context-260915-105037.md and the current codebase, confirm the minimal impacted file set for this story.
>
> Start with these candidate files only:
> - `src/main/java/org/springframework/samples/petclinic/owner/Owner.java`
> - `src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java`
>
> Check whether any other file is genuinely required, including non-code files such as config, schema, migration, or seed data. Remove any candidate that is not actually impacted. Do not propose edits yet. Return the final file list as `path | one-line role`, and explicitly say whether controller, repository, template, endpoint, and persistence files remain untouched.

**Review checkpoint:** Confirm the final file set stays limited to the owner entity and its owner-domain tests. If Copilot proposes controller, repository, template, or persistence changes, reject that drift unless it proves a real dependency the story text missed.

---

## Step 2 — Lock the method shape and test scope

**Goal:** Freeze the exact contract of the new helper and the smallest complete test set before editing code.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 1

**Suggested prompt:**

> Using owner-has-pets-context-260915-105037.md plus F1 and F2 from the Impacted Files table, describe the exact change to make before generating code.
>
> Confirm:
> 1. the method signature to add in F1,
> 2. the existing collection/member it must read,
> 3. the implementation approach that keeps the method read-only,
> 4. the minimum complete set of unit tests to add in F2 for empty, single-pet, multi-pet, and non-mutation behaviour.
>
> Match the existing local test style in F2. Do not modify any file yet.

**Review checkpoint:** Confirm the method is a zero-argument boolean helper on F1 and that the tests stay focused on observable behaviour, not implementation trivia. If the proposed approach mutates the pets collection or introduces any new field/state, stop and re-scope.

**Intended code target:**

Add this method to F1:

```java
public boolean hasPets()
```

The implementation should be equivalent to a read-only check of the existing pets collection, for example:

```java
public boolean hasPets() {
	return !getPets().isEmpty();
}
```

---

## Step 3 — Implement the Owner helper

**Goal:** Add the new read-only `hasPets()` convenience method to the owner entity without changing how pets are stored, added, or retrieved.
**Implements:** AC-1, AC-2, AC-3, AC-4
**Depends on:** Step 2

**Suggested prompt:**

> Edit F1 only.
>
> Add the new convenience method decided in Step 2:
> - `public boolean hasPets()`
>
> Requirements from owner-has-pets-context-260915-105037.md:
> - return `true` when the existing pets collection has at least one pet,
> - return `false` when the existing pets collection is empty,
> - read the existing collection only,
> - do not add, remove, reorder, or otherwise modify pets,
> - do not change persistence annotations, fields, constructors, or existing methods unless strictly necessary for this helper.
>
> Do not modify F2 yet.

**Review checkpoint:** Confirm only F1 changed, the method is additive, and the implementation is a direct read of the current collection state. If any existing method body or persistence mapping changed for convenience, reject that diff and regenerate more narrowly.

---

## Step 4 — Add focused Owner tests

**Goal:** Extend the owner-domain test file so the new helper is covered for true/false outcomes and non-mutation behaviour while preserving the package's existing test style.
**Implements:** AC-1, AC-2, AC-3, AC-4
**Depends on:** Step 3

**Suggested prompt:**

> Edit F2 only, using the local JUnit style already present there.
>
> Add focused unit tests for the new `hasPets()` method on F1 that cover:
> 1. empty owner returns `false`,
> 2. owner with one pet returns `true`,
> 3. owner with multiple pets returns `true`,
> 4. calling `hasPets()` does not change the pets collection contents or size before and after the call,
> 5. repeated checks reflect the current state if a pet is added between checks.
>
> Keep existing tests intact. Do not modify any controller, repository, or UI test file.

**Review checkpoint:** Confirm F2 still reads like the existing owner tests, every new assertion checks observable behaviour, and there is at least one explicit non-mutation assertion. If the test diff starts refactoring unrelated existing tests, roll it back and regenerate just the new cases.

**Suggested test names:**

```java
hasPetsReturnsFalseWhenOwnerHasNoPets
hasPetsReturnsTrueWhenOwnerHasOnePet
hasPetsReturnsTrueWhenOwnerHasMultiplePets
hasPetsDoesNotModifyPetsCollection
hasPetsReflectsCurrentStateAcrossRepeatedChecks
```

---

## Step 5 — Review for convention and scope drift

**Goal:** Inspect the changed files for drift against the story boundaries and the repo's observed local conventions before doing manual validation.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 4

**Suggested prompt:**

> Review all changes made to F1 and F2 against owner-has-pets-context-260915-105037.md and the local conventions visible in the owner package.
>
> Check specifically for:
> - any mutation inside `hasPets()`,
> - any new dependency, field, annotation, or persistence change that the story did not ask for,
> - any controller/repository/UI spillover,
> - any mismatch with the simple JUnit assertion style already used in F2.
>
> Do not change code automatically. List only concrete drift items, or say `no drift found`.

**Review checkpoint:** If any drift is listed, fix that specific earlier step instead of accepting the extra scope. The correct end state is a tiny additive entity helper plus matching owner-domain tests, nothing more.

---

## Step 6 — Manual validation against acceptance criteria

**Goal:** Walk every acceptance criterion from the context file and record a pass/fail verdict against the completed change.
**Implements:** — (verification step, no AC)
**Depends on:** Step 5

**Suggested prompt:**

> Using owner-has-pets-context-260915-105037.md, produce a manual verification checklist with one row per acceptance criterion (AC-1 through AC-5).
>
> For each AC, give:
> - the exact code-level or test-level evidence to inspect,
> - the expected result,
> - a blank verdict column (`pass`, `fail`, or `unclear`) for me to fill in.
>
> Include AC-5 even though no separate code step was required for it. Do not run anything; generate the checklist only.

**Review checkpoint:** Record a verdict for every AC individually. If any item is `fail` or `unclear`, loop back to the step that caused it rather than patching around the symptom at the end.

---

## Step 7 — Prepare the final handoff summary

**Goal:** Produce a concise completion note that captures the exact file changes, test additions, and AC outcomes for commit or PR use.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 6

**Suggested prompt:**

> Summarize the completed story using owner-has-pets-context-260915-105037.md and the final diffs in F1 and F2.
>
> Provide:
> 1. the exact files changed by ID,
> 2. the new method signature added,
> 3. the test methods added or updated,
> 4. the AC verdicts from Step 6,
> 5. one sentence confirming that no controller, repository, template, endpoint, or persistence file changed.
>
> Keep it concise and factual.

**Review checkpoint:** Confirm the summary matches the actual diff and the Step 6 verdict table. If the summary claims unchanged scope or passed criteria that the recorded evidence does not support, correct the work first.

---

## Done criteria

Before opening a PR, confirm:
- F1 contains an additive `public boolean hasPets()` method that reads the existing pets collection only.
- F2 covers empty, one-pet, multi-pet, repeated-check, and non-mutation scenarios for `hasPets()`.
- No file outside the confirmed impacted set was changed.
- Existing add-pet, direct pets access, and owner-related behaviour remain unchanged.
- Every acceptance criterion in owner-has-pets-context-260915-105037.md has an explicit verdict recorded in Step 6.

---

## --- EXECUTION RECORD (appended by harness) ---
- timestamp: 2026-09-15T10:54:11
- phase: coding
- approved impacted files: ['src/main/java/org/springframework/samples/petclinic/owner/Owner.java', 'src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java']
- actually touched: ['src/main/java/org/springframework/samples/petclinic/owner/Owner.java']
- scope: matches approved plan (no additions)
- review status: APPROVED by human at 2026-09-15T10:54:41
