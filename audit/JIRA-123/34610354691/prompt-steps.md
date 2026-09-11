# Plan for: Add a method to check whether an Owner has any pets

**Source:** .github/story-context-files/owner-has-pets-context-260911-142910.md + .github/copilot-instructions.md (missing in repo)
**Stack:** backend
**Total steps:** 5
**Unresolved clarifications:** None

---

## Acceptance criteria coverage

| AC | Criterion (abbreviated) | Covered by |
|---|---|---|
| AC-1 | Non-empty pets collection returns `true` | Step 2, Step 3 |
| AC-2 | Empty pets collection returns `false` | Step 2, Step 3 |
| AC-3 | Result is derived from the existing pets collection | Step 2, Step 3 |
| AC-4 | Query does not add, remove, replace, or reorder pets | Step 2, Step 3 |
| AC-5 | Existing `addPet` behaviour is reflected without extra mutation | Step 2, Step 3 |
| AC-6 | Reading pets after `hasPets()` returns the same contents | Step 2, Step 3 |

---

## Before you execute any step

1. Keep .github/story-context-files/owner-has-pets-context-260911-142910.md in your Copilot Chat context throughout the plan. Re-attach it after any session restart.
2. `.github/copilot-instructions.md` is the usual repo-wide instruction source. It is currently absent, so generated code should follow the generic Spring Boot defaults captured in .github/story-context-files/owner-has-pets-context-260911-142910.md and the existing patterns in the owner package.
3. Execute steps in one Copilot Chat session when possible. If you restart, paste this full plan back into the new chat alongside .github/story-context-files/owner-has-pets-context-260911-142910.md.
4. If a step asks Copilot to modify a file, confirm Copilot is reading the file's current contents rather than guessing. If the response does not reference real methods such as `getPets()` and `addPet(...)`, ask Copilot to read the file first.
5. After Step 1, confirm the Impacted Files block still matches the final file set. Later steps refer to files by ID; if Step 1 adds a file, append a new ID without renumbering the existing rows.

---

## Pre-flight

The plan assumes:

1. Backend uses the existing Spring Boot domain model in `org.springframework.samples.petclinic.owner`, with `Owner` as a JPA entity and plain in-memory collection logic for pet access.
2. Existing owner and pet behaviour is preserved: `addPet(...)`, `getPets()`, owner-detail rendering, and pet listing/view flows remain unchanged; this story adds only a read-only convenience query on `Owner`.
3. No separate non-functional target is introduced. The work is limited to a constant-time collection emptiness check plus unit coverage and manual AC walkthrough; no persistence, API, or performance tuning step is required.
4. No assumed criteria.

If any of these assumptions are wrong, stop and revise the context file or the plan before proceeding.

---

## Impacted Files

| ID | Path | Role |
|----|------|------|
| F1 | src/main/java/org/springframework/samples/petclinic/owner/Owner.java | Owner domain entity where the new `hasPets()` convenience query will be added |
| F2 | src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java | Existing owner unit test class that should cover the new query and preserved collection behaviour |

> Later steps refer to files by ID (for example, "edit F1"), never by re-listing paths. If Step 1 discovers a genuinely required file, add a new ID here and keep F1/F2 unchanged.

---

## Step 1 — Confirm the exact impacted file set

**Goal:** Confirm that the story only needs the owner entity and its existing unit test, and explicitly rule out any hidden source, config, or migration changes.
**Implements:** — (enabling step, no AC)
**Depends on:** —

**Suggested prompt:**

> Read .github/story-context-files/owner-has-pets-context-260911-142910.md first, then inspect these candidate files:
> - src/main/java/org/springframework/samples/petclinic/owner/Owner.java
> - src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java
>
> Start with those, add any genuinely required files (including non-code files such as schema, migration, seed, or config files), and remove any file that is not actually impacted.
>
> Return the final impacted file list as:
> - path
> - one-line role
>
> Do not propose edits or code yet. Do not include controller, repository, UI, or endpoint files unless the story text truly forces them.

**Review checkpoint:** Confirm the final set is still just F1 and F2, or append a new file ID only if Copilot gives a concrete reason tied to the story scope. If it tries to include controller, repository, UI, or persistence-mapping files without a direct requirement, reject that drift and rerun the step with the out-of-scope list from the context file.

---

## Step 2 — Add the read-only `hasPets()` query to the owner entity

**Goal:** Implement the convenience method on `Owner` so callers can ask directly whether the existing pets collection is empty or not.
**Implements:** AC-1, AC-2, AC-3, AC-4, AC-5, AC-6
**Depends on:** Step 1

**Suggested prompt:**

