package es.fporto.demo.web.controller.signup;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.fporto.demo.data.model.account.Account;
import es.fporto.demo.web.account.service.AccountService;
import es.fporto.demo.web.controller.signup.validator.SignupFormValidator;
import es.fporto.demo.web.support.AjaxUtils;
import es.fporto.demo.web.support.MessageHelper;

@Controller
public class SignupController {

	private static final String SIGNUP_VIEW_NAME = "signup/signup";

	@Autowired
	private AccountService accountService;

	@Autowired
	SignupFormValidator signupFormValidator;

	@InitBinder
	public void dataBinding(WebDataBinder binder) {
		binder.addValidators(signupFormValidator);
	}

	@GetMapping("signup")
	String signup(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		model.addAttribute(new SignupForm());
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			return SIGNUP_VIEW_NAME.concat(" :: signupForm");
		}
		return SIGNUP_VIEW_NAME;
	}

	@PostMapping("signup")
	public String signup(@Valid @ModelAttribute SignupForm signupForm, Errors errors, RedirectAttributes ra) {
		if (errors.hasErrors()) {
			return SIGNUP_VIEW_NAME;
		}
		try {
			if (accountService.findByUserName(signupForm.getEmail()) != null) {
			}
		} catch (UsernameNotFoundException e) { e.printStackTrace();
		}

		Account account = accountService.save(signupForm.createAccount());
		accountService.signin(account);
		MessageHelper.addSuccessAttribute(ra, "signup.success");
		return "redirect:/";
	}
}