package es.fporto.demo.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import es.fporto.demo.catalog.service.CatalogService;
import es.fporto.demo.config.SecurityConfig;
import es.fporto.demo.config.WebMvcConfig;
import es.fporto.demo.controller.config.ControllerTestConfig;
import es.fporto.demo.controller.config.SpringSecurityWebAuxTestConfig;
import es.fporto.demo.data.model.Address;
import es.fporto.demo.data.model.Category;
import es.fporto.demo.data.model.Customer;
import es.fporto.demo.data.model.ItemState;
import es.fporto.demo.data.model.Price;
import es.fporto.demo.data.model.Product;
import es.fporto.demo.data.model.ProductItem;
import es.fporto.demo.repository.AddressRepository;
import es.fporto.demo.repository.CategoryRepository;
import es.fporto.demo.repository.CustomerRepository;
import es.fporto.demo.repository.PriceRepository;
import es.fporto.demo.repository.ProductItemRepository;
import es.fporto.demo.repository.ProductRepository;
import es.fporto.demo.web.account.service.AccountService;
import es.fporto.demo.web.controller.catalog.CatalogController;
import es.fporto.demo.web.controller.catalog.validator.CategoriesFormValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SecurityConfig.class,WebMvcConfig.class,ControllerTestConfig.class,CatalogController.class,CategoriesFormValidator.class,SpringSecurityWebAuxTestConfig.class})
@TestPropertySource("classpath:application-test.properties")
public class CatalogControllerIntegrationTest {

	@MockBean
	AccountService accountService;
	@MockBean
	CustomerRepository customerRepository;
	@MockBean
	ProductRepository productRepository;
	@MockBean
	AddressRepository addressRepository;
	@MockBean
	ProductItemRepository productItemRepository;
	@MockBean
	CategoryRepository categoryRepository;
	@MockBean
	PriceRepository priceRepository;
	@MockBean
	CatalogService catalogService;

		
	@Autowired
	private WebApplicationContext wac;
	
	MockMvc mockMvc;
	
	private final static List<Product> products=new ArrayList<>();
	private final static List<Customer> customers=new ArrayList<>();
	private final static List<Category> categories=new ArrayList<>();
	private final static Category electronicCategory=new Category("Electronic","Test category");
	private static Product testProduct; 
	static {
		
		categories.add(electronicCategory);
		
		testProduct=new Product("TestProduct",10,10d,20d,null,null,5,Arrays.asList(new Category[] {electronicCategory}));
		Price price=new Price(10d,testProduct,Calendar.getInstance());
		testProduct.setPrices(Arrays.asList(new Price[] {price}));
		
		products.add(testProduct);
		
		Customer customer=new Customer("E60654233","Test customer","Test customer description",null);
		customer.setAddress(new Address(0,0,customer));
		customers.add(customer);
		
		}
	
	@Before
	 public void setup() {
		
		 when(productRepository.findAll()).thenReturn(products);
		 when(productRepository.existsByCategoriesContains(any(Category.class))).thenReturn(true);
		 when(customerRepository.findAll()).thenReturn(customers);
		 when(categoryRepository.findAll()).thenReturn(categories);
		 when(productItemRepository.findByProductAndState(any(Product.class), any(ItemState.class))).thenReturn(new ArrayList<ProductItem>());
		 when(catalogService.updateProduct(any(Product.class), any(Double.class), anyList())).thenReturn(testProduct);

		 this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	 }
	
	@Test
	public void givenWac_whenServletContext_thenItProvidesSigninController() {
	    ServletContext servletContext = wac.getServletContext();
	     
	    Assert.assertNotNull(servletContext);
	    Assert.assertTrue(servletContext instanceof MockServletContext);
	    Assert.assertNotNull(wac.getBean("catalogController"));
	    
	}

	@Test
	//@WithMockUser(username="admin",roles={"USER","ADMIN"})
    @WithUserDetails("TestUser")
	public void whenGet_thenResultIsOKAndReturnsContent() throws Exception {
		
		this.mockMvc.perform(get("/catalog")).andExpect(status().isOk()).andDo(print())
				.andExpect(content().string(containsString("TestProduct")))
				.andExpect(content().string(containsString("E60654233")))
				.andExpect(content().string(containsString("Test category")));
	}


