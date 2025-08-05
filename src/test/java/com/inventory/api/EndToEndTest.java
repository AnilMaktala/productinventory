package com.inventory.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.api.dto.CategoryDTO;
import com.inventory.api.dto.InventoryUpdateDTO;
import com.inventory.api.dto.ProductDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long categoryId;
    private static Long productId;

    @Test
    @Order(1)
    void testCreateCategory() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Electronics");
        categoryDTO.setDescription("Electronic devices");

        MvcResult result = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Electronics")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        CategoryDTO createdCategory = objectMapper.readValue(responseContent, CategoryDTO.class);
        categoryId = createdCategory.getId();
    }

    @Test
    @Order(2)
    void testCreateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Smartphone");
        productDTO.setDescription("Latest smartphone model");
        productDTO.setPrice(new BigDecimal("999.99"));
        productDTO.setInventoryQuantity(50);
        productDTO.setSku("PHONE-123");
        productDTO.setCategoryId(categoryId);
        productDTO.setLowStockThreshold(10);

        MvcResult result = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Smartphone")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.categoryId", is(categoryId.intValue())))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        ProductDTO createdProduct = objectMapper.readValue(responseContent, ProductDTO.class);
        productId = createdProduct.getId();
    }

    @Test
    @Order(3)
    void testGetProductById() throws Exception {
        mockMvc.perform(get("/api/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId.intValue())))
                .andExpect(jsonPath("$.name", is("Smartphone")));
    }

    @Test
    @Order(4)
    void testGetCategoryById() throws Exception {
        mockMvc.perform(get("/api/categories/" + categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryId.intValue())))
                .andExpect(jsonPath("$.name", is("Electronics")));
    }

    @Test
    @Order(5)
    void testGetProductsByCategory() throws Exception {
        mockMvc.perform(get("/api/categories/" + categoryId + "/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Smartphone")));
    }

    @Test
    @Order(6)
    void testUpdateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Smartphone");
        productDTO.setDescription("Updated smartphone model");
        productDTO.setPrice(new BigDecimal("1099.99"));
        productDTO.setInventoryQuantity(50);
        productDTO.setSku("PHONE-123");
        productDTO.setCategoryId(categoryId);
        productDTO.setLowStockThreshold(10);

        mockMvc.perform(put("/api/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Smartphone")))
                .andExpect(jsonPath("$.price", is(1099.99)));
    }

    @Test
    @Order(7)
    void testDecreaseInventory() throws Exception {
        InventoryUpdateDTO inventoryUpdateDTO = new InventoryUpdateDTO();
        inventoryUpdateDTO.setQuantity(10);

        mockMvc.perform(post("/api/products/" + productId + "/inventory/decrease")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventoryUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inventoryQuantity", is(40)));
    }

    @Test
    @Order(8)
    void testIncreaseInventory() throws Exception {
        InventoryUpdateDTO inventoryUpdateDTO = new InventoryUpdateDTO();
        inventoryUpdateDTO.setQuantity(20);

        mockMvc.perform(post("/api/products/" + productId + "/inventory/increase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventoryUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inventoryQuantity", is(60)));
    }

    @Test
    @Order(9)
    void testSearchProducts() throws Exception {
        mockMvc.perform(get("/api/products/search")
                .param("name", "Updated")
                .param("minPrice", "1000.00")
                .param("maxPrice", "1200.00")
                .param("inStock", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Updated Smartphone")));
    }

    @Test
    @Order(10)
    void testUpdateCategory() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Updated Electronics");
        categoryDTO.setDescription("Updated electronic devices");

        mockMvc.perform(put("/api/categories/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Electronics")));
    }

    @Test
    @Order(11)
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.content[0].name", is("Updated Smartphone")));
    }

    @Test
    @Order(12)
    void testGetAllCategories() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", is("Updated Electronics")));
    }

    @Test
    @Order(13)
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/" + productId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/" + productId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(14)
    void testDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/categories/" + categoryId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/categories/" + categoryId))
                .andExpect(status().isNotFound());
    }
}