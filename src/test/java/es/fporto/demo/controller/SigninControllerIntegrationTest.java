package es.fporto.demo.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import es.fporto.demo.mail.impl.EmailServiceImpl;
import es.fporto.demo.web.account.service.AccountService;
import es.fporto.demo.web.controller.signin.SigninController;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SecurityConfig.class,WebMvcConfig.class,SigninController.class,ControllerTestConfig.class})
@TestPropertySource("classpath:application-test.properties")
public class SigninControllerIntegrationTest {

    
	@MockBean
	AccountService accountService;
	@MockBean
	EmailServiceImpl emailServiceImpl;
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
	    Assert.assertNotNull(wac.getBean("signinController"));
	    
	}
	
	@Test
	public void whenGet_thenResultIsOKAndReturnsContent() throws Exception {
		this.mockMvc.perform(get("/signin")).andExpect(status().isOk()).andDo(print())
				.andExpect(content().string(containsString("Sign in")));
	}
	
	
}
