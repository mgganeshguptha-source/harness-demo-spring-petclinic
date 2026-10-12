## Summary

- add `Vet.findSpecialty(String name)` so callers can retrieve the matching `Specialty` directly as an `Optional`
- implement the lookup against `getSpecialtiesInternal()` with case-insensitive name matching, returning `Optional.empty()` for null, blank, missing, or unmatched names
- keep specialty storage and mutation behavior unchanged, and align `hasSpecialty(String name)` with the new lookup by delegating to `findSpecialty`

## Coverage

- add `VetTests` coverage for matching and non-matching lookups, vets with no specialties, null/blank input, non-mutating behavior, and parity between `findSpecialty(...).isPresent()` and `hasSpecialty(...)`
