package com.techie.microservices.product.dto;

import java.math.BigDecimal;

public record ProductRequest(String name, String description, BigDecimal price) {
    // This record class is used to encapsulate the product request data
    // It automatically generates getters and a constructor
    // No additional methods are needed as it is a simple DTO



}
