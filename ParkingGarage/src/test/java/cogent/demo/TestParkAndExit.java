package cogent.demo;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import cogent.demo.dao.ParkingPlaceDao;
import cogent.demo.dao.RowDao;
import cogent.demo.dao.VehicleDao;
import cogent.demo.model.ParkingPlace;
import cogent.demo.model.Row;
import cogent.demo.model.Vehicle;
import cogent.demo.service.EntryAndExit;
import cogent.demo.service.ParkingSearch;

class TestParkAndExit {
	
	private static ApplicationContext context;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		context = SpringApplication.run(ParkingGarageApplication.class);
		ParkingGarageApplication.initialize(context);
	}

	
	@BeforeEach
	void updateDatabase() {
		ParkingPlaceDao parkingDao = context.getBean(ParkingPlaceDao.class);
		VehicleDao vehicleDao = context.getBean(VehicleDao.class);
		for (ParkingPlace place: parkingDao.findAll()) {
			if (place.isTaken()) {
				Vehicle vehicle = place.getVehicle();
				vehicleDao.saveAndFlush(vehicle);
			}
		}
	}

	@Test
	void testParkSuccessBus() {
		int parkingId = 727;
		EntryAndExit entryExitBean = context.getBean(EntryAndExit.class);
		ParkingPlaceDao parkingDao = context.getBean(ParkingPlaceDao.class);
		Vehicle bus = new Vehicle(1234567, "Bus");
		Optional<ParkingPlace> possibleSpot = parkingDao.findById(parkingId);
		Assertions.assertTrue(possibleSpot.isPresent());
		ParkingPlace parkingPlace = possibleSpot.get();
		Assertions.assertTrue(parkingPlace.isEmpty());
		Assertions.assertNotNull(entryExitBean.park(bus, parkingPlace));
		Assertions.assertEquals(bus, parkingPlace.getVehicle());
		entryExitBean.exit(bus);
	}
	
	@Test
	void testParkSuccessMotorcycle() {
		int parkingId = 400;
		EntryAndExit entryExitBean = context.getBean(EntryAndExit.class);
		ParkingPlaceDao parkingDao = context.getBean(ParkingPlaceDao.class);
		Vehicle motorcycle = new Vehicle(5555555, "Motorcycle");
		Optional<ParkingPlace> possibleSpot = parkingDao.findById(parkingId);
		Assertions.assertTrue(possibleSpot.isPresent());
		ParkingPlace parkingPlace = possibleSpot.get();
		Assertions.assertTrue(parkingPlace.isEmpty());
		Assertions.assertNotNull(entryExitBean.park(motorcycle, parkingPlace));
		Assertions.assertEquals(motorcycle, parkingPlace.getVehicle());
		entryExitBean.exit(motorcycle);
	}
	
	@Test
	void testParkFailBus() {
		int parkingId = 739;
		EntryAndExit entryExitBean = context.getBean(EntryAndExit.class);
		ParkingPlaceDao parkingDao = context.getBean(ParkingPlaceDao.class);
		Vehicle bus = new Vehicle(1234567, "Bus");
		Optional<ParkingPlace> possibleSpot = parkingDao.findById(parkingId);
		Assertions.assertTrue(possibleSpot.isPresent());
		ParkingPlace parkingPlace = possibleSpot.get();
		Assertions.assertTrue(parkingPlace.isEmpty());
		Assertions.assertNotNull(entryExitBean.park(bus, parkingPlace));
		Assertions.assertTrue(parkingPlace.isEmpty());
	}
	
	@Test
	void testParkFailCarInMotorcycleSpot() {
		int parkingId = 400;
		EntryAndExit entryExitBean = context.getBean(EntryAndExit.class);
		ParkingPlaceDao parkingDao = context.getBean(ParkingPlaceDao.class);
		Vehicle car = new Vehicle(2345678, "Car");
		Optional<ParkingPlace> possibleSpot = parkingDao.findById(parkingId);
		Assertions.assertTrue(possibleSpot.isPresent());
		ParkingPlace parkingPlace = possibleSpot.get();
		Assertions.assertTrue(parkingPlace.isEmpty());
		Assertions.assertNotNull(entryExitBean.park(car, parkingPlace));
		Assertions.assertTrue(parkingPlace.isEmpty());
	}
	
	@Test
	void testParkFailAlreadyTaken() {
		int parkingId = 440;
		EntryAndExit entryExitBean = context.getBean(EntryAndExit.class);
		ParkingPlaceDao parkingDao = context.getBean(ParkingPlaceDao.class);
		ParkingSearch searchBean = context.getBean(ParkingSearch.class);
		Vehicle motorcycle = new Vehicle(7654321, "Motorcycle");
		Assertions.assertFalse(searchBean.isInGarage(motorcycle));
		Optional<ParkingPlace> possibleSpot = parkingDao.findById(parkingId);
		Assertions.assertTrue(possibleSpot.isPresent());
		ParkingPlace parkingPlace = possibleSpot.get();
		Assertions.assertTrue(parkingPlace.isTaken());
		Assertions.assertNull(entryExitBean.park(motorcycle, parkingPlace));
		Assertions.assertTrue(parkingPlace.isTaken());
		Assertions.assertFalse(searchBean.isInGarage(motorcycle));
	}
	
	@Test 
	void testExitSuccess() {
		EntryAndExit entryExitBean = context.getBean(EntryAndExit.class);
		ParkingPlaceDao parkingDao = context.getBean(ParkingPlaceDao.class);
		List<ParkingPlace> parkingList = parkingDao.findAll();
		int parkingPlaceId = 0;
		Vehicle vehicle = new Vehicle(1111111, "Motorcycle");
		for (ParkingPlace spot: parkingList) {
			if (spot.isEmpty()) {
				entryExitBean.park(vehicle, spot);
				Assertions.assertFalse(parkingDao.findByVehicle(vehicle).isEmpty());
				Assertions.assertTrue(entryExitBean.exit(vehicle));
				parkingPlaceId = spot.getId();
			}
		}
		Optional<ParkingPlace> possibleSpot = parkingDao.findById(parkingPlaceId);
		Assertions.assertTrue(possibleSpot.isPresent());
		ParkingPlace spot = possibleSpot.get();
		Assertions.assertTrue(spot.isEmpty());
	}
	
	@Test
	void testExitFailNoSuchVehicle() {
		int vehicleId = -1;
		Vehicle vehicle = new Vehicle(vehicleId, "Motorcycle");
		EntryAndExit entryExitBean = context.getBean(EntryAndExit.class);
		VehicleDao vehicleDao = context.getBean(VehicleDao.class);
		Assertions.assertFalse(vehicleDao.existsById(vehicleId));
		Assertions.assertFalse(entryExitBean.exit(vehicle));
		Assertions.assertFalse(vehicleDao.existsById(vehicleId));
	}
	

}
