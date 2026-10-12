## Summary

- add `Vet.hasSpecialty(String name)` so callers can check for a specialty directly on the entity instead of iterating its specialties
- make the lookup return `true` for case-insensitive matches after trimming leading and trailing whitespace, and return `false` for `null`, blank, missing, or non-matching names
- preserve existing specialty storage and helper behavior by reading through `getSpecialtiesInternal()` without modifying the specialty collection

## Tests

- extend `VetTests` to cover case-insensitive matches, trimmed input, non-matches, vets with no specialties, `null`/blank input, and the guarantee that `hasSpecialty` does not change existing specialties or helper behavior
