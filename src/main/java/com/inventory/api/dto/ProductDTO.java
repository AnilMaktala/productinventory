package com.inventory.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @Min(value = 0, message = "Inventory quantity cannot be negative")
    private Integer inventoryQuantity;

    private String sku;

    private Long categoryId;

    private String categoryName;

    private Long supplierId;

    private String supplierName;

    private boolean lowStock;

    @Min(value = 1, message = "Low stock threshold must be at least 1")
    private Integer lowStockThreshold;
}