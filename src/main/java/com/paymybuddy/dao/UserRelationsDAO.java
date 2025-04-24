package com.paymybuddy.dao;

import com.paymybuddy.model.UserRelations;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRelationsDAO {

    private final EntityManager entityManager;

    public UserRelationsDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Récupérer les relations d'un utilisateur (user_id_1 ou user_id_2)
    public List<UserRelations> getUserRelations(int userId) {
        return entityManager.createQuery("FROM UserRelations ur WHERE ur.user1.id = :userId OR ur.user2.id = :userId", UserRelations.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    // Sauvegarder une relation dans la base de données

    public UserRelations save(UserRelations userRelations) {
        try {
            entityManager.persist(userRelations);
            return userRelations;
        } catch (Exception e) {
            throw new RuntimeException("Error saving relation", e);
        }
    }

    // Trouver une relation entre deux utilisateurs en fonction de leurs identifiants
    public UserRelations findRelationByIds(int userId1, int userId2) {
        try {
            return entityManager.createQuery(
                            "FROM UserRelations ur WHERE (ur.user1.id = :userId1 AND ur.user2.id = :userId2) OR (ur.user1.id = :userId2 AND ur.user2.id = :userId1)",
                            UserRelations.class)
                    .setParameter("userId1", userId1)
                    .setParameter("userId2", userId2)
                    .getSingleResult();
        } catch (Exception e) {
            return null; // Si aucune relation n'est trouvée
        }
    }
}
