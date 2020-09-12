package com.client.bridge.service;

public interface BridgeService 
{
	UserBridgeService getUserService(boolean isLogging);
	//UserBridgeService getUserService(boolean isLogging);
	StockBridgeService getStockService();
}
