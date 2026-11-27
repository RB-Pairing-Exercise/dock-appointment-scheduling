# Dock Appointment Scheduling

A monorepo for the Dock Appointment Scheduling app: a Java Spring Boot backend
and a Next.js frontend. Everything runs locally with zero external
dependencies — no Docker, no external database.

## Repository Structure

- `backend/` — Spring Boot 3 (Java toolchain 21), Gradle (Kotlin DSL), Spring
  Web, Spring Data JPA, Bean Validation, H2 in-memory database.
- `frontend/` — Next.js (App Router), TypeScript, Tailwind CSS, Vitest, React ( requires node 22+)
  Testing Library.
- `handoff/` — placeholder for upcoming PRD / interview artifacts.

## Quickstart

### 1. Run the backend (port 8080)

```bash
cd backend
./gradlew bootRun
```

The API seeds 4 docks and 6 sample appointments on startup (H2 in-memory DB,
reset on every restart). Try it: `curl http://localhost:8080/api/docks`

### 2. Run the frontend (port 3000)

```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:3000. It talks to the backend at
`NEXT_PUBLIC_API_URL` (defaults to `http://localhost:8080` if unset — see
`frontend/.env.local.example`).

### 3. Run the test suites

```bash
# Backend: unit + integration tests
cd backend && ./gradlew test

# Frontend: component tests (single-run CI mode)
cd frontend && npm test

# Frontend: production build check
cd frontend && npm run build
```

## API (read-only baseline)

- `GET /api/docks` — all docks.
- `GET /api/appointments?date=YYYY-MM-DD&carrierId=carrier-alpha` — scheduled
  appointments, optionally filtered by date and/or carrier.

`POST /api/appointments` and `GET /api/availability` are intentionally not
implemented yet — this phase is strictly read-only.