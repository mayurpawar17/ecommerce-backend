package dev.mayur.ecommerce_backend.features.product.repo;

import dev.mayur.ecommerce_backend.features.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom method to fetch products by category with pagination
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
}


