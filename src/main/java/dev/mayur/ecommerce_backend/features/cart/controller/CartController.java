package dev.mayur.ecommerce_backend.features.cart.controller;

import dev.mayur.ecommerce_backend.features.cart.dto.AddToCartRequestDTO;
import dev.mayur.ecommerce_backend.features.cart.dto.CartResponseDTO;
import dev.mayur.ecommerce_backend.features.cart.entity.Cart;
import dev.mayur.ecommerce_backend.features.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // POST http://localhost:8080/api/cart/add?userId=1&productId=5&quantity=2
    @PostMapping("/add/items")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<CartResponseDTO> addToCart(@RequestParam Long userId,
                                                     @RequestBody AddToCartRequestDTO request) {

        CartResponseDTO response = cartService.addProductToCart(userId, request);
        return ResponseEntity.ok(response);
    }
}
