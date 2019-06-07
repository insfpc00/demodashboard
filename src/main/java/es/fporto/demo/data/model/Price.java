package es.fporto.demo.data.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Price {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private double cost;
	@ManyToOne
	@JsonBackReference
	private Product product;
	private Calendar startDate;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Calendar getStartDate() {
		return startDate;
	}
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	
	public Price(double cost, Product product, Calendar startDate) {
		super();
		this.cost = cost;
		this.product = product;
		this.startDate = startDate;
	
	}
	
	public Price() {
	}
	
	
		
	
}
