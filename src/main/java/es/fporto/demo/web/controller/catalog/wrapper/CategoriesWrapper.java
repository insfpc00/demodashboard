package es.fporto.demo.web.controller.catalog.wrapper;

import java.util.List;

import es.fporto.demo.data.model.Category;

public class CategoriesWrapper {

	private List<Category> categories;
	private List<Boolean> indexToBeDeleted;
	private String customvalue="";
	public String getCustomvalue() {
		return customvalue;
	}

	public void setCustomvalue(String customvalue) {
		this.customvalue = customvalue;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Boolean> getIndexToBeDeleted() {
		return indexToBeDeleted;
	}

	public void setIndexToBeDeleted(List<Boolean> indexToBeDeleted) {
		this.indexToBeDeleted = indexToBeDeleted;
	}

	public CategoriesWrapper() {
		super();
	}

	public CategoriesWrapper(List<Category> categories, List<Boolean> indexToBeDeleted) {
		super();
		this.categories = categories;
		this.indexToBeDeleted = indexToBeDeleted;
	}

}
