package com.aldisued.iot.monitoring.tasks;

import static org.mockito.ArgumentMatchers.eq;

import com.aldisued.iot.monitoring.IntegrationTestBase;
import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.service.AlertService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "/sql/task-6-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class Task6Tests extends IntegrationTestBase {

  private static final UUID SENSOR_ID = UUID.fromString(
      "e3242ea2-0514-46d3-aad8-b2012980c41c");

  @Autowired
  private AlertService alertService;

  @Autowired
  private AlertRepository alertRepository;

  @MockitoBean
  private KafkaTemplate<String, AlertDto> kafkaTemplate;

  @AfterEach
  public void cleanup() {
    alertRepository.deleteAll();
  }

  @Test
  public void verifySensorReadingProperties() {
    var alertDto = testAlertDto();

    Alert alert = alertService.saveAlert(alertDto);

    Assertions.assertEquals(alertDto.message(), alert.getMessage());
    Assertions.assertEquals(alertDto.timestamp(), alert.getTimestamp());
  }

  @Test
  @Transactional
  public void verifySensorEntity() {
    var alertDto = testAlertDto();

    Alert alert = alertService.saveAlert(alertDto);

    Assertions.assertEquals(SENSOR_ID, alert.getSensor().getId());
  }

  @Test
  @Transactional
  public void verifyKafkaMessage() {
    var alertDto = testAlertDto();

    alertService.saveAlert(alertDto);

    Mockito.verify(
        kafkaTemplate,
        Mockito.times(1)).send(eq("alerts"), eq(alertDto)
    );
  }

  private static @NotNull AlertDto testAlertDto() {
    return new AlertDto(
        SENSOR_ID,
        "Alert message",
        LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)
    );
  }

}
