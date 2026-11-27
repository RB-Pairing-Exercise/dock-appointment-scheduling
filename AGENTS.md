# AGENTS.md

## Project Overview
Carrier Dock Appointment Scheduling portal consisting of a Java Spring Boot backend and a Next.js frontend. Carriers select their organization in the UI (simulated auth) to view scheduled dock appointments and request new time slots.

## Repository Structure
- `backend/` — Java 21, Spring Boot 3.4.1, Gradle (Kotlin DSL), Spring Web, Spring Data JPA, H2 in-memory database.
- `frontend/` — Next.js (App Router), TypeScript, Tailwind CSS, Vitest + React Testing Library. Requires Node 22+.

## Build, Run & Test Commands

### Backend (`backend/`)
- Run tests (unit + integration): `./gradlew test`
- Start server (port `8080`): `./gradlew bootRun`
- H2 Console (when running): `http://localhost:8080/h2-console` (`jdbc:h2:mem:dockdb`, user `sa`, empty password)

### Frontend (`frontend/`)
- Install dependencies: `npm install`
- Run unit tests (single run): `npm test`
- Production build check: `npm run build`
- Start dev server (port `3000`): `npm run dev`

## Architecture & Current Conventions
- **API Base URL:** Backend serves REST endpoints under `/api/**` on `http://localhost:8080`. CORS is configured for `http://localhost:3000`.
- **Simulated Auth:** There is no login flow. The frontend passes the active carrier context (`carrierId`) via request parameters/headers from the top-bar carrier switcher (`carrier-alpha`, `carrier-beta`, `carrier-gamma`).
- **Database:** H2 runs in-memory with `create-drop` and seeds initial docks and appointments on startup. No external database or Docker setup is required.
- **Existing Patterns:** Follow the existing package structure in `backend/src/main/java` (controller, service, repository, model/dto) and component structure in `frontend/src`.

## Agent Working Rules
- Keep changes incremental and easy to review; avoid unnecessary abstractions or new external dependencies.
- Maintain parity between backend JSON payloads and frontend TypeScript types.
- Run `./gradlew test` and `npm test` after making changes to verify nothing is broken.