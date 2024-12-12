-- liquibase formatted sql

-- changeset user:1730897093924-22
-- Добавление пользователей
INSERT INTO "user" (first_name, last_name, patronymic, phone_number, created_at)
VALUES ('John', 'Doe', 'Ivanovich', '81234567891', '2023-01-01 10:00:00');
INSERT INTO "user" (first_name, last_name, patronymic, phone_number, created_at)
VALUES ('Jane', 'Smith', 'Sergeevna', '89876543210', '2023-02-01 10:00:00');
INSERT INTO "user" (first_name, last_name, patronymic, phone_number, created_at)
VALUES ('Alice', 'Brown', 'Egorovna', '85555555555', '2023-03-01 10:00:00');
INSERT INTO "user" (first_name, last_name, patronymic, phone_number, created_at)
VALUES ('Bob', 'Green', 'Alexandrovich', '86666666666', '2023-04-01 10:00:00');

-- changeset user:1730897093924-23
-- Добавление данных для auth_data (пользователи с ролями)
INSERT INTO auth_data (email, password, role, user_id)
VALUES ('user1@example.com', '$2a$10$O129c6DEgGCGEHG7TmwaBOGwScOQIX9jSDY/u/939XIWX6JvsGI1i', 'ROLE_CLIENT', 1);
INSERT INTO auth_data (email, password, role, user_id)
VALUES ('user2@example.com', '$2a$10$O129c6DEgGCGEHG7TmwaBOGwScOQIX9jSDY/u/939XIWX6JvsGI1i', 'ROLE_CLIENT', 2);
INSERT INTO auth_data (email, password, role, user_id)
VALUES ('admin@example.com', '$2a$10$O129c6DEgGCGEHG7TmwaBOGwScOQIX9jSDY/u/939XIWX6JvsGI1i', 'ROLE_ADMIN', 3);
INSERT INTO auth_data (email, password, role, user_id)
VALUES ('manager@example.com', '$2a$10$O129c6DEgGCGEHG7TmwaBOGwScOQIX9jSDY/u/939XIWX6JvsGI1i', 'ROLE_MANAGER', 4);

-- changeset user:1730897093924-24
-- Добавление механиков
INSERT INTO mechanic (initials)
VALUES ('Ivanov M. S.');
INSERT INTO mechanic (initials)
VALUES ('Puchkov D. Y.');

-- changeset user:1730897093924-25
-- Добавление автомобилей
INSERT INTO vehicle (license_plate, model, client_id)
VALUES ('K001OK52', 'Toyota Camry', 1);
INSERT INTO vehicle (license_plate, model, client_id)
VALUES ('S999YS52', 'Honda Civic', 2);
INSERT INTO vehicle (license_plate, model, client_id)
VALUES ('C666CC152', 'Ford Focus', 2);

-- changeset user:1730897093924-26
-- Добавление расписаний
INSERT INTO schedule (start_date, end_date, manager_id, mechanic_id)
VALUES ('2024-11-07 14:00:00', '2024-11-07 18:00:00', 4, 1);
INSERT INTO schedule (start_date, end_date, manager_id, mechanic_id)
VALUES ('2024-11-08 10:00:00', '2024-11-08 14:00:00', 4, 2);

-- changeset user:1730897093924-27
-- Добавление заявок
INSERT INTO appointment (status, appointment_date, client_id, vehicle_id, service_type, schedule_id)
VALUES ('NEW', '2024-11-06 10:00:00', 1, 1, 'REPAIR', NULL);
INSERT INTO appointment (status, appointment_date, client_id, vehicle_id, service_type, schedule_id)
VALUES ('AT_WORK', '2024-11-07 14:00:00', 2, 2, 'DIAGNOSTIC', 1);
INSERT INTO appointment (status, appointment_date, client_id, vehicle_id, service_type, schedule_id)
VALUES ('DONE', '2024-11-07 16:00:00', 2, 3, 'DIAGNOSTIC', 2);

-- changeset user:1730897093924-28
-- Обновление расписания с назначением на заявки
UPDATE schedule
SET appointment_id = 2
WHERE schedule_id = 1;
UPDATE schedule
SET appointment_id = 3
WHERE schedule_id = 2;

