package com.client.bridge.model.dto;

public class StockDTO{

	private int id;
	
	private String name;
	
	private String lastUpdate;
	
	private double developmentUpdate;
	
	private double lastPrice;
	
	private double actualPrice;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
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
