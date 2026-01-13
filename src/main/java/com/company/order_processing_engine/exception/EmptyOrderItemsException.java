package com.company.order_processing_engine.exception;

public class EmptyOrderItemsException extends RuntimeException{
    public EmptyOrderItemsException(){
        super("Order must contain at least one item");
    }
}
