package es.fporto.demo.sale.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.fporto.demo.controller.data.SaleRequest;
import es.fporto.demo.controller.data.SaleRequestItem;
import es.fporto.demo.controller.data.SaleResponse;
import es.fporto.demo.controller.exception.CustomSalesException;
import es.fporto.demo.data.model.Customer;
import es.fporto.demo.data.model.ItemState;
import es.fporto.demo.data.model.Product;
import es.fporto.demo.data.model.ProductItem;
import es.fporto.demo.data.model.Sale;
import es.fporto.demo.repository.CustomerRepository;
import es.fporto.demo.repository.ProductRepository;
import es.fporto.demo.repository.SaleRepository;

@Service

public class SaleServiceImpl implements SaleService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private SaleRepository saleRepository;
	@Autowired
	private CustomerRepository customerRepository;

	private static final String CUSTOMER_NOT_FOUND="Customer not found";
	private static final String NOT_ENOUGH_ITEMS="Not enough items available";
	private static final String PRODUCT_NOT_FOUND="Product not found";
	
	@Transactional
	public SaleResponse processSale(SaleRequest request) throws Exception {

		SaleResponse response = new SaleResponse(0d, new ArrayList<>());
		
		Optional<Customer> customerOpt = customerRepository.findById(request.getCustomerCIF());

		if (customerOpt.isEmpty()) {
			throw new CustomSalesException(SaleServiceImpl.CUSTOMER_NOT_FOUND);
		}

		for (SaleRequestItem saleItem : request.getItems()) {

			Optional<Product> productOpt = productRepository.findById(saleItem.getProductName());
			if (productOpt.isEmpty()) {
				throw new CustomSalesException(SaleServiceImpl.PRODUCT_NOT_FOUND);
			}

			Product prod = productOpt.get();

			if (prod.getProductItems().isEmpty()) {
				throw new CustomSalesException(SaleServiceImpl.NOT_ENOUGH_ITEMS);
			}
			Predicate<ProductItem> itemAvaliablePred = item -> item.getState().equals(ItemState.FREE);

			List<ProductItem> items = prod.getProductItems().stream().filter(itemAvaliablePred)
					.limit(saleItem.getQuantity()).collect(Collectors.toList());

			if (items.size() != saleItem.getQuantity()) {
				throw new CustomSalesException(SaleServiceImpl.NOT_ENOUGH_ITEMS);
			}

			Sale sale = new Sale(Calendar.getInstance(), new ArrayList<>(), customerOpt.get());
			sale = saleRepository.save(sale);

			for (ProductItem it : items) {
				it.setState(ItemState.SOLD);
				it.setCreatedTime(Calendar.getInstance());
				response.getItems().add(it);
				it.setSale(sale);
			}

			response.setTotal(response.getTotal() + saleItem.getQuantity() * prod.getActualPrice().getCost());
		}
		
		return response;
		
	}

}
