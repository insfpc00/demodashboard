package es.fporto.demo.view.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class SaleStatsVO implements Serializable{

	
	private static final long serialVersionUID = 5446744815410955303L;

	public static class ItemVO{
		
		private String productName;
		private List<String> categories;
		
		public List<String> getCategories() {
			return categories;
		}
		public void setCategories(List<String> categories) {
			this.categories = categories;
		}

		private Calendar saleDate;
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public Calendar getSaleDate() {
			return saleDate;
		}
		public void setSaleDate(Calendar saleDate) {
			this.saleDate = saleDate;
		}

		public ItemVO(String productName, List<String> categories, Calendar saleDate) {
			super();
			this.productName = productName;
			this.saleDate = saleDate;
			this.categories = categories;
		}
		
		public ItemVO() {
			super();
		}
		
	}
	
	private Map<String,Long> salesGroupByProduct;
	private Map<String,Long> salesGroupByCategory;
	private Map<String,Long> salesGroupByCustomer;
	private Map<Long,Map<String,Long>> salesGroupByProductAndMinute;
	private Map<Long,Map<String,Long>> salesGroupByCategoryAndMinute;
	private Map<Long,Map<String,Long>> salesGroupByCustomerAndMinute;
	private String customerName;
	private double customerLatitude;
	private double customerLongitude;
	
	public Map<String, Long> getSalesGroupByCustomer() {
		return salesGroupByCustomer;
	}

	public void setSalesGroupByCustomer(Map<String, Long> salesGroupByCustomer) {
		this.salesGroupByCustomer = salesGroupByCustomer;
	}

	public Map<Long, Map<String, Long>> getSalesGroupByCustomerAndMinute() {
		return salesGroupByCustomerAndMinute;
	}

	public void setSalesGroupByCustomerAndMinute(Map<Long, Map<String, Long>> salesGroupByCustomerAndMinute) {
		this.salesGroupByCustomerAndMinute = salesGroupByCustomerAndMinute;
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Map<Long, Map<String, Long>> getSalesGroupByProductAndMinute() {
		return salesGroupByProductAndMinute;
	}

	public void setSalesGroupByProductAndMinute(Map<Long, Map<String, Long>> salesGroupByProductAndMinute) {
		this.salesGroupByProductAndMinute = salesGroupByProductAndMinute;
	}

	public Map<Long, Map<String, Long>> getSalesGroupByCategoryAndMinute() {
		return salesGroupByCategoryAndMinute;
	}

	public void setSalesGroupByCategoryAndMinute(Map<Long, Map<String, Long>> salesGroupByCategoryAndMinute) {
		this.salesGroupByCategoryAndMinute = salesGroupByCategoryAndMinute;
	}

	public Map<String, Long> getSalesGroupByCategory() {
		return salesGroupByCategory;
	}

	public void setSalesGroupByCategory(Map<String, Long> salesGroupByCategory) {
		this.salesGroupByCategory = salesGroupByCategory;
	}

	private Long totalSales;
	private List<ItemVO> itemsSold;
	
	public Map<String, Long> getSalesGroupByProduct() {
		return salesGroupByProduct;
	}
	
	public void setSalesGroupByProduct(Map<String, Long> salesGroupByProduct) {
		this.salesGroupByProduct = salesGroupByProduct;
	}
	public Long getTotalSales() {
		return totalSales;
	}
	public void setTotalSales(Long totalSales) {
		this.totalSales = totalSales;
	}
	public List<ItemVO> getItemsSold() {
		return itemsSold;
	}
	public void setItemsSold(List<ItemVO> itemsSold) {
		this.itemsSold = itemsSold;
	}
	
	public double getCustomerLatitude() {
		return customerLatitude;
	}

	public void setCustomerLatitude(double customerLatitude) {
		this.customerLatitude = customerLatitude;
	}

	public double getCustomerLongitude() {
		return customerLongitude;
	}

	public void setCustomerLongitude(double customerLongitude) {
		this.customerLongitude = customerLongitude;
	}

	public SaleStatsVO(Map<String, Long> salesGroupByProduct,Map<String, Long> salesGroupByCategory,Map<String, Long> salesGroupByCustomer, Map<Long, Map<String, Long>> salesGroupByProductAndMinute,Map<Long, Map<String, Long>> salesGroupByCategoryAndMinute,Map<Long, Map<String, Long>> salesGroupByCustomerAndMinute,Long totalSales, List<ItemVO> itemsSold,String customerName,double customerLatitude,double customerLongitude) {
		super();
		this.salesGroupByProduct = salesGroupByProduct;
		this.salesGroupByCategory = salesGroupByCategory;
		this.salesGroupByCustomer = salesGroupByCustomer;
		this.salesGroupByCategoryAndMinute=salesGroupByCategoryAndMinute;
		this.salesGroupByProductAndMinute=salesGroupByProductAndMinute;
		this.salesGroupByCustomerAndMinute=salesGroupByCustomerAndMinute;
		this.totalSales = totalSales;
		this.itemsSold = itemsSold;
		this.customerName=customerName;
		this.customerLatitude=customerLatitude;
		this.customerLongitude=customerLongitude;
	}
	
	public SaleStatsVO() {
		super();
	}
	
}
