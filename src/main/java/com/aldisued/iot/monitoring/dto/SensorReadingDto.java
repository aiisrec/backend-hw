package com.aldisued.iot.monitoring.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SensorReadingDto(
    UUID sensorId,
    Double value,
    LocalDateTime timestamp
) {}
