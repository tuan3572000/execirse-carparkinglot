CREATE DATABASE carparkinglot;
\c carparkinglot
Create TABLE car_park_location (
    car_park_no varchar(10) PRIMARY KEY,
    address varchar(100),
    longitude float,
    latitude float
);

Create TABLE car_park_availability (
    car_park_no varchar(10),
    lot_type varchar(10),
    total_lots integer,
    available_lots integer,
    updated_at timestamp,
    PRIMARY KEY(car_park_no, lot_type)
);
