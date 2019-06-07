package es.fporto.demo.loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

@Component
public class DataLoader implements CommandLineRunner {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private PriceRepository priceRepository;
	
	@Autowired
	private ProductItemRepository productItemRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	private void buildProduct(Product product,int numberOfItems,Double priceValue,Calendar validUntil) {
		
		product=productRepository.save(product);
		Price price=priceRepository.save(new Price(priceValue, product, validUntil));
		product.setPrices(new ArrayList<>());
		product.getPrices().add(price);
		
		for (int i=0;i<numberOfItems;i++) {
			productItemRepository.save(new ProductItem(product, Calendar.getInstance(), ItemState.FREE));
		}
	}
	
	private void buildCustomer(String cif,String name,String description,Address address) {
		
		Customer customer=new Customer(cif,name,description,null);
		customer=customerRepository.save(customer);
		address.setCustomer(customer);
		addressRepository.save(address);
		
	}
	public void initDb() {
		
		Category electronicCategory=categoryRepository.save(new Category("electronic", "Electronic devices"));
		Category storageCategory=categoryRepository.save(new Category("storage","Storage devices"));
		Category peripheralCategory=categoryRepository.save(new Category("peripheral","Peripherals"));
		
		Calendar threeMonthsBefore=Calendar.getInstance();
		threeMonthsBefore.add(Calendar.MONTH, -3);
		Calendar threeMonthsAfter=Calendar.getInstance();
		threeMonthsAfter.add(Calendar.MONTH, 3);
		
		buildProduct(new Product("Pendrive", 1, 2d, 100d, null, null, 100, Arrays.asList(new Category[] {electronicCategory,storageCategory})),100,50d,threeMonthsBefore); 
		buildProduct(new Product("Keyboard", 2, 40d, 150d, null, null, 100, Arrays.asList(new Category[] {peripheralCategory})),100,100d,threeMonthsBefore);
	
		buildCustomer("Q7731334D","ComputerTronic S.A","Computer components seller",new Address(42.23282,-8.72264,null));
		buildCustomer("V7792132H","MediaStore","Entertainment market",new Address(39.4697500,-0.3773900,null));
		
	}

	@Override
	public void run(String... args) throws Exception {
		for (String arg:args) {
			if ("initDb".equals(arg)) {
				initDb();
			}
		}
		
	}
	
}
