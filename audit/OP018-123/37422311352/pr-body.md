## Summary

- add `Vet.hasSpecialty(String name)` so callers can check for a specialty directly without looping over the collection
- trim the requested name, return `false` for `null` or blank input, and match specialty names case-insensitively via `getSpecialtiesInternal()`
- keep specialty storage and existing `addSpecialty`, `getSpecialties`, and `getNrOfSpecialties` behavior unchanged by making the lookup read-only

## Tests

- cover case-insensitive and trimmed matches, unmatched names, vets with no specialties, `null` and blank input, preserved internal whitespace semantics, and no mutation of the stored specialties
