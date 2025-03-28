package com.paymybuddy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        // Créer un objet ErrorResponse avec le code d'erreur et le message
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        // Retourner la réponse avec le statut HTTP 400 (Bad Request) et le message d'erreur
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Autres gestionnaires d'exceptions peuvent être ajoutés ici
}
