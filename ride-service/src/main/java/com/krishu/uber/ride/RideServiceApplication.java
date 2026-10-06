package com.krishu.uber.ride;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

// Main entry point for the Ride Booking & Dispatch Microservice
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class RideServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideServiceApplication.class, args);
        System.out.println("==========================================================");
        System.out.println("  Ride Booking & Dispatch Microservice is running on port 8083");
        System.out.println("  Registered with Eureka as: RIDE-SERVICE                 ");
        System.out.println("==========================================================");
    }
}
