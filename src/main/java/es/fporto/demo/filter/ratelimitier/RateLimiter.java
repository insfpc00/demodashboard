package es.fporto.demo.filter.ratelimitier;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class RateLimiter {

	private Semaphore semaphore;
	
	public RateLimiter(int rateLimit,int rateLimitDuration) {

		this.semaphore = new Semaphore(rateLimit);
		
		ScheduledExecutorService  scheduler = Executors.newScheduledThreadPool(1);
		
		scheduler.scheduleAtFixedRate(() -> {
			semaphore.release(rateLimit - semaphore.availablePermits());
		}, 1, rateLimitDuration, TimeUnit.SECONDS);
	}
	
	public boolean tryAcquire() {
		return semaphore.tryAcquire();
	}

	
}