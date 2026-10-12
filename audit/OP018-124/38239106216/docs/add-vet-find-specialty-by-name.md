# Add a method to find a Vet's specialty by name

## Story

As a developer, I want to get a Vet's `Specialty` object by its name, so calling code can use the specialty (for example its id) directly instead of looping over the specialty list.

## Change

Add a convenience method `findSpecialty(String name)` to the `Vet` entity class.

- Returns the matching `Specialty` wrapped in `Optional` when the vet has a specialty whose name matches the input case-insensitively.
- Returns `Optional.empty()` when no specialty matches.
- Returns `Optional.empty()` when the vet has no specialties.
- Returns `Optional.empty()` when the input name is `null` or blank.
- Reads the existing specialties collection through `getSpecialtiesInternal()` only.
- Does not add, remove, or modify specialties.

## Acceptance criteria

1. When a vet has specialty `"surgery"`, `findSpecialty("surgery")` and `findSpecialty("Surgery")` both return that `Specialty`.
2. When a vet has specialty `"surgery"`, `findSpecialty("dentistry")` returns `Optional.empty()`.
3. When a vet has no specialties, `findSpecialty("surgery")` returns `Optional.empty()`.
4. `findSpecialty(null)` and `findSpecialty(" ")` return `Optional.empty()` and do not throw.
5. `findSpecialty` does not add, remove, or modify specialties. Existing behaviour of `getSpecialties`, `getNrOfSpecialties`, `hasSpecialty`, and `addSpecialty` is unchanged.
6. For every name, `findSpecialty(name).isPresent()` equals `hasSpecialty(name)`.

## Scope

- **In scope:** the `Vet` entity class only.
- **Out of scope:** UI, templates, controllers, repositories, and database changes.
