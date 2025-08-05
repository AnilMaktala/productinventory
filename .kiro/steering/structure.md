# Project Structure

## Package Organization
Base package: `com.inventory.api`

```
src/main/java/com/inventory/api/
├── ProductInventoryApiApplication.java    # Main Spring Boot application
├── config/                               # Configuration classes
│   ├── CacheConfig.java                 # Caching configuration
│   └── OpenApiConfig.java               # Swagger/OpenAPI setup
├── controller/                          # REST controllers
│   ├── ProductController.java           # Product endpoints
│   └── CategoryController.java          # Category endpoints
├── dto/                                 # Data Transfer Objects
│   ├── ProductDTO.java                  # Product request/response
│   ├── CategoryDTO.java                 # Category request/response
│   └── InventoryUpdateDTO.java          # Inventory update requests
├── exception/                           # Exception handling
│   ├── GlobalExceptionHandler.java      # Centralized error handling
│   ├── ResourceNotFoundException.java   # 404 errors
│   └── InsufficientInventoryException.java # Business logic errors
├── mapper/                              # Entity-DTO conversion
│   ├── ProductMapper.java               # Product mapping logic
│   └── CategoryMapper.java              # Category mapping logic
├── model/                               # JPA entities
│   ├── Product.java                     # Product entity
│   └── Category.java                    # Category entity
├── repository/                          # Data access layer
│   ├── ProductRepository.java           # Product data operations
│   └── CategoryRepository.java          # Category data operations
└── service/                             # Business logic layer
    ├── ProductService.java              # Product service interface
    ├── CategoryService.java             # Category service interface
    └── impl/                            # Service implementations
        ├── ProductServiceImpl.java      # Product business logic
        └── CategoryServiceImpl.java     # Category business logic
```

## Test Structure
```
src/test/java/com/inventory/api/
├── EndToEndTest.java                    # Full application tests
├── controller/                          # Controller integration tests
├── repository/                          # Repository layer tests
└── service/                             # Service layer unit tests
```

## Architecture Patterns

### Layered Architecture
- **Controller Layer**: REST endpoints, request/response handling
- **Service Layer**: Business logic and validation
- **Repository Layer**: Data access and persistence
- **Model Layer**: JPA entities representing database tables

### Key Conventions
- Use `@RestController` for REST endpoints with `/api` prefix
- Service interfaces with `Impl` implementations
- DTOs for external API contracts, entities for internal data
- Mappers handle entity-DTO conversions
- Global exception handler for consistent error responses
- Repository interfaces extend Spring Data JPA repositories

### Annotations Usage
- **Lombok**: `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- **JPA**: `@Entity`, `@Table`, `@Id`, `@GeneratedValue`
- **Validation**: `@Valid`, `@NotBlank`, `@Positive`, `@Min`
- **Spring**: `@RestController`, `@Service`, `@Repository`, `@Autowired`
- **OpenAPI**: `@Tag`, `@Operation`, `@ApiResponse` for documentation