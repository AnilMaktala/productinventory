package com.inventory.api.mapper;

import com.inventory.api.dto.SupplierDTO;
import com.inventory.api.model.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    /**
     * Convert Supplier entity to SupplierDTO
     */
    public SupplierDTO toDTO(Supplier supplier) {
        if (supplier == null) {
            return null;
        }

        return SupplierDTO.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .contactPerson(supplier.getContactPerson())
                .email(supplier.getEmail())
                .phone(supplier.getPhone())
                .address(supplier.getAddress())
                .city(supplier.getCity())
                .country(supplier.getCountry())
                .postalCode(supplier.getPostalCode())
                .notes(supplier.getNotes())
                .active(supplier.getActive())
                .productCount(supplier.getProducts() != null ? supplier.getProducts().size() : 0)
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();
    }

    /**
     * Convert SupplierDTO to Supplier entity
     */
    public Supplier toEntity(SupplierDTO supplierDTO) {
        if (supplierDTO == null) {
            return null;
        }

        return Supplier.builder()
                .id(supplierDTO.getId())
                .name(supplierDTO.getName())
                .contactPerson(supplierDTO.getContactPerson())
                .email(supplierDTO.getEmail())
                .phone(supplierDTO.getPhone())
                .address(supplierDTO.getAddress())
                .city(supplierDTO.getCity())
                .country(supplierDTO.getCountry())
                .postalCode(supplierDTO.getPostalCode())
                .notes(supplierDTO.getNotes())
                .active(supplierDTO.getActive() != null ? supplierDTO.getActive() : true)
                .build();
    }

    /**
     * Update existing Supplier entity with data from SupplierDTO
     */
    public void updateEntityFromDTO(SupplierDTO supplierDTO, Supplier supplier) {
        if (supplierDTO == null || supplier == null) {
            return;
        }

        supplier.setName(supplierDTO.getName());
        supplier.setContactPerson(supplierDTO.getContactPerson());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setPhone(supplierDTO.getPhone());
        supplier.setAddress(supplierDTO.getAddress());
        supplier.setCity(supplierDTO.getCity());
        supplier.setCountry(supplierDTO.getCountry());
        supplier.setPostalCode(supplierDTO.getPostalCode());
        supplier.setNotes(supplierDTO.getNotes());

        if (supplierDTO.getActive() != null) {
            supplier.setActive(supplierDTO.getActive());
        }
    }

    /**
     * Convert Supplier entity to SupplierDTO with product count
     */
    public SupplierDTO toDTOWithProductCount(Supplier supplier, int productCount) {
        SupplierDTO dto = toDTO(supplier);
        dto.setProductCount(productCount);
        return dto;
    }
}