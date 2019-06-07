package es.fporto.demo.web.controller.catalog.wrapper;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import es.fporto.demo.data.model.Product;

public class ProductWrapper {
	
	private static final String MIN_MESSAGE = "{min.message}";

	
	@Valid
	private Product product;
	private List<String> categories;
	@Min(value=0,message = ProductWrapper.MIN_MESSAGE)
	private double price;
	
	public double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Product getProduct() {
		return product;
	}
	public ProductWrapper(Product product, List<String> categories,double price) {
		super();
		this.product = product;
		this.categories = categories;
		this.price=price;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	
}
