package com.paymybuddy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Représente une relation entre deux utilisateurs dans le système de gestion de transferts d'argent.
 * Cette entité contient des informations sur les utilisateurs impliqués dans la relation,
 * ainsi que le statut de la relation et la date de création.
 */
@Entity
@Table(name = "user_relations")
public class UserRelations {

    /**
     * Identifiant unique de la relation entre les utilisateurs.
     * Cet identifiant est généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id")
    private int relationId;

    /**
     * Le premier utilisateur impliqué dans la relation.
     * Ce champ est une clé étrangère vers l'entité `User` représentant le premier utilisateur.
     */
    @ManyToOne
    @JoinColumn(name = "user_id_1", nullable = false)
    private User user1;

    /**
     * Le deuxième utilisateur impliqué dans la relation.
     * Ce champ est une clé étrangère vers l'entité `User` représentant le deuxième utilisateur.
     */
    @ManyToOne
    @JoinColumn(name = "user_id_2", nullable = false)
    private User user2;

    /**
     * Le statut de la relation entre les deux utilisateurs.
     * Le statut est par défaut "ACCEPTEE" et ne peut pas être nul.
     */
    @Column(name = "relationship_status", nullable = false)
    private String relationshipStatus = "ACCEPTEE";

    /**
     * Date et heure de création de la relation.
     * Ce champ a une valeur par défaut correspondant à l'heure actuelle.
     */
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Getters et setters

    /**
     * Retourne l'identifiant de la relation.
     *
     * @return l'identifiant de la relation
     */
    public int getRelationId() {
        return relationId;
    }

    /**
     * Définit l'identifiant de la relation.
     *
     * @param relationId l'identifiant de la relation à définir
     */
    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    /**
     * Retourne le premier utilisateur de la relation.
     *
     * @return le premier utilisateur
     */
    public User getUser1() {
        return user1;
    }

    /**
     * Définit le premier utilisateur de la relation.
     *
     * @param user1 le premier utilisateur à définir
     */
    public void setUser1(User user1) {
        this.user1 = user1;
    }

    /**
     * Retourne le deuxième utilisateur de la relation.
     *
     * @return le deuxième utilisateur
     */
    public User getUser2() {
        return user2;
    }

    /**
     * Définit le deuxième utilisateur de la relation.
     *
     * @param user2 le deuxième utilisateur à définir
     */
    public void setUser2(User user2) {
        this.user2 = user2;
    }

    /**
     * Retourne le statut de la relation.
     *
     * @return le statut de la relation
     */
    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    /**
     * Définit le statut de la relation.
     *
     * @param relationshipStatus le statut de la relation à définir
     */
    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    /**
     * Retourne la date et l'heure de création de la relation.
     *
     * @return la date et l'heure de création de la relation
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Définit la date et l'heure de création de la relation.
     *
     * @param createdAt la date et l'heure de création à définir
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
