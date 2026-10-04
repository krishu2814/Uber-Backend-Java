package com.krishu.uber.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

// @EnableEurekaServer turns this Spring Boot app into a Netflix Eureka Service Registry.
// In a microservices architecture, services don't hardcode each other's IP/port.
// Instead, every microservice (User, Driver, Ride, Payment) registers here on startup.
@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistryApplication.class, args);
        System.out.println("==========================================================");
        System.out.println("  Eureka Service Registry is running on port 8761");
        System.out.println("  Open the Eureka Dashboard at: http://localhost:8761     ");
        System.out.println("==========================================================");
    }
}
