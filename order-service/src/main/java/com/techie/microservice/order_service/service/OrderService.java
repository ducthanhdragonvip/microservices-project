package com.techie.microservice.order_service.service;
import com.techie.microservice.order_service.client.InventoryClient;
import com.techie.microservice.order_service.dto.OrderRequest;
import com.techie.microservice.order_service.event.OrderPlacedEvent;
import com.techie.microservice.order_service.model.Order;
import com.techie.microservice.order_service.repository.OrderRepository;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;


    public void placeOrder(OrderRequest orderRequest) {
        boolean inStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if (inStock) {
            var order = mapToOrder(orderRequest);
            orderRepository.save(order);

            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(order.getOrderNumber(),orderRequest.userDetails().email());
            log.info("Start- Sending OrderPlacedEvent {} to Kafka Topic", orderPlacedEvent);
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("End- Sending OrderPlacedEvent {} to Kafka Topic", orderPlacedEvent);

        } else {
            throw new RuntimeException("Product with Skucode " + orderRequest.skuCode() + "is not in stock");
        }
    }

    private static Order mapToOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkuCode(orderRequest.skuCode());
        return order;
    }
}
