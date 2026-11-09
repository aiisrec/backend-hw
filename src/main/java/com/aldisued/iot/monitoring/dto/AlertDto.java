package com.aldisued.iot.monitoring.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AlertDto(
    UUID sensorId,
    String message,
    LocalDateTime timestamp
) {
}
