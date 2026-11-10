package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AlertService.class);

  private final AlertRepository alertRepository;
  private final SensorRepository sensorRepository;
  private final KafkaTemplate<String, AlertDto> kafkaTemplate;

  public AlertService(AlertRepository alertRepository, SensorRepository sensorRepository,
      KafkaTemplate<String, AlertDto> kafkaTemplate) {
    this.alertRepository = alertRepository;
    this.sensorRepository = sensorRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Transactional
  public Alert saveAlert(AlertDto alertDto) {
    Sensor sensor = sensorRepository.findById(alertDto.sensorId()).orElse(null);
    if (sensor == null) {
      LOGGER.warn("Cannot save alert: sensor with ID {} was not found", alertDto.sensorId());
      return null;
    }

    Alert alert = alertRepository.save(new Alert(
        alertDto.message(),
        alertDto.timestamp(),
        sensor
    ));
    kafkaTemplate.send("alerts", alertDto);
    return alert;
  }

  public AlertDto findLastAlertBySensorId(UUID sensorId) {
    Alert alert = alertRepository.findFirstBySensor_IdOrderByTimestampDesc(sensorId)
        .orElse(null);
    if (alert == null) {
      return null;
    }

    return new AlertDto(
        alert.getSensor().getId(),
        alert.getMessage(),
        alert.getTimestamp()
    );
  }
}
