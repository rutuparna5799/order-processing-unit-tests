package com.company.order_processing_engine.service;

import com.company.order_processing_engine.exception.OrderAmountLimitExceededException;
import com.company.order_processing_engine.model.Order;

public class ComplianceServiceImpl implements ComplianceService{

    @Override
    public void validate(Order order) {
        double total = order.getItems()
                .stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        if (total > 500000) {
            throw new OrderAmountLimitExceededException();
        }
    }
}
