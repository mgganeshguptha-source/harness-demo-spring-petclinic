# Plan for: Add a method to check whether a Vet has a given specialty

**Source:** .github/story-context-files/vet-specialty-check-context-261006-061148.md + .github/copilot-instructions.md (absent in repo)
**Stack:** backend
**Total steps:** 6
**Unresolved clarifications:** None

---

## Acceptance criteria coverage

| AC | Criterion (abbreviated) | Covered by |
|---|---|---|
| AC-1 | Case-insensitive specialty match returns true | Step 2, Step 3 |
| AC-2 | Trim leading and trailing whitespace before matching | Step 2, Step 3 |
| AC-3 | Unmatched trimmed name returns false | Step 2, Step 3 |
| AC-4 | No specialties returns false | Step 2, Step 3 |
| AC-5 | Null input returns false | Step 2, Step 3 |
| AC-6 | Blank-after-trim input returns false | Step 2, Step 3 |
| AC-7 | Lookup leaves stored specialties unchanged | Step 2, Step 3 |
| AC-8 | Existing addSpecialty path feeds the lookup | Step 2, Step 3 |
| AC-9 | Internal whitespace is not normalized | Step 2, Step 3 |
| AC-10 | Existing read/count/add behaviour is unchanged | Step 2, Step 3 |

---

## Before you execute any step

1. Keep .github/story-context-files/vet-specialty-check-context-261006-061148.md in Copilot Chat context throughout the plan. Re-attach it after any session restart.
2. `.github/copilot-instructions.md` is absent in this repo, so use the repo instruction files already in effect plus generic Spring Boot / Java 17 defaults. Do not invent extra repo conventions.
3. Execute the steps in one Copilot Chat session when possible. If you restart, paste this full plan back into the new chat alongside the context file.
4. If a step modifies a file, make sure Copilot has read that file's current contents first. If the response does not reference the real members already present, ask it to read the file before editing.
5. After Step 1, update the Impacted Files block with the confirmed final file set. Later steps use file IDs only.

---

## Pre-flight

The plan assumes:

1. **Stack assumption:** this is a backend-only Java 17 Spring Boot domain-model change inside the JPA entity layer. No controller, repository, UI, or database work is expected.
2. **Behaviour preservation:** the existing `getSpecialties()`, `getNrOfSpecialties()`, and `addSpecialty(Specialty)` behaviours are preserved exactly, and specialty storage/mapping through `getSpecialtiesInternal()` remains unchanged.
3. **Non-functional handling:** there is no separate performance, accessibility, or load target in this story. Verification is functional only: focused unit tests plus final AC-by-AC validation.
4. **Assumed criteria:** no assumed criteria.
5. **Repo convention note:** `.github/copilot-instructions.md` is missing, so any convention review should check against the active instruction files and the existing local code pattern instead of repo-wide custom rules.

If any of these assumptions are wrong, stop and revise the context file or the plan before proceeding.

---

## Impacted Files

| ID | Path | Role |
|----|------|------|
| F1 | src/main/java/org/springframework/samples/petclinic/vet/Vet.java | Domain entity that will gain `hasSpecialty(String name)` and must keep specialty storage unchanged |
| F2 | src/test/java/org/springframework/samples/petclinic/vet/VetTests.java | Unit test file to cover the new lookup behaviour and preservation of existing state |

> Later steps refer to files by ID only. If Step 1 discovers another genuinely required file, add a new ID here and do not renumber F1 or F2.

---

## Step 1 — Confirm the minimal file set

**Goal:** Confirm that the story really touches only the vet entity and its direct unit tests, and that no repository/controller/UI/mapping file is required.
**Implements:** — (enabling step, no AC)
**Depends on:** —

**Suggested prompt:**

> Read .github/story-context-files/vet-specialty-check-context-261006-061148.md and inspect these candidate files only:
> - src/main/java/org/springframework/samples/petclinic/vet/Vet.java
> - src/test/java/org/springframework/samples/petclinic/vet/VetTests.java
>
> Confirm whether those are the only impacted files for this story. Add any genuinely required file only if the change cannot be completed without it, and explicitly check for non-code files as well. Remove any candidate that does not need to change.
>
> Return the final impacted file list as: path + one-line role. Do not propose edits yet. Do not modify any files.

