package cogent.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cogent.demo.model.Vehicle;

@Repository
public interface VehicleDao extends JpaRepository<Vehicle, Integer> {
	List<Vehicle> findByType(String type);
}
