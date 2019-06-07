package es.fporto.demo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import es.fporto.demo.data.model.account.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends PagingAndSortingRepository<PasswordResetToken,Long>{

	public PasswordResetToken findByToken(String token);
}
