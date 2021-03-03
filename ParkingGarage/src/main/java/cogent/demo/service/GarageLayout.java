package cogent.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cogent.demo.dao.ParkingPlaceDao;
import cogent.demo.model.ParkingPlace;
import cogent.demo.model.Row;

@Service
public class GarageLayout {

	@Autowired
	private ParkingPlaceDao placeDao;
	
	public ParkingPlace findByRowAndPosition(Row row, int position) {
		if (row == null) return null;
		List<ParkingPlace> placesInRow = placeDao.findByRow(row);
		for (ParkingPlace place: placesInRow) {
			if (place != null && place.getPosition() == position) {
				return place;
			}
		}
		return null;
	}
	
	public ParkingPlace getNeighbor(ParkingPlace place, int offset) {
		if (place == null) return null;
		Row row = place.getRow();
		if (row == null) return null;
		return findByRowAndPosition(row, place.getPosition() + offset);
	}
	
	public List<ParkingPlace> getByFloor(int floor) {
		List<ParkingPlace> parkingPlaceList = placeDao.findAll();
		return parkingPlaceList.parallelStream()
				.filter(place -> place != null && place.getRow() != null 
				&& place.getRow().getFloor() == floor).sorted()
				.collect(Collectors.toList());
	}
}
