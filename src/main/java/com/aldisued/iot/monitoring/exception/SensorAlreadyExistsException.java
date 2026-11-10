package com.aldisued.iot.monitoring.exception;

public class SensorAlreadyExistsException extends RuntimeException {

  public SensorAlreadyExistsException(String sensorName) {
    super("A sensor with name '" + sensorName + "' already exists.");
  }
}
