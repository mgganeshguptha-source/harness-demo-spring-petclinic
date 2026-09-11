Title: Add last-name filter to the Owners list

Story:
As a clinic staff member, I want to filter the Owners list by last name so I can find an owner quickly without paging through the whole list. Today the Owners page (/owners) shows all owners with no way to narrow the list. This story adds a last-name search to that existing page.

Expected behaviour:
The Owners page gains a single text input labelled "Last name" above the results table. Submitting the form filters the list to owners whose last name starts with the entered text, case-insensitive. The existing table columns stay unchanged (Name, Address, City, Telephone, Pets).

Acceptance criteria (as the BA wrote them):

Searching "Davis" returns only owners whose last name begins with "Davis", ignoring case ("davis" and "DAVIS" return the same results).
Searching with an empty last-name field returns all owners.
If no owner matches, the page shows the message "No owners found" and the empty table.
Matching is prefix-only — searching "avis" does not return "Davis".
Results stay sorted by last name, ascending, as they are today.
The search is server-side; results are paginated at the current page size (existing behaviour, do not change page size).
Max length: "The Last name input accepts up to 50 characters. If the input exceeds 50 characters, the page shows a validation message 'Last name must be 50 characters or fewer' and does not run the search."
Unicode/normalization: "Matching compares characters as entered, case-insensitive only. No accent or Unicode normalization is applied — 'e' does not match 'é'."

In scope: the Owners list page and its backend query.
Out of scope: searching by first name, city, or any other field; changes to the owner detail page.