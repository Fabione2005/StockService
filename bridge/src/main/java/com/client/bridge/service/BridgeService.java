package com.client.bridge.service;

import com.client.bridge.model.dto.StockDTO;
import com.client.bridge.model.dto.UserDTO;
import com.client.bridge.model.generic.BaseResultDTO;
import com.client.bridge.model.wrapper.StockResultDTO;
import com.client.bridge.model.wrapper.UserResultDTO;

public interface BridgeService 
{
	StockResultDTO retrieveAllStocks();
	UserResultDTO retrieveAllUsers();
	BaseResultDTO logOutService();
	BaseResultDTO createUserService(UserDTO user);
	BaseResultDTO createStockService(StockDTO stock);
	UserResultDTO loggingService(String userName, String password);
}
