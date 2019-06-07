package es.fporto.demo.web.account.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String theme;
	
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,String theme) {
		super(username, password, authorities);
		this.theme=theme;
	}

	public CustomUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities,String theme) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.theme=theme;
	}
	
	

	
}
