package dev.mayur.ecommerce_backend.features.category.service;

import dev.mayur.ecommerce_backend.features.category.dto.CategoryRequestDTO;
import dev.mayur.ecommerce_backend.features.category.dto.CategoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO);

    Page<CategoryResponseDTO> getAllCategories(Pageable pageable);

    CategoryResponseDTO getCategoryById(Long id);

    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO);

    void deleteCategory(Long id);
}




