package com.inventory.api.service;

import com.inventory.api.dto.CategoryDTO;
import com.inventory.api.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO getCategoryById(Long id);

    List<CategoryDTO> getAllCategories();

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);

    Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable);
}