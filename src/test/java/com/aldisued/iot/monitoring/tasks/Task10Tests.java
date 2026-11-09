package com.aldisued.iot.monitoring.tasks;

import com.aldisued.iot.monitoring.IntegrationTestBase;
import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.entity.SensorType;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import com.aldisued.iot.monitoring.service.SensorReadingService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

public class Task10Tests extends IntegrationTestBase {

  private static final LocalDateTime TIMESTAMP = LocalDateTime.parse("2025-06-01T12:00:00");

  @Autowired
  private SensorReadingService sensorReadingService;

  @Autowired
  private SensorRepository sensorRepository;

  @Autowired
  private AlertRepository alertRepository;

  @AfterEach
  public void cleanup() {
    alertRepository.deleteAll();
    sensorRepository.deleteAll();
  }

  @ParameterizedTest
  @MethodSource("readingsOutsideLimits")
  public void verifyAlertIsCreatedForReadingOutsideLimits(SensorType type, Double value,
      String expectedMessage) {
    Sensor sensor = createSensor(type);

    SensorReading reading = sensorReadingService.saveSensorReading(
        new SensorReadingDto(sensor.getId(), value, TIMESTAMP));

    Assertions.assertNotNull(reading);
    List<Alert> alerts = findAlertsForSensor(sensor.getId());
    Assertions.assertEquals(1, alerts.size());
    Assertions.assertEquals(expectedMessage, alerts.get(0).getMessage());
    Assertions.assertEquals(TIMESTAMP, alerts.get(0).getTimestamp());
  }

  @ParameterizedTest
  @MethodSource("readingsWithinLimits")
  public void verifyNoAlertIsCreatedForReadingWithinLimits(SensorType type, Double value) {
    Sensor sensor = createSensor(type);

    SensorReading reading = sensorReadingService.saveSensorReading(
        new SensorReadingDto(sensor.getId(), value, TIMESTAMP));

    Assertions.assertNotNull(reading);
    Assertions.assertTrue(findAlertsForSensor(sensor.getId()).isEmpty());
  }

  static Stream<Arguments> readingsOutsideLimits() {
    return Stream.of(
        Arguments.of(SensorType.TEMPERATURE, 45.0, "Temperature out of range: 45.0"),
        Arguments.of(SensorType.TEMPERATURE, -25.5, "Temperature out of range: -25.5"),
        Arguments.of(SensorType.HUMIDITY, 95.0, "Humidity too high: 95.0"),
        Arguments.of(SensorType.ATMOSPHERIC_PRESSURE, 940.0, "Pressure out of range: 940.0"),
        Arguments.of(SensorType.ATMOSPHERIC_PRESSURE, 1060.0, "Pressure out of range: 1060.0")
    );
  }

  static Stream<Arguments> readingsWithinLimits() {
    return Stream.of(
        Arguments.of(SensorType.TEMPERATURE, 40.0),
        Arguments.of(SensorType.TEMPERATURE, -20.0),
        Arguments.of(SensorType.TEMPERATURE, 21.5),
        Arguments.of(SensorType.HUMIDITY, 90.0),
        Arguments.of(SensorType.HUMIDITY, 0.0),
        Arguments.of(SensorType.ATMOSPHERIC_PRESSURE, 950.0),
        Arguments.of(SensorType.ATMOSPHERIC_PRESSURE, 1050.0),
        Arguments.of(SensorType.ATMOSPHERIC_PRESSURE, 1013.25)
    );
  }

  private Sensor createSensor(SensorType type) {
    return sensorRepository.save(new Sensor(type + "-" + UUID.randomUUID(), type));
  }

  private List<Alert> findAlertsForSensor(UUID sensorId) {
    List<Alert> result = new ArrayList<>();
    for (Alert alert : alertRepository.findAll()) {
      if (alert.getSensor().getId().equals(sensorId)) {
        result.add(alert);
      }
    }
    return result;
  }
}
