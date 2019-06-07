package es.fporto.demo.catalog.service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fporto.demo.data.model.Category;
import es.fporto.demo.data.model.ItemState;
import es.fporto.demo.data.model.Price;
import es.fporto.demo.data.model.Product;
import es.fporto.demo.data.model.ProductItem;
import es.fporto.demo.repository.CategoryRepository;
import es.fporto.demo.repository.PriceRepository;
import es.fporto.demo.repository.ProductItemRepository;
import es.fporto.demo.repository.ProductRepository;
@Service
public class CatalogService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductItemRepository productItemRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private PriceRepository priceRepository;
	
	public Product updateProduct(Product product,Double cost,List<String> categories) {
		
	product.setCategories(categories.stream()
			.map(s -> categoryRepository.findById(s).get()).collect(Collectors.toList()));

	product=productRepository.save(product);
	
	Price price=new Price(cost,product,Calendar.getInstance());
	
	price=priceRepository.save(price);
	
	List<ProductItem> pois=productItemRepository.findByProductAndState(product, ItemState.FREE);

	int numberOfNewItems=product.getQuantity()-pois.size();
	
	while (numberOfNewItems>0) {
		ProductItem poi=new ProductItem(product,Calendar.getInstance(),ItemState.FREE);
		productItemRepository.save(poi);
		numberOfNewItems--;
	}
	
	if (numberOfNewItems<0) {
		pois.subList(0, -numberOfNewItems).forEach(productItemRepository::delete);
	}
	
	return product;
	
	}
	
	public Iterable<Category> updateCategories(List<Category> categories,List<Boolean> indexesToBeDeleted){
		
		List<Category> deletedCategories = categories.stream().filter(c -> {
			int index = categories.indexOf(c);
			return (indexesToBeDeleted.size() <= index) ? false
					: (indexesToBeDeleted.get(index)!=null);
		}).collect(Collectors.toList());
		
		if (!deletedCategories.isEmpty()) {
			Optional<Boolean> canBeDeleted = deletedCategories.stream().map(c -> !productRepository.existsByCategoriesContains(c))
					.reduce(Boolean::logicalAnd);
			if (canBeDeleted.get()) {
				deletedCategories.forEach(categoryRepository::delete);
				deletedCategories.forEach(categories::remove);
			}
		}
		return
		categoryRepository.saveAll(categories.stream()
				.filter(c -> c.getName() != null && !c.getName().isEmpty()).collect(Collectors.toList()));
	}
	
}
