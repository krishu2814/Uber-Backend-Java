package com.krishu.uber.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// User & Authentication Microservice
// Manages riders, drivers, login accounts, and customer profiles.
// Registers dynamically with Eureka Service Registry.
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("==========================================================");
        System.out.println("  User & Auth Microservice is running on port 8081        ");
        System.out.println("  Registered with Eureka as: USER-SERVICE                 ");
        System.out.println("==========================================================");
    }
}
