package com.paymybuddy.dao;

import com.paymybuddy.model.User;
import org.springframework.stereotype.Repository;
import jakarta.persistence.*;

/**
 * Classe d'accès aux données (DAO) pour l'entité User.
 */
@Repository
public class UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Recherche un utilisateur par son adresse email.
     */
    public User findByEmail(String email) {
        try {
            return entityManager.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            return null;
        }
    }

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     */
    public User findByUsername(String username) {
        try {
            return entityManager.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.err.println("Error finding user by username: " + e.getMessage());
            return null;
        }
    }

    /**
     * Sauvegarde un utilisateur dans la base de données.
     */
    public User save(User user) {
        try {
            entityManager.persist(user);
            return user;
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error saving user", e);
        }
    }

    /**
     * Met à jour un utilisateur existant dans la base de données.
     */
    public User update(User user) {
        try {
            return entityManager.merge(user);
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error updating user", e);
        }
    }
}
