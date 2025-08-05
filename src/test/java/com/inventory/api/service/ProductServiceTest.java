package com.inventory.api.service;

import com.inventory.api.dto.ProductDTO;
import com.inventory.api.exception.InsufficientInventoryException;
import com.inventory.api.exception.ResourceNotFoundException;
import com.inventory.api.mapper.ProductMapper;
import com.inventory.api.model.Category;
import com.inventory.api.model.Product;
import com.inventory.api.repository.CategoryRepository;
import com.inventory.api.repository.ProductRepository;
import com.inventory.api.service.impl.ProductServiceImpl;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setDescription("Electronic devices");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("99.99"));
        product.setInventoryQuantity(10);
        product.setSku("TEST-SKU-123");
        product.setCategory(category);
        product.setLowStockThreshold(5);
        product.setLowStock(false);

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Test Product");
        productDTO.setDescription("Test Description");
        productDTO.setPrice(new BigDecimal("99.99"));
        productDTO.setInventoryQuantity(10);
        productDTO.setSku("TEST-SKU-123");
        productDTO.setCategoryId(1L);
        productDTO.setCategoryName("Electronics");
        productDTO.setLowStockThreshold(5);
        productDTO.setLowStock(false);
    }

    @Test
    void createProduct_Success() {
        when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        ProductDTO result = productService.createProduct(productDTO);

        assertNotNull(result);
        assertEquals(productDTO.getName(), result.getName());
        assertEquals(productDTO.getPrice(), result.getPrice());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(productDTO.getId(), result.getId());
        assertEquals(productDTO.getName(), result.getName());
    }

    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void getAllProducts_Success() {
        List<Product> products = Arrays.asList(product);
        Page<Product> productPage = new PageImpl<>(products);
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        Page<ProductDTO> result = productService.getAllProducts(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void updateProduct_Success() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setName("Updated Product");
        updatedDTO.setDescription("Updated Description");
        updatedDTO.setPrice(new BigDecimal("129.99"));
        updatedDTO.setCategoryId(1L);

        ProductDTO result = productService.updateProduct(1L, updatedDTO);

        assertNotNull(result);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_NotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1L, new ProductDTO()));
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(any(Product.class));

        productService.deleteProduct(1L);

        verify(productRepository).delete(any(Product.class));
    }

    @Test
    void deleteProduct_NotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    void getProductInventory_Success() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Integer result = productService.getInventoryLevel(1L);

        assertEquals(product.getInventoryQuantity(), result);
    }

    @Test
    void updateInventory_Success() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.updateInventoryLevel(1L, 20);

        assertNotNull(result);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void increaseInventory_Success() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.increaseInventory(1L, 5);

        assertNotNull(result);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void decreaseInventory_Success() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.decreaseInventory(1L, 5);

        assertNotNull(result);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void decreaseInventory_InsufficientInventory() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        assertThrows(InsufficientInventoryException.class, () -> productService.decreaseInventory(1L, 15));
    }

    @Test
    void assignProductToCategory_Success() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.assignCategory(1L, 1L);

        assertNotNull(result);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void assignProductToCategory_ProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.assignCategory(1L, 1L));
    }

    @Test
    void assignProductToCategory_CategoryNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.assignCategory(1L, 1L));
    }

    @Test
    void searchProducts_ByName() {
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product));
        when(productRepository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        Page<ProductDTO> result = productService.searchProducts("Test", null, null, null, null, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void searchProducts_ByCategory() {
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product));
        when(productRepository.findByCategoryId(anyLong(), any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        Page<ProductDTO> result = productService.searchProducts(null, 1L, null, null, null, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void searchProducts_ByPriceRange() {
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product));
        when(productRepository.findByPriceBetween(any(BigDecimal.class), any(BigDecimal.class), any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        Page<ProductDTO> result = productService.searchProducts(
                null, null, new BigDecimal("50.00"), new BigDecimal("150.00"), null, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void searchProducts_InStock() {
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product));
        when(productRepository.findByInventoryQuantityGreaterThan(anyInt(), any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        Page<ProductDTO> result = productService.searchProducts(null, null, null, null, true, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}