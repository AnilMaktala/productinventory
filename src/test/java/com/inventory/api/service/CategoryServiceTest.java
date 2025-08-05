package com.inventory.api.service;

import com.inventory.api.dto.CategoryDTO;
import com.inventory.api.dto.ProductDTO;
import com.inventory.api.exception.ResourceNotFoundException;
import com.inventory.api.mapper.CategoryMapper;
import com.inventory.api.mapper.ProductMapper;
import com.inventory.api.model.Category;
import com.inventory.api.model.Product;
import com.inventory.api.repository.CategoryRepository;
import com.inventory.api.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDTO categoryDTO;
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setDescription("Electronic devices");
        category.setProducts(new ArrayList<>());

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Electronics");
        categoryDTO.setDescription("Electronic devices");
        categoryDTO.setProductCount(0);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("99.99"));
        product.setInventoryQuantity(10);
        product.setSku("TEST-SKU-123");
        product.setCategory(category);

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Test Product");
        productDTO.setDescription("Test Description");
        productDTO.setPrice(new BigDecimal("99.99"));
        productDTO.setInventoryQuantity(10);
        productDTO.setSku("TEST-SKU-123");
        productDTO.setCategoryId(1L);
        productDTO.setCategoryName("Electronics");
    }

    @Test
    void createCategory_Success() {
        when(categoryMapper.toEntity(any(CategoryDTO.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.createCategory(categoryDTO);

        assertNotNull(result);
        assertEquals(categoryDTO.getName(), result.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void getAllCategories_Success() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDTO);

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getCategoryById_Success() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(categoryDTO.getId(), result.getId());
        assertEquals(categoryDTO.getName(), result.getName());
    }

    @Test
    void getCategoryById_NotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void updateCategory_Success() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDTO);

        CategoryDTO updatedDTO = new CategoryDTO();
        updatedDTO.setName("Updated Category");
        updatedDTO.setDescription("Updated Description");

        CategoryDTO result = categoryService.updateCategory(1L, updatedDTO);

        assertNotNull(result);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void updateCategory_NotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(1L, new CategoryDTO()));
    }

    @Test
    void deleteCategory_Success() {
        Category emptyCategory = new Category();
        emptyCategory.setId(1L);
        emptyCategory.setName("Electronics");
        emptyCategory.setProducts(new ArrayList<>());

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(emptyCategory));
        doNothing().when(categoryRepository).delete(any(Category.class));

        categoryService.deleteCategory(1L);

        verify(categoryRepository).delete(any(Category.class));
    }

    @Test
    void deleteCategory_WithProducts() {
        category.setProducts(Arrays.asList(product));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        assertThrows(IllegalStateException.class, () -> categoryService.deleteCategory(1L));
    }

    @Test
    void deleteCategory_NotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }

    @Test
    void getProductsByCategory_Success() {
        category.setProducts(Arrays.asList(product));
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        Page<ProductDTO> result = categoryService.getProductsByCategory(1L, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getProductsByCategory_CategoryNotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.getProductsByCategory(1L, PageRequest.of(0, 10)));
    }
}