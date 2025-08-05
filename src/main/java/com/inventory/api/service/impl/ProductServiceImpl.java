package com.inventory.api.service.impl;

import com.inventory.api.dto.ProductDTO;
import com.inventory.api.exception.InsufficientInventoryException;
import com.inventory.api.exception.ResourceNotFoundException;
import com.inventory.api.mapper.ProductMapper;
import com.inventory.api.model.Category;
import com.inventory.api.model.Product;
import com.inventory.api.repository.CategoryRepository;
import com.inventory.api.repository.ProductRepository;
import com.inventory.api.repository.SupplierRepository;
import com.inventory.api.model.Supplier;
import com.inventory.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

        private final ProductRepository productRepository;
        private final CategoryRepository categoryRepository;
        private final SupplierRepository supplierRepository;
        private final ProductMapper productMapper;

        @Autowired
        public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                        SupplierRepository supplierRepository, ProductMapper productMapper) {
                this.productRepository = productRepository;
                this.categoryRepository = categoryRepository;
                this.supplierRepository = supplierRepository;
                this.productMapper = productMapper;
        }

        @Override
        @Caching(evict = {
                        @CacheEvict(value = "productsList", allEntries = true),
                        @CacheEvict(value = "productsSearch", allEntries = true)
        })
        public ProductDTO createProduct(ProductDTO productDTO) {
                Product product = productMapper.toEntity(productDTO);

                if (productDTO.getCategoryId() != null) {
                        Category category = categoryRepository.findById(productDTO.getCategoryId())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Category not found with id: " + productDTO.getCategoryId()));
                        product.setCategory(category);
                }

                if (productDTO.getSupplierId() != null) {
                        Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Supplier not found with id: " + productDTO.getSupplierId()));
                        product.setSupplier(supplier);
                }

                Product savedProduct = productRepository.save(product);
                return productMapper.toDto(savedProduct);
        }

        @Override
        @Transactional(readOnly = true)
        @Cacheable(value = "products", key = "#id")
        public ProductDTO getProductById(Long id) {
                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
                return productMapper.toDto(product);
        }

        @Override
        @Transactional(readOnly = true)
        @Cacheable(value = "products", key = "'sku_' + #sku")
        public ProductDTO getProductBySku(String sku) {
                Product product = productRepository.findBySku(sku)
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
                return productMapper.toDto(product);
        }

        @Override
        @Transactional(readOnly = true)
        @Cacheable(value = "productsList", key = "'page_' + #pageable.pageNumber + '_' + #pageable.pageSize")
        public Page<ProductDTO> getAllProducts(Pageable pageable) {
                return productRepository.findAll(pageable)
                                .map(productMapper::toDto);
        }

        @Override
        @Caching(put = { @CachePut(value = "products", key = "#id") }, evict = {
                        @CacheEvict(value = "productsList", allEntries = true),
                        @CacheEvict(value = "productsSearch", allEntries = true)
        })
        public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
                Product existingProduct = productRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

                // Update fields
                existingProduct.setName(productDTO.getName());
                existingProduct.setDescription(productDTO.getDescription());
                existingProduct.setPrice(productDTO.getPrice());
                existingProduct.setSku(productDTO.getSku());
                existingProduct.setLowStockThreshold(productDTO.getLowStockThreshold());

                // Update category if provided
                if (productDTO.getCategoryId() != null) {
                        Category category = categoryRepository.findById(productDTO.getCategoryId())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Category not found with id: " + productDTO.getCategoryId()));
                        existingProduct.setCategory(category);
                } else {
                        existingProduct.setCategory(null);
                }

                // Update supplier if provided
                if (productDTO.getSupplierId() != null) {
                        Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Supplier not found with id: " + productDTO.getSupplierId()));
                        existingProduct.setSupplier(supplier);
                } else {
                        existingProduct.setSupplier(null);
                }

                Product updatedProduct = productRepository.save(existingProduct);
                return productMapper.toDto(updatedProduct);
        }

        @Override
        @Caching(evict = {
                        @CacheEvict(value = "products", key = "#id"),
                        @CacheEvict(value = "productsList", allEntries = true),
                        @CacheEvict(value = "productsSearch", allEntries = true)
        })
        public void deleteProduct(Long id) {
                if (!productRepository.existsById(id)) {
                        throw new ResourceNotFoundException("Product not found with id: " + id);
                }
                productRepository.deleteById(id);
        }

        @Override
        @Transactional(readOnly = true)
        @Cacheable(value = "productInventory", key = "#id")
        public Integer getInventoryLevel(Long id) {
                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
                return product.getInventoryQuantity();
        }

        @Override
        @Caching(put = {
                        @CachePut(value = "products", key = "#id"),
                        @CachePut(value = "productInventory", key = "#id")
        }, evict = {
                        @CacheEvict(value = "productsList", allEntries = true),
                        @CacheEvict(value = "productsSearch", allEntries = true)
        })
        public ProductDTO updateInventoryLevel(Long id, Integer quantity) {
                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

                product.setInventoryQuantity(quantity);
                Product updatedProduct = productRepository.save(product);
                return productMapper.toDto(updatedProduct);
        }

        @Override
        @Caching(put = {
                        @CachePut(value = "products", key = "#id"),
                        @CachePut(value = "productInventory", key = "#id")
        }, evict = {
                        @CacheEvict(value = "productsList", allEntries = true),
                        @CacheEvict(value = "productsSearch", allEntries = true)
        })
        public ProductDTO increaseInventory(Long id, Integer quantity) {
                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

                int newQuantity = product.getInventoryQuantity() + quantity;
                product.setInventoryQuantity(newQuantity);

                Product updatedProduct = productRepository.save(product);
                return productMapper.toDto(updatedProduct);
        }

        @Override
        @Caching(put = {
                        @CachePut(value = "products", key = "#id"),
                        @CachePut(value = "productInventory", key = "#id")
        }, evict = {
                        @CacheEvict(value = "productsList", allEntries = true),
                        @CacheEvict(value = "productsSearch", allEntries = true)
        })
        public ProductDTO decreaseInventory(Long id, Integer quantity) {
                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

                int newQuantity = product.getInventoryQuantity() - quantity;
                if (newQuantity < 0) {
                        throw new InsufficientInventoryException(
                                        "Cannot decrease inventory below zero. Current inventory: "
                                                        + product.getInventoryQuantity() + ", Requested decrease: "
                                                        + quantity);
                }

                product.setInventoryQuantity(newQuantity);
                Product updatedProduct = productRepository.save(product);
                return productMapper.toDto(updatedProduct);
        }

        @Override
        @Transactional(readOnly = true)
        @Cacheable(value = "productsSearch", key = "'name_' + #name + '_category_' + #categoryId + '_minPrice_' + #minPrice + '_maxPrice_' + #maxPrice + '_inStock_' + #inStock + '_page_' + #pageable.pageNumber")
        public Page<ProductDTO> searchProducts(
                        String name,
                        Long categoryId,
                        BigDecimal minPrice,
                        BigDecimal maxPrice,
                        Boolean inStock,
                        Pageable pageable) {

                // This is a simplified implementation - in a real application, you would use a
                // more sophisticated query
                if (name != null && !name.isEmpty()) {
                        return productRepository.findByNameContainingIgnoreCase(name, pageable)
                                        .map(productMapper::toDto);
                } else if (categoryId != null) {
                        return productRepository.findByCategoryId(categoryId, pageable)
                                        .map(productMapper::toDto);
                } else if (minPrice != null && maxPrice != null) {
                        return productRepository.findByPriceBetween(minPrice, maxPrice, pageable)
                                        .map(productMapper::toDto);
                } else if (inStock != null && inStock) {
                        return productRepository.findByInventoryQuantityGreaterThan(0, pageable)
                                        .map(productMapper::toDto);
                } else {
                        return productRepository.findAll(pageable)
                                        .map(productMapper::toDto);
                }
        }

        @Override
        @Transactional(readOnly = true)
        @Cacheable(value = "productsLowStock")
        public List<ProductDTO> getLowStockProducts() {
                return productRepository.findByLowStockIsTrue().stream()
                                .map(productMapper::toDto)
                                .collect(Collectors.toList());
        }

        @Override
        @Caching(put = { @CachePut(value = "products", key = "#productId") }, evict = {
                        @CacheEvict(value = "productsList", allEntries = true),
                        @CacheEvict(value = "productsSearch", allEntries = true)
        })
        public ProductDTO assignCategory(Long productId, Long categoryId) {
                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Product not found with id: " + productId));

                Category category = categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Category not found with id: " + categoryId));

                product.setCategory(category);
                Product updatedProduct = productRepository.save(product);
                return productMapper.toDto(updatedProduct);
        }

        @Override
        @Caching(put = { @CachePut(value = "products", key = "#productId") }, evict = {
                        @CacheEvict(value = "productsList", allEntries = true),
                        @CacheEvict(value = "productsSearch", allEntries = true)
        })

        public ProductDTO assignSupplier(Long productId, Long supplierId) {
                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Product not found with id: " + productId));

                Supplier supplier = supplierRepository.findById(supplierId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Supplier not found with id: " + supplierId));

                product.setSupplier(supplier);
                Product updatedProduct = productRepository.save(product);
                return productMapper.toDto(updatedProduct);
        }

        @Override
        @Transactional(readOnly = true)
        @Cacheable(value = "productsBySupplier", key = "'supplier_' + #supplierId + '_page_' + #pageable.pageNumber")
        public Page<ProductDTO> getProductsBySupplier(Long supplierId, Pageable pageable) {
                return productRepository.findBySupplierId(supplierId, pageable)
                                .map(productMapper::toDto);
        }
}