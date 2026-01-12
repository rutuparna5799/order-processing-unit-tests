package com.company.order_processing_engine.repository;

import com.company.order_processing_engine.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(String orderId);
    List<Order> findByCustomerId(String customerId);
    Order save(Order order);
}
