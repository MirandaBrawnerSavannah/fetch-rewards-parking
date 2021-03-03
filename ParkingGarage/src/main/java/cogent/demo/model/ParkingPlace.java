package cogent.demo.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "spot")
public class ParkingPlace implements Comparable<ParkingPlace> {
	
	@Id
	@Column(name = "spot_number")
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "row_number")
	private Row row;
	
	@Column(name = "position_in_row")
	private int position;
	
	@Column(name = "spot_type")
	private String spotType;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vehicle_id")
	private Vehicle vehicle;
	
	public ParkingPlace() {}

	public ParkingPlace(int id, Row row, int position, String spotType, Vehicle vehicle) {
		this.id = id;
		this.row = row;
		this.position = position;
		this.spotType = spotType;
		this.vehicle = vehicle;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Row getRow() {
		return row;
	}

	public void setRow(Row row) {
		this.row = row;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getSpotType() {
		return spotType;
	}

	public void setSpotType(String spotType) {
		this.spotType = spotType;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ParkingPlace))
			return false;
		ParkingPlace other = (ParkingPlace) obj;
		return id == other.id;
	}
	
	public boolean canHoldCar() {
		return !spotType.equals("Motorcycle");
	}
	public boolean isLarge() {
		return spotType.equals("Large");
	}
	public boolean isTaken() {
		return vehicle.isNotEmpty();
	}
	public boolean isEmpty() {
		return vehicle.isEmpty();
	}

	@Override
	public int compareTo(ParkingPlace o) {
		Integer thisId = Integer.valueOf(id);
		Integer argumentId = Integer.valueOf(o.id);
		return thisId.compareTo(argumentId);
	}
	
	@Override
	public String toString() {
		return String.format("Parking Place %d", id);
	}
	
}