**Review checkpoint:** Confirm the file set stays limited to the vet domain class and its focused tests. If Copilot proposes controller, repository, database, or UI files, reject that drift unless it shows a concrete code dependency the story cannot avoid.

---

## Step 2 — Implement `Vet.hasSpecialty(String name)`

**Goal:** Add the new lookup method to F1 using the existing specialty collection and without changing how specialties are stored, loaded, or added.
**Implements:** AC-1, AC-2, AC-3, AC-4, AC-5, AC-6, AC-7, AC-8, AC-9, AC-10
**Depends on:** Step 1

**Target signature:** `public boolean hasSpecialty(String name)`

**Planned edit sketch (text only):**

```java
public boolean hasSpecialty(String name) {
    if (name == null) {
        return false;
    }

    String trimmedName = name.trim();
    if (trimmedName.isBlank()) {
        return false;
    }

    return getSpecialtiesInternal().stream()
        .map(Specialty::getName)
        .anyMatch(specialtyName -> specialtyName != null && specialtyName.equalsIgnoreCase(trimmedName));
}
```

**Suggested prompt:**

> Using .github/story-context-files/vet-specialty-check-context-261006-061148.md, edit F1 only.
>
> Add `public boolean hasSpecialty(String name)` to the existing `Vet` class. Requirements:
> - read specialties through `getSpecialtiesInternal()`
> - return `false` for `null`
> - trim leading and trailing whitespace from the input before matching
> - return `false` if the trimmed value is blank
> - compare the trimmed input to each stored specialty name case-insensitively
> - keep internal whitespace unchanged (`"sur gery"` must not match `"surgery"`)
> - do not add, remove, reorder, or mutate stored specialties
> - do not change `getSpecialties()`, `getNrOfSpecialties()`, `addSpecialty(...)`, mappings, imports unrelated to this method, or any other file
>
> Show the exact diff for F1 only.

**Review checkpoint:** Confirm only F1 changed, the new method is public, it uses `getSpecialtiesInternal()`, and there is no storage or mapping change. Reject any version that normalizes stored names, touches existing methods, or adds defensive behaviour not asked for.

---

## Step 3 — Add focused unit tests for the new lookup

**Goal:** Extend F2 so the story is covered with deterministic unit tests that match the existing test style.
**Implements:** AC-1, AC-2, AC-3, AC-4, AC-5, AC-6, AC-7, AC-8, AC-9, AC-10
**Depends on:** Step 2

**Planned test coverage (text only):**

```text
- hasSpecialtyReturnsTrueForCaseInsensitiveMatch
- hasSpecialtyTrimsLeadingAndTrailingWhitespace
- hasSpecialtyReturnsFalseForUnmatchedName
- hasSpecialtyReturnsFalseWhenVetHasNoSpecialties
- hasSpecialtyReturnsFalseForNullInput
- hasSpecialtyReturnsFalseForBlankInputAfterTrimming
- hasSpecialtyKeepsInternalWhitespaceSignificant
- hasSpecialtyDoesNotModifyStoredSpecialties
- hasSpecialtySeesSpecialtiesAddedThroughAddSpecialty
- existingSpecialtyAccessorsStillBehaveAsBefore
```

**Suggested prompt:**

> Using .github/story-context-files/vet-specialty-check-context-261006-061148.md, edit F2 only.
>
> Add or update focused JUnit 5 tests for the new `hasSpecialty(String name)` method in F1. Match the local file's existing conventions. Cover:
> - case-insensitive match returning true
> - leading/trailing whitespace trimming
> - unmatched name returning false
> - no specialties returning false
> - `null` returning false
> - blank-after-trim returning false
> - internal whitespace remaining significant
> - lookup not mutating the stored specialties or names
> - a specialty added through `addSpecialty(...)` being visible to the lookup
> - existing `getSpecialties()`, `getNrOfSpecialties()`, and `addSpecialty(...)` behaviour staying unchanged
>
> Use clearly synthetic specialty names only. Do not modify F1 in this step. Do not add integration, controller, or repository tests. Show the exact diff for F2 only.

