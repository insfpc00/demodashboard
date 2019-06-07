package es.fporto.demo.web.controller.catalog.validator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import es.fporto.demo.data.model.Category;
import es.fporto.demo.repository.ProductRepository;
import es.fporto.demo.web.controller.catalog.wrapper.CategoriesWrapper;

@Component
public class CategoriesFormValidator implements Validator {

	private static final String NOT_BLANK_MESSAGE = "notBlank.message";
	private static final String CATEGORY_BEING_USED_MESSAGE = "categoryBeingUsed.message";

	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return CategoriesWrapper.class.equals(clazz);

	}

	@Override
	public void validate(Object target, Errors errors) {

		CategoriesWrapper categoriesWrapper = (CategoriesWrapper) target;

		List<Category> categories = categoriesWrapper.getCategories();
		List<Boolean> indexesToBeDeleted = categoriesWrapper.getIndexToBeDeleted();

		List<Category> noDeletableCategories = categories.stream().filter(c -> {
			int index = categories.indexOf(c);
			return (indexesToBeDeleted.size() <= index) ? false : (indexesToBeDeleted.get(index) != null);
		}).filter(c -> productRepository.existsByCategoriesContains(c)).collect(Collectors.toList());

		for (Category nodeletableCategory : noDeletableCategories) {
			errors.pushNestedPath("categories[" + categories.indexOf(nodeletableCategory) + "]");
			errors.rejectValue("name", CategoriesFormValidator.CATEGORY_BEING_USED_MESSAGE);
			errors.popNestedPath();
		}
		
		for (Category category:categories) {
			if (category.getName().isBlank()) {
				errors.pushNestedPath("categories[" + categories.indexOf(category) + "]");
				errors.rejectValue("name", CategoriesFormValidator.NOT_BLANK_MESSAGE);
				errors.popNestedPath();
			}
		}
	}	

}