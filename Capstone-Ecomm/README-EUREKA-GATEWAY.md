This workspace adds a Spring Cloud Eureka Server and a Spring Cloud Gateway (API Gateway) configured to use Eureka for service discovery and Resilience4j for circuit breaking.

Quick run steps (development):

1. Start the Eureka server module:
   - cd eureka-server/eureka-server
   - ./mvnw spring-boot:run

2. Start each service (customer, product-catalog, inventory, order-processing, payment) in any order. Each has an `application.yml` configured to register with Eureka at http://localhost:8761/eureka/.

3. Start the API Gateway:
   - cd api-gateway/api-gateway
   - ./mvnw spring-boot:run

Notes:
- Hystrix is no longer supported on Spring Boot 3; Resilience4j is used instead as the circuit breaker implementation.
- The gateway uses `lb://SERVICE-NAME` URIs so calls are load-balanced via the discovery client.