**Review checkpoint:** Confirm each acceptance criterion has a direct assertion path in F2, especially the negative cases and the non-mutation check. If the tests only prove mock interactions or skip the unchanged-behaviour assertions, send the step back.

---

## Step 4 — Run the focused verification tests

**Goal:** Execute the narrowest existing test command that proves the new behaviour and catches regressions in the touched area.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 3

**Suggested prompt:**

> Run the focused test command for F2 from the repository root, using the existing Maven wrapper:
>
> `./mvnw -q -Dtest=VetTests test`
>
> If that passes cleanly and no unrelated failures appear, stop there. If it fails, report the exact failure and fix only F1 or F2 as needed. Do not broaden scope to other files unless the failure proves another file must change.

**Review checkpoint:** Confirm the failure surface, if any, stays inside F1/F2. If a proposed fix expands beyond the planned file set without a concrete compile or test reason, reject it and narrow the change back down.

---

## Step 5 — Manual validation against acceptance criteria

**Goal:** Walk every acceptance criterion from the context file against the implemented code and its focused test evidence.
**Implements:** — (verification step, no AC)
**Depends on:** Step 4

**Suggested prompt:**

> Using .github/story-context-files/vet-specialty-check-context-261006-061148.md, produce a verification checklist with one row per acceptance criterion (AC-1 through AC-10).
>
> For each row, state:
> - the exact code path or test evidence to inspect
> - the expected outcome
> - a pass/fail/unclear column for the reviewer to fill in
>
> Include the preservation criteria around unchanged specialty storage and unchanged `getSpecialties()`, `getNrOfSpecialties()`, and `addSpecialty(...)` behaviour. Do not modify code.

**Review checkpoint:** Record a verdict for every AC individually. If any AC is unclear, treat that as unresolved and loop back to the relevant earlier step instead of hand-waving it as covered.

---

## Step 6 — Review for convention and scope drift

**Goal:** Verify the final diff stays inside story scope and matches the existing local code pattern plus the active instruction files.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 5

**Suggested prompt:**

> Review the final diff in F1 and F2 against .github/story-context-files/vet-specialty-check-context-261006-061148.md and the active instruction files.
>
> Check specifically:
> - only F1 and F2 changed
> - no controller, repository, database, mapping, or UI drift
> - `hasSpecialty(String name)` is the only new production API
> - no logging, transaction, fetch-type, or persistence behaviour was introduced
> - test coverage remains focused and deterministic
>
> List any drift you find. Do not auto-fix it in this review step.

**Review checkpoint:** Accept the work only if it stays inside the story's stated scope and the diff is limited to the new method plus its focused tests. Any extra production edit needs a concrete reason or it should be removed.

---

## Done criteria

Before opening a PR, confirm:
- AC-1 through AC-3 are proven by direct match, trim, and mismatch tests for `hasSpecialty(String name)`.
- AC-4 through AC-6 are proven by empty-specialty, null-input, and blank-input tests.
- AC-7 through AC-9 are proven by tests showing lookup is read-only, respects `addSpecialty(...)`, and preserves internal whitespace semantics.
- AC-10 is proven by unchanged observable behaviour of `getSpecialties()`, `getNrOfSpecialties()`, and `addSpecialty(...)`.
- The final diff is limited to the vet entity and its focused unit tests.

---

## --- EXECUTION RECORD (appended by harness) ---
- timestamp: 2026-10-06T06:14:51
- phase: coding
- approved impacted files: ['src/main/java/org/springframework/samples/petclinic/vet/Vet.java', 'src/test/java/org/springframework/samples/petclinic/vet/VetTests.java']
- actually touched: ['src/main/java/org/springframework/samples/petclinic/vet/Vet.java']
- scope: matches approved plan (no additions)
- review status: APPROVED by human at 2026-10-06T06:14:51
