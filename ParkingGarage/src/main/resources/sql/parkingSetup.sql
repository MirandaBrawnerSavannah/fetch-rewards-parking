CREATE SCHEMA IF NOT EXISTS parking;
USE parking;
DROP TABLE IF EXISTS vehicle;
DROP TABLE IF EXISTS spot;
DROP TABLE IF EXISTS vehicle;
DROP TABLE IF EXISTS parking_row;
CREATE TABLE parking_row (
	row_number INT PRIMARY KEY,
    floor INT
);
CREATE TABLE vehicle (
	vehicle_id INT PRIMARY KEY,
    vehicle_type VARCHAR(255)
);
CREATE TABLE spot (
	spot_number INT PRIMARY KEY,
    row_number INT,
    position_in_row INT,
    spot_type VARCHAR(255),
    vehicle_id INT,
    FOREIGN KEY (row_number) REFERENCES parking_row (row_number) 
		ON DELETE SET NULL,
	FOREIGN KEY (vehicle_id) REFERENCES vehicle (vehicle_id)
		ON DELETE SET NULL
);