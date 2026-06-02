package dev.mayur.ecommerce_backend.features.cart.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponseDTO {
    private Long cartId;
    private Long userId;
    private List<CartItemResponseDTO> items;
    private BigDecimal totalAmount; // Sum of all item subTotals
}
