package es.fporto.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import es.fporto.demo.data.model.ItemState;
import es.fporto.demo.data.model.Product;
import es.fporto.demo.data.model.ProductItem;


@RunWith(SpringRunner.class)
@DataJpaTest

public class ProductItemRepositoryIntegrationTest {

	@Autowired
	ProductItemRepository productItemRepository;
	@Autowired
	ProductRepository productRepository;

	@Autowired
	private TestEntityManager entityManager;
	
	private Product testProduct;
	private ProductItem freePoi;
	
	
	@Before
	 public void setup() {
		testProduct = new Product();
	    testProduct.setName("testProduct");
	    
	    testProduct=productRepository.save(testProduct);
	    
	    freePoi=productItemRepository.save(new ProductItem(testProduct,Calendar.getInstance(),ItemState.FREE));
	    productItemRepository.save(new ProductItem(testProduct,Calendar.getInstance(),ItemState.SOLD));
	    
	    entityManager.flush();
	    
	 }
	
	@Test
	public void whenFindByProductAndStateAndExistsProductItems_thenReturnsProductItems(){
		
	    List<ProductItem> pois=productItemRepository.findByProductAndState(testProduct, ItemState.FREE);
	    
	    assertThat(pois.size()).isEqualTo(1);
	    assertTrue(pois.contains(freePoi));
	}
	
	@Test
	public void whenFindByProductAndStateAndNotExistsProductItems_thenDoesntReturnProductItems(){
		
	    List<ProductItem> pois=productItemRepository.findByProductAndState(testProduct, ItemState.UNAVAILABLE);
	    
	    assertThat(pois.size()).isEqualTo(0);
	    
	}
	
}
