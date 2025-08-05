package com.inventory.api.mapper;

import com.inventory.api.dto.ProductDTO;
import com.inventory.api.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDto(Product product) {
        if (product == null) {
            return null;
        }

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .inventoryQuantity(product.getInventoryQuantity())
                .sku(product.getSku())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .lowStock(product.isLowStock())
                .lowStockThreshold(product.getLowStockThreshold())
                .build();
    }

    public Product toEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }

        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .inventoryQuantity(productDTO.getInventoryQuantity())
                .sku(productDTO.getSku())
                .lowStock(productDTO.isLowStock())
                .lowStockThreshold(productDTO.getLowStockThreshold())
                .build();
    }
}