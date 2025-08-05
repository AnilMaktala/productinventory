package com.inventory.api.service;

import com.inventory.api.dto.SupplierDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierService {

    /**
     * Create a new supplier
     */
    SupplierDTO createSupplier(SupplierDTO supplierDTO);

    /**
     * Get supplier by ID
     */
    SupplierDTO getSupplierById(Long id);

    /**
     * Get all suppliers with pagination
     */
    Page<SupplierDTO> getAllSuppliers(Pageable pageable);

    /**
     * Update existing supplier
     */
    SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO);

    /**
     * Delete supplier by ID
     */
    void deleteSupplier(Long id);

    /**
     * Search suppliers by various criteria
     */
    Page<SupplierDTO> searchSuppliers(String name, String contactPerson, String city,
            String country, Boolean active, Pageable pageable);

    /**
     * Get suppliers by name (case-insensitive search)
     */
    Page<SupplierDTO> getSuppliersByName(String name, Pageable pageable);

    /**
     * Get suppliers by city
     */
    Page<SupplierDTO> getSuppliersByCity(String city, Pageable pageable);

    /**
     * Get suppliers by country
     */
    Page<SupplierDTO> getSuppliersByCountry(String country, Pageable pageable);

    /**
     * Get active suppliers only
     */
    Page<SupplierDTO> getActiveSuppliers(Pageable pageable);

    /**
     * Get inactive suppliers only
     */
    Page<SupplierDTO> getInactiveSuppliers(Pageable pageable);

    /**
     * Get all active suppliers for dropdown lists (no pagination)
     */
    List<SupplierDTO> getActiveSuppliersForDropdown();

    /**
     * Activate supplier
     */
    SupplierDTO activateSupplier(Long id);

    /**
     * Deactivate supplier
     */
    SupplierDTO deactivateSupplier(Long id);

    /**
     * Check if supplier exists by name
     */
    boolean existsByName(String name);

    /**
     * Get supplier with product count
     */
    SupplierDTO getSupplierWithProducts(Long id);
}