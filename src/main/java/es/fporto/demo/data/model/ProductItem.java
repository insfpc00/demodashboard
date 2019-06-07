package es.fporto.demo.data.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ProductItem {
    @Entity
	public static class StateChange{
    	@Id
    	@GeneratedValue(strategy = GenerationType.AUTO)
    	private long stateChangeId;
		private ItemState oldState;
		private Calendar stateChangeDate;
		
		public StateChange() {
			super();
		}
		
		public Calendar getStateChangeDate() {
			return stateChangeDate;
		}
		public void setStateChangeDate(Calendar stateChangeDate) {
			this.stateChangeDate = stateChangeDate;
		}
		public long getStateChangeId() {
			return stateChangeId;
		}
		public void setStateChangeId(long stateChangeId) {
			this.stateChangeId = stateChangeId;
		}
		
		public ItemState getOldState() {
			return oldState;
		}
		public void setOldState(ItemState oldState) {
			this.oldState = oldState;
		}
		
		public StateChange(ItemState oldState, Calendar stateChangeDate) {
			super();
			this.oldState = oldState;
			this.stateChangeDate = stateChangeDate;
		}
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long itemId;
	@ManyToOne
	@JsonBackReference(value="product")
	private Product product;
	private Calendar createdTime;
	@OneToMany(cascade=CascadeType.ALL)
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<StateChange> stateChanges;
	@Enumerated 
	private ItemState state;
	@ManyToOne
	@JsonBackReference(value="sale")
	private Sale sale;
	
	public Sale getSale() {
		return sale;
	}
	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public ItemState getState() {
		return state;
	}
	public void setState(ItemState state) {
		
		this.getStateChanges().add(new StateChange(this.state,Calendar.getInstance()));
		this.state = state;
		
	}
	public long getItemId() {
	
		return itemId;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	
	public Calendar getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}
	public List<StateChange> getStateChanges() {
		if (stateChanges==null) {
			stateChanges=new ArrayList<>();
		}
		return stateChanges;
	}
//	public void setStateChanges(List<StateChange> stateChanges) {
//		this.stateChanges = stateChanges;
//	}
	
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	public ProductItem() {
		super();
	}
	
	public ProductItem(Product product, Calendar createdTime, ItemState state) {
		super();
		this.product = product;
		this.createdTime = createdTime;
		this.state=state;
	}
}
