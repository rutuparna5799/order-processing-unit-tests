package com.company.order_processing_engine.exception;

public class OrderAmountLimitExceededException extends RuntimeException{
    public OrderAmountLimitExceededException() {
        super("Total amount must not exceed 500000");
    }
}
