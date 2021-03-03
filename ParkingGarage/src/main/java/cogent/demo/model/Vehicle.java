package cogent.demo.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vehicle")
public class Vehicle implements Comparable<Vehicle> {
	
	public static final Vehicle EMPTY = new Vehicle(Integer.MIN_VALUE, "EMPTY");
	
	@Id
	@Column(name = "vehicle_id")
	private int id;
	
	@Column(name = "vehicle_type")
	private String type;
	
	public Vehicle() {}
	
	public Vehicle(String type) {
		this.type = type;
	}
	
	public Vehicle(int id, String type) {
		this.id = id;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Vehicle))
			return false;
		Vehicle other = (Vehicle) obj;
		return id == other.id;
	}
	
	public boolean isEmpty() {
		return equals(EMPTY);
	}
	
	public boolean isNotEmpty() {
		return !equals(EMPTY);
	}

	@Override
	public int compareTo(Vehicle o) {
		Integer thisId = Integer.valueOf(id);
		Integer argumentId = Integer.valueOf(o.id);
		return thisId.compareTo(argumentId);
	}
}
