package com.company.order_processing_engine.service;

import com.company.order_processing_engine.model.Order;

public class OrderServiceImpl {
    public Order createOrder(Order order) {
        if (!order.getCustomer().isActive()) {
            throw new RuntimeException("Customer is not active");
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
