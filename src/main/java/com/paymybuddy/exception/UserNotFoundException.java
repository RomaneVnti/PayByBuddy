package com.paymybuddy.exception;

/**
 * Exception personnalisée levée lorsqu'un utilisateur n'est pas trouvé
 * dans la base de données lors d'une opération (ex : recherche, connexion, relation, etc.).
 *
 * Cette exception est utilisée pour signaler l'absence d'un utilisateur correspondant
 * aux critères fournis (email, identifiant, etc.).
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Construit une nouvelle exception UserNotFoundException avec un message explicite.
     *
     * @param message le message décrivant la cause de l'exception (ex : "Utilisateur introuvable avec cet email")
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
