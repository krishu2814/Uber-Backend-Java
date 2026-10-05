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

## Implementation Progress

- [x] **Phase 1: Service Registry** (`service-registry` on port `8761`) — Eureka Server initialized & verified.
- [x] **Phase 2: API Gateway** (`api-gateway` on port `8080`) — Spring Cloud Gateway routing `/api/auth/**`, `/api/drivers/**`, `/api/rides/**`, `/api/wallets/**` via Eureka load balancer.
- [x] **Phase 3: User & Auth Microservice** (`user-service` on port `8081`) — User & Rider entities, signup, login, validation, and profile retrieval tested end-to-end.
- [ ] **Phase 4: Driver & Location Microservice** (`driver-service` on port `8082`) — Driver registration, status toggling, and Haversine nearby driver discovery.
- [ ] **Phase 5: Ride Booking & Lifecycle Microservice** (`ride-service` on port `8083`) — Ride requesting, fare estimation, OTP verification, and trip state machine.
- [ ] **Phase 6: Payment & Wallet Microservice** (`payment-service` on port `8084`) — ₹100 bonus on signup, atomic fare deductions, and driver payouts.

---

## How to Build the Project

From the root directory:
```bash
mvn clean compile
```

---

## Phase 1: Service Registry (Eureka Server)
* **Port**: `8761`
* **Dashboard**: `http://localhost:8761`
* **Command to run**:
  ```bash
  mvn -pl service-registry spring-boot:run
  ```

---

## Phase 2: API Gateway (Spring Cloud Gateway)
* **Port**: `8080`
* **Command to run**:
  ```bash
  mvn -pl api-gateway spring-boot:run
  ```

---

## Phase 3: User & Auth Service (`user-service`)
* **Port**: `8081`
* **Database**: `userdb` (In-memory H2 Console at `http://localhost:8081/h2-console`)
* **Command to run**:
  ```bash
  mvn -pl user-service spring-boot:run
  ```

---

## Local API Testing & Routes Guide

You can test these endpoints **directly** on `http://localhost:8081` or **via API Gateway** on `http://localhost:8080`.

### 1. Health Check
* **Endpoint**: `GET /api/auth/health`
* **cURL Command**:
  ```bash
  curl -s http://localhost:8081/api/auth/health
  ```
* **Expected Response**:
  ```
  User Service is UP and healthy on port 8081
  ```

---

### 2. Register a Rider
* **Endpoint**: `POST /api/auth/register-rider`
* **cURL Command**:
  ```bash
  curl -s -X POST http://localhost:8081/api/auth/register-rider \
    -H "Content-Type: application/json" \
    -d '{
      "name": "Rahul Sharma",
      "email": "rahul@example.com",
      "password": "password123",
      "phone": "9876543210",
      "role": "RIDER"
    }'
  ```
* **Expected Response (`201 Created`)**:
  ```json
  {
    "userId": 1,
    "riderId": 1,
    "name": "Rahul Sharma",
    "email": "rahul@example.com",
    "phone": "9876543210",
    "role": "RIDER",
    "message": "Rider registered successfully"
  }
  ```

---

### 3. Register a Driver User Account
* **Endpoint**: `POST /api/auth/register-driver`
* **cURL Command**:
  ```bash
  curl -s -X POST http://localhost:8081/api/auth/register-driver \
    -H "Content-Type: application/json" \
    -d '{
      "name": "Amit Kumar",
      "email": "amit@example.com",
      "password": "password123",
      "phone": "9876543211",
      "role": "DRIVER"
    }'
  ```
* **Expected Response (`201 Created`)**:
  ```json
  {
    "userId": 2,
    "riderId": null,
    "name": "Amit Kumar",
    "email": "amit@example.com",
    "phone": "9876543211",
    "role": "DRIVER",
    "message": "Driver user account created successfully"
  }
  ```

---

### 4. User Login
* **Endpoint**: `POST /api/auth/login`
* **cURL Command**:
  ```bash
  curl -s -X POST http://localhost:8081/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{
      "email": "rahul@example.com",
      "password": "password123"
    }'
  ```
* **Expected Response (`200 OK`)**:
  ```json
  {
    "userId": 1,
    "riderId": 1,
    "name": "Rahul Sharma",
    "email": "rahul@example.com",
    "phone": "9876543210",
    "role": "RIDER",
    "message": "Login successful"
  }
  ```

---

### 5. Duplicate Email Validation (Error Handling Test)
* **Endpoint**: `POST /api/auth/register-rider` (with already registered email)
* **cURL Command**:
  ```bash
  curl -s -X POST http://localhost:8081/api/auth/register-rider \
    -H "Content-Type: application/json" \
    -d '{
      "name": "Rahul Duplicate",
      "email": "rahul@example.com",
      "password": "password123",
      "phone": "9999999999",
      "role": "RIDER"
    }'
  ```
* **Expected Response (`400 Bad Request`)**:
  ```json
  {
    "timestamp": "2026-10-04T21:11:35.249996",
    "status": 400,
    "error": "Bad Request",
    "message": "An account with email rahul@example.com already exists"
  }
  ```

---

### 6. Get User Details by ID
* **Endpoint**: `GET /api/auth/users/{userId}`
* **cURL Command**:
  ```bash
  curl -s http://localhost:8081/api/auth/users/1
  ```
* **Expected Response (`200 OK`)**:
  ```json
  {
    "id": 1,
    "name": "Rahul Sharma",
    "email": "rahul@example.com",
    "phone": "9876543210",
    "role": "RIDER",
    "createdAt": "2026-10-04T21:11:21.123456"
  }
  ```

---

### 7. Get Rider Profile by User ID
* **Endpoint**: `GET /api/auth/riders/user/{userId}`
* **cURL Command**:
  ```bash
  curl -s http://localhost:8081/api/auth/riders/user/1
  ```
* **Expected Response (`200 OK`)**:
  ```json
  {
    "riderId": 1,
    "userId": 1,
    "name": "Rahul Sharma",
    "email": "rahul@example.com",
    "phone": "9876543210",
    "rating": 5.0
  }
  ```

> **Tip**: When `service-registry`, `api-gateway`, and `user-service` are running together, replace port `8081` with port `8080` in any of the above commands to test calls routed through the Spring Cloud API Gateway!
