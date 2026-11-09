# IoT Sensor Monitoring Application

This is an **IoT monitoring system** where multiple sensors  
(temperature, humidity, and atmospheric pressure) continuously send readings  
to a backend service. The backend is built with **Spring Boot** and provides  
REST APIs for collecting, processing, and analyzing sensor data.
The collected data is stored in a PostgreSQL database.
The database tables are created by Hibernate.If you want to change the tables, you must do so by modifying the entity classes.

You will need to finish the implementation of the application by completing  
the tasks listed below. These tasks should take about **60 minutes** to complete.  
It is recommended to leave the optional tasks until the end and implement them  
only if you have enough time.

### 0. Task

Some tasks require a **PostgreSQL** database to run the prepared integration tests.  
The tests can automatically start a PostgreSQL **Testcontainer**, but you may also set up a local PostgreSQL database if you prefer.

---

#### Option 1: Use Testcontainers (recommended)
If you have **Docker** installed, you already have everything you need to run PostgreSQL in a container.  
To verify your setup, run the `Task0Test` test class. If the test passes, you are ready to continue with the next tasks.

#### Option 2: Use a Local PostgreSQL Database
If you want to set up a PostgreSQL database locally, run the following SQL commands to create the required database and user:

```sql
CREATE DATABASE monitoring;
CREATE USER monitoring_user WITH PASSWORD 'IOT123!';
GRANT ALL PRIVILEGES ON DATABASE monitoring TO monitoring_user;
```
In the `com.aldisued.iot.monitoring.IntegrationTestBase` test class you need to change the profile to `local` by changing the annotation to `@ActiveProfiles("local")` on line 14.
To verify your setup, run the `Task0Test` test class. If the test passes, you are ready to continue with the next tasks.


### 1. task
The `Sensor` entity has a `type` property, which is defined as an **ENUM**.  
In the current implementation, this property is stored as an **integer** in the database.

Your task:  
Modify the implementation so that the `type` property is stored as a **string**  
(e.g., `"TEMPERATURE"`, `"HUMIDITY"`, `"PRESSURE"`) instead of an integer.

You can test your implementation by running the `Task1Tests` test class.

### 2. Task

The application contains three entities: `Sensor`, `SensorReading`, and `Alert`.  
A `Sensor` can have multiple `SensorReading`s and multiple `Alert`s.  
However, the relationships between these entities are not yet implemented.

**Your task:**
- Modify the `Sensor`, `SensorReading`, and `Alert` entities to establish the necessary relationships.
- `SensorReading`, and `Alert` must have a foreign key column referencing the `Sensor` entity named `sensor_id`.
- The `Sensor` entity must have:
  - a list of `SensorReading`s
  - a list of `Alert`s
- The `SensorReading` and `Alert` entities must each have a reference to the `Sensor` they belong to.
- Getter and setter method stubs are already prepared for the new properties and must be fully implemented.

You can test your implementation by running the `Task2Tests` test class.

### 3. Task

`SensorReading`s can arrive either via an HTTP POST request (handled by `SensorReadingController`)  
or via a Kafka message (handled by `SensorReadingListener`).  
In both cases, the `SensorReading` must be saved to the database using the `SensorReadingService`.

**Your task:**
- Implement the `SensorReadingService.saveSensorReading` method to persist the `SensorReading` in the database.

You can test your implementation by running the `Task3Tests` test class.

### 4. Task
The Sensor entities can be created via an HTTP POST request to the `/sensors` endpoint.

There are some issues with the current implementation:
- If no sensor name is provided in the request, the application must return a **400 Bad Request** instead of a `500 Internal Server Error`.
- The sensor name must be **unique**. If a sensor with the same name already exists, the application should return a **409 Conflict** status.

**Your task:**
- Fix the implementation.

You can test your implementation by running the `Task4Tests` test class.

### 5. Task
The application stores alerts related to sensors in the `Alert` entity.

