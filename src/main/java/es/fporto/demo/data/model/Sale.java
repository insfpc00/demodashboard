package es.fporto.demo.data.model;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Sale {

	@Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long saleId;
	private Calendar date;
	@OneToMany(mappedBy="sale")
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonManagedReference(value="sale")
	private List<ProductItem> items;
	@ManyToOne
	private Customer customer;
	
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Sale( Calendar date, List<ProductItem> items,Customer customer) {
		super();
		this.date = date;
		this.items = items;
		this.customer=customer;
	}
	
	public Sale() {
		super();
	}
	
	public long getSaleId() {
		return saleId;
	}
	public void setSaleId(long saleId) {
		this.saleId = saleId;
	}

	public Calendar getDate() {
		return date;
	}
	
	public void setDate(Calendar date) {
		this.date = date;
	}
	public List<ProductItem> getItems() {
		return items;
	}
	public void setItems(List<ProductItem> items) {
		this.items = items;
	}
	
}
