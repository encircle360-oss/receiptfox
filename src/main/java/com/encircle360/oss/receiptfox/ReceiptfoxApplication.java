package com.encircle360.oss.receiptfox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.encircle360.oss.receiptfox.model")
public class ReceiptfoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceiptfoxApplication.class, args);
    }

}
