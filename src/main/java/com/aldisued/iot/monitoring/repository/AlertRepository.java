package com.aldisued.iot.monitoring.repository;

import com.aldisued.iot.monitoring.entity.Alert;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, String> {

  Optional<Alert> findFirstBySensor_IdOrderByTimestampDesc(UUID sensorId);
}
