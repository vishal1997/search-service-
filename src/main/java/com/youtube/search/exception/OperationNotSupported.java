package com.youtube.search.exception;

public class OperationNotSupported extends Exception {

    public OperationNotSupported(String message) {
        super(message);
    }

    public OperationNotSupported(String message, Throwable cause) {
        super(message, cause);
    }
}
