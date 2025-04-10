package com.paymybuddy.exception;

/**
 * Exception personnalisée lancée lorsqu'une tentative de création d'utilisateur est effectuée
 * avec une adresse email déjà existante dans le système.
 *
 * Cette exception est une RuntimeException, ce qui signifie qu'elle n'a pas besoin
 * d'être explicitement déclarée dans la signature des méthodes qui peuvent la lancer.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Construit une nouvelle exception avec le message d'erreur spécifié.
     *
     * @param message le message détaillant la raison de l'exception
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}