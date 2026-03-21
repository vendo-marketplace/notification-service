# Notification Service

Service responsible for managing and sending **notifications** in the Vendo platform.

Currently, the service supports sending **email notifications** (powered by Brevo) by consuming asynchronous events from Kafka (e.g., user registration, order updates).

---

# Tech Stack

* Java 17
* Spring Boot
* Kafka
* Redis
* Docker
* Brevo API / SDK
* Eureka
* Zipkin
* MapStruct
* Lombok
* Maven
* JUnit 5
* Mockito

---

# Architecture

The service follows **Hexagonal Architecture (Ports and Adapters)**.

The application is designed to be highly decoupled, separating the core application logic from messaging mechanisms (Kafka) and third-party integrations (Brevo).

## Layers

**application**

Contains the core application use cases and business logic.
* Application services

**port**

Defines interfaces used to communicate with external systems.
* Output ports for sending emails & rendering messages

**adapter**

Implementations of the ports for external integrations.
* **adapter.in:** Entry points to the application
* **adapter.out:** Outgoing integrations

**infrastructure**

Framework-specific configurations and bean definitions.
* Configurations for Service Discovery, Messaging, and Mail integrations.

---

# Project Structure

```text
src
 └── main
     └── java
         └── com.vendo.notification-service
             ├── adapter
             │   ├── mail/out/brevo
             │   └── otp
             │       ├── in/messaging
             │       └── out
             ├── application
             │   └── otp
             ├── infrastructure
             │   └── config
             └── port
                 ├── mail
                 └── otp
```

---

# Prerequisites

Before running this service, you need to start required infrastructure services.

## Dependencies

This service depends on:

- **Config Server** – provides externalized configuration
- **Service Registry (Eureka)** – service discovery
- **Kafka & Zookeeper** – event broker
- **Redis** – caching / idempotency storage
- **Zipkin** – distributed tracing

---

## 1. Run Infrastructure (Docker)

Start the required middleware (Kafka, Redis, Zipkin) using the provided configuration:

```bash
docker-compose up -d
```

## 2. Clone and run Config Server

```bash
git clone [https://github.com/vendo-marketplace/config-server](https://github.com/vendo-marketplace/config-server)
cd config-server
mvn spring-boot:run
```

## 3. Clone and run Service Registry

```bash
git clone [https://github.com/vendo-marketplace/registry-service](https://github.com/vendo-marketplace/registry-service)
cd registry-service
mvn spring-boot:run
```

---

# Running the Service

Build and run the application:

```bash
mvn clean package
java -jar target/notification-service.jar
```

---

# Environment Variables

| Variable          | Description       | Default   |
|-------------------|-------------------|-----------|
| CONFIG_SERVER_URL | Config server url | 8010      |

---

# Running Tests

Run all tests

```
mvn test
```

Run integration tests

```
mvn verify
```

---

# Code Style

The project follows standard **Java code conventions**.

Key principles:

* Clean Architecture
* SOLID principles
* Clear separation between layers
* Constructor injection
* Event-Driven approach for asynchronous tasks

---

# Contributing

1. Create feature branch
2. Write tests
3. Ensure tests pass
4. Create pull request