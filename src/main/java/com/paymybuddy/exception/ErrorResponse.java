package com.paymybuddy.exception;

/**
 * Classe représentant une réponse d'erreur standardisée.
 * Utilisée pour retourner des informations d'erreur structurées aux clients,
 * notamment dans les cas d'exceptions et d'erreurs de validation.
 */
public class ErrorResponse {

    /**
     * Le code de statut HTTP associé à cette erreur.
     * Par exemple, 400 pour les requêtes incorrectes, 404 pour les ressources non trouvées.
     */
    private int status;

    /**
     * Message décrivant l'erreur, destiné à être affiché à l'utilisateur
     * ou utilisé par le client pour comprendre la nature de l'erreur.
     */
    private String message;

    /**
     * Constructeur pour créer une nouvelle instance d'ErrorResponse.
     *
     * @param status le code de statut HTTP associé à cette erreur
     * @param message le message détaillant la nature de l'erreur
     */
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Retourne le code de statut HTTP associé à cette erreur.
     *
     * @return le code de statut HTTP
     */
    public int getStatus() {
        return status;
    }

    /**
     * Définit le code de statut HTTP associé à cette erreur.
     *
     * @param status le code de statut HTTP à définir
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Retourne le message d'erreur.
     *
     * @return le message décrivant l'erreur
     */
    public String getMessage() {
        return message;
    }

    /**
     * Définit le message d'erreur.
     *
     * @param message le message à définir
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
