# Plan for: Add a method to check whether an Owner has any pets

**Source:** owner-has-pets-context-260916-091413.md + .github/copilot-instructions.md (not present in repo; use generic Spring Boot and JUnit conventions)
**Stack:** backend
**Total steps:** 6
**Unresolved clarifications:** None

---

## Acceptance criteria coverage

| AC | Criterion (abbreviated) | Covered by |
|---|---|---|
| AC-1 | Non-empty pet collection returns true | Step 2, Step 3, Step 4 |
| AC-2 | Empty pet collection returns false | Step 2, Step 3, Step 4 |
| AC-3 | Read existing pet collection only | Step 2, Step 5 |
| AC-4 | Does not add, remove, or modify pets | Step 2, Step 3, Step 5 |
| AC-5 | Existing owner and pet behaviour unchanged | Step 3, Step 4, Step 5 |

---

## Before you execute any step

1. Keep owner-has-pets-context-260916-091413.md in your Copilot Chat context throughout the plan. Re-attach it after any session restart.
2. `.github/copilot-instructions.md` is not present in this repo, so use the repository's existing Spring Boot and JUnit patterns from the touched files as the coding convention source.
3. Execute steps in one Copilot Chat session when possible. If you restart, paste the full plan back into the new chat alongside owner-has-pets-context-260916-091413.md.
4. If a step asks Copilot to modify a file, confirm Copilot is reading the file's current contents rather than guessing. If the response does not reference real method names from the file, ask Copilot to read the file first.
5. After Step 1, record the confirmed file set in the Impacted Files block below with IDs. Later steps refer to files by ID; if you do not fill the block, those references will not resolve.

---

## Pre-flight

The plan assumes:

1. **Stack assumption:** backend uses the existing Spring Boot / JPA domain model in `org.springframework.samples.petclinic.owner`; this story is a local domain-model change, not an API or UI change.
2. **Behaviour preservation:** existing `Owner` and `Pet` behaviour is preserved, especially adding pets, listing pets through `getPets()`, duplicate-add rejection, and viewing owner details through the current controller flow.
3. **Non-functional handling:** there is no new performance or API contract target here; the change should remain an in-memory boolean read of the already-initialized pets collection and be verified through focused unit tests plus regression checks rather than any load or endpoint testing.
4. **Assumed criteria:** No assumed criteria.
5. **Convention fallback:** because `.github/copilot-instructions.md` is absent, step prompts should follow the current local file patterns in `Owner.java` and `OwnerTests.java` instead of repo-wide custom instructions.

If any of these assumptions are wrong, stop and revise the context file or the plan before proceeding.

---

## Impacted Files

| ID | Path | Role |
|----|------|------|
| F1 | src/main/java/org/springframework/samples/petclinic/owner/Owner.java | Owner domain entity where the new convenience method will be added |
| F2 | src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java | Existing focused owner-domain test class to extend for the new boolean helper and regression checks |

> Later steps refer to files by ID (for example, "edit F1"), never by re-listing paths. If Step 1 discovers a file the seed missed, add a new ID here — do not renumber existing IDs.

---

## Step 1 — Confirm the exact file set

**Goal:** Confirm that only the owner entity and its focused owner-domain tests are impacted, and explicitly rule out controller, repository, UI, template, config, or migration changes.
**Implements:** — (enabling step, no AC)
**Depends on:** —

**Suggested prompt:**

> Using owner-has-pets-context-260916-091413.md, inspect the current codebase and confirm the impacted file set for this story.
>
> Start with these candidate files only:
> - src/main/java/org/springframework/samples/petclinic/owner/Owner.java
> - src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java
>
> Check whether any additional files are genuinely required, including non-code files such as config, schema, migration, or seed files. If none are needed, say so explicitly. Also call out any nearby files that are related but should remain untouched for this story.
>
> Return the result as:
> 1. confirmed impacted files with one-line roles
> 2. files explicitly out of scope
> 3. a short explanation of why no controller, repository, or UI change is needed
>
> Do not propose edits yet.

**Review checkpoint:** Confirm that the result keeps the scope to the `Owner` entity and `OwnerTests` only, with no controller, repository, template, endpoint, config, or persistence mapping change. If Copilot suggests extra files without a concrete need tied to an acceptance criterion, reject them and rerun this step.

---

## Step 2 — Add the `Owner.hasPets()` convenience method

**Goal:** Add a minimal boolean helper to the owner domain model that answers pet presence directly from the existing collection without changing collection state or association behaviour.
**Implements:** AC-1, AC-2, AC-3, AC-4
**Depends on:** Step 1

**Suggested prompt:**

> Using owner-has-pets-context-260916-091413.md, edit F1 only.
>
> Add a convenience method on `Owner` with this signature:
>
> ```java
> public boolean hasPets()
> ```
>
> Implement it by reading the existing `pets` collection state only. It must return `true` when the owner currently has one or more pets and `false` when the collection is empty. It must not add, remove, reorder, persist, or otherwise modify pets. Keep the existing persistence mapping, `getPets()`, `addPet(...)`, and other owner behaviour unchanged.
>
> Match the style already used in F1. Do not modify F2 yet. Do not modify any controller, repository, template, or endpoint file.
>
> In your response, include the exact diff and briefly point to where the new method sits within F1.

