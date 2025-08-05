package com.inventory.api.repository;

import com.inventory.api.model.Category;
import com.inventory.api.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        // Create and persist a category
        category = new Category();
        category.setName("Electronics");
        category.setDescription("Electronic devices");
        entityManager.persist(category);
        entityManager.flush();

        // Create and persist products
        product1 = new Product();
        product1.setName("Smartphone");
        product1.setDescription("Latest smartphone model");
        product1.setPrice(new BigDecimal("999.99"));
        product1.setInventoryQuantity(50);
        product1.setSku("PHONE-123");
        product1.setCategory(category);
        product1.setLowStockThreshold(10);
        entityManager.persist(product1);

        product2 = new Product();
        product2.setName("Laptop");
        product2.setDescription("High-performance laptop");
        product2.setPrice(new BigDecimal("1499.99"));
        product2.setInventoryQuantity(20);
        product2.setSku("LAPTOP-456");
        product2.setCategory(category);
        product2.setLowStockThreshold(5);
        entityManager.persist(product2);

        entityManager.flush();
    }

    @Test
    void findByNameContainingIgnoreCase_Success() {
        Page<Product> products = productRepository.findByNameContainingIgnoreCase("phone",
                PageRequest.of(0, 10));

        assertNotNull(products);
        assertEquals(1, products.getTotalElements());
        assertEquals("Smartphone", products.getContent().get(0).getName());
    }

    @Test
    void findByCategoryId_Success() {
        Page<Product> products = productRepository.findByCategoryId(category.getId(),
                PageRequest.of(0, 10));

        assertNotNull(products);
        assertEquals(2, products.getTotalElements());
    }

    @Test
    void findByPriceBetween_Success() {
        Page<Product> products = productRepository.findByPriceBetween(
                new BigDecimal("900.00"), new BigDecimal("1000.00"), PageRequest.of(0, 10));

        assertNotNull(products);
        assertEquals(1, products.getTotalElements());
        assertEquals("Smartphone", products.getContent().get(0).getName());
    }

    @Test
    void findByInventoryQuantityGreaterThan_Success() {
        Page<Product> products = productRepository.findByInventoryQuantityGreaterThan(30,
                PageRequest.of(0, 10));

        assertNotNull(products);
        assertEquals(1, products.getTotalElements());
        assertEquals("Smartphone", products.getContent().get(0).getName());
    }

    @Test
    void save_Success() {
        Product newProduct = new Product();
        newProduct.setName("Tablet");
        newProduct.setDescription("New tablet model");
        newProduct.setPrice(new BigDecimal("499.99"));
        newProduct.setInventoryQuantity(30);
        newProduct.setSku("TABLET-789");
        newProduct.setCategory(category);
        newProduct.setLowStockThreshold(5);

        Product savedProduct = productRepository.save(newProduct);

        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getId());
        assertEquals("Tablet", savedProduct.getName());
    }

    @Test
    void delete_Success() {
        productRepository.delete(product1);

        assertFalse(productRepository.findById(product1.getId()).isPresent());
    }
}