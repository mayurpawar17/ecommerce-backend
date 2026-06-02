package dev.mayur.ecommerce_backend.features.order.repo;

import dev.mayur.ecommerce_backend.features.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}




