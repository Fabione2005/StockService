package dao;

import java.util.Date;
import java.util.List;

import model.Stock;

public interface StockDao {
	void addNewStock(Stock stock);
	
	void deleteStock(String name);
	
	List<Stock> retriveAllStocks();
	
	void deleteStock(int id);
	
	Stock retrieveStockById(int id);
	
	void updateStock(Stock stock);
	
	void updateStockDevelopment(int id,double developmentUpdate);
	
	List<Stock> retrieveStockDevelopmentLike();
	
	boolean saveOrUpdateAll(List<Stock> listStocks);
	
	List<Stock> retrieveByLastUpdate(Date lastUpdate);
	
	List<Stock> retrieveByDevelopmentUpdateLessValue(double developmentUpdate);
	
}
