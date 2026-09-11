## What Are We Trying to Achieve
Add a boolean convenience check to the owner domain entity so calling code can ask directly whether an owner currently has any pets, instead of reading the pet collection and inspecting its size. This improves readability at the domain-model level while preserving the existing owner and pet storage behaviour.

## Current Behaviour
Calling code reads the owner domain entity's existing pet collection and determines pet presence by checking whether that collection is empty.

## Expected Behaviour
The owner domain entity exposes a boolean convenience check named hasPets() that returns true when the existing pet collection contains one or more pets and false when it contains none. The check reads the current collection state only and does not change how pets are stored, added, listed, or looked up.

## Acceptance Criteria
- AC-1: WHEN the owner domain entity's existing pet collection contains one or more pets, THE boolean pet-presence check SHALL return true.
- AC-2: WHEN the owner domain entity's existing pet collection contains no pets, THE boolean pet-presence check SHALL return false.
- AC-3: WHEN the boolean pet-presence check is invoked, THE owner domain entity SHALL read the existing pet collection without adding, removing, or modifying any pet.
- AC-4: WHEN a pet is added through the existing owner-domain add path, THE boolean pet-presence check SHALL reflect the updated collection state.
- AC-5: THE boolean pet-presence check SHALL NOT change the observable behaviour of existing pet listing, pet lookup, or owner-view flows.

## Edge Cases
- An empty pet collection returns false.
- A single pet in the existing collection returns true.
- Multiple pets in the existing collection return true.
- A newly added pet already present in the existing collection counts as present even before any separate persistence round-trip.
- Repeated calls to the boolean pet-presence check return the same result until the pet collection changes.

## Constraints
- Generic backend defaults apply because .github/copilot-instructions.md is not present in this repository.
- Java 17 and Spring Boot project conventions remain unchanged.
- Existing owner persistence mapping and pet collection storage semantics must remain unchanged.
- Any added verification should follow the repository's JUnit 5 test stack.
- No controller, endpoint, template, UI, repository, schema, or storage-behaviour changes are allowed.

## Out of Scope
- Any UI, template, controller, or endpoint change.
- Any change to how pets are added, stored, loaded, or ordered.
- Any new repository or service behaviour built on top of the convenience check.
- Any rename, removal, or contract change to existing pet-access behaviour.

## Story Quality Score
| Dimension | Score | Basis |
|---|---|---|
| Clarity | 20/20 | The story states the new behaviour, in-scope class, and unchanged areas concretely |
| Testability | 20/20 | Every acceptance criterion maps to observable true or false outcomes or unchanged collection behaviour |
| Traceability | 20/20 | Every criterion traces to the story's expected behaviour, scope, or stated acceptance criteria |
| Atomicity | 20/20 | Each acceptance criterion expresses one independently testable behaviour |
| Completeness | 19/20 | The story is complete for the entity change, but it does not state an explicit test expectation for repeated invocation |
| Edge coverage | 18/20 | Empty, single, and multiple-pet states are covered, but the story itself does not call out persistence-reload boundaries |
| **Total** | **117/120** | |

## Design trigger
**DESIGN REQUIRED: NO**
No trigger fired: this is a self-contained in-process domain-model helper addition, following the existing helper-method pattern already present on the owner entity for pet lookup and visit attachment.

## Feasibility
**VERDICT: GO**
Basis: this repository already contains the owner domain entity, its pet collection, and existing owner-domain tests, so the boolean convenience check can be added without crossing service boundaries, changing published contracts, or altering storage behaviour.
