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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        // Create and persist categories
        category1 = new Category();
        category1.setName("Electronics");
        category1.setDescription("Electronic devices");
        entityManager.persist(category1);

        category2 = new Category();
        category2.setName("Books");
        category2.setDescription("Books and publications");
        entityManager.persist(category2);

        // Create and persist products
        Product product1 = new Product();
        product1.setName("Smartphone");
        product1.setDescription("Latest smartphone model");
        product1.setPrice(new BigDecimal("999.99"));
        product1.setInventoryQuantity(50);
        product1.setSku("PHONE-123");
        product1.setCategory(category1);
        product1.setLowStockThreshold(10);
        entityManager.persist(product1);

        Product product2 = new Product();
        product2.setName("Novel");
        product2.setDescription("Fiction novel");
        product2.setPrice(new BigDecimal("19.99"));
        product2.setInventoryQuantity(100);
        product2.setSku("BOOK-456");
        product2.setCategory(category2);
        product2.setLowStockThreshold(20);
        entityManager.persist(product2);

        entityManager.flush();
    }

    @Test
    void findByName_Success() {
        Optional<Category> foundCategory = categoryRepository.findByName("Electronics");

        assertTrue(foundCategory.isPresent());
        assertEquals("Electronics", foundCategory.get().getName());
    }

    @Test
    void findAll_Success() {
        List<Category> categories = categoryRepository.findAll();

        assertNotNull(categories);
        assertEquals(2, categories.size());
    }

    @Test
    void save_Success() {
        Category newCategory = new Category();
        newCategory.setName("Clothing");
        newCategory.setDescription("Apparel and accessories");

        Category savedCategory = categoryRepository.save(newCategory);

        assertNotNull(savedCategory);
        assertNotNull(savedCategory.getId());
        assertEquals("Clothing", savedCategory.getName());
    }

    @Test
    void delete_Success() {
        categoryRepository.delete(category2);

        assertFalse(categoryRepository.findById(category2.getId()).isPresent());
    }

    @Test
    void findById_Success() {
        Optional<Category> foundCategory = categoryRepository.findById(category1.getId());

        assertTrue(foundCategory.isPresent());
        assertEquals("Electronics", foundCategory.get().getName());
    }
}