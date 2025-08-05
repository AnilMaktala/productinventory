package com.inventory.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.api.dto.CategoryDTO;
import com.inventory.api.dto.ProductDTO;
import com.inventory.api.service.CategoryService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDTO categoryDTO;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Electronics");
        categoryDTO.setDescription("Electronic devices");
        categoryDTO.setProductCount(1);

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
    void createCategory_Success() throws Exception {
        when(categoryService.createCategory(org.mockito.ArgumentMatchers.any(CategoryDTO.class)))
                .thenReturn(categoryDTO);

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Electronics")));

        verify(categoryService).createCategory(org.mockito.ArgumentMatchers.any(CategoryDTO.class));
    }

    @Test
    void getAllCategories_Success() throws Exception {
        List<CategoryDTO> categories = Arrays.asList(categoryDTO);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Electronics")));

        verify(categoryService).getAllCategories();
    }

    @Test
    void getCategoryById_Success() throws Exception {
        when(categoryService.getCategoryById(anyLong())).thenReturn(categoryDTO);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Electronics")));

        verify(categoryService).getCategoryById(1L);
    }

    @Test
    void updateCategory_Success() throws Exception {
        when(categoryService.updateCategory(anyLong(), org.mockito.ArgumentMatchers.any(CategoryDTO.class)))
                .thenReturn(categoryDTO);

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Electronics")));

        verify(categoryService).updateCategory(eq(1L), org.mockito.ArgumentMatchers.any(CategoryDTO.class));
    }

    @Test
    void deleteCategory_Success() throws Exception {
        doNothing().when(categoryService).deleteCategory(anyLong());

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(1L);
    }

    @Test
    void getProductsByCategory_Success() throws Exception {
        List<ProductDTO> products = Arrays.asList(productDTO);
        Page<ProductDTO> productPage = new PageImpl<>(products);
        when(categoryService.getProductsByCategory(anyLong(), org.mockito.ArgumentMatchers.any(Pageable.class)))
                .thenReturn(productPage);

        mockMvc.perform(get("/api/categories/1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Test Product")));

        verify(categoryService).getProductsByCategory(eq(1L), org.mockito.ArgumentMatchers.any(Pageable.class));
    }
}