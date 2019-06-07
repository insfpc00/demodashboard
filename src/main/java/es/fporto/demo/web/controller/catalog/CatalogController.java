package es.fporto.demo.web.controller.catalog;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.fporto.demo.catalog.service.CatalogService;
import es.fporto.demo.data.model.Address;
import es.fporto.demo.data.model.Category;
import es.fporto.demo.data.model.Customer;
import es.fporto.demo.data.model.Product;
import es.fporto.demo.repository.CategoryRepository;
import es.fporto.demo.repository.CustomerRepository;
import es.fporto.demo.repository.ProductRepository;
import es.fporto.demo.web.controller.catalog.validator.CategoriesFormValidator;
import es.fporto.demo.web.controller.catalog.wrapper.CategoriesWrapper;
import es.fporto.demo.web.controller.catalog.wrapper.ProductWrapper;

@Controller
public class CatalogController {

	private static final String CATALOG_VIEW_NAME = "catalog/catalog";
	
	
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private CatalogService catalogService;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	CategoriesFormValidator categoriesFormValidator;
	
	@InitBinder("categoriesWrapper")
	public void dataBinding(WebDataBinder binder) {
		binder.setValidator(categoriesFormValidator);
	}
	
	@GetMapping("/catalog")
	public String getCatalog(Model model) {
		return CATALOG_VIEW_NAME;
	}

	@PostMapping("/updateProduct")
	@Transactional
	public String updateProduct(@Valid @ModelAttribute ProductWrapper productWrapper, Errors errors, RedirectAttributes ra) {
		
		if (errors.hasErrors()) {
			
			return CATALOG_VIEW_NAME;
		}
		
		catalogService.updateProduct(productWrapper.getProduct(), productWrapper.getPrice(), productWrapper.getCategories());
		return "redirect:/catalog";
	}
	
	@PostMapping("/updateCustomer")
	public String updateCustomer(@Valid @ModelAttribute Customer customer, Errors errors, RedirectAttributes ra) {
		
		if (errors.hasErrors()) {
			return CATALOG_VIEW_NAME;
		}
		customer.getAddress().setCustomer(customer);
		customerRepository.save(customer);
		
		return "redirect:/catalog";
	}
	
	@ModelAttribute("productWrapper")
	public ProductWrapper getProduct(){
		return new ProductWrapper(new Product(),new ArrayList<>(),0d);
	}

	@ModelAttribute("customer")
	public Customer getCustomer(){
		Customer customer=new Customer();
		customer.setAddress(new Address());
		return customer;
	}
	
	@ModelAttribute("products")
	public Iterable<Product> getProductList() {
		return productRepository.findAll();
	}
	
	@ModelAttribute("customers")
	public Iterable<Customer> getCustomerList() {
		return customerRepository.findAll();
	}

	@ModelAttribute("categoriesWrapper")
	public CategoriesWrapper getCategoriesList() {

		List<Category> categoryList = new ArrayList<>();
		categoryRepository.findAll().forEach(categoryList::add);
		return new CategoriesWrapper(categoryList,new ArrayList<Boolean>());
	}
	

	@PostMapping("/saveCategories")
	public String saveCategories(@ModelAttribute @Valid CategoriesWrapper categoriesForm, Errors errors, RedirectAttributes ra) {
		
		if (errors.hasErrors()) {
			return CATALOG_VIEW_NAME;
		}
		catalogService.updateCategories(categoriesForm.getCategories(), categoriesForm.getIndexToBeDeleted());
		return "redirect:/catalog";
	}
}
