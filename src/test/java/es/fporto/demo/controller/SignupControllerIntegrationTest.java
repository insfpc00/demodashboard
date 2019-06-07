package es.fporto.demo.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import es.fporto.demo.config.SecurityConfig;
import es.fporto.demo.config.WebMvcConfig;
import es.fporto.demo.controller.config.ControllerTestConfig;
import es.fporto.demo.controller.config.SpringSecurityWebAuxTestConfig;
import es.fporto.demo.web.account.service.AccountService;
import es.fporto.demo.web.controller.signup.SignupController;
import es.fporto.demo.web.controller.signup.SignupForm;
import es.fporto.demo.web.controller.signup.validator.SignupFormValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SecurityConfig.class,WebMvcConfig.class,ControllerTestConfig.class,SignupController.class,SignupFormValidator.class})
@TestPropertySource("classpath:application-test.properties")
public class SignupControllerIntegrationTest {

    
	@MockBean
	AccountService accountService;
	@Autowired
	private WebApplicationContext wac;
	
	MockMvc mockMvc;
	
	@Before
	 public void setup() {
		
		 this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	 }
	
	@Test
	public void givenWac_whenServletContext_thenItProvidesSigninController() {
	    ServletContext servletContext = wac.getServletContext();
	     
	    Assert.assertNotNull(servletContext);
	    Assert.assertTrue(servletContext instanceof MockServletContext);
	    Assert.assertNotNull(wac.getBean("signupController"));
	    
	}
	
	@Test
	public void whenGet_thenResultIsOKAndReturnsContent() throws Exception {
		this.mockMvc.perform(get("/signup")).andExpect(status().isOk()).andDo(print())
				.andExpect(content().string(containsString("Sign up")));
	}
	
	@Test
	public void givenValidUserAndPassword_whenPostSignupForm_thenResultIsRedirect() throws Exception {
		this.mockMvc.perform(
				post("/signup").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("email", "newuser@demo.com")
						.param("password", "StrongPassword12").sessionAttr("signupForm", new SignupForm()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/"));
	}
	
	@Test
	public void givenNotValidUserAndPassword_whenPostSignupForm_thenFormContainsErrors() throws Exception {
		this.mockMvc.perform(

				post("/signup").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("email", "newuserdemo.com")
						.param("password", "StrongPassword12").sessionAttr("signupForm", new SignupForm()))
				.andExpect(status().isOk()).andDo(print())
				.andExpect(content().string(containsString("Form contains errors")))
				.andExpect(model().attributeHasFieldErrors("signupForm", "email"));
		
	}
	

}