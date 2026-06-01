package dev.mayur.ecommerce_backend.features.category.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryResponseDTO {
    // Getters and Setters
    private Long id;
    private String name;

    public CategoryResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
