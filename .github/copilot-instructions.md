## Repository Overview

- Purpose: [TODO — MUST COMPLETE]: One-paragraph description of what this service does and its primary consumers. Needed so Copilot understands domain context.
- Tech Stack: Java 17 Maven application using the internal parent `com.petclinic.platform.pom:petclinic-starter-parent:4.3.0-SNAPSHOT`. This repo declares Spring Boot starters for Actuator, Cache, Data JPA, Thymeleaf, Validation, and Web MVC; runtime dependencies include H2, MySQL, PostgreSQL, Caffeine, JAXB, and WebJars assets; tests use Spring Boot test starters for JPA, Web MVC, Validation, Thymeleaf, Actuator, RestClient, Docker Compose, and Testcontainers. The service also depends on `com.petclinic.vets:vets-service-openapi-client-code:1.0.0-SNAPSHOT`. [TODO — MUST COMPLETE]: Exact resolved Spring Boot version is inherited from the parent and not declared directly in this repo. Record it here so Copilot uses the correct framework APIs.
- Architecture: Base package `org.springframework.samples.petclinic` with feature packages `owner`, `vet`, and `system`, plus shared `model`. The application is a layered Spring MVC app where controllers talk directly to Spring Data JPA repositories and JPA entities, with Thymeleaf views for HTML flows and a small serialized `/vets` endpoint. No repo-local OpenAPI spec or springdoc configuration was discovered.

## API Conventions

- Versioning: No `/api/v1` style URI versioning was discovered. Existing routes are MVC paths such as `/owners`, `/owners/{ownerId}`, `/owners/{ownerId}/edit`, `/owners/new`, `/vets`, and `/vets.html`.
- Error response format: No `@ControllerAdvice`/global exception handler or consistent JSON error envelope was discovered. Validation failures are handled in-controller with `BindingResult`, redisplaying the same Thymeleaf view, and some missing-record paths throw `IllegalArgumentException`. [TODO — MUST COMPLETE]: Define the standard error response format for new API endpoints and how controller exceptions should be translated for clients.
- Input validation: Use Jakarta Validation annotations on form-backed model objects/entities and check `BindingResult` immediately after binding in controller POST handlers. Existing code also uses `@InitBinder` to disallow client binding of server-managed identifiers where applicable.
- HTTP status conventions and pagination format: HTML form GETs return `200 OK`; successful POSTs typically return `3xx` redirects; validation errors return `200 OK` with the original view and model errors. Existing paginated list screens use a 1-based `page` query parameter, `PageRequest.of(page - 1, 5)`, and model attributes such as `currentPage`, `totalPages`, `totalItems`, and `listOwners`/`listVets`. The `/vets` response returns a wrapped `Vets` object rather than a bare collection.

## Security Guidelines

- Never hardcode secrets, API keys, passwords, or connection strings.
- Never build SQL or JPQL via string concatenation when a repository query or parameterized mechanism is available.
- Never log sensitive data or entire domain objects; log minimal identifiers and operational context only.
- Always validate inbound form or request data with Jakarta Validation and reject invalid submissions explicitly.
- Always hash passwords with a secure one-way algorithm if authentication is introduced.
- [TODO — MUST COMPLETE]: Document the authentication/authorization model for this service. No Spring Security starter or project-specific auth provider configuration was discovered in this repo, so Copilot must not guess access-control rules.
- [TODO — MUST COMPLETE]: Document the expected CORS, CSRF, and session-management policy for browser clients and any machine-to-machine consumers.

## Design Patterns

- Spring MVC controllers use constructor injection and are organized by feature package.
- Persistence follows the Spring Data JPA repository pattern (`OwnerRepository`, `PetTypeRepository`, `VetRepository`) over JPA entities.
- Domain entities are used directly as both persistence models and MVC form-backing objects in the current codebase.
- Shared domain inheritance is used in `model` and feature packages extend that shared model.
- The codebase includes both server-rendered Thymeleaf flows and a response-body wrapper object (`Vets`) for serialized collection responses.

## Unique Patterns For This Project

[TODO — MUST COMPLETE]: Project-specific rules an AI would not infer from code — date/timezone handling, i18n strategy, idempotency requirements, audit logging, and any rules for the internal vets-service client. These cause the most defects when missed.

## What To Avoid

- Do not move business validation into views; keep binding checks and persistence decisions in controllers/repositories consistent with the current MVC flow.
- Do not expose bindable `id` fields from browser forms; preserve the `WebDataBinder` pattern that disallows `id` and `*.id` binding for owner flows.
- Do not introduce a different pagination contract for owner/vet list screens without changing the existing 1-based `page` parameter and page-size convention intentionally across the feature.
- Do not return bare `List<Vet>` payloads from the existing `/vets` endpoint family; keep the wrapper-object pattern unless the API contract is deliberately changed.
- Do not add a service layer opportunistically for simple CRUD changes; the current production code uses controller → repository → entity directly. If you need a service layer for new non-trivial business logic, refactor explicitly and consistently rather than mixing patterns ad hoc.
- Do not use entities as external API DTOs for new machine-facing APIs if a stable contract is required; the current entity-as-form model pattern is tied to MVC views and can leak persistence concerns.

## Documentation Requirements

- Preserve or add Javadoc on public repository/domain methods when behavior, invariants, or query semantics are not obvious from the signature.
- When controller behavior changes, update the corresponding Thymeleaf views, redirects, flash-message expectations, and tests in the same feature package.
- Document new database/profile assumptions and developer/runtime prerequisites in the README or deployment documentation because this repo supports multiple runtime databases and test profiles.
- No repo-local OpenAPI annotations or API documentation standard were discovered. [TODO — MUST COMPLETE]: Define whether new endpoints require OpenAPI annotations, external API documentation, or both.
