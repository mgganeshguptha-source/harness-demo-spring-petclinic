# Plan for: Add a direct specialty-check method to the vet model

**Source:** vet-specialty-check-context-261012-093451.md + .github/copilot-instructions.md
**Stack:** backend
**Total steps:** 5
**Unresolved clarifications:** None

---

## Acceptance criteria coverage

| AC | Criterion (abbreviated) | Covered by |
|---|---|---|
| AC-1 | Matching specialty name returns true | Step 2 |
| AC-2 | Non-matching specialty name returns false | Step 2 |
| AC-3 | Empty specialty set returns false | Step 2 |
| AC-4 | Null or blank input returns false without exception | Step 2 |
| AC-5 | Trim outer whitespace before compare | Step 2 |
| AC-6 | Read specialties through existing internal accessor | Step 2 |
| AC-7 | Leave specialty collection unchanged | Step 2 |
| AC-8 | Existing specialty listing/count/addition stay unchanged | Step 2 |

**Every row must name a step or state why none is needed.** A blank cell is a failure of the plan, not a formatting detail: an AC that no step covers and no one has excused is a requirement the plan does not deliver. The two cases look identical when the cell is empty — a negative criterion that needs no code, and a criterion the plan simply forgot. Forcing a written reason makes the first case a claim someone can challenge, and leaves the second nowhere to hide.

Legitimate reasons a criterion needs no step:
- Negative criterion satisfied by scope (*SHALL NOT match on firstName* — satisfied by keeping the query on lastName)
- Already true of existing behaviour and explicitly preserved
- Enforced by a constraint rather than code (see Pre-flight)

In every one of those cases the criterion is still checked in the final validation step. "No step required" means no code to write, never no verification.

---

## Before you execute any step

1. Keep vet-specialty-check-context-261012-093451.md in your Copilot Chat context throughout the plan. Re-attach it after any session restart.
2. .github/copilot-instructions.md is auto-loaded by Copilot when present in the repo. You don't need to attach it manually. If team coding conventions are not being applied to the generated code, verify that the file exists at .github/copilot-instructions.md and that the VS Code setting github.copilot.chat.codeGeneration.useInstructionFiles is enabled.
3. Execute steps in one Copilot Chat session when possible. If you restart, paste the full plan back into the new chat alongside vet-specialty-check-context-261012-093451.md.
4. If a step asks Copilot to modify a file, confirm Copilot is reading the file's current contents rather than guessing. If the response doesn't reference real function names or imports from the file, ask Copilot to read the file first.
5. After Step 1, record the confirmed file set in the Impacted Files block (IDs F1, F2, ...). Later steps refer to files by ID; if you don't fill the block, those references won't resolve.

---

## Pre-flight

The plan assumes:

1. Backend uses Spring Boot with Spring Data JPA entities and JUnit 5 model tests. This story is a domain-model enhancement inside the existing server-side Java application; there is no frontend, API, or persistence-contract work.
2. Existing behaviour to preserve is: `getSpecialties()` still returns the current specialties sorted by name, `getNrOfSpecialties()` still reflects the current set size, `addSpecialty(...)` still adds to the same backing set, and the JPA specialty mapping plus eager-loading behaviour stay unchanged.
3. There is no separate performance, accessibility, or load target in this story. Non-functional handling is limited to keeping the helper method local to the in-memory entity state, plus targeted model-test coverage and a manual AC-by-AC verification checklist rather than any new benchmark or infrastructure work.
4. No assumed criteria.
5. Story-specific clarifications stay in force: `hasSpecialty(String name)` trims only leading and trailing whitespace, keeps internal whitespace untouched, compares stored specialty names as they are, returns `false` for null or blank-after-trim input, and reads the current set via `getSpecialtiesInternal()` without changing how specialties are stored or loaded.

If any of these assumptions are wrong, stop and revise the context file or the plan before proceeding.

---

## Impacted Files

| ID | Path | Role |
|----|------|------|
| F1 | src/main/java/org/springframework/samples/petclinic/vet/Vet.java | Domain model that will gain the new `hasSpecialty(String name)` helper |
| F2 | src/test/java/org/springframework/samples/petclinic/vet/VetTests.java | Existing JUnit 5 model test class to extend for the new helper behaviour |

> Later steps refer to files by ID (e.g. "edit F3 and F5"), never by re-listing paths. If Step 1 discovers a file the seed missed, add a new ID here — do not renumber existing IDs.

---

## Step 1 — Confirm the minimal file set

**Goal:** Confirm that only the vet model and its model test need changes, and explicitly rule out any controller, repository, mapping, database, or non-code file updates.
**Implements:** — (enabling step, no AC)
**Depends on:** —

**Suggested prompt:**

> Using vet-specialty-check-context-261012-093451.md and .github/copilot-instructions.md, inventory the files required for this story.
>
> Start with these candidate paths only:
> - src/main/java/org/springframework/samples/petclinic/vet/Vet.java
> - src/test/java/org/springframework/samples/petclinic/vet/VetTests.java
>
> Check whether any other files are genuinely required, including non-code files such as config, schema, migration, or seed-data files. Remove any candidate that is not impacted. Return only the confirmed file list with one-line roles, and call out any files that should stay untouched because they are out of scope. Do not propose edits yet.

**Review checkpoint:** Confirm the output keeps the scope to the vet model and its model test only. If Copilot adds controllers, repositories, templates, mappings, or database files without a concrete reason tied to the story, reject that expansion and rerun the inventory.

---

## Step 2 — Implement the specialty-check helper in the vet model

