package com.aldisued.iot.monitoring.tasks;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aldisued.iot.monitoring.IntegrationTestBase;
import com.aldisued.iot.monitoring.dto.SensorDto;
import com.aldisued.iot.monitoring.entity.SensorType;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

public class Task4Tests extends IntegrationTestBase {

  private static final String BASE_ENDPOINT = "/sensors";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private SensorRepository sensorRepository;

  @AfterEach
  public void tearDown() {
    sensorRepository.deleteAll();
  }

  @Test
  public void tryCreateSensorWithoutName() throws Exception {
    var sensorDto = new SensorDto(
        null,
        SensorType.TEMPERATURE
    );;
    mockMvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sensorDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void tryToSave2SensorsWithTheSameName() throws Exception {
    var sensorDto = new SensorDto(
        "TestSensor",
        SensorType.TEMPERATURE
    );;

    mockMvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sensorDto)))
        .andExpect(status().isOk());

    mockMvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sensorDto)))
        .andExpect(status().isConflict());
  }
}
