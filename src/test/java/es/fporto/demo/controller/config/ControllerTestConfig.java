package es.fporto.demo.controller.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class ControllerTestConfig {
	@MockBean
	private MessageSource messageSource;

	@Bean
	public MessageSource messageSource() {
		messageSource = Mockito.mock(MessageSource.class);
		when(messageSource.getMessage(anyString(), any(Object[].class), any(Locale.class))).thenReturn("MockedMessage");
		when(messageSource.getMessage(anyString(), any(Object[].class),anyString(),any(Locale.class))).thenReturn("MockedMessage");
		when(messageSource.getMessage(any(MessageSourceResolvable.class), any(Locale.class))).thenReturn("MockedMessage");
		
		return messageSource;
	}
}
