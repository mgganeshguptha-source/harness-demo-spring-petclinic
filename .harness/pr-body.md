## Summary

- add `Owner.hasPets()` as a convenience method that returns `true` when the owner's existing `pets` collection is non-empty and `false` otherwise
- keep pet storage and mutation behavior unchanged by reading the existing collection rather than altering how pets are added or stored
- extend `OwnerTests` to cover owners with no pets, owners with pets, and unchanged state after duplicate add attempts
