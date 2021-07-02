package com.trix.wowgarrisontracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableAsync
public class WowGarrisonTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WowGarrisonTrackerApplication.class, args);
    }



}
