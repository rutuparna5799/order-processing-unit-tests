package com.company.order_processing_engine.exception;

public class InvalidQuantityException extends  RuntimeException{
    public InvalidQuantityException() {
        super("Order Quantity mut be > 0");
    }
}
