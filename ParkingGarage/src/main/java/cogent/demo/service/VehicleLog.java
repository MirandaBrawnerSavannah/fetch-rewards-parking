package cogent.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cogent.demo.dao.VehicleDao;
import cogent.demo.model.Vehicle;

@Service
public class VehicleLog {
	
	@Autowired
	private VehicleDao dao;

	public boolean assignId(Vehicle vehicle) {
		List<Vehicle> vehicleList = dao.findAll().parallelStream()
				.filter(v -> v.getId() > 0)
				.sorted().collect(Collectors.toList());
		int nextId = 1;
		if (!vehicleList.isEmpty()) {
			int highestId = vehicleList.get(vehicleList.size() - 1).getId();
			if (highestId == Integer.MAX_VALUE) {
				return false;
			}
			nextId = highestId + 1;
		}
		vehicle.setId(nextId);
		dao.saveAndFlush(vehicle);
		return true;
	}
}
