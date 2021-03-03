package cogent.demo.request;

import java.io.Serializable;

public class ExitRequest implements Serializable {
	
	private static final long serialVersionUID = 9107437320766145999L;
	private Integer vehicleId;
	
	public ExitRequest() {}

	public ExitRequest(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}
	
}
