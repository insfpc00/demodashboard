package es.fporto.demo.controller.config;

import java.util.Arrays;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import es.fporto.demo.web.account.service.CustomUserDetails;

@TestConfiguration
public class SpringSecurityWebAuxTestConfig {
	@Bean
	@Primary
	public UserDetailsService userDetailsService() {
		final UserDetails user = new CustomUserDetails("TestUser", "password",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")), "DEFAULT");
		// return new InMemoryUserDetailsManager(Arrays.asList(user));

		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				if (username.equals(user.getUsername())) {
					return user;
				} else {
					throw new UsernameNotFoundException(username);
				}
			}
		};
	}
}
