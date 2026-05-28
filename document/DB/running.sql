drop database running;
create database running;
use running;

drop table running_data;
create table running_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mean_heart_rate INT,
    exercise_type VARCHAR(10),
    start_time DATETIME,
    mean_speed DOUBLE,
    distance DOUBLE,
    calorie DOUBLE,
    duration_seconds BIGINT,
    pace VARCHAR(20)
);