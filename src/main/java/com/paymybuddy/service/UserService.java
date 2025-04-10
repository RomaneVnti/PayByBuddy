package com.paymybuddy.service;

import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.exception.EmailAlreadyExistsException;
import com.paymybuddy.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service gérant les opérations liées aux utilisateurs.
 */
@Service
public class UserService {

    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructeur avec injection de dépendances.
     *
     * @param userDAO DAO permettant d'accéder aux données des utilisateurs
     */
    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Crée un nouvel utilisateur avec son nom d'utilisateur, son email et son mot de passe.
     * Le mot de passe est hashé avant d'être enregistré.
     *
     * @param username Nom d'utilisateur
     * @param email    Adresse email
     * @param password Mot de passe (non hashé)
     * @return L'utilisateur nouvellement créé
     * @throws EmailAlreadyExistsException si un utilisateur avec le même email existe déjà
     */
    @Transactional
    public User createUser(String username, String email, String password) {
        if (userDAO.findByEmail(email) != null) {
            throw new EmailAlreadyExistsException("An account with this email already exists.");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        newUser.setCreatedAt(LocalDateTime.now());

        return userDAO.save(newUser);
    }
}
