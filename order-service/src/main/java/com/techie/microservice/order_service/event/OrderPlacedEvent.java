package com.techie.microservice.order_service.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderPlacedEvent {
    private String orderNumber;
    private String email;
}
