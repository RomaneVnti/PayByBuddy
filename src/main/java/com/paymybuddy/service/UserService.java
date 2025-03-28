package com.paymybuddy.service;

import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.exception.EmailAlreadyExistsException;
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

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public User createUser(String username, String email, String password) {
        // Vérifie si l'email existe déjà
        User existingUser = userDAO.findByEmail(email);
        if (existingUser != null) {
            throw new EmailAlreadyExistsException("An account with this email already exists.");
        }

        // Hash du mot de passe
        String hashedPassword = passwordEncoder.encode(password);

        // Crée un nouvel utilisateur
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        newUser.setCreatedAt(LocalDateTime.now());

        return userDAO.save(newUser);
    }
}
