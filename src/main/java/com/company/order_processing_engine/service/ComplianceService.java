package com.company.order_processing_engine.service;

import com.company.order_processing_engine.model.Order;

public interface ComplianceService {
    void validate(Order order);
}
