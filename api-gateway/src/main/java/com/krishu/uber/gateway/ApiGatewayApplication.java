package com.krishu.uber.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Spring Cloud Gateway acts as the single public "front door" for our entire Uber backend.
// Instead of the frontend or mobile app knowing 5 different ports (8081, 8082, 8083, 8084),
// all client requests hit port 8080.
// The gateway inspects the URL path (e.g. /api/auth/**, /api/rides/**)
// and forwards the request to the right microservice using Eureka discovery (lb://).
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("==========================================================");
        System.out.println("  API Gateway is running on port 8080                     ");
        System.out.println("  All client requests should be sent to http://localhost:8080");
        System.out.println("==========================================================");
    }
}
