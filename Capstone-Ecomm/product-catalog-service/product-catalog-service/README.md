# Product Catalog Service

This service manages product listings and broadcasts product changes to downstream services via Kafka.

Prerequisites
- Java 17 JDK installed and on PATH
- Maven (wrapper included) or use provided `mvnw`
- MongoDB running locally or provide `spring.data.mongodb.uri` in `application.properties`
- Kafka running locally or provide `spring.kafka.bootstrap-servers` in `application.properties`

Build
```powershell
cd C:\Projects\poc\Capstone-Ecomm\product-catalog-service\product-catalog-service
.\mvnw -DskipTests package
```

Run
```powershell
# using maven
.\mvnw spring-boot:run
# or run jar
java -jar .\target\product-catalog-service-0.0.1-SNAPSHOT.jar
```

Tests
```powershell
.\mvnw test
```

API Endpoints
- POST /products — create product
- POST /products/batch — create multiple
- GET /products — list all
- GET /products/{productId} — get single
- PUT /products/{productId} — update
- DELETE /products/{productId} — delete
- GET /products/inventory/{productId} — get inventory quantity

Notes
- Default `application.properties` uses `mongodb://localhost:27017/product_catalog_db` and `localhost:9092` for Kafka. Change these if needed.
- Topics used: `product-created`, `product-updated`, `product-deleted`.

If you run into build errors, paste the last 200 lines of `build.log` and I'll fix them.
