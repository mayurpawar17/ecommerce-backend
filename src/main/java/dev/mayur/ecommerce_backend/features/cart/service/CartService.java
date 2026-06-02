package dev.mayur.ecommerce_backend.features.cart.service;

import dev.mayur.ecommerce_backend.features.cart.dto.AddToCartRequestDTO;
import dev.mayur.ecommerce_backend.features.cart.dto.CartItemResponseDTO;
import dev.mayur.ecommerce_backend.features.cart.dto.CartResponseDTO;
import dev.mayur.ecommerce_backend.features.cart.entity.Cart;
import dev.mayur.ecommerce_backend.features.cart.entity.CartItem;
import dev.mayur.ecommerce_backend.features.cart.repo.CartRepository;
import dev.mayur.ecommerce_backend.features.product.entity.Product;
import dev.mayur.ecommerce_backend.features.product.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartResponseDTO addProductToCart(Long userId, AddToCartRequestDTO addToCartRequestDTO) {
        // 1. Find or create a cart for the user
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return cartRepository.save(newCart);
        });

        // 2. Verify the product exists
        Product product = productRepository.findById(addToCartRequestDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

        // 3. Check if the product is already in the cart
        Optional<CartItem> existingItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(addToCartRequestDTO.getProductId())).findFirst();

        if (existingItem.isPresent()) {
            // Increase quantity if already present
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + addToCartRequestDTO.getQuantity());
        } else {
            // Add a completely new item to the cart
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(addToCartRequestDTO.getQuantity());
            cart.getItems().add(newItem);
        }

        // 4. Persist changes and convert the Entity graph to a clean DTO
        Cart updatedCart = cartRepository.save(cart);
        return mapToCartResponse(updatedCart);
    }


    /**
     * Helper mapping method to transform Cart Entity to CartResponse DTO
     */
    private CartResponseDTO mapToCartResponse(Cart cart) {
        CartResponseDTO response = new CartResponseDTO();
        response.setCartId(cart.getId());
        response.setUserId(cart.getUserId());

        // Map individual child items and compute sub-totals inline
        List<CartItemResponseDTO> itemDTOs = cart.getItems().stream().map(item -> {
            CartItemResponseDTO itemDTO = new CartItemResponseDTO();
            itemDTO.setItemId(item.getId());
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setProductName(item.getProduct().getName());
            itemDTO.setProductPrice(item.getProduct().getPrice());
            itemDTO.setQuantity(item.getQuantity());

            BigDecimal subTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            itemDTO.setSubTotal(subTotal);
            return itemDTO;
        }).collect(Collectors.toList());

        response.setItems(itemDTOs);

        // Sum all item sub-totals to calculate the final shopping cart total
        BigDecimal totalAmount = itemDTOs.stream().map(CartItemResponseDTO::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotalAmount(totalAmount);

        return response;
    }

}
