package cogent.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import cogent.demo.dao.ParkingPlaceDao;
import cogent.demo.dao.RowDao;
import cogent.demo.dao.VehicleDao;
import cogent.demo.model.ParkingPlace;
import cogent.demo.model.Row;
import cogent.demo.model.Vehicle;

@SpringBootApplication
public class ParkingGarageApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ParkingGarageApplication.class, args);
		
		/* When launching the application for the first time, run the initialize method to add 
		 some parking places and vehicles to the database. */
		// initialize(context);
	}

	public static void initialize(ApplicationContext context) {
		int maxSpotsInRow = 30;
		int maxRowsInFloor = 12;
		RowDao rowDao = context.getBean(RowDao.class);
		ParkingPlaceDao placeDao = context.getBean(ParkingPlaceDao.class);
		VehicleDao vehicleDao = context.getBean(VehicleDao.class);
		Vehicle yellowBus = new Vehicle(1, "Bus");
		yellowBus = vehicleDao.save(yellowBus);
		Vehicle redBus = new Vehicle(2, "Bus");
		redBus = vehicleDao.save(redBus);
		vehicleDao.save(Vehicle.EMPTY);
		int vehicleIndex = 3;
		for (int floor = 1; floor <= 7; floor++) {
			for (int rowNumberInFloor = 1; rowNumberInFloor <= maxRowsInFloor; rowNumberInFloor++) {
				int rowId = rowNumberInFloor + (floor * maxRowsInFloor);
				Row row = new Row(rowId, floor);
				row = rowDao.save(row);
				int spotsInRow = maxSpotsInRow;
				if (rowNumberInFloor % 2 == 1) {
					spotsInRow = 15;
				}
				for (int position = 1; position <= spotsInRow; position++) {
					int placeId = position + (row.getId() * maxSpotsInRow);
					String spotType = "Motorcycle";
					Vehicle vehicle = Vehicle.EMPTY;
					if (rowNumberInFloor % 2 == 0 && position < 20) {
						spotType = "Large";
						if (5 <= position && position <= 9) {
							if (floor == 1) {
								vehicle = yellowBus;
							} else if (floor == 3) {
								vehicle = redBus;
							}
						} else if (Math.random() < 0.1) {
							vehicle = new Vehicle(vehicleIndex, "Motorcycle");
							vehicleDao.save(vehicle);
							vehicleIndex++;
						} else if (Math.random() < 0.45) {
							vehicle = new Vehicle(vehicleIndex, "Car");
							vehicleDao.save(vehicle);
							vehicleIndex++;
						}
					} else if (position < 10) {
						spotType = "Compact";
						if (Math.random() < 0.15) {
							vehicle = new Vehicle(vehicleIndex, "Motorcycle");
							vehicleDao.save(vehicle);
							vehicleIndex++;
						} else if (Math.random() < 0.3) {
							vehicle = new Vehicle(vehicleIndex, "Car");
							vehicleDao.save(vehicle);
							vehicleIndex++;
						}
					} else {
						if (Math.random() < 0.55) {
							vehicle = new Vehicle(vehicleIndex, "Motorcycle");
							vehicleDao.save(vehicle);
							vehicleIndex++;
						}
					}
					ParkingPlace parkingPlace = new ParkingPlace(
							placeId, row, position, spotType, vehicle);
					parkingPlace = placeDao.save(parkingPlace);
				}
			}
		}
	}
}
