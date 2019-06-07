package es.fporto.demo.controller.data;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class SaleRequest {
	
	@NotEmpty
	private String customerCIF;
	@NotEmpty
	@Valid
	private List<SaleRequestItem> items;
	
	public SaleRequest(String customerCIF, List<SaleRequestItem> items) {
		super();
		this.customerCIF = customerCIF;
		this.items = items;
	}

	public List<SaleRequestItem> getItems() {
		return items;
	}

	public void setItems(List<SaleRequestItem> items) {
		this.items = items;
	}

	public String getCustomerCIF() {
		return customerCIF;
	}

	public void setCustomerCIF(String customerCIF) {
		this.customerCIF = customerCIF;
	}

	public SaleRequest() {
		super();
	}
	
	
}