**Goal:** Add the new boolean helper on the vet entity while preserving the current specialty storage, accessor, and mutation behaviour.
**Implements:** AC-1, AC-2, AC-3, AC-4, AC-5, AC-6, AC-7, AC-8
**Depends on:** Step 1

**Suggested prompt:**

> Using vet-specialty-check-context-261012-093451.md and .github/copilot-instructions.md, edit F1 only.
>
> Add `public boolean hasSpecialty(String name)` to the vet model with this exact behaviour:
> - return `false` when `name` is `null`
> - trim leading and trailing whitespace into a local comparison value
> - return `false` when the trimmed value is blank
> - iterate the current specialties through `getSpecialtiesInternal()`
> - compare each stored specialty name case-insensitively against the trimmed input
> - return `true` on the first match and `false` otherwise
> - do not add, remove, reorder, or replace specialties
> - do not change the JPA mapping, the existing accessor methods, or `addSpecialty(...)`
>
> Keep the implementation consistent with the simple domain-helper style already used in this codebase. If you add Javadoc, keep it concise and behaviour-focused. Do not modify F2 yet.

**Review checkpoint:** Confirm only F1 changed, the method is public, it reads via `getSpecialtiesInternal()`, trims outer whitespace only, short-circuits on match, and never mutates the specialty set. Also confirm `getSpecialties()`, `getNrOfSpecialties()`, and `addSpecialty(...)` are byte-for-byte unchanged unless a documentation-only adjustment is truly necessary.

---

## Step 3 — Add focused model tests and run the targeted test class

**Goal:** Extend the existing vet model tests so every caller-visible branch of `hasSpecialty(...)` is covered without broadening the story scope.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 2

**Suggested prompt:**

> Using vet-specialty-check-context-261012-093451.md and .github/copilot-instructions.md, edit F2 only and keep the existing test style already used there.
>
> Add focused JUnit 5 tests for `hasSpecialty(String name)` that cover:
> - exact-case and different-case matches returning `true`
> - a non-matching specialty returning `false`
> - an empty specialty set returning `false`
> - `null` input returning `false` without throwing
> - blank-after-trim input returning `false`
> - leading/trailing whitespace being trimmed while internal whitespace is left untouched
> - the specialty collection staying unchanged after the call
> - existing behaviour remaining intact for specialty listing/count/addition in the same class where practical
>
> Match the package's assertion style. Keep controller tests out of scope. After updating F2, run the targeted model test class only and include the result summary. Do not edit any production file other than F2 in this step.

**Review checkpoint:** Confirm the new tests live only in F2, use the repo's existing JUnit 5 + AssertJ style, and cover every branch called out in the story clarifications. If a test forces a production-code change outside the `hasSpecialty(...)` contract, stop and revisit Step 2 instead of widening scope here.

---

## Step 4 — Manual validation against acceptance criteria

**Goal:** Walk every acceptance criterion against the implemented change and record a verdict per criterion before treating the story as done.
**Implements:** — (verification step, no AC)
**Depends on:** Step 3

**Suggested prompt:**

> Using vet-specialty-check-context-261012-093451.md, produce a manual verification checklist with one row per acceptance criterion, keyed by AC id.
>
> For each row, give:
> - the exact setup needed on a `Vet` instance
> - the method call to make
> - the expected boolean result or unchanged-state observation
>
> Include AC-7 and AC-8 explicitly so collection immutability and existing helper preservation are checked, not assumed. Do not run anything; provide the checklist only.

**Review checkpoint:** Execute the checklist and record pass / fail / unclear for AC-1 through AC-8 individually. If any criterion fails, loop back to the step that introduced the mismatch instead of patching around it after the fact.

---

## Step 5 — Review the changed files for convention drift

**Goal:** Do one last repo-convention pass on the changed files so the final diff stays aligned with the current domain-model and test patterns.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 4

**Suggested prompt:**

> Review the final diff for F1 and F2 against vet-specialty-check-context-261012-093451.md and .github/copilot-instructions.md.
>
> Report only real convention drift or scope drift that still needs attention, with one item per finding and the concrete fix to apply. Focus on Java domain-model conventions, JUnit test style, behaviour preservation, and accidental scope expansion. Do not make automatic edits in this step.

**Review checkpoint:** Expect either "no drift found" or a short, concrete list. If Copilot suggests changing files outside F1 or F2 here, treat that as drift in the review itself and ignore it unless the story text now clearly requires a broader change.

---

## Done criteria

Before opening a PR, confirm:
- `Vet` exposes `public boolean hasSpecialty(String name)` and the method returns `true` only for case-insensitive matches against the trimmed input name.
- `hasSpecialty(...)` returns `false` for no match, no specialties, `null`, and blank-after-trim input, without throwing.
- `hasSpecialty(...)` reads specialties through the existing internal accessor and does not change specialty storage, mapping, or mutation behaviour.
- The specialty collection is unchanged before and after `hasSpecialty(...)` calls.
- Existing `getSpecialties()`, `getNrOfSpecialties()`, and `addSpecialty(...)` behaviour remains intact.
- The targeted vet model tests cover the new helper branches and pass.
- Manual validation is recorded for AC-1 through AC-8 with no unresolved failures.

---

## --- EXECUTION RECORD (appended by harness) ---
- timestamp: 2026-10-12T09:37:54
- phase: coding
- approved impacted files: ['src/main/java/org/springframework/samples/petclinic/vet/Vet.java', 'src/test/java/org/springframework/samples/petclinic/vet/VetTests.java']
- actually touched: ['src/main/java/org/springframework/samples/petclinic/vet/Vet.java']
- scope: matches approved plan (no additions)
- review status: APPROVED by human at 2026-10-12T09:37:54
