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
     * Ne peut pas être vide ou nul.
     */
    @NotBlank(message = "Le nom d'utilisateur est requis")
    private String username;

    /**
     * Adresse email de l'utilisateur.
     * Doit être une adresse email valide et ne peut pas être vide ou nulle.
     */
    @NotBlank(message = "L'email est requis")
    @Email(message = "Format de l'email invalide")
    private String email;

    /**
     * Mot de passe de l'utilisateur.
     * Doit contenir au moins 8 caractères et ne pas contenir d'espaces.
     */
    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(
            regexp = "^[^\\s]+$",
            message = "Le mot de passe n'est pas valide : il ne doit pas contenir d'espaces"
    )
    private String password;

    /**
     * Constructeur de UserDTO pour initialiser les valeurs de l'objet avec les informations de l'utilisateur.
     *
     * @param username le nom d'utilisateur
     * @param email l'adresse email de l'utilisateur
     * @param password le mot de passe de l'utilisateur
     */
    public UserDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

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
