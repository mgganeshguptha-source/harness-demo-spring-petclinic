# Add a method to check whether a Vet has a given specialty

## Story

As a developer, I want to ask a `Vet` whether it has a specialty with a given name, so calling code can check directly instead of looping over the specialty list.

## Change

Add a convenience method `hasSpecialty(String name)` to the `Vet` entity class.

- Returns `true` when any of the vet's specialties has the given name.
- Compares specialty names case-insensitively.
- Trims leading and trailing whitespace from the input before comparing.
- Returns `false` when no specialty matches.
- Returns `false` when the vet has no specialties.
- Returns `false` when `name` is `null` or blank after trimming.
- Reads specialties through `getSpecialtiesInternal()`.
- Does not add, remove, or modify specialties.
- Does not change how specialties are stored or added.

## Acceptance criteria

1. For a vet with specialty `"surgery"`, `hasSpecialty("surgery")` and `hasSpecialty("Surgery")` both return `true`.
2. For a vet with specialty `"surgery"`, `hasSpecialty(" surgery ")` and `hasSpecialty(" Surgery")` return `true`.
3. For a vet with specialty `"surgery"`, `hasSpecialty("dentistry")` returns `false`.
4. For a vet with no specialties, `hasSpecialty("surgery")` returns `false`.
5. `hasSpecialty(null)` and `hasSpecialty(" ")` return `false` and do not throw.
6. Whitespace inside the name is preserved, so `"sur gery"` does not match `"surgery"`.
7. `hasSpecialty` does not add, remove, or modify specialties.
8. Existing behavior for `getSpecialties`, `getNrOfSpecialties`, and `addSpecialty` is unchanged.

## Scope

- **In scope:** the `Vet` class only.
- **Out of scope:** UI, templates, controllers, repositories, database, and any change to specialty mapping or persistence behavior.

## Clarifications

- `getSpecialtiesInternal()` never returns `null`; it creates an empty set if needed.
- Specialties are mapped with `FetchType.EAGER`, so there is no lazy-loading or transaction concern for this method.
- Stored specialty names are compared as they are and are not modified.
- Do not change the specialty mapping.
