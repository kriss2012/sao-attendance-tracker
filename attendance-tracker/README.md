# SAO Attendance Tracker

A Spring Boot attendance system with a Sword Art Online-inspired "Cardinal
System" dashboard, backed by a Google Sheets sync.

## What's inside

| Concern | Where |
|---|---|
| Abstract base entity (`@MappedSuperclass`, id + audit timestamps) | `entity/AbstractAuditable.java` |
| JPA entities & mappings (`@OneToMany` / `@ManyToOne`) | `entity/Student.java`, `entity/AttendanceRecord.java` |
| Repositories | `repository/` |
| Service interfaces + impls (business rules, "is attendance marked?") | `service/`, `service/impl/` |
| Google Sheets integration | removed/disabled; app now uses local MySQL/H2 CRUD |
| Custom exceptions | `exception/` |
| REST API + its own exception handler | `controller/api/` |
| Thymeleaf web dashboard + its own exception handler | `controller/web/` |
| SAO-themed CSS/JS (no image assets used) | `static/css/sao-theme.css`, `static/js/script.js` |
| Demo data (runs with zero setup) | `config/DemoDataSeeder.java` |

## Running it

Requires Java 17+ and Maven.

```bash
mvn spring-boot:run
```

Then open **http://localhost:8080** — you'll land on the "Link Start" splash
page. Click through to the dashboard; it's pre-populated with demo students
and attendance so you can see everything working immediately, with no
external services required.

The H2 console is available at `/h2-console` (JDBC URL:
`jdbc:h2:mem:attendancedb`, user `sa`, empty password) if you want to
inspect the data directly.

## Switching to MySQL

The project matches the datasource shape from a typical MySQL + JPA setup.
To use MySQL instead of the built-in H2 database, open
`src/main/resources/application.properties` and follow the commented block:
comment out the H2 lines, uncomment the MySQL lines, and fill in your own
URL/username/password.

## Connecting your Google Sheet

The integration code is fully wired up and ready to run — it just needs
your own Google credentials, since I have no way to authenticate to your
sheet on your behalf.

**1. Enable the API and create a service account**
- In Google Cloud Console, create (or pick) a project and enable the
  **Google Sheets API**.
- Under *IAM & Admin → Service Accounts*, create a service account and
  generate a **JSON key**. This downloads a `.json` file — that's your
  credentials.

**2. Share the sheet with the service account**
- Open the JSON key file and copy the `client_email` value
  (looks like `something@your-project.iam.gserviceaccount.com`).
- Open your Google Sheet → **Share** → paste that email in as a Viewer.
  Without this step the API will return a 403 and the app will surface it
  as a clear `GoogleSheetSyncException`.

**3. Drop the key into the project**
- Rename the downloaded file to `google-credentials.json`.
- Place it at `src/main/resources/credentials/google-credentials.json`
  (a placeholder file with these same instructions already lives in that
  folder).

**4. Point the app at your sheet**
- In `application.properties`, `googlesheets.spreadsheet-id` is already
  set to the ID from the sheet URL you shared:
  `1M78OGUbCN2GROD6gkW3BLGeqoPKz92ANrgEtqKZyuXg`
- `googlesheets.range` defaults to `Sheet1!A:E` — update the tab name if
  yours differs.

**5. Expected sheet layout** (row 1 is treated as a header and skipped):

| RollNumber | Name | Guild | Date | Status |
|---|---|---|---|---|
| S001 | Kirito | Knights of Blood | 2026-07-20 | PRESENT |

`Status` must be one of `PRESENT`, `ABSENT`, or `LATE`.

**6. Sync**
- Click **"⇪ Sync from Sheet"** on the dashboard, or `POST /api/sheets/sync`.
- Rows are upserted: existing students/dates are updated, new ones are
  created. A malformed row is skipped and logged rather than failing the
  whole sync.

Until you complete these steps, the app runs completely normally on the
seeded demo data — a sync attempt will just fail gracefully with a
readable error message instead of crashing anything.

## REST API

| Method | Path | Description |
|---|---|---|
| GET | `/api/students` | List all students |
| GET | `/api/students/{rollNumber}` | Get one student |
| GET | `/api/attendance/status/{rollNumber}?date=yyyy-MM-dd` | Is attendance marked for that date? |
| GET | `/api/attendance/history/{rollNumber}` | Full attendance history |
| POST | `/api/attendance/mark` | Mark attendance (409 if already marked) |
| PUT | `/api/attendance/update` | Update an existing mark |
| GET | `/api/attendance/summary?date=yyyy-MM-dd` | Dashboard counts |
| POST | `/api/sheets/sync` | Trigger a Google Sheets sync |

All errors come back as a consistent JSON body:
```json
{ "status": 404, "message": "No student found with roll number 'S999'.", "timestamp": "..." }
```

## On the "anime theme"

The dashboard's look is original — cut-corner glass panels, a radial
SVG stat gauge, Orbitron/Rajdhani/Share Tech Mono typography, and a
cyan/gold HUD palette inspired by Sword Art Online's in-world menu
system. No SAO artwork or any other copyrighted images are included;
every visual is CSS/SVG generated, so there's nothing licensed to worry
about if you deploy or share this project.
