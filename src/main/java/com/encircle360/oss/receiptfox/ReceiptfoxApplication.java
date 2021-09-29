package com.encircle360.oss.receiptfox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@EntityScan("com.encircle360.oss.receiptfox.model")
public class ReceiptfoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceiptfoxApplication.class, args);
    }

}
