package com.aldisued.iot.monitoring.tasks;

import com.aldisued.iot.monitoring.IntegrationTestBase;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorType;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Sql(scripts = "/sql/task-1-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class Task1Tests extends IntegrationTestBase {

  private static final UUID ID = UUID.fromString("e3242ea2-0514-46d3-aad8-b2012980c41c");

  @Autowired
  private SensorRepository sensorRepository;

  @Test
  public void verifySensorType() {
    Sensor sensor = sensorRepository.findById(ID)
        .orElseThrow();
    Assertions.assertEquals(SensorType.TEMPERATURE, sensor.getType());
  }
}
