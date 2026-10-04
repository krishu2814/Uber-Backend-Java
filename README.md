# RidePulse — Uber Backend Microservices System

RidePulse is a production-grade ride-hailing backend engine built with **Java 21**, **Spring Boot 3**, and **Spring Cloud**.

Instead of a single monolithic backend, this project is designed using the **Microservices Architecture**, where each business capability runs as an independent, decoupled service.

---

## Architecture Overview

```
                      [ Client / Postman ]
                               │
                               ▼
        ┌──────────────────────────────────────────────┐
        │     API Gateway (Spring Cloud Gateway)       │
        │                 Port: 8080                   │
        └───────┬──────────────┬──────────────┬────────┘
                │              │              │
     Routes:    │              │              │
   /api/auth/** │ /api/drivers/** /api/rides/** /api/wallets/**
                ▼              ▼              ▼
    ┌──────────────┐   ┌──────────────┐   ┌──────────────┐   ┌──────────────┐
    │ user-service │   │driver-service│   │ ride-service │   │payment-serv. │
    │  Port: 8081  │   │  Port: 8082  │   │  Port: 8083  │   │  Port: 8084  │
    └──────┬───────┘   └──────┬───────┘   └──────┬───────┘   └──────┬───────┘
           │                  │                  │                  │
           ▼                  ▼                  ▼                  ▼
    [ userdb (H2) ]    [driverdb (H2)]    [ ridedb (H2) ]    [paymentdb(H2)]
           ▲                  ▲                  ▲                  ▲
           └──────────────────┴────────┬─────────┴──────────────────┘
                                       │
                        Registers With & Discovers Via
                                       │
                                       ▼
                     ┌────────────────────────────────────┐
                     │  Service Registry (Eureka Server)  │
                     │            Port: 8761              │
                     └────────────────────────────────────┘
```

---

## Microservices Breakdown

| Module | Port | Technology | Purpose |
|---|---|---|---|
| **`service-registry`** | `8761` | Spring Cloud Netflix Eureka | Central service directory where all microservices register dynamically. |
| **`api-gateway`** | `8080` | Spring Cloud Gateway | Single entry point that routes client requests to backend microservices. |
| **`user-service`** | `8081` | Spring Boot + JPA | Authentication, Rider & Driver accounts, and login. |
| **`driver-service`** | `8082` | Spring Boot + JPA | Driver availability, live GPS locations, and nearby driver discovery (Haversine formula). |
| **`ride-service`** | `8083` | Spring Boot + OpenFeign | Core ride lifecycle orchestrator, fare calculation, 4-digit OTP, and atomic driver acceptance. |
| **`payment-service`** | `8084` | Spring Boot + JPA | Digital wallets, ₹100 welcome bonus, ride fare deduction, and driver earnings payout. |

---

## Phase 1: Service Registry (Eureka Server)

### What is Netflix Eureka?
In a microservices system, you have multiple services running across different ports or servers.
* **Without Eureka**: Services must hardcode each other's URLs (e.g., `http://localhost:8082`), which breaks easily when scaling or changing ports.
* **With Eureka**: Every service registers its name on startup (e.g., `DRIVER-SERVICE`). When `ride-service` needs a driver, it asks Eureka for available instances dynamically.

### How to Run the Service Registry

#### Step 1: Compile the project
From the root `uber-backend` folder:
```bash
mvn clean compile
```

#### Step 2: Start the Service Registry
```bash
cd service-registry
mvn spring-boot:run
```

#### Step 3: Open the Dashboard
Open your browser and navigate to:
```
http://localhost:8761
```
You will see the **Spring Eureka Dashboard** showing system status and all registered microservice instances.
