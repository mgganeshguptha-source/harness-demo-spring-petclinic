## What Are We Trying to Achieve
Enhance the owner domain model with a direct boolean pet-presence query so calling code can ask whether an owner currently has any pets without fetching the pets collection and testing its size itself. **Pet-presence query** means a read-only boolean check that answers whether the owner currently has at least one pet.

## Current Behaviour
The owner domain model exposes its pets collection, and calling code determines whether any pets exist by reading that collection and checking its size or emptiness directly.

## Expected Behaviour
The owner domain model exposes a direct boolean pet-presence query that returns true when at least one pet is present and false when no pets are present. The query reads the existing pets collection only and does not change pet storage, pet addition, or owner-related views.

## Acceptance Criteria
- AC-1: WHEN the owner has one or more pets, THE owner domain model SHALL report that pets are present.
- AC-2: WHEN the owner has no pets, THE owner domain model SHALL report that no pets are present.
- AC-3: THE owner domain model SHALL determine pet presence by reading the existing pets collection only.
- AC-4: THE owner domain model SHALL NOT add, remove, or modify any pet while answering whether pets are present.
- AC-5: WHEN existing owner and pet behaviour is exercised after this change, THE system SHALL preserve current add-pet, list-pets, and owner-view behaviour.

## Edge Cases
- Empty pets collection: the pet-presence query returns false.
- Single-pet owner: the pet-presence query returns true.
- Multiple-pet owner: the pet-presence query returns true.
- Repeated checks before and after a pet is added: each result reflects the current collection state without mutating the collection.
- Existing callers that still inspect the pets collection directly: they continue to observe the same collection contents as before.

## Constraints
- Generic backend defaults apply because .github/copilot-instructions.md is not present in this repository.
- Constructor injection remains the default project convention for any supporting code outside this story's scope.
- Standard error-response conventions remain unchanged because this story does not alter any endpoint behaviour.
- Jakarta Validation and persistence mappings on the owner domain model remain unchanged.
- JUnit 5 and Mockito remain the default test conventions for any automated coverage added for this change.

## Out of Scope
- Any UI, template, controller, or endpoint change.
- Any change to how pets are added to an owner.
- Any change to how pets are stored or persisted.
- Any change to owner search, listing, or detail-page behaviour beyond preserving current results.

## Story Quality Score
| Dimension | Score | Basis |
|---|---|---|
| Clarity | 20/20 | Goal, behaviour, and exclusions are concrete with no vague qualifiers |
| Testability | 20/20 | Every acceptance criterion has an observable boolean or behaviour-preservation outcome |
| Traceability | 20/20 | Every criterion traces directly to the story text or explicit scope statement |
| Atomicity | 20/20 | Each acceptance criterion covers one behaviour in one sentence |
| Completeness | 19/20 | Functional scope is complete, but constraints rely on generic defaults because repo-wide instructions are absent |
| Edge coverage | 20/20 | Empty, single-item, multi-item, repeat-check, and non-mutation scenarios are all named |
| **Total** | **119/120** | |

## Design trigger
**DESIGN REQUIRED: NO**
Matches the existing domain-helper pattern already used for owner-to-pet lookups; no new dependency, published contract, or data-ownership change is introduced.

## Feasibility
**VERDICT: GO**
Basis: the repository already contains the owner domain model, pet collection behaviour, and owner-focused unit tests, so this change fits the established in-process domain-model pattern.
