package com.krishu.uber.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// Main entry point for the Payment & Digital Wallet Microservice
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
        System.out.println("==========================================================");
        System.out.println("  Payment & Wallet Microservice is running on port 8084    ");
        System.out.println("  Registered with Eureka as: PAYMENT-SERVICE              ");
        System.out.println("==========================================================");
    }
}
