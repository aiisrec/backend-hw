package com.aldisued.iot.monitoring.tasks;

import com.aldisued.iot.monitoring.service.MeasurementCalculatorService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class Task9Tests {

  private final MeasurementCalculatorService measurementCalculatorService
      = new MeasurementCalculatorService();

  @ParameterizedTest
  @MethodSource("filteredValuesByAverageDeviationSource")
  public void verifyFilteredAverageDeviation(
      List<Double> values,
      Double deviation,
      List<Double> expectedValues) {
    List<Double> filteredByAverageDeviation = measurementCalculatorService.filterByAverageDeviation(
        values, deviation);

    Assertions.assertEquals(expectedValues, filteredByAverageDeviation);
  }

  @ParameterizedTest
  @ValueSource(doubles = {-0.1, 1.1, 2.0})
  public void verifyInvalidDeviationHandling(Double deviation) {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> measurementCalculatorService.filterByAverageDeviation(Collections.emptyList(),
            deviation));
  }

  static Stream<Arguments> filteredValuesByAverageDeviationSource() {
    return Stream.of(
        Arguments.of(
            List.of(10.0, 11.0, 12.0),
            0.1,
            List.of(10.0, 11.0, 12.0)
        ),
        Arguments.of(
            List.of(10.0, 15.0, 20.0),
            0.3,
            List.of(15.0)
        ),
        Arguments.of(
            List.of(0.0, 100.0),
            0.99,
            Collections.emptyList()
        ),
        Arguments.of(
            Collections.emptyList(),
            0.3,
            Collections.emptyList()
        )
    );
  }

}
