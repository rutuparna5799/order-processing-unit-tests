package com.company.order_processing_engine.service;

import com.company.order_processing_engine.model.Order;

public class TestOrder {
    public static void main(String[] args) {
        Order o = new Order();
        System.out.println(o.getCustomer()); // should compile
    }
}
