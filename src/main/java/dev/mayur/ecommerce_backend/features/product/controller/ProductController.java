package dev.mayur.ecommerce_backend.features.product.controller;

import dev.mayur.ecommerce_backend.core.utils.dto.ApiResponse;
import dev.mayur.ecommerce_backend.core.utils.dto.Pagination;
import dev.mayur.ecommerce_backend.features.product.dto.ProductRequestDTO;
import dev.mayur.ecommerce_backend.features.product.dto.ProductResponseDTO;
import dev.mayur.ecommerce_backend.features.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create Product with Validation
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO createdProduct = productService.createProduct(requestDTO);


        ApiResponse<ProductResponseDTO> body = ApiResponse.success("Expense created successfully!", createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // Get Single Product by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductById(@PathVariable Long id) {
        ProductResponseDTO product = productService.getProductById(id);
        ApiResponse<ProductResponseDTO> body = ApiResponse.success("Expense created successfully!", product);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // Get All Products with Pagination and Custom Sorting
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponseDTO> products = productService.getAllProducts(pageable);

        Pagination<ProductResponseDTO> pagination = new Pagination<>();
        pagination.setPage(products.getNumber());
        pagination.setSize(products.getSize());
        pagination.setTotalElements(products.getTotalElements());
        pagination.setTotalPages(products.getTotalPages());
        pagination.setLast(products.isLast());

        var data = products.getContent();
        ApiResponse<List<ProductResponseDTO>> body = ApiResponse.success("Products fetched successfully!", data, pagination);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
