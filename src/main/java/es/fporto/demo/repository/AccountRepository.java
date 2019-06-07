package es.fporto.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import es.fporto.demo.data.model.account.Account;

@RepositoryRestResource
public interface AccountRepository extends PagingAndSortingRepository<Account,Long> {

	public Optional<Account> findByEmail(String email);
	
}
