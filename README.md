# IoT Monitoring System

We are building an **IoT monitoring system** to collect, process, and analyze device data.  
The project has already been started, and we are looking for it to be completed.

---

## 📋 Project Tasks

A detailed project description and the remaining tasks can be found in the [TASKS.md](./TASKS.md) file located in the root directory.

---

## 🚀 Assignment Instructions

1. Clone this repository
2. Review the [TASKS.md](./TASKS.md) file.
3. Complete the remaining implementation tasks.
4. Ensure the project is working end-to-end.
5. Upload the finished project into your own **GitHub repository**.
6. Share the repository link with us.

---

## ⏰ Timeline

Please complete the assignment **within one week**.

---

## 🧰 Prerequisites

- **Java 21**
- **Docker** (recommended): the integration tests start a PostgreSQL 17 Testcontainer
---

## 🧪 Build & Test

```bash
./mvnw clean verify                 # build and run all tests
./mvnw test -Dtest=Task4Tests       # run the tests of a single task
```

Each task in [TASKS.md](./TASKS.md) has a matching `TaskNTests` class in
`src/test/java/com/aldisued/iot/monitoring/tasks/`.

Without Docker, start PostgreSQL yourself (see **Task 0** in [TASKS.md](./TASKS.md)) and switch
`IntegrationTestBase` to `@ActiveProfiles("local")`.

---

## ▶️ Running the Application

The datasource is not configured in `application.properties`, so provide it at startup:

```bash
docker compose up -d
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/monitoring \
SPRING_DATASOURCE_USERNAME=monitoring_user \
SPRING_DATASOURCE_PASSWORD='IOT123!' \
./mvnw spring-boot:run
```

`docker-compose.yml` only contains PostgreSQL. Kafka listeners expect a broker on `localhost:9092`.

---

## 📂 Project Structure

```
src/main/java/com/aldisued/iot/monitoring/
  controller/     REST endpoints (sensors, sensor readings, alerts)
  dto/            Request/response records
  entity/         JPA entities: Sensor, SensorReading, Alert, SensorType
  messaging/      Kafka listeners for readings and alerts
  repository/     Spring Data JPA repositories
  service/        Business logic
src/main/resources/
  application.properties
src/test/java/com/aldisued/iot/monitoring/
  IntegrationTestBase.java          Shared setup for integration tests
  TestcontainersConfiguration.java  PostgreSQL Testcontainer
  tasks/                            Task0Test ... Task10Tests
src/test/resources/
  application-docker.yml / application-local.yml
  sql/                              Seed data for the task tests
docker-compose.yml                  Local PostgreSQL
TASKS.md                            Task descriptions
```
