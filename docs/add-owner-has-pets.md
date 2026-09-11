# Add a method to check whether an Owner has any pets

## Story

As a developer, I want to ask an `Owner` whether it currently has any pets, so that calling code can check this directly instead of fetching the pet list and testing its size.

Today the `Owner` entity exposes its list of pets, and callers check emptiness themselves. This change adds a convenience method on the `Owner` class that answers the question directly.

## Change

Add `hasPets()` to the `Owner` entity class.

- Return `true` when the owner has one or more pets.
- Return `false` when the owner has no pets.
- Read the existing pets collection only.
- Do not change how pets are stored, added, removed, or exposed.

## Acceptance criteria

1. When an owner has one or more pets, `hasPets()` returns `true`.
2. When an owner has no pets, `hasPets()` returns `false`.
3. `hasPets()` does not add, remove, or modify any pet.
4. Existing owner and pet functionality, including add pet, list pets, and view owner, is unchanged.

## Scope

**In scope:** `Owner` entity class only.

**Out of scope:** UI, template, controller, endpoint, and any change to how pets are added or stored.
