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