package com.paymybuddy.dao;

import com.paymybuddy.model.User;
import org.springframework.stereotype.Repository;
import jakarta.persistence.*;

/**
 * Classe d'accès aux données (DAO) pour l'entité User.
 * Gère les opérations de persistance et de recherche des utilisateurs dans la base de données.
 */
@Repository
public class UserDAO {

    /**
     * Gestionnaire d'entités JPA pour effectuer les opérations de persistance.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Recherche un utilisateur par son adresse email.
     *
     * @param email l'adresse email à rechercher
     * @return l'utilisateur correspondant à l'email ou null si aucun utilisateur n'est trouvé
     */
    public User findByEmail(String email) {
        try {
            // Exécute une requête JPQL pour trouver l'utilisateur par email
            return entityManager.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Si aucun utilisateur n'est trouvé, retourne null
            return null;
        } catch (Exception e) {
            // Gestion des autres exceptions potentielles
            System.err.println("Error finding user by email: " + e.getMessage());
            return null;
        }
    }

    /**
     * Sauvegarde un utilisateur dans la base de données.
     *
     * @param user l'objet utilisateur à persister
     * @return l'utilisateur persisté avec son identifiant généré
     * @throws RuntimeException si une erreur survient lors de la persistance
     */
    public User save(User user) {
        try {
            // Persiste l'utilisateur dans la base de données
            entityManager.persist(user);

            // Log de confirmation
            System.out.println("User saved successfully: " + user);

            return user;
        } catch (Exception e) {
            // Gestion et log des erreurs
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error saving user", e);
        }
    }
}