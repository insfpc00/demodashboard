package es.fporto.demo.sale.service;

import es.fporto.demo.controller.data.SaleRequest;
import es.fporto.demo.controller.data.SaleResponse;

public interface SaleService {

	public SaleResponse processSale(SaleRequest request) throws Exception;

}