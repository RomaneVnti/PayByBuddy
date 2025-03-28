package com.paymybuddy.dao;

import com.paymybuddy.model.User;
import org.springframework.stereotype.Repository;
import jakarta.persistence.*;

@Repository
public class UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    // Méthode pour vérifier si un utilisateur existe déjà par email
    public User findByEmail(String email) {
        try {
            return entityManager.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null; // Si aucun utilisateur n'est trouvé, retourne null
        }
    }

    // Méthode pour sauvegarder un utilisateur
    public User save(User user) {
        try {
            entityManager.persist(user);
            System.out.println("User saved: " + user);
            return user;
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error saving user", e);
        }
    }
}