## Summary

- add `Owner.hasPets()` as a convenience method for checking whether an owner currently has any pets
- keep the implementation read-only by returning whether the existing pets collection is empty
- leave existing owner and pet behavior unchanged, including how pets are stored, added, and listed

## Testing

- owner with one or more pets returns `true`
- owner with no pets returns `false`
- existing owner and pet interactions remain unchanged
