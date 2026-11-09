package com.aldisued.iot.monitoring.tasks;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aldisued.iot.monitoring.IntegrationTestBase;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Sql(scripts = "/sql/task-5-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class Task5Tests extends IntegrationTestBase {

  private static final UUID SENSOR_ID = UUID.fromString(
      "e3242ea2-0514-46d3-aad8-b2012980c41c");

  private static final String BASE_ENDPOINT = "/alerts";

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void getLatestAlertForExistingSensor() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT + "/latest")
        .queryParam("sensorId", SENSOR_ID.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message", Matchers.equalTo("Temperature alert 4")));
  }

  @Test
  public void verifyMissingSensorIdHandling() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT + "/latest"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void verifyMissingAlertHandling() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT + "/latest")
            .queryParam("sensorId", UUID.randomUUID().toString()))
        .andExpect(status().isNotFound());
  }


}
