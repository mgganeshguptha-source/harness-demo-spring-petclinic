Title: Add a method to check whether an Owner has any pets

Story:
As a developer, I want to ask an Owner whether it currently has any pets, so that calling code can check this directly instead of fetching the pet list and testing its size. Today the Owner entity exposes its list of pets, and callers check emptiness themselves. This story adds a convenience method on the Owner class that answers the question directly.

Expected behaviour:
The Owner class gains a method hasPets() that returns a boolean: true when the owner has one or more pets, false when the owner has none. It reads the owner's existing pets collection and does not change how pets are stored or added.

Acceptance criteria:

When an owner has one or more pets, hasPets() returns true.
When an owner has no pets, hasPets() returns false.
hasPets() does not add, remove, or modify any pet.
Existing owner and pet functionality (add pet, list pets, view owner) is unchanged.

In scope: the Owner entity class only.
Out of scope: any UI, template, controller, or endpoint change; any change to how pets are added or stored.

Q1 – Can the pets collection be null?
No. Owner.pets is declared private final List<Pet> pets = new ArrayList<>();, so it is always initialized and can never be null (Hibernate also never sets a mapped collection to null). hasPets() needs no null handling: return !pets.isEmpty(). No null-specific behaviour or test is required. AC-3 means only "hasPets() must not add, remove or modify pets"; it is not about null.

Q2 – Lazy loading / transactions?
Not applicable. pets is mapped @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER), so pets are always loaded together with the owner. hasPets() cannot trigger a lazy load or a LazyInitializationException, even with spring.jpa.open-in-view=false. Implement it with isEmpty() on the existing collection. Do not change the fetch type or add transactional code.

Also: count every pet in the collection, including a newly added pet that is not saved yet (no id). This matches "one or more pets".
