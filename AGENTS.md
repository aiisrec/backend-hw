# AGENTS.md

Guidance for AI coding agents working on this repository.

## Project overview

IoT sensor monitoring backend. Sensors (temperature, humidity, atmospheric pressure) send readings and
alerts via REST or Kafka. Data is persisted in PostgreSQL through Spring Data JPA / Hibernate.

This is a take-home assignment. `TASKS.md` is the specification and source of truth: tasks 0 to 10, each
verified by a matching `TaskNTests` class. Read the relevant task in `TASKS.md` before you change behavior.

## Tech stack

- Java 21
- Spring Boot 4.1.x (`spring-boot-starter-webmvc`, `-data-jpa`, `-validation`, `-kafka`)
- PostgreSQL 17 (runtime driver)
- Maven via wrapper (`./mvnw`, Maven 3.9.11)
- JUnit 5, AssertJ, Mockito, MockMvc, Testcontainers (PostgreSQL)
- Jackson 3: tests use `tools.jackson.databind.ObjectMapper`. Annotations still come from
  `com.fasterxml.jackson.annotation`.
- No Lombok. Write getters, setters and constructors by hand.

## Layout

```
src/main/java/com/aldisued/iot/monitoring/
  MonitoringApplication.java   # Spring Boot entry point
  controller/                  # REST: /sensors (POST), /sensor-readings (POST), /alerts/latest (GET)
  dto/                         # Java records: SensorDto (validated), SensorReadingDto, AlertDto
  entity/                      # JPA entities: Sensor (UUID id), SensorReading, Alert (Long ids), SensorType enum
  messaging/                   # @KafkaListener consumers
  repository/                  # Spring Data JPA repositories (derived queries + JPQL @Query)
  service/                     # Business logic; MeasurementCalculatorService is pure (no Spring deps)
src/main/resources/application.properties
src/test/java/com/aldisued/iot/monitoring/
  IntegrationTestBase.java          # @SpringBootTest + MockMvc + @ActiveProfiles("docker")
  TestcontainersConfiguration.java  # postgres:17.6 container, active only in the "docker" profile
  tasks/TaskNTests.java             # One test class per task in TASKS.md
src/test/resources/
  application-docker.yml / application-local.yml
  sql/task-N-test-data.sql          # Seed data loaded with @Sql(BEFORE_TEST_CLASS)
docker-compose.yml                  # Local PostgreSQL (monitoring / monitoring_user / IOT123!)
```

## Coding conventions

- Follow Google Java Style: 2-space indentation, no wildcard imports, 100-column limit.
- Use SLF4J (`LoggerFactory.getLogger(...)`) for logging. Don't log full payloads at `INFO`.
- Keep DTOs as Java records. Don't add setters to records or turn them into classes.
- Avoid using the Stream API. Use for-loops and collections instead.
- For new Spring beans, use `@Autowired` field injection to keep classes short. Existing constructors
  can stay as they are.
- Service lookup methods should return `null` when nothing is found. Controllers check for `null` and
  map it to the right HTTP status.
- Handle exceptions in the controller method with `try`/`catch` and return a `ResponseEntity`. Don't add
  a global `@ControllerAdvice`, because it hides per-endpoint behavior from reviewers.
- Controllers may return JPA entities directly. Jackson 3 handles bidirectional JPA relationships and
  Hibernate proxies out of the box, so separate response DTOs are only needed where `TASKS.md` asks for them.
- Annotate service methods that write data with `@Transactional`. This keeps the database writes and any
  Kafka publishing in the same method atomic.
- Build user-facing strings that contain numbers with `String.format` (for example `"%.1f"`) so the output
  is consistent everywhere.
- Don't rename public methods or change signatures that `TaskNTests` call.
- Don't modify test classes or `sql/task-N-test-data.sql` to make a test pass.

## Build and test

```bash
./mvnw clean verify                        # build and run all tests
./mvnw test -Dtest=Task4Tests              # run a single task's tests
./mvnw test -Dtest=Task9Tests              # pure unit tests, no Docker needed
./mvnw test -Dtest='Task3Tests,Task10Tests' # run several task classes in one go
```

- **Docker must be running** for integration tests (`Task0Test` through `Task8Tests` and `Task10Tests`).
  They start a PostgreSQL Testcontainer. Each test class gets a fresh context (`@DirtiesContext`), and the
  first run is slow while the `postgres:17.6` image downloads.
- Integration tests load their seed data once per class. Tests that write data clean up in `@AfterEach`.
- `spring.jpa.show-sql=true` is on, so the test log shows the SQL that Hibernate generates.
- If Docker isn't available, start PostgreSQL with `docker compose up -d` or a local install. Then switch
  `@ActiveProfiles("docker")` to `"local"` in `IntegrationTestBase`. Don't commit that change unless asked.
- `Task9Tests` creates `MeasurementCalculatorService` directly with `new`. Keep that class
  free of injected dependencies.
- Always run the relevant `TaskNTests` after a change, and run the full suite before you finish.

## Running the application

`application.properties` doesn't define a datasource or Kafka bootstrap servers. Supply them at runtime,
for example:

```bash
docker compose up -d
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/monitoring \
SPRING_DATASOURCE_USERNAME=monitoring_user \
SPRING_DATASOURCE_PASSWORD='IOT123!' \
./mvnw spring-boot:run
```

`docker-compose.yml` includes only PostgreSQL, not Kafka. Kafka defaults to `localhost:9092`. No JSON
serializer or deserializer is configured, so end-to-end Kafka messaging needs extra `spring.kafka.*`
configuration.

Kafka topics: listeners consume `sensor-reading` and `sensor-alerts`, and the application publishes to
`alerts`. Keep consumed and published topics separate, otherwise a published alert is consumed and saved
again in a loop. `KafkaTemplate.send(...)` blocks until the broker acknowledges the record, so callers
don't need extra error handling. Integration tests replace `KafkaTemplate` with a `@MockitoBean` where
needed, so no broker is required for the test suite.

## Domain and data rules

- **Schema is generated by Hibernate** (`ddl-auto=create-drop`). There are no Flyway or Liquibase
  migrations. Change tables **only by editing entity classes**, as `TASKS.md` requires.
- `Sensor` ids are UUIDs. `SensorReading` and `Alert` use generated `Long` ids. Don't change id types.
  The repository generic types already match these ids.
- Timestamps are stored as `LocalDateTime` in UTC.
- DB constraint creation is not necessary. The service runs as a single instance, so checking for an
  existing record before saving is enough to enforce uniqueness.
- Initialize entity collection fields with `List.of()` so getters never return `null`.
- `spring.jpa.open-in-view` is left at the Spring Boot default (`true`). Lazy associations can be loaded
  while a web request is rendered, but not in Kafka listeners or plain service calls outside a transaction.

## Commits and pull requests

- Use Conventional Commits, scoped by task where possible, for example `feat(task-5): add latest alert endpoint`.
- Make one logical change per commit. Don't commit `target/` or IDE files from `.idea/`.