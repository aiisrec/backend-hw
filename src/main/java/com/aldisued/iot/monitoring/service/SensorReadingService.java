package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import com.aldisued.iot.monitoring.service.alert.strategy.ReadingAlertStrategy;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SensorReadingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SensorReadingService.class);

  private final SensorReadingRepository sensorReadingRepository;
  private final SensorRepository sensorRepository;
  private final AlertRepository alertRepository;
  private final List<ReadingAlertStrategy> alertStrategies;

  public SensorReadingService(SensorReadingRepository sensorReadingRepository,
      SensorRepository sensorRepository, AlertRepository alertRepository,
      List<ReadingAlertStrategy> alertStrategies) {
    this.sensorReadingRepository = sensorReadingRepository;
    this.sensorRepository = sensorRepository;
    this.alertRepository = alertRepository;
    this.alertStrategies = alertStrategies;
  }

  @Transactional
  public SensorReading saveSensorReading(SensorReadingDto sensorReadingDto) {
    Optional<Sensor> sensor = sensorRepository.findById(sensorReadingDto.sensorId());
    if (sensor.isEmpty()) {
      LOGGER.warn("Cannot save sensor reading: sensor with ID {} was not found",
          sensorReadingDto.sensorId());
      return null;
    }
    Sensor sensorEntity = sensor.get();
    SensorReading reading = sensorReadingRepository.save(new SensorReading(
        sensorReadingDto.value(),
        sensorReadingDto.timestamp(),
        sensorEntity
    ));

    for (ReadingAlertStrategy alertStrategy : alertStrategies) {
      if (alertStrategy.getSensorType().equals(sensorEntity.getType())) {
        alertStrategy.getAlertMessage(reading.getValue()).ifPresent(message ->
            alertRepository.save(new Alert(message, reading.getTimestamp(), sensorEntity)));
        break;
      }
    }

    return reading;
  }

}
