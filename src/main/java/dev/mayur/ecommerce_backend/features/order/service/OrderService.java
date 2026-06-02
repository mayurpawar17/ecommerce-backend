package dev.mayur.ecommerce_backend.features.order.service;

import dev.mayur.ecommerce_backend.features.cart.entity.Cart;
import dev.mayur.ecommerce_backend.features.cart.entity.CartItem;
import dev.mayur.ecommerce_backend.features.cart.repo.CartRepository;
import dev.mayur.ecommerce_backend.features.order.dto.OrderItemResponseDTO;
import dev.mayur.ecommerce_backend.features.order.dto.OrderResponseDTO;
import dev.mayur.ecommerce_backend.features.order.entity.Order;
import dev.mayur.ecommerce_backend.features.order.entity.OrderItem;
import dev.mayur.ecommerce_backend.features.order.repo.OrderRepository;
import dev.mayur.ecommerce_backend.features.product.entity.Product;
import dev.mayur.ecommerce_backend.features.product.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponseDTO checkout(Long userId) {
        // 1. Fetch the user's cart
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart");
        }

        // 2. Initialize a new Order
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PENDING");
        BigDecimal total = BigDecimal.ZERO;

        // 3. Process each item in the cart
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            // Validate Stock Availability
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Deduct Inventory Stock
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            // Map CartItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            order.getItems().add(orderItem);

            // Calculate running total
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            total = total.add(itemTotal);
        }

        order.setTotalAmount(total);

        // 4. Save the finalized Order to the Database
        Order savedOrder = orderRepository.save(order);

        // 5. Clear the Cart items so the user has an empty basket again
        cart.getItems().clear();
        cartRepository.save(cart);

        // 6. Map finalized entities over to clean Data Transfer Objects
        return mapToOrderResponse(savedOrder);
    }



    /**
     * Helper mapping method to transform Order Entity into OrderResponse DTO
     */
    private OrderResponseDTO mapToOrderResponse(Order order) {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());

        List<OrderItemResponseDTO> itemDTOs = order.getItems().stream().map(item -> {
            OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setProductName(item.getProduct().getName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPriceAtPurchase(item.getPriceAtPurchase());
            return itemDTO;
        }).collect(Collectors.toList());

        response.setItems(itemDTOs);
        return response;
    }
}
