package com.company.order_processing_engine.exception;

public class InvalidItemPriceException extends  RuntimeException{
    public InvalidItemPriceException() {
        super("Item Price must be > 0");
    }
}
