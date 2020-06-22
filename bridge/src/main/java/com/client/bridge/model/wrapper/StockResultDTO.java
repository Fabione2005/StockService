package com.client.bridge.model.wrapper;

import java.util.List;

import com.client.bridge.model.dto.StockDTO;
import com.client.bridge.model.generic.BaseResultDTO;

public class StockResultDTO extends BaseResultDTO{

	private List<StockDTO> stockArray;
	
	public StockResultDTO() {
		super();
	}

	public StockResultDTO(List<StockDTO> stockArray) {
		super();
		this.stockArray = stockArray;
	}

	public List<StockDTO> getStockArray() {
		return stockArray;
	}

	public void setStockArray(List<StockDTO> stockArray) {
		this.stockArray = stockArray;
	}
	
}
