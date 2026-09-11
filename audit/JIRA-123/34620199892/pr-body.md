## Summary

- add `Owner.hasPets()` as a convenience method that returns `true` when the existing pets collection is non-empty and `false` otherwise
- keep pet storage and mutation behavior unchanged by reading the current collection without modifying it
- add owner tests covering owners with and without pets, post-`addPet()` behavior, and the non-mutating contract

## Testing

- `OwnerTests`
