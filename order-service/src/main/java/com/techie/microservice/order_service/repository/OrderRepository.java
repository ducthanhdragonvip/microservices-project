package com.techie.microservice.order_service.repository;

import com.techie.microservice.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Custom query methods can be defined here if needed
    // For example, to find orders by order number:
    // Optional<Order> findByOrderNumber(String orderNumber);
}
