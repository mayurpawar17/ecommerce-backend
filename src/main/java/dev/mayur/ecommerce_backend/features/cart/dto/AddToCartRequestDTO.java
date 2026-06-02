package dev.mayur.ecommerce_backend.features.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddToCartRequestDTO {
    private Long productId;
    private Integer quantity;
}
