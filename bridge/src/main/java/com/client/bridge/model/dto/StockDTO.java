package com.client.bridge.model.dto;

import java.time.LocalDate;

public class StockDTO{

	private String name;
	
	private LocalDate lastUpdate;
	
	private double developmentUpdate;
	
	private double lastPrice;
	
	private double actualPrice;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDate lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public double getDevelopmentUpdate() {
		return developmentUpdate;
	}

	public void setDevelopmentUpdate() {
		this.developmentUpdate = ((this.getActualPrice() - this.getLastPrice())/this.getLastPrice())*100;
	}

	public double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public double getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(double actualPrice) {
		this.actualPrice = actualPrice;
	}

	
}
