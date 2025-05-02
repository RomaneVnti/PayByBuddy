package com.paymybuddy.service;

import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.model.User;
import com.paymybuddy.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.paymybuddy.exception.InvalidLoginException;

/**
 * Service responsable de l'authentification des utilisateurs.
 * Il vérifie les informations d'identification et génère un token JWT en cas de succès.
 */
@Service
public class LoginService {

    /**
     * DAO permettant d'accéder aux données des utilisateurs.
     */
    @Autowired
    private UserDAO userDAO;

    /**
     * Encodeur de mot de passe utilisant l'algorithme BCrypt.
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Fournisseur de jetons JWT pour générer des tokens sécurisés après authentification.
     */
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Authentifie un utilisateur via son email et mot de passe.
     * Si les informations sont valides, un token JWT est généré.
     *
     * @param email    l'adresse email fournie par l'utilisateur
     * @param password le mot de passe fourni par l'utilisateur
     * @return une chaîne représentant le token JWT généré
     * @throws InvalidLoginException si l'email n'existe pas ou si le mot de passe est incorrect
     */
    public String authenticate(String email, String password) {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new InvalidLoginException("Email ou mot de passe invalide");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidLoginException("Email ou mot de passe invalide");
        }

        return jwtTokenProvider.generateToken(user);
    }
}
