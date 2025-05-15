package com.paymybuddy.exception;

public class SoldeInvalidException extends RuntimeException {

    public SoldeInvalidException(String message) {
        super(message);
    }

    public SoldeInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
