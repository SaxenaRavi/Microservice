# Multi-stage Dockerfile for customer-service
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Copy maven files first for better caching
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src

# Build the application
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app/app.jar"]
