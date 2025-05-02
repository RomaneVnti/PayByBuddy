package com.paymybuddy.dao;

import com.paymybuddy.model.UserRelations;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Classe d'accès aux données (DAO) pour l'entité {@link UserRelations}.
 * Gère les opérations de persistance et de recherche des relations entre utilisateurs dans la base de données.
 */
@Repository
public class UserRelationsDAO {

    private final EntityManager entityManager;

    /**
     * Constructeur qui initialise l'EntityManager pour les opérations de persistance.
     *
     * @param entityManager le gestionnaire d'entités JPA.
     */
    public UserRelationsDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Récupère toutes les relations d'un utilisateur en fonction de son identifiant.
     *
     * @param userId l'identifiant de l'utilisateur.
     * @return une liste des relations où l'utilisateur est impliqué (soit en tant que user1, soit en tant que user2).
     */
    public List<UserRelations> getUserRelations(int userId) {
        return entityManager.createQuery(
                        "FROM UserRelations ur WHERE ur.user1.id = :userId OR ur.user2.id = :userId", UserRelations.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * Sauvegarde une nouvelle relation dans la base de données.
     *
     * @param userRelations l'objet {@link UserRelations} à persister.
     * @return la relation persistée.
     * @throws RuntimeException si une erreur survient lors de la persistance.
     */
    public UserRelations save(UserRelations userRelations) {
        try {
            entityManager.persist(userRelations);
            return userRelations;
        } catch (Exception e) {
            throw new RuntimeException("Error saving relation", e);
        }
    }

    /**
     * Recherche une relation entre deux utilisateurs en fonction de leurs identifiants.
     *
     * @param userId1 l'identifiant du premier utilisateur.
     * @param userId2 l'identifiant du second utilisateur.
     * @return la relation entre les deux utilisateurs, ou null si aucune relation n'est trouvée.
     */
    public UserRelations findRelationByIds(int userId1, int userId2) {
        try {
            return entityManager.createQuery(
                            "FROM UserRelations ur WHERE (ur.user1.id = :userId1 AND ur.user2.id = :userId2) OR (ur.user1.id = :userId2 AND ur.user2.id = :userId1)",
                            UserRelations.class)
                    .setParameter("userId1", userId1)
                    .setParameter("userId2", userId2)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
