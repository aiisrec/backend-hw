INSERT INTO sensors (id, name, type) VALUES ('e3242ea2-0514-46d3-aad8-b2012980c41c', 'Temperature Sensor',  'TEMPERATURE');
INSERT INTO sensors (id, name, type) VALUES ('f94001f4-1450-4b73-8b7e-7a9491bc3695', 'Atmospheric Pressure Sensor',  'ATMOSPHERIC_PRESSURE');
INSERT INTO sensors (id, name, type) VALUES ('ac723c77-955f-469d-9d6a-d56bac39c202', 'Humidity Sensor',  'HUMIDITY');

INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (1, 98.4, '2020-06-22 06:00:00', 'f94001f4-1450-4b73-8b7e-7a9491bc3695');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (2, 0.45, '2020-06-22 06:00:00', 'ac723c77-955f-469d-9d6a-d56bac39c202');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (3, 15.8, '2020-06-22 06:00:00', 'e3242ea2-0514-46d3-aad8-b2012980c41c');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (4, 17.3, '2020-06-24 06:00:00', 'e3242ea2-0514-46d3-aad8-b2012980c41c');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (5, 95.4, '2020-06-24 06:00:00', 'f94001f4-1450-4b73-8b7e-7a9491bc3695');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (6, 0.41, '2020-06-24 06:00:00', 'ac723c77-955f-469d-9d6a-d56bac39c202');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (7, 16.1, '2020-06-23 06:00:00', 'e3242ea2-0514-46d3-aad8-b2012980c41c');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (8, 98.2, '2020-06-23 06:00:00', 'f94001f4-1450-4b73-8b7e-7a9491bc3695');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (9, 0.44, '2020-06-23 06:00:00', 'ac723c77-955f-469d-9d6a-d56bac39c202');
