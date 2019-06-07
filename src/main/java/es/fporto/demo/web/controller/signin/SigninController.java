package es.fporto.demo.web.controller.signin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SigninController {

	@Autowired
	MessageSource messageSource;
	@RequestMapping(value = "signin")
	public String signin() {
	
		return "signin/signin";
	}
}
