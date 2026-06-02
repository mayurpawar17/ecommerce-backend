package dev.mayur.ecommerce_backend.features.order.controller;

import dev.mayur.ecommerce_backend.features.order.dto.OrderResponseDTO;
import dev.mayur.ecommerce_backend.features.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // POST http://localhost:8080/api/orders/checkout?userId=1
    @PostMapping("/checkout")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<OrderResponseDTO> checkout(@RequestParam Long userId) {
        OrderResponseDTO orderResponseDTO = orderService.checkout(userId);
        return ResponseEntity.ok(orderResponseDTO);
    }
}