	@Test
	//@WithMockUser(username="admin",roles={"USER","ADMIN"})
    @WithUserDetails("TestUser")
	public void givenValidCustomer_whenPostUpdateCustomer_thenResultisRedirection() throws Exception {
		this.mockMvc.perform(

				post("/updateCustomer").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("CIF", "D49798531")
						.param("name", "Added Test customer").param("description", "Description of test added customer")
						.param("latitude", "10").param("longitude", "20"))
				.andExpect(status().is3xxRedirection()).andDo(print());
				
	}
	
	@Test
    @WithUserDetails("TestUser")
	//@WithMockUser(username="admin",roles={"USER","ADMIN"})
	public void givenNotValidCustomer_whenPostUpdateCustomer_thenFormContainsErrors() throws Exception {
		this.mockMvc.perform(

				post("/updateCustomer").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("CIF", "BADCIF")
						.param("name", "Added Test customer").param("description", "Description of test added customer")
						.param("latitude", "10").param("longitude", "20"))
				.andExpect(content().string(containsString("Form contains errors")))
				.andExpect(model().attributeHasFieldErrors("customer", "CIF"));

	}

	@Test
    @WithUserDetails("TestUser")
	//@WithMockUser(username="admin",roles={"USER","ADMIN"})
	public void givenValidCategories_whenPostSaveCategories_thenResultisRedirection() throws Exception {
		this.mockMvc.perform(

				post("/saveCategories").contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("categories[0].name", "Test category")
						.param("categories[0].description", "Description of test category"))
				.andExpect(status().is3xxRedirection()).andDo(print());
				
	}
	
	@Test
    @WithUserDetails("TestUser")
	//@WithMockUser(username="admin",roles={"USER","ADMIN"})
	public void givenValidProduct_whenPostUpdateProduct_thenResultisRedirection() throws Exception {

		this.mockMvc.perform(
				
				post("/updateProduct").contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("product.name", testProduct.getName())
						.param("product.sizeInCm", Double.valueOf(testProduct.getSizeInCm()).toString())
						.param("product.weightInGrams", Double.valueOf(testProduct.getWeightInGrams()).toString())
						.param("product.quantity", Integer.valueOf(testProduct.getQuantity()).toString())
						.param("price", Double.valueOf(testProduct.getActualPrice().getCost()).toString())
						)
				.andExpect(status().is3xxRedirection()).andDo(print());
	}
	
	@Test
    @WithUserDetails("TestUser")
	//@WithMockUser(username="admin",roles={"USER","ADMIN"})
	public void givenNotValidProduct_whenPostUpdateProduct_thenFormContainsErrors() throws Exception {

		this.mockMvc.perform(
				
				post("/updateProduct").contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("product.name", "")
						.param("product.sizeInCm", Double.valueOf(testProduct.getSizeInCm()).toString())
						.param("product.weightInGrams", Double.valueOf(testProduct.getWeightInGrams()).toString())
						.param("product.quantity", Integer.valueOf(testProduct.getQuantity()).toString())
						.param("price", Double.valueOf(testProduct.getActualPrice().getCost()).toString())
						)
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("Form contains errors")))
		.andExpect(model().attributeHasFieldErrors("productWrapper", "product.name"));
	}
	
//	@Test
//	@WithMockUser(username="admin",roles={"USER","ADMIN"})
//	public void givenEmptyCategoriesAndCategoryBeingUsed_whenPostSaveCategories_thenFormContainsErrors() throws Exception {
//		this.mockMvc.perform(
//				
//				post("/saveCategories").contentType(MediaType.APPLICATION_FORM_URLENCODED)
//						.param("categories[0].name", "Test category")
//						.param("categories[0].description", "Description of test category")
//						.param("indexToBeDeleted[0]", "true"))
//				.andExpect(status().is3xxRedirection()).andDo(print());
//				
//	}
	
}
