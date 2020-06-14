package dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import model.Stock;

@Repository
public interface StockJpaSpring extends JpaRepository<Stock, Integer>{
	Stock findByName(String name);
	List<Stock> findByLastUpdate(Date lastUpdate);
	
	@Transactional
	@Modifying
	@Query("Delete from Stock c Where c.name=?1")
	void deleteByName(String email);
	
	@Transactional
	@Modifying
	@Query("Update Stock c set c.developmentUpdate =:developmentUpdate where c.id=:id")
	void updateStockDevelopment(@Param("id")int id,@Param("developmentUpdate") double developmentUpdate);
	
	
	@Transactional
	@Query("Select c FROM Stock c WHERE c.developmentUpdate = 0")
	List<Stock> retrieveStockDevelopmentLike();
	
	@Transactional
	@Query("Select c FROM Stock c WHERE c.developmentUpdate < :developmentUpdate")
	List<Stock> retrieveByDevelopmentUpdateLessValue(@Param("developmentUpdate")double developmentUpdate);
	
}
