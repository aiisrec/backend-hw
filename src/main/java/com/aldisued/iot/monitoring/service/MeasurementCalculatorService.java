package com.aldisued.iot.monitoring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MeasurementCalculatorService {

  public List<Double> filterByAverageDeviation(List<Double> values, Double deviation) {
    if (deviation == null || !Double.isFinite(deviation) || deviation < 0.0 || deviation > 1.0) {
      throw new IllegalArgumentException("Deviation must be between 0.0 and 1.0.");
    }
    if (values.isEmpty()) {
      return List.of();
    }

    double sum = 0.0;
    for (Double value : values) {
      sum += value;
    }
    double average = sum / values.size();
    double allowedDifference = Math.abs(average) * deviation;
    double lowerBound = average - allowedDifference;
    double upperBound = average + allowedDifference;

    List<Double> filteredValues = new ArrayList<>();
    for (Double value : values) {
      if (value >= lowerBound && value <= upperBound) {
        filteredValues.add(value);
      }
    }
    return filteredValues;
  }
}