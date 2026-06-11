package dev.mayur.ecommerce_backend.core.exception.custom;

public class FailedToDeleteException extends RuntimeException {
    public FailedToDeleteException(String message) {
        super(message);
    }
}
