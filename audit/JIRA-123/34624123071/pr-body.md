## Summary

- add `Owner.hasPets()` as a convenience method that returns `true` when the existing pets collection is not empty and `false` otherwise
- keep owner pet storage and `addPet` behavior unchanged by reading the current pets list without modifying it
- cover the new behavior in `OwnerTests` for empty owners, owners with pets, the transition after adding a first pet, and the non-mutating contract
