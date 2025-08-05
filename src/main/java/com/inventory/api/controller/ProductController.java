package com.inventory.api.controller;

import com.inventory.api.dto.InventoryUpdateDTO;
import com.inventory.api.dto.ProductDTO;
import com.inventory.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Controller", description = "API endpoints for product management")
public class ProductController {

        private final ProductService productService;

        @Autowired
        public ProductController(ProductService productService) {
                this.productService = productService;
        }

        @PostMapping
        @Operation(summary = "Create a new product", description = "Creates a new product in the inventory")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Product created successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid product data provided"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
                ProductDTO createdProduct = productService.createProduct(productDTO);
                return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get product by ID", description = "Returns a product by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product found"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        public ResponseEntity<ProductDTO> getProductById(
                        @Parameter(description = "Product ID", required = true) @PathVariable Long id) {
                ProductDTO product = productService.getProductById(id);
                return ResponseEntity.ok(product);
        }

        @GetMapping
        @Operation(summary = "Get all products", description = "Returns a paginated list of all products")
        @ApiResponse(responseCode = "200", description = "List of products retrieved successfully")
        public ResponseEntity<Page<ProductDTO>> getAllProducts(Pageable pageable) {
                Page<ProductDTO> products = productService.getAllProducts(pageable);
                return ResponseEntity.ok(products);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update a product", description = "Updates an existing product by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid product data provided"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        public ResponseEntity<ProductDTO> updateProduct(
                        @Parameter(description = "Product ID", required = true) @PathVariable Long id,
                        @Valid @RequestBody ProductDTO productDTO) {
                ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
                return ResponseEntity.ok(updatedProduct);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete a product", description = "Deletes a product by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        public ResponseEntity<Void> deleteProduct(
                        @Parameter(description = "Product ID", required = true) @PathVariable Long id) {
                productService.deleteProduct(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}/inventory")
        @Operation(summary = "Get product inventory", description = "Returns the current inventory level for a product")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Inventory retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        public ResponseEntity<Integer> getProductInventory(
                        @Parameter(description = "Product ID", required = true) @PathVariable Long id) {
                Integer inventoryLevel = productService.getInventoryLevel(id);
                return ResponseEntity.ok(inventoryLevel);
        }

        @PutMapping("/{id}/inventory")
        @Operation(summary = "Update product inventory", description = "Updates the inventory level for a product")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Inventory updated successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid inventory data provided"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        public ResponseEntity<ProductDTO> updateProductInventory(
                        @Parameter(description = "Product ID", required = true) @PathVariable Long id,
                        @Valid @RequestBody InventoryUpdateDTO inventoryUpdateDTO) {
                ProductDTO updatedProduct = productService.updateInventoryLevel(id, inventoryUpdateDTO.getQuantity());
                return ResponseEntity.ok(updatedProduct);
        }

        @PostMapping("/{id}/inventory/increase")
        @Operation(summary = "Increase product inventory", description = "Increases the inventory level for a product")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Inventory increased successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid inventory data provided"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        public ResponseEntity<ProductDTO> increaseProductInventory(
                        @Parameter(description = "Product ID", required = true) @PathVariable Long id,
                        @Valid @RequestBody InventoryUpdateDTO inventoryUpdateDTO) {
                ProductDTO updatedProduct = productService.increaseInventory(id, inventoryUpdateDTO.getQuantity());
                return ResponseEntity.ok(updatedProduct);
        }

        @PostMapping("/{id}/inventory/decrease")
        @Operation(summary = "Decrease product inventory", description = "Decreases the inventory level for a product")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Inventory decreased successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid inventory data or insufficient stock"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        public ResponseEntity<ProductDTO> decreaseProductInventory(
                        @Parameter(description = "Product ID", required = true) @PathVariable Long id,
                        @Valid @RequestBody InventoryUpdateDTO inventoryUpdateDTO) {
                ProductDTO updatedProduct = productService.decreaseInventory(id, inventoryUpdateDTO.getQuantity());
                return ResponseEntity.ok(updatedProduct);
        }

        @PutMapping("/{id}/category")
        @Operation(summary = "Assign product to category", description = "Assigns a product to a specific category")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product assigned to category successfully"),
                        @ApiResponse(responseCode = "404", description = "Product or category not found")
        })
        public ResponseEntity<ProductDTO> assignProductToCategory(
                        @Parameter(description = "Product ID", required = true) @PathVariable Long id,
                        @Parameter(description = "Category ID", required = true) @RequestParam Long categoryId) {
                ProductDTO updatedProduct = productService.assignCategory(id, categoryId);
                return ResponseEntity.ok(updatedProduct);
        }

        @GetMapping("/search")
        @Operation(summary = "Search products", description = "Searches for products based on various criteria")
        @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
        public ResponseEntity<Page<ProductDTO>> searchProducts(
                        @Parameter(description = "Product name pattern") @RequestParam(required = false) String name,
                        @Parameter(description = "Category ID") @RequestParam(required = false) Long categoryId,
                        @Parameter(description = "Minimum price") @RequestParam(required = false) BigDecimal minPrice,
                        @Parameter(description = "Maximum price") @RequestParam(required = false) BigDecimal maxPrice,
                        @Parameter(description = "Only in-stock products") @RequestParam(required = false) Boolean inStock,
                        Pageable pageable) {
                Page<ProductDTO> products = productService.searchProducts(name, categoryId, minPrice, maxPrice, inStock,
                                pageable);
                return ResponseEntity.ok(products);
        }

        @GetMapping("/low-stock")
        @Operation(summary = "Get low stock products", description = "Returns products with low stock")
        @ApiResponse(responseCode = "200", description = "Low stock products retrieved successfully")
        public ResponseEntity<List<ProductDTO>> getLowStockProducts() {
                List<ProductDTO> products = productService.getLowStockProducts();
                return ResponseEntity.ok(products);
        }
}