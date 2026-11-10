package com.aldisued.iot.monitoring.service.alert.strategy;

import com.aldisued.iot.monitoring.entity.SensorType;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AtmosphericPressureAlertStrategy implements ReadingAlertStrategy {

  @Override
  public SensorType getSensorType() {
    return SensorType.ATMOSPHERIC_PRESSURE;
  }

  @Override
  public Optional<String> getAlertMessage(Double value) {
    if (value < 950.0 || value > 1050.0) {
      return Optional.of(String.format(Locale.ROOT, "Pressure out of range: %.1f", value));
    }
    return Optional.empty();
  }
}
