package com.paymybuddy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Gestionnaire global d'exceptions pour l'application.
 * Cette classe intercepte les exceptions spécifiques lancées dans l'application
 * et les transforme en réponses HTTP appropriées avec des messages d'erreur structurés.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les exceptions de type EmailAlreadyExistsException.
     * Cette méthode est appelée lorsqu'une tentative de création d'utilisateur
     * est faite avec une adresse email déjà existante.
     *
     * @param ex l'exception capturée
     * @return une ResponseEntity contenant un objet ErrorResponse et un statut HTTP 400 (Bad Request)
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        // Créer un objet ErrorResponse avec le code d'erreur et le message
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        // Retourner la réponse avec le statut HTTP 400 (Bad Request) et le message d'erreur
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les exceptions de validation des arguments de méthode.
     * Cette méthode est appelée lorsque les validations d'entrée (@Valid) échouent,
     * par exemple quand un mot de passe contient des espaces ou qu'un email est mal formaté.
     *
     * @param ex l'exception de validation capturée
     * @return une ResponseEntity contenant un objet ErrorResponse avec le premier message d'erreur
     *         et un statut HTTP 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Récupérer la première erreur de validation depuis les résultats de la validation
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)  // Extrait le message d'erreur personnalisé
                .findFirst()                          // Prend le premier message d'erreur
                .orElse("Validation error");          // Message par défaut si aucun message spécifique n'est trouvé

        // Créer la réponse d'erreur avec le statut 400 et le message
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);

        // Retourner la réponse formatée
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}