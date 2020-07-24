package service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.StockJpaSpring;
import model.Stock;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	StockJpaSpring dao;

	@Override
	public void addStock(Stock stock) {
		stock.setDevelopmentUpdate();
		dao.save(stock);
	}

	@Override
	public List<Stock> retriveStocks() {
		return dao.findAll();
	}

	@Override
	public void updateStock(Stock stock) {

		if (dao.findById(stock.getId()) != null) {
			stock.setDevelopmentUpdate();
			dao.save(stock);
		}
	}

	@Override
	public boolean deleteStock(int idStock) {

		if (dao.findById(idStock) != null) {
			dao.deleteById(idStock);
			return true;
		}

		return false;
	}
	
	@Override
	public Stock findStockByName(String name) {
		return dao.findByName(name);
	}

	@Override
	public Stock findStock(int id) {
		return dao.findById(id).get();
	}

	@Override
	public String updateStockDevelopment(int id) {
		String result = null;
		Stock stock = dao.findById(id).get();

		if (stock != null) {
			double newDevelopmentValue = Math.floor(stock.getDevelopmentUpdate());
			dao.updateStockDevelopment(id, newDevelopmentValue);
			result = "";
		} else {
			result = "stock not found";
		}

		return result;
	}

	@Override
	public String updateStocksByDevelopmentUpdate() {
		String result = null;

		List<Stock> stocksToUpdate = dao.retrieveStockDevelopmentLike();

		if (stocksToUpdate != null && stocksToUpdate.size() > 0) {
			stocksToUpdate.stream().forEach(i -> i.setDevelopmentUpdate());

			if (stocksToUpdate.stream().anyMatch(i -> i.getDevelopmentUpdate() == 0)) {
				result = "an error has ocurred";
			} else {
				dao.saveAll(stocksToUpdate);
				result = "success";
			}
		} else {
			result = "no stocks where found";
		}

		return result;
	}

	@Override
	public List<Stock> retrieveStockByLastUpdate(Date lastUpdate) {
		return dao.findByLastUpdate(lastUpdate);
	}

	@Override
	public List<Stock> retrieveStockByDevelopmentUpdateLessValue(double developmentUpdate) {
		return dao.retrieveByDevelopmentUpdateLessValue(developmentUpdate);

	}

}
