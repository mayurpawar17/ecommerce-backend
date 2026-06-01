package dev.mayur.ecommerce_backend.features.product.service;

import dev.mayur.ecommerce_backend.features.product.dto.ProductRequestDTO;
import dev.mayur.ecommerce_backend.features.product.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO requestDTO);

    Page<ProductResponseDTO> getAllProducts(Pageable pageable);

    ProductResponseDTO getProductById(Long id);
}



