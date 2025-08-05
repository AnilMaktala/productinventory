# Product Inventory API

A Spring Boot RESTful API for managing product inventory. This API provides endpoints for product management, inventory tracking, category organization, and search functionality.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
  - [Product Endpoints](#product-endpoints)
  - [Category Endpoints](#category-endpoints)
- [Example API Calls](#example-api-calls)
- [Error Handling](#error-handling)

## Features

- CRUD operations for products and categories
- Inventory management (update, increase, decrease)
- Low stock threshold monitoring
- Product categorization
- Advanced search and filtering
- Comprehensive error handling
- API documentation with Swagger/OpenAPI

## Technologies

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- H2 Database (for development)
- Swagger/OpenAPI for documentation
- JUnit 5 and Mockito for testing

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/product-inventory-api.git
   cd product-inventory-api
   ```

2. Build the application:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. The API will be available at `http://localhost:8080`
5. The Swagger UI will be available at `http://localhost:8080/swagger-ui.html`
6. The H2 Console will be available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:inventorydb`, Username: `sa`, Password: `password`)

## API Documentation

The API is documented using Swagger/OpenAPI. You can access the documentation at:

```
http://localhost:8080/swagger-ui.html
```

This provides an interactive interface to explore and test all available endpoints.

## API Endpoints

### Product Endpoints

| Method | URL                                   | Description                           |
| ------ | ------------------------------------- | ------------------------------------- |
| POST   | /api/products                         | Create a new product                  |
| GET    | /api/products                         | Get all products (paginated)          |
| GET    | /api/products/{id}                    | Get a product by ID                   |
| PUT    | /api/products/{id}                    | Update a product                      |
| DELETE | /api/products/{id}                    | Delete a product                      |
| GET    | /api/products/{id}/inventory          | Get product inventory level           |
| PUT    | /api/products/{id}/inventory          | Update product inventory level        |
| POST   | /api/products/{id}/inventory/increase | Increase product inventory            |
| POST   | /api/products/{id}/inventory/decrease | Decrease product inventory            |
| PUT    | /api/products/{id}/category           | Assign product to a category          |
| GET    | /api/products/search                  | Search products with various criteria |

### Category Endpoints

| Method | URL                           | Description                                   |
| ------ | ----------------------------- | --------------------------------------------- |
| POST   | /api/categories               | Create a new category                         |
| GET    | /api/categories               | Get all categories                            |
| GET    | /api/categories/{id}          | Get a category by ID                          |
| PUT    | /api/categories/{id}          | Update a category                             |
| DELETE | /api/categories/{id}          | Delete a category (if no associated products) |
| GET    | /api/categories/{id}/products | Get all products in a category                |

## Example API Calls

### Create a Product

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone",
    "description": "Latest smartphone model",
    "price": 999.99,
    "inventoryQuantity": 50,
    "sku": "PHONE-123",
    "categoryId": 1,
    "lowStockThreshold": 10
  }'
```

### Create a Category

```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices"
  }'
```

### Update Product Inventory

```bash
curl -X PUT http://localhost:8080/api/products/1/inventory \
  -H "Content-Type: application/json" \
  -d '{
    "quantity": 100
  }'
```

### Search Products

```bash
curl -X GET "http://localhost:8080/api/products/search?name=phone&minPrice=500&maxPrice=1500&inStock=true"
```

## Error Handling

The API uses standard HTTP status codes to indicate the success or failure of requests:

- 200 OK: The request was successful
- 201 Created: A new resource was successfully created
- 204 No Content: The request was successful but there is no content to return
- 400 Bad Request: The request was invalid or cannot be served
- 404 Not Found: The requested resource could not be found
- 500 Internal Server Error: An unexpected condition was encountered

Error responses follow a standard format:

```json
{
  "timestamp": "2025-06-23T10:15:30.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": [
    "Product name cannot be empty",
    "Price must be greater than zero"
  ],
  "path": "/api/products"
}
```