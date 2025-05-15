package com.paymybuddy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


/**
 * Représente un utilisateur dans le système de gestion de transferts d'argent.
 * Cette entité contient des informations personnelles de l'utilisateur, telles que son nom d'utilisateur,
 * son email, son mot de passe, ainsi que la date de création de son compte.
 */
@Entity

@Table(name = "user")
public class User {

    /**
     * Identifiant unique de l'utilisateur.
     * Cet identifiant est généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    /**
     * Nom d'utilisateur de l'utilisateur.
     * Ce champ ne peut pas être nul.
     */
    @Column(name = "username", nullable = false)
    private String username;

    /**
     * Adresse email de l'utilisateur.
     * Ce champ ne peut pas être nul et doit être unique dans la base de données.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Mot de passe de l'utilisateur.
     * Ce champ ne peut pas être nul.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Date et heure de création du compte utilisateur.
     * Ce champ a une valeur par défaut correspondant à l'heure actuelle.
     */
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "solde", nullable = false)
    private double solde = 100.0;
    // Getters et setters

    /**
     * Retourne l'identifiant de l'utilisateur.
     *
     * @return l'identifiant de l'utilisateur
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Définit l'identifiant de l'utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur à définir
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Retourne le nom d'utilisateur de l'utilisateur.
     *
     * @return le nom d'utilisateur
     */
    public String getUsername() {
        return username;
    }

    /**
     * Définit le nom d'utilisateur de l'utilisateur.
     *
     * @param username le nom d'utilisateur à définir
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retourne l'adresse email de l'utilisateur.
     *
     * @return l'adresse email de l'utilisateur
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse email de l'utilisateur.
     *
     * @param email l'adresse email de l'utilisateur à définir
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne le mot de passe de l'utilisateur.
     *
     * @return le mot de passe de l'utilisateur
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     *
     * @param password le mot de passe de l'utilisateur à définir
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retourne la date et l'heure de création du compte utilisateur.
     *
     * @return la date et l'heure de création du compte utilisateur
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Définit la date et l'heure de création du compte utilisateur.
     *
     * @param createdAt la date et l'heure de création à définir
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }
}
