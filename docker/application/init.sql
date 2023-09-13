create DATABASE carpark;
\c carpark
create table car_park_location (
    car_park_no varchar(10) primary key,
    address varchar(100),
    longitude float,
    latitude float
);

create table car_park_availability (
    car_park_no varchar(10),
    lot_type varchar(10),
    total_lots integer,
    available_lots integer,
    updated_at timestamp,
    primary key(car_park_no, lot_type)
);
