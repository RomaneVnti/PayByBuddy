package com.paymybuddy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Représente une transaction entre deux utilisateurs dans le système de gestion de transferts d'argent.
 * Cette entité contient des informations sur l'expéditeur, le destinataire, la description de la transaction,
 * le montant, et la date de création.
 */
@Entity
@Table(name = "transactions")
public class Transactions {

    /**
     * Identifiant unique de la transaction.
     * Cet identifiant est généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;

    /**
     * L'utilisateur qui envoie l'argent.
     * Ce champ est une clé étrangère vers l'entité `User` représentant l'expéditeur.
     */
    @ManyToOne
    @JoinColumn(name = "user_id_sender", nullable = false)
    private User sender;

    /**
     * L'utilisateur qui reçoit l'argent.
     * Ce champ est une clé étrangère vers l'entité `User` représentant le destinataire.
     */
    @ManyToOne
    @JoinColumn(name = "user_id_receiver", nullable = false)
    private User receiver;

    /**
     * Description de la transaction, pouvant être utilisée pour spécifier l'objet de la transaction.
     */
    @Column(name = "description")
    private String description;

    /**
     * Montant de la transaction.
     * Ce champ ne peut pas être nul.
     */
    @Column(name = "amount", nullable = false)
    private double amount;

    /**
     * Date et heure de la création de la transaction.
     * Ce champ a une valeur par défaut correspondant à l'heure actuelle.
     */
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Getters et setters

    /**
     * Retourne l'identifiant de la transaction.
     *
     * @return l'identifiant de la transaction
     */
    public int getTransactionId() {
        return transactionId;
    }

    /**
     * Définit l'identifiant de la transaction.
     *
     * @param transactionId l'identifiant de la transaction à définir
     */
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Retourne l'utilisateur expéditeur de la transaction.
     *
     * @return l'utilisateur expéditeur
     */
    public User getSender() {
        return sender;
    }

    /**
     * Définit l'utilisateur expéditeur de la transaction.
     *
     * @param sender l'utilisateur expéditeur à définir
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * Retourne l'utilisateur destinataire de la transaction.
     *
     * @return l'utilisateur destinataire
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * Définit l'utilisateur destinataire de la transaction.
     *
     * @param receiver l'utilisateur destinataire à définir
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /**
     * Retourne la description de la transaction.
     *
     * @return la description de la transaction
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit la description de la transaction.
     *
     * @param description la description de la transaction à définir
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retourne le montant de la transaction.
     *
     * @return le montant de la transaction
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Définit le montant de la transaction.
     *
     * @param amount le montant de la transaction à définir
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Retourne la date et l'heure de création de la transaction.
     *
     * @return la date et l'heure de création
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Définit la date et l'heure de création de la transaction.
     *
     * @param createdAt la date et l'heure de création à définir
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
