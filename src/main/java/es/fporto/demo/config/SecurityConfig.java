package es.fporto.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import es.fporto.demo.web.account.service.AccountService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AccountService accountService;

    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices("remember-me-key", accountService);
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
        .authorizeRequests()
        	.antMatchers(HttpMethod.POST,"/sales/**").permitAll()
            .antMatchers("/","/#", "/favicon.ico", "/resources/**", "/signup","/remindPassword","/webjars/**","/changePassword*").permitAll()
            .antMatchers("/updatePassword*",
                    "/savePassword*")
            //.permitAll()
            .hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/signin")
            .permitAll()
            .failureUrl("/signin?error=1")
            .loginProcessingUrl("/authenticate") 
            .and()
        .logout()
            .logoutUrl("/logout")
            .permitAll()
            .logoutSuccessUrl("/")
            .and()
        .rememberMe()
            .rememberMeServices(rememberMeServices())
            .key("remember-me-key");	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//          .inMemoryAuthentication()
//          .withUser("user").password(passwordEncoder().encode("password")).roles("USER")
//          .and()
//          .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
		auth.eraseCredentials(true).userDetailsService(accountService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}