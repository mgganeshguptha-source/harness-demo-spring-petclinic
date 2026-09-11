## What Are We Trying to Achieve
Add a direct pet-presence check to the owner domain model so calling code can ask whether any pets exist without retrieving the collection and checking its size manually.

New terms introduced by this story:
- Pet-presence check: the requested boolean convenience method, named `hasPets()`, that answers whether the owner currently has at least one pet.

## Current Behaviour
The owner domain model exposes the pets collection, and calling code determines whether pets exist by reading that collection and checking whether it is empty.

## Expected Behaviour
The owner domain model exposes a pet-presence check that returns true when the existing pets collection contains one or more pets and false when it contains none. The check reads the current collection only and leaves pet storage, pet addition, and higher-layer owner and pet behaviour unchanged.

## Acceptance Criteria
- AC-1: WHEN the owner has one or more pets in its existing collection, THE owner domain model SHALL return `true` from the pet-presence check.
- AC-2: WHEN the owner has no pets in its existing collection, THE owner domain model SHALL return `false` from the pet-presence check.
- AC-3: WHEN the pet-presence check is invoked, THE owner domain model SHALL NOT add, remove, or modify any pet in the existing collection.
- AC-4: WHEN the existing pet-addition behaviour adds the first pet to an owner, THE pet-presence check SHALL return `true` on the next call.

## Edge Cases
- Newly created owner with an empty pets collection returns `false`
- Owner with exactly one pet returns `true`
- Owner with multiple pets returns `true`
- Owner with a newly added pet that is not yet persisted still returns `true`
- Repeated calls without changes to the pets collection return the same boolean value

## Constraints
- Generic Spring Boot defaults are being used because `.github/copilot-instructions.md` is absent in this repository
- Java 17 and Spring Boot 4.1.0 conventions remain in force
- Existing owner and pet persistence behaviour must remain unchanged
- No controller, template, endpoint, or API contract changes are permitted
- JUnit 5 test conventions apply to any added or updated automated tests

## Out of Scope
- Any UI, template, controller, or endpoint change
- Any change to how pets are added, stored, or ordered
- Any new validation, authorization, or logging behaviour
- Any new derived field, counter, or persistence column for pet presence

## Story Quality Score
| Dimension | Score | Basis |
|---|---|---|
| Clarity | 20/20 | The requested behaviour and non-mutating constraint are concrete and specific |
| Testability | 20/20 | Every acceptance criterion maps to an observable boolean or collection-state outcome |
| Traceability | 20/20 | All acceptance criteria trace directly to the story text |
| Atomicity | 20/20 | Each acceptance criterion describes one behaviour and one response |
| Completeness | 18/20 | The story is fully specified for the entity change, but repository-specific Copilot constraints are unavailable |
| Edge coverage | 18/20 | Empty, single, multiple, and newly added pet states are covered, but no explicit business rule was supplied beyond collection presence |
| **Total** | **116/120** | |

## Design trigger
**DESIGN REQUIRED: NO**
Follows the existing owner-domain convenience-method pattern in the same model area and does not cross a service boundary, change a published contract, or introduce a new architectural pattern.

## Feasibility
**VERDICT: GO**
Basis: the repository already contains the owner entity, its pets collection, and owner-focused domain tests, so this story is a local domain-model enhancement with no missing dependency or stack conflict.
