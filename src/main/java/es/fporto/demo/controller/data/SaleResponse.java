package es.fporto.demo.controller.data;

import java.util.List;

import es.fporto.demo.data.model.ProductItem;

public class SaleResponse {
	
	private double total;
	private List<ProductItem> items;
	
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public List<ProductItem> getItems() {
		return items;
	}
	public void setItems(List<ProductItem> items) {
		this.items = items;
	}
	
	public SaleResponse(double total, List<ProductItem> items) {
		super();
		this.total = total;
		this.items = items;
	}
	
	public SaleResponse() {
		super();
	}
	
}
