package com.paymybuddy.controller;

import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import com.paymybuddy.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * Contrôleur REST qui gère les opérations liées aux utilisateurs.
 * Expose les endpoints pour la création et la gestion des utilisateurs.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * Service gérant la logique métier des utilisateurs.
     */
    @Autowired
    private UserService userService;

    /**
     * Crée un nouvel utilisateur dans le système.
     *
     * @param userDTO l'objet DTO contenant les informations utilisateur validées
     * @return ResponseEntity avec l'utilisateur créé et le code HTTP 201 (CREATED)
     * @throws com.paymybuddy.exception.EmailAlreadyExistsException si l'email existe déjà
     * @throws org.springframework.web.bind.MethodArgumentNotValidException si les données ne sont pas valides
     */
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
        // Appel au service pour créer l'utilisateur avec les données validées
        User createdUser = userService.createUser(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword()
        );

        // Retourne l'utilisateur créé avec le statut HTTP 201 (Created)
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}