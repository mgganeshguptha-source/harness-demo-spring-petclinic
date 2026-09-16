## What Are We Trying to Achieve
Add a direct pet-presence query to the owner domain object so calling code can ask whether an owner currently has any pets without reading the pet collection and checking its size. This keeps the behaviour local to the domain model, removes repeated emptiness checks from callers, and preserves the current pet-association model.

New term introduced by this story:
- **Pet-presence query** — a boolean convenience check that answers whether an owner currently has at least one associated pet.

## Current Behaviour
Calling code reads the owner's pet collection and checks whether it is empty when it needs to know whether that owner currently has any pets.

## Expected Behaviour
The owner domain object exposes a boolean pet-presence query that returns true when the existing pet collection contains one or more pets and false when it contains none. The query reads only the current collection state and does not change how pets are stored, added, or exposed elsewhere.

## Acceptance Criteria
- AC-1: WHEN the owner's existing pet collection contains one or more pets, THE pet-presence query SHALL return true.
- AC-2: WHEN the owner's existing pet collection contains no pets, THE pet-presence query SHALL return false.
- AC-3: THE pet-presence query SHALL read only the owner's existing pet collection state.
- AC-4: THE pet-presence query SHALL NOT add, remove, or modify any pet in the owner's collection.
- AC-5: THE owner and pet behaviours for adding pets, listing pets, and viewing owner details SHALL remain unchanged after the pet-presence query is introduced.

## Edge Cases
- A newly created owner with no associated pets returns false.
- An owner with one newly added pet that has not been persisted yet returns true.
- Repeated calls against the same owner state return the same boolean result.
- Calling the pet-presence query after a rejected duplicate add attempt reflects the unchanged collection state.

## Constraints
- Backend defaults only — .github/copilot-instructions.md is not present in this repository, so generic Spring Boot constraints apply.
- Constructor injection remains the backend default for any collaborating classes outside this story's scope.
- Standard error response format remains unchanged because this story does not add or alter an API surface.
- Jakarta Validation conventions remain unchanged because this story does not add new external inputs.
- JUnit 5 plus Mockito remains the default testing approach for any supporting tests.
- Existing owner-to-pet persistence mapping and pet-association behaviour must remain unchanged.

## Out of Scope
- Any UI or template change
- Any controller or endpoint change
- Any change to how pets are added to an owner
- Any change to how pets are stored or persisted
- Any repository or service-layer behaviour outside the owner domain object

## Story Quality Score
| Dimension | Score | Basis |
|---|---|---|
| Clarity | 20/20 | The story and criteria use concrete true-or-false behaviour with no vague terms |
| Testability | 20/20 | Every acceptance criterion has an observable boolean or unchanged-behaviour outcome |
| Traceability | 20/20 | Every criterion traces directly to the story text |
| Atomicity | 20/20 | Each criterion states one behaviour and one response |
| Completeness | 19/20 | The story fully defines the new query but leaves regression coverage breadth to the implementation phase |
| Edge coverage | 19/20 | Empty, non-empty, repeated-call, and duplicate-add scenarios are named, but caller-side migration scenarios are intentionally out of scope |
| **Total** | **118/120** | |

## Design trigger
**DESIGN REQUIRED: NO**
No trigger fired; this is a local domain-model enhancement that follows the existing owner-to-pet helper pattern and does not add a new dependency or change a published contract.

## Feasibility
**VERDICT: GO**
Basis: the repository already contains the owner domain object, its initialized pet collection, and existing pet-association tests, so the boolean pet-presence query can be added locally without introducing a new dependency or violating the current stack conventions.
