package com.aldisued.iot.monitoring.tasks;

import com.aldisued.iot.monitoring.IntegrationTestBase;
import com.aldisued.iot.monitoring.entity.SensorType;
import com.aldisued.iot.monitoring.service.MeasurementService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Sql(scripts = "/sql/task-8-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class Task8Tests extends IntegrationTestBase {

  @Autowired
  private MeasurementService measurementService;

  @ParameterizedTest
  @MethodSource("measurementValuesBySensorType")
  public void verifyMeasurementValuesBySensorType(
      SensorType sensorType,
      LocalDateTime from,
      LocalDateTime to,
      List<Double> expectedValues) {
    var values = measurementService.getMeasurementValuesBySensorType(
        sensorType,
        from,
        to
    );

    Assertions.assertEquals(expectedValues, values);
  }

  private static Stream<Arguments> measurementValuesBySensorType() {
    return Stream.of(
        Arguments.of(SensorType.TEMPERATURE,
            LocalDateTime.parse("2020-01-01T00:00:00"),
            LocalDateTime.parse("2020-12-31T23:59:59"),
            List.of(15.8, 16.1, 17.3)),
        Arguments.of(SensorType.HUMIDITY,
            LocalDateTime.parse("2020-01-01T00:00:00"),
            LocalDateTime.parse("2020-12-31T23:59:59"),
            List.of(0.45, 0.44, 0.41)),
        Arguments.of(SensorType.ATMOSPHERIC_PRESSURE,
            LocalDateTime.parse("2020-01-01T00:00:00"),
            LocalDateTime.parse("2020-12-31T23:59:59"),
            List.of(98.4, 98.2, 95.4)),
        Arguments.of(SensorType.TEMPERATURE,
            LocalDateTime.parse("2020-06-22T00:00:00"),
            LocalDateTime.parse("2020-06-22T23:59:59"),
            List.of(15.8))
    );
  }

}
