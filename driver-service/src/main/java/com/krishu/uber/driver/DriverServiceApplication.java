package com.krishu.uber.driver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// Main entry point for the Driver & Location Microservice
@SpringBootApplication
@EnableDiscoveryClient
public class DriverServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverServiceApplication.class, args);
        System.out.println("==========================================================");
        System.out.println("  Driver & Location Microservice is running on port 8082   ");
        System.out.println("  Registered with Eureka as: DRIVER-SERVICE               ");
        System.out.println("==========================================================");
    }
}
