package com.paymybuddy.service;

import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    // Utilisation de BCrypt pour hasher le mot de passe
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Méthode pour créer un utilisateur
    @Transactional
    public User createUser(String username, String email, String password) {
        // Vérifie si l'email existe déjà
        User existingUser = userDAO.findByEmail(email);
        if (existingUser != null) {
            throw new RuntimeException("Email already in use");
        }

        // Hash du mot de passe
        String hashedPassword = passwordEncoder.encode(password);

        // Crée un nouvel utilisateur
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        newUser.setCreatedAt(LocalDateTime.now());

        // Log de création
        System.out.println("Creating user with email: " + email);
        System.out.println("Saving user to database: " + newUser);

        // Enregistre l'utilisateur dans la base de données
        return userDAO.save(newUser);
    }
}
