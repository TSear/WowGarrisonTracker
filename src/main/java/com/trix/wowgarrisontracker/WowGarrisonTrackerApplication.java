package com.trix.wowgarrisontracker;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;

@SpringBootApplication
@EnableAsync
public class WowGarrisonTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WowGarrisonTrackerApplication.class, args);
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(2);
		executor.setCorePoolSize(2);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("AuctionHouseUpdate-");
		executor.initialize();
		return executor;
	}

	
}
