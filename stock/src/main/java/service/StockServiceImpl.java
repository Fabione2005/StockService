package service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.StockDao;
import model.Stock;

@Service
public class StockServiceImpl implements StockService{

	@Autowired
	StockDao dao;
	
	@Override
	public boolean addStock(Stock stock) {
		
		if(dao.retrieveStockById(stock.getId()) == null) 
		{
			dao.addNewStock(stock);
			return true;
		}
		return false;
	}

	@Override
	public List<Stock> retriveStocks() {
		return dao.retriveAllStocks();
	}

	@Override
	public void updateStock(Stock stock) {
		
		if(dao.retrieveStockById(stock.getId()) != null) 
		{
			dao.updateStock(stock);
		}
	}

	@Override
	public boolean deleteStock(int idStock) {
		
		if(dao.retrieveStockById(idStock) != null) 
		{
			dao.deleteStock(idStock);
			return true;
		}
		
		return false;
	}

	@Override
	public Stock findStock(int id) {
		return dao.retrieveStockById(id);
	}

	@Override
	public String updateStockDevelopment(int id) {
		String result = null;
		Stock stock = dao.retrieveStockById(id);
		
		if(stock != null) 
		{
			double newDevelopmentValue = Math.floor(stock.getDevelopmentUpdate());
			dao.updateStockDevelopment(id,newDevelopmentValue);
			result = "";
		}
		else
		{
			result = "stock not found";
		}
		
		
		return result;
	}

	@Override
	public String updateStocksByDevelopmentUpdate() {
		String result = null;
		
		List<Stock> stocksToUpdate = dao.retrieveStockDevelopmentLike();
		
		if(stocksToUpdate != null && stocksToUpdate.size() > 0) 
		{
			stocksToUpdate.stream().forEach(i -> i.setDevelopmentUpdate());
			
			if(stocksToUpdate.stream().anyMatch(i -> i.getDevelopmentUpdate() == 0)) 
			{
				result = "an error has ocurred";
			}
			else
			{
				result = dao.saveOrUpdateAll(stocksToUpdate) ? "success" : "an error has ocurred";
			}
		}
		else
		{
			result = "no stocks where found";
		}
		
		return result;
	}

	@Override
	public List<Stock> retrieveStockByLastUpdate(Date lastUpdate) {
		return dao.retrieveByLastUpdate(lastUpdate);
	}

	@Override
	public List<Stock> retrieveStockByDevelopmentUpdateLessValue(double developmentUpdate) {
		return dao.retrieveByDevelopmentUpdateLessValue(developmentUpdate);
		
	}

}
