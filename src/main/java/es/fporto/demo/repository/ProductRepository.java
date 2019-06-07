package es.fporto.demo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.fporto.demo.data.model.Category;
import es.fporto.demo.data.model.Product;

@RepositoryRestResource
public interface ProductRepository extends PagingAndSortingRepository<Product,String> {

	public Boolean existsByCategoriesContains(Category category);
	
}
