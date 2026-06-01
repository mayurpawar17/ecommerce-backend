package dev.mayur.ecommerce_backend.features.category.controller;

import dev.mayur.ecommerce_backend.core.utils.dto.ApiResponse;
import dev.mayur.ecommerce_backend.core.utils.dto.Pagination;
import dev.mayur.ecommerce_backend.features.category.dto.CategoryRequestDTO;
import dev.mayur.ecommerce_backend.features.category.dto.CategoryResponseDTO;
import dev.mayur.ecommerce_backend.features.category.service.CategoryService;
import dev.mayur.ecommerce_backend.features.product.dto.ProductResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Create Category with JSR-380 validation
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
        CategoryResponseDTO createdCategory = categoryService.createCategory(requestDTO);
        ApiResponse<CategoryResponseDTO> response = ApiResponse.success("Category created successfully", createdCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get Single Category
    @GetMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> getCategoryById(@PathVariable Long id) {
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        ApiResponse<CategoryResponseDTO> response = ApiResponse.success("Category found successfully", category);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Get All Categories with Pagination & Sort properties
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getAllCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CategoryResponseDTO> categories = categoryService.getAllCategories(pageable);
        Pagination<ProductResponseDTO> pagination = new Pagination<>();
        pagination.setPage(categories.getNumber());
        pagination.setSize(categories.getSize());
        pagination.setTotalElements(categories.getTotalElements());
        pagination.setTotalPages(categories.getTotalPages());
        pagination.setLast(categories.isLast());
        var data = categories.getContent();
        ApiResponse<List<CategoryResponseDTO>> body = ApiResponse.success("Categories fetched successfully!", data, pagination);
        return ResponseEntity.status(HttpStatus.OK).body(body);

    }

    // Update Category
    @PutMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO requestDTO) {
        CategoryResponseDTO updatedCategory = categoryService.updateCategory(id, requestDTO);
        ApiResponse<CategoryResponseDTO> response = ApiResponse.success("Category updated successfully", updatedCategory);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Delete Category
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);

        ApiResponse<String> response = ApiResponse.success("Category deleted successfully", "Deleted");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
