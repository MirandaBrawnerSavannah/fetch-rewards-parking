package cogent.demo.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parking_row")
public class Row implements Comparable<Row> {

	@Id
	@Column(name = "row_number")
	private int id;
	
	@Column(name = "floor")
	private int floor;
	
	public Row() {}

	public Row(int id, int floor) {
		this.id = id;
		this.floor = floor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Row))
			return false;
		Row other = (Row) obj;
		return id == other.id;
	}

	@Override
	public int compareTo(Row o) {
		Integer thisId = Integer.valueOf(id);
		Integer argumentId = Integer.valueOf(o.id);
		return thisId.compareTo(argumentId);
	}
	
	
}
