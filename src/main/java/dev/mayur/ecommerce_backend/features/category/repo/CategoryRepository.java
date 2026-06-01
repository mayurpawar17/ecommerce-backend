package dev.mayur.ecommerce_backend.features.category.repo;

import dev.mayur.ecommerce_backend.features.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Check if category exists by name to prevent database-level constraint crashes
    boolean existsByName(String name);
}


