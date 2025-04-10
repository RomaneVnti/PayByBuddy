package com.paymybuddy.exception;

/**
 * Exception personnalisée lancée lorsqu'il y a un échec de login (mauvais email ou mot de passe).
 */
public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String message) {
        super(message);
    }
}
