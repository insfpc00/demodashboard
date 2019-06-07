package es.fporto.demo.data.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Customer {

	private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";
	private static final String SIZE_MESSAGE = "{fixedSizeWrong.message}";
	
	@Id
	@NotBlank(message = Customer.NOT_BLANK_MESSAGE)
	@Size(min=9,max=9,message = Customer.SIZE_MESSAGE)
	private String CIF;
	@NotBlank(message = Customer.NOT_BLANK_MESSAGE)
	private String name;
	private String description;
	@OneToOne(mappedBy="customer",cascade=CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	@Valid
	private Address address;
	
	public Customer(String cIF, String name, String description, Address address) {
		super();
		CIF = cIF;
		this.name = name;
		this.description = description;
		this.address = address;
	}
	public Customer() {
		super();
	}
	
	public String getCIF() {
		return CIF;
	}
	public void setCIF(String cIF) {
		CIF = cIF;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
}
