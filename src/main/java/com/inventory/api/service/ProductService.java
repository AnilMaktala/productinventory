package com.inventory.api.service;

import com.inventory.api.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO getProductById(Long id);

    ProductDTO getProductBySku(String sku);

    Page<ProductDTO> getAllProducts(Pageable pageable);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long id);

    Integer getInventoryLevel(Long id);

    ProductDTO updateInventoryLevel(Long id, Integer quantity);

    ProductDTO increaseInventory(Long id, Integer quantity);

    ProductDTO decreaseInventory(Long id, Integer quantity);

    Page<ProductDTO> searchProducts(
            String name,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean inStock,
            Pageable pageable);

    List<ProductDTO> getLowStockProducts();

    ProductDTO assignCategory(Long productId, Long categoryId);

    ProductDTO assignSupplier(Long productId, Long supplierId);

    Page<ProductDTO> getProductsBySupplier(Long supplierId, Pageable pageable);
}