Title: Add a method to check whether a Vet has a given specialty

Story: As a developer, I want to ask a Vet whether it has a specialty with a given name, so calling code can check directly instead of looping over the specialty list.

Expected behaviour: Vet gains public boolean hasSpecialty(String name). It returns true when any of the vet's specialties has that name, compared case-insensitively ("Surgery" matches "surgery"). It returns false when no specialty matches, when the vet has no specialties, or when name is null or blank. It reads the existing specialties through getSpecialtiesInternal() and does not change how specialties are stored or added.

Acceptance criteria:

Vet with specialty "surgery": hasSpecialty("surgery") and hasSpecialty("Surgery") both return true.
Vet with specialty "surgery": hasSpecialty("dentistry") returns false.
Vet with no specialties: hasSpecialty("surgery") returns false.
hasSpecialty(null) and hasSpecialty(" ") return false and do not throw.
hasSpecialty does not add, remove or modify specialties. Existing behaviour (getSpecialties, getNrOfSpecialties, addSpecialty) is unchanged.


In scope: the Owner entity class only.
Out of scope: any UI, template, controller, or endpoint change; any change to how pets are added or stored.

Q1 – Can the pets collection be null?
No. Owner.pets is declared private final List<Pet> pets = new ArrayList<>();, so it is always initialized and can never be null (Hibernate also never sets a mapped collection to null). hasPets() needs no null handling: return !pets.isEmpty(). No null-specific behaviour or test is required. AC-3 means only "hasPets() must not add, remove or modify pets"; it is not about null.

Q2 – Lazy loading / transactions?
Not applicable. pets is mapped @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER), so pets are always loaded together with the owner. hasPets() cannot trigger a lazy load or a LazyInitializationException, even with spring.jpa.open-in-view=false. Implement it with isEmpty() on the existing collection. Do not change the fetch type or add transactional code.

Also: count every pet in the collection, including a newly added pet that is not saved yet (no id). This matches "one or more pets".
=======
Clarifications (pre-answered): getSpecialtiesInternal() never returns null; it creates an empty set if needed. Specialties are mapped FetchType.EAGER, so there is no lazy-loading or transaction concern. Do not change the mapping.

In scope: the Vet class only. Out of scope: UI, templates, controllers, repositories, database.

Q – Leading/trailing whitespace around a non-blank name?
Trim it. Remove leading and trailing whitespace from the input name, then compare it case-insensitively with each specialty's name as stored. For a vet with specialty "surgery", hasSpecialty(" surgery ") and hasSpecialty(" Surgery") return true. Whitespace inside the name is kept ("sur gery" does not match). Stored specialty names are compared as they are and are not modified. A name that is blank after trimming returns false (AC-4).

