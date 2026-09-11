# Add a method to check whether an Owner has any pets

## Summary

Add a convenience method `hasPets()` to the `Owner` entity so calling code can check whether an owner currently has any pets without reading the pets collection and comparing its size.

## In scope

- `Owner` entity class only

## Out of scope

- UI, templates, controllers, and endpoints
- Changes to how pets are added or stored
- Changes to any other owner or pet behavior

## Expected behavior

- `hasPets()` returns `true` when the owner has one or more pets
- `hasPets()` returns `false` when the owner has no pets
- The method reads the existing pets collection only
- The method does not add, remove, or modify pets

## Acceptance criteria

1. When an owner has one or more pets, `hasPets()` returns `true`.
2. When an owner has no pets, `hasPets()` returns `false`.
3. `hasPets()` does not add, remove, or modify any pet.
4. Existing owner and pet functionality, including add pet, list pets, and view owner, remains unchanged.

## Notes

This change is intended as a small domain-level convenience API. It improves readability for callers while preserving the current `Owner`/`Pet` relationship and storage model.
