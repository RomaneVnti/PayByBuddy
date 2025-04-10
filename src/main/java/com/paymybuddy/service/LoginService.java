package com.paymybuddy.service;

import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.model.User;
import com.paymybuddy.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.paymybuddy.exception.InvalidLoginException;

/**
 * Service pour gérer l'authentification des utilisateurs.
 */
@Service
public class LoginService {

    @Autowired
    private UserDAO userDAO;  // DAO pour accéder aux données utilisateur

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // Pour vérifier les mots de passe

    @Autowired
    private JwtTokenProvider jwtTokenProvider;  // Fournisseur de token JWT

    /**
     * Authentifie un utilisateur en vérifiant son email et son mot de passe.
     * Si les informations sont valides, génère un token JWT.
     *
     * @param email    L'email de l'utilisateur
     * @param password Le mot de passe de l'utilisateur
     * @return Le token JWT généré
     * @throws IllegalArgumentException Si l'email ou le mot de passe est incorrect
     */
    public String authenticate(String email, String password) {
        // Recherche l'utilisateur dans la base de données
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new InvalidLoginException("Invalid email or password");
        }

        // Vérifie le mot de passe avec BCrypt
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidLoginException("Invalid email or password");
        }

        // Génère et retourne un token JWT si l'utilisateur est authentifié
        return jwtTokenProvider.generateToken(user);
    }
}
