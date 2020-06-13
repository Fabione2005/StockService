package dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import model.Stock;

@Repository
public class StockDaoImpl implements StockDao{

	@Autowired
	StockJpaSpring agenda;

	@Override
	public void addNewStock(Stock stock) {
		agenda.save(stock);
		
	}

	@Override
	public void deleteStock(String name) {
		agenda.deleteByName(name);
		
	}

	@Override
	public List<Stock> retriveAllStocks() {
		return agenda.findAll();
	}

	@Override
	public void deleteStock(int id) {
		agenda.deleteById(id);
	}

	@Override
	public Stock retrieveStockById(int id) {
		return agenda.findById(id).orElse(null);
	}

	@Override
	public void updateStock(Stock stock) {
		agenda.save(stock);
	}

	@Override
	public void updateStockDevelopment(int id,double developmentUpdate) {
		agenda.updateStockDevelopment(id, developmentUpdate);
	}

	@Override
	public List<Stock> retrieveStockDevelopmentLike() {
		return agenda.retrieveStockDevelopmentLike();
	}

	@Override
	public boolean saveOrUpdateAll(List<Stock> listStocks) {
		agenda.saveAll(listStocks);
		return true;
	}

	@Override
	public List<Stock> retrieveByLastUpdate(Date lastUpdate) {
		return agenda.findByLastUpdate(lastUpdate);
	}

	@Override
	public List<Stock> retrieveByDevelopmentUpdateLessValue(double developmentUpdate) {
		return agenda.retrieveByDevelopmentUpdateLessValue(developmentUpdate);
		
	}
	
}
