package dev.mayur.ecommerce_backend.features.product.service;

import dev.mayur.ecommerce_backend.core.exception.custom.ResourceNotFoundException;
import dev.mayur.ecommerce_backend.features.category.entity.Category;
import dev.mayur.ecommerce_backend.features.category.repo.CategoryRepository;
import dev.mayur.ecommerce_backend.features.product.dto.ProductRequestDTO;
import dev.mayur.ecommerce_backend.features.product.dto.ProductResponseDTO;
import dev.mayur.ecommerce_backend.features.product.entity.Product;
import dev.mayur.ecommerce_backend.features.product.repo.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + dto.getCategoryId()));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return mapToResponseDTO(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findByIdWithCategory( id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return mapToResponseDTO(product);
    }

    // Helper method to convert Entity to DTO
    private ProductResponseDTO mapToResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory().getName()
        );
    }
}
