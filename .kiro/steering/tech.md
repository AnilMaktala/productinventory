# Technology Stack

## Core Technologies
- **Java 17** - Language version
- **Spring Boot 3.1.0** - Application framework
- **Maven** - Build system and dependency management
- **H2 Database** - In-memory database for development

## Key Dependencies
- **Spring Boot Starters**:
  - `spring-boot-starter-web` - REST API support
  - `spring-boot-starter-data-jpa` - JPA/Hibernate ORM
  - `spring-boot-starter-validation` - Bean validation
  - `spring-boot-starter-test` - Testing framework
- **Lombok** - Reduces boilerplate code with annotations
- **SpringDoc OpenAPI** (v2.1.0) - API documentation and Swagger UI
- **Caffeine** - Caching implementation

## Database Configuration
- **Development**: H2 in-memory database
- **Connection**: `jdbc:h2:mem:inventorydb`
- **Console**: Available at `/h2-console` (username: `sa`, password: `password`)

## Common Commands

### Build & Run
```bash
# Clean and build
mvn clean install

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Package as JAR
mvn package
```

### Development URLs
- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **H2 Console**: http://localhost:8080/h2-console

## Configuration Features
- Caching enabled with Caffeine
- SQL logging enabled for debugging
- Pagination defaults: 20 items per page, max 100
- Comprehensive validation and error handling