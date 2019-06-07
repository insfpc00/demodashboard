package es.fporto.demo.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.fporto.demo.data.model.ItemState;
import es.fporto.demo.data.model.Product;
import es.fporto.demo.data.model.ProductItem;

@RepositoryRestResource
public interface ProductItemRepository extends PagingAndSortingRepository<ProductItem,Long> {

	public List<ProductItem> findByProductAndState(Product product,ItemState state);
	
}
