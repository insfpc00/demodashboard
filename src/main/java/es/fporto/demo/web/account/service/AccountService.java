package es.fporto.demo.web.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.fporto.demo.data.model.account.Account;
import es.fporto.demo.data.model.account.PasswordResetToken;
import es.fporto.demo.repository.AccountRepository;
import es.fporto.demo.repository.PasswordResetTokenRepository;

import javax.annotation.PostConstruct;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountService implements UserDetailsService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@PostConstruct
	protected void initialize() {
		save(new Account("user", passwordEncoder.encode("demo"), "ROLE_USER"));
		save(new Account("admin",passwordEncoder.encode("admin"), "ROLE_ADMIN"));
	}

	@Transactional
	public Account save(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		accountRepository.save(account);
		return account;
	}
	
	@Transactional
	public Account update(Account account) {
		accountRepository.save(account);
		return account;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(username).get();
		if (account == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return createUser(account);
	}
	
	public Account findByUserName(String username) throws UsernameNotFoundException{
		Optional<Account> accountOpt = accountRepository.findByEmail(username);
		if (accountOpt.isEmpty()) {
			throw new UsernameNotFoundException("user not found");
		}
		
		return accountOpt.get();
	}
	
	public void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}

	private Authentication authenticate(Account account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null,
				Collections.singleton(createAuthority(account)));
	}

	private User createUser(Account account) {
		return new CustomUserDetails(account.getEmail(), account.getPassword(), Collections.singleton(createAuthority(account)),account.getTheme());
	}
	
	private GrantedAuthority createAuthority(Account account) {
		return new SimpleGrantedAuthority(account.getRole());
	}

	public boolean isValidPasswordResetToken(long id, String token) {
		
		PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
		
		if ((passToken == null) || (passToken.getAccount().getId() != id)) {
			return false;
		}

		if ((passToken.getExpiryDate().getTime() < Calendar.getInstance().getTimeInMillis())) {
			return false;
		}
		Authentication auth = new UsernamePasswordAuthenticationToken(passToken.getAccount(), null,
				Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		return true;
	}
	
	public void createPasswordResetToken(Account account, String tokenStr) {
	    PasswordResetToken token = new PasswordResetToken(tokenStr, account);
	    passwordResetTokenRepository.save(token);
	  }
	
	public void changeAccountPassword(Account account, String password) {
		account.setPassword(passwordEncoder.encode(password));
		accountRepository.save(account);
	}
	
}
	