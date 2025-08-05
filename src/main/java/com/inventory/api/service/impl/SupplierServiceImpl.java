package com.inventory.api.service.impl;

import com.inventory.api.dto.SupplierDTO;
import com.inventory.api.exception.ResourceNotFoundException;
import com.inventory.api.mapper.SupplierMapper;
import com.inventory.api.model.Supplier;
import com.inventory.api.repository.SupplierRepository;
import com.inventory.api.repository.ProductRepository;
import com.inventory.api.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final ProductRepository productRepository;

    @Override
    @CacheEvict(value = "suppliers", allEntries = true)
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        log.info("Creating new supplier: {}", supplierDTO.getName());

        // Check if supplier with same name already exists
        if (supplierRepository.existsByNameIgnoreCase(supplierDTO.getName())) {
            throw new IllegalArgumentException("Supplier with name '" + supplierDTO.getName() + "' already exists");
        }

        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        Supplier savedSupplier = supplierRepository.save(supplier);

        log.info("Successfully created supplier with ID: {}", savedSupplier.getId());
        int productCount = (int) productRepository.countBySupplierId(savedSupplier.getId());
        return supplierMapper.toDTOWithProductCount(savedSupplier, productCount);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierDTO getSupplierById(Long id) {
        log.info("Fetching supplier with ID: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));

        return supplierMapper.toDTO(supplier);
    }

    @Override
    @Cacheable(value = "suppliers", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<SupplierDTO> getAllSuppliers(Pageable pageable) {
        log.info("Fetching all suppliers with pagination: page {}, size {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Supplier> suppliers = supplierRepository.findAll(pageable);
        return suppliers.map(supplier -> {
            int productCount = (int) productRepository.countBySupplierId(supplier.getId());
            return supplierMapper.toDTOWithProductCount(supplier, productCount);
        });
    }

    @Override
    @CacheEvict(value = "suppliers", allEntries = true)
    public SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
        log.info("Updating supplier with ID: {}", id);

        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));

        // Check if name is being changed and if new name already exists
        if (!existingSupplier.getName().equalsIgnoreCase(supplierDTO.getName()) &&
                supplierRepository.existsByNameIgnoreCase(supplierDTO.getName())) {
            throw new IllegalArgumentException("Supplier with name '" + supplierDTO.getName() + "' already exists");
        }

        supplierMapper.updateEntityFromDTO(supplierDTO, existingSupplier);
        Supplier updatedSupplier = supplierRepository.save(existingSupplier);

        log.info("Successfully updated supplier with ID: {}", id);
        int productCount = (int) productRepository.countBySupplierId(id);
        return supplierMapper.toDTOWithProductCount(updatedSupplier, productCount);
    }

    @Override
    @CacheEvict(value = "suppliers", allEntries = true)
    public void deleteSupplier(Long id) {
        log.info("Deleting supplier with ID: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));

        // Check if supplier has associated products
        if (supplier.getProducts() != null && !supplier.getProducts().isEmpty()) {
            throw new IllegalStateException("Cannot delete supplier with associated products. " +
                    "Please reassign or remove products first.");
        }

        supplierRepository.delete(supplier);
        log.info("Successfully deleted supplier with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierDTO> searchSuppliers(String name, String contactPerson, String city,
            String country, Boolean active, Pageable pageable) {
        log.info("Searching suppliers with criteria - name: {}, contactPerson: {}, city: {}, country: {}, active: {}",
                name, contactPerson, city, country, active);

        Page<Supplier> suppliers = supplierRepository.searchSuppliers(
                name, contactPerson, city, country, active, pageable);

        return suppliers.map(supplier -> {
            int productCount = (int) productRepository.countBySupplierId(supplier.getId());
            return supplierMapper.toDTOWithProductCount(supplier, productCount);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierDTO> getSuppliersByName(String name, Pageable pageable) {
        log.info("Fetching suppliers by name: {}", name);

        Page<Supplier> suppliers = supplierRepository.findByNameContainingIgnoreCase(name, pageable);
        return suppliers.map(supplier -> {
            int productCount = (int) productRepository.countBySupplierId(supplier.getId());
            return supplierMapper.toDTOWithProductCount(supplier, productCount);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierDTO> getSuppliersByCity(String city, Pageable pageable) {
        log.info("Fetching suppliers by city: {}", city);

        Page<Supplier> suppliers = supplierRepository.findByCityIgnoreCase(city, pageable);
        return suppliers.map(supplier -> {
            int productCount = (int) productRepository.countBySupplierId(supplier.getId());
            return supplierMapper.toDTOWithProductCount(supplier, productCount);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierDTO> getSuppliersByCountry(String country, Pageable pageable) {
        log.info("Fetching suppliers by country: {}", country);

        Page<Supplier> suppliers = supplierRepository.findByCountryIgnoreCase(country, pageable);
        return suppliers.map(supplier -> {
            int productCount = (int) productRepository.countBySupplierId(supplier.getId());
            return supplierMapper.toDTOWithProductCount(supplier, productCount);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierDTO> getActiveSuppliers(Pageable pageable) {
        log.info("Fetching active suppliers");

        Page<Supplier> suppliers = supplierRepository.findByActiveTrue(pageable);
        return suppliers.map(supplier -> {
            int productCount = (int) productRepository.countBySupplierId(supplier.getId());
            return supplierMapper.toDTOWithProductCount(supplier, productCount);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierDTO> getInactiveSuppliers(Pageable pageable) {
        log.info("Fetching inactive suppliers");

        Page<Supplier> suppliers = supplierRepository.findByActiveFalse(pageable);
        return suppliers.map(supplier -> {
            int productCount = (int) productRepository.countBySupplierId(supplier.getId());
            return supplierMapper.toDTOWithProductCount(supplier, productCount);
        });
    }

    @Override
    @Cacheable(value = "activeSuppliers")
    @Transactional(readOnly = true)
    public List<SupplierDTO> getActiveSuppliersForDropdown() {
        log.info("Fetching active suppliers for dropdown");

        List<Supplier> suppliers = supplierRepository.findByActiveTrueOrderByName();
        return suppliers.stream()
                .map(supplier -> {
                    int productCount = (int) productRepository.countBySupplierId(supplier.getId());
                    return supplierMapper.toDTOWithProductCount(supplier, productCount);
                })
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "suppliers", allEntries = true)
    public SupplierDTO activateSupplier(Long id) {
        log.info("Activating supplier with ID: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));

        supplier.setActive(true);
        Supplier updatedSupplier = supplierRepository.save(supplier);

        log.info("Successfully activated supplier with ID: {}", id);
        int productCount = (int) productRepository.countBySupplierId(id);
        return supplierMapper.toDTOWithProductCount(updatedSupplier, productCount);
    }

    @Override
    @CacheEvict(value = "suppliers", allEntries = true)
    public SupplierDTO deactivateSupplier(Long id) {
        log.info("Deactivating supplier with ID: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));

        supplier.setActive(false);
        Supplier updatedSupplier = supplierRepository.save(supplier);

        log.info("Successfully deactivated supplier with ID: {}", id);
        int productCount = (int) productRepository.countBySupplierId(id);
        return supplierMapper.toDTOWithProductCount(updatedSupplier, productCount);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return supplierRepository.existsByNameIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierDTO getSupplierWithProducts(Long id) {
        log.info("Fetching supplier with products for ID: {}", id);

        Supplier supplier = supplierRepository.findByIdWithProducts(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));

        int productCount = supplier.getProducts() != null ? supplier.getProducts().size() : 0;
        return supplierMapper.toDTOWithProductCount(supplier, productCount);
    }
}