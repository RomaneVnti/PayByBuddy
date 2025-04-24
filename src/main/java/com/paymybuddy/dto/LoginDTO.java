package com.paymybuddy.dto;

/**
 * DTO (Data Transfer Object) utilisé pour la connexion d'un utilisateur.
 * Contient les informations nécessaires pour authentifier un utilisateur : email et mot de passe.
 */
public class LoginDTO {

    private String email;
    private String password;

    /**
     * Constructeur par défaut pour LoginDTO.
     * Utilisé pour créer un objet LoginDTO vide.
     */
    public LoginDTO() {}

    /**
     * Constructeur pour initialiser LoginDTO avec un email et un mot de passe.
     *
     * @param email l'adresse email de l'utilisateur
     * @param password le mot de passe de l'utilisateur
     */
    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Récupère l'adresse email de l'utilisateur.
     *
     * @return l'email de l'utilisateur
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse email de l'utilisateur.
     *
     * @param email l'email à attribuer à l'utilisateur
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Récupère le mot de passe de l'utilisateur.
     *
     * @return le mot de passe de l'utilisateur
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     *
     * @param password le mot de passe à attribuer à l'utilisateur
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
