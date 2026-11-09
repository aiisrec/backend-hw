INSERT INTO sensors (id, name, type) VALUES ('e3242ea2-0514-46d3-aad8-b2012980c41c', 'Temperature Sensor 1',  'TEMPERATURE');
INSERT INTO sensors (id, name, type) VALUES ('f94001f4-1450-4b73-8b7e-7a9491bc3695', 'Temperature Sensor 2',  'TEMPERATURE');
INSERT INTO sensors (id, name, type) VALUES ('ac723c77-955f-469d-9d6a-d56bac39c202', 'Humidity Sensor 1',  'HUMIDITY');

INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (1, 15, '2020-06-22 19:10:25', 'e3242ea2-0514-46d3-aad8-b2012980c41c');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (2, 18, '2020-06-22 19:12:56', 'f94001f4-1450-4b73-8b7e-7a9491bc3695');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (3, 20, '2020-06-23 21:45:56', 'e3242ea2-0514-46d3-aad8-b2012980c41c');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (4, 21, '2020-06-23 21:48:16', 'f94001f4-1450-4b73-8b7e-7a9491bc3695');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (5, 12, '2020-06-24 18:23:12', 'e3242ea2-0514-46d3-aad8-b2012980c41c');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (6, 13, '2020-06-24 18:42:00', 'f94001f4-1450-4b73-8b7e-7a9491bc3695');

INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (7, 0.45, '2020-06-22 16:22:22', 'ac723c77-955f-469d-9d6a-d56bac39c202');
INSERT INTO sensor_readings(id, value, timestamp, sensor_id) VALUES (8, 0.48, '2020-06-23 18:56:43', 'ac723c77-955f-469d-9d6a-d56bac39c202');