---
inclusion: always
---

# Product Domain Guidelines

## Business Rules
- **SKU Validation**: Products must have unique, non-blank SKUs
- **Price Constraints**: All prices must be positive values using `BigDecimal`
- **Inventory Management**: 
  - Quantities cannot go negative
  - Use atomic operations for inventory updates
  - Throw `InsufficientInventoryException` for invalid decreases
- **Category Dependencies**: Categories cannot be deleted if associated products exist
- **Low Stock Monitoring**: Implement threshold-based alerts for inventory levels

## Entity Relationships
- **Product** ↔ **Category**: Many-to-One relationship
- Products require valid category associations
- Cascade operations appropriately (avoid orphaned data)

## API Conventions
- **Endpoints**: Use `/api/products` and `/api/categories` prefixes
- **HTTP Methods**: Follow REST conventions (GET, POST, PUT, DELETE)
- **Response Format**: Consistent JSON structure with timestamps
- **Pagination**: Default 20 items, max 100 per page
- **Error Handling**: Use `GlobalExceptionHandler` for standardized responses

## Data Validation
- Use Bean Validation annotations (`@NotBlank`, `@Positive`, `@Min`)
- Validate DTOs at controller layer with `@Valid`
- Implement custom validators for business logic constraints
- Return detailed validation error messages

## Code Style
- **DTOs**: Use for external API contracts, separate from entities
- **Mappers**: Implement entity-DTO conversion logic
- **Services**: Keep business logic in service layer, not controllers
- **Repositories**: Use Spring Data JPA query methods
- **Exceptions**: Create specific exception types for business errors