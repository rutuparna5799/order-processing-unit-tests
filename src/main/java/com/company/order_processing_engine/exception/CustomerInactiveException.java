package com.company.order_processing_engine.exception;

public class CustomerInactiveException extends RuntimeException {
    public CustomerInactiveException() {
        super("Customer is not active");
    }
}
