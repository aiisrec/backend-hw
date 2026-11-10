package com.aldisued.iot.monitoring.service.alert.strategy;

import com.aldisued.iot.monitoring.entity.SensorType;
import java.util.Optional;

public interface ReadingAlertStrategy {

  SensorType getSensorType();

  Optional<String> getAlertMessage(Double value);
}
