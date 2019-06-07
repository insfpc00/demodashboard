package es.fporto.demo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.fporto.demo.data.model.Sale;

@RepositoryRestResource
public interface SaleRepository extends PagingAndSortingRepository<Sale,Long> {

}
