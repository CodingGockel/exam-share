# Prüfprot – Altklausuren-Plattform

Demo-App für den Seminarvortrag „Software Testing mit KI“: Studierende laden Altklausuren (PDF) hoch,
andere können sie nach Uni, Modul, Studiengang usw. durchsuchen, herunterladen und bewerten.

Seiten: Startseite `/` (Suche aufklappen oder zum Upload), Ergebnisse `/suchen`, Upload `/hochladen`. Die fachliche Spezifikation steht in der `CLAUDE.md`.

**Stack:** Vue 3 + Vite + TypeScript · Spring Boot 4 (Java 25) · PostgreSQL 18 · nginx · Docker Compose · Gradle (Kotlin DSL)

## Voraussetzungen

- Docker inklusive Compose-Plugin
- Ein JDK ab Version 17 zum Starten von Gradle. Das JDK 25 für den Build lädt Gradle bei Bedarf automatisch (Foojay).
- Node.js wird für den Gradle-Build automatisch geladen. Ein lokales Node ≥ 22.12 ist nur für `npm run dev` nötig.

## Schnellstart (Produktions-Stack)

```bash
cp .env.example .env        # Passwort anpassen
./deploy.sh                 # baut alles, startet den Stack und wartet auf die Healthchecks
./database/seed.sh          # optional: Beispieldaten anlegen
```

Die App läuft dann auf <http://localhost> (Port über `PROXY_PORT` in der `.env`).

| Befehl                    | Zweck                                        |
|---------------------------|----------------------------------------------|
| `./deploy.sh [up]`        | Images bauen, starten, auf „healthy“ warten  |
| `./deploy.sh down`        | Stoppen (die Daten bleiben im Volume)        |
| `./deploy.sh logs [svc]`  | Container-Logs (`proxy`, `frontend`, `backend`, `db`) |
| `./deploy.sh serverlog`   | Logdatei des Backends (`server-log`) verfolgen |
| `./deploy.sh status`      | Container- und Health-Status                 |

## Entwicklung

```bash
docker compose -f docker-compose.dev.yml up -d   # nur Postgres auf localhost:5432
./gradlew :backend:bootRun                       # Backend auf :8080 (Pfad /api)
cd frontend && npm install && npm run dev        # Vite auf :5173, Proxy /api -> :8080
```

Beispieldaten im Dev-Setup: `BASE_URL=http://localhost:8080 ./database/seed.sh`

Die Logdatei des Backends liegt lokal unter `backend/build/server/logs/server-log`
(im Docker-Stack im Volume `backend-logs`, siehe `./deploy.sh serverlog`).

## Gradle-Tasks

| Task                               | Zweck                                                                |
|------------------------------------|----------------------------------------------------------------------|
| `./gradlew build`                  | Alles bauen inklusive Tests                                          |
| `./gradlew test`                   | Unit-Tests (backend, legacy, frontend via Vitest)                    |
| `./gradlew integrationTest`        | Backend-Integrationstests mit Testcontainers (Docker nötig)          |
| `./gradlew e2eTest`                | Playwright-Tests gegen `BASE_URL` (Standard `http://localhost`)      |
| `./gradlew pitest`                 | Mutation Testing (Report: `backend/build/reports/pitest/index.html`) |
| `./gradlew check`                  | `test` + `integrationTest`                                           |
| `./gradlew stackUp` / `stackDown`  | Produktions-Stack starten/stoppen                                    |
| `./gradlew :e2e:installPlaywrightBrowsers` | Chromium für Playwright vorab laden                          |

## Module

| Ordner      | Inhalt                                                                         |
|-------------|--------------------------------------------------------------------------------|
| `backend/`  | Spring-Boot-API; Flyway-Migrationen in `src/main/resources/db/migration`       |
| `frontend/` | Vue-App; das Dockerfile baut statische Assets in ein nginx-Image               |
| `nginx/`    | Reverse Proxy (einziger offener Port): `/api` → Backend, `/` → Frontend         |
| `database/` | Seed-Skript und Dummy-PDFs                                                     |
| `legacy/`   | Kleiner CSV-Import mit Semester-Parser und JUnit-4-Tests                       |
| `e2e/`      | End-to-End-Tests mit Playwright for Java (`src/e2eTest/java`)                  |

## REST-API (Prefix `/api`)

| Methode | Pfad                       | Beschreibung                                                      |
|---------|----------------------------|-------------------------------------------------------------------|
| POST    | `/api/exams`               | Multipart-Upload (alles Pflicht): `file`, `university`, `faculty`, `studyProgram`, `degree`, `subject`, `lecturer`, `semester` |
| GET     | `/api/exams`               | Liste, optional `?university=&faculty=&studyProgram=&degree=&subject=&lecturer=&semester=`, nach Score sortiert |
| GET     | `/api/exams/suggestions`   | Vorhandene Unis, Fakultäten und Studiengänge (Eingabevorschläge)  |
| GET     | `/api/exams/{id}/download` | PDF-Download                                                      |
| POST    | `/api/exams/{id}/votes`    | Body `{ "direction": "UP" \| "DOWN" }`                            |
| GET     | `/api/actuator/health`     | Healthcheck                                                       |

Fehler kommen als `application/problem+json` (RFC 9457).
