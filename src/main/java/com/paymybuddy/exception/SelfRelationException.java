package com.paymybuddy.exception;

/**
 * Exception lancée lorsqu'un utilisateur tente d'ajouter sa propre adresse email en relation.
 */
public class SelfRelationException extends RuntimeException {
    public SelfRelationException(String message) {
        super(message);
    }
}
