package es.fporto.demo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.fporto.demo.data.model.ProductItem.StateChange;

@RepositoryRestResource
public interface StateChangeRepository extends PagingAndSortingRepository<StateChange,Long> {

}
