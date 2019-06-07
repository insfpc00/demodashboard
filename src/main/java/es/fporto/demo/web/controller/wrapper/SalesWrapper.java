package es.fporto.demo.web.controller.wrapper;

import es.fporto.demo.data.model.Sale;
import es.fporto.demo.view.model.SaleStatsVO;

public class SalesWrapper {
	
	private Iterable<Sale> sales;
	private SaleStatsVO stats;
	
	public SalesWrapper(Iterable<Sale> sales, SaleStatsVO stats) {
		super();
		this.sales = sales;
		this.stats = stats;
	}
	
	public SalesWrapper() {
		super();
	}
	public Iterable<Sale> getSales() {
		return sales;
	}
	public void setSales(Iterable<Sale> sales) {
		this.sales = sales;
	}
	public SaleStatsVO getStats() {
		return stats;
	}
	public void setStats(SaleStatsVO stats) {
		this.stats = stats;
	}
	
}
