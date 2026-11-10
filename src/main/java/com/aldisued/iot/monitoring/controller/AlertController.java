package com.aldisued.iot.monitoring.controller;

import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.service.AlertService;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alerts")
public class AlertController {

  private final AlertService alertService;

  public AlertController(AlertService alertService) {
    this.alertService = alertService;
  }

  @GetMapping("/latest")
  public ResponseEntity<AlertDto> findLatestAlert(@RequestParam UUID sensorId) {
    AlertDto alert = alertService.findLastAlertBySensorId(sensorId);
    if (alert == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(alert);
  }
}
