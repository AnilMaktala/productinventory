package com.inventory.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.api.dto.InventoryUpdateDTO;
import com.inventory.api.dto.ProductDTO;
import com.inventory.api.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;
    private InventoryUpdateDTO inventoryUpdateDTO;

    @BeforeEach
    void setUp() {
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

        inventoryUpdateDTO = new InventoryUpdateDTO();
        inventoryUpdateDTO.setQuantity(5);
    }

    @Test
    void createProduct_Success() throws Exception {
        when(productService.createProduct(org.mockito.ArgumentMatchers.any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.price", is(99.99)));

        verify(productService).createProduct(org.mockito.ArgumentMatchers.any(ProductDTO.class));
    }

    @Test
    void getProductById_Success() throws Exception {
        when(productService.getProductById(anyLong())).thenReturn(productDTO);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Product")));

        verify(productService).getProductById(1L);
    }

    @Test
    void getAllProducts_Success() throws Exception {
        List<ProductDTO> products = Arrays.asList(productDTO);
        Page<ProductDTO> productPage = new PageImpl<>(products);

        when(productService.getAllProducts(org.mockito.ArgumentMatchers.any(Pageable.class))).thenReturn(productPage);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Test Product")));

        verify(productService).getAllProducts(org.mockito.ArgumentMatchers.any(Pageable.class));
    }

    @Test
    void updateProduct_Success() throws Exception {
        when(productService.updateProduct(anyLong(), org.mockito.ArgumentMatchers.any(ProductDTO.class)))
                .thenReturn(productDTO);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Product")));

        verify(productService).updateProduct(eq(1L), org.mockito.ArgumentMatchers.any(ProductDTO.class));
    }

    @Test
    void deleteProduct_Success() throws Exception {
        doNothing().when(productService).deleteProduct(anyLong());

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

    @Test
    void getProductInventory_Success() throws Exception {
        when(productService.getInventoryLevel(anyLong())).thenReturn(10);

        mockMvc.perform(get("/api/products/1/inventory"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(productService).getInventoryLevel(1L);
    }

    @Test
    void updateProductInventory_Success() throws Exception {
        when(productService.updateInventoryLevel(anyLong(), anyInt())).thenReturn(productDTO);

        mockMvc.perform(put("/api/products/1/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventoryUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Product")));

        verify(productService).updateInventoryLevel(eq(1L), eq(5));
    }

    @Test
    void increaseProductInventory_Success() throws Exception {
        when(productService.increaseInventory(anyLong(), anyInt())).thenReturn(productDTO);

        mockMvc.perform(post("/api/products/1/inventory/increase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventoryUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Product")));

        verify(productService).increaseInventory(eq(1L), eq(5));
    }

    @Test
    void decreaseProductInventory_Success() throws Exception {
        when(productService.decreaseInventory(anyLong(), anyInt())).thenReturn(productDTO);

        mockMvc.perform(post("/api/products/1/inventory/decrease")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventoryUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Product")));

        verify(productService).decreaseInventory(eq(1L), eq(5));
    }

    @Test
    void assignProductToCategory_Success() throws Exception {
        when(productService.assignCategory(anyLong(), anyLong())).thenReturn(productDTO);

        mockMvc.perform(put("/api/products/1/category")
                .param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Product")));

        verify(productService).assignCategory(eq(1L), eq(1L));
    }

    @Test
    void searchProducts_Success() throws Exception {
        List<ProductDTO> products = Arrays.asList(productDTO);
        Page<ProductDTO> productPage = new PageImpl<>(products);
        when(productService.searchProducts(anyString(), org.mockito.ArgumentMatchers.any(Long.class),
                org.mockito.ArgumentMatchers.any(BigDecimal.class),
                org.mockito.ArgumentMatchers.any(BigDecimal.class),
                org.mockito.ArgumentMatchers.any(Boolean.class),
                org.mockito.ArgumentMatchers.any(Pageable.class))).thenReturn(productPage);

        mockMvc.perform(get("/api/products/search")
                .param("name", "Test")
                .param("category", "1")
                .param("minPrice", "50.00")
                .param("maxPrice", "150.00")
                .param("inStock", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Test Product")));

        verify(productService).searchProducts(eq("Test"), eq(1L),
                eq(new BigDecimal("50.00")), eq(new BigDecimal("150.00")), eq(true),
                org.mockito.ArgumentMatchers.any(Pageable.class));
    }
}