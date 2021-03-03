package cogent.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cogent.demo.dao.ParkingPlaceDao;
import cogent.demo.model.ParkingPlace;
import cogent.demo.model.Vehicle;

@Service
public class ParkingSearch {
	
	@Autowired
	private GarageLayout layout;
	
	@Autowired
	private ParkingPlaceDao parkingDao;

	public boolean canParkHere(String vehicleType, ParkingPlace parkingPlace) {
		if (vehicleType.equals("Bus")) {
			boolean soFarSoGood = true;
			for (int offset = -2; offset <= 2 && soFarSoGood; offset++) {
				ParkingPlace neighbor = layout.getNeighbor(parkingPlace, offset);
				if (neighbor == null || neighbor.isTaken() || !neighbor.isLarge()) {
					soFarSoGood = false;
				}
			}
			return soFarSoGood;
		} else if (vehicleType.equals("Car")) {
			return parkingPlace.canHoldCar() && parkingPlace.isEmpty();
		} else if (vehicleType.equals("Motorcycle")) {
			return parkingPlace.isEmpty();
		} else return false;
	}
	
	public boolean canParkHere(Vehicle vehicle, ParkingPlace parkingPlace) {
		return canParkHere(vehicle.getType(), parkingPlace);
	}
	
	public boolean isInGarage(Vehicle vehicle) {
		return !parkingDao.findByVehicle(vehicle).isEmpty();
	}
	
	public int numberOfOpenSpaces(String vehicleType) {
		List<ParkingPlace> spaces = parkingDao.findByVehicle(Vehicle.EMPTY);
		long count = spaces.parallelStream().filter(
				place -> canParkHere(vehicleType, place)).count();
		return (int) count;
	}
	
	public int numberOfOpenSpaces(Vehicle vehicle) {
		return numberOfOpenSpaces(vehicle.getType());
	}
	
	public List<ParkingPlace> getOpenSpaces(String vehicleType) {
		List<ParkingPlace> spaces = parkingDao.findByVehicle(Vehicle.EMPTY);
		return spaces.parallelStream().filter(place -> canParkHere(vehicleType, place))
		.sorted().collect(Collectors.toList());
	}
	
	public List<ParkingPlace> getOpenSpaces(Vehicle vehicle) {
		return getOpenSpaces(vehicle.getType());
	}
}
