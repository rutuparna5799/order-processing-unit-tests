package com.company.order_processing_engine.exception;

public class InvalidOrderStateException extends RuntimeException{
    public InvalidOrderStateException() {
        super("Only CREATED orders can be cancelled");
    }
}
