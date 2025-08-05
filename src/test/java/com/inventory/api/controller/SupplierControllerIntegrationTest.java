package com.inventory.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.api.dto.SupplierDTO;
import com.inventory.api.model.Supplier;
import com.inventory.api.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class SupplierControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Supplier testSupplier;
    private SupplierDTO testSupplierDTO;

    @BeforeEach
    void setUp() {
        supplierRepository.deleteAll();

        testSupplier = Supplier.builder()
                .name("Test Supplier")
                .contactPerson("John Doe")
                .email("john@testsupplier.com")
                .phone("+1234567890")
                .address("123 Test Street")
                .city("Test City")
                .country("Test Country")
                .postalCode("12345")
                .notes("Test notes")
                .active(true)
                .build();

        testSupplierDTO = SupplierDTO.builder()
                .name("New Supplier")
                .contactPerson("Jane Smith")
                .email("jane@newsupplier.com")
                .phone("+0987654321")
                .address("456 New Street")
                .city("New City")
                .country("New Country")
                .postalCode("54321")
                .notes("New supplier notes")
                .active(true)
                .build();
    }

    @Test
    void createSupplier_Success() throws Exception {
        mockMvc.perform(post("/api/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSupplierDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Supplier")))
                .andExpect(jsonPath("$.contactPerson", is("Jane Smith")))
                .andExpect(jsonPath("$.email", is("jane@newsupplier.com")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    void createSupplier_InvalidData_ReturnsBadRequest() throws Exception {
        SupplierDTO invalidSupplier = SupplierDTO.builder()
                .name("") // Invalid: empty name
                .contactPerson("") // Invalid: empty contact person
                .email("invalid-email") // Invalid: malformed email
                .build();

        mockMvc.perform(post("/api/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidSupplier)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSupplierById_Success() throws Exception {
        Supplier savedSupplier = supplierRepository.save(testSupplier);

        mockMvc.perform(get("/api/suppliers/{id}", savedSupplier.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedSupplier.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Supplier")))
                .andExpect(jsonPath("$.contactPerson", is("John Doe")));
    }

    @Test
    void getSupplierById_NotFound_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/suppliers/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllSuppliers_Success() throws Exception {
        supplierRepository.save(testSupplier);

        mockMvc.perform(get("/api/suppliers")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Test Supplier")))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    void updateSupplier_Success() throws Exception {
        Supplier savedSupplier = supplierRepository.save(testSupplier);

        SupplierDTO updateDTO = SupplierDTO.builder()
                .name("Updated Supplier")
                .contactPerson("Updated Contact")
                .email("updated@supplier.com")
                .phone("+1111111111")
                .address("Updated Address")
                .city("Updated City")
                .country("Updated Country")
                .postalCode("11111")
                .notes("Updated notes")
                .active(false)
                .build();

        mockMvc.perform(put("/api/suppliers/{id}", savedSupplier.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Supplier")))
                .andExpect(jsonPath("$.contactPerson", is("Updated Contact")))
                .andExpect(jsonPath("$.active", is(false)));
    }

    @Test
    void updateSupplier_NotFound_ReturnsNotFound() throws Exception {
        mockMvc.perform(put("/api/suppliers/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSupplierDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSupplier_Success() throws Exception {
        Supplier savedSupplier = supplierRepository.save(testSupplier);

        mockMvc.perform(delete("/api/suppliers/{id}", savedSupplier.getId()))
                .andExpect(status().isNoContent());

        // Verify supplier is deleted
        mockMvc.perform(get("/api/suppliers/{id}", savedSupplier.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSupplier_NotFound_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/suppliers/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchSuppliers_ByName_Success() throws Exception {
        supplierRepository.save(testSupplier);

        mockMvc.perform(get("/api/suppliers/search")
                .param("name", "Test")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", containsString("Test")));
    }

    @Test
    void searchSuppliers_ByCity_Success() throws Exception {
        supplierRepository.save(testSupplier);

        mockMvc.perform(get("/api/suppliers/search")
                .param("city", "Test City")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].city", is("Test City")));
    }

    @Test
    void getActiveSuppliers_Success() throws Exception {
        // Create active supplier
        supplierRepository.save(testSupplier);

        // Create inactive supplier
        Supplier inactiveSupplier = Supplier.builder()
                .name("Inactive Supplier")
                .contactPerson("Inactive Contact")
                .email("inactive@supplier.com")
                .active(false)
                .build();
        supplierRepository.save(inactiveSupplier);

        mockMvc.perform(get("/api/suppliers/active")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].active", is(true)))
                .andExpect(jsonPath("$.content[0].name", is("Test Supplier")));
    }

    @Test
    void getSuppliersForDropdown_Success() throws Exception {
        supplierRepository.save(testSupplier);

        mockMvc.perform(get("/api/suppliers/dropdown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Supplier")))
                .andExpect(jsonPath("$[0].active", is(true)));
    }

    @Test
    void activateSupplier_Success() throws Exception {
        testSupplier.setActive(false);
        Supplier savedSupplier = supplierRepository.save(testSupplier);

        mockMvc.perform(put("/api/suppliers/{id}/activate", savedSupplier.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    void deactivateSupplier_Success() throws Exception {
        Supplier savedSupplier = supplierRepository.save(testSupplier);

        mockMvc.perform(put("/api/suppliers/{id}/deactivate", savedSupplier.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active", is(false)));
    }

    @Test
    void checkSupplierExists_ExistingName_ReturnsTrue() throws Exception {
        supplierRepository.save(testSupplier);

        mockMvc.perform(get("/api/suppliers/exists")
                .param("name", "Test Supplier"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void checkSupplierExists_NonExistingName_ReturnsFalse() throws Exception {
        mockMvc.perform(get("/api/suppliers/exists")
                .param("name", "Non-existent Supplier"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void getSupplierWithProducts_Success() throws Exception {
        Supplier savedSupplier = supplierRepository.save(testSupplier);

        mockMvc.perform(get("/api/suppliers/{id}/with-products", savedSupplier.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedSupplier.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Supplier")))
                .andExpect(jsonPath("$.productCount", is(0))); // No products associated
    }
}