package es.fporto.demo.web.controller.theme;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.fporto.demo.data.model.account.Account;
import es.fporto.demo.web.account.service.AccountService;
import es.fporto.demo.web.account.service.CustomUserDetails;

@Controller
public class ThemeController {
	
	private final static String THEME_VIEW_NAME="theme/theme";
	
	@Autowired
	AccountService accountService;
	
	@GetMapping("/theme")
	public String theme(Principal principal) {
		return THEME_VIEW_NAME;
	}
	
	@ModelAttribute("themeForm")
	public ThemeForm getThemeForm() {		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user= (CustomUserDetails) authentication.getPrincipal();
		
		return new ThemeForm(user.getTheme());
	}
	
	@PostMapping("/updateTheme")
	public String updateTheme(HttpServletRequest request,@ModelAttribute ThemeForm themeForm, Errors errors, RedirectAttributes ra) {		
		
		if (errors.hasErrors()) {
			return THEME_VIEW_NAME;
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user= (CustomUserDetails) authentication.getPrincipal();
		user.setTheme(themeForm.getTheme());
		
		Account account=accountService.findByUserName(user.getUsername());
		account.setTheme(themeForm.getTheme());
		accountService.update(account);
		
		String referer = request.getHeader("Referer");
	    return "redirect:"+ referer;

	}
	
}
