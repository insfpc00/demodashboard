package es.fporto.demo.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteSource;

import es.fporto.demo.controller.data.SaleRequest;
import es.fporto.demo.filter.ratelimitier.RateLimiter;
import es.fporto.demo.filter.stream.CustomHttpServletRequestWrapper;


//@Component
public class RateLimiterFilter implements Filter {

	private int rateLimit;
	private int rateLimitDuration;	
	
	public RateLimiterFilter(int rateLimit,int rateLimitDuration) {
		this.rateLimit=rateLimit;
		this.rateLimitDuration=rateLimitDuration;
	}
	
	private static final Map<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (request instanceof HttpServletRequest
				&& ((HttpServletRequest) request).getMethod().equals(HttpMethod.POST.toString())) {
			CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper(
					(HttpServletRequest) request);

			String body = ByteSource.wrap(requestWrapper.getBody()).asCharSource(StandardCharsets.UTF_8).read();

			SaleRequest saleRequest = new ObjectMapper().readValue(body, SaleRequest.class);

			RateLimiter rateLimiter = rateLimiters.computeIfAbsent(saleRequest.getCustomerCIF(),
					s -> new RateLimiter(rateLimit,rateLimitDuration));

			if (rateLimiter.tryAcquire()) {
				chain.doFilter(requestWrapper, response);
			} else {
				((HttpServletResponse) response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
				return;
			}
		}
		chain.doFilter(request, response);
	}

}
