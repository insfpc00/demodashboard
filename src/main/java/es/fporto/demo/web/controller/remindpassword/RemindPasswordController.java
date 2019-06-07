package es.fporto.demo.web.controller.remindpassword;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.fporto.demo.data.model.account.Account;
import es.fporto.demo.mail.EmailService;
import es.fporto.demo.web.account.service.AccountService;
import es.fporto.demo.web.support.AjaxUtils;

@Controller
public class RemindPasswordController {

	private static final String REMIND_PASSWORD_VIEW_NAME = "remindpassword/remindPassword";
	private static final String UPDATE_PASSWORD_VIEW_NAME = "updatepassword/updatePassword";
	private static final String CHANGE_PASSWORD_URL = "http://localhost:8080/changePassword";

	@Autowired
	private EmailService emailService;

	@Autowired
	AccountService accountService;

	@GetMapping("remindPassword")
	String signup(Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

		model.addAttribute(new RemindPasswordForm());
		if (AjaxUtils.isAjaxRequest(requestedWith)) {
			return REMIND_PASSWORD_VIEW_NAME.concat(" :: remindPasswordForm");
		}
		return REMIND_PASSWORD_VIEW_NAME;
	}

	@PostMapping("remindPassword")
	public String signup(@Valid @ModelAttribute RemindPasswordForm remindPasswordForm, Errors errors,
			RedirectAttributes ra) {

		if (errors.hasErrors()) {
			return REMIND_PASSWORD_VIEW_NAME;
		}
		
		Account account = accountService.findByUserName(remindPasswordForm.getEmail());
		String token = UUID.randomUUID().toString();
		System.out.println(token);
		System.out.println(account.getId());
		emailService.sendSimpleMessage(remindPasswordForm.getEmail(), "Password Reminder",
				"Change your password here: "+CHANGE_PASSWORD_URL+"?id=" + account.getId() + "&token="
						+ token);
		accountService.createPasswordResetToken(account, token);

		return "redirect:/";
	}

	@GetMapping("changePassword")
	public String showChangePasswordPage(Model model, @RequestParam("id") long id,
			@RequestParam("token") String token) {
		//Boolean isValidPasswordResetToken = accountService.isValidPasswordResetToken(id, token);
//		    if (result != null) {
//		        model.addAttribute("message", 
//		          messages.getMessage("auth.message." + result, null, locale));
//		        return "redirect:/login?lang=" + locale.getLanguage();
//		    }

		model.addAttribute(new ChangePasswordForm());
		return UPDATE_PASSWORD_VIEW_NAME;
	}

	@PostMapping(value = "updatePassword")
	public String savePassword(@Valid @ModelAttribute ChangePasswordForm passwordForm,Errors errors,
			RedirectAttributes ra) {
		Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		accountService.changeAccountPassword(account, passwordForm.getNewPassword());
		return "redirect:/";
	}

}
