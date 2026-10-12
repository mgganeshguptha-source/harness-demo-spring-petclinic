## What Are We Trying to Achieve
Add a direct specialty-check method to the vet model so calling code can ask whether a vet has a named specialty without iterating the specialty list itself. In this story, **specialty-check method** means the new boolean query on the vet model that answers whether any current specialty name matches a supplied name after outer-whitespace trimming and case-insensitive comparison.

## Current Behaviour
The vet model exposes specialty listing, specialty counting, and specialty addition, but it does not expose a direct boolean query by specialty name, so calling code must iterate the current specialties to answer that question.

## Expected Behaviour
The vet model gains a public boolean specialty-check method that returns true when any current specialty name matches the supplied name after trimming leading and trailing whitespace and comparing case-insensitively. It returns false when no specialty matches, when the vet has no specialties, or when the supplied name is null or blank after trimming. It reads the current specialties through the existing internal specialty-set accessor and does not change how specialties are stored, loaded, or added.

## Acceptance Criteria
- AC-1: WHEN the vet has a specialty whose stored name equals the trimmed input name ignoring case, THE vet model SHALL return true from the specialty-check method.
- AC-2: IF no stored specialty name equals the trimmed input name ignoring case, THEN THE vet model SHALL return false from the specialty-check method.
- AC-3: IF the vet has no specialties, THEN THE vet model SHALL return false from the specialty-check method.
- AC-4: IF the input name is null or blank after trimming, THEN THE vet model SHALL return false from the specialty-check method without raising an exception.
- AC-5: WHEN the input name has leading or trailing whitespace, THE vet model SHALL trim only the outer whitespace before comparing specialty names.
- AC-6: THE vet model SHALL read specialties through the existing internal specialty-set accessor when evaluating the specialty-check method.
- AC-7: WHEN the specialty-check method is called, THE vet model SHALL leave the specialty collection unchanged.
- AC-8: WHEN the specialty-check method is added, THE existing specialty listing, specialty counting, and specialty addition behaviours SHALL keep their current results.

## Edge Cases
- Input name is null: return false and leave the specialty set unchanged
- Input name is blank after trimming: return false and leave the specialty set unchanged
- Input name has leading or trailing whitespace: trim only the outer whitespace before matching
- Input name contains internal whitespace: keep the internal whitespace as typed, so `sur gery` does not match `surgery`
- Vet has an empty specialty set: return false
- A specialty added to the current in-memory set before persistence is checked like any other current specialty because the method reads the existing internal set

## Constraints
- Preserve the existing JPA entity pattern and keep the specialty mapping unchanged
- Reuse the existing internal specialty-set accessor instead of introducing a new storage path
- Keep specialty storage and addition semantics unchanged
- Follow the existing Java 17 and Spring model conventions already used in the domain layer
- Use JUnit 5 test coverage in the existing model-test style

## Out of Scope
- Any UI, template, controller, or endpoint change
- Any repository, database, or mapping change for specialties
- Any change to how specialties are added, stored, sorted, or exposed through the existing specialty list
- Any change outside the vet model

## Story Quality Score
| Dimension | Score | Basis |
|---|---|---|
| Clarity | 20/20 | Match rule, trimming rule, and false-return cases are concrete |
| Testability | 20/20 | Every acceptance criterion has an observable boolean or collection-state outcome |
| Traceability | 20/20 | Every criterion maps to the story or its pre-answered clarifications |
| Atomicity | 20/20 | Each criterion states one behaviour in one sentence |
| Completeness | 19/20 | Behaviour is fully specified for caller-visible outcomes; persistence-layer invariants stay intentionally out of scope |
| Edge coverage | 19/20 | Null, blank, whitespace, empty-set, and current in-memory state cases are covered; invalid stored specialty data is not part of the story |
| **Total** | **118/120** | |

## Design trigger
**DESIGN REQUIRED: NO**
Follows the same domain-model convenience-method precedent already used by the owner model's direct boolean helper methods, with no new dependency, contract change, or new architectural pattern.

## Feasibility
**VERDICT: GO**
Basis: the vet model, specialty model, and internal specialty-set accessor already exist, the specialty association is already eagerly available, and the story adds only a local boolean helper without changing persistence, APIs, or cross-service boundaries.
