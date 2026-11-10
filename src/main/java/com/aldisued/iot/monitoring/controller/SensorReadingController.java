package com.aldisued.iot.monitoring.controller;

import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.service.SensorReadingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sensor-readings")
public class SensorReadingController {

  private final SensorReadingService sensorReadingService;

  public SensorReadingController(SensorReadingService sensorReadingService) {
    this.sensorReadingService = sensorReadingService;
  }

  @PostMapping
  public ResponseEntity<SensorReading> saveSensorReading(
      @RequestBody SensorReadingDto sensorReadingDto) {
    SensorReading sensorReading = sensorReadingService.saveSensorReading(sensorReadingDto);
    if (sensorReading == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.ok(sensorReading);
  }
}
