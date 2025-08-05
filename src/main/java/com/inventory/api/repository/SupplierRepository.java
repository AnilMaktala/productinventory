package com.inventory.api.repository;

import com.inventory.api.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    /**
     * Find suppliers by name containing the given string (case-insensitive)
     */
    Page<Supplier> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Find suppliers by contact person containing the given string
     * (case-insensitive)
     */
    Page<Supplier> findByContactPersonContainingIgnoreCase(String contactPerson, Pageable pageable);

    /**
     * Find suppliers by city (case-insensitive)
     */
    Page<Supplier> findByCityIgnoreCase(String city, Pageable pageable);

    /**
     * Find suppliers by country (case-insensitive)
     */
    Page<Supplier> findByCountryIgnoreCase(String country, Pageable pageable);

    /**
     * Find active suppliers only
     */
    Page<Supplier> findByActiveTrue(Pageable pageable);

    /**
     * Find inactive suppliers only
     */
    Page<Supplier> findByActiveFalse(Pageable pageable);

    /**
     * Find suppliers by active status
     */
    Page<Supplier> findByActive(Boolean active, Pageable pageable);

    /**
     * Search suppliers by multiple criteria
     */
    @Query("SELECT s FROM Supplier s WHERE " +
            "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:contactPerson IS NULL OR LOWER(s.contactPerson) LIKE LOWER(CONCAT('%', :contactPerson, '%'))) AND " +
            "(:city IS NULL OR LOWER(s.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
            "(:country IS NULL OR LOWER(s.country) LIKE LOWER(CONCAT('%', :country, '%'))) AND " +
            "(:active IS NULL OR s.active = :active)")
    Page<Supplier> searchSuppliers(@Param("name") String name,
            @Param("contactPerson") String contactPerson,
            @Param("city") String city,
            @Param("country") String country,
            @Param("active") Boolean active,
            Pageable pageable);

    /**
     * Find suppliers with products count
     */
    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.products WHERE s.id = :id")
    Optional<Supplier> findByIdWithProducts(@Param("id") Long id);

    /**
     * Find all active suppliers for dropdown lists
     */
    List<Supplier> findByActiveTrueOrderByName();

    /**
     * Check if supplier exists by name (case-insensitive)
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Find supplier by name (case-insensitive)
     */
    Optional<Supplier> findByNameIgnoreCase(String name);
}