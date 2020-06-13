package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="stock")
@NamedQuery(name="Stock.findAll", query="SELECT c FROM Stock c")
public class Stock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@Temporal(TemporalType.DATE)
	private Date lastUpdate;
	
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

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
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

	@Override
	public String toString() {
		return "Stock [id=" + id + ", name=" + name + ", lastUpdate=" + lastUpdate + ", developmentUpdate="
				+ developmentUpdate + ", lastPrice=" + lastPrice + ", actualPrice=" + actualPrice + "]";
	}
	
	
	
	
}
