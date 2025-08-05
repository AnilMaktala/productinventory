package com.inventory.api.repository;

import com.inventory.api.model.Category;
import com.inventory.api.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

        Optional<Product> findBySku(String sku);

        Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

        Page<Product> findByCategory(Category category, Pageable pageable);

        Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

        Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

        Page<Product> findByInventoryQuantityGreaterThan(int quantity, Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.inventoryQuantity > 0")
        Page<Product> findInStockProducts(Pageable pageable);

        @Query("SELECT p FROM Product p WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
                        "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
                        "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
                        "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
                        "AND (:inStock IS NULL OR (:inStock = true AND p.inventoryQuantity > 0) OR (:inStock = false))")
        Page<Product> searchProducts(
                        @Param("name") String name,
                        @Param("categoryId") Long categoryId,
                        @Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice,
                        @Param("inStock") Boolean inStock,
                        Pageable pageable);

        List<Product> findByLowStockIsTrue();

        Page<Product> findBySupplierId(Long supplierId, Pageable pageable);

        long countBySupplierId(Long supplierId);
}