package com.inventory.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI productInventoryOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Product Inventory API")
                                                .description("RESTful API for managing product inventory. This API provides endpoints for product management, inventory tracking, category organization, and search functionality.")
                                                .version("v1.0.0")
                                                .contact(new Contact()
                                                                .name("API Support")
                                                                .email("support@inventory.com"))
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                                .servers(List.of(
                                                new Server()
                                                                .url("http://localhost:8080")
                                                                .description("Development Server")))
                                .components(new Components()
                                                .responses(createApiResponses())
                                                .examples(createExamples()));
        }

        private Map<String, ApiResponse> createApiResponses() {
                Map<String, ApiResponse> responses = new HashMap<>();

                // 400 Bad Request
                responses.put("BadRequest", new ApiResponse()
                                .description("Bad Request - The request was invalid or cannot be served")
                                .content(new Content()
                                                .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                                                new MediaType().example(
                                                                                "{\"timestamp\":\"2025-06-23T10:15:30.123Z\",\"status\":400,\"error\":\"Bad Request\",\"message\":\"Validation failed\",\"details\":[\"Product name cannot be empty\",\"Price must be greater than zero\"],\"path\":\"/api/products\"}"))));

                // 404 Not Found
                responses.put("NotFound", new ApiResponse()
                                .description("Not Found - The requested resource could not be found")
                                .content(new Content()
                                                .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                                                new MediaType().example(
                                                                                "{\"timestamp\":\"2025-06-23T10:15:30.123Z\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Product with ID 1 not found\",\"path\":\"/api/products/1\"}"))));

                // 500 Internal Server Error
                responses.put("InternalServerError", new ApiResponse()
                                .description("Internal Server Error - An unexpected condition was encountered")
                                .content(new Content()
                                                .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                                                new MediaType().example(
                                                                                "{\"timestamp\":\"2025-06-23T10:15:30.123Z\",\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"An unexpected error occurred\",\"path\":\"/api/products\"}"))));

                return responses;
        }

        private Map<String, Example> createExamples() {
                Map<String, Example> examples = new HashMap<>();

                // Product examples
                examples.put("product", new Example()
                                .value("{\"name\":\"Smartphone\",\"description\":\"Latest smartphone model\",\"price\":999.99,\"inventoryQuantity\":50,\"sku\":\"PHONE-123\",\"categoryId\":1,\"lowStockThreshold\":10}"));

                examples.put("productResponse", new Example()
                                .value("{\"id\":1,\"name\":\"Smartphone\",\"description\":\"Latest smartphone model\",\"price\":999.99,\"inventoryQuantity\":50,\"sku\":\"PHONE-123\",\"categoryId\":1,\"categoryName\":\"Electronics\",\"lowStock\":false,\"lowStockThreshold\":10}"));

                // Category examples
                examples.put("category", new Example()
                                .value("{\"name\":\"Electronics\",\"description\":\"Electronic devices\"}"));

                examples.put("categoryResponse", new Example()
                                .value("{\"id\":1,\"name\":\"Electronics\",\"description\":\"Electronic devices\",\"productCount\":5}"));

                // Inventory update example
                examples.put("inventoryUpdate", new Example()
                                .value("{\"quantity\":10}"));

                return examples;
        }
}