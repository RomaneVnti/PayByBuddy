package com.paymybuddy.exception;

/**
 * Exception lancée lorsqu'une adresse email de relation n'existe pas dans le système.
 */
public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
