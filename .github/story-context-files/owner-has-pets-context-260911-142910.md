## What Are We Trying to Achieve
Enhance the owner domain object so calling code can ask directly whether any pets are currently associated with an owner, removing repeated collection-emptiness checks while preserving the current owner-pet behaviour.

Pet-presence query — the boolean convenience method that reports whether the owner currently has any associated pets.

## Current Behaviour
The owner domain object exposes its pets collection, and calling code determines pet presence by reading that collection and checking whether it is empty.

## Expected Behaviour
The owner domain object exposes a `hasPets()` boolean query that returns `true` when the existing pets collection contains at least one pet and `false` when it is empty. The query reads existing state only and does not change how pets are stored, added, listed, or viewed.

## Acceptance Criteria
- AC-1: WHEN the owner domain object has one or more pets in its existing collection, THE `hasPets()` query SHALL return `true`.
- AC-2: WHEN the owner domain object has no pets in its existing collection, THE `hasPets()` query SHALL return `false`.
- AC-3: THE `hasPets()` query SHALL determine its result from the owner domain object's existing pets collection.
- AC-4: THE `hasPets()` query SHALL NOT add, remove, replace, or reorder pets in the owner's existing collection.
- AC-5: WHEN a pet is added through the owner domain object's existing behaviour, THE `hasPets()` query SHALL reflect the updated collection without any additional mutation step.
- AC-6: WHEN calling code reads the pets collection after invoking `hasPets()`, THE collection contents SHALL match the contents present immediately before the query call.

## Edge Cases
- Empty pets collection returns `false`
- Single-pet collection returns `true`
- Multiple-pet collection returns `true`
- A rejected null-pet add remains a no-op, so the subsequent `hasPets()` result still reflects the unchanged collection
- Duplicate-pet rejection remains unchanged, so repeated adds do not change the `hasPets()` result beyond the first successful add

## Constraints
- Generic Spring Boot defaults apply because no repo-wide Copilot instructions file is present
- Constructor injection remains the backend default for surrounding application code
- Standard Spring validation and error-handling conventions remain unchanged because this story does not add an endpoint
- JUnit 5 and Mockito remain the default test stack for any supporting tests
- Scope is limited to the owner domain object and must not change persistence mapping or published API contracts

## Out of Scope
- Any UI, template, controller, or endpoint change
- Any change to how pets are added to an owner
- Any change to how pets are stored or mapped for persistence
- Any new request parameter, response field, or view behaviour

## Clarifications Needed
None.

## Assumptions
None.

## Story Quality Score
| Dimension | Score | Basis |
|---|---|---|
| Clarity | 20/20 | The story and expected behaviour define the query outcome and scope directly |
| Testability | 20/20 | Every acceptance criterion has an observable collection state or boolean outcome |
| Traceability | 20/20 | Every criterion traces to the story text or explicit in-scope and out-of-scope statements |
| Atomicity | 20/20 | Each acceptance criterion describes one behaviour and one response |
| Completeness | 19/20 | The story is tightly scoped and complete; caller-side repeated-read examples are implicit rather than explicit |
| Edge coverage | 18/20 | Empty, single, multiple, null-add, and duplicate-add scenarios are covered; no broader boundary conditions apply to this read-only query |
| **Total** | **117/120** | |

## Design trigger
**DESIGN REQUIRED: NO**
Confined to an existing domain-object convenience query with no contract change, no new dependency, and the same owner-side behaviour pattern already used for pet lookups.

## Feasibility
**VERDICT: GO**
Basis: the owner domain object and its existing pets collection already exist, and the story adds a read-only helper aligned with the current in-memory owner behaviour.
