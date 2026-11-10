package com.aldisued.iot.monitoring.service.alert.strategy;

import com.aldisued.iot.monitoring.entity.SensorType;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class HumidityAlertStrategy implements ReadingAlertStrategy {

  @Override
  public SensorType getSensorType() {
    return SensorType.HUMIDITY;
  }

  @Override
  public Optional<String> getAlertMessage(Double value) {
    if (value > 90.0) {
      return Optional.of(String.format(Locale.ROOT, "Humidity too high: %.1f", value));
    }
    return Optional.empty();
  }
}