> Using .github/story-context-files/owner-has-pets-context-260911-142910.md and the confirmed Impacted Files table, edit F1 only.
>
> Add a new method with this signature to `Owner`:
>
> ```java
> public boolean hasPets()
> ```
>
> Requirements:
> - Derive the result from the existing `pets` collection already exposed through `getPets()`
> - Return `true` when the owner currently has one or more pets
> - Return `false` when the owner has none
> - Keep the method read-only: no add/remove/reorder/mutation side effects
> - Do not change persistence annotations, `addPet(...)`, `getPets()`, `getPet(...)`, controller code, repository code, or any API contract
>
> Place the method where it fits the existing `Owner` class structure and keep the coding style aligned with the surrounding file. Do not modify tests yet.

**Review checkpoint:** Confirm only F1 changed, the method name and signature are exactly `public boolean hasPets()`, the implementation reads existing state only, and there is no collateral edit to persistence mappings or pet mutation logic. If the diff touches any method other than the new query or nearby formatting/import noise, loop back and tighten the prompt.

---

## Step 3 — Extend owner unit tests for the new query and preserved behaviour

**Goal:** Add focused unit coverage for the new query while preserving the existing `OwnerTests` style.
**Implements:** AC-1, AC-2, AC-3, AC-4, AC-5, AC-6
**Depends on:** Step 2

**Suggested prompt:**

> Using .github/story-context-files/owner-has-pets-context-260911-142910.md and the confirmed Impacted Files table, edit F2 only.
>
> Match the existing JUnit 5 style already used in `OwnerTests` (`org.junit.jupiter.api.Test` plus JUnit assertions). Keep the existing tests intact and add coverage for `Owner.hasPets()` with focused tests for:
> - empty owner returns `false`
> - owner with one pet returns `true`
> - owner with multiple pets returns `true`
> - calling `hasPets()` does not change the pet collection contents or size
> - `addPet(null)` remains a no-op, and `hasPets()` still reflects the unchanged collection
> - duplicate-pet rejection remains unchanged, and `hasPets()` stays `true` after the first successful add
>
> Do not modify F1 in this step unless a compile issue in the just-added method forces a minimal correction. Do not add controller or integration tests.

**Review checkpoint:** Confirm F2 now covers both boolean outcomes and the no-mutation guarantees from AC-4 through AC-6, while preserving the existing simple JUnit style. If the tests start asserting controller/view behaviour or require unrelated fixture changes, reject that scope expansion and rerun the step.

---

## Step 4 — Manually validate each acceptance criterion

**Goal:** Walk every acceptance criterion from the context file against the finished code and test behaviour before treating the story as done.
**Implements:** — (verification step, no AC)
**Depends on:** Step 3

**Suggested prompt:**

> Using .github/story-context-files/owner-has-pets-context-260911-142910.md, produce a verification checklist with one row per acceptance criterion (AC-1 through AC-6).
>
> For each row, give:
> - the object setup
> - the exact action (`hasPets()`, plus any setup call such as `addPet(...)`)
> - the expected boolean or collection outcome
>
> Include the collection-preservation checks explicitly for AC-4 and AC-6. Do not run anything; provide the checklist only.

**Review checkpoint:** Record a pass/fail/unclear result for every AC id individually. If any criterion fails, go back to the step that introduced the gap instead of patching loosely at the end.

---

## Step 5 — Review for convention and scope drift

**Goal:** Make one last pass over the changed files to ensure the story stayed within scope and matched the repo's existing Java/test conventions.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 4

**Suggested prompt:**

> Review the final diffs for F1 and F2 against .github/story-context-files/owner-has-pets-context-260911-142910.md and the expected repo conventions.
>
> `.github/copilot-instructions.md` is absent, so use the surrounding package patterns and generic Spring Boot defaults instead.
>
> Check for:
> - exact scope control: only F1 and F2 changed
> - `hasPets()` remains a read-only convenience query
> - no controller, repository, UI, endpoint, persistence-mapping, or logging changes slipped in
> - test style still matches the existing `OwnerTests` conventions
>
> Do not auto-fix anything. List any drift as file + issue + recommended rollback/fix.

**Review checkpoint:** Confirm there is no drift report, or resolve every flagged item before opening a PR. If this step reports a file outside F1/F2, treat that as a scope failure and remove it unless the story was formally expanded.

---

## Done criteria

Before opening a PR, confirm:

- `Owner` exposes `public boolean hasPets()` in F1, and the method reads the existing pets collection without mutating it
- F2 covers empty, single-pet, multi-pet, null-add, duplicate-add, and collection-preservation scenarios for `hasPets()`
- No files outside F1 and F2 changed for this story
- Manual validation has an explicit verdict for AC-1, AC-2, AC-3, AC-4, AC-5, and AC-6
- Existing add/list/view owner and pet behaviour remains unchanged


## --- EXECUTION RECORD (appended by harness) ---
- timestamp: 2026-09-11T14:31:19
- phase: coding
- approved impacted files: ['src/main/java/org/springframework/samples/petclinic/owner/Owner.java', 'src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java']
- actually touched: ['src/main/java/org/springframework/samples/petclinic/owner/Owner.java']
- scope: matches approved plan (no additions)
- review status: APPROVED by human at 2026-09-11T14:31:19
