# Add a method to check whether a Vet has a given specialty

## Story

As a developer, I want to ask a `Vet` whether it has a specialty with a given name, so that calling code can check this directly instead of looping over the specialty list.

## Change

Add a convenience method `hasSpecialty(String name)` to the `Vet` entity class.

- Returns `true` when any specialty name matches the supplied name case-insensitively.
- Trims leading and trailing whitespace from the input name before comparison.
- Returns `false` when no specialty matches, when the vet has no specialties, or when the input is `null` or blank after trimming.
- Reads the existing specialties through `getSpecialtiesInternal()`.
- Does not add, remove, or modify specialties.
- Leaves existing `getSpecialties`, `getNrOfSpecialties`, and `addSpecialty` behavior unchanged.

## Acceptance criteria

1. For a vet with specialty `"surgery"`, `hasSpecialty("surgery")` returns `true`.
2. For a vet with specialty `"surgery"`, `hasSpecialty("Surgery")` returns `true`.
3. For a vet with specialty `"surgery"`, `hasSpecialty(" surgery ")` returns `true`.
4. For a vet with specialty `"surgery"`, `hasSpecialty("dentistry")` returns `false`.
5. For a vet with no specialties, `hasSpecialty("surgery")` returns `false`.
6. `hasSpecialty(null)` and `hasSpecialty(" ")` return `false` and do not throw.
7. `hasSpecialty` does not add, remove, or modify specialties.
8. Existing behavior for `getSpecialties`, `getNrOfSpecialties`, and `addSpecialty` is unchanged.

## Scope

- **In scope:** the `Vet` entity class only.
- **Out of scope:** any UI, template, controller, repository, database, or specialty mapping change.

## Clarifications captured

- `getSpecialtiesInternal()` never returns `null`; it creates an empty set if needed.
- `specialties` remain `FetchType.EAGER`; there is no lazy-loading or transaction concern for `hasSpecialty`, and the mapping must not change.
- Input names are trimmed only at the ends; internal whitespace is preserved, so `"sur gery"` does not match `"surgery"`.
