package es.fporto.demo.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import es.fporto.demo.data.model.Address;
import es.fporto.demo.data.model.Category;
import es.fporto.demo.data.model.ProductItem;
import es.fporto.demo.view.model.SaleStatsVO;

@Service
public class SaleProcessor {
	private static final String ENDPOINT="/topic/items"; 
	
	private static Map<String, Long> salesGroupByProduct = new ConcurrentHashMap<>();
	private static Map<String, Long> salesGroupByCategory = new ConcurrentHashMap<>();
	private static Map<String, Long> salesGroupByCustomer = new ConcurrentHashMap<>();
	private static Map<Long, Map<String, Long>> salesGroupByProductAndMinute = new ConcurrentHashMap<>();
	private static Map<Long, Map<String, Long>> salesGroupByCategoryAndMinute = new ConcurrentHashMap<>();
	private static Map<Long, Map<String, Long>> salesGroupByCustomerAndMinute = new ConcurrentHashMap<>();
	private static final long baseTime = Calendar.getInstance().getTimeInMillis();
	private static Long minuteLastSent=0l;
	private static long totalSales = 0l;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	private void computeStats(List<ProductItem> pois, Map<String, Long> groupByMap,
			Map<Long, Map<String, Long>> groupByMinuteMap, Function<ProductItem, List<String>> elementFunction) {

		for (ProductItem poi : pois) {
			List<String> keys = elementFunction.apply(poi);
			for (String key : keys) {
				Long actualValue = groupByMap.get(key);
				actualValue = actualValue == null ? 1l : actualValue + 1;
				groupByMap.put(key, actualValue);

				Long minute = (Calendar.getInstance().getTimeInMillis() - baseTime) / (60 * 1000);
				if (!groupByMinuteMap.containsKey(minute)) {
					groupByMinuteMap.put(minute, new ConcurrentHashMap<>());
				}

				actualValue = groupByMinuteMap.get(minute).get(key);
				actualValue = actualValue == null ? 1l : actualValue + 1;
				groupByMinuteMap.get(minute).put(key, actualValue);

			}
		}
	}
	
	private Function<Map<Long,Map<String, Long>>,Map<Long,Map<String, Long>>> getChunkFunction(Long baseMinute){
		
		return map->map.entrySet().stream().filter(x -> (x.getKey() >= baseMinute))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
		
	}
	
	public SaleStatsVO process(List<ProductItem> pois) {

		List<SaleStatsVO.ItemVO> itemsVO = new ArrayList<>();
		minuteLastSent = (Calendar.getInstance().getTimeInMillis() - baseTime) / (60 * 1000);

		this.computeStats(pois, salesGroupByProduct, salesGroupByProductAndMinute,
				(it) -> Arrays.asList(new String[] { it.getProduct().getName() }));
		this.computeStats(pois, salesGroupByCategory, salesGroupByCategoryAndMinute,
				(it) -> it.getProduct().getCategories().stream().map(c -> c.getName()).collect(Collectors.toList()));
		this.computeStats(pois, salesGroupByCustomer, salesGroupByCustomerAndMinute,
				(it) -> Arrays.asList(new String[] { it.getSale().getCustomer().getName() }));
		

		totalSales += pois.size();

		String customerName=null;
		Address address=null;
		
		if (!pois.isEmpty()){
			customerName=pois.get(0).getSale().getCustomer().getName();
			address=pois.get(0).getSale().getCustomer().getAddress();
		}
		
		for (ProductItem poi : pois) {
			itemsVO.add(new SaleStatsVO.ItemVO(poi.getProduct().getName(),
					poi.getProduct().getCategories().stream().map(Category::getName).collect(Collectors.toList()),
					poi.getCreatedTime()));
		}

		Function<Map<Long,Map<String, Long>>,Map<Long,Map<String, Long>>> chunkFunction=this.getChunkFunction(minuteLastSent);

		SaleStatsVO saleStats = new SaleStatsVO(salesGroupByProduct, salesGroupByCategory,salesGroupByCustomer, chunkFunction.apply(salesGroupByProductAndMinute),
				chunkFunction.apply(salesGroupByCategoryAndMinute),chunkFunction.apply(salesGroupByCustomerAndMinute), totalSales, itemsVO,customerName,address.getLatitude(),address.getLongitude());

		messagingTemplate.convertAndSend(SaleProcessor.ENDPOINT, saleStats);

		return saleStats;
	}

	public static Map<String, Long> getSalesGroupByProduct() {
		return salesGroupByProduct;
	}

	public static void setSalesGroupByProduct(Map<String, Long> salesGroupByProduct) {
		SaleProcessor.salesGroupByProduct = salesGroupByProduct;
	}

	public static Map<String, Long> getSalesGroupByCategory() {
		return salesGroupByCategory;
	}

	public static void setSalesGroupByCategory(Map<String, Long> salesGroupByCategory) {
		SaleProcessor.salesGroupByCategory = salesGroupByCategory;
	}

	public static Map<String, Long> getSalesGroupByCustomer() {
		return salesGroupByCustomer;
	}

	public static void setSalesGroupByCustomer(Map<String, Long> salesGroupByCustomer) {
		SaleProcessor.salesGroupByCustomer = salesGroupByCustomer;
	}

	public static Map<Long, Map<String, Long>> getSalesGroupByProductAndMinute() {
		return salesGroupByProductAndMinute;
	}

	public static void setSalesGroupByProductAndMinute(Map<Long, Map<String, Long>> salesGroupByProductAndMinute) {
		SaleProcessor.salesGroupByProductAndMinute = salesGroupByProductAndMinute;
	}

	public static Map<Long, Map<String, Long>> getSalesGroupByCategoryAndMinute() {
		return salesGroupByCategoryAndMinute;
	}

	public static void setSalesGroupByCategoryAndMinute(Map<Long, Map<String, Long>> salesGroupByCategoryAndMinute) {
		SaleProcessor.salesGroupByCategoryAndMinute = salesGroupByCategoryAndMinute;
	}

	public static Map<Long, Map<String, Long>> getSalesGroupByCustomerAndMinute() {
		return salesGroupByCustomerAndMinute;
	}

	public static void setSalesGroupByCustomerAndMinute(Map<Long, Map<String, Long>> salesGroupByCustomerAndMinute) {
		SaleProcessor.salesGroupByCustomerAndMinute = salesGroupByCustomerAndMinute;
	}

	public static long getTotalSales() {
		return totalSales;
	}

	public static void setTotalSales(long totalSales) {
		SaleProcessor.totalSales = totalSales;
	}
}
