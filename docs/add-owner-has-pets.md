# Add a method to check whether an Owner has any pets

## Story

As a developer, I want to ask an `Owner` whether it currently has any pets, so that calling code can check this directly instead of fetching the pet list and testing its size.

## Change

Add a convenience method `hasPets()` to the `Owner` entity class.

- Returns `true` when the owner has one or more pets.
- Returns `false` when the owner has no pets.
- Reads the existing pets collection only.
- Does not add, remove, or modify pets.

## Acceptance criteria

1. When an owner has one or more pets, `hasPets()` returns `true`.
2. When an owner has no pets, `hasPets()` returns `false`.
3. `hasPets()` does not add, remove, or modify any pet.
4. Existing owner and pet functionality, including add pet, list pets, and view owner, is unchanged.

## Scope

- **In scope:** the `Owner` entity class only.
- **Out of scope:** any UI, template, controller, or endpoint change; any change to how pets are added or stored.
