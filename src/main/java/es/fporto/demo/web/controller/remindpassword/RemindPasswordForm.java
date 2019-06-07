	package es.fporto.demo.web.controller.remindpassword;
	
	import javax.validation.constraints.Email;
	
	public class RemindPasswordForm {
	
		private static final String EMAIL_MESSAGE = "{email.message}";
	
	    @Email(message = RemindPasswordForm.EMAIL_MESSAGE)
		private String email;
	
	        public String getEmail() {
			return email;
		}
	
		public void setEmail(String email) {
			this.email = email;
		}
	}
