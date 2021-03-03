package cogent.demo.response;

import java.io.Serializable;
import java.util.Set;

import cogent.demo.model.ParkingPlace;

public class ParkResponse implements Serializable {

	private static final long serialVersionUID = 4587771481831464335L;
	private int vehicleId;
	private Set<ParkingPlace> parkingPlaces;
	
	public ParkResponse() {}

	public ParkResponse(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public ParkResponse(int vehicleId, Set<ParkingPlace> parkingPlaces) {
		this.vehicleId = vehicleId;
		this.parkingPlaces = parkingPlaces;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Set<ParkingPlace> getParkingPlaces() {
		return parkingPlaces;
	}

	public void setParkingPlaces(Set<ParkingPlace> parkingPlaces) {
		this.parkingPlaces = parkingPlaces;
	}
	
	
}
