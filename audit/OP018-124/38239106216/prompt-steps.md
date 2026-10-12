# Plan for: Add a method to find a Vet's specialty by name

**Source:** .github/story-context-files/vet-specialty-lookup-context-261012-105341.md + .github/copilot-instructions.md
**Stack:** backend
**Total steps:** 7
**Unresolved clarifications:** None

---

## Acceptance criteria coverage

| AC | Criterion (abbreviated) | Covered by |
|---|---|---|
| AC-1 | Matching name returns stored specialty | Step 3 |
| AC-2 | Outer whitespace is trimmed before matching | Step 3 |
| AC-3 | Null input returns Optional.empty() | Step 3 |
| AC-4 | Blank-after-trim input returns Optional.empty() | Step 3 |
| AC-5 | Unmatched name returns Optional.empty() | Step 3 |
| AC-6 | No specialties returns Optional.empty() | Step 3 |
| AC-7 | Lookup is read-only over existing specialty set | Step 3 |
| AC-8 | Existing specialty helpers remain unchanged | Step 3 |
| AC-9 | Presence result matches hasSpecialty(name) | Step 3 |

**Every row must name a step or state why none is needed.** A blank cell is a failure of the plan, not a formatting detail: an AC that no step covers and no one has excused is a requirement the plan does not deliver.

---

## Before you execute any step

1. Keep .github/story-context-files/vet-specialty-lookup-context-261012-105341.md in your Copilot Chat context throughout the plan. Re-attach it after any session restart.
2. .github/copilot-instructions.md is auto-loaded by Copilot when present in the repo. You don't need to attach it manually. If team coding conventions are not being applied to the generated code, verify that the file exists at .github/copilot-instructions.md and that the VS Code setting github.copilot.chat.codeGeneration.useInstructionFiles is enabled.
3. Execute steps in one Copilot Chat session when possible. If you restart, paste the full plan back into the new chat alongside .github/story-context-files/vet-specialty-lookup-context-261012-105341.md.
4. If a step asks Copilot to modify a file, confirm Copilot is reading the file's current contents rather than guessing. If the response doesn't reference real function names or imports from the file, ask Copilot to read the file first.
5. After Step 1, record the confirmed file set in the Impacted Files block (IDs F1, F2, ...). Later steps refer to files by ID; if you don't fill the block, those references won't resolve.

---

## Pre-flight

The plan assumes:

1. **Stack assumption:** Backend uses the existing Java 17 Spring Boot / JPA domain model in `org.springframework.samples.petclinic.vet`; this story is a local domain-object enhancement, not a controller, repository, API, or UI change.
2. **Behaviour preservation:** `getSpecialties()`, `getNrOfSpecialties()`, `hasSpecialty(String)`, and `addSpecialty(Specialty)` keep their current observable behaviour, and the existing specialty storage/access model through `getSpecialtiesInternal()` remains intact per D-OP018-123-1 and D-OP018-123-2.
3. **Non-functional handling:** No separate performance, accessibility, or load target is introduced; correctness is verified through focused domain tests plus the final AC-by-AC validation step rather than additional non-functional tooling.
4. **Assumed criteria:** No assumed criteria.

If any of these assumptions are wrong, stop and revise the context file or the plan before proceeding.

---

## Impacted Files

| ID | Path | Role |
|----|------|------|
| F1 | src/main/java/org/springframework/samples/petclinic/vet/Vet.java | Domain object that will gain `findSpecialty(String)` and any required `Optional` import |
| F2 | src/test/java/org/springframework/samples/petclinic/vet/VetTests.java | Existing JUnit 5 + AssertJ domain tests to extend for the new lookup and invariants |

> Later steps refer to files by ID (e.g. "edit F1 and F2"), never by re-listing paths. If Step 1 discovers a file the seed missed, add a new ID here — do not renumber existing IDs.

---

## Step 1 — Confirm the exact file set

**Goal:** Verify that the story really is limited to the Vet domain object and its focused tests, with no hidden source, config, or documentation files required.
**Implements:** — (enabling step, no AC)
**Depends on:** —

**Suggested prompt:**

