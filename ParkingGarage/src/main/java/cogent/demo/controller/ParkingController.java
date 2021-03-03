package cogent.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cogent.demo.dao.ParkingPlaceDao;
import cogent.demo.dao.VehicleDao;
import cogent.demo.model.ParkingPlace;
import cogent.demo.model.Vehicle;
import cogent.demo.request.ExitRequest;
import cogent.demo.request.ParkRequest;
import cogent.demo.response.ParkResponse;
import cogent.demo.service.EntryAndExit;
import cogent.demo.service.ParkingSearch;
import cogent.demo.service.VehicleLog;

@RestController
@CrossOrigin
public class ParkingController {
	
	@Autowired
	private VehicleLog vehicleLog;
	
	@Autowired
	private EntryAndExit entryExit;
	
	@Autowired
	private ParkingSearch parkingSearch;
	
	@Autowired
	private ParkingPlaceDao parkingDao;
	
	@Autowired
	private VehicleDao vehicleDao;
	
	@GetMapping(path = "/showAll", produces = "application/json")
	public List<ParkingPlace> showAll() {
		return parkingDao.findAll();
	}
	
	@GetMapping(path = "/showOpenings", produces = "application/json")
	public List<ParkingPlace> showOpenings(
			@RequestParam(name = "vehicleType", required = false) String vehicleType) {
		if (vehicleType == null) vehicleType = "Motorcycle";
		return parkingSearch.getOpenSpaces(vehicleType);
	}

	@PostMapping(path = "/park", produces = "application/json")
	public ResponseEntity<?> parkAnywhere(@RequestBody ParkRequest request) {
		if (request == null || request.getVehicleType() == null) {
			return ResponseEntity.badRequest().body("The request is empty.");
		}
		Vehicle vehicle = new Vehicle(request.getVehicleType());
		boolean idAssigned = vehicleLog.assignId(vehicle);
		if (!idAssigned) {
			return ResponseEntity.status(500).build();
		}
		Set<ParkingPlace> parkingSet = null;
		if (request.getParkingPlace() == null) {
			parkingSet = entryExit.parkInNextOpen(vehicle);
			if (parkingSet == null || parkingSet.isEmpty()) {
				return ResponseEntity.status(503).body(
						"No available parking places at the moment.");
			}
		} else {
			Optional<ParkingPlace> possible = parkingDao.findById(request.getParkingPlace());
			if (possible.isEmpty()) {
				return ResponseEntity.badRequest().body("No parking place with the id "
						+ request.getParkingPlace() + " exists in this garage.");
			}
			ParkingPlace parkingPlace = possible.get();
			parkingSet = entryExit.park(vehicle, parkingPlace);
			if (parkingSet == null || parkingSet.isEmpty()) {
				return ResponseEntity.status(503).body(
						"The requested parking place is not available at the moment.");
			}
		}
		ParkResponse responseContent = new ParkResponse(vehicle.getId(), parkingSet);
		return ResponseEntity.ok(responseContent);
	}
	
	@PostMapping(path = "/exit", produces = "application/json")
	public ResponseEntity<?> exit(@RequestBody ExitRequest request) {
		if (request == null || request.getVehicleId() == null) {
			return ResponseEntity.badRequest().body("The request is empty.");
		}
		Optional<Vehicle> possible = vehicleDao.findById(request.getVehicleId());
		if (possible.isEmpty()) {
			return ResponseEntity.status(404).body(
					"We could not find any vehicle with that id.");
		} 
		Vehicle vehicle = possible.get();
		if (!parkingSearch.isInGarage(vehicle)) {
			return ResponseEntity.status(404).body(
					"We could not find any vehicle with that id currently in the garage.");
		}
		if (!entryExit.exit(vehicle)) {
			return ResponseEntity.status(500).build();
		}
		return ResponseEntity.ok("You are now checked out. Have a good day.");
	}
	
}
