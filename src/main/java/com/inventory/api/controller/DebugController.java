package com.inventory.api.controller;

import com.inventory.api.model.Supplier;
import com.inventory.api.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
public class DebugController {

    private final SupplierRepository supplierRepository;

    @GetMapping("/suppliers")
    public List<Supplier> getSuppliers() {
        return supplierRepository.findAll();
    }

    @GetMapping("/suppliers/count")
    public long getSuppliersCount() {
        return supplierRepository.count();
    }
}