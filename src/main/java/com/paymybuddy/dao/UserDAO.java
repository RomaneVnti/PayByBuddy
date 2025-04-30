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
     * @param email l'adresse email à rechercher.
     * @return l'utilisateur correspondant à l'email ou null si aucun utilisateur n'est trouvé.
     */
    public User findByEmail(String email) {
        try {
            return entityManager.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Si aucun utilisateur n'est trouvé, renvoie null
            return null;
        } catch (Exception e) {
            // Gestion d'autres exceptions, log de l'erreur
            System.err.println("Error finding user by email: " + e.getMessage());
            return null;
        }
    }

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur à rechercher.
     * @return l'utilisateur correspondant au nom d'utilisateur ou null si aucun utilisateur n'est trouvé.
     */
    public User findByUsername(String username) {
        try {
            return entityManager.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Si aucun utilisateur n'est trouvé, renvoie null
            return null;
        } catch (Exception e) {
            // Gestion d'autres exceptions, log de l'erreur
            System.err.println("Error finding user by username: " + e.getMessage());
            return null;
        }
    }

    /**
     * Sauvegarde un utilisateur dans la base de données.
     *
     * @param user l'objet utilisateur à persister.
     * @return l'utilisateur persisté avec son identifiant généré.
     * @throws RuntimeException si une erreur survient lors de la persistance.
     */
    public User save(User user) {
        try {
            entityManager.persist(user);
            // Log de succès
            System.out.println("User saved successfully: " + user);
            return user;
        } catch (Exception e) {
            // Gestion des erreurs de persistance
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error saving user", e);
        }
    }

    /**
     * Met à jour un utilisateur existant dans la base de données.
     *
     * @param user l'utilisateur avec les informations mises à jour.
     * @return l'utilisateur mis à jour.
     * @throws RuntimeException si une erreur survient lors de la mise à jour.
     */
    public User update(User user) {
        try {
            // Utilisation de merge() pour mettre à jour l'utilisateur existant
            User updatedUser = entityManager.merge(user);
            // Log de succès
            System.out.println("User updated successfully: " + updatedUser);
            return updatedUser;
        } catch (Exception e) {
            // Gestion des erreurs de mise à jour
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error updating user", e);
        }
    }
}
