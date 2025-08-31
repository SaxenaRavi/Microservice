# Multi-stage Dockerfile for inventory-service
FROM eclipse-temurin:17-jdk as build
WORKDIR /workspace
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml pom.xml
COPY src src
RUN ./mvnw -DskipTests package -q

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]
