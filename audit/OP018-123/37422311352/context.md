## What Are We Trying to Achieve
Add a direct specialty lookup on the vet domain object so calling code can ask whether a vet has a named specialty without iterating the specialty collection itself. The lookup must treat case differences as equal, trim leading and trailing whitespace from the caller input before matching, return false for null or blank input and for vets with no specialties, and preserve the existing specialty storage and add/read/count behaviour. New term introduced by this story: specialty lookup — the boolean check that answers whether the current vet has a specialty whose stored name matches a caller-supplied name after trimming the input and comparing case-insensitively.

## Current Behaviour
The vet domain object exposes existing specialty read, count, and add behaviour, but callers must currently loop through the vet's specialties themselves to answer whether a named specialty is present.

## Expected Behaviour
The vet domain object exposes a boolean specialty lookup that returns true when any existing specialty name matches the supplied name case-insensitively after trimming leading and trailing whitespace. It returns false when no stored specialty matches, when the vet has no specialties, and when the supplied name is null or blank after trimming. The lookup does not add, remove, reorder, or modify stored specialties, and the current specialty read, count, and add behaviour stays unchanged.

## Acceptance Criteria
- AC-1: WHEN the caller supplies a specialty name that matches any stored specialty name case-insensitively, THE vet specialty lookup SHALL return true.
- AC-2: WHEN the caller supplies a specialty name with leading or trailing whitespace, THE vet specialty lookup SHALL trim the input before matching.
- AC-3: IF the trimmed specialty name does not match any stored specialty name, THEN THE vet specialty lookup SHALL return false.
- AC-4: IF the vet has no specialties, THEN THE vet specialty lookup SHALL return false.
- AC-5: IF the supplied specialty name is null, THEN THE vet specialty lookup SHALL return false.
- AC-6: IF the supplied specialty name is blank after trimming, THEN THE vet specialty lookup SHALL return false.
- AC-7: WHEN the vet specialty lookup is invoked, THE stored specialty collection SHALL remain unchanged in membership and stored names.
- AC-8: WHEN a specialty is added through the existing specialty-addition behaviour, THE vet specialty lookup SHALL evaluate that added specialty without requiring any new storage path.
- AC-9: THE vet specialty lookup SHALL NOT normalize or alter whitespace inside stored or supplied specialty names before comparing them.
- AC-10: THE vet specialty lookup SHALL NOT change the observable behaviour of the existing specialty read, count, or add operations.

## Edge Cases
- null input name: return false
- blank input after trimming, including a whitespace-only string: return false
- leading or trailing whitespace around a matching name: trim the input and match successfully
- internal whitespace inside the supplied name: keep it unchanged, so a value like `sur gery` does not match `surgery`
- no specialties on the vet: return false
- specialty present with different letter case: return true
- unmatched specialty name: return false

## Constraints
- Repository-wide .github/copilot-instructions.md is absent, so generic Spring Boot defaults apply for this context
- Java 17 codebase
- Spring Boot application with Spring Data JPA domain entities
- Preserve existing domain model storage and mapping behaviour
- JUnit 5 and Mockito remain the default test conventions for any generated tests
- No API contract, controller, repository, UI, or database schema change in this story

## Out of Scope
- Any UI, template, controller, or endpoint change
- Any repository or database mapping change
- Any change to how specialties are stored, loaded, or added
- Any new search across pets or owner data

## Clarifications Needed
None.

## Assumptions
None.

## Story Quality Score
| Dimension | Score | Basis |
|---|---|---|
| Clarity | 20/20 | Match rule, trimming rule, negative cases, and preservation rules are concrete |
| Testability | 20/20 | Every AC has an observable boolean or state-preservation outcome |
| Traceability | 20/20 | Every AC traces to the story text or its pre-answered clarifications |
| Atomicity | 20/20 | Each AC states one behaviour in one sentence |
| Completeness | 19/20 | Functional behaviour is complete; repo-wide coding constraints rely on generic defaults because .github/copilot-instructions.md is missing |
| Edge coverage | 20/20 | Null, blank, whitespace, empty collection, mismatch, and case-variation scenarios are explicit |
| **Total** | **119/120** | |

## Design trigger
**DESIGN REQUIRED: NO**
Follows the existing domain-model helper pattern beside the current specialty read, count, and add behaviour. No service boundary, published contract, persistence strategy, or new structural pattern changes.

## Feasibility
**VERDICT: GO**
Basis: the repository already contains the vet domain entity, the specialty model, and the existing specialty collection behaviour this story builds on. The story adds one local boolean lookup with no missing dependency, contract conflict, scope mismatch, or stack incompatibility.
