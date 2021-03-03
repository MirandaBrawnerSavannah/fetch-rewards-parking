package cogent.demo.request;

import java.io.Serializable;

public class ParkRequest implements Serializable {

	private static final long serialVersionUID = 7583455792810283243L;
	private String vehicleType;
	private Integer parkingPlace;
	
	public ParkRequest() {}

	public ParkRequest(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public ParkRequest(String vehicleType, Integer parkingPlace) {
		this.vehicleType = vehicleType;
		this.parkingPlace = parkingPlace;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Integer getParkingPlace() {
		return parkingPlace;
	}

	public void setParkingPlace(Integer parkingPlace) {
		this.parkingPlace = parkingPlace;
	}
}
