package es.fporto.demo.rest.controller;

import java.util.concurrent.ForkJoinPool;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import es.fporto.demo.controller.data.SaleRequest;
import es.fporto.demo.controller.data.SaleResponse;
import es.fporto.demo.processor.SaleProcessor;
import es.fporto.demo.sale.service.SaleService;

@RestController
@RequestMapping(path = "/sales")
public class SaleController {

@Autowired 
private SaleService saleService;

@Autowired 
private SaleProcessor saleProcessor;


	@PostMapping
	@Transactional
	public DeferredResult<SaleResponse> addSale(@RequestBody @Valid SaleRequest request) throws Exception {

		DeferredResult<SaleResponse> deferredResult = new DeferredResult<>();

		ForkJoinPool.commonPool().submit(() -> {
			try {
				SaleResponse response=saleService.processSale(request);
				deferredResult.setResult(response);
				saleProcessor.process(response.getItems());
			} catch (Exception e) {
				deferredResult.setErrorResult(e);
			}
		});   

		return deferredResult;
	}
	

	
}
