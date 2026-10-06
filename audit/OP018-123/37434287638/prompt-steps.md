# Plan for: Add a method to check whether a Vet has a given specialty

**Source:** vet-specialty-check-context-261006-081022.md + .github/copilot-instructions.md (absent in this repo)
**Stack:** backend
**Total steps:** 6
**Unresolved clarifications:** None

---

## Acceptance criteria coverage

| AC | Criterion (abbreviated) | Covered by |
|---|---|---|
| AC-1 | matching stored specialty returns true | Step 2, Step 3, Step 5 |
| AC-2 | non-matching stored specialty returns false | Step 2, Step 3, Step 5 |
| AC-3 | no specialties returns false | Step 2, Step 3, Step 5 |
| AC-4 | null/empty/blank input returns false | Step 2, Step 3, Step 5 |
| AC-5 | use existing internal specialty access path | Step 2, Step 3, Step 5 |
| AC-6 | evaluation does not mutate specialties | Step 2, Step 3, Step 5 |
| AC-7 | specialty list accessor remains unchanged | Step 2, Step 3, Step 5 |
| AC-8 | specialty count accessor remains unchanged | Step 2, Step 3, Step 5 |
| AC-9 | add flow still makes specialty available for lookup | Step 2, Step 3, Step 5 |

---

## Before you execute any step

1. Keep vet-specialty-check-context-261006-081022.md in your Copilot Chat context throughout the plan. Re-attach it after any session restart, or treat it as the authoritative story file on disk if you are running non-interactively.
2. This repository does not have .github/copilot-instructions.md. Use the existing Java/Spring/JUnit patterns already present in F1 and F2 instead of waiting for instruction-file conventions to load.
3. Execute steps in one Copilot Chat session when possible. If you restart, paste the full plan back into the new chat alongside vet-specialty-check-context-261006-081022.md.
4. If a step asks Copilot to modify a file, confirm Copilot is reading the file's current contents rather than guessing. If the response does not reference real method names or imports from the file, ask Copilot to read the file first.
5. After Step 1, confirm the file set still matches the Impacted Files block. If Step 1 discovers a genuinely required extra file, add a new ID without renumbering existing IDs.

---

## Pre-flight

The plan assumes:

1. **Stack assumption:** backend code is standard Spring Boot / JPA domain-model Java with JUnit 5 and AssertJ tests. The story stays inside the `org.springframework.samples.petclinic.vet` domain package.
2. **Behaviour preservation:** existing `getSpecialties()`, `getNrOfSpecialties()`, and `addSpecialty(Specialty)` behaviour is preserved exactly; sorted specialty access, specialty counts, and add semantics remain unchanged.
3. **Non-functional handling:** there is no separate performance, accessibility, or load target in this story. Non-functional handling here means keeping the helper as an in-memory check over the existing eagerly loaded specialty set, with no mapping, transaction, or storage changes.
4. **Assumed criteria:** No assumed criteria.

If any of these assumptions are wrong, stop and revise the context file or the plan before proceeding.

---

## Impacted Files

| ID | Path | Role |
|----|------|------|
| F1 | src/main/java/org/springframework/samples/petclinic/vet/Vet.java | Domain entity that will gain `hasSpecialty(String name)` |
| F2 | src/test/java/org/springframework/samples/petclinic/vet/VetTests.java | Existing unit test class to extend with specialty helper coverage |

> Later steps refer to files by ID only. If Step 1 finds a genuinely required file beyond these seeds, add it as a new ID and keep F1/F2 stable.

---

## Step 1 — Confirm the exact file set

**Goal:** Confirm that only the vet domain class and its direct unit test need changes, and explicitly reject UI, controller, repository, database, or owner/pet drift.
**Implements:** — (enabling step, no AC)
**Depends on:** —

**Suggested prompt:**

> Using vet-specialty-check-context-261006-081022.md, inspect the current codebase and confirm the impacted files for this story.
>
> Start with these seed candidates only:
> - src/main/java/org/springframework/samples/petclinic/vet/Vet.java
> - src/test/java/org/springframework/samples/petclinic/vet/VetTests.java
>
> Add a file only if it is genuinely required for this story. Remove any seed that is not actually impacted. Explicitly check whether any non-code file, controller, repository, template, database script, or unrelated owner/pet file is required; if not, say so.
>
> Return the final file set as:
> - path
> - one-line role
> - why it is needed
>
> Do not propose edits yet.

**Review checkpoint:** Confirm the answer keeps scope to the vet story only: F1 and F2 should remain, and no UI/controller/repository/database file should be added. If Copilot pulls in owner/pet files from the unrelated text, reject that drift and rerun the step with the story scope restated.

---

## Step 2 — Add the direct specialty-name helper to the vet entity

**Goal:** Implement `public boolean hasSpecialty(String name)` in F1 using the existing internal specialty access path and preserving all current behaviour.
**Implements:** AC-1, AC-2, AC-3, AC-4, AC-5, AC-6, AC-7, AC-8, AC-9
**Depends on:** Step 1

**Suggested prompt:**

> Using vet-specialty-check-context-261006-081022.md, edit F1 only.
>
> Add `public boolean hasSpecialty(String name)` to `Vet` with these exact rules:
> - return `false` for `null`
> - trim leading/trailing whitespace from the supplied name
> - return `false` if the trimmed name is empty or blank
> - read stored specialties through `getSpecialtiesInternal()`
> - compare each stored specialty name case-insensitively against the trimmed input
> - return `true` on the first match and `false` otherwise
> - do not modify, reorder, add, or remove specialties while checking
> - do not change field mappings, storage, `getSpecialties()`, `getNrOfSpecialties()`, or `addSpecialty(Specialty)`
>
> Keep the implementation simple and aligned with the existing `Vet` helper style. Do not edit F2 yet.

