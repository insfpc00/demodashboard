package es.fporto.demo.websocket;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import es.fporto.demo.config.WebSocketConfig;
import es.fporto.demo.view.model.SaleStatsVO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes= {WebSocketConfig.class,WebSocketTest.WebSocketTestConfiguration.class})
@TestPropertySource(locations="classpath:application-test.properties")

public class WebSocketTest {

	@LocalServerPort
    private int port;
    private String URL;
    
    private CompletableFuture<SaleStatsVO> completableFuture;

    private static final String ENDPOINT = "/topic/items";
    
    @Before
    public void setup() {
        completableFuture = new CompletableFuture<>();
        URL = "ws://localhost:" + port + "/websocket";
    }

    
    @Test

    public void sockJSisFullyWorking() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
    			
    	String uuid = UUID.randomUUID().toString();
        
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport( new StandardWebSocketClient()) );
        WebSocketClient transport = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(transport);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());        
        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
          }).get(10, SECONDS);
        
        stompSession.subscribe(ENDPOINT + uuid, new SaleStatsVOStompFrameHandler());
        stompSession.send(ENDPOINT + uuid, new SaleStatsVO());

        assertNotNull(completableFuture.get(10,SECONDS));
    	
    }
    
	
    public class SaleStatsVOStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return SaleStatsVO.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
        	completableFuture.complete((SaleStatsVO) o);
        }
    }
    
    
    @Configuration
    @EnableWebSecurity
    @EnableAutoConfiguration(exclude= {SecurityAutoConfiguration.class})
	public static class WebSocketTestConfiguration extends WebSecurityConfigurerAdapter {

    	 
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().permitAll();

		}

	}
}