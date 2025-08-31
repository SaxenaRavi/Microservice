# Stage 1: build jar using local maven wrapper (optional). This Dockerfile expects the jar to be built locally in target/.
FROM eclipse-temurin:17-jdk-jammy AS runtime

WORKDIR /app

# Copy the application jar (built by mvn package)
COPY target/payment-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
