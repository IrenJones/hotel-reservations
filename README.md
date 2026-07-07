# Hotel Reservation Backend Template

A minimal Spring Boot template for a hotel reservation backend, meant as a starting point
for real feature work rather than a finished product. It ships a single application entry
point, Flyway-managed schema versioning, and a small example domain (Hotel / Room /
Reservation) with basic CRUD REST endpoints to demonstrate the intended layering.

**Not included by design:** authentication/authorization, Docker Compose, Testcontainers,
or a mapper library (MapStruct). Add these when the real project needs them.

## Tech stack

- Java 21
- Spring Boot 3.4.1 (Web, Data JPA, Validation) — bump to the latest 3.4.x/3.5.x patch when you start the real project
- Gradle (Groovy DSL)
- Flyway for schema migrations
- H2 database — file-based for local dev, in-memory for tests
- Lombok — generates getters/setters on entities (`@Getter`/`@Setter`); your IDE needs annotation processing enabled and, for IntelliJ/Eclipse, the Lombok plugin installed
- springdoc-openapi (Swagger UI) — interactive API docs generated from the controllers

## Prerequisites

Install a JDK 21. Either:

```bash
sudo apt update && sudo apt install -y openjdk-21-jdk
```

or, without root, via [SDKMAN](https://sdkman.io/):

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 21.0.5-tem
```

## One-time: generate the Gradle wrapper

This repo does not include `gradlew` / `gradle-wrapper.jar` yet, since generating them
requires a Gradle installation. Install Gradle once (e.g. `sdk install gradle 8.11.1`),
then from the project root run:

```bash
gradle wrapper --gradle-version 8.11.1 --distribution-type bin
```

This creates `gradlew`, `gradlew.bat`, and `gradle/wrapper/gradle-wrapper.jar`. From then
on, always use the wrapper (`./gradlew ...`) rather than a system-installed Gradle.

## Build, run, test

```bash
./gradlew build       # compiles and runs tests
./gradlew bootRun      # starts the app on :8080 using the `dev` profile (file-based H2 under ./data/)
./gradlew test         # runs tests against an isolated in-memory H2 database
```

While running with `bootRun`, the H2 console is available at `http://localhost:8080/h2-console`
(JDBC URL `jdbc:h2:file:./data/hotel-db;AUTO_SERVER=TRUE`, user `sa`, empty password).

Swagger UI is available at `http://localhost:8080/swagger-ui.html` (raw OpenAPI JSON at
`/v3/api-docs`), documenting the hotel/room/reservation endpoints automatically from the
controllers.

### Try it out

```bash
curl -X POST localhost:8080/api/hotels \
  -H 'Content-Type: application/json' \
  -d '{"name":"Grand Test Hotel","address":"1 Test St","city":"Testville","starRating":4}'

curl localhost:8080/api/hotels
```

## Project layout

| Path | Purpose |
|---|---|
| `HotelReservationTemplateApplication.java` | The single `@SpringBootApplication` entry point |
| `domain/` | JPA entities (`Hotel`, `Room`, `Reservation`) and enums |
| `dto/` | Request/response records with Bean Validation |
| `repository/` | Spring Data JPA repositories |
| `service/` | Business logic, entity↔DTO mapping |
| `controller/` | REST controllers (`/api/hotels`, `/api/rooms`, `/api/reservations`) |
| `config/` | Exception types, `@RestControllerAdvice` error handling, OpenAPI/Swagger config |
| `resources/db/migration/` | Flyway migrations |

## Migrations

Flyway owns the schema; Hibernate is configured with `ddl-auto: validate` so it only checks
the entities match the tables Flyway created — it never generates DDL itself. Migrations
live in `src/main/resources/db/migration/` and follow the `V<version>__description.sql`
naming convention:

- `V1__create_hotel_table.sql`
- `V2__create_room_table.sql`
- `V3__create_reservation_table.sql`

To add a new migration, create a new `Vn__description.sql` file with the next version
number — never edit an already-applied migration.

## Extending the template

To add a new entity, follow the pattern used by Hotel/Room/Reservation:

1. Add a Flyway migration creating its table (`Vn__create_<entity>_table.sql`)
2. Add the JPA entity in `domain/`
3. Add a `JpaRepository` in `repository/`
4. Add a DTO record (with validation) in `dto/`
5. Add a `@Service` with entity↔DTO mapping in `service/`
6. Add a `@RestController` in `controller/`
