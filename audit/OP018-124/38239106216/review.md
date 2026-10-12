VERDICT: PASS

[NOTE]: New `findSpecialty(String)` method correctly implements all nine acceptance criteria: null input returns empty Optional; blank/trimmed input returns empty Optional; case-insensitive match returns the stored specialty object; unmatched or no-specialty cases return empty Optional. Defensive null check on `specialty.getName()` prevents NPE. Method is read-only (no mutations via stream operations). Refactored `hasSpecialty()` to delegate to `findSpecialty().isPresent()` maintains backward compatibility and satisfies AC-9 parity. Import of `java.util.Optional` is correct and necessary.

[NOTE]: Edge cases all handled correctly: null input (explicit check); blank after trim (isEmpty check); empty specialty set (empty stream → empty Optional); null specialty name (short-circuit in filter predicate). Trimming applied only to outer whitespace as required.

[NOTE]: Code follows project conventions: method naming in camelCase, proper use of Optional type, stream API used appropriately, no SQL/JPQL injection risk, no secrets exposed, no error-handling gaps.
