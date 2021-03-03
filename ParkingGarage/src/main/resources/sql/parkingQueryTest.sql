USE parking;
SELECT * FROM parking_row;
SELECT * FROM vehicle ORDER BY vehicle_id;
SELECT * FROM spot;
SELECT * FROM spot WHERE spot_type = 'Large' AND  vehicle_id < 0
	AND row_number = 24;
SELECT * FROM spot WHERE spot_type = 'Motorcycle' AND vehicle_id > 0;
SELECT DISTINCT vehicle_id FROM spot;
SELECT * FROM spot WHERE spot_number = 401;
SELECT * FROM (SELECT COUNT(*) spots_taken FROM spot WHERE vehicle_id > 0) occupied,
	(SELECT COUNT(*) spots_available FROM spot WHERE vehicle_id < 0) available;
SELECT * FROM spot WHERE vehicle_id = 5555560;