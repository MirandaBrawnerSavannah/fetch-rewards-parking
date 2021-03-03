package cogent.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.util.StringUtils;

import cogent.demo.dao.ParkingPlaceDao;
import cogent.demo.model.ParkingPlace;
import cogent.demo.service.ParkingSearch;

class TestSearch {
	
	private static ApplicationContext context;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		context = SpringApplication.run(ParkingGarageApplication.class);
	}

	@Test
	void testCountAvailable() {
		ParkingSearch searchBean = context.getBean(ParkingSearch.class);
		ParkingPlaceDao dao = context.getBean(ParkingPlaceDao.class);
		String[] types = {"Motorcycle", "Car", "Bus"};
		for (String type: types) {
			int count = 0;
			for (ParkingPlace parkingPlace: dao.findAll()) {
				if (searchBean.canParkHere(type, parkingPlace)) {
					count++;
				}
			}
			int streamCount = searchBean.numberOfOpenSpaces(type);
			Assertions.assertEquals(count, streamCount, String.format("The number of the open "
					+ "parking places did not match the expected value for "
					+ "%ss.\n", type));
		}
	}
	
	@Test
	void getAvailableSpaces() {
		ParkingSearch searchBean = context.getBean(ParkingSearch.class);
		ParkingPlaceDao dao = context.getBean(ParkingPlaceDao.class);
		String[] types = {"Motorcycle", "Car", "Bus"};
		for (String type: types) {
			List<ParkingPlace> testList = new ArrayList<>();
			for (ParkingPlace parkingPlace: dao.findAll()) {
				if (searchBean.canParkHere(type, parkingPlace)) {
					testList.add(parkingPlace);
				}
			}
			Collections.sort(testList); 
			List<ParkingPlace> streamList = searchBean.getOpenSpaces(type);
			Assertions.assertEquals(testList.size(), streamList.size());
			for (int i = 0; i < streamList.size(); i++) {
				String testString = StringUtils.toString(testList.get(i));
				String streamString = StringUtils.toString(streamList.get(i));
				String message = String.format("The parking places at index %d "
						+ "did not match. The list that used streams had parking place "
						+ "%s, while the test list without streams had parking place %s.",
						i, streamString, testString);
				Assertions.assertEquals(testList.get(i), streamList.get(i), message);
			}
		}
	}
}
