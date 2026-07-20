# SAO Attendance Tracker

A Spring Boot attendance system with a Sword Art Online-inspired "Cardinal
System" dashboard, backed by direct CRUD storage in H2 or MySQL.

## What's inside

| Concern | Where |
|---|---|
| Abstract base entity (`@MappedSuperclass`, id + audit timestamps) | `entity/AbstractAuditable.java` |
| JPA entities & mappings (`@OneToMany` / `@ManyToOne`) | `entity/Student.java`, `entity/AttendanceRecord.java` |
| Repositories | `repository/` |
| Service interfaces + impls (business rules, "is attendance marked?") | `service/`, `service/impl/` |
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
and attendance so you can see everything working immediately.

By default the app now connects to MySQL using the `Kirito` database.
If you prefer a zero-setup in-memory database instead, comment the MySQL
block in `src/main/resources/application.properties` and uncomment the H2
block.

## Switching databases

The project is configured to use MySQL by default with the `Kirito`
database. If you'd rather run with H2 for local testing, open
`src/main/resources/application.properties`, comment the MySQL block,
and uncomment the H2 block.

## Direct CRUD usage

The application now stores students and attendance directly in a local
JPA-backed database. You can run it immediately with the built-in H2
in-memory database or switch to MySQL by following the instructions in
`src/main/resources/application.properties`.

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
