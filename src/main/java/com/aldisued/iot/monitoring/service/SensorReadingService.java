package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SensorReadingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SensorReadingService.class);

  private final SensorReadingRepository sensorReadingRepository;
  private final SensorRepository sensorRepository;

  public SensorReadingService(SensorReadingRepository sensorReadingRepository,
      SensorRepository sensorRepository) {
    this.sensorReadingRepository = sensorReadingRepository;
    this.sensorRepository = sensorRepository;
  }

    @Transactional
    public SensorReading saveSensorReading(SensorReadingDto sensorReadingDto) {
      Sensor sensor = sensorRepository.findById(sensorReadingDto.sensorId()).orElse(null);
      if (sensor == null) {
        LOGGER.warn("Cannot save sensor reading: sensor with ID {} was not found",
            sensorReadingDto.sensorId());
        return null;
      }

      return sensorReadingRepository.save(new SensorReading(
          sensorReadingDto.value(),
          sensorReadingDto.timestamp(),
          sensor
      ));
    }

}
