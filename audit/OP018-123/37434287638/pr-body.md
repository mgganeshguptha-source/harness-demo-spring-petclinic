## Summary
- add `Vet.hasSpecialty(String name)` so callers can check a vet's specialties directly without looping
- trim the requested name, compare it case-insensitively against specialties read through `getSpecialtiesInternal()`, and return `false` for null, blank, missing, or non-matching values
- preserve existing specialty storage and behavior by leaving `getSpecialties()`, `getNrOfSpecialties()`, and `addSpecialty(...)` unchanged

## Testing
- cover specialty matches, trimmed input, case-insensitive lookup, null/blank input, no-specialty cases, and unchanged specialty ordering/count behavior in `VetTests`