**Your task:**
- Implement a REST endpoint in the `AlertController` to retrieve the **most recent alert** for a given sensor.
- The endpoint URL should be `/alerts/latest`.
- The device ID must be provided as a **query parameter** (mandatory).
- If there is no alert for the given sensor, the endpoint must return a **404 Not Found** status.
- Example request GET `http://localhost:8080/alerts/latest?sensorId=777d727e-6650-415f-85eb-9c9ca05f65c1`

You can test your implementation by running the `Task5Tests` test class.


### 6. Task (Optional)

Similar to `SensorReading`s, `Alert`s can also arrive via an HTTP POST request or a Kafka message.

**Your task:**
- Implement the `AlertService.saveAlert` method to:
  1. Persist the `Alert` in the database.
  2. Publish a Kafka message with the `AlertDto` object as the payload to the `alerts` topic.

You can assume that Kafka is already configured in the application, and you can use the `KafkaTemplate<String, AlertDto>` for publishing messages.

You can test your implementation by running the `Task6Tests` test class.

### 7. Task

The application can store multiple types of sensor readings, such as temperature, humidity, and pressure.

**Your task:**
- Implement the `MeasurementService.getAverageTemperature` method.
- The method must return the **average temperature reading** over a specified time period.

You can test your implementation by running the `Task7Tests` test class.

### 8. Task (Optional)

To enable further processing, the application must be able to retrieve all measurement values for a specific sensor type.

**Your task:**
- Implement the `MeasurementService.getMeasurementValuesBySensorType` method.
- The method must return a **list of measurement values** for a given sensor type, ordered by their timestamp.

You can test your implementation by running the `Task8Tests` test class.

### 9. Task

Sensor data is often noisy and may contain outliers. To improve the accuracy of reports, a new calculation is being introduced.

**Your task:**
- Implement the `MeasurementCalculatorService.filterByAverageDeviation` method.
- The method input is a list of `Double` values representing sensor measurements;
- The method must filter out values that deviate too much from the **average**.
- The acceptable deviation is provided as a parameter (a `double` between `0.0` and `1.0`), representing the allowed percentage of deviation from the average.
- The method must validate the deviation parameter:
  - If the parameter is **outside the range [0.0, 1.0]**, the method should throw an `IllegalArgumentException`.

**Example:**
- Input values: `[10.0, 12.0, 20.0]`
- Deviation: `0.2` (20%)
- Average = `(10 + 12 + 20) / 3 = 14.0`
- Acceptable range = `14.0 - 20% * 14.0, 14.0 + 20% * 14.0]` = `[11.2, 16.8]`
- Filtered result: `[12.0]` → only these values remain outside the acceptable range and should be excluded.

You can test your implementation by running the `Task9Tests` test class.

### 10. Task

Readings outside safe limits must be reported automatically. Each sensor type has its own rule.

**Your task:**
- When `SensorReadingService.saveSensorReading` saves a `SensorReading`, check it against the rule for its sensor's type.
- If the reading breaks the rule, save an `Alert` for the same sensor. Use the reading's timestamp and the message from the table below.
- If the reading is within the limits, don't create an `Alert`.

| Sensor type            | Alert when                    | Alert message                       |
|------------------------|-------------------------------|-------------------------------------|
| `TEMPERATURE`          | value `> 40.0` or `< -20.0`   | `Temperature out of range: <value>` |
| `HUMIDITY`             | value `> 90.0`                | `Humidity too high: <value>`        |
| `ATMOSPHERIC_PRESSURE` | value `< 950.0` or `> 1050.0` | `Pressure out of range: <value>`    |

The limits are exclusive. For example, a temperature of exactly `40.0` does not create an alert.

**Example:**
- A `TEMPERATURE` sensor sends the value `45.0`.
- The reading is saved, and an `Alert` with the message `Temperature out of range: 45.0` is saved for that sensor.


You can test your implementation by running the `Task10Tests` test class.