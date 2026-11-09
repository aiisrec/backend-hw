package com.aldisued.iot.monitoring.dto;

import com.aldisued.iot.monitoring.entity.SensorType;

public record SensorDto(
    String name,
    SensorType type
) {
}
