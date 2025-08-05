package com.inventory.api.controller;

import com.inventory.api.dto.SupplierDTO;
import com.inventory.api.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Supplier Management", description = "APIs for managing suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    @Operation(summary = "Create a new supplier", description = "Creates a new supplier in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid supplier data provided"),
            @ApiResponse(responseCode = "409", description = "Supplier with same name already exists")
    })
    public ResponseEntity<SupplierDTO> createSupplier(
            @Valid @RequestBody SupplierDTO supplierDTO) {
      log.info("Creating new supplier: {}", supplierDTO.getName());
        SupplierDTO createdSupplier = supplierService.createSupplier(supplierDTO);
        return new ResponseEntity<>(createdSupplier, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by ID", description = "Retrieves a supplier by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier found"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    public ResponseEntity<SupplierDTO> getSupplierById(
            @Parameter(description = "Supplier ID") @PathVariable Long id) {
        log.info("Fetching supplier with ID: {}", id);
        SupplierDTO supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplier);
    }

    @GetMapping
    @Operation(summary = "Get all suppliers", description = "Retrieves all suppliers with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppliers retrieved successfully")
    })
    public ResponseEntity<Page<SupplierDTO>> getAllSuppliers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("Fetching all suppliers - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<SupplierDTO> suppliers = supplierService.getAllSuppliers(pageable);
        return ResponseEntity.ok(suppliers);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update supplier", description = "Updates an existing supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid supplier data provided"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "409", description = "Supplier with same name already exists")
    })
    public ResponseEntity<SupplierDTO> updateSupplier(
            @Parameter(description = "Supplier ID") @PathVariable Long id,
            @Valid @RequestBody SupplierDTO supplierDTO) {
        log.info("Updating supplier with ID: {}", id);
        SupplierDTO updatedSupplier = supplierService.updateSupplier(id, supplierDTO);
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete supplier", description = "Deletes a supplier from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supplier deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "409", description = "Cannot delete supplier with associated products")
    })
    public ResponseEntity<Void> deleteSupplier(
            @Parameter(description = "Supplier ID") @PathVariable Long id) {
        log.info("Deleting supplier with ID: {}", id);
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search suppliers", description = "Search suppliers by various criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<Page<SupplierDTO>> searchSuppliers(
            @Parameter(description = "Supplier name") @RequestParam(required = false) String name,
            @Parameter(description = "Contact person") @RequestParam(required = false) String contactPerson,
            @Parameter(description = "City") @RequestParam(required = false) String city,
            @Parameter(description = "Country") @RequestParam(required = false) String country,
            @Parameter(description = "Active status") @RequestParam(required = false) Boolean active,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("Searching suppliers with criteria - name: {}, contactPerson: {}, city: {}, country: {}, active: {}", 
                name, contactPerson, city, country, active);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<SupplierDTO> suppliers = supplierService.searchSuppliers(
                name, contactPerson, city, country, active, pageable);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active suppliers", description = "Retrieves all active suppliers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active suppliers retrieved successfully")
    })
    public ResponseEntity<Page<SupplierDTO>> getActiveSuppliers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("Fetching active suppliers");
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<SupplierDTO> suppliers = supplierService.getActiveSuppliers(pageable);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/dropdown")
    @Operation(summary = "Get suppliers for dropdown", description = "Retrieves active suppliers for dropdown lists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppliers for dropdown retrieved successfully")
    })
    public ResponseEntity<List<SupplierDTO>> getSuppliersForDropdown() {
        log.info("Fetching suppliers for dropdown");
        List<SupplierDTO> suppliers = supplierService.getActiveSuppliersForDropdown();
        return ResponseEntity.ok(suppliers);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate supplier", description = "Activates a supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier activated successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    public ResponseEntity<SupplierDTO> activateSupplier(
            @Parameter(description = "Supplier ID") @PathVariable Long id) {
        log.info("Activating supplier with ID: {}", id);
        SupplierDTO supplier = supplierService.activateSupplier(id);
        return ResponseEntity.ok(supplier);
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate supplier", description = "Deactivates a supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    public ResponseEntity<SupplierDTO> deactivateSupplier(
            @Parameter(description = "Supplier ID") @PathVariable Long id) {
        log.info("Deactivating supplier with ID: {}", id);
        SupplierDTO supplier = supplierService.deactivateSupplier(id);
        return ResponseEntity.ok(supplier);
    }

    @GetMapping("/{id}/with-products")
    @Operation(summary = "Get supplier with products", description = "Retrieves a supplier with its associated products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier with products retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    public ResponseEntity<SupplierDTO> getSupplierWithProducts(
            @Parameter(description = "Supplier ID") @PathVariable Long id) {
        log.info("Fetching supplier with products for ID: {}", id);
        SupplierDTO supplier = supplierService.getSupplierWithProducts(id);
        return ResponseEntity.ok(supplier);
    }

    @GetMapping("/exists")
    @Operation(summary = "Check if supplier exists", description = "Checks if a supplier exists by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    public ResponseEntity<Boolean> checkSupplierExists(
            @Parameter(description = "Supplier name") @RequestParam String name) {
        log.info("Checking if supplier exists with name: {}", name);
        boolean exists = supplierService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
}