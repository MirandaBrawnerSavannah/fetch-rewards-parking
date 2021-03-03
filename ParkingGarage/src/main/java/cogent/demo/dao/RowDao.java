package cogent.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cogent.demo.model.Row;

@Repository
public interface RowDao extends JpaRepository<Row, Integer> {
	List<Row> findByFloor(int floor);
}
