package com.company.order_processing_engine.exception;

public class CustomerIdRequiredException  extends RuntimeException {
    public CustomerIdRequiredException() {
        super("Customer ID is required");
    }
}