**Review checkpoint:** Confirm that only F1 changed, the new method is exactly `public boolean hasPets()`, and its body is a read-only boolean check of the existing collection. If the diff changes mapping annotations, `addPet(...)`, `getPets()`, or any unrelated method, reject it and rerun this step.

**Intended code change (text only):**

```java
public boolean hasPets() {
	return !this.pets.isEmpty();
}
```

---

## Step 3 — Extend owner-domain tests for the new helper

**Goal:** Add focused tests that prove the new helper returns the correct boolean and does not disturb existing add-pet behaviour.
**Implements:** AC-1, AC-2, AC-4, AC-5
**Depends on:** Step 2

**Suggested prompt:**

> Using owner-has-pets-context-260916-091413.md, edit F2 only.
>
> Extend the existing owner-domain tests to cover `Owner.hasPets()` using the same JUnit 5 assertion style already present in F2.
>
> Add tests for at least these scenarios:
> 1. a new owner with no pets returns `false`
> 2. an owner with one pet returns `true`
> 3. repeated calls on the same unchanged owner return the same result
> 4. a rejected duplicate add attempt leaves the collection unchanged and `hasPets()` still reflects that unchanged state
>
> Keep the tests deterministic and focused on owner-domain behaviour. Do not modify F1 in this step unless a compile fix is strictly required from Step 2. Do not create new test classes unless there is a concrete need.
>
> Return the diff and a short checklist mapping each new test to the acceptance criteria it covers.

**Review checkpoint:** Confirm the tests cover both empty and non-empty states, plus the unchanged-state regression around duplicate add behaviour. If Copilot rewrites the whole test class, changes unrelated assertions, or introduces controller/service tests, reject it and rerun this step.

**Intended test additions (text only):**

```java
@Test
void hasPetsReturnsFalseForOwnerWithNoPets() { ... }

@Test
void hasPetsReturnsTrueForOwnerWithPets() { ... }

@Test
void hasPetsReflectsUnchangedStateAfterDuplicateAddAttempt() { ... }
```

---

## Step 4 — Run focused regression checks

**Goal:** Run the smallest relevant test scope to confirm the new helper and existing owner behaviour work together.
**Implements:** AC-1, AC-2, AC-5
**Depends on:** Step 3

**Suggested prompt:**

> Using owner-has-pets-context-260916-091413.md, identify the narrowest existing Maven test command that exercises F2 and any immediately related owner-domain behaviour.
>
> Run that focused test scope first. If it passes, report the command used and the key behaviours it covers. If it fails, show the failure details and point to whether the defect is in F1 or F2.
>
> Do not broaden to full-suite changes or unrelated fixes unless the focused result proves they are required.

**Review checkpoint:** Confirm the executed test scope is focused on the owner domain and not a broad unrelated suite first. If failures appear outside owner-domain behaviour, verify whether they are pre-existing before widening the scope.

---

## Step 5 — Manually validate against acceptance criteria

**Goal:** Verify the implemented change against every acceptance criterion and the stated out-of-scope boundaries.
**Implements:** AC-3, AC-4, AC-5
**Depends on:** Step 4

**Suggested prompt:**

> Using owner-has-pets-context-260916-091413.md, produce a manual verification checklist keyed by acceptance criterion.
>
> For each AC, state:
> - what to inspect or execute
> - the exact expected result
> - what would count as a failure
>
> Include explicit checks that the story did not change controller behaviour, repository behaviour, persistence mapping, or UI flow, since those are out of scope.
>
> Do not run anything in this step; produce the checklist only.

**Review checkpoint:** Record a pass/fail result per AC, not one result for the story. If any item is unclear, loop back to the implementation or test step that should make it provable instead of accepting ambiguity.

---

## Step 6 — Review convention drift before handoff

**Goal:** Check the small diff for drift from the existing local code patterns and confirm that nothing outside scope was changed.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 5

**Suggested prompt:**

> Review the final diffs in F1 and F2 against the current local patterns used in those files and against the constraints in owner-has-pets-context-260916-091413.md.
>
> Report:
> 1. any convention drift from the current `Owner` and `OwnerTests` style
> 2. any change that exceeds the story scope
> 3. any missing regression coverage tied to AC-1 through AC-5
>
> Do not apply fixes automatically in this step. Produce a concise findings list only.

**Review checkpoint:** Confirm the final change remains a small `Owner` helper plus matching owner-domain tests, with no API, controller, repository, template, or persistence-behaviour drift. If drift is reported, fix it before handoff rather than documenting it as acceptable.

---

## Done criteria

Before opening a PR, confirm:

- `Owner` exposes `hasPets()` as a boolean convenience method.
- The method returns `true` for a non-empty pet collection and `false` for an empty one.
- The method reads existing collection state only and does not mutate the collection.
- Existing add-pet and owner-view behaviour remains unchanged.
- Only the owner entity and its focused owner-domain tests were modified.
- Focused regression checks passed for the owner-domain scope.

---

## --- EXECUTION RECORD (appended by harness) ---
- timestamp: 2026-09-16T09:17:32
- phase: coding
- approved impacted files: ['src/main/java/org/springframework/samples/petclinic/owner/Owner.java', 'src/test/java/org/springframework/samples/petclinic/owner/OwnerTests.java']
- actually touched: ['src/main/java/org/springframework/samples/petclinic/owner/Owner.java']
- scope: matches approved plan (no additions)
- review status: APPROVED by human at 2026-09-16T09:18:02
