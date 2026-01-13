package com.company.order_processing_engine.exception;

public class MissingCancellationReasonException extends RuntimeException {
    public MissingCancellationReasonException() {
        super("Cancellation reason is required");
    }
}
