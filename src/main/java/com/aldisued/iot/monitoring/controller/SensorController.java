package com.aldisued.iot.monitoring.controller;

import com.aldisued.iot.monitoring.dto.SensorDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.exception.SensorAlreadyExistsException;
import com.aldisued.iot.monitoring.service.SensorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sensors")
public class SensorController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SensorController.class);

  private final SensorService sensorService;

  public SensorController(SensorService sensorService) {
    this.sensorService = sensorService;
  }

  @PostMapping
  public ResponseEntity<Sensor> saveSensor(@Valid @RequestBody SensorDto sensorDto) {
    try {
      return ResponseEntity.ok(sensorService.saveSensor(sensorDto));
    } catch (SensorAlreadyExistsException exception) {
      LOGGER.warn("Sensor with name '{}' already exists", sensorDto.name());
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

}