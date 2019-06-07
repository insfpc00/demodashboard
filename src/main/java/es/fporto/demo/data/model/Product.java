package es.fporto.demo.data.model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Product {

	private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";
	private static final String MIN_MESSAGE = "{min.message}";

	
//    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
	

	@Id
	@NotBlank(message = Product.NOT_BLANK_MESSAGE)
	private String name;
	private int number;
	@Min(value=0,message = Product.MIN_MESSAGE)
	private double sizeInCm;
	@Min(value=0,message = Product.MIN_MESSAGE)
	private double weightInGrams;
	@OneToMany(mappedBy="product")
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonManagedReference
	private List<Price> prices;
	@OneToMany(mappedBy="product")
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonManagedReference(value="product")
	private List<ProductItem> productItems;
	@Min(value=0,message = Product.MIN_MESSAGE)
	private int quantity;
	
	@ManyToMany()
    @JoinTable(name = "productCategories",
     joinColumns = @JoinColumn(name = "name",referencedColumnName="name"),
     inverseJoinColumns = @JoinColumn(name = "categoryname",referencedColumnName="name")
    )
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Category> categories;
	
	public Product(String name, int number, double sizeInCm, double weightInGrams, List<Price> prices,
			List<ProductItem> productItems, int quantity, List<Category> categories) {
		super();
		this.name = name;
		this.number = number;
		this.sizeInCm = sizeInCm;
		this.weightInGrams = weightInGrams;
		this.prices = prices;
		this.productItems = productItems;
		this.quantity = quantity;
		this.categories = categories;
	}
	
	public Product() {
		super();
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public double getSizeInCm() {
		return sizeInCm;
	}
	public void setSizeInCm(double sizeInCm) {
		this.sizeInCm = sizeInCm;
	}
	public double getWeightInGrams() {
		return weightInGrams;
	}
	public void setWeightInGrams(double weightinGrams) {
		this.weightInGrams = weightinGrams;
	}
	
	public List<Price> getPrices() {
		return prices;
	}
	public void setPrices(List<Price> prices) {
		this.prices = prices;
	}
	public List<ProductItem> getProductItems() {
		return productItems;
	}
	public void setProductItems(List<ProductItem> productItems) {
		this.productItems = productItems;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Transient
	public Price getActualPrice() {
		final Comparator<Price> calComparator=(c1,c2)->c1.getStartDate().compareTo(c2.getStartDate());
		Optional<Price> actualPrice = this.getPrices().stream().max(calComparator);
		
		return actualPrice.get();	
	}
	
}