**Review checkpoint:** Confirm only F1 changed, the new method is `public boolean hasSpecialty(String name)`, it uses `getSpecialtiesInternal()` rather than bypassing the existing access path, trims only the input, and does not alter any existing helper or mapping code. If any other method or entity mapping changed, reject the diff and rerun.

---

## Step 3 — Add focused unit coverage for the new helper and preserved behaviour

**Goal:** Extend F2 so the new helper is covered across match, no-match, empty-set, invalid-input, and preservation scenarios without broadening scope.
**Implements:** AC-1, AC-2, AC-3, AC-4, AC-5, AC-6, AC-7, AC-8, AC-9
**Depends on:** Step 2

**Suggested prompt:**

> Using vet-specialty-check-context-261006-081022.md, edit F2 only and follow the existing JUnit 5 + AssertJ style already used there.
>
> Add focused tests for `Vet.hasSpecialty(String name)` covering:
> - exact and case-insensitive match (`"surgery"` and `"Surgery"`)
> - trimmed match (`" surgery "` and `" Surgery"`)
> - non-match (`"dentistry"`)
> - no specialties
> - `null`, empty string, and blank string
> - internal whitespace preserved (`"sur gery"` does not match `"surgery"`)
>
> Also add preservation checks proving:
> - calling `hasSpecialty` does not change the sorted output from `getSpecialties()`
> - calling `hasSpecialty` does not change `getNrOfSpecialties()`
> - after `addSpecialty(...)`, the added specialty can still be found by `hasSpecialty`
>
> Keep the tests self-contained and synthetic. Do not edit controller tests or add unrelated coverage.

**Review checkpoint:** Confirm F2 covers every acceptance criterion without touching `VetControllerTests` or any other file. Make sure there is at least one explicit assertion for each invalid-input case and at least one state-preservation assertion before/after calling `hasSpecialty`. If the tests only verify interactions or skip preservation checks, rerun the step.

---

## Step 4 — Run the focused vet tests and fix only local issues

**Goal:** Prove the targeted unit coverage passes and keep any follow-up fixes constrained to F1/F2.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 3

**Suggested prompt:**

> Run the focused vet unit tests for this story with the repository's Maven wrapper:
>
> `./mvnw -q -Dtest=VetTests test`
>
> If the run fails, inspect the failure and fix only F1 and/or F2 as needed for this story. Do not widen scope to unrelated tests or files unless the failure proves a directly coupled requirement.

**Review checkpoint:** Confirm the run exercised `VetTests` only and any fixes stayed inside F1/F2. If Copilot proposes touching unrelated files to satisfy the run, reject that change and keep the fix local to this story.

---

## Step 5 — Manually validate each acceptance criterion

**Goal:** Turn the context file into a concrete verification checklist and record a verdict for every AC against the implemented code.
**Implements:** — (verification step, no AC)
**Depends on:** Step 4

**Suggested prompt:**

> Using vet-specialty-check-context-261006-081022.md, produce a manual verification checklist with one row per acceptance criterion, keyed by AC id.
>
> For each row, give:
> - the exact vet setup needed
> - the exact method call to make
> - the expected boolean or preservation outcome
>
> Include AC-7, AC-8, and AC-9 explicitly; do not collapse them into a general note. Do not run anything; give me the checklist only.

**Review checkpoint:** Record `pass`, `fail`, or `unclear` for every AC from AC-1 through AC-9. A criterion is not complete until it has its own verdict. If any AC fails, loop back to the step that introduced the gap instead of patching ad hoc at the end.

---

## Step 6 — Review for convention and scope drift

**Goal:** Check the final diff for unnecessary complexity, repository-pattern drift, and accidental changes outside the vet story before opening a PR.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 5

**Suggested prompt:**

> Review the final changes in F1 and F2 against vet-specialty-check-context-261006-081022.md and the surrounding repository conventions.
>
> Flag only substantive drift such as:
> - scope escaping beyond F1/F2
> - changes to specialty storage or mappings
> - missing trim / blank handling
> - mutation of specialties during lookup
> - tests that miss an acceptance criterion or rely on unrelated files
>
> This repo has no .github/copilot-instructions.md, so compare against the existing local patterns in the `vet` package. Do not edit code automatically; list any drift plainly.

**Review checkpoint:** Confirm the final change set is still limited to F1/F2, the method signature and semantics match the story, and there are no opportunistic cleanups or unrelated refactors. If drift is flagged, fix it before PR prep.

---

## Done criteria

Before opening a PR, confirm:
- `Vet` exposes `public boolean hasSpecialty(String name)` and only F1/F2 changed.
- AC-1 through AC-4 are each proven by direct unit assertions on true/false outcomes.
- AC-5 is satisfied by reading specialties through `getSpecialtiesInternal()`.
- AC-6 is satisfied by tests showing `hasSpecialty` does not mutate specialty state.
- AC-7 and AC-8 are satisfied by unchanged `getSpecialties()` ordering and `getNrOfSpecialties()` counts after invoking `hasSpecialty`.
- AC-9 is satisfied by proving `addSpecialty(...)` still feeds later `hasSpecialty(...)` checks.
- No UI, controller, repository, database, owner, or pet file was modified for this story.

---

## --- EXECUTION RECORD (appended by harness) ---
- timestamp: 2026-10-06T08:12:58
- phase: coding
- approved impacted files: ['src/main/java/org/springframework/samples/petclinic/vet/Vet.java', 'src/test/java/org/springframework/samples/petclinic/vet/VetTests.java']
- actually touched: ['src/main/java/org/springframework/samples/petclinic/vet/Vet.java']
- scope: matches approved plan (no additions)
- review status: APPROVED by human at 2026-10-06T08:12:58
