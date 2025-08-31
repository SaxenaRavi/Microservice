# Multi-stage build: build with Maven and run with a slim JRE
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Copy project files
COPY pom.xml mvnw ./
# copy .mvn if present
COPY .mvn .mvn
COPY src src

# Build the application (skip tests for faster builds)
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar from the builder stage
ARG JAR_FILE=target/order-processing-service-0.0.1-SNAPSHOT.jar
COPY --from=builder /workspace/${JAR_FILE} app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app/app.jar"]