> Using .github/story-context-files/vet-specialty-lookup-context-261012-105341.md and .github/copilot-instructions.md, inventory the exact files needed for this story.
>
> Start with these candidate files only:
> - src/main/java/org/springframework/samples/petclinic/vet/Vet.java
> - src/test/java/org/springframework/samples/petclinic/vet/VetTests.java
>
> Add any genuinely required files, including non-code files such as config or documentation, only if the story truly needs them. Remove any candidate that is not actually impacted. For each confirmed file, return path + one-line role + whether it will be edited or only referenced.
>
> Do not propose code changes yet. Do not include out-of-scope files such as controllers, repositories, templates, database scripts, or API contracts unless you can point to a concrete requirement in the context file.

**Review checkpoint:** Confirm the file set stays constrained to the Vet domain unit of change. Record the confirmed files in the Impacted Files table with stable IDs before continuing.

---

## Step 2 — Lock the method contract and invariants

**Goal:** Confirm the exact implementation contract for `findSpecialty(String)` so the later edit stays aligned with approved decisions and existing helper behaviour.
**Implements:** — (enabling step, no AC)
**Depends on:** Step 1

**Suggested prompt:**

> Read F1 and F2 together, using .github/story-context-files/vet-specialty-lookup-context-261012-105341.md as the source of truth.
>
> Summarize the precise implementation contract for the new `public Optional<Specialty> findSpecialty(String name)` method:
> - null handling
> - blank/trimmed handling
> - case-insensitive matching
> - required use of `getSpecialtiesInternal()`
> - read-only behaviour
> - parity expectation with `hasSpecialty(String)`
>
> Also note any tiny refactor that would be acceptable inside F1 to keep behaviour aligned, and what must not change in existing helpers. Do not edit files yet. Do not suggest changes outside F1 or F2.

**Review checkpoint:** Confirm the contract explicitly reflects AC-1 through AC-9 and D-OP018-123-1/2/3, especially the trim rule and the requirement that storage/addition behaviour remains unchanged.

---

## Step 3 — Add the specialty lookup to Vet

**Goal:** Implement the new `findSpecialty(String)` method on the Vet domain object without changing how specialties are stored, added, or exposed.
**Implements:** AC-1, AC-2, AC-3, AC-4, AC-5, AC-6, AC-7, AC-8, AC-9
**Depends on:** Step 2

**Suggested prompt:**

> Edit F1 only.
>
> Add `public Optional<Specialty> findSpecialty(String name)` to the Vet domain object. The method must:
> - return `Optional<Specialty>`
> - return `Optional.empty()` for null input
> - trim outer whitespace and return `Optional.empty()` if the trimmed name is blank
> - read specialties through `getSpecialtiesInternal()`
> - compare specialty names case-insensitively against the trimmed input
> - return the stored matching `Specialty` object, not a copy
> - return `Optional.empty()` when no match exists or when the vet has no specialties
> - avoid adding, removing, sorting, or otherwise mutating specialties
>
> Align the matching semantics with the approved trim/case rules from D-OP018-123-3. Preserve existing helper behaviour per D-OP018-123-1 and D-OP018-123-2.
>
> If a tiny internal refactor is needed to keep `findSpecialty(String)` and `hasSpecialty(String)` consistent, keep it minimal and preserve `hasSpecialty(String)`'s observable behaviour exactly. Do not edit F2 yet.

**Review checkpoint:** Confirm only F1 changed, the new method signature is exactly `public Optional<Specialty> findSpecialty(String name)`, and the diff shows a read-only lookup rather than a storage or API redesign.

---

## Step 4 — Extend the focused Vet domain tests

**Goal:** Add domain-level tests that pin the new lookup behaviour and guard the unchanged helper behaviour around it.
**Implements:** — (verification step, no AC)
**Depends on:** Step 3

**Suggested prompt:**

> Edit F2 only.
>
> Add JUnit 5 + AssertJ tests in the existing style for `findSpecialty(String)`. Cover:
> - case-insensitive match returning the stored specialty object
> - trimmed-input match
> - unmatched input returning `Optional.empty()`
> - no-specialty case returning `Optional.empty()`
> - null and blank inputs returning `Optional.empty()` without throwing
> - read-only behaviour: calling `findSpecialty` does not change count or contents
> - parity checks where `findSpecialty(name).isPresent()` matches `hasSpecialty(name)` for representative matching, non-matching, null, blank, and trimmed names
>
> Keep the scope limited to F2. Do not change production code in this step unless a compilation issue proves Step 3 was incomplete, in which case explain the minimal fix needed.

