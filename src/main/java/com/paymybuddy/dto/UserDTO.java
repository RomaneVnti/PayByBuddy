package com.paymybuddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Objet de transfert de données (DTO) pour les utilisateurs.
 * Cette classe permet de transporter et valider les données utilisateur lors des opérations de création et de mise à jour.
 */
public class UserDTO {

    /**
     * Nom d'utilisateur.
     * Ne peut pas être vide ou null.
     */
    @NotBlank(message = "Username is required")
    private String username;

    /**
     * Adresse email de l'utilisateur.
     * Doit être une adresse email valide et ne peut pas être vide ou null.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * Mot de passe de l'utilisateur.
     * Doit contenir au moins 8 caractères et ne pas contenir d'espaces.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^[^\\s]+$",
            message = "Password is not valid: it must not contain spaces"
    )
    private String password;

    /**
     * Retourne le nom d'utilisateur.
     *
     * @return le nom d'utilisateur
     */
    public String getUsername() {
        return username;
    }

    /**
     * Définit le nom d'utilisateur.
     *
     * @param username le nom d'utilisateur à définir
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retourne l'adresse email de l'utilisateur.
     *
     * @return l'adresse email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse email de l'utilisateur.
     *
     * @param email l'adresse email à définir
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne le mot de passe de l'utilisateur.
     *
     * @return le mot de passe
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     *
     * @param password le mot de passe à définir
     */
    public void setPassword(String password) {
        this.password = password;
    }
}