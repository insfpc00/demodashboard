package es.fporto.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import es.fporto.demo.data.model.Category;
import es.fporto.demo.data.model.Product;
import es.fporto.demo.repository.CategoryRepository;
import es.fporto.demo.repository.ProductRepository;

@RunWith(SpringRunner.class)
@DataJpaTest

public class ProductRepositoryIntegrationTest {


	
@Autowired
ProductRepository productRepository;
@Autowired
CategoryRepository categoryRepository;	

@Autowired
private TestEntityManager entityManager;

	@Test
	public void whenFindByName_thenReturnProduct() {
	    
		Product testProduct = new Product();
	    testProduct.setName("testProduct");
	    productRepository.save(testProduct);
	    entityManager.flush();
	    
	    Product found = productRepository.findById(testProduct.getName()).get();
	 
	    assertThat(found.getName())
	      .isEqualTo(testProduct.getName());
	}
	
	@Test
	public void whenExistsByCategory_thenReturnTrue() {
	    
		Product testProduct = new Product();
	    testProduct.setName("testProduct");
	    
	    Category category=categoryRepository.save(new Category("testCategory", ""));
	    
	    testProduct.setCategories(Arrays.asList(new Category[] {category}));
	    productRepository.save(testProduct);
	    
	    entityManager.flush();
	    
	    
	 
	    assertThat(productRepository.existsByCategoriesContains(category))
	      .isEqualTo(true);
	}

	@Test
	public void whenNotExistsByCategory_thenReturnFalse() {
	    
		Product testProduct = new Product();
	    testProduct.setName("testProduct");
	    
	    Category category=categoryRepository.save(new Category("testCategory", ""));
	    Category otherCategory=categoryRepository.save(new Category("otherCategory", ""));
	    
	    testProduct.setCategories(Arrays.asList(new Category[] {category}));
	    productRepository.save(testProduct);
	    
	    entityManager.flush();
	    
	    
	 
	    assertThat(productRepository.existsByCategoriesContains(otherCategory))
	      .isEqualTo(false);
	}

	
}
