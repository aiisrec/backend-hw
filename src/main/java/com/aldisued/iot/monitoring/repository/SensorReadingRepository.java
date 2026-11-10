package com.aldisued.iot.monitoring.repository;

import com.aldisued.iot.monitoring.entity.SensorReading;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

  @Query(value = """
      SELECT reading.value
      FROM sensor_readings reading
      JOIN sensors sensor ON sensor.id = reading.sensor_id
      WHERE sensor.type = :sensorType
        AND reading.timestamp BETWEEN :from AND :to
      ORDER BY reading.timestamp ASC
      """, nativeQuery = true)
  List<Double> findValuesBySensorTypeAndTimestampBetween(
      @Param("sensorType") String sensorType,
      @Param("from") LocalDateTime from,
      @Param("to") LocalDateTime to
  );

  @Query(value = """
      SELECT AVG(reading.value)
      FROM sensor_readings reading
      JOIN sensors sensor ON sensor.id = reading.sensor_id
      WHERE sensor.type = :sensorType
        AND reading.timestamp BETWEEN :from AND :to
      """, nativeQuery = true)
  Double findAverageBySensorTypeAndTimestampBetween(
      @Param("sensorType") String sensorType,
      @Param("from") LocalDateTime from,
      @Param("to") LocalDateTime to
  );
}
