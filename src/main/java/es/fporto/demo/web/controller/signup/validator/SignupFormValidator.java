package es.fporto.demo.web.controller.signup.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import es.fporto.demo.web.account.service.AccountService;
import es.fporto.demo.web.controller.signup.SignupForm;

@Component

public class SignupFormValidator implements Validator {

	private static final String NOT_BLANK_MESSAGE = "notBlank.message";
	private static final String ACCOUNT_EXISTS_MESSAGE = "accountExists.message";
	private static final String EMAIL_MESSAGE = "email.message";
	private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w\\d._-]+@[\\w\\d.-]+\\.[\\w\\d]{2,6}$");

	
	@Autowired
	private AccountService accountService;

	@Override
	public boolean supports(Class<?> clazz) {
		return SignupForm.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors err) {

		ValidationUtils.rejectIfEmptyOrWhitespace(err, "email", SignupFormValidator.NOT_BLANK_MESSAGE);
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "password", SignupFormValidator.NOT_BLANK_MESSAGE);
		
		SignupForm form = (SignupForm) obj;
		try {
			if (accountService.findByUserName(form.getEmail()) != null) {
				err.rejectValue("email", SignupFormValidator.ACCOUNT_EXISTS_MESSAGE);
			}
		} catch (UsernameNotFoundException e) {
		}
		
		if (form.getEmail() != null && !EMAIL_REGEX.matcher(form.getEmail()).matches()) {
			err.rejectValue("email", SignupFormValidator.EMAIL_MESSAGE);
		}

	}
}
