# context.md Template — All Story Types & Development Layers

---

## What Are We Trying to Achieve
Add a direct specialty lookup to the vet domain object so calling code can retrieve the matching Specialty itself, rather than manually looping through the specialty collection when it needs the object for follow-on use such as reading its id. **Vet specialty lookup** in this story means the new Optional-returning name-based lookup on a vet.

## Current Behaviour
The vet domain object already supports listing specialties, counting them, checking whether a specialty name matches case-insensitively, and adding specialties, but it does not expose a lookup that returns the matching Specialty object. Callers that need the Specialty itself must iterate over the specialty collection manually.

## Expected Behaviour
The vet domain object exposes a public Optional-returning specialty lookup by name. The lookup reads the existing specialties through the current internal specialty accessor, matches case-insensitively, trims outer whitespace from the input per D-OP018-123-3, returns Optional.empty() for null, blank, unmatched, or no-specialty cases, and leaves specialty storage plus existing helper behaviour unchanged.

## Acceptance Criteria
- AC-1: WHEN the vet has a specialty whose name matches the supplied name case-insensitively, THEN THE vet specialty lookup SHALL return an Optional containing that stored specialty.
- AC-2: WHEN the supplied name contains leading or trailing whitespace around a non-blank value, THEN THE vet specialty lookup SHALL compare using the trimmed input without changing internal whitespace, per D-OP018-123-3.
- AC-3: IF the supplied name is null, THEN THE vet specialty lookup SHALL return Optional.empty() without throwing.
- AC-4: IF the supplied name is blank after trimming, THEN THE vet specialty lookup SHALL return Optional.empty() without throwing.
- AC-5: IF no stored specialty name matches the supplied name, THEN THE vet specialty lookup SHALL return Optional.empty().
- AC-6: IF the vet has no specialties, THEN THE vet specialty lookup SHALL return Optional.empty().
- AC-7: THE vet specialty lookup SHALL be read-only over the existing specialty set obtained from the current internal accessor.
- AC-8: THE existing specialty helpers SHALL preserve their current behaviour after the new lookup is added.
- AC-9: THE presence result of the vet specialty lookup SHALL equal the existing specialty-match helper for every supplied name.

## Edge Cases
- null input returns Optional.empty() and does not throw
- blank input, space-only input, and tab-only input return Optional.empty()
- input with outer whitespace, such as `" surgery "`, matches the same specialty as `"surgery"`
- input with changed internal whitespace, such as `"sur gery"`, does not match `"surgery"`, per D-OP018-123-3
- vet with an empty specialty set returns Optional.empty() without creating any externally visible change in specialty count or contents
- mixed-case input returns the stored Specialty object rather than a derived copy

## Constraints
- Backend-only Java 17 / Spring Boot / JPA domain-model change; no frontend or HTTP contract work
- Scope is limited to the vet domain object; controllers, repositories, database, templates, and API endpoints remain untouched
- Preserve the current non-null internal specialty accessor behaviour per D-OP018-123-1
- Preserve the current eager specialty loading and specialty storage model per D-OP018-123-2
- Follow the existing JUnit 5 plus AssertJ domain-test style if tests are updated

## Out of Scope
- UI, template, or page-flow changes
- Controller, endpoint, or serialized response changes
- Repository, query, database, or schema changes
- Changing how specialties are stored, added, or loaded
- Adding new behaviour beyond direct name-based Specialty lookup

## Story Quality Score
| Dimension | Score | Basis |
|---|---|---|
| Clarity | 20/20 | Input, match, empty-result, and non-mutation behaviour are concrete |
| Testability | 20/20 | Every AC has an observable Optional or helper-behaviour outcome |
| Traceability | 20/20 | Criteria trace to the story and approved decisions D-OP018-123-1/2/3 |
| Atomicity | 20/20 | Each AC states one behaviour with a single response |
| Completeness | 19/20 | Scope, invariants, and empty-input handling are explicit; duplicate-name behaviour is intentionally unspecified |
| Edge coverage | 19/20 | Null, blank, whitespace, empty-set, and casing edges are covered; duplicate-name data is out of scope |
| **Total** | **118/120** | |

## Design trigger
**DESIGN REQUIRED: NO**
Follows the existing vet-domain helper pattern established by the current specialty helper methods; no service boundary, persistence-boundary, or external contract change is introduced.

## Feasibility
**VERDICT: GO**
Basis: the vet and Specialty domain types already exist, the current specialty helpers and approved decisions already define the matching and storage invariants, and the story is intentionally limited to a local domain-model enhancement with no missing dependency.
