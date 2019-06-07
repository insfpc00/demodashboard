package es.fporto.demo.web.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.fporto.demo.data.model.Category;
import es.fporto.demo.data.model.ProductItem;
import es.fporto.demo.data.model.Sale;
import es.fporto.demo.processor.SaleProcessor;
import es.fporto.demo.repository.SaleRepository;
import es.fporto.demo.view.model.SaleStatsVO;
import es.fporto.demo.view.model.SaleStatsVO.ItemVO;

@Controller
public class SalesController {

	@Autowired
	SaleRepository saleRepository;

	@GetMapping("/")
	String index(Principal principal) {
		return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
	}

	@GetMapping("/dashboard")
	public String homePage(Model model) {
		return "dashboard/salesDashBoard";
	}

	@ModelAttribute("salesStats")
	public List<SaleStatsVO> getStats() {

		Iterable<Sale> sales = saleRepository.findAll();
		List<SaleStatsVO> salesStats = new ArrayList<SaleStatsVO>();

		SaleStatsVO stats = new SaleStatsVO(SaleProcessor.getSalesGroupByProduct(),
				SaleProcessor.getSalesGroupByCategory(), SaleProcessor.getSalesGroupByCustomer(),
				SaleProcessor.getSalesGroupByProductAndMinute(), SaleProcessor.getSalesGroupByCategoryAndMinute(),
				SaleProcessor.getSalesGroupByCustomerAndMinute(), SaleProcessor.getTotalSales(),
				new ArrayList<ItemVO>(), null, 0, 0);

		salesStats.add(stats);

		for (Sale sale : sales) {
			SaleStatsVO saleVO = new SaleStatsVO();
			
			saleVO.setCustomerName(sale.getCustomer().getName());
			saleVO.setCustomerLatitude(sale.getCustomer().getAddress().getLatitude());
			saleVO.setCustomerLongitude(sale.getCustomer().getAddress().getLongitude());
			
			saleVO.setItemsSold(new ArrayList<>());
			
			for (ProductItem poi : sale.getItems()) {
				saleVO.getItemsSold().add(new SaleStatsVO.ItemVO(poi.getProduct().getName(),
						poi.getProduct().getCategories().stream().map(Category::getName).collect(Collectors.toList()),
						poi.getCreatedTime()));
			}
			salesStats.add(saleVO);
		}
		
		return salesStats;
	}

	
	
}
