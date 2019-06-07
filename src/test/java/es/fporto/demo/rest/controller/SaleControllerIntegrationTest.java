package es.fporto.demo.rest.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.Every.everyItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import es.fporto.demo.DemoApplication;
import es.fporto.demo.controller.data.SaleRequest;
import es.fporto.demo.controller.data.SaleRequestItem;
import es.fporto.demo.controller.data.SaleResponse;
import es.fporto.demo.controller.exception.CustomExceptionHandler;
import es.fporto.demo.data.model.ItemState;
import es.fporto.demo.loader.DataLoader;
import es.fporto.demo.processor.SaleProcessor;
import es.fporto.demo.sale.service.SaleServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {
		SaleControllerIntegrationTest.SaleControllerIntegrationTestConfiguration.class, SaleServiceImpl.class,
		SaleController.class, CustomExceptionHandler.class,DataLoader.class })

@TestPropertySource(locations = "classpath:application-test.properties")
public class SaleControllerIntegrationTest {

	@LocalServerPort
	private int port;
	@MockBean
	SaleProcessor saleProcessor;
	@Autowired
	DataLoader dataLoader;
	
	private String salesUrl;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();

	@Before
	public void setup() {

		salesUrl = "http://localhost:" + port + "/sales";
		dataLoader.initDb();

	}

	@Test
	public void givenNotValidCIF_thenItReturnsError() {

		SaleRequest request = new SaleRequest();

		request.setCustomerCIF("notValidCIF");
		SaleRequestItem item = new SaleRequestItem("noValidProductName", 10);
		request.setItems(Arrays.asList(new SaleRequestItem[] { item }));

		HttpEntity<SaleRequest> entity = new HttpEntity<>(request, headers);

		ResponseEntity<String> response = restTemplate.exchange(salesUrl, HttpMethod.POST, entity, String.class);
		assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
		assertThat(response.getBody(), containsString("Customer not found"));

	}

	@Test
	public void givenNotValidProductName_thenItReturnsError() {

		SaleRequest request = new SaleRequest();

		request.setCustomerCIF("Q7731334D");
		SaleRequestItem item = new SaleRequestItem("noValidProductName", 10);
		request.setItems(Arrays.asList(new SaleRequestItem[] { item }));

		HttpEntity<SaleRequest> entity = new HttpEntity<>(request, headers);

		ResponseEntity<String> response = restTemplate.exchange(salesUrl, HttpMethod.POST, entity, String.class);

		assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
		assertThat(response.getBody(), containsString("Product not found"));
	}
	
	
	@Test
	public void givenValidCustomerAndProduct_thenReturnsIsOK() {

		SaleRequest request = new SaleRequest();
		
		int numberOfItemsRequested=10;
		
		request.setCustomerCIF("Q7731334D");
		SaleRequestItem item = new SaleRequestItem("Pendrive", numberOfItemsRequested);
		request.setItems(Arrays.asList(new SaleRequestItem[] { item }));

		HttpEntity<SaleRequest> entity = new HttpEntity<>(request, headers);

		ResponseEntity<SaleResponse> response = restTemplate.exchange(salesUrl, HttpMethod.POST, entity, SaleResponse.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody().getItems().size(), numberOfItemsRequested);
		assertThat(response.getBody().getItems(), (everyItem(hasProperty("state", is(ItemState.SOLD)))));
		
	}

	@Configuration
	@EnableWebSecurity
	@EnableAutoConfiguration()
	@EnableJpaRepositories(basePackageClasses = DemoApplication.class)
	@EntityScan(basePackageClasses = DemoApplication.class)
	public static class SaleControllerIntegrationTestConfiguration extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().authorizeRequests().anyRequest().permitAll();

		}

	}
}
