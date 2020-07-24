package service;

import java.util.Date;
import java.util.List;

import model.Stock;

public interface StockService {
	void addStock(Stock stock);
	List<Stock> retriveStocks();
	void updateStock(Stock stock);
	boolean deleteStock(int idStock);
	Stock findStock(int id);
	Stock findStockByName(String name);
	String updateStockDevelopment(int id);
	String updateStocksByDevelopmentUpdate();
	List<Stock> retrieveStockByLastUpdate(Date lastUpdate);
	List<Stock> retrieveStockByDevelopmentUpdateLessValue(double developmentUpdate);
}
