## Summary

- add `Owner.hasPets()` as a convenience method that returns `true` when the owner has at least one pet and `false` when the pet collection is empty
- keep the implementation read-only by checking the existing pets collection without changing how pets are stored or added
- cover the new behavior in `OwnerTests`, including empty, single-pet, multiple-pet, and no-op scenarios to confirm existing owner/pet behavior remains unchanged
