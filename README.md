<div align="center">

# 🚖 UberApp Backend

### A production-grade Ride Hailing REST API built with Java 21 · Spring Boot 3.3 · Spring Security · JWT · PostGIS

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.1-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-success?style=for-the-badge&logo=springsecurity)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-PostGIS-blue?style=for-the-badge&logo=postgresql)](https://postgis.net/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D?style=for-the-badge&logo=swagger)](http://localhost:8080/swagger-ui/index.html)
[![License](https://img.shields.io/badge/License-MIT-lightgrey?style=for-the-badge)](LICENSE)

</div>

---

## 📖 Overview

**UberApp Backend** is a fully functional backend system for an Uber-like ride-hailing application. It handles the complete ride lifecycle — from rider requesting a ride to driver accepting, starting, completing, and rating — backed by real geospatial queries, JWT-based authentication, strategy-pattern business logic, and a wallet payment system.

The project is designed following **clean architecture principles**: thin controllers, interface-driven service layer, repository abstraction, and separate strategy classes for pluggable business rules.

---

## ✨ Core Features

### 🔐 Authentication & Security
- JWT Access Token (24h expiry) + Refresh Token (6 months, stored in **HttpOnly Cookie**)
- Role-based access control — `ROLE_ADMIN`, `ROLE_DRIVER`, `ROLE_RIDER`
- Spring Security filter chain with a custom `JwtAuthFilter`
- Secure password hashing via Spring Security's `PasswordEncoder`
- Token refresh endpoint reads refresh token from cookie (never exposed in response body)

### 🗺️ Geospatial Driver Matching (PostGIS)
- Driver locations stored as `Geometry(Point, 4326)` using **Hibernate Spatial**
- Two native SQL strategies using PostGIS functions:
  - **Nearest Driver** — `ST_DWithin` (10 km radius) + `ST_Distance` ORDER BY
  - **Top-Rated Driver** — `ST_DWithin` (15 km radius) + ORDER BY `rating DESC`
- Distance calculation via **OSRM** (Open Source Routing Machine) API for real road distances

### 💰 Fare Calculation (Strategy Pattern)
- `RideFareCalculationStrategy` interface with two implementations:
  - **Default Fare** — base rate × road distance
  - **Surge Pricing** — base rate × road distance × **2x surge multiplier**
- `RideStrategyManager` selects the correct strategy at runtime based on demand

### 💳 Payment System (Strategy Pattern)
- `PaymentStrategy` interface with two implementations:
  - **Cash Payment** — marks payment confirmed, adds fare to driver wallet
  - **Wallet Payment** — debits rider wallet, credits driver wallet, logs transactions
- `PaymentStrategyManager` resolves strategy from `PaymentMethod` enum at runtime

### 🚗 Ride Lifecycle Management
- Full state machine: `PENDING → CONFIRMED → ONGOING → ENDED / CANCELLED`
- OTP-verified ride start (driver submits OTP, rider receives it on booking)
- Atomic `@Transactional` operations across ride, payment, and driver availability
- Paginated ride history for both rider and driver (sorted by `createdTime DESC`)

### ⭐ Ratings System
- Rider rates driver; driver rates rider — both stored per ride
- Average rating auto-computed and persisted on `Driver` and `Rider` entities

### 👛 Wallet & Transactions
- Each user has a `Wallet` with balance tracking
- `WalletTransaction` records every debit/credit with `TransactionType` and `TransactionMethod`
- Wallet created automatically on user signup

### 📧 Email Notifications
- Spring Mail (SMTP/Gmail) integration via `EmailSenderService`
- Sends transactional emails (ride confirmations, onboarding, etc.)

### 📋 Global API Standards
- `GlobalResponseHandler` wraps every successful response in `ApiResponse<T>`
- `GlobalExceptionHandler` maps exceptions to structured `ApiError` responses:
  - `ResourceNotFoundException` → 404
  - `RunTimeConflictException` → 409
  - `JwtException` → 401
  - `AccessDeniedException` → 403
  - `MethodArgumentNotValidException` → 400 with field-level sub-errors
- **Swagger / OpenAPI UI** auto-generated at `/swagger-ui/index.html`
- Spring Boot Actuator enabled for health and metrics

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.1 |
| Security | Spring Security + JJWT 0.12.6 |
| ORM | Spring Data JPA + Hibernate 6 |
| Geospatial | Hibernate Spatial + PostGIS |
| Database | PostgreSQL |
| Distance API | OSRM (Open Source Routing Machine) |
| Mapping | ModelMapper 3.2.0 |
| API Docs | SpringDoc OpenAPI (Swagger UI) 2.6.0 |
| Monitoring | Spring Boot Actuator |
| Email | Spring Mail (Gmail SMTP) |
| Build | Maven (Maven Wrapper included) |
| Utilities | Lombok |

---

## 📐 Architecture

```
┌─────────────────────────────────────────────────────┐
│                    Client / Postman                 │
└────────────────────────┬────────────────────────────┘
                         │ HTTP
┌────────────────────────▼────────────────────────────┐
│              JwtAuthFilter (Security Layer)         │
│         Validates JWT → sets SecurityContext        │
└────────────────────────┬────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────┐
│               REST Controllers (3)                  │
│         AuthController / DriverController           │
│                  RiderController                    │
└──────────┬──────────────────────────┬───────────────┘
           │                          │
┌──────────▼──────────┐  ┌────────────▼──────────────┐
│   Service Layer     │  │    Strategy Layer          │
│  AuthService        │  │  DriverMatchingStrategy    │
│  RideService        │  │  RideFareCalculationStrat. │
│  DriverService      │  │  PaymentStrategy           │
│  RiderService       │  │  (Strategy Pattern)        │
│  PaymentService     │  └────────────────────────────┘
│  WalletService      │
│  RatingService      │
│  EmailService       │
│  DistanceService    │
└──────────┬──────────┘
           │
┌──────────▼──────────────────────────────────────────┐
│              Repository Layer (JPA)                 │
│  DriverRepo / RiderRepo / RideRepo / UserRepo       │
│  WalletRepo / PaymentRepo / RatingRepo              │
└──────────┬──────────────────────────────────────────┘
           │
┌──────────▼──────────────────────────────────────────┐
│           PostgreSQL + PostGIS Database             │
└─────────────────────────────────────────────────────┘
```

---

## 📂 Project Structure

```
uberApp-Backend/
├── src/main/java/com/appdefine/uber/uberApp/
│   ├── advices/
│   │   ├── ApiError.java                   # Error response model
│   │   ├── ApiResponse.java                # Unified response wrapper
│   │   ├── GlobalExceptionHandler.java     # @RestControllerAdvice
│   │   └── GlobalResponseHandler.java      # Auto-wraps all responses
│   │
│   ├── configs/
│   │   ├── MapperConfig.java               # ModelMapper bean
│   │   ├── SecurityConfig.java             # JWT filter, CORS, CSRF
│   │   └── WebSecurityConfig.java          # HTTP security rules
│   │
│   ├── controllers/
│   │   ├── AuthController.java             # /auth/*
│   │   ├── DriverController.java           # /drivers/* (ROLE_DRIVER)
│   │   └── RiderController.java            # /riders/* (ROLE_RIDER)
│   │
│   ├── dto/                                # Request/Response DTOs
│   ├── entities/                           # JPA Entities
│   │   ├── enums/                          # RideStatus, PaymentMethod, Role ...
│   │   ├── Driver.java
│   │   ├── Ride.java                       # Geometry(Point,4326) fields
│   │   ├── RideRequest.java
│   │   ├── Rider.java
│   │   ├── User.java
│   │   ├── Wallet.java
│   │   ├── WalletTransaction.java
│   │   ├── Payment.java
│   │   └── Rating.java
│   │
│   ├── exceptions/
│   │   ├── ResourceNotFoundException.java
│   │   └── RunTimeConflictException.java
│   │
│   ├── repositories/                       # Spring Data JPA Repositories
│   │   └── DriverRepository.java           # Native PostGIS spatial queries
│   │
│   ├── security/
│   │   ├── JWTService.java                 # Token generation & validation
│   │   └── JwtAuthFilter.java              # OncePerRequestFilter
│   │
│   ├── services/                           # Service interfaces
│   │   └── impl/                           # Service implementations
│   │       ├── DistanceServiceOSRMImpl.java # OSRM road distance API
│   │       ├── DriverServiceImpl.java
│   │       ├── RiderServiceImpl.java
│   │       ├── RideServiceImpl.java
│   │       ├── PaymentServiceImpl.java
│   │       ├── WalletServiceImpl.java
│   │       ├── RatingServiceImpl.java
│   │       └── EmailSenderServiceImpl.java
│   │
│   ├── strategies/                         # Strategy pattern interfaces
│   │   └── impl/
│   │       ├── DriverMatchingNearestDriverStrategy.java
│   │       ├── DriverMatchingHighestRatedDriverStrategy.java
│   │       ├── RideFareDefaultFareCalculationStrategy.java
│   │       ├── RideFareSurgePricingFareCalculationStrategy.java
│   │       ├── CashPaymentStrategy.java
│   │       └── WalletPaymentStrategy.java
│   │
│   └── utils/
│       └── GeometryUtil.java               # WKT ↔ Point conversion helpers
│
├── src/main/resources/
│   ├── application.properties
│   └── data.sql                            # Seed data
│
├── uberApp.postman_collection.json         # Ready-to-import Postman collection
├── pom.xml
└── mvnw / mvnw.cmd
```

---

## 🌐 API Reference

> All protected endpoints require `Authorization: Bearer <access_token>` header.

### Auth — `/auth`

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| `POST` | `/auth/signup` | Public | Register a new user |
| `POST` | `/auth/login` | Public | Login, returns access token; sets refresh token as HttpOnly cookie |
| `GET` | `/auth/refresh` | Public | Issue new access token using refresh token cookie |
| `POST` | `/auth/onBoardNewDriver/{userId}` | `ADMIN` | Promote user to driver with vehicle ID |

### Driver — `/drivers` *(requires `ROLE_DRIVER`)*

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/drivers/acceptRide/{rideRequestId}` | Accept a pending ride request |
| `POST` | `/drivers/startRide/{rideRequestId}` | Start ride after OTP verification |
| `POST` | `/drivers/endRide/{rideId}` | Complete the ride, trigger payment |
| `POST` | `/drivers/cancelRide/{rideId}` | Cancel a confirmed ride |
| `POST` | `/drivers/rateRider` | Rate the rider after ride completion |
| `GET` | `/drivers/getMyProfile` | Get authenticated driver's profile |
| `GET` | `/drivers/getMyRides?pageOffset=0&pageSize=10` | Paginated ride history |

### Rider — `/riders` *(requires `ROLE_RIDER`)*

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/riders/requestRide` | Request a new ride with pickup & drop-off location |
| `POST` | `/riders/cancelRide/{rideId}` | Cancel a ride |
| `POST` | `/riders/rateDriver` | Rate the driver after ride |
| `GET` | `/riders/getMyProfile` | Get authenticated rider's profile |
| `GET` | `/riders/getMyRides?pageOffset=0&pageSize=10` | Paginated ride history |

---

## ⚙️ Getting Started

### Prerequisites

- Java 21+
- PostgreSQL 14+ with **PostGIS** extension enabled
- Maven (or use included `./mvnw`)

### 1. Clone the Repository

```bash
git clone https://github.com/AppDefine/uberApp-Backend.git
cd uberApp-Backend
```

### 2. Setup Database

```sql
CREATE DATABASE uberApp;
\c uberApp
CREATE EXTENSION postgis;
```

### 3. Configure `application.properties`

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/uberApp?useSSL=false
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update

# JWT
jwt.secretKey=your_strong_secret_key_min_32_chars

# SMTP (Gmail example)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 4. Build & Run

```bash
# Build
./mvnw clean install

# Run
./mvnw spring-boot:run
```

The server starts at `http://localhost:8080`

### 5. Explore APIs

| Tool | URL |
|------|-----|
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| Actuator Health | http://localhost:8080/actuator/health |
| Postman Collection | Import `uberApp.postman_collection.json` |

---

## 🔑 Authentication Flow

```
POST /auth/signup         →  Create account
POST /auth/login          →  Get access token (+ refresh token in HttpOnly cookie)
                             Add header: Authorization: Bearer <access_token>
GET  /auth/refresh        →  Renew access token using cookie (no body needed)
```

---

## 🧠 Design Patterns Used

| Pattern | Where Applied |
|---------|--------------|
| **Strategy** | Driver matching (Nearest / Highest Rated) |
| **Strategy** | Fare calculation (Default / Surge Pricing) |
| **Strategy** | Payment processing (Cash / Wallet) |
| **Factory / Manager** | `RideStrategyManager`, `PaymentStrategyManager` resolve strategies at runtime |
| **Repository** | Spring Data JPA with custom native PostGIS queries |
| **DTO** | All controller I/O uses DTOs, never entities directly |
| **Global Advice** | `@RestControllerAdvice` for centralized error handling and response wrapping |

---

## 🧪 Testing

```bash
./mvnw test
```

A Postman collection with pre-configured requests for all endpoints is included:

```
uberApp.postman_collection.json
```

Import it into Postman and set the `base_url` variable to `http://localhost:8080`.

---

## 🔒 Security Highlights

- Passwords never stored in plain text — BCrypt hashing via Spring Security
- JWT signed with HMAC-SHA256; validated on every request via `JwtAuthFilter`
- Refresh token stored in **HttpOnly cookie** — inaccessible to JavaScript (XSS protection)
- Role-based endpoint guards using `@Secured` annotations
- Input validation with `@Valid` + field-level error messages in response

---

## 🗺️ Geospatial Queries (PostGIS)

Driver locations use the WGS-84 coordinate system (`SRID 4326`). Example native query:

```sql
-- Find 10 nearest available drivers within 10 km
SELECT d.*, ST_Distance(d.current_location, :pickUpLocation) AS distance
FROM driver d
WHERE d.available = true
  AND ST_DWithin(d.current_location, :pickUpLocation, 10000)
ORDER BY distance
LIMIT 10;
```

---

## 📦 Key Maven Dependencies

```xml
spring-boot-starter-web
spring-boot-starter-data-jpa
spring-boot-starter-security
spring-boot-starter-mail
spring-boot-starter-actuator
hibernate-spatial:6.5.2.Final
jjwt-api/impl/jackson:0.12.6
springdoc-openapi-starter-webmvc-ui:2.6.0
modelmapper:3.2.0
postgresql (runtime)
lombok
```

---

## 🚀 Planned Enhancements

- [ ] Google Maps / HERE Maps integration for live routing
- [ ] WebSocket — real-time ride status updates
- [ ] OTP delivery via SMS (Twilio)
- [ ] Push notifications (Firebase FCM)
- [ ] Admin dashboard APIs
- [ ] Ride fare estimation before booking
- [ ] Docker + Docker Compose setup
- [ ] CI/CD pipeline (GitHub Actions)

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m "feat: add your feature"`
4. Push: `git push origin feature/your-feature`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

<div align="center">

Made with ❤️ using **Java 21 · Spring Boot 3.3 · Spring Security · PostGIS**

</div>
