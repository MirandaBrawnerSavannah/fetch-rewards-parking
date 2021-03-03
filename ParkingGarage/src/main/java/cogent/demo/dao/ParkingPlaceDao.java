package cogent.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cogent.demo.model.ParkingPlace;
import cogent.demo.model.Row;
import cogent.demo.model.Vehicle;

@Repository
public interface ParkingPlaceDao extends JpaRepository<ParkingPlace, Integer> {
	List<ParkingPlace> findBySpotType(String spotType);
	List<ParkingPlace> findByRow(Row row);
	List<ParkingPlace> findByPosition(int position);
	List<ParkingPlace> findByVehicle(Vehicle vehicle);
}