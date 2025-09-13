CREATE DATABASE meddb;
USE meddb;

CREATE TABLE reminder (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_name   VARCHAR(100),
    medicine_name  VARCHAR(100),
    dose           VARCHAR(50),
    doctor_name    VARCHAR(100),
    instructions   VARCHAR(255),
    reminder_time  DATETIME
);
