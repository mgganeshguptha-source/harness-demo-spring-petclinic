# Validation — vet-specialty-check

**Source:** vet-specialty-check-context-261012-093451.md
**Criteria checked:** 8  (met 8 · not met 0 · unverifiable 0)

**VERDICT: PASS**

## Per-criterion verdicts

### AC-1 — MET
WHEN the vet has a specialty whose stored name equals the trimmed input name ignoring case, THE vet model SHALL return true from the specialty-check method.

**Evidence:** `Vet.hasSpecialty()` (lines 70–83) implements case-insensitive comparison via `specialtyName.equalsIgnoreCase(trimmedName)` at line 82. `VetTests.hasSpecialtyReturnsTrueForCaseInsensitiveMatch()` (lines 42–48) asserts `vet.hasSpecialty("surgery")` and `vet.hasSpecialty("Surgery")` both return `true`.

### AC-2 — MET
IF no stored specialty name equals the trimmed input name ignoring case, THEN THE vet model SHALL return false from the specialty-check method.

**Evidence:** `Vet.hasSpecialty()` uses `getSpecialtiesInternal().stream().anyMatch(...)` (line 80–82), which returns `false` if no specialty matches. `VetTests.hasSpecialtyReturnsFalseWhenNoSpecialtyMatches()` (lines 60–66) asserts `vet.hasSpecialty("dentistry")` returns `false` when only "surgery" exists, and `vet.hasSpecialty("sur gery")` returns `false`.

### AC-3 — MET
IF the vet has no specialties, THEN THE vet model SHALL return false from the specialty-check method.

**Evidence:** `Vet.hasSpecialty()` uses `getSpecialtiesInternal().stream()` (line 80), which produces an empty stream if the vet has no specialties, causing `anyMatch()` to return `false`. `VetTests.hasSpecialtyReturnsFalseWhenVetHasNoSpecialties()` (lines 69–73) creates a vet with no specialties and asserts `vet.hasSpecialty("surgery")` returns `false`.

### AC-4 — MET
IF the input name is null or blank after trimming, THEN THE vet model SHALL return false from the specialty-check method without raising an exception.

**Evidence:** `Vet.hasSpecialty()` handles both cases: lines 71–73 check `if (name == null) return false;` and lines 76–78 check `if (trimmedName.isEmpty()) return false;`. No exception is thrown. `VetTests.hasSpecialtyReturnsFalseForNullOrBlankInput()` (lines 76–83) asserts `vet.hasSpecialty(null)`, `vet.hasSpecialty(" ")`, and `vet.hasSpecialty("\t")` all return `false` without exception.

### AC-5 — MET
WHEN the input name has leading or trailing whitespace, THE vet model SHALL trim only the outer whitespace before comparing specialty names.

**Evidence:** `Vet.hasSpecialty()` at line 75 applies `String.trim()` to the input (`String trimmedName = name.trim();`), which removes only leading and trailing whitespace, preserving internal spaces. The trimmed name is then compared via `equalsIgnoreCase(trimmedName)` at line 82. `VetTests.hasSpecialtyReturnsTrueForTrimmedMatch()` (lines 51–57) asserts `vet.hasSpecialty(" surgery ")` and `vet.hasSpecialty(" Surgery")` return `true`. `VetTests.hasSpecialtyReturnsFalseWhenNoSpecialtyMatches()` (line 65) asserts `vet.hasSpecialty("sur gery")` returns `false`, confirming internal spaces are not trimmed.

### AC-6 — MET
THE vet model SHALL read specialties through the existing internal specialty-set accessor when evaluating the specialty-check method.

**Evidence:** `Vet.hasSpecialty()` at line 80 explicitly calls `getSpecialtiesInternal().stream()` to read specialties, using the existing protected accessor as required.

### AC-7 — MET
WHEN the specialty-check method is called, THE vet model SHALL leave the specialty collection unchanged.

**Evidence:** `Vet.hasSpecialty()` (lines 70–83) reads specialties only via `.stream()` and `.anyMatch()`, performing no add, remove, or update operations. The method is a pure query with no mutations. `VetTests.hasSpecialtyDoesNotModifySpecialties()` (lines 86–102) asserts `getNrOfSpecialties()` and `getSpecialties()` return the same values before and after calling `hasSpecialty()` with multiple input cases.

### AC-8 — MET
WHEN the specialty-check method is added, THE existing specialty listing, specialty counting, and specialty addition behaviours SHALL keep their current results.

**Evidence:** The existing methods `getSpecialties()` (lines 60–64), `getNrOfSpecialties()` (lines 66–68), and `addSpecialty()` (lines 85–87) remain unchanged in their signatures and implementations. `VetTests.specialtyHelpersRemainUnchanged()` (lines 105–115) asserts `getNrOfSpecialties()` returns 2 and `getSpecialties()` returns both specialties sorted by name, confirming that existing behaviours are preserved.

## Assumed criteria

None.

## Specification findings

None.

## Summary

All eight acceptance criteria are satisfied. The `hasSpecialty(String name)` method is implemented correctly in the `Vet` class, handles all edge cases (null, blank, whitespace trimming) without throwing exceptions, performs case-insensitive matching, uses the existing internal specialty-set accessor, reads specialties only without modification, and does not affect the existing specialty query and manipulation methods. Comprehensive test coverage in `VetTests` validates all criteria and edge cases.
