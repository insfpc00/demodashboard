package es.fporto.demo.data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Address {
	
	private static final String MIN_MESSAGE = "{min.message}";
	private static final String MAX_MESSAGE = "{max.message}";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long addressId;
	
	@Min(value=-90,message = Address.MIN_MESSAGE)
	@Max(value=90,message = Address.MAX_MESSAGE)
	
	private double latitude;
	
	@Min(value=-180,message = Address.MIN_MESSAGE)
	@Max(value=180,message = Address.MAX_MESSAGE)
	
	private double longitude;
	@OneToOne
	@JsonBackReference
	private Customer customer;
	
	public long getAddressId() {
		return addressId;
	}
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Address(double latitude, double longitude, Customer customer) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.customer = customer;
	}
	
	public Address() {
		super();
	}
	
}
