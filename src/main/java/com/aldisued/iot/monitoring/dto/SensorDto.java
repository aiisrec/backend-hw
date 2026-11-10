package com.aldisued.iot.monitoring.dto;

import com.aldisued.iot.monitoring.entity.SensorType;
import jakarta.validation.constraints.NotBlank;

public record SensorDto(
    @NotBlank
    String name,
    SensorType type
) {
}
