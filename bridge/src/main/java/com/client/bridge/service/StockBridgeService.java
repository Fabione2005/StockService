package com.client.bridge.service;

import com.client.bridge.model.dto.StockDTO;
import com.client.bridge.model.generic.BaseResultDTO;
import com.client.bridge.model.wrapper.StockResultDTO;
import com.client.bridge.model.wrapper.UserResultDTO;

public interface StockBridgeService 
{
	void setUserLoggValidate(UserResultDTO userLoggedResult);
	StockResultDTO retrieveAllStocks();
	BaseResultDTO createStockService(StockDTO stock);
	BaseResultDTO deleteStockService(int id);
}
