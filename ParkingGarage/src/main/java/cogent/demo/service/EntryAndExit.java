package cogent.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cogent.demo.dao.ParkingPlaceDao;
import cogent.demo.dao.VehicleDao;
import cogent.demo.model.ParkingPlace;
import cogent.demo.model.Vehicle;

@Service
public class EntryAndExit {
	
	private static final Logger logger = LoggerFactory.getLogger(EntryAndExit.class);
	
	@Autowired
	private ParkingPlaceDao placeDao;
	
	@Autowired
	private VehicleDao vehicleDao;
	
	@Autowired
	private ParkingSearch search;
	
	@Autowired
	private GarageLayout layout;

	@Transactional
	public Set<ParkingPlace> park(Vehicle vehicle, ParkingPlace parkingPlace) {
		if (search.isInGarage(vehicle)) {
			exit(vehicle);
		}
		if (!search.canParkHere(vehicle, parkingPlace)) return null;
		Set<ParkingPlace> resultSet = new HashSet<>();
		try {
			vehicle = vehicleDao.saveAndFlush(vehicle);
			int startOffset = 0;
			int endOffset = 0;
			if (vehicle.getType().equals("Bus")) {
				startOffset = -2;
				endOffset = 2;
			}
			for (int offset = startOffset; offset <= endOffset; offset++) {
				ParkingPlace tempParkingPlace = layout.getNeighbor(parkingPlace, offset);
				tempParkingPlace.setVehicle(vehicle);
				tempParkingPlace = placeDao.saveAndFlush(tempParkingPlace);
				resultSet.add(tempParkingPlace);
			}
			return resultSet;
		} catch (Exception exception) {
			logger.error(exception.getLocalizedMessage());
			return null;
		}
	}
	
	public Set<ParkingPlace> parkInNextOpen(Vehicle vehicle) {
		List<ParkingPlace> parkingPlaceList = search.getOpenSpaces(vehicle);
		if (parkingPlaceList.isEmpty()) return null;
		ParkingPlace parkingPlace = parkingPlaceList.get(0);
		return park(vehicle, parkingPlace);
	}
	
	@Transactional
	public boolean exit(Vehicle vehicle) {
		try {
			List<ParkingPlace> parkingPlaceList = placeDao.findByVehicle(vehicle);
			if (parkingPlaceList.isEmpty()) return false;
			for (ParkingPlace place: parkingPlaceList) {
				place.setVehicle(Vehicle.EMPTY);
				placeDao.save(place);
			}
			return true;
		} catch (Exception exception) {
			logger.error(exception.getLocalizedMessage());
			return false;
		}
	}
}