**Review checkpoint:** Confirm the added tests cover every story branch without broadening into controller, repository, or serialization concerns that are outside this story.

---

## Step 5 — Run the narrowest relevant test target

**Goal:** Execute the focused Vet-domain tests and resolve only issues introduced by this story's change.
**Implements:** — (verification step, no AC)
**Depends on:** Step 4

**Suggested prompt:**

> Run the narrowest existing test target that validates F2 and the surrounding Vet domain helper behaviour.
>
> Summarize the result briefly. If anything fails because of the story change, fix only the files already in scope and explain the root cause before re-running. If an unrelated failure appears, stop after identifying it clearly rather than broadening scope.

**Review checkpoint:** Confirm the focused domain tests pass without unrelated file churn, and that any fix stayed inside the already confirmed file set.

---

## Step 6 — Manual validation against acceptance criteria

**Goal:** Walk every acceptance criterion from the context file and map it to a concrete verification of the implemented domain behaviour.
**Implements:** — (verification step, no AC)
**Depends on:** Step 5

**Suggested prompt:**

> Using .github/story-context-files/vet-specialty-lookup-context-261012-105341.md, produce a verification checklist with one row per acceptance criterion, keyed by AC id.
>
> For each AC, give the exact unit-level observation to confirm:
> - setup needed on a `Vet`
> - method call to make
> - expected `Optional` or helper outcome
> - any invariant that must still hold after the call
>
> Include AC-7, AC-8, and AC-9 explicitly; do not collapse them into the happy-path cases. Do not run anything; give the checklist only.

**Review checkpoint:** Record a pass/fail/unclear result for each AC individually. If any criterion fails, return to the step that introduced the drift instead of patching around it at the end.

---

## Step 7 — Review for convention drift

**Goal:** Check the final changed files for drift from repo instructions and story scope before opening a PR.
**Implements:** — (verification step, no AC)
**Depends on:** Step 6

**Suggested prompt:**

> Review the final diffs in F1 and F2 against .github/copilot-instructions.md and .github/story-context-files/vet-specialty-lookup-context-261012-105341.md.
>
> Flag only meaningful drift:
> - scope drift beyond the Vet domain object and its focused tests
> - behaviour drift from AC-1 through AC-9
> - naming, Optional usage, or helper-pattern mismatches against the existing Vet class
> - any accidental mutation or storage-model change
>
> Do not auto-fix anything in this step. Provide a concise checklist of drift found or state that none remains.

**Review checkpoint:** Confirm there is no remaining drift, or capture the exact deliberate deviation that must be explained in the PR description before merge.

---

## Done criteria

Before opening a PR, confirm:
- `Vet` exposes `public Optional<Specialty> findSpecialty(String name)` and the implementation reads specialties through the existing internal accessor.
- Matching is case-insensitive and trims only outer whitespace, consistent with D-OP018-123-3.
- Null, blank, unmatched, and no-specialty cases all return `Optional.empty()` without throwing.
- Calling `findSpecialty` does not add, remove, reorder, or otherwise modify specialties.
- Existing helper behaviour for `getSpecialties()`, `getNrOfSpecialties()`, `hasSpecialty(String)`, and `addSpecialty(Specialty)` remains unchanged.
- Representative tests demonstrate `findSpecialty(name).isPresent()` matches `hasSpecialty(name)` for matching and non-matching inputs.
- Only the confirmed in-scope files were changed.

---

## --- EXECUTION RECORD (appended by harness) ---
- timestamp: 2026-10-12T10:56:45
- phase: coding
- approved impacted files: ['src/main/java/org/springframework/samples/petclinic/vet/Vet.java', 'src/test/java/org/springframework/samples/petclinic/vet/VetTests.java']
- actually touched: ['src/main/java/org/springframework/samples/petclinic/vet/Vet.java']
- scope: matches approved plan (no additions)
- review status: APPROVED by human at 2026-10-12T10:56:45
