package com.aldisued.iot.monitoring.service.alert.strategy;

import com.aldisued.iot.monitoring.entity.SensorType;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class TemperatureAlertStrategy implements ReadingAlertStrategy {

  @Override
  public SensorType getSensorType() {
    return SensorType.TEMPERATURE;
  }

  @Override
  public Optional<String> getAlertMessage(Double value) {
    if (value > 40.0 || value < -20.0) {
      return Optional.of(String.format(Locale.ROOT, "Temperature out of range: %.1f", value));
    }
    return Optional.empty();
  }
}
