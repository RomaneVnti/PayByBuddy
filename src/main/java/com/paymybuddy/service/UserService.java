package com.paymybuddy.service;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.dao.UserDAO;
import com.paymybuddy.exception.EmailAlreadyExistsException;
import com.paymybuddy.exception.UserNotFoundException;
import com.paymybuddy.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service gérant les opérations liées aux utilisateurs,
 * telles que la création et la mise à jour des profils.
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
     * Crée un nouvel utilisateur avec un nom, un email et un mot de passe sécurisé (hashé).
     *
     * @param username le nom d'utilisateur
     * @param email l'adresse email unique de l'utilisateur
     * @param password le mot de passe en clair à hasher
     * @return l'utilisateur nouvellement créé
     * @throws EmailAlreadyExistsException si un utilisateur avec cet email existe déjà
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

    /**
     * Met à jour les informations d’un utilisateur existant à partir d’un objet {@link UserDTO}.
     *
     * @param currentUserEmail l’email actuel de l'utilisateur connecté
     * @param userDTO l'objet contenant les nouvelles données (nom, email, mot de passe)
     * @return l'utilisateur mis à jour
     * @throws UserNotFoundException si l'utilisateur avec l'email donné n'existe pas
     * @throws EmailAlreadyExistsException si le nouvel email est déjà utilisé par un autre utilisateur
     */
    @Transactional
    public User updateUser(String currentUserEmail, UserDTO userDTO) {
        User existingUser = userDAO.findByEmail(currentUserEmail);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found.");
        }

        if (!existingUser.getEmail().equals(userDTO.getEmail()) && userDAO.findByEmail(userDTO.getEmail()) != null) {
            throw new EmailAlreadyExistsException("The email is already in use.");
        }

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        return userDAO.save(existingUser);
    }
}
