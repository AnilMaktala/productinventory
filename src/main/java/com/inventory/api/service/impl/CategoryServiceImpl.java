package com.inventory.api.service.impl;

import com.inventory.api.dto.CategoryDTO;
import com.inventory.api.dto.ProductDTO;
import com.inventory.api.exception.ResourceNotFoundException;
import com.inventory.api.mapper.CategoryMapper;
import com.inventory.api.mapper.ProductMapper;
import com.inventory.api.model.Category;
import com.inventory.api.repository.CategoryRepository;
import com.inventory.api.repository.ProductRepository;
import com.inventory.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository,
            CategoryMapper categoryMapper, ProductMapper productMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "categoriesList", allEntries = true)
    })
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "categories", key = "#id")
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        CategoryDTO categoryDTO = categoryMapper.toDto(category);
        categoryDTO.setProductCount(category.getProducts().size());

        return categoryDTO;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "categoriesList")
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> {
                    CategoryDTO dto = categoryMapper.toDto(category);
                    dto.setProductCount(category.getProducts().size());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Caching(put = { @CachePut(value = "categories", key = "#id") }, evict = {
            @CacheEvict(value = "categoriesList", allEntries = true) })
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        existingCategory.setName(categoryDTO.getName());
        existingCategory.setDescription(categoryDTO.getDescription());

        Category updatedCategory = categoryRepository.save(existingCategory);

        CategoryDTO updatedDTO = categoryMapper.toDto(updatedCategory);
        updatedDTO.setProductCount(updatedCategory.getProducts().size());

        return updatedDTO;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "categories", key = "#id"),
            @CacheEvict(value = "categoriesList", allEntries = true)
    })
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getProducts().isEmpty()) {
            throw new DataIntegrityViolationException(
                    "Cannot delete category with associated products. Remove products first or reassign them to another category.");
        }

        categoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "categoryProducts", key = "#categoryId + '_page_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        return productRepository.findByCategory(category, pageable)
                .map(productMapper::toDto);
    }
}