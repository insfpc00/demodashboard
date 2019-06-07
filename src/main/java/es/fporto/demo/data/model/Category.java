package es.fporto.demo.data.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity

public class Category {
	
	@Id
	private String name;

	private String description;
	
	public Category(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	
	public Category() {
		super();
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
	
	
	
}
