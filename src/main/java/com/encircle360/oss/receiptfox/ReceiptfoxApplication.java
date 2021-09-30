package com.encircle360.oss.receiptfox;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@EnableFeignClients
@SpringBootApplication
@EntityScan("com.encircle360.oss.receiptfox.model")
public class ReceiptfoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceiptfoxApplication.class, args);
    }

    @Bean
    public Executor asyncExecutor() {
        return new ThreadPoolTaskExecutor();
    }
}
