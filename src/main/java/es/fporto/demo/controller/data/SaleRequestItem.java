package es.fporto.demo.controller.data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class SaleRequestItem {

	@NotEmpty
	private String productName;
	@Min(value=1)
	private int quantity;
	
	public SaleRequestItem() {
		super();
	}
	public SaleRequestItem(String productName, int quantity) {
		super();
		this.productName = productName;
		this.quantity = quantity;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
